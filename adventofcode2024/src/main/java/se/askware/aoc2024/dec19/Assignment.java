package se.askware.aoc2024.dec19;

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

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final List<String> towels = Arrays.stream(input.remove(0).split(",")).map(String::trim).toList();
		input.remove(0);
		System.out.println(towels);
		int sum = 0;
		for (String wanted : input) {
			final List<String> combination = findCombination(wanted, towels);
			//System.out.println(wanted + " " + combination);
			if (!combination.isEmpty()){
				sum++;
			}
		}
		System.out.println(sum);
	}

	private List<String> findCombination(String wanted, List<String> towels) {
		List<String> result = new ArrayList<>();
		Queue<String> candidates = new PriorityQueue<>(Comparator.comparingInt(String::length).reversed());
		candidates.add("");
		while(!candidates.isEmpty()) {
			//System.out.println(candidates.size());
			String current = candidates.poll();
			int index = current.length();
			for (String towel : towels) {
				if (wanted.substring(index).startsWith(towel)) {
					String next = current + towel;
					if (next.equals(wanted)) {
						result.add(next);
						return result;
					} else {
						candidates.add(next);
					}
				}
			}
		}
		return result;
	}

	public static long findCombinations(final Map<String, Long> seen, final List<String> towels, final String wanted) {
		if (wanted.isEmpty()){
			return 1;
		}
		if (seen.containsKey(wanted)) {
			return seen.get(wanted);
		}

		long ways = 0;
		for (final String towel : towels) {
			if (wanted.startsWith(towel)) {
				ways += findCombinations(seen, towels, wanted.substring(towel.length()));
			}
		}

		seen.put(wanted, ways);
		return ways;
	}

	@Override
	public void solvePartTwo(List<String> input) {

		final List<String> towels = Arrays.stream(input.remove(0).split(",")).map(String::trim).toList();
		input.remove(0);
		long sum = 0;
		for (String wanted : input) {
			final long ways = findCombinations(new HashMap<>(), towels, wanted);
			System.out.println(wanted + " " + ways);
			sum+= ways;
		}
		System.out.println(sum);

	}


}
