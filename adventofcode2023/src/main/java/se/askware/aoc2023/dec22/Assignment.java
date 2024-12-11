package se.askware.aoc2023.dec22;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import se.askware.aoc2023.common.AocBase;
import se.askware.aoc2023.common.CharCell;
import se.askware.aoc2023.common.Grid;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.values()).run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final List<Brick> bricks = input.stream().map(line -> {
			final String[] split = line.split("[~,]");
			return new Brick(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]));
		}).toList();

		final int xmax = bricks.stream().mapToInt(l -> Math.max(l.x, l.x2 + 1)).max().orElseThrow();
		final int ymax = bricks.stream().mapToInt(l -> Math.max(l.y, l.y2 + 1)).max().orElseThrow();
		final int zmax = bricks.stream().mapToInt(l -> Math.max(l.z, l.z2 + 1)).max().orElseThrow();

		debug(bricks);

		List<Grid<CharCell>> grids = new ArrayList<>();
		for (int i = 0; i < zmax; i++) {
			grids.add(Grid.charGrid(ymax, xmax, (x, y) -> '.'));
		}

		bricks.forEach(brick -> {
			paintBrick(brick, grids, '#');
		});

		for (int z = zmax - 1; z >= 0; z--) {
			debug(z);
			final Grid<CharCell> grid = grids.get(z);
			//grid.print();
			debug("");
		}

		printXY(xmax, ymax, zmax, grids);

		int numChanged = fall(bricks, grids);

		debug("-------- " + numChanged);
		for (int z = zmax - 1; z >= 0; z--) {
			debug(z);
			final Grid<CharCell> grid = grids.get(z);
			//grid.print();
			debug("");
		}
		printXY(xmax, ymax, zmax, grids);

		int safeToRemove = 0;
		for (Brick brick : bricks) {
			paintBrick(brick, grids, '.');
			boolean supports = false;
			for (Brick brick2 : bricks) {
				if (brick != brick2) {
					if (canFall(grids, brick2)) {
						debug("can fall " + brick2);
						supports = true;
					}
				}
			}
			paintBrick(brick, grids, '#');
			if (!supports) {
				debug(brick);
				safeToRemove++;
			}
		}
		System.out.println(safeToRemove);

	}

	private static int fall(List<Brick> bricks, List<Grid<CharCell>> grids) {
		Map<Integer, Boolean> fallen = new HashMap<>();
		boolean changed = true;
		while (changed) {
			changed = false;
			for (int i = 0; i < bricks.size(); i++) {
				Brick brick = bricks.get(i);
				if (canFall(grids, brick)) {
					paintBrick(brick, grids, '.');
					brick.z--;
					brick.z2--;
					changed = true;
					fallen.put(i, true);
					paintBrick(brick, grids, '#');
				}
			}
		}
		//debug(fallen);
		return fallen.size();
	}

	private static void paintBrick(Brick brick, List<Grid<CharCell>> grids, char value) {
		for (int z = brick.z; z <= brick.z2; z++) {
			final Grid<CharCell> g = grids.get(z);
			for (int x = brick.x; x <= brick.x2; x++) {
				for (int y = brick.y; y <= brick.y2; y++) {
					g.getCell(x, y).value = value;
				}
			}
		}
	}

	private static boolean canFall(List<Grid<CharCell>> grids, Brick brick) {
		final int zMin = Math.min(brick.z, brick.z2);
		if (zMin <= 1) {
			return false;
		}
		final Grid<CharCell> grid = grids.get(zMin - 1);
		for (int x = brick.x; x <= brick.x2; x++) {
			for (int y = brick.y; y <= brick.y2; y++) {
				if (grid.getCell(x, y).value != '.') {
					return false;
				}
			}
		}

		return true;
	}

	private static void printXY(int xmax, int ymax, int zmax, List<Grid<CharCell>> grids) {
		for (int z = zmax - 1; z >= 0; z--) {
			String s = "" + z + " ";
			final Grid<CharCell> g = grids.get(z);
			for (int x = 0; x < xmax; x++) {
				int y = ymax - 1;
				while (y > 0 && g.getCell(x, y).value == '.') {
					y--;
				}
				s += g.getCell(x, y).value;
			}
			s += "   ";
			for (int y = 0; y < ymax; y++) {
				int x = xmax - 1;
				while (x > 0 && g.getCell(x, y).value == '.') {
					x--;
				}
				s += g.getCell(x, y).value;
			}
			debug(s);
		}
	}

	@Override
	public void solvePartTwo(List<String> input) {
		ParseResult result = getParseResult(input);

		debug(result.bricks());

		List<Grid<CharCell>> grids = new ArrayList<>();
		for (int i = 0; i < result.zmax(); i++) {
			grids.add(Grid.charGrid(result.ymax(), result.xmax(), (x, y) -> '.'));
		}
		result.bricks().forEach(brick -> {
			paintBrick(brick, grids, '#');
		});

		fall(result.bricks(), grids);

		int sum = 0;
		for (Brick brick : result.bricks()) {
			List<Grid<CharCell>> gridCopy = grids.stream()
					.map(g -> Grid.charGrid(g.getNumRows(), g.getNumColumns(), (i, j) -> g.getCell(i, j).value))
					.toList();
			//printXY(result.xmax, result.ymax, result.zmax, gridCopy);

			paintBrick(brick, gridCopy, '.');
			final List<Brick> brickCopy = result.bricks.stream()
					.filter(b -> b != brick)
					.map(b -> new Brick(b.x, b.y, b.z, b.x2, b.y2, b.z2)).toList();
			final int fall = fall(brickCopy, gridCopy);
			//debug(brick + " " + fall);
			sum += fall;
			//printXY(result.xmax, result.ymax, result.zmax, gridCopy);
			//debug("------------");
		}
		System.out.println(sum);
	}

	private static ParseResult getParseResult(List<String> input) {
		final List<Brick> bricks = input.stream().map(line -> {
			final String[] split = line.split("[~,]");
			return new Brick(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]), Integer.parseInt(split[5]));
		}).toList();

		final int xmax = bricks.stream().mapToInt(l -> Math.max(l.x, l.x2 + 1)).max().orElseThrow();
		final int ymax = bricks.stream().mapToInt(l -> Math.max(l.y, l.y2 + 1)).max().orElseThrow();
		final int zmax = bricks.stream().mapToInt(l -> Math.max(l.z, l.z2 + 1)).max().orElseThrow();
		ParseResult result = new ParseResult(bricks, xmax, ymax, zmax);
		return result;
	}

	private record ParseResult(List<Brick> bricks, int xmax, int ymax, int zmax) {
	}

	static final class Brick {
		private final int x;
		private final int y;
		private int z;
		private final int x2;
		private final int y2;
		private int z2;

		Brick(int x, int y, int z, int x2, int y2, int z2) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.x2 = x2;
			this.y2 = y2;
			this.z2 = z2;
		}

		public int x() {
			return x;
		}

		public int y() {
			return y;
		}

		public int z() {
			return z;
		}

		public int x2() {
			return x2;
		}

		public int y2() {
			return y2;
		}

		public int z2() {
			return z2;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (obj == null || obj.getClass() != this.getClass()) {
				return false;
			}
			var that = (Brick) obj;
			return this.x == that.x &&
					this.y == that.y &&
					this.z == that.z &&
					this.x2 == that.x2 &&
					this.y2 == that.y2 &&
					this.z2 == that.z2;
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y, z, x2, y2, z2);
		}

		@Override
		public String toString() {
			return "Brick[" +
					"x=" + x + ", " +
					"y=" + y + ", " +
					"z=" + z + ", " +
					"x2=" + x2 + ", " +
					"y2=" + y2 + ", " +
					"z2=" + z2 + ']';
		}

	}

}
