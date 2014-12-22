

import java.util.Hashtable;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;



public class MemoryMaker {
	
	 
	 
   Xls_Reader xls=new Xls_Reader(System.getProperty("user.dir")+"//src//data//2014_WDPRO_Roz_Affiliate 5 UPDATE 4-20.xlsx");
	
	@Test(dataProvider="getData")
	
	public void leadTest(Hashtable<String,String> data){
		
         
//		 System.out.print(data.get("DataO")+"--");
//		 System.out.print(data.get("DataT")+"--");
//		 System.out.print(data.get("DataF")+"--");
		
		System.out.print(data.get("Description")+"--");
		System.out.print(data.get("Base1")+"--");
		System.out.print(data.get("Net1")+"--");
		System.out.print(data.get("Base2")+"--");
		System.out.print(data.get("Tax")+"--");
		System.out.print(data.get("Total")+"--");
		System.out.print(data.get("Date")+"--");
		System.out.print(data.get("Rate")+"--");
		System.out.print(data.get("%")+"--");
		System.out.print(data.get("$")+"--");
		 

		 
		 System.out.println();
		 
	}
	
     
     
	@DataProvider()
	
	 public Object[][] getData(){
		
		//return TestUtil.getData("MM2", xls);
		
		//return TestUtil.getData1("MM1", xls);
		
		return TestUtil.getData_MM("AKW0G", xls);
		
		
	}  

}
	
