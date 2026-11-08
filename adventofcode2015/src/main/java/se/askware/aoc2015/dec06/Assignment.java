package se.askware.aoc2015.dec06;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.askware.aoc2015.common.AocBase;
import se.askware.aoc2015.common.Cell;
import se.askware.aoc2015.common.Grid;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		Pattern p = Pattern.compile("(.*) ([0-9]+),([0-9]+) through ([0-9]+),([0-9]+)");

		Grid<Light> g = new Grid<>(1000, 1000);
		g.init((i, j) -> new Light());

		for (String line : input) {
			Matcher matcher = p.matcher(line);
			matcher.find();
			String command = matcher.group(1);
			int fromX = Integer.parseInt(matcher.group(2));
			int fromY = Integer.parseInt(matcher.group(3));
			int toX = Integer.parseInt(matcher.group(4));
			int toY = Integer.parseInt(matcher.group(5));

			for (int i = fromX; i < toX + 1; i++) {
				for (int j = fromY; j < toY + 1; j++) {

					Light cell = g.getCell(i, j);
					if (command.equals("turn on")) {
						cell.on = true;
						cell.brightness++;
					}
					if (command.equals("turn off")) {
						cell.on = false;
						cell.brightness = Math.max(0, cell.brightness - 1);
					}
					if (command.equals("toggle")) {
						cell.on = !cell.on;
						cell.brightness += 2;
					}
				}
			}

			System.out.println(command + " " + fromX + "," + fromY + " -> " + toX + "," + toY);
		}
		System.out.println(g.getAll().filter(l -> l.on).count());
		System.out.println(g.getAll().mapToInt(l -> l.brightness).sum());

	}

	@Override
	public void solvePartTwo(List<String> input) {
	}

	private static class Light extends Cell {
		boolean on = false;
		int brightness = 0;
	}
}
