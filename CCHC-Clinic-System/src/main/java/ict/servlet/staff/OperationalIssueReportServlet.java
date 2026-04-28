/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.servlet.staff;
import ict.bean.User;
import ict.db.cchc_clinic;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
/**
 *
 * @author User
 */
@WebServlet(name = "OperationalIssueReportServlet", urlPatterns = {"/staffServlet/reportIssue"})
public class OperationalIssueReportServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get current logged-in staff from session
        HttpSession session = request.getSession();
        User currentStaff = (User) session.getAttribute("currentUser");

        // Extract form parameters and staff context
        int clinicId = currentStaff.getAssignedClinicId();
        int staffId = currentStaff.getUserId();
        String issueType = request.getParameter("issueType");
        String issueDescription = request.getParameter("issueDescription");

        // Fully align with the database logic of LoginServlet
        cchc_clinic db = new cchc_clinic("jdbc:mysql://localhost:3306/cchc_clinic", "root", "password");
        
        try {
            // Save new operational issue to database
            boolean isSuccess = db.reportOperationalIssue(clinicId, staffId, issueType, issueDescription);
            if (isSuccess) {
                // Redirect to report page with success flag (follows PRG pattern)
                response.sendRedirect(request.getContextPath() + "/staff/report-operational-issue.jsp?success=1");
            } else {
                // Redirect to report page with error flag on failure
                response.sendRedirect(request.getContextPath() + "/staff/report-operational-issue.jsp?error=1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Redirect to generic error page on exception
            response.sendRedirect(request.getContextPath() + "/common/error.jsp?message=Failed to submit operational issue report");
        }
    }
}