
package se.askware.aoc2023.dec11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import se.askware.aoc2023.common.AocBase;
import se.askware.aoc2023.common.Cell;
import se.askware.aoc2023.common.CharCell;
import se.askware.aoc2023.common.Grid;
import se.askware.aoc2023.common.GridPos;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		solve(input, 1);
	}


	@Override
	public void solvePartTwo(List<String> input) {
		solve(input, 999_999);
	}

	private static void solve(List<String> input, int expansionPerEmptyLine) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		Expansion expansion = getExpansion(grid);

		debug(expansion.extraRows());
		debug(expansion.extraCols());

		final Map<CharCell, GridPos> galaxies = expandGrid(expansionPerEmptyLine, grid, expansion);

		final List<GridPos> todo = new ArrayList<>(galaxies.values());
		long sum = 0;
		while (!todo.isEmpty()){
			final GridPos galaxy = todo.remove(0);
			for (GridPos other : todo) {
				sum+= galaxy.distance(other);
			}
		}
		System.out.println(sum);
	}

	private static Map<CharCell, GridPos> expandGrid(int expansionPerEmptyLine, Grid<CharCell> grid, Expansion expansion) {
		final Map<CharCell, GridPos> galaxies = grid.getAll().filter(c -> c.matches('#'))
				.collect(Collectors.toMap(c -> c, Cell::pos));

		for (Integer r : expansion.extraRows()) {
			for (Map.Entry<CharCell, GridPos> galaxy : galaxies.entrySet()) {
				if (galaxy.getValue().getRow() > r){
					final GridPos translate = galaxy.getValue().translate(new GridPos(expansionPerEmptyLine, 0));
					galaxy.setValue(translate);

				}
			}
		}
		for (Integer r : expansion.extraCols()) {
			for (Map.Entry<CharCell, GridPos> galaxy : galaxies.entrySet()) {
				if (galaxy.getValue().getCol() > r){
					final GridPos translate = galaxy.getValue().translate(new GridPos(0, expansionPerEmptyLine));
					galaxy.setValue(translate);
				}
			}
		}
		return galaxies;
	}

	private static Expansion getExpansion(Grid<CharCell> grid) {
		List<Integer> extraRows = new ArrayList<>();
		List<Integer> extraCols = new ArrayList<>();
		for (int i = 0; i < grid.getNumRows(); i++) {
			if (grid.getRow(i).stream().allMatch(c -> c.matches('.'))){
				extraRows.add(0, i);
			}
		}
		for (int i = 0; i < grid.getNumColumns(); i++) {
			if (grid.getColumn(i).stream().allMatch(c -> c.matches('.'))){
				extraCols.add(0, i);
			}
		}
		return new Expansion(extraRows, extraCols);
	}

	private record Expansion(List<Integer> extraRows, List<Integer> extraCols) {
	}

}
