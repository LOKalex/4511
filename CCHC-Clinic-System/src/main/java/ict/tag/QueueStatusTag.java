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
public class QueueStatusTag extends SimpleTagSupport {
    private String status;

    // Setter for status attribute
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        String badgeClass = "";
        String displayText = status;

        // Map queue status to style and display text
        switch (status) {
            case "WAITING":
                badgeClass = "badge bg-warning text-dark";
                displayText = "Waiting";
                break;
            case "CALLED":
                badgeClass = "badge bg-primary";
                displayText = "Called";
                break;
            case "SKIPPED":
                badgeClass = "badge bg-secondary";
                displayText = "Skipped";
                break;
            case "COMPLETED":
                badgeClass = "badge bg-success";
                displayText = "Completed";
                break;
            case "EXPIRED":
                badgeClass = "badge bg-danger";
                displayText = "Expired";
                break;
            default:
                badgeClass = "badge bg-light text-dark";
                break;
        }

        // Output formatted HTML
        out.print("<span class=\"" + badgeClass + "\">" + displayText + "</span>");
    }
}
