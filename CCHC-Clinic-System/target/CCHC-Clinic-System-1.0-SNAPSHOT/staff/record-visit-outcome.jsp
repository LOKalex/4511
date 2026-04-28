<%-- 
    Document   : record-visit-outcome
    Created on : 2026年4月28日, 上午11:50:25
    Author     : User
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="../common/header.jsp" />

<!-- JSP Action to load appointment bean -->
<jsp:useBean id="appointmentDAO" class="ict.dao.AppointmentDAO" scope="page" />
<c:set var="appointmentId" value="${param.appointmentId}" />
<c:set var="appointment" value="${appointmentDAO.getAppointmentById(appointmentId)}" />

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header bg-primary text-white">
                <h5 class="card-title mb-0">Record Visit Outcome - Appointment #${appointment.appointmentId}</h5>
            </div>
            <div class="card-body">
                <c:if test="${appointment == null}">
                    <div class="alert alert-danger">
                        Appointment not found! Please go back to the appointment list.
                    </div>
                    <a href="${pageContext.request.contextPath}/staff/appointment-list.jsp" class="btn btn-primary">Back to Appointments</a>
                </c:if>

                <c:if test="${appointment != null}">
                    <!-- Appointment Details -->
                    <div class="mb-4">
                        <h6>Appointment Details</h6>
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>Patient Name:</strong> ${appointment.patientName}</p>
                                <p><strong>Date:</strong> <fmt:formatDate value="${appointment.appointmentDate}" pattern="yyyy-MM-dd" /></p>
                                <p><strong>Time:</strong> <fmt:formatDate value="${appointment.appointmentTime}" pattern="HH:mm" /></p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Service ID:</strong> ${appointment.serviceId}</p>
                                <p><strong>Current Status:</strong> ${appointment.status}</p>
                            </div>
                        </div>
                        <hr>
                    </div>

                    <!-- Visit Outcome Form -->
                    <form action="${pageContext.request.contextPath}/staffServlet/visitOutcome" method="post">
                        <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">
                        
                        <div class="mb-3">
                            <label for="status" class="form-label">Visit Status <span class="text-danger">*</span></label>
                            <select class="form-select" id="status" name="status" required>
                                <option value="">-- Select Status --</option>
                                <option value="ARRIVED">Patient Arrived</option>
                                <option value="COMPLETED">Visit Completed</option>
                                <option value="NO_SHOW">No Show</option>
                                <option value="CANCELLED">Cancelled by Clinic</option>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="visitOutcome" class="form-label">Visit Outcome / Notes <span class="text-danger">*</span></label>
                            <textarea class="form-control" id="visitOutcome" name="visitOutcome" rows="4" required 
                                      placeholder="Enter visit details, diagnosis, or cancellation reason here..."></textarea>
                        </div>

                        <div class="d-flex justify-content-between">
                            <a href="${pageContext.request.contextPath}/staff/appointment-list.jsp" class="btn btn-secondary">Cancel</a>
                            <button type="submit" class="btn btn-primary">Save Visit Outcome</button>
                        </div>
                    </form>
                </c:if>
            </div>
        </div>
    </div>
</div>

    </div>
    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>