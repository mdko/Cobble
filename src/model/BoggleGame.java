package model;

import java.io.*;
import java.util.*;

//TODO remove duplicate words that be reached in different ways
public class BoggleGame {

	final int SIZE = 4;
	Letter[][] lettersArray;
	Letter[][] copyLettersArray;
	Squares squares;
	StringBuilder initialSquares;
	File wordsFile;
	File outputFile;
	ArrayList<Word> foundWordsList;
	Object[] fileWordsArray;
	int combinationsTried;
	
	public BoggleGame() {
		lettersArray = new Letter[SIZE][SIZE];
		initialSquares = new StringBuilder();
		wordsFile = new File("words.txt");
		outputFile = new File("output.txt");
		foundWordsList = new ArrayList<Word>();
		combinationsTried = 0;
		squares = new Squares();
		
		shuffleSquares();
	}
	
	public void shuffleSquares() {
		
		squares.shuffleSquares();
		
		for (int Y = 0; Y < SIZE; Y++) {
			for (int X = 0; X < SIZE; X++) {
				lettersArray[Y][X] = new Letter(squares.getLetter(Y,X), X, Y);
				//initialSquares.append(lettersArray[Y][X] + " ");
				//System.out.println(lettersArray[Y][X].toString() + " " + lettersArray[Y][X].printCoordinate());
			}
			//initialSquares.append("\n");
		}
		
	}
	
	public void showSquares() {
		initialSquares = new StringBuilder();
		//pw.write(initialSquares.toString());
		for (int Y = 0; Y < SIZE; Y++) {
			for (int X = 0; X <SIZE; X++) {
				initialSquares.append(lettersArray[Y][X] + " ");
			}
			initialSquares.append("\n");
		}
		System.out.println(initialSquares.toString());
	}
	
	public void setLetter(int i, int j, String letter) {
		lettersArray[j][i] = new Letter(letter, j, i);
	}
	
	public String getNextLetter(int i) {
		int row = i / SIZE;
		int column = i % SIZE; 
		
		return lettersArray[row][column].toString();
	}

	public void findWords() {
		
		fileWordsArray = readWordsFileToArrayList().toArray();
		
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				find(i,j);
			}
		}
	}
	
	/**
	 * TODO make it so we stop searching fileWordsArray if there are no words that start with
	 * what the string we currently have (not just until length of word is greater than 16)
	 * @param i starting x-coordinate //TODO fix this in relation to coordinate objects
	 * @param j starting y-coordinate
	 * @return
	 */
	StringBuilder sb = new StringBuilder();
	ArrayList<Letter> formingWord = new ArrayList<Letter>();  //for use in maintaining list of letter objects used to create word
	private boolean find(int i, int j) {

		//if out of bounds or letter already encountered, return
		if (i < 0 || i > 3 || j < 0 || j > 3 || lettersArray[i][j].hasBeenEncountered())
			return false;

		//add letter to forming word, set it as encountered
		sb.append(lettersArray[i][j]);
		formingWord.add(lettersArray[i][j]);
		
		lettersArray[i][j].setAsEncountered();
		combinationsTried++;
		
		
		if (!foundWordsList.contains(sb.toString())) {
			if (Arrays.binarySearch(fileWordsArray, sb.toString().toLowerCase()) > -1) {
				Word foundWord = new Word(sb.toString(), formingWord);
				foundWordsList.add(foundWord);
				foundWord.getLetters().get(0).addWord(foundWord); //get first letter of this new word, add word to its list of words reachable from ti
			}
		}
		
		find(i, j-1);	//left
		find(i+1, j-1);	//down-left
		find(i+1, j);	//down
		find(i+1, j+1);	//down-right
		find(i, j+1);	//right
		find(i-1, j+1);	//up-right
		find(i-1, j);	//up
		find(i-1, j-1);	//up-left
				
		lettersArray[i][j].setAsNotEncountered();
		if (lettersArray[i][j].toString().length() > 1) {
			sb.deleteCharAt(sb.length()-1);	//delete an additional letter (QU)
			/*formingWord.remove(formingWord.size()-1);*/ //the letter Qu will be a single letter, don't try to remove twice
		}
		sb.deleteCharAt(sb.length()-1);
		formingWord.remove(formingWord.size()-1);
		return true;	
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Word> getFoundWords() {
		/*System.out.println(combinationsTried);*/
		Collections.sort(foundWordsList);
		return foundWordsList;
		/*pw.print(foundWordsList.toString());
		pw.close();*/
	}
	
	/**
	 * Changes a list of words separated by spaces to a list of words separated by new lines
	 * and writes the new list to a file
	 * Also adds words to an arrayList (but potentially unsorted)
	 * (addition specific to boggle) removes 2-letter words
	 */
	public void formatWordsFile() {
		try {
			StringBuilder sb = new StringBuilder();
			String word = "";
			int c;
			BufferedReader bf = new BufferedReader(new FileReader(wordsFile));
			while ((c = bf.read()) != -1) {
				if (Character.isWhitespace((char)c)) {
					if (word.length() > 2) {	// REMOVE IF NOT BOGGLE LIST
						sb.append((String)word + "\n");
					}
					word = "";
				}
				else
					word += Character.toString((char)c);
			}
			
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(wordsFile)));
			
			pw.write(sb.toString());
			pw.close();
		}
		catch (Exception ex) {
		}
	}

	/**
	 * Sorts the list of words into alphabetical order and writes the new list to a file
	 * Also returns an arrayList with sortedWords
	 */
	public void sortWordsFile() {
		Object[] wl = readWordsFileToArrayList().toArray();
		Arrays.sort(wl);
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(wordsFile)));
			
			for (Object s: wl) {
				pw.write((String)s);
				pw.write("\n");
			}
			
			pw.close();
		}
		catch (Exception ex) {
		}
	}
	
	private ArrayList<String> readWordsFileToArrayList() {
		try {
			ArrayList<String> wordsList = new ArrayList<String>();
			BufferedReader bf = new BufferedReader(new FileReader(wordsFile));
			int c;
			String word = "";
			
			while ((c = bf.read()) != -1) {
				if (Character.isWhitespace((char)c)) {
					if (word.length() > 2)
						wordsList.add(word.toLowerCase());
					word = "";
				}
				else
					word += Character.toString((char)c);
			}
			
			return wordsList;
		}
		catch (Exception ex){
			return null;
		}
	}

	public void clearFoundWords() {
		foundWordsList.clear();
	}

	public ArrayList<Word> getWordsOnLetter(int i, int j) {
		return lettersArray[j][i].getWordsOnThisLetter();
	}
}
