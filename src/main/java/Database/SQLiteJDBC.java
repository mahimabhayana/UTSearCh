package Database;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.*;


public class SQLiteJDBC {
    private static Connection c = null;
    //private Statement stmt = null;
    //private static ResultSet rs = null;
    private static SQLiteJDBC db = null;

    public SQLiteJDBC(String filename) {
        // try to connect to DB
        try {
            // find jar file
            Class.forName("org.sqlite.JDBC");
            //c= DriverManager.getConnection("jdbc:sqlite:UTSearChDB.sqlite");
            c = DriverManager.getConnection("jdbc:sqlite:" + filename + ".sqlite");
            System.out.println("Connected to DB");
            //c.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static SQLiteJDBC getDBReference(String filename) {
        if(db == null) {
            db = new SQLiteJDBC(filename);
        }
        return db;
    }

    /**
     * This method closes the database connection
     */
    public void closeConnection() {
        try {
            c.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * This method takes in a UPDATE/INSERT/DELETE query and executes it
     *
     * @param query the query to be executed
     */
    public void executeQuery(String query) {
        try {
            Statement stmt = c.createStatement();
            // for SELECT use executeQuery instead
            stmt.executeUpdate(query);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * This method returns true iff account associated with given String sub exists in the database, otherwise
     * return false
     *
     * @param sub the unique identifier for a google account
     * @return true iff account with given String sub is in database
     */
    public boolean accountExists(String sub) {
        boolean ret = true;
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1 FROM Accounts where sub = '" + sub + "'");
            if (!rs.isBeforeFirst()) {
                // if this runs, the result set is empty
                ret = false;
            }
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;

    }
    
    /**
     * This method returns a string which is the type of the user's token that was inputted.
     *
     * @param token the unique identifier for a google account
     * @return account type iff user is in db
     */
    public String getAccountType(String token) {
    	// empty string
        String ret = "";
        try {
        	// get the sub from the inputted token
        	String sub = getSubFromToken(token);
        	// new statement
            Statement stmt = c.createStatement();
            // get the first user with that sub
            ResultSet rs = stmt.executeQuery("SELECT type FROM Accounts where sub = '" + sub + "' limit 1");
            // get the users type
            ret = rs.getString("type");
            // close result set
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;

    }
    
    /**
     * This method returns a string which is the type of the user's token that was inputted.
     *
     * @param token the unique identifier for a google account
     * @return account type iff user is in db
     */
    public String getAccountTypeByName(String firstname,String lastname) {
    	// empty string
        String ret = null;
        try {
        	// new statement
            Statement stmt = c.createStatement();
            // get the first user with that sub
            ResultSet rs = stmt.executeQuery("select type from Accounts where UPPER(firstname) = '" + firstname.toUpperCase() + "' and UPPER(lastname) = '" + lastname.toUpperCase() + "' limit 1");
            // get the users type
            ret = rs.getString("type");
            // close result set
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;

    }

    /**
     * This method returns true iff account associated with given gmail exists in the database, otherwise
     * return false
     *
     * @param email the google account
     * @return true iff account with given String sub is in database
     */
    public boolean accountExistsGmail(String email) {
        boolean ret = true;
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1 FROM Accounts where email = '" + email + "'");
            if (!rs.isBeforeFirst()) {
                // if this runs, the result set is empty
                ret = false;
            }
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;

    }

    /**
     * This method returns true iff account associated with given uoft mail exists in the database, otherwise
     * return false
     *
     * @param email the uoft email account
     * @return true iff account with given String sub is in database
     */
    public boolean accountExistsUtormail (String email) {
        boolean ret = true;
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1 FROM Accounts where utorEmail = '" + email + "'");
            if (!rs.isBeforeFirst()) {
                // if this runs, the result set is empty
                ret = false;
            }
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;

    }

    /**
     * This method returns true iff account associated with given String sub exists in the database, otherwise
     * return false
     *
     * @param name is full name the google account
     * @return true iff account with given String sub is in database
     */
    public boolean accountExistsName(String name) {
        boolean ret = true;
        String firstName = name.split(" ")[0];
        String lastName = name.split(" ")[1];
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1 FROM Accounts where firstname = '" + firstName + "' AND lastname = '" + lastName + "'");
            if (!rs.isBeforeFirst()) {
                // if this runs, the result set is empty
                ret = false;
            }
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;

    }

    /**
     * This methods adds an account to the database
     *
     * @param sub       the unique id of a google account
     * @param firstname the first name of user
     * @param lastname  the last name of user
     * @param gmail     the gmail of user
     * @param uoftMail  the uoft email of user
     * @param type  the type of account
     */
    public void addAccount(String sub, String firstname, String lastname, String gmail, String uoftMail, String type, int verified) {
        try {
            Statement stmt = c.createStatement();
            String queryadd = "INSERT INTO Accounts (sub, firstname, lastname, email, utorEmail, type, verified) VALUES ('" + sub + "', '" + firstname + "', '" + lastname + "', '" + gmail + "', '" +uoftMail + "', '" + type + "', '" + verified + "')";
            stmt.executeUpdate(queryadd);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * This methods adds an account to the database
     *
     * @param sub the unique id of a google account
     * @return the full name of the user at that sub
     * 
     */
    public String getAccountName(String sub) {
    	String ret = "";
        try {
            Statement stmt = c.createStatement();
            String getUser = "select * from Accounts where sub = '" + sub + "' limit 1";
            ResultSet rs = stmt.executeQuery(getUser);
            ret = rs.getString("firstname") + " " + rs.getString("lastname");
            rs.close();
            return ret;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "failed";
    }

    /**
     * Returns true iff the session is already in database
     *
     * @param sub of the user
     * @return true if session exists in database
     */
    public boolean sessionExists(String sub) {
        boolean ret = true;
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1 FROM Sessions where sub = '" + sub + "'");
            if (!rs.isBeforeFirst()) {
                // this will run if empty
                ret = false;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // exists if code reaches here
        return ret;
    }

    /**
     * method to check if user's session is still valid
     *
     * @param token of the user
     * @param accessDateTime is time the user requested access. Format is 2018-07-08 20:06:25.460
     * @return true iff user's session is within expiry date
     */
    public boolean sessionValid(String token, String accessDateTime) {
        boolean ret = true;
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT date FROM Sessions where token = '" + token + "'");
            if (!rs.isBeforeFirst()) {
                // this will run if empty, return false since no session was created
                rs.close();
                return false;
            } else {
                // session exists, check if within time frame
                // create date time of given date
                String[] dateTime = accessDateTime.split("T");
                String[] date = dateTime[0].split("-");
                String[] time = dateTime[1].split(":");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                System.out.println("Time given:" + date[0] + "-"+date[1] + "-"+date[2] + "-" + time[0] + ":" + time[1] + ":"+ time[2]);
                LocalDateTime dateTimeGiven = LocalDateTime.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]), Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]));
                String givenFormattedDateTime = dateTimeGiven.format(formatter);
                // create date time of date in db
                String dbDate = rs.getString("date");
                String[] dateTimeDB = dbDate.split("T");
                String[] dateDB = dateTimeDB[0].split("-");
                String[] timeDB = dateTimeDB[1].split(":");
                System.out.println("DB expiry time:" + date[0] + "-"+dateDB[1] + "-"+dateDB[2] + "-" + timeDB[0] + ":" + timeDB[1] + ":"+ timeDB[2]);

                LocalDateTime dbDateTime = LocalDateTime.of(Integer.parseInt(dateDB[0]), Integer.parseInt(dateDB[1]), Integer.parseInt(dateDB[2]), Integer.parseInt(timeDB[0]), Integer.parseInt(timeDB[1]), Integer.parseInt(timeDB[2]));
                // compare date time
                rs.close();
                if (dateTimeGiven.isBefore(dbDateTime)) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



	/**
	 * Method to create a session for the user in database
	 * @param sub of the user
	 * @param expiryDate of the session
	 * @return true iff successfully created a session in db
	 */
	public boolean createSession(String sub, String expiryDate, String token) {
	  // check if in session already
	  try {
          Statement stmt = c.createStatement();
	      if (this.sessionExists(sub)) {
	        // session exists, delete it
	        if (!this.deleteSession(sub)) {
	          System.out.println("Error deleting old session");
	          System.exit(0);
	          return false;
	        }
	      }
	        // session does not exist, create new one
	      String queryadd = "INSERT INTO Sessions (sub, date, token) VALUES ('"+ sub + "',  '" + expiryDate + "', '" + token + "')";
	      stmt.executeUpdate(queryadd);
	      return true;
	    
	  } catch (SQLException e) {
	    System.out.println("Error: " + e.getMessage());
	    return false;
	  }
	}
	
	/**
	 * This method returns true iff account associated with given String sub exists in the database, otherwise
	 * return false
	 * @param name the name of the file
	 * @return true iff file with given name is in the db
	 */
	public boolean docExists(String name) {
		boolean ret = true;
		try {
            Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT 1 FROM Documents where FileName = '" + name +"'" );
			if (!rs.isBeforeFirst()) {
				// if this runs, the result set is empty
				ret = false;
			}
            rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
		
	}

	/**
	 * Method to delete user session from database
	 * @param sub of the user
	 * @return true iff successfully deleted user session
	 */
	public boolean deleteSession(String sub) {
	     try {
             Statement stmt = c.createStatement();
           String queryDelete = "DELETE FROM Sessions where sub = '" + sub + "'";
           stmt.executeUpdate(queryDelete);
           return true;
       } catch (Exception e) {
           System.out.println("Error: " + e.getMessage());
           return false;
       }
	}

    public boolean deleteSessionToken(String token) {
        try {
            Statement stmt = c.createStatement();
            String queryDelete = "DELETE FROM Sessions where token = '" + token + "'";
            stmt.executeUpdate(queryDelete);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
	
	/**
	 * This methods adds a document to the db
	 * @param name name of the file
	 * @param fileName exact name of the file
	 * @param uploaderType type of the person who uploaded Instructor/Student
	 * @param fileType type of file, html, txt, pdf
	 * @param docType type of document notes, exam etc
	 * @param course course the file relates to
	 */
	public boolean addDoc(String name,String fileName, String uploaderType, String fileType, String docType, String course,String uploader, String sub) {
		try {
			// if doc is already in db
			if (docExists(fileName)) {
				// put a 1 in the name
				fileName = fileName.substring(0, fileName.lastIndexOf(".")) + "1" + fileName.substring(fileName.lastIndexOf("."),fileName.length());
			}
			// rating init
            int rating = 0;
            // date formatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            // right now
            LocalDateTime date = LocalDateTime.now();
            // parse the date time to the format we selected
            String parsedDate = date.format(formatter);
            //LocalDateTime parsedDate = LocalDateTime.parse(text, formatter);
            // new stmt
                  Statement stmt = c.createStatement();
                  // query to insert user to db
                  String queryadd = "INSERT INTO Documents (Name, FileName, UploaderType, FileType, DocType, Course, Uploader, Sub, DateTime, Rating) VALUES"
                + " ('" + name +"', '" + fileName + "', '" + uploaderType +"', '" + fileType +"', '" + docType +"','" + course + "','" + uploader + "','" + sub + "','" + parsedDate + "','" + rating + "')";
                  // execute the adding query
                  stmt.executeUpdate(queryadd);
                  // went through
                  return true;
              } catch (Exception e) {
                  System.out.println("Error: " + e.getMessage());
                  // didnt go through
                  return false;
              }
	}
	
	/**
	 * This methods adds a document to the db
	 * @param name name of the file
	 * @return the result set from the query, data for a document
	 */
	public ResultSet getDocByName(String name) {
	    ResultSet rs = null;
		try {
			Statement stmt = c.createStatement();
			// sql staement to get all date from user by filename
			rs = stmt.executeQuery("SELECT Name, FileName, UploaderType, FileType, DocType, Course, Uploader, DateTime, Rating"
          + " FROM Documents where FileName = '" + name +"'" );
			if (!rs.isBeforeFirst()) {
				// if this runs, the result set is empty
                rs.close();
				return null;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 * Gets all the document related to the inputted course of the specifc uploadertype
	 * @param course the course code that you're searching for
	 * @param type the uploadertype being searched for
	 * @return the result set from the query, data for course documents
	 */
	public ResultSet getCourseDocs(String course,String type) {
	    ResultSet rs = null;
		try {
			Statement stmt = c.createStatement();
			// sql staement to get all date from user by filename
			rs = stmt.executeQuery("select * from Documents where Course = '" + course.toUpperCase() + "' and UploaderType = '" + type + "'");
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 * Gets all the data about all the docs uploaded by the given user
	 * @param name the name of the user
	 * @return the result set from the query, document data uploaded by user
	 */
	public ResultSet getUserDocs(String name) {
	    ResultSet rs = null;
		try {
			Statement stmt = c.createStatement();
			// sql staement to get all date from user by filename
			rs = stmt.executeQuery("select * from Documents where UPPER(Uploader) = '" + name.toUpperCase() + "'");
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 * Gets the sub for a user given their name
	 * @param firstname the firstname of the user
	 * @param lastname the lastname of the user
	 * @return The sub of the user, from the db
	 */
	public String getSubFromName(String firstname,String lastname) {
	    ResultSet rs = null;
		try {
			Statement stmt = c.createStatement();
			// sql staement to get all date from user by filename
			rs = stmt.executeQuery("select sub from Accounts where UPPER(firstname) = '" + firstname.toUpperCase() + "' and UPPER(lastname) = '" + lastname.toUpperCase() + "' limit 1");
			return rs.getString("sub");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the list of courses a user is teaching
	 * @param name the name of the user
	 * @return List of course theyre teaching
	 */
	public ResultSet getCoursesTaughtByUser(String name) {
	    ResultSet rs = null;
		try {
			Statement stmt = c.createStatement();
			// sql staement to get all date from user by filename
			rs = stmt.executeQuery("select Name from Course where UPPER(Instructor) = '" + name.toUpperCase() + "'");
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

    /**
     * Returns a string array of all user's courses
     * @param sub of the user to search
     * @return the list of courses the student has
     */
	public List<String> getUserCourses(String sub) {
        List<String> courses = new ArrayList<String>();
	    try {
            Statement stmt = c.createStatement();
	        String query = "SELECT course from StudentToCourses where sub = '" + sub + "'";

            ResultSet rs = stmt.executeQuery(query);
            // loop through results
            while (rs.next()) {
                // add column 1/course column into courses list
                courses.add(rs.getString(1));
            }
            rs.close();
        } catch (SQLException e) {
	        e.printStackTrace();
        }
        return courses;
    }

	/**
     * Adds a document to the db
     * @param sub the unique id for the user
     * @param filename the name of the file
     * @param name the name to be displayed to the users
     * @param docType type of document
     */
    public void addUserDocument(String sub, String filename, String name, String docType) {
	    try {
            Statement stmt = c.createStatement();
	        String query = "INSERT INTO StudentToDocuments(sub, FileName, Name, DocType) VALUES ('" + sub + "',  '" + filename + "', '" + name + "', '" + docType + "')";
	        stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Adds a document to the db
     * @param course the course code
     * @param name the instructor's name
     */
    public boolean addCourse(String course, String name) {
	    try {
            Statement stmt = c.createStatement();
            // sql to add course
	        String query = "INSERT INTO Course(Name, Instructor) VALUES ('" + course.toUpperCase() + "',  '" + name + "')";
	        stmt.executeUpdate(query);
	        return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Adds a document to the db
     * @param course the course code
     * @param name the instructor's name
     */
    public boolean addUserCourse(String sub, String course) {
	    try {
            Statement stmt = c.createStatement();
	        String query = "INSERT INTO StudentToCourses(sub, course) VALUES ('" + sub + "',  '" + course.toUpperCase() + "')";
	        stmt.executeUpdate(query);
	        return true;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    /*
     * Returns the list of courses currently being taught.
     * @return List of strings of all the course codes
     */
    public String getCourseInstructor(String course) {
    	String ret = "";
        try {
        	ResultSet rs = null;
            Statement stmt = c.createStatement();
            // sql to get the instructor for that course
            String query = "SELECT Instructor from Course where Name = '" + course + "' limit 1";
            rs = stmt.executeQuery(query);
            ret = rs.getString("Instructor");
            rs.close();
            return ret;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /*
     * Returns the list of courses currently being taught.
     * @return List of strings of all the course codes
     */
    public List<String> getCourses() {
        ResultSet rs = null;
        List<String> queries = new ArrayList<String>();
        try {
            Statement stmt = c.createStatement();
            String query = "SELECT Name from Course order by Name DESC";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                queries.add(rs.getString(1));
            }
            rs.close();
            return queries;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Returns the documents the user uploaded, more efficient because smaller table
     * @param sub is the email of the user
     * @return a string of the format name;filename which are the filenames of documents the student uploaded
     */
    public String getUserDocumentsEfficient(String sub) {
        //TODO: when user uploads document, have it added to db
        ResultSet rs = null;

        JSONObject rootobj = new JSONObject();
        JSONArray resultList = new JSONArray();

        try {
            Statement stmt = c.createStatement();
            String query = "SELECT Name, FileName, FileType, DocType, Course, Rating, DateTime from Documents where sub = '" + sub + "'";
            rs = stmt.executeQuery(query);
            // loop through results
            while (rs.next()) {
                resultList.put(new JSONObject().put("name", rs.getString(1))
                .put("path", rs.getString(2))
                .put("file_type", rs.getString(3))
                .put("doc_type", rs.getString(4))
                .put("course", rs.getString(5))
                .put("rating", rs.getString(6))
                .put("doc_upload_time", rs.getString(7)));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        rootobj.put("results", resultList);
        return rootobj.toString();

    }

    /**
     * Add the user's query in database to keep track
     * @param sub of the user
     * @param query the user searched
     * @param dateTime the time query searched at
     * @return true if successfully added, false ow
     */
    public boolean addUserQuery(String sub, String query, String dateTime) {
        try {
            Statement stmt = c.createStatement();
            String userQueryAdd = "INSERT INTO UserQueries (sub, query, date_time) VALUES ('"+ sub + "',  '" + query + "', '" + dateTime + "')";
            stmt.executeUpdate(userQueryAdd);
            return true;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public List<String> getUserQuery(String sub, int num) {
        ResultSet rs = null;
        List<String> queries = new ArrayList<String>();
        try {
            Statement stmt = c.createStatement();
            String query = "SELECT query from UserQueries where sub = '" + sub +"' order by date_time DESC limit " + num;
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                queries.add(rs.getString(1));
            }
            rs.close();
            return queries;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deletes all user queries associated with email
     * @param sub of the user
     * @return true iff successfuly deleted
     */
    public boolean deleteAllUserQueries(String sub) {
        try {
            Statement stmt = c.createStatement();
            String userQueryDelete = "DELETE FROM UserQueries where sub='" + sub + "'";
            stmt.executeUpdate(userQueryDelete);
            return true;

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Returns a map of doctype(notes, past exams, assignments, to number of times uploaded from the user
     * @param sub of the User
     * @return a hashmap containing doctype to number of uploads
     */
    public HashMap userDocTypes (String sub) {
        // file type ie notes, past exams, assignments, etc
        // hashmap store filetype to number of encounters
        HashMap<String, Integer> docMap = new HashMap<>();
        try {
            Statement stmt = c.createStatement();
            String query = "SELECT distinct FileName, DocType from Documents where Sub = '" + sub + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String docType = rs.getString(2);
                if(docMap.containsKey(docType)) {
                    // it contains file type so just increment
                    docMap.put(docType, docMap.get(docType) + 1);
                } else {
                    // it does not contain this type, so add it
                    docMap.put(docType, 1);
                }
            }
            rs.close();
            return docMap;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns the a map of filetype(html, pdf, txt) to number of times uploaded from the user
     * @param sub of the user
     * @return a hashmap containing filetype to number of uploads
     */
    public HashMap userFileTypes (String sub) {
        // doc types such as html, pdf, txt
        // hashmap store filetype to number of encounters
        HashMap<String, Integer> fileMap = new HashMap<>();
        try {
            Statement stmt = c.createStatement();
            String query = "SELECT distinct filename, FileType from Documents where Sub = '" + sub +"'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String fileType = rs.getString(2);
                if(fileMap.containsKey(fileType)) {
                    // it contains file type so just increment
                    fileMap.put(fileType, fileMap.get(fileType) + 1);
                } else {
                    // it does not contain this type, so add it
                    fileMap.put(fileType, 1);
                }
            }
            rs.close();
            return fileMap;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public String getSubFromToken (String token) {
        ResultSet rs = null;
        String sub = "";
        try {
            Statement stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT sub FROM Sessions where token = '" + token +"'" );
            if (!rs.isBeforeFirst()) {
                // if this runs, the result set is empty
                rs.close();
                return null;
            }
            sub = rs.getString(1);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sub;
    }

    /**
     * Delete the file with filename from database
     * @param filename of the file to delete
     * @return true iff success
     */
    public boolean deleteDocument(String filename) {
        try {
            Statement stmt = c.createStatement();
            String queryDelete = "DELETE FROM Documents where FileName = '" + filename + "'";
            stmt.executeUpdate(queryDelete);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Create a validation token for the email
     * @param sub of the gmail account
     * @param token randomly generated token to verify account
     */
    public void createValidationEntry (String sub, String token) {
        try {
            Statement stmt = c.createStatement();
            String queryadd = "INSERT INTO EmailValidation (sub, token) VALUES ('" + sub + "', '" + token + "')";
            stmt.executeUpdate(queryadd);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Returns true iff token exists in EmailValidation
     * @param token is the unique token given to email to verify
     * @return true iff token exists
     */
    public boolean validateEmail (String token) {
        boolean ret = true;
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1 FROM EmailValidation where token = '" + token + "'");
            if (!rs.isBeforeFirst()) {
                // if this runs, the result set is empty
                ret = false;
            }
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;

    }

    /**
     * Change the verified column of the account to TRUE
     * @param sub unique id of gmail to verify
     */
    public void verifyAccount (String sub) {
        try {
            Statement stmt = c.createStatement();
            String queryadd = "UPDATE Accounts SET verified = 1 where sub = '" + sub + "'" ;
            stmt.executeUpdate(queryadd);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Delete the validation entry form EmailValidation and return the sub of entry deleted
     * @param token associated with validation
     * @return
     */
    public String deleteValidationEntry (String token) {
        ResultSet rs = null;
        String retString = "";
        try {
            Statement stmt = c.createStatement();
            rs = stmt.executeQuery("SELECT sub FROM EmailValidation  where token = '" + token +"'" );
            if (rs.isBeforeFirst()) {
                retString = rs.getString(1);
            }
            stmt = c.createStatement();
            String queryDelete = "DELETE FROM EmailValidation where token = '" + token + "'";
            stmt.executeUpdate(queryDelete);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return retString;
    }

    /**
     * Delete the account with the given sub from the database
     * @param sub is the unique id of the google account
     */
    public void deleteAccount (String sub) {
        ResultSet rs = null;
        String retString = "";
        try {
            Statement stmt = c.createStatement();
            stmt = c.createStatement();
            String queryDelete = "DELETE FROM Accounts where sub = '" + sub + "'";
            stmt.executeUpdate(queryDelete);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Returns true iff account with given sub is verified
     * @param sub is the unique google account
     * @return true iff account is verified
     */
    public boolean isVerified(String sub) {
        boolean ret = true;
        try {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Accounts where sub = '" + sub + "' and verified = 1");
            if (!rs.isBeforeFirst()) {
                // if this runs, the result set is empty
                ret = false;
            }
            rs.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;

    }

}