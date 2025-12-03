package se.askware.aoc2024.dec03;

import se.askware.aoc2024.common.AocBase;

import java.io.IOException;
import java.util.List;

public class Assignment extends AocBase {

    public static void main(String[] args) throws IOException {
        new Assignment().run();
    }

    @Override
    public long solvePartOne(List<String> input) {
        return input.stream().mapToLong(this::getMaximumJoltage).sum();
    }

    private long getMaximumJoltage(String s) {
        return getMaximumJoltage(s, 2);
    }

    private long getMaximumJoltage(String s, int length) {
        if (length == 1) {
            return getMaxValue(s);
        }
        int first = getMaxValue(s.substring(0, s.length() - (length - 1)));
        int i = s.indexOf("" + first);
        long second = getMaximumJoltage(s.substring(i + 1), length - 1);
        return Long.parseLong(("" + first) + second);
    }

    private static int getMaxValue(String s) {
        int max = 0;
        for (int i = 0; i < s.length(); i++) {
            int c = s.charAt(i) - '0';
            if (c == 9) {
                return 9;
            }
            max = Math.max(max, c);
        }
        return max;
    }

    @Override
    public long solvePartTwo(List<String> input) {
        return input.stream().mapToLong(l -> getMaximumJoltage(l, 12)).sum();
    }
}
