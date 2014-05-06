package edu.jhu.cvrg.ceptests.search;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import edu.jhu.cvrg.ceptests.CEPException;
import edu.jhu.cvrg.ceptests.GenericCEPTester;
import edu.jhu.cvrg.ceptests.TestScenarioEnum;
import edu.jhu.cvrg.seleniummain.BrowserEnum;
import edu.jhu.cvrg.seleniummain.LogfileManager;
import edu.jhu.cvrg.seleniummain.TestNameEnum;

public final class CEPSearchTester extends GenericCEPTester {

	public CEPSearchTester(String site, String viewPath, String welcomePath,
			String userName, String passWord, boolean loginRequired,
			BrowserEnum whichBrowser) {
		super(site, viewPath, welcomePath, userName, passWord, loginRequired, whichBrowser);
	}
	
	public CEPSearchTester(String site, String viewPath, String welcomePath,
			String userName, String passWord, boolean loginRequired) {
		super(site, viewPath, welcomePath, userName, passWord, loginRequired);
		// TODO Auto-generated constructor stub
	}
	
	public CEPSearchTester(String site, String viewPath, String welcomePath, String userName, String passWord, WebDriver existingDriver) {
		super(site, viewPath, welcomePath, userName, passWord, existingDriver);
	}
	
	@Override
	public void runAllTests() throws CEPException, IOException {
		
		String inputBoxID = "A0660:step1start:se";
		String step1NextButtonID = "A0660:step1start:step1next";
		String step2NextButtonID = "A0660:myform1:step2next2";
		
		portletLogMessages.add("Beginning CEP Search Portlet Tests");
		
		// first, run different test case methods for each type of case
		for(TestScenarioEnum testCase : TestScenarioEnum.values()) {
			portletLogMessages.add("*********************");
			portletLogMessages.add("Currently searching by " + testCase);
			
			
			switch(testCase) {
				case BLANK:
					this.emptySearch(inputBoxID, step1NextButtonID);
					break;
				case FIRSTNAME:
					this.searchByFirstname(inputBoxID, step1NextButtonID, step2NextButtonID);
					if(step2Success) {
						this.testLastStep();
					}
					break;
				case FULLNAME:
					this.searchByFullname(inputBoxID, step1NextButtonID, step2NextButtonID);
					if(step2Success) {
						this.testLastStep();
					}
					break;
				case LASTNAME:
					this.searchByLastname(inputBoxID, step1NextButtonID, step2NextButtonID);
					if(step2Success) {
						this.testLastStep();
					}
					break;
				case PUBMEDID:
					this.searchByPubmedID(inputBoxID, step1NextButtonID, step2NextButtonID);
					if(step2Success) {
						this.testLastStep();
					}
					break;
				case GIBBERISH:
					this.gibberishSearch(inputBoxID, step1NextButtonID, step2NextButtonID);
					break;
				case FIRSTINITIAL:
					this.searchByFirstInitial(inputBoxID, step1NextButtonID, step2NextButtonID);
					if(step2Success) {
						this.testLastStep();
					}
					break;
				case TITLE:
					this.searchByTitle(inputBoxID, step1NextButtonID, step2NextButtonID);
					if(step2Success) {
						this.testLastStep();
					}
					break;
				default:
					break;
			}
			portletLogMessages.add("*********************");
			
			portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			portletLogMessages.add("Test case finished, resetting page");
			portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			this.resetPage(inputBoxID);
		}
		
		logger.addToLog(portletLogMessages, TestNameEnum.CEPSEARCH);
		if(seleniumLogMessages.size() > 0) {
			logger.addToLog(seleniumLogMessages, TestNameEnum.SELENIUM);
		}
	}

	@Override
	protected void resetPage(String firstStepInputBoxID) {
		
		// Since the back buttons on each page go directly to the starting page, there is no need to iterate through the back buttons
		portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			try {
				// check to see if it is on the search results page
				if(!(portletDriver.findElements(By.id("A0660:myform1:step2back2")).isEmpty())) {
					portletLogMessages.add("Currently on search results page, clicking back button");
					portletDriver.findElement(By.id("A0660:myform1:step2back2")).click();
				}
				// next, check to see if it is on the download page
				else if(!(portletDriver.findElements(By.id("A0660:finalcomplete:startover")).isEmpty())) {
					portletLogMessages.add("Currently on download page, clicking Start Over button");
					portletDriver.findElement(By.id("A0660:finalcomplete:startover")).click();
				}	
			} catch(NoSuchElementException ne) {
				seleniumLogMessages.add("ERROR:  A NoSuchElemenetException was caught while resetting the page.  Here are the details:  " + LogfileManager.extractStackTrace(ne));
			} finally {
				
				portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				
				// finally, see if we arrived back at the main page, stop testing if we did not
				// If the first page can't be found, then later tests
				if(portletDriver.findElements(By.id(firstStepInputBoxID)).isEmpty()) {
					portletLogMessages.add("ERROR:  Unable to return to the original starting page, one of the back buttons is missing or non-functional.  Doing a refresh of the page");
					
					portletDriver.navigate().refresh();
				}
			}
	}
	
	private void testLastStep() {
		boolean success = false;
		
		success = this.checkStep3Success();
		
		if(success) {
			portletLogMessages.add("Loading the download page has succeeded");
			
			
		// TODO:  In the future, download buttons will be tested, but since these are being run in a loop and the test needs
		// to reset the page each time, a way to know when the manual download process has finished must be found.  If such a way is
		// found, then testing the download buttons should go here.
			
			

		}
		else {
			portletLogMessages.add("ERROR:  Loading the download page has failed");
		}
	}
	
	private boolean checkStep3Success() {
		boolean success = false;
		
		// check to see if the form exists at all
		if(!(portletDriver.findElements(By.id("A0660:finalcomplete")).isEmpty())) {
			portletLogMessages.add("The form element on the download page has successfully loaded");
			// now see if the datatable and buttons are present
			if(!(portletDriver.findElements(By.xpath("//table")).isEmpty())) {
				portletLogMessages.add("The main datatable on the download page has successfully loaded");
				success = true;
			}
			
		}
		
		return success;
	}
	

}
