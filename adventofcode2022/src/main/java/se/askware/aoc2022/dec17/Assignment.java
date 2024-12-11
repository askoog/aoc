package se.askware.aoc2022.dec17;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import se.askware.aoc2022.common.AocBase;
import se.askware.aoc2022.common.CharCell;
import se.askware.aoc2022.common.Grid;
import se.askware.aoc2022.common.GridPos;
import se.askware.aoc2022.common.Pair;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment()
				.withRunMode(RunMode.PART2)
				.withLogLevel(LogLevel.INFO).run();
	}

	private class Rock {
		// upper left coord
		GridPos pos;
		Grid<CharCell> rock;

		public Rock(GridPos pos, Grid<CharCell> rock) {
			this.pos = pos;
			this.rock = rock;
		}

		public boolean tryMoveLeft(Grid<CharCell> grid) {
			return tryMove(grid, new GridPos(0, -1));
		}

		private boolean tryMove(Grid<CharCell> grid, GridPos p) {
			if (canMove(grid, p)) {
				pos = pos.translate(p);
				return true;
			} else {
				return false;
			}
		}

		public boolean tryMoveRight(Grid<CharCell> grid) {
			return tryMove(grid, new GridPos(0, 1));
		}

		public boolean tryMoveDown(Grid<CharCell> grid) {
			return tryMove(grid, new GridPos(1, 0));
		}

		private boolean canMove(Grid<CharCell> grid, GridPos move) {
			return rock.getAll().filter(c -> c.value == '#')
					.map(c -> grid.getOptionalCell(c.pos().translate(pos).translate(move)))
					.allMatch(c -> c.isPresent() && c.get().value == '.');
		}

		public void place(Grid<CharCell> grid) {
			rock.getAll().filter(c -> c.value == '#').forEach(c -> grid.getCell(c.pos().translate(pos)).value = c.value);
		}
	}

	public List<Grid<CharCell>> initRocks() {
		final Grid<CharCell> rock1 = Grid.init(List.of("####"), (p, c) -> new CharCell(c));
		final Grid<CharCell> rock2 = Grid.init(List.of(".#.", "###", ".#."), (p, c) -> new CharCell(c));
		final Grid<CharCell> rock3 = Grid.init(List.of("..#", "..#", "###"), (p, c) -> new CharCell(c));
		final Grid<CharCell> rock4 = Grid.init(List.of("#", "#", "#", "#"), (p, c) -> new CharCell(c));
		final Grid<CharCell> rock5 = Grid.init(List.of("##", "##"), (p, c) -> new CharCell(c));

		return List.of(rock1, rock2, rock3, rock4, rock5);
	}

	public int getBottom(Grid<CharCell> grid) {
		for (int i = grid.getNumRows() - 1; i >= 0; i--) {
			if (grid.getRow(i).stream().noneMatch(c -> c.value == '#')) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public void solvePartOne(List<String> input) {
		final List<Grid<CharCell>> rocks = initRocks();
		int rock = 0;
		int move = 0;
		Grid<CharCell> grid = Grid.charGrid(10000, 7, (x, y) -> '.');
		final String line = input.get(0);

		while (rock < 2022) {
			//System.out.println(getBottom(grid));

			final Grid<CharCell> rockGrid = rocks.get(rock % rocks.size());
			Rock r = new Rock(new GridPos(getBottom(grid) - 2 - rockGrid.getNumRows(), 2), rockGrid);

			//rockGrid.print();
			do {
				char dir = line.charAt(move % line.length());
				if (dir == '<') {
					final boolean b = r.tryMoveLeft(grid);
					debug("Left : " + b);
				} else if (dir == '>') {
					final boolean b = r.tryMoveRight(grid);
					debug("Right : " + b);
				} else {
					throw new RuntimeException("invalid dir " + dir);
				}
				move++;
			} while (r.tryMoveDown(grid));
			r.place(grid);

			rock++;
		}
		grid.print();
		System.out.println(grid.getNumRows() - getBottom(grid) - 1);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final List<Grid<CharCell>> rocks = initRocks();
		long rock = 0;
		int move = 0;
		Grid<CharCell> grid = Grid.charGrid(10000000, 7, (x, y) -> '.');
		final String line = input.get(0);
		List<Integer> wrapRows = new ArrayList<>();

		Map<String, Pair<Long>> seen = new HashMap<>();

		long cycleAddition = 0;
		final long max = 1_000_000_000_000L;
		while (rock < max) {
			//System.out.println(getBottom(grid));
			if (rock % 10_000 == 0){
				System.out.println(rock);
			}




			final Grid<CharCell> rockGrid = rocks.get((int)(rock % rocks.size()));
			Rock r = new Rock(new GridPos(getBottom(grid) - 2 - rockGrid.getNumRows(), 2), rockGrid);

			//rockGrid.print();
			do {
				char dir = line.charAt(move % line.length());
				if (dir == '<') {
					final boolean b = r.tryMoveLeft(grid);
					debug("Left : " + b);
				} else if (dir == '>') {
					final boolean b = r.tryMoveRight(grid);
					debug("Right : " + b);
				} else {
					throw new RuntimeException("invalid dir " + dir);
				}
				move++;
			} while (r.tryMoveDown(grid));
			r.place(grid);

			rock++;

			int bottom = getBottom(grid) + 1;

			StringBuilder sb = new StringBuilder(rock%rocks.size() + ":" + move%input.size() + ":");
			if (rock > 100 && cycleAddition == 0) {
				for (int i = 0; i < 20; i++) {
					sb.append(grid.getRow(i + bottom).stream().map(c -> String.valueOf(c.value)).collect(Collectors.joining()));
				}
				if (seen.containsKey(sb.toString())) {
					final Pair<Long> longPair = seen.get(sb.toString());
					final Long lastBottom = longPair.getFirst();
					long lastRocks = longPair.getSecond();
					final long cycleLength = lastBottom - bottom;
					//System.out.println("Cycle found " + cycleLength);
					if (cycleLength > 5000){
						long rockDiff = rock - lastRocks;
						long numCycles = (max - rock) / rockDiff;
						System.out.println(numCycles);
						cycleAddition = numCycles * cycleLength;
						System.out.println(cycleLength);
						System.out.println(cycleAddition);
						rock += (numCycles * rockDiff);
					}
				} else {
					seen.put(sb.toString(), new Pair<>((long)bottom, rock));
				}
			}
/*			String bottomRow = grid.getRow(bottom).stream().map(c -> String.valueOf(c.value)).collect(Collectors.joining());
			//System.out.println(bottomRow);
			boolean found = false;
			for (int i = bottom + 2 ; i < grid.getNumRows() ; i++) {
				String s2 = grid.getRow(i).stream().map(c -> String.valueOf(c.value)).collect(Collectors.joining());
				if (bottomRow.equals(s2)){
					found = true;
					//System.out.println(bottom + " = " + i + ": " + bottomRow);
					bottom++;
					bottomRow = grid.getRow(bottom).stream().map(c -> String.valueOf(c.value)).collect(Collectors.joining());
				} else {
					if (found && (bottom - getBottom(grid) > 10)){
						System.out.println("CYCLE " + (bottom - getBottom(grid)));
						break;
					}
				}
			}
			*/

			//System.out.println(rock);
		} //             1514285714288
		//grid.print(); 1000000004861
		System.out.println(grid.getNumRows() - getBottom(grid) - 1 + cycleAddition);
	}

}
