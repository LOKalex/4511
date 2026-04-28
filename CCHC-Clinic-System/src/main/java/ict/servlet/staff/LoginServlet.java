/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ict.servlet.staff;

import ict.bean.User;
import ict.db.cchc_clinic;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        cchc_clinic db = new cchc_clinic("jdbc:mysql://localhost:3306/cchc_clinic", "root", "password");
        String[] userRecord = db.getUserByUsername(username);
        
        if (userRecord != null && userRecord[2].equals(password)) {
            // Password matches
            User user = new User();
            user.setUserId(Integer.parseInt(userRecord[0]));
            user.setUsername(userRecord[1]);
            user.setFullName(userRecord[3]);
            user.setRole(userRecord[4]);
            
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);
            
            // Redirect based on role
            if ("STAFF".equals(userRecord[4])) {
                response.sendRedirect(request.getContextPath() + "/staff/dashboard");
            } else if ("ADMIN".equals(userRecord[4])) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/patient/dashboard");
            }
        } else {
            // Login failed
            request.setAttribute("errorMsg", "Invalid username or password");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
