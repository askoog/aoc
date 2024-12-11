package se.askware.aoc2022.dec14;

import java.io.IOException;
import java.util.List;

import se.askware.aoc2022.common.AocBase;
import se.askware.aoc2022.common.Cell;
import se.askware.aoc2022.common.CharCell;
import se.askware.aoc2022.common.Grid;
import se.askware.aoc2022.common.Pair;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Grid<CharCell> grid = parseGrid(input);

		int iterations = 0;
		CharCell initial = grid.getCell(0, 500);
		while (true) {
			CharCell next = move(initial, grid);
			CharCell last = initial;
			while (next != last) {
				last = next;
				next = move(next, grid);
			}
			if (next.row + 1 == grid.getNumRows()) {
				break;
			}
			next.value = 'o';
			iterations++;

		}

		//grid.print();

		System.out.println(iterations);
	}

	private  Grid<CharCell> parseGrid(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(1000, 1000, (x, y) -> '.');
		for (String s : input) {
			final String[] split = s.split(" -> ");
			Pair<Integer> last = Pair.valueOf(split[0], ',').map(Integer::parseInt);
			for (int i = 1; i < split.length; i++) {
				final Pair<Integer> cur = Pair.valueOf(split[i], ',').map(Integer::parseInt);
				debug(last + " -> " + cur);
				if (last.getFirst().intValue() == cur.getFirst().intValue()) {
					for (int j = Math.min(last.getSecond(), cur.getSecond()); j <= Math.max(last.getSecond(), cur.getSecond()); j++) {
						debug(last.getFirst() + ":" + j);
						grid.getCell(j, last.getFirst()).value = '#';
					}
				} else {
					for (int j = Math.min(last.getFirst(), cur.getFirst()); j <= Math.max(last.getFirst(), cur.getFirst()); j++) {
						debug(j + ":" + last.getSecond());
						grid.getCell(last.getSecond(), j).value = '#';
					}
				}
				last = cur;
			}
		}
		return grid;
	}

	public CharCell move(CharCell pos, Grid<CharCell> grid) {
		if (pos.row + 1 == grid.getNumRows() || pos.col == 0 || pos.col == grid.getNumColumns()) {
			return pos;
		}
		CharCell next = grid.getCell(pos.row + 1, pos.col);
		if (next.matches('#', 'o')) {
			next = grid.getCell(pos.row + 1, pos.col - 1);
			if (next.matches('#', 'o')) {
				next = grid.getCell(pos.row + 1, pos.col + 1);
				if (next.matches('#', 'o')) {
					next = pos;
				}
			}
		}
		return next;
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final Grid<CharCell> grid = parseGrid(input);

		int maxRow = grid.getAll().filter(c -> c.value == '#').mapToInt(c -> c.row).max().getAsInt();

		grid.getRow(maxRow + 2).forEach(c -> c.value = '#');
		int iterations = 0;
		CharCell initial = grid.getCell(0, 500);
		while(true){

			CharCell next = move(initial, grid);
			CharCell last = initial;
			while(next != last){
				last = next;
				next = move(next, grid);
			}
			if (next.row + 1 == grid.getNumRows()) {
				break;
			}
			if (next == initial) {
				break;
			}
			next.value = 'o';
			iterations++;

		}
		System.out.println(iterations + 1);
	}

}
