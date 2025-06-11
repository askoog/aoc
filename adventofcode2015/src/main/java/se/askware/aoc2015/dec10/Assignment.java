package se.askware.aoc2015.dec10;

import java.io.IOException;
import java.util.List;

import se.askware.aoc2015.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		countAndTell(input, 40);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		countAndTell(input, 50);
	}

	private static void countAndTell(List<String> input, int numIterations) {
		String s = input.get(0);
		for (int i = 0; i < numIterations; i++) {
			s = countAndTell(s);
		}
		System.out.println(s.length());
	}

	private static String countAndTell(String s) {
		StringBuilder next = new StringBuilder();
		char last = s.charAt(0);
		int currentCount = 1;
		for (int i = 1; i < s.length(); i++) {
			if (s.charAt(i) == last) {
				currentCount++;
			} else {
				next.append(currentCount).append(last);
				currentCount = 1;
				last = s.charAt(i);
			}
		}
		next.append(currentCount).append(last);
		return next.toString();
	}


}
