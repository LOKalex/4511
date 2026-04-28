/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.servlet.staff;

import ict.bean.Appointment;
import ict.bean.User;
import ict.bean.WalkInQueue;
import ict.dao.AppointmentDAO;
import ict.dao.WalkInQueueDAO;

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
    private AppointmentDAO appointmentDAO;
    private WalkInQueueDAO walkInQueueDAO;

    @Override
    public void init() {
        appointmentDAO = new AppointmentDAO();
        walkInQueueDAO = new WalkInQueueDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentStaff = (User) session.getAttribute("currentUser");
        int clinicId = currentStaff.getAssignedClinicId();

        // Get filter parameters
        int serviceId = request.getParameter("serviceId") != null ? Integer.parseInt(request.getParameter("serviceId")) : 0;
        Date currentDate = new Date(System.currentTimeMillis());

        try {
            // Load daily appointments
            List<Appointment> dailyAppointments = appointmentDAO.getDailyAppointments(clinicId, serviceId, currentDate);
            // Load pending approval appointments
            List<Appointment> pendingAppointments = appointmentDAO.getPendingApprovalAppointments(clinicId);
            // Load active walk-in queue
            List<WalkInQueue> walkInQueue = walkInQueueDAO.getActiveWalkInQueue(clinicId, serviceId, currentDate);

            // Set data to request scope
            request.setAttribute("dailyAppointments", dailyAppointments);
            request.setAttribute("pendingAppointments", pendingAppointments);
            request.setAttribute("walkInQueue", walkInQueue);
            request.setAttribute("selectedServiceId", serviceId);
            request.setAttribute("currentDate", currentDate);

            // Forward to dashboard JSP (View)
            request.getRequestDispatcher("/staff/staff-dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // Redirect to error page on exception
            response.sendRedirect(request.getContextPath() + "/common/error.jsp?message=Failed to load dashboard data");
        }
    }
}
