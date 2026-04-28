<%-- 
    Document   : error
    Created on : 2026年4月28日, 下午2:00:15
    Author     : User
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>System Error - CCHC Clinic</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #fff5f5; margin: 0; padding: 0; }
        .error-container {
            width: 500px;
            margin: 100px auto;
            padding: 40px;
            background: white;
            border: 1px solid #ffcccc;
            border-radius: 8px;
            text-align: center;
        }
        h1 { color: #dc3545; font-size: 48px; margin: 0 0 20px 0; }
        h3 { color: #555; margin-bottom: 20px; }
        p { color: #666; line-height: 1.6; }
        .btn-home {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        .btn-home:hover { background-color: #5a6268; }
    </style>
</head>
<body>

    <jsp:include page="header.jsp" />

    <div class="error-container">
        <h1>Oops!</h1>
        <h3>Something went wrong</h3>
        
        <p>
            <% 
                String msg = (String) request.getAttribute("errorMessage");
                if (msg != null) {
                    out.println(msg);
                } else {
                    out.println("An unexpected error has occurred. Please contact the system administrator.");
                }
            %>
        </p>

        <a href="<%= request.getContextPath() %>/login.jsp" class="btn-home">Back to Login Page</a>
    </div>

    </div>
    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

</body>
</html>
