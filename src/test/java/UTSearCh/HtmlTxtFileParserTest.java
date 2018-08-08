package UTSearCh;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class HtmlTxtFileParserTest {
	
	HtmlTxtFileParser parser = new HtmlTxtFileParser();
	PdfFileParser pdfParser = new PdfFileParser();

	@Test
	public void testParseTxtFile() {
		String txtResult = "testing line 1\ntest on line 2\n";
		File txtFile = new File("testFiles/test.txt");
		assertEquals(txtResult, parser.parseDoc(txtFile).replace("\r\n", "\n"));
	}
	
	@Test
	public void testParseHtmlFile() {
		String htmlResult = "Testing\nTest.\n";
		File htmlFile = new File("testFiles/test.html");
		System.out.println(parser.parseDoc(htmlFile));
		assertEquals(htmlResult, parser.parseDoc(htmlFile).replace("\r\n", "\n"));
	}
	
	@Test
	public void testParsePdfFile() {
		String pdfResult = "This is a test pdf file.\n";
		File pdfFile = new File("testFiles/test.pdf");
		System.out.println(pdfResult);
		System.out.println(pdfParser.parseDoc(pdfFile));
		assertEquals(pdfResult, pdfParser.parseDoc(pdfFile).replace("\r\n", "\n"));
	}
	
	@Test
	public void testParseNonHtmlTxtFile() {
		File rtfFile = new File("testFiles/meeting.rtf");
		assertEquals(null, parser.parseDoc(rtfFile));
	}


}
