package se.askware.aoc2024.dec11;

import java.io.IOException;
import java.util.ArrayList;
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
		int numBlinks = 25;
		final List<Integer> intList = toIntList(input.get(0));
		int count = 0;
		for (int j = 0; j < intList.size(); j++) {
			long n = intList.get(j);
			List<Long> blinks = new ArrayList<>();
			blinks.add(n);
			for (int i = 0; i < numBlinks; i++) {
				List<Long> newBlinks = new ArrayList<>();
				for (Long blink : blinks) {
					newBlinks.addAll(blink(blink));
				}
				blinks = newBlinks;
			}
			count += blinks.size();
		}
		System.out.println(count);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		int numBlinks = 75;
		for (int i = 0; i <= numBlinks; i++) {
			cache.add(new HashMap<>());
		}
		final List<Integer> intList = toIntList(input.get(0));
		long count = 0;
		for (Integer integer : intList) {
			count += blink(integer, numBlinks);
		}
		System.out.println(count);
		System.out.println(cacheMisses);
		System.out.println(cacheTries);
	}

	List<Map<Long,Long>> cache = new ArrayList<>();
	AtomicLong cacheMisses = new AtomicLong();
	AtomicLong cacheTries = new AtomicLong();

	private long blink(long stone, int numBlinks) {
		if (numBlinks == 0) {
			return 1;
		}
		cacheTries.incrementAndGet();
		Map<Long, Long> c = cache.get(numBlinks);
		return c.computeIfAbsent(stone, s -> {
			cacheMisses.incrementAndGet();
			List<Long> blinks = blink(s);
			return blinks.stream().mapToLong(b -> blink(b, numBlinks - 1)).sum();
		});
	}

	private List<Long> blink(long stone) {
		/*
		If the stone is engraved with the number 0, it is replaced by a stone engraved with the number 1.
If the stone is engraved with a number that has an even number of digits, it is replaced by two stones. The left half of the digits are engraved on the new left stone, and the right half of the digits are engraved on the new right stone. (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)
If none of the other rules apply, the stone is replaced by a new stone; the old stone's number multiplied by 2024 is engraved on the new stone.
		 */
		if (stone == 0) {
			return List.of(1L);
		} else {
			String s = Long.toString(stone);
			if (s.length() % 2 == 0) {
				int half = s.length() / 2;
				long left = Long.parseLong(s.substring(0, half));
				long right = Long.parseLong(s.substring(half));
				return List.of(left, right);
			} else {
				return List.of(stone * 2024);
			}
		}
	}
}
