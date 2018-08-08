package Users;

import Database.SQLiteJDBC;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserGoogleChartsTest {
  public static final SQLiteJDBC db2 = SQLiteJDBC.getDBReference("UTSearChDB_test");

  @Test
  public void testUserFilesTypes() {
    UserGoogleCharts ugc = new UserGoogleCharts();
    String ret = ugc.userFileTypes("921312");
    System.out.println(ret);
    String compare = "TXT:1,PDF:2,HTML:1";
    assertEquals(ret, compare);

  }

  @Test
  public void testUserDocTypes() {
    UserGoogleCharts udt = new UserGoogleCharts();
    String ret = udt.userDocTypes("921312");
    String compare = "Test File:2,Test3 File:1,Test2 File:1";
    assertEquals(ret, compare);
  }

  @Test
  public void testInvalidUserFilesTypes() {
    UserGoogleCharts ugc = new UserGoogleCharts();
    String ret = ugc.userFileTypes("92131234342");
    System.out.println(ret);
    String compare = "";
    assertEquals(ret, compare);

  }

  @Test
  public void testInvalidUserDocTypes() {
    UserGoogleCharts udt = new UserGoogleCharts();
    String ret = udt.userDocTypes("9213121232131");
    String compare = "";
    assertEquals(ret, compare);
  }
}
