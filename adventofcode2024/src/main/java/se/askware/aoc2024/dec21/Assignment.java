package se.askware.aoc2024.dec21;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.rowset.Joinable;

import se.askware.aoc2024.common.AocBase;
import se.askware.aoc2024.common.CharCell;
import se.askware.aoc2024.common.Direction;
import se.askware.aoc2024.common.Grid;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Grid<CharCell> keypad = Grid.charGrid(List.of("789", "456", "123", "X0A"));
		final Grid<CharCell> arrowpad = Grid.charGrid(List.of("X^A", "<v>>"));

		final Map<ButtonPair, Set<List<Instruction>>> paths = findAllPaths(arrowpad);
		final Map<ButtonPair, Set<List<Instruction>>> keypadPaths = findAllPaths(keypad);


		Map<ButtonPair, Set<List<Instruction>>> expandedPaths = paths;
		for (int i = 0; i < 1; i++) {
			expandedPaths = expandPathd(expandedPaths);
		}

		System.out.println(expandedPaths.get(new ButtonPair('A', '<')));

		/*int numExpansions = 2;
		for (int i = 0; i < numExpansions; i++) {
			for (Map.Entry<ButtonPair, List<List<Instruction>>> e : keypadPaths.entrySet()) {
				final List<List<Instruction>> value = e.getValue();
				List<List<Instruction>> next = new ArrayList<>();
				for (List<Instruction> instructions : value) {
					final List<List<Instruction>> expand = expand(instructions, paths);


				}

				value.forEach(l -> {
					final List<List<Instruction>> expand = expand(l, paths);
					e.getValue().addAll(expand);
				});
			}
		}*/
		//System.out.println(paths);
		//System.out.println(keypadPaths);

		int numArrowPads = 1;
		char current = 'A';
		for (String line : input) {
			Set<List<Instruction>> all = new HashSet<>();

			for (int i = 0; i < line.length(); i++) {
				char next = line.charAt(i);
				Set<List<Instruction>> instructions = keypadPaths.get(new ButtonPair(current, next));
				System.out.println(current + "->" + next + ": " + instructions);
				for (List<Instruction> instruction : instructions) {
					final List<List<Instruction>> expand = expand(instruction, expandedPaths);
					System.out.println("ex" + expand);
				}


/*
				for (int j = 0; j < 3; j++) {
					final Set<List<Instruction>> expand = expand2(instructions, paths);
					System.out.println("ex" + expand);
					final List<Instruction> instructions1 = expand.stream().sorted(Comparator.comparingInt(List::size)).findFirst().get();
					instructions = Set.of(instructions1);
					System.out.println("xhosen" + instructions);
				}
*/

				all.addAll(instructions);
				current = next;
			}

			final List<Instruction> instructions = all.stream().sorted(Comparator.comparingInt(List::size)).findFirst().orElseThrow();
			System.out.println(instructions);
			System.out.println(line);
			System.out.println(instructions.stream().map(Instruction::toString).collect(Collectors.joining()));
			final String replay = replay(instructions, arrowpad);
			System.out.println(replay);
			System.out.println(replay(toInstructions(replay(instructions, arrowpad)), arrowpad));
			System.out.println(replay(toInstructions(replay(toInstructions(replay(instructions, arrowpad)), arrowpad)), keypad));

/*
			System.out.println(all.stream().map(Instruction::toString).collect(Collectors.joining()));
			final String replay = replay(all, arrowpad);
			System.out.println(replay);
*/

			//System.out.println(replay(toInstructions(replay), keypad));

		}

		System.out.println(replay(toInstructions("<v<A>>^AvA^A<vA<AA>>^AAvA<^A>AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A"), arrowpad));
		System.out.println(replay(toInstructions("<A>Av<<AA>^AA>AvAA^A<vAAA>^A"), arrowpad));
		System.out.println(replay(toInstructions("^A<<^^A>>AvvvA"), keypad));

	}

	private Map<ButtonPair, Set<List<Instruction>>> expandPathd(Map<ButtonPair, Set<List<Instruction>>> paths) {
		final Map<ButtonPair, Set<List<Instruction>>> keypadPaths2 = new HashMap<>();
		for (Map.Entry<ButtonPair, Set<List<Instruction>>> e : paths.entrySet()) {
			final Set<List<Instruction>> value = e.getValue();
			final Set<List<Instruction>> lists = expand2(value, paths);
			keypadPaths2.put(e.getKey(), lists);
		}
		return keypadPaths2;
	}

	private static List<Instruction> toInstructions(String replay) {
		return replay.chars().mapToObj(c -> Instruction.valueOf((char) c)).collect(Collectors.toList());
	}

	Set<List<Instruction>> expand2(Collection<List<Instruction>> instructions, Map<ButtonPair, Set<List<Instruction>>> paths) {
		Set<List<Instruction>> result = new HashSet<>();
		for (List<Instruction> instruction : instructions) {
			result.addAll(expand(instruction, paths));
		}
		return result;
	}

	List<List<Instruction>> expand(List<Instruction> instructions, Map<ButtonPair, Set<List<Instruction>>> paths) {
		char current = 'A';
		List<List<Instruction>> result = new ArrayList<>();
		result.add(new ArrayList<>());
		for (int j = 0; j < instructions.size(); j++) {
			final Instruction dir = instructions.get(j);
			final Set<List<Instruction>> path = paths.get(new ButtonPair(current, dir.charValue()));
			if (path == null) {
				throw new IllegalArgumentException(current + " " + dir.charValue());
			}
			List<List<Instruction>> next = new ArrayList<>();
			for (List<Instruction> list : result) {
				for (List<Instruction> nextPath : path) {
					final ArrayList<Instruction> a = new ArrayList<>(list);
					a.addAll(nextPath);
					next.add(a);
				}
			}
			result = next;
			//			result.addAll(path);
			current = dir.charValue();
		}
		//System.out.println("expand " + instructions + ": " + result);
		return result;

	}

	String replay(List<Instruction> instructions, Grid<CharCell> grid) {
		CharCell current = grid.getAll().filter(c -> c.value == 'A').findFirst().orElseThrow();

		StringBuilder sb = new StringBuilder();
		for (Instruction instruction : instructions) {
			if (instruction == Instruction.PUSH) {
				sb.append(current.value);
			} else {
				CharCell c = current;
				current = grid.getOptionalCell(current.pos(), Direction.valueOf(instruction.charValue()))
						.orElseThrow(() -> new IllegalArgumentException(c + " " + instruction.toString()));
			}
		}
		return sb.toString();
	}

	private static Map<ButtonPair, Set<List<Instruction>>> findAllPaths(Grid<CharCell> grid) {
		Map<ButtonPair, Set<List<Instruction>>> paths = new HashMap<>();
		grid.print();
		final CharCell x = grid.findFirst(c -> c.value == 'X').orElseThrow();
		final CharCell aPos = grid.findFirst(c -> c.value == 'A').orElseThrow();

		grid.getAll().filter(c -> c.value != 'X').forEach(c -> {
			grid.getAll().filter(c1 -> c1.value != 'X' && c1 != c).forEach(c1 -> {
				List<Instruction> path = new ArrayList<>();
				List<Instruction> first = new ArrayList<>();
				List<Instruction> second = new ArrayList<>();
				final int rowDist = c.row - c1.row;
				for (int i = 0; i < Math.abs(rowDist); i++) {
					first.add(rowDist < 0 ? Instruction.DOWN : Instruction.UP);
				}
				final int colDist = c.col - c1.col;
				for (int i = 0; i < Math.abs(colDist); i++) {
					second.add(colDist < 0 ? Instruction.RIGHT : Instruction.LEFT);
				}

				final Set<List<Instruction>> permutations = new HashSet<>();
				permutations.add(join(first,second));
				permutations.add(join(second,first));

				Set<List<Instruction>> result = new HashSet<>();
				int skip = 0;
				for (List<Instruction> permutation : permutations) {
					boolean xFound = false;
					CharCell current = c;
					for (Instruction instruction : permutation) {
						if (instruction != Instruction.PUSH) {
							CharCell cfinal = current;
							final CharCell next = grid.getOptionalCell(current.pos(), Direction.valueOf(instruction.charValue()))
									.orElseThrow(() -> new IllegalArgumentException(cfinal + " " + instruction));
							if (next.value == 'X') {
								xFound = true;
								break;
							}
							current = next;
						}
					}
					if (!xFound) {
						final ArrayList<Instruction> a = new ArrayList<>(permutation);
						a.add(Instruction.PUSH);
						result.add(a);
					} else {
						skip++;
						//System.out.println("skip" + permutation);
					}

				}

				paths.put(new ButtonPair(c.value, c1.value), result);
				//System.out.println("permutations " + path + ": " + result + " skip:" + skip);

				// TODO: Handle 'X'
			});
			paths.put(new ButtonPair(c.value, c.value), Set.of(List.of(Instruction.PUSH)));
		});
		return paths;
	}

	private static <T> List<T> join(List<T> first, List<T> second) {
		List<T> result = new ArrayList<>(first);
		result.addAll(second);
		return result;
	}

	private static <T> Set<List<T>> permutate(List<T> input) {
		if (input.size() == 1) {
			return Set.of(List.of(input.get(0)));
		} else {
			Set<List<T>> result = new HashSet<>();
			for (int i = 0; i < input.size(); i++) {
				List<T> next = new ArrayList<>(input);
				final T remove = next.remove(i);
				permutate(next).forEach(l -> {
					List<T> a = new ArrayList<>();
					a.add(0, remove);
					a.addAll(l);
					result.add(a);
				});
			}
			return result;
		}
	}

	@Override
	public void solvePartTwo(List<String> input) {
	}

	enum Instruction {

		UP, DOWN, LEFT, RIGHT, PUSH;

		char charValue() {
			return switch (this) {
				case UP -> '^';
				case DOWN -> 'v';
				case LEFT -> '<';
				case RIGHT -> '>';
				case PUSH -> 'A';
			};
		}

		static Instruction valueOf(char c) {
			return switch (c) {
				case '^' -> UP;
				case 'v' -> DOWN;
				case '<' -> LEFT;
				case '>' -> RIGHT;
				case 'A' -> PUSH;
				default -> throw new IllegalArgumentException("Unknown instruction: " + c);
			};
		}

		@Override
		public String toString() {
			return charValue() + "";
		}
	}

	record ButtonPair(char button1, char button2) {
	}

	record ButtonPath(char start, char end, List<Instruction> path) {
		int length() {
			return path.size();
		}
	}
}
