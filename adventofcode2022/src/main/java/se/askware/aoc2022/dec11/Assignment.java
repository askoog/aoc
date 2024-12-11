package se.askware.aoc2022.dec11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import se.askware.aoc2022.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	private static class Item {

		long value;

		public Item(long value) {
			this.value = value;
		}
	}

	private static class Monkey {
		int id;
		List<Item> items;
		Function<Long, Long> operation;
		Long divisor;
		int[] resultingMonkeys;
		int numInspected = 0;
		Function<Long,Long> limitingFunction;

		public Monkey(int id, List<Item> items, Function<Long, Long> operation, Long divisor, int[] resultingMonkeys) {
			this.id = id;
			this.items = items;
			this.operation = operation;
			this.divisor = divisor;
			this.resultingMonkeys = resultingMonkeys;
		}

		public void addItem(Item item) {
			items.add(item);
		}

		public void inspect(List<Monkey> monkeys) {

			List<Item> itemCopy = new ArrayList<>(items);
			items.clear();
			for (Item item : itemCopy) {
				numInspected++;

				item.value = limitingFunction.apply(operation.apply(item.value));

				if (item.value % divisor == 0) {
					monkeys.get(resultingMonkeys[0]).addItem(item);
					//System.out.println("monkey " + id + " inspects " + old + " -- > " + item.values.get(divisor) + " true  -->" + resultingMonkeys[0]);
				} else {
					monkeys.get(resultingMonkeys[1]).addItem(item);
					//System.out.println("monkey " + id + " inspects " + item + " -- > " + newLevel + " false  -->" + resultingMonkeys[1]);
				}
			}
		}
	}

	@Override
	public void solvePartOne(List<String> input) {

		List<Monkey> monkeys = parseMonkeys(input);

		for (Monkey monkey : monkeys) {
			monkey.limitingFunction = n -> (long)Math.floor(n / 3);
		}

		for (int i = 0; i < 20; i++) {
			for (Monkey monkey : monkeys) {
				monkey.inspect(monkeys);
			}
		}

		final List<Long> collect = monkeys.stream().mapToLong(m -> m.numInspected).boxed().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
		System.out.println(collect.get(0) * collect.get(1));
	}

	private static List<Monkey> parseMonkeys(List<String> input) {
		List<Monkey> monkeys = new ArrayList<>();
		for (int i = 0; i < input.size(); i++) {
			int monkey = Integer.parseInt(input.get(i).replace("Monkey ", "").replace(":", ""));
			List<Item> items = Arrays.stream(input.get(++i).replace("Starting items: ", "")
							.replace(" ", "").split(",")).mapToInt(Integer::parseInt).boxed()
					.map(n -> new Item(n))
					.collect(Collectors.toList());
			String s = input.get(++i);
			Function<Long, Long> operation;
			final String termString = s.substring(s.lastIndexOf(' ') + 1);

			if (termString.equals("old")) {
				if (s.indexOf('*') > 0) {
					operation = (n) -> n * n;
				} else {
					operation = (n) -> n + n;
				}

			} else {
				Long term = Long.valueOf(Integer.parseInt(termString));
				if (s.indexOf('*') > 0) {
					operation = (n) -> n * term;
				} else {
					operation = (n) -> n + term;
				}
			}
			s = input.get(++i);
			long divisor = Long.parseLong(s.substring(s.lastIndexOf(' ') + 1));
			s = input.get(++i);
			int trueMonkey = Integer.parseInt(s.substring(s.lastIndexOf(' ') + 1));
			s = input.get(++i);
			int falseMonkey = Integer.parseInt(s.substring(s.lastIndexOf(' ') + 1));

			Monkey m = new Monkey(monkey, items, operation, divisor, new int[] { trueMonkey, falseMonkey });
			i++;
			monkeys.add(m);
		}
		return monkeys;
	}

	@Override
	public void solvePartTwo(List<String> input) {

		List<Monkey> monkeys = parseMonkeys(input);

		long modulo = 1;
		for (Monkey monkey : monkeys) {
			modulo *= monkey.divisor;
		}
		long limit = modulo;
		for (Monkey monkey : monkeys) {
			monkey.limitingFunction = n -> n % limit;
		}

		for (int i = 0; i < 10000; i++) {
			for (Monkey monkey : monkeys) {
				monkey.inspect(monkeys);
			}
		}

		final List<Long> collect = monkeys.stream().mapToLong(m -> m.numInspected).boxed().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
		System.out.println(collect.get(0) * collect.get(1));
	}

}
