package UTSearCh;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class HtmlTxtPdfFileFilterTest {
	
	HtmlTxtPdfFileFilter filter = new HtmlTxtPdfFileFilter();

	@Test
	public void testTxtFileAccept() {
		File txtFile = new File("testFiles/test.txt");
		assertTrue(filter.accept(txtFile));
	}
	
	@Test
	public void testHtmlFileAccept() {
		File htmlFile = new File("testFiles/test.html");
		assertTrue(filter.accept(htmlFile));
	}
	
	@Test
	public void testPdfFileAccept() {
		File pdfFile = new File("testFiles/test.pdf");
		assertTrue(filter.accept(pdfFile));
	}
	
	@Test
	public void testNonAcceptedFile() {
		File rtfFile = new File("testFiles/meeting.rtf");
		assertTrue(!filter.accept(rtfFile));
	}
}
