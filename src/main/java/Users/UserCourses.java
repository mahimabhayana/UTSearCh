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
import org.json.*;


@WebServlet("/usercourses")
public class UserCourses extends HttpServlet {

    SQLiteJDBC db = SQLiteJDBC.getDBReference("UTSearChDB");

    @Override
    protected void doGet (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        // get token and sub
        String token = req.getParameter("token");
        String sub = db.getSubFromToken(token);
        System.out.println("Requested user courses from: " + sub);
        boolean accountExists = false;

        if (sub != null) {
            // check if account exists
            accountExists = db.accountExists(sub);
        }

        if (accountExists) {
            JSONObject results = new JSONObject();
            JSONArray courses = new JSONArray();

            // set status to 200
            resp.setStatus(HttpServletResponse.SC_OK);
            // get courses
            List<String> userCourses = db.getUserCourses(sub);
            for(String course : userCourses) {
                courses.put(course);
            }

            results.put("courses", courses);
            String response = results.toString();
            // response text
            resp.setContentLength(response.length());
            resp.getWriter().write(response);
            System.out.println("Sent " + sub + "'s course data");
        }
    }

    public void setTestDb(SQLiteJDBC db) {
        this.db = db;
    }

    public String userCourses(String sub) {
        // get courses
        List<String> courses = db.getUserCourses(sub);
        String courseString = "";
        for(String course : courses) {
            courseString = courseString + course + ",";
        }
        if (courseString.length() > 0 ) {
            courseString = courseString.substring(0, courseString.length() - 1);
        }

        return courseString;
    }
}
