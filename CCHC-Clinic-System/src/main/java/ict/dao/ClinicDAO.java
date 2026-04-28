/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.dao;

import ict.bean.Clinic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author User
 */
public class ClinicDAO {
    private Connection conn;

    /**
     * Get clinic by ID
     * @param clinicId ID of the clinic
     * @return Clinic object
     * @throws SQLException if database operation fails
     */
    public Clinic getClinicById(int clinicId) throws SQLException {
        conn = DBConnection.getConnection();
        String sql = "SELECT * FROM clinic WHERE clinic_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, clinicId);
        ResultSet rs = pstmt.executeQuery();

        Clinic clinic = null;
        if (rs.next()) {
            clinic = new Clinic();
            clinic.setClinicId(rs.getInt("clinic_id"));
            clinic.setClinicName(rs.getString("clinic_name"));
            clinic.setAddress(rs.getString("address"));
            clinic.setPhone(rs.getString("phone"));
            clinic.setOpeningHours(rs.getString("opening_hours"));
            clinic.setWalkInEnabled(rs.getBoolean("walkin_enabled"));
        }

        rs.close();
        pstmt.close();
        DBConnection.closeConnection(conn);
        return clinic;
    }

    /**
     * Get all active clinics
     * @return List of Clinic objects
     * @throws SQLException if database operation fails
     */
    public List<Clinic> getAllClinics() throws SQLException {
        List<Clinic> clinics = new ArrayList<>();
        conn = DBConnection.getConnection();
        String sql = "SELECT * FROM clinic ORDER BY clinic_name ASC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Clinic clinic = new Clinic();
            clinic.setClinicId(rs.getInt("clinic_id"));
            clinic.setClinicName(rs.getString("clinic_name"));
            clinic.setAddress(rs.getString("address"));
            clinic.setPhone(rs.getString("phone"));
            clinic.setOpeningHours(rs.getString("opening_hours"));
            clinic.setWalkInEnabled(rs.getBoolean("walkin_enabled"));
            clinics.add(clinic);
        }

        rs.close();
        pstmt.close();
        DBConnection.closeConnection(conn);
        return clinics;
    }
}
