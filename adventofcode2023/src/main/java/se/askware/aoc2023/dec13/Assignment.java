package se.askware.aoc2023.dec13;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.askware.aoc2023.common.AocBase;
import se.askware.aoc2023.common.CharCell;
import se.askware.aoc2023.common.Grid;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.values()).run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		System.out.println(parseGrids(input).stream().mapToLong(l -> solve(l, 0)).sum());
	}

	@Override
	public void solvePartTwo(List<String> input) {
		System.out.println(parseGrids(input).stream().mapToLong(l -> solve(l, 1)).sum());
	}

	private static List<List<String>> parseGrids(List<String> input) {
		List<List<String>> grids = new ArrayList<>();
		List<String> gridInput = new ArrayList<>();
		for (String s : input) {
			if (s.isEmpty()) {
				grids.add(gridInput);
				gridInput = new ArrayList<>();
			} else {
				gridInput.add(s);
			}
		}
		grids.add(gridInput);
		return grids;
	}

	private int solve(List<String> input, int allowedErrors) {
		if (input.isEmpty()) {
			return 0;
		}
		final Grid<CharCell> grid = Grid.charGrid(input);
		for (int i = 0; i < grid.getNumColumns() - 1; i++) {
			int errors = 0;
			for (int j = i, k = i + 1; j >= 0 && k < grid.getNumColumns(); j--, k++) {
				errors += distance(grid.getColumn(j), grid.getColumn(k));
			}
			if (errors == allowedErrors) {
				return i + 1;
			}
		}

		for (int i = 0; i < grid.getNumRows() - 1; i++) {
			int errors = 0;
			for (int j = i, k = i + 1; j >= 0 && k < grid.getNumRows(); j--, k++) {
				errors += distance(grid.getRow(j), grid.getRow(k));
			}
			if (errors == allowedErrors) {
				return (i + 1) * 100;
			}
		}
		return 0;
	}

	private int distance(List<CharCell> r1, List<CharCell> r2) {
		int errors = 0;
		for (int i = 0; i < r1.size(); i++) {
			if (r1.get(i).value != r2.get(i).value) {
				errors++;
			}
		}
		return errors;
	}


}
