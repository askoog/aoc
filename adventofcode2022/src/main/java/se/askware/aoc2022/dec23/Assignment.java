package se.askware.aoc2022.dec23;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import se.askware.aoc2022.common.AocBase;
import se.askware.aoc2022.common.CharCell;
import se.askware.aoc2022.common.Grid;
import se.askware.aoc2022.common.GridPos;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	enum Dir {
		N, S, W, E
	}

	static Map<Dir, List<GridPos>> translations = Map.of(
			Dir.N, List.of(new GridPos(-1, 0), new GridPos(-1, 1), new GridPos(-1, -1)),
			Dir.S, List.of(new GridPos(1, 0), new GridPos(1, 1), new GridPos(1, -1)),
			Dir.E, List.of(new GridPos(0, 1), new GridPos(-1, 1), new GridPos(1, 1)),
			Dir.W, List.of(new GridPos(0, -1), new GridPos(-1, -1), new GridPos(1, -1))
	);

	private static class Elve {
		GridPos pos;

		public Elve(GridPos pos) {
			this.pos = pos;
		}

		public Optional<GridPos> proposeMove(Grid<CharCell> grid, List<Dir> dirs) {
			if (grid.getAllNeighbors(pos).stream().allMatch(c -> c.value == '.')) {
				return Optional.empty();
			}
			for (Dir dir : dirs) {
				List<GridPos> trans = translations.get(dir);
				if (trans.stream().allMatch(p -> grid.getCell(pos.translate(p)).value == '.')) {
					return Optional.of(pos.translate(trans.get(0)));
				}
			}
			//System.out.println(pos + " unable to move ...");
			return Optional.empty();
		}

		@Override
		public String toString() {
			return "Elve{" +
					"pos=" + pos +
					'}';
		}
	}

	@Override
	public void solvePartOne(List<String> input) {
		int rows = input.size();
		int cols = input.get(0).length();
		int expansion = 10;
		List<Elve> elves = new ArrayList<>();
		Grid<CharCell> grid = Grid.charGrid(rows + expansion, cols + expansion, (row, col) -> '.');
		for (int row = 0; row < input.size(); row++) {
			String s = input.get(row);
			for (int col = 0; col < s.length(); col++) {
				final CharCell cell = grid.getCell(row + expansion / 2, col + expansion / 2);
				cell.value = s.charAt(col);
				if (s.charAt(col) == '#') {
					elves.add(new Elve(cell.pos()));
				}
			}
		}

		List<Dir> dirs = new ArrayList<>(Arrays.asList(Dir.values()));

		int numIterations = 10;
		for (int i = 0; i < numIterations; i++) {

			//grid.print();

			Map<Elve, GridPos> moves = new HashMap<>();
			for (Elve elf : elves) {
				final Optional<GridPos> gridPos = elf.proposeMove(grid, dirs);
				gridPos.ifPresent(m -> moves.put(elf, m));
			}

			final Collection<GridPos> values = moves.values();

			for (Map.Entry<Elve, GridPos> entry : moves.entrySet()) {
				if (values.stream().filter(e -> e.equals(entry.getValue())).count() > 1) {
				//	System.out.println("no move for " + entry.getKey());
				} else {
				//	System.out.println("move " + entry.getKey() + " to " + entry.getValue());
					grid.getCell(entry.getKey().pos).value = '.';
					entry.getKey().pos = entry.getValue();
					grid.getCell(entry.getKey().pos).value = '#';
				}
			}

			grid.print();
			final Dir d = dirs.remove(0);
			dirs.add(d);
		}

		final int rowMin = elves.stream().mapToInt(c -> c.pos.getRow()).min().getAsInt();
		final int rowMax = elves.stream().mapToInt(c -> c.pos.getRow()).max().getAsInt();
		final int colMin = elves.stream().mapToInt(c -> c.pos.getCol()).min().getAsInt();
		final int colMax = elves.stream().mapToInt(c -> c.pos.getCol()).max().getAsInt();
		int count = 0;
		for (int row = rowMin ; row <= rowMax; row++) {
			for (int col = colMin; col <= colMax; col++) {
				count += grid.getCell(row,col).value == '.'	? 1:0;
			}
		}
		System.out.println(count);

	}

	@Override
	public void solvePartTwo(List<String> input) {
		int rows = input.size();
		int cols = input.get(0).length();
		int expansion = 1000;
		List<Elve> elves = new ArrayList<>();
		Grid<CharCell> grid = Grid.charGrid(rows + expansion, cols + expansion, (row, col) -> '.');
		for (int row = 0; row < input.size(); row++) {
			String s = input.get(row);
			for (int col = 0; col < s.length(); col++) {
				final CharCell cell = grid.getCell(row + expansion / 2, col + expansion / 2);
				cell.value = s.charAt(col);
				if (s.charAt(col) == '#') {
					elves.add(new Elve(cell.pos()));
				}
			}
		}

		List<Dir> dirs = new ArrayList<>(Arrays.asList(Dir.values()));

		int numIterations = 1000;
		for (int i = 1; i < numIterations; i++) {

	//		grid.print();

			Map<Elve, GridPos> moves = new HashMap<>();
			for (Elve elf : elves) {
				final Optional<GridPos> gridPos = elf.proposeMove(grid, dirs);
				gridPos.ifPresent(m -> moves.put(elf, m));
			}

			final Collection<GridPos> values = moves.values();
			if (moves.isEmpty()){
				System.out.println(i);
				return;
			}
			for (Map.Entry<Elve, GridPos> entry : moves.entrySet()) {
				if (values.stream().filter(e -> e.equals(entry.getValue())).count() > 1) {
					//System.out.println("no move for " + entry.getKey());
				} else {
					//System.out.println("move " + entry.getKey() + " to " + entry.getValue());
					grid.getCell(entry.getKey().pos).value = '.';
					entry.getKey().pos = entry.getValue();
					grid.getCell(entry.getKey().pos).value = '#';
				}
			}

			//grid.print();
			final Dir d = dirs.remove(0);
			dirs.add(d);
		}

		System.out.println("no match");
	}

}
