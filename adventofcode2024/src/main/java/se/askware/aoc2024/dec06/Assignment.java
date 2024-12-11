package se.askware.aoc2024.dec06;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.askware.aoc2024.common.AocBase;
import se.askware.aoc2024.common.CharCell;
import se.askware.aoc2024.common.Direction;
import se.askware.aoc2024.common.Grid;
import se.askware.aoc2024.common.GridPos;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		CharCell guard = grid.findFirst(c -> c.getValue() == '^').orElseThrow();
		traverse(grid, guard);
		grid.print();
		System.out.println(grid.getAll().filter(c -> c.value == 'X').count());
	}

	private static boolean traverse(Grid<CharCell> grid, CharCell guard) {
		Direction dir = Direction.UP;
		Set<Seen> seen = new HashSet<>();
		while (true) {
			grid.getCell(guard.pos()).value = 'X';
			Seen s = new Seen(guard.pos(), dir);
			if (!seen.add(s)) {
				return false;
			}
			CharCell cell = grid.getOptionalCell(guard.pos(), dir).orElse(null);
			if (cell == null) {
				break;
			} else if (cell.getValue() == '#') {
				dir = dir.turn(Direction.RIGHT);
			} else {
				guard = cell;
			}
		}
		return true;
	}

	record Seen(GridPos pos, Direction dir){
	}

	@Override
	public void solvePartTwo(List<String> input) {
		Grid<CharCell> grid = Grid.charGrid(input);

		CharCell guard = grid.findFirst(c -> c.getValue() == '^').orElseThrow();
		traverse(grid, guard);
		int count = 0;
		final List<CharCell> list = grid.getAll().filter(c -> c.value == 'X').toList();
		for (CharCell charCell : list) {
			if (charCell.pos().equals(guard.pos())) {
				continue;
			}
			grid = Grid.charGrid(input);
			grid.getCell(charCell.pos()).value = '#';
			if (!traverse(grid, guard)){
				count++;
			}

		}
		System.out.println(count);

	}

}
