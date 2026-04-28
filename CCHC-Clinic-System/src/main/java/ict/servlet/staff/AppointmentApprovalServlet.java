/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.servlet.staff;

import ict.dao.AppointmentDAO;

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
    private AppointmentDAO appointmentDAO;

    @Override
    public void init() {
        appointmentDAO = new AppointmentDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form parameters
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        String action = request.getParameter("action"); // APPROVE or REJECT
        String approvalNote = request.getParameter("approvalNote");

        String status = action.equals("APPROVE") ? "APPROVED" : "REJECTED";

        try {
            // Update appointment status
            boolean isSuccess = appointmentDAO.updateAppointmentApproval(appointmentId, status, approvalNote);

            if (isSuccess) {
                // Redirect to appointment list on success (PRG pattern)
                response.sendRedirect(request.getContextPath() + "/staff/appointment-list.jsp?success=1");
            } else {
                response.sendRedirect(request.getContextPath() + "/staff/appointment-list.jsp?error=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/common/error.jsp?message=Failed to process appointment approval");
        }
    }
}
