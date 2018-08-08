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

public class CourseSubscriberTest {
	
	SQLiteJDBC db = SQLiteJDBCTest.db2;
	
	@Test
	 public void testSubscribeToCourseNotInstructor() throws ServletException, IOException {
		db.executeQuery("INSERT INTO Accounts (sub, firstname, lastname, email, type) VALUES ('4','test','student','test@student.com', 'Student')");
		db.executeQuery("INSERT INTO Sessions (sub, date, token) VALUES ('4', '2018-07-09 16:00:00','student_token')");
		CourseSubscriber sub = new CourseSubscriber();
		sub.db = db;
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(req.getParameter("course")).thenReturn("CSCTEST");
		when(req.getParameter("token")).thenReturn("student_token");
		sub.doPost(req, resp);
		List<String> courses = db.getUserCourses(db.getSubFromToken("student_token"));
		boolean inCourses = false;
		for (String str:courses) {
			inCourses = inCourses || str.equals("CSCTEST");
		}
		assertTrue(inCourses);
		db.executeQuery("delete from StudentToCourses where course = 'CSCTEST'");
		db.executeQuery("delete from Accounts where firstname = 'test'");
		db.executeQuery("delete from Sessions where token = 'student_token'");
	}
	
	@Test
	 public void testSubscribeToCourseInstructor() throws ServletException, IOException {
		db.executeQuery("INSERT INTO Accounts (sub, firstname, lastname, email, type) VALUES ('6','test','instructor','test@instructor.com', 'Instructor')");
    	db.executeQuery("INSERT INTO Sessions (sub, date, token) VALUES ('6', '2018-07-09 16:00:00','instructor_token')");
		CourseSubscriber sub = new CourseSubscriber();
		sub.db = db;
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(req.getParameter("course")).thenReturn("CSCTEST");
		when(req.getParameter("token")).thenReturn("instructor_token");
		sub.doPost(req, resp);
		List<String> courses = db.getUserCourses(db.getSubFromToken("instructor_token"));
		boolean inCourses = false;
		for (String str:courses) {
			inCourses = inCourses || str.equals("CSCTEST");
		}
		assertTrue(inCourses);
		db.executeQuery("delete from StudentToCourses where course = 'CSCTEST'");
		db.executeQuery("delete from Accounts where firstname = 'test'");
		db.executeQuery("delete from Sessions where token = 'instructor_token'");
	}

}
