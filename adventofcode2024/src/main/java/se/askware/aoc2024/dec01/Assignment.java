package se.askware.aoc2024.dec01;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import se.askware.aoc2024.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final int[] first = input.stream().mapToInt(s -> Integer.parseInt(s.split("   ")[0])).sorted().toArray();
		final int[] second = input.stream().mapToInt(s -> Integer.parseInt(s.split("   ")[1])).sorted().toArray();

		long sum = 0;
		for (int i = 0; i < first.length; i++) {
			//System.out.println(first[i] + " " + second[i]);
			sum += Math.abs(first[i] - second[i]);
		}
		System.out.println(sum);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final int[] first = input.stream().mapToInt(s -> Integer.parseInt(s.split("   ")[0])).sorted().toArray();
		final Map<Integer, List<Integer>> collect = input.stream().map(s -> Integer.parseInt(s.split("   ")[1])).collect(Collectors.groupingBy(s -> s));

		long sum = 0;
		for (int i = 0; i < first.length; i++) {
			System.out.println(first[i] + " " + collect.getOrDefault(first[i], List.of()).size());
			sum += (long)first[i] * collect.getOrDefault(first[i], List.of()).size();
		}
		System.out.println(sum);

	}

}
