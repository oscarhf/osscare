package gessi.plateoss.osseco.sourceconn;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.jsontype.impl.MinimalClassNameIdResolver;
import com.sleepycat.utilint.StringUtils;

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
public class ProjectTypesCrawler extends WebCrawler {

	/****
	 * User Types
	 */
	ArrayList<String> listTypesOfPrjects = new ArrayList<String>();

	private static final Logger logger = LoggerFactory.getLogger(ProjectTypesCrawler.class);

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
	public ProjectTypesCrawler() {
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

		int contS = 0;

		for (int i = 0; i < href.length(); i++) {
			if (href.charAt(i) == '/')
				contS++;
		}
		return (href.startsWith("https://projects.eclipse.org/") && contS == 3);

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

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			// String text = htmlParseData.getText();

			String htmlt = htmlParseData.getHtml();
			if (htmlt.contains("technology-type")) {
				miningTypeValues(htmlt);
				html=String.valueOf(listTypesOfPrjects.size());
				System.out.println("");
			}
		}
		// Set<WebURL> links = htmlParseData.getOutgoingUrls();
		// System.out.println("Visited: " + page.getWebURL().getURL());
		// html = toXHTML(html);
		// miningTypeValues(html);
		// html=String.valueOf(listTypesOfPrjects.size());

	}

	/*
	 * 
	 * @param html
	 * 
	 * @param url
	 */
	void miningTypeValues(String html) {

		//Pattern p = Pattern.compile("technology-type[^>]+", Pattern.MULTILINE);
		Pattern p = Pattern.compile("field_project_techology_types_tid(.)+select", Pattern.MULTILINE);
		Matcher m = p.matcher(html);
		m.find();
		p = Pattern.compile("(option value)[^>]+");
		
		m = p.matcher(m.group(0));
		String type = m.replaceAll("");
		System.out.println(type);
		p = Pattern.compile("(<>)([\\w]|[\\s]?)+");
				m = p.matcher(type);
		m.find();
		type= m.group(0);
	
		
		while (m.find()) {

			type = m.group(0).replace("<>", "");
			if (!listTypesOfPrjects.contains(type)) {
				listTypesOfPrjects.add(type);
			}
		}

	}

	/**
	 * 
	 * @param html
	 * @param url
	 *//*
		 * void miningTypeValues(String html) { Document doc = Jsoup.parse(html);
		 * Elements content = doc.getElementsByClass("col-xs-24"); String[] tmpType;
		 * 
		 * for (Element e : content) { tmpType = e.text().split("Project "); if
		 * (tmpType.length > 0) { tmpType= tmpType[0].split("Projects");
		 * 
		 * if((tmpType.length > 0) && (!listTypesOfPrjects.contains(tmpType[1]))) {
		 * listTypesOfPrjects.add(tmpType[1]); System.out.println(tmpType[1]);
		 * logger.info("Project type: {}", tmpType[1]); }
		 * 
		 * }
		 * 
		 * } }
		 */

	/**
	 * This function is called by controller to get the local data of this crawler
	 * when job is finished
	 * 
	 * @return listOfActivities;
	 */
	@Override
	public Object getMyLocalData() {
		return String.valueOf(listTypesOfPrjects.size());
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
