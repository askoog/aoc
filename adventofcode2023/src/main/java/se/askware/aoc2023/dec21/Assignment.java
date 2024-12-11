package se.askware.aoc2023.dec21;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
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
		new Assignment().withRunMode(RunMode.PART2).run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final long count = solve(input, true, 64);
		System.out.println(count);
	}

	private static long solve(List<String> input, boolean oneGrid, int limit) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		final CharCell startCell = grid.getAll().filter(c -> c.value == 'S').findFirst().get();
		startCell.value = '.';
		GridPos pos = startCell.pos();
		Queue<Path> todo = new ArrayDeque<>();
		todo.add(new Path(pos, 0, new GridPos(0, 0)));
		Set<Path> visited = new HashSet<>();
		long iter=1;
		while (!todo.isEmpty()) {
			if (iter++%1000 == 0){
				//System.out.println(iter);
			}
			final Path p = todo.poll();
			if (visited.add(p) && p.numSteps < limit) {
				//System.out.println(p);
				if (oneGrid) {
					grid.getXYNeighbors(p.pos).stream()
							.filter(c -> c.value == '.')
							.forEach(n ->
							todo.add(new Path(n.pos(), p.numSteps + 1, p.offset)));
				} else {
					for (Direction dir : new Direction[] { Direction.UP, Direction.DOWN }) {
						CharCell optionalCell = grid.getOptionalCell(p.pos(), dir).orElse(null);
						if (optionalCell != null) {
							if (optionalCell.value == '.') {
								todo.add(new Path(optionalCell.pos(), p.numSteps + 1, p.offset));

							}
						} else {
							final GridPos move = p.pos().move(dir.opposite(), grid.getNumRows() - 1);
							//System.out.println(p.pos() +"->" + move);
							if (grid.getCell(pos).value == '.') {
								todo.add(new Path(move, p.numSteps + 1, p.offset.move(dir)));
							}
						}
					}
					for (Direction dir : new Direction[] { Direction.LEFT, Direction.RIGHT }) {
						CharCell optionalCell = grid.getOptionalCell(p.pos(), dir).orElse(null);
						if (optionalCell != null) {
							if (optionalCell.value == '.') {
								todo.add(new Path(optionalCell.pos(), p.numSteps + 1, p.offset));
							}
						} else {
							final GridPos move = p.pos().move(dir.opposite(), grid.getNumColumns() - 1);
							if (grid.getCell(pos).value == '.') {
								//System.out.println(p.pos() +"->" + move);
								todo.add(new Path(move, p.numSteps + 1, p.offset.move(dir)));
							}
						}
					}
				}
			}
		}
		//System.out.println(visited);
		final long count = visited.stream().filter(c -> c.numSteps == limit).count();
		//visited.stream().filter(c -> c.numSteps == limit).forEach(System.out::println);
		return count;
	}

	@Override
	public void solvePartTwo(List<String> input) {

		int steps = 26501365;
		final int width= input.get(0).length();
		int remainder = steps % width;
		System.out.println(remainder);

		System.out.println("solve " + remainder);
		final long count1 = solve(input, false, remainder);
		System.out.println(count1);
		System.out.println("solve " + (remainder + width));
		final long count2 = solve(input, false, remainder + width );
		System.out.println(count2);
		System.out.println("solve " + (remainder + 2*width));
		final long count3 = solve(input, false, remainder + 2*width);
		System.out.println(count3);


		long a = (count1 - 2*count2 + count3) / 2;
		long b = (-3*count1 + 4*count2 - count3) / 2;
		long c = count1;
		long n = steps / width;
		//print "Equation: $a n^2 + $b n + $c, with n = $n\n";
		long result = a * n * n + b * n + c;
		System.out.println(result);
	}

	record Path(GridPos pos, int numSteps, GridPos offset) {
	}

}
