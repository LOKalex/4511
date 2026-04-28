/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.servlet.staff;
import ict.db.cchc_clinic;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 *
 * @author User
 */
@WebServlet(name = "AppointmentApprovalServlet", urlPatterns = {"/staffServlet/appointmentApproval"})
public class AppointmentApprovalServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form parameters from request
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        String action = request.getParameter("action"); // APPROVE or REJECT
        String approvalNote = request.getParameter("approvalNote");
        // Map action to final status value
        String status = action.equals("APPROVE") ? "APPROVED" : "REJECTED";

        // Fully align with the database logic of LoginServlet
        cchc_clinic db = new cchc_clinic("jdbc:mysql://localhost:3306/cchc_clinic", "root", "password");
        
        try {
            // Update appointment approval status in database
            boolean isSuccess = db.updateAppointmentApproval(appointmentId, status, approvalNote);
            if (isSuccess) {
                // Redirect to appointment list on success (follows PRG pattern)
                response.sendRedirect(request.getContextPath() + "/staff/appointment-list.jsp?success=1");
            } else {
                // Redirect to appointment list with error flag on failure
                response.sendRedirect(request.getContextPath() + "/staff/appointment-list.jsp?error=1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Redirect to generic error page on exception
            response.sendRedirect(request.getContextPath() + "/common/error.jsp?message=Failed to process appointment approval");
        }
    }
}