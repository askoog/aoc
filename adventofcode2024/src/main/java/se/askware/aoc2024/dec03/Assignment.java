package se.askware.aoc2024.dec03;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.askware.aoc2024.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		String regex = "mul\\((\\d+),(\\d+)\\)";
		Pattern p = Pattern.compile(regex);
		long result = 0;
		for (String line : input) {
			final Matcher matcher = p.matcher(line);
			while (matcher.find()){
				//System.out.println(matcher.group(1));
				//System.out.println(matcher.group(2));
				result += Long.parseLong(matcher.group(1)) * Long.parseLong(matcher.group(2));
			}
		}
		System.out.println(result);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		String regex = "(mul\\((\\d+),(\\d+)\\))|(don't\\(\\))|(do\\(\\))";
		Pattern p = Pattern.compile(regex);
		long result = 0;
			boolean dont = false;
		for (String line : input) {
			final Matcher matcher = p.matcher(line);
			while (matcher.find()) {
				System.out.println(matcher.group());
				if (matcher.group().equals("don't()")) {
					dont = true;
				} else if (matcher.group().equals("do()")) {
					dont = false;
				} else {
					if (!dont) {
						result += Long.parseLong(matcher.group(2)) * Long.parseLong(matcher.group(3));
					} else {
						System.out.println("Skipping: " + matcher.group(2) + " * " + matcher.group(3));
					}
					//dont = false;
				}
			}
		}
		System.out.println(result);
	}

}
