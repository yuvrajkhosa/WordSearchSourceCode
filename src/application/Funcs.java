package application;

public class Funcs {
	public static String fixURL(String s) {//Checks to see if any BAD characters are in it and converts them to a hex character. Add % before.
		String finalString = "";
		char[] cArray = s.toCharArray();
		for(char c : cArray) {
			if( ((int) c < 65 || (int) c > 122) && (int) c != 46 && (int) c != 58) {
				finalString += "%" + Integer.toHexString((int) c);
			}
			else {
				finalString += c;
			}
		}
		return(finalString);
	}
	
}


  
