package se.askware.aoc2015.dec07;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.askware.aoc2015.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {

		Map<String, Long> wires = new HashMap<>();
		for (String s : input) {
			Map<Pattern, Consumer<Matcher>> patterns = Map.of(
					Pattern.compile("(\\d+) -> ([a-z]+)"),
					m -> {
						final long value = Long.parseLong(m.group(1));
						final String wire = m.group(2);
						wires.put(wire, value);
					},
					Pattern.compile("([a-z]+) -> ([a-z]+)"),
					m -> {
						final String wire1 = m.group(1);
						final String wire2 = m.group(2);
						wires.put(wire2, wires.get(wire1));
					},
					Pattern.compile("([a-z]+) AND ([a-z]+) -> ([a-z]+)"),
					m -> {
						final String wire1 = m.group(1);
						final String wire2 = m.group(2);
						final String wire3 = m.group(3);
						wires.put(wire3, wires.get(wire1) & wires.get(wire2));
					},
					Pattern.compile("([a-z]+) OR ([a-z]+) -> ([a-z]+)"),
					m -> {
						final String wire1 = m.group(1);
						final String wire2 = m.group(2);
						final String wire3 = m.group(3);
						wires.put(wire3, wires.get(wire1) | wires.get(wire2));
					},
					Pattern.compile("([a-z]+) LSHIFT (\\d+) -> ([a-z]+)"),
					m -> {
						final String wire1 = m.group(1);
						final int val = Integer.parseInt(m.group(2));
						final String wire2 = m.group(3);
						wires.put(wire2, wires.get(wire1) << val);
					},
					Pattern.compile("([a-z]+) RSHIFT (\\d+) -> ([a-z]+)"),
					m -> {
						final String wire1 = m.group(1);
						final int val = Integer.parseInt(m.group(2));
						final String wire2 = m.group(3);
						wires.put(wire2, wires.get(wire1) >> val);
					},
					Pattern.compile("NOT ([a-z]+) -> ([a-z]+)"),
					m -> {
						final String wire1 = m.group(1);
						final String wire2 = m.group(2);
						wires.put(wire2, ~wires.get(wire1));
					}
			);
			final Map.Entry<Pattern, Consumer<Matcher>> entry = patterns.entrySet()
					.stream().filter(e -> e.getKey().matcher(s).matches())
					.findFirst().orElseThrow(() -> new RuntimeException("unmatched " + s));
			System.out.println(s);
			final Matcher matcher = entry.getKey().matcher(s);
			if (matcher.find()) {
				entry.getValue().accept(matcher);
			}

		}

		System.out.println(wires);
	}

	@Override
	public void solvePartTwo(List<String> input) {
	}

}
