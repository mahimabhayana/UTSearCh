package UTSearCh;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import Database.SQLiteJDBC;
import Database.SQLiteJDBCTest;

public class UploaderTest {
	
	private static final SQLiteJDBC db = SQLiteJDBCTest.db2;

	@Test
	public void testUploadTxt() throws Exception {
		db.executeQuery("DELETE FROM Documents where FileName = 'test.txt'");
		File testFile = new File("data/test.txt");
		if (testFile.exists()) {
			testFile.delete();
		}
		testFile = new File("testFiles/test.txt");
		HttpServletResponse response = mock(HttpServletResponse.class);
		MockMultipartHttpServletRequest req = new MockMultipartHttpServletRequest();
		req.setContentType("multipart/form-data; boundary=someBoundary");
		Path fileLoc = Paths.get("testFiles/test.txt");
		MockMultipartFile file = new MockMultipartFile("test.txt","test.txt","text/plain",Files.readAllBytes(fileLoc));
		req.setContent(createFileContent(Files.readAllBytes(fileLoc),"someBoundary","text/plain","test.txt"));
		req.addFile(file);
		req.addParameter("title", "Test TXT");
		req.addParameter("document-type", "Test File");
		req.addParameter("course", "CSCC43");
		req.addParameter("user", "Vincent Landolfi");
		req.addParameter("uploaderType", "Instructor");
		req.addParameter("token", "test");
		File fileUp = new File("data/test.txt");
		Uploader uploader = new Uploader();
		uploader.docToUpload.setDB(db);
		uploader.doPost(req, response);
		Document doc = new Document();
		doc.setDB(db);
		doc.fileName = fileUp.getName();
		assertTrue(fileUp.exists() && doc.docInDB());
	}
	
	@Test
	public void testUploadHtml() throws FileUploadException, ServletException, IOException {
		db.executeQuery("DELETE FROM Documents where FileName = 'test.html'");
		File testFile = new File("data/test.html");
		if (testFile.exists()) {
			testFile.delete();
		}
		testFile = new File("testFiles/test.html");
		HttpServletResponse response = mock(HttpServletResponse.class);
		MockMultipartHttpServletRequest req = new MockMultipartHttpServletRequest();
		req.setContentType("multipart/form-data; boundary=someBoundary");
		Path fileLoc = Paths.get("testFiles/test.html");
		MockMultipartFile file = new MockMultipartFile("test.html","test.html","text/plain",Files.readAllBytes(fileLoc));
		req.setContent(createFileContent(Files.readAllBytes(fileLoc),"someBoundary","text/plain","test.html"));
		req.addFile(file);
		req.addParameter("title", "Test HTML");
		req.addParameter("document-type", "Test File");
		req.addParameter("course", "CSCC01");
		req.addParameter("user", "Vincent Landolfi");
		req.addParameter("uploaderType", "Instructor");
		req.addParameter("token", "test");
		File fileUp = new File("data/test.html");
		Uploader uploader = new Uploader();
		uploader.docToUpload.setDB(db);
		uploader.doPost(req, response);
		Document doc = new Document();
		doc.setDB(db);
		doc.fileName = fileUp.getName();
		assertTrue(fileUp.exists() && db.docExists(doc.fileName));
	}
	
	@Test
	public void testNonAcceptedFile() throws FileUploadException, ServletException, IOException {
		File testFile = new File("data/meeting.rtf");
		if (testFile.exists()) {
			testFile.delete();
		}
		testFile = new File("testFiles/meeting.rtf");
		HttpServletResponse response = mock(HttpServletResponse.class);
		MockMultipartHttpServletRequest req = new MockMultipartHttpServletRequest();
		req.setContentType("multipart/form-data; boundary=someBoundary");
		Path fileLoc = Paths.get("testFiles/meeting.rtf");
		MockMultipartFile file = new MockMultipartFile("meeting.rtf","meeting.rtf","text/plain",Files.readAllBytes(fileLoc));
		req.setContent(createFileContent(Files.readAllBytes(fileLoc),"someBoundary","text/plain","meeting.rtf"));
		req.addFile(file);
		req.addParameter("title", "Test RTF");
		req.addParameter("document-type", "Test File");
		req.addParameter("course", "CSCC01");
		req.addParameter("user", "Vincent Landolfi");
		req.addParameter("uploaderType", "Instructor");
		req.addParameter("token", "test");
		File fileUp = new File("data/meeting.rtf");
		Uploader uploader = new Uploader();
		uploader.docToUpload.setDB(db);
		uploader.doPost(req, response);
		Document doc = new Document();
		doc.setDB(db);
		doc.fileName = fileUp.getName();
		System.out.println("run run");
		assertTrue(!fileUp.exists() && !db.docExists(doc.fileName));
	}
	
	
	@Test
	public void testUploadPdf() throws FileUploadException, ServletException, IOException {
		db.executeQuery("DELETE FROM Documents where FileName = 'test.pdf'");
		File testFile = new File("data/test.pdf");
		if (testFile.exists()) {
			testFile.delete();
		}
		testFile = new File("testFiles/test.pdf");
		HttpServletResponse response = mock(HttpServletResponse.class);
		MockMultipartHttpServletRequest req = new MockMultipartHttpServletRequest();
		req.setContentType("multipart/form-data; boundary=someBoundary");
		Path fileLoc = Paths.get("testFiles/test.pdf");
		MockMultipartFile file = new MockMultipartFile("test.pdf","test.pdf","application/pdf",Files.readAllBytes(fileLoc));
		req.setContent(createFileContent(Files.readAllBytes(fileLoc),"someBoundary","application/pdf","test.pdf"));
		req.addFile(file);
		req.addParameter("title", "Test PDF");
		req.addParameter("document-type", "Test File");
		req.addParameter("course", "CSCC69");
		req.addParameter("user", "Jason Zhong");
		req.addParameter("uploaderType", "Student");
		req.addParameter("token", "test");
		File fileUp = new File("data/test.pdf");
		Uploader uploader = new Uploader();
		uploader.docToUpload.setDB(db);
		uploader.doPost(req, response);
		Document doc = new Document();
		doc.setDB(db);
		doc.fileName = fileUp.getName();
		assertTrue(fileUp.exists() && doc.docInDB());
	}
	
	@Test
	public void testGet() throws FileUploadException, ServletException, IOException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		File indexDir = new File("index");
		new Uploader().doGet(req, resp);
		assertTrue(indexDir.listFiles().length > 0);
	}
	
	
	@Test
	public void testCreateIndex() throws FileUploadException, ServletException, IOException, SQLException {
		File indexDir = new File("index");
		new Uploader().createIndex();
		assertTrue(indexDir.listFiles().length > 0);
	}
	
	public byte[] createFileContent(byte[] data, String boundary, String contentType, String fileName){
        String start = "--" + boundary + "\r\n Content-Disposition: form-data; name=\"file\"; filename=\""+fileName+"\"\r\n"
                 + "Content-type: "+contentType+"\r\n\r\n";;

        String end = "\r\n--" + boundary + "--"; // correction suggested @butfly 
        return ArrayUtils.addAll(start.getBytes(),ArrayUtils.addAll(data,end.getBytes()));
    }

}
