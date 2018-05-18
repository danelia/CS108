//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.
package assign1;

public class TetrisGrid {

	private boolean[][] grid;

	/**
	 * Constructs a new instance with the given grid. Does not make a copy.
	 * 
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
	}

	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		for (int i = 0; i < grid[0].length; i++) {
			if (clearThisRow(i)) {
				clearSingleRow(i);
			}
		}
	}

	private boolean clearThisRow(int row) {
		for (int i = 0; i < grid.length; i++)
			if (!grid[i][row])
				return false;

		return true;
	}

	private void clearSingleRow(int row) {
		for (int i = row; i < grid[0].length - 1; i++) {
			for (int j = 0; j < grid.length; j++) {
				grid[j][i] = grid[j][i + 1];
			}
		}
		for (int i = 0; i < grid.length; i++)
			grid[i][grid[0].length - 1] = false;
	}

	/**
	 * Returns the internal 2d grid array.
	 * 
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return this.grid;
	}
}
