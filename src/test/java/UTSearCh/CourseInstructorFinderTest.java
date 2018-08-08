package UTSearCh;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import Database.SQLiteJDBC;
import Database.SQLiteJDBCTest;

public class CourseInstructorFinderTest {
	
	SQLiteJDBC db = SQLiteJDBCTest.db2;
	
	@Test
	 public void testFindInstructor() throws ServletException, IOException {
		db.executeQuery("insert into Course (Name, Instructor) values ('CSCTEST','Test Prof')");
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(req.getParameter("course")).thenReturn("CSCTEST");
		StringWriter stringWriter = new StringWriter();
		Writer writer = new PrintWriter(stringWriter);
		when(resp.getWriter()).thenReturn((PrintWriter) writer);
		CourseInstructorFinder finder = new CourseInstructorFinder();
		finder.db = db;
		finder.doGet(req, resp);
		String output = stringWriter.toString();
		assertTrue(output.equals("Test Prof"));
		db.executeQuery("delete from Course where Name = 'CSCTEST'");
	}
	
	@Test
	 public void testFindInstructorCourseNotPresent() throws ServletException, IOException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(req.getParameter("course")).thenReturn("CSCTEST");
		StringWriter stringWriter = new StringWriter();
		Writer writer = new PrintWriter(stringWriter);
		when(resp.getWriter()).thenReturn((PrintWriter) writer);
		CourseInstructorFinder finder = new CourseInstructorFinder();
		finder.db = db;
		finder.doGet(req, resp);
		String output = stringWriter.toString();
		assertTrue(output.equals("Not found"));
	}

}
