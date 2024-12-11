package se.askware.aoc2024.dec02;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.askware.aoc2024.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		int count = 0;
		for (String s : input) {
			final int[] s1 = Arrays.stream(s.split(" ")).mapToInt(Integer::parseInt).toArray();
			//System.out.println(isAllIncreasingOrDecreasing(s1));
			//System.out.println(isSafe(s1));
			//System.out.println();
			count += isSafe(s1) && isAllIncreasingOrDecreasing(s1) ? 1 : 0;
		}
		System.out.println(count);
	}

	private boolean isAllIncreasingOrDecreasing(int[] row){
		Set<Boolean> increasing = new HashSet<>();
		for (int j = 0; j < row.length - 1; j++) {
			if (row[j] == row[j+1]) {
				return false;
			}
			increasing.add(row[j] < row[j+1]);
		}
		//System.out.println(increasing);
		return increasing.size() == 1;
	}

	private boolean isSafe(int[] row){
		for (int j = 0; j < row.length - 1; j++) {
			if (Math.abs(row[j] - row[j + 1]) > 3) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void solvePartTwo(List<String> input) {

		int count = 0;
		for (String s : input) {
			final int[] s1 = Arrays.stream(s.split(" ")).mapToInt(Integer::parseInt).toArray();
			//System.out.println(isAllIncreasingOrDecreasing(s1));
			//System.out.println(isSafe(s1));
			//System.out.println();
			if (isSafe(s1) && isAllIncreasingOrDecreasing(s1)) {
				count++;
			} else {
				count += isSafeWithModification(s1) ? 1 : 0;

			}
		}
		System.out.println(count);
	}

	private boolean isSafeWithModification(int[] s1) {
		for (int i = 0; i < s1.length; i++) {
			List<Integer> list = new ArrayList<>(Arrays.stream(s1).boxed().toList());
			list.remove(i);
			final int[] array = list.stream().mapToInt(Integer::intValue).toArray();
			if (isSafe(array) && isAllIncreasingOrDecreasing(array)) {
				return true;
			}
		}
		return false;
	}

}
