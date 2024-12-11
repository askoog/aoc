package se.askware.aoc2023.dec19;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import se.askware.aoc2023.common.AocBase;

public class Assignment2 extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment2().withRunMode(RunMode.EXAMPLE2).run();
	}

	@Override
	public void solvePartOne(List<String> input) {

	}

	@Override
	public void solvePartTwo(List<String> input) {
		var workflows = parse(input);
		System.out.println(workflows);
		final long result = countAccepted(workflows,
				new HashMap<>("xmas".chars().boxed().collect(Collectors.toMap(e -> (char) e.intValue(), e -> Constraint.create()))),
				"in");
		System.out.println(result);
	}

	public record WorkflowItem(char c, char op, long n, String s) {
		public WorkflowItem(String s) {
			this(' ', ' ', 0, s);
		}
	}

	public record Part(Map<Character, Long> numbers) {
		public Part(List<Number> numbers) {
			this(numbers.stream().collect(Collectors.toMap(n -> n.c, n -> n.n)));
		}
	}

	public record Number(char c, long n) {
	}

	public record Constraint(long moreThan, long lessThan) {
		public static Constraint create() {
			return new Constraint(0, 4001);
		}

		public Constraint moreThan(long moreThan) {
			return new Constraint(Math.max(this.moreThan, moreThan), lessThan);
		}

		public Constraint lessThan(long lessThan) {
			return new Constraint(moreThan, Math.min(this.lessThan, lessThan));
		}

		public long numsAccepted() {
			if (moreThan > lessThan) {
				return 0;
			}
			return lessThan - moreThan - 1;
		}
	}

	private boolean isAccepted(Map<String, List<WorkflowItem>> workflows, Part p, String workflow) {
		var items = workflows.get(workflow);
		for (var item : items) {
			if (item.c == ' ') {
				return checkItem(workflows, p, item);
			} else {
				if (item.op == '<') {
					if (p.numbers.get(item.c) < item.n) {
						return checkItem(workflows, p, item);
					}
				} else if (item.op == '>') {
					if (p.numbers.get(item.c) > item.n) {
						return checkItem(workflows, p, item);
					}
				} else {
					throw new IllegalStateException("Unknown operator: " + item.op);
				}
			}
		}
		throw new IllegalStateException("Reached end = impossible");
	}

	private boolean checkItem(Map<String, List<WorkflowItem>> workflows, Part p, WorkflowItem item) {
		if (item.s.equals("A")) {
			return true;
		} else if (item.s.equals("R")) {
			return false;
		} else {
			return isAccepted(workflows, p, item.s);
		}
	}

	private long countAccepted(Map<String, List<WorkflowItem>> workflows, Map<Character, Constraint> constraints, String workflow) {
		var items = workflows.get(workflow);
		long sum = 0;
		//System.out.println(constraints	);
		for (var item : items) {
			if (item.op == ' ') {
				sum += checkItem2(workflows, constraints, item);
			} else {
				if (item.op == '<') {
					var newConstraints = new HashMap<>(constraints);
					var constraint = constraints.get(item.c);
					newConstraints.put(item.c, constraint.lessThan(item.n));
					constraints.put(item.c, constraint.moreThan(item.n - 1));
					sum += checkItem2(workflows, newConstraints, item);
				} else if (item.op == '>') {
					var newConstraints = new HashMap<>(constraints);
					var constraint = constraints.get(item.c);
					newConstraints.put(item.c, constraint.moreThan(item.n));
					constraints.put(item.c, constraint.lessThan(item.n + 1));
					sum += checkItem2(workflows, newConstraints, item);
				} else {
					throw new IllegalStateException("Unknown operator: " + item.op);
				}
			}
		}
		return sum;
	}

	private long checkItem2(Map<String, List<WorkflowItem>> workflows, Map<Character, Constraint> constraints, WorkflowItem item) {
		if (item.s.equals("A")) {
			System.out.println(constraints);
			final long reduce = constraints.values().stream().mapToLong(Constraint::numsAccepted).reduce(1, (a, b) -> a * b);
			System.out.println(reduce);
			System.out.println();
			return reduce;
		} else if (item.s.equals("R")) {
			return 0;
		} else {
			return countAccepted(workflows, constraints, item.s);
		}
	}

	private Map<String, List<WorkflowItem>> parse(List<String> input) {
		Map<String, List<WorkflowItem>> rules = new HashMap<>();
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
								WorkflowItem r = new WorkflowItem(varName.charAt(0), '<', value, result);
								rules.computeIfAbsent(identifier, str -> new ArrayList<>()).add(r);
							} else if (cond.indexOf('>') > 0) {
								final String[] split = cond.split(">");
								String varName = split[0];
								long value = Long.parseLong(split[1]);
								WorkflowItem r = new WorkflowItem(varName.charAt(0), '>', value, result);
								rules.computeIfAbsent(identifier, str -> new ArrayList<>()).add(r);
							} else {
								throw new IllegalArgumentException(cond);
							}
						} else {
							rules.computeIfAbsent(identifier, str -> new ArrayList<>()).add(new WorkflowItem('?', ' ', 0L, rule));

						}
					}
				}
			}
		}
		return rules;
	}
}
