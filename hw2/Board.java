// Board.java
package tetris;

/**
 * CS108 Tetris Board. Represents a Tetris board -- essentially a 2-d grid of
 * booleans. Supports tetris pieces and row clearing. Has an "undo" feature that
 * allows clients to add and remove pieces efficiently. Does not do any drawing
 * or have any idea of pixels. Instead, just represents the abstract 2-d board.
 */
public class Board {
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;

	private int[] widths;
	private int[] heights;

	// These are for backUp
	private int[] xWidths;
	private int[] xHeights;
	private boolean[][] xGrid;

	// Here a few trivial methods are provided:

	/**
	 * Creates an empty board of the given width and height measured in blocks.
	 */
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;

		widths = new int[height];
		heights = new int[width];

		xGrid = new boolean[width][height];
		xWidths = new int[height];
		xHeights = new int[width];
	}

	/**
	 * Returns the width of the board in blocks.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the board in blocks.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the max column height present in the board. For an empty board
	 * this is 0.
	 */
	public int getMaxHeight() {
		int result = 0;

		for (int i = 0; i < heights.length; i++)
			if (heights[i] > result)
				result = heights[i];

		return result;
	}

	/**
	 * Checks the board for internal consistency -- used for debugging.
	 */
	public void sanityCheck() {
		if (DEBUG) {
			int count;

			// heights array check
			for (int i = grid.length - 1; i >= 0; i--)
				for (int j = grid[i].length - 1; j >= 0; j--)
					if (grid[i][j])
						if (j + 1 != heights[i])
							throw new RuntimeException("Check heights array");
						else
							break;

			// widths array check
			// in first for cycle we first check for k and then for i so that we
			// avoid array out of bounds exception, otherwise we would try to
			// get grid[length+1]
			int k = 0;
			for (int i = 0; k < grid.length && i < grid[k].length; i++, k++) {
				count = 0;
				for (int j = 0; j < grid.length; j++) {
					if (grid[j][i])
						count++;
					if (j == grid.length - 1 && count != widths[i])
						throw new RuntimeException("Check widths array");
				}
			}

			// max height check
			// in getMaxHeight() we iterate through heights array, that we check
			// in this method but checking it again won't do any harm
			// if we are here it means we already checked heights array and
			// there is no problem with it so we can use that
			for (int i = 0; i < heights.length; i++)
				if (heights[i] == getMaxHeight())
					break;
				else if (i == heights.length - 1)
					throw new RuntimeException("Check max height");
				else continue; // this line is odd but i really like using continue

		}
	}

	/**
	 * Given a piece and an x, returns the y value where the piece would come to
	 * rest if it were dropped straight down at that x.
	 * 
	 * <p>
	 * Implementation: use the skirt and the col heights to compute this fast --
	 * O(skirt length).
	 */
	public int dropHeight(Piece piece, int x) {
		if (x < 0 || x >= height)
			return -1;

		int result = 0;

		int[] skirt = piece.getSkirt();

		for (int i = 0; i < piece.getWidth(); i++)
			if (getColumnHeight(x + i) - skirt[i] > result)
				result = getColumnHeight(x + i) - skirt[i];

		return result;
	}

	/**
	 * Returns the height of the given column -- i.e. the y value of the highest
	 * block + 1. The height is 0 if the column contains no blocks.
	 */
	public int getColumnHeight(int x) {
		if (x < 0 || x >= width)
			return -1;

		return heights[x];
	}

	/**
	 * Returns the number of filled blocks in the given row.
	 */
	public int getRowWidth(int y) {
		if (y < 0 || y >= height)
			return -1;

		return widths[y];
	}

	/**
	 * Returns true if the given block is filled in the board. Blocks outside of
	 * the valid width/height area always return true.
	 */
	public boolean getGrid(int x, int y) {
		if (x >= width || x < 0 || y >= height || y < 0)
			return true;

		return grid[x][y];
	}

	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;

	/**
	 * Attempts to add the body of a piece to the board. Copies the piece blocks
	 * into the board grid. Returns PLACE_OK for a regular placement, or
	 * PLACE_ROW_FILLED for a regular placement that causes at least one row to
	 * be filled.
	 * 
	 * <p>
	 * Error cases: A placement may fail in two ways. First, if part of the
	 * piece may falls out of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 * Or the placement may collide with existing blocks in the grid in which
	 * case PLACE_BAD is returned. In both error cases, the board may be left in
	 * an invalid state. The client can use undo(), to recover the valid,
	 * pre-place state.
	 */
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed)
			throw new RuntimeException("place commit problem");

		int result = PLACE_OK;

		commit();

		committed = false;

		if (x < 0 || x >= width || y < 0 || y >= height)
			return PLACE_OUT_BOUNDS;

		TPoint[] body = piece.getBody();
		for (int i = 0; i < body.length; i++) {
			int newX = x + body[i].x;
			int newY = y + body[i].y;

			if (newX < 0 || newX >= width || newY < 0 || newY >= height)
				return PLACE_OUT_BOUNDS;

			if (grid[newX][newY])
				return PLACE_BAD;

			grid[newX][newY] = true;
			widths[newY] += 1;
			heights[newX] = (heights[newX] >= newY + 1) ? heights[newX] : newY + 1;

			if (widths[newY] == width)
				result = PLACE_ROW_FILLED;
		}

		// if result is out of bonds or bad then it returns immediately
		// otherwise it checks for sanity and then returns
		sanityCheck();

		return result;
	}

	/**
	 * Deletes rows that are filled all the way across, moving things above
	 * down. Returns the number of rows cleared.
	 */
	public int clearRows() {
		int rowsCleared = 0;

		for (int i = 0; i < getMaxHeight(); i++) {
			if (widths[i] == width) {
				for (int k = i; k < getMaxHeight() - 1; k++) {
					for (int j = 0; j < width; j++) {
						grid[j][k] = grid[j][k + 1];
					}
					widths[k] = widths[k + 1];
				}
				for (int j = 0; j < width; j++){
					grid[j][getMaxHeight() - 1] = false;
					widths[getMaxHeight() - 1] = 0;
						
				}
				rowsCleared++;
				i--;
			}
		}
		for (int j = 0; j < width; j++){
			int l = heights[j] - 1;
			while(l >= 0 && !grid[j][l])
				l--;
			heights[j] = l + 1;
		}

		sanityCheck();
		committed = false;
		
		return rowsCleared;
	}


	/**
	 * Reverts the board to its state before up to one place and one
	 * clearRows(); If the conditions for undo() are not met, such as calling
	 * undo() twice in a row, then the second undo() does nothing. See the
	 * overview docs.
	 */
	public void undo() {
		if (!committed) {

			System.arraycopy(xWidths, 0, widths, 0, xWidths.length);
			System.arraycopy(xHeights, 0, heights, 0, xHeights.length);

			for (int i = 0; i < width; i++)
				System.arraycopy(xGrid[i], 0, grid[i], 0, xGrid[i].length);
		}
		sanityCheck();
		commit();
	}

	private void backUp() {
		System.arraycopy(widths, 0, xWidths, 0, widths.length);
		System.arraycopy(heights, 0, xHeights, 0, heights.length);

		for (int i = 0; i < width; i++)
			System.arraycopy(grid[i], 0, xGrid[i], 0, grid[i].length);
	}

	/**
	 * Puts the board in the committed state.
	 */
	public void commit() {
		committed = true;
		backUp();
	}

	/*
	 * Renders the board state as a big String, suitable for printing. This is
	 * the sort of print-obj-state utility that can help see complex state
	 * change over time. (provided debugging utility)
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height - 1; y >= 0; y--) {
			buff.append('|');
			for (int x = 0; x < width; x++) {
				if (getGrid(x, y))
					buff.append('+');
				else
					buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x = 0; x < width + 2; x++)
			buff.append('-');
		return (buff.toString());
	}
}
