package se.askware.aoc2015.dec15;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.askware.aoc2015.common.AocBase;

public class Assignment extends AocBase {

    public static void main(String[] args) throws IOException {
        new Assignment().run();
    }

    @Override
    public void solvePartOne(List<String> input) {
        Pattern p = Pattern.compile("(.+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)");
        final List<Ingredient> list = buildIngredients(input, p);

        long max = findBest(list);
        System.out.println("max: " + max);
    }

    private long findBest(List<Ingredient> list) {
        return findBest(list, Integer.MAX_VALUE);
    }

    private long findBest(List<Ingredient> list, int caloriesMax) {
        return findBest(list, 100, new Ingredient("", 0, 0, 0, 0, 0), caloriesMax);
        
    }
    private long findBest(List<Ingredient> list, int remaining, Ingredient current, int caloriesMax) {
        long max = 0;
        Ingredient next = list.getFirst();
        if (list.size() == 1) {
            Ingredient multiply = next.multiply(remaining);

            Ingredient sum = current.sum(multiply);
            if (caloriesMax == Integer.MAX_VALUE || sum.calories == caloriesMax) {
                long l = Math.max(0, sum.capacity) * Math.max(0, sum.flavor)
                        * Math.max(0, sum.texture) * Math.max(0, sum.durability);
                max = Math.max(max, l);
            }

        }
        
        for (int i = 0; i <= remaining; i++) {
            Ingredient multiply = next.multiply(i);

            if (list.size() > 1) {
                long tmp = findBest(list.subList(1, list.size()), remaining - i, current.sum(multiply), caloriesMax);
                max = Math.max(max, tmp);
            }
        }
        return max;
    }


    private static List<Ingredient> buildIngredients(List<String> input, Pattern p) {
        final List<Ingredient> list = input.stream().map(line -> {
            final Matcher matcher = p.matcher(line);
            matcher.find();
            return new Ingredient(matcher.group(1), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)),
                    Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6)));
        }).toList();
        return list;
    }

    long mul(int[] array) {
        return Arrays.stream(array).reduce(1, (i1, i2) -> i1 * i2);
    }

    int sum(int[] array) {
        return Arrays.stream(array).sum();
    }

    record Ingredient(String name, long capacity, long durability, long flavor, long texture, long calories) {
        Ingredient multiply(long factor) {
            return new Ingredient("",
                    capacity * factor,
                    durability * factor,
                    flavor * factor,
                    texture * factor,
                    calories * factor);
        }

        Ingredient sum(Ingredient other) {
            return new Ingredient("",
                    capacity + other.capacity,
                    durability + other.durability,
                    flavor + other.flavor,
                    texture + other.texture,
                    calories + other.calories);
        }
    }

    @Override
    public void solvePartTwo(List<String> input) {
        Pattern p = Pattern.compile("(.+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)");
        final List<Ingredient> list = buildIngredients(input, p);

        long max = findBest(list, 500);
        System.out.println("max: " + max);
    }

}
