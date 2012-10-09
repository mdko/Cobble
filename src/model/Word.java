package model;

import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class Word implements Comparable {

	private String word;
	private ArrayList<Letter> setOfLetters; //not hashset to maintain ordering of letters
	
	public Word (String word, ArrayList<Letter> letterList) {
		this.word = word;
		setOfLetters = new ArrayList<Letter>(letterList);
	}
	
	@Override
	public int compareTo(Object o) {
		return word.compareTo(((Word)o).getValue());
	}
	
	public String getValue() {
		return word;
	}
	
	public void printLetters() {
		for (Letter l: setOfLetters)
			System.out.print(l.toString());
		System.out.println();
	}
	
	public String toString() {
		return word;
	}
	
	public ArrayList<Letter> getLetters() {
		return setOfLetters;
	}
	
	
}
