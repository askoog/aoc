package se.askware.aoc2015.dec11;

import java.io.IOException;
import java.util.List;

import se.askware.aoc2015.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		String line = input.get(0);

		while (!isValid(line)) {
			line = increment(line);
		}
		System.out.println(line);
		line = increment(line);
		while (!isValid(line)) {
			line = increment(line);
		}
		System.out.println(line);

	}

	private String increment(String line) {
		int i = line.length() - 1;
		final int i1 = line.charAt(i) + 1;
		if (i1 > 'z') {
			return increment(line.substring(0, i)) + "a";
		} else {
			return line.substring(0, i) + (char) i1;
		}
	}

	private boolean isValid(String line) {
		return hasIncreasingStraight(line) && !line.contains("i") && !line.contains("o") && !line.contains("l") && hasTwoPairs(line);
	}

	private boolean hasTwoPairs(String line) {
		int pairs = 0;
		for (int i = 0; i < line.length() - 1; i++) {
			if (line.charAt(i) == line.charAt(i + 1)) {
				pairs++;
				i++;
			}
		}
		return pairs >= 2;
	}

	private boolean hasIncreasingStraight(String line) {
		for (int i = 0; i < line.length() - 2; i++) {
			if (line.charAt(i) + 1 == line.charAt(i + 1) && line.charAt(i) + 2 == line.charAt(i + 2)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void solvePartTwo(List<String> input) {
	}

}
