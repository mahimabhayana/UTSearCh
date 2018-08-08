package UTSearCh;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.io.FileUtils;

import Database.SQLiteJDBC;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import sun.security.provider.SecureRandom;

public class Crawler extends WebCrawler{
	
	private static final long serialVersionUID = 1L;
	public SQLiteJDBC db = null;

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|png|jpg|jpeg|gif|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|rm|smil|wmv|swf|wma|zip|rar|gz|xml|json|jsonp))$");
	
	/*
	 * Returns true iff the crawler should visit the url
	 * @param referringPage The page referring the url
	 * @param url the url to determine if we should visit
	 * @return true iff the page has utoronto.ca in it and it is html txt pdf
	 */
	public boolean shouldVisit(Page referringPage, WebURL url) {
		// lowercase the url
		String givenURL = url.getURL().toLowerCase();
        // check if url doesnt match the filter
        boolean ret = !FILTERS.matcher(url.getURL().toLowerCase()).matches();
        // if the given url starts valid
        if (isValid(givenURL)) {
        	// if url ends with /
        	if (givenURL.endsWith("/")) {
        		URL url2;
        		String urlType = "";
				try {
					// new url
					url2 = new URL(givenURL);
					// open connection to it, get the content type
					urlType = url2.openConnection().getContentType();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// if the content type is not pdf, image, or doc
        		if (urlType != null && !urlType.contains("text") && !urlType.contains("html") && !urlType.contains("pdf")) {
        			// we dont want to visit
                	return false;
                }
        	}
        	// return if it passed through the filter
        	return ret;
        // didnt start valid
        } else {
        	// not valid
        	return false;
        }
	}
	
	/*
	 * Returns true if the given URL starts with a valid prefix
	 * @param url The url of a site page
	 */
	public boolean isValid(String url) {
		// is valid is utoronto.ca is in it
		return url.contains("utoronto.ca");
	}
	
	/**
     * Classes that extends WebCrawler can overwrite this function to process
     * the content of the fetched and parsed page.
     *
     * @param page the page object that is just fetched and parsed.
     */
    public void visit(Page page) {
    	// get the url
    	String url = page.getWebURL().getURL();
    	// replace , with _ as per handout
        url = url.replaceAll(",", "_");
        // get content type
        String type = page.getContentType();
        String fileType = "";
        // get the actual string url of the page were visiting
        String urlstr = page.getWebURL().getURL();
        // get the name of the file before the extension
        String fName = urlstr.substring(0, urlstr.lastIndexOf("."));
        // cut of the usless url before the filename
        fName = fName.substring(fName.lastIndexOf("/") + 1, fName.length());
        // new doc to upload
        Document docToUpload = new Document();
        // html
        if (type.contains("html")) {
        	// set file types
        	fileType = ".html";
        	docToUpload.fileType = "HTML";
        // pdf
        } else if (type.contains("pdf")) {
        	// set file types
        	fileType = ".pdf";
        	docToUpload.fileType = "PDF";
        // txt
        } else if (type.contains("text")){
        	// set file types
        	fileType = ".txt";
        	docToUpload.fileType = "TXT";
        }
        // if we got a file type
        if (!fileType.equals("")) {
        	// set all the values
	        docToUpload.docType = "CrawlerDoc";
	        docToUpload.fileName = fName + fileType;
	        docToUpload.course = "CR4W13R";
	        docToUpload.name = fName;
	        // static var from the crawling user, which got this in the query string
	        docToUpload.uploader = CrawlerCaller.crawlCaller;
	        // static var for the users token, which got this in the query string
	        docToUpload.uploaderType = SQLiteJDBC.getDBReference("UTSearchDB").getAccountType(CrawlerCaller.crawlSub);
	        docToUpload.token = CrawlerCaller.crawlSub;
	        // if theres a db
	        if (db != null) {
	        	// set the db to upload
	        	docToUpload.setDB(db);
	        }
	        try {
	        	// write the files data to the data folder
				FileUtils.writeByteArrayToFile(new File("data/" + fName + fileType), page.getContentData());
				System.out.println("wrote to file");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        // add the doc to the db
	        docToUpload.addToDB();
	        // number of outlinks is 0
	        int numOutLinks = 0;
	        // html parsed data
	        if (page.getParseData() instanceof HtmlParseData) {
	        	// get the parsed data
	            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
	            // set number of outgoing urls
	            numOutLinks = htmlParseData.getOutgoingUrls().size();
	        }
        }
	}
}
