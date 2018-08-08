package UTSearCh;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.fileupload.FileItem;

public class HtmlTxtPdfFileFilter implements FileFilter{
	
	/*
	 * Returns true if the file is an accepted type for this application
	 * @param pathname: String of path to file.
	 * @return true iff the servlet accepts this file
	 */
	public boolean accept(File pathname){
		// instantiate mime
		String mime = "";
		try {
			// try to get the mime type
			mime = Files.probeContentType(pathname.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// instantiate if we have a good mime type
		boolean goodMime = false;
		// if we found a mime type
		if (mime != null) {
			// check if its a good mime
			goodMime = mime.contains("text/plain") || mime.contains("html") || mime.contains("pdf");
		}
		// path lowercased
		String lowerPath = "";
		// if we have a pathanme
		if (pathname.getName() != null) {
			// lowercase it, set it
			lowerPath = pathname.getName().toLowerCase();
		}
		// right extension and not null
	      return pathname.getName() != null && !lowerPath.endsWith("rtf") && (lowerPath.endsWith(".txt") || lowerPath.endsWith(".html") 
	    		  || lowerPath.endsWith(".pdf") || goodMime);
	   }
	
	/*
	 * Returns true if the file is an accepted type for this application
	 * @param pathname: String of path to file.
	 * @return true iff the serlvet accepts this file
	 */
	public boolean accept(FileItem pathname) {
		// mime type
		String mime = pathname.getContentType();
		// instantiate if its a good mime or not
		boolean goodMime = false;
		// if we got a mime
		if (mime != null) {
			// check if its a good mime
			goodMime = mime.contains("text/plain") || mime.contains("html") || mime.contains("pdf");
		}
		// lowercased path
		String lowerPath = "";
		// if we have a path name
		if (pathname.getName() != null) {
			// lowercase it, set it
			lowerPath = pathname.getName().toLowerCase();
		}
		// right extension and not null
		return pathname.getName() != null && !lowerPath.endsWith("rtf") &&  (lowerPath.endsWith(".txt") 
				|| lowerPath.endsWith(".html") || lowerPath.endsWith(".pdf") || goodMime) ;
	   }
}
