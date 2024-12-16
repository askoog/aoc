package se.askware.aoc2024.dec14;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.askware.aoc2024.common.AocBase;
import se.askware.aoc2024.common.CharCell;
import se.askware.aoc2024.common.Grid;
import se.askware.aoc2024.common.GridPos;
import se.askware.aoc2024.common.XYPair;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		XYPair gridSize = new XYPair(11, 7);
		if (input.size() > 15) {
			gridSize = new XYPair(101, 103);
		}
		final Grid<CharCell> charCellGrid = Grid.charGrid(gridSize.getY(), gridSize.getX(), (x, y) -> '.');
		long[] q = new long[] { 0, 0, 0, 0 };
		for (String line : input) {
			final String[] s = line.split(" ");
			final int[] pos = Arrays.stream(s[0].substring(2).split(",")).mapToInt(Integer::parseInt).toArray();
			final int[] velocity = Arrays.stream(s[1].substring(2).split(",")).mapToInt(Integer::parseInt).toArray();

			final Robot robot = new Robot(new GridPos(pos[1], pos[0]), new XYPair(velocity[0], velocity[1]));
			//System.out.println(robot);

			final Robot move = robot.move(100, gridSize);
			//System.out.println(move);
			//charCellGrid.getOptionalCell(move.pos()).stream().mapToInt(c -> ).findFirst().ifPresent(System.out::println);
			charCellGrid.setCell(new CharCell('#'), move.pos().getRow(), move.pos().getCol());
			if (move.pos().getRow() < gridSize.getY() / 2 &&
				move.pos().getCol() < gridSize.getX() / 2) {
				q[0]++;
			}
			if (move.pos().getRow() < gridSize.getY() / 2 &&
				move.pos().getCol() > gridSize.getX() / 2) {
				q[1]++;
			}
			if (move.pos().getRow() > gridSize.getY() / 2 &&
				move.pos().getCol() < gridSize.getX() / 2) {
				q[2]++;
			}
			if (move.pos().getRow() > gridSize.getY() / 2 &&
				move.pos().getCol() > gridSize.getX() / 2) {
				q[3]++;
			}
		}
		//charCellGrid.print();
		final long reduce = Arrays.stream(q).reduce(1, (i1, i2) -> i1 * i2);
		System.out.println(reduce);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		if (input.size() < 15) {
			return;
		}
		XYPair gridSize = new XYPair(101, 103);
		List<Robot> robots = new ArrayList<>();
		for (String line : input) {
			final String[] s = line.split(" ");
			final int[] pos = Arrays.stream(s[0].substring(2).split(",")).mapToInt(Integer::parseInt).toArray();
			final int[] velocity = Arrays.stream(s[1].substring(2).split(",")).mapToInt(Integer::parseInt).toArray();

			final Robot robot = new Robot(new GridPos(pos[1], pos[0]), new XYPair(velocity[0], velocity[1]));
			robots.add(robot);

		}
		for (int i = 1; i < 10000; i++) {
			robots = robots.stream().map(r -> r.move(1, gridSize)).toList();
			final Grid<CharCell> charCellGrid = Grid.charGrid(gridSize.getY(), gridSize.getX(), (x, y) -> '.');
			for (Robot robot : robots) {
				charCellGrid.setCell(new CharCell('#'), robot.pos().getRow(), robot.pos().getCol());
			}
			boolean foundRow = false;
			for (int j = 0; j < charCellGrid.getNumRows(); j++) {
				if (charCellGrid.getRow(j).stream().filter(c -> c.value == '#').count() > 20) {
					foundRow = true;
					break;
				}
			}
			boolean foundCol = false;

			for (int j = 0; j < charCellGrid.getNumColumns(); j++) {
				if (charCellGrid.getColumn(j).stream().filter(c -> c.value == '#').count() > 20) {
					foundCol = true;
					break;
				}
			}

			//final long sum = robots.stream().filter(r -> charCellGrid.getAllNeighbors(r.pos).stream().anyMatch(c -> c.value == '#')).count();
			//System.out.println(sum);
			//if (sum > 300){
			if (foundRow && foundCol) {
				System.out.println("i: " + i);

				charCellGrid.print();

			}
		}
	}

	record Robot(GridPos pos, XYPair velocity) {

		Robot move(int numMoves, XYPair gridSize) {
			final GridPos move = pos.move(velocity, numMoves);
			GridPos newPos = new GridPos(
					((move.getRow() % gridSize.getY()) + gridSize.getY()) % gridSize.getY(),
					((move.getCol() % gridSize.getX()) + gridSize.getX()) % gridSize.getX());
			return new Robot(newPos, velocity);
		}
	}

}
