package se.askware.aoc2015.dec01;

import java.io.IOException;
import java.util.List;

import se.askware.aoc2015.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		System.out.println(input.get(0).chars().map(c -> c == '(' ? 1 : -1).sum());
	}

	@Override
	public void solvePartTwo(List<String> input) {
		char[] chars = input.get(0).toCharArray();
		int level = 0;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '(') {
				level++;
			} else {
				level--;
				if (level == -1) {
					System.out.println(i + 1);
				}
			}
		}
	}

}
