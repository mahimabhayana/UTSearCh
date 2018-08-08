package UTSearCh;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Database.SQLiteJDBC;

@WebServlet("/courseData")
public class CourseDataGetter extends HttpServlet {
	
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
		// add instructor to json
		json.put("Instructor", name);
		// get result set of instructor documents
		ResultSet rs = db.getCourseDocs(course, "Instructor");
		// get rest set of student documents
		ResultSet rs2 = db.getCourseDocs(course, "Student");
		// add it to json
		try {
			json.put("InstructorDocs", convertToJSON(rs));
			// add it to json
			json.put("StudentDocs", convertToJSON(rs2));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// 500 not found
			resp.setStatus(500);
			resp.getWriter().write("SQL error");
			return;
		} 
		// create actual json obj
		resp.getWriter().write(json.toString());
		// good response
		resp.setStatus(200);
	}
	
	/*
	 * Given a result set, converts the result set to JSON
	 * @param resultSet the set to convert
	 * @return JSONArray of the result set
	 */
	public static JSONArray convertToJSON(ResultSet resultSet) throws JSONException, SQLException {
		// new json array
        JSONArray jsonArray = new JSONArray();
        // while there is data
        while (resultSet.next()) {
        	// get rows
            int total_rows = resultSet.getMetaData().getColumnCount();
            // new obj
            JSONObject obj = new JSONObject();
            // go through rows
            for (int i = 0; i < total_rows; i++) {
            	// put data from each row
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getObject(i + 1));
            }
            // put the whole row in the array
          jsonArray.put(obj);
        }
        // return array
        return jsonArray;
    }

}
