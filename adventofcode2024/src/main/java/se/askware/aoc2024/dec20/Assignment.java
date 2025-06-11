package se.askware.aoc2024.dec20;

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
import java.util.concurrent.atomic.AtomicLong;

import se.askware.aoc2024.common.AocBase;
import se.askware.aoc2024.common.CharCell;
import se.askware.aoc2024.common.Grid;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		final CharCell start = grid.findFirst(c -> c.value == 'S').orElseThrow();
		final CharCell end = grid.findFirst(c -> c.value == 'E').orElseThrow();

		Map<CharCell, Integer> times = new HashMap<>();
		CharCell current = start;
		int count = 0;
		times.put(current, count++);
		while (current != end) {
			current = grid.getXYNeighbors(current).stream().filter(c -> c.value != '#')
					.filter(c -> !times.containsKey(c)).findFirst().orElseThrow();
			times.put(current, count++);
		}
/*
		System.out.println(times);

		times.forEach((c, t) -> {
			c.value =  (char)('0' + (char)(t%10));
		});
		grid.print();
*/
		//7658
		//System.out.println(times.size());
		Set<Cheat> cheats = new HashSet<>();
		times.forEach((c, t) -> {
			grid.getXYNeighbors(c).stream().filter(n -> n.value == '#').forEach(n -> {
				grid.getXYNeighbors(n).stream().filter(n2 -> n2 != n && n2.value != '#').forEach(n2 -> {
					final Integer cheatTime = times.getOrDefault(n2, 0) - 2;
					if (cheatTime > t) {
						cheats.add(new Cheat(List.of(c, n, n2), cheatTime - t));
					}
/*
					grid.getXYNeighbors(n2).stream().filter(n3 -> n3.value != '#').forEach(n3 -> {
						final Integer cheatTime2 = times.getOrDefault(n3, 0) -3;
						if (cheatTime2 > t){
							cheats.add(new Cheat(List.of(c,	n, n2,n3), cheatTime2 - t));
						}
					});
*/
				});
			});
		});
		int limit = 100;
		final long numCheats = cheats.stream().sorted(Comparator.comparing(Cheat::time).reversed())
				.filter(c -> c.time >= limit)
				//.peek(System.out::println)
				.count();
		System.out.println(numCheats);
	}

	record Cheat(List<CharCell> cells, int time) {
	}

	record Cheat2(CharCell start, CharCell end) {
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		final CharCell start = grid.findFirst(c -> c.value == 'S').orElseThrow();
		final CharCell end = grid.findFirst(c -> c.value == 'E').orElseThrow();

		Map<CharCell, Integer> times = new HashMap<>();
		CharCell current = start;
		int count = 0;
		times.put(current, count++);
		while (current != end) {
			current = grid.getXYNeighbors(current).stream().filter(c -> c.value != '#')
					.filter(c -> !times.containsKey(c)).findFirst().orElseThrow();
			times.put(current, count++);
		}

		//getNeighbors(start, grid, 1	).forEach(c -> c.getLast().value=' ');
		//grid.print();


		/*
		System.out.println(times);

		times.forEach((c, t) -> {
			c.value =  (char)('0' + (char)(t%10));
		});
		grid.print();
*/
		//7658
		//System.out.println(times.size());
		Map<Cheat2, Integer> cheats = new HashMap<>();
		int steps = 20;
		times.forEach((c, t) -> {
			grid.getAll().filter(c2 -> manhattanDistance(c, c2) <= steps)
					.filter(n -> n.value != '#')
					.forEach(
							n -> {
								final int time = times.get(n) - t - manhattanDistance(c, n);
								if (time > 0) {
									final Cheat2 cheat2 = new Cheat2(c, n);
									if (cheats.getOrDefault(cheat2, 0) < time) {
										cheats.put(cheat2, time);
									}
								}
							});
		});
		for (int i = 100; i <= 100; i++) {

			int limit = i;
			AtomicLong numCheats = new AtomicLong();
			cheats.forEach((c, t) -> {
				if (t >= limit) {
					numCheats.incrementAndGet();
				}
				//.peek(System.out::println)
			});
			System.out.println(i + ": " + numCheats);
		}
	}

	public int manhattanDistance(CharCell c1, CharCell c2) {
		return Math.abs(c1.col - c2.col) +
			   Math.abs(c1.row - c2.row);
	}

	Set<List<CharCell>> getNeighbors(CharCell c, Grid<CharCell> grid, int steps) {
		Set<List<CharCell>> result = new HashSet<>();
		Set<CharCell> seen = new HashSet<>();
		seen.add(c);
		Queue<List<CharCell>> queue = new ArrayDeque<>();
		queue.add(List.of(c));
		while (!queue.isEmpty()) {
			final List<CharCell> poll = queue.poll();
			if (poll.size() <= steps) {

				grid.getXYNeighbors(poll.getLast()).forEach(n -> {
					if (seen.add(n)) {
						final ArrayList<CharCell> e = new ArrayList<>(poll);
						e.add(n);
						if (n.value != '#') {
							result.add(e);
						} else {
							queue.add(e);
						}
					}
				});
			}
		}
		//System.out.println(result);
		return result;
	}

}
