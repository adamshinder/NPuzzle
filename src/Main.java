import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
	Scanner input = new Scanner(System.in);
	final int SIDE_SIZE = 4;
	ArrayList<State> closedList = new ArrayList<>();
	State goal1 = new State(SIDE_SIZE);// , goal2 = new State(SIDE_SIZE);
	// int freq[] = new int[256];

	// call on the AStar
	// call on the BFS

	public Main(String fileName) {
		State current = null;
		if (fileName == null)
			current = new State(SIDE_SIZE);
		else
			current = loadStateFromFile(fileName);

		ArrayList<Integer> list = new ArrayList<>();
		for (int i = 0; i < SIDE_SIZE * SIDE_SIZE; i++)
			list.add(i);
		for (int r = 0; r < SIDE_SIZE; r++)
			for (int c = 0; c < SIDE_SIZE; c++)
				if (list.size() > 0)
					goal1.board[r][c] = list.remove(0);

		System.out.println("Starting State Heuristic:" + h(current));
		printState(current);

//		for(State s: generateSuccessors(current))
//			printState(s);

		// INITIATE ALGORITHIM HERE
		State finalState = AStar(current); // call on the BFS function
		//State finalState = BFS(current); //call on the BFS function
		// System.out.print("Final:" + list);-> List is empty here
		// System.out.print("Final:" + finalState);//finalState is nullSolution
		System.out.println("Solution complete!  Press Enter to scroll through the moves:");
		ArrayList<State> path = new ArrayList<>();
		while (finalState != null) {
			path.add(finalState);
			finalState = finalState.parent;
		}

		for (int i = path.size() - 1; i >= 0; i--) {
			System.out.println("Moves to completion: " + i + "  h: " + h(path.get(i)));
			printState(path.get(i));
			System.out.println("Enter to continue...");
			input.nextLine();// that is the enter button
		}
	}

	private State loadStateFromFile(String fileName) {
		State state = null;
		try {
			Scanner in = new Scanner(new File(fileName));
			String s = "";
			while (in.hasNext()) {
				s += in.nextLine() + ":";
			}
			String[] lines = s.split(":");
			state = new State(lines.length);
			for (int r = 0; r < lines.length; r++) {
				String[] tokens = lines[r].split(" ");
				for (int c = 0; c < tokens.length; c++)
					state.board[r][c] = Integer.parseInt(tokens[c]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return state;
	}

	private State solve(State startingState) {
		return null;
	}

	private int h(State st) // given a current state (st), whats it worth, return what the state is worth.
							// use manhattan
	{
		int heuristic = 0;

		// heuristic distance goes here // goalSat
		// look for a number, find its spot, compare it to the spot of the goal state
		for (int rst = 0; rst < st.board.length; rst++)
			for (int cst = 0; cst < st.board.length; cst++)// length is height
			{
				// System.out.println(st.board[rst][cst] + " is at row " + rst + " col " + cst);
				// for each spot in st.board,
				// go find the matching value in goal1.board
				for (int rgoal = 0; rgoal < goal1.board.length; rgoal++)
					for (int cgoal = 0; cgoal < goal1.board.length; cgoal++)// length is height
						if (st.board[rst][cst] == goal1.board[rgoal][cgoal])
							heuristic = heuristic + Math.abs(rst - rgoal) + Math.abs(cst - cgoal);

				// System.out.println(heuristic);
			}
		return heuristic; // h = 0
	}

	// make two methods here
	private State AStar(State take)// informed search
	{
		ArrayList<State> openList = new ArrayList<>();
		openList.add(take);
		// State p = new State;
		ArrayList<State> closedList = new ArrayList<>();
		// closed list - places we've visited
		while (openList.size() > 0) {

			// sort the list here //value(State) = heuristic(State) + cost(State);
			take = openList.get(0);
			// openList.remove(take);

			if (closedList.contains(take)) // contains uses the equals() method in AStarNode
				continue; // skip ahead to the next iteration of the loop
			// if it's not, add it to the closed list
			closedList.add(take);

			if (isGoal(take) == true) // is it the goal?
				return take; // and then if not

			else {
				openList.addAll(generateSuccessors(take));
				for (int i = 0; i < openList.size(); i++) {
					openList.get(i).value = h(openList.get(i)) + openList.get(i).cost; // h is a funciton - here cost is a variable in take
					for (int j = i + 1; j < openList.size(); j++) {
						openList.get(j).value = h(openList.get(j)) + openList.get(j).cost; // h is a funciton - here cost is a variable in take	
						if (openList.get(i).value > openList.get(j).value) // should I make it i - 1
						{
							State temp = openList.get(i);
							openList.set(i, openList.get(j));
							openList.set(j, temp);
						}
					}
				}
			}

			// cost is for the first time 1 and for every state after is +1
			// each child need to be calculated in the state - V(s) = h(s) + c(s)
		}
		return null;
	}

	private State BFS(State startingState) // uninformed search
	{
		// if its possible to do any of the 4 moves, do it.
		ArrayList<State> closedList = new ArrayList<>();
		ArrayList<State> queue = new ArrayList<>();
		queue.add(startingState);

		// do I add heuristics to the State class?
		// as long as there are things in the Queue
		while (queue.size() > 0) {
			State checkState = queue.remove(0);
			// add new states to the

			// see if this node is in the closed list
			if (closedList.contains(checkState)) // contains uses the equals() method in BFSNode
				continue; // skip ahead to the next iteration of the loop

			// if it's not, add it to the closed list
			closedList.add(checkState);

			printState(checkState);

			if (isGoal(checkState) == true) // if it's terminal?
				return checkState; // and then if not

			queue.addAll(generateSuccessors(checkState));
			// add "successors"to the queue and remove the last thing -> remove(0)
		}
		System.out.println("No solution found!");
		return null;
		//

	}

	private ArrayList<State> generateSuccessors(State st) {
		ArrayList<State> successors = new ArrayList<>();
		// were is the gap on the board
		// what are the 4 new moves
		Point gap = st.getGap();

		int r = gap.x;
		int c = gap.y;

		// switch the zero with a left or right, up, down??
		//
		// Left
		if (c > 0) {
			State s = new State(st);
			s.board[r][c] = s.board[r][c - 1];
			s.board[r][c - 1] = 0;
			successors.add(s);
		}

		// right
		if ((SIDE_SIZE - 1) > c) {
			State s = new State(st);
			s.board[r][c] = s.board[r][c + 1];
			s.board[r][c + 1] = 0;
			successors.add(s);
		}
		// up
		if (r > 0) {
			State s = new State(st);
			s.board[r][c] = s.board[r - 1][c];
			s.board[r - 1][c] = 0;
			successors.add(s);
		}

		// down
		if (r < (SIDE_SIZE - 1)) {
			State s = new State(st);
			s.board[r][c] = s.board[r + 1][c];
			s.board[r + 1][c] = 0;
			successors.add(s);
		}

		// find the potential moves around the "gap"
		// make new states as the succesors
		return successors;
	}

	private boolean isGoal(State st) // checks if its the goal stae - boolean - true flase
	{
		return h(st) == 0;
	}

	private void printState(State st) {
		if (st == null)
			return;
		String s = "";
		for (int r = 0; r < st.board.length; r++) {
			for (int c = 0; c < st.board.length; c++)
				s += st.board[r][c] + "\t";
			s += "\n";
		}
		System.out.println(s);
	}

	public static void main(String[] args) {
		//new Main(null); //to make a random board
		new Main("4x4_test.txt"); // to load the specified file
	}
}