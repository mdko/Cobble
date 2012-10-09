package controller;

import java.util.ArrayList;

import model.BoggleGame;
import model.Letter;
import model.Word;
import view.BoggleView;

public class BoggleController {

	private static BoggleController controller;
	private static BoggleView view;
	private BoggleGame game;
	
	private BoggleController() {
		game = new BoggleGame();
	}

	public static BoggleController getSingleton() {
		if (controller == null)
			controller = new BoggleController();
		return controller;
	}
	
	public void setView(BoggleView view) {
		this.view = view;		
	}

	public void solveAll() {
		game.findWords();
	}
	
	public ArrayList<Word> getFoundWords() {
		return game.getFoundWords();
	}

	public String getNextLetter(int i) {
		return game.getNextLetter(i);
	}

	public void shuffleLetters() {
		game.shuffleSquares();
	}

	public void clearFoundWords() {
		game.clearFoundWords();
	}

	public void changeLetter(int xCoordinate, int yCoordinate, String text) {
		game.setLetter(xCoordinate, yCoordinate, text);
		//game.showSquares(); //TODO debugging, making sure letter is actually changed
	}

	public ArrayList<Word> getWordsOnLetter(int xCoordinate, int yCoordinate) {
		return game.getWordsOnLetter(xCoordinate, yCoordinate);
	}
}
