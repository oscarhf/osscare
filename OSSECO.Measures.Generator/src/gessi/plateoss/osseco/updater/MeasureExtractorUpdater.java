package gessi.plateoss.osseco.updater;



import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import gessi.plateoss.osseco.measure_calculator.MeasureExtractor;
import gessi.plateoss.osseco.sourceconn.CrawlerMeasures;
import gessi.plateoss.osseco.sourceconn.OssecoCrawlerController;

public class MeasureExtractorUpdater {

	
	
	public static void updateMeasures() {
		// TODO Auto-generated method stub
		
		  try {
			Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			testMeasureExtractor();
			System.out.println("Done!");
			//testCrawler();
			
			
		System.out.println("Done");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	@SuppressWarnings("unused")
	private static void testCrawler()
	{
		//OssecoCrawlerController  objCrw = new OssecoCrawlerController("https://git.eclipse.org/c/", CrawlerMeasures.CommitRate);
		
		OssecoCrawlerController  objCrw = new OssecoCrawlerController("https://www.eclipse.org/downloads/packages/", CrawlerMeasures.NumberOfDownloads);
		
		try {
		 	 System.out.println(objCrw.getHtmlCrawler());
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static void testMeasureExtractor()
	{
		      	 
		try {
			
			/**
			 * Measures For OSSCARE 
			 */
			MeasureExtractor.addNumberOfPartnersRecord("https://www.eclipse.org/membership/exploreMembership.php#tab-all-members");
			MeasureExtractor.addDateLastComit("https://git.eclipse.org/c/");
			MeasureExtractor.addDateLastRelease("https://projects.eclipse.org/projects/eclipse");			
			MeasureExtractor.createVersionHistoryTable("https://archive.eclipse.org/eclipse/downloads/");
			MeasureExtractor.createNumberOfDownloadsTable();
			MeasureExtractor.addNumberOfProjectTypes("https://projects.eclipse.org/");
			
		
			
			
			//MeasureExtractor.addCommitRate("https://git.eclipse.org/c/4diac/org.eclipse.4diac.forte.git/log/");
			
			//MeasureExtractor.createVersionHistoryTable("https://archive.eclipse.org/eclipse/downloads/");
			//MeasureExtractor.createBugsClosedTable("");
			
			//MeasureExtractor.createBugsOpenedTable("");
			
			//MeasureExtractor.createFilesChangedTable("");
			
			//MeasureExtractor.createNumberFilesPerReleaseTable("");
			
			//MeasureExtractor.createNumberOfCommitsTable("");
			
			//MeasureExtractor.createNumberOfCommitersTable("");
			//MeasureExtractor.createNumberResponsesTable("");
			//MeasureExtractor.addNumberOfPartnersRecord("https://www.eclipse.org/membership/exploreMembership.php#allmembers");
			//MeasureExtractor.addDateLastComit("https://git.eclipse.org/c/");
			//MeasureExtractor.addDateLastRelease("https://projects.eclipse.org/projects/eclipse");
			// FALTA EJECUTAR DE NUEVO MeasureExtractor.addCommitRate("https://git.eclipse.org/c/");
			//MeasureExtractor.addNumberOfProjectTypes("https://projects.eclipse.org/");
			//MeasureExtractor.createCommitRateTable("");
			//MeasureExtractor.createGiniIndexTable(); 
			//MeasureExtractor.createNumberOfDownloadsTable();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}