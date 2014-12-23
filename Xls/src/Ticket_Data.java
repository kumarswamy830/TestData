

import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;



public class Ticket_Data {
	
	 
	 
   Excel_Reader xls=new Excel_Reader(System.getProperty("user.dir")+"\\src\\data\\TestData.xlsx");
	
    WebDriver driver=new FirefoxDriver();
    
    
	@Test(dataProvider="Test1")
	public void testTickets(Hashtable<String,String> data){
		
		
		
		
        driver.get("https://wdw-latest.disney.go.com/");
      	
      	driver.manage().window().maximize();
      	
      	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
      	
      	driver.findElement(By.xpath("//*[contains(@class,'gnbCategory gnbParksAndTickets')]/a")).click();
         
      	
      	
	     //Select No.of Adults
      	 if(Integer.parseInt(data.get("Adult_Quantity"))>=0){
      		 
      		int Adult_quantity=Integer.parseInt(data.get("Adult_Quantity"));
      		 
      		driver.findElement(By.xpath("//*[starts-with(@aria-owns,'adultTotal-dropdown-list')]")).click();
      		 
      		driver.findElement(By.xpath("//*[@id='adultTotal-" + String.valueOf(Adult_quantity) + "']")).click();
      		
      		System.out.println("Selected "+data.get("Adult_Quantity")+ " Adult");
		 
	     }
      	//Select No.of Childs
      	 if(Integer.parseInt(data.get("Child_Quantity"))>0){
      		 
      		 
      		 int child_quantity=Integer.parseInt(data.get("Child_Quantity"));
      		 
       		driver.findElement(By.xpath("//*[@id='childTotal-wrapper']/div[1]")).click();
       		 
       		driver.findElement(By.xpath("//*[@id='childTotal-"
						+ String.valueOf(child_quantity) + "']")).click();
       		
       		System.out.println("Selected "+data.get("Child_Quantity")+ " Child");
      		 
      		 
      	 }
      	//Select 1 Day Park (Non-Magic Kingdom park/Non- Magic Kingdom park)
      	  if(Integer.parseInt(data.get("SingleDay"))>0){
      		  
      		int days=Integer.parseInt(data.get("SingleDay"));
      		  
      		  driver.findElement(By.xpath("//*[@id='numberOfDaysListTable']/tbody[1]/tr["+ String.valueOf(days+1)+"]/td/span/div")).click();
      		  
      		switch (days)
            {
             case 1 : System.out.println("Selected "+data.get("SingleDay")+ " Day Magic Kingdom park");
                      break;
             case 2 : System.out.println("Selected "+data.get("SingleDay")+ " Day Non-Magic Kingdom park");
                      break;
             default : System.out.println("Not Selected 1 day Ticket ");;
                      break;
            }
      		  
      	  }
      	//Select 2-10 Days Park 
      	if(Integer.parseInt(data.get("MultiDay"))>0){
      		
      		if(Integer.parseInt(data.get("MultiDay"))>5){
      			
      			driver.findElement(By.xpath("//*[@id='numberOfDaysListHelper']/a")).click();
      		}
      		
      		int days=Integer.parseInt(data.get("MultiDay"));
      		
      		driver.findElement(By.xpath("//*[@id='numberOfDaysListTable']/tbody[2]/tr["+ String.valueOf(days)+"]/td/span/div")).click();
      		
      		System.out.println("Selected "+data.get("MultiDay")+ " Day resort");
      	}
    	//Select Ticket Options
      	 int option=Integer.parseInt(data.get("TicketOptions"));
      	 
      	 driver.findElement(By.xpath("//*[@id='addonid-base']/div/span[" + String.valueOf(option) + "]")).click();
      	 
      	switch (option)
        {
         case 1 : System.out.println("Selected Base Ticket only");
                  break;
         case 2 : System.out.println("Selected Park Hopper Option");
                  break;
         case 3 : System.out.println("Selected Water Park Fun & More Option");
                  break;
         case 4 : System.out.println("Selected Hopper Option and Water Park Fun & More Options");
                  break;
         
         default : System.out.println("Selected Base Ticket only");;
                  break;
        }
	
      	 //Verify price per ticket
      	 String priceperticket=driver.findElement(By.xpath("//*[@class='ticketTotalCost']")).getText().trim();
      	 
      	 System.out.println("Priceperticket: "+priceperticket);
      	 
      	 if(("($"+data.get("Base")+"0"+"/ticket)").equals(priceperticket)){
      		 
      		 System.out.println("Price per ticket is correct");
      	 }else{
      		 
      		System.out.println("Price per ticket is not correct");
      	 }
      	 
      	//Verify Tax amount
      	 String tax_amount=driver.findElement(By.xpath("//*[@id='ticketBuilderTaxContainer']/div[2]")).getText();
      	 
      	 System.out.println("Tax_amount: "+tax_amount);
      	 
      	 if(("$"+data.get("Tax")).equals(tax_amount)){
      		 
      		 System.out.println("Tax amount is correct");
      	 }else{
      		 
      		System.out.println("Tax amount is not correct");
      	 }
      	 
      	//Verify Total amount
      	 String Total_Amount=driver.findElement(By.xpath("//*[@id='ticketBuilderSubTotalContainer']/div[2]")).getText().trim();
      	 
      	 System.out.println("Total_Amount: "+Total_Amount);
      	 
      	 if(("$"+data.get("Total")+" USD").equals(Total_Amount)){
      		 
      		 System.out.println("Total amount is correct");
      	 }else{
      		 
      		System.out.println("Total amount is not correct");
      	 }
      	 
	}
     
	@DataProvider(name="Test1")
	public Object[][] getData(){
		
		//return TestUtil.getData("MM2", xls);
		
		//return TestUtil.getData1("AJW3G", xls);
		
		//return TestUtil.getData_MM("AKW0G", xls);
		
		return TestUtil.getData2("Store1", xls);
		
		
	}
	
	@AfterTest
	public void closeBrowser(){
		
		driver.close();
	}
	
	

}
	
