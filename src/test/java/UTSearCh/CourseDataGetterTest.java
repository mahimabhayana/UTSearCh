package UTSearCh;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.junit.Test;

import Database.SQLiteJDBC;
import Database.SQLiteJDBCTest;

public class CourseDataGetterTest {
	
	SQLiteJDBC db = SQLiteJDBCTest.db2;
	
	@Test
	 public void testGetCourseData() throws ServletException, IOException {
		db.executeQuery("insert into Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime) values ('y','y.html','Instructor','TEST','TEST','CSCC69','Testing Tester','1','2018-07-27 16:59:14')");
		db.executeQuery("insert into Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime) values ('h','h.txt','Student','TEST','TEST','CSCC01','Test Tester','1','2018-07-27 16:59:14')");
		db.executeQuery("insert into Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime) values ('t','t.pdf','Student','TEST','TEST','CSCC69','Test Tester','1','2018-07-27 16:59:14')");
		db.executeQuery("insert into Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime) values ('g','g.html','Instructor','TEST','TEST','CSCC24','Testing Tester','1','2018-07-27 16:59:14')");
		db.executeQuery("insert into Course (Name, Instructor) values ('CSCC69','Testing Tester')");
		db.executeQuery("insert into Accounts (sub, firstname, lastname, email, type) values ('43333', 'Test', 'Tester', 'testing@test.com', 'Student')");
    	db.executeQuery("insert into Accounts (sub, firstname, lastname, email, type) values ('4443333', 'Testing', 'Tester', 'testing@test.com', 'Instructor')");
		CourseDataGetter getter = new CourseDataGetter();
		getter.db = db;
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(req.getParameter("course")).thenReturn("CSCC69");
		StringWriter stringWriter = new StringWriter();
		Writer writer = new PrintWriter(stringWriter);
		when(resp.getWriter()).thenReturn((PrintWriter) writer);
		getter.doGet(req, resp);
		String output = stringWriter.toString();
		assertTrue(output.contains("y.html") && output.contains("t.pdf") && !output.contains("h.txt") 
				&& !output.contains("g.html") && !output.contains("[]") && output.contains("Testing Tester"));
		db.executeQuery("delete from Course where Instructor = 'Testing Tester'");
		db.executeQuery("delete from Documents where DocType = 'TEST'");
		db.executeQuery("delete from Accounts where email = 'testing@test.com'");
	}
	
	@Test
	 public void testGetCourseDataCourseNotExisting() throws ServletException, IOException {
		CourseDataGetter getter = new CourseDataGetter();
		getter.db = db;
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(req.getParameter("course")).thenReturn("CSCNOTAREALCOURSE");
		StringWriter stringWriter = new StringWriter();
		Writer writer = new PrintWriter(stringWriter);
		when(resp.getWriter()).thenReturn((PrintWriter) writer);
		getter.doGet(req, resp);
		String output = stringWriter.toString();
		assertTrue(output.equals("Not found"));
	}
	
	@Test
	 public void testJSONConvert() throws ServletException, IOException, JSONException, SQLException {
		CourseDataGetter getter = new CourseDataGetter();
		db.executeQuery("insert into Course (Name, Instructor) values ('CSCC69','Testing Tester')");
		db.executeQuery("insert into Course (Name, Instructor) values ('CSCC24','Testing Tester')");
		db.executeQuery("insert into Course (Name, Instructor) values ('CSCC73','Testing Tester')");
		ResultSet rs = db.getCoursesTaughtByUser("Testing Tester");
		String test = getter.convertToJSON(rs).toString();
		assertTrue(test.equals("[{\"name\":\"CSCC69\"},{\"name\":\"CSCC24\"},{\"name\":\"CSCC73\"}]"));
		db.executeQuery("delete from Course where Instructor = 'Testing Tester'");
	}
	
	@Test
	 public void testJSONConvertEmpty() throws ServletException, IOException, JSONException, SQLException {
		CourseDataGetter getter = new CourseDataGetter();
		ResultSet rs = db.getCoursesTaughtByUser("Testing Tester");
		String test = getter.convertToJSON(rs).toString();
		assertTrue(test.equals("[]"));
	}

}
