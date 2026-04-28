/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Database access class for CCHC Clinic System
 * Handles database connections and CRUD operations for clinic-related tables
 * 
 * @author User
 */
public class cchc_clinic {
    private final String url;
    private final String username;
    private final String password;

    /**
     * Constructor to initialize database credentials
     * 
     * @param url Database connection URL (e.g., jdbc:mysql://localhost:3306/cchc_clinic)
     * @param username Database username
     * @param password Database password
     */
    public cchc_clinic(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Establishes and returns a database connection
     * 
     * @return Connection object to the database
     * @throws SQLException If connection fails
     */
    public Connection getConnection() throws SQLException {
        try {
            // Register MySQL JDBC driver (for older JDBC versions)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Closes database resources to prevent leaks
     * 
     * @param conn Connection to close (can be null)
     * @param stmt Statement to close (can be null)
     * @param rs ResultSet to close (can be null)
     */
    private void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }

    // ------------------------------ CLINIC TABLE OPERATIONS ------------------------------
    /**
     * Retrieves all clinics from the database
     * 
     * @return List of clinic records (each as String array: [clinic_id, clinic_name, address, phone, opening_hours, walkin_enabled])
     */
    public List<String[]> getAllClinics() {
        List<String[]> clinics = new ArrayList<>();
        String query = "SELECT * FROM clinic";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                String[] clinic = {
                    String.valueOf(rs.getInt("clinic_id")),
                    rs.getString("clinic_name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("opening_hours"),
                    String.valueOf(rs.getBoolean("walkin_enabled"))
                };
                clinics.add(clinic);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving clinics: " + e.getMessage());
        } finally {
            closeResources(conn, stmt, rs);
        }
        return clinics;
    }

    /**
     * Retrieves a clinic by its ID
     * 
     * @param clinicId ID of the clinic to retrieve
     * @return String array with clinic details or null if not found
     */
    public String[] getClinicById(int clinicId) {
        String query = "SELECT * FROM clinic WHERE clinic_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, clinicId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new String[]{
                    String.valueOf(rs.getInt("clinic_id")),
                    rs.getString("clinic_name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("opening_hours"),
                    String.valueOf(rs.getBoolean("walkin_enabled"))
                };
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving clinic by ID: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }

    // ------------------------------ USER TABLE OPERATIONS ------------------------------
    /**
     * Retrieves a user by username (for login/authentication)
     * 
     * @param username Username to search for
     * @return String array with user details or null if not found
     */
    public String[] getUserByUsername(String username) {
        String query = "SELECT * FROM user WHERE username = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new String[]{
                    String.valueOf(rs.getInt("user_id")),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getString("role"),
                    rs.getString("assigned_clinic_id"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("created_at")
                };
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by username: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }

    /**
     * Adds a new user to the database
     * 
     * @param username User's username (unique)
     * @param password User's password
     * @param fullName User's full name
     * @param role User role (PATIENT/STAFF/ADMIN)
     * @param clinicId Assigned clinic ID (null for patients without assigned clinic)
     * @param email User's email (unique)
     * @param phone User's phone number
     * @return true if user was added successfully, false otherwise
     */
    public boolean addUser(String username, String password, String fullName, String role, 
                          Integer clinicId, String email, String phone) {
        String query = "INSERT INTO user (username, password, full_name, role, assigned_clinic_id, email, phone) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, role);
            
            if (clinicId == null) {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            } else {
                pstmt.setInt(5, clinicId);
            }
            
            pstmt.setString(6, email);
            pstmt.setString(7, phone);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    // ------------------------------ APPOINTMENT TABLE OPERATIONS ------------------------------
    /**
     * Books a new appointment for a patient
     * 
     * @param patientId ID of the patient
     * @param clinicId ID of the clinic
     * @param serviceId ID of the service
     * @param apptDate Appointment date (format: YYYY-MM-DD)
     * @param apptTime Appointment time (format: HH:MM:SS)
     * @return true if appointment was created successfully, false otherwise
     */
    public boolean bookAppointment(int patientId, int clinicId, int serviceId, 
                                  String apptDate, String apptTime) {
        String query = "INSERT INTO appointment (patient_id, clinic_id, service_id, appointment_date, appointment_time) " +
                       "VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, patientId);
            pstmt.setInt(2, clinicId);
            pstmt.setInt(3, serviceId);
            pstmt.setString(4, apptDate);
            pstmt.setString(5, apptTime);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error booking appointment: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    /**
     * Retrieves all appointments for a specific patient
     * 
     * @param patientId ID of the patient
     * @return List of appointments (each as String array with appointment details)
     */
    public List<String[]> getPatientAppointments(int patientId) {
        List<String[]> appointments = new ArrayList<>();
        String query = "SELECT * FROM appointment WHERE patient_id = ? ORDER BY appointment_date DESC, appointment_time DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, patientId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] appt = {
                    String.valueOf(rs.getInt("appointment_id")),
                    String.valueOf(rs.getInt("patient_id")),
                    String.valueOf(rs.getInt("clinic_id")),
                    String.valueOf(rs.getInt("service_id")),
                    rs.getString("appointment_date"),
                    rs.getString("appointment_time"),
                    rs.getString("status"),
                    rs.getString("approval_note"),
                    rs.getString("visit_outcome"),
                    rs.getString("created_at")
                };
                appointments.add(appt);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patient appointments: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return appointments;
    }

    // ------------------------------ WALK-IN QUEUE OPERATIONS ------------------------------
    /**
     * Adds a patient to the walk-in queue
     * 
     * @param patientId ID of the patient
     * @param clinicId ID of the clinic
     * @param serviceId ID of the service
     * @param queueDate Date of the queue (YYYY-MM-DD)
     * @param queueNumber Queue number (unique per clinic/service/date)
     * @return true if added successfully, false otherwise
     */
    public boolean addWalkInQueue(int patientId, int clinicId, int serviceId, 
                                 String queueDate, int queueNumber) {
        String query = "INSERT INTO walkin_queue (patient_id, clinic_id, service_id, queue_date, queue_number) " +
                       "VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, patientId);
            pstmt.setInt(2, clinicId);
            pstmt.setInt(3, serviceId);
            pstmt.setString(4, queueDate);
            pstmt.setInt(5, queueNumber);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding walk-in queue: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    /**
     * Gets the next available queue number for a clinic/service/date
     * 
     * @param clinicId ID of the clinic
     * @param serviceId ID of the service
     * @param queueDate Date of the queue (YYYY-MM-DD)
     * @return Next queue number (starts at 1 if no existing queue)
     */
    public int getNextQueueNumber(int clinicId, int serviceId, String queueDate) {
        String query = "SELECT MAX(queue_number) AS max_num FROM walkin_queue " +
                       "WHERE clinic_id = ? AND service_id = ? AND queue_date = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, clinicId);
            pstmt.setInt(2, serviceId);
            pstmt.setString(3, queueDate);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt("max_num") > 0) {
                return rs.getInt("max_num") + 1;
            }
            return 1; // First queue number if none exist
        } catch (SQLException e) {
            System.err.println("Error getting next queue number: " + e.getMessage());
            return 1; // Fallback to 1 on error
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    // ------------------------------ OPERATIONAL ISSUE OPERATIONS ------------------------------
    /**
     * Reports a new operational issue for a clinic
     * 
     * @param clinicId ID of the clinic
     * @param staffId ID of the staff reporting the issue
     * @param issueType Type of issue (e.g., "Equipment", "Staffing", "Facility")
     * @param description Detailed description of the issue
     * @return true if issue was reported successfully, false otherwise
     */
    public boolean reportOperationalIssue(int clinicId, int staffId, String issueType, String description) {
        String query = "INSERT INTO operational_issue (clinic_id, reported_by_staff_id, issue_type, issue_description) " +
                       "VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, clinicId);
            pstmt.setInt(2, staffId);
            pstmt.setString(3, issueType);
            pstmt.setString(4, description);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error reporting operational issue: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    /**
     * Updates the status of an operational issue (e.g., OPEN → RESOLVED)
     * 
     * @param issueId ID of the issue to update
     * @param newStatus New status (OPEN/IN_PROGRESS/RESOLVED/CLOSED)
     * @param resolutionNote Note about the resolution (null if not resolved)
     * @param resolvedAt Resolution date (null if not resolved)
     * @return true if updated successfully, false otherwise
     */
    public boolean updateIssueStatus(int issueId, String newStatus, String resolutionNote, String resolvedAt) {
        String query = "UPDATE operational_issue SET status = ?, resolution_note = ?, resolved_at = ? WHERE issue_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, newStatus);
            
            if (resolutionNote == null) {
                pstmt.setNull(2, java.sql.Types.VARCHAR);
            } else {
                pstmt.setString(2, resolutionNote);
            }
            
            if (resolvedAt == null) {
                pstmt.setNull(3, java.sql.Types.DATE);
            } else {
                pstmt.setString(3, resolvedAt);
            }
            
            pstmt.setInt(4, issueId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating issue status: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
}
