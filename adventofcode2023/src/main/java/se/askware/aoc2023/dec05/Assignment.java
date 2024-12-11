package se.askware.aoc2023.dec05;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.askware.aoc2023.common.AocBase;
import se.askware.aoc2023.common.Range;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.PART2).run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		Input result = getInput(input);

		long lowest = Long.MAX_VALUE;
		String from = "seed";
		String to = "location";
		for (long seed : result.seeds()) {
			String key = from;
			long val = seed;
			while (!key.equals(to)) {
				final SeedMap seedMap = result.maps().get(key);
				val = seedMap.map(val);
				key = seedMap.dest;
			}
			System.out.printf("%s ----> %s\n", seed, val);
			lowest = Long.min(lowest, val);
		}
		System.out.println(lowest);
	}

	private static Input getInput(List<String> orgInput) {
		List<String> input = new ArrayList<>(orgInput);
		System.out.println(input);
		final long[] seeds = Arrays.stream(input.remove(0).split(":")[1].trim().split(" "))
				.mapToLong(Long::parseLong)
				.toArray();
		input.remove(0);
		Map<String, SeedMap> maps = new HashMap<>();
		SeedMap current = null;
		for (int i = 0; i < input.size(); i++) {
			String s = input.get(i);
			if (s.isEmpty()) {

			} else if (s.contains("map:")) {
				current = new SeedMap(s.split(" ")[0]);
				maps.put(current.name, current);
			} else {
				current.mapRange(s);
			}
		}
		return new Input(seeds, maps);
	}

	private record Input(long[] seeds, Map<String, SeedMap> maps) {
	}

	@Override
	public void solvePartTwo(List<String> input) {
		Input result = getInput(input);

		long lowest = Long.MAX_VALUE;
		String from = "seed";
		String to = "location";

		for (int i = 0; i < result.seeds.length; i += 2) {
			final Range range = new Range(result.seeds[i], result.seeds[i] + result.seeds[i + 1] - 1);

			String key = from;
			List<Range> map = List.of(range);
			while (!key.equals(to)) {
				final SeedMap seedMap = result.maps().get(key);
				List<Range> nextMap = new ArrayList<>();
				for (Range r : map) {
					nextMap.addAll(seedMap.map(r));
				}
				System.out.println(nextMap);
				System.out.println("-----");
				map = nextMap;
				key = seedMap.dest;
			}
			System.out.println(i);
			//System.out.printf("%s ----> %s\n", seed, val);
			lowest = Math.min(lowest, map.stream().mapToLong(r -> r.start).min().orElse(Long.MAX_VALUE));
		}

		System.out.println(lowest);
	}

	private static class SeedMap {

		private String name;
		private String dest;
		Map<Long, Long> map = new HashMap<>();
		List<long[]> ranges = new ArrayList<>();

		public SeedMap(String s) {
			final String[] split = s.split("-");
			this.name = split[0];
			this.dest = split[2];
		}

		public void mapRange(String s) {
			final long[] vals = Arrays.stream(s.split(" ")).mapToLong(Long::parseLong).toArray();
			ranges.add(vals);
		}

		public List<Range> map(Range in) {
			List<Range> result = new ArrayList<>();
			List<Range> todo = new ArrayList<>();
			todo.add(in);
			System.out.println("mapping " + in);
			for (long[] range : ranges) {
				System.out.println("     " + range[0] + " " + range[1] + " "+range[2]);
			}
			while (!todo.isEmpty()) {
				Range rangeToMap = todo.remove(0);
				boolean found = false;
				for (long[] range : ranges) {
					Range r2 = new Range(range[1], range[1] + range[2] - 1);
					System.out.print(r2 + " " + rangeToMap);
					if (r2.covers(rangeToMap)) {
						System.out.println(" covers ");
						long rangeOffset = rangeToMap.start - r2.start;
						long rangeLength = rangeToMap.end - rangeToMap.start;
						final Range rp = new Range(range[0] + rangeOffset, range[0] + rangeOffset + rangeLength);
						System.out.println(" --> " +rp);
						result.add(rp);
						found = true;
						break;
					} else if (r2.overlaps(rangeToMap)) {
						System.out.println(" overlaps");
						if (r2.start <= rangeToMap.start) {
							Range rp1 = new Range(rangeToMap.start, r2.end);
							Range rp2 = new Range(r2.end + 1, rangeToMap.end);
							System.out.println(" --> " +rp1 + "," + rp2);
							todo.add(rp1);
							todo.add(rp2);
						} else {
							Range rp1 = new Range(rangeToMap.start, r2.start - 1);
							Range rp2 = new Range(r2.start, rangeToMap.end);
							System.out.println(" --> " +rp1 + "," + rp2);
							todo.add(rp1);
							todo.add(rp2);
						}
						found = true;
						break;
					} else {
						System.out.println(" ?");
					}
				}
				if (!found){
					System.out.println("no match");
					result.add(in);
				}
			}
			return result;
		}

		public Long map(Long key) {
			for (long[] range : ranges) {
				if (range[1] <= key && range[1] + range[2] >= key) {
					long rangeOffset = key - range[1];
					return range[0] + rangeOffset;
				}
			}
			return key;
			/*for (long i = 0; i < vals[2]; i++) {
				map.put(vals[1] + i, vals[0] + i);

			}

			return map.getOrDefault(key, key);*/
		}

		@Override
		public String toString() {
			return "SeedMap{" +
					"name='" + name + '\'' +
					", dest='" + dest + '\'' +
					", map=" + map +
					'}';
		}
	}

}
