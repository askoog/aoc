package se.askware.aoc2022.dec12;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

import se.askware.aoc2022.common.AocBase;
import se.askware.aoc2022.common.Cell;
import se.askware.aoc2022.common.CharCell;
import se.askware.aoc2022.common.Grid;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}



	@Override
	public void solvePartOne(List<String> input) {
		final Grid<CharCell> grid = Grid.init(input, (p, c) -> new CharCell(c));

		final CharCell start = grid.findFirst(c -> c.value == 'S').get();
		start.value = 'a';
		final CharCell end = grid.findFirst(c -> c.value == 'E').get();
		end.value = 'z';

		final List<CharCell> best = grid.findPathXY(start, end, (last, next) -> next.value - last.value <= 1);

		System.out.println(best.size() - 1);

	}

	@Override
	public void solvePartTwo(List<String> input) {
		final Grid<CharCell> grid = Grid.init(input, (p, c) -> new CharCell(c));

		final CharCell start = grid.findFirst(c -> c.value == 'S').get();
		start.value = 'a';
		final CharCell end = grid.findFirst(c -> c.value == 'E').get();
		end.value = 'z';

		List<CharCell> allA = grid.getAll().filter(c -> c.value == 'a').collect(Collectors.toList());

		int minPath = allA.stream()
				.map(a -> grid.findPathXY(a, end, (last, next) -> next.value - last.value <= 1))
				.filter(l -> !l.isEmpty())
				.mapToInt(l -> l.size() - 1)
				.sorted()
				.findFirst().getAsInt();

		System.out.println(minPath);
	}

}
