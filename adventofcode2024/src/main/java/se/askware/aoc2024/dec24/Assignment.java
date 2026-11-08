package se.askware.aoc2024.dec24;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import se.askware.aoc2024.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.PART1, RunMode.PART2).run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Input parsed = getParseInput(input);

		calculate(parsed.rules(), parsed.registry());
		System.out.println(parsed.registry());

		final String prefix = "z";
		final long sum = getValueByPrefix(parsed.registry(), prefix);
		System.out.println(sum);
	}

	private static boolean calculate(List<Rule> rules, Map<String, Long> registry) {
		List<Rule> left = new ArrayList<>(rules);
		while (!left.isEmpty()) {
			List<Rule> next = left.stream().filter(r -> !r.apply(registry)).toList();
			if (next.size() == left.size()) {
				return false; // cycle or unreachable wire
			}
			left = next;
		}
		return true;
	}

	private static Input getParseInput(List<String> input) {
		boolean parseRules = false;
		Pattern p = Pattern.compile("(.+) (.+) (.+) -> (.+)");
		List<Rule> rules = new ArrayList<>();
		Map<String, Long> registry = new HashMap<>();
		for (String line : input) {
			if (line.isBlank()) {
				parseRules = true;
				continue;
			}
			if (!parseRules) {
				final String[] split = line.split(":");
				registry.put(split[0].strip(), Long.parseLong(split[1].strip()));
			} else {
				final Matcher matcher = p.matcher(line);
				matcher.find();
				final Rule rule = new Rule(matcher.group(1), matcher.group(3), Operation.valueOf(matcher.group(2)), matcher.group(4));
				rules.add(rule);
			}
		}
		System.out.println(registry);
		System.out.println(rules);
		Input parsed = new Input(rules, registry);
		return parsed;
	}

	private record Input(List<Rule> rules, Map<String, Long> registry) {
	}

	private static long getValueByPrefix(Map<String, Long> registry, String prefix) {
		final String sum = getBinaryValueByPrefix(registry, prefix);
		//System.out.println(sum);
		return Long.parseLong(sum, 2);
	}

	private static String getBinaryValueByPrefix(Map<String, Long> registry, String prefix) {
		final String sum = registry.entrySet().stream()
				.filter(e -> e.getKey().startsWith(prefix))
				.sorted(Map.Entry.<String, Long>comparingByKey().reversed())
				.map(e -> e.getValue().toString()).collect(Collectors.joining());
		return sum;
	}

	enum Operation {
		OR, XOR, AND;

		public long apply(long a, long b) {
			switch (this) {
				case OR:
					return a | b;
				case XOR:
					return a ^ b;
				case AND:
					return a & b;
			}
			return 0;
		}
	}

	record Rule(String op1, String op2, Operation operation, String result) {

		boolean apply(Map<String, Long> registry) {
			if (registry.containsKey(op1) && registry.containsKey(op2)) {
				final long a = registry.get(op1);
				final long b = registry.get(op2);
				registry.put(result, operation.apply(a, b));
				return true;
			}
			return false;
		}
	}

	@Override
	public void solvePartTwo(List<String> input) {
		
		final Input parsed = getParseInput(input);
		final int numBits = (int) parsed.registry().keySet().stream().filter(k -> k.startsWith("x")).count();
		List<Rule> rules = parsed.rules();
		Set<Integer> bad = findErrors(rules, numBits);
		System.out.println("failing " + bad);
		final List<String> found = new ArrayList<>();

		while (!bad.isEmpty()) {
			final int j = bad.iterator().next();
			final Set<String> start = Set.of(
					register("x", j), register("y", j),
					register("x", j + 1), register("y", j + 1));
			final List<String> candidates = new ArrayList<>(forward(rules, start, 3));

			List<Rule> bestRules = null;
			Set<Integer> bestBad = null;
			String bestA = null;
			String bestB = null;
			for (int a = 0; a < candidates.size(); a++) {
				for (int b = a + 1; b < candidates.size(); b++) {
					final List<Rule> swapped = swap(rules, candidates.get(a), candidates.get(b));
					final Set<Integer> newBad = findErrors(swapped, numBits);
					final boolean improves = !newBad.contains(j) && bad.containsAll(newBad);
					if (improves && (bestBad == null || newBad.size() < bestBad.size())) {
						bestRules = swapped;
						bestBad = newBad;
						bestA = candidates.get(a);
						bestB = candidates.get(b);
					}
				}
			}
			if (bestRules == null) {
				System.out.println("No swap fixes bit " + j);
				return;
			}
			System.out.println("bit " + j + ": swap " + bestA + " <-> " + bestB + ", still failing " + bestBad);
			rules = bestRules;
			bad = bestBad;
			found.add(bestA);
			found.add(bestB);
		}

		System.out.println(found.stream().sorted().collect(Collectors.joining(",")));
	

	}

	private static Set<Integer> findErrors(List<Rule> rules, int numBits) {
		final Set<Integer> failing = new TreeSet<>();
		for (int j = 0; j < numBits; j++) {
			for (long[] test : testCases(j)) {
				if (!addsCorrectly(rules, numBits, test[0], test[1])) {
					failing.add(j);
					break;
				}
			}
		}
		return failing;
	}

	private static boolean addsCorrectly(List<Rule> rules, int numBits, long x, long y) {
		final Map<String, Long> registry = new HashMap<>();
		for (int i = 0; i < numBits; i++) {
			registry.put(register("x", i), (x >> i) & 1);
			registry.put(register("y", i), (y >> i) & 1);
		}
		if (!calculate(rules, registry)) {
			return false;
		}
		return getValueByPrefix(registry, "z") == x + y;
	}

	private static List<long[]> testCases(int j) {
		final long bit = 1L << j;
		final List<long[]> tests = new ArrayList<>();
		tests.add(new long[] { bit, 0 });
		tests.add(new long[] { 0, bit });
		tests.add(new long[] { bit, bit });
		if (j > 0) {
			final long below = 1L << (j - 1);
			tests.add(new long[] { bit | below, below });
			tests.add(new long[] { bit | below, bit | below });
		}
		return tests;
	}

	private static Set<String> forward(List<Rule> rules, Set<String> start, int depth) {
		Set<String> result = new HashSet<>();
		Set<String> front = start;
		for (int d = 0; d < depth; d++) {
			Set<String> f = front;
			front = rules.stream()
					.filter(r -> f.contains(r.op1()) || f.contains(r.op2()))
					.map(Rule::result).collect(Collectors.toSet());
			result.addAll(front);
		}
		return result;
	}

	private static String register(String prefix, int i) {
		return prefix + (i < 10 ? "0" : "") + i;
	}

	List<Rule> swap(List<Rule> rules, String first, String second) {
		return rules.stream().map(r -> {
			if (r.result.equals(first)) {
				return new Rule(r.op1, r.op2, r.operation, second);
			} else if (r.result.equals(second)) {
				return new Rule(r.op1, r.op2, r.operation, first);
			}
			return r;
		}).toList();
	}

}
