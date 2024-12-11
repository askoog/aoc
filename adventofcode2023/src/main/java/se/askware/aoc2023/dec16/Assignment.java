package se.askware.aoc2023.dec16;

import static se.askware.aoc2023.common.Direction.DOWN;
import static se.askware.aoc2023.common.Direction.LEFT;
import static se.askware.aoc2023.common.Direction.RIGHT;
import static se.askware.aoc2023.common.Direction.UP;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import se.askware.aoc2023.common.AocBase;
import se.askware.aoc2023.common.CharCell;
import se.askware.aoc2023.common.Direction;
import se.askware.aoc2023.common.Grid;
import se.askware.aoc2023.common.GridPos;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.values()).run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		Set<GridPos> energized = energize(Grid.charGrid(input), new Beam(new GridPos(0, -1), RIGHT));
		System.out.println(energized.size());
	}

	private static Set<GridPos> energize(Grid<CharCell> grid, Beam start) {
		Queue<Beam> beams = new ArrayDeque<>();
		beams.add(start);
		Set<GridPos> energized = new HashSet<>();
		Set<Beam> memento = new HashSet<>();
		while (!beams.isEmpty()) {
			final Beam beam = beams.poll();
			energized.add(beam.pos);
			if (!memento.add(beam)) {
				continue;
			}
			grid.getOptionalCell(beam.pos.move(beam.direction)).ifPresent(c -> {
				Direction d = beam.direction;
				if (c.value == '.') {
					beams.add(new Beam(c.pos(), beam.direction));
				} else if (c.value == '\\') {
					d = switch (d) {
						case RIGHT -> DOWN;
						case LEFT -> UP;
						case UP -> LEFT;
						case DOWN -> RIGHT;
					};
					beams.add(new Beam(c.pos(), d));
				} else if (c.value == '/') {
					d = switch (d) {
						case LEFT -> DOWN;
						case RIGHT -> UP;
						case DOWN -> LEFT;
						case UP -> RIGHT;
					};
					beams.add(new Beam(c.pos(), d));
				} else if (c.value == '|') {
					if (d == LEFT || d == RIGHT) {
						beams.add(new Beam(c.pos(), DOWN));
						beams.add(new Beam(c.pos(), UP));
					} else {
						beams.add(new Beam(c.pos(), d));
					}
				} else if (c.value == '-') {
					if (d == UP || d == DOWN) {
						beams.add(new Beam(c.pos(), LEFT));
						beams.add(new Beam(c.pos(), RIGHT));
					} else {
						beams.add(new Beam(c.pos(), d));
					}
				}
			});
		}
		energized.remove(start.pos);
		return energized;
	}

	record Beam(GridPos pos, Direction direction) {
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);

		long max = 0;
		for (int i = 0; i < grid.getNumRows(); i++) {
			max = Math.max(energize(grid, new Beam(new GridPos(i, -1), RIGHT)).size(), max);
			max = Math.max(energize(grid, new Beam(new GridPos(i, grid.getNumColumns()), LEFT)).size(), max);
		}
		for (int i = 0; i < grid.getNumColumns(); i++) {
			max = Math.max(energize(grid, new Beam(new GridPos(-1, i), DOWN)).size(), max);
			max = Math.max(energize(grid, new Beam(new GridPos(grid.getNumRows(), i), UP)).size(), max);
		}

		System.out.println(max);
	}

}
