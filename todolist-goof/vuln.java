import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/vulnerable2")
public class VulnerableServlet2 extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");

        if ("transfer".equals(action)) {
            // CWE-352: Missing CSRF protection
            String fromAccount = request.getParameter("from");
            String toAccount = request.getParameter("to");
            String amount = request.getParameter("amount");

            if (fromAccount != null && toAccount != null && amount != null) {
                // Simulate transfer (vulnerable)
                out.println("Transfer of " + amount + " from " + fromAccount + " to " + toAccount + " initiated!");
                System.out.println("Transfer: From=" + fromAccount + ", To=" + toAccount + ", Amount=" + amount); //CWE-117
            } else {
                out.println("Missing transfer parameters.");
            }
        } else if ("showTransferForm".equals(action)) {
            out.println("<form action=\"vulnerable2?action=transfer\" method=\"GET\">");
            out.println("From Account: <input type=\"text\" name=\"from\"><br>");
            out.println("To Account: <input type=\"text\" name=\"to\"><br>");
            out.println("Amount: <input type=\"text\" name=\"amount\"><br>");
            out.println("<input type=\"submit\" value=\"Transfer\">");
            out.println("</form>");
        } else {
            out.println("<a href=\"vulnerable2?action=showTransferForm\">Transfer Funds</a>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // CWE-117: Improper Output Neutralization for Logs (and potential XSS if reflected)
        String userComment = request.getParameter("comment");
        if (userComment != null) {
            System.out.println("User Comment: " + userComment); // Vulnerable log statement
            out.println("Comment received: " + userComment); //Also vulnerable to XSS if not properly escaped.
        } else {
            out.println("No comment received.");
        }
    }
}
