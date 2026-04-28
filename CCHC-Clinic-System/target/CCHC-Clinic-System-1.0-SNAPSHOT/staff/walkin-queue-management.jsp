<%-- 
    Document   : walkin-queue-management
    Created on : 2026年4月28日, 上午11:49:50
    Author     : User
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="staff" uri="/WEB-INF/tlds/staff-taglib.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp" />

<div class="row">
    <div class="col-12 mb-4">
        <h2>Walk-in Queue Management</h2>
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/staff/dashboard">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Walk-in Queue</li>
            </ol>
        </nav>
    </div>

    <!-- Success/Error Alerts -->
    <c:if test="${param.success == 1}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            Queue action completed successfully!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${param.error == 1}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            Queue action failed! Please try again.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Call Next Patient Card -->
    <div class="col-12 mb-4">
        <div class="card border-success">
            <div class="card-header bg-success text-white">
                <h5 class="card-title mb-0">Queue Control</h5>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/staffServlet/queueManagement" method="post">
                    <div class="row align-items-end">
                        <div class="col-md-4">
                            <label for="serviceId" class="form-label">Select Service</label>
                            <select class="form-select" id="serviceId" name="serviceId" required>
                                <option value="">-- Select Service --</option>
                                <c:forEach var="service" items="${allServices}">
                                    <option value="${service.serviceId}">${service.serviceName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <input type="hidden" name="action" value="CALL_NEXT">
                            <button type="submit" class="btn btn-success w-100 btn-lg">
                                Call Next Patient
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Current Called Patient Card -->
    <div class="col-12 mb-4">
        <div class="card border-primary">
            <div class="card-header bg-primary text-white">
                <h5 class="card-title mb-0">Currently Called Patient</h5>
            </div>
            <div class="card-body text-center">
                <c:set var="currentCalled" value="${null}" />
                <c:forEach var="queue" items="${walkInQueue}">
                    <c:if test="${queue.status == 'CALLED'}">
                        <c:set var="currentCalled" value="${queue}" />
                    </c:if>
                </c:forEach>

                <c:if test="${currentCalled != null}">
                    <h1 class="display-1 fw-bold text-primary">${currentCalled.queueNumber}</h1>
                    <h3>Patient Name: ${currentCalled.patientName}</h3>
                    <p>Service: ${currentCalled.serviceName} | Called Time: <fmt:formatDate value="${currentCalled.calledTime}" pattern="HH:mm" /></p>
                    
                    <form action="${pageContext.request.contextPath}/staffServlet/queueManagement" method="post" class="d-inline">
                        <input type="hidden" name="queueId" value="${currentCalled.queueId}">
                        <input type="hidden" name="serviceId" value="${currentCalled.serviceId}">
                        <input type="hidden" name="action" value="COMPLETED">
                        <button type="submit" class="btn btn-success btn-lg me-2">Mark as Completed</button>
                    </form>
                    <form action="${pageContext.request.contextPath}/staffServlet/queueManagement" method="post" class="d-inline">
                        <input type="hidden" name="queueId" value="${currentCalled.queueId}">
                        <input type="hidden" name="serviceId" value="${currentCalled.serviceId}">
                        <input type="hidden" name="action" value="SKIP">
                        <button type="submit" class="btn btn-warning btn-lg me-2">Skip Patient</button>
                    </form>
                    <form action="${pageContext.request.contextPath}/staffServlet/queueManagement" method="post" class="d-inline">
                        <input type="hidden" name="queueId" value="${currentCalled.queueId}">
                        <input type="hidden" name="serviceId" value="${currentCalled.serviceId}">
                        <input type="hidden" name="action" value="EXPIRED">
                        <button type="submit" class="btn btn-danger btn-lg">Mark as Expired</button>
                    </form>
                </c:if>
                <c:if test="${currentCalled == null}">
                    <h3 class="text-muted">No patient currently called</h3>
                    <p>Use the "Call Next Patient" button to call the next waiting patient</p>
                </c:if>
            </div>
        </div>
    </div>

    <!-- Full Queue List -->
    <div class="col-12">
        <div class="card">
            <div class="card-header">
                <h5 class="card-title mb-0">Full Walk-in Queue</h5>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th>Queue Number</th>
                                <th>Patient Name</th>
                                <th>Service</th>
                                <th>Status</th>
                                <th>Est. Wait Time</th>
                                <th>Called Time</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="queue" items="${walkInQueue}">
                                <tr>
                                    <td>${queue.queueNumber}</td>
                                    <td>${queue.patientName}</td>
                                    <td>${queue.serviceName}</td>
                                    <td><staff:queueStatus status="${queue.status}" /></td>
                                    <td>${queue.estimatedWaitMinutes} mins</td>
                                    <td>
                                        <c:if test="${queue.calledTime != null}">
                                            <fmt:formatDate value="${queue.calledTime}" pattern="HH:mm" />
                                        </c:if>
                                        <c:if test="${queue.calledTime == null}">
                                            <span class="text-muted">-</span>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty walkInQueue}">
                                <tr>
                                    <td colspan="6" class="text-center text-muted">No patients in the walk-in queue</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

    </div>
    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
