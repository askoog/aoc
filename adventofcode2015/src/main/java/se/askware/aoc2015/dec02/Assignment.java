package se.askware.aoc2015.dec02;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import se.askware.aoc2015.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		int sum = 0;
		for (String string : input) {
			String[] vals = string.split("x");
			int l = Integer.parseInt(vals[0]);
			int w = Integer.parseInt(vals[1]);
			int h = Integer.parseInt(vals[2]);
			int area = 2 * l * w + 2 * w * h + 2 * h * l;
			area += Math.min(Math.min(l * w, w * h), h * l);
			sum += area;
			
		}
		System.out.println(sum);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		int sum = 0;
		for (String string : input) {
			int[] array = Arrays.stream(string.split("x")).mapToInt(Integer::parseInt).sorted().toArray();
			int ribbon = 2 * array[0] + 2 * array[1] + (array[0] * array[1] * array[2]);
			sum += ribbon;

		}
		System.out.println(sum);
	}

}
