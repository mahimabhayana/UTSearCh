package UTSearCh;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Database.SQLiteJDBC;

@WebServlet("/courses")
public class CourseCreator extends HttpServlet{
	
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
		System.out.println(course);
		String token = req.getParameter("token");
		System.out.println(token);
		// get the users type
		String accountType = db.getAccountType(token);
		System.out.println("Account type is " + accountType);
		
		// if not the right perms
		if (!accountType.equalsIgnoreCase("Instructor")) {
		    System.out.println("Account not an instructor");
			// permission error
			resp.setStatus(403);
			// tell the user access denied
			 resp.getWriter().write("access denied");
			return;
		}
		// right type
		// get the uid of the user
		String sub = db.getSubFromToken(token);
		// use the sub to get the user's name
		String name = db.getAccountName(sub);
		// add the course to the db
		boolean worked = db.addCourse(course, name);
		// if creating the course worked
		if (worked) {
			// subscribe the instructor to their own course
			db.addUserCourse(sub, course);
			System.out.println("Added course to the db");
			// good response
			resp.setStatus(200);
		// didnt work
		} else {
		    System.out.println("Something went wrong while adding; account is instructor's");
		    resp.getWriter().write("something went wrong");
			resp.setStatus(500);
		}
		}
	
	@Override
	/*
	 * Sends back a list of courses in the database
	 * @param req The request data.
	 * @param resp The response data.
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
	         throws ServletException, IOException {
		// get list of courses
		List<String> courses = db.getCourses();
		// right back the json obj
		resp.getWriter().write(new Gson().toJson(courses));
		}

}
