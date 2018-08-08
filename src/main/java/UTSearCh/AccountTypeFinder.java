package UTSearCh;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Database.SQLiteJDBC;

@WebServlet("/accountType")
public class AccountTypeFinder extends HttpServlet{
    
    private static final long serialVersionUID = 1L;
    // grab the db
    SQLiteJDBC db = SQLiteJDBC.getDBReference("UTSearChDB");
    
    @Override
    /*
     * Sends back a list of courses in the database
     * @param req The request data.
     * @param resp The response data.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) 
             throws ServletException, IOException {
        // get the token
        String token = req.getParameter("token");
        // get the type
        String type = db.getAccountType(token);
        System.out.println("User's account type is " + type);
        // check failed
        if (type.equals("failed")) {
            // write failed back to user
            resp.getWriter().write("failed");
            // server error
            resp.setStatus(500);
        } else {
            if(type.equals("Instructor")) {
              // write back instructor
              resp.getWriter().write("Instructor");
              // good status
              resp.setStatus(200);
            } else {
              resp.getWriter().write("");
              resp.setStatus(500);
            }
            
        }
        }

}
