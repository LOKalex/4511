/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.dao;

import ict.bean.OperationalIssue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author User
 */
public class OperationalIssueDAO {
    private Connection conn;

    /**
     * Create a new operational issue report
     * @param issue OperationalIssue object
     * @return true if creation successful
     * @throws SQLException if database operation fails
     */
    public boolean createOperationalIssue(OperationalIssue issue) throws SQLException {
        conn = DBConnection.getConnection();
        String sql = "INSERT INTO operational_issue " +
                "(clinic_id, reported_by_staff_id, issue_type, issue_description, status, reported_at) " +
                "VALUES (?, ?, ?, ?, 'OPEN', CURRENT_TIMESTAMP)";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, issue.getClinicId());
        pstmt.setInt(2, issue.getReportedByStaffId());
        pstmt.setString(3, issue.getIssueType());
        pstmt.setString(4, issue.getIssueDescription());

        int result = pstmt.executeUpdate();
        pstmt.close();
        DBConnection.closeConnection(conn);
        return result > 0;
    }

    /**
     * Get all issues reported by a clinic
     * @param clinicId ID of the clinic
     * @return List of OperationalIssue objects
     * @throws SQLException if database operation fails
     */
    public List<OperationalIssue> getIssuesByClinic(int clinicId) throws SQLException {
        List<OperationalIssue> issueList = new ArrayList<>();
        conn = DBConnection.getConnection();
        String sql = "SELECT i.*, c.clinic_name, u.full_name as reported_by_staff_name " +
                "FROM operational_issue i " +
                "JOIN clinic c ON i.clinic_id = c.clinic_id " +
                "JOIN user u ON i.reported_by_staff_id = u.user_id " +
                "WHERE i.clinic_id = ? " +
                "ORDER BY i.reported_at DESC";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, clinicId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            OperationalIssue issue = new OperationalIssue();
            issue.setIssueId(rs.getInt("issue_id"));
            issue.setClinicId(rs.getInt("clinic_id"));
            issue.setClinicName(rs.getString("clinic_name"));
            issue.setReportedByStaffId(rs.getInt("reported_by_staff_id"));
            issue.setReportedByStaffName(rs.getString("reported_by_staff_name"));
            issue.setIssueType(rs.getString("issue_type"));
            issue.setIssueDescription(rs.getString("issue_description"));
            issue.setStatus(rs.getString("status"));
            issue.setReportedAt(rs.getTimestamp("reported_at"));
            issue.setResolvedAt(rs.getDate("resolved_at"));
            issue.setResolutionNote(rs.getString("resolution_note"));
            issueList.add(issue);
        }

        rs.close();
        pstmt.close();
        DBConnection.closeConnection(conn);
        return issueList;
    }
}
