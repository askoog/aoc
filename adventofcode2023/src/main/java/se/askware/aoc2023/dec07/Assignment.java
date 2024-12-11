package se.askware.aoc2023.dec07;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import se.askware.aoc2023.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withLogLevel(LogLevel.INFO).withRunMode(RunMode.values()).run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		Hand.useJokers = false;
		solve(input);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		Hand.useJokers = true;

		solve(input);
	}

	private void solve(List<String> input) {
		List<Hand> hands = convertInput(input, s -> {
			final String[] split = s.split(" ");
			return new Hand(split[0], Long.parseLong(split[1]));
		});

		Collections.sort(hands);

		long productSum = 0;
		for (int i = 0; i < hands.size(); i++) {
			Hand h = hands.get(i);
			productSum += h.bet * (i + 1);
		}

		System.out.println(productSum);
	}

	private static class Hand implements Comparable<Hand> {

		private static final String ORDER_WITH_JOKERS = "AKQT98765432J";
		private static final String ORDER_WITHOUT_JOKERS = "AKQJT98765432";
		String value;
		long bet;

		private static boolean useJokers = false;

		public Hand(String value, long bet) {
			this.value = value;
			this.bet = bet;
		}

		public int getScore() {
			final Map<Character, Long> m = getCharacterListMap();

			long numJokers = Optional.ofNullable(useJokers ? m.remove('J') : null).orElse(0L);

			final long max = m.values().stream().mapToLong(Long::longValue).max().orElse(0L);
			if (max + numJokers == 5) {
				// Five of a kind
				return 6;
			} else if (max + numJokers == 4) {
				// four of a kind
				return 5;
			} else if (max + numJokers == 3) {
				if (m.size() == 2) {
					// full house
					return 4;
				} else {
					// three of a kind
					return 3;
				}
			} else if (max + numJokers == 2) {
				if (m.size() == 3) {
					// Two pairs
					return 2;
				} else {
					// one pair
					return 1;
				}
			} else {
				// High card
				return 0;
			}

		}

		@Override
		public int compareTo(Hand o) {

			final Map<Character, Long> map1 = getCharacterListMap();
			final Map<Character, Long> map2 = o.getCharacterListMap();

			final int score1 = getScore();
			final int score2 = o.getScore();
			debug("");
			debug(this.value + " " + map1 + " " + score1);
			debug(o.value + " " + map2 + " " + score2);

			if (score1 > score2) {
				debug(">");
				return 1;
			} else if (score1 < score2) {
				debug("<");
				return -1;
			} else {
				for (int i = 0; i < value.length(); i++) {
					final String cardOrder = useJokers ? ORDER_WITH_JOKERS : ORDER_WITHOUT_JOKERS;
					final int index1 = cardOrder.indexOf(value.charAt(i));
					final int index2 = cardOrder.indexOf(o.value.charAt(i));
					int order = index1 - index2;
					debug("indexof: " + i + " " + index1 + " - " + index2 + "=" + order);
					if (order != 0) {
						return index1 < index2 ? 1 : -1;
					}
				}
			}
			debug("huh?");
			return 0;
		}

		private Map<Character, Long> getCharacterListMap() {
			return value.chars()
					.mapToObj(c -> (char) c)
					.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		}

		@Override
		public String toString() {
			return "Hand{" +
					"value='" + value + '\'' +
					", bet=" + bet +
					'}';
		}
	}
}
