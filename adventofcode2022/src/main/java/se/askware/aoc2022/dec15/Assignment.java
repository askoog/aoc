package se.askware.aoc2022.dec15;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.output.AppendableOutputStream;

import se.askware.aoc2022.common.AocBase;
import se.askware.aoc2022.common.CharCell;
import se.askware.aoc2022.common.Grid;
import se.askware.aoc2022.common.Range;
import se.askware.aoc2022.common.XYPair;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	private static class Sensor {
		XYPair pos;
		XYPair nearestBeacon;

		public Sensor(XYPair pos, XYPair nearestBeacon) {
			this.pos = pos;
			this.nearestBeacon = nearestBeacon;
		}

		@Override
		public String toString() {
			return "Sensor{" +
					"pos=" + pos +
					", nearestBeacon=" + nearestBeacon +
					", dist=" + manhattanDistance() +
					'}';
		}

		public int manhattanDistance() {
			return Math.abs(pos.getX() - nearestBeacon.getX()) +
					Math.abs(pos.getY() - nearestBeacon.getY());
		}

	}

	Pattern p = Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");

	@Override
	public void solvePartOne(List<String> input) {
		final List<Sensor> sensors = getSensors(input);
		final Map<XYPair, Sensor> map = sensors.stream().collect(Collectors.toMap(s -> s.pos, s -> s));
		System.out.println(sensors);

		final int xmax = sensors.stream().mapToInt(s -> s.pos.getX() + s.manhattanDistance()).max().getAsInt();
		final int xmin = sensors.stream().mapToInt(s -> s.pos.getX() - s.manhattanDistance()).min().getAsInt();
		final int ymax = sensors.stream().mapToInt(s -> s.pos.getY() + s.manhattanDistance()).max().getAsInt();
		final int ymin = sensors.stream().mapToInt(s -> s.pos.getY() - s.manhattanDistance()).min().getAsInt();

		System.out.println(xmin + "," + xmax + "," + ymin + "," + ymax);
		int rowOffset = Math.abs(ymin);
		int colOffset = Math.abs(xmin);

		IntStream.of(10, 2000000).forEach(wantedRow -> {
			char[] chars = new char[colOffset + ymax];
			for (int i = 0; i < chars.length; i++) {
				chars[i] = ' ';
			}

			sensors.forEach(s -> {
				if (s.pos.getY() == wantedRow) {
					for (int i = -s.manhattanDistance(); i <= s.manhattanDistance(); i++) {
						chars[s.pos.getX() + colOffset + i] = '#';
					}
				}
				if (s.pos.getY() < wantedRow && s.pos.getY() + s.manhattanDistance() > wantedRow) {
					int width = s.manhattanDistance() + (s.pos.getY() - wantedRow);
					for (int i = -width; i <= width; i++) {
						chars[s.pos.getX() + colOffset + i] = '#';
					}
				}
				if (s.pos.getY() > wantedRow && s.pos.getY() - s.manhattanDistance() < wantedRow) {
					int width = s.manhattanDistance() + (wantedRow - s.pos.getY());
					for (int i = -width; i <= width; i++) {
						chars[s.pos.getX() + colOffset + i] = '#';
					}
				}
			});
			sensors.forEach(s -> {
				if (s.nearestBeacon.getY() == wantedRow) {
					chars[s.nearestBeacon.getX() + colOffset] = 'B';
				}
			});

			int count = 0;
			for (int i = 0; i < chars.length; i++) {
				if (chars[i] == '#') {
					count++;
				}
				;
			}
			System.out.println(count);
		});
/*
		final Grid<CharCell> grid = Grid.charGrid(xmax + colOffset + 1, ymax + rowOffset + 1, (row, col) -> '.');
		System.out.println(grid.getNumColumns() + "," + grid.getNumColumns());

		sensors.forEach(s -> {
			//			if (s.pos.getX() == 8 && s.pos.getY() == 7) {
			final XYPair tpos = s.pos.translate(colOffset, rowOffset);
			final XYPair btpos = s.nearestBeacon.translate(colOffset, rowOffset);
			int num = 1;
			for (int row = 0; row < s.manhattanDistance(); row++) {
				for (int col = 0; col < num; col++) {
					grid.getCell(row + tpos.getY() - s.manhattanDistance(), tpos.getX() + col).value = '#';
					grid.getCell(row + tpos.getY() - s.manhattanDistance(), tpos.getX() - col).value = '#';
				}
				num++;
			}
			num = 0;
			for (int row = s.manhattanDistance() + 1; row >= 0; row--) {
				for (int col = 0; col < num; col++) {
					grid.getCell(row + tpos.getY(), tpos.getX() + col).value = '#';
					grid.getCell(row + tpos.getY(), tpos.getX() - col).value = '#';
				}
				num++;
			}
			grid.getCell(tpos.getY(), tpos.getX()).value = 'S';
			grid.getCell(btpos.getY(), btpos.getX()).value = 'B';
			//}
		});
		sensors.forEach(s -> {
			System.out.println(s);
			final XYPair tpos = s.pos.translate(colOffset, rowOffset);
			final XYPair btpos = s.nearestBeacon.translate(colOffset, rowOffset);
			grid.getCell(tpos.getY(), tpos.getX()).value = 'S';
			grid.getCell(btpos.getY(), btpos.getX()).value = 'B';
		});

		grid.print();

		final long count = grid.getRow(10 + rowOffset).stream().filter(c -> c.value == '#' || c.value == 'S').count();
		System.out.println(count);
*/
	}

	private List<Sensor> getSensors(List<String> input) {
		final List<Sensor> sensors = convertInput(input, s -> {
			final Matcher matcher = p.matcher(s);
			matcher.find();
			Sensor sensor = new Sensor(new XYPair(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
					new XYPair(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4))));
			return sensor;
		});
		return sensors;
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final List<Sensor> sensors = getSensors(input);
		final Map<XYPair, Sensor> map = sensors.stream().collect(Collectors.toMap(s -> s.pos, s -> s));
		System.out.println(sensors);

		final int xmax = sensors.stream().mapToInt(s -> s.pos.getX() + s.manhattanDistance()).max().getAsInt();
		final int xmin = sensors.stream().mapToInt(s -> s.pos.getX() - s.manhattanDistance()).min().getAsInt();
		final int ymax = sensors.stream().mapToInt(s -> s.pos.getY() + s.manhattanDistance()).max().getAsInt();
		final int ymin = sensors.stream().mapToInt(s -> s.pos.getY() - s.manhattanDistance()).min().getAsInt();

		System.out.println(xmin + "," + xmax + "," + ymin + "," + ymax);
		int rowOffset = Math.abs(ymin);
		int colOffset = Math.abs(ymin);
		System.out.println(colOffset);
		System.out.println(colOffset + ymax);

		int scope,rowmin,rowmax;
		if (ymax < 100000){
			scope = 20;
			rowmin = 11;
			rowmax = 12;
		} else {
			scope = 4_000_000;
			rowmin = 0;
			rowmax = 4_000_000;
			//return;
		}

		IntStream.range(rowmin, rowmax	).forEach(wantedRow -> {
			//System.out.println("lkö");
			if (wantedRow % 10000 == 0){
			//	System.out.println(wantedRow);
			}
			//char[] chars = new char[colOffset + ymax + 1000];
			//for (int i = 0; i < chars.length; i++) {
			//	chars[i] = ' ';
			//}

			List<Range> ranges = new ArrayList<>();
			sensors.forEach(s -> {
				//System.out.println(wantedRow + " : " + s);
				if (s.pos.getY() == wantedRow) {
					Range r = new Range(s.pos.getX() + colOffset - s.manhattanDistance(),
							s.pos.getX() + colOffset + s.manhattanDistance());
//					for (int i = -s.manhattanDistance(); i < s.manhattanDistance(); i++) {
//						chars[s.pos.getX() + colOffset + i] = '#';
//					}
					ranges.add(r);
				}
				if (s.pos.getY() < wantedRow && s.pos.getY() + s.manhattanDistance() > wantedRow) {
					int width = s.manhattanDistance() + (s.pos.getY() - wantedRow);
					Range r = new Range(s.pos.getX() + colOffset -width,
							s.pos.getX() + colOffset + width);
					ranges.add(r);
					//
					//					for (int i = -width; i <= width; i++) {
						//chars[s.pos.getX() + colOffset + i] = '#';
					//}
				}
				if (s.pos.getY() > wantedRow && s.pos.getY() - s.manhattanDistance() < wantedRow) {
					int width = s.manhattanDistance() + (wantedRow - s.pos.getY());
					//System.out.println(width + "  " + s.pos.getX() + ","  + colOffset);
//					for (int i = -width; i <= width; i++) {
//						chars[s.pos.getX() + colOffset + i] = '#';
//					}
					Range r = new Range(s.pos.getX() + colOffset -width,
							s.pos.getX() + colOffset + width);
					ranges.add(r);
				}
			});

			final List<Range> collect = ranges.stream().sorted(Comparator.comparing(r -> r.start)).collect(Collectors.toList());
			List<Range> merged = merge(collect);
//			System.out.println(collect);
//			System.out.println(merged);
			Range wanted = new Range(0 + colOffset, scope + colOffset);
			if (merged.stream().noneMatch(r -> r.covers(wanted))){
				System.out.println(wantedRow + " " + merged);
				System.out.println(wanted);
				System.out.println(colOffset);
				final List<Range> sorted = merged.stream().sorted(Comparator.comparing(r -> r.start)).collect(Collectors.toList());
				System.out.println((sorted.get(0).end - colOffset + 1 ) * 4_000_000L + wantedRow);
				//4385842
			}
			/*
			for (int i = 0; i < collect.size() - 1; i++) {
				if (collect.get(i).overlaps(collect.get()))
			}

			sensors.forEach(s -> {
				if (s.nearestBeacon.getY() == wantedRow) {
					chars[s.nearestBeacon.getX() + colOffset] = 'B';
				}
			});

			for (int i = 0; i < scope; i++) {
				if (chars[i + colOffset] == ' ') {
					System.out.println(wantedRow + ":" + i + " --> " + (i*4_000_000 + wantedRow));
				}
			}
			*/

		});
	}

	public List<Range> merge(List<Range> ranges){
		if (ranges.size() <= 1){
			return ranges;
		}
		List<Range> result = new ArrayList<>();
		Range r = ranges.get(0);
		for (int i = 1; i < ranges.size(); i++) {
			if (r.overlaps(ranges.get(i))){
				r = r.merge(r, ranges.get(i));
			} else {
				result.add(ranges.get(i));
			}
		}
		result.add(r);
		if (result.size() == ranges.size()){
			return result;
		} else {
			return merge(result);
		}

	}

}
