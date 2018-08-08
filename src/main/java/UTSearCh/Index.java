package UTSearCh;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import Database.SQLiteJDBC;
import GoogleSignIn.LoginServlet;

@WebServlet("/index")
public class Index extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// connect to the database
    public SQLiteJDBC db = LoginServlet.db;
	private IndexWriter writer;
	private StandardAnalyzer analyzer;
	private Directory indexDirectory;
	
	/*
	 * Constructor for the index class
	 * @patam path: Path to the directory where the index files are stored
	 */
	public Index(String path) throws IOException {
		// create a file from the path
		File indexPath = new File(path);
		// if theres no folder
		if (!indexPath.exists()) {
			// try to make a new directory
			try{
		        indexPath.mkdir();
		    } // catch security error
		    catch(SecurityException se){
		        //handle it
		    }     
		}
		// open the directory
		indexDirectory = FSDirectory.open(Paths.get(path));
		// Having CharArraySet as empty will make sure stop words are being searched
		analyzer = new StandardAnalyzer(CharArraySet.EMPTY_SET);
		// new config
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		// new write with that config
		writer = new IndexWriter(indexDirectory, config);
	}
	
	/*
	 * Getter method for writer, mainly used for testing.
	 * @return the write for this index
	 */
	public IndexWriter getWriter() {
		return writer;
	}
	
	/*
	 * Closes the writer
	 */
	public void close() throws IOException {
		// close the writer
		writer.close();
	}
	
	/*
	 * Initializes the servlet
	 * @param config1: Servlet configuration variable
	 */
	public void init(ServletConfig config1) throws ServletException {
		// use the servlet init function
	    super.init(config1);
	    // log that it has started
	    System.out.println("Started: init");
	    try {
	    	// create the index
			createIndex("index",new HtmlTxtPdfFileFilter());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    // log that it has finished
	    System.out.println("Finished: init");
	  }
	
	/*
	 * Gets a given file's data and inserts it into a document to be
	 * added to the index.
	 * @param file: A file whose data will be stored in the index.
	 * @return the file to be added to the index
	 */
	public Document getDocument(File file) throws IOException, SQLException {
		String parsedDate;
		// filter
		HtmlTxtPdfFileFilter filter = new HtmlTxtPdfFileFilter();
		// null case
		if (file == null || !filter.accept(file)) {
			return null;
		}
		// document that we are going to add to the index
		Document doc = new Document();
		// document parser
		DocumentParser docParser = null;
		// title field in the index
		doc.add(new TextField("title", file.getName(), Field.Store.YES));
		// get file data from db
		ResultSet rs = db.getDocByName(file.getName());
		// if we found the file in the database
		if (rs != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime date = LocalDateTime.now();
			parsedDate = date.format(formatter);
			// The dateTime and rating will be null for the 3 testing files
			// so populate those fields
			if (rs.getString("DateTime")!= null){
				parsedDate = rs.getString("DateTime");
			}
			System.out.println(parsedDate);
			// set all the fields in the index to their corresponding db rows
			doc.add(new TextField("Name", rs.getString("Name"), Field.Store.YES));
			doc.add(new TextField("FileType", rs.getString("FileType"), Field.Store.YES));
			doc.add(new TextField("DocType", rs.getString("DocType"), Field.Store.YES));
			doc.add(new TextField("Course", rs.getString("Course"), Field.Store.YES));
			doc.add(new TextField("UploaderType", rs.getString("UploaderType"), Field.Store.YES));
			doc.add(new TextField("Uploader", rs.getString("Uploader"), Field.Store.YES));
			doc.add(new TextField("DateTime", parsedDate, Field.Store.YES));
			// Can't use an int point because it won't store the value of rating
			// hence using a stored field to retain its value
			doc.add(new StoredField("Rating", rs.getInt("Rating")));
		// didnt find the file in the db
		} else {
			// set everything to n/a
			doc.add(new TextField("Name", "N/A", Field.Store.YES));
			doc.add(new TextField("FileType", "N/A", Field.Store.YES));
			doc.add(new TextField("DocType", "N/A", Field.Store.YES));
			doc.add(new TextField("Course", "N/A", Field.Store.YES));
			doc.add(new TextField("UploaderType", "N/A", Field.Store.YES));
			doc.add(new TextField("Uploader", "N/A", Field.Store.YES));
			doc.add(new TextField("DateTime", "N/A", Field.Store.YES));
			// Can't use an int point because it won't store the value of rating
			// hence using a stored field to retain its value
			doc.add(new StoredField("Rating", 0));
		}
		// file contents init
		String contents = "";
		// txt or html file
		if (file.getName().contains(".txt") || file.getName().contains(".html")) {
			// doc parser is the html/txt parser
			docParser = new HtmlTxtFileParser();
		} else if (file.getName().contains(".pdf")){
			// Code to read pdfs, we'll check this later
			docParser = new PdfFileParser();
		// not an accepted file
		} else {
			return null;
		}
		// get the parsed contents of the file
		contents = docParser.parseDoc(file);
		// contents field
		doc.add(new TextField("contents", contents, Field.Store.YES));
		// path field
		doc.add(new TextField("path", file.getAbsolutePath(), Field.Store.YES));
		// return the document
		return doc; 
	}
	
	/*
	 * Indexes the given file
	 * @param file: File to be indexed
	 * @return whether the file has been indexed or not (String)
	 */
	String indexFile(File file) throws IOException, SQLException {
		// return text to let us know it worked
		String ret = "";
		// get the document to be added to the index
	    Document document = getDocument(file);
	    // new filter
	    HtmlTxtPdfFileFilter filter = new HtmlTxtPdfFileFilter();
	    // if we got a document back
	    if (document != null && filter.accept(file)) {
	    	// log the file being indexed
			System.out.println("Indexing "+file.getCanonicalPath());
		    // write the doc to the index
		    writer.addDocument(document);
		    // update return text
		    ret = "indexed";
	    }
	    return ret;
	   }
	
	/*
	 * Creates a new index
	 * @param dataDirPath: Directory where the index files will be stored
	 * @param filter: Filter object used to ensure proper file type
	 * @return the number of docs in the writer
	 */
	public int createIndex(String dataDirPath, FileFilter filter) 
		      throws IOException, SQLException {
		// new file for the data path directory
				File dataDir = new File(dataDirPath);
				// if the directory doesnt exist
				if (!dataDir.exists()) {
					// make the directory
					dataDir.mkdir();
				}
		      //get all files in the data directory
		      File[] files = new File(dataDirPath).listFiles();
		      // go through all files in directory
		      for (File file : files) {
		    	  // log which file we're checking
		    	  System.out.println("checking " + file.getName());
		    	  // make sure its a file, not hidden, exists, readable, abd accepted
		         if(!file.isDirectory()
		            && !file.isHidden()
		            && file.exists()
		            && file.canRead()
		            && filter.accept(file)
		         ){
		        	 // index the file
		            indexFile(file);
		         }
		      }
		      //db.closeConnection();
		      // return number of docs that were indexed
		      return writer.numDocs();
		   }
}
