package Database;

import org.json.JSONArray;
import org.junit.AfterClass;
import org.junit.Test;

import UTSearCh.CourseDataGetter;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteJDBCTest {
	//public static final SQLiteJDBC db2 = new SQLiteJDBC("UTSearChDB_test");
    public static final SQLiteJDBC db2 = SQLiteJDBC.getDBReference("UTSearChDB_test");

    @Test
    public void executeQuery() {
    	db2.executeQuery("DELETE FROM Accounts where id >=0");
    	db2.executeQuery("INSERT INTO Accounts (sub, firstname, lastname, email) VALUES ('5','we','wee','weee')");
    	assertEquals(true, db2.accountExists("5"));
    	db2.executeQuery("UPDATE Accounts SET sub = '50' WHERE sub ='5'");
    	assertEquals(true, db2.accountExists("50"));
    	db2.executeQuery("DELETE FROM Accounts where sub='50'");
    	assertEquals(false, db2.accountExists("50"));
    }

    @Test
    public void accountExists() {
        //public void addAccount(String sub, String firstname, String lastname, String gmail, String uoftMail, String accountType, int verified) {
    	db2.addAccount("3", "test", "toast", "email.com", "uoftmail.com", "Student", 1);
    	assertEquals(true, db2.accountExists("3"));
    }

    @Test
    public void addAccount() {
    	db2.addAccount("4", "test", "toast", "unique", "auoftmail.com", "Student", 1);
    	assertEquals(true, db2.accountExists("4"));
    	db2.executeQuery("DELETE FROM Accounts where sub = '4'");
    	
    }
    
    @Test
    public void sessionExists() {
      assertEquals(true, db2.sessionExists("toast@mail.com"));
      assertEquals(false, db2.sessionExists("ttte_token"));
      assertEquals(false, db2.sessionExists("ex_token"));
    }
    
    @Test
    public void deleteSession() {
      db2.deleteSession("test_token");
      assertEquals(false, db2.sessionExists("test_token"));
      
    }
    
    @Test
    public void createSession() {
      db2.createSession("test@mail.com", "2018-07-09 16:00:00", "test_token");
      assertEquals(true, db2.sessionExists("test@mail.com"));
    }
    
    @Test
    public void sessionValid() {
      assertEquals(false, db2.sessionValid("exp_token", "2018-07-09T16:00:01"));
      assertEquals(false, db2.sessionValid("exp_token", "2018-07-09T16:01:00"));
      assertEquals(false, db2.sessionValid("exp_token", "2018-07-09T16:10:00"));
      assertEquals(false, db2.sessionValid("exp_token", "2018-07-09T17:00:00"));
      assertEquals(false, db2.sessionValid("exp_token", "2018-07-10T15:00:00"));
      assertEquals(false, db2.sessionValid("exp_token", "2018-08-10T16:00:00"));
      assertEquals(false, db2.sessionValid("exp_token", "2019-07-10T16:00:00"));
      assertEquals(false, db2.sessionValid("exp_token", "2018-07-10T15:00:00"));
      assertEquals(true, db2.sessionValid("exp_token", "2018-07-09T15:59:00"));
      assertEquals(true, db2.sessionValid("exp_token", "2018-07-09T12:00:01"));
    }

    @Test
    public void getUserQuery() {
        // clean first
        db2.deleteAllUserQueries("jon");
        assertEquals(0, db2.getUserQuery("jon", 5).size());
        db2.addUserQuery("jon", "http://localhost:8081/search?query=abc&match=&checkedBox=Courses", "2018-07-19T12:35:50");
        ArrayList<String>  al= (ArrayList<String>) db2.getUserQuery("jon",5);
        assertEquals("http://localhost:8081/search?query=abc&match=&checkedBox=Courses", al.get(0));
        db2.addUserQuery("jon", "http://localhost:8081/search?query=ayyyy&match=&checkedBox=Courses", "2018-07-19T12:36:50");
        al= (ArrayList<String>) db2.getUserQuery("jon",5);
        assertEquals("http://localhost:8081/search?query=ayyyy&match=&checkedBox=Courses", al.get(0));
    }

    @Test
    public void addUserQuery() {
        db2.addUserQuery("job2", "http://localhost:8081/search?query=abc&match=&checkedBox=Courses", "2018-07-19T12:35:50");
        assertEquals(1, db2.getUserQuery("job2",3).size());
        db2.addUserQuery("job2", "http://localhost:8081/search?query=abcd&match=&checkedBox=Courses", "2018-07-19T12:36:50");
        assertEquals(2, db2.getUserQuery("job2",3).size());
        db2.addUserQuery("job2", "http://localhost:8081/search?query=abcde&match=&checkedBox=Courses", "2018-07-19T12:37:50");
        assertEquals(3, db2.getUserQuery("job2",3).size());
        db2.addUserQuery("job2", "http://localhost:8081/search?query=abcdef&match=&checkedBox=Courses", "2018-07-19T12:38:50");
        assertEquals(3, db2.getUserQuery("job2",3).size());
        //clean up
        db2.deleteAllUserQueries("job2");
    }

    @Test
    public void deleteAllUserQueries() {
        db2.addUserQuery("delete", "http://localhost:8081/search?query=a&match=&checkedBox=Courses", "2018-07-19T12:35:50");
        db2.addUserQuery("delete", "http://localhost:8081/search?query=ab&match=&checkedBox=Courses", "2018-07-19T12:35:50");
        db2.addUserQuery("delete", "http://localhost:8081/search?query=abc&match=&checkedBox=Courses", "2018-07-19T12:35:50");
        db2.addUserQuery("delete", "http://localhost:8081/search?query=ad&match=&checkedBox=Courses", "2018-07-19T12:35:50");
        db2.addUserQuery("delete", "http://localhost:8081/search?query=aqwe&match=&checkedBox=Courses", "2018-07-19T12:35:50");
        assertEquals(5, db2.getUserQuery("delete",5).size());
        db2.deleteAllUserQueries("delete");
        assertEquals(0, db2.getUserQuery("delete",5).size());
    }
    
    @Test
    public void testGetDocExists() throws SQLException {
    	ResultSet rs = db2.getDocByName("test.pdf");
    	assertTrue(rs.getString("Name").equals("Test PDF"));
    }
    
    @Test
    public void testGetDocNotExists() throws SQLException {
    	ResultSet rs = db2.getDocByName("notafile.xml");
    	assertTrue(rs == null);
    }
    
    @Test
    public void testAddDocAlreadyAdded() throws SQLException {
    	db2.addDoc("Test File", "test.txt", "JUnit", "TXT", "Test File", "TEST01","test","1");
    	assertTrue(!db2.addDoc("Test File", "test.txt", "JUnit", "TXT", "Test File", "TEST01","test","1"));
    	db2.executeQuery("DELETE FROM Documents where UploaderType = 'JUnit'");
    }
    
    @Test
    public void testAddDoc() throws SQLException, InterruptedException {
    	db2.addDoc("Test File", "testfile.txt", "JUnit", "TXT", "Test File", "TEST01","test","1");
    	ResultSet rs = db2.getDocByName("testfile.txt");
    	assertTrue(rs.getString("Name").equals("Test File"));
    	rs.close();
    	db2.executeQuery("DELETE FROM Documents where UploaderType = 'JUnit'");
    }
    
    @Test
    public void testAddCourse() throws SQLException {
    	assertTrue(db2.addCourse("CSCTEST", "tester"));
    	db2.executeQuery("delete from Course where Name = 'CSCTEST'");
    }
    
    @Test
    public void testAddUserCourse() throws SQLException {
    	assertTrue(db2.addUserCourse("3", "CSCTEST"));
    	db2.executeQuery("delete from StudentToCourses where course = 'CSCTEST'");
    }
    
    @Test
    public void testGetCourses() throws SQLException {
    	db2.executeQuery("insert into Course (Name, Instructor) values ('CSCC69','Sina Meraji')");
		db2.executeQuery("insert into Course (Name, Instructor) values ('CSCC01','Abbas Attarwala')");
		db2.executeQuery("insert into Course (Name, Instructor) values ('CSCC24','Anya Tafliovich')");
		List<String> courses = db2.getCourses();
		boolean c69 = false,c01 = false,c24 = false;
		for (String course: courses) {
			c69 = c69 || course.equals("CSCC69");
			c01 = c01 || course.equals("CSCC01");
			c24 = c24 || course.equals("CSCC24");
		}
    	assertTrue(c69 && c01 && c24);
    	db2.executeQuery("delete from Course where Name like '%24' or Name like '%01' or Name like '%69'");
    }
    
    @Test
    public void testGetCourseInstructor() throws SQLException {
    	db2.executeQuery("insert into Course (Name, Instructor) values ('CSCTEST','Sina Meraji')");
    	assertTrue(db2.getCourseInstructor("CSCTEST").equals("Sina Meraji"));
    	db2.executeQuery("delete from Course where Name = 'CSCTEST'");
    }
    
    @Test
    public void testGetCourseInstructorNotExists() throws SQLException {
    	assertTrue(db2.getCourseInstructor("ZXXZZZZXXX") == null);
    }
    
    @Test
    public void testGetCourseDocs() throws SQLException {
    	db2.executeQuery("insert into Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime) values ('t','t.html','test','TEST','TEST','CSCC01','TEST','1','2018-07-27 16:59:14')");
    	db2.executeQuery("insert into Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime) values ('r','r.html','test','TEST','TEST','CSCC01','TEST','1','2018-07-27 16:59:14')");
    	db2.executeQuery("insert into Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime) values ('f','f.html','tester','TEST','TEST','CSCC01','TEST','1','2018-07-27 16:59:14')");
    	db2.executeQuery("insert into Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime) values ('y','y.html','tester','TEST','TEST','CSCC69','TEST','1','2018-07-27 16:59:14')");
    	ResultSet courseDocs = db2.getCourseDocs("CSCC01", "test");
    	JSONArray courseArray = CourseDataGetter.convertToJSON(courseDocs);
    	String results = courseArray.toString();
    	assertTrue(results.contains("t.html") && results.contains("r.html") && !results.contains("f.html") && !results.contains("y.html"));
    	courseDocs = db2.getCourseDocs("CSCC01","tester");
    	results = CourseDataGetter.convertToJSON(courseDocs).toString();
    	assertTrue(!results.contains("t.html") && !results.contains("r.html") && results.contains("f.html") && !results.contains("y.html"));
    	db2.executeQuery("delete from Documents where Uploader = 'TEST'");
    }
    
    @Test
    public void testGetCourseDocsEmpty() throws SQLException {
    	ResultSet courseDocs = db2.getCourseDocs("CSCCNOTACOURSE", "test");
    	JSONArray courseArray = CourseDataGetter.convertToJSON(courseDocs);
    	String results = courseArray.toString();
    	assertTrue(results.equals("[]"));
    }
    
    @Test
    public void testGetAccountTypeByName() throws SQLException {
    	db2.executeQuery("insert into Accounts (sub, firstname, lastname, email, type) values ('43333', 'Test', 'Tester', 'testing@test.com', 'Instructor')");
    	db2.executeQuery("insert into Accounts (sub, firstname, lastname, email, type) values ('4443333', 'Testing', 'Test', 'testing@test.com', 'Student')");
    	String type = db2.getAccountTypeByName("Test", "Tester");
    	String type2 = db2.getAccountTypeByName("Testing", "Test");
    	String noType = db2.getAccountTypeByName("zzzxxxxxzzzg", "zzzzxxxxxx");
    	assertTrue(type.equals("Instructor") && type2.equals("Student") && noType == null);
    	db2.executeQuery("delete from Accounts where email = 'testing@test.com'");
    }
    
    @Test
    public void testGetCoursesTaughtByUser() throws SQLException {
    	db2.executeQuery("insert into Course (Name, Instructor) values ('CSCC01','Testing Tester')");
    	db2.executeQuery("insert into Course (Name, Instructor) values ('CSCC24','Testing Tester')");
    	db2.executeQuery("insert into Course (Name, Instructor) values ('CSCC69','Test Tester')");
    	String test1 = CourseDataGetter.convertToJSON(db2.getCoursesTaughtByUser("Testing Tester")).toString();
    	String test2 = CourseDataGetter.convertToJSON(db2.getCoursesTaughtByUser("Test Tester")).toString();
    	String test3 = CourseDataGetter.convertToJSON(db2.getCoursesTaughtByUser("T")).toString();
    	assertTrue(test1.contains("CSCC01") && test1.contains("CSCC24") && test2.contains("CSCC69") && test3.equals("[]"));
    	db2.executeQuery("delete from Course where UPPER(Instructor) like '%TESTER'");
    }
    
    @Test
    public void testGetSubFromName() throws SQLException {
    	db2.executeQuery("insert into Accounts (sub, firstname, lastname, email, type) values ('43333', 'Test', 'Tester', 'testing@test.com', 'Instructor')");
    	db2.executeQuery("insert into Accounts (sub, firstname, lastname, email, type) values ('4443333', 'Testing', 'Test', 'testing@test.com', 'Student')");
    	String sub = db2.getSubFromName("test", "tester");
    	String sub1 = db2.getSubFromName("testing", "test");
    	String sub2 = db2.getSubFromName("zzzxxxxxzzzg", "zzzzxxxxxx");
    	assertTrue(sub.equals("43333") && sub1.equals("4443333") && sub2 == null);
    	db2.executeQuery("delete from Accounts where email = 'testing@test.com'");
    }
}