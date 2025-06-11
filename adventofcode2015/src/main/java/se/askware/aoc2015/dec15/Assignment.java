package se.askware.aoc2015.dec15;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.askware.aoc2015.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		Pattern p = Pattern.compile("(.+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)");
		final List<Ingredient> list = input.stream().map(line -> {
			final Matcher matcher = p.matcher(line);
			matcher.find();
			return new Ingredient(matcher.group(1), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)),
					Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)));
		}).toList();

		Queue<int[]> queue = new ArrayDeque<>();
		final int[] e = new int[list.size()];
		e[0] = 100;
		queue.add(e);

		long max = 0;
		while (!queue.isEmpty()) {
			final int[] poll = queue.poll();

			int[] sums = new int[4];
			if (poll[0] == 44 && poll[1] == 56) {
				System.out.println("here");
			}
			for (int i = 0; i < poll.length; i++) {
				final Ingredient ingredient = list.get(i);
				sums[0] += ingredient.capacity * poll[i];
				sums[1] += ingredient.durability * poll[i];
				sums[2] += ingredient.flavor * poll[i];
				sums[3] += ingredient.texture * poll[i];
				//sums[4] = ingredient.calories * poll[i];
			}
			for (int i = 0; i < sums.length; i++) {
				if (sums[i] < 0){
					sums[i] = 0;
				}
			}
			final long sum = mul(sums);
			if (sum > max) {
				max = sum;
				System.out.println(max+ " " + Arrays.toString(poll));
			}

			//System.out.println(Arrays.toString(poll));
			if (poll[0] > 0) {
				for (int i = 1; i < list.size(); i++) {
					final int[] ints = Arrays.copyOf(poll, poll.length);
					ints[0]--;
					ints[i]++;
					queue.add(ints);
				}
			}

		}
		System.out.println(max);

	}

	long mul(int[] array) {
		return Arrays.stream(array).reduce(1, (i1, i2) -> i1 * i2);
	}

	int sum(int[] array) {
		return Arrays.stream(array).sum();
	}

	record Ingredient(String name, int capacity, int durability, int flavor, int texture, int calories) {
	}

	@Override
	public void solvePartTwo(List<String> input) {
	}

}
