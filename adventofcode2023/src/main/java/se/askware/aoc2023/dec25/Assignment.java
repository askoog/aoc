package se.askware.aoc2023.dec25;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import se.askware.aoc2023.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.PART1).run();
	}

	@Override
	public void solvePartOne(List<String> input) {

		List<Wire> wires = new ArrayList<>();
		input.forEach(line -> {
			final String id = line.substring(0, line.indexOf(":"));
			final String[] split =
					line.substring(line.indexOf(":") + 2).split(" ");
			for (String s : split) {
				wires.add(new Wire(id, s));
				wires.add(new Wire(s, id));
			}
		});
		final List<String> distinctComponents = wires.stream().map(Wire::from).distinct().toList();

		for (int count = 0; count < 3; count++) {

			Map<Wire, AtomicInteger> wireUsage = new HashMap<>();
			final List<String> allEndPoints = wires.stream().map(Wire::from).distinct().toList();
			// Increment determines the number of end points tested, smaller increment means higher probability, but
			// larger increments means quicker solution
			for (int i = 0; i < allEndPoints.size(); i += 200) {
				for (int j = 0; j < allEndPoints.size(); j += 300) {
					if (i != j) {
						final List<Wire> path = findPath(allEndPoints.get(i), allEndPoints.get(j), wires);
						for (Wire wire : path) {
							wireUsage.computeIfAbsent(wire, w -> new AtomicInteger()).incrementAndGet();
							wireUsage.computeIfAbsent(new Wire(wire.to, wire.from), w -> new AtomicInteger()).incrementAndGet();
						}
					}
				}
			}
			final Map.Entry<Wire, AtomicInteger> bestUsedWire = wireUsage.entrySet().stream().sorted((e1, e2) -> Integer.compare(e2.getValue().get(), e1.getValue().get())).limit(1).findFirst().orElseThrow();
			wires.remove(bestUsedWire.getKey());
			wires.remove(new Wire(bestUsedWire.getKey().to, bestUsedWire.getKey().from));
			System.out.println("cutting " + bestUsedWire.getKey());
		}
		final int size = traverse(wires.get(0), wires).size();
		System.out.println(size * (distinctComponents.size() - size));
	}

	private static List<Wire> findPath(String start, String end, List<Wire> wires) {
		Set<String> seen = new HashSet<>();
		seen.add(start);
		final List<List<Wire>> list = wires.stream().filter(w -> w.from.equals(start)).map(List::of).toList();
		Queue<List<Wire>> queue = new ArrayDeque<>(list);
		final Map<String, List<Wire>> wireMap = wires.stream().collect(Collectors.groupingBy(Wire::from));
		while (!queue.isEmpty()) {
			final List<Wire> w = queue.poll();
			final Wire wire = w.get(w.size() - 1);
			seen.add(wire.to);

			final List<Wire> wires2 = wireMap.get(wire.to);
			for (Wire w2 : wires2) {
				if (!seen.contains(w2.to())) {

					final List<Wire> wires1 = new ArrayList<>(w);
					wires1.add(w2);
					queue.add(wires1);
					if (w2.to.equals(end)) {
						return wires1;
					}
				}
			}
		}
		return new ArrayList<>();
	}

	private static Set<String> traverse(Wire start, List<Wire> wires) {
		Set<String> seen = new HashSet<>();
		Queue<String> queue = new ArrayDeque<>();
		queue.add(start.from);
		final Map<String, List<Wire>> wireMap = wires.stream().collect(Collectors.groupingBy(Wire::from));
		while (!queue.isEmpty()) {
			final String w = queue.poll();
			seen.add(w);
			wireMap.get(w).forEach(w2 -> {

				if (!seen.contains(w2.to())) {
					queue.add(w2.to);
				}
			});
		}
		return seen;
	}

	@Override
	public void solvePartTwo(List<String> input) {
		// Just push the button
	}

	record Wire(String from, String to) {
	}

}
