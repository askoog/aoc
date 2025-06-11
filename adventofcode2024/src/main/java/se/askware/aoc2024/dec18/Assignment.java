package se.askware.aoc2024.dec18;

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
import se.askware.aoc2024.common.GridPos;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solveExampleOne(List<String> input) {
		solvePartOne(input, 7, 12);
	}

	private static void solvePartOne(List<String> input, int size, int numSteps) {
		final List<GridPos> path = solve(input, size, numSteps);
		System.out.println("Found path: " + path);
		System.out.println(path.size() - 1);
	}

	private static List<GridPos> solve(List<String> input, int size, int numSteps) {
		final Grid<CharCell> grid = Grid.charGrid(size, size, (x, y) -> '.');

		for (int i = 0; i < numSteps && i < input.size(); i++) {
			final GridPos pos = GridPos.parse(input.get(i));
			grid.getCell(pos).value = '#';
		}
		//grid.print();

		final GridPos endPos = new GridPos(size - 1, size - 1);
		final Path path = new Path(List.of(new GridPos(0, 0)));
		Queue<Path> queue = new ArrayDeque<>();
		Set<GridPos> visited = new HashSet<>();
		queue.add(path);
		while (!queue.isEmpty()) {
			final Path poll = queue.poll();
			final GridPos currentPos = poll.path.get(poll.path.size() - 1);
			if (currentPos.equals(endPos)) {
				poll.path.forEach(c -> grid.getCell(c).value = 'O');
				//grid.print();
				return poll.path;
			}
			grid.getXYNeighbors(currentPos).stream().map(CharCell::pos).forEach(c -> {
				if (grid.getCell(c).value == '.' && !visited.contains(c)) {
					queue.add(poll.withPath(c));
					visited.add(c);
				}
			});

		}
		throw new IllegalArgumentException("No path found");
	}

	record Path(List<GridPos> path) {
		Path withPath(GridPos pos) {
			final List<GridPos> newPath = new ArrayList<>(path);
			newPath.add(pos);
			return new Path(newPath);
		}
	}

	@Override
	public void solvePartOne(List<String> input) {
		solvePartOne(input, 71, 1024);
	}

	@Override
	public void solveExampleTwo(List<String> input) {
		solvePartTwo(input, 1, 7);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		solvePartTwo(input, 1024, 71);
	}

	private static void solvePartTwo(List<String> input, int initialOffset, int size) {
		List<GridPos> solve = solve(input, size, initialOffset);
		for (int i = initialOffset; i < input.size(); i++) {
			final GridPos pos = GridPos.parse(input.get(i-1));
			if (solve.contains(pos)){
				try {
					solve = solve(input, size, i);
				} catch (Exception e) {
					System.out.println(pos.getCol() + "," + pos.getRow());
					return;
				}
			}
		}
	}

}
