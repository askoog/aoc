package se.askware.aoc2024.dec10;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import se.askware.aoc2024.common.AocBase;
import se.askware.aoc2024.common.CharCell;
import se.askware.aoc2024.common.Grid;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		System.out.println(findPaths(input).distinct);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		System.out.println(findPaths(input).total);
	}

	record Path(List<CharCell> visited) {
		Path withNode(CharCell cell) {
			final ArrayList<CharCell> newPath = new ArrayList<>(visited);
			newPath.add(cell);
			return new Path(newPath);
		}
	}

	private static PathResult findPaths(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		final List<CharCell> start = grid.getAll().filter(c -> c.value == '0').toList();
		int total = 0;
		int distinct = 0;
		for (CharCell charCell : start) {
			Queue<Path> paths = new ArrayDeque<>();
			paths.add(new Path(List.of(charCell)));

			Set<CharCell> tops = new HashSet<>();
			while (!paths.isEmpty()) {
				final Path path = paths.poll();
				final CharCell last = path.visited().get(path.visited.size() - 1);
				if (last.value == '9') {
					if (tops.add(last)) {
						distinct++;
					}
					total++;
				} else {
					grid.getXYNeighbors(last).stream()
							.filter(c -> c.value == last.value + 1)
							.forEach(c -> paths.add(path.withNode(c)));
				}
			}
		}
		return new PathResult(total, distinct);
	}

	record PathResult(int total, int distinct) {
	}

}
