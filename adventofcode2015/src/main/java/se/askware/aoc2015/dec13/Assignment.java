package se.askware.aoc2015.dec13;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.askware.aoc2015.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Map<String, Map<String, Integer>> happiness = parseInput(input);
		final List<String> names = new ArrayList<>(happiness.keySet().stream().sorted().toList());
		final long max = calculateHappiness(names, happiness);
		System.out.println(max);

	}

	private static Map<String, Map<String, Integer>> parseInput(List<String> input) {
		Pattern p = Pattern.compile("(.+) would (gain|lose) (\\d+) happiness units by sitting next to (.+)\\.");
		Map<String, Map<String, Integer>> happiness = new HashMap<>();
		for (String line : input) {
			final Matcher matcher = p.matcher(line);
			matcher.find();
			final String p1 = matcher.group(1);
			final boolean increase = matcher.group(2).equals("gain");
			final int amount = Integer.parseInt(matcher.group(3)) * (increase ? 1 : -1);
			final String p2 = matcher.group(4);

			happiness.computeIfAbsent(p1, k -> new HashMap<>()).put(p2, amount);
		}
		return happiness;
	}

	private static long calculateHappiness(List<String> names, Map<String, Map<String, Integer>> happiness) {
		final List<List<String>> combinations = getCombinations(names);
		long max = 0;
		for (List<String> combination : combinations) {
			int sum = 0;
			for (int i = 0; i < combination.size(); i++) {
				final String p1 = combination.get(i);
				final String p2 = combination.get((i + 1) % combination.size());
				sum += happiness.computeIfAbsent(p1, o -> new HashMap<>()).getOrDefault(p2, 0);
				sum += happiness.computeIfAbsent(p2, o -> new HashMap<>()).getOrDefault(p1, 0);
			}
			if (max < sum) {
				max = sum;
			}
		}
		return max;
	}

	private static List<List<String>> getCombinations(List<String> names) {
		List<List<String>> combinations = new ArrayList<>();
		combinations.add(List.of(names.remove(0)));
		for (int i1 = 0; i1 < names.size(); i1++) {
			List<List<String>> next = new ArrayList<>();
			for (List<String> combination1 : combinations) {
				for (int j = 0; j < names.size(); j++) {
					final String o = names.get(j);
					if (!combination1.contains(o)){
						final ArrayList<String> e = new ArrayList<>(combination1);
						e.add(o);
						next.add(e);
					}
				}
			}
			combinations = next;

		}
		return combinations;
	}

	record Pair(String p1, String p2, int amount) {
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final Map<String, Map<String, Integer>> happiness = parseInput(input);
		final List<String> names = new ArrayList<>(happiness.keySet().stream().sorted().toList());
		names.add("Me");
		final long max = calculateHappiness(names, happiness);
		System.out.println(max);
	}

}
