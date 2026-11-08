package se.askware.aoc2026.common;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
        input = IOUtils.readLines(getClass().getResourceAsStream("inputs.txt"), StandardCharsets.UTF_8);
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
		long[] times = new long[4];
        long[] results = new long[4];
		if (example != null && modes.contains(RunMode.EXAMPLE1)) {
			System.out.println("** example **");
			times[0] = time(() -> {
                try{
                    results[0] = solveExampleOne(new ArrayList<>(example));
                    System.out.println(results[0]);
                } catch(Exception e){
                    results[0] = -1;
                    e.printStackTrace();
                }
            });
		}
		if (modes.contains(RunMode.PART1)) {
			System.out.println("** part one **");
			times[1] = time(() -> {
                try{
                    results[1] = solvePartOne(new ArrayList<>(input));
                    System.out.println(results[1]);
                } catch(Exception e){
                    results[1] = -1;
                    e.printStackTrace();
                }
            });
		}
		if (modes.contains(RunMode.EXAMPLE2)) {
            final List<String> e2 = example2 != null ? example2 : example;
            if (e2 != null) {
				System.out.println("** example two **");
				times[2] = time(() -> {
                    try {
                        results[2] = solveExampleTwo(new ArrayList<>(e2));
                        System.out.println(results[2]);

                    } catch (Exception e) {
                        results[2] = -1;
                        e.printStackTrace();
                    }
                });
			}
		}
		if (modes.contains(RunMode.PART2)) {
			System.out.println("** part two **");
			times[3] = time(() -> {
                try {
                    results[3] = solvePartTwo(new ArrayList<>(input));
                    System.out.println(results[3]);

                } catch (Exception e) {
                    results[3] = -1;
                    e.printStackTrace();
                } 
            });
		}
        System.out.println();
        System.out.println("Results: ");
        System.out.println("Example 1: " + results[0] + "\t(" + times[0] + "ms)");
        System.out.println("Part 1:    " + results[1] + "\t(" + times[1] + "ms)");
        System.out.println("Example 2: " + results[2] + "\t(" + times[2] + "ms)");
        System.out.println("Part 2:    " + results[3] + "\t(" + times[3] + "ms)");
	}

	public long time(Runnable r) {
		long start = System.nanoTime();
		r.run();
		return (System.nanoTime() - start) / 1000000;
	}

	public abstract long solvePartOne(List<String> input);

	public abstract long solvePartTwo(List<String> input);

	public long solveExampleOne(List<String> input) {
		return solvePartOne(input);
	}

	public long solveExampleTwo(List<String> input) {
		return solvePartTwo(input);
	}

	public <T> List<T> convertInput(List<String> input, Function<String, T> func) {
		return input.stream().map(func).collect(Collectors.toList());
	}

	public <T> List<T> mapInput(List<String> input, Function<String, T> func) {
		return input.stream().map(func).collect(Collectors.toList());
	}

	public List<Integer> toIntList(String line) {
		return toIntList(line, " ");
	}
	public List<Integer> toIntList(String line, String delimiter) {
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
