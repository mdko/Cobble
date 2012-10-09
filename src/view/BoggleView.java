package view;

import java.util.ArrayList;

import javax.swing.JFrame;

import model.Word;

import controller.BoggleController;

public class BoggleView {

	private static BoggleController controller;
	
	private static BoggleView view;
	
	private BoggleGui boggleBox;
	
	private BoggleView() {
		
	}

	public static BoggleView getSingleton() {
		if (view == null)
			view = new BoggleView();
		return view;
	}

	public void setController(BoggleController controller) {
		this.controller = controller;
	}

	public void run() {
		
		JFrame frame = new JFrame("Boggle Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		boggleBox = new BoggleGui(view);
		
		frame.getContentPane().add(boggleBox);
		frame.setResizable(false); //TODO change so only a minimum size is enforced (so buttons don't overlap checkboxes)
		frame.pack();
		frame.setVisible(true);
	}
	
	public void solveAll() {
		controller.solveAll();
	}
	
	public ArrayList<Word> getFoundWords() {
		//for testing, seeing if coordinates found correctly
		return controller.getFoundWords();
	}

	public String getNextLetter(int i) {
		return controller.getNextLetter(i);
	}

	public void shuffleLetters() {
		controller.shuffleLetters();
	}

	public void clearFoundWords() {
		controller.clearFoundWords();
	}

	public void changeLetter(int xCoordinate, int yCoordinate, String text) {
		controller.changeLetter(xCoordinate, yCoordinate, text);
	}

	public ArrayList<Word> getWordsOnLetter(int xCoordinate, int yCoordinate) {
		return controller.getWordsOnLetter(xCoordinate, yCoordinate);
	}
}
