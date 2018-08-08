package UTSearCh;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import org.junit.Test;

import Database.SQLiteJDBC;
import Database.SQLiteJDBCTest;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;

public class CrawlerTest {
	Crawler crawler = new Crawler();
	SQLiteJDBC db = SQLiteJDBCTest.db2;
	
	@Test
	 public void testIsValidBadURL() {
		assertTrue(!crawler.isValid("www.test.com"));
	}
	
	@Test
	 public void testIsValidGoodURL() {
		assertTrue(crawler.isValid("www.mathlub.utsc.utoronto.ca"));
	}
	
	@Test
	 public void testShouldVisitBadURL() {
		Page page = mock(Page.class);
		WebURL url = mock(WebURL.class);
		when(url.getURL()).thenReturn("www.test.com");
		assertTrue(!crawler.shouldVisit(page, url));
	}
	@Test
	 public void testShouldVisitGoodURL() {
		Page page = mock(Page.class);
		WebURL url = mock(WebURL.class);
		when(url.getURL()).thenReturn("www.mathlub.utsc.utoronto.ca");
		assertTrue(crawler.shouldVisit(page, url));
	}
	
	/*
	 * Commenting this out just in case the site were to go down
	@Test
	 public void testShouldVisitGoodEndingWithSlash() {
		Page page = mock(Page.class);
		WebURL url = mock(WebURL.class);
		when(url.getURL()).thenReturn("https://www.utsc.utoronto.ca/");
		assertTrue(crawler.shouldVisit(page, url));
	}
	*/
	
	@Test
	 public void testVisitHTML() throws IOException {
		Path html = Paths.get("testFiles/test.html");
		Page page = mock(Page.class);
		WebURL url = mock(WebURL.class);
		when(url.getURL()).thenReturn("https://www.utsc.utoronto.ca/testing.html");
		when(page.getWebURL()).thenReturn(url);
		when(page.getContentType()).thenReturn("html");
		when(page.getContentData()).thenReturn(Files.readAllBytes(html));
		crawler.db = db;
		crawler.visit(page);
		Document doc = new Document();
		doc.fileName = "testing.html";
		File txtFile = new File("data/testing.html");
		assertTrue(doc.docInDB() && txtFile.exists());
		db.executeQuery("DELETE FROM Documents where FileName = 'testing.html'");
		txtFile.delete();
	}
	
	@Test
	 public void testVisitPDF() throws IOException {
		Path pdf = Paths.get("testFiles/test.pdf");
		Page page = mock(Page.class);
		WebURL url = mock(WebURL.class);
		when(url.getURL()).thenReturn("https://www.utsc.utoronto.ca/testing1.pdf");
		when(page.getWebURL()).thenReturn(url);
		when(page.getContentType()).thenReturn("pdf");
		when(page.getContentData()).thenReturn(Files.readAllBytes(pdf));
		crawler.db = db;
		crawler.visit(page);
		Document doc = new Document();
		doc.fileName = "testing1.pdf";
		File pdfFile = new File("data/testing1.pdf");
		assertTrue(doc.docInDB() && pdfFile.exists());
		db.executeQuery("DELETE FROM Documents where FileName = 'testing1.pdf'");
		pdfFile.delete();
	}
	
	@Test
	 public void testVisitTXT() throws IOException {
		Path txt = Paths.get("testFiles/test.txt");
		Page page = mock(Page.class);
		WebURL url = mock(WebURL.class);
		when(url.getURL()).thenReturn("https://www.utsc.utoronto.ca/testing2.txt");
		when(page.getWebURL()).thenReturn(url);
		when(page.getContentType()).thenReturn("text");
		when(page.getContentData()).thenReturn(Files.readAllBytes(txt));
		crawler.db = db;
		crawler.visit(page);
		Document doc = new Document();
		doc.fileName = "testing2.txt";
		File txtFile = new File("data/testing2.txt");
		assertTrue(doc.docInDB() && txtFile.exists());
		db.executeQuery("DELETE FROM Documents where FileName = 'testing2.txt'");
		txtFile.delete();
	}
	
	@Test
	 public void testVisitNonAccepted() {
		Page page = mock(Page.class);
		WebURL url = mock(WebURL.class);
		when(url.getURL()).thenReturn("https://www.utsc.utoronto.ca/testfile.rtf");
		when(page.getWebURL()).thenReturn(url);
		when(page.getContentType()).thenReturn("octet-stream");
		crawler.visit(page);
		File file = new File("data/testfile.rtf");
		assertTrue(!file.exists());
	}
}