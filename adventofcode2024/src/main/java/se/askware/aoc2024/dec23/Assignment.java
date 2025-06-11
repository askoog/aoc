package se.askware.aoc2024.dec23;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import se.askware.aoc2024.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		Map<String, List<String>> connections = new HashMap<>();
		for (String line : input) {
			final String[] split = line.split("-");
			connections.computeIfAbsent(split[0], s -> new ArrayList<>()).add(split[1]);
			connections.computeIfAbsent(split[1], s -> new ArrayList<>()).add(split[0]);
		}
		Set<Connection> cons = new HashSet<>();
		connections.forEach((s1, v1) -> {
			for (String s2 : v1) {
				final List<String> v2 = connections.get(s2);
				for (String s3 : v2) {
					if (v1.contains(s3)) {
						final String[] array = Arrays.stream(new String[] { s1, s2, s3 }).sorted().toArray(String[]::new);
						cons.add(new Connection(array[0], array[1], array[2]));
					}
				}
			}
		});
		System.out.println(cons.size());
		final long count = cons.stream()
				.filter(c -> c.s1.startsWith("t")
							 || c.s2.startsWith("t")
							 || c.s3.startsWith("t"))
				.count();
		System.out.println(count);

	}

	record Connection(String s1, String s2, String s3) {
	}

	@Override
	public void solvePartTwo(List<String> input) {
		Map<String, List<String>> connections = new HashMap<>();
		for (String line : input) {
			final String[] split = line.split("-");
			connections.computeIfAbsent(split[0], s -> new ArrayList<>()).add(split[1]);
			connections.computeIfAbsent(split[1], s -> new ArrayList<>()).add(split[0]);
		}

		Set<String> best = new HashSet<>();

		AtomicInteger maxSize = new AtomicInteger();
		connections.forEach((s1, v1) -> {
			Set<String> candidates = new HashSet<>(v1);
			candidates.add(s1);
			if (candidates.size() > maxSize.get()) {
				maxSize.set(candidates.size());
			}
			for (String s : v1) {
				if (candidates.contains(s)) {
					final List<String> strings = connections.get(s);
					for (String candidate : List.copyOf(candidates)) {
						if (!candidate.equals(s) && !strings.contains(candidate)) {
							candidates.remove(s);
						}
					}
				}
			}
			if (best.size() <= candidates.size()) {
				best.clear();
				best.addAll(candidates);
				System.out.println(best);
			}
		});

		for (String s : best) {
			final List<String> strings = connections.get(s);
			Set<String> s1 = new HashSet<>(strings);
			s1.retainAll(best);
			System.out.println(s + ": " + s1.stream().sorted().collect(Collectors.joining(",")) + " " +
							   strings.stream().sorted().collect(Collectors.joining(",")));

		}

		System.out.println(best.stream().sorted().collect(Collectors.joining(",")));
		System.out.println(maxSize);
/*		System.out.println(cons.size());
			final long count = cons.stream()
				.filter(c -> c.s1.startsWith("t")
							 || c.s2.startsWith("t")
							 || c.s3.startsWith("t"))
				.count();
		System.out.println(count);*/
		try {
			if (input.size() > 33) {

				new Puzzle().solve();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	class Puzzle {
		private final Set<Set<String>> computerPairs;
		private final Set<String> computers; // slight speedup, not really needed

		Puzzle() throws Exception {
			try (var input = Objects.requireNonNull(getClass().getResourceAsStream("day23_input.txt"))) {
				computerPairs = new BufferedReader(new InputStreamReader(input)).lines().map(Puzzle::pair).collect(Collectors.toSet());
				computers = computerPairs.stream().flatMap(Set::stream).collect(Collectors.toSet());
			}
		}

		static Set<String> pair(String line) {
			return Arrays.stream(line.split("-")).collect(Collectors.toSet());
		}

		long countTripletsWithT(Set<Set<String>> triplets) {
			return triplets.stream().filter(s -> s.stream().anyMatch(t -> t.startsWith("t"))).count();
		}

		Set<Set<String>> embiggen(Set<Set<String>> cliques) {
			Set<Set<String>> embiggened = new HashSet<>();
			for (var clique : cliques) {
				var others = new HashSet<>(computers);
				others.removeAll(clique);
				for (var other : others) {
					if (clique.stream().allMatch(s -> computerPairs.contains(Set.of(s, other)))) {
						var biggerClique = new HashSet<>(clique);
						biggerClique.add(other);
						embiggened.add(biggerClique);
					}
				}
			}
			return embiggened;
		}

		void solve() {
			var cliques = embiggen(computerPairs);
			System.out.println(countTripletsWithT(cliques));
			for (var bigger = embiggen(cliques); !bigger.isEmpty(); bigger = embiggen(bigger)) {
				cliques = bigger;
			}
			System.out.println(cliques.iterator().next().stream().sorted(String::compareTo).collect(Collectors.joining(",")));
		}
	}
}
