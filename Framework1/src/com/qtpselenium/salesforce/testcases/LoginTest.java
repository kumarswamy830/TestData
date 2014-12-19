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

public class LoginTest {
	
	Keywords k=Keywords.getInstanc();
	 
Xls_Reader xls=new Xls_Reader(System.getProperty("user.dir")+"//src//com//qtpselenium//salesforce//config//TestCases.xlsx");
		

	
	@Test(dataProvider = "getData")
	public void doLogin(Hashtable<String,String> data) {
		
		k.log("********Starting of logintest************");
		
		//runmode of test
		
				if(!TestUtil.getRunmode("LoginTest", xls)){
					k.log("Skipping logintest");
					throw new SkipException("Skipping  Test as Runmode as No");
				}
	
				 //runmode of dataset
				
				if(data.get("Runmode").equals("N")){
					k.log("Skipping logintest data iteration");
					throw new SkipException("Skipping as flag is set to No");
				}
		       k.executeKeywords("LoginTest", xls, data);
		       
		       k.log("******Ending logintest*********");
		
	}
	
	 @AfterTest
	 
	 public void close(){
		 
		 k.closeBrowser();
	 }

	@DataProvider
	public Object[][] getData() {
		
		return TestUtil.getData("LoginTest",xls);

		/*Object data[][] = new Object[3][4];

		data[0][0] = "u1";
		data[0][1] = "p1";
		data[0][2] = "chrome";
		data[0][3] = "Y";

		data[1][0] = "u2";
		data[1][1] = "p2";
		data[1][2] = "firefox";
		data[1][3] = "Y";

		data[2][0] = "u3";
		data[2][1] = "p3";
		data[2][2] = "chrome";
		data[2][3] = "Y";

		return data;*/
	}

}
