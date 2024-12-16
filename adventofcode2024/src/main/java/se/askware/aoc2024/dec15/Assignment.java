package se.askware.aoc2024.dec15;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

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
		solve(input, false);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		solve(input, true);
	}

	private static void solve(List<String> input, boolean expandGrid) {
		final ParsedInput parsedInput = getParsedInput(input, expandGrid);

		final Grid<CharCell> grid = Grid.charGrid(parsedInput.gridInput);
		Robot robot = grid.getAll().filter(c -> c.value == '@').map(c -> new Robot(c.pos())).findFirst().orElseThrow();

		for (Direction dir : parsedInput.moves) {
			robot = robot.move(grid, dir);
		}
		//grid.print();

		System.out.println(calculateGPS(grid));
	}

	private static ParsedInput getParsedInput(List<String> input, boolean expandGrid) {
		List<String> gridInput = new ArrayList<>();
		List<String> moves = new ArrayList<>();
		List<String> current = gridInput;
		for (String string : input) {
			if (string.isEmpty()) {
				current = moves;
			} else {
				current.add(string);
			}
		}
		if (expandGrid) {
			gridInput = gridInput.stream().map(s -> s.chars().mapToObj(c -> switch (c) {
				case '#' -> "##";
				case '.' -> "..";
				case 'O' -> "[]";
				case '@' -> "@.";
				default -> throw new IllegalArgumentException("Unexpected value: " + (char) c);
			}).collect(Collectors.joining())).toList();
		}
		final List<Direction> moveDirs = moves.stream().flatMap(s -> s.chars().mapToObj(c -> (char) c)).map(Direction::valueOf).toList();

		return new ParsedInput(gridInput, moveDirs);
	}

	private record ParsedInput(List<String> gridInput, List<Direction> moves) {
	}

	private static AtomicLong calculateGPS(Grid<CharCell> grid) {
		AtomicLong sum = new AtomicLong();
		for (int i = 0; i < grid.getNumRows(); i++) {
			int row = i;
			grid.getRow(row).stream().filter(c -> c.value == 'O' || c.value == '[')
					.forEach(c -> sum.addAndGet(c.col + (100L * row)));
		}
		return sum;
	}

	record Robot(GridPos pos) {

		public Robot move(Grid<CharCell> grid, Direction dir) {
			final CharCell charCell = grid.getOptionalCell(pos.move(dir)).orElseThrow();

			if (canMove(grid, pos, dir)) {
				doMove(grid, pos, dir);
				grid.getCell(pos).value = '.';
				charCell.value = '@';
				return new Robot(charCell.pos());
			} else {
				return this;
			}
		}

		boolean canMove(Grid<CharCell> grid, GridPos pos, Direction dir) {
			final CharCell neighbor = grid.getOptionalCell(pos, dir).orElse(null);
			if (neighbor == null) {
				return false;
			} else if (neighbor.value == 'O') {
				return canMove(grid, neighbor.pos(), dir);
			} else if (neighbor.value == '.') {
				return true;
			} else if (neighbor.value == '[') {
				if (dir == Direction.UP || dir == Direction.DOWN) {
					return canMove(grid, neighbor.pos(), dir) && canMove(grid, neighbor.pos().neighbor(Direction.RIGHT), dir);
				} else {
					return canMove(grid, neighbor.pos().neighbor(Direction.RIGHT), dir);
				}
			} else if (neighbor.value == ']') {
				if (dir == Direction.UP || dir == Direction.DOWN) {
					return canMove(grid, neighbor.pos(), dir) && canMove(grid, neighbor.pos().neighbor(Direction.LEFT), dir);
				} else {
					return canMove(grid, neighbor.pos().neighbor(Direction.LEFT), dir);
				}
			} else {
				return false;
			}
		}

		void doMove(Grid<CharCell> grid, GridPos pos, Direction dir) {
			final CharCell neighbor = grid.getOptionalCell(pos, dir).orElseThrow();
			if (neighbor.value == 'O') {
				doMove(grid, neighbor.pos(), dir);
			} else if (neighbor.value == '[') {
				doMove(grid, neighbor.pos().neighbor(Direction.RIGHT), dir);
				doMove(grid, neighbor.pos(), dir);
			} else if (neighbor.value == ']') {
				doMove(grid, neighbor.pos().neighbor(Direction.LEFT), dir);
				doMove(grid, neighbor.pos(), dir);
			}
			final CharCell cell = grid.getCell(pos);
			neighbor.value = cell.value;
			cell.value = '.';
		}

	}

}

