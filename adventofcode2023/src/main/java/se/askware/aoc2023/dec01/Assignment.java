package se.askware.aoc2023.dec01;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import se.askware.aoc2023.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.values()).run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		long sum = 0;
		for (String s : input) {
			String s2 = s.replaceAll("[a-z]", "");
			final int i = Integer.parseInt(s2.charAt(0) + String.valueOf(s2.charAt(s2.length() - 1)));
			sum+=i;
		}
		System.out.println(sum);
	}

	@Override
	public void solvePartTwo(List<String> input) {

		String[] names = new String[]{"one", "two", "three", "four", "five", "six","seven","eight", "nine"};
		long sum = 0;
		for (String s : input) {
			String val = "";

			outer:
			for (int i = 0; i < s.length(); i++) {
				if (Character.isDigit(s.charAt(i))) {
					val += s.charAt(i);
					break;
				}
				for (int j = 0; j < names.length; j++) {
					if (names[j].equals(s.substring(i, Math.min(i + names[j].length(), s.length())))){
						val += String.valueOf(j+1);
						System.out.println(names[j]);
						break outer;
					}
				}
			}
			outer2:
			for (int i = s.length() - 1; i >= 0 ; i--) {
				if (Character.isDigit(s.charAt(i))) {
					val += s.charAt(i);
					break;
				}
				for (int j = 0; j < names.length; j++) {
					final String substring = s.substring(i, Math.min(i + names[j].length(), s.length()));
					if (names[j].equals(substring)){
						val += String.valueOf(j+1);
						break outer2;
					}
				}
			}
			sum+=Integer.parseInt(val);
		}
		System.out.println(sum);
	}

}
