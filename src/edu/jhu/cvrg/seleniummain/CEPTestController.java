package edu.jhu.cvrg.seleniummain;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import edu.jhu.cvrg.seleniummain.LogfileManager;
import edu.jhu.cvrg.ceptests.CEPTestProperties;
import edu.jhu.cvrg.ceptests.search.CEPSearchTester;
import edu.jhu.cvrg.ceptests.upload.CEPUploadTester;


public class CEPTestController extends TestController{

	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		if(args.length != 5 || args[0].equals("--help")) {
			System.out.println("Usage:  CVRG_Tests.jar <LOGON | CEP | ALL> <hostname> <username> <password> <logfile_location>\n");
			System.exit(0);
		}

		String testType = args[0];
		String hostname = args[1];
		String username = args[2];
		String password = args[3];
		String logfilePath = args[4];

		String commonPropsLocation = "./src/testconfig/global_properties.config";
		String cepPropsLocation = "./src/testconfig/cep_properties.config";
		
		// initialize the Singleton instance of the global properties
		CommonProperties init = CommonProperties.getInstance();
		init.loadConfiguration(commonPropsLocation);
		
		CEPTestController mainControl = new CEPTestController(hostname, logfilePath, username, password);
		
		TestControlTypeEnum testTypeEnum = TestControlTypeEnum.valueOf(testType);
		
		switch(testTypeEnum) {
			case LOGON:
				mainControl.testAuthentication();
				break;
			case CEP:
				mainControl.testCEPTools(cepPropsLocation);
				break;
			case ALL:
				mainControl.testAuthentication();
				mainControl.testCEPTools(cepPropsLocation);
				break;
			default:
				// Exit
				System.out.println("Invalid test option entered.\n");
				System.out.println("Usage:  CVRG_Tests.jar <LOGON | CEP | ALL> <hostname> <username> <password> <logfile_location>\n");
				System.exit(0);
				break;
		}
		
	}
	
	public CEPTestController(String newHostname, String newLogfilePath, String newUsername, String newPassword) {
		super(newHostname, newLogfilePath, newUsername, newPassword);
	}

	public void testCEPTools(String propertiesFileLocation) {
			
		CEPTestProperties testProps = CEPTestProperties.getInstance();
		
		try {
			this.setup();
		
			testProps.loadConfiguration(propertiesFileLocation);
			
			logger.addToLog("CEP Tools Selenium Test Begin:  " + dateFormat.format(todaysDate.getTime()));
			
			String uploadpath = testProps.getUploadpath();
			String searchpath = testProps.getSearchpath();
			
			CEPUploadTester upload = new CEPUploadTester(hostname, uploadpath, initialWelcomePath, username, password, true, whichBrowser);
			
			upload.login(false);
			upload.goToPage();

			upload.runAllTests();
			upload.logout();
			upload.close();
			
			CEPSearchTester search = new CEPSearchTester(hostname, searchpath, initialWelcomePath, username, password, true, whichBrowser);

			search.login(false);
			search.goToPage();

			search.runAllTests();
			search.logout();
			search.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
	}

}
