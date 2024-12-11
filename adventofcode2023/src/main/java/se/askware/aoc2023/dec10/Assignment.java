package se.askware.aoc2023.dec10;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import se.askware.aoc2023.common.AocBase;
import se.askware.aoc2023.common.CharCell;
import se.askware.aoc2023.common.Direction;
import se.askware.aoc2023.common.Grid;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.EXAMPLE2).run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);

		final CharCell startCell = grid.getAll().filter(c -> c.matches('S')).findAny().orElseThrow();

		System.out.println(startCell);
		for (Direction d : Direction.values()) {
			System.out.println(d + " " + getConnectedCell(startCell, d, grid));
		}
		List<List<CharCell>> paths = new ArrayList<>();
		paths.add(new ArrayList<>(List.of(startCell)));
		List<List<CharCell>> loops = new ArrayList<>();
		while (!paths.isEmpty()) {
			final List<CharCell> p = paths.remove(0);
			debug(p);
			final CharCell c = p.get(p.size() - 1);
			for (Direction d : Direction.values()) {
				if (hasExit(c, d)) {
					getConnectedCell(c, d, grid)
							.filter(n -> p.size() < 2 || n != p.get(p.size() - 2))
							.ifPresent(n -> {
								List<CharCell> newPath = new ArrayList<>(p);
								newPath.add(n);
								if (n == startCell) {
									loops.add(newPath);
								} else {
									paths.add(newPath);
								}
							});
				}
			}
		}

		debug(loops);
		System.out.println((loops.get(0).size() - 1) / 2);

		Set<CharCell> loop = new HashSet<>(loops.get(0));

	}

	@Override
	public void solvePartTwo(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);

		final CharCell startCell = grid.getAll().filter(c -> c.matches('S')).findAny().orElseThrow();

		System.out.println(startCell);
		for (Direction d : Direction.values()) {
			System.out.println(d + " " + getConnectedCell(startCell, d, grid));
		}
		List<List<CharCell>> paths = new ArrayList<>();
		paths.add(new ArrayList<>(List.of(startCell)));
		List<List<CharCell>> loops = new ArrayList<>();
		while (!paths.isEmpty()) {
			final List<CharCell> p = paths.remove(0);
			debug(p);
			final CharCell c = p.get(p.size() - 1);
			for (Direction d : Direction.values()) {
				if (hasExit(c, d)) {
					getConnectedCell(c, d, grid)
							.filter(n -> p.size() < 2 || n != p.get(p.size() - 2))
							.ifPresent(n -> {
								List<CharCell> newPath = new ArrayList<>(p);
								newPath.add(n);
								if (n == startCell) {
									loops.add(newPath);
								} else {
									paths.add(newPath);
								}
							});
				}
			}
		}

		debug(loops);
		System.out.println((loops.get(0).size() - 1) / 2);

		Set<CharCell> loop = new HashSet<>(loops.get(0));

		// part 2

		// Expand map to 3x3 grid

		grid.getAll().forEach(c -> {
			if (!loop.contains(c)) {
				c.value = '.';
			}
		});

		Map<Character, String[]> expandMap = Map.of(
				'-', new String[] { "...", "###", "..." },
				'|', new String[] { ".#.", ".#.", ".#." },
				'F', new String[] { "...", ".##", ".#." },
				'L', new String[] { ".#.", ".##", "..." },
				'7', new String[] { "...", "##.", ".#." },
				'J', new String[] { ".#.", "##.", "..." },
				'.', new String[] { "...", "...", "..." },
				'S', new String[] { ".#.", "###", ".#." });

		Grid<CharCell> g2 = new Grid<>(grid.getNumRows() * 3, grid.getNumColumns() * 3);
		for (int row = 0; row < grid.getNumRows(); row++) {
			for (int column = 0; column < grid.getNumColumns(); column++) {
				final String[] strings = expandMap.get(grid.getCell(row, column).value);

				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						g2.setCell(new CharCell(strings[i].charAt(j)), (row * 3) + i, (column * 3) + j);
					}
				}
			}
		}
		g2.print();

		// Flood fill
		Queue<CharCell> queue = new ArrayDeque<>();
		queue.add(g2.getCell(0, 0));
		Set<CharCell> done = new HashSet<>();
		while (!queue.isEmpty()) {
			final CharCell c = queue.poll();
			done.add(c);
			if (c.value == '.') {
				c.value = ' ';
				g2.getAllNeighbors(c).stream()
						.filter(c1 -> !done.contains(c1))
						.forEach(queue::add);
			}
		}
		g2.print();

		// Find 3x3 empty spaces
		long count = 0;
		for (int row = 0; row < g2.getNumRows(); row+=3) {
			for (int column = 0; column < g2.getNumColumns(); column += 3) {
				boolean all = true;
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						if (g2.getCell(row + i, column + j).value != '.'){
							all = false;
						}
					}
				}
				if (all){
					count++;
				}
			}
		}
		System.out.println(count);
	}

	private Optional<CharCell> getConnectedCell(CharCell cell, Direction direction, Grid<CharCell> grid) {
		return grid.getOptionalCell(cell.pos().translate(direction.getPos()))
				.filter(c -> hasExit(c, direction.opposite()));

	}

	private boolean hasExit(CharCell cell, Direction dir) {
		switch (dir) {
			case UP:
				return cell.matches('|', 'J', 'L', 'S');
			case RIGHT:
				return cell.matches('-', 'L', 'F', 'S');
			case DOWN:
				return cell.matches('|', '7', 'F', 'S');
			case LEFT:
				return cell.matches('-', 'J', '7', 'S');
			default:
				return false;
		}
	}

}
