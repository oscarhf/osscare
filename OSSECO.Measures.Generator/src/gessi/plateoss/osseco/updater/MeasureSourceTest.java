package gessi.plateoss.osseco.updater;

import java.sql.SQLException;
import java.text.ParseException;

import org.json.JSONException;

import gessi.plateoss.osseco.json_adapter.JSonMeasure;
import gessi.plateoss.osseco.measure_calculator.MeasureListAdapter;

/**
 * <dl>
 * <dt>Purpose: Test the OSSECO Generator measure component
 *
 * <dt>Description:
 * <dd>This class implement a basic program
 * <dt>Example usage:
 * <dd>
 * 
 * <pre>
 * </pre>
 * 
 * </dd>
 * </dl>
 * \
 * 
 * @version v0.1, 2016/03/30 (March) -- first release
 * @author Oscar Franco-Bedoya
 *         (<a href="oscarhf2002@hotmail.com">oscarhf2002.@hotmail.com</a>)
 */

public class MeasureSourceTest {
	public static void main(String[] args) {
		MeasureListAdapter objMHTAdapter = new MeasureListAdapter();
		JSonMeasure objJSonMeasure = new JSonMeasure();

		System.out.println("STARTING..");
		try {

			/**
			 * Number of partners
			 */

			System.out.println(objJSonMeasure.jSonMessure("number_of_partners",
					objMHTAdapter.getSingleMeasure("single_measures", "numberOfPartners"), "eclipse"));

			/**
			 * Changed files date
			 */

			//System.out.println(objJSonMeasure.jSonMessure("changed files",
				//	objMHTAdapter.getCollectionMeasureList("changed_files_date"), "eclipse"));

			/**
			 * Number of commiters
			 */

			// System.out.println(objJSonMeasure.jSonMessure("number of commiters",
			// objMHTAdapter.getCollectionMeasureList("number_of_commiters_date"),
			// "eclipse"));
			/**
			 * Number of commits
			 */

			// System.out.println(objJSonMeasure.jSonMessure("number of commits",
			// objMHTAdapter.getCollectionMeasureList("number_of_commits_date"),
			// "eclipse"));
			/**
			 * Number of contributors
			 */

			// System.out.println(objJSonMeasure.jSonMessure("number of contributors",
			// objMHTAdapter.getCollectionMeasureList("number_of_contributors_date"),
			// "eclipse"));

			/**
			 * Size of network community
			 */
			// System.out.println(objJSonMeasure.jSonMessure("mailing_list",objMHTAdapter.getCollectionMeasureList("mailing_list_date"),
			// "eclipse"));
			/**
			 * Main versions
			 */
			// System.out.println(objJSonMeasure.jSonMessure("main_versions",objMHTAdapter.getCollectionMeasureList("main_versions_date"),
			// "eclipse"));
			/**
			 * Number of passive users
			 */
			// System.out.println(objJSonMeasure.jSonMessure("passive_users",objMHTAdapter.getCollectionMeasureList("passive_users_date"),
			// "eclipse"));
			/**
			 * Number of opened_bugs
			 */
			// System.out.println(objJSonMeasure.jSonMessure("opened_bugs",
			// objMHTAdapter.getCollectionMeasureList("opened_bugs_date"), "eclipse"));
			/**
			 * Number of closed_bugs
			 */
			// System.out.println(objJSonMeasure.jSonMessure("closed_bugs",
			// objMHTAdapter.getCollectionMeasureList("closed_bugs_date"), "eclipse"));

			/**
			 * Files_Commit_Date
			 */

			// System.out.println(objJSonMeasure.jSonMessure("files_commit_date",objMHTAdapter.getCollectionMeasureList("files_commit_date"),
			// "eclipse"));

			/**
			 * Messages_Sent_Date
			 */

			// System.out.println(objJSonMeasure.jSonMessure("messages_sent_date",objMHTAdapter.getCollectionMeasureList("messages_sent_date"),
			// "eclipse"));

			/**
			 * Sent_Responses_Date
			 */

			// System.out.println(objJSonMeasure.jSonMessure("sent_response_date",objMHTAdapter.getCollectionMeasureList("sent_response_date"),
			// "eclipse"));
			/**
			 * timeliness
			 */

			// System.out.println(objJSonMeasure.jSonMessure("Timeliness",objMHTAdapter.getCollectionMeasureList("timeliness_date"),
			// "eclipse"));

			/**
			 * Days Last Commit
			 */
			// System.out.println(objJSonMeasure.jSonMessure("days_Last_Commit",objMHTAdapter.getSingleMeasure("single_measures",
			// "daysLastCommit"), "eclipse"));
			/**
			 * Days Last Release
			 */
			// System.out.println(objJSonMeasure.jSonMessure("days_Last_release",objMHTAdapter.getSingleMeasure("single_measures",
			// "daysLastRelease"), "eclipse"));

			/**
			 * Community Commit rate
			 */
			// System.out.println(objJSonMeasure.jSonMessure("community_commit_rate",objMHTAdapter.getSingleMeasure("single_measures",
			// "communityCommitRate"), "eclipse"));

			/**
			 * Number Of Events
			 */
			// System.out.println(objJSonMeasure.jSonMessure("number_Of_Events",objMHTAdapter.getCollectionMeasureList("number_of_events_date"),
			// "number_of_events_date"));

			/**
			 * Culminating Point
			 */
			// System.out.println(objJSonMeasure.jSonMessure("culminating_Point",objMHTAdapter.getSingleMeasure("single_measures",
			// "culminatingPoint"), "eclipse"));

			/**
			 * Declining Point
			 */
			// System.out.println(objJSonMeasure.jSonMessure("declining_Point",objMHTAdapter.getSingleMeasure("single_measures",
			// "decliningPoint"), "eclipse"));

			/**
			 * Time Zone
			 */
			// System.out.println(objJSonMeasure.jSonMessure("time_Zone",objMHTAdapter.getCollectionMeasureList("time_zone_date"),
			// "time_zone_date"));

			/**
			 * Number of project types
			 */

			// System.out.println(objJSonMeasure.jSonMessure("project_Types",objMHTAdapter.getSingleMeasure("single_measures",
			// "numberOfProjectTypes"), "eclipse"));

			/**
			 * Number of downloads
			 */

			// System.out.println(objJSonMeasure.jSonMessure("project_Types",objMHTAdapter.getSingleMeasure("single_measures",
			// "numberOfDownloads"), "eclipse"));

			System.out.println("DONE");

		} catch (JSONException | InstantiationException | IllegalAccessException | ClassNotFoundException
				| ParseException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
