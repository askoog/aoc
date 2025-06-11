package se.askware.aoc2015.dec09;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import se.askware.aoc2015.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		Pattern pattern = Pattern.compile("(.*) to (.*) = (\\d+)");
		Map<Path, Integer> map = new HashMap<>();
		input.stream().map(pattern::matcher).forEach(m -> {
			m.find();
			map.put(new Path(m.group(1), m.group(2)), Integer.parseInt(m.group(3)));
			map.put(new Path(m.group(2), m.group(1)), Integer.parseInt(m.group(3)));
		});

		final Set<String> cities = map.entrySet().stream().map(e -> e.getKey().from).distinct().collect(Collectors.toSet());

		Queue<List<Path>> queue = new ArrayDeque<>();
		cities.forEach(c1 -> {
			cities.forEach(c2 -> {
				if (!c1.equals(c2)) {
					List<Path> list = List.of(new Path(c1, c2));
					queue.add(list);
				}
			});
		});

		int min = Integer.MAX_VALUE;
		int max = 0;
		while(!queue.isEmpty()){
			final List<Path> poll = queue.poll();
			if(poll.size() == cities.size() - 1) {
				final int sum1 = poll.stream().mapToInt(map::get).sum();
				if (min > sum1) {
					min = sum1;
				}
				if (max < sum1) {
					max = sum1;
				}
				continue;
			}
			final Set<String> visited = poll.stream().flatMap(p -> Stream.of(p.from, p.to)).distinct().collect(Collectors.toSet());
			cities.forEach(c -> {
				if (!visited.contains(c)){
					List<Path> newPath = new ArrayList<>(poll);
					newPath.add(new Path(poll.get(poll.size() - 1).to, c));
					queue.add(newPath);
				}
			});
		}
		System.out.println(min);
		System.out.println(max);
	}

	@Override
	public void solvePartTwo(List<String> input) {
	}

	record Path(String from, String to) {
	}

}
