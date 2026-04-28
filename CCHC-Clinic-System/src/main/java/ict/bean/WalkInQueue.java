/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.bean;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
/**
 *
 * @author User
 */
public class WalkInQueue implements Serializable {
    private int queueId;
    private int patientId;
    private String patientName;
    private int clinicId;
    private int serviceId;
    private String serviceName;
    private Date queueDate;
    private int queueNumber;
    private String status; // WAITING, CALLED, SKIPPED, COMPLETED, EXPIRED
    private Time calledTime;
    private Time completedTime;
    private int estimatedWaitMinutes;

    // No-arg constructor
    public WalkInQueue() {}

    // Getters and Setters
    public int getQueueId() { return queueId; }
    public void setQueueId(int queueId) { this.queueId = queueId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public int getClinicId() { return clinicId; }
    public void setClinicId(int clinicId) { this.clinicId = clinicId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public Date getQueueDate() { return queueDate; }
    public void setQueueDate(Date queueDate) { this.queueDate = queueDate; }

    public int getQueueNumber() { return queueNumber; }
    public void setQueueNumber(int queueNumber) { this.queueNumber = queueNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Time getCalledTime() { return calledTime; }
    public void setCalledTime(Time calledTime) { this.calledTime = calledTime; }

    public Time getCompletedTime() { return completedTime; }
    public void setCompletedTime(Time completedTime) { this.completedTime = completedTime; }

    public int getEstimatedWaitMinutes() { return estimatedWaitMinutes; }
    public void setEstimatedWaitMinutes(int estimatedWaitMinutes) { this.estimatedWaitMinutes = estimatedWaitMinutes; }
}
