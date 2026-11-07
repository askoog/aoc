package se.askware.aoc2024.dec06;

import se.askware.aoc2024.common.AocBase;
import se.askware.aoc2024.common.CharCell;
import se.askware.aoc2024.common.Grid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Assignment extends AocBase {

    public static void main(String[] args) throws IOException {
        new Assignment().run();
    }

    @Override
    public long solvePartOne(List<String> input) {
        List<String[]> parsed =
                input.stream().map(s -> s.trim().split("\\s+")).toList();
        long totalSum = 0;
        for (int i = 0; i < parsed.getFirst().length; i++) {
            char ch = parsed.getLast()[i].charAt(0);
            long sum = ch == '*' ? 1 : 0;
            for (int j = 0; j < parsed.size() - 1; j++) {
                long num = Long.parseLong(parsed.get(j)[i]);
                sum = switch (ch) {
                    case '+':
                        yield num + sum;
                    case '*':
                        yield num * sum;
                    default:
                        throw new IllegalArgumentException();
                };
            }
            totalSum += sum;
        }
        return totalSum;
    }


    @Override
    public long solvePartTwo(List<String> input) {
        long totalSum = 0;
        Grid<CharCell> grid = Grid.charGrid(input);
        List<Long> numbers = new ArrayList<>();
        for (int column = grid.getNumColumns() - 1; column >= 0; column--) {

            numbers.add(getColumnValue(grid, column));

            char operator = grid.getCell(grid.getNumRows() - 1, column).value;
            if (operator != ' ') {
                totalSum += getTaskResult(operator, numbers);
                numbers = new ArrayList<>();
                column--;
            }
        }

        return totalSum;
    }

    private static long getTaskResult(char operator, List<Long> numbers) {
        long taskResult = switch (operator) {
            case '+' -> numbers.stream().mapToLong(Long::longValue).sum();
            case '*' -> numbers.stream().mapToLong(Long::longValue).reduce((l1, l2) -> l1 * l2).getAsLong();
            default -> throw new IllegalArgumentException();
        };
        return taskResult;
    }

    private static long getColumnValue(Grid<CharCell> grid, int column) {
        long colValue = 0;
        for (int j = 0; j < grid.getNumRows() - 1; j++) {
            char value1 = grid.getCell(j, column).value;
            if (value1 != ' ') {
                colValue = colValue * 10 + (value1 - '0');
            }
        }
        return colValue;
    }
}
