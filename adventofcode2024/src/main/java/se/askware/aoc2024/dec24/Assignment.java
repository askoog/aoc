package se.askware.aoc2024.dec24;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import se.askware.aoc2024.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
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

	private static void calculate(List<Rule> rules, Map<String, Long> registry) {
		final ArrayList<Rule> rules1 = new ArrayList<>(rules);
		while (!rules1.isEmpty()) {
			for (Rule rule : rules1) {
				if (rule.apply(registry)) {
					rules1.remove(rule);
					break;
				}
			}
		}
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

		final Map<String, Long> registry = new HashMap<>(parsed.registry());
		final long x = getValueByPrefix(registry, "x");
		final long y = getValueByPrefix(registry, "y");
		System.out.println("x: " + x + " y: " + y + ", sum " + (x + y));
		System.out.println("x:" + getBinaryValueByPrefix(registry, "x"));
		System.out.println("y:" + getBinaryValueByPrefix(registry, "y"));
		long wanted = x + y;
		String binaryStringWanted = Long.toBinaryString(wanted);
		System.out.println("z:" + binaryStringWanted);

		final List<String> list = parsed.rules.stream().flatMap(r -> Stream.of(r.op1, r.op2, r.result)).distinct().sorted().toList();
		//		System.out.println(list.size());

		calculate(parsed.rules(), registry);
		long sum = getValueByPrefix(registry, "z");
		//System.out.println("sum "+ sum + " " + Long.toBinaryString(sum));

		final long numDigits = registry.entrySet().stream()
				.filter(e -> e.getKey().startsWith("z"))
				.count();

		if (binaryStringWanted.length() > numDigits) {
			binaryStringWanted = binaryStringWanted.substring(binaryStringWanted.length() - (int) numDigits);
		}

		String binaryStringResult = Long.toBinaryString(sum);
		System.out.println("w:" + binaryStringWanted);
		if (binaryStringWanted.length() != binaryStringResult.length()) {
			int diff = binaryStringWanted.length() - binaryStringResult.length();
			for (int i = 0; i < diff; i++) {
				binaryStringResult = "0" + binaryStringResult;
			}
		}
		System.out.println("r:" + binaryStringResult);

		findErrors(numDigits, parsed);

		Set<String> wrongZ = new HashSet<>();
		for (int i = 0; i < binaryStringWanted.length(); i++) {
			if (binaryStringWanted.charAt(i) != binaryStringResult.charAt(i)) {
				System.out.println(i + " " + binaryStringWanted.charAt(i) + " " + binaryStringResult.charAt(i));
				wrongZ.add(register("z", i));
			}
		}
		System.out.println(wrongZ);
		for (String output : wrongZ) {
			final Set<Rule> rules = findInvolvedRules(output, parsed);
			System.out.println(rules);
		}

		final List<Rule> swap = swap(List.of(new WireSwap("z05", "z00"), new WireSwap("z02", "z01")), parsed.rules);
		System.out.println(swap);
		final Map<String, Long> registry1 = new HashMap<>(parsed.registry);
		calculate(swap, registry1);
		final long z = getValueByPrefix(registry1, "z");
		System.out.println("RESULT " + z + " " + Long.toBinaryString(z));

		Set<WireSwap> swaps = new HashSet<>();
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {

				String w1 = list.get(i);
				String w2 = list.get(j);
				swaps.add(w1.compareTo(w2) > 0 ? new WireSwap(w1, w2) : new WireSwap(w2, w1));
			}
		}
		System.out.println(swaps.size());

		/*Set<Set<WireSwap>> swaps2 = new HashSet<>();
		List<WireSwap> swapList = new ArrayList<>(swaps);
		for (int i = 0; i < swapList.size(); i++) {
			for (int j = i+1; j < swapList.size(); j++) {
				for (int k = i+j+1; k < swapList.size(); k++) {
					for (int l = i+j+k+1; l < swapList.size(); l++) {
						Set<WireSwap> set = new HashSet<>();
						set.add(swapList.get(i));
						set.add(swapList.get(j));
						set.add(swapList.get(k));
						set.add(swapList.get(l));
						swaps2.add(set);
					}
				}
			}
		System.out.println(swaps2.size());
		}*/

	}

	private static Set<Rule> findInvolvedRules(String registry, Input parsed) {
		Set<Rule> rules = new HashSet<>();
		Queue<String> queue = new ArrayDeque<>();
		queue.add(registry);
		while (!queue.isEmpty()) {
			String current = queue.poll();
			parsed.rules.stream()
					.filter(r -> r.result.equals(current))
					.forEach(r -> {
						if (rules.add(r)) {
							queue.add(r.op1);
							queue.add(r.op2);
							queue.add(r.result);
						}
					});
		}
		return rules;
	}

	private static void findErrors(long numDigits, Input parsed) {
		for (int j = 0; j < numDigits; j++) {

			final Map<String, Long> registry2 = new HashMap<>(parsed.registry);
			String expected = "";
			for (int i = 0; i < numDigits; i++) {
				registry2.put(register("x", i), 0L);
				registry2.put(register("y", i), 0L);
				if (i == j+1) {
					expected = "1" + expected;
				} else {
					expected = "0" + expected;
				}
			}
			registry2.put(register("x", j), 1L);
			registry2.put(register("y", j), 1L);
			calculate(parsed.rules(), registry2);
			String sum2 = getBinaryValueByPrefix(registry2, "z");
			if (expected.equals(sum2)) {
				System.out.println("OK " + j);
			} else {
				System.out.println("FAIL " + j + " \n exp " + expected + " \n act " + sum2);
				final Set<Rule> z = findInvolvedRules(register("z", j), parsed);
				final Set<String> list = z.stream().map(r -> r.result).collect(Collectors.toSet());
				System.out.println(list);
			}
		}
	}

	private static String register(String prefix, int i) {
		return prefix + (i < 10 ? "0" : "") + i;
	}

	private List<Rule> swap(List<WireSwap> swaps, List<Rule> rules) {
		List<Rule> result = rules;
		for (WireSwap swap : swaps) {
			result = swap(result, swap.from, swap.to);
		}
		return result;
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

	record WireSwap(String from, String to) {
	}
}
