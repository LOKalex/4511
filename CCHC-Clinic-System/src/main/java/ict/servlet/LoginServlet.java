import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String redirect = "/login.jsp";

        try {
            // Database connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdbname", "yourdbuser", "yourdbpassword");
            String query = "SELECT role FROM users WHERE username = ? AND password = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // User authenticated
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                String role = rs.getString("role");
                if (role.equals("admin")) {
                    redirect = "/adminDashboard.jsp";
                } else if (role.equals("user")) {
                    redirect = "/userDashboard.jsp";
                }
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect(redirect);
    }
}