package UTSearCh;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import Database.SQLiteJDBC;
import Database.SQLiteJDBCTest;

public class IndexTest {
	
	static SQLiteJDBC db = SQLiteJDBCTest.db2;
	public static Index index;
	
	static {
		try {
			index = new Index("index");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	
	@Test
	public void testIndexFile() throws IOException, SQLException {
		File file = new File("testFiles/test.txt");
		assertTrue(index.indexFile(file).equals("indexed"));
	}
	
	@Test
	public void testIndexNull() throws IOException, SQLException {
		assertTrue(index.indexFile(null).equals(""));
	}
	
	@Test
	public void testIndexInvalidFile() throws IOException, SQLException {
		File file = new File("testFiles/meeting.rtf");
		assertTrue(index.indexFile(file).equals(""));
	}
	
	@Test
	public void testGetDocTxt() throws IOException, SQLException {
		index.db = db;
		File file = new File("testFiles/test.txt");
		Document doc = index.getDocument(file);
		boolean ret = true;
		for (IndexableField field : doc.getFields()) {
			if (field.name() == "contents") {
				ret = ret && (field.stringValue().replace("\r\n", "\n").equals("testing line 1\ntest on line 2\n"));
			} else if (field.name() == "title") {
				ret = ret && (field.stringValue().equals("test.txt"));
			} else if (field.name() == "path") {
				ret = ret && ((field.stringValue().equals(System.getProperty("user.dir") + "/testFiles/test.txt")) || (field.stringValue().equals(System.getProperty("user.dir") + "\\testFiles\\test.txt")));
			} else if (field.name() == "Name") {
				ret = ret && (field.stringValue().equals("Test TXT"));
			} else if (field.name() == "DocType") {
				ret = ret && (field.stringValue().equals("Test File"));
			} else if (field.name() == "UploaderType") {
				ret = ret && (field.stringValue().equals("Instructor"));
			} else if (field.name() == "Course") {
				ret = ret && (field.stringValue().equals("CSCC43"));
			} else if (field.name() == "FileType") {
				ret = ret && (field.stringValue().equals("TXT"));
			} else if (field.name() == "Uploader") {
				ret = ret && (field.stringValue().equals("Vincent Landolfi"));
			}
		}
		assertTrue(ret);
	}
	
	@Test
	public void testGetDocHtml() throws IOException, SQLException {
		index.db = db;
		File file = new File("testFiles/test.html");
		Document doc = index.getDocument(file);
		boolean ret = true;
		for (IndexableField field : doc.getFields()) {
			if (field.name() == "contents") {
				ret = ret && (field.stringValue().replace("\r\n", "\n").equals("Testing\nTest.\n"));
			} else if (field.name() == "title") {
				ret = ret && (field.stringValue().equals("test.html"));
			} else if (field.name() == "path") {
				ret = ret && ((field.stringValue().equals(System.getProperty("user.dir") + "/testFiles/test.html")) || (field.stringValue().equals(System.getProperty("user.dir") + "\\testFiles\\test.html")));
			} else if (field.name() == "Name") {
				ret = ret && (field.stringValue().equals("Test HTML"));
			} else if (field.name() == "DocType") {
				ret = ret && (field.stringValue().equals("Test File"));
			} else if (field.name() == "UploaderType") {
				ret = ret && (field.stringValue().equals("Instructor"));
			} else if (field.name() == "Course") {
				ret = ret && (field.stringValue().equals("CSCC01"));
			} else if (field.name() == "FileType") {
				ret = ret && (field.stringValue().equals("HTML"));
			} else if (field.name() == "Uploader") {
				ret = ret && (field.stringValue().equals("Vincent Landolfi"));
			}
		}
		assertTrue(ret);
	}
	
	@Test
	public void testGetDocPdf() throws IOException, SQLException {
		index.db = db;
		File file = new File("testFiles/test.pdf");
		Document doc = index.getDocument(file);
		boolean ret = true;
		for (IndexableField field : doc.getFields()) {
			if (field.name() == "contents") {
				ret = ret && (field.stringValue().replace("\r\n", "\n").equals("This is a test pdf file.\n"));
			} else if (field.name() == "title") {
				ret = ret && (field.stringValue().equals("test.pdf"));
			} else if (field.name() == "path") {
				ret = ret && ((field.stringValue().equals(System.getProperty("user.dir") + "/testFiles/test.pdf")) || (field.stringValue().equals(System.getProperty("user.dir") + "\\testFiles\\test.pdf")));
			} else if (field.name() == "Name") {
				ret = ret && (field.stringValue().equals("Test PDF"));
			} else if (field.name() == "DocType") {
				ret = ret && (field.stringValue().equals("Test File"));
			} else if (field.name() == "UploaderType") {
				ret = ret && (field.stringValue().equals("Student"));
			} else if (field.name() == "Course") {
				ret = ret && (field.stringValue().equals("CSCC69"));
			} else if (field.name() == "FileType") {
				ret = ret && (field.stringValue().equals("PDF"));
			} else if (field.name() == "Uploader") {
				ret = ret && (field.stringValue().equals("Jason Zhong"));
			}
		}
		assertTrue(ret);
	}
	
	@Test
	public void testGetDocNoDBEntry() throws IOException, SQLException {
		index.db = db;
		File file = new File("testFiles/copy.txt");
		Document doc = index.getDocument(file);
		boolean ret = true;
		for (IndexableField field : doc.getFields()) {
			if (field.name() == "contents") {
				ret = ret && (field.stringValue().replace("\r\n", "\n").equals("this\nis a test file\nwith multiple lines\n123 123\n"));
			} else if (field.name() == "title") {
				ret = ret && (field.stringValue().equals("copy.txt"));
			} else if (field.name() == "path") {
				ret = ret && ((field.stringValue().equals(System.getProperty("user.dir") + "/testFiles/copy.txt")) || (field.stringValue().equals(System.getProperty("user.dir") + "\\testFiles\\copy.txt")));
			} else if (field.name() == "Name") {
				ret = ret && (field.stringValue().equals("N/A"));
			} else if (field.name() == "DocType") {
				ret = ret && (field.stringValue().equals("N/A"));
			} else if (field.name() == "UploaderType") {
				ret = ret && (field.stringValue().equals("N/A"));
			} else if (field.name() == "Course") {
				ret = ret && (field.stringValue().equals("N/A"));
			} else if (field.name() == "FileType") {
				ret = ret && (field.stringValue().equals("N/A"));
			} else if (field.name() == "Uploader") {
				ret = ret && (field.stringValue().equals("N/A"));
			}
		}
		assertTrue(ret);
	}
	
	@Test
	public void testGetDocInvalid() throws IOException, SQLException {
		File file = new File("testFiles/meeting.rtf");
		Document doc = index.getDocument(file);
		assertTrue(doc == null);
	}
	
	
	@Test
	public void testCreateIndex() throws IOException, SQLException {
		index.getWriter().deleteAll();
		File indexDir = new File("index");
		int numIndexed = index.createIndex("testFiles", new HtmlTxtPdfFileFilter());
		assertTrue(numIndexed == 4 && indexDir.listFiles().length > 0);
	}
	
	 @AfterClass
	    public static void doYourOneTimeTeardown() throws IOException {
	        index.close();
	    }    
	
}
