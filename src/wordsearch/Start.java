package wordsearch;
import java.util.Collections;
import java.util.ArrayList;
import application.Main;
import javafx.scene.shape.Line;
//CURRENTLY NOT REMOVING DUPLICATES FROM WORDBANK
public class Start {

	static ArrayList<ArrayList<Character>> arr;
	public static ArrayList<Line> lineArr = new ArrayList<Line>();
	static int totalWords;
	enum direction{
		VERTICAL, HORIZONTAL, DIAGONALRIGHT, DIAGONALLEFT;
	}
	public static String reverseString(String s){
		String reversedString = "";
		for(int i = s.length() - 1; i >= 0; i--) {
			reversedString += s.charAt(i);
		}
		return(reversedString);
	}
	
	public static ArrayList<ArrayList<Character>> reverseMainArray(ArrayList<ArrayList<Character>> mainArr){
		for(int i = 0; i < mainArr.size(); i++) {
			Collections.reverse(mainArr.get(i));//Reverse the subarrays inorder to get Right to left diagonal
		}
		return(mainArr);
	}
	public static void startFunction(String searchURL, String bankURL) {
		arr = new ArrayList<ArrayList<Character>>();
		Recognition r = new Recognition(searchURL, bankURL);//Word Search
		
		
		arr = r.recognizeSearch();//Recognize the wordsearch its self and convert to 2 dimensional array of columns and rows.
		
		ArrayList<String> wordBankArr = r.getWordbank();
		
		Possibilites p = new Possibilites(arr);
		
		ArrayList<ArrayList<String>> arrOfJumbleArrs = new ArrayList<ArrayList<String>>();
		
		//Create Arrays with all the possibilites on the search array
	
		ArrayList<String> verticalArray = p.vertical();
		
		verticalArray.add("VERTICAL");//To tell what the directions of the letters are
		ArrayList<String> horizontalArray = p.horizontal();
		horizontalArray.add("HORIZONTAL");
		ArrayList<String> diagonalArrayRighttoLeft = p.diagonal(arr);
		diagonalArrayRighttoLeft.add("DIAGONALRIGHTLEFT");
		ArrayList<String> diagonalArrayLeftToRight = p.diagonal(reverseMainArray(arr));
		diagonalArrayLeftToRight.add("DIAGONALLEFTRIGHT");
		
		
		//Add them to a 2D array
		arrOfJumbleArrs.add(horizontalArray);
		arrOfJumbleArrs.add(verticalArray);
		arrOfJumbleArrs.add(diagonalArrayRighttoLeft);
		arrOfJumbleArrs.add(diagonalArrayLeftToRight);
		
		//Send the 2d array to a searcher which cycles through them and prints whether they are found and if they are removes from wordbank
		
		goThroughArray(wordBankArr, arrOfJumbleArrs);
		System.out.println("Not Found: " + wordBankArr);
		
	}
	
	public static void main(String[] args){
		
		//startFunction("C:\\Users\\Yuvraj\\eclipse-workspace\\wordsearch\\images\\hPotter.jpg", "C:\\Users\\Yuvraj\\eclipse-workspace\\wordsearch\\images\\hBank.jpg");
		arr = null;
		
	}

	public static void goThroughArray(ArrayList<String> wordBankArr, ArrayList<ArrayList<String>> arrToSearch){
		System.out.println("WordBank: " + wordBankArr);
		String currentWord = "";
		for(int bankWord = 0; bankWord < wordBankArr.size(); bankWord++) {//Loop through the word bank
			
			subArrayLoop://Loop for the sub array. Break out of this one.
			for(int subArray = 0; subArray < arrToSearch.size(); subArray++) {//Loop through the Main array that contains the 4 sub arrays of VERTICAL, DIAGONAL, ETC.
				
				for(int possibility = 0; possibility < arrToSearch.get(subArray).size() - 1; possibility++) {//Loop Through the possiblities array
					int wordResult = findWord(arrToSearch.get(subArray).get(possibility), wordBankArr.get(bankWord));
					if(wordResult == -1) {//If found, returns the i iteration not -1;
						wordResult = findWord(arrToSearch.get(subArray).get(possibility), reverseString(wordBankArr.get(bankWord)));
					}
					
					if(wordResult != -1){//KEEP IN MIND ARRAYS ARE ONE LONGER THAN THEY SHOULD BE BECASUE OF IDENTIFICATION ELEMENT IN THE END | ALWAYS START COUNTING FROM ZERO
						currentWord = wordBankArr.get(bankWord);
						int xPos = 0;
						int yPos = 0;
						int toXPos = 0;//Negative one because we have to remove one anyways.
						int toYPos = 0;
						int xAdd = 30;
						int yAdd = 100;
						switch(arrToSearch.get(subArray).get(arrToSearch.get(subArray).size() - 1)) {//get last element of sub array to see direction
							case("HORIZONTAL"):
								yAdd -= 8;
								xPos = wordResult;
								yPos = possibility;
								toXPos = xPos + currentWord.length() - 1;
								toYPos = yPos;
								System.out.println("Horizontal : " + (xPos) + " " +  (yPos));//ROW, COLUMN
								break;
							case("VERTICAL"):
								xAdd -= 4;
								yAdd -= 5;
								xPos = possibility;
								yPos = wordResult;
								toXPos = xPos;
								toYPos = yPos + currentWord.length() - 1;
								System.out.println("Vertical: " + (xPos) + " " + (yPos));//COLUMN, ROW
								break;
							case("DIAGONALRIGHTLEFT"):
								System.out.println("Diagonal Right to Left: " + (possibility) + " starting: " + (wordResult));//Start from top left. go Right (possibility + 1). Then go LEFT DOWN (wordResult). Initial position is zero.
								yAdd -= 10;
								if(possibility < arr.size() - 1) {
									xPos = possibility - wordResult;
									yPos = wordResult;
									System.out.println("XPos: " + xPos + " YPos: " + yPos + " to " + (xPos - currentWord.length() + 1) + ":" + (yPos + currentWord.length() - 1));
								}
								else {
									yPos = wordResult + (possibility % (arr.get(0).size() - 1));
									xPos = (arr.get(0).size() - 1) - wordResult;
									System.out.println("xPos: " + xPos + " yPos: " + yPos + " to " + (xPos - currentWord.length() + 1) + ":" + (yPos + currentWord.length() - 1));
									//xPos = possibility - (possibility % arr.get(0).size() - 2);//-2 because -1 because we are in zero based index. Subtract one more because last element is identification element. Doesnt count.
									
									//yPos = possibility % arr.size() - 1;
									//System.out.println("RL XPOS: " + x + "YPOS: " + y  + " to " + (x - currentWord.length() - 1) + ":"  + (y + currentWord.length() - 1));
								}
								toXPos = xPos - currentWord.length() + 1;
								toYPos = yPos + currentWord.length() - 1;
								break;
							case("DIAGONALLEFTRIGHT"):
								xAdd -= 2;
								yAdd -= 2;
								System.out.println("Diagonal Left to Right: " + (possibility) + ":" + (wordResult) + " to " + (possibility + currentWord.length()) + ":" + (currentWord.length() - 1));//Start from top right. Go left (possibility + 1). Then go RIGHT DOWN (wordResult). Initial position is zero
								if(possibility < arr.size() - 1) {
									 xPos = arr.size() - 1 - possibility + wordResult;
									System.out.println("XPos: " + xPos + " YPos: " + wordResult + " to " + (xPos + currentWord.length() - 1) + ":" + (wordResult + currentWord.length() - 1));
								}
								else {
									yPos = (possibility % arr.size() + 1) + wordResult;
									xPos = wordResult;
									//System.out.println("YPOS: " + yPos + " XPOS: " + wordResult + " to " + (yPos + currentWord.length() - 1) + ":" + (wordResult + currentWord.length() - 1));
									System.out.println("Xpos: " + xPos + "Ypos" + yPos + " to " + (xPos + currentWord.length() - 1) + ":" + (yPos + currentWord.length() - 1));
								}
								toXPos = xPos + currentWord.length() - 1;
								toYPos = yPos + currentWord.length() - 1;
								break;                                																											  //Second to (x) is how many to the right for end.
						}
						System.out.println("From: " + xPos + " To: " + toXPos);
						System.out.println("From: " + yPos + " To: " + toYPos);
						Line l = new Line(xAdd + xPos * Recognition.spaceBetweenHorizontal,yAdd + yPos * Recognition.spaceBetweenVertical, xAdd +toXPos * Recognition.spaceBetweenHorizontal, yAdd + toYPos * Recognition.spaceBetweenVertical);
						l.setStrokeWidth(24);
						l.setStyle("-fx-stroke: yellow;");
						l.setOpacity(0.5);
						lineArr.add(l);
						Main.root.getChildren().add(l);
						
						System.out.println("\n");
						wordBankArr.remove(bankWord);
						bankWord--;
						break subArrayLoop;
					}
					
					
					
				}
			}
			
		}
		System.out.println(Recognition.amountOfWordsFound + " words found!");
	
	}
	public static int findWord(String letters, String wordToFind) {//Simple brute force to see all combinations of a word in a string of random words. 
		int startingLocation = -1;
		for(int i = 0; i < letters.length(); i++) {
			if(i + wordToFind.length() > letters.length()) {
				break;
			}
			if(letters.substring(i, i + wordToFind.length()).equals(wordToFind)){
				Recognition.amountOfWordsFound++;
				System.out.println("Found '" + wordToFind + "'");
				startingLocation = i;
				break;
			}
		}
		return(startingLocation);
	}
}
