import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/vulnerable")
public class VulnerableServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // CWE-352: Missing CSRF protection
        String action = request.getParameter("action");
        if ("deleteAccount".equals(action)) {
            // Simulate account deletion (vulnerable)
            String accountId = request.getParameter("accountId");
            if(accountId != null && !accountId.isEmpty()){
                out.println("Account " + accountId + " deleted!"); //CWE-117. Log injection
            } else {
                out.println("Account ID missing.");
            }

        } else if ("showForm".equals(action)) {
            // Display a form (no CSRF token)
            out.println("<form action=\"vulnerable?action=deleteAccount\" method=\"GET\">");
            out.println("Account ID: <input type=\"text\" name=\"accountId\">");
            out.println("<input type=\"submit\" value=\"Delete Account\">");
            out.println("</form>");

        } else {
            out.println("<a href=\"vulnerable?action=showForm\">Delete Account</a>");
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // CWE-117: Improper Output Neutralization for Logs
        String userInput = request.getParameter("userInput");
        if (userInput != null) {
            System.out.println("User input: " + userInput); // Vulnerable log statement
            out.println("Logged user input.");
        } else {
            out.println("No input received.");
        }
    }
}
