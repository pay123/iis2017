import java.math.BigInteger;


public class decript {
	
	static BigInteger buildint (String cipher){
		
		char[] c_char = cipher.toCharArray();
		BigInteger c_int = BigInteger.ZERO; 
		
		int[] array;
		
	    for(char c : c_char)
	    {
	        int temp = (int)c;
	        int temp_integer = 65; //for upper case
	if(temp<=90 & temp>=65)
	    System.out.print(temp-temp_integer);
		
		
	
		c_int.add(BigInteger.valueOf(temp-temp_integer));
		
	    }
		
		
		return c_int;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		BigInteger c  = buildint("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		//buildint("ZZZZZZZZZZZZZZZAAAAAAAAAAAA");
		System.out.println("");
		System.out.println(c);
		
	}

}
