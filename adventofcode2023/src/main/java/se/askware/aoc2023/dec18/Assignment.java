package se.askware.aoc2023.dec18;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

		final List<Line> lines = parseLines(input);
		Result result = solveNaive(lines, 500);
		System.out.println("naive: " + (result.beforeFill() + result.afterFill()));
		System.out.println("shoe lace: " + solveWithShoeLace(lines));
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final List<Line> lines = parseLines(input);
		final List<Line> newLines = lines.stream().map(line -> {
			final int i = Integer.parseInt(line.color().toUpperCase().substring(0, 5), 16);
			Direction dir = switch (line.color().charAt(5)) {
				case '0' -> Direction.RIGHT;
				case '1' -> Direction.DOWN;
				case '2' -> Direction.LEFT;
				case '3' -> Direction.UP;
				default -> throw new IllegalArgumentException(line.color());
			};
			return new Line(dir, i, line.color());
		}).toList();

		System.out.println(solveWithShoeLace(newLines));
	}

	private BigDecimal solveWithShoeLace(List<Line> lines) {
		List<GridPos> corners = new ArrayList<>();
		GridPos last = new GridPos(0, 0);
		corners.add(last);
		long lineLength = 0;
		for (Line line : lines) {
			last = last.move(line.dir(), line.length);
			corners.add(last);
			lineLength += line.length;
		}
		final BigDecimal area = area(corners);
		final BigDecimal result = area.add(BigDecimal.valueOf(lineLength)
				.divide(BigDecimal.valueOf(2)).add(BigDecimal.ONE));
		return result;
	}

	private Result solveNaive(List<Line> lines, int size) {

		Grid<CharCell> grid = Grid.charGrid(size, size, (i, j) -> '.');
		GridPos current = new GridPos(size / 2, size / 2);
		grid.getCell(current).value = '#';
		for (Line line : lines) {
			for (int i = 0; i < line.length; i++) {
				current = current.move(line.dir);
				grid.getCell(current).value = '#';
			}
		}

		final long beforeFill = grid.getAll().filter(c -> c.value == '#').count();

		// Flood fill
		Queue<CharCell> queue = new ArrayDeque<>();
		queue.add(grid.getCell(0, 0));
		Set<CharCell> done = new HashSet<>();
		while (!queue.isEmpty()) {
			final CharCell c = queue.poll();
			done.add(c);
			if (c.value == '.') {
				c.value = ' ';
				grid.getAllNeighbors(c).stream()
						.filter(c1 -> !done.contains(c1))
						.forEach(queue::add);
			}
		}
		//grid.print();
		final long afterFill = grid.getAll().filter(c -> c.value == '.').count();
		return new Result(beforeFill, afterFill);
	}

	private static List<Line> parseLines(List<String> input) {
		Pattern p = Pattern.compile("([LRUD]) (\\d+) \\(#([a-z0-9]+)\\)");
		return input.stream().filter(s -> !s.isEmpty()).map(s -> {
			final Matcher matcher = p.matcher(s);
			matcher.find();
			final Direction dir = Direction.parse(matcher.group(1));
			final int length = Integer.parseInt(matcher.group(2));
			String color = matcher.group(3);

			return new Line(dir, length, color);
		}).toList();
	}

	private record Result(long beforeFill, long afterFill) {
	}

	record Line(Direction dir, int length, String color) {
	}

	// Shoelace algorithm. Proudly copied and modified from https://www.sanfoundry.com/java-program-shoelace-algorithm/
	public BigDecimal area(List<GridPos> pos) {
		BigDecimal det = new BigDecimal(0);
		/** add product of x coordinate of ith point with y coordinate of (i + 1)th point **/
		for (int i = 0; i < pos.size() - 1; i++) {
			det = det
					.add(BigDecimal.valueOf(pos.get(i).getCol())
							.multiply(BigDecimal.valueOf(pos.get(i + 1).getRow())));
		}
		/** subtract product of y coordinate of ith point with x coordinate of (i + 1)th point **/
		for (int i = 0; i < pos.size() - 1; i++) {
			det = det
					.subtract(BigDecimal.valueOf(pos.get(i).getRow())
							.multiply(BigDecimal.valueOf(pos.get(i + 1).getCol())));
		}

		/** find absolute value and divide by 2 **/
		det = det.abs().divide(BigDecimal.valueOf(2));
		return det;
	}
}
