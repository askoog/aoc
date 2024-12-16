package se.askware.aoc2024.dec16;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import se.askware.aoc2024.common.AocBase;
import se.askware.aoc2024.common.CharCell;
import se.askware.aoc2024.common.Direction;
import se.askware.aoc2024.common.Grid;
import se.askware.aoc2024.common.GridPos;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		final CharCell start = grid.findFirst(c -> c.value == 'S').orElseThrow();
		final CharCell end = grid.findFirst(c -> c.value == 'E').orElseThrow();

		Map<DirectedPos, Reindeer> seen = new HashMap<>();
		List<CharCell> path = List.of(start);
		Queue<Reindeer> queue = new PriorityQueue<>(Comparator.comparingInt(r -> r.cost));
		queue.add(new Reindeer(path, Direction.RIGHT, 0));
		while (!queue.isEmpty()) {
			Reindeer reindeer = queue.poll();
			final Reindeer best = seen.get(new DirectedPos(reindeer.path.get(reindeer.path.size() - 1).pos(), reindeer.dir));
			if (best != null && best.cost <= reindeer.cost) {
				continue;
			} else {
				seen.put(new DirectedPos(reindeer.path.get(reindeer.path.size() - 1).pos(), reindeer.dir), reindeer);
			}
			CharCell current = reindeer.path.get(reindeer.path.size() - 1);
			if (current.equals(end)) {
				System.out.println(reindeer.cost);
				break;
			}
			CharCell next = grid.getOptionalCell(current.pos().move(reindeer.dir)).orElseThrow();
			if (next.value == '.' || next.value == 'E') {
				List<CharCell> newPath = new ArrayList<>(reindeer.path);
				newPath.add(next);
				queue.add(new Reindeer(newPath, reindeer.dir, reindeer.cost + 1));
			}
			Arrays.stream(Direction.values()).forEach(dir ->
					queue.add(new Reindeer(new ArrayList<>(reindeer.path), dir, reindeer.cost + 1000)));
		}

	}

	@Override
	public void solvePartTwo(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		final CharCell start = grid.findFirst(c -> c.value == 'S').orElseThrow();
		final CharCell end = grid.findFirst(c -> c.value == 'E').orElseThrow();

		Map<DirectedPos, Reindeer> seen = new HashMap<>();
		List<CharCell> path = List.of(start);
		Queue<Reindeer> queue = new PriorityQueue<>(Comparator.comparingInt(r -> r.cost));
		queue.add(new Reindeer(path, Direction.RIGHT, 0));
		List<Reindeer> finished = new ArrayList<>();

		while (!queue.isEmpty()) {
			Reindeer reindeer = queue.poll();
			if(!finished.isEmpty() && reindeer.cost > finished.get(0).cost) {
				continue;
			}
			final Reindeer best = seen.get(new DirectedPos(reindeer.path.get(reindeer.path.size() - 1).pos(), reindeer.dir));
			if (best != null && best.cost < reindeer.cost) {
				continue;
			} else {
				seen.put(new DirectedPos(reindeer.path.get(reindeer.path.size() - 1).pos(), reindeer.dir), reindeer);
			}
			CharCell current = reindeer.path.get(reindeer.path.size() - 1);
			if (current.equals(end)) {
				finished.add(reindeer);
				continue;
			}
			CharCell next = grid.getOptionalCell(current.pos().move(reindeer.dir)).orElseThrow();
			if (next.value == '.' || next.value == 'E') {
				List<CharCell> newPath = new ArrayList<>(reindeer.path);
				newPath.add(next);
				queue.add(new Reindeer(newPath, reindeer.dir, reindeer.cost + 1));
			}
			Arrays.stream(Direction.values()).forEach(dir ->
					queue.add(new Reindeer(new ArrayList<>(reindeer.path), dir, reindeer.cost + 1000)));
		}

		System.out.println(finished.stream().flatMap(r -> r.path.stream()).distinct().count());
	}

	record DirectedPos(GridPos pos, Direction dir) {
	}

	record Reindeer(List<CharCell> path, Direction dir, int cost) {
	}
}
