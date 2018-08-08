package UTSearCh;

import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Test;

import Database.SQLiteJDBC;
import Database.SQLiteJDBCTest;

public class DocumentTest {
	SQLiteJDBC db = SQLiteJDBCTest.db2;
	Document doc = new Document();
	 @Test
	 public void testAddDoc() throws SQLException, InterruptedException {
	   	doc.setDB(db);
    	doc.fileName = "testing123.xml";
	    doc.course = "CSCC69";
	    doc.docType = "JUnit";
	    doc.fileType = "XML";
	    doc.name = "JUnit Test";
	    doc.uploaderType = "JUnit";
	    assertTrue(doc.addToDB());
	    db.executeQuery("DELETE FROM Documents where FileName = 'testing123.xml'");
	}
	 
	@Test
    public void testGetDocExists() throws SQLException {
    	doc.setDB(db);
    	doc.fileName = "test.pdf";
    	assertTrue(doc.docInDB());
    }
    
    @Test
    public void testGetDocNotExists() {
    	doc.setDB(db);
    	doc.fileName = "notafile.xml";
    	assertTrue(!doc.docInDB());
    }
    
    @Test
    public void testAddDocAlreadyAdded() throws SQLException, InterruptedException {
    	doc.setDB(db);
    	doc.fileName = "test.pdf";
    	doc.course = "CSCC69";
    	doc.docType = "JUnit";
    	doc.fileType = "XML";
    	doc.name = "JUnit Test";
    	doc.uploaderType = "JUnit";
    	doc.addToDB();
    	Document newDoc = new Document();
    	newDoc.fileName = "test1.pdf";
    	assertTrue(newDoc.docInDB());
    	db.executeQuery("DELETE FROM Documents where UploaderType = 'JUnit'");
    }
    
}
