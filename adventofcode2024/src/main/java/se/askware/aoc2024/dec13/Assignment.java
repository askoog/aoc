package se.askware.aoc2024.dec13;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.askware.aoc2024.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		solve(input, false);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		solve(input, true);
	}

	record Prize(long x, long y) {
	}

	private static Rule parseRule(Pattern p, String first) {
		final Matcher matcher = p.matcher(first);
		matcher.find();
		String button = matcher.group(1);
		int x = Integer.parseInt(matcher.group(2));
		int y = Integer.parseInt(matcher.group(3));

		return new Rule(button, x, y);
	}

	private static Prize parsePrize(Pattern p, String input) {
		final Matcher matcher = p.matcher(input);
		matcher.find();
		long x = Long.parseLong(matcher.group(1));
		long y = Long.parseLong(matcher.group(2));
		return new Prize(x, y);
	}

	record Rule(String button, long x, long y) {
	}

	private static void solve(List<String> input, boolean partTwo) {
		Pattern buttonPattern = Pattern.compile("Button ([A-Z]): X\\+(\\d+), Y\\+(\\d+)");
		Pattern prizePattern = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");
		AtomicInteger numPrizes = new AtomicInteger();
		AtomicLong cost = new AtomicLong();
		for (int i = 0; i < input.size(); i++) {

			String first = input.get(i++);
			String second = input.get(i++);
			String prize = input.get(i++);

			final Rule a = parseRule(buttonPattern, first);
			final Rule b = parseRule(buttonPattern, second);
			Prize p = parsePrize(prizePattern, prize);
			if (partTwo) {
				p = new Prize(p.x + 10000000000000L, p.y + 10000000000000L);
			}
			long bVal = ((p.y * a.x) - (p.x * a.y)) / (a.x * b.y - a.y * b.x);
			long aVal = (p.y - (b.y * bVal)) / a.y;
/*
			System.out.println(bVal + " " + aVal);
			System.out.println(a.x * aVal + b.x * bVal);
			System.out.println(a.y * aVal + b.y * bVal);
*/
			if (a.x * aVal + b.x * bVal == p.x &&
				a.y * aVal + b.y * bVal == p.y) {
				cost.addAndGet(aVal * 3 + bVal);
				numPrizes.incrementAndGet();
			}

		}
		System.out.println("Num prizes: " + numPrizes.get());
		System.out.println("Cost: " + cost.get());
	}

}
