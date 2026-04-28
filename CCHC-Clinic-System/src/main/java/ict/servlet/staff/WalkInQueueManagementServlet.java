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
import java.sql.Date;
/**
 *
 * @author User
 */
@WebServlet(name = "WalkInQueueManagementServlet", urlPatterns = {"/staffServlet/queueManagement"})
public class WalkInQueueManagementServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get current logged-in staff from session
        HttpSession session = request.getSession();
        User currentStaff = (User) session.getAttribute("currentUser");
        int clinicId = currentStaff.getAssignedClinicId();
        
        // Get form parameters from request
        String action = request.getParameter("action");
        int queueId = request.getParameter("queueId") != null ? Integer.parseInt(request.getParameter("queueId")) : 0;
        int serviceId = Integer.parseInt(request.getParameter("serviceId"));
        Date currentDate = new Date(System.currentTimeMillis());

        // Fully align with the database logic of LoginServlet
        cchc_clinic db = new cchc_clinic("jdbc:mysql://localhost:3306/cchc_clinic", "root", "password");
        
        try {
            boolean isSuccess = false;
            // Execute different queue operations based on action type
            switch (action) {
                case "CALL_NEXT":
                    // Retrieve next waiting patient and mark as CALLED
                    String[] nextPatient = db.getNextWaitingPatient(clinicId, serviceId, currentDate);
                    if (nextPatient != null) {
                        isSuccess = db.updateQueueStatus(Integer.parseInt(nextPatient[0]), "CALLED");
                    }
                    break;
                case "SKIP":
                    // Mark current patient as SKIPPED
                    isSuccess = db.updateQueueStatus(queueId, "SKIPPED");
                    break;
                case "COMPLETED":
                    // Mark patient visit as COMPLETED
                    isSuccess = db.updateQueueStatus(queueId, "COMPLETED");
                    break;
                case "EXPIRED":
                    // Mark patient queue entry as EXPIRED
                    isSuccess = db.updateQueueStatus(queueId, "EXPIRED");
                    break;
            }
            // Redirect back to queue management page (follows PRG pattern)
            if (isSuccess) {
                response.sendRedirect(request.getContextPath() + "/staff/walkin-queue-management.jsp?success=1");
            } else {
                response.sendRedirect(request.getContextPath() + "/staff/walkin-queue-management.jsp?error=1");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Redirect to generic error page on exception
            response.sendRedirect(request.getContextPath() + "/common/error.jsp?message=Failed to process queue action");
        }
    }
}