package se.askware.aoc2015.common;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

public abstract class AocBase {

	protected List<String> input;
	protected List<String> example;
	protected List<String> example2;
	 
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
		if (example != null) {
			System.out.println("** example **");
			solvePartOne(example);
		}
		System.out.println("** part one **");
		solvePartOne(input);
		if (example2 != null) {
			System.out.println("** example two **");
			solvePartTwo(example2);
		} else if (example != null) {
			System.out.println("** example two **");
			solvePartTwo(example);
		}
		System.out.println("** part two **");
		solvePartTwo(input);
	}
	
	public abstract void solvePartOne(List<String> input);
	public abstract void solvePartTwo(List<String> input);

	
	public <T> List<T> convertInput(List<String> input, Function<String,T> func){
		return input.stream().map(func::apply).collect(Collectors.toList());
	}
}
