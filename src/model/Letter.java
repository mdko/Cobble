package model;

import java.util.ArrayList;
import java.util.Random;

public class Letter {

	private String originalLetter;
	private Coordinate coordinate;
	private boolean encountered;
	private ArrayList<Word> wordSetOnThisLetter;
	
	//Unused
	private Letter() {
		originalLetter = "";
		encountered = false;
	}
	
	public Letter(String s, int X, int Y) {
		originalLetter = s;
		encountered = false;
		coordinate = new Coordinate(X,Y);
		wordSetOnThisLetter = new ArrayList<Word>();
	}
	
	public void setAsEncountered() {
		encountered = true;
	}
	
	public void setAsNotEncountered() {
		encountered = false;
	}
	
	public boolean hasBeenEncountered() {
		return encountered;
	}
	
	public String toString() {
		return originalLetter;
	}
	
	public String printCoordinate() {
		return coordinate.toString();
	}
	
	public Coordinate getCoordinate() {
		return coordinate;
	}
	
	public void addWord(Word w) {
		wordSetOnThisLetter.add(w);
	}
	
	public ArrayList<Word> getWordsOnThisLetter() {
		return wordSetOnThisLetter;
	}
}
