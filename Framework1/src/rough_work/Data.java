package rough_work;

import com.qtpselenium.util.TestUtil;
import com.qtpselenium.util.Xls_Reader;

public class Data {

	public static void main(String[] args) {
		
		Xls_Reader xls=new Xls_Reader(System.getProperty("user.dir")+"//src//com//qtpselenium//salesforce//config//TestCases.xlsx");
		
		TestUtil.getData("LoginTest", xls);
		TestUtil.getData("CreateLeadTest", xls);
		TestUtil.getData("MenuTest", xls);
		
		System.out.println(TestUtil.getRunmode("LoginTest", xls));
		System.out.println(TestUtil.getRunmode("CreateLeadTest", xls));
		
		System.out.println(TestUtil.getRunmode("MenuTest", xls));
		
		
		
	}

}
