package se.askware.aoc2023.common;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

public abstract class AocBase {

	protected List<String> input;
	protected List<String> example;
	protected List<String> example2;
	protected static LogLevel logLevel = LogLevel.INFO;

	public enum RunMode {
		EXAMPLE1, PART1, EXAMPLE2, PART2
	}

	public AocBase withRunMode(RunMode... mode) {
		modes = Set.of(mode);
		return this;
	}

	public AocBase withLogLevel(LogLevel level) {
		logLevel = level;
		return this;
	}

	Set<RunMode> modes = Set.of(RunMode.values());

	public enum LogLevel {
		ALL, DEBUG, INFO, WARN
	}

	public AocBase() {
		try {
			input = IOUtils.readLines(getClass().getResourceAsStream("inputs.txt"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		try {
			example = IOUtils.readLines(getClass().getResourceAsStream("example.txt"), StandardCharsets.UTF_8);
		} catch (Exception e) {
		}
		try {
			example2 = IOUtils.readLines(getClass().getResourceAsStream("example2.txt"), StandardCharsets.UTF_8);
		} catch (Exception e) {
		}
	}

	public void run() {
		if (example != null && modes.contains(RunMode.EXAMPLE1)) {
			System.out.println("** example **");
			solvePartOne(example);
		}
		if (modes.contains(RunMode.PART1)) {
			System.out.println("** part one **");
			solvePartOne(input);
		}
		if (modes.contains(RunMode.EXAMPLE2)) {
			if (example2 != null) {
				System.out.println("** example two **");
				solvePartTwo(example2);
			} else if (example != null) {
				System.out.println("** example two **");
				solvePartTwo(example);
			}
		}
		if (modes.contains(RunMode.PART2)) {
			System.out.println("** part two **");
			solvePartTwo(input);
		}
	}

	public abstract void solvePartOne(List<String> input);

	public abstract void solvePartTwo(List<String> input);

	public <T> List<T> convertInput(List<String> input, Function<String, T> func) {
		return input.stream().map(func).collect(Collectors.toList());
	}

	public <T> List<T> mapInput(List<String> input, Function<String, T> func) {
		return input.stream().map(func).collect(Collectors.toList());
	}

	public static List<Integer> toIntList(String line) {
		return toIntList(line, " ");
	}
	public static List<Integer> toIntList(String line, String delimiter) {
		return Arrays.stream(line.split(delimiter))
				.map(Integer::parseInt)
				.collect(Collectors.toList());
	}

	public static void debug(String format, Object... args) {
		log(LogLevel.DEBUG, format, args);
	}

	public static void debug(Object message) {
		log(LogLevel.DEBUG, String.valueOf(message));
	}

	public static void log(LogLevel level, String format, Object... args) {
		if (logLevel.compareTo(level) <= 0) {
			System.out.println(String.format(format, args));
		}
	}

	/**
	 * <a href="https://en.wikipedia.org/wiki/Least_common_multiple">Least common multiple</a> for a list of longs.
	 */
	protected static long lcm(List<Long> paths) {
		long result = 1;
		for (final long val : paths) {
			result = lcm(result, val);
		}
		return result;
	}

	protected static long lcm(long number1, long number2) {
		long absHigherNumber = Math.max(Math.abs(number1), Math.abs(number2));
		long absLowerNumber = Math.min(Math.abs(number1), Math.abs(number2));
		long lcm = absHigherNumber;
		while (lcm % absLowerNumber != 0) {
			lcm += absHigherNumber;
		}
		return lcm;
	}
}
