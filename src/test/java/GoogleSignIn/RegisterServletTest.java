package GoogleSignIn;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import Database.SQLiteJDBC;
import GoogleSignIn.IdTokenVerifierAndParser;
import GoogleSignIn.LoginServlet;
import GoogleSignIn.RegisterServlet;
import GoogleSignIn.SendMail;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterServletTest {
  public static final SQLiteJDBC db2 = SQLiteJDBC.getDBReference("UTSearChDB_test");
  /**
   * Creating an account no issues
   */
  @Test
  public void testCreateValidAccount() {
    try {
      // mock http resp and req
      HttpServletRequest request = mock(HttpServletRequest.class);
      HttpServletResponse response = mock(HttpServletResponse.class);

      GoogleIdToken.Payload payLoad = new GoogleIdToken.Payload();
      // mock these parameters
      when(request.getParameter("id_token")).thenReturn("create");
      // mock payload get stuff
      when(IdTokenVerifierAndParser.getPayload(any())).thenReturn(payLoad);
      // mock payload params
      when(payLoad.get("given_name")).thenReturn("new");
      when(payLoad.get("family_name")).thenReturn("acc");
      when(payLoad.get("email")).thenReturn("special2@mail.com");
      when(payLoad.getSubject()).thenReturn("1843521");
      // mock return uoftEmail special@email and Student
      when(request.getParameter("email")).thenReturn("special2132132220@student.com");
      when(request.getParameter("type")).thenReturn("Student");

      // call register servlet
      new RegisterServlet().doPost(request, response);
      // register servlet will check if gmail or uoftEmail exists in db, but they should not exists
      // check if validationenry exists in EmailValidation table
      assertEquals(true,db2.validateEmail("1843521"));
      // check if an account was created
      assertEquals(true, db2.accountExists("1843521"));
      // delete these entries in database
      db2.deleteValidationEntry("1843521");
      db2.deleteAccount("1843521");
      // check if
//      db.createValidationEntry(sub,token);
//      // send email
//      SendMail.send(uoftEmail, firstName, token);
//      // create an account
//      db.addAccount(sub, firstName, lastName, gmail, uoftEmail, accountType, 0);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Attempt at creating an account with an in use gmail account. Account should not be created.
   */
  @Test
  public void testCreateInvalidGmailAccount() {
    try {
      // mock http resp and req
      HttpServletRequest request = mock(HttpServletRequest.class);
      HttpServletResponse response = mock(HttpServletResponse.class);

      GoogleIdToken.Payload payLoad = new GoogleIdToken.Payload();
      // mock these parameters
      when(request.getParameter("id_token")).thenReturn("create");
      // mock payload get stuff
      when(IdTokenVerifierAndParser.getPayload(any())).thenReturn(payLoad);
      // mock payload params
      when(payLoad.get("given_name")).thenReturn("new");
      when(payLoad.get("family_name")).thenReturn("acc");
      when(payLoad.get("email")).thenReturn("inuse@test.com");
      when(payLoad.getSubject()).thenReturn("1843521");
      // mock return uoftEmail special@email and Student
      when(request.getParameter("email")).thenReturn("special2132132220@student.com");
      when(request.getParameter("type")).thenReturn("Student");

      // call register servlet
      new RegisterServlet().doPost(request, response);
      // register servlet will check if gmail or uoftEmail exists in db, but they should not exists
      // check if validationenry exists in EmailValidation table
      assertEquals(false, db2.validateEmail("1843521"));
      // check if an account was created
      assertEquals(false, db2.accountExists("1843521"));
      // delete these entries in database
      db2.deleteValidationEntry("1843521");
      db2.deleteAccount("1843521");
      // check if
//      db.createValidationEntry(sub,token);
//      // send email
//      SendMail.send(uoftEmail, firstName, token);
//      // create an account
//      db.addAccount(sub, firstName, lastName, gmail, uoftEmail, accountType, 0);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Attempt at creating an account with an in use utor account. Account should not be created.
   */
  @Test
  public void testCreateInvalidUtorAccount() {
    try {
      // mock http resp and req
      HttpServletRequest request = mock(HttpServletRequest.class);
      HttpServletResponse response = mock(HttpServletResponse.class);

      GoogleIdToken.Payload payLoad = new GoogleIdToken.Payload();
      // mock these parameters
      when(request.getParameter("id_token")).thenReturn("create");
      // mock payload get stuff
      when(IdTokenVerifierAndParser.getPayload(any())).thenReturn(payLoad);
      // mock payload params
      when(payLoad.get("given_name")).thenReturn("new");
      when(payLoad.get("family_name")).thenReturn("acc");
      when(payLoad.get("email")).thenReturn("2123213esasasd");
      when(payLoad.getSubject()).thenReturn("1843521");
      // mock return uoftEmail special@email and Student
      when(request.getParameter("email")).thenReturn("inuse@test.com");
      when(request.getParameter("type")).thenReturn("Student");

      // call register servlet
      new RegisterServlet().doPost(request, response);
      // register servlet will check if gmail or uoftEmail exists in db, but they should not exists
      // check if validationenry exists in EmailValidation table
      assertEquals(false, db2.validateEmail("1843521"));
      // check if an account was created
      assertEquals(false, db2.accountExists("1843521"));
      // delete these entries in database
      db2.deleteValidationEntry("1843521");
      db2.deleteAccount("1843521");
      // check if
//      db.createValidationEntry(sub,token);
//      // send email
//      SendMail.send(uoftEmail, firstName, token);
//      // create an account
//      db.addAccount(sub, firstName, lastName, gmail, uoftEmail, accountType, 0);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
