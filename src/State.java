import java.util.ArrayList;
import java.awt.Point;

public class State {
	int[][] board;
	int cost = 0;
	int value;
	
	State parent = null;

	public State(int n) {
		board = new int[n][n];
		ArrayList<Integer> list = new ArrayList<>();
		for (int i = 1; i < n * n; i++)
			list.add(i);
		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board.length; c++)
				if (list.size() > 0)
					board[r][c] = list.remove((int) (Math.random() * list.size()));
		// keep track of the zero here
	}

	public State(State other) {
		parent = other;
		cost = parent.cost + 1;
		board = new int[other.board.length][other.board.length];
		for (int r = 0; r < other.board.length; r++)
			for (int c = 0; c < other.board.length; c++)
				board[r][c] = other.board[r][c];
	}

	public Point getGap() {
		// Point p = new Point(2, 3);
		// p.y is equal to 3
		// p.x is equal to 2

		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board.length; c++) {
				if (board[r][c] == 0) // if the integer is 0
				{
					return new Point(r, c);
				}
			}
		return null;
		// the Point is board.getGap();
		// Point p = board.getGap();
		// p.x; --> to get the x value
		// p.y; --> to get the y value

	}
	public boolean equals(Object other)
	{
		if(other instanceof State)
			return ((State)other).board == board && ((State)other).board == board;
		return false;
	}


}
