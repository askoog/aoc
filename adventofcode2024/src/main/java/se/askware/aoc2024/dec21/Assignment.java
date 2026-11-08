package se.askware.aoc2024.dec21;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        final Grid<CharCell> arrowpad = Grid.charGrid(List.of("X^A", "<v>"));

        final Map<ButtonPair, Set<List<Instruction>>> paths = findAllPaths(arrowpad);
        final Map<ButtonPair, Set<List<Instruction>>> keypadPaths = findAllPaths(keypad);

        long sum = 0;
        for (String code : input) {
            System.out.println(code);
            List<String> p2 = getPaths("A" + code, keypadPaths);
            String shortest = null;
            for (String instruction : p2) {
                //System.out.println(instruction);
                List<String> paths1 = getPaths("A" + instruction, paths);
                //System.out.println(paths1);
                for (String instruction2 : paths1) {
                    //System.out.println(instruction2);
                    List<String> paths2 = getPaths("A" + instruction2, paths);
                    //System.out.println(paths2);
                    for (String s : paths2) {
                        shortest =  shortest == null ? s : shortest.length() < s.length() ? shortest : s;
                    }
                }
            }
            long n = Long.parseLong(code.replaceAll("A", "")) * shortest.length();
            sum += n;
        }
        System.out.println(sum);

    }

    private static List<String> getPaths(String code, Map<ButtonPair, Set<List<Instruction>>> keypadPaths) {
        List<String> p2 = new ArrayList<>();
        p2.add("");
        for (int i = 0; i < code.length() - 1; i++) {
            ButtonPair buttonPair = new ButtonPair(code.charAt(i), code.charAt(i + 1));
            Set<List<Instruction>> lists = keypadPaths.get(buttonPair);
            //System.out.println(lists);
            List<String> p3 = new ArrayList<>();
            for (String s : p2) {
                for (List<Instruction> list : lists) {
                    p3.add(s + list.stream().map(Instruction::toString).collect(Collectors.joining()));
                }
            }
            p2 = p3;
            //System.out.println(p3);
        }
        return p2;
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
                permutations.add(join(first, second));
                permutations.add(join(second, first));

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

    @Override
    public void solvePartTwo(List<String> input) {
        System.out.println(complexitySum(input, 25));
    }

    private static long complexitySum(List<String> input, int numArrowRobots) {
        final Map<ButtonPair, Set<List<Instruction>>> keypadPaths = findAllPaths(Grid.charGrid(List.of("789", "456", "123", "X0A")));
        final Map<ButtonPair, Set<List<Instruction>>> arrowPaths = findAllPaths(Grid.charGrid(List.of("X^A", "<v>")));
        final Map<CostKey, Long> cache = new HashMap<>();

        long sum = 0;
        for (String code : input) {
            final String sequence = "A" + code;
            long length = 0;
            for (int i = 0; i < sequence.length() - 1; i++) {
                final ButtonPair pair = new ButtonPair(sequence.charAt(i), sequence.charAt(i + 1));
                long best = Long.MAX_VALUE;
                for (List<Instruction> path : keypadPaths.get(pair)) {
                    best = Math.min(best, pathCost(path, numArrowRobots, arrowPaths, cache));
                }
                length += best;
            }
            sum += Long.parseLong(code.replaceAll("A", "")) * length;
        }
        return sum;
    }

    /**
     * Number of human presses needed to type the given path on an arrowpad
     * that has {@code depth} arrowpads above it. The arm always starts on 'A',
     * since every path ends with a push.
     */
    private static long pathCost(List<Instruction> path, int depth, Map<ButtonPair, Set<List<Instruction>>> arrowPaths,
            Map<CostKey, Long> cache) {
        long total = 0;
        char current = 'A';
        for (Instruction instruction : path) {
            total += pairCost(new ButtonPair(current, instruction.charValue()), depth, arrowPaths, cache);
            current = instruction.charValue();
        }
        return total;
    }

    private static long pairCost(ButtonPair pair, int depth, Map<ButtonPair, Set<List<Instruction>>> arrowPaths,
            Map<CostKey, Long> cache) {
        if (depth == 0) {
            return 1;
        }
        final CostKey key = new CostKey(pair, depth);
        final Long cached = cache.get(key);
        if (cached != null) {
            return cached;
        }
        long best = Long.MAX_VALUE;
        for (List<Instruction> path : arrowPaths.get(pair)) {
            best = Math.min(best, pathCost(path, depth - 1, arrowPaths, cache));
        }
        cache.put(key, best);
        return best;
    }

    record CostKey(ButtonPair pair, int depth) {
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

}
