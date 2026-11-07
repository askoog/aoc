package se.askware.aoc2025.dec07;

import se.askware.aoc2025.common.AocBase;
import se.askware.aoc2025.common.CharCell;
import se.askware.aoc2025.common.Grid;
import se.askware.aoc2025.common.GridPos;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Assignment extends AocBase {

    public static void main(String[] args) throws IOException {
        new Assignment().run();
    }

    @Override
    public long solvePartOne(List<String> input) {
        Grid<CharCell> grid = Grid.charGrid(input);

        long numSplits = 0;
        for (int row = 1; row < grid.getNumRows(); row++) {
            for (int column = 0; column < grid.getNumColumns(); column++) {
                char above = grid.getCell(row - 1, column).value;
                if (above == '|' || above == 'S') {
                    CharCell current = grid.getCell(row, column);
                    if (current.value == '^') {
                        CharCell left = grid.getCell(row, column - 1);
                        CharCell right = grid.getCell(row, column + 1);
                        if (left.value != '|') {
                            left.value = '|';
                        }
                        right.value = '|';
                        numSplits++;
                    } else {
                        current.value = '|';
                    }
                }
            }
        }
        return numSplits;
    }

    @Override
    public long solvePartTwo(List<String> input) {
        Grid<CharCell> grid = Grid.charGrid(input);

        Map<GridPos, AtomicLong> seen = new HashMap<>();
        for (int row = 1; row < grid.getNumRows(); row++) {
            for (int column = 0; column < grid.getNumColumns(); column++) {
                CharCell above = grid.getCell(row - 1, column);
                if (above.value == '|' || above.value == 'S') {
                    AtomicLong numPathsToAbove = seen.computeIfAbsent(above.pos(), c-> new AtomicLong(1));
                    CharCell current = grid.getCell(row, column);
                    if (current.value == '^') {
                        CharCell left = grid.getCell(row, column - 1);
                        CharCell right = grid.getCell(row, column + 1);
                        left.value = '|';
                        right.value = '|';
                        seen.computeIfAbsent(left.pos(), c -> new AtomicLong(0)).addAndGet(numPathsToAbove.get());
                        seen.computeIfAbsent(right.pos(), c-> new AtomicLong(0)).addAndGet(numPathsToAbove.get());
                    } else {
                        seen.computeIfAbsent(current.pos(), c-> new AtomicLong(0)).addAndGet(numPathsToAbove.get());
                        current.value = '|';
                    }
                }
            }
        }
        return seen.entrySet().stream().filter(e -> e.getKey().getRow() == grid.getNumRows() -1).mapToLong(e -> e.getValue().get()).sum();
    }

}
