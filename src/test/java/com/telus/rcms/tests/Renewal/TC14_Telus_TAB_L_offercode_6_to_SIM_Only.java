package com.telus.rcms.tests.Renewal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.telus.api.test.utils.APIJava;
import com.telus.rcms.jsonPathLibrary.ActivationPayloadJsonPath;
import com.telus.rcms.jsonPathLibrary.AgreementItem;
import com.telus.rcms.jsonPathLibrary.All;
import com.telus.rcms.utils.APIUtils;
import com.telus.rcms.utils.DBUtils;
import com.telus.rcms.utils.GenericUtils;
import com.telus.rcms.utils.JSONUtils;
import com.test.reporting.Reporting;
import com.test.ui.actions.BaseTest;
import com.test.ui.actions.Validate;
import com.test.ui.actions.WebDriverSteps;
import com.test.utils.SystemProperties;

import com.aventstack.extentreports.ExtentTest;
import com.test.reporting.ExtentTestManager;

/**
 * 
 * Testcase name : TC03 Customer having DB+DF+BIB -  Renewed to SIM only +AF
 *
 */

public class TC14_Telus_TAB_L_offercode_6_to_SIM_Only extends BaseTest {

	String testCaseName = null;
	String scriptName = null;
	String testCaseDescription = null;
	String requestPayloadFilePath = null;
	String jsonPathLibrary = null;

	String environment = null;
	static String connectionString = null;
	HashMap<String, String> hpaDetails = new HashMap<String, String>();
	String accountID = null;
	String subscriptionID = null;
	String subscriberNum = null;

	String startDate = null;
	String jsonString=null;
	String jsonString_activation=null;
	String jsonString_earlyPenalty=null;
	
	ExtentTest parentTest = null;
	

	/**
	 * @param iTestContext
	 */
	@BeforeTest(alwaysRun = true)
	public void BeforeMethod(ITestContext iTestContext) {

		testCaseName = this.getClass().getName();
		scriptName = GenericUtils.getTestCaseName(testCaseName);
		testCaseDescription = "The purpose of this test case is to verify \"" + scriptName + "\" workflow";
		environment = SystemProperties.EXECUTION_ENVIRONMENT;
		
		requestPayloadFilePath = System.getProperty("user.dir") +  "\\src\\test\\resources\\testSpecs\\RCMS\\Renewal\\TC03_Telus_DB_DF_BIB_Renewal_SIM_only_AF.json";
	}

	@Test(groups = { "Loyalty_Agreement","Renewal","TC03_Telus_DB_DF_BIB_Renewal_SIM_only_AF","CompleteRegressionSuite" })

	public void testMethod_Renewal(ITestContext iTestContext) throws Exception {

		parentTest = ExtentTestManager.getTest();
		parentTest.assignCategory("RENEWAL_SERVICE");

		
		Reporting.setNewGroupName("Automation Configurations / Environment Details & Data Setup");
		Reporting.logReporter(Status.INFO,
				"Automation Configuration - Environment Configured for Automation Execution [" + environment + "]");
		Reporting.printAndClearLogGroupStatements();

		/*** Test Case Details ***/
		Reporting.setNewGroupName("Test Case Details");
		Reporting.logReporter(Status.INFO, "Test Case Name : [" + scriptName + "]");
		Reporting.logReporter(Status.INFO, "Test Case Description : [" + testCaseDescription + "]");
		Reporting.logReporter(Status.INFO, "Request Payload Path : [" + requestPayloadFilePath + "]");
		Reporting.printAndClearLogGroupStatements();

		/**
		 * API Call Steps
		 */

		Reporting.setNewGroupName("ACCESS TOKEN GENERATION");
		String rewardServiceaccessToken = APIUtils.getAccessToken(environment, "rewardService");
		String violationaccessToken = APIUtils.getAccessToken(environment, "violation");
		
		
		Reporting.printAndClearLogGroupStatements();

		// Activation API Call
		
		Reporting.setNewGroupName("ACTIVATION SERVICE API CALL");
		String apiEnv = GenericUtils.getAPIEnvironment(environment);
		Reporting.logReporter(Status.INFO, "API Test Env is : [" + apiEnv + "]");
		hpaDetails = DBUtils.getHpaAllDetails("6");
		accountID = hpaDetails.get("BAN");
		subscriptionID = hpaDetails.get("SUBSCRIPTION_ID");
		subscriberNum = hpaDetails.get("CELL_PHONE_NO");
		Reporting.logReporter(Status.INFO, "Exisiting Subscription ID picked from CR DB: " + subscriptionID);
		Reporting.logReporter(Status.INFO, "Exisiting Account ID picked from CR DB: " + accountID);
		Reporting.logReporter(Status.INFO, "Exisiting Subscription Number picked from CR DB: " + subscriberNum);
		startDate = JSONUtils.getGMTStartDate();
		System.setProperty("karate.auth_token", rewardServiceaccessToken);
		System.setProperty("karate.auth_token_reward", rewardServiceaccessToken);
		System.setProperty("karate.auth_token_violation", violationaccessToken);
		System.setProperty("karate.accID", accountID);
		System.setProperty("karate.subID", subscriptionID);
		System.setProperty("karate.subNum", subscriberNum);
		System.setProperty("karate.startDate", startDate);
		System.setProperty("karate.apiEnv", apiEnv);
		Reporting.printAndClearLogGroupStatements();
		
		// Get Early Renewal Penalty API call

		Reporting.setNewGroupName("GET REWARD SERVICE API CALL");
		Reporting.logReporter(Status.INFO, "API Test Env is : [" + apiEnv + "]");

		Map<String, Object> apiOperation3 = GenericUtils.featureFileFailLoop_status(environment,
				"classpath:tests/RCMS/GetEarlyRenewalPenalty/Renewal/GetEarlyRenewalPenaltyTC2.feature","getEarlyRenewalPenaltyStatus","201");
		Reporting.logReporter(Status.INFO,
				"API Operation status: " + apiOperation3.get("getEarlyRenewalPenaltyResponse"));
		Reporting.logReporter(Status.INFO,
				"API Operation Request: " + apiOperation3.get("getEarlyRenewalPenaltyStatus"));

		jsonString_earlyPenalty = String.valueOf(apiOperation3.get("getEarlyRenewalPenaltyResponse")).replace("=", ":");
		Reporting.printAndClearLogGroupStatements();


		// Renewal API Call

		Reporting.setNewGroupName("RENEWAL SERVICE API CALL -SIM ONLY");
		Map<String, Object> apiOperation4 = GenericUtils.featureFileFailLoop_status(environment,
				"classpath:tests/RCMS/Renewal/notifySubscriptionRenewalTC14.feature","tc14RenewalStatus","200");
		Reporting.logReporter(Status.INFO, "API Operation status: " + apiOperation4.get("tc14RenewalStatus"));
		Reporting.logReporter(Status.INFO, "API Operation Request: " + apiOperation4.get("tc14RenewalRequest"));


		Reporting.printAndClearLogGroupStatements();
		
	
		
		/*** DB VALIDATION ***/
		Reporting.setNewGroupName("DB VERIFICATION");
		payloadAndDbCheck();
		Reporting.printAndClearLogGroupStatements();

	}

	public void payloadAndDbCheck() throws SQLException, IOException, InterruptedException {

		DBUtils.callDBConnect();

		/**
		 * DB Verification Steps
		 */

		Reporting.logReporter(Status.INFO, "Path of the file" + requestPayloadFilePath);
		String jsonString = GenericUtils.readFileAsString(requestPayloadFilePath);

		jsonString = jsonString.replace("#(subID)", subscriptionID).replace("#(subNum)", subscriberNum)
				.replace("#(accID)", accountID).replace("#(startDate)", startDate);
		Reporting.logReporter(Status.INFO, "Pretty Payload: " + jsonString);

		// Declaring variable from payload

		GenericUtils.validateActivationAfterRenewal(jsonString_activation,subscriptionID,3);
		
		GenericUtils.payloadDBCheckRenewalAgrmt(jsonString);
		GenericUtils.payloadnDBCheckAgrmtItemRenewal(jsonString, 2);
		GenericUtils.payloadnDBCheckPaymentRenewal(jsonString, jsonString_earlyPenalty, subscriptionID, 3);
		
		Reporting.logReporter(Status.INFO, "--------------------DB Validation Completed--------------------");
	}

	/**
	 * Close Connections
	 */

	@AfterMethod(alwaysRun = true)
	public void afterTest() {
		Reporting.setNewGroupName("Close DB Connection");
		try {
			DBUtils.dbDisConnect();
		} catch (SQLException e) {
			Reporting.logReporter(Status.INFO, "DB Connection Closed Successfully!");
		}
		Reporting.printAndClearLogGroupStatements();
		Reporting.setNewGroupName("Close All Browser");
		WebDriverSteps.closeTheBrowser();
		Reporting.printAndClearLogGroupStatements();
	}

}