package UTSearCh;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import Database.SQLiteJDBC;
import Database.SQLiteJDBCTest;

public class CrawlerCallerTest {
	//SQLiteJDBC db = SQLiteJDBCTest.db2;
	
	/*
	Commenting out this test since it takes a long time when running mvn clean install,
	can run it separately to test the class if need be.
	
	@Test
	 public void testCrawlerCaller() throws ServletException, IOException {
		HttpServletResponse resp = mock(HttpServletResponse.class);
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getParameter("path")).thenReturn("https://www.utsc.utoronto.ca/~bretscher/a20/");
		when(req.getParameter("user")).thenReturn("JUnit Testing");
		new CrawlerCaller().doGet(req, resp);
		ArrayList<File> files = new ArrayList<>();
		files.add(new File("data/a2.pdf"));
		files.add(new File("data/assignments.html"));
		files.add(new File("data/calendar.html"));
		files.add(new File("data/e2.txt"));
		files.add(new File("data/e4.txt"));
		files.add(new File("data/e6_soln.txt"));
		files.add(new File("data/grades.txt"));
		files.add(new File("data/index.html"));
		files.add(new File("data/lectures.html"));
		files.add(new File("data/news.html"));
		files.add(new File("data/resources.html"));
		files.add(new File("data/slideshowdemo.html"));
		files.add(new File("data/starter.txt"));
		files.add(new File("data/tests.html"));
		files.add(new File("data/www.utsc.utoronto.html"));
		files.add(new File("labs.html"));
		boolean ret = true;
		for (File file: files) {
			ret = ret && file.exists();
		}
		assertTrue(ret);
		for (File file: files) {
			file.delete();
		}
		db.executeQuery("DELETE FROM Documents where Uploader = 'JUnit Testing'");
	}
	*/
	
}