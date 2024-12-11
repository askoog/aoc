package se.askware.aoc2022.dec10;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.askware.aoc2022.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		Pattern p = Pattern.compile("add([a-z]) (-?\\d+)");
		AtomicInteger cycle = new AtomicInteger();
		Map<String, AtomicInteger> registers = new HashMap<>();
		registers.put("x", new AtomicInteger(1));
		AtomicInteger signalSum = new AtomicInteger();
		for (String line : input) {
			final Matcher matcher = p.matcher(line);
			if (matcher.matches()) {
				final String reg = matcher.group(1);
				int val = Integer.parseInt(matcher.group(2));
				passCycles(cycle, 2, registers, signalSum);
				registers.computeIfAbsent(reg, r -> new AtomicInteger()).addAndGet(val);
				//System.out.println(reg);
			} else if (line.equals("noop")) {
				passCycles(cycle, 1, registers, signalSum);
			}
		}

		System.out.println(cycle);
		System.out.println(registers);
		System.out.println(signalSum.get());
	}

	private void passCycles(AtomicInteger cycle, int numCycles, Map<String, AtomicInteger> registers, AtomicInteger sum) {
		Set<Integer> summingCycles = Set.of(20, 60, 100, 140, 180, 220);
		for (int i = 0; i < numCycles; i++) {
			if (summingCycles.contains(cycle.incrementAndGet())) {
				System.out.println(cycle.get() + ": " + registers.get("x") + " ->" + (cycle.get() * registers.get("x").get()));
				sum.addAndGet(cycle.get() * registers.get("x").get());
			}
		}
	}

	@Override
	public void solvePartTwo(List<String> input) {
		Pattern p = Pattern.compile("add([a-z]) (-?\\d+)");
		AtomicInteger cycle = new AtomicInteger();
		Map<String, AtomicInteger> registers = new HashMap<>();
		registers.put("x", new AtomicInteger(1));
		for (String line : input) {
			final Matcher matcher = p.matcher(line);
			if (matcher.matches()) {
				final String reg = matcher.group(1);
				int val = Integer.parseInt(matcher.group(2));
				passCycles2(cycle, 2, registers);
				registers.computeIfAbsent(reg, r -> new AtomicInteger()).addAndGet(val);
			} else if (line.equals("noop")) {
				passCycles2(cycle, 1, registers);
			}
		}
		System.out.println();
		System.out.println(cycle);
		System.out.println(registers);
	}

	private void passCycles2(AtomicInteger cycle, int numCycles, Map<String, AtomicInteger> registers) {
		int spritePos = registers.get("x").get();
		for (int i = 0; i < numCycles; i++) {
			int curPos = cycle.get() % 40;
			if (curPos == 0){
				System.out.println();
			}
			cycle.incrementAndGet();
			if (spritePos >= curPos - 1 && spritePos <= curPos + 1){
				System.out.print("#");
			} else {
				System.out.print(".");
			}

		}
	}
}
