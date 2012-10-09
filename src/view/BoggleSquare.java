package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class BoggleSquare extends JPanel {

	private final Color SQUARE_COLOR_DISPLAYING_WORD = Color.GREEN;
	private final Color SQUARE_COLOR_INITIAL = Color.WHITE;
	private final Color SQUARE_COLOR_SELECTED = Color.CYAN;
	private final int SQUARE_HEIGHT = 100;
	private final int SQUARE_WIDTH = 100;
	private JLabel label;
	private boolean selected;
	private int xCoordinate;
	private int yCoordinate;
	
	public BoggleSquare(String letter, int x, int y) {
		
		super();
		
		setBackground(SQUARE_COLOR_INITIAL);
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(SQUARE_HEIGHT, SQUARE_WIDTH));
		label = new JLabel(letter);
		xCoordinate = x;
		yCoordinate = y;
		label.setFont(new Font(Font.SERIF, Font.PLAIN, 50));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		add(label, BorderLayout.CENTER);
		setBorder(BorderFactory.createRaisedBevelBorder());
	}
	
	public void toggleSelected() {
		selected = (selected) ? false : true;
		updateColor();
	}

	public boolean isSelected() {
		return selected;
	}
	
	private void updateColor() {
		if (selected)
			setBackground(SQUARE_COLOR_SELECTED);
		else
			setBackground(SQUARE_COLOR_INITIAL);
	}
	
	public String getText() {
		return label.getText();
	}
	
	public void setText(String letter) {
		label.setText(letter);
	}

	public void highlightForDisplayingWord() {
		setBackground(SQUARE_COLOR_DISPLAYING_WORD);
	}

	public void unhighlight() {
		setBackground(SQUARE_COLOR_INITIAL);
	}
	
	public int getXCoordinate() {
		return xCoordinate;
	}
	
	public int getYCoordinate() {
		return yCoordinate;
	}
}
