package com.qtpselenium.salesforce.testcases;

import java.util.Hashtable;

import org.openqa.selenium.internal.seleniumemulation.IsElementPresent;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qtpselenium.util.Keywords;
import com.qtpselenium.util.TestUtil;
import com.qtpselenium.util.Xls_Reader;

public class MenuTest {
	
	Keywords k=Keywords.getInstanc();
	 
 Xls_Reader xls=new Xls_Reader(System.getProperty("user.dir")+"//src//com//qtpselenium//salesforce//config//TestCases.xlsx");
		
	
	
	@Test(dataProvider="getData")
	
	public void topMenuTest(Hashtable<String,String> data){
		
		//runmode of test
		
				if(!TestUtil.getRunmode("MenuTest", xls))
					throw new SkipException("Skipping  Test as Runmode as No");
				
				//runmode of dataset
				
						if(data.get("Runmode").equals("N"))
							throw new SkipException("Skipping as flag is set to No");
		
		
		
		 /*k.openBrowser(data.get("Browser"));
		 k.navigate("testSiteURL");
		 
		 //Assert.assertTrue(k.isElementPresent("solTab"),"Solutions tab is not present");
		 
		 k.click("solTab");
		 k.navigate("testSiteURL");
		 
		 //Assert.assertTrue(k.isElementPresent("prodTab"),"products tab is not present");
		 k.click("prodTab");
		 k.navigate("testSiteURL");
		
		 //Assert.assertTrue(k.isElementPresent("servTab"),"Services tab is not present");
		 k.click("servTab");*/
						
		   k.executeKeywords("MenuTest", xls, data);				
		 
	}
	
	
       @AfterTest
	 
	 public void close(){
		 
		 k.closeBrowser();
	 }
       
	@DataProvider
	
	public Object[][] getData(){
		
		return TestUtil.getData("MenuTest", xls);
		
		/*Object data[][]=new Object[2][1];
		
		  data[0][0]="firefox";
		  data[1][0]="chrome";
		  
		return data;*/
		  
		  
	}

}
