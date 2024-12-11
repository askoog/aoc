package se.askware.aoc2022.dec02;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import se.askware.aoc2022.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		System.out.println( input.stream().map(Game::new).mapToInt(Game::evaluate).peek(System.out::println).sum());

		System.out.println( input.stream().mapToInt(outcome::get).peek(System.out::println).sum());

	}

	@Override
	public void solvePartTwo(List<String> input) {
		System.out.println(input.stream()
				.peek(System.out::print)
				.map(this::modify)
				.peek(System.out::print)
				.mapToInt(outcome::get)
				.peek(System.out::println)
				.sum());
	}

	private  String modify(String s) {
		int opponent = s.charAt(0) - 'A' + 1;
		int player = s.charAt(2) - 'X' + 1;
		if (player == 1){
			// lose
			switch (opponent){
				case 1:
					return "A Z";
				case 2:
					return "B X";
				case 3:
					return "C Y";
				default:
					throw new RuntimeException("");
			}
		} else if (player == 2){
			return s.charAt(0) + " " + Character.valueOf((char)('X' + opponent - 1)).toString();
		} else if (player == 3){
			switch (opponent) {
				case 1:
					return "A Y";
				case 2:
					return "B Z";
				case 3:
					return "C X";
				default:
					throw new RuntimeException("");
			}
		} else {
			throw new RuntimeException("illegal input " + s);
		}
	}

	private enum Type {
		Q,X,Y,Z

	}

	private Map<String,Integer> outcome =
			Map.of("A X", 1 + 3,
					"A Y", 2 + 6,
					"A Z", 3 + 0,

					"B X", 1 + 0,
					"B Y", 2 + 3,
					"B Z", 3 + 6,

					"C X", 1 + 6,
					"C Y", 2 + 0,
					"C Z", 3 + 3
			);

	private static class Game {
		String input;

		public Game(String input) {
			this.input = input;
		}

		public int evaluate(){
			char opponent = input.charAt(0);
			char player = input.charAt(2);
			int score = (player - 'X' + 1);
			int opponentVal = (opponent - 'A' + 1);
			System.out.println(input + " score " + score);
			if (opponentVal == score){
				return opponentVal*2;
			}
			if (opponent == 'A') {
				return player == 'Y' ? score + 6 : score;
			}
			if (opponent == 'B') {
				return player == 'Z' ? score + 6 : score;
			}
			if (opponent == 'C') {
				return player == 'X' ? score + 6 : score;
			}
			throw new RuntimeException("invalid input " + input);
		}
	}

}
