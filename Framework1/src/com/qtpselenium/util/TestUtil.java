package com.qtpselenium.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Hashtable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
	
	public static boolean getRunmode(String testName, Xls_Reader xls){
		
		  for(int rNum=2;rNum<=xls.getRowCount("Test cases");rNum++){
			  String testCaseName=xls.getCellData("Test Cases", "TCID", rNum);
			     if(testCaseName.equals(testName)){
			    	   if(xls.getCellData("Test Cases", "Runmode", rNum).equals("Y")){
			    		       return true;
			    		       
			    	   }else {
			    		       return false;
			    	   }
			     }
		  }
		return false;
	}
	
	
	// make zip of reports
		public static void zip(String filepath){
		 	try
		 	{
		 		File inFolder=new File(filepath);
		 		File outFolder=new File("Reports.zip");
		 		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(outFolder)));
		 		BufferedInputStream in = null;
		 		byte[] data  = new byte[1000];
		 		String files[] = inFolder.list();
		 		for (int i=0; i<files.length; i++)
		 		{
		 			in = new BufferedInputStream(new FileInputStream
		 			(inFolder.getPath() + "/" + files[i]), 1000);  
		 			out.putNextEntry(new ZipEntry(files[i])); 
		 			int count;
		 			while((count = in.read(data,0,1000)) != -1)
		 			{
		 				out.write(data, 0, count);
		 			}
		 			out.closeEntry();
	  }
	  out.flush();
	  out.close();
		 	
	}
	  catch(Exception e)
	  {
		  e.printStackTrace();
	  } 
	 }
		
}
