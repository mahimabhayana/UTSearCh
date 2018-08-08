package UTSearCh;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Database.SQLiteJDBC;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

@MultipartConfig(location="/tmp", fileSizeThreshold=1024*1024,
        maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)
@WebServlet("/upload")
public class Uploader extends HttpServlet{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    String indexDir = "index";
    String dataDir = "data";
    SQLiteJDBC db = SQLiteJDBC.getDBReference("UTSearChDB");
    // document to upload
    public Document docToUpload = new Document();
    /*
     * Post request which uploads file to the server using mutlipart/form-data
     * @param req Request data
     * @param resp Response data
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            setAccessControlHeaders(resp);
            ServletFileUpload sf = new ServletFileUpload(new DiskFileItemFactory());
            // parse the request
            Collection<FileItem> files = sf.parseRequest(req);
            // new filter to check if the file is the right type
            HtmlTxtPdfFileFilter filter = new HtmlTxtPdfFileFilter();
            // flag will tell us if we are uploading doc
            boolean flag = false;
            // data folder
            File dataPath = new File(dataDir);
            // if we dont have a data folder
            if (!dataPath.exists()) {
                try{
                    // make a new one
                    dataPath.mkdir();
                }
                catch(SecurityException se){
                    //handle it
                }
            }
            // go through all the items in the multipart formdata
            for (FileItem item : files) {
                // if it is an html pdf or txt doc
                if (filter.accept(item)) {
                	// get content type
                    String mime = item.getContentType();
                    // set the file name
                    docToUpload.fileName = item.getName();
                    // if the doc is already in the db
                    if (docToUpload.docInDB()) {
                    	// add a 1 in the name, before the extension
                        docToUpload.fileName = docToUpload.fileName.substring(0,docToUpload.fileName.lastIndexOf(".")) + "1"
                                + docToUpload.fileName.substring(docToUpload.fileName.lastIndexOf("."),docToUpload.fileName.length());
                    }
                    // write the file to the server
                    item.write(new File(dataDir + "/" + docToUpload.fileName));
                    // if it ends with html
                    if (item.getName().toLowerCase().endsWith("html")  || ( mime != null && mime.contains("html"))) {
                        // set file type
                        docToUpload.fileType = "HTML";
                        // pdf or txt
                    } else {
                        if (item.getName().toLowerCase().endsWith("txt") || item.getName().toLowerCase().endsWith("pdf")) {
                            // last three letters of file capitalized
                            docToUpload.fileType = item.getName().toLowerCase().substring(item.getName().length() - 3).toUpperCase();
                        // mime is txt
                        } else if (mime != null && mime.contains("text")) {
                            docToUpload.fileType = "TXT";
                        // pdf
                        } else {
                            docToUpload.fileType = "PDF";
                        }
                    }
                    // we're going to upload it
                    flag = true;
                    System.out.println("uploaded " + item.getName());
                    // if the current part is a form field, not a file
                } else if (item.isFormField()) {
                    // title field
                    if (item.getFieldName().equals("title")) {
                        // set the name
                        docToUpload.name = item.getString();
                        // doc type field
                    } else if (item.getFieldName().equals("document-type")) {
                        // set the doc type
                        docToUpload.docType = item.getString();
                        // course field
                    } else if (item.getFieldName().equals("course")) {
                        // set the course
                        docToUpload.course = item.getString();
                    } else if (item.getFieldName().equals("user")) {
                        // set the course
                        docToUpload.uploader = item.getString();
                    } else if (item.getFieldName().equals("token")) {
                        // get sub
                    	String token = item.getString();
                        docToUpload.token = token;
                        docToUpload.uploaderType = db.getAccountType(token);
                    }
                } else {
                	resp.setStatus(500);
                	resp.getWriter().write("Wrong file type");
                	return;
                }
            }
            // uploading file and its not already in the db
            if (flag && !docToUpload.docInDB()) {
                System.out.println("adding to db");
                // this block is mainly used for testing
                // if there is a title param
                if (req.getParameter("title") != null) {
                    // set title
                    docToUpload.name = req.getParameter("title");
                }
                // if there is a doc type param
                if (req.getParameter("document-type") != null) {
                    // set doc type
                    docToUpload.docType = req.getParameter("document-type");
                }
                // if there is a course param
                if (req.getParameter("course") != null) {
                    // set course
                    docToUpload.course = req.getParameter("course");
                }
                // if there is a user param
                if (req.getParameter("user") != null) {
                    // set course
                    docToUpload.uploader = req.getParameter("user");
                }
                
                // if there is a uploader type param
                if (req.getParameter("uploaderType") != null) {
                    // set type
                    docToUpload.uploaderType = req.getParameter("uploaderType");
                }

                if (req.getParameter("token") != null) {
                	String token = req.getParameter("token");
                    docToUpload.token = token;
                    if (!token.equals("test")) {
                    	docToUpload.uploaderType = db.getAccountType(token);
                    }
                }
                // add the documents data to the db
                docToUpload.addToDB();
            }
            createIndex();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /*
     * Creates a new index on get request. Mainly used for testing
     * @param req Request data
     * @patam resp Response data
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            createIndex();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
     * Creates a new index, with the files in the data folder.
     */
    public void createIndex() throws IOException, SQLException {
    	// number of files indexed, instantiation
        int numIndexed;
        // make a new file from the index directory
        File indexFile = new File(indexDir);
        // if the indexfile doesnt exist
        if (!indexFile.exists()) {
            // make the file
            indexFile.mkdir();
        }
        // new index in the index directory
        Index index = new Index(indexDir);
        // delete everything in the writer
        index.getWriter().deleteAll();
        // start time
        long startTime = System.currentTimeMillis();
        // get number of files indexed
        numIndexed = index.createIndex(dataDir, new HtmlTxtPdfFileFilter());
        // end time
        long endTime = System.currentTimeMillis();
        // close index
        index.close();
        // debug
        System.out.println(numIndexed+" File indexed, time taken: "
                +(endTime-startTime)+" ms");
    }

    /*
     * Sets the access control headers to avoid front end issues.
     * @param resp The response data
     */
    private void setAccessControlHeaders(HttpServletResponse resp) {
    	// set response headers to alleviate frontend errors
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE");
    }
}
