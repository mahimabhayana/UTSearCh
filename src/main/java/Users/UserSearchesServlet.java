package Users;

import Database.SQLiteJDBC;
import GoogleSignIn.LoginServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet("/usersearch")
public class UserSearchesServlet extends HttpServlet {
    public SQLiteJDBC db = LoginServlet.db;

    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        // get token
        String token = req.getParameter("token");
        if (token != null) {
            // get sub
            String sub = db.getSubFromToken(token);
            String action = req.getParameter("action");
            boolean accountExists = false;
            if (sub != null) {
                // check if account exists
                accountExists = db.accountExists(sub);
            }
            if (accountExists && action.equals("set")) {
                String queryUrl = req.getParameter("queryUrl");
                // get match and checkedBox
                String match = req.getParameter("match");
                String checkedBox = req.getParameter("checkedBox");
                String reconstructed = queryUrl + "&match=" + match + "&checkedBox=" + checkedBox;
                System.out.println(reconstructed);
                // set status to 200
                resp.setStatus(HttpServletResponse.SC_OK);
                // get current date time
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-DD HH:MM:SS");
                LocalDateTime now = LocalDateTime.now();
                // add to database
                db.addUserQuery(sub, reconstructed, now.toString().split(Pattern.quote("."))[0].trim().replaceAll("T", " "));
                // response text
                String responseText = "Added user's query to database!";
                resp.setContentLength(responseText.length());
                PrintWriter out = resp.getWriter();
                out.println(responseText);
                out.close();
                out.flush();
                System.out.println("Query added");

            }
            else if (accountExists && action.equals("get")) {
                // get the users latest 5 searches from database
                List<String> queries = new ArrayList<String>();
                queries = db.getUserQuery(sub, 100);
                String responseText = "";
                // construct a string to send back in format
                // query1;query2;query3
                for(String s:queries) {
                    responseText = responseText + s + ";";

                }
                if (responseText.length() >0 ) {
                    responseText = responseText.substring(0, responseText.length() - 1);
                }
                //System.out.println("Searches: " + responseText);
                resp.setContentLength(responseText.length());
                PrintWriter out = resp.getWriter();
                out.println(responseText);
                out.close();
                out.flush();
                System.out.println("Sent user's queries!");
                //System.out.println(responseText);

            } else {
                // email does not exists
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }


    }
    /**
     * Set the database to connect to the database given
     * @param db the new database to connect to
     */
    public void setDb(SQLiteJDBC db) {
        this.db = db;
    }
}
