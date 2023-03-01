package com.telus.rcms.tests.getLoyaltyAgreement;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
 * Testcase Name : TC05 Call getRewardCommitment operation for Telus Sub with TIASSETCREDIT and TIPROMOCREDIT when device is received_Update item with installment applied num as 1
 *
 */
public class TC05_Telus_with_TIA_TIP_Update_DeviceReceived extends BaseTest {

	String testCaseName = null;
	String scriptName = null;
	String testCaseDescription = null;
	String requestPayloadFilePath = null;
	String jsonPathLibrary = null;

	String environment = null;
	static String connectionString = null;

	String accountID = null;
	String subscriptionID = null;
	String subscriberNum = null;

	String startDate = null;
	String jsonString = null;

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
	}

	@Test(groups = {"Loyalty_Agreement","getLoyaltyAgreement","TC05_Telus_with_TIA_TIP_Update_InstallmentDate_to_1","CompleteRegressionSuite" })

	public void testMethod_getLoyaltyAgreement(ITestContext iTestContext) throws Exception {

		parentTest = ExtentTestManager.getTest();
		parentTest.assignCategory("GET_LOYALTY_AGREEMENT");

		Reporting.setNewGroupName("Automation Configurations / Environment Details & Data Setup");
		Reporting.logReporter(Status.INFO,
				"Automation Configuration - Environment Configured for Automation Execution [" + environment + "]");
		Reporting.printAndClearLogGroupStatements();

		/*** Test Case Details ***/
		Reporting.setNewGroupName("Test Case Details");
		Reporting.logReporter(Status.INFO, "Test Case Name : [" + scriptName + "]");
		Reporting.logReporter(Status.INFO, "Test Case Description : [" + testCaseDescription + "]");
		Reporting.printAndClearLogGroupStatements();

		/**
		 * API Call Steps
		 */

		/*** Test Case - Activation - AccessoryFinance ***/

		Reporting.setNewGroupName("ACCESS TOKEN GENERATION");
		String accessToken = APIUtils.getAccessToken(environment, "rewardService");
		
		Reporting.printAndClearLogGroupStatements();

		// Activation API Call

		Reporting.setNewGroupName("ACTIVATION SERVICE API CALL - AccessoryFinance");
		String apiEnv = GenericUtils.getAPIEnvironment(environment);
		Reporting.logReporter(Status.INFO, "API Test Env is : [" + apiEnv + "]");
		accountID = GenericUtils.getUniqueAccountID(apiEnv);
		subscriptionID = GenericUtils.getUniqueSubscriptionID(apiEnv);
		subscriberNum = GenericUtils.getUniqueSubscriberNumber(apiEnv);
		startDate = JSONUtils.getStartDate();
		System.setProperty("karate.auth_token", accessToken);
		System.setProperty("karate.accID", accountID);
		System.setProperty("karate.subID", subscriptionID);
		System.setProperty("karate.subNum", subscriberNum);
		System.setProperty("karate.startDate", startDate);
		System.setProperty("karate.apiEnv", apiEnv);

		Map<String, Object> apiOperation1 = GenericUtils.featureFileFailLoop_status(environment,
				"classpath:tests/RCMS/Activation/activationTC7.feature","tc07ActivatePrepaidSubTIASSETCREDIT_TIPROMOCREDITStatus","200");
		Reporting.logReporter(Status.INFO, "API Operation status: "
				+ apiOperation1.get("tc07ActivatePrepaidSubTIASSETCREDIT_TIPROMOCREDITStatus"));
		Reporting.logReporter(Status.INFO, "API Operation Request: "
				+ apiOperation1.get("tc07ActivatePrepaidSubTIASSETCREDIT_TIPROMOCREDITRequest"));

		Reporting.printAndClearLogGroupStatements();

		/*** Test Case - Update - Installment Date ***/

		// Update API Call

		Reporting.setNewGroupName("UPDATE SERVICE API CALL - TIASSETCREDIT, TIPROMOCREDIT - Installment date");
		Reporting.logReporter(Status.INFO, "API Test Env is : [" + apiEnv + "]");

		Map<String, Object> apiOperation2 = GenericUtils.featureFileFailLoop_status(environment,
				"classpath:tests/RCMS/UpdateItem/getLoyaltyAgreement/updateTC1.feature","tc01UpdateTelusSubWithTICreditInstalStatus","200");
		Reporting.logReporter(Status.INFO,
				"API Operation status: " + apiOperation2.get("tc01UpdateTelusSubWithTICreditInstalStatus"));
		Reporting.logReporter(Status.INFO,
				"API Operation Request: " + apiOperation2.get("tc01UpdateTelusSubWithTICreditInstalRequest"));

		Reporting.printAndClearLogGroupStatements();

		// Get Reward API Call

		Reporting.setNewGroupName("GET REWARD SERVICE API CALL - TIASSETCREDIT, TIPROMOCREDIT - Installment date");
		Reporting.logReporter(Status.INFO, "API Test Env is : [" + apiEnv + "]");

		Map<String, Object> apiOperation3 = GenericUtils.featureFileFailLoop_status(environment,
				"classpath:tests/RCMS/GetRewardCommitment/GetRewardCommTC5.feature","getRewardCommStatus","200");
		Reporting.logReporter(Status.INFO, "API Operation status: " + apiOperation3.get("getRewardCommStatus"));
		Reporting.logReporter(Status.INFO, "API Operation Request: " + apiOperation3.get("getRewardCommResponse"));

		jsonString = String.valueOf(apiOperation3.get("getRewardCommResponse"));
		Reporting.printAndClearLogGroupStatements();

		/*** DB VALIDATION ***/
		Reporting.setNewGroupName("DB VERIFICATION - TC02");
		payloadAndDbCheck();
		Reporting.printAndClearLogGroupStatements();

	}

	public void payloadAndDbCheck() throws SQLException, IOException, InterruptedException {

		DBUtils.callDBConnect();

		/**
		 * DB Verification Steps
		 */

		Reporting.logReporter(Status.INFO, "Pretty Payload: " + jsonString);

		// Declaring variable from payload

		GenericUtils.responseDBCheckAgrmtItemNew(jsonString, subscriptionID, 1);

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