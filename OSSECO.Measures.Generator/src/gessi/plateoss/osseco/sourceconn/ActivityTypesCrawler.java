package gessi.plateoss.osseco.sourceconn;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import gessi.plateoss.osseco.measure_calculator.TypeActivities;

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
public class ActivityTypesCrawler extends WebCrawler {

	/****
	 * User Types
	 */
	public static final int NUMBER_OF_ACTIVITIES = 13;

	public class Activity {

		ArrayList<String> listURL = new ArrayList<String>();
		String year;
		String month;
		// int[] listOfActivities = new int[NUMBER_OF_ACTIVITIES];
		int[] listOfMembers = new int[NUMBER_OF_ACTIVITIES];
		float giniIndex;

		public ArrayList<String> getListURL() {
			return listURL;
		}

		public int[] getListOfMembers() {
			return listOfMembers;
		}

		public void setListOfMembers(int[] listOfMembers) {
			this.listOfMembers = listOfMembers;
		}

		public float getGiniIndex() {
			return giniIndex;
		}

		public void setGiniIndex(float giniIndex) {
			this.giniIndex = giniIndex;
		}

		public void addtNumberOfMembers(int index, int numberOfMembers) {
			this.listOfMembers[index] += numberOfMembers;
		}

		public String getYear() {
			return year;
		}

		public void setYear(String year) {
			this.year = year;
		}

		public String getMonth() {
			return month;
		}

		public void setMonth(String month) {
			this.month = month;
		}

		/*
		 * public int getValueOfActivity(int index) { return listOfActivities[index]; }
		 * 
		 * public void incNumberOfActivities(int index) {
		 * this.listOfActivities[index]++; }
		 */

	}

	private ArrayList<Activity> listOfActivities = new ArrayList<>();

	public ArrayList<Activity> getListOfActivities() {
		return listOfActivities;
	}

	private static final Logger logger = LoggerFactory.getLogger(ActivityTypesCrawler.class);

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
	public ActivityTypesCrawler() {
		super();
	}

	/**
	 * Verify if it is necessary to crawl the referring page
	 * 
	 * @param referringPage<
	 * @param url            TODO:pendiente
	 * @return {True if the page should be crawler, False otherwise}
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();

		int contS = 0;
		String strTmp = referringPage.getWebURL().getURL();
		for (int i = 0; i < strTmp.length(); i++) {
			if (strTmp.charAt(i) == '/')
				contS++;
		}

		if (href.startsWith("https://git.eclipse.org") && href.contains("/commit/")) {
			System.out.println("********************************************************************"
					+ referringPage.getWebURL().getURL());
			return true;
		}
		return false;

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
			html = htmlParseData.getHtml();
			// Set<WebURL> links = htmlParseData.getOutgoingUrls();
			System.out.println("Visited: " + page.getWebURL().getURL());
			html = toXHTML(html);
			miningActivityValues(html, page.getWebURL().getURL());

		}
	}

	/**
	 * 
	 * @param html
	 * @param url
	 * @return
	 * @throws Exception
	 */
	/*
	 * void miningActivityValues(String html, String url) { Activity objActivity =
	 * new Activity(); Document doc = Jsoup.parse(html); boolean[] tmpActivity = new
	 * boolean[NUMBER_OF_ACTIVITIES];
	 *//**
		 * Count the number of people in the commit
		 */
	/*
	 * // Select authors and committers Elements content = doc
	 * .select("th+td:matches(^[\\p{L}\\p{M}\\p{Mn}\\.\\s]+$)"); ArrayList<String>
	 * listPeople = new ArrayList<String>();
	 * 
	 * for (Element e : content) { if (!listPeople.contains(e.text())) { //
	 * System.out.println(e.text()); listPeople.add(e.text()); } }
	 * 
	 *//**
		 * Get the date
		 */
	/*
	 * 
	 * content = doc.getElementsByClass("right");
	 * 
	 * String date = ""; //
	 * "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} ((\\(EST\\))|(\\(EDT\\)))")) // {
	 * for (Element e : content) { if (e.text().matches(
	 * "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\s.+")) { date = e.text(); break;
	 * }
	 * 
	 * } String[] partDate = date.split("-"); String strYear = partDate[0]; String
	 * strMonth = (partDate[1]);
	 * 
	 * System.out.println("******************"); System.out.println();
	 * System.out.println("Year->" + strYear + " Month->" + strMonth);
	 * System.out.println(); System.out.println("******************");
	 * 
	 * 
	 * objActivity = getActivityObject(strYear, strMonth);
	 * objActivity.getListURL().add(url);
	 * 
	 * // System.out.println("After " + objActivity.getYear() + "---" // +
	 * objActivity.getMonth() + "----" + listOfActivities.size());
	 * 
	 *//**
		 * 
		 * Get number of activities
		 *//*
			 * content = doc.getElementsByClass("diffstat");
			 * 
			 * TypeActivities typeA;
			 * 
			 * for (Element e : content) { String[] listAct = e.text().split("[rw-]{10}");
			 * 
			 * System.out.println("Activity->" + e.text());
			 * 
			 * objActivity = getActivityObject(strYear, strMonth);
			 * 
			 * for (String s : listAct) { if (!s.equals("")) { typeA =
			 * typeOfActivityClassifier(s); if (!tmpActivity[typeA.ordinal()]) {
			 * tmpActivity[typeA.ordinal()] = true; //
			 * objActivity.listOfActivities[typeA.ordinal()]=1;
			 * objActivity.addtNumberOfMembers(typeA.ordinal(), listPeople.size()); //
			 * System.out.println("count Act-->" + typeA); }
			 * 
			 * }
			 * 
			 * } }
			 * 
			 * }
			 */

	/**
	 * 
	 * @param html
	 * @param url
	 * @return
	 * @throws Exception
	 */

	void miningActivityValues(String html, String url) {
		Activity objActivity = new Activity();
		ArrayList<String> listPeople = new ArrayList<String>();
		// Obtiene las personas

		Pattern p = Pattern.compile("(committer|author)</th><td>[^<]+", Pattern.MULTILINE);
		Matcher m = p.matcher(html);

		while (m.find()) {
			if (!listPeople.contains(m.group(0))) {
				listPeople.add(m.group(0));

			}

		}
		if (listPeople.size() > 0)

			System.out.println("mining-------------------------------------" + url);
	}

	/**
	 * 
	 * @param year
	 * @param month
	 * @return
	 */

	private Activity getActivityObject(String year, String month) {

		Activity objActivity = null;

		try

		{
			objActivity = listOfActivities.stream().filter(u -> u.getYear().equals(year) && u.getMonth().equals(month))
					.findFirst().get();
		}

		catch (Exception ex) {
			objActivity = new Activity();
			objActivity.setYear(year);
			objActivity.setMonth(month);
			listOfActivities.add(objActivity);
			return objActivity;
		}
		return objActivity;
	}

	/**
	 * 
	 **/
	private TypeActivities typeOfActivityClassifier(String activity) {

		String doc = "(.*\\.((s|x|g|p|(gt))?)htm(l?))|" + "(.*\\.zabw)|(.*\\.chm)|(.*\\.css)|"
				+ "(.*\\.txt)|(.*\\.rtf)|(.*\\.tex)|" + "(.*\\.sgml)|(.*\\.eps)|(.*\\.xsd)|"
				+ "(.*\\.texi)|(.*\\.doc)|(.*\\.docx)|" + "(.*\\.schemas)|(.*\\.vcf)|(.*\\.ics)|"
				+ "(.*\\.man)|(.*\\.vcard(~?))|(.*\\.ods)|" + "(.*\\.wml)|(.*\\.page)|(.*\\.xdc)|(.*\\.MF)";
		String img = "(.*\\.jpg)|(.*\\.png)|(.*\\.jpeg)|(.*\\.bmp)|(.*\\.chm)|(.*\\.vdx)|"
				+ "(.*\\.gif)|(.*\\.sgv(z?))|(.*\\.nsh)|(.*\\.ico)|(.*\\.xcf)|(.*\\.bld)";

		String l10n = "(.*\\.i18ns(~?))|(.*\\.pot(~?))|(.*\\.mo(~?))|"
				+ "(.*\\.lingua)|(.*\\.wxl)|(.*\\.gmo(~?))|(.*\\.resx(~?))|" + "(.*\\.po(~?))";

		String ui = "(.*\\.glade(\\d?)((\\.bak)?)(∼?))|(.*\\.desktop)|(.*\\.xul(~?))|"
				+ "(.*\\.gladed(\\d?)((\\.bak)?)(∼?))|(.*\\.ui)|(.*\\.xpm)|(.*\\.xdt)|"
				+ "(.*\\.gladep(\\d?)((\\.bak)?)(∼?))|(.*\\.theme)";

		String media = "(.*\\.ogg)|(.*\\.ogv)|(.*\\.shape)|(.*\\.wav)|(.*\\.au)|"
				+ "(.*\\.otf(~?))|(.*\\.gnl)|(.*\\.mov)|(.*\\.avi)|(.*\\.sfd(~?))|"
				+ "(.*\\.pgn)|(.*\\.mid)|(.*\\.xspf)|(.*\\.ttf(~?))|(.*\\.cdf)"
				+ "(.*\\.m4f)|(.*\\.ps)|(.*\\.afm)|(.*\\.bse)|(.*\\.pls)|(.*\\.omf)|" + "(.*\\.pfb)|(.*\\.cur)";

		String code = "(.*\\.g)|(.*\\.dmg(~?))|(.*\\.swg(~?))|(.*\\.so(~?))|(.*\\.i(~?))|"
				+ "(.*\\.o(~?))|(.*\\.exe(~?))|(.*\\.oafinfo(~?))|(.*\\.pyd(~?))|"
				+ "(.*\\.awk(~?))|(.*\\.scm(~?))|(.*\\.glsl(~?))|(.*\\.patch(~?))|"
				+ "(.*\\.c((\\.swp)?)(~?))|(.*\\.jar(~?))|(.*\\.m((\\.swp)?)(~?))|"
				+ "(.*\\.cs(~?))|(.*\\.idl(~?))|(.*\\.s(~?))|(.*\\.r((\\.swp)?)(~?))|"
				+ "(.*\\.cxx(~?))|(.*\\.pyc(~?))|(.*\\.asm(x?)(~?))|(.*\\.py((\\.swp)?)(~?))|"
				+ "(.*\\.y((\\.swp)?)(~?))|(.*\\.gi((\\.swp)?)(∼?))|(.*\\.t((\\.swp)?)(~?))|"
				+ "(.*\\.dll(~?))|(.*\\.htemplate((\\.swp)?)(∼?))|"
				+ "(.*\\.js((\\.swp)?)(~?))|(.*\\.rb((\\.swp)?)(~?))|(.*\\.ctemplate((˙\\.swp)?)(∼?))|"
				+ "(.*\\.hg((\\.swp)?)(~?))|(.*\\.pm((\\.swp)?)(~?))|(.*\\.php((\\.swp)?)(\\d?)(∼?))|"
				+ "(.*\\.cc((\\.swp)?)(~?))|(.*\\.sh((\\.swp)?)(~?))|(.*\\.php((\\.swp)?)(\\d?)(∼?))|"
				+ "(.*\\.el((\\.swp)?)(~?))|(.*\\.hh((\\.swp)?)(~?))|(.*\\.h((pp)?)((\\.swp)?)(∼?))|"
				+ "(.*\\.xs((\\.swp)?)(~?))|(.*\\.pl((\\.swp)?)(~?))|(.*\\.h\\.tmpl((\\.swp)?)(∼?))|"
				+ "(.*\\.mm((\\.swp)?)(~?))|(.*\\.idl((\\.swp)?)(~?))|(.*\\.h.win32((\\.swp)?)(∼?))|"
				+ "(.*\\.xpt((\\.swp)?)(~?))|(.*\\.ccg((\\.swp)?)(~?))|(.*\\.ctmpl(( ˙ \\.swp)?)(∼?))|"
				+ "(.*\\.snk((\\.swp)?)(~?))|(.*\\.inc((\\.swp)?)(~?))|(.*\\.asp(x?)((\\.swp)?)(∼?))|"
				+ "(.*\\.cpp((\\.swp)?)(~?))|(.*\\.gob((\\.swp)?)(~?))|(.*\\.vapi((\\.swp)?)(∼?))|"
				+ "(.*\\.giv((\\.swp)?)(~?))|(.*\\.dtd((\\.swp)?)(~?))|(.*\\.gidl((\\.swp)?)(∼?))|"
				+ "(.*\\.giv((\\.swp)?)(~?))|(.*\\.ada((\\.swp)?)(~?))|(.*\\.defs((\\.swp)?)(∼?))|"
				+ "(.*\\.tcl((\\.swp)?)(~?))|(.*\\.vbs((\\.swp)?)(~?))|(.*\\.java((\\.swp)?)(∼?))";

		String meta = "(.*\\.svn(.*))|(.*\\.git(.*))|(.*\\.doap)|(.*\\.mdp.*\\.cvs(.*))|(.*\\.bzr(.*))|"
				+ "(.*\\.mds)|(.*\\.vbg)|(meta .*\\.sln)";

		String config = "(.*\\.conf)|(.*\\.cfg)|(.*\\.anjuta)|(.*\\.dsw)|(.*\\.gnorba)|(.*\\.project)|(.*\\.cproject)|"
				+ "(.*\\.pgp(~?))|(.*\\.ini)|(.*\\.prefs)|(.*\\.vsprops)|(.*\\.gpg(~?))|(.*\\.config)|"
				+ "(.*\\.vmrc)|(.*\\.csproj)|(.*\\.gpg)|(.*\\.pub(~?))|(.*\\.xml)|(.*\\.cproj)|(.*\\.cbproj)|"
				+ "(.*\\.pgp\\.pub(~?).*\\.dsp)|(.*\\.emacs)|(.*\\.groupproj)|(.*\\.xcconfig)|(.*\\.plist)|"
				+ "(.*\\.pbxproj)|(.*anjuta\\.session)|(.*\\.*setting(s?).*/.*\\.jp)|(.*\\.*config(s?).*/.*\\.jp)";

		String build = "(.*\\.buildpath)|(.*\\.cmake)|(.*/install-sh)|(.*/build/.*)|(.*\\.ezt)|(.*\\.cbp)|(.*\\.pch)|"
				+ "(.*/pkg-info)|(.*\\.wxilib)|(.*\\.m4(~?))|(.*makefile.*)|(.*\\.prj)|(.*\\.plo)|"
				+ "(.*\\.mk)|(.*\\.make)|(.*\\.deps)|(.*\\.wxiproj)|(.*\\.am(~?))|(.*\\.mp4)|"
				+ "(.*\\.builder)|(.*\\.lo)|(.*\\.target)|(.*\\.iss)|(.*\\.nsi)|(.*\\.wxi)|"
				+ "(.*/configure((\\..+)?))|(.*\\.wxs)|(.*\\.in)|(.*\\.wpj)|(.*\\.vc(x?)proj(i?)n((\\.filters((in)?))?))|"
				+ "(.*\\.vcproj((\\.filters((in)?))?))";

		String devdoc = "(.*\\.doxi)|(.*\\.rst)";

		String db = "(.*\\.sql)|(.*\\.sqlite)|(.*\\.mdb)|(.*\\.yaml)|(.*\\.sdb)|(.*\\.dat)|"
				+ "(.*\\.yaml)|(.*\\.json)|(.*\\.db)";

		String test = "(.*\\.test(s?))";

		Pattern p = Pattern.compile(doc);
		Matcher m = p.matcher(activity);

		if (m.find()) {
			return TypeActivities.Doc;
		}

		p = Pattern.compile(img);
		m = p.matcher(activity);
		if (m.find()) {
			return TypeActivities.Img;
		}

		p = Pattern.compile(l10n);
		m = p.matcher(activity);
		if (m.find()) {
			return TypeActivities.L10n;
		}

		p = Pattern.compile(ui);
		m = p.matcher(activity);
		if (m.find()) {
			return TypeActivities.Ui;
		}

		p = Pattern.compile(media);
		m = p.matcher(activity);
		if (m.find()) {
			return TypeActivities.Media;
		}

		p = Pattern.compile(code);
		m = p.matcher(activity);
		if (m.find()) {
			return TypeActivities.Code;
		}

		p = Pattern.compile(meta);
		m = p.matcher(activity);
		if (m.find()) {
			return TypeActivities.Meta;
		}

		p = Pattern.compile(config);
		m = p.matcher(activity);
		if (m.find()) {
			return TypeActivities.Config;
		}

		p = Pattern.compile(build);
		m = p.matcher(activity);
		if (m.find()) {
			return TypeActivities.Buid;
		}

		p = Pattern.compile(devdoc);
		m = p.matcher(activity);
		if (m.find()) {
			return TypeActivities.Devdoc;
		}

		p = Pattern.compile(db);
		m = p.matcher(activity);
		if (m.find()) {
			return TypeActivities.Db;
		}

		p = Pattern.compile(test);
		m = p.matcher(activity);
		if (m.find()) {
			return TypeActivities.Test;
		}

		return TypeActivities.None;
	}

	/**
	 * This function is called by controller to get the local data of this crawler
	 * when job is finished
	 * 
	 * @return listOfActivities;
	 */
	@Override
	public Object getMyLocalData() {
		return listOfActivities;
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
