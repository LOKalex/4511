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
@WebServlet(name = "VisitOutcomeServlet", urlPatterns = {"/staffServlet/visitOutcome"})
public class VisitOutcomeServlet extends HttpServlet {
    private AppointmentDAO appointmentDAO;

    @Override
    public void init() {
        appointmentDAO = new AppointmentDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form parameters
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        String status = request.getParameter("status"); // ARRIVED, COMPLETED, NO_SHOW, CANCELLED
        String visitOutcome = request.getParameter("visitOutcome");

        try {
            // Update appointment visit outcome
            boolean isSuccess = appointmentDAO.updateVisitOutcome(appointmentId, status, visitOutcome);

            if (isSuccess) {
                // Redirect to appointment list (PRG pattern)
                response.sendRedirect(request.getContextPath() + "/staff/appointment-list.jsp?success=2");
            } else {
                response.sendRedirect(request.getContextPath() + "/staff/appointment-list.jsp?error=2");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/common/error.jsp?message=Failed to record visit outcome");
        }
    }
}
