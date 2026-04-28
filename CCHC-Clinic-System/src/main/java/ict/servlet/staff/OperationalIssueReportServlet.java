/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.servlet.staff;

import ict.bean.OperationalIssue;
import ict.bean.User;
import ict.dao.OperationalIssueDAO;

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
    private OperationalIssueDAO operationalIssueDAO;

    @Override
    public void init() {
        operationalIssueDAO = new OperationalIssueDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentStaff = (User) session.getAttribute("currentUser");

        // Create issue bean from form data
        OperationalIssue issue = new OperationalIssue();
        issue.setClinicId(currentStaff.getAssignedClinicId());
        issue.setReportedByStaffId(currentStaff.getUserId());
        issue.setIssueType(request.getParameter("issueType"));
        issue.setIssueDescription(request.getParameter("issueDescription"));

        try {
            // Save issue to database
            boolean isSuccess = operationalIssueDAO.createOperationalIssue(issue);

            if (isSuccess) {
                // Redirect to report page with success message (PRG pattern)
                response.sendRedirect(request.getContextPath() + "/staff/report-operational-issue.jsp?success=1");
            } else {
                response.sendRedirect(request.getContextPath() + "/staff/report-operational-issue.jsp?error=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/common/error.jsp?message=Failed to submit operational issue report");
        }
    }
}