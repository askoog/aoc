package se.askware.aoc2022.dec08;

import java.io.IOException;
import java.util.List;

import se.askware.aoc2022.common.AocBase;
import se.askware.aoc2022.common.Cell;
import se.askware.aoc2022.common.Grid;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		int[][] grid = getGrid(input);

		int count = 0;
		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[0].length; x++) {

				boolean seen = true;
				seen = isSeenFromTop(grid, y, x) ||
						isSeenFromBottom(grid, y, x) ||
						isSeenFromLeft(grid, y, x)
						|| isSeenFromRight(grid, y, x);

				if (seen){
					count++;
				}
				System.out.print( " " + grid[y][x] + (seen?"*":" "));
			}
			System.out.println();
		}

		System.out.println(count);
	}

	private static int[][] getGrid(List<String> input) {
		int[][] grid = new int[input.get(0).length()][input.size()];

		int row = 0;
		for (String s : input) {
			for (int i = 0; i < s.length(); i++) {
				grid[row][i] = s.charAt(i) - '0';
			}
			row++;
		}
		return grid;
	}

	private static boolean isSeenFromTop(int[][] grid, int y, int x) {
		for (int i = 0; i < y; i++) {
			if (grid[i][x] >= grid[y][x]) {
				return false;
			}
		}
		return true;
	}

	private static boolean isSeenFromBottom(int[][] grid, int y, int x) {
		for (int i = y + 1; i < grid.length; i++) {
			if (grid[i][x] >= grid[y][x]) {
				return false;
			}
		}
		return true;
	}

	private static boolean isSeenFromLeft(int[][] grid, int y, int x) {
		for (int i = 0; i < x; i++) {
			if (grid[y][i] >= grid[y][x]) {
				return false;
			}
		}
		return true;
	}

	private static boolean isSeenFromRight(int[][] grid, int y, int x) {
		for (int i = x + 1; i < grid[0].length; i++) {
			if (grid[y][i] >= grid[y][x]) {
				return false;
			}
		}
		return true;
	}

	private static class Tree extends Cell {
		int height;

		public Tree(int height) {
			this.height = height;
		}
	}

	@Override
	public void solvePartTwo(List<String> input) {

		Grid<Tree> treeGrid= new Grid<>(input.size(), input.get(0).length());
		treeGrid.init((row, col) -> new Tree(input.get(row).charAt(col) - '0'));

		System.out.println(treeGrid);

		int[][] grid = getGrid(input);

		int max = 0;
		for (int y = 0; y < grid.length; y++) {
			for (int x = 0; x < grid[0].length; x++) {


				int seen = seenFromTop(grid, y, x) *
						seenFromBottom(grid, y, x) *
						seenFromLeft(grid, y, x) *
						seenFromRight(grid, y, x);

				System.out.print( " " + grid[y][x] + "(" + seenFromTop(grid, y, x) + "," + seenFromBottom(grid, y, x)  + "," + seenFromLeft(grid, y, x) + "," + seenFromRight(grid, y, x) + "=" + seen + ")");
				max = Math.max(max, seen);
			}
			System.out.println();

		}

		System.out.println(max);
	}


	private static int seenFromTop(int[][] grid, int y, int x) {
		int count=0;
		for (int i = y-1; i >= 0; i--) {
			count++;
			if (grid[i][x] >= grid[y][x]) {
				break;
			}
		}
		return count;
	}

	private static int seenFromBottom(int[][] grid, int y, int x) {
		int count=0;

		for (int i = y + 1; i < grid.length; i++) {
			count++;
			if (grid[i][x] >= grid[y][x]) {
				break;
			}
		}
		return count;
	}

	private static int seenFromLeft(int[][] grid, int y, int x) {
		int count=0;

		for (int i = x - 1; i >= 0 ; i--) {
			count++;
			if (grid[y][i] >= grid[y][x]) {
				break;
			}
		}
		return count;
	}

	private static int seenFromRight(int[][] grid, int y, int x) {
		int count=0;

		for (int i = x + 1; i < grid[0].length; i++) {
			count++;

			if (grid[y][i] >= grid[y][x]) {
				break;
			}
		}
		return count;
	}
}
