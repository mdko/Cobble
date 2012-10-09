package model;

public class Coordinate {
	private int X; //Columns
	private int Y; //Rows
	
	public Coordinate(int X, int Y) {
		this.X = X;
		this.Y = Y;
	}
	
	public int getX() {
		return X;
	}
	
	public int getY() {
		return Y;
	}
	
	public String toString() {
		return "X: " + X + ", Y: " + Y;
	}
}
