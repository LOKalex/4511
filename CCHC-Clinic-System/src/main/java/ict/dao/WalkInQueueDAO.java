/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.dao;

import ict.bean.WalkInQueue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class WalkInQueueDAO {
    private Connection conn;

    /**
     * Get active walk-in queue for a clinic and service on current date
     * @param clinicId ID of the clinic
     * @param serviceId ID of the service (0 for all services)
     * @param queueDate Current date
     * @return List of WalkInQueue objects
     * @throws SQLException if database operation fails
     */
    public List<WalkInQueue> getActiveWalkInQueue(int clinicId, int serviceId, Date queueDate) throws SQLException {
        List<WalkInQueue> queueList = new ArrayList<>();
        conn = DBConnection.getConnection();
        StringBuilder sql = new StringBuilder(
            "SELECT q.*, u.full_name as patient_name, s.service_name " +
            "FROM walkin_queue q " +
            "JOIN user u ON q.patient_id = u.user_id " +
            "JOIN service s ON q.service_id = s.service_id " +
            "WHERE q.clinic_id = ? AND q.queue_date = ? " +
            "AND q.status IN ('WAITING', 'CALLED', 'SKIPPED') "
        );

        if (serviceId > 0) {
            sql.append("AND q.service_id = ? ");
        }
        sql.append("ORDER BY q.queue_number ASC");

        PreparedStatement pstmt = conn.prepareStatement(sql.toString());
        pstmt.setInt(1, clinicId);
        pstmt.setDate(2, queueDate);
        if (serviceId > 0) {
            pstmt.setInt(3, serviceId);
        }

        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            WalkInQueue queue = new WalkInQueue();
            queue.setQueueId(rs.getInt("queue_id"));
            queue.setPatientId(rs.getInt("patient_id"));
            queue.setPatientName(rs.getString("patient_name"));
            queue.setClinicId(rs.getInt("clinic_id"));
            queue.setServiceId(rs.getInt("service_id"));
            queue.setServiceName(rs.getString("service_name"));
            queue.setQueueDate(rs.getDate("queue_date"));
            queue.setQueueNumber(rs.getInt("queue_number"));
            queue.setStatus(rs.getString("status"));
            queue.setCalledTime(rs.getTime("called_time"));
            queue.setEstimatedWaitMinutes(rs.getInt("estimated_wait_minutes"));
            queueList.add(queue);
        }

        rs.close();
        pstmt.close();
        DBConnection.closeConnection(conn);
        return queueList;
    }

    /**
     * Update queue status for call next patient
     * @param queueId ID of the queue record
     * @param status New status (CALLED, SKIPPED, COMPLETED, EXPIRED)
     * @return true if update successful
     * @throws SQLException if database operation fails
     */
    public boolean updateQueueStatus(int queueId, String status) throws SQLException {
        conn = DBConnection.getConnection();
        StringBuilder sql = new StringBuilder("UPDATE walkin_queue SET status = ? ");

        // Set called time when status is CALLED
        if (status.equals("CALLED")) {
            sql.append(", called_time = CURRENT_TIME ");
        }
        // Set completed time when status is COMPLETED
        if (status.equals("COMPLETED")) {
            sql.append(", completed_time = CURRENT_TIME ");
        }
        sql.append("WHERE queue_id = ?");

        PreparedStatement pstmt = conn.prepareStatement(sql.toString());
        pstmt.setString(1, status);
        pstmt.setInt(2, queueId);

        int result = pstmt.executeUpdate();
        pstmt.close();
        DBConnection.closeConnection(conn);
        return result > 0;
    }

    /**
     * Get the next waiting patient in the queue
     * @param clinicId ID of the clinic
     * @param serviceId ID of the service
     * @param queueDate Current date
     * @return WalkInQueue object of next patient, null if no waiting patient
     * @throws SQLException if database operation fails
     */
    public WalkInQueue getNextWaitingPatient(int clinicId, int serviceId, Date queueDate) throws SQLException {
        conn = DBConnection.getConnection();
        String sql = "SELECT q.*, u.full_name as patient_name, s.service_name " +
                "FROM walkin_queue q " +
                "JOIN user u ON q.patient_id = u.user_id " +
                "JOIN service s ON q.service_id = s.service_id " +
                "WHERE q.clinic_id = ? AND q.service_id = ? AND q.queue_date = ? " +
                "AND q.status = 'WAITING' " +
                "ORDER BY q.queue_number ASC LIMIT 1";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, clinicId);
        pstmt.setInt(2, serviceId);
        pstmt.setDate(3, queueDate);
        ResultSet rs = pstmt.executeQuery();

        WalkInQueue nextPatient = null;
        if (rs.next()) {
            nextPatient = new WalkInQueue();
            nextPatient.setQueueId(rs.getInt("queue_id"));
            nextPatient.setPatientId(rs.getInt("patient_id"));
            nextPatient.setPatientName(rs.getString("patient_name"));
            nextPatient.setServiceName(rs.getString("service_name"));
            nextPatient.setQueueNumber(rs.getInt("queue_number"));
            nextPatient.setStatus(rs.getString("status"));
        }

        rs.close();
        pstmt.close();
        DBConnection.closeConnection(conn);
        return nextPatient;
    }
}
