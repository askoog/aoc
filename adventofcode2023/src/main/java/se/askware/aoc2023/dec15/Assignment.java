package se.askware.aoc2023.dec15;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;

import se.askware.aoc2023.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.PART2).run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final String[] split = input.get(0).split(",");
		final long sum = Arrays.stream(split).mapToLong(this::hash).sum();
		System.out.println(sum);
	}

	private int hash(String s) {
		int currentValue = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			currentValue += (int) c;
			currentValue *= 17;
			currentValue %= 256;
			debug(currentValue);

		}
		return currentValue;

	}

	@Override
	public void solvePartTwo(List<String> input) {
		Box[] boxes = new Box[256];
		for (int i = 0; i < boxes.length; i++) {
			boxes[i] = new Box();
		}
		final String[] split = input.get(0).split(",");
		Arrays.stream(split).mapToLong(s -> box(s, boxes)).sum();

		long sum = 0;
		for (int i = 0; i < boxes.length; i++) {

			long val = 0;
			for (int j = 0; j < boxes[i].lenses.size(); j++) {
				final Lens lens = boxes[i].lenses.get(j);
				val += (i + 1) * (j + 1) * (long) lens.value;
			}
			if (val != 0) {
				System.out.println(boxes[i]);
				System.out.println(val);
				sum += val;
			}
		}
		System.out.println(sum);
	}

	private static class Box {
		List<Lens> lenses = new ArrayList<>();

		void removeLens(String identifier) {
			lenses = new ArrayList<>(lenses.stream().filter(l -> !l.id.equals(identifier)).toList());
		}

		public void addLens(Lens lens) {
			for (int i = 0; i < lenses.size(); i++) {
				if (lenses.get(i).id.equals(lens.id)) {
					lenses.get(i).value = lens.value;
					return;
				}
			}
			lenses.add(lens);
		}

		@Override
		public String toString() {
			return "Box{" +
					"lenses=" + lenses +
					'}';
		}
	}

	private static class Lens {
		String id;
		int value;

		public Lens(String id, int value) {
			this.id = id;
			this.value = value;
		}

		@Override
		public String toString() {
			return id + ", " + value;
		}
	}

	private long box(String s, Box[] boxes) {

		if (s.indexOf("=") > 0) {
			final String[] split = s.split("=");
			int hash = hash(split[0]);
			Lens lens = new Lens(split[0], Integer.parseInt(split[1]));
			boxes[hash].addLens(lens);
		} else {
			final String label = s.replace("-", "");
			int hash = hash(label);
			boxes[hash].removeLens(label);
		}
		return 0;
	}

}
