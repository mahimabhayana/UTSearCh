package Users;

import Database.SQLiteJDBC;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


@WebServlet("/userdocuments")
public class UserDocuments extends HttpServlet {

    SQLiteJDBC db = SQLiteJDBC.getDBReference("UTSearChDB");

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        String token = req.getParameter("token");
        String sub = db.getSubFromToken(token);

        System.out.println("Requested user documents from: " + sub);
        boolean accountExists = false;
        if (sub != null) {
            // check if account exists
            accountExists = db.accountExists(sub);
        }
        if (accountExists) {
            // account exists
            resp.setStatus(HttpServletResponse.SC_OK);
            // get user's documents
            String responseText = db.getUserDocumentsEfficient(sub);
            // send to user
            // response text
            if (responseText != null) {
                resp.setContentLength(responseText.length());
                resp.getWriter().write(responseText);
                System.out.println("Sent " + sub + "'s documents data");
            }
        }
    }

    public void setDb(SQLiteJDBC db) {
        this.db = db;
    }
}
