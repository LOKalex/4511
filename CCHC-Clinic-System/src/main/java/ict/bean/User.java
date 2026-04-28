/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.bean;

import java.io.Serializable;
/**
 *
 * @author User
 */
public class User implements Serializable {
    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String role; // PATIENT, STAFF, ADMIN
    private int assignedClinicId; // For clinic staff only
    private String email;
    private String phone;

    // No-arg constructor (required for JavaBean)
    public User() {}

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getAssignedClinicId() { return assignedClinicId; }
    public void setAssignedClinicId(int assignedClinicId) { this.assignedClinicId = assignedClinicId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
