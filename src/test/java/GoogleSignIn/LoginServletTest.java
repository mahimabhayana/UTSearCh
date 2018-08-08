package GoogleSignIn;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import Database.SQLiteJDBC;
import GoogleSignIn.IdTokenVerifierAndParser;
import GoogleSignIn.LoginServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LoginServletTest {
  public static final SQLiteJDBC db2 = SQLiteJDBC.getDBReference("UTSearChDB_test");

  /**
   * Testing logging in with an email associated with an existing account. A session should be created in the database
   */
  @Test
  public void testValidLogin() {
    try {
      // mock http resp and req
      HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
      HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

      GoogleIdToken.Payload payLoad = new GoogleIdToken.Payload();
//      payLoad.setEmail("tester@mail.com");
//      payLoad.setSubject("12345");
//      payLoad.set("given_name", "Test");
//      payLoad.set("family_name", "Tester");
      // mock these parameters
      //String idToken = req.getParameter("id_token");
      Mockito.when(request.getParameter("id_token")).thenReturn("forever");
      // mock payload get stuff
      Mockito.when(IdTokenVerifierAndParser.getPayload(Matchers.any())).thenReturn(payLoad);
      // mock payload params
      Mockito.when(payLoad.get("given_name")).thenReturn("Test");
      Mockito.when(payLoad.get("family_name")).thenReturn("Tester");
      Mockito.when(payLoad.get("email")).thenReturn("tester@mail.com");
      Mockito.when(payLoad.getSubject()).thenReturn("12345");
      // mock unique token generator
      Mockito.when(LoginServlet.tokenGenerator()).thenReturn("123456789");
      // call login servlet
      new LoginServlet().doPost(request, response);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ServletException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
//    verify(request, atLeast(1)).getParameter("username"); // only if you want to verify username was called...
//    writer.flush(); // it may not have been flushed yet...
//    assertTrue(stringWriter.toString().contains("My expected string"));
    assertEquals(true, db2.sessionExists("12345"));

  }

  /**
   * Testing logging in with an email that has no account associated with it. No session should be created in the
   * database
   */
  @Test
  public void testInvalidLogin() {
    // testing when no account exists, trying to login
    try {
      // mock http resp and req
      HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
      HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

      GoogleIdToken.Payload payLoad = new GoogleIdToken.Payload();
      // mock these parameters
      //String idToken = req.getParameter("id_token");
      Mockito.when(request.getParameter("id_token")).thenReturn("forever");
      // mock payload get stuff
      Mockito.when(IdTokenVerifierAndParser.getPayload(Matchers.any())).thenReturn(payLoad);
      // mock payload params
      Mockito.when(payLoad.get("given_name")).thenReturn("Test");
      Mockito.when(payLoad.get("family_name")).thenReturn("Tester");
      Mockito.when(payLoad.get("email")).thenReturn("tester@mail.com");
      // this sub should not exist, no account associated
      Mockito.when(payLoad.getSubject()).thenReturn("0210312931293219");
      // call login servlet
      new LoginServlet().doPost(request, response);
      // check to make sure no session create
      assertEquals(false, db2.sessionExists("0210312931293219"));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ServletException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

}
