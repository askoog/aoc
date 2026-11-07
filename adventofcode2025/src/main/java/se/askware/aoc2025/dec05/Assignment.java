package se.askware.aoc2025.dec05;

import se.askware.aoc2025.common.AocBase;
import se.askware.aoc2025.common.Range;

import java.io.IOException;
import java.util.*;

public class Assignment extends AocBase {

    public static void main(String[] args) throws IOException {
        new Assignment().run();
    }

    @Override
    public long solvePartOne(List<String> input) {
        boolean range = true;
        List<Range> ranges = new ArrayList<>();
        List<Long> ingredientIds = new ArrayList<>();
        for (String line : input) {
            if (line.isBlank()) {
                range = false;
            } else if (range) {
                ranges.add(Range.valueOf(line));
            } else {
                ingredientIds.add(Long.parseLong(line));
            }
        }
        return ingredientIds.stream().filter(i -> isInRange(ranges, i)).count();
    }

    public boolean isInRange(List<Range> ranges, Long ingredientId) {
        return ranges.stream().anyMatch(r -> r.includes(ingredientId));
    }

    @Override
    public long solvePartTwo(List<String> input) {
        boolean range = true;
        List<Range> ranges = new ArrayList<>();
        for (String line : input) {
            if (line.isBlank()) {
                break;
            }
            ranges.add(Range.valueOf(line));
        }

        List<Range> merged = new ArrayList<>(ranges);
        do {
            ranges = merged;
            merged = merge(ranges);
        } while (merged.size() != ranges.size());
        return merged.stream().mapToLong(Range::distance).sum();
    }

    private List<Range> merge(List<Range> ranges) {
        List<Range> tmp = new ArrayList<>(ranges);
        List<Range> result = new ArrayList<>();
        while (!tmp.isEmpty()) {
            Range r = tmp.removeFirst();
            for (int i = 0; i < tmp.size(); i++) {
                if (tmp.get(i).overlaps(r)) {
                    Range remove = tmp.remove(i);
                    r = r.merge(remove);
                    break;
                }
            }
            result.add(r);
        }
        return result;

    }
}
