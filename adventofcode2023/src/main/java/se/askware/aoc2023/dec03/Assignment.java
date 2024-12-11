package se.askware.aoc2023.dec03;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import se.askware.aoc2023.common.AocBase;
import se.askware.aoc2023.common.CharCell;
import se.askware.aoc2023.common.Grid;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		Grid<CellNumber> grid = Grid.init(input, (p, v) -> new CellNumber(p.getFirst(), p.getSecond(), v));

		long sum = grid.getAll().filter(c1 -> c1.isStartOfNumber(grid))
				.filter(c -> c.isPartNumber(grid))
				.mapToLong(c -> c.getValue(grid))
				.sum();
		System.out.println(sum);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		Grid<CellNumber> grid = Grid.init(input, (p, v) -> new CellNumber(p.getFirst(), p.getSecond(), v));

		final List<CellNumber> gears = grid.getAll().filter(c -> c.value == '*').toList();

		List<CellNumber> numbers = grid.getAll()
				.filter(c1 -> c1.isStartOfNumber(grid))
				.toList();

		long sum = 0;
		for (CellNumber gear : gears) {
			List<CellNumber> adjacent = numbers.stream().filter(c ->
							c.getNumericCellNumbers(grid)
									.stream()
									.anyMatch(c2 -> grid.getAllNeighbors(c2.pos()).contains(gear)))
					.toList();
			if (adjacent.size() == 2) {
				final long reduce = adjacent.stream()
						.mapToLong(c -> c.getValue(grid))
						.reduce((l1, l2) -> l1 * l2)
						.orElse(0);
				sum += reduce;
			}
		}
		System.out.println(sum);

	}

	private static class CellNumber extends CharCell {

		public CellNumber(int row, int col, char value) {
			super(row, col, value);
		}

		public boolean isStartOfNumber(Grid<CellNumber> grid) {
			return Character.isDigit(value)
					&& (col == 0 || !Character.isDigit(grid.getCell(row, col - 1).value));
		}

		public boolean isDigit() {
			return Character.isDigit(value);
		}

		public boolean isPartNumber(Grid<CellNumber> grid) {
			return getNumericCellNumbers(grid).stream().anyMatch(c ->
					grid.getAllNeighbors(c).stream().anyMatch(c2 -> !Character.isDigit(c2.value) && c2.value != '.'));
		}

		public long getValue(Grid<CellNumber> grid) {
			if (isStartOfNumber(grid)) {
				List<CellNumber> vals = getNumericCellNumbers(grid);
				final String collect = vals.stream().map(cell -> "" + cell.value).collect(Collectors.joining());
				return Long.parseLong(collect);

			} else {
				return 0L;
			}
		}

		public List<CellNumber> getNumericCellNumbers(Grid<CellNumber> grid) {
			List<CellNumber> vals = new ArrayList<>();
			vals.add(this);
			CellNumber c = this;
			while ((c = grid.getOptionalCell(c.row, c.col + 1).orElse(null)) != null && c.isDigit()) {
				vals.add(c);
			}
			return vals;
		}
	}
}
