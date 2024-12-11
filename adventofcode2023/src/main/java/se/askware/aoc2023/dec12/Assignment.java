
package se.askware.aoc2023.dec12;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import se.askware.aoc2023.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.PART2).run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		System.out.println(input);
		final long count = input.stream().mapToLong(i -> findNumMatching(i).size()).sum();
		System.out.println(count);
	}

	private List<String> findNumMatching(String input) {
		final String[] split = input.split(" ");
		List<String> templates = new ArrayList<>();
		templates.add("");
		String line = split[0];
		final String pattern = split[1];

		final int maxItems = Arrays.stream(pattern.split(",")).mapToInt(Integer::parseInt).sum();
		final int maxConsecutive = Arrays.stream(pattern.split(",")).mapToInt(Integer::parseInt).max().orElse(0);

		long iter = 0;
		for (int i = 0; i < line.length() ; i++) {
			char c = line.charAt(i);
			if (c == '?'){
				templates = templates.stream().flatMap(s -> Stream.of(s + "#", s + ".")).toList();
			} else {
				templates = templates.stream().map(s -> s + c).toList();
			}
			templates = templates.stream()
					.filter(s -> numOccurences(s, '#') <= maxItems)
					.filter(s -> isCandidate(s, pattern, maxConsecutive))
					.toList();
			iter++;
		}
		return templates.stream().filter(s -> matches(s, pattern)).toList();
	}
	
	private long numOccurences(String s, char c){
		return s.chars().filter(i -> i == c).count();
	}

	private boolean matches(String input, String pattern) {
		String result = "";
		int current = 0;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '#') {
				current++;
			} else {
				if (current != 0) {
					result = result + (result.isEmpty() ? "" : ",") + current;
					current = 0;
				}
			}
		}
		if (current != 0) {
			result = result + "," + current;
		}

		return result.equals(pattern);
	}

	private boolean isCandidate(String input, String pattern, int maxItems) {
		String result = "";
		int current = 0;
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == '?'){
				break;
			}else if (input.charAt(i) == '#') {
				current++;
			} else {
				if (current != 0) {
					if (current > maxItems){
						return false;
					}
					result = result + (result.isEmpty() ? "" : ",") + current;
					current = 0;
				}
			}
		}
		debug(input + " " + result + " ? " +  pattern + " ! " + pattern.startsWith(result));
		numOccurences(input,'?');

		return pattern.startsWith(result);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		long sum = 0;
		for (var line: input) {
			String[] parts = line.split(" ");
			String springsLine = parts[0].chars().mapToObj(Character::toString).collect(Collectors.joining());
			List<Integer> numbers = toIntList(parts[1], ",");
			String newSpringsLine = springsLine + ('?' + springsLine).repeat(4);

			List<Integer> newNumbers = new LinkedList<>(numbers) {{
				addAll(numbers);
				addAll(numbers);
				addAll(numbers);
				addAll(numbers);
			}};

			sum += combinationsCount(newSpringsLine, newNumbers, false);
		}

		System.out.println(sum);
	}

	record State(String line, List<Integer> damaged, boolean inGroup){}

	long cacheHits = 0;
	private long combinationsCount(String line, List<Integer> springs, boolean inGroup) {
		State state = new State(line, springs, inGroup);
		if (memory.containsKey(state)){
			cacheHits++;
			if (cacheHits % 10_000 == 0){
				System.out.println(cacheHits);
			}
			return memory.get(state);
		}

		if (line.isEmpty()) {
			return springs.isEmpty() || (springs.size() == 1 && springs.get(0) == 0) ? 1 : 0;
		}

		char firstChar = line.charAt(0);
		String remainingLine = line.substring(1);

		switch (firstChar) {
			case '.' -> {
				if (inGroup) {
					if (springs.get(0) != 0) {
						return 0;
					}

					return cache(state, combinationsCount(remainingLine, springs.subList(1, springs.size()), false));
				}

				return cache(state, combinationsCount(remainingLine, springs, false));
			}
			case '#' -> {
				if (!springs.isEmpty() && springs.get(0) > 0) {
					List<Integer> reducedSprings = new ArrayList<>(springs);
					reducedSprings.set(0, springs.get(0) - 1);;
					return cache(state, combinationsCount(remainingLine, reducedSprings, true));
				}
			}
			case '?' -> {
				return cache(
						state,
						combinationsCount('.' + remainingLine, springs, inGroup)
								+ combinationsCount('#' + remainingLine, springs, inGroup)
				);
			}
		}

		return 0;
	}

	private final HashMap<State, Long> memory = new HashMap<>();

	private long cache(State state, long result) {
		memory.put(state, result);
		return result;
	}

}
