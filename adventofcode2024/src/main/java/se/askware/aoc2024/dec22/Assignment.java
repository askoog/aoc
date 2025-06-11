package se.askware.aoc2024.dec22;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import se.askware.aoc2024.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		/*
		In particular, each buyer's secret number evolves into the next secret number in the sequence via the following process:

Calculate the result of multiplying the secret number by 64.
 Then, mix this result into the secret number.
 Finally, prune the secret number.

Calculate the result of dividing the secret number by 32.
Round the result down to the nearest integer.
Then, mix this result into the secret number.
Finally, prune the secret number.

Calculate the result of multiplying the secret number by 2048.
Then, mix this result into the secret number.
 Finally, prune the secret number.

Each step of the above process involves mixing and pruning:

To mix a value into the secret number,
calculate the bitwise XOR of the given value and the secret number.
Then, the secret number becomes the result of that operation.
(If the secret number is 42 and you were to mix 15 into the secret number, the secret number would become 37.)

To prune the secret number, calculate the value of the secret number modulo 16777216.
Then, the secret number becomes the result of that operation.
(If the secret number is 100000000 and you were to prune the secret number, the secret number would become 16113920.)
		 */

		final int iterations = 2000;
		long sum = 0;
		for (String line : input) {
			long l = getSecret(iterations, Long.parseLong(line));
			//System.out.println(l);
			sum += l;
		}

		System.out.println(sum);
	}

	private long getSecret(int iterations, long secret) {
		for (int i = 0; i < iterations; i++) {
			secret = calculateNext(secret);
		}
		return secret;
	}

	private long[] getSecrets(int iterations, long secret) {
		long[] secrets = new long[iterations];
		long current = secret;
		for (int i = 0; i < iterations; i++) {
			current = calculateNext(current);
			secrets[i] = current;
		}
		return secrets;
	}

	private long calculateNext(long secret) {
		final long l = prune(mix(secret, secret * 64));
		final long l2 = prune(mix(l, l / 32));
		final long l3 = prune(mix(l2, l2 * 2048));
		return l3;
	}

	long mix(long secret, long value) {
		return secret ^ value;
	}

	long prune(long secret) {
		return secret % 16777216;
	}

	@Override
	public void solvePartTwo(List<String> input) {

		final int iterations = 2000;
		long secret;
		List<BananaPrice> bananaList = new ArrayList<>();
		for (String line : input) {
			secret = Long.parseLong(line);
			long digit = secret % 10;
			long[] secrets = getSecrets(iterations, secret);
			final long[] prices = Arrays.stream(secrets).map(s -> s % 10).toArray();
			long[] diff = new long[prices.length];
			diff[0] = prices[0] - digit;
			for (int j = 1; j < prices.length; j++) {
				diff[j] = prices[j] - prices[j - 1];
			}
			final BananaPrice price = new BananaPrice(prices, diff);
			bananaList.add(price);
		}

		Map<String, AtomicLong> map = new HashMap<>();
		bananaList.forEach(b -> b.addToMap(map));
		map.entrySet().stream().max(Comparator.comparingLong(e -> e.getValue().get())).ifPresent(System.out::println);

	}

	record BananaPrice(long[] prices, long[] diffs) {

		void addToMap(Map<String, AtomicLong> map){
			Map<String,AtomicLong> myMap = new HashMap<>();
			for (int i = 4; i <= diffs.length; i++) {
				final String longs = Arrays.toString(Arrays.copyOfRange(diffs, i - 4, i));
				if (!myMap.containsKey(longs)) {
					final long price = prices[i - 1];
					final AtomicLong atomicInteger = new AtomicLong(price);
					myMap.put(longs, atomicInteger);
				}
			}
			myMap.forEach((k,v) -> map.computeIfAbsent(k, s -> new AtomicLong()).addAndGet(v.get()));
		}

		@Override
		public String toString() {
			return Arrays.toString(prices) + " " + Arrays.toString(diffs);
		}
	}

}
