package edu.jhu.cvrg.ceptests.viewpub;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import edu.jhu.cvrg.ceptests.CEPException;
import edu.jhu.cvrg.ceptests.upload.CEPUploadTester;
import edu.jhu.cvrg.seleniummain.BrowserEnum;
import edu.jhu.cvrg.seleniummain.LogfileManager;
import edu.jhu.cvrg.seleniummain.TestNameEnum;

public final class CEPViewPubsTester extends CEPUploadTester {
	
	private int numOriginalFiles;

	public CEPViewPubsTester(String site, String viewPath, String welcomePath,
			String userName, String passWord, boolean loginRequired,
			BrowserEnum whichBrowser) {
		super(site, viewPath, welcomePath, userName, passWord, loginRequired,
				whichBrowser);
		// TODO Auto-generated constructor stub
	}

	public CEPViewPubsTester(String site, String viewPath, String welcomePath,
			String userName, String passWord, boolean loginRequired) {
		super(site, viewPath, welcomePath, userName, passWord, loginRequired);
		// TODO Auto-generated constructor stub
	}

	public CEPViewPubsTester(String site, String viewPath, String welcomePath,
			String userName, String passWord, WebDriver existingDriver) {
		super(site, viewPath, welcomePath, userName, passWord, existingDriver);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void runAllTests() throws CEPException, IOException {
		portletLogMessages.add("Beginning CEP Tools View Publications tests");
		
		// In this case, go straight to step two tests since there is no search
		// TODO:  There is a known issue with checking if an error message pops up
		//        if no selection is made.  The portlet breaks down and it is very difficult for
		//        Selenium to recover due to the lack of a back button and a broken Next button on the first page.
		//        When these are fixed, 
		boolean ableToProceed = this.checkStep2Success("A7196:viewmypubs:j_idt16", false);
		
		portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		if(ableToProceed) {
			// if all goes well, check to make sure the add new files page loaded
			ableToProceed = this.checkAddNewPageSuccess();
			
			portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			
			if(ableToProceed) {
				// now check the File modification page 
				ableToProceed = this.checkModificationPageSuccess();
				
				portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				
				if(ableToProceed) {
					// make sure the final page loads and that it is possible to return to previous pages
					ableToProceed = this.checkFinalPage();
				}
			}
		}
		
		logger.addToLog(portletLogMessages, TestNameEnum.CEPVIEW);
		if(seleniumLogMessages.size() > 0) {
			logger.addToLog(seleniumLogMessages, TestNameEnum.SELENIUM);
		}
	}
	
	
	private boolean checkAddNewPageSuccess() {
		boolean success = true;
		
		portletLogMessages.add("\nTesting the Add New Files page:\n");
		
		try {
			// check to see if the Currently Stored Files table exists
			if(!(portletDriver.findElements(By.id("A7196:uploader:ft")).isEmpty())) {
				portletLogMessages.add("The Currently Stored Files table element on the View Publications page has successfully loaded");
				
				// make sure there is data in it, and keep track of how many files for later tests.  Will also need to check it from the search
				// portlet in the future to make sure the files are correct
				if(!(portletDriver.findElements(By.xpath("//tbody[@id='A7196:uploader:ft_data']/tr")).isEmpty())) {
					portletLogMessages.add("There are files on the entry");
					numOriginalFiles = portletDriver.findElements(By.xpath("//tbody[@id='A7196:uploader:ft_data']/tr")).size();
				}
				else {
					portletLogMessages.add("ERROR:  There are no existing files on this entry, there should be.");
					success = false;
				}
			}
			else {
				portletLogMessages.add("ERROR:  The Currently Stored Files table did not load");
				success = false;
			}
			
			// check to make sure the New Uploaded files  table exists
			if(!(portletDriver.findElements(By.id("A7196:uploader:newf")).isEmpty())) {
				portletLogMessages.add("The New Files table element on the View Publications page has successfully loaded");
	
			}
			else {
				portletLogMessages.add("ERROR:  The New Files table element on the View Publications page did not load");
				success = false;
			}
			
			// check to make sure the upload table exists
			if(!(portletDriver.findElements(By.id("A7196:uploader:j_idt60")).isEmpty())) {
				portletLogMessages.add("The main datatable on the download page has successfully loaded");
				
			}
			else {
				portletLogMessages.add("ERROR:  The table to upload new files does not exist");
				success = false;
			}
			
			// Click the Save Progress button
			if(!(portletDriver.findElements(By.id("A7196:uploader:j_idt66")).isEmpty())) {
				portletDriver.findElement(By.id("A7196:uploader:j_idt66")).click();
			}
			else {
				portletLogMessages.add("ERROR:  The Save Progress button does not exist");
				success = false;
			}
			
			// Click the next button
			if(!(portletDriver.findElements(By.id("A7196:uploader:j_idt67")).isEmpty())) {
				portletDriver.findElement(By.id("A7196:uploader:j_idt67")).click();
			}
			else {
				portletLogMessages.add("ERROR:  The Next button does not exist");
				success = false;
			}
		} catch (StaleElementReferenceException ser) {
			seleniumLogMessages.add("A stale element reference was caught on the Modify Files page in the View Publications portlet, details are here:\n" + LogfileManager.extractStackTrace(ser));
			success = false;
		} catch (NoSuchElementException nse) {
			seleniumLogMessages.add("A certain element does not exist on the Modify Files page in the View Publications portlet, details are here:\n" + LogfileManager.extractStackTrace(nse));
			success = false;
		}
		
		return success;
	}
	
	
	private boolean checkModificationPageSuccess() {
		boolean success = true;
		
		portletLogMessages.add("\nTesting the Modify Files page:\n");
		
		try {
			// check to see if the Currently Stored Files table exists
			if(!(portletDriver.findElements(By.id("A7196:describefileafterupload2:editList")).isEmpty())) {
				portletLogMessages.add("The Currently Stored Files table element on the View Publications page has successfully loaded");
				
				// make sure there is data in it, and keep track of how many files for later tests.  Will also need to check it from the search
				// portlet in the future to make sure the files are correct
				portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				
				if(!(portletDriver.findElements(By.xpath("//tbody[@id='A7196:describefileafterupload2:editList_data']/tr")).isEmpty())) {
					portletLogMessages.add("There are files on the entry");
					int numOriginalFilesEditPage = portletDriver.findElements(By.xpath("//tbody[@id='A7196:describefileafterupload2:editList_data']/tr")).size();
					
					
					if(numOriginalFilesEditPage != numOriginalFiles) {
						portletLogMessages.add("ERROR:  The number of files in the current list are not the same as the one on the previous page");
						success = false;
					}
					else {
						// edit all three values for one of the files
						if(!(portletDriver.findElements(By.id("A7196:describefileafterupload2:editList:0:j_idt76")).isEmpty())) {
							WebElement figureDiv = portletDriver.findElement(By.id("A7196:describefileafterupload2:editList:0:j_idt76"));
							WebElement figureInput = portletDriver.findElement(By.id("A7196:describefileafterupload2:editList:0:j_idt78"));
							WebElement figureNum = figureDiv.findElement(By.xpath(".."));
							
							figureNum.click();
							figureInput.clear();
							figureInput.sendKeys("1234");
							figureInput.sendKeys(Keys.RETURN);
							
							portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
							portletLogMessages.add("Successfully edited the figure number");
						}
						else {
							portletLogMessages.add("ERROR:  Could not access the figure number box on the Currently Stored Files table");
							success = false;
						}
						
						if(!(portletDriver.findElements(By.id("A7196:describefileafterupload2:editList:0:j_idt80")).isEmpty())) {
							WebElement figureDiv = portletDriver.findElement(By.id("A7196:describefileafterupload2:editList:0:j_idt80"));
							WebElement figureInput = portletDriver.findElement(By.id("A7196:describefileafterupload2:editList:0:j_idt82"));
							WebElement figureNum = figureDiv.findElement(By.xpath(".."));
							
							figureNum.click();
							figureInput.clear();
							figureInput.sendKeys("4321");
							figureInput.sendKeys(Keys.RETURN);
							
							portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
							portletLogMessages.add("Successfully edited the panel number");
						}
						else {
							portletLogMessages.add("ERROR:  Could not access the panel number box on the Currently Stored Files table");
							success = false;
						}
						
						if(!(portletDriver.findElements(By.id("A7196:describefileafterupload2:editList:0:j_idt84")).isEmpty())) {
							WebElement figureDiv = portletDriver.findElement(By.id("A7196:describefileafterupload2:editList:0:j_idt84"));
							WebElement figureInput = portletDriver.findElement(By.id("A7196:describefileafterupload2:editList:0:j_idt86"));
							WebElement figureNum = figureDiv.findElement(By.xpath(".."));
							
							figureNum.click();
							figureInput.clear();
							figureInput.sendKeys("A test description");
							figureInput.sendKeys(Keys.RETURN);
							
							portletDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
							portletLogMessages.add("Successfully edited the description");
						}
						else {
							portletLogMessages.add("ERROR:  Could not access the description box on the Currently Stored Files table");
							success = false;
						}
						
					}
				}
				else {
					portletLogMessages.add("ERROR:  There are no existing files on this entry, there should be.");
					success = false;
				}
			}
			else {
				portletLogMessages.add("ERROR:  The Currently Stored Files table did not load");
				success = false;
			}
			
			// check to make sure the New Uploaded files  table exists
			if(!(portletDriver.findElements(By.id("A7196:describefileafterupload2:editList2")).isEmpty())) {
				portletLogMessages.add("The New Files table element on the View Publications page has successfully loaded");
	
			}
			else {
				portletLogMessages.add("ERROR:  The New Files table element on the View Publications page did not load");
				success = false;
			}
			
			// Click the Save Progress button
			if(!(portletDriver.findElements(By.id("A7196:describefileafterupload2:j_idt109")).isEmpty())) {
				portletDriver.findElement(By.id("A7196:describefileafterupload2:j_idt109")).click();
			}
			else {
				portletLogMessages.add("ERROR:  The Save Progress button does not exist");
				success = false;
			}
			
			// Click the next button
			if(!(portletDriver.findElements(By.id("A7196:describefileafterupload2:movetstep")).isEmpty())) {
				portletDriver.findElement(By.id("A7196:describefileafterupload2:movetstep")).click();
			}
			else {
				portletLogMessages.add("ERROR:  The Next button does not exist");
				success = false;
			}
		} catch (StaleElementReferenceException ser) {
			seleniumLogMessages.add("A stale element reference was caught on the Modify Files page in the View Publications portlet, details are here:\n" + LogfileManager.extractStackTrace(ser));
			success = false;
		} catch (NoSuchElementException nse) {
			seleniumLogMessages.add("A certain element does not exist on the Modify Files page in the View Publications portlet, details are here:\n" + LogfileManager.extractStackTrace(nse));
			success = false;
		}
		
		
		return success;
	}
	
	private boolean checkFinalPage() {
		boolean success = true;
		
		// check to make sure the upload table exists
		if(!(portletDriver.findElements(By.id("A7196:finalcomplete:ft_data")).isEmpty())) {
			portletLogMessages.add("The main datatable on the download page has successfully loaded");
			
			// TODO:  Check to make sure the values on the page are the same as the ones changed earlier.
		}
		else {
			portletLogMessages.add("ERROR:  The table to download files does not exist");
			success = false;
		}
		
		// Click the Save Progress button
		if(!(portletDriver.findElements(By.id("A7196:finalcomplete:startover")).isEmpty())) {
			portletDriver.findElement(By.id("A7196:finalcomplete:startover")).click();
		}
		else {
			portletLogMessages.add("ERROR:  The Save Progress button does not exist");
			success = false;
		}
		
		return success;
	}

}
