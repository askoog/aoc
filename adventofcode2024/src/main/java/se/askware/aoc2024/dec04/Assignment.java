package se.askware.aoc2024.dec04;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import se.askware.aoc2024.common.AocBase;
import se.askware.aoc2024.common.CharCell;
import se.askware.aoc2024.common.Grid;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		final long count = grid.getAll().filter(c -> c.value == 'X')
				.flatMap(c -> findXmas(grid, c))
				.filter(s -> s.equals("XMAS"))
				.count();
		System.out.println(count);
	}

	private Stream<String> findXmas(Grid<CharCell> grid, CharCell c) {
		String xmas = "XMAS";
		List<String> strings = new ArrayList<>();
		strings.add(getString(grid, c, xmas, 0, 1));
		strings.add(getString(grid, c, xmas, 0, -1));

		strings.add(getString(grid, c, xmas, 1, 0));
		strings.add(getString(grid, c, xmas, -1, 0));

		strings.add(getString(grid, c, xmas, 1, 1));
		strings.add(getString(grid, c, xmas, 1, -1));

		strings.add(getString(grid, c, xmas, -1, 1));
		strings.add(getString(grid, c, xmas, -1, -1));

		return strings.stream();
	}

	private static String getString(Grid<CharCell> grid, CharCell c, String xmas, int yOperator, int xOperator) {
		List<CharCell> cells = new ArrayList<>();
		for (int i = 0; i < xmas.length(); i++) {
			cells.add(grid.getOptionalCell(c.row + (yOperator * i), c.col + (xOperator * i)).orElse(CharCell.EMPTY));
		}
		return cells.stream().map(cell -> String.valueOf(cell.value)).collect(Collectors.joining());
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		final long count = grid.getAll().filter(c -> c.value == 'A')
				.filter(c -> findmas(grid, c))
				.count();
		System.out.println(count);
	}

	private boolean findmas(Grid<CharCell> grid, CharCell c) {
		String xmas = "MAS";
		if (((getValue(grid, c.row - 1, c.col - 1) == 'M' &&
			  getValue(grid, c.row + 1, c.col + 1) == 'S') ||
			 (getValue(grid, c.row - 1, c.col - 1) == 'S' &&
			  getValue(grid, c.row + 1, c.col + 1) == 'M')) &&

			((getValue(grid, c.row + 1, c.col - 1) == 'M' &&
			  getValue(grid, c.row - 1, c.col + 1) == 'S') ||
			 (getValue(grid, c.row + 1, c.col - 1) == 'S' &&
			  getValue(grid, c.row - 1, c.col + 1) == 'M'))) {
			return true;
		}
		return false;
	}

	private static char getValue(Grid<CharCell> grid, int row, int col) {
		return grid.getOptionalCell(row, col).orElse(CharCell.EMPTY).value;
	}
}
