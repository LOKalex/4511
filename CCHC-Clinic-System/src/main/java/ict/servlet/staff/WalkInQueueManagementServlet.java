/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.servlet.staff;

import ict.bean.User;
import ict.bean.WalkInQueue;
import ict.dao.WalkInQueueDAO;

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
    private WalkInQueueDAO walkInQueueDAO;

    @Override
    public void init() {
        walkInQueueDAO = new WalkInQueueDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User currentStaff = (User) session.getAttribute("currentUser");
        int clinicId = currentStaff.getAssignedClinicId();

        String action = request.getParameter("action");
        int queueId = request.getParameter("queueId") != null ? Integer.parseInt(request.getParameter("queueId")) : 0;
        int serviceId = Integer.parseInt(request.getParameter("serviceId"));
        Date currentDate = new Date(System.currentTimeMillis());

        try {
            boolean isSuccess = false;
            switch (action) {
                case "CALL_NEXT":
                    // Get next waiting patient and mark as CALLED
                    WalkInQueue nextPatient = walkInQueueDAO.getNextWaitingPatient(clinicId, serviceId, currentDate);
                    if (nextPatient != null) {
                        isSuccess = walkInQueueDAO.updateQueueStatus(nextPatient.getQueueId(), "CALLED");
                    }
                    break;
                case "SKIP":
                    // Mark current patient as SKIPPED
                    isSuccess = walkInQueueDAO.updateQueueStatus(queueId, "SKIPPED");
                    break;
                case "COMPLETED":
                    // Mark patient as COMPLETED
                    isSuccess = walkInQueueDAO.updateQueueStatus(queueId, "COMPLETED");
                    break;
                case "EXPIRED":
                    // Mark patient as EXPIRED
                    isSuccess = walkInQueueDAO.updateQueueStatus(queueId, "EXPIRED");
                    break;
            }

            // Redirect back to queue management page (PRG pattern)
            if (isSuccess) {
                response.sendRedirect(request.getContextPath() + "/staff/walkin-queue-management.jsp?success=1");
            } else {
                response.sendRedirect(request.getContextPath() + "/staff/walkin-queue-management.jsp?error=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/common/error.jsp?message=Failed to process queue action");
        }
    }
}
