package se.askware.aoc2024.dec09;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.askware.aoc2024.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	record Block(int index, int length) {
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Block[] refs = parse(input);
		int spaceIndex = 0;
		for (int i = refs.length - 1; i >= 0; i--) {
			if (refs[i] != null ) {
				while (spaceIndex < refs.length && refs[spaceIndex] != null) {
					spaceIndex++;
				}
				if (spaceIndex < i) {
					Block b = refs[i];
					refs[i] = null;
					refs[spaceIndex] = b;
				} else {
					break;
				}
			}
		}
		/*for (int j = 0; j < refs.length; j++) {
			if (refs[j] == null) {
				System.out.print('.');
			} else {
				System.out.print(refs[j].index);
			}
		}
		System.out.println();*/
		long sum = 0;
			for (int j = 0; j < refs.length; j++) {
			if (refs[j] != null) {
				sum += (long)refs[j].index * j;
			}
		}
		System.out.println(sum);
	}

	private static Block[] parse(List<String> input) {
		final String line = input.get(0);
		Block[] refs = new Block[100_000];
		List<Block> blocks = new ArrayList<>();
		int index = 0;

		for (int i = 0; i < line.length(); i++) {
			if (i % 2 == 0) {
				final int i1 = line.charAt(i) - '0';
				Block block = new Block(i / 2, i1);
				for (int j = 0; j < i1; j++) {
					refs[index++] = block;
				}
			} else {
				final int i1 = line.charAt(i) - '0';
				for (int j = 0; j < i1; j++) {
					//System.out.print('.');
					index++;
				}
			}
		}
		refs = Arrays.copyOfRange(refs, 0, index + 1);
		return refs;
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final Block[] refs = parse(input);
		for (int i = refs.length - 1; i>= 0; i--) {

			if (refs[i] != null) {
				Block b = refs[i];
				int zeroCount=0;

				int spaceIndex = 0;
				for (; spaceIndex < refs.length && zeroCount != b.length(); spaceIndex++) {
					if (refs[spaceIndex] == null){
						zeroCount++;
					} else {
						zeroCount = 0;
					}
				}
				spaceIndex-= b.length;
				//System.out.println(spaceIndex + " " + zeroCount);
				if (spaceIndex < i && zeroCount == refs[i].length()) {
					for (int j = 0; j < b.length; j++) {
						refs[spaceIndex + j] = b;
						refs[i - j] = null;
					}
					//print(refs);
				}
			}

		}
		//print(refs);
		long sum = 0;
		for (int j = 0; j < refs.length; j++) {
			if (refs[j] != null) {
				sum += (long)refs[j].index * j;
			}
		}
		System.out.println(sum);
	}

	private static void print(Block[] refs) {
		for (int j = 0; j < refs.length; j++) {
			if (refs[j] == null) {
				System.out.print('.');
			} else {
				System.out.print(refs[j].index);
			}
		}
		System.out.println();
	}

}
