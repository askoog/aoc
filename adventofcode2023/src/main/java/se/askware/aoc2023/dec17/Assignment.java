package se.askware.aoc2023.dec17;

import static se.askware.aoc2023.common.Direction.DOWN;
import static se.askware.aoc2023.common.Direction.LEFT;
import static se.askware.aoc2023.common.Direction.RIGHT;
import static se.askware.aoc2023.common.Direction.UP;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicLong;

import se.askware.aoc2023.common.AocBase;
import se.askware.aoc2023.common.CharCell;
import se.askware.aoc2023.common.Direction;
import se.askware.aoc2023.common.Grid;
import se.askware.aoc2023.common.GridPos;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.EXAMPLE1, RunMode.PART2).run();
	}

	GridPos end;
	AtomicLong best = new AtomicLong(Long.MAX_VALUE);

	@Override
	public void solvePartOne(List<String> input) {
		calculate(input, 3, 0);
		System.out.println(best.get());
		best.set(Long.MAX_VALUE);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		calculate(input, 10, 4);
		System.out.println(best.get());
		best.set(Long.MAX_VALUE);

	}

	private void calculate(List<String> input, int maxAllowed, int minRequired) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		PriorityQueue<List<Path>> queue = new PriorityQueue<>(this::comparePath);
		queue.add(List.of(new Path(new DirPos(grid.getCell(0, 0).pos(), DOWN, 0), 0)));
		Map<DirPos, Long> seen = new HashMap<>();
		long count = 0;
		end = new GridPos(grid.getNumRows() - 1, grid.getNumColumns() - 1);

		while (!queue.isEmpty()) {
			final List<Path> path = queue.poll();

			for (Direction dir : new Direction[] { DOWN, RIGHT, LEFT, UP }) {
				final ArrayList<Path> paths = new ArrayList<>(path);
				Path current = paths.get(paths.size() - 1);

				if (dir.opposite() == current.pos().dir) {
					continue;
				}
				int currentStreak;
				if (dir == current.pos.dir) {
					currentStreak = current.pos.strikeLength + 1;
					if (currentStreak > maxAllowed) {
						continue;
					}
				} else {
					int numRequired = minRequired - current.pos.strikeLength;
					if (numRequired > 0) {
						continue;
					}
					currentStreak = 1;
				}

				grid.getOptionalCell(current.pos().pos().move(dir)).ifPresent(c -> {
					long newVal = current.value() + c.intValue();
					final DirPos dirPos = new DirPos(c.pos(), dir, currentStreak);
					if (newVal < best.get() && (!seen.containsKey(dirPos) || seen.get(dirPos) > newVal)) {
						paths.add(new Path(dirPos, newVal));
						if (dirPos.pos().equals(end)) {
							best.set(newVal);

							if (logLevel == LogLevel.DEBUG) {
								final Grid<CharCell> g2 = Grid.charGrid(input);
								for (Path path1 : paths) {
									g2.getOptionalCell(path1.pos.pos()).ifPresent(c1 -> c1.value = path1.pos.dir.charValue());
								}
								g2.print();
								System.out.println(best.get());
							}
						} else {
							queue.add(new ArrayList<>(paths));
						}
						seen.put(dirPos, newVal);
					}
				});

			}
			if (++count % 100_000 == 0) {
				System.out.println(count + " " + seen.size() + " " + queue.size() + " " + best.get());
			}

		}
	}

	private int comparePath(List<Path> e, List<Path> e1) {
		if (best.get() < Long.MAX_VALUE) {
			return Long.compare(e.get(e.size() - 1).value, e1.get(e1.size() - 1).value);
		} else {
			return Long.compare(e.get(e.size() - 1).pos.pos.distance(end), e1.get(e1.size() - 1).pos.pos.distance(end));
		}
	}

	record Path(DirPos pos, long value) {
	}

	record DirPos(GridPos pos, Direction dir, int strikeLength) {
	}



}
