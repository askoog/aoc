package se.askware.aoc2015.dec08;

import java.io.IOException;
import java.util.List;

import se.askware.aoc2015.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		int total = 0;
		for (String line : input) {
			final String substring = line.substring(1, line.length() - 1);
			int count = 0;
			for (int i = 0; i < substring.length(); i++) {
				count++;
				if (substring.charAt(i) == '\\') {
					i++;
					if (substring.charAt(i) == 'x') {
						i += 2;
					}
				}
			}
			total += line.length() - count;
		}
		System.out.println(total);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final List<String> list = input.stream()
				.map(s -> "\"" + s.replace("\\", "\\\\")
						.replace("\"", "\\\"") + "\"")
				.toList();
		solvePartOne(list);

	}

}
