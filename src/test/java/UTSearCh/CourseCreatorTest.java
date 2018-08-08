package UTSearCh;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import Database.SQLiteJDBC;
import Database.SQLiteJDBCTest;

public class CourseCreatorTest {
	
	SQLiteJDBC db = SQLiteJDBCTest.db2;
	
	@Test
	 public void testCreateCourseInstructor() throws ServletException, IOException {
    	db.executeQuery("INSERT INTO Accounts (sub, firstname, lastname, email, type) VALUES ('6','test','instructor','test@instructor.com', 'Instructor')");
    	db.executeQuery("INSERT INTO Sessions (sub, date, token) VALUES ('6', '2018-07-09 16:00:00','instructor_token')");
		CourseCreator creator = new CourseCreator();
		creator.db = db;
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(req.getParameter("course")).thenReturn("CSCTEST");
		when(req.getParameter("token")).thenReturn("instructor_token");
		StringWriter stringWriter = new StringWriter();
		Writer writer = new PrintWriter(stringWriter);
		when(resp.getWriter()).thenReturn((PrintWriter) writer);
		creator.doPost(req, resp);
		List<String> courses = db.getUserCourses(db.getSubFromToken("instructor_token"));
		boolean inCourses = false;
		for (String str:courses) {
			inCourses = inCourses || str.equals("CSCTEST");
		}
		assertTrue(db.getCourseInstructor("CSCTEST").equals("test instructor") && inCourses);
		db.executeQuery("delete from Course where Name = 'CSCTEST'");
		db.executeQuery("delete from StudentToCourses where Name = 'CSCTEST'");
		db.executeQuery("delete from Accounts where firstname = 'test'");
		db.executeQuery("delete from Sessions where token = 'instructor_token'");
	}
	
	@Test
	 public void testCreateCourseNotInstructor() throws ServletException, IOException {
		db.executeQuery("INSERT INTO Accounts (sub, firstname, lastname, email, type) VALUES ('4','test','student','test@student.com', 'Student')");
		db.executeQuery("INSERT INTO Sessions (sub, date, token) VALUES ('4', '2018-07-09 16:00:00','student_token')");
		CourseCreator creator = new CourseCreator();
		creator.db = db;
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(req.getParameter("course")).thenReturn("CSCTEST");
		when(req.getParameter("token")).thenReturn("student_token");
		StringWriter stringWriter = new StringWriter();
		Writer writer = new PrintWriter(stringWriter);
		when(resp.getWriter()).thenReturn((PrintWriter) writer);
		creator.doPost(req, resp);
		String output = stringWriter.toString();
		assertTrue(output.equals("access denied"));
		db.executeQuery("delete from Accounts where firstname = 'test'");
		db.executeQuery("delete from Sessions where token = 'student_token'");
	}
	
	@Test
	 public void testGetCourses() throws ServletException, IOException {
		db.executeQuery("insert into Course (Name, Instructor) values ('CSCC69','Sina Meraji')");
		db.executeQuery("insert into Course (Name, Instructor) values ('CSCC01','Abbas Attarwala')");
		db.executeQuery("insert into Course (Name, Instructor) values ('CSCC24','Anya Tafliovich')");
		CourseCreator creator = new CourseCreator();
		creator.db = db;
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		StringWriter stringWriter = new StringWriter();
		Writer writer = new PrintWriter(stringWriter);
		when(resp.getWriter()).thenReturn((PrintWriter) writer);
		creator.doGet(req, resp);
		String output = stringWriter.toString();
		assertTrue(output.equals("[\"CSCC69\",\"CSCC24\",\"CSCC01\"]"));
		db.executeQuery("delete from Course where Name like '%24' or Name like '%01' or Name like '%69'");
	}

}
