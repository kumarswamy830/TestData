

import java.util.Hashtable;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;



public class CreateLead {
	
	 
	 
   Xls_Reader xls=new Xls_Reader(System.getProperty("user.dir")+"//src//data//TestCases.xlsx");
	
	@Test(dataProvider="getData")
	
	public void leadTest(Hashtable<String,String> data){
		
         
		 //System.out.print(data.get("Data6")+"--");
		System.out.print("Sample code");
		 System.out.print(data.get("DataT")+"--");
		 System.out.print(data.get("DataF")+"--");
		 
//		 System.out.print(data.get("Title"));
//		 System.out.print(data.get("FirstName"));
//		 System.out.print(data.get("LastName"));
//		 System.out.print(data.get("Country"));
//		 System.out.print(data.get("Status"));
//		
		 
		 System.out.println();
		 
	}
	
     
     
	@DataProvider()
	
	 public Object[][] getData(){
		
		//return TestUtil.getData("MM2", xls);
		
		return TestUtil.getData1("MM1", xls);
		
		
	}  

}
	
