// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

package assign1;

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid. Does not make a copy.
	 * 
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}

	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * 
	 * @param ch
	 *            char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {

		if (grid.length == 0)
			return 0;

		int rightest = -1;
		int bottomest = -1;
		int leftest = -1;
		int topest = -1;

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == ch) {

					if (leftest == -1) {
						rightest = i;
						bottomest = j;
						leftest = i;
						topest = j;
					}

					if (i > rightest)
						rightest = i;
					if (j > bottomest)
						bottomest = j;
					if (j < topest)
						topest = j;
				}
			}
		}

		return (leftest == -1) ? 0 : (rightest - leftest + 1) * (bottomest - topest + 1);
	}

	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * 
	 * @return number of + in grid
	 */
	public int countPlus() {

		int result = 0;

		for (int i = 1; i < grid.length - 1; i++)
			for (int j = 1; j < grid[0].length - 1; j++)
				if (grid[i][j] != ' ')
					result += findPlus(i, j);

		return result;
	}

	private int findPlus(int row, int col) {
		int top = 0, bot = 0, left = 0, right = 0;

		for (int i = row; i >= 0; i--)
			if (grid[row][col] == grid[i][col])
				top++;
			else
				break;

		for (int i = row; i < grid.length; i++)
			if (grid[row][col] == grid[i][col])
				bot++;
			else
				break;

		for (int i = col; i >= 0; i--)
			if (grid[row][col] == grid[row][i])
				left++;
			else
				break;

		for (int i = col; i < grid[0].length; i++)
			if (grid[row][col] == grid[row][i])
				right++;
			else
				break;

		return (top == bot && bot == left && left == right) ? 1 : 0;
	}

}
