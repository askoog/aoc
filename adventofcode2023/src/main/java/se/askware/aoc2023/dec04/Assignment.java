package se.askware.aoc2023.dec04;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import se.askware.aoc2023.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		AtomicLong sum = new AtomicLong();
		input.forEach(line -> {
			final String[] split = line.split(":");
			final String[] card = split[1].split("\\|");
			final Set<String> collect = Arrays.stream(card[0].trim().split(" "))
					.collect(Collectors.toSet());
			final long myNumbers = Arrays.stream(card[1].trim().replace("  ", " ")
							.split(" "))
					.filter(collect::contains)
					.count();
			long win = 0;
			for (int i = 0; i < myNumbers; i++) {
				win = (win == 0) ? 1 : win * 2;
			}

			System.out.println(collect);
			System.out.println(win);
			sum.addAndGet(win);

		});
		System.out.println(sum);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		List<Card> cards = new ArrayList<>();
		int index = 1;
		for (String line : input) {
			final String[] split = line.split(":");
			final String[] card = split[1].split("\\|");
			cards.add(new Card(index, card[0].trim().split(" "), card[1].trim().replace("  ", " ")
					.split(" ")));
			index++;
		}
		System.out.println(cards);

		List<Card> pile = new ArrayList<>(cards);
		System.out.println(cards.size());

		Map<Integer, AtomicInteger> countPerCard = new HashMap<>();
		for (Card card : cards) {
			countPerCard.put(card.number, new AtomicInteger(1));
		}

		while (!pile.isEmpty()){
			Card c = pile.remove(0);
			final int[] winning = c.winning();
			int numCards = countPerCard.getOrDefault(c.number,new AtomicInteger()).get();
			for (int i = 1; i <= winning.length; i++) {
				countPerCard.computeIfAbsent(c.number + i, v -> new AtomicInteger()).addAndGet(numCards);
			}

		}
		System.out.println(countPerCard.values().stream().mapToInt(AtomicInteger::intValue).sum());

	}

	private static class Card {
		int number;
		List<String> winningNumbers;
		private final String[] myNumbers;

		public Card(int number, String[] winningNumbers, String[] myNumbers) {
			this.number = number;
			this.winningNumbers = List.of(winningNumbers);
			this.myNumbers = myNumbers;
		}

		public int[] winning(){
			return Arrays.stream(myNumbers)
					.filter(winningNumbers::contains)
					.mapToInt(Integer::parseInt)
					.toArray();
		}
	}
}
