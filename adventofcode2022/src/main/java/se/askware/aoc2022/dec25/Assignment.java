package se.askware.aoc2022.dec25;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.plaf.ColorUIResource;

import se.askware.aoc2022.common.AocBase;
import se.askware.aoc2022.common.CharCell;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		System.out.println(parseSnafu("1=0=0=02000-0-0=2-120"));
		final long snafu = input.stream().mapToLong(this::parseSnafu).sum();
		System.out.println(snafu);
		final String s = toSnafu(snafu);
		System.out.println(s);
		System.out.println(parseSnafu(s));
	}

	Map<Character, Integer> conversion = Map.of('1', 1,
			'2', 2,
			'0', 0,
			'-', -1,
			'=', -2);

	Map<Integer, Character> conversion2 = conversion.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));

	private long parseSnafu(String s) {
		long sum = 0;
		long multiplier = 1;
		for (int i = s.length() - 1; i >= 0; i--) {
			sum += (conversion.get(s.charAt(i)) * multiplier);
			multiplier *= 5;
//			System.out.println(sum);
		}
		//System.out.println(s + "=" + sum);
		return sum;
	}

	private String toSnafu(long number){


		long multiplier = 5;
		long n = number;
		List<Integer> nums = new ArrayList<>();
		while(n > 0){
			//System.out.println(n / 5);
			//System.out.println(n % 5);
			nums.add((int)(n%5));
			//System.out.println();
			n = n/5;
		}
		//Collections.reverse(nums);
		StringBuilder sb = new StringBuilder();
		int carry = 0;
		for (Integer num : nums) {
			//System.out.println(num);
			int n2 = num + carry;
			if (n2 == 3){
				sb.append('=');
				carry = 1;
			} else if (n2 == 4) {
				sb.append('-');
				carry = 1;
			} else if (n2 == 5) {
				sb.append('0');
				carry = 1;
			} else {
				sb.append(n2);
				carry = 0;
			}
		}
		if (carry > 0){
			sb.append(carry);
		}
		sb.reverse();
		System.out.println(sb);
		return sb.toString();
	}

	@Override
	public void solvePartTwo(List<String> input) {
	}

}
