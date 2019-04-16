package wordsearch;
import java.util.ArrayList;

public class Possibilites {
	ArrayList<ArrayList<Character>> searchArr;
	public Possibilites(ArrayList<ArrayList<Character>> arr) {
		searchArr = arr;
	}
	
	public ArrayList<String> horizontal(){//Get all elements from each row
		ArrayList<String> finalArr = new ArrayList<String>();
		for(int mainArray = 0; mainArray < searchArr.size(); mainArray++) {//Look through the array that is given to the findWords function. And find the word from the word bank. 
			String s = "";
			for(int i = 0; i < searchArr.get(mainArray).size(); i++) {//Make the string from the word search
				s += searchArr.get(mainArray).get(i);
			}
			finalArr.add(s);
		}
		return(finalArr);

	}
	
	public ArrayList<String> vertical(){//Get all elements in each column
		ArrayList<String> finalArr = new ArrayList<String>();
		for (int mainArray = 0; mainArray < searchArr.get(0).size(); mainArray++) {//Get size of the columns. Or how many in one array. Same as top but for vertical
			String s = "";
			for(int i = 0; i < searchArr.size(); i++) {
				s += searchArr.get(i).get(mainArray);
			}
			finalArr.add(s);
		}
		return(finalArr);
	}
	
	public ArrayList<String> diagonal(ArrayList<ArrayList<Character>> inputtedArray){
		//FIND DIAGONAL | Reverse the arrays and send through same function.  | Explanation in Python on repl.it (https://repl.it/@yuvrajkhosa/GentleHeavenlyDevice)
		ArrayList<String> finalArr = new ArrayList<String>();
		int iterAmount = inputtedArray.get(0).size() + inputtedArray.size() - 1;
		int bufferAmount = iterAmount - inputtedArray.get(0).size();
		int subArrayAmount = 0;
		int firstIndex = 0;
		int secondIndex = 0;
		int secondCount;
		String lettersArray = "";
		for(int i = 0; i < iterAmount; i++) {
			//ArrayList<Character> lettersArr = new ArrayList<Character>();
			lettersArray = "";
			if(i <= bufferAmount) {
				subArrayAmount++;
			}
			else if(i > iterAmount - bufferAmount - 1) {
				subArrayAmount--;
			}
//			if(iterAmount % 2 == 0) {
//				if(i < (int)(iterAmount / 2)) {
//					subArrayAmount++;
//				}
//				else if(i > (int)(iterAmount / 2)) {
//					subArrayAmount--;
//				}
//			}
//			else {
//				if(i <= (int)(iterAmount / 2) - 1) {
//					subArrayAmount++;
//				}
//				else if(i > (int)(iterAmount / 2) + 1) {
//					subArrayAmount--;
//				}
//			}
			
			if(i < inputtedArray.get(0).size()) {
				firstIndex = 0;
			}
			else {
				firstIndex = i - inputtedArray.get(0).size() + 1;
			}
			secondIndex = i - firstIndex;
			lettersArray += inputtedArray.get(firstIndex).get(secondIndex);
			secondCount = firstIndex;
			if(subArrayAmount > 1) {
				for(int j = 0; j < subArrayAmount - 1; j++) {
					secondCount++;
					//System.out.println(inputtedArray.get(secondCount).get(i - secondCount));
					lettersArray += inputtedArray.get(secondCount).get(i - secondCount);
				}
			}
			finalArr.add(lettersArray);
		}
		
		return(finalArr);
	}
}
