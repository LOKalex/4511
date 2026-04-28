/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
/**
 *
 * @author User
 */
public class AppointmentStatusTag extends SimpleTagSupport {
    private String status;

    // Setter for status attribute (called by JSP container)
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        String badgeClass = "";
        String displayText = status;

        // Map status to Bootstrap badge style and display text
        switch (status) {
            case "PENDING":
                badgeClass = "badge bg-warning text-dark";
                displayText = "Pending Approval";
                break;
            case "APPROVED":
                badgeClass = "badge bg-success";
                displayText = "Approved";
                break;
            case "REJECTED":
                badgeClass = "badge bg-danger";
                displayText = "Rejected";
                break;
            case "ARRIVED":
                badgeClass = "badge bg-info text-dark";
                displayText = "Arrived";
                break;
            case "COMPLETED":
                badgeClass = "badge bg-primary";
                displayText = "Completed";
                break;
            case "NO_SHOW":
                badgeClass = "badge bg-secondary";
                displayText = "No Show";
                break;
            case "CANCELLED":
                badgeClass = "badge bg-dark";
                displayText = "Cancelled";
                break;
            default:
                badgeClass = "badge bg-light text-dark";
                break;
        }

        // Output the formatted HTML
        out.print("<span class=\"" + badgeClass + "\">" + displayText + "</span>");
    }
}
