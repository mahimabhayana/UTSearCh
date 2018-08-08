package UTSearCh;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

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

public class UserDataGetterTest {
	
	SQLiteJDBC db = SQLiteJDBCTest.db2;
	
	@Test
	 public void testGetUserData() throws ServletException, IOException {
		db.executeQuery("insert into Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime) values ('y','y.html','Instructor','TEST','TEST','CSCC69','Testing Tester','1','2018-07-27 16:59:14')");
		db.executeQuery("insert into Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime) values ('h','h.txt','Student','TEST','TEST','CSCC01','Test Tester','1','2018-07-27 16:59:14')");
		db.executeQuery("insert into Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime) values ('t','t.pdf','Student','TEST','TEST','CSCC69','Test Tester','1','2018-07-27 16:59:14')");
		db.executeQuery("insert into Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime) values ('g','g.html','Instructor','TEST','TEST','CSCC24','Testing Tester','1','2018-07-27 16:59:14')");
		db.executeQuery("insert into Course (Name, Instructor) values ('CSCC69','Testing Tester')");
		db.executeQuery("insert into Course (Name, Instructor) values ('CSCC01','Testing Tester')");
		db.executeQuery("insert into Course (Name, Instructor) values ('CSCC24','Testing Tester')");
		db.executeQuery("insert into Accounts (sub, firstname, lastname, email, type) values ('43333', 'Test', 'Tester', 'testing@test.com', 'Student')");
    	db.executeQuery("insert into Accounts (sub, firstname, lastname, email, type) values ('4443333', 'Testing', 'Tester', 'testing@test.com', 'Instructor')");
    	db.executeQuery("insert into StudentToCourses (sub, course) values ('43333','CSCC01')");
    	db.executeQuery("insert into StudentToCourses (sub, course) values ('43333','CSCC69')");
    	db.executeQuery("insert into StudentToCourses (sub, course) values ('4443333','CSCC01')");
    	db.executeQuery("insert into StudentToCourses (sub, course) values ('4443333','CSCC69')");
    	db.executeQuery("insert into StudentToCourses (sub, course) values ('4443333','CSCC24')");
		UserDataGetter getter = new UserDataGetter();
		getter.db = db;
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(req.getParameter("user")).thenReturn("Testing Tester");
		StringWriter stringWriter = new StringWriter();
		Writer writer = new PrintWriter(stringWriter);
		when(resp.getWriter()).thenReturn((PrintWriter) writer);
		getter.doGet(req, resp);
		String output = stringWriter.toString();
		assertTrue(output.equals("{\"Type\":\"Instructor\",\"CoursesTaught\":[{\"name\":\"CSCC69\"},{\"name\":\"CSCC01\"},"
				+ "{\"name\":\"CSCC24\"}],\"CoursesTaken\":[\"CSCC01\",\"CSCC69\",\"CSCC24\"],\"Docs\":[{\"doctype\":\"TEST\",\"filetype\":\"TEST\","
				+ "\"sub\":\"1\",\"datetime\":\"2018-07-27 16:59:14\",\"filename\":\"y.html\",\"uploadertype\":\"Instructor\",\"uploader\":\"Testing Tester\","
				+ "\"name\":\"y\",\"course\":\"CSCC69\"},{\"doctype\":\"TEST\",\"filetype\":\"TEST\",\"sub\":\"1\",\"datetime\":\"2018-07-27 16:59:14\",\"filename\""
				+ ":\"g.html\",\"uploadertype\":\"Instructor\",\"uploader\":\"Testing Tester\",\"name\":\"g\",\"course\":\"CSCC24\"}],\"Name\":\"Testing Tester\"}"));
		db.executeQuery("delete from Course where Instructor = 'Testing Tester'");
		db.executeQuery("delete from Documents where DocType = 'TEST'");
		db.executeQuery("delete from Accounts where email = 'testing@test.com'");
		db.executeQuery("delete from StudentToCourses where sub = '43333' or sub = '4443333'");
	}
	
	@Test
	 public void testGetUserData2() throws ServletException, IOException {
		db.executeQuery("insert into Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime) values ('y','y.html','Instructor','TEST','TEST','CSCC69','Testing Tester','1','2018-07-27 16:59:14')");
		db.executeQuery("insert into Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime) values ('h','h.txt','Student','TEST','TEST','CSCC01','Test Tester','1','2018-07-27 16:59:14')");
		db.executeQuery("insert into Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime) values ('t','t.pdf','Student','TEST','TEST','CSCC69','Test Tester','1','2018-07-27 16:59:14')");
		db.executeQuery("insert into Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime) values ('g','g.html','Instructor','TEST','TEST','CSCC24','Testing Tester','1','2018-07-27 16:59:14')");
		db.executeQuery("insert into Course (Name, Instructor) values ('CSCC69','Testing Tester')");
		db.executeQuery("insert into Course (Name, Instructor) values ('CSCC01','Testing Tester')");
		db.executeQuery("insert into Course (Name, Instructor) values ('CSCC24','Testing Tester')");
		db.executeQuery("insert into Accounts (sub, firstname, lastname, email, type) values ('43333', 'Test', 'Tester', 'testing@test.com', 'Student')");
	   	db.executeQuery("insert into Accounts (sub, firstname, lastname, email, type) values ('4443333', 'Testing', 'Tester', 'testing@test.com', 'Instructor')");
	   	db.executeQuery("insert into StudentToCourses (sub, course) values ('43333','CSCC01')");
	   	db.executeQuery("insert into StudentToCourses (sub, course) values ('43333','CSCC69')");
	   	db.executeQuery("insert into StudentToCourses (sub, course) values ('4443333','CSCC01')");
	   	db.executeQuery("insert into StudentToCourses (sub, course) values ('4443333','CSCC69')");
	   	db.executeQuery("insert into StudentToCourses (sub, course) values ('4443333','CSCC24')");
		UserDataGetter getter = new UserDataGetter();
		getter.db = db;
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(req.getParameter("user")).thenReturn("Test Tester");
		StringWriter stringWriter = new StringWriter();
		Writer writer = new PrintWriter(stringWriter);
		when(resp.getWriter()).thenReturn((PrintWriter) writer);
		getter.doGet(req, resp);
		String output = stringWriter.toString();
		assertTrue(output.equals("{\"Type\":\"Student\",\"CoursesTaken\":[\"CSCC01\",\"CSCC69\"],\"Docs\":[{\"doctype\":\"TEST\","
				+ "\"filetype\":\"TEST\",\"sub\":\"1\",\"datetime\":\"2018-07-27 16:59:14\",\"filename\":\"h.txt\",\"uploadertype\":"
				+ "\"Student\",\"uploader\":\"Test Tester\",\"name\":\"h\",\"course\":\"CSCC01\"},{\"doctype\":\"TEST\",\"filetype\":"
				+ "\"TEST\",\"sub\":\"1\",\"datetime\":\"2018-07-27 16:59:14\",\"filename\":\"t.pdf\",\"uploadertype\":\"Student\",\"uploader\""
				+ ":\"Test Tester\",\"name\":\"t\",\"course\":\"CSCC69\"}],\"Name\":\"Test Tester\"}"));
		db.executeQuery("delete from Course where Instructor = 'Testing Tester'");
		db.executeQuery("delete from Documents where DocType = 'TEST'");
		db.executeQuery("delete from Accounts where email = 'testing@test.com'");
		db.executeQuery("delete from StudentToCourses where sub = '43333' or sub = '4443333'");
	}
	
	@Test
	 public void testGetUserDataNotExisting() throws ServletException, IOException {
		UserDataGetter getter = new UserDataGetter();
		getter.db = db;
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(req.getParameter("user")).thenReturn("NOTA REALUSER");
		StringWriter stringWriter = new StringWriter();
		Writer writer = new PrintWriter(stringWriter);
		when(resp.getWriter()).thenReturn((PrintWriter) writer);
		getter.doGet(req, resp);
		String output = stringWriter.toString();
		assertTrue(output.equals("Sub not found"));
	}

}
