package GoogleSignIn;

import Database.SQLiteJDBC;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

@WebServlet("/validateToken")
public class ValidateToken extends HttpServlet {
    public static final SQLiteJDBC db = SQLiteJDBC.getDBReference("UTSearChDB");

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        // get the token
        String token = req.getParameter("token");
        // check if in db
        String responseText = "invalid";
        // get current date time
        LocalDateTime now = LocalDateTime.now();
        if (db.sessionValid(token,  now.toString().substring(0, now.toString().indexOf('.')))) {
            //session exists, send valid message
            responseText = "valid";
        }

        resp.setContentLength(responseText.length());
        PrintWriter out = resp.getWriter();
        out.println(responseText);
        out.close();
        out.flush();
    }
}