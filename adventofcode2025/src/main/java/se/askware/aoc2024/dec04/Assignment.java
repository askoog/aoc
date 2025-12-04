package se.askware.aoc2024.dec04;

import se.askware.aoc2024.common.AocBase;
import se.askware.aoc2024.common.CharCell;
import se.askware.aoc2024.common.Grid;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Assignment extends AocBase {

    public static void main(String[] args) throws IOException {
        new Assignment().run();
    }

    @Override
    public long solvePartOne(List<String> input) {
        Grid<CharCell> grid = Grid.charGrid(input);
        return grid.getAll().filter(c -> canBeRemoved(c, grid)).count();
    }

    @Override
    public long solvePartTwo(List<String> input) {
        Grid<CharCell> grid = Grid.charGrid(input);
        AtomicLong numRolls = new AtomicLong();
        while (true) {
            long sum = grid.getAll()
                    .filter(c -> removeIfPossible(c, grid))
                    .count();
            if (sum == 0) {
                return numRolls.longValue();
            }
            numRolls.addAndGet(sum);
        }
    }

    private static boolean removeIfPossible(CharCell c, Grid<CharCell> grid) {
        if (canBeRemoved(c, grid)) {
            c.value = ' ';
            return true;
        } else {
            return false;
        }
    }

    private static boolean canBeRemoved(CharCell c, Grid<CharCell> grid) {
        return isRoll(c) && getNumNeighborRolls(c, grid) < 4;
    }

    private static long getNumNeighborRolls(CharCell c, Grid<CharCell> grid) {
        return grid.getAllNeighbors(c).stream()
                .filter(Assignment::isRoll)
                .count();
    }

    private static boolean isRoll(CharCell c) {
        return c.value == '@';
    }
}
