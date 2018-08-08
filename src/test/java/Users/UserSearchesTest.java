package Users;

import Database.SQLiteJDBC;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserSearchesTest {
  public static final SQLiteJDBC db2 = SQLiteJDBC.getDBReference("UTSearChDB_test");


  @Test
  public void testUserSearchesSet() {
    // mock http resp and req
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    // mock token
    when(request.getParameter("token")).thenReturn("4121321312");
    when(request.getParameter("action")).thenReturn("set");
    when(request.getParameter("queryUrl")).thenReturn("http://localhost:8081/search?query=test");
    when(request.getParameter("match")).thenReturn("&match=");
    when(request.getParameter("checkedBox")).thenReturn("&checkedBox=Courses");
    // make sure queries is 0 before calling servlet
    db2.deleteAllUserQueries("921312");
    List<String> queries;
    queries = db2.getUserQuery("921312",5);
    assertEquals(0, queries.size());
    try {
      //                String queryUrl = req.getParameter("queryUrl");
      //                // get match and checkedBox
      //                String match = req.getParameter("match");
      //                String checkedBox = req.getParameter("checkedBox");
      //String reconstructed = queryUrl + "&match=" + match + "&checkedBox=" + checkedBox;
      UserSearchesServlet us = new UserSearchesServlet();
      us.setDb(db2);
      us.doPost(request, response);
      // make sure queries is increase
      assertEquals(1, db2.getUserQuery("921312",5).size());
      // delete queries
      db2.deleteAllUserQueries("921312");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
