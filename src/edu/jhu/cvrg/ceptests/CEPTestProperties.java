package edu.jhu.cvrg.ceptests;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class CEPTestProperties {
	private static final CEPTestProperties test = new CEPTestProperties();
	private String propsLocation;
	private String firstname;
	private String lastname;
	private String pubmedid;
	private String fullname;
	private String gibberish;
	private String firstinitial;
	private String title;
	
	private String uploadpath;
	private String searchpath;
	
	private CEPTestProperties() {
	       
	}
	
	public static CEPTestProperties getInstance() {
		return test;
	}
	
	public void loadConfiguration(String locationPath) {
		File file = new File(locationPath);
		
		propsLocation = file.getAbsolutePath();
		
		System.out.println(propsLocation);
		
		Properties props = new Properties();
		
		try {
			InputStream tStream = new FileInputStream(propsLocation);
			
			props.load(tStream);
			
			firstname = props.getProperty("firstname", "confignotfound");
			lastname = props.getProperty("lastname", "confignotfound");
			pubmedid = props.getProperty("existingpubmedid", "confignotfound");
			fullname = props.getProperty("fullname", "confignotfound");
			gibberish = props.getProperty("gibberish", "confignotfound");
			firstinitial = props.getProperty("firstinitial", "confignotfound");
			title = props.getProperty("title", "confignotfound");
			
			uploadpath = props.getProperty("uploadpath", "confignotfound");
			searchpath = props.getProperty("searchpath", "confignotfound");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public String getLastname() {
		return lastname;
	}
	
	public String getPubmedID() {
		return pubmedid;
	}
	
	public String getFullname() {
		return fullname;
	}
	
	public String getGibberish() {
		return gibberish;
	}
	
	public String getFirstinitial() {
		return firstinitial;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getUploadpath() {
		return uploadpath;
	}
	
	public String getSearchpath() {
		return searchpath;
	}
}
