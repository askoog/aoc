package se.askware.aoc2023.dec09;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import se.askware.aoc2023.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.values()).withLogLevel(LogLevel.INFO).run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final int sum = parseAndCalculate(input)
				.mapToInt(line -> line.get(0).get(line.get(0).size() - 1))
				.peek(AocBase::debug)
				.sum();
		System.out.println(sum);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final int sum = parseAndCalculate(input)
				.mapToInt(line -> line.get(0).get(0))
				.peek(AocBase::debug)
				.sum();
		System.out.println(sum);
	}

	private Stream<List<List<Integer>>> parseAndCalculate(List<String> input) {
		return input.stream()
				.map(AocBase::toIntList)
				.peek(n -> debug("-----------"))
				.map(this::calcRecursive);
	}

	private List<List<Integer>> calcRecursive(List<Integer> input) {
		debug(input);
		List<List<Integer>> result = new ArrayList<>();
		result.add(input);
		if (input.stream().allMatch(i -> i == 0)) {
			return result;
		} else {
			List<Integer> next = new ArrayList<>();
			for (int i = 0; i < input.size() - 1; i++) {
				next.add(input.get(i + 1) - input.get(i));
			}
			final List<List<Integer>> recursive = calcRecursive(next);
			input.add(next.get(next.size() - 1) + input.get(input.size() - 1));
			input.add(0, input.get(0) - next.get(0));
			result.addAll(recursive);
			debug(input);
			return result;
		}
	}

}
