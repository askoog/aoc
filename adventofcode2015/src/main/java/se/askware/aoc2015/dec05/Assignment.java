package se.askware.aoc2015.dec05;

import java.io.IOException;
import java.util.List;

import se.askware.aoc2015.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		int badStringCount = 0;
		for (String string : input) {

			/*A nice string is one with all of the following properties:
			
			It contains at least three vowels (aeiou only), like aei, xazegov, or aeiouaeiouaeiou.
			It contains at least one letter that appears twice in a row, like xx, abcdde (dd), or aabbccdd (aa, bb, cc, or dd).
			It does not contain the strings ab, cd, pq, or xy, even if they are part of one of the other requirements.
			*/
			String[] badStrings = new String[] { "ab", "cd", "pq", "xy" };
			boolean badstring = false;
			for (String bad : badStrings) {
				if (string.contains(bad)) {
					badstring = true;
				}
			}
			if (!badstring) {
				int vovelcount = 0;
				String vovels = "aeiou";
				for (int i = 0; i < string.length(); i++) {
					if (vovels.contains("" + string.charAt(i))) {
						vovelcount++;
					}
				}
				if (vovelcount < 3) {
					badstring = true;
				}
			}
			if (!badstring) {
				badstring = true;
				for (int i = 1; i < string.length(); i++) {
					if (string.charAt(i - 1) == string.charAt(i)) {
						badstring = false;
						break;
					}
				}
			}
			if (badstring) {
				badStringCount++;
			}
		}
		System.out.println(input.size() - badStringCount);
	}

	/**
	 *
	 */
	/**
	 *
	 */
	@Override
	public void solvePartTwo(List<String> input) {
		int count = 0;
		for (String string : input) {
			/*
			 * It contains a pair of any two letters that appears at least twice in the string without 
			 * overlapping, like xyxy (xy) or aabcdefgaa (aa), but not like aaa (aa, but it overlaps).
			 * 
			 * It contains at least one letter which repeats with exactly one letter between them, like 
			 * xyx, abcdefeghi (efe), or even aaa.	
			 */
			boolean badstring = true;
			for (int i = 1; i < string.length(); i++) {
				String pair = string.substring(i-1, i+1);
				int indexOf = string.indexOf(pair, i + 1);
				if (indexOf >= 0) {
					String substring = string.substring(i - 1, indexOf + 2);
					System.out.println(
							string + " " + pair + " " + substring);
					for (int j = 0; j < string.length() - 2; j++) {
						if (string.charAt(j) == string.charAt(j + 2)) {
							System.out.println(
									string + " " + pair + " " + substring + " " + string.substring(j, j + 3));
							badstring = false;
						}
					}
				}

			}
			if (!badstring) {
				count++;
			} else {
				System.out.println(string);
			}
		}
		System.out.println(count);
	}

}
