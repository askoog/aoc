package se.askware.aoc2022.dec04;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import se.askware.aoc2022.common.AocBase;
import se.askware.aoc2022.common.Pair;
import se.askware.aoc2022.common.Range;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		long count = input.stream()
				.map(Pair.parse(','))
				.map(p -> p.map(Range::valueOf))
				.filter(p -> p.matches(Range::contains) || p.flip().matches(Range::contains))
				.count();
		System.out.println(count);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		long count = input.stream()
				.map(Pair.parse(','))
				.map(p -> p.map(Range::valueOf))
				.filter(p -> p.matches(Range::overlaps))
				.count();

		System.out.println(count);

	}

}
