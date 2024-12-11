package se.askware.aoc2023.dec23;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import se.askware.aoc2023.common.AocBase;
import se.askware.aoc2023.common.CharCell;
import se.askware.aoc2023.common.Direction;
import se.askware.aoc2023.common.Grid;
import se.askware.aoc2023.common.GridPos;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.PART2).run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);

		final CharCell start = grid.getRow(0).stream().filter(c -> c.value == '.').findFirst().orElseThrow();
		final CharCell end = grid.getRow(grid.getNumRows() - 1).stream().filter(c -> c.value == '.').findFirst().orElseThrow();

		Queue<List<GridPos>> queue = new ArrayDeque<>();
		queue.add(List.of(start.pos()));
		long max = 0;
		Map<GridPos, Integer> seen = new HashMap<>();
		long count = 0;
		while (!queue.isEmpty()) {
			if (count++ % 10_000 == 0) {
				System.out.println(count + " " + queue.size());
			}
			//			System.out.println(queue.size());
			final List<GridPos> poll = queue.poll();
			final GridPos gridPos = poll.get(poll.size() - 1);
			final CharCell cell = grid.getCell(gridPos);
			if (cell.equals(end)) {
				max = Math.max(max, poll.size());
				System.out.println(max);
			} else {
				final Integer best = seen.getOrDefault(cell.pos(), 0);
				if (best < poll.size()) {
					seen.put(cell.pos(), poll.size());
					if (cell.value == '.') {
						grid.getXYNeighbors(gridPos).forEach(c -> {
							if (!poll.contains(c.pos())) {
								if (c.value != '#') {
									List<GridPos> newList = new ArrayList<>(poll);
									newList.add(c.pos());
									queue.add(newList);

								}
							}
						});
					} else if ("<>v^".indexOf(cell.value) >= 0) {
						final Direction direction = Direction.valueOf(cell.value);
						final GridPos move = gridPos.move(direction);
						if (!poll.contains(move)) {
							List<GridPos> newList = new ArrayList<>(poll);
							newList.add(move);
							seen.put(move, newList.size());
							queue.add(newList);
						}
					}
				}
			}
		}

		System.out.println(max - 1);

	}

	record State(GridPos pos, int length) {
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final int i = longestPath(input, false);
		System.out.println(i);
		if (1 == 1){
			return;
		}

		final Grid<CharCell> grid = Grid.charGrid(input);

		final CharCell start = grid.getRow(0).stream().filter(c -> c.value == '.').findFirst().orElseThrow();
		final CharCell end = grid.getRow(grid.getNumRows() - 1).stream().filter(c -> c.value == '.').findFirst().orElseThrow();

		final List<CharCell> crossRoads = grid.getAll()
				.filter(c -> c.value != '#' && grid.getXYNeighbors(c).stream()
						.filter(c2 -> c2.value != '#').count() > 2)
				.toList();
		crossRoads.forEach(c -> grid.getCell(c.pos()).value = 'X');
		grid.print();


		long max = 0;
		Map<GridPos, Integer> seen = new HashMap<>();
		List<GridPos> bestPath = null;

		List<CharCell> allCross = new ArrayList<>(crossRoads);
		allCross.add(end);
		allCross.add(start);

		List<CrossPath> paths = new ArrayList<>();

		for (CharCell crossRoad : allCross) {
			Queue<List<GridPos>> queue = new ArrayDeque<>();
			queue.add(List.of(crossRoad.pos()));
			long count = 0;

			while (!queue.isEmpty()) {
				if (count++ % 10_000 == 0) {
					System.out.println(count + " " + queue.size());
				}
				//			System.out.println(queue.size());
				final List<GridPos> poll = queue.poll();
				final GridPos gridPos = poll.get(poll.size() - 1);
				final CharCell cell = grid.getCell(gridPos);


				if (cell != crossRoad && allCross.contains(cell)) {

					final CrossPath bp = paths.stream().filter(p -> p.start == crossRoad && p.end == cell).findFirst().orElse(null);
					//if (bp != null && bp.length.size() < poll.size()){
					//	paths.remove(bp);
					//		paths.add(new CrossPath(crossRoad, cell, poll));
					//	} else {
					paths.add(new CrossPath(crossRoad, cell, new HashSet<>(poll)));
					//	}
					//if (poll.size() > max) {
					//	max = Math.max(max, poll.size());
					//	System.out.println(max);
					//		bestPath = new ArrayList<>(poll);
					//	}
				} else {
					final Integer best = seen.getOrDefault(cell.pos(), 0);
					//if (best < poll.size()) {
					seen.put(cell.pos(), poll.size());
					if (cell.value != '#') {
						grid.getXYNeighbors(gridPos).forEach(c -> {
							if (!poll.contains(c.pos())) {
								if (c.value != '#') {
									List<GridPos> newList = new ArrayList<>(poll);
									newList.add(c.pos());
									queue.add(newList);

								}
							}
						});
					} /*else if ("<>v^".indexOf(cell.value) >= 0) {
					final Direction direction = Direction.valueOf(cell.value);
					final GridPos move = gridPos.move(direction);
					if (!poll.contains(move)) {
						List<GridPos> newList = new ArrayList<>(poll);
						newList.add(move);
						seen.put(move, newList.size());
						queue.add(newList);
					}
				}*/
					//}
				}
			}
		}

		System.out.println(max - 1);

		for (CrossPath path : paths) {
			System.out.println(path.start + " " + path.end + " " + path.length.size());
			if (path.end == end){
				System.out.println("LAST ");
			}
		/*	Grid<CharCell> g2 = Grid.charGrid(input);
					path.length.forEach(c -> g2.getCell(c).value = 'O');
					g2.print();

			System.out.println();
		*/
		}

		List<CrossPath> queue = new ArrayList<>();
		queue.add(new CrossPath(start, start, new HashSet<>()));
		long count = 0;

		 max = 0;

		while (!queue.isEmpty()) {
			final CrossPath last = queue.remove(queue.size() - 1);

			if (count++ % 10_000 == 0) {
				System.out.println(count + " " + queue.size());
			//System.out.println(last.start + " " + last.end + " " + last.length.size());
			}
			//			System.out.println(queue.size());

			if (last.end == end) {
				if (max < last.length.size()){

				System.out.println(last.length.size());
				max = Math.max(max, last.length.size());
				}

			} else {

				paths.stream()
						.filter(p -> p.start == last.end || p.end == last.end)
						.forEach(n -> {
							//System.out.println("    ? " + n.start + " " + n.end);

							final List<GridPos> list = n.length.stream().filter(last.length::contains).toList();
							//System.out.println("      " + list);

							if (list.size() <= 1) {

								//System.out.println("    ! " + n.start + " " + n.end);
								Set<GridPos> p2 = new HashSet<>(last.length);
								p2.addAll(n.length);
								queue.add(new CrossPath(last.start,n.end, p2));
							}
							//System.out.println(n);
						});
			}
		}
		System.out.println(max);
	}

	record CrossPath(CharCell start, CharCell end, Set<GridPos> length) {
	}

	static Set<Point> visited = new HashSet<>();
	static Map<Point, Map<Point, Integer>> graphs = new HashMap<>();


	public static int longestPath(List<String> input, boolean slipperySlopes) {
		var start = new Point(1, 0);
		var end = new Point(input.get(0).length() - 2, input.size() - 1);
		var grid = input.stream().map(String::toCharArray).toArray(char[][]::new);
		var points = new HashSet<Point>();
		points.add(start);
		points.add(end);
		for (var y = 0; y < grid.length; y++) {
			for (var x = 0; x < grid[0].length; x++) {
				if (grid[y][x] != '#') {
					var numNeighbours = Stream.of(
									new Point(x + 1, y),
									new Point(x - 1, y),
									new Point(x, y + 1),
									new Point(x, y - 1))
							.filter(p -> p.x >= 0 && p.x < grid[0].length && p.y >= 0 && p.y < grid.length)
							.filter(p -> grid[p.y][p.x] != '#')
							.count();
					if (numNeighbours >= 3) {
						points.add(new Point(x, y));
					}
				}
			}
		}

		for (var point : points) {
			var stack = new ArrayDeque<Point>();
			var stackMap = new HashMap<Point, Integer>();
			var seen = new HashSet<Point>();
			stack.add(point);
			stackMap.put(point, 0);
			seen.add(point);
			while (!stack.isEmpty()) {
				var currentPoint = stack.pop();
				var currentDistance = stackMap.get(currentPoint);
				if (currentDistance != 0 && points.contains(currentPoint)) {
					graphs.computeIfAbsent(point, p -> new HashMap<>())
							.put(currentPoint, currentDistance);
					continue;
				}
				var neighbours = neighbours(List.of(currentPoint), grid, slipperySlopes)
						.stream()
						.filter(p -> !seen.contains(p))
						.toList();
				for (var neighbour : neighbours) {
					stack.add(neighbour);
					stackMap.put(neighbour, currentDistance + 1);
					seen.add(neighbour);
				}
			}
		}
		return dfs(start, end);
	}
12/24/2023 18:54:50 - INFO - Transfer from account 89100212 to account 17240084 was successful. Amount: 135.60


	static int dfs(Point p, Point end) {
		if (p.equals(end)) {
			return 0;
		}
		var max = Integer.MIN_VALUE;
		visited.add(p);
		for (var graph : graphs.get(p).entrySet()) {
			if (!visited.contains(graph.getKey())) {
				max = Math.max(max, graph.getValue() + dfs(graph.getKey(), end));
			}
		}
		visited.remove(p);
		return max;
	}

	static List<Point> neighbours(List<Point> path, char[][] grid, boolean slipperySlopes) {
		var p = path.get(path.size() - 1);
		List<Point> points;
		if (slipperySlopes) {
			points = switch (grid[p.y][p.x]) {
				case '>' -> List.of(new Point(p.x + 1, p.y));
				case '<' -> List.of(new Point(p.x - 1, p.y));
				case '^' -> List.of(new Point(p.x, p.y - 1));
				case 'v' -> List.of(new Point(p.x, p.y + 1));
				default -> List.of(
						new Point(p.x + 1, p.y),
						new Point(p.x - 1, p.y),
						new Point(p.x, p.y + 1),
						new Point(p.x, p.y - 1));
			};
		} else {
			points = List.of(
					new Point(p.x + 1, p.y),
					new Point(p.x - 1, p.y),
					new Point(p.x, p.y + 1),
					new Point(p.x, p.y - 1));
		}
		return points.stream()
				.filter(p2 -> p2.x >= 0 && p2.x < grid[0].length && p2.y >= 0 && p2.y < grid.length)
				.filter(p2 -> grid[p2.y][p2.x] != '#')
				.filter(p2 -> !path.contains(p2))
				.collect(Collectors.toList());
	}
}
