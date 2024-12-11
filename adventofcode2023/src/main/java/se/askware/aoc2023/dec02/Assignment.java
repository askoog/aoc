package se.askware.aoc2023.dec02;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import se.askware.aoc2023.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {

		//		Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
		Map<Integer, List<Map<String, Integer>>> games = parseInput(input);
		Map<String, Integer> max = Map.of(
				"red", 12,
				"green", 13,
				"blue", 14
		);

		final int sum = games.entrySet().stream()
				.filter(g ->
						g.getValue().stream().allMatch(r ->
								max.entrySet().stream().allMatch(
										e -> r.getOrDefault(e.getKey(), 0) <= e.getValue()
								))
				).mapToInt(Map.Entry::getKey).sum();
		System.out.println(sum);

	}

	@Override
	public void solvePartTwo(List<String> input) {

		//		Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
		Map<Integer, List<Map<String, Integer>>> games = parseInput(input);

		final List<String> allColors = games.values().stream().flatMap(g -> g.stream().flatMap(r -> r.keySet().stream())).distinct().collect(Collectors.toList());

		Map<String, Integer> max = new HashMap<>(Map.of(
				"red", Integer.MAX_VALUE,
				"green", Integer.MAX_VALUE,
				"blue", Integer.MAX_VALUE
		));
		final long sum = games.values().stream().mapToLong(maps -> {

			for (String color : allColors) {
				max.put(color, 0);

				for (int i = max.get(color); i < 1000; i++) {
					max.put(color, i);
					final boolean matchesGame = maps.stream()
							.allMatch(r -> max.entrySet().stream().allMatch(
									e -> r.getOrDefault(e.getKey(), 0) <= e.getValue()
							));
					if (matchesGame) {
						break;
					}
				}
			}
			System.out.println(max.values().stream().mapToLong(i -> i).reduce(Math::multiplyExact).orElse(0));
			long factor = 1;
			for (Integer value : max.values()) {
				factor *= value;
			}
			System.out.println(factor);
			return factor;
		}).sum();
		System.out.println(sum);
	}

	private static Map<Integer, List<Map<String, Integer>>> parseInput(List<String> input) {
		Map<Integer, List<Map<String, Integer>>> games = new HashMap<>();
		input.forEach(line -> {

			final String[] split = line.split(":");
			int id = Integer.parseInt(split[0].replace("Game ", ""));
			List<Map<String, Integer>> rounds = new ArrayList<>();
			Arrays.stream(split[1].split(";")).forEach(round -> {
				Map<String, Integer> colours = new HashMap<>();
				Arrays.stream(round.trim().split(",")).forEach(turn -> {
					final String[] color = turn.trim().split(" ");
					colours.put(color[1], Integer.parseInt(color[0]));
				});
				rounds.add(colours);
			});
			games.put(id, rounds);
		});
		return games;
	}

}
