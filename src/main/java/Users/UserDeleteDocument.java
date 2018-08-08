package Users;

import Database.SQLiteJDBC;
import UTSearCh.Index;
import UTSearCh.Uploader;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.sql.SQLException;

@WebServlet("/deleteDocument")
public class UserDeleteDocument extends HttpServlet {
    private final String INDEX_FOLDER = "/data/";
    SQLiteJDBC db = SQLiteJDBC.getDBReference("UTSearChDB");

    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");

        System.out.println("req " + req.getQueryString());

        String token = req.getParameter("token");
        String filename = req.getParameter("filename");
        String sub = db.getSubFromToken(token);
        System.out.println("Requested to delete document " + filename + " from: " + sub);
        boolean accountExists = false;
        if (sub != null) {

            // check if account exists
            accountExists = db.accountExists(sub);
        }
        if (accountExists) {
            deleteFromIndex(filename);
        }

    }

    public void deleteFromIndex(String filename) {
        try {
            // delete from database
            if (db.deleteDocument(filename)) {
                System.out.println("Deleted " + filename + " from database!");
            } else {
                System.out.println("Unable to delete " + filename + " from database!");
            }
            // delete file from directory
            String file = FileSystems.getDefault().getPath("data").toAbsolutePath() + "/"+ filename;
            //delete file
            File del = new File(file);
            if(del.delete()) {
                System.out.println("Deleted " + file);
            }
            // create the index
            Uploader up = new Uploader();
            up.createIndex();
        } catch (IOException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
