package gessi.plateoss.osseco.sourceconn;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
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
 * </dd><
 * </dl>
 * 
 * @version v0.1, 2016/08/20 (August) -- first release
 * @author Oscar Franco-Bedoya
 *         (<a href="oscarhf2002@hotmail.com">oscarhf2002.@hotmail.com</a>)
 */

public class NumberOfDownloadsCrawler extends WebCrawler {
	class MyData {
		protected long numberOfDownloads = 0;

		public long getNumberOfDownloads() {
			return numberOfDownloads;
		}

		public void setNumberOfDownloads(long numberOfDownloads) {
			this.numberOfDownloads = numberOfDownloads;
		}

	}

	private MyData myData = new MyData();
	private static final Logger logger = LoggerFactory.getLogger(VersionHistoryCrawler.class);

	public String getMyData() {
		return String.valueOf(myData.numberOfDownloads);
	}

	/**
	 * This extensions are not crawled
	 */
	private static final Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	/**
	 * HTML text with the crawler result
	 */
	private String html;
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

	private long sumDays = 0;

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
	public NumberOfDownloadsCrawler() {
		super();
	}

	/**
	 * Verify if it is necessary to crawl the referring page In this instance the
	 * crawler is visiting log pages of projects in https://git.eclipse.org/c/
	 * 
	 * @param referringPage
	 * @param url
	 * @return {True if the page should be crawler, False otherwise}
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();

		int contS = 0;

		for (int i = 0; i < href.length(); i++) {
			if (href.charAt(i) == '/')
				contS++;
		}

		System.out.println("*************************" + href);
		boolean rBol = href.endsWith("/packages/") || href.contains("/release/");

		return rBol;
	}

	/**
	 * This function is called when a page is fetched and ready to be processed by
	 * your program.
	 * 
	 * @param page
	 */
	@Override
	public void visit(Page page) {

		logger.info("Visited: {}", page.getWebURL().getURL());
		String href = page.getWebURL().getURL().toLowerCase();

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			// String text = htmlParseData.getText();

			String htmlt = htmlParseData.getHtml();

			try {
				long numberOfDownloads = numberOfDownloads(htmlt);
				
				myData.setNumberOfDownloads(myData.getNumberOfDownloads() + numberOfDownloads);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Calculate numberOfDowmloads
	 * 
	 * @param html
	 * @return
	 * @throws Exception
	 */
	private long numberOfDownloads(String html) throws Exception {

		Pattern p = Pattern.compile("[0-9]+,[0-9]+\\s", Pattern.MULTILINE);
		Matcher m = p.matcher(html);
		
		long numberOd=0;
		
		long longN=0;
	
		while (m.find()) {
			longN=Integer.valueOf(m.group(0).replace(",", "").trim());
			System.out.println(longN);
			numberOd+=longN;
			}
		
		return numberOd;
	}

	/**
	 * This function is called by controller to get the local data of this crawler
	 * when job is finished
	 * 
	 * @return html string
	 */
	@Override
	public Object getMyLocalData() {
		return getMyData();
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

	/**
	 * 
	 * @param html
	 * @return
	 */
	private String toXHTML(String html) {
		final org.jsoup.nodes.Document document = Jsoup.parse(html);
		document.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);

		return document.html();
	}

}
