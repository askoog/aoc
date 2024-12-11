package se.askware.aoc2022.common;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

public abstract class AocBase {

	protected List<String> input;
	protected List<String> example;
	protected List<String> example2;
	protected LogLevel logLevel = LogLevel.INFO;

	public enum RunMode {
		EXAMPLE1,PART1,EXAMPLE2,PART2
	}

	public AocBase withRunMode(RunMode ... mode){
		modes = Set.of(mode);
		return this;
	}

	public AocBase withLogLevel(LogLevel level){
		logLevel = level;
		return this;
	}

	Set<RunMode> modes = Set.of(RunMode.values());

	public enum LogLevel {
		ALL,DEBUG,INFO, WARN
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

	
	public <T> List<T> convertInput(List<String> input, Function<String,T> func){
		return input.stream().map(func::apply).collect(Collectors.toList());
	}
	public <T> List<T> mapInput(List<String> input, Function<String,T> func){
		return input.stream().map(func::apply).collect(Collectors.toList());
	}

	public void debug(String format, Object ... args){
		log(LogLevel.DEBUG, format, args);
	}

	public void log(LogLevel level, String format, Object ... args){
		if (logLevel.compareTo(level) <= 0){
			System.out.println(String.format(format,args));
		}
	}
}
