package se.askware.aoc2023.dec19;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import se.askware.aoc2023.common.AocBase;
import se.askware.aoc2023.common.Range;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.values()).run();
	}

	@Override
	public void solvePartOne(List<String> input) {

		ParseResult result = parse(input);

		List<Map<String, Long>> accepted = new ArrayList<>();
		List<Map<String, Long>> rejected = new ArrayList<>();

		for (Map<String, Long> var : result.vars()) {
			String next = "in";
			boolean done = false;
			debug(var);
			List<Rule> evaluated = new ArrayList<>();
			while (!done) {
				debug(next);
				if (next.equals("A")) {
					accepted.add(var);
					done = true;
				} else if (next.equals("R")) {
					rejected.add(var);
					done = true;
				} else {
					List<Rule> current = result.rules().get(next);
					debug(current);
					if (current == null) {
						throw new IllegalArgumentException(next);
					}
					for (Rule rule : current) {
						if (rule.condition == '>') {
							if (var.get(rule.varName) > rule.value) {
								next = rule.result;
								evaluated.add(rule);
								break;
							} else {
								evaluated.add(new Rule(rule.varName, '<', rule.value, "?"));
							}
						} else if (rule.condition == '<') {
							if (var.get(rule.varName) < rule.value) {
								evaluated.add(rule);
								next = rule.result;
								break;
							} else {
								evaluated.add(new Rule(rule.varName, '>', rule.value, "?"));
							}
						} else if (rule.condition == '.') {
							next = rule.result;
							break;
						} else {
							throw new IllegalArgumentException(rule.toString());
						}
					}
				}
			}
			debug("Evaluated: " + evaluated);
			for (Rule rule : evaluated) {
				debug(rule.varName + rule.condition + rule.value);
			}
		}
		debug(accepted);
		for (Map<String, Long> stringLongMap : accepted) {
			debug(stringLongMap);
		}
		debug(rejected);

		final long sum = accepted.stream().flatMapToLong(a -> a.values().stream().mapToLong(l -> l)).sum();
		System.out.println(sum);
	}

	private static ParseResult parse(List<String>	 input) {
		Map<String, List<Rule>> rules = new HashMap<>();
		List<Map<String, Long>> vars = new ArrayList<>();
		for (String s : input) {
			if (!s.isEmpty()) {
				if (s.startsWith("{")) {
					Map<String, Long> values = new HashMap<>();
					final String[] ruleArr = s.substring(s.indexOf('{') + 1, s.length() - 1).split(",");
					for (String s1 : ruleArr) {
						final String[] split = s1.split("=");
						values.put(split[0], Long.parseLong(split[1]));
					}
					vars.add(values);
				} else {
					final String identifier = s.substring(0, s.indexOf('{'));
					final String[] ruleArr = s.substring(s.indexOf('{') + 1, s.length() - 1).split(",");
					for (String rule : ruleArr) {
						if (rule.indexOf(":") > 0) {

							final String[] condition = rule.split(":");
							String result = condition[1];
							final String cond = condition[0];
							if (cond.indexOf('<') > 0) {
								final String[] split = cond.split("<");
								String varName = split[0];
								long value = Long.parseLong(split[1]);
								Rule r = new Rule(varName, '<', value, result);
								rules.computeIfAbsent(identifier, str -> new ArrayList<>()).add(r);
							} else if (cond.indexOf('>') > 0) {
								final String[] split = cond.split(">");
								String varName = split[0];
								long value = Long.parseLong(split[1]);
								Rule r = new Rule(varName, '>', value, result);
								rules.computeIfAbsent(identifier, str -> new ArrayList<>()).add(r);
							} else {
								throw new IllegalArgumentException(cond);
							}
						} else {
							rules.computeIfAbsent(identifier, str -> new ArrayList<>()).add(new Rule("", '.', 0L, rule));

						}
					}
				}
			}
		}
		debug(rules);
		debug(vars);
		return new ParseResult(rules, vars);
	}

	private record ParseResult(Map<String, List<Rule>> rules, List<Map<String, Long>> vars) {
	}

	record Rule(String varName, char condition, long value, String result) {
	}

	private static class Path {
		List<Rule> rules = new ArrayList<>();

		String getNext() {
			return rules.isEmpty() ? "in" : rules.get(rules.size() - 1).result;
		}

		Path copy() {
			final Path path = new Path();
			path.rules.addAll(rules);
			return path;
		}

		@Override
		public String toString() {
			return "Path{\n" +
					rules.stream().map(r -> "  " + r.varName + r.condition + r.value).collect(Collectors.joining("\n")) +
					"\n}";
		}
	}

	@Override
	public void solvePartTwo(List<String> input) {
		ParseResult result = parse(input);

		Map<String, Span> spans = new HashMap<>();
		String[] vars = new String[] { "x", "m", "a", "s" };
		for (String var : vars) {
			spans.put(var,new Span(0,4001));
		}
		final long sum = sumPaths(result.rules, spans, "in");
		System.out.println(sum);

	}

	public long sumPaths(Map<String, List<Rule>> ruleList, Map<String, Span> spans, String ruleId){
		long sum = 0;
		final List<Rule> rules = ruleList.get(ruleId);
		for (Rule rule : rules) {
			if (rule.condition == '.'){
				sum += sumEnd(ruleList, spans, rule);
			} else if (rule.condition == '<') {
				Map<String, Span> nextRanges = new HashMap<>(spans);
				Span c = spans.get(rule.varName);
				nextRanges.put(rule.varName, c.lessThan(rule.value));
				spans.put(rule.varName, c.moreThan(rule.value - 1));
				sum += sumEnd(ruleList, nextRanges,rule);

			} else if (rule.condition == '>') {
				Map<String, Span> nextRanges = new HashMap<>(spans);
				Span c = spans.get(rule.varName);
				nextRanges.put(rule.varName, c.moreThan(rule.value));
				spans.put(rule.varName, c.lessThan(rule.value + 1));
				sum += sumEnd(ruleList, nextRanges,rule);
			} else {
				throw new RuntimeException();
			}
		}
		return sum;
	}

	public long sumEnd(Map<String, List<Rule>> ruleList, Map<String, Span> ranges, Rule rule) {
		if (rule.result.equals("A")) {
			return ranges.values().stream().mapToLong(Span::numItems).reduce(1L, (l1, l2) -> l1 * l2);
		} else if (rule.result.equals("R")) {
			return 0;
		} else {
			return sumPaths(ruleList, ranges, rule.result);
		}
	}

	record Span(long moreThan, long lessThan){
		Span moreThan(long moreThan){
			return new Span(Math.max(this.moreThan, moreThan), lessThan);
		}
		Span lessThan(long lessThan){
			return new Span(moreThan, Math.min(this.lessThan, lessThan));
		}

		long numItems(){
			if (moreThan > lessThan){
				return 0;
			}
			return lessThan - moreThan - 1;
		}
	}

}

