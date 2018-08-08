package Users;

import Database.SQLiteJDBC;
import GoogleSignIn.IdTokenVerifierAndParser;
import GoogleSignIn.LoginServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class UserDocumentsTest {
  public static final SQLiteJDBC db2 = SQLiteJDBC.getDBReference("UTSearChDB_test");

  /**
   * Test user documents with valid  account
   */
  @Test
  public void testUserDocuments() {
    // mock http resp and req
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    // mock token
    when(request.getParameter("token")).thenReturn("4121321312");
    try {

      UserDocuments ud = new UserDocuments();
      ud.setDb(db2);
      PrintWriter out = new PrintWriter(System.out);
      when(response.getWriter()).thenReturn(out);
      ud.doGet(request, response);
      String s = db2.getUserDocumentsEfficient("921312");
      String compare = "{\"results\":[{\"path\":\"toast\",\"file_type\":\"HTML\",\"doc_upload_time\":\"2018-08-06 00:03:00\",\"name\":\"toast HTML\",\"rating\":\"0\",\"course\":\"CSCC01\",\"doc_type\":\"Test File\"},{\"path\":\"toast2\",\"file_type\":\"PDF\",\"doc_upload_time\":\"2018-08-06 00:03:00\",\"name\":\"toast PDF\",\"rating\":\"0\",\"course\":\"CSCC01\",\"doc_type\":\"Test File\"},{\"path\":\"toast3\",\"file_type\":\"PDF\",\"doc_upload_time\":\"2018-08-06 00:03:00\",\"name\":\"toast3 PDF\",\"rating\":\"0\",\"course\":\"CSCC03\",\"doc_type\":\"Test3 File\"},{\"path\":\"asda\",\"file_type\":\"TXT\",\"doc_upload_time\":\"2018-08-06 00:03:00\",\"name\":\"asda TXT\",\"rating\":\"0\",\"course\":\"CSCC02\",\"doc_type\":\"Test2 File\"}]}";
      System.out.println("FFFF: "+ s);
      assertEquals(s, compare);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Test user documents with invalid account
   */
  @Test
  public void testInvalidUserDocuments() {
    // mock http resp and req
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    // mock token
    when(request.getParameter("token")).thenReturn("123123423");
    try {

      UserDocuments ud = new UserDocuments();
      ud.setDb(db2);
      PrintWriter out = new PrintWriter(System.out);
      when(response.getWriter()).thenReturn(out);
      ud.doGet(request, response);
      String s = db2.getUserDocumentsEfficient("123124456");
      String compare = "{\"results\":[]}";
      assertEquals(s, compare);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
