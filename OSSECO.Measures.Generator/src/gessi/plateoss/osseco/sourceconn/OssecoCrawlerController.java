package gessi.plateoss.osseco.sourceconn;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <dl>
 * <dt>Purpose: Configure an run the crawler
 * <dd>
 *
 * <dt>Description:
 * <dd>With this Java class you can crawling a web.
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

public class OssecoCrawlerController {

	public static final Logger logger = LoggerFactory.getLogger(OssecoCrawlerController.class);
	/**
	 * The URL for crawling
	 */
	private String url;
	/**
	 * The kind of measure to crawler to select the specific crawler
	 */
	private CrawlerMeasures crawlerMeasures;
	/**
	 * Crawl controller
	 */
	private CrawlController controller;

	public CrawlController getController() {
		return controller;
	}

	/**
	 * Constructor of the OssecoCrawlerController class
	 * 
	 * @param url           for starting the crawling
	 * @param filter        to filter the pages to crawl (if page contains filter
	 *                      the it is crawled)
	 * @param stopCondition when is founded the crawling finished
	 * @param url
	 */
	public OssecoCrawlerController(String url, CrawlerMeasures crawlerMeasure) {

		this.url = url;
		this.crawlerMeasures = crawlerMeasure;

	}

	/**
	 * Instantiate and start the crawler
	 * 
	 * @return String with the HTML of the pages founded
	 * @throws Exception
	 */
	public StringBuilder getHtmlCrawler() throws Exception {
		String crawlStorageFolder = "./data/crawl/root";

		// int numberOfCrawlers = 50;

		int numberOfCrawlers = 10;// ProjectTypesCrawler

		CrawlConfig config = new CrawlConfig();

		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxPagesToFetch(1000);
		config.setPolitenessDelay(1000);
		config.setIncludeHttpsPages(true);
		config.setFollowRedirects(true);
		config.setMaxDepthOfCrawling(numberOfCrawlers);
		// config.setMaxOutgoingLinksToFollow(10000);

		// TODO: Change according to measure
		// config.setMaxDepthOfCrawling(50);

		config.setMaxDepthOfCrawling(5); // ProjectTypesCrawler

		// Instantiate the controller for this crawl.

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		controller = new CrawlController(config, pageFetcher, robotstxtServer);

		controller.addSeed(url);
		/*
		 * Start the crawl. This is a blocking operation, meaning that your code will
		 * reach the line after this only when crawling is finished.
		 */

		switch (crawlerMeasures) {
		case Partners: {
			config.setMaxDepthOfCrawling(1); // ProjectTypesCrawler
			NumberOfPartnersCrawler my = new NumberOfPartnersCrawler();
			controller.start(my.getClass(), numberOfCrawlers);
			break;
		}
		case Versions: {
			VersionHistoryCrawler my = new VersionHistoryCrawler();
			controller.start(my.getClass(), numberOfCrawlers);
			break;
		}
		case LastCommit: {
			DateLastCommitCrawler my = new DateLastCommitCrawler();
			controller.start(my.getClass(), numberOfCrawlers);
			break;
		}
		case LastRelease: {
			DateLastReleaseCrawler my = new DateLastReleaseCrawler();
			controller.start(my.getClass(), numberOfCrawlers);
			break;
		}
		case CommitRate: {
			CommunityCommitRateCrawler my = new CommunityCommitRateCrawler();
			System.out.println(my.getMyData());
			controller.start(my.getClass(), numberOfCrawlers);
			break;
		}
		case NumberOfEvent: {
			NumberOfEventsCrawler my = new NumberOfEventsCrawler();
			controller.start(my.getClass(), numberOfCrawlers);
			break;

		}
		case ProjectType: {
			ProjectTypesCrawler my = new ProjectTypesCrawler();
			controller.start(my.getClass(), numberOfCrawlers);
			break;

		}
		case ActivityType: {
			ActivityTypesCrawler my = new ActivityTypesCrawler();
			controller.start(my.getClass(), numberOfCrawlers);
			break;
		}

		case NumberOfDownloads: {
			NumberOfDownloadsCrawler my = new NumberOfDownloadsCrawler();
			controller.start(my.getClass(), numberOfCrawlers);
			/// TODO: la lista de valores se debe sumar en todos
			List<Object> crawlersData = controller.getCrawlersLocalData();
			long total = 0;
			for (Object ob : crawlersData) {
				total += Integer.valueOf((String) ob);
			}
			StringBuilder strTotal = new StringBuilder(String.valueOf(total));

			return strTotal;
			
		}

		}

		List<Object> crawlersLocalData = controller.getCrawlersLocalData();
//
//		try (FileWriter f = new FileWriter("names.txt", true);
//				BufferedWriter b = new BufferedWriter(f);
//				PrintWriter p = new PrintWriter(b);) {
//				
//			for(Object ht: crawlersLocalData)
//			{
//				p.println((String) ht );
//			}
//
//				
//
//		} catch (Exception e) {
//			System.out.println(e);
//		}

		StringBuilder sb = new StringBuilder();
		sb.append(crawlersLocalData.get(0));
		return sb;
		// return (StringBuilder) crawlersLocalData.get(0);

	}

	/**
	 * Instantiate and start the crawler
	 * 
	 * @return Array list of activities
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<ActivityTypesCrawler.Activity> getActivitiesCrawler() throws Exception {
		String crawlStorageFolder = "./data/crawl/root";
		int numberOfCrawlers = 1;

		CrawlConfig config = new CrawlConfig();

		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxPagesToFetch(10000);
		config.setPolitenessDelay(1000);
		// config.setIncludeHttpsPages(true);
		config.setFollowRedirects(true);
		config.setMaxDepthOfCrawling(numberOfCrawlers);
		config.setMaxOutgoingLinksToFollow(10000);
		config.setIncludeBinaryContentInCrawling(true);

		// TODO: Change according to measure
		config.setMaxDepthOfCrawling(100);

		// Instantiate the controller for this crawl.

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		controller = new CrawlController(config, pageFetcher, robotstxtServer);

		controller.addSeed(url);
		/*
		 * Start the crawl. This is a blocking operation, meaning that your code will
		 * reach the line after this only when crawling is finished.
		 */

		ActivityTypesCrawler my = new ActivityTypesCrawler();
		controller.start(my.getClass(), numberOfCrawlers);
		List<Object> crawlersLocalData = controller.getCrawlersLocalData();

		return (ArrayList<ActivityTypesCrawler.Activity>) crawlersLocalData.get(0);

	}
}
