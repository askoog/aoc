package se.askware.aoc2023.dec14;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import se.askware.aoc2023.common.AocBase;
import se.askware.aoc2023.common.CharCell;
import se.askware.aoc2023.common.Grid;
import se.askware.aoc2023.common.GridPos;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.values()).run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		//grid.print();
		tiltGrid(grid, Direction.UP);
		//grid.print();

		final long sum =
				grid.getAll().filter(c -> c.value == 'O').mapToLong(c -> grid.getNumRows() - c.pos().getRow()).sum();
		System.out.println(sum);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		//grid.print();
		Map<String, Integer> seen = new HashMap<>();
		for (int i = 0; i < 1000_000_000	; i++) {

			tiltGrid(grid, Direction.UP);
			tiltGrid(grid, Direction.LEFT);
			tiltGrid(grid, Direction.DOWN);
			tiltGrid(grid, Direction.RIGHT);

			StringBuilder sb = new StringBuilder();
			grid.getAll().forEach(c -> sb.append(c.value));
			if (seen.containsKey(sb.toString())){
				final Integer old = seen.get(sb.toString());
				System.out.println("STABILIZED AFTER " + i + " " + old);
				int loopSize = i - old;
				while(i+loopSize < 1000_000_000){
					i+= loopSize;
				}
				System.out.println("skipped to "+ i);
			}else {
				seen.put(sb.toString(), i);
			}
			//grid.print();
			if (i % 1_000 == 0){
				System.out.println(i);
			}
		}

		final long sum =
				grid.getAll().filter(c -> c.value == 'O').mapToLong(c -> grid.getNumRows() - c.pos().getRow()).sum();
		System.out.println(sum);

	}

	enum Direction {UP, DOWN, LEFT, RIGHT}

	private void tiltGrid(Grid<CharCell> grid, Direction dir) {

		final GridPos translation = switch (dir) {
			case UP -> new GridPos(-1, 0);
			case DOWN -> new GridPos(1, 0);
			case LEFT -> new GridPos(0, -1);
			case RIGHT -> new GridPos(0, 1);
		};
		AtomicBoolean changed = new AtomicBoolean(true);
		while (changed.get()) {
			changed.set(false);
			grid.getAll().forEach(c -> {
				if (c.value == 'O') {
					final CharCell other = grid.getOptionalCell(c.pos().translate(translation)).orElse(null);
					if (other != null && other.value == '.') {
						c.value = '.';
						other.value = 'O';
						changed.set(true);
					}
				}
			});
		}

	}

}
