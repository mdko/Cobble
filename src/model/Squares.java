package model;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/**
 * Represents the set of all 16 squares in a normal boggle game
 * @author Michael
 *
 */
public class Squares {

	private ArrayList<Square> squares;
	
	public Squares() {
		squares = new ArrayList<Square>();
		squares.add(new Square("R", "N", "Z", "N", "H", "L"));
		squares.add(new Square("E", "S", "U", "E", "N", "I"));
		squares.add(new Square("K", "P", "F", "A", "F", "S"));
		squares.add(new Square("R", "L", "T", "Y", "E", "T"));
		squares.add(new Square("Y", "S", "D", "I", "T", "T"));
		squares.add(new Square("U", "T", "C", "I", "O", "M"));
		squares.add(new Square("H", "QU", "U", "M", "I", "N"));
		squares.add(new Square("O", "T", "T", "A", "O", "W"));
		squares.add(new Square("G", "A", "E", "A", "N", "E"));
		squares.add(new Square("B", "J", "A", "O", "O", "B"));
		squares.add(new Square("S", "T", "I", "E", "S", "O"));
		squares.add(new Square("O", "H", "C", "P", "S", "A"));
		squares.add(new Square("E", "H", "N", "E", "W", "G"));
		squares.add(new Square("D", "E", "X", "L", "I", "R"));
		squares.add(new Square("V", "R", "H", "T", "W", "E"));
		squares.add(new Square("V", "E", "R", "L", "D", "Y"));
	}
	
	public void shuffleSquares() {
		Random generator = new Random();
		ArrayList<Integer> randNums = new ArrayList<Integer>();
		ArrayList<Square> tempSquares = new ArrayList<Square>();
		
		for (int i = 0; i < squares.size(); i++) {
			int randNum = generator.nextInt(16);
			while (randNums.contains(randNum))
				randNum = generator.nextInt(16);
			
			randNums.add(randNum);
			tempSquares.add(squares.get(randNum));
		}
		
		squares = tempSquares;
	}
	
	public String getLetter(int i, int j) {
		return squares.get((i*4)+j).getRandomLetter();
	}
	
}


class Square {
	private String[] lettersOnCube;
	Random generator;
	final int NUM_OF_SIDES = 6;
	String currentLetter;
	
	public Square(String s0,
				  String s1,
				  String s2,
				  String s3,
				  String s4,
				  String s5) {
		lettersOnCube = new String[NUM_OF_SIDES];
		lettersOnCube[0] = s0;
		lettersOnCube[1] = s1;
		lettersOnCube[2] = s2;
		lettersOnCube[3] = s3;
		lettersOnCube[4] = s4;
		lettersOnCube[5] = s5;
		
		generator = new Random();
		currentLetter = "";
	}
	
	public String getRandomLetter() {
		currentLetter = lettersOnCube[generator.nextInt(6)];
		return currentLetter;
	}
	
	public String toString() {
		return currentLetter;
	}
}