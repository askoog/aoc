package se.askware.aoc2024.dec07;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.askware.aoc2024.common.AocBase;

public class Assignment extends AocBase {

	Map<Integer, List<List<Operation>>> permutations = new HashMap<>();

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Operation[] validOps = { Operation.PLUS, Operation.MUL };
		solve(input, validOps);
		permutations.clear();

	}

	private void solve(List<String> input, Operation[] validOps) {
		long result = 0;
		for (String line : input) {
			final String[] split = line.split(": ");
			final long target = Long.parseLong(split[0]);
			final long[] parts = Arrays.stream(split[1].split(" ")).mapToLong(Long::parseLong).toArray();

			List<List<Operation>> results = find(target, parts, validOps);
			if (!results.isEmpty()) {
				result += target;
			}
			/*for (List<Operation> operations : results) {
				//				System.out.println(target + " = " + Arrays.stream(parts).boxed().toList() + "  " + operations);
			}*/
		}
		System.out.println(result);
	}

	private List<List<Operation>> find(long target, long[] parts, Operation[] operations) {
		List<List<Operation>> results = new ArrayList<>();
		final List<List<Operation>> allPermutations = getPermutations(operations, parts.length);

		//System.out.println(allPermutations);

		for (List<Operation> permutation : allPermutations) {
			long result = parts[0];
			for (int i = 0; i < parts.length - 1; i++) {
				result = permutation.get(i).apply(result, parts[i + 1]);
				if (result > target) {
					break;
				}
			}
			if (result == target) {
				results.add(permutation);
				return results;
			}
		}
		return results;
	}

	private List<List<Operation>> getPermutations(Operation[] operations, int length) {
		return permutations.computeIfAbsent(length, l -> {
			List<List<Operation>> allPermutations = new ArrayList<>();

			for (int i = 0; i < operations.length; i++) {
				final ArrayList<Operation> e = new ArrayList<>();
				e.add(operations[i]);
				allPermutations.add(e);
			}
			for (int i = 0; i < length - 2; i++) {
				List<List<Operation>> newPermutations = new ArrayList<>();
				for (int j = 0; j < operations.length; j++) {
					for (List<Operation> permutation : allPermutations) {
						List<Operation> newPermutation = new ArrayList<>(permutation);
						newPermutation.add(operations[j]);
						newPermutations.add(newPermutation);
					}
				}
				allPermutations = newPermutations;
			}
			return allPermutations;
		});
	}

	@Override
	public void solvePartTwo(List<String> input) {
		solve(input, Operation.values());
	}

	enum Operation {
		PLUS, MUL, OR;

		long apply(long a, long b) {
			return switch (this) {
				case PLUS -> a + b;
				case MUL -> a * b;
				case OR -> Long.parseLong(String.valueOf(a) + String.valueOf(b));
			};
		}
	}

}
