package se.askware.aoc2024.dec12;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import se.askware.aoc2024.common.AocBase;
import se.askware.aoc2024.common.CharCell;
import se.askware.aoc2024.common.Direction;
import se.askware.aoc2024.common.Grid;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);

		final Map<Character, List<Region>> regions = parseRegions(grid);
		System.out.println(regions.keySet());
		AtomicLong sum = new AtomicLong();
		regions.forEach((key, value) -> value.forEach(region -> {
			//System.out.println(r.getKey() + " : " + region.size() + "," + region.perimeter(grid));
			sum.addAndGet(region.size() * region.perimeter(grid));
		}));
		System.out.println(sum);
	}

	private static Map<Character, List<Region>> parseRegions(Grid<CharCell> grid) {
		Map<Character, List<Region>> regions = new HashMap<>();
		Set<CharCell> tagged = new HashSet<>();
		grid.getAll().forEach(c -> {
			if (tagged.add(c)) {
				final Region region = new Region(new ArrayList<>(List.of(c)));
				regions.computeIfAbsent(c.value, k -> new ArrayList<>()).add(region);
				Queue<CharCell> queue = new ArrayDeque<>(grid.getXYNeighbors(c));
				while (!queue.isEmpty()) {
					final CharCell poll = queue.poll();

					if (poll.value == c.value && tagged.add(poll)) {
						region.add(poll);
						queue.addAll(grid.getXYNeighbors(poll));

					}
				}
			}
		});
		return regions;
	}

	record Region(List<CharCell> cells) {
		void add(CharCell cell) {
			cells.add(cell);
		}

		int size() {
			return cells.size();
		}

		long perimeter(Grid<CharCell> grid) {
			long perimeter = 0;
			for (CharCell cell : cells) {
				final long numNeighbors = grid.getXYNeighbors(cell).stream().filter(n -> n.value == cell.value).count();
				perimeter += 4 - numNeighbors;
			}
			return perimeter;
		}

		long sides(Grid<CharCell> grid) {
			long sideCount = 0;
			for (int i = 0; i < grid.getNumRows(); i++) {
				sideCount += countSidesYWay(grid, i, Direction.UP);
			}
			for (int i = grid.getNumRows()-1; i >= 0; i--) {
				sideCount += countSidesYWay(grid, i, Direction.DOWN);
			}
			for (int i = 0; i < grid.getNumColumns(); i++) {
				sideCount += countSidesXWay(grid, i, Direction.LEFT);
			}
			for (int i = grid.getNumColumns()-1; i >= 0; i--) {
				sideCount += countSidesXWay(grid, i, Direction.RIGHT);
			}
			return sideCount;
		}

		private long countSidesYWay(Grid<CharCell> grid, int row, Direction dir) {
			AtomicInteger lastEnd = new AtomicInteger(Integer.MIN_VALUE);
			AtomicLong sideCount = new AtomicLong();
			cells.stream()
					.filter(c -> c.row == row)
					// Do not count if a bordering cell in the given direction is part of the same region
					.filter(c -> grid.getOptionalCell(c.pos().translate(dir.getPos()))
							.map(cc -> cc.value != c.value)
							.orElse(true))
					.sorted(Comparator.comparingInt(c -> c.col))
					.forEach(c -> {
						// Is this a new side or part of the current one?
						if (lastEnd.get() + 1 < c.col) {
							sideCount.incrementAndGet();
						}
						lastEnd.set(c.col);
					});
			return sideCount.get();
		}

		private long countSidesXWay(Grid<CharCell> grid, int col, Direction dir) {
			AtomicInteger lastEnd = new AtomicInteger(Integer.MIN_VALUE);
			AtomicLong sideCount = new AtomicLong();
			cells.stream()
					.filter(c -> c.col == col)
					.filter(c -> grid.getOptionalCell(c.pos().translate(dir.getPos()))
							.map(cc -> cc.value != c.value)
							.orElse(true))
					.sorted(Comparator.comparingInt(c -> c.row))
					.forEach(c -> {
						if (lastEnd.get() + 1 < c.row) {
							sideCount.incrementAndGet();
						}
						lastEnd.set(c.row);
					});
			return sideCount.get();
		}
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		final Map<Character, List<Region>> regions = parseRegions(grid);
		AtomicLong sum = new AtomicLong();
		regions.forEach((key, value) ->
				value.forEach(region ->
						sum.addAndGet(region.sides(grid) * region.size())));
		System.out.println(sum);
	}

}
