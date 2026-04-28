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
import java.util.List;
/**
 *
 * @author User
 */
@WebServlet(name = "StaffDashboardServlet", urlPatterns = {"/staff/dashboard"})
public class StaffDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get current logged-in staff from session
        HttpSession session = request.getSession();
        User currentStaff = (User) session.getAttribute("currentUser");
        int clinicId = currentStaff.getAssignedClinicId();
        
        // Get filter parameters from request (0 = all services)
        int serviceId = request.getParameter("serviceId") != null ? Integer.parseInt(request.getParameter("serviceId")) : 0;
        Date currentDate = new Date(System.currentTimeMillis());

        // Fully align with the database logic of LoginServlet
        cchc_clinic db = new cchc_clinic("jdbc:mysql://localhost:3306/cchc_clinic", "root", "password");
        
        try {
            // Load daily appointments for the clinic
            List<String[]> dailyAppointments = db.getDailyAppointments(clinicId, serviceId, currentDate);
            // Load pending approval appointments for the clinic
            List<String[]> pendingAppointments = db.getPendingApprovalAppointments(clinicId);
            // Load active walk-in queue for the clinic
            List<String[]> walkInQueue = db.getActiveWalkInQueue(clinicId, serviceId, currentDate);
            
            // Set all data to request scope for JSP rendering
            request.setAttribute("dailyAppointments", dailyAppointments);
            request.setAttribute("pendingAppointments", pendingAppointments);
            request.setAttribute("walkInQueue", walkInQueue);
            request.setAttribute("selectedServiceId", serviceId);
            request.setAttribute("currentDate", currentDate);
            
            // Forward request to dashboard JSP (View layer)
            request.getRequestDispatcher("/staff/staff-dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // Redirect to generic error page on exception
            response.sendRedirect(request.getContextPath() + "/common/error.jsp?message=Failed to load dashboard data");
        }
    }
}