<%-- 
    Document   : appointment-list
    Created on : 2026年4月28日, 上午11:49:30
    Author     : User
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="staff" uri="/WEB-INF/tlds/staff-taglib.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="../common/header.jsp" />

<div class="row">
    <div class="col-12 mb-4">
        <h2>Appointment Management</h2>
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/staff/dashboard">Dashboard</a></li>
                <li class="breadcrumb-item active" aria-current="page">Appointments</li>
            </ol>
        </nav>
    </div>

    <!-- Success/Error Alerts -->
    <c:if test="${param.success == 1}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            Appointment approval processed successfully!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${param.success == 2}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            Visit outcome recorded successfully!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${param.error == 1 || param.error == 2}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            Operation failed! Please try again.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Pending Approval Tab -->
    <div class="col-12 mb-5">
        <div class="card border-warning">
            <div class="card-header bg-warning text-dark">
                <h5 class="card-title mb-0">Pending Approval Appointments</h5>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Appt ID</th>
                                <th>Patient Name</th>
                                <th>Date</th>
                                <th>Time</th>
                                <th>Service</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="appt" items="${pendingAppointments}">
                                <tr>
                                    <td>${appt.appointmentId}</td>
                                    <td>${appt.patientName}</td>
                                    <td><fmt:formatDate value="${appt.appointmentDate}" pattern="yyyy-MM-dd" /></td>
                                    <td><fmt:formatDate value="${appt.appointmentTime}" pattern="HH:mm" /></td>
                                    <td>${appt.serviceName}</td>
                                    <td>
                                        <!-- Approve/Reject Modal Trigger -->
                                        <button type="button" class="btn btn-sm btn-success" data-bs-toggle="modal" data-bs-target="#approveModal${appt.appointmentId}">
                                            Approve
                                        </button>
                                        <button type="button" class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#rejectModal${appt.appointmentId}">
                                            Reject
                                        </button>

                                        <!-- Approve Modal -->
                                        <div class="modal fade" id="approveModal${appt.appointmentId}" tabindex="-1">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <form action="${pageContext.request.contextPath}/staffServlet/appointmentApproval" method="post">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title">Approve Appointment #${appt.appointmentId}</h5>
                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <p>Are you sure you want to approve this appointment for <strong>${appt.patientName}</strong>?</p>
                                                            <div class="mb-3">
                                                                <label for="approvalNote" class="form-label">Approval Note (Optional)</label>
                                                                <textarea class="form-control" id="approvalNote" name="approvalNote" rows="2"></textarea>
                                                            </div>
                                                            <input type="hidden" name="appointmentId" value="${appt.appointmentId}">
                                                            <input type="hidden" name="action" value="APPROVE">
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                            <button type="submit" class="btn btn-success">Confirm Approve</button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- Reject Modal -->
                                        <div class="modal fade" id="rejectModal${appt.appointmentId}" tabindex="-1">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <form action="${pageContext.request.contextPath}/staffServlet/appointmentApproval" method="post">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title">Reject Appointment #${appt.appointmentId}</h5>
                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <p>Are you sure you want to reject this appointment for <strong>${appt.patientName}</strong>?</p>
                                                            <div class="mb-3">
                                                                <label for="rejectionNote" class="form-label">Rejection Reason (Required)</label>
                                                                <textarea class="form-control" id="rejectionNote" name="approvalNote" rows="2" required></textarea>
                                                            </div>
                                                            <input type="hidden" name="appointmentId" value="${appt.appointmentId}">
                                                            <input type="hidden" name="action" value="REJECT">
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                            <button type="submit" class="btn btn-danger">Confirm Reject</button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty pendingAppointments}">
                                <tr>
                                    <td colspan="6" class="text-center text-muted">No pending approval appointments</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- All Appointments Table -->
    <div class="col-12">
        <div class="card">
            <div class="card-header">
                <h5 class="card-title mb-0">All Appointments</h5>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th>Date</th>
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
                                    <td><fmt:formatDate value="${appt.appointmentDate}" pattern="yyyy-MM-dd" /></td>
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
