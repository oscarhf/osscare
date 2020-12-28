package gessi.plateoss.osseco.sourceconn;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
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

public class CommunityCommitRateCrawler extends WebCrawler {
	class MyData {
		protected long numberOfDays = 0;
		protected long numberOfProyects = 0;

		public long getNumberOfDays() {
			return numberOfDays;
		}

		public long getNumberOfProyects() {
			return numberOfProyects;
		}

		public void setNumberOfDays(long numberOfDays) {
			this.numberOfDays = numberOfDays;
		}

		public void setNumberOfProyects(long numberOfProyects) {
			this.numberOfProyects = numberOfProyects;
		}

	}

	private MyData myData = new MyData();
	private static final Logger logger = LoggerFactory.getLogger(VersionHistoryCrawler.class);

	public String getMyData() {
		return "NumberOfDays=" + myData.numberOfDays + ",NumberOfProjects=" + myData.getNumberOfProyects();
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
	public CommunityCommitRateCrawler() {
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

		boolean rBol = !FILTERS.matcher(href).matches() && href.startsWith("https://git.eclipse.org/c/")
				&& href.endsWith("git");
		//

		// ||
		// href.equals("https://git.eclipse.org/c/acceleo/org.eclipse.acceleo.git/log/"));

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

		try {
			int numberOfDays = numberOfDaysBetweenComits(page.getWebURL().getURL());

			myData.setNumberOfDays(myData.getNumberOfDays() + numberOfDays);
			myData.setNumberOfProyects(myData.getNumberOfProyects() + 1);
			System.out.println(page.getWebURL().getURL());
			System.out.println("\n\n\n*************************");
			System.out.println("numDays-->" + numberOfDays + "  SumDays-->" + myData.getNumberOfDays()
					+ "  NumProjects-->" + myData.numberOfProyects);
			System.out.println("*************************\\n\\n\\n");

			if (myData.getNumberOfProyects() > 50)
				this.myController.shutdown();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Calculate numberOfDaysBetweenComits
	 * 
	 * @param html
	 * @return
	 * @throws Exception
	 */
	private int numberOfDaysBetweenComits(String url) throws Exception {

		String initialDate;
		String finalDate;

		int offset = 0;
		String urlI = url;
		/**
		 * Find Final date from first log page
		 */

		Elements tableElements = getTableCommitsDates(urlI, offset);

		System.out.println(tableElements.get(0));

		finalDate = tableElements.get(0).text();
		/**
		 * Find Initial date from last log page
		 */

		do {
			offset += 50;
			tableElements = getTableCommitsDates(urlI, offset);

		} while (tableElements.size() > 0);

		offset -= 50;
		tableElements = getTableCommitsDates(urlI, offset);

		initialDate = tableElements.get(tableElements.size() - 1).text();
		System.out.println(initialDate);

		/**
		 * Calculating number of days
		 */
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date finalDateD;
		Date initialDateD;

		finalDateD = formatter.parse(finalDate);
		initialDateD = formatter.parse(initialDate);

		long diff = finalDateD.getTime() - initialDateD.getTime();
		int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

		return days;
	}

	/**
	 * 
	 * @param url
	 * @param offset Number of records to read per page
	 * @return
	 * @throws IOException
	 */

	private Elements getTableCommitsDates(String url, int offset) throws IOException {

		String urlI = url;
		urlI = url + "?ofs=" + offset;

		// System.out.println(urlI);
		URL urlt = new URL(urlI);
		Document docT = Jsoup.parse(urlt, 5000);
		Elements tableElements = docT.select("td:matches([0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9])");
		return tableElements;

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
