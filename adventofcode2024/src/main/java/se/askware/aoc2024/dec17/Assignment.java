package se.askware.aoc2024.dec17;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import se.askware.aoc2024.common.AocBase;

public class Assignment extends AocBase {

	static String expectedOutput = "";

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {

		int a = Integer.parseInt(input.get(0).split(":")[1].trim());
		int b = Integer.parseInt(input.get(1).split(":")[1].trim());
		int c = Integer.parseInt(input.get(2).split(":")[1].trim());
		Registers registers = new Registers(a, b, c, 0, "");

		final int[] code = Arrays.stream(input.get(4).split(" ")[1].split(",")).mapToInt(Integer::parseInt).toArray();
		System.out.println(runOps(registers, code));
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final String wantedPattern = input.get(4).split(" ")[1];
		final int[] code = Arrays.stream(wantedPattern.split(",")).mapToInt(Integer::parseInt).toArray();
		System.out.println(wantedPattern);

		for (long num = 1; num < Long.MAX_VALUE; num++) {
			final Registers r = runOps(new Registers(num, 0, 0), code);
			if (wantedPattern.equals(r.output)) {
				System.out.println(num + ":" + r);
				break;
			}
			if (wantedPattern.endsWith(r.output)) {
				System.out.println(num + ":" + r);
				num = num * 8L - 1;
			}

		}

	}

	private Registers runOps(Registers registers, int[] code) {
		while (registers.instructionPointer < code.length) {
			final int op = code[registers.instructionPointer];
			final int comboOp = code[registers.instructionPointer + 1];
			registers = doOperation(op, comboOp, registers);
		}
		return registers;
	}

	record Registers(long a, long b, long c, int instructionPointer, String output) {
		Registers(long a, long b, long c) {
			this(a, b, c, 0, "");
		}

		Registers jump(int jump) {
			return new Registers(a, b, c, instructionPointer + jump, output);
		}

		Registers setPointer(int pointer) {
			return new Registers(a, b, c, pointer, output);
		}

		Registers setA(long value) {
			return new Registers(value, b, c, instructionPointer, output);
		}

		Registers setB(long value) {
			return new Registers(a, value, c, instructionPointer, output);
		}

		Registers setC(long value) {
			return new Registers(a, b, value, instructionPointer, output);
		}

		Registers output(long output) {
			return new Registers(a, b, c, instructionPointer, this.output + (this.output.isBlank() ? "" : ",") + output);
		}

	}

	private Registers doOperation(int op, int literal, Registers registers) {
		long comboValue = getComboValue(literal, registers);
		if (op == 0) {
			return registers.setA((long) (registers.a / Math.pow(2, comboValue))).jump(2);
		} else if (op == 1) {
			return registers.setB(registers.b ^ literal).jump(2);
		} else if (op == 2) {
			return registers.setB(comboValue % 8).jump(2);
		} else if (op == 3) {
			if (registers.a == 0) {
				return registers.jump(2);
			}
			return registers.setPointer(literal);
		} else if (op == 4) {
			return registers.setB(registers.b ^ registers.c).jump(2);
		} else if (op == 5) {
			return registers.output(comboValue % 8).jump(2);
		} else if (op == 6) {
			return registers.setB((long) (registers.a / Math.pow(2, comboValue))).jump(2);
		} else if (op == 7) {
			return registers.setC((long) (registers.a / Math.pow(2, comboValue))).jump(2);
		}
		throw new IllegalArgumentException("" + op);
	}

	private long getComboValue(int op, Registers registers) {
		if (op < 4) {
			return op;
		} else if (op == 4) {
			return registers.a;
		} else if (op == 5) {
			return registers.b;
		} else if (op == 6) {
			return registers.c;
		} else {
			throw new IllegalArgumentException("Invalid combo op: " + op);
		}
	}

}
