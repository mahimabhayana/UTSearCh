package GoogleSignIn;

import Database.SQLiteJDBC;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    public static final SQLiteJDBC db = SQLiteJDBC.getDBReference("UTSearChDB");

    /**
     * This method intercepts a post request and logs out the google user
     *
     * @param req  the http request
     * @param resp the http response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        String email = req.getParameter("email");
        String token = req.getParameter("token");
        System.out.println("Logout email: " + email + "\nToken: " + token);
        // placeholder
        // delete email from session
        db.deleteSessionToken(token);

    }

}

