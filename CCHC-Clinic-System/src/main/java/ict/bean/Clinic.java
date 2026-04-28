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
public class Clinic implements Serializable {
    private int clinicId;
    private String clinicName;
    private String address;
    private String phone;
    private String openingHours;
    private boolean isWalkInEnabled;

    public Clinic() {}

    // Getters and Setters
    public int getClinicId() { return clinicId; }
    public void setClinicId(int clinicId) { this.clinicId = clinicId; }

    public String getClinicName() { return clinicName; }
    public void setClinicName(String clinicName) { this.clinicName = clinicName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getOpeningHours() { return openingHours; }
    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }

    public boolean isWalkInEnabled() { return isWalkInEnabled; }
    public void setWalkInEnabled(boolean walkInEnabled) { isWalkInEnabled = walkInEnabled; }
}
