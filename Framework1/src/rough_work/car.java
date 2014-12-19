package rough_work;

public class car {

	String name;
	int number;
	
	static car c;
	
	  private car(){
		  
		  System.out.println("Object Created");
	  }
	  
	  public static car  getInstance(){
		  
		  if(c==null)
			  c=new car();
		return c;
		  
	  }
	}


