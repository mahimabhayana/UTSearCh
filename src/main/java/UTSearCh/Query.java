package UTSearCh;

import java.io.IOException;
import java.nio.file.Paths;

import java.util.Formatter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.*;

@WebServlet("/search")
public class Query extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static StandardAnalyzer analyzer;
	private static Directory indexDirectory;


	/*
	 * Get request to /index, this was used for testing mainly.
	 * @param req: Request data passed to the servlet
	 * @param resp: Response data form the servlet
	 */

	@Override
	// Query function
	public void doGet(HttpServletRequest req, HttpServletResponse resp) 
	         throws ServletException, IOException{
			JSONObject rootobj = new JSONObject();
			JSONArray resultList = new JSONArray();
	 	  String final_contents="";
			// json application
	    resp.setContentType("application/json");
	    // get the query params
	    String querystr = req.getParameter("query");
	    String match = req.getParameter("match");
	    String filterDocs = req.getParameter("checkedBox");
	    String course = req.getParameter("course");
	    String user = req.getParameter("user");
			rootobj.put("query", querystr);
			// set index vars
	    analyzer = new StandardAnalyzer(CharArraySet.EMPTY_SET);
	    indexDirectory = FSDirectory.open(Paths.get("index"));
	    // if no filters, turn all the filters on
			if (filterDocs == null || filterDocs.equals("")) {
	    	filterDocs = "Test File Assignment/Exercise Courses Past exam Notes CrawlerDoc";
	    }
	    try {
				// Store the fields in an array and pass that as the first param, second is the analyzer
				MultiFieldQueryParser q = new MultiFieldQueryParser(new String[]{"title","contents"}, analyzer);
				// By default MultiFieldQueryParser uses OR operator
				// Toggle the below line to switch it to AND operator so both words must exist
				q.setDefaultOperator(MultiFieldQueryParser.Operator.AND);
				long startTime = System.currentTimeMillis();
				// exact match
				if (match.equals("Exact Match")) {
					// words have to be right beside each other
					q.setPhraseSlop(0);
				// word match or no match
				} else if (match.equals("Word Match") || match.equals("No Match")) {
					// look for any of the words
					q.setDefaultOperator(MultiFieldQueryParser.Operator.OR);
				} 
				// parse the query
				org.apache.lucene.search.Query final_q = q.parse(querystr);
				// 10 hits per page
	    	int hitsPerPage = 10;
	    	// open index related stuff
	    	IndexReader reader = DirectoryReader.open(indexDirectory);
	    	IndexSearcher searcher = new IndexSearcher(reader);
	    	// docs we findd
	    	TopDocs docs = searcher.search(final_q, hitsPerPage);
	    	// Highlight the query code
				SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter();
				//It scores text fragments by the number of unique query terms found
				//Basically the matching score in layman terms
				QueryScorer scorer = new QueryScorer(final_q);
				//used to markup highlighted terms found in the best sections of a text
				Highlighter highlighter = new Highlighter(htmlFormatter, scorer);
				//It breaks text up into same-size texts but does not split up spans
				Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 10);
				//set fragmenter to highlighter
				highlighter.setTextFragmenter(fragmenter);
				// end time of the search
				long endTime = System.currentTimeMillis();
				// scores
				ScoreDoc[] hits = docs.scoreDocs;
				// time it took to query
				long timeTaken = endTime - startTime;
				rootobj.put("hits_found",hits.length);
				rootobj.put("time_taken",timeTaken);
				// if no match
				if (match.equals("No Match")) {
					// get the number of docs not match
					int nonmatches = reader.maxDoc() - hits.length;
					int i = 0;
					int found = 0;
					// go through all the docs until we find all the non matches
					while(i<reader.maxDoc() && found < nonmatches) {
						// save docid
						//int docid = hits[i].doc;
						// get doc
						Document d = reader.document(i);
						boolean flag = true;
						// go through all docs
						for (ScoreDoc doc: hits) {
							// if this doc had a match
							if (doc.doc == i) {
								// turn the flag off
								flag = false;
							}
						}
						// didnt have a match
						if (flag) {
							// filters has this files type, course, or uploader
							if (filterDocs.contains(d.get("DocType")) && (course == null || (course != null && course.equals(d.get("Course")))) && (user == null || (user != null && user.equals(d.get("Uploader"))))){
								found ++;
								//Create token stream
								String text = d.get("contents");
								TokenStream stream = TokenSources.getAnyTokenStream(reader, i, "contents", analyzer);
								//Get highlighted text fragments
								try{
									String[] content = highlighter.getBestFragments(stream, text, 10);
									final_contents = String.join(" ", content);
								} catch (InvalidTokenOffsetsException e){
									System.err.println("InvalidTokenOffsetsException: " + e.getMessage());
								}
								// put all its data into the json object
								resultList.put(new JSONObject().put("title",d.get("title"))
										.put("path", d.get("path"))
										.put("doc_type", d.get("DocType"))
										.put("name", d.get("Name"))
										.put("course", d.get("Course"))
										.put("uploader", d.get("Uploader"))
										.put("uploader_type", d.get("UploaderType"))
										.put("file_type", d.get("FileType"))
										.put("contents", final_contents)
										.put("doc_upload_time", d.get("DateTime"))
										.put("rating", d.get("Rating")));
							}
						}
						i++;
					}
					// put the results into the root obj
					rootobj.put("results", resultList);
					// the the root obj to writer
					resp.getWriter().write(rootobj.toString());
				// exact or word match
				} else {
					// num of found docs
					int foundNum = 0;
					// go through all the hits
					for(int i=0;i<hits.length;++i) {
						// doc id
						int docId = hits[i].doc;
						// current doc
						Document d = searcher.doc(docId);
						// doctype, course, and uploader searches
						if (filterDocs.contains(d.get("DocType")) && (course == null || (course != null && course.equals(d.get("Course")))) && (user == null || (user != null && user.equals(d.get("Uploader"))))){
							foundNum++;
							//Create token stream
							String text = d.get("contents");
							TokenStream stream = TokenSources.getAnyTokenStream(reader, docId, "contents", analyzer);
							//Get highlighted text fragments
							try{
								String[] content = highlighter.getBestFragments(stream, text, 10);
								final_contents = String.join(" ", content);
							} catch (InvalidTokenOffsetsException e){
								System.err.println("InvalidTokenOffsetsException: " + e.getMessage());
							}
							// add it to the result list json obj
							resultList.put(new JSONObject().put("title",d.get("title"))
							.put("path", d.get("path"))
							.put("doc_type", d.get("DocType"))
							.put("name", d.get("Name"))
							.put("course", d.get("Course"))
							.put("uploader", d.get("Uploader"))
							.put("uploader_type", d.get("UploaderType"))
							.put("file_type", d.get("FileType"))
							.put("contents", final_contents)
							.put("doc_upload_time", d.get("DateTime"))
							.put("rating", d.get("Rating")));
						}
					}
					// add resultlist to results
					rootobj.put("results", resultList);
					// write results
					resp.getWriter().write(rootobj.toString());
				}
	    }
	    catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}

}
