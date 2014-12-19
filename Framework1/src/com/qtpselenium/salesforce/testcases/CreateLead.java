package com.qtpselenium.salesforce.testcases;

import java.util.Hashtable;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qtpselenium.util.Keywords;
import com.qtpselenium.util.TestUtil;
import com.qtpselenium.util.Xls_Reader;

public class CreateLead {
	
	 Keywords k=Keywords.getInstanc();
	 
   Xls_Reader xls=new Xls_Reader(System.getProperty("user.dir")+"//src//com//qtpselenium//salesforce//config//TestCases.xlsx");
	
	@Test(dataProvider="getData")
	
	public void leadTest(Hashtable<String,String> data){
		
          //runmode of test
		
		if(!TestUtil.getRunmode("CreateLead", xls))
			throw new SkipException("Skipping  Test as Runmode as No");
		
		//runmode of dataset
		
				if(data.get("Runmode").equals("N"))
					throw new SkipException("Skipping as flag is set to No");

		
		  
		   
		   k.openBrowser(data.get("Browser"));
		   
		   //check if you are logged in
		 //   if(!k.isElementPresent("searchField"))
		  //  {
		    	//login
		    	k.loginAsDefaultUser();
		  //  }
		    
		  k.click("leadsTab");
		    k.click("newLeadsbutton");
		  // Assert.assertTrue(k.validateText("kumar swamy", "actual_text"), "Name text is not matching") ;
		  k.input("firstNameTitle",data.get("Title"));
		  k.input("firstName", data.get("FirtsName"));
		   k.input("lastName", data.get("LastName"));
		  k.input("leadCompany", data.get("Country"));
		  k.input("leadStatus", data.get("Status"));
		  k.click("saveButton");
	}
	
     @AfterTest
	 
	 public void close(){
		 
		 k.closeBrowser();
	 }
     
	@DataProvider(/*parallel=true*/)
	
	 public Object[][] getData(){
		
		return TestUtil.getData("CreateLead", xls);
		
		/*Object data[][]=new Object[2][1];
		
	Hashtable<String,String> t1=new Hashtable<String,String>();
	
	     t1.put("title","Mr.");
	     t1.put("firstName","cts");
	     t1.put("lastName","cbe");
	     t1.put("company","UK");
	     t1.put("status","Qualified");
	     t1.put("browser","firefox");
		
		data[0][0]="Mr.";
		data[0][1]="cts";
		data[0][2]="cbe";
		data[0][3]="UK";
		data[0][4]="Qualified";
		data[0][5]="firefox";
		
	Hashtable<String,String> t2=new Hashtable<String,String>();
	
	     t2.put("title","Mr.");
	     t2.put("firstName","ml");
	     t2.put("lastName","hyd");
	     t2.put("company","USA");
	     t2.put("status","Qualified");
	     t2.put("browser","chrome");	
	     
		data[1][0]="Mr.";
		data[1][1]="Motivity";
		data[1][2]="Labs";
		data[1][3]="NV";
		data[1][4]="Qualified";
		data[1][5]="chrome";
	     
	     data[0][0]=t1;
	     data[1][0]=t2;
		
		
		return data;*/
	}  

}
	
