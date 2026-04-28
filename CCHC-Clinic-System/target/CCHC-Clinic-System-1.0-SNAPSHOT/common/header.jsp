<%-- 
    Document   : header
    Created on : 2026年4月28日, 上午11:47:03
    Author     : User
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    // Check if user is logged in
    if (session.getAttribute("currentUser") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp?error=session_expired");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CCHC Clinic System - Staff Portal</title>
    <!-- Bootstrap 5 CSS for responsive design -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/staff/dashboard">CCHC Clinic Staff Portal</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/staff/dashboard">Dashboard</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/staff/appointment-list.jsp">Appointments</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/staff/walkin-queue-management.jsp">Walk-in Queue</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/staff/report-operational-issue.jsp">Report Issue</a>
                    </li>
                </ul>
                <div class="navbar-nav">
                    <span class="nav-link text-light">Welcome, ${currentUser.fullName}</span>
                    <a class="nav-link" href="${pageContext.request.contextPath}/logoutServlet">Logout</a>
                </div>
            </div>
        </div>
    </nav>
    <div class="container mt-4">
