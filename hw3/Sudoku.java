package assign3;

import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.
	
	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
	"1 6 4 0 0 0 0 0 2",
	"2 0 0 4 0 3 9 1 0",
	"0 0 5 0 8 0 4 0 7",
	"0 9 0 0 0 6 5 0 0",
	"5 0 0 1 0 2 0 0 8",
	"0 0 8 9 0 0 0 3 0",
	"8 0 9 0 4 0 2 0 0",
	"0 7 3 5 0 9 0 0 1",
	"4 0 0 0 0 0 6 7 9");
	
	
	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
	 "530070000",
	 "600195000",
	 "098000060",
	 "800060003",
	 "400803001",
	 "700020006",
	 "060000280",
	 "000419005",
	 "000080079");
	
	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
	"3 7 0 0 0 0 0 8 0",
	"0 0 1 0 9 3 0 0 0",
	"0 4 0 7 8 0 0 0 3",
	"0 9 3 8 0 0 0 1 2",
	"0 0 0 0 4 0 0 0 0",
	"5 2 0 0 0 6 7 9 0",
	"6 0 0 0 2 1 0 4 0",
	"0 0 0 5 3 0 9 0 0",
	"0 3 0 0 0 0 0 5 1");
	
	
	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;
	
	// Provided various static utility methods to
	// convert data formats to int[][] grid.
	
	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}
	
	
	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}
		
		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}
	
	
	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);
		
		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}
	
	private int grid[][], solutionGrid[][], solutionsNum;
	private long elapsedTime;
	private ArrayList<Spot> spots;
	
	private class Spot{
		
		private int x, y;
		
		public Spot(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		public void set(int value){
			grid[this.x][this.y] = value;
		}
		
		public HashSet<Integer> getProbableValues(){
			HashSet<Integer> result = new HashSet<Integer>();
			
			for(int i = 0; i < SIZE; i++)
				if(add(i+1))
					result.add(i + 1);
			
			return result;
		}
		
		private boolean add(int i){
			for(int j = 0; j < SIZE; j++)
				if(grid[x][j] == i || grid[j][y] == i ||
						grid[j/PART + x/PART * PART][j%PART + y/PART * PART] == i)
					return false;
				
			return true;
		}
		
	}
	

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		grid = ints;
		solutionGrid = new int[SIZE][SIZE];
		elapsedTime = 0;
		solutionsNum = 0;
		
		spots = new ArrayList<Spot>();
		
		for(int i = 0; i < SIZE; i++)
			for(int j = 0; j < SIZE; j++)
				if(grid[i][j] == 0)
					spots.add(new Spot(i, j));
					
	}
	
	
	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		long start = System.currentTimeMillis();
		
		helper(0);
		
		long end = System.currentTimeMillis();
		
		elapsedTime = end - start;
		
		return solutionsNum;
	}
	
	private void helper(int i){
		
		if(solutionsNum >= MAX_SOLUTIONS)
			return;
		
		if(i >= spots.size()){
			solutionsNum++;
			if(solutionsNum == 1){
				for(int j = 0; j < SIZE; j++)
					System.arraycopy(grid[j], 0, solutionGrid[j], 0, SIZE);
			}
			i --;
			return;
		}
		
		Spot curr = spots.get(i);
		HashSet<Integer> currProb = curr.getProbableValues();
		
		for(int j : currProb){
			curr.set(j);
			helper(i + 1);
			curr.set(0);
		}
		
	}
	
	
	public String getSolutionText() {
		String res = "";
		
		if(solutionsNum != 0)
			for(int i = 0; i < SIZE; i++)
				res += Arrays.toString(solutionGrid[i])
						.replace("[", "")
					    .replace("]", "")
					    .replace(",", "")
					    .replace(" ", "")
					    .trim() + "\n";
		
		return res;
	}
	
	public long getElapsed() {
		return elapsedTime;
	}

}
