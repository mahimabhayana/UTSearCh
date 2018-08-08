package UTSearCh;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class HtmlTxtFileParser implements DocumentParser{

	/*
	 * Parses the data inside an html or txt file
	 * @param file: A file to be parsed.
	 * @return The string of the file's contents
	 */
	public String parseDoc(File file) {
		// if not txt or html
		if (!file.getName().toLowerCase().contains(".txt") && !file.getName().toLowerCase().contains(".html")) {
			return null;
		}
		// init scanner
		Scanner scanner = null;
		try {
			// try to scan the file in
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// if file is txt file
		if (file.getName().toLowerCase().contains(".txt")) {
			// return the scanned text from the file
			return scanner.useDelimiter("\\A").next();
		// html
		} else {
			// parse the html doc
			org.jsoup.nodes.Document doc = Jsoup.parse(scanner.useDelimiter("\\A").next());
			String ret = "";
			// get all the elements
			for (Element elm: doc.getAllElements()) {
				// if there is some text in this element
				if (!elm.ownText().equals("")) {
					// add the text to teh return value, newline
					ret += elm.ownText() + "\n";
				}
			}
			// return text
			return ret;
		}
	}
	
}
