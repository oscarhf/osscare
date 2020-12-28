package gessi.plateoss.osseco.measure_calculator;

import gessi.plateoss.osseco.sourceconn.ActivityTypesCrawler;
import gessi.plateoss.osseco.sourceconn.ConnectionJDBC;
import gessi.plateoss.osseco.sourceconn.CrawlerMeasures;
import gessi.plateoss.osseco.sourceconn.OssecoCrawlerController;
import gessi.plateoss.osseco.sourceconn.pojos.EvolutionaryITS;
import gessi.plateoss.osseco.sourceconn.pojos.EvolutionaryMLS;
import gessi.plateoss.osseco.sourceconn.pojos.EvolutionaryPRJ;
import gessi.plateoss.osseco.sourceconn.pojos.EvolutionarySCM;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <dl>
 * <dt>Purpose: Obtain the measures data from OSSECO repositories
 * <dd>
 *
 * <dt>Description:
 * <dd>This class implement the extraction of the data form the OSSECO
 * repositories and create the tables for the measures
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
public class MeasureExtractor {

	/**
	 * Create Network size Table :Number of edges
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	public static void createNetworkSizeTable()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		Connection conn = getConnection();

		/*
		 * Connection conn= new ConnectionJDBC("ba16735c7f9a7f", "a8f86cb3", "mysql",
		 * "br-cdbr-azure-south-a.cloudapp.net", "3306",
		 * "ossecodatabase").getConnection();
		 */

		String strSQL = "SET GLOBAL innodb_buffer_pool_size=402653184";
		PreparedStatement statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();
		// ***************************************************************
		strSQL = "CREATE TABLE event_mailing_list SELECT  D.mailId, D.mailFrom, C.mailTo,"
				+ " D.mailDate FROM (SELECT messages_people.message_id as "
				+ "mailId,email_address AS mailFrom, messages.first_date AS mailDate "
				+ "FROM messages_people JOIN messages ON" + " (messages.message_ID=messages_people.message_id)"
				+ " WHERE type_of_recipient=\"From\" )AS D 	LEFT JOIN "
				+ "(SELECT messages_people.message_id AS mailId2, email_address AS"
				+ " mailTo FROM messages_people WHERE " + "type_of_recipient=\"To\")AS C ON (C.mailId2=D.mailId)";
		statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();
		// ***************************************************************
		strSQL = "CREATE TABLE osseco_measures.mailing_list_date AS " + "Select T1.year,T1.month,count(*) "
				+ "from (select *,EXTRACT(YEAR FROM mailDate) AS year,"
				+ "EXTRACT(MONTH FROM mailDate) AS month from event_mailing_list "
				+ "group by mailFrom, mailTo) AS T1 group by year, month";
		statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();
		conn.close();
	}

	/**
	 * Create table Version History
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static void createVersionHistoryTable(String url) throws Exception {
		// view-source:http://archive.eclipse.org/eclipse/downloads

		OssecoCrawlerController crawlerController = new OssecoCrawlerController(url, CrawlerMeasures.Versions);
		String htmlString = crawlerController.getHtmlCrawler().toString();

		Pattern p = Pattern.compile("<a href=\"drops[^)]+", Pattern.MULTILINE);
		Matcher m = p.matcher(htmlString);

		Pattern v = Pattern.compile(">([0-9]|\\.)+<", Pattern.MULTILINE);
		Pattern q = Pattern.compile(">[a-zA-Z][^(]+", Pattern.MULTILINE);

		SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy -- HH:mm");

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		Connection conn = getConnection();

		Calendar cal = Calendar.getInstance();
		String strSQL;
		PreparedStatement statementSQL;
		/* DESCOMENTAR PARA ACTUALIZAR */

		strSQL = "DELETE FROM osseco_measures.main_versions";
		statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();

		DatabaseMetaData dbm = conn.getMetaData();
		ResultSet rs = dbm.getTables(null, null, "osseco_measures.main_versions_date", null);

		if (rs.next()) {
			strSQL = "DROP TABLE osseco_measures.main_versions_date";
			statementSQL = conn.prepareStatement(strSQL);
			statementSQL.executeUpdate();
		}
		
		while (m.find()) {
			Matcher m2 = v.matcher(m.group(0));
			if (m2.find()) {

				String version = m2.group(0);

				version = version.replace(">", "");
				version = version.replace("<", "");

				Matcher m3 = q.matcher(m.group(0));

				System.out.println(version);

				if (m3.find()) {
					String date = m3.group(0);
					date = date.replace(">", "");
					System.out.println(version);
					System.out.println(date);
					Date datet = new SimpleDateFormat("EEE, dd MMM yyyy -- hh:mm", Locale.ENGLISH).parse(date);
					// Date datet = formatter.parse(date);
					cal.setTime(datet);
					strSQL = "INSERT INTO osseco_measures.main_versions " + "(year,month,version) VALUES("
							+ cal.get(Calendar.YEAR) + "," + cal.get(Calendar.MONTH) + "," + "\"" + version + "\""
							+ ")";

					statementSQL = conn.prepareStatement(strSQL);
					statementSQL.executeUpdate();
				}
			}
		}

		strSQL = "CREATE TABLE osseco_measures.main_versions_date AS "
				+ "SELECT year, month, count(*) AS count from main_versions " + "group by year";
		statementSQL = conn.prepareStatement(strSQL);

		statementSQL.executeUpdate();
		conn.close();

	}

	/**
	 * create BugsOpened Table
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static void createBugsOpenedTable(String url) throws Exception {

		EvolutionaryITS openedEvolutionary;
		openedEvolutionary = EvolutionaryITS.getEvolutionaryITS("data/json/Independent-its-com-evolutionary.json");
		List<String> listOpenedValue = openedEvolutionary.getOpened();
		List<String> listOpenedDate = openedEvolutionary.getDate();

		SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
		Calendar cal = Calendar.getInstance();
		Date datet;

		Connection conn = getConnection();

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "127.0.0.1", "3306", "osseco_measures")
		// .getConnection();

		int year;
		int month;
		int count;

		for (int i = 0; i < listOpenedValue.size(); i++) {
			count = Integer.parseInt(listOpenedValue.get(i));
			datet = formatter.parse(listOpenedDate.get(i));
			cal.setTime(datet);
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;

			String strSQL = "INSERT INTO osseco_measures.opened_bugs_date" + "(year,month,count) VALUES(" + year + ","
					+ month + "," + count + ")";
			PreparedStatement statementSQL = conn.prepareStatement(strSQL);
			statementSQL.executeUpdate();

		}
		conn.close();
	}

	/**
	 * create BugsClosed Table
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static void createBugsClosedTable(String url) throws Exception {

		EvolutionaryITS openedEvolutionary;
		openedEvolutionary = EvolutionaryITS.getEvolutionaryITS("data/json/Independent-its-com-evolutionary.json");
		List<String> listClosedValue = openedEvolutionary.getClosed();
		List<String> listClosedDate = openedEvolutionary.getDate();

		SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
		Calendar cal = Calendar.getInstance();
		Date datet;

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		Connection conn = getConnection();

		int year;
		int month;
		int count;

		for (int i = 0; i < listClosedValue.size(); i++) {
			count = Integer.parseInt(listClosedValue.get(i));
			datet = formatter.parse(listClosedDate.get(i));
			cal.setTime(datet);
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;

			String strSQL = "INSERT INTO osseco_measures.closed_bugs_date " + "(year,month,count) VALUES(" + year + ","
					+ month + "," + count + ")";
			PreparedStatement statementSQL = conn.prepareStatement(strSQL);
			statementSQL.executeUpdate();

		}
		conn.close();
	}

	/**
	 * create FilesChanged Table
	 * 
	 * @param url
	 * @throws Exception
	 */
	// TODO: change literal file to url

	public static void createFilesChangedTable(String url) throws Exception {

		EvolutionaryITS changedEvolutionary;
		changedEvolutionary = EvolutionaryITS.getEvolutionaryITS("data/json/Independent-its-com-evolutionary.json");
		List<String> listChangedValue = changedEvolutionary.getChanged();
		List<String> listChangeddDate = changedEvolutionary.getDate();

		SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
		Calendar cal = Calendar.getInstance();
		Date datet;

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		Connection conn = getConnection();

		int year;
		int month;
		int count;

		for (int i = 0; i < listChangedValue.size(); i++) {
			count = Integer.parseInt(listChangedValue.get(i));
			datet = formatter.parse(listChangeddDate.get(i));
			cal.setTime(datet);
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;

			String strSQL = "INSERT INTO osseco_measures.changed_files_date " + "(year,month,count) VALUES(" + year
					+ "," + month + "," + count + ")";
			PreparedStatement statementSQL = conn.prepareStatement(strSQL);
			statementSQL.executeUpdate();

		}
		conn.close();
	}

	/**
	 * Create Number Files Per Release Table
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static void createNumberFilesPerReleaseTable(String url) throws Exception {

		EvolutionarySCM changedEvolutionary;
		changedEvolutionary = EvolutionarySCM.getEvolutionarySCM();
		List<String> listFilesValue = changedEvolutionary.getFiles();
		List<String> listChangeddDate = changedEvolutionary.getDate();
		SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
		Calendar cal = Calendar.getInstance();
		Date datet;

		Connection conn = getConnection();

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		int year;
		int month;
		int count;

		for (int i = 0; i < listFilesValue.size(); i++) {
			count = Integer.parseInt(listFilesValue.get(i));
			datet = formatter.parse(listChangeddDate.get(i));
			cal.setTime(datet);
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;

			String strSQL = "INSERT INTO osseco_measures.files_commit_date " + "(year,month,count) VALUES(" + year + ","
					+ month + "," + count + ")";
			PreparedStatement statementSQL = conn.prepareStatement(strSQL);
			statementSQL.executeUpdate();

		}
		conn.close();

	}

	/**
	 * Create Number Of Commits Table
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static void createNumberOfCommitsTable(String url) throws Exception {

		EvolutionaryPRJ commitsEvolutionary;
		commitsEvolutionary = EvolutionaryPRJ.getEvolutionaryPRJ("data/json/scm-prj-all-evolutionary.json");
		List<String> listCommitsValue = commitsEvolutionary.getCommits();
		List<String> listChangeddDate = commitsEvolutionary.getDate();

		Connection conn = getConnection();

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
		Calendar cal = Calendar.getInstance();
		Date datet;

		int year;
		int month;
		int count;

		for (int i = 0; i < listCommitsValue.size(); i++) {
			count = Integer.parseInt(listCommitsValue.get(i));
			datet = formatter.parse(listChangeddDate.get(i));
			cal.setTime(datet);
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;

			String strSQL = "INSERT INTO osseco_measures.number_of_commits_date" + "(year,month,count) VALUES(" + year
					+ "," + month + "," + count + ")";
			PreparedStatement statementSQL = conn.prepareStatement(strSQL);
			statementSQL.executeUpdate();

		}
		conn.close();

	}

	/**
	 * Create Number Of Commiters Table
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static void createNumberOfCommitersTable(String url) throws Exception {

		EvolutionaryPRJ commitsEvolutionary;
		commitsEvolutionary = EvolutionaryPRJ.getEvolutionaryPRJ("data/json/scm-prj-all-evolutionary.json");
		List<String> listCommitersValue = commitsEvolutionary.getCommitters();
		List<String> listChangeddDate = commitsEvolutionary.getDate();

		Connection conn = getConnection();

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
		Calendar cal = Calendar.getInstance();
		Date datet;

		int year;
		int month;
		int count;

		for (int i = 0; i < listCommitersValue.size(); i++) {
			count = Integer.parseInt(listCommitersValue.get(i));
			datet = formatter.parse(listChangeddDate.get(i));
			cal.setTime(datet);
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;

			String strSQL = "INSERT INTO osseco_measures.number_of_commiters_date" + "(year,month,count) VALUES(" + year
					+ "," + month + "," + count + ")";
			PreparedStatement statementSQL = conn.prepareStatement(strSQL);
			statementSQL.executeUpdate();

		}
		conn.close();

	}

	/**
	 * Create Number Of Commiters Table
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static void createNumberOfContributorsTable(String url) throws Exception {

		EvolutionaryPRJ commitsEvolutionary;
		commitsEvolutionary = EvolutionaryPRJ.getEvolutionaryPRJ("data/json/scm-prj-all-evolutionary.json");
		List<String> listCommitersValue = commitsEvolutionary.getCommitters();
		List<String> listChangeddDate = commitsEvolutionary.getDate();

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		Connection conn = getConnection();

		SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
		Calendar cal = Calendar.getInstance();
		Date datet;

		int year;
		int month;
		int count;

		for (int i = 0; i < listCommitersValue.size(); i++) {
			count = Integer.parseInt(listCommitersValue.get(i));
			datet = formatter.parse(listChangeddDate.get(i));
			cal.setTime(datet);
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;

			String strSQL = "INSERT INTO osseco_measures.number_of_contributors_date" + "(year,month,count) VALUES("
					+ year + "," + month + "," + count + ")";
			PreparedStatement statementSQL = conn.prepareStatement(strSQL);
			statementSQL.executeUpdate();

		}
		conn.close();

	}

	/**
	 * create NumberMessages Table
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static void createNumberMessagesTable(String url) throws Exception {

		EvolutionaryMLS openedEvolutionary;
		openedEvolutionary = EvolutionaryMLS.getEvolutionaryMLS("data/json/mls-evolutionary.json");
		List<String> listMessageValue = openedEvolutionary.getSent();
		List<String> listMessageDate = openedEvolutionary.getDate();

		SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
		Calendar cal = Calendar.getInstance();
		Date datet;

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		Connection conn = getConnection();

		int year;
		int month;
		int count;

		for (int i = 0; i < listMessageValue.size(); i++) {
			count = Integer.parseInt(listMessageValue.get(i));
			datet = formatter.parse(listMessageDate.get(i));
			cal.setTime(datet);
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;

			String strSQL = "INSERT INTO osseco_measures.messages_sent_date" + "(year,month,count) VALUES(" + year + ","
					+ month + "," + count + ")";
			PreparedStatement statementSQL = conn.prepareStatement(strSQL);
			statementSQL.executeUpdate();

		}
		conn.close();
	}

	/**
	 * create NumberResponses Table
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static void createNumberResponsesTable(String url) throws Exception {

		EvolutionaryMLS openedEvolutionary;
		openedEvolutionary = EvolutionaryMLS.getEvolutionaryMLS("data/json/mls-evolutionary.json");
		List<String> listResponseValue = openedEvolutionary.getSent_response();
		List<String> listResponseDate = openedEvolutionary.getDate();

		SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
		Calendar cal = Calendar.getInstance();
		Date datet;

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		Connection conn = getConnection();

		int year;
		int month;
		int count;

		for (int i = 0; i < listResponseValue.size(); i++) {
			count = Integer.parseInt(listResponseValue.get(i));
			datet = formatter.parse(listResponseDate.get(i));
			cal.setTime(datet);
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;

			String strSQL = "INSERT INTO osseco_measures.sent_response_date" + "(year,month,count) VALUES(" + year + ","
					+ month + "," + count + ")";
			PreparedStatement statementSQL = conn.prepareStatement(strSQL);
			statementSQL.executeUpdate();

		}
		conn.close();
	}

	/**
	 * Add a record to the table of single measures
	 * 
	 * @param url
	 * @throws Exception
	 * @CORREGIR
	 */
	public static void addNumberOfPartnersRecord(String url) throws Exception {
		// https://www.eclipse.org/membership/exploreMembership.php
		// TODO: no calcula bien
		ArrayList<String> listOfPartners = new ArrayList<String>();

		OssecoCrawlerController crawlerController = new OssecoCrawlerController(url, CrawlerMeasures.Partners);
		StringBuilder htmlString = crawlerController.getHtmlCrawler();

		Pattern p = Pattern.compile("title=\"[a-zA-Z ]+", Pattern.MULTILINE);
		Matcher m = p.matcher(htmlString.toString());

		System.out.println(htmlString);

		try {
			FileWriter fw = new FileWriter("c:\\tmp\\testout.html");
			fw.write(htmlString.toString());
			fw.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		while (m.find()) {

			String partner = m.group(0).replace("title=\"", "");
			if (!listOfPartners.contains(partner)) {
				listOfPartners.add(partner);
			}

		}

		Date date = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		Connection conn = MeasureExtractor.getConnection();

		String strSQL = "INSERT INTO osseco_measures.single_measures "
				+ "(measure,year,month,count) VALUES(+\"numberOfPartners\"" + "," + year + "," + month + ","
				+ listOfPartners.size() + ")";

		PreparedStatement statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();
		conn.close();
	}

	/**
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static void addDateLastComit(String url) throws Exception {

		// "https://git.eclipse.org/c/"
		OssecoCrawlerController crawlerController = new OssecoCrawlerController(url, CrawlerMeasures.LastCommit);
		String htmlString = crawlerController.getHtmlCrawler().toString();

		Pattern p = Pattern.compile("\\d\\d\\s([d][a][y][s]|[w][e][e][k][s]|[m][o][n][t][h][s]|[y][e][a][r][s])",
				Pattern.MULTILINE);
		if (htmlString != null) {

			Matcher m = p.matcher(htmlString);

			int shortestTime = 0;
			int timeInDays;
			boolean isfirst = true;

			while (m.find()) {

				String[] strTime = m.group(0).split(" ");
				timeInDays = Integer.parseInt(strTime[0]);
				// TODO: refactor this code
				switch (strTime[1]) {
				case "weeks": {
					timeInDays *= 7;
					break;
				}
				case "months": {
					timeInDays *= 30;
					break;
				}
				case "years": {

					timeInDays *= 365;
					break;
				}
				/**
				 * minutes or hours
				 */
				default: {
					timeInDays = 1;
				}

				}
				if (isfirst) {
					shortestTime = timeInDays;
					isfirst = false;
				} else {
					if (shortestTime > timeInDays) {
						shortestTime = timeInDays;
					}
				}

			}

			Date date = new Date();

			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;

			System.out.println("Year-->" + year + " Month-->" + month);

			// String url1 = String.format("jdbc:mysql://%s:%d/%s?useSSL=false",
			// "localhost", 3306, "osseco_measures");

			// String url1 =
			// "jdbc:mysql://127.0.0.1:3306/osseco_measures?useSSL=false&serverTimezone=UTC"
			// + "";

			// Connection conn = DriverManager.getConnection(url1, "root", "09122006Hf");

			Connection conn = getConnection("serverTimezone", "UTC");

			String strSQL = "INSERT INTO osseco_measures.single_measures "
					+ "(measure,year,month,count) VALUES(+\"daysLastCommit\"" + "," + year + "," + month + ","
					+ shortestTime + ")";

			PreparedStatement statementSQL = conn.prepareStatement(strSQL);
			statementSQL.executeUpdate();
			conn.close();
		} else
			System.out.println("NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");

	}

	/**
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static void addDateLastRelease(String url) throws Exception {
		// "https://projects.eclipse.org/projects/eclipse
		OssecoCrawlerController crawlerController = new OssecoCrawlerController(url, CrawlerMeasures.LastRelease);
		String htmlString = crawlerController.getHtmlCrawler().toString();

		Pattern p = Pattern.compile("(</td><td>)[^<]+", Pattern.MULTILINE);
		Matcher m = p.matcher(htmlString);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date datet = new Date();

		Calendar cal = Calendar.getInstance();
		String date;

		String strToday = formatter.format(new Date());
		Date today = formatter.parse(strToday);

		boolean isfirst = true;
		Date greaterDate = null;
		while (m.find()) {
			date = m.group(0).replace("</td><td>", "");
			datet = formatter.parse(date);
			if (datet.compareTo(today) < 0) {
				if (isfirst) {
					greaterDate = datet;
					isfirst = false;
				} else if (datet.after(greaterDate)) {
					greaterDate = datet;
				}
			}

		}

		long diff = today.getTime() - greaterDate.getTime();
		long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

		cal.setTime(greaterDate);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		Connection conn = getConnection();

		String strSQL = "INSERT INTO osseco_measures.single_measures "
				+ "(measure,year,month,count) VALUES(+\"daysLastRelease\"" + "," + year + "," + month + "," + days
				+ ")";

		PreparedStatement statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();
		conn.close();

	}

	/**
	 * Add Commit Rate
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static void addCommitRate(String url) throws Exception {
		// "https://git.eclipse.org/c/
		OssecoCrawlerController crawlerController = new OssecoCrawlerController(url, CrawlerMeasures.CommitRate);
		String htmlString = crawlerController.getHtmlCrawler().toString();

		System.out.println(htmlString);
		Pattern p = Pattern.compile("[0-9]+", Pattern.MULTILINE);
		Matcher m = p.matcher(htmlString);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		new Date();

		Calendar cal = Calendar.getInstance();
		int numberOfDays = 0;
		int numberOfProjects = 1;

		boolean isfirst = true;
		while (m.find()) {

			if (isfirst) {
				numberOfDays = Integer.parseInt(m.group(0));
				isfirst = false;
			} else {
				numberOfProjects = Integer.parseInt(m.group(0));
			}
			System.out.println(m.group(0));
		}

		String strToday = formatter.format(new Date());
		formatter.parse(strToday);

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int cm = numberOfDays / numberOfProjects;

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		Connection conn = getConnection();

		String strSQL = "INSERT INTO osseco_measures.single_measures "
				+ "(measure,year,month,count) VALUES(+\"communityCommitRate\"" + "," + year + "," + month + "," + cm
				+ ")";

		PreparedStatement statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();
		conn.close();

	}

	/**
	 * Add a record to the table of single measures
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static void addNumberOfProjectTypes(String url) throws Exception {
		// https://projects.eclipse.org/projects/

		OssecoCrawlerController crawlerController = new OssecoCrawlerController(url, CrawlerMeasures.ProjectType);
		String htmlString = crawlerController.getHtmlCrawler().toString();

		Date date = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();
		Connection conn = getConnection("serverTimezone", "UTC");

		String strSQL = "INSERT INTO osseco_measures.single_measures "
				+ "(measure,year,month,count) VALUES(+\"numberOfProjectTypes\"" + "," + year + "," + month + ","
				+ Integer.parseInt(htmlString) + ")";

		PreparedStatement statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();
		conn.close();
	}

	/**
	 * 
	 * @param url
	 * @throws Exception
	 */
	public static void createCommitRateTable(String url) throws Exception {

		EvolutionaryMLS openedEvolutionary;
		openedEvolutionary = EvolutionaryMLS.getEvolutionaryMLS("data/json/mls-evolutionary.json");
		List<String> listMessageValue = openedEvolutionary.getSent();
		List<String> listMessageDate = openedEvolutionary.getDate();

		SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
		Calendar cal = Calendar.getInstance();
		Date datet;

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		Connection conn = getConnection("serverTimezone", "UTC");

		int year;
		int month;
		int count;

		int yearC = 0;
		int monthC = 0;
		int countC = 0;
		int iC = 0;

		for (int i = 0; i < listMessageValue.size(); i++) {
			count = Integer.parseInt(listMessageValue.get(i));
			datet = formatter.parse(listMessageDate.get(i));
			cal.setTime(datet);
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;
			if (count > countC) {
				yearC = year;
				monthC = month;
				countC = count;
				iC = i;
			}

		}

		int yearD = 0;
		int monthD = 0;
		int countD = 0;

		System.out.println("year->" + yearC + " month->" + monthC + " val->" + countC);
		String strSQL = "INSERT INTO osseco_measures.single_measures "
				+ "(measure,year,month,count) VALUES(+\"culminatingPoint\"" + "," + yearC + "," + monthC + "," + countC
				+ ")";

		PreparedStatement statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();

		for (int i = iC; i < listMessageValue.size(); i++) {
			count = Integer.parseInt(listMessageValue.get(i));
			datet = formatter.parse(listMessageDate.get(i));
			cal.setTime(datet);
			year = cal.get(Calendar.YEAR);
			month = cal.get(Calendar.MONTH) + 1;
			if (count <= countC * 0.8) {
				yearD = year;
				monthD = month;
				countD = count;
				iC = i;
				break;
			}

		}

		System.out.println("year->" + yearC + " month->" + monthC + " val->" + countC);
		strSQL = "INSERT INTO osseco_measures.single_measures "
				+ "(measure,year,month,count) VALUES(+\"decliningPoint\"" + "," + yearD + "," + monthD + "," + countD
				+ ")";

		statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();

		System.out.println("year->" + yearD + " month->" + monthD + " val->" + countD);

		conn.close();
	}

	/**
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void createTimelinessTable()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		Connection conn = getConnection("serverTimezone", "UTC");

		/*
		 * Connection conn= new ConnectionJDBC("ba16735c7f9a7f", "a8f86cb3", "mysql",
		 * "br-cdbr-azure-south-a.cloudapp.net", "3306",
		 * "ossecodatabase").getConnection();
		 */

		String strSQL = "SET GLOBAL innodb_buffer_pool_size=402653184";
		PreparedStatement statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();
		// ***************************************************************
		strSQL = "CREATE TABLE tmp_timeliness " + "select a.issue_id, a.submitted_on,"
				+ "( select b.submitted_on from comments b " + "where a.issue_id=b.issue_id"
				+ " order by issue_id LIMIT 1,1) " + "from comments a group by a.issue_id";
		statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();
		// ***************************************************************
		strSQL = "create table timeliness_datetimeliness_date "
				+ "select  year(submitted_on) as y, month(submitted_on)as month,"
				+ "ROUND(AVG(TIMESTAMPDIFF(SECOND,submitted_on,answered_on))) " + "from tmp_timeliness "
				+ "group by y, month";
		statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();
		conn.close();
	}

	/**
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void createTimeZoneTable()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		Connection conn = getConnection("serverTimezone", "UTC");

		/*
		 * Connection conn= new ConnectionJDBC("ba16735c7f9a7f", "a8f86cb3", "mysql",
		 * "br-cdbr-azure-south-a.cloudapp.net", "3306",
		 * "ossecodatabase").getConnection();
		 */

		String strSQL = "SET GLOBAL innodb_buffer_pool_size=402653184";
		PreparedStatement statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();
		// ***************************************************************
		strSQL = "create table tmp_geographic "
				+ "select  year(first_date) as year , month(first_date) as month, count(*) " + "from messages "
				+ "group by year, month, first_date_tz";

		statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();
		// ***************************************************************
		strSQL = "create table time_zone_date " + "select year, month, count(*) from tmp_geographic "
				+ "group by year, monthtime_zone_date";

		statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();
		conn.close();
	}

	/**
	 * Create the Gini Index Table
	 * 
	 * @throws Exception
	 */
	public static void createGiniIndexTable() throws Exception {

		// "https://git.eclipse.org/c/"
		OssecoCrawlerController objCrw = new OssecoCrawlerController("https://git.eclipse.org/c/",
				CrawlerMeasures.ActivityType);

		ArrayList<ActivityTypesCrawler.Activity> listOfActivities = objCrw.getActivitiesCrawler();

		/**
		 * Gini Index table update
		 */

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();

		Connection conn = getConnection("serverTimezone", "UTC");

		for (ActivityTypesCrawler.Activity a : listOfActivities) {
			calculateGiniIndex(a);
			System.out.println(a.getYear() + "---" + a.getMonth() + "---" + a.getGiniIndex());
			String strSQL = "INSERT INTO osseco_measures.gini_index_date " + "(year,month,count) VALUES(" + a.getYear()
					+ "," + a.getMonth() + "," + a.getGiniIndex() + ")";
			PreparedStatement statementSQL = conn.prepareStatement(strSQL);
			statementSQL.executeUpdate();
			System.out.println("Saving file");

			for (String s : a.getListURL()) {
				loggInFile("./loggOSSECO/urls.txt", a.getMonth() + "---" + a.getGiniIndex() + "---" + s);
			}
		}
		conn.close();
	}

	/**
	 * 
	 * @param a
	 */
	private static void calculateGiniIndex(ActivityTypesCrawler.Activity a) {

		float sum = 0.0f;
		float giniI = -1;
		System.out.println("Year->" + a.getYear() + " Month->" + a.getMonth());
		for (int i = 0; i < ActivityTypesCrawler.NUMBER_OF_ACTIVITIES; i++) {
			sum += (float) a.getListOfMembers()[i];
		}
		if (sum > 0.0) {
			giniI = 1;
			for (int i = 0; i < ActivityTypesCrawler.NUMBER_OF_ACTIVITIES; i++) {
				giniI -= (float) Math.pow((double) (a.getListOfMembers()[i] / (double) sum), 2.0);
				// System.out.println("Members-->"+a.getListOfMembers()[i]);
			}
		}

		a.setGiniIndex(Math.abs(giniI));
	}

	/**
	 * Create the Gini Index Table
	 * 
	 * @throws Exception
	 */
	public static void createNumberOfDownloadsTable() throws Exception {

		// "https://www.eclipse.org/downloads/"
		OssecoCrawlerController objCrw = new OssecoCrawlerController("https://www.eclipse.org/downloads/packages/",
				CrawlerMeasures.NumberOfDownloads);

		String htmlString = objCrw.getHtmlCrawler().toString();

		Date date = new Date();

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);

		// Connection conn = new ConnectionJDBC("root", "09122006Hf", "mysql",
		// "localhost", "3306", "osseco_measures")
		// .getConnection();
		Connection conn = getConnection("serverTimezone", "UTC");

		String strSQL = "INSERT INTO osseco_measures.single_measures "
				+ "(measure,year,month,count) VALUES(+\"numberOfDownloads\"" + "," + year + "," + month + ","
				+ Integer.parseInt(htmlString) + ")";

		PreparedStatement statementSQL = conn.prepareStatement(strSQL);
		statementSQL.executeUpdate();
		conn.close();
	}

	/**
	 * 
	 */
	private static void loggInFile(String file, String msg) throws IOException {
		FileWriter fw = null;
		BufferedWriter bw = null;
		PrintWriter out = null;
		try {
			fw = new FileWriter(file, true);
			bw = new BufferedWriter(fw);
			out = new PrintWriter(bw);
			out.println(msg);
			out.close();
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		} finally {
			if (out != null)
				out.close();
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) {
				// exception handling left as an exercise for the reader
			}
			try {
				if (fw != null)
					fw.close();
			} catch (IOException e) {
				// exception handling left as an exercise for the reader
			}

		}
	}

	private static Connection getConnection()

	{
		Properties prop = new Properties();
		File configFile = null;
		try {

			File configDir = new File(System.getProperty("catalina.base") + "/webapps/OSSECOMeasuresRESTServer",
					"conf");
			configFile = new File(configDir, "config.properties");
			InputStream stream = new FileInputStream(configFile);
			prop.load(stream);

			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			String dbms = prop.getProperty("dbms");
			String port = prop.getProperty("port");
			String server = prop.getProperty("server");
			String dbName = prop.getProperty("dbName");
			Connection conn = new ConnectionJDBC(user, password, dbms, server, port, dbName).getConnection();

			return conn;

		} catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException
				| SQLException ex) {
			ex.printStackTrace();
		} finally {

		}

		return null;

	}

	private static Connection getConnection(String propName, String propValue)

	{
		Properties prop = new Properties();
		File configFile = null;

		try {

			File configDir = new File(System.getProperty("catalina.base") + "/webapps/OSSECOMeasuresRESTServer",
					"conf");
			configFile = new File(configDir, "config.properties");
			InputStream stream = new FileInputStream(configFile);
			prop.load(stream);

			;
			String user = prop.getProperty("user");
			String password = prop.getProperty("password");
			prop.getProperty("dbms");
			String port = prop.getProperty("port");
			String server = prop.getProperty("server");
			String dbName = prop.getProperty("dbName");
			String driver = prop.getProperty("driver");

			// String url1 =
			// "jdbc:mysql://127.0.0.1:3306/osseco_measures?useSSL=false&serverTimezone=UTC"
			// + "";

			String url = "jdbc:mysql://" + server + ":" + port + "/" + dbName + "?useSSL=false&" + propName + "="
					+ propValue;

			Connection conn = DriverManager.getConnection(url, user, password);

			return conn;

		} catch (IOException | SQLException ex) {
			ex.printStackTrace();
		}

		return null;

	}
}
