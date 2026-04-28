/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.filter;

import ict.bean.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
/**
 *
 * @author User
 */
@WebFilter(filterName = "StaffAuthFilter", urlPatterns = {"/staff/*", "/staffServlet/*"})
public class StaffAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter initialization
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Check if user is logged in and has STAFF role
        boolean isLoggedIn = (session != null && session.getAttribute("currentUser") != null);
        boolean isStaff = false;

        if (isLoggedIn) {
            User currentUser = (User) session.getAttribute("currentUser");
            isStaff = "STAFF".equals(currentUser.getRole());
        }

        if (isLoggedIn && isStaff) {
            // Allow access to staff resources
            chain.doFilter(request, response);
        } else {
            // Redirect to login page if not authorized
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp?error=unauthorized_staff");
        }
    }

    @Override
    public void destroy() {
        // Filter cleanup
    }
}
