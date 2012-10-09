package controller;

import view.BoggleView;
//import model.BoggleGame;

public class Driver {
	
	public static void main(String[] args) {
		
		BoggleView view = BoggleView.getSingleton();
		BoggleController controller = BoggleController.getSingleton();
		
		controller.setView(view);
		view.setController(controller);

		view.run();
		
		/*BoggleGame game = new BoggleGame();
		//ask user to input squares, or make them randomly
		game.shuffleSquares();
		
		game.showSquares();
		game.findWords();
		game.printFoundWords();
		
//		game.formatWordsFile();
//		game.sortWordsFile();
*/	}
}
