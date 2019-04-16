package wordsearch;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import wordsearch.Start.direction;

public class Recognition{
	ITesseract instance = new Tesseract();  // JNA Interface Mapping
	//public InputStream tessDataLoc = Recognition.class.getResourceAsStream("tessdata");
	public static ArrayList<Text> textArr = new ArrayList<Text>();
	public static int amountOfWordsFound;
	public static int spaceBetweenHorizontal;
	public static int spaceBetweenVertical;
	public static ArrayList<ArrayList<Character>> gridArr;
	public String fileURL;//Make Var to store URL of file location of image.
	public String bankURL;
	public ArrayList<Character> arr = new ArrayList<Character>(0);//Create Array list to add items at end
	public ArrayList<String> wordBankArr;
	private int amountOfArrays = 0;//Amount of subarrays.
	private boolean isDuplicateCheck = false; //To make sure duplicates aren't added by mistake. At the end. NEW LINE's Are sometimes added.
	//public ArrayList<ArrayList<Character>> arr = new ArrayList<ArrayList<Character>>();
	Recognition(String fileLocation, String bankLocation) {//Constructor
		this.fileURL = fileLocation;
		this.bankURL = bankLocation;
	}
	
	public ArrayList<ArrayList<Character>> recognizeSearch() {//Method to recognize image
		String result = null;
		gridArr = new ArrayList<ArrayList<Character>>();
		amountOfWordsFound = 0;
		File imageFile = new File(this.fileURL);
        instance.setDatapath(".\\tessdata"); // path to tessdata directory | The Ai
        instance.setTessVariable("tessedit_char_whitelist", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");//Only look for letters. Sometimes mistakes | for I
        
        
        try{
            result = instance.doOCR(imageFile);//Store resulting ocr into this
            
            result = result.replace(" ", "");//Get rid of white space between characters when word search has lots of space
            System.out.println(result);
            result = result.toUpperCase();
            //System.out.println(result);

            for(int i = 0; i < result.length(); i++) {//Iterate through the string of characters and if there is a line break. Don't add that line break char
            										  //Instead add a '-' to signify line break
            	if(result.charAt(i) != '\n') {
            		
            		arr.add(result.charAt(i));
            		isDuplicateCheck = false;
            	}
            	else {
            		if(!isDuplicateCheck) {//Check to make sure that the last '-' are not duplicated for OCR errors.
            			isDuplicateCheck = true;
            			amountOfArrays++;
            			//arr.add('-');
            		}
            	}
            }
           // System.out.println("Amount of arrays: " + amountOfArrays);
            //System.out.println(arr);
         }
       catch(TesseractException e) {
            System.err.println(e.getMessage());
       }
     
    //Create Main grid array.
     //int counter = 0;
     for(int i = 0 ; i < amountOfArrays; i++) {//Add the sub arrays to make columns. | To get the amount of CHARACTERS do arr.size() - amountOfArrays.
    	 gridArr.add(new ArrayList<Character>());
    	 
    	for(int j = 0; j < arr.size() / amountOfArrays; j++) {
    	
    		
    		gridArr.get(i).add(arr.get( (i * (arr.size() / amountOfArrays)) + j) );//see which step on, then add the sub step of the array. to get pos in main array.

    	}
    	 
    	 
     }
     
     
     System.out.println("Array size: " + arr.size());
     System.out.println(arr.size() / amountOfArrays + " x " + amountOfArrays);
     System.out.println(gridArr);
  
    
     for(int i = 0; i < gridArr.size(); i++) {
    	 for(int j = 0; j < gridArr.get(i).size(); j++) {//Can be gridArr.get(0).size() because all rows are same length.
    		 spaceBetweenHorizontal = application.SampleController.screenWidth / gridArr.get(0).size();
    		 spaceBetweenVertical = (application.SampleController.screenHeight - 100) / gridArr.size();
    		 Text t = new Text(20 + (j * spaceBetweenHorizontal), 100 +(i * spaceBetweenVertical), String.valueOf(gridArr.get(i).get(j)));
    		 t.setFont(Font.font("Consolas", 24));
    		 textArr.add(t);
        	 application.Main.root.getChildren().add(t);
    	 }
    	 
     }
      return(gridArr);
       
	}
	
	public String lookForWord(String letters, String wordToFind, int column, boolean isReversed, direction dir){
		
		for(int i = 0; i < letters.length(); i++) {
			if(i + wordToFind.length() > letters.length()) {
				//System.out.println("NOT FOUND! '" + wordToFind + "'");
				return("");
			}
			if(letters.substring(i, i + wordToFind.length()).equals(wordToFind)) {
				System.out.println("FOUND '" + wordToFind +"'! INDEX:  " + i + " TO " + (i + wordToFind.length()) + " at column: " + (column + 1) + " Directoin: " + dir +" Reversed: " + isReversed );//Must plus 1 beacuse counting starts at 0;
				amountOfWordsFound++;
				return(wordToFind);
			}
		}
		return("");
	}
	


	public ArrayList<String> getWordbank() {
		
		File imageFile = new File(this.bankURL);
	    
	    //System.out.println(currentDir + "");
	    instance.setDatapath(".//tessdata"); // path to tessdata directory | The Ai
	    instance.setTessVariable("tessedit_char_whitelist", "ABCDEFGHIJKLMNOPQRSTUVWXYZ");//Only look for letters. Sometimes mistakes | for I
	    
	    try{
	        String result = instance.doOCR(imageFile);//Store resulting ocr into this
	        
	        result = result.toUpperCase();
	       // System.out.println(result.replaceAll("\n", " ").split(" "));//Replace newlines with spaces so we can split it into array
	        String[] tempWordBankArr = result.replaceAll("\n", " ").split(" ");
	        wordBankArr = new ArrayList<String>(Arrays.asList(tempWordBankArr));
	        //wordBankArr = removevDuplicates(wordBankArr);//Remove duplicates. Add blanks where duplicates are.
	        
	        for(int i = 0; i < wordBankArr.size(); i++) {//Remove Blanks
	        	if(wordBankArr.get(i).matches("[^a-zA-z]") || wordBankArr.get(i).equals("")) {
	        		//System.out.println("MATCHES");
	        		wordBankArr.remove(i);
	        		i--;//Remove one iterations VAR because I just removed a variable which decreases the size of the array.
	        	}
	        }
	      
	        System.out.println("\n");
	        
	      }
	   catch (TesseractException e) {
	        System.err.println(e.getMessage());
	   }
	   
	   
	   return(wordBankArr);
	}

	public ArrayList<String> removevDuplicates(ArrayList<String> arr){
		ArrayList<String> alreadyOccured = new ArrayList<String>();
		for(int i = 0; i < arr.size(); i++) {
			for(int j = 0; j < alreadyOccured.size(); j++) {
				if(arr.get(i).equals(alreadyOccured.get(j))) {//If duplicate found
					arr.set(i, "");
				}
			}
			alreadyOccured.add(arr.get(i));
		}
		return(arr);
	}
}