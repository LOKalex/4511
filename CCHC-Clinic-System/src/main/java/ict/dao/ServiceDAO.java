/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.dao;

import ict.bean.Service;

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
public class ServiceDAO {
    private Connection conn;

    /**
     * Get all active services
     * @return List of Service objects
     * @throws SQLException if database operation fails
     */
    public List<Service> getAllActiveServices() throws SQLException {
        List<Service> services = new ArrayList<>();
        conn = DBConnection.getConnection();
        String sql = "SELECT * FROM service WHERE is_active = true ORDER BY service_name ASC";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Service service = new Service();
            service.setServiceId(rs.getInt("service_id"));
            service.setServiceName(rs.getString("service_name"));
            service.setServiceDescription(rs.getString("service_description"));
            service.setDurationMinutes(rs.getInt("duration_minutes"));
            service.setPrice(rs.getBigDecimal("price"));
            service.setRequiresApproval(rs.getBoolean("requires_approval"));
            service.setActive(rs.getBoolean("is_active"));
            services.add(service);
        }

        rs.close();
        pstmt.close();
        DBConnection.closeConnection(conn);
        return services;
    }

    /**
     * Get service by ID
     * @param serviceId ID of the service
     * @return Service object
     * @throws SQLException if database operation fails
     */
    public Service getServiceById(int serviceId) throws SQLException {
        conn = DBConnection.getConnection();
        String sql = "SELECT * FROM service WHERE service_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, serviceId);
        ResultSet rs = pstmt.executeQuery();

        Service service = null;
        if (rs.next()) {
            service = new Service();
            service.setServiceId(rs.getInt("service_id"));
            service.setServiceName(rs.getString("service_name"));
            service.setRequiresApproval(rs.getBoolean("requires_approval"));
            service.setActive(rs.getBoolean("is_active"));
        }

        rs.close();
        pstmt.close();
        DBConnection.closeConnection(conn);
        return service;
    }
}
