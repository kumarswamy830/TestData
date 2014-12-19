package com.qtpselenium.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.sun.jna.platform.FileUtils;



public class Keywords {
	
	WebDriver driver=null;
	Properties OR=null;
	Properties ENV=null;
	WebDriver bak_firefox;
	WebDriver bak_chrome;
	WebDriver bak_ie;
	static Keywords k;
	WebDriverWait wait;
	
	Logger APPLICATION_LOGS=Logger.getLogger("devpinoyLogger");
	
	
	 public Keywords() {
		 
		 try{
		   OR=new Properties();
		   FileInputStream fo=new FileInputStream(System.getProperty("user.dir")+"\\src\\com\\qtpselenium\\salesforce\\config\\OR.properties");
		   OR.load(fo);
		   
		   ENV=new Properties();
		  String filename=OR.getProperty("environment")+".properties";
		      fo=new FileInputStream(System.getProperty("user.dir")+"\\src\\com\\qtpselenium\\salesforce\\config\\"+filename);
		      ENV.load(fo);
		    
		   
		 }catch(Exception e){
			 
			 e.printStackTrace();
			 
		 }
	 }
	 
	  
	
	public String openBrowser(String browser){
		
		   if(browser.equals("firefox") && bak_firefox!=null){
			    driver=bak_firefox;
			    return "Pass";
		   } else if(browser.equals("chrome") && bak_chrome!=null){
			    driver=bak_chrome;
			    return "Pass";
		   } else if(browser.equals("ie") && bak_ie!=null){
			    driver=bak_ie;
			    return "Pass";
		   }
			   
			  
		
		 if(browser.equals("firefox")){
			 driver=new FirefoxDriver();
			 bak_firefox=driver;
		 }else if(browser.equals("chrome")){
			 System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir") + "\\driver\\chromedriver.exe");
			 driver=new ChromeDriver();
			 bak_chrome=driver;
		 }else if(browser.equals("ie")){
			 System.setProperty("webdriver.ie.driver",System.getProperty("user.dir") + "\\driver\\IEDriverServer.exe");
			 driver=new InternetExplorerDriver();
			 bak_ie=driver;}
		 
		      driver.manage().window().maximize();
		   
		      driver.manage().timeouts().implicitlyWait(2,TimeUnit.SECONDS);
		      
		      //init webdriverwait
		       if(driver!=null){
		    	   
		    	   wait=new WebDriverWait(driver, 2);
		       }
		 
		      return "Pass";
	 }
	
	public static Keywords getInstanc(){
		  
		   if(k==null)
			   k=new Keywords();
		   
		   return k;
	  }
	
	public void executeKeywords(String testName,Xls_Reader xls,Hashtable<String,String> table){
		
		
		         int rows= xls.getRowCount("Test steps");
		         for(int rNum=2;rNum<=rows;rNum++){
		        	 
		        	 String tcid=xls.getCellData("Test steps", "TCID", rNum);
		        	 
		        if(tcid.equals(testName)){
		        	 
		        	 
		        	 String keyword=xls.getCellData("Test steps", "Keyword", rNum);
		        	 String object=xls.getCellData("Test steps", "Object", rNum);
		        	 String data=xls.getCellData("Test steps", "Data", rNum);
		        	 
		        	  String result="";
		        	 
		        	 //execute the keywords
		        	 if(keyword.equals("openBrowser"))
		        		 result=openBrowser(table.get(data));
		        	  else if(keyword.equals("navigate"))
		        		  result=navigate(data);
		        	  else if(keyword.equals("click"))
		        		  result=click(object);
		        	  else if(keyword.equals("input"))
		        		  result=input(object,table.get(data));
		        	  else if(keyword.equals("isElementPresent"))
	        		      result=isElementPresent(object);
		        	  else if(keyword.equals("verifyLogin"))
	        		      result=verifyLogin(table.get("Flag"));
		        	  else if(keyword.equals("waitForElementPresence"))
	        		      result=waitForElementPresence(object);
		        	 
		        	   //Assertions
		        	 
		        	    if(!result.equals("Pass")){
		        	    	
		        	    	//screenshot
		        	    	try{
		        	    		
		        	    		// testname_keywordname_line_num.jpg
		        	    		String filename=tcid+"_"+keyword+"_"+rNum+".jpg";
		        	    		
		        	    		File srcFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		        	    		org.apache.commons.io.FileUtils.copyFile(srcFile, new File(System.getProperty("user.dir")+"//screenshots//"+filename));
		        	    	}catch(Exception e){
		        	    		
		        	    	}
		        	    	
		        	    	String proceed=xls.getCellData("Test steps", "Proceed_On_Fail", rNum);
		        	    	
		        	    	if(proceed.equals("Y")){
		        	    		// fail and continue the test
		        	    		
		        	    		try{
		        	    			Assert.fail(result);
		        	    			
		                             }catch(Throwable t){
		                            	  System.out.println("****Error****");
		                            	  
		                            	//listeners
		                         		 
		                         		 ErrorUtil.addVerificationFailure(t);
		                             }
		        	    		
		        	    	}else{
		        	    	          // fail and stop
		        	    	          Assert.fail(result);
		        	    	     }    
		        	    }
		        	 
		        	 
		        	 
		        	    log(tcid+"---"+keyword+"----"+object+"----"+data+"----"+result);
		        	 
		        	 }
		         }
	
	}
     public String navigate(String URL){
    	 
    	   try{
	
	       driver.get(ENV.getProperty(URL));
	       
    	   }catch(Exception e){
    		   
    		   return "Fail - Unable to navigate to - "+ URL;
    		   
    	   }
	       
	       return "Pass";
		
	  }

     public String click(String xpathkey){
    	 
    	 try{
	
	     driver.findElement(By.xpath(OR.getProperty(xpathkey))).click();
	     
    	 }catch(Exception e){
    		 return "Fail - Unable to click on - "+ xpathkey;
    	 }
	     return "Pass";
	
      }

    public String input(String xpathkey,String text){
    	
    	 try{
	
	     driver.findElement(By.xpath(OR.getProperty(xpathkey))).sendKeys(text);
	     
    	 }catch(Exception e){
    		 
    		 return "Fail - Unable to write on - "+ xpathkey;
    		 
    		 
    		 
    	 }
	     return "Pass";
     }

    
    
    public boolean compareTitle(String actualtitle){
    	
    	String actual_title= OR.getProperty(actualtitle);
    	String expected_title=driver.getTitle();
    	
    	
    	if(expected_title.equals(actual_title))
    		 return true;
    	else 
    		return false;
    	
    	
    	  
    }
    
    public String validateText(String expected_text,String xpathkey){
    	
    	String actual_text=driver.findElement(By.xpath(OR.getProperty(xpathkey))).getText();
    	if(actual_text.equals(expected_text))
    		return "Pass";
    	else 
    		return "Fail";
    	
    	
    }
    
    public String isElementPresent(String xpathkey){
    	
    	int count=driver.findElements(By.xpath(OR.getProperty(xpathkey))).size();
    	
    	   if(count==0)
    		   return "Fail - Element not present  "+ xpathkey ;
    	   else 
    		   return "Pass";
    	
    }
    
    public String closeBrowser(){
    	
    	if(driver !=null){
	     driver.quit();
    	if(bak_chrome !=null)
    		bak_chrome.quit();
    	if(bak_firefox!=null)
    		bak_firefox.quit();
    	
    	bak_chrome=bak_firefox=null;
    	
    	}
	     
    	return "Pass";
	    
     }
    
     public String waitForElementPresence(String objectey){
    	 
    	 try{
    	 wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(OR.getProperty(objectey))));
    	 }catch(Exception e){
    		 return " Fail - Element not visible " + objectey;
    	 }
    	 
		return "Pass";
     }
    
    
    /**************Application dependent methods*******************************************************************/

    public void loginAsDefaultUser(){
    	
    	k.navigate("testSiteURL");
    	k.click("Loginlink");
		k.input("username", ENV.getProperty("adminUsername"));
		k.input("password", ENV.getProperty("adminPassword"));
		k.click("loginbutton");
    }
    
    public String verifyLogin(String flag){
    	
    	String actualTitle=driver.getTitle();
    	String loginPageTitle=OR.getProperty("loginPageTitle");
    	  if(actualTitle.equals(loginPageTitle) && flag.equals("Y"))
    		   return "Fail - not able to login with right data";
    	    else if(actualTitle.equals(loginPageTitle) && flag.equals("N")) 
    		   return "Fail -   able to login with wrong data";
    	    else 
		        return "Pass";
    	  
    	  
    }

     //logging
    
    public void log(String msg){
    	
    	APPLICATION_LOGS.debug(msg);
    }

}
