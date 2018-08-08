package Users;

import Database.SQLiteJDBC;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@WebServlet("/usercharts")
public class UserGoogleCharts extends HttpServlet {

    SQLiteJDBC db = SQLiteJDBC.getDBReference("UTSearChDB");

    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        // get token and sub
        String token = req.getParameter("token");
        String sub = db.getSubFromToken(token);
        // format of
        // filetype:#,filetype:#;doctype:#,doctype:#
        String ft = userFileTypes(sub);
        String dt = userDocTypes(sub);
        String responseText = ft + ";" + dt;
        if (responseText != null) {
            System.out.println("uploads: " + responseText);
            resp.setContentLength(responseText.length());
            PrintWriter out = resp.getWriter();
            out.println(responseText);
            out.close();
            out.flush();
            System.out.println("Sent " + sub + "'s upload data");
        }
    }

    /**
     * Get user's associated with given sub the file types they've uploaded
     * @param sub the unique identifier of user
     * @return a string of the format filetype:# of uploads , filetype:# of uploads, ....
     */
    public String userFileTypes(String sub) {
        // get user file types (pdf, html, txt)
        HashMap<String, Integer> ft = db.userFileTypes(sub);
        // return a string of filetype:# of occurrences,filetype:# of occurrences, etc
        String retString = "";
        for (String fileType: ft.keySet()) {
            // fileType is key
            retString = retString + fileType + ":" + ft.get(fileType).toString() + ",";
        }
        // remove last comma
        if (retString.length() > 0) {
            retString = retString.substring(0, retString.length() - 1);
        }
        return retString;
    }

    /**
     * Get user's associated with given sub the doc types they've uploaded
     * @param sub the unique identifier of user
     * @return a string of the format doc type:# of uploads , doc type:# of uploads, ....
     */
    public String userDocTypes(String sub) {
        // get user's doc types (Notes, exams, etc)
        HashMap<String, Integer> dt = db.userDocTypes(sub);
        String retString = "";
        for (String fileType: dt.keySet()) {
            // fileType is key
            retString = retString + fileType + ":" + dt.get(fileType).toString() + ",";
        }
        // remove last comma
        if (retString.length() > 0) {
            retString = retString.substring(0, retString.length() - 1);
        }
        return retString;

    }

    /**
     * Set the database to connect to the database given
     * @param db the new database to connect to
     */
    public void setDb(SQLiteJDBC db) {
        this.db = db;
    }



}
