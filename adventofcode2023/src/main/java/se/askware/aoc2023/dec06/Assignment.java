package se.askware.aoc2023.dec06;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.askware.aoc2023.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		List<Game> games = parse(input);
		System.out.println(games);
		long factor = 1;
		for (Game game : games) {
			long numAlts = 0;
			for (long i = 0; i < game.distance * 2; i++) {
				long d = i * (game.time - i);
				if (d > game.distance){
					numAlts++;
				}
			}
			factor *= numAlts;

			System.out.println(game + " " + numAlts);


		}
		System.out.println(factor);

	}

	private List<Game> parse(List<String> input) {
		final String[] times = input.get(0).replaceAll("\\s+", " ").split(" ", -1);
		final String[] distances = input.get(1).replaceAll("\\s+", " ").split(" ", -1);

		List<Game> result = new ArrayList<>();
		for (int i = 1; i < times.length; i++) {
			result.add(new Game(Integer.parseInt(times[i].trim()), Integer.parseInt(distances[i].trim())));
		}
		return result;
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final String[] times = input.get(0).replaceAll("\\s+", "").split(":", -1);
		final String[] distances = input.get(1).replaceAll("\\s+", "").split(":", -1);

		List<Game> games = new ArrayList<>();
		for (int i = 1; i < times.length; i++) {
			games.add(new Game(Long.parseLong(times[i].trim()), Long.parseLong(distances[i].trim())));
		}
		System.out.println(games);
		long factor = 1;
		for (Game game : games) {
			long min = 0;
			for (long i = 0; i < game.distance; i++) {
				long d = i * (game.time - i);
				if (d > game.distance){
					min = i;
					break;
				}
			}
			long max = 0;
			for (long i = game.time; i >= 0; i--) {

				long d = i * (game.time - i);
				if (d > game.distance){
					max = i;
					break;
				}
			}

			System.out.println(game + " " + min + " " + max + " = " + (max - min + 1));


		}
		System.out.println(factor);
	}

	private record Game(long time, long distance){}

}
