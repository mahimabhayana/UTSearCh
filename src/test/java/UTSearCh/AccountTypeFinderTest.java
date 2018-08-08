package UTSearCh;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import Database.SQLiteJDBC;
import Database.SQLiteJDBCTest;

public class AccountTypeFinderTest {
    
    SQLiteJDBC db = SQLiteJDBCTest.db2;
    
    @Test
     public void testGetTypeExists() throws ServletException, IOException {
        db.executeQuery("INSERT INTO Accounts (sub, firstname, lastname, email, type) VALUES ('6','test','instructor','test@instructor.com', 'Instructor')");
        db.executeQuery("INSERT INTO Sessions (sub, date, token) VALUES ('6', '2018-07-09 16:00:00','instructor_token')");
        AccountTypeFinder finder = new AccountTypeFinder();
        finder.db = db;
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getParameter("token")).thenReturn("instructor_token");
        StringWriter stringWriter = new StringWriter();
        Writer writer = new PrintWriter(stringWriter);
        when(resp.getWriter()).thenReturn((PrintWriter) writer);
        finder.doGet(req, resp);
        String output = stringWriter.toString();
        assertTrue(output.equals("Instructor"));
        db.executeQuery("delete from Accounts where firstname = 'test'");
        db.executeQuery("delete from Sessions where token = 'instructor_token'");
    }
    
    @Test
     public void testGetTypeDoesntExist() throws ServletException, IOException {
        AccountTypeFinder finder = new AccountTypeFinder();
        finder.db = db;
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getParameter("token")).thenReturn("fake_token");
        StringWriter stringWriter = new StringWriter();
        Writer writer = new PrintWriter(stringWriter);
        when(resp.getWriter()).thenReturn((PrintWriter) writer);
        finder.doGet(req, resp);
        String output = stringWriter.toString();
        
        assertTrue(output.equals(""));
    }
  
}
