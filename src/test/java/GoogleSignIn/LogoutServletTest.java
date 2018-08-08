package GoogleSignIn;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import Database.SQLiteJDBC;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServletTest {
  public static final SQLiteJDBC db2 = SQLiteJDBC.getDBReference("UTSearChDB_test");
  @Test
  public void test() {

    try {
      // mock http resp and req
      HttpServletRequest request = mock(HttpServletRequest.class);
      HttpServletResponse response = mock(HttpServletResponse.class);

      GoogleIdToken.Payload payLoad = new GoogleIdToken.Payload();
//      payLoad.setEmail("tester@mail.com");
//      payLoad.setSubject("12345");
//      payLoad.set("given_name", "Test");
//      payLoad.set("family_name", "Tester");
      // mock these parameters
      //String idToken = req.getParameter("id_token");
      when(request.getParameter("id_token")).thenReturn("forever");
      // mock payload get stuff
      when(IdTokenVerifierAndParser.getPayload(any())).thenReturn(payLoad);
      // mock payload params
      when(payLoad.get("given_name")).thenReturn("Test");
      when(payLoad.get("family_name")).thenReturn("Tester");
      when(payLoad.get("email")).thenReturn("tester@mail.com");
      when(payLoad.getSubject()).thenReturn("12345");
      // mock unique token generator
      when(LoginServlet.tokenGenerator()).thenReturn("123456789");
      // call login servlet
      new LoginServlet().doPost(request, response);
      // should log in
      assertEquals(true, db2.sessionExists("12345"));
      // call logout servlt
      when(request.getParameter("email")).thenReturn("tester@mail.com");
      when(request.getParameter("token")).thenReturn("123456789");
      new LogoutServlet().doPost(request, response);
      assertEquals(false, db2.sessionExists("12345"));

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


  }

}
