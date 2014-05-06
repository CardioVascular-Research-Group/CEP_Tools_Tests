package edu.jhu.cvrg.ceptests;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import edu.jhu.cvrg.seleniummain.BaseFunctions;
import edu.jhu.cvrg.seleniummain.BrowserEnum;
import edu.jhu.cvrg.seleniummain.LogfileManager;
import edu.jhu.cvrg.authenticationtests.*;

public abstract class GenericCEPTester extends BaseFunctions {
	
	protected boolean step1Success = false;
	protected boolean step2Success = false;
	
	protected CEPTestProperties cepProps;

	protected GenericCEPTester(String site, String viewPath, String welcomePath,
			String userName, String passWord, boolean loginRequired) {
		super(site, viewPath, welcomePath, userName, passWord, loginRequired);
		
		cepProps = CEPTestProperties.getInstance();
	}
	
	protected GenericCEPTester(String site, String viewPath, String welcomePath, String userName, String passWord, WebDriver existingDriver) {
		super(site, viewPath, welcomePath, userName, passWord, existingDriver);
		cepProps = CEPTestProperties.getInstance();
	}
	
	protected GenericCEPTester(String site, String viewPath, String welcomePath,
			String userName, String passWord, boolean loginRequired,
			BrowserEnum whichBrowser) {
		super(site, viewPath, welcomePath, userName, passWord, loginRequired, whichBrowser);
		cepProps = CEPTestProperties.getInstance();
	}

	// This overridden version uses the GlobusLogin class for testing 
	@Override
	public final void login(boolean newWindowNeeded) {
		GlobusLogin gLogin;
		
		if(newWindowNeeded) {
			// create new window
			gLogin = new GlobusLogin(this.host, this.portletPage, this.welcomeScreen, this.username, this.password, newWindowNeeded, browser);
		}
		else {
			// pass the current window that the portlet driver is using 
			gLogin = new GlobusLogin(this.host, this.portletPage, this.welcomeScreen, this.username, this.password, this.portletDriver);
		}
		
		// test if login through Globus works.
		// Note:  Technically speaking the Globus doesn't doesn't explicitly require Globus so this can work with
		//        a non-Globus enabled machine too
		try {
			boolean isSuccess = gLogin.testGlobus();
			
			if(!isSuccess) {
				portletLogMessages.add("Login failed");
			}
			else {
				portletLogMessages.add("Login succeeded");
			}
			
		} catch (IOException e) {
			seleniumLogMessages.add("An IOException was found, here are the details:\n" + LogfileManager.extractStackTrace(e));
			e.printStackTrace();
		}
	}
	
	public abstract void runAllTests() throws CEPException, IOException;
	
	protected void emptySearch(String inputBoxID, String nextButtonID) {
		portletLogMessages.add("Entering nothing in the search, expecting a message prompting to enter something into the search box:");
		
		portletDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		
		try {
			portletDriver.findElement(By.id(inputBoxID)).clear();
			portletDriver.findElement(By.id(nextButtonID)).click();
			portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			
			if(!(portletDriver.findElements(By.xpath("//li[@class='portlet-msg-error']")).isEmpty())) {
				portletLogMessages.add("An empty string was entered into the search field.\n  A message asking to enter data a value in the search field has successfully been given");
			}
			else {
				portletLogMessages.add("ERROR:  An empty string was entered into the search field, but no error message displayed");
			}
		} catch (NoSuchElementException ne) {
			seleniumLogMessages.add("An element in the input page was unable to be found, here is more information:  " + LogfileManager.extractStackTrace(ne));
		}
	}
	
	// The search methods will be completed up to the point where the checkStep2Success method is called.  An overridden version
	// can be added according the individual needs of each portlet
	
	protected void searchByFirstname(String inputBoxID, String step1NextID, String step2NextButtonID) {
		// TODO:  Utilize the CEPTestProperties class for this
		String inputValue = cepProps.getFirstname();
		
		portletLogMessages.add("Searching by first name, first name used is " + inputValue);
		
		this.conductCommonTests(inputBoxID, step1NextID, inputValue, step2NextButtonID);
	}
	
	protected void searchByLastname(String inputBoxID, String step1NextID, String step2NextButtonID) {
		// TODO:  Utilize the CEPTestProperties class for this
		String inputValue = cepProps.getLastname();
		
		portletLogMessages.add("Searching by last name, last name used is " + inputValue);
		
		this.conductCommonTests(inputBoxID, step1NextID, inputValue, step2NextButtonID);
	}
	
	protected void searchByPubmedID(String inputBoxID, String step1NextID, String step2NextButtonID) {
		// TODO:  Utilize the CEPTestProperties class for this
		String inputValue = cepProps.getPubmedID();
		
		portletLogMessages.add("Searching by Pubmed ID, Pubmed ID used is " + inputValue);
		
		this.conductCommonTests(inputBoxID, step1NextID, inputValue, step2NextButtonID);
	}
	
	protected void searchByFullname(String inputBoxID, String step1NextID, String step2NextButtonID) {
		// TODO:  Utilize the CEPTestProperties class for this
		String inputValue = cepProps.getFullname();
		
		portletLogMessages.add("Searching by full name, full name used is " + inputValue);
		
		this.conductCommonTests(inputBoxID, step1NextID, inputValue, step2NextButtonID);
	}
	
	protected void gibberishSearch(String inputBoxID, String step1NextID, String step2NextButtonID) {
		String inputValue = cepProps.getGibberish();
		
		portletLogMessages.add("Putting in a junk value, value is " + inputValue);
		
		this.conductCommonTests(inputBoxID, step1NextID, inputValue, step2NextButtonID);
	}
	
	protected void searchByFirstInitial(String inputBoxID, String step1NextID, String step2NextButtonID) {
		String inputValue = cepProps.getFirstinitial();
		
		portletLogMessages.add("Putting in a Last name and first initial, value is " + inputValue);
		
		this.conductCommonTests(inputBoxID, step1NextID, inputValue, step2NextButtonID);
	}
	
	protected void searchByTitle(String inputBoxID, String step1NextID, String step2NextButtonID) {
		String inputValue = cepProps.getTitle();
		
		portletLogMessages.add("Putting in a junk value, value is " + inputValue);
		
		this.conductCommonTests(inputBoxID, step1NextID, inputValue, step2NextButtonID);
	}
	
	// This will carry out both step 1 and 2, which are very similar in both upload and search
	protected void conductCommonTests(String inputBoxID, String step1NextID, String inputValue, String step2NextButtonID) {
		step1Success = this.checkStep1Success(inputBoxID, step1NextID, inputValue);
		
		if(step1Success) {
			portletLogMessages.add("Step 1 has been completed");
			
			// check to see if the data table reports that no search results were found
			if(!(portletDriver.findElements(By.xpath("//tr[@class='ui-widget-content ui-datatable-empty-message']")).isEmpty())) {
				portletLogMessages.add("No search results were found");
			}
			
			step2Success = this.checkStep2Success(step2NextButtonID);
		}
		else {
			portletLogMessages.add("ERROR:  Unable to reach data results page");
			step2Success = false;
		}
	}
	
	protected boolean checkStep1Success(String inputBoxID, String step1NextID, String inputValue) {
		// The main goal is to check for the existence of the datatable and the back and next buttons on the next page.  If there is a better way to test this then
		// this can be replaced with that check instead.
		boolean success = false;
		
		try {
			portletDriver.findElement(By.id(inputBoxID)).clear();
			portletDriver.findElement(By.id(inputBoxID)).sendKeys(inputValue);
			portletDriver.findElement(By.id(step1NextID)).click();
			
			portletDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			
	
			
			if(!(portletDriver.findElements(By.xpath("//table")).isEmpty())) {
				portletLogMessages.add("The datatable on the second step exists");
				success = true;
				
			}
			else {
				portletLogMessages.add("The datatable is missing from the page");
			}
		} catch (NoSuchElementException ne) {
			seleniumLogMessages.add("ERROR:  An element was not found when checking the search results page.  This is the stack trace:\n" + LogfileManager.extractStackTrace(ne));
		}
		
		return success;
	}
	
	protected boolean checkStep2Success(String step2NextButtonID) {
		boolean success = true;
		
		// first, if there are no results or nothing has been highlighted, check the next button and make sure it does not proceed to the next page
		try {
			portletDriver.findElement(By.id(step2NextButtonID)).click();
			portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			
			// unfortunately it appears that the inline style is the only thing these error messages have in common across the portlets
			if(!(portletDriver.findElements(By.xpath("//span[@style='color:red']")).isEmpty())) { 
				portletLogMessages.add("There were no search results, and clicking Next produced the correct message");
			}
			else {
				portletLogMessages.add("ERROR:  There were no search results selected, but clicking Next did not produce a message");
				success = false;
			}
			
			// select an entry and then try again, it should proceed this time				
			if(!(portletDriver.findElements(By.xpath("//tr[@class='ui-widget-content ui-datatable-empty-message']")).isEmpty())) {
				portletLogMessages.add("No search results were present, and clicking Next did not allow the page to proceed");
			}
			else {
				portletDriver.findElement(By.xpath("//tr[@data-rk='1']/td")).click();
				portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				portletDriver.findElement(By.id(step2NextButtonID)).click();
				portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			}			
		} catch(NoSuchElementException ne) {
			seleniumLogMessages.add("Selenium was unable to find an element in the search results page, here is the full stack trace:\n" + LogfileManager.extractStackTrace(ne));
			success = false;
		}
		
		return success;
	}
	
	protected abstract void resetPage(String firstStepInputBoxID);
	
	

}
