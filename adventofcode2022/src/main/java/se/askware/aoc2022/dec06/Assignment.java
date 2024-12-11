package se.askware.aoc2022.dec06;

import java.io.IOException;
import java.util.List;

import se.askware.aoc2022.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		for (String s : input) {
			int len = 4;
			findFirstUniqueString(s, len);
		}
	}

	private static void findFirstUniqueString(String s, int len) {
		for (int i = 0; i < s.length() - 4; i++) {
			String sub = s.substring(i, i + len);
			if (!containsDuplicateCharacters(sub)) {
				System.out.println(i + len);
				return;
			}
		}
	}

	private static boolean containsDuplicateCharacters(String sub) {
		for (int j = 0; j < sub.length(); j++) {
			char c = sub.charAt(j);
			for (int k = j + 1; k < sub.length(); k++) {
				if (sub.charAt(k) == c) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void solvePartTwo(List<String> input) {
		for (String s : input) {
			int len = 14;
			findFirstUniqueString(s, len);
		}
	}

}
