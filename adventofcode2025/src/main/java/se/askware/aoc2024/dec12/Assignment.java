package se.askware.aoc2024.dec12;

import se.askware.aoc2024.common.AocBase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Assignment extends AocBase {

    public static void main(String[] args) throws IOException {
        new Assignment().run();
    }

    @Override
    public long solvePartOne(List<String> input) {
        List<Pattern> patterns = new ArrayList<>();
        long sum = 0;
        for (int i = 0; i < input.size(); i++) {
            String s1 = input.get(i);
            System.out.println(s1);
            if (s1.contains("x")){
                long[] grid = Arrays.stream(s1.split(":")[0].split("x")).mapToLong(Long::parseLong).toArray();
                int[] pieces = Arrays.stream(s1.split(":")[1].trim().split(" ")).mapToInt(Integer::parseInt).toArray();
                long gridSize = grid[0]*grid[1];

                long totalSize = 0;
                for (int j = 0; j < pieces.length; j++) {
                    totalSize += pieces[j] * patterns.get(j).size();
                }
                System.out.println(gridSize + " " + totalSize);
                
                if (gridSize > totalSize){
                    
                    sum++;
                }
            } else {
                i++;
                String s = input.get(i++) + "\n" +
                        input.get(i++) + "\n" +
                        input.get(i++);
                Pattern p = new Pattern(s);
                System.out.println(p);
                patterns.add(p);
            }
        }
        return sum;
    }

    @Override
    public long solvePartTwo(List<String> input) {
        return -1;
    }

    record Pattern(String input) {
        long size() {
            return input.chars().filter(c -> c == '#').count();
        }
    }
}
