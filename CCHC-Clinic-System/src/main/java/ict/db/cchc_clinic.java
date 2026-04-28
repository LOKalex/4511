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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
/**
 * Database access class for CCHC Clinic System
 * Handles database connections and CRUD operations for clinic-related tables
 * 
 * @author User
 */
public class cchc_clinic {
    private String url = "";
    private String username = "";
    private String password = "";
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
     * Closes database resources to prevent connection leaks
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

    // ========== New: Update appointment approval status method (used by AppointmentApprovalServlet) ==========
    /**
     * Update appointment approval status (APPROVED/REJECTED)
     * @param appointmentId ID of the target appointment
     * @param status New approval status
     * @param approvalNote Staff's note for approval or rejection
     * @return true if update executed successfully
     */
    public boolean updateAppointmentApproval(int appointmentId, String status, String approvalNote) {
        String query = "UPDATE appointment SET status = ?, approval_note = ? WHERE appointment_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, status);
            pstmt.setString(2, approvalNote);
            pstmt.setInt(3, appointmentId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating appointment approval: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    // ========== New: Get daily appointment list for clinic (used by StaffDashboardServlet) ==========
    /**
     * Get daily appointment list for a clinic, filtered by service and date
     * @param clinicId ID of the staff's assigned clinic
     * @param serviceId ID of the service (0 for all services)
     * @param appointmentDate Date of appointments to retrieve
     * @return List of appointment records (each as String array)
     */
    public List<String[]> getDailyAppointments(int clinicId, int serviceId, Date appointmentDate) {
        List<String[]> appointments = new ArrayList<>();
        StringBuilder query = new StringBuilder(
            "SELECT a.*, u.full_name as patient_name, c.clinic_name, s.service_name " +
            "FROM appointment a " +
            "JOIN user u ON a.patient_id = u.user_id " +
            "JOIN clinic c ON a.clinic_id = c.clinic_id " +
            "JOIN service s ON a.service_id = s.service_id " +
            "WHERE a.clinic_id = ? AND a.appointment_date = ? "
        );
        // Add service filter if specific service is selected
        if (serviceId > 0) {
            query.append("AND a.service_id = ? ");
        }
        query.append("ORDER BY a.appointment_time ASC");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setInt(1, clinicId);
            pstmt.setDate(2, appointmentDate);
            if (serviceId > 0) {
                pstmt.setInt(3, serviceId);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] appt = {
                    String.valueOf(rs.getInt("appointment_id")),
                    String.valueOf(rs.getInt("patient_id")),
                    rs.getString("patient_name"),
                    String.valueOf(rs.getInt("clinic_id")),
                    rs.getString("clinic_name"),
                    String.valueOf(rs.getInt("service_id")),
                    rs.getString("service_name"),
                    rs.getString("appointment_date"),
                    rs.getString("appointment_time"),
                    rs.getString("status"),
                    rs.getString("approval_note"),
                    rs.getString("visit_outcome")
                };
                appointments.add(appt);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving daily appointments: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return appointments;
    }

    // ========== New: Get pending approval appointments for clinic (used by StaffDashboardServlet) ==========
    /**
     * Get pending approval appointments for a specified clinic
     * @param clinicId ID of the target clinic
     * @return List of pending appointment records (each as String array)
     */
    public List<String[]> getPendingApprovalAppointments(int clinicId) {
        List<String[]> appointments = new ArrayList<>();
        String query = "SELECT a.*, u.full_name as patient_name, c.clinic_name, s.service_name " +
                "FROM appointment a " +
                "JOIN user u ON a.patient_id = u.user_id " +
                "JOIN clinic c ON a.clinic_id = c.clinic_id " +
                "JOIN service s ON a.service_id = s.service_id " +
                "WHERE a.clinic_id = ? AND a.status = 'PENDING' " +
                "ORDER BY a.created_at ASC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, clinicId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] appt = {
                    String.valueOf(rs.getInt("appointment_id")),
                    String.valueOf(rs.getInt("patient_id")),
                    rs.getString("patient_name"),
                    String.valueOf(rs.getInt("clinic_id")),
                    rs.getString("clinic_name"),
                    String.valueOf(rs.getInt("service_id")),
                    rs.getString("service_name"),
                    rs.getString("appointment_date"),
                    rs.getString("appointment_time"),
                    rs.getString("status")
                };
                appointments.add(appt);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving pending appointments: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return appointments;
    }

    // ========== New: Update visit outcome method (used by VisitOutcomeServlet) ==========
    /**
     * Update appointment check-in status and visit outcome notes
     * @param appointmentId ID of the target appointment
     * @param status New visit status (ARRIVED, COMPLETED, NO_SHOW, CANCELLED)
     * @param visitOutcome Clinical notes and outcome of the visit
     * @return true if update executed successfully
     */
    public boolean updateVisitOutcome(int appointmentId, String status, String visitOutcome) {
        String query = "UPDATE appointment SET status = ?, visit_outcome = ? WHERE appointment_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, status);
            pstmt.setString(2, visitOutcome);
            pstmt.setInt(3, appointmentId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating visit outcome: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
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
     * @return true if patient added to queue successfully
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
     * Gets the next available queue number for a clinic/service/date combination
     * 
     * @param clinicId ID of the clinic
     * @param serviceId ID of the service
     * @param queueDate Date of the queue (YYYY-MM-DD)
     * @return Next queue number (starts at 1 if no existing queue entries)
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
            return 1; // Fallback to first queue number on empty result
        } catch (SQLException e) {
            System.err.println("Error getting next queue number: " + e.getMessage());
            return 1; // Fallback to 1 on error
        } finally {
            closeResources(conn, pstmt, rs);
        }
    }

    // ========== New: Get active walk-in queue list (used by StaffDashboardServlet) ==========
    /**
     * Get active walk-in queue for a clinic and service on the current date
     * @param clinicId ID of the target clinic
     * @param serviceId ID of the service (0 for all services)
     * @param queueDate Current queue date
     * @return List of walk-in queue records (each as String array)
     */
    public List<String[]> getActiveWalkInQueue(int clinicId, int serviceId, Date queueDate) {
        List<String[]> queueList = new ArrayList<>();
        StringBuilder query = new StringBuilder(
            "SELECT q.*, u.full_name as patient_name, s.service_name " +
            "FROM walkin_queue q " +
            "JOIN user u ON q.patient_id = u.user_id " +
            "JOIN service s ON q.service_id = s.service_id " +
            "WHERE q.clinic_id = ? AND q.queue_date = ? " +
            "AND q.status IN ('WAITING', 'CALLED', 'SKIPPED') "
        );
        // Add service filter if specific service is selected
        if (serviceId > 0) {
            query.append("AND q.service_id = ? ");
        }
        query.append("ORDER BY q.queue_number ASC");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setInt(1, clinicId);
            pstmt.setDate(2, queueDate);
            if (serviceId > 0) {
                pstmt.setInt(3, serviceId);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] queue = {
                    String.valueOf(rs.getInt("queue_id")),
                    String.valueOf(rs.getInt("patient_id")),
                    rs.getString("patient_name"),
                    String.valueOf(rs.getInt("clinic_id")),
                    String.valueOf(rs.getInt("service_id")),
                    rs.getString("service_name"),
                    rs.getString("queue_date"),
                    String.valueOf(rs.getInt("queue_number")),
                    rs.getString("status"),
                    rs.getString("called_time"),
                    String.valueOf(rs.getInt("estimated_wait_minutes"))
                };
                queueList.add(queue);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving active walk-in queue: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return queueList;
    }

    // ========== New: Get next waiting patient in queue (used by WalkInQueueManagementServlet) ==========
    /**
     * Get the next waiting patient in the walk-in queue
     * @param clinicId ID of the target clinic
     * @param serviceId ID of the target service
     * @param queueDate Current queue date
     * @return String array of next patient details, null if no waiting patients
     */
    public String[] getNextWaitingPatient(int clinicId, int serviceId, Date queueDate) {
        String query = "SELECT q.*, u.full_name as patient_name, s.service_name " +
                "FROM walkin_queue q " +
                "JOIN user u ON q.patient_id = u.user_id " +
                "JOIN service s ON q.service_id = s.service_id " +
                "WHERE q.clinic_id = ? AND q.service_id = ? AND q.queue_date = ? " +
                "AND q.status = 'WAITING' " +
                "ORDER BY q.queue_number ASC LIMIT 1";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, clinicId);
            pstmt.setInt(2, serviceId);
            pstmt.setDate(3, queueDate);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return new String[]{
                    String.valueOf(rs.getInt("queue_id")),
                    String.valueOf(rs.getInt("patient_id")),
                    rs.getString("patient_name"),
                    rs.getString("service_name"),
                    String.valueOf(rs.getInt("queue_number")),
                    rs.getString("status")
                };
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving next waiting patient: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }

    // ========== New: Update queue status (used by WalkInQueueManagementServlet) ==========
    /**
     * Update walk-in queue status for patient management
     * @param queueId ID of the target queue record
     * @param status New queue status (CALLED, SKIPPED, COMPLETED, EXPIRED)
     * @return true if update executed successfully
     */
    public boolean updateQueueStatus(int queueId, String status) {
        StringBuilder query = new StringBuilder("UPDATE walkin_queue SET status = ? ");
        // Set called time when status is updated to CALLED
        if (status.equals("CALLED")) {
            query.append(", called_time = CURRENT_TIME ");
        }
        // Set completed time when status is updated to COMPLETED
        if (status.equals("COMPLETED")) {
            query.append(", completed_time = CURRENT_TIME ");
        }
        query.append("WHERE queue_id = ?");

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setString(1, status);
            pstmt.setInt(2, queueId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating queue status: " + e.getMessage());
            return false;
        } finally {
            closeResources(conn, pstmt, null);
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
     * @return true if issue was reported successfully
     */
    public boolean reportOperationalIssue(int clinicId, int staffId, String issueType, String description) {
        String query = "INSERT INTO operational_issue (clinic_id, reported_by_staff_id, issue_type, issue_description, status, reported_at) " +
                       "VALUES (?, ?, ?, ?, 'OPEN', CURRENT_TIMESTAMP)";
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
     * @return true if update executed successfully
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

    // ========== New: Get operational issues by clinic (for future extension) ==========
    /**
     * Get all operational issues reported by a specified clinic
     * @param clinicId ID of the target clinic
     * @return List of operational issue records (each as String array)
     */
    public List<String[]> getIssuesByClinic(int clinicId) {
        List<String[]> issueList = new ArrayList<>();
        String query = "SELECT i.*, c.clinic_name, u.full_name as reported_by_staff_name " +
                "FROM operational_issue i " +
                "JOIN clinic c ON i.clinic_id = c.clinic_id " +
                "JOIN user u ON i.reported_by_staff_id = u.user_id " +
                "WHERE i.clinic_id = ? " +
                "ORDER BY i.reported_at DESC";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, clinicId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String[] issue = {
                    String.valueOf(rs.getInt("issue_id")),
                    String.valueOf(rs.getInt("clinic_id")),
                    rs.getString("clinic_name"),
                    String.valueOf(rs.getInt("reported_by_staff_id")),
                    rs.getString("reported_by_staff_name"),
                    rs.getString("issue_type"),
                    rs.getString("issue_description"),
                    rs.getString("status"),
                    rs.getString("reported_at"),
                    rs.getString("resolved_at"),
                    rs.getString("resolution_note")
                };
                issueList.add(issue);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving operational issues: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return issueList;
    }
}