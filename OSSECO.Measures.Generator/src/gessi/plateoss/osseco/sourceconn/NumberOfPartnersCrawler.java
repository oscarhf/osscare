package gessi.plateoss.osseco.sourceconn;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * <dl>
 * <dt>Purpose: Obtain the pages(s) with the data crawled
 * <dd>
 *
 * <dt>Description:
 * <dd>This class implement a specific Web crawler to retrieve information from
 * an OSSECO web site.
 * <dt>Example usage:
 * <dd>
 * 
 * <pre>
 * </pre>
 * 
 * </dd>
 * </dl>
 * 
 * @version v0.1, 2016/03/30 (March) -- first release
 * @author Oscar Franco-Bedoya
 *         (<a href="oscarhf2002@hotmail.com">oscarhf2002.@hotmail.com</a>)
 */

public class NumberOfPartnersCrawler extends WebCrawler {

	private static final Logger logger = LoggerFactory.getLogger(NumberOfPartnersCrawler.class);
	/**
	 * This extensions are not crawled
	 */
	private static final Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	/**
	 * HTML text with the crawler result
	 */
	private StringBuilder html;
	/**
	 * Filter for the web pages visited
	 */
	private String filter;
	/**
	 * Stop condition for crawling process
	 */
	private String stopCondition;

	/**
	 * get the crawler filter
	 * 
	 * @return The crawler filer
	 */

	public String getFilter() {
		return filter;
	}

	/**
	 * Set the crawler filter
	 * 
	 * @param filter
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * Get the stop condition
	 * 
	 * @return the stop condition
	 */
	public String getStopCondition() {
		return stopCondition;
	}

	/**
	 * set the stop condition
	 * 
	 * @param stopCondition
	 */
	public void setStopCondition(String stopCondition) {
		this.stopCondition = stopCondition;
	}

	/**
	 * Constructor
	 */
	public NumberOfPartnersCrawler() {
		super();
	}

	/**
	 * Verify if it is necessary to crawl the referring page
	 * 
	 * @param referringPage
	 * @param url
	 * @return {True if the page should be crawler, False otherwise}
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();

		return !FILTERS.matcher(href).matches() && href.contains("exploreMembership");
		// return !FILTERS.matcher(href).matches() && href.contains(filter);
	}

	/**
	 * This function is called when a page is fetched and ready to be processed by
	 * your program.
	 * 
	 * 
	 * @param page
	 */
	@Override
	public void visit(Page page) {
		logger.info("Visited: {}", page.getWebURL().getURL());

		if (page.getParseData() instanceof HtmlParseData) {
			//*HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			// String text = htmlParseData.getText();

			//html = new StringBuilder(htmlParseData.getHtml());
			// Set<WebURL> links = htmlParseData.getOutgoingUrls();
			
			html= new StringBuilder(getHTML());

			if (html.toString().contains("<h1>Explore The Eclipse Membership")) {
				// if (html.contains(stopCondition)) {
				this.myController.shutdown();
			}
		}
	}

	private String getHTML() {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://www.eclipse.org/membership/exploreMembership.php#tab-all-members")).GET() // GET is default
				.build();

		HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			return response.body();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * This function is called by controller to get the local data of this crawler
	 * when job is finished
	 * 
	 * @return html string
	 */
	@Override
	public Object getMyLocalData() {
		return html;
	}

	/**
	 * This function is called by controller before finishing the job. You can put
	 * whatever stuff you need here.
	 */
	@Override
	public void onBeforeExit() {
		dumpMyData();
	}

	public void dumpMyData() {
		int id = getMyId();
		// You can configure the log to output to file
		logger.info("Crawler {} > html Text {}", id, html);
	}

}
