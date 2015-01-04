
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class TestUtil {
	
	private static SimpleDateFormat scrShot = new SimpleDateFormat("MMddyy_HHmmss");
	private static String scrShotDir = new SimpleDateFormat("MMddyy").format(new Date());
	private static String strScreenshotName = "";

	public static Object[][] getData(String testCase, Excel_Reader xls) {

		// row on which test is lying
		// rows of data
		// cols of data
		// extract data

		// row on which test is lying
		int testStartRowNum = 1;

		while (!xls.getCellData("Data", 0, testStartRowNum).equals(testCase)) {

			testStartRowNum++;

		}

		// System.out.println("Test Case "+testCase+" starts from row number "+
		// testStartRowNum);

		// rows of data

		int dataStartRowNum = testStartRowNum + 2;
		int rows = 0;

		while (!xls.getCellData("Data", 0, dataStartRowNum + rows).equals("")) {

			rows++;

		}

		// System.out.println("Total rows in test case "+testCase+" are "+
		// rows);

		// cols of data

		int colStartRowNum = testStartRowNum + 1;
		int cols = 0;

		while (!xls.getCellData("Data", cols, colStartRowNum).equals("")) {

			cols++;

		}

		// System.out.println("Total cols in test case "+testCase+" are "+
		// cols);

		Object testData[][] = new Object[rows][1];
		int index = 0;
		Hashtable<String, String> table = null;
		// extract data

		for (int rNum = dataStartRowNum; rNum < dataStartRowNum + rows; rNum++) {

			table = new Hashtable<String, String>();// init for every row

			for (int cNum = 0; cNum < cols; cNum++) {

				String key = xls.getCellData("Data", cNum, colStartRowNum);
				String value = xls.getCellData("Data", cNum, rNum);
				// System.out.print(value+"---");

				// fill the table
				table.put(key, value);
			}
			// System.out.println("");
			testData[index][0] = table;
			index++;
		}

		// System.out.println("*****************");

		return testData;

	}

	public static Object[][] getData1(String testCase, Excel_Reader xls) {

		// row on which test is lying
		// rows of data
		// cols of data
		// extract data

		// row on which test is lying
		int testRowNum = 1;

		while (!xls.getCellData("Data", 0, testRowNum).equals(testCase)) {

			testRowNum++;

		}

		Object testData1[][] = new Object[1][1];

		Hashtable<String, String> table = new Hashtable<String, String>();
		// extract data

		for (int colNum = 1; colNum < xls.getColumnCount("Data"); colNum++) {

			String key = xls.getCellData("Data", colNum, 1);

			String value = xls.getCellData("Data", colNum, testRowNum);

			table.put(key, value);

		}

		testData1[0][0] = table;

		return testData1;

	}

	public static Object[][] getData2(String testCase, Excel_Reader xls) {

		// row on which test is lying
		// rows of data
		// cols of data
		// extract data

		// row on which test is lying
		int testRowNum = 1;

		while (!xls.getCellData("Data1", 0, testRowNum).equals(testCase)) {

			testRowNum++;

		}

		// rows of data

		int dataStartRowNum = testRowNum;
		int rows = 0;

		while (!xls.getCellData("Data1", 1, dataStartRowNum + rows).equals("")) {

			rows++;

		}


		Object testData2[][] = new Object[rows][1];

		int index = 0;

		Hashtable<String, String> table = null;
		
		// extract data
	

		for (int rNum = dataStartRowNum; rNum < dataStartRowNum + rows; rNum++) {

			table = new Hashtable<String, String>();
			
			 String rowNum=Integer.toString(rNum);
			
			 table.put("rNum",rowNum);
		
			for (int colNum=1; colNum < xls.getColumnCount("Data1")-1; colNum++) {

				String key = xls.getCellData("Data1", colNum, 1);

				String value = xls.getCellData("Data1", colNum, rNum);

				table.put(key, value);
				
			}
			testData2[index][0] = table;
			index++;

		}

		return testData2;

	}

	public static Object[][] getData_MM(String testCase, Excel_Reader xls) {

		// row on which test is lying
		// rows of data
		// cols of data
		// extract data

		// row on which test is lying
		int testRowNum = 1;

		while (!xls.getCellData("Web Transfer 5", 1, testRowNum).equals(
				testCase)) {

			testRowNum++;

		}

		Object testData_MM[][] = new Object[1][1];

		Hashtable<String, String> table = new Hashtable<String, String>();
		// extract data

		for (int colNum = 2; colNum < xls.getColumnCount("Web Transfer 5"); colNum++) {

			String key = xls.getCellData("Web Transfer 5", colNum, 5);

			String value = xls
					.getCellData("Web Transfer 5", colNum, testRowNum);

			table.put(key, value);

		}

		testData_MM[0][0] = table;

		return testData_MM;

	}
	
public static void takeScreenShot(String Code,WebDriver webdriver,String page) {
		
		String name = System.getProperty("user.dir")+"\\ScreenShots\\GateChanges" ;
		
		createDir(name,"Application Name");
		
		name = name + "\\GateChanges_Execution_" + scrShotDir;
		
		createDir(name,"TimeStamp");
		
        name = name + "\\Product_" + Code;
		
		createDir(name,"Product Code");
		
		strScreenshotName = name + "\\" + page + "_" +  scrShot.format(new Date()) + ".png";

		File f = ((TakesScreenshot) webdriver).getScreenshotAs(OutputType.FILE);

		try {
			FileUtils.copyFile(f, new File(strScreenshotName));
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
		//strScreenshotName = ". Screen Shot : " + strScreenshotName;
	}

public static void createDir(String dirName,String Message){
	File f = new File(dirName);
	try {
		if (!f.exists()) {
			f.mkdir();
			System.out.println("Directory Created :: " + Message);
		}
	} catch (Throwable e) {
		System.out.println("Unable to create directory with " + Message);
	}
}

}
