import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/transfer")
public class VulnerableServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String amount = request.getParameter("amount");
        String recipient = request.getParameter("recipient");
        String user = request.getParameter("user");

        // CSRF Vulnerability (CWE-352)
        // No CSRF token validation, allowing an attacker to trick a user into making unintended requests.
        if (amount != null && recipient != null && user != null) {
            processTransfer(user, recipient, amount);
            response.getWriter().println("Transfer successful!");
        } else {
            response.getWriter().println("Invalid input!");
        }

        // Log Forgery Vulnerability (CWE-117)
        // Directly logging user input without sanitization, enabling log injection attacks.
        System.out.println("User " + user + " initiated a transfer of " + amount + " to " + recipient);
    }

    private void processTransfer(String user, String recipient, String amount) {
        // Simulated money transfer logic (vulnerable to CSRF)
        System.out.println("Processing transfer: " + user + " -> " + recipient + " : $" + amount);
    }
}
