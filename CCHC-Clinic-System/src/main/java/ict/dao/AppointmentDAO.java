/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.dao;

import ict.bean.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class AppointmentDAO {
    private Connection conn;

    /**
     * Get daily appointment list for a clinic, filtered by service and date
     * @param clinicId ID of the staff's assigned clinic
     * @param serviceId ID of the service (0 for all services)
     * @param appointmentDate Date of appointments
     * @return List of Appointment objects
     * @throws SQLException if database operation fails
     */
    public List<Appointment> getDailyAppointments(int clinicId, int serviceId, Date appointmentDate) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        conn = DBConnection.getConnection();
        StringBuilder sql = new StringBuilder(
            "SELECT a.*, u.full_name as patient_name, c.clinic_name, s.service_name " +
            "FROM appointment a " +
            "JOIN user u ON a.patient_id = u.user_id " +
            "JOIN clinic c ON a.clinic_id = c.clinic_id " +
            "JOIN service s ON a.service_id = s.service_id " +
            "WHERE a.clinic_id = ? AND a.appointment_date = ? "
        );

        // Add service filter if specified
        if (serviceId > 0) {
            sql.append("AND a.service_id = ? ");
        }
        sql.append("ORDER BY a.appointment_time ASC");

        PreparedStatement pstmt = conn.prepareStatement(sql.toString());
        pstmt.setInt(1, clinicId);
        pstmt.setDate(2, appointmentDate);
        if (serviceId > 0) {
            pstmt.setInt(3, serviceId);
        }

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Appointment appt = new Appointment();
            appt.setAppointmentId(rs.getInt("appointment_id"));
            appt.setPatientId(rs.getInt("patient_id"));
            appt.setPatientName(rs.getString("patient_name"));
            appt.setClinicId(rs.getInt("clinic_id"));
            appt.setClinicName(rs.getString("clinic_name"));
            appt.setServiceId(rs.getInt("service_id"));
            appt.setServiceName(rs.getString("service_name"));
            appt.setAppointmentDate(rs.getDate("appointment_date"));
            appt.setAppointmentTime(rs.getTime("appointment_time"));
            appt.setStatus(rs.getString("status"));
            appt.setApprovalNote(rs.getString("approval_note"));
            appt.setVisitOutcome(rs.getString("visit_outcome"));
            appointments.add(appt);
        }

        rs.close();
        pstmt.close();
        DBConnection.closeConnection(conn);
        return appointments;
    }

    /**
     * Get pending approval appointments for a clinic
     * @param clinicId ID of the clinic
     * @return List of pending appointments
     * @throws SQLException if database operation fails
     */
    public List<Appointment> getPendingApprovalAppointments(int clinicId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        conn = DBConnection.getConnection();
        String sql = "SELECT a.*, u.full_name as patient_name, c.clinic_name, s.service_name " +
                "FROM appointment a " +
                "JOIN user u ON a.patient_id = u.user_id " +
                "JOIN clinic c ON a.clinic_id = c.clinic_id " +
                "JOIN service s ON a.service_id = s.service_id " +
                "WHERE a.clinic_id = ? AND a.status = 'PENDING' " +
                "ORDER BY a.created_at ASC";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, clinicId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Appointment appt = new Appointment();
            appt.setAppointmentId(rs.getInt("appointment_id"));
            appt.setPatientId(rs.getInt("patient_id"));
            appt.setPatientName(rs.getString("patient_name"));
            appt.setClinicId(rs.getInt("clinic_id"));
            appt.setClinicName(rs.getString("clinic_name"));
            appt.setServiceId(rs.getInt("service_id"));
            appt.setServiceName(rs.getString("service_name"));
            appt.setAppointmentDate(rs.getDate("appointment_date"));
            appt.setAppointmentTime(rs.getTime("appointment_time"));
            appt.setStatus(rs.getString("status"));
            appointments.add(appt);
        }

        rs.close();
        pstmt.close();
        DBConnection.closeConnection(conn);
        return appointments;
    }

    /**
     * Update appointment approval status (APPROVED/REJECTED)
     * @param appointmentId ID of the appointment
     * @param status New status
     * @param approvalNote Staff's note for approval/rejection
     * @return true if update successful
     * @throws SQLException if database operation fails
     */
    public boolean updateAppointmentApproval(int appointmentId, String status, String approvalNote) throws SQLException {
        conn = DBConnection.getConnection();
        String sql = "UPDATE appointment SET status = ?, approval_note = ? WHERE appointment_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, status);
        pstmt.setString(2, approvalNote);
        pstmt.setInt(3, appointmentId);

        int result = pstmt.executeUpdate();
        pstmt.close();
        DBConnection.closeConnection(conn);
        return result > 0;
    }

    /**
     * Update appointment check-in status and visit outcome
     * @param appointmentId ID of the appointment
     * @param status New status (ARRIVED, COMPLETED, NO_SHOW, CANCELLED)
     * @param visitOutcome Outcome note of the visit
     * @return true if update successful
     * @throws SQLException if database operation fails
     */
    public boolean updateVisitOutcome(int appointmentId, String status, String visitOutcome) throws SQLException {
        conn = DBConnection.getConnection();
        String sql = "UPDATE appointment SET status = ?, visit_outcome = ? WHERE appointment_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, status);
        pstmt.setString(2, visitOutcome);
        pstmt.setInt(3, appointmentId);

        int result = pstmt.executeUpdate();
        pstmt.close();
        DBConnection.closeConnection(conn);
        return result > 0;
    }

    /**
     * Get appointment by ID
     * @param appointmentId ID of the appointment
     * @return Appointment object
     * @throws SQLException if database operation fails
     */
    public Appointment getAppointmentById(int appointmentId) throws SQLException {
        conn = DBConnection.getConnection();
        String sql = "SELECT a.*, u.full_name as patient_name FROM appointment a " +
                "JOIN user u ON a.patient_id = u.user_id WHERE a.appointment_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, appointmentId);
        ResultSet rs = pstmt.executeQuery();

        Appointment appt = null;
        if (rs.next()) {
            appt = new Appointment();
            appt.setAppointmentId(rs.getInt("appointment_id"));
            appt.setPatientId(rs.getInt("patient_id"));
            appt.setPatientName(rs.getString("patient_name"));
            appt.setClinicId(rs.getInt("clinic_id"));
            appt.setServiceId(rs.getInt("service_id"));
            appt.setAppointmentDate(rs.getDate("appointment_date"));
            appt.setAppointmentTime(rs.getTime("appointment_time"));
            appt.setStatus(rs.getString("status"));
        }

        rs.close();
        pstmt.close();
        DBConnection.closeConnection(conn);
        return appt;
    }
}
