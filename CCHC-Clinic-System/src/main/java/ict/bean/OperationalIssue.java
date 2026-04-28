/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.bean;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
/**
 *
 * @author User
 */
public class OperationalIssue implements Serializable {
    private int issueId;
    private int clinicId;
    private String clinicName;
    private int reportedByStaffId;
    private String reportedByStaffName;
    private String issueType; // DOCTOR_UNAVAILABLE, SERVICE_SUSPENDED, EQUIPMENT_FAILURE, OTHER
    private String issueDescription;
    private String status; // OPEN, IN_PROGRESS, RESOLVED, CLOSED
    private Timestamp reportedAt;
    private Date resolvedAt;
    private String resolutionNote;

    // No-arg constructor
    public OperationalIssue() {}

    // Getters and Setters
    public int getIssueId() { return issueId; }
    public void setIssueId(int issueId) { this.issueId = issueId; }

    public int getClinicId() { return clinicId; }
    public void setClinicId(int clinicId) { this.clinicId = clinicId; }

    public String getClinicName() { return clinicName; }
    public void setClinicName(String clinicName) { this.clinicName = clinicName; }

    public int getReportedByStaffId() { return reportedByStaffId; }
    public void setReportedByStaffId(int reportedByStaffId) { this.reportedByStaffId = reportedByStaffId; }

    public String getReportedByStaffName() { return reportedByStaffName; }
    public void setReportedByStaffName(String reportedByStaffName) { this.reportedByStaffName = reportedByStaffName; }

    public String getIssueType() { return issueType; }
    public void setIssueType(String issueType) { this.issueType = issueType; }

    public String getIssueDescription() { return issueDescription; }
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getReportedAt() { return reportedAt; }
    public void setReportedAt(Timestamp reportedAt) { this.reportedAt = reportedAt; }

    public Date getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Date resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getResolutionNote() { return resolutionNote; }
    public void setResolutionNote(String resolutionNote) { this.resolutionNote = resolutionNote; }
}
