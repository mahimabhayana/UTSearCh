package GoogleSignIn;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.booleanThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import Database.SQLiteJDBC;
import GoogleSignIn.IdTokenVerifierAndParser;
import GoogleSignIn.LoginServlet;
import GoogleSignIn.ValidateEmailServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ValidateEmailServletTest {
  public static final SQLiteJDBC db2 = SQLiteJDBC.getDBReference("UTSearChDB_test");
  @Test
  public void test() {
    //String token = req.getParameter("token");
    try {
      // mock http resp and req
      HttpServletRequest request = mock(HttpServletRequest.class);
      HttpServletResponse response = mock(HttpServletResponse.class);
      db2.deleteAccount("921312");
      // create test account
      db2.addAccount("921312", "testy", "toasty", "ttttest@gmail.com", "testgseas@utoronto.ca", "Student", 0);
      // create a session for this account
      db2.createSession("921312", "2999-07-09T16:00:00", "4121321312");
      // create a validation entry
      db2.createValidationEntry("921312", "4121321312");
      when(request.getParameter("token")).thenReturn("4121321312");
      // verify entry created
      assertEquals(true, db2.validateEmail("4121321312"));
      // verify account is not verified
      assertEquals(false, db2.isVerified("921312"));
      // call ValidateEmailServlet
      new ValidateEmailServlet().doGet(request, response);
      // verify entry deleted
      assertEquals(false, db2.validateEmail("4121321312"));
      // verify account is verified
      assertEquals(true, db2.isVerified("921312"));

      //db.validateEmail(token)
      //        if (db.validateEmail(token)) {
      //            // true, so this email needs to be validated
      //            // create account and delete token from EmailValidation
      //            String sub = db.deleteValidationEntry(token);
      //            db.verifyAccount(sub);
      //            resp.sendRedirect("http://localhost:8080/");
      //        }
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

}
