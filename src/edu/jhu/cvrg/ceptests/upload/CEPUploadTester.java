package edu.jhu.cvrg.ceptests.upload;

import java.io.IOException;
import java.util.ArrayDeque;  // Used to implement a stack (It is in most ways preferable to the actual Stack class)
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

public final class CEPUploadTester extends GenericCEPTester {
	private ArrayDeque<String> backButtonStack;

	public CEPUploadTester(String site, String viewPath, String welcomePath,
			String userName, String passWord, boolean loginRequired, BrowserEnum whichBrowser) {
		super(site, viewPath, welcomePath, userName, passWord, loginRequired, whichBrowser);
		backButtonStack = new ArrayDeque<String>();
	}
	
	public CEPUploadTester(String site, String viewPath, String welcomePath,
			String userName, String passWord, boolean loginRequired) {
		super(site, viewPath, welcomePath, userName, passWord, loginRequired);
		backButtonStack = new ArrayDeque<String>();
	}
	
	public CEPUploadTester(String site, String viewPath, String welcomePath, String userName, String passWord, WebDriver existingDriver) {
		super(site, viewPath, welcomePath, userName, passWord, existingDriver);
		backButtonStack = new ArrayDeque<String>();
	}

	@Override
	public void runAllTests() throws CEPException, IOException {
		// TODO Auto-generated method stub
		String inputBoxID = "A2724:j_idt7:titleinput";
		String step1NextButtonID = "A2724:j_idt7:step3next";
		String step2NextButtonID = "A2724:soak:step4nexttop";
		
		portletLogMessages.add("Beginning CEP Upload Portlet Tests");
		
		// first, run different test case methods for each type of case
		for(TestScenarioEnum testCase : TestScenarioEnum.values()) {
			portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			portletLogMessages.add("*********************");
			portletLogMessages.add("Currently searching by " + testCase);
			
			switch(testCase) {
				case BLANK:
					this.emptySearch(inputBoxID, step1NextButtonID);
					break;
				case FIRSTNAME:
					this.searchByFirstname(inputBoxID, step1NextButtonID, step2NextButtonID);
					if(step2Success) {
						this.testRemainingSteps();
					}
					break;
				case FULLNAME:
					this.searchByFullname(inputBoxID, step1NextButtonID, step2NextButtonID);
					if(step2Success) {
						this.testRemainingSteps();
					}
					break;
				case LASTNAME:
					this.searchByLastname(inputBoxID, step1NextButtonID, step2NextButtonID);
					if(step2Success) {
						this.testRemainingSteps();
					}
					break;
				case PUBMEDID:
					this.searchByPubmedID(inputBoxID, step1NextButtonID, step2NextButtonID);
					if(step2Success) {
						this.testRemainingSteps();
					}
					break;
				case GIBBERISH:
					this.gibberishSearch(inputBoxID, step1NextButtonID, step2NextButtonID);
					break;
				case FIRSTINITIAL:
					this.searchByFirstInitial(inputBoxID, step1NextButtonID, step2NextButtonID);
					if(step2Success) {
						this.testRemainingSteps();
					}
					break;
				case TITLE:
					this.searchByTitle(inputBoxID, step1NextButtonID, step2NextButtonID);
					if(step2Success) {
						this.testRemainingSteps();
					}
					break;
				default:
					break;
			}
			portletLogMessages.add("*********************");
			portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			portletLogMessages.add("Now resetting page");
			portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			this.resetPage(inputBoxID);
		}
		
		logger.addToLog(portletLogMessages, TestNameEnum.CEPUPLOAD);
		if(seleniumLogMessages.size() > 0) {
			logger.addToLog(seleniumLogMessages, TestNameEnum.SELENIUM);
		}

	}
	
	// This extends the base version by adding the back button ID for this step onto the stack for later use
	@Override
	protected boolean checkStep1Success(String inputBoxID, String step1NextID, String inputValue) {
		// The main goal is to check for the existence of the datatable and the back and next buttons on the next page.  If there is a better way to test this then
		// this can be replaced with that check instead.
		boolean success = super.checkStep1Success(inputBoxID, step1NextID, inputValue);
		
		if(!(portletDriver.findElements(By.id("A2724:soak:j_idt19"))).isEmpty()) {
			backButtonStack.push("A2724:soak:j_idt19");
		}
		
		return success;
	}
	
	// This extends the base version by adding the back button ID for this step onto the stack for later use
	@Override
	protected boolean checkStep2Success(String step2NextButtonID) {
		// The main goal is to check for the existence of the datatable and the back and next buttons on the next page.  If there is a better way to test this then
		// this can be replaced with that check instead.
		boolean success = super.checkStep2Success(step2NextButtonID);
		
		if(!(portletDriver.findElements(By.id("A2724:bee:j_idt75"))).isEmpty()) {
			backButtonStack.push("A2724:bee:j_idt75");
		}
		
		return success;
	}

	// This only tests up to the first three steps until a way around upload is found
	@Override
	protected void resetPage(String firstStepInputBoxID) {
		int i = backButtonStack.size();
		
		while(i >= 1) {
			
			String backButtonID = backButtonStack.pop();
			
			// check to see if it is on the current page
			if(!(portletDriver.findElements(By.id(backButtonID)).isEmpty())) {
				portletLogMessages.add("Clicking back button(s) to return to starting screen");
				portletDriver.findElement(By.id(backButtonID)).click();
				portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			}
			
			i--;

		}
		
		
		// finally, see if we arrived back at the main page, stop testing if we did not
		// If the first page can't be found, then later tests
		if(portletDriver.findElements(By.id(firstStepInputBoxID)).isEmpty()) {
			portletLogMessages.add("ERROR:  Unable to return to the original starting page, one of the back buttons is missing or non-functional.  Doing a refresh of the page");
			
			portletDriver.navigate().refresh();
		}

	}
	
	private void testRemainingSteps() {
		boolean success = false;
		
		success = this.checkStep3Success();
		
		try {
		if(success) {
			portletDriver.findElement(By.id("A2724:bee:j_idt80")).click();
			portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			
			// now see if the Upload page itself loaded
			success = this.checkStep4Success();
			
			if(success) {
				portletLogMessages.add("Loading the upload page has succeeded");
			}
			else {
				portletLogMessages.add("ERROR:  Loading the upload page has failed");
			}
			
		
		// TODO:  In the future, the final steps will be tested, but since these are being run in a loop and the test needs
		// to reset the page each time, a way to know when the manual upload process has finished must be found.  If such a way is
		// found, then testing the download buttons should go here.

		}
		else {
			portletLogMessages.add("ERROR:  Loading the verify citations page has failed");
		}
		} catch (NoSuchElementException ne) {
			seleniumLogMessages.add("While on the citation confirmation page, a NoSuchElementException was caught, here are the details:  " + LogfileManager.extractStackTrace(ne));
		}
	}
	
	private boolean checkStep3Success() {
		boolean success = false;
		
		portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		// check to see if the form exists at all
		if(!(portletDriver.findElements(By.id("A2724:bee")).isEmpty())) {
			// now see if the citation elements and buttons are present
			if(!(portletDriver.findElements(By.id("A2724:bee:selectedpubconfirmtt")).isEmpty())) {
				portletLogMessages.add("Field which displays the publication information title appears");
				
				if(!(portletDriver.findElements(By.id("A2724:bee:j_idt80")).isEmpty())) {
					portletLogMessages.add("The Next button appears");
						if(!(portletDriver.findElements(By.id("A2724:bee:j_idt75")).isEmpty())) {
							portletLogMessages.add("The Next button appears");
							portletLogMessages.add("The verify citations page has successfully loaded");
							success = true;
						}
					}
			}
			
		}
		
		return success;
	}
	
	private boolean checkStep4Success() {
		boolean success = false;
		
		portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		// check to see if the form exists at all
		if(!(portletDriver.findElements(By.id("A2724:j_idt82")).isEmpty())) {
			// now see if the upload button bar and buttons are present
			if(!(portletDriver.findElements(By.xpath("//div[@class='fileupload-buttonbar ui-widget-header ui-corner-top']")).isEmpty())) {
				//check the upload buttons first
				if(!(portletDriver.findElements(By.xpath("//button[@class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-left cancel']")).isEmpty()) && !(portletDriver.findElements(By.id("A2724:j_idt82:j_idt105_input")).isEmpty()) && !(portletDriver.findElements(By.xpath("//button[@class='ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-left cancel']")).isEmpty())) {
					portletLogMessages.add("The verify citations page has successfully loaded");
					
					// now check for the back and next buttons
					if(!(portletDriver.findElements(By.id("A2724:j_idt82:j_idt107")).isEmpty()) && !(portletDriver.findElements(By.id("A2724:j_idt82:j_idt112")).isEmpty())) {
						success = true;
						backButtonStack.push("A2724:j_idt82:j_idt107");
					}
				}
				else {
					portletLogMessages.add("ERROR:  One or more upload bars are missing");
				}
			}
			else {
				portletLogMessages.add("ERROR:  Upload bar is missing");
			}
			
		}
		
		return success;
	}

}
