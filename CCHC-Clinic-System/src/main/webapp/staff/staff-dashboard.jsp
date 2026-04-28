<%-- 
    Document   : staff-dashboard
    Created on : 2026年4月28日, 上午11:48:54
    Author     : User
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="staff" uri="/WEB-INF/tlds/staff-taglib.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Include common header -->
<jsp:include page="../common/header.jsp" />

<!-- JSP Action to use currentUser bean -->
<jsp:useBean id="currentUser" scope="session" class="ict.bean.User" />

<div class="row">
    <!-- Dashboard Header -->
    <div class="col-12 mb-4">
        <h2>Clinic Dashboard - <jsp:getProperty name="currentUser" property="fullName" /></h2>
        <p class="text-muted">Clinic: <jsp:getProperty name="currentUser" property="assignedClinicId" /> | Date: <fmt:formatDate value="${currentDate}" pattern="yyyy-MM-dd" /></p>
    </div>

    <!-- Success/Error Alerts -->
    <c:if test="${param.success == 1}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            Operation completed successfully!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${param.error == 1}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            Operation failed! Please try again.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Pending Approvals Card -->
    <div class="col-md-6 col-lg-4 mb-4">
        <div class="card h-100 border-warning">
            <div class="card-header bg-warning text-dark">
                <h5 class="card-title mb-0">Pending Approvals</h5>
            </div>
            <div class="card-body">
                <h3 class="text-center">${fn:length(pendingAppointments)}</h3>
                <p class="text-center">Appointments waiting for your approval</p>
                <a href="${pageContext.request.contextPath}/staff/appointment-list.jsp" class="btn btn-warning w-100">View All</a>
            </div>
        </div>
    </div>

    <!-- Daily Appointments Card -->
    <div class="col-md-6 col-lg-4 mb-4">
        <div class="card h-100 border-primary">
            <div class="card-header bg-primary text-white">
                <h5 class="card-title mb-0">Today's Appointments</h5>
            </div>
            <div class="card-body">
                <h3 class="text-center">${fn:length(dailyAppointments)}</h3>
                <p class="text-center">Total appointments for today</p>
                <a href="${pageContext.request.contextPath}/staff/appointment-list.jsp" class="btn btn-primary w-100">Manage Appointments</a>
            </div>
        </div>
    </div>

    <!-- Active Walk-in Queue Card -->
    <div class="col-md-6 col-lg-4 mb-4">
        <div class="card h-100 border-info">
            <div class="card-header bg-info text-dark">
                <h5 class="card-title mb-0">Active Walk-in Queue</h5>
            </div>
            <div class="card-body">
                <h3 class="text-center">${fn:length(walkInQueue)}</h3>
                <p class="text-center">Patients in the walk-in queue</p>
                <a href="${pageContext.request.contextPath}/staff/walkin-queue-management.jsp" class="btn btn-info w-100">Manage Queue</a>
            </div>
        </div>
    </div>

    <!-- Today's Appointments Table -->
    <div class="col-12 mb-4">
        <div class="card">
            <div class="card-header">
                <h5 class="card-title mb-0">Today's Appointments</h5>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th>Time</th>
                                <th>Patient Name</th>
                                <th>Service</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="appt" items="${dailyAppointments}">
                                <tr>
                                    <td><fmt:formatDate value="${appt.appointmentTime}" pattern="HH:mm" /></td>
                                    <td>${appt.patientName}</td>
                                    <td>${appt.serviceName}</td>
                                    <td><staff:appointmentStatus status="${appt.status}" /></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/staff/record-visit-outcome.jsp?appointmentId=${appt.appointmentId}" 
                                           class="btn btn-sm btn-outline-primary">Record Outcome</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty dailyAppointments}">
                                <tr>
                                    <td colspan="5" class="text-center text-muted">No appointments for today</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Active Walk-in Queue Table -->
    <div class="col-12">
        <div class="card">
            <div class="card-header">
                <h5 class="card-title mb-0">Active Walk-in Queue</h5>
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
                                <th>Actions</th>
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
                                        <c:if test="${queue.status == 'WAITING'}">
                                            <form action="${pageContext.request.contextPath}/staffServlet/queueManagement" method="post" class="d-inline">
                                                <input type="hidden" name="queueId" value="${queue.queueId}">
                                                <input type="hidden" name="serviceId" value="${queue.serviceId}">
                                                <input type="hidden" name="action" value="CALL_NEXT">
                                                <button type="submit" class="btn btn-sm btn-success">Call</button>
                                            </form>
                                        </c:if>
                                        <c:if test="${queue.status == 'CALLED'}">
                                            <form action="${pageContext.request.contextPath}/staffServlet/queueManagement" method="post" class="d-inline">
                                                <input type="hidden" name="queueId" value="${queue.queueId}">
                                                <input type="hidden" name="serviceId" value="${queue.serviceId}">
                                                <input type="hidden" name="action" value="COMPLETED">
                                                <button type="submit" class="btn btn-sm btn-success">Complete</button>
                                            </form>
                                            <form action="${pageContext.request.contextPath}/staffServlet/queueManagement" method="post" class="d-inline">
                                                <input type="hidden" name="queueId" value="${queue.queueId}">
                                                <input type="hidden" name="serviceId" value="${queue.serviceId}">
                                                <input type="hidden" name="action" value="SKIP">
                                                <button type="submit" class="btn btn-sm btn-warning">Skip</button>
                                            </form>
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
