package se.askware.aoc2025.dec02;

import se.askware.aoc2025.common.AocBase;
import se.askware.aoc2025.common.Range;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class Assignment extends AocBase {

    public static void main(String[] args) throws IOException {
        new Assignment().run();
    }

    @Override
    public long solvePartOne(List<String> input) {
        return calculateInvalidIds(input, this::isInvalidId);
    }

    private long calculateInvalidIds(List<String> input, Function<Long, Boolean> check) {
        AtomicLong sum = new AtomicLong();
        Arrays.stream(input.getFirst().split(","))
                .forEach(line -> {
                    Range range = Range.valueOf(line);
                    range.stream().forEach(id -> {

                        if (check.apply(id)) {
                            sum.addAndGet(id);
                        }

                    });
                });

        return sum.get();
    }

    private boolean isInvalidId(long id) {
        String s = String.valueOf(id);
        if (s.length() % 2 == 0) {
            int offset = s.length() / 2;
            for (int i = 0; i < offset; i++) {
                if (s.charAt(i) != s.charAt(i + offset)) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }


    @Override
    public long solvePartTwo(List<String> input) {
        return calculateInvalidIds(input, this::isInvalidId2);
    }

    private Boolean isInvalidId2(Long id) {
        String s = String.valueOf(id);
        int numParts = 2;
        while (true) {
            if (numParts > s.length()){
                return false;
            }
            if (s.length() % numParts == 0) {
                Set<String> set = new HashSet<>();
                int offset = 0;
                int split = s.length() / numParts;
                for (int j = 0; j < numParts; j++) {
                    set.add(s.substring(offset, offset + split));
                    if (set.size() > 1){
                        break;
                    }
                    offset += split;
                }
                if (set.size() == 1) {
                    return true;
                }
            }
            numParts++;
        }
    }
}
