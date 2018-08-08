package UTSearCh;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

@WebServlet("/display")
public class DisplayFile extends HttpServlet {
    String dataPath;
    private final String INDEX_FOLDER = "/data/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // set header
        resp.setContentType("text/html");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        // get the path name
        String filename = req.getParameter("file");
        if (dataPath == null) {
            File currentDirectory = new File(new File(".").getAbsolutePath());
            System.out.println("can: " + currentDirectory.getCanonicalPath());
            dataPath = currentDirectory.getCanonicalPath() + INDEX_FOLDER;
            currentDirectory.delete();
        }
        String path = dataPath + filename;
        System.out.println("FILEPATH: " + path);
        //path = "file://" + path;
        System.out.println(path);

        // reads input file from an absolute path
        String filePath = path;
        File downloadFile = new File(filePath);
        System.out.println(downloadFile.exists());
        FileInputStream inStream = new FileInputStream(downloadFile);

        // obtains ServletContext
        ServletContext context = getServletContext();

        // gets MIME type of the file
        String mimeType = context.getMimeType(filePath);
        if (mimeType == null) {
            // set to binary type if MIME mapping not found
            mimeType = "application/octet-stream";
        }
        System.out.println("MIME type: " + mimeType);

        // modifies response
        resp.setContentType(mimeType);
        resp.setContentLength((int) downloadFile.length());

        // forces download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
        System.out.println(headerValue);
        resp.setHeader(headerKey, headerValue);

        // obtains response's output stream
        OutputStream outStream = resp.getOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        inStream.close();
        outStream.close();
    }

    }



