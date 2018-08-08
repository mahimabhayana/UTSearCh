package UTSearCh;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Database.SQLiteJDBC;

@WebServlet("/userData")
public class UserDataGetter extends HttpServlet{
	
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
		resp.setContentType("application/json");

		JSONObject json = new JSONObject();
		// get params
		String name = req.getParameter("user");
		// send back the name
		json.put("Name", name);
		// split name into first and last name
		String[] nameSplit = name.split(" ");
		// get the type of user
		String type = db.getAccountTypeByName(nameSplit[0], nameSplit[1]);
		// didnt find it
		if (type == null) {
			// default type
			type = "N/A";
		}
		// add type to json
		json.put("Type", type);
		// if they're an instructor
		if (type.toLowerCase().equals("instructor")) {
			// get the course they teach
			ResultSet courses = db.getCoursesTaughtByUser(name);
			// add it to the json
			try {
				json.put("CoursesTaught", CourseDataGetter.convertToJSON(courses));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resp.getWriter().write("JSON Error");
				resp.setStatus(500);
				return;
			}
		}
		// get document the person has uploaded
		ResultSet docs = db.getUserDocs(name);
		// add it to the json obj
		try {
			json.put("Docs", CourseDataGetter.convertToJSON(docs));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.getWriter().write("JSON Error");
			resp.setStatus(500);
			return;
		}
		// get the sub of the user
		String sub = db.getSubFromName(nameSplit[0], nameSplit[1]);
		// didnt find it
		if (sub == null) {
			// 500 not found
			resp.setStatus(500);
			resp.getWriter().write("Sub not found");
			return;
		}
		// get the users courses by sub
		List<String> userCourses = db.getUserCourses(sub);
		// json array to put courses in
		JSONArray courseArray = new JSONArray();
		// go through courses
		for (String course:userCourses) {
			courseArray.put(course);
		}
		// add courses to json
		json.put("CoursesTaken", courseArray);
		// create actual json obj
		resp.getWriter().write(json.toString());
		// good response
		resp.setStatus(200);
	}

}
