package UTSearCh;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

public class QueryTest {
	
	@Test
	public void testQueryMatchesExact() throws IOException, ServletException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getParameter("query")).thenReturn("test pdf");
		when(req.getParameter("match")).thenReturn("Exact Match");
		when(req.getParameter("checkedBox")).thenReturn("Test File");
		File file = new File("queryTest.txt");
		PrintWriter writer = new PrintWriter(file);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(resp.getWriter()).thenReturn(writer);
		Query query = new Query();
		query.doGet(req, resp);
		HtmlTxtFileParser parser = new HtmlTxtFileParser();
		writer.close();
		String output = parser.parseDoc(file);
		file.delete();
		assertTrue(output.contains("test pdf"));
	}
	
	@Test
	public void testQueryNoMatch() throws IOException, ServletException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getParameter("query")).thenReturn("test pdf");
		when(req.getParameter("match")).thenReturn("No Match");
		when(req.getParameter("checkedBox")).thenReturn("Test File");
		File file = new File("queryTest.txt");
		PrintWriter writer = new PrintWriter(file);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(resp.getWriter()).thenReturn(writer);
		Query query = new Query();
		query.doGet(req, resp);
		HtmlTxtFileParser parser = new HtmlTxtFileParser();
		writer.close();
		String output = parser.parseDoc(file);
		file.delete();
		output = output.replaceAll("\"query\":\"test pdf\",", "");
		assertTrue(!output.contains("test") && !output.contains("pdf"));
	}
	
	@Test
	public void testQueryWordMatch() throws IOException, ServletException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getParameter("query")).thenReturn("test html txt");
		when(req.getParameter("match")).thenReturn("Word Match");
		when(req.getParameter("checkedBox")).thenReturn("Test File");
		File file = new File("queryTest.txt");
		PrintWriter writer = new PrintWriter(file);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(resp.getWriter()).thenReturn(writer);
		Query query = new Query();
		query.doGet(req, resp);
		HtmlTxtFileParser parser = new HtmlTxtFileParser();
		writer.close();
		String output = parser.parseDoc(file);
		file.delete();
		output = output.replaceAll("\"query\":\"test html txt\",", "");
		assertTrue(output.contains("test") && output.contains("html") && output.contains("txt"));
	}
	@Test
	public void testQueryWordMatch2() throws IOException, ServletException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getParameter("query")).thenReturn("test html");
		when(req.getParameter("match")).thenReturn("Word Match");
		when(req.getParameter("checkedBox")).thenReturn("Test File");
		File file = new File("queryTest.html");
		PrintWriter writer = new PrintWriter(file);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(resp.getWriter()).thenReturn(writer);
		Query query = new Query();
		query.doGet(req, resp);
		HtmlTxtFileParser parser = new HtmlTxtFileParser();
		writer.close();
		String output = parser.parseDoc(file);
		file.delete();
		output = output.replaceAll("\"query\":\"test html\",", "");
		assertTrue(output.contains("test") && output.contains("html"));
	}
	@Test
	public void testQueryUserMatch() throws IOException, ServletException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getParameter("query")).thenReturn("test html pdf");
		when(req.getParameter("match")).thenReturn("Word Match");
		when(req.getParameter("checkedBox")).thenReturn("Test File");
		when(req.getParameter("user")).thenReturn("Vincent Landolfi");
		File file = new File("queryTest.txt");
		PrintWriter writer = new PrintWriter(file);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(resp.getWriter()).thenReturn(writer);
		Query query = new Query();
		query.doGet(req, resp);
		HtmlTxtFileParser parser = new HtmlTxtFileParser();
		writer.close();
		String output = parser.parseDoc(file);
		file.delete();
		output = output.replaceAll("\"query\":\"test html pdf\",", "");
		assertTrue(output.contains("test") && output.contains("html") && !output.contains("pdf") 
				&& output.contains("CSCC01") && output.contains("CSCC43"));
	}
	@Test
	public void testQueryUserMatch2() throws IOException, ServletException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getParameter("query")).thenReturn("test html pdf");
		when(req.getParameter("match")).thenReturn("Word Match");
		when(req.getParameter("checkedBox")).thenReturn("Test File");
		when(req.getParameter("user")).thenReturn("Jason Zhong");
		File file = new File("queryTest.txt");
		PrintWriter writer = new PrintWriter(file);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(resp.getWriter()).thenReturn(writer);
		Query query = new Query();
		query.doGet(req, resp);
		HtmlTxtFileParser parser = new HtmlTxtFileParser();
		writer.close();
		String output = parser.parseDoc(file);
		file.delete();
		output = output.replaceAll("\"query\":\"test html pdf\",", "");
		assertTrue(output.contains("test") && !output.contains("html") && output.contains("pdf") && output.contains("CSCC69"));
	}
	@Test
	public void testQueryUserNoMatch() throws IOException, ServletException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getParameter("query")).thenReturn("test html pdf");
		when(req.getParameter("match")).thenReturn("Word Match");
		when(req.getParameter("checkedBox")).thenReturn("Test File");
		when(req.getParameter("user")).thenReturn("Johnny Testman");
		File file = new File("queryTest.txt");
		PrintWriter writer = new PrintWriter(file);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(resp.getWriter()).thenReturn(writer);
		Query query = new Query();
		query.doGet(req, resp);
		HtmlTxtFileParser parser = new HtmlTxtFileParser();
		writer.close();
		String output = parser.parseDoc(file);
		file.delete();
		output = output.replaceAll("\"query\":\"test html pdf\",", "");
		assertTrue(!output.contains("test") && !output.contains("html") && !output.contains("pdf"));
	}
	@Test
	public void testQueryCourse() throws IOException, ServletException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getParameter("query")).thenReturn("test html pdf");
		when(req.getParameter("match")).thenReturn("Word Match");
		when(req.getParameter("checkedBox")).thenReturn("Test File");
		when(req.getParameter("course")).thenReturn("CSCC01");
		File file = new File("queryTest.txt");
		PrintWriter writer = new PrintWriter(file);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(resp.getWriter()).thenReturn(writer);
		Query query = new Query();
		query.doGet(req, resp);
		HtmlTxtFileParser parser = new HtmlTxtFileParser();
		writer.close();
		String output = parser.parseDoc(file);
		file.delete();
		output = output.replaceAll("\"query\":\"test html pdf\",", "");
		assertTrue(output.contains("test") && output.contains("HTML") && !output.contains("pdf") 
				&& !output.contains("TXT"));
	}
	
	@Test
	public void testQueryCourse2() throws IOException, ServletException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getParameter("query")).thenReturn("test html pdf");
		when(req.getParameter("match")).thenReturn("Word Match");
		when(req.getParameter("checkedBox")).thenReturn("Test File");
		when(req.getParameter("course")).thenReturn("CSCC69");
		File file = new File("queryTest.txt");
		PrintWriter writer = new PrintWriter(file);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(resp.getWriter()).thenReturn(writer);
		Query query = new Query();
		query.doGet(req, resp);
		HtmlTxtFileParser parser = new HtmlTxtFileParser();
		writer.close();
		String output = parser.parseDoc(file);
		file.delete();
		output = output.replaceAll("\"query\":\"test html pdf\",", "");
		assertTrue(output.contains("test") && !output.contains("html") && output.contains("pdf"));
	}
	@Test
	public void testQueryCourseNoMatch() throws IOException, ServletException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getParameter("query")).thenReturn("test html pdf");
		when(req.getParameter("match")).thenReturn("Word Match");
		when(req.getParameter("checkedBox")).thenReturn("Test File");
		when(req.getParameter("course")).thenReturn("CSCC99");
		File file = new File("queryTest.txt");
		PrintWriter writer = new PrintWriter(file);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(resp.getWriter()).thenReturn(writer);
		Query query = new Query();
		query.doGet(req, resp);
		HtmlTxtFileParser parser = new HtmlTxtFileParser();
		writer.close();
		String output = parser.parseDoc(file);
		file.delete();
		output = output.replaceAll("\"query\":\"test html pdf\",", "");
		assertTrue(!output.contains("test") && !output.contains("html") && !output.contains("pdf"));
	}
	@Test
	public void testQueryCourseUserMatch() throws IOException, ServletException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getParameter("query")).thenReturn("test html pdf");
		when(req.getParameter("match")).thenReturn("Word Match");
		when(req.getParameter("checkedBox")).thenReturn("Test File");
		when(req.getParameter("course")).thenReturn("CSCC01");
		when(req.getParameter("user")).thenReturn("Vincent Landolfi");
		File file = new File("queryTest.txt");
		PrintWriter writer = new PrintWriter(file);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(resp.getWriter()).thenReturn(writer);
		Query query = new Query();
		query.doGet(req, resp);
		HtmlTxtFileParser parser = new HtmlTxtFileParser();
		writer.close();
		String output = parser.parseDoc(file);
		file.delete();
		output = output.replaceAll("\"query\":\"test html pdf\",", "");
		assertTrue(output.contains("test") && output.contains("html") && !output.contains("TXT") && !output.contains("pdf"));
	}
	@Test
	public void testQueryCourseUserNoMatch() throws IOException, ServletException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getParameter("query")).thenReturn("test html pdf");
		when(req.getParameter("match")).thenReturn("Word Match");
		when(req.getParameter("checkedBox")).thenReturn("Test File");
		when(req.getParameter("course")).thenReturn("CSCC43");
		when(req.getParameter("user")).thenReturn("Jane Tester");
		File file = new File("queryTest.txt");
		PrintWriter writer = new PrintWriter(file);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		when(resp.getWriter()).thenReturn(writer);
		Query query = new Query();
		query.doGet(req, resp);
		HtmlTxtFileParser parser = new HtmlTxtFileParser();
		writer.close();
		String output = parser.parseDoc(file);
		file.delete();
		output = output.replaceAll("\"query\":\"test html pdf\",", "");
		assertTrue(!output.contains("test") && !output.contains("html") && !output.contains("pdf"));
	}
}
