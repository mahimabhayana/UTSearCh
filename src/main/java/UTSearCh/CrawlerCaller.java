package UTSearCh;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@WebServlet("/crawler")
public class CrawlerCaller extends HttpServlet{
	
	public static String crawlCaller = "CrawlCaller";
	public static String crawlSub = "CrawlSub";
	
	@Override
	/*
	 * Get request to start the crawler with the given URL.
	 * @param req The request data.
	 * @param resp The response data.
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
	         throws ServletException, IOException {
	    // get the query params
	    String path = req.getParameter("path");
	    String user = req.getParameter("user");
	    String sub = req.getParameter("token");
	    // set static vars for the crawler class to pick up
	    crawlCaller = user;
	    crawlSub = sub;
	    // crawler config
	    CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder("Crawl");
        // for now, depth 2, pages 25, dont want to waste too much time
        config.setMaxDepthOfCrawling(2);
        config.setMaxPagesToFetch(25);
        config.setIncludeBinaryContentInCrawling(Boolean.TRUE);
        config.setPolitenessDelay(150);
        // more config
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller;
		try {
			// start the crawl
			controller = new CrawlController(config, pageFetcher, robotstxtServer);
			controller.addSeed(path);
	        controller.start(Crawler.class, 1);
	        // recreate the index once its done
	        new Uploader().createIndex();
	        // allow CORS, eliminates errors on the frontend
	        resp.addHeader("Access-Control-Allow-Origin", "*");
	        // let the frontend know were done
	        resp.getWriter().write("Done");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	
}
