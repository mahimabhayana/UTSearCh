package UTSearCh;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Database.SQLiteJDBC;

@WebServlet("/courseInstructor")
public class CourseInstructorFinder extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
		// grab the db
		SQLiteJDBC db = SQLiteJDBC.getDBReference("UTSearChDB");
	
	@Override
	/*
	 * Given a course code, returns back the course's instructor
	 * @param req The request data.
	 * @param resp The response data.
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
	         throws ServletException, IOException {
		// get params
		String course = req.getParameter("course");
		// get the uid of the user
		String name = db.getCourseInstructor(course.toUpperCase());
		// didnt find it
		if (name == null) {
			// 500 not found
			resp.setStatus(500);
			resp.getWriter().write("Not found");
			return;
		}
		// write name back to frontend
		resp.getWriter().write(name);
		// good response
		resp.setStatus(200);
		}

}
