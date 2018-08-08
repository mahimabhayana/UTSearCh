package UTSearCh;

import Database.SQLiteJDBC;
import GoogleSignIn.LoginServlet;

public class Document {
	// connect to the database
    private static SQLiteJDBC db = LoginServlet.db;
	public String docType = "N/A";
	public String name = "N/A";
	public String uploaderType = "Tester";
	public String fileType = "N/A";
	public String course = "N/A";
	public String fileName = "N/A";
	public String uploader = "N/A";
	public String token = "N/A";
	
	/*
	 * Returns true if documents was added document to database.
	 * @return true iff document added to db
	 */
	public boolean addToDB() {
		return db.addDoc(this.name, this.fileName, this.uploaderType, this.fileType, this.docType, this.course, this.uploader, db.getSubFromToken(this.token));
	}
	
	/*
	 * Returns true if document is in database
	 * @return true if doc is in db
	 */
	public boolean docInDB() {
		return db.docExists(this.fileName);
	}
	
	/*
	 * Sets the databse that this class uses
	 * @param db2 An sqlite database
	 */
	public void setDB(SQLiteJDBC db2) {
		db = db2;
	}

}
