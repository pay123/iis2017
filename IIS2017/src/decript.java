import java.math.BigInteger;


public class decript {
	
	static BigInteger buildint (String cipher){
		
		char[] c_char = cipher.toCharArray();
		//BigInteger c_int = BigInteger.ZERO; 
		
		String number = "";
		
		
	    for(char c : c_char)
	    {
	        int temp = (int)c;
	        int temp_integer = 65; 
	        if(temp<=90 & temp>=65){
	        //	System.out.print(temp-temp_integer);
	        	int num = temp-temp_integer;
	        	number = number + num;
	   
	        }
		
	    }
	  //  System.out.println("");
	  //  System.out.println(number);
	    BigInteger c_int = new BigInteger(number); 
		
		return c_int;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		BigInteger c  = buildint("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		BigInteger c1 = buildint("HALLOHALLOHALLO");
		
		System.out.println(c);
		System.out.println(c1);
		
	}

}
