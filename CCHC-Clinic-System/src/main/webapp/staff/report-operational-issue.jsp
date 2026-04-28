<%-- 
    Document   : report-operational-issue
    Created on : 2026年4月28日, 上午11:50:57
    Author     : User
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp" />

<div class="row justify-content-center">
    <div class="col-md-8">
        <div class="card">
            <div class="card-header bg-danger text-white">
                <h5 class="card-title mb-0">Report Operational Issue</h5>
            </div>
            <div class="card-body">
                <!-- Success/Error Alerts -->
                <c:if test="${param.success == 1}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        Issue reported successfully! We will review it shortly.
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                <c:if test="${param.error == 1}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        Failed to report issue! Please try again.
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Issue Report Form -->
                <form action="${pageContext.request.contextPath}/staffServlet/reportIssue" method="post">
                    <div class="mb-3">
                        <label for="issueType" class="form-label">Issue Type <span class="text-danger">*</span></label>
                        <select class="form-select" id="issueType" name="issueType" required>
                            <option value="">-- Select Issue Type --</option>
                            <option value="DOCTOR_UNAVAILABLE">Doctor Unavailable</option>
                            <option value="SERVICE_SUSPENDED">Service Suspended</option>
                            <option value="EQUIPMENT_FAILURE">Equipment Failure</option>
                            <option value="SYSTEM_ISSUE">System Issue</option>
                            <option value="OTHER">Other</option>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="issueDescription" class="form-label">Issue Description <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="issueDescription" name="issueDescription" rows="5" required 
                                  placeholder="Please provide detailed information about the issue, including time, location, and impact..."></textarea>
                    </div>

                    <div class="d-flex justify-content-between">
                        <a href="${pageContext.request.contextPath}/staff/dashboard" class="btn btn-secondary">Back to Dashboard</a>
                        <button type="submit" class="btn btn-danger">Submit Issue Report</button>
                    </div>
                </form>

                <!-- Previous Issues Table -->
                <hr class="my-4">
                <h6>Your Previously Reported Issues</h6>
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Report Date</th>
                                <th>Issue Type</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="issue" items="${reportedIssues}">
                                <tr>
                                    <td><fmt:formatDate value="${issue.reportedAt}" pattern="yyyy-MM-dd HH:mm" /></td>
                                    <td>${issue.issueType}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${issue.status == 'OPEN'}">
                                                <span class="badge bg-danger">Open</span>
                                            </c:when>
                                            <c:when test="${issue.status == 'IN_PROGRESS'}">
                                                <span class="badge bg-warning text-dark">In Progress</span>
                                            </c:when>
                                            <c:when test="${issue.status == 'RESOLVED'}">
                                                <span class="badge bg-success">Resolved</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">Closed</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty reportedIssues}">
                                <tr>
                                    <td colspan="3" class="text-center text-muted">No previously reported issues</td>
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