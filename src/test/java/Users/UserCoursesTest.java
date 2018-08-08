package Users;

import Database.SQLiteJDBC;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class UserCoursesTest {
  public static final SQLiteJDBC db2 = SQLiteJDBC.getDBReference("UTSearChDB_test");
  /**
   * Test user courses with valid  account
   */
  @Test
  public void testUserCourses() {
    // mock http resp and req
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    // mock token
    when(request.getParameter("token")).thenReturn("4121321312");
    try {

      UserCourses uc = new UserCourses();
      uc.setTestDb(db2);
      PrintWriter out = new PrintWriter(System.out);
      when(response.getWriter()).thenReturn(out);
      uc.doGet(request, response);
      String s =uc.userCourses("921312");
      String compare = "CSCC01,CSCC69,CSCC24,CSCB07,CSCB09";
      assertEquals(s, compare);
      } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Test user courses with invalid account
   */
  @Test
  public void testInvalidUserCourses() {
    // mock http resp and req
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    // mock token
    when(request.getParameter("token")).thenReturn("asd2ae");
    try {

      UserCourses uc = new UserCourses();
      uc.setTestDb(db2);
      PrintWriter out = new PrintWriter(System.out);
      when(response.getWriter()).thenReturn(out);
      uc.doGet(request, response);
      String s =uc.userCourses("12312312");
      String compare = "";
      assertEquals(s, compare);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
