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
@WebServlet(name = "VisitOutcomeServlet", urlPatterns = {"/staffServlet/visitOutcome"})
public class VisitOutcomeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get form parameters from request
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
        String status = request.getParameter("status"); // ARRIVED, COMPLETED, NO_SHOW, CANCELLED
        String visitOutcome = request.getParameter("visitOutcome");

        // Fully align with the database logic of LoginServlet
        cchc_clinic db = new cchc_clinic("jdbc:mysql://localhost:3306/cchc_clinic", "root", "password");
        
        try {
            // Update appointment visit outcome and status in database
            boolean isSuccess = db.updateVisitOutcome(appointmentId, status, visitOutcome);
            if (isSuccess) {
                // Redirect to appointment list on success (follows PRG pattern)
                response.sendRedirect(request.getContextPath() + "/staff/appointment-list.jsp?success=2");
            } else {
                // Redirect to appointment list with error flag on failure
                response.sendRedirect(request.getContextPath() + "/staff/appointment-list.jsp?error=2");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Redirect to generic error page on exception
            response.sendRedirect(request.getContextPath() + "/common/error.jsp?message=Failed to record visit outcome");
        }
    }
}