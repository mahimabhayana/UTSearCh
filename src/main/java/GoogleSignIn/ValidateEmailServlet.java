package GoogleSignIn;

import Database.SQLiteJDBC;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/verify")
public class ValidateEmailServlet extends HttpServlet {
    public static final SQLiteJDBC db = SQLiteJDBC.getDBReference("UTSearChDB");

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        // get verify token
        String token = req.getParameter("token");
        // check if this token is in EmailValidation table
        if (db.validateEmail(token)) {
            // true, so this email needs to be validated
            // create account and delete token from EmailValidation
            String sub = db.deleteValidationEntry(token);
            db.verifyAccount(sub);
            resp.sendRedirect("http://localhost:8080/");
        }
        // create an account
        //db.addAccount(sub, firstName, lastName, gmail, uoftEmail, accountType, false);
    }
}
