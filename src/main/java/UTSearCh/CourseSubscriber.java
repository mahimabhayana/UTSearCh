package UTSearCh;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Database.SQLiteJDBC;

@WebServlet("/subscribeCourse")
public class CourseSubscriber extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	// grab the db
	SQLiteJDBC db = SQLiteJDBC.getDBReference("UTSearChDB");

	@Override
	/*
	 * Given the user's token and a course code, adds a course to the db
	 * @param req The request data.
	 * @param resp The response data.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
	         throws ServletException, IOException {
		// get params
		String course = req.getParameter("course");
		String token = req.getParameter("token");

		// get the uid of the user
		String sub = db.getSubFromToken(token);
		
		// check if the user has already enrolled in the course
		List<String> currCourses = db.getUserCourses(sub);
		if(currCourses.contains(course)) {
		  // bad response
		  resp.setStatus(403);
		} else {
  		  // add user to course
  		  boolean worked = db.addUserCourse(sub, course);
  		  if (worked) {
  			// good response
  			resp.setStatus(200);
  		} else {
  			// bad response
  			resp.setStatus(500);
  		}
		}
	}
}
