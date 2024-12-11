package se.askware.aoc2024.dec05;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import se.askware.aoc2024.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Input result = getInput(input);
		long sum = 0;
		for (List<Integer> page : result.pages()) {
			final boolean match = result.rules().stream()
					.allMatch(rule -> rule.matches(page));
			if (match) {
				final Integer i = page.get(page.size() / 2);
				sum += i;
			}
		}
		System.out.println(sum);
	}

	private static Input getInput(List<String> input) {
		boolean parseRules = true;
		List<OrderingRule> rules = new ArrayList<>();
		List<List<Integer>> pages = new ArrayList<>();
		for (int i = 0; i < input.size(); i++) {
			final String row = input.get(i);
			if (row.isBlank()) {
				parseRules = false;
			} else if (parseRules) {
				final String[] split = row.split("\\|");
				rules.add(new OrderingRule(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
			} else {
				pages.add(new ArrayList<>(Arrays.stream(row.split(",")).map(Integer::parseInt).toList()));
			}
		}
		return new Input(rules, pages);
	}

	private record Input(List<OrderingRule> rules, List<List<Integer>> pages) {
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final Input parsedInput = getInput(input);
		AtomicLong sum = new AtomicLong();
		parsedInput.pages().stream()
				.filter(page -> !parsedInput.rules().stream().allMatch(rule -> rule.matches(page)))
				.forEach(page -> {
					do {
						for (OrderingRule rule : parsedInput.rules()) {
							if (!rule.matches(page)) {
								swap(page, rule);
							}
						}
					} while (!parsedInput.rules().stream().allMatch(rule -> rule.matches(page)));
					sum.addAndGet(page.get(page.size() / 2));
				});
		System.out.println(sum);
	}

	private static void swap(List<Integer> page, OrderingRule rule) {
		final int i = page.indexOf(rule.first);
		final int i1 = page.indexOf(rule.second);
		final Integer i2 = page.get(i);
		page.set(i, page.get(i1));
		page.set(i1, i2);
	}

	private record OrderingRule(int first, int second) {
		public boolean matches(List<Integer> pages) {

			final int i1 = pages.indexOf(first);
			final int i2 = pages.indexOf(second);
			return i1 < i2 || i1 == -1 || i2 == -1;
		}
	}
}
