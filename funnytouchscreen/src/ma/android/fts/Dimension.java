package ma.android.fts;

public class Dimension
{
	private int squareNumberX;
	private int squareNumberY;
	
	public Dimension (){
	}
	public Dimension (int x, int y)
	{
		squareNumberX = x;
		squareNumberY = y;
	}
	public int getSquareNumberX() {
		return squareNumberX;
	}
	public void setSquareNumberX(int squareNumberX) {
		this.squareNumberX = squareNumberX;
	}
	public int getSquareNumberY() {
		return squareNumberY;
	}
	public void setSquareNumberY(int squareNumberY) {
		this.squareNumberY = squareNumberY;
	}
}
