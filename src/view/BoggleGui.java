package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Letter;
import model.Word;

//TODO make it so you can select a BoggleSquare and selecting it will
//show you all the words that start from that letter (draw them/animate with line the words
//with different colors?)
//TODO be able to select amount of time you want timer to go for
//TODO remove duplicate words (duplicates because different ways to find same word...)
//TODO fix hiding lower panel
//TODO add Return functionality to when entering words in Your Finds section
//TODO make sure lower/upper case compares are consistent
//TODO add compareResults result to GUI, number missed (seems to be working algorithmically (10.8)
//TODO add progress bar
//TODO fix QU size
//TODO fix so that when selecting first word in found words list, it displays (right now no highlights until second word)
//TODO add arrows, highlight first letter in different color
//TODO make rightclick context menus hover-overable like lower panel
//TODO unhighlight letters when wordsFromletterList closed
//TODO remove any duplicates users might enter in wordsEnteredList
//TODO fix highlighting
//TODO when you click on something already highlighted, unhighlights
//TODO clear wordsEnteredList when you shuffle
//TODO make sure that non-words entered aren't counted
//TODO only tell user words they didn't find in results after timer is up
//TODO make sure that answers only appear when checkbox is checked
//TODO when done seeing words reachable, unhighlights everything
//TODO fix size of wordsReachable panel
//TODO fix sizing of lower panel (gets smaller after pressing starttimer button first time)
@SuppressWarnings("serial")
public class BoggleGui extends JPanel {

	private BoggleView view;

	private final Dimension CONTEXT_SQUARES_DIM;
	private final int NUM_SQUARES = 16;
	private final int CONTEXT_MENU_SQUARES = 28;
	private final int NUM_PER_ROW_COL = 4;

	private JPanel squaresPanel;
	private JPanel controlsPanel;
	private JPanel resultsPanel;

	private JButton findWordsOnOneLetter;
	private JButton findWordsAll;
	private JButton shuffleLetters;

	private JFrame lettersContextFrame;
	private JPanel lettersContextPanel;
	private ArrayList<JButton> lettersSelectButtons;

	private ContextMenuListener contextListener;
	private BoggleButtonListener buttonListener;

	private JPanel[] squaresArray;

	private BoggleSquare selectedBoggleSquare;
	private PanelListener panelListener;

	private ArrayList<BoggleSquare> displayingWordBoggleSquares;

	private JScrollPane scrollPane1;
	// storing word objects so when we select them in list, we can access
	// object's attributes/methods
	private JList<Word> wordFoundList;
	private DefaultListModel<Word> model;

	private ListListener listListener;

	private JCheckBox checkBoxAnimateSlowly;
	private JCheckBox checkBoxShowAnswers;
	private ComboBoxListener comboBoxListener;

	private JPanel timerPanel;
	private JLabel timerDisplay;
	private JButton startTimerButton;
	private Timer timer;
	private final int DELAY = 1000; // 1 second
	private final int START_TIME = 180; // 3 minutes
	private final String START_TIME_STRING = "03:00";
	private int currentTime;

	private BoggleWindowFocusListener windowFocusListener;
	private JFrame timesUp;
	
	private boolean lettersChanged;
	
	private JPanel lowerPanelLip;
	private JPanel lowerPanel;
	private JPanel upperPanel;
	
	private ArrayList<JTextField> wordsToEnter;
	private int numberMissed;
	
	private JFrame wordsReachableFrame;
	private JPanel wordsReachablePanel;
	private DefaultListModel wordsFromLetterModel;
	private JList<Word> wordsFromLetterList;
	private WordsFromLetterListListener wordsFromLetterListListener;
	private JScrollPane scrollPane2;
	
	private JFrame rightClickFrame;
	private JPanel rightClickPanel;
	private JButton lettersSelectButton;
	private JButton wordsReachableButton;
	

	TitledBorder tb = new TitledBorder("Hover Over To Reveal Menu");

	public BoggleGui(BoggleView view) {

		super();

		lettersChanged = true;
		this.view = view;

		displayingWordBoggleSquares = new ArrayList<BoggleSquare>();

		selectedBoggleSquare = null;
		CONTEXT_SQUARES_DIM = new Dimension(50, 50);

		buttonListener = new BoggleButtonListener();

		comboBoxListener = new ComboBoxListener();

		panelListener = new PanelListener();

		// Set up main panel to hold primary panels
		// BoxLayout so resizing means that bottom section
		// stays connected to bottom of top section
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Set up three primary panels
		squaresPanel = new JPanel();
		squaresPanel.setLayout(new GridLayout(4, 4));

		controlsPanel = new JPanel();
		controlsPanel.setLayout(new BorderLayout());

		resultsPanel = new JPanel();
		resultsPanel.setLayout(new GridLayout(1, 2));
		// resultsPanel.setBorder(new TitledBorder("Results"));

		// Set up for timer Panel
		timerPanel = new JPanel();
		timerPanel.setLayout(new BoxLayout(timerPanel, BoxLayout.X_AXIS));
		timerPanel.setBorder(new TitledBorder("Timer"));
		timerDisplay = new JLabel(START_TIME_STRING);
		startTimerButton = new JButton("Start Timer");
		startTimerButton.addActionListener(buttonListener);
		timerPanel.add(startTimerButton);
		timerPanel.add(Box.createRigidArea(new Dimension(40, 0)));
		timerPanel.add(timerDisplay);
		currentTime = START_TIME;
		timer = new Timer(DELAY, new TimeDisplayListener());

		// For when time's up
		timesUp = new JFrame();
		JPanel timesUpPanel = new JPanel();
		timesUpPanel.setLayout(new BorderLayout());
		JLabel timesUpLabel = new JLabel("Time's Up!");
		timesUpLabel.setFont(new Font(Font.SERIF, Font.PLAIN, 300));
		timesUpPanel.add(timesUpLabel, BorderLayout.CENTER);
		timesUpPanel.setBackground(Color.RED);
		timesUp.getContentPane().add(timesUpPanel);
		timesUp.setResizable(false);
		timesUp.pack();
		timesUp.setVisible(false);
		windowFocusListener = new BoggleWindowFocusListener();
		timesUp.addWindowFocusListener(windowFocusListener);

		// Set up for squaresPanel;
		squaresArray = new JPanel[NUM_SQUARES];

		for (int i = 0; i < NUM_SQUARES; i++) {
			String currLetter = view.getNextLetter(i);
			BoggleSquare square = new BoggleSquare(currLetter, (i % NUM_PER_ROW_COL), (i / NUM_PER_ROW_COL));
			square.addMouseListener(panelListener);
			squaresArray[i] = square;
			squaresPanel.add(square);
		}

		// Set up contents of second primary panel
		// Subpanel for Western side of second primary panel
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		findWordsOnOneLetter = new JButton(
				"Find All Words Starting with Highlighted Letter");
		findWordsAll = new JButton("Find All Words");
		/*
		 * findWordsAll.setSize(200, 40); //TODO fix so same size
		 * findWordsOnOneLetter.setSize(100,40);
		 */
		shuffleLetters = new JButton("Shuffle Letters");

		findWordsOnOneLetter.addActionListener(buttonListener);
		findWordsAll.addActionListener(buttonListener);
		shuffleLetters.addActionListener(buttonListener);

		// TODO implement and add
		// buttonsPanel.add(findWordsOnOneLetter);
		buttonsPanel.add(findWordsAll);
		buttonsPanel.add(shuffleLetters);

		controlsPanel.add(buttonsPanel, BorderLayout.WEST);

		// Subpanel for Eastern side of second primary panel
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
		checkBoxAnimateSlowly = new JCheckBox("Animate Slowly");
		checkBoxShowAnswers = new JCheckBox("Show Answers");
		checkBoxAnimateSlowly.addChangeListener(comboBoxListener);
		checkBoxShowAnswers.addChangeListener(comboBoxListener);

		checkBoxPanel.add(checkBoxAnimateSlowly);
		checkBoxPanel.add(checkBoxShowAnswers);

		controlsPanel.add(checkBoxPanel, BorderLayout.CENTER);
		controlsPanel.add(timerPanel, BorderLayout.EAST);

		// Set up contents of third primary panel
		// Panel to enter guessed words in
		JPanel wordsSubPanel = new JPanel();
		wordsSubPanel.setLayout(new BoxLayout(wordsSubPanel, BoxLayout.Y_AXIS));
		wordsToEnter = new ArrayList<JTextField>(50);
		for (int i = 0; i < 50; i++) {
			JTextField field = new JTextField();
			wordsSubPanel.add(field);
			wordsToEnter.add(field);
			//TODO add change listener, so when you type in a word/press enter, it shows you where it was on the map?
		}
		JScrollPane scrollPane0 = new JScrollPane(wordsSubPanel);
		scrollPane0.setBorder(new TitledBorder("Words You Find"));
		scrollPane0.setPreferredSize(new Dimension(100, 30));
		scrollPane0
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		resultsPanel.add(scrollPane0);

		// Panel to display found words from algorithm
		listListener = new ListListener();
		model = new DefaultListModel<Word>();
		wordFoundList = new JList<Word>(model);
		wordFoundList.addMouseListener(listListener);
		scrollPane1 = new JScrollPane(wordFoundList);
		scrollPane1.setBorder(new TitledBorder("Answers"));
		scrollPane1
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		resultsPanel.add(scrollPane1);

		upperPanel = new JPanel();
		upperPanel.add(squaresPanel);
		upperPanel.addMouseListener(panelListener);
		add(upperPanel);
		
		lowerPanel = new JPanel();
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));
		lowerPanel.add(controlsPanel);
		lowerPanel.add(resultsPanel);
		//lowerPanel.setVisible(false); //no need since when program starts, mouse will be over top panel and initially hidden anyway
										//this also helps to get the shape right. TODO fix shape, glue lowerPanelLip to bottom of topPanel
		
		lowerPanelLip = new JPanel();
		tb = new TitledBorder("Hover Over To Reveal Menu");
		tb.setTitleJustification(TitledBorder.CENTER);
		lowerPanelLip.setBorder(tb);
		lowerPanelLip.addMouseListener(panelListener);
		lowerPanelLip.add(lowerPanel);
		
		add(lowerPanelLip);

		// END TOP PANEL CONFIGURATION

		// Right-click context menu frame
		// A frame so it pops up over the original frame
		lettersContextFrame = new JFrame();
		lettersContextPanel = new JPanel();
		lettersContextPanel.setLayout(new GridLayout(4, 7));
		contextListener = new ContextMenuListener();
		lettersSelectButtons = new ArrayList<JButton>(CONTEXT_MENU_SQUARES);
		for (int i = 0; i < CONTEXT_MENU_SQUARES; i++) {
			// Add Letters
			if (i < 27) { // 0 thru 26, inclusive (27 because of Qu)
				if (i > 17) {
					if (i == 18)
						lettersSelectButtons.add(i, new JButton("Qu")); //fix to QU can be seen entirely in button
					else
						lettersSelectButtons
								.add(i,
										new JButton(Character
												.toString((char) (i + 64)))); // minus
																				// 1
																				// because
																				// i
																				// is
																				// one
																				// higher
																				// now
				} else
					lettersSelectButtons.add(i,
							new JButton(Character.toString((char) (i + 65))));
			}
			// Add Exit button
			else {
				lettersSelectButtons.add(i, new JButton("Exit"));
				lettersSelectButtons.get(i).setFont(
						new Font(Font.SANS_SERIF, Font.BOLD, 8));
			}
			lettersSelectButtons.get(i).setPreferredSize(CONTEXT_SQUARES_DIM);
			lettersSelectButtons.get(i).addActionListener(contextListener);
			lettersContextPanel.add(lettersSelectButtons.get(i));
		}
		lettersContextFrame.getContentPane().add(lettersContextPanel);
		lettersContextFrame.setResizable(false);
		lettersContextFrame.pack();
		lettersContextFrame.setVisible(false);
		
		wordsReachableFrame = new JFrame();
		wordsReachablePanel = new JPanel();
		//TODO fix size
		wordsFromLetterModel = new DefaultListModel<Word>();
		wordsFromLetterList = new JList<Word>(wordsFromLetterModel);
		wordsFromLetterListListener = new WordsFromLetterListListener();
		wordsFromLetterList.addMouseListener(wordsFromLetterListListener);
		scrollPane2 = new JScrollPane(wordsFromLetterList);
		scrollPane2
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		wordsReachablePanel.add(scrollPane2);
		
		wordsReachableFrame.getContentPane().add(wordsReachablePanel);
		wordsReachableFrame.setVisible(false);
		
		rightClickFrame = new JFrame();
		rightClickPanel = new JPanel();
		lettersSelectButton = new JButton("Change Letter");
		lettersSelectButton.addActionListener(buttonListener);
		wordsReachableButton = new JButton("Show Words Reachable");
		wordsReachableButton.addActionListener(buttonListener);
		rightClickPanel.setLayout(new BoxLayout(rightClickPanel, BoxLayout.Y_AXIS));
		rightClickPanel.add(lettersSelectButton);
		rightClickPanel.add(wordsReachableButton);
		rightClickFrame.getContentPane().add(rightClickPanel);
		rightClickFrame.setResizable(false);
		rightClickFrame.pack();
		rightClickFrame.setVisible(false);

	}
	
	private void compareResults() {
		numberMissed = 0;
		ArrayList<String> wordsEntered = new ArrayList<String>();
		for (JTextField field : wordsToEnter) {
			wordsEntered.add(field.getText().toUpperCase());
		}
		
		if (!view.getFoundWords().isEmpty()) {
			for (Word w: view.getFoundWords()) {
				if (!wordsEntered.contains((String)w.getValue())) {
					numberMissed--;
				}
				else
					System.out.println(w.getValue()); //TODO add to gui
			}
		}
		System.out.println(numberMissed); //TODO add to gui
	}

	private void updateFoundWords() {
		// TODO make simpler
		ArrayList<Word> wordsFound = view.getFoundWords();
		assert (wordsFound != null);

		// TODO review why using a defaultListModel words/what this is
		model.removeAllElements();
		for (Word w : wordsFound)
			model.addElement(w/* (String)w.getValue() */);
	}

	private void clearFoundWords() {
		view.clearFoundWords();
		model.removeAllElements();
	}

	private void updateSquares() {
		for (int i = 0; i < NUM_SQUARES; i++) {
			String currLetter = view.getNextLetter(i);
			((BoggleSquare) squaresArray[i]).setText(currLetter);
		}

		clearHighlights();
	}

	private void clearHighlights() {
		for (JPanel square : squaresArray) {
			((BoggleSquare) square).unhighlight();
		}
	}
	
	private void populateFoundWordsForCurrentSquare() {
		ArrayList<Word> wordsOnLetter = view.getWordsOnLetter(
					selectedBoggleSquare.getXCoordinate(), selectedBoggleSquare.getYCoordinate());
		
		wordsFromLetterModel.removeAllElements();
		for (Word w: wordsOnLetter) {
			wordsFromLetterModel.addElement(w);
		}
	}
	
	private void highlightWordOnBoard(Word w) {
		// un-highlight previous word that might be highlighted in grid
		for (BoggleSquare currHighlighted : displayingWordBoggleSquares) {
			currHighlighted.unhighlight();
		}

		// highlight new word
		if (w != null) { //in case we are selected inside the area when there aren't any words inside
			for (Letter l : w.getLetters()) {
				BoggleSquare square = (BoggleSquare) squaresArray[l
						.getCoordinate().getY() * 4 + l.getCoordinate().getX()];
				displayingWordBoggleSquares.add(square);
				square.highlightForDisplayingWord();
			}
		}
	}

	private class BoggleWindowFocusListener implements WindowFocusListener {

		@Override
		public void windowGainedFocus(WindowEvent e) {
		}

		@Override
		public void windowLostFocus(WindowEvent e) {
			((JFrame) e.getSource()).setVisible(false);
		}

	}

	// Timer button
	// TODO fix it so timer keeps going while findingWordsAlgorithm is going
	private class TimeDisplayListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			currentTime--;
			String displayTime = "0";
			
			int seconds = 0;
			
			if (currentTime >= 120 ) {
				displayTime += "2:";
				seconds = currentTime - 120;
			}
			else if (currentTime >= 60) {
				displayTime += "1:";
				seconds = currentTime - 60;
			}
			else {
				displayTime += "0:";
				seconds = currentTime;
			}
			
			if (seconds < 10)
				displayTime += "0";
			displayTime += Integer.toString(seconds);
			timerDisplay.setText(displayTime);
			if (currentTime == 0) {
				timer.stop();
				timesUp.setVisible(true);
				compareResults();
				currentTime = START_TIME;
				timerDisplay.setText(START_TIME_STRING);
				startTimerButton.setText("Start Timer");
			}
		}

	}

	private class BoggleButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == findWordsAll) {
				if (lettersChanged) {
					view.solveAll();
					clearHighlights();
					updateFoundWords();
					lettersChanged = false;
				}
			} else if (e.getSource() == shuffleLetters) {
				view.shuffleLetters();
				updateSquares();
				clearFoundWords();
				lettersChanged = true;
			} else if (e.getSource() == startTimerButton) {
				JButton timerButton = ((JButton) e.getSource());
				if (timer.isRunning()) {
					timer.stop();
					timerButton.setText("Continue");
				} else {
					timer.start();
					timerButton.setText("Pause");
				}
			} else if (e.getSource() == lettersSelectButton) {
				lettersContextFrame.setVisible(true);
				rightClickFrame.setVisible(false);
			} else if (e.getSource() == wordsReachableButton) {
				populateFoundWordsForCurrentSquare();
				wordsReachableFrame.setVisible(true);
				rightClickFrame.setVisible(false);
			}

		}
	}

	private class ComboBoxListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			if (e.getSource() == checkBoxShowAnswers) {
				if (checkBoxShowAnswers.isSelected())
					wordFoundList.setVisible(true);
				else
					wordFoundList.setVisible(false);
			}

		}

	}

	// For context menu square buttons
	private class ContextMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton selectedButton = (JButton) e.getSource();
			String text = selectedButton.getText();
			if (!text.equalsIgnoreCase("Exit")) {
				if (text != selectedBoggleSquare.getText()) { //don't do anything is letter isn't changed
					view.changeLetter(selectedBoggleSquare.getXCoordinate(), selectedBoggleSquare.getYCoordinate(), text);
					updateSquares(); //for testing, have higher class update all and make sure only the one I'm trying to change changes
					//selectedBoggleSquare.setText(text); //comment in after sure that model is changing letter in data correctly
					lettersChanged = true;
				}
			}
			lettersContextFrame.setVisible(false);
		}
	}

	private class PanelListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// un-highlight previous
			if (selectedBoggleSquare != null)
				selectedBoggleSquare.toggleSelected();

			// highlight new, assign to currSelected
			selectedBoggleSquare = (BoggleSquare) e.getSource();
			selectedBoggleSquare.toggleSelected();

			// Left-click a square, normal operation
			if (e.getButton() == MouseEvent.BUTTON1) {
			}
			// Right-click a square, open up context menu
			else if (e.getButton() == MouseEvent.BUTTON3) {

				if (rightClickFrame.isVisible()) {
					rightClickFrame.setVisible(false);
				} else {
					rightClickFrame.setVisible(true);
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		//So we can hide/show bottom panel
		public void mouseEntered(MouseEvent e) {
			if ((JPanel)e.getSource() == lowerPanelLip) {
				lowerPanel.setVisible(true);
				lowerPanelLip.setBorder(new TitledBorder(""));
			}
			else {
				lowerPanel.setVisible(false);
				lowerPanelLip.setBorder(tb);
			}

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}
	
	private class WordsFromLetterListListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			highlightWordOnBoard(wordsFromLetterList.getSelectedValue());
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}

	// TODO not sure if mouse listener is best for list, but it's working
	private class ListListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {

			highlightWordOnBoard(wordFoundList.getSelectedValue());
			/*// un-highlight previous word that might be highlighted in grid
			for (BoggleSquare currHighlighted : displayingWordBoggleSquares) {
				currHighlighted.unhighlight();
			}

			// highlight new word
			Word w = wordFoundList.getSelectedValue();

			if (w != null) { //in case we are selected inside the area when there aren't any words inside
				for (Letter l : w.getLetters()) {
					BoggleSquare square = (BoggleSquare) squaresArray[l
							.getCoordinate().getY() * 4 + l.getCoordinate().getX()];
					displayingWordBoggleSquares.add(square);
					square.highlightForDisplayingWord();
				}
			}*/
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}

}
