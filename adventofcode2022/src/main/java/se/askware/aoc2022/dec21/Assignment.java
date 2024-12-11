package se.askware.aoc2022.dec21;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import se.askware.aoc2022.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	private static class Yell {

	}

	private static class Monkey {
		String key;
		boolean yelled = false;
		String[] calculation;
		Long calculatedValue;

		public Monkey(String key, String[] calculation) {
			this.key = key;
			this.calculation = calculation;
			try {
				calculatedValue = Long.parseLong(calculation[0]);
			} catch (NumberFormatException e) {
			}
		}

		public Long getValue() {
			if (yelled) {
				return calculatedValue;
			} else {
				return null;
			}
		}

		public Long calculate(Map<String, Monkey> monkeys) {
			if (calculatedValue != null) {
				yelled = true;
				return calculatedValue;
			} else {
				final Monkey monkey = monkeys.get(calculation[0]);
				Long monkeyValue = monkey.getValue();
				if (monkeyValue != null && monkey.yelled) {
					if (calculation.length == 1) {
						yelled = true;
						calculatedValue = monkey.getValue();
						return calculatedValue;
					} else {
						final Monkey monkey2 = monkeys.get(calculation[2]);
						Long monkeyValue2 = monkey2.getValue();
						if (monkeyValue2 != null && monkey2.yelled) {
							yelled = true;
							switch (calculation[1]) {
								case "*":
									//System.out.println(key + ": " + monkeyValue + "*" + monkeyValue2 + "=" + (monkeyValue * monkeyValue2));
									return calculatedValue = monkeyValue * monkeyValue2;
								case "+":
									//System.out.println(key + ": " + monkeyValue + "+" + monkeyValue2 + "=" + (monkeyValue + monkeyValue2));
									return calculatedValue = monkeyValue + monkeyValue2;
								case "/":
									//System.out.println(key + ": " + monkeyValue + "/" + monkeyValue2 + "=" + (monkeyValue / monkeyValue2));
									return calculatedValue = monkeyValue / monkeyValue2;
								case "-":
									//System.out.println(key + ": " + monkeyValue + "-" + monkeyValue2 + "=" + (monkeyValue - monkeyValue2));
									return calculatedValue = monkeyValue - monkeyValue2;
								default:
									throw new RuntimeException();
							}
						}
					}
				}
				return null;
			}
		}

		@Override
		public String toString() {
			return "Monkey{" +
					"key='" + key + '\'' +
					", yelled=" + yelled +
					", calculation=" + Arrays.toString(calculation) +
					", calculatedValue=" + calculatedValue +
					'}';
		}
	}

	@Override
	public void solvePartOne(List<String> input) {

		final List<Monkey> monkeyList = input.stream().map(s -> {
			final String[] split = s.split(":");
			return new Monkey(split[0], split[1].trim().split(" "));
		}).collect(Collectors.toList());
		final Map<String, Monkey> monkeyMap = monkeyList.stream().collect(Collectors.toMap(m -> m.key, m -> m));

		long count = 0;
		while (true) {
			for (int i = 0; i < monkeyList.size(); i++) {
				count++;
				Monkey m = monkeyList.get(i);
				System.out.println(m);
				if (m.key.equals("root") && m.getValue() != null) {
					System.out.println(m.getValue());
					printMonkey(0, m, monkeyMap);

					return;
				}
				m.calculate(monkeyMap);

			}
		}

	}

	public void printMonkey(int indent, Monkey m, Map<String, Monkey> monkeyMap) {
		for (int i = 0; i < indent; i++) {
			System.out.print(' ');
		}
		System.out.println(m);
		if (m.calculation.length == 3) {
			printMonkey(indent + 1, monkeyMap.get(m.calculation[0]), monkeyMap);
			printMonkey(indent + 1, monkeyMap.get(m.calculation[2]), monkeyMap);
		}
	}

	@Override
	public void solvePartTwo(List<String> input) {

		long i = input.size() < 100 ? 0 : 3582317955000L;
		long min = 0;
		long max = 3580000000000L;
		while (true) {

			final List<Monkey> monkeyList = input.stream().map(s -> {
				final String[] split = s.split(":");
				return new Monkey(split[0], split[1].trim().split(" "));
			}).collect(Collectors.toList());
			final Map<String, Monkey> monkeyMap = monkeyList.stream().collect(Collectors.toMap(m -> m.key, m -> m));

			final HashMap<String, Monkey> monkeyMap1 = new HashMap<>(monkeyMap);
			if (findHmn(i, monkeyList, monkeyMap1)) {
				System.out.println(i);
				;
				return;
			}
			i++;
			if (i % 10_000 == 0) {
				System.out.println(i);
			}
		}
	}

	private boolean findHmn(long hmn, List<Monkey> monkeyList, Map<String, Monkey> monkeyMap) {

		//monkeyMap.get("root").calculation[0];

		monkeyMap.get("humn").calculatedValue = hmn;
		//monkeyMap.put("humn", new Monkey("humn", new String[]{String.valueOf(5)}));
		//System.out.println(monkeyMap.get("humn"));

		while (true) {
			for (int i = 0; i < monkeyList.size(); i++) {
				Monkey m = monkeyList.get(i);
				//System.out.println(m);

				if (m.key.equals("root")) {
					//	printMonkey(0, m, monkeyMap);
				}
				if (m.key.equals("root") && m.getValue() != null) {
					//System.out.println(m);
					if (monkeyMap.get(m.calculation[0]).calculatedValue.equals(monkeyMap.get(m.calculation[2]).calculatedValue)) {
						//System.out.println(m.getValue());
						return true;
					} else {

						System.out.println(hmn + ": " +
										monkeyMap.get(m.calculation[0]).calculatedValue + (
										monkeyMap.get(m.calculation[0]).calculatedValue > monkeyMap.get(m.calculation[2]).calculatedValue ? ">" : "<"
								) + monkeyMap.get(m.calculation[2]).calculatedValue
						);
						return false;
					}
				}
				m.calculate(monkeyMap);
			}
		}
	}

}
