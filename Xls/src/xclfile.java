
public class xclfile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
             Xls_Reader x=new Xls_Reader("D:\\workspace\\WD-mod15\\src\\config\\Suite.xlsx");
            System.out.println(isTestSuiteRunnable(x,"A Suite"));
             System.out.println(isTestSuiteRunnable(x,"B Suite"));
             System.out.println(isTestSuiteRunnable(x,"C Suite"));
           
	}
	     public static boolean isTestSuiteRunnable(Xls_Reader xls,String testSuiteName){
	    	  boolean isExecutable=false;
	    	 
	    	   for(int i=2;i<=xls.getRowCount("Test Suite");i++){
	    		   
	    		   String suiteName=xls.getCellData("Test Suite","TSID",i);
	    		   String runmode=xls.getCellData("Test Suite","Runmode",i);
	    		   
	    		   if(suiteName.equalsIgnoreCase(testSuiteName))
	    		   {   if(runmode.equals("Y")){
	    			     isExecutable=true;}
	    		   else {isExecutable=false;}
	    		   }
	    			   
	    		   }
	    		   
	    		   
	    	   
			return isExecutable;
	    	 
	     }

}
