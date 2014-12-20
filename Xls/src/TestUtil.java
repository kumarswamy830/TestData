

import java.util.Hashtable;

public class TestUtil {
	
	
	public static Object[][] getData(String testCase,Xls_Reader xls){
		
		//row on which test is lying
		//rows of data
		//cols of data
		//extract data
		
		//row on which test is lying
		int testStartRowNum=1;
		
		while(!xls.getCellData("Data", 0, testStartRowNum).equals(testCase)){
			
			testStartRowNum++;
			
		}
		
		//System.out.println("Test Case "+testCase+" starts from row number "+ testStartRowNum);
		
		//rows of data
		
        int dataStartRowNum=testStartRowNum+2;
        int rows=0;
		
		while(!xls.getCellData("Data", 0 , dataStartRowNum+rows).equals("")){
			
			rows++;
			
		}
		
		//System.out.println("Total rows in test case "+testCase+" are "+ rows);
		
		//cols of data
		
		int colStartRowNum=testStartRowNum+1;
        int cols=0;
		
		while(!xls.getCellData("Data", cols , colStartRowNum).equals("")){
			
			cols++;
			
		}
		
		//System.out.println("Total cols in test case "+testCase+" are "+ cols);
		
		 Object testData[][]=new Object[rows][1];
		 int index=0;
		 Hashtable<String,String> table=null;
		//extract data
		
		for(int rNum=dataStartRowNum;rNum<dataStartRowNum+rows;rNum++){
			
			table=new Hashtable<String,String>();//init for every row
			
			for(int cNum=0;cNum<cols;cNum++){
				
				 String   key=xls.getCellData("Data", cNum,colStartRowNum); 
				 String value=xls.getCellData("Data", cNum, rNum);
				 //System.out.print(value+"---");
				 
				 // fill the table
				 table.put(key, value);
			}
			    // System.out.println("");
			     testData[index][0]=table;
			     index++;
		}
		
		
		//System.out.println("*****************");
		
		return testData;
		
		}
	
	

}
