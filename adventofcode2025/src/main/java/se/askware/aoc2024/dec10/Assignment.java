package se.askware.aoc2024.dec10;

import se.askware.aoc2024.common.AocBase;

import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assignment extends AocBase {

    public static void main(String[] args) throws IOException {
        new Assignment().withLogLevel(LogLevel.INFO).run();
    }

    @Override
    public long solvePartOne(List<String> input) {
        List<LightDiagram> list = parse(input);


        return list.stream().mapToLong(this::solve).sum();
    }

    private static List<LightDiagram> parse(List<String> input) {
        Pattern p = Pattern.compile("\\[(.*)] (.*) \\{(.*)}");
        List<LightDiagram> list = input.stream().map(s -> {

            Matcher matcher = p.matcher(s);
            matcher.matches();
            String pattern = matcher.group(1);
            AtomicInteger id = new AtomicInteger();
            List<Button> buttons = Arrays.stream(matcher.group(2).split(" "))
                    .map(b -> Arrays.stream(b.substring(1, b.length() - 1).split(","))
                            .map(Integer::parseInt).toList())
                    .map(ints -> new Button(id.getAndIncrement(), ints)).toList();
            return new LightDiagram(pattern, buttons, matcher.group(3));
        }).toList();
        return list;
    }

    private long solve(LightDiagram lightDiagram) {
        String goal = lightDiagram.pattern;
        String initial = "";
        for (int i = 0; i < goal.length(); i++) {
            initial += '.';
        }
        String i = initial;
        Set<String> seen = new HashSet<>();
        seen.add(initial);
        Queue<State> queue = new ArrayDeque<>();
        lightDiagram.buttons.forEach(b -> queue.add(new State(b.press(i), new ArrayList<>(Collections.singletonList(b)))));
        long min = Long.MAX_VALUE;
        while (!queue.isEmpty()) {
            State state = queue.poll();
            // System.out.println(state);

            if (goal.equals(state.pattern)) {
                   /*
                String t = initial;
                    System.out.println(initial + " -> " + goal);
                    for (Button b1 : state.numPresses) {
                        t = b1.numPresses(t);
                        System.out.println(t + " " + b1);
                    }
                    System.out.println(s + " " + b);
                    //System.out.println(state + " " + b + " -> " + s + " " + (state.numPresses.size() + 1));
                    if (min != Long.MAX_VALUE && min >state.numPresses.size() + 1){
                        System.out.println("new min!");
                    }
                    
                    */
                min = Math.min(min, state.press.size());
            } else {
                for (Button b : lightDiagram.buttons) {

                    List<Button> buttons = new ArrayList<>(state.press);
                    buttons.add(b);
                    String s = b.press(state.pattern);

                    if (seen.add(s)) {
                        queue.add(new State(s, buttons));

                    }

                }

            }

        }
        return min;
    }

    record State(String pattern, List<Button> press) {
    }

    record State2(int[] pattern, long numPresses, int [] presses) {
        public boolean matches(int[] goal) {
            return Arrays.equals(pattern, goal);
        }

        public boolean isDone() {
            for (int i = 0; i < pattern.length; i++) {
                if (pattern[i] != 0) {
                    return false;
                }
            }
            return true;
        }

        public boolean isValidState() {
            for (int i = 0; i < pattern.length; i++) {
                if (pattern[i] < 0) {
                    return false;
                }
            }
            return true;
        }

        public boolean anyJoltageOverruled(int[] goal, List<Integer> switches) {
            for (int i = 0; i < switches.size(); i++) {
                int index = switches.get(i);
                if (pattern[index] > goal[index]) {
                    return true;
                }
            }
            return false;
        }

        public boolean isEvenState() {
            for (int i = 0; i < pattern.length; i++) {
                if (pattern[i] % 2 == 1) {
                    return false;
                }
            }
            return true;
        }

        public boolean isCandidate(long min) {
            long requiredPresses = 0;
            for (int i = 0; i < pattern.length; i++) {
                requiredPresses = Math.max(requiredPresses, pattern[i]);
            }
            return requiredPresses + numPresses < min;
        }
    }

    @Override
    public long solvePartTwo(List<String> input) {
        List<LightDiagram> list = parse(input);


        return list.stream().mapToLong(this::solve2).sum();
    }

    private long solve2(LightDiagram lightDiagram) {
        debug(lightDiagram);
        int[] goal = Arrays.stream(lightDiagram.joltage.split(",")).mapToInt(Integer::parseInt).toArray();
        solvedStates.clear();
        allCombinations.clear();
        allCombinations.add(new ArrayList<>());
        for (Button button : lightDiagram.buttons) {
            final List<List<Button>> next = new ArrayList<>();
            allCombinations.forEach(l -> {
                        next.add(l);
                        ArrayList<Button> objects = new ArrayList<>(l);
                        objects.add(button);
                        next.add(objects);
                    }
            );
            allCombinations = next;
        }
        
        long aLong = solve2(lightDiagram, goal, "").sum();
        debug(lightDiagram + " " + aLong);
        return aLong;
    }

    List<List<Button>> allCombinations = new ArrayList<>();
    Map<String, ButtonPress> solvedStates = new HashMap<>();
    
    record ButtonPress (int [] presses){
        ButtonPress doubleVal(){
            int[] array = Arrays.stream(presses).map(i -> i * 2).toArray();
            return new ButtonPress(array);
        }
        
        ButtonPress press(List<Button> press){
            int[] next = new int[presses.length];
            System.arraycopy(presses, 0, next, 0, next.length);
            for (Button button : press) {
                next[button.id]++;
            }
            return new ButtonPress(next);
        }
        
        long sum(){
            return Arrays.stream(presses).mapToLong(Long::valueOf).sum();
        }

        @Override
        public String toString() {
            return "P" + Arrays.toString(presses) + "=" + sum();
        }
    }
    
    
    
    private ButtonPress solve2(LightDiagram lightDiagram, int[] goal, String indent) {
        //System.out.println(solvedStates.size());
        //debug(indent + Arrays.toString(goal));
        if (solvedStates.containsKey(Arrays.toString(goal))) {
            //debug(indent + Arrays.toString(goal)+ "=" + solvedStates.get(Arrays.toString(goal)) + " (cached)");
            return solvedStates.get(Arrays.toString(goal));
        }
        int [] max = new int[lightDiagram.buttons.size()];
        Arrays.fill(max, Integer.MAX_VALUE);
        ButtonPress min = new ButtonPress(max);
        
        for (List<Button> combination : allCombinations) {
            State2 next = new State2(goal, 0, new int[lightDiagram.buttons.size()]);
            for (Button b : combination) {
                int[] buttons = new int[next.presses.length];
                System.arraycopy(next.presses, 0, buttons, 0, buttons.length);
                buttons[b.id]++;
                next = new State2(b.press2(next.pattern), next.numPresses + 1, buttons);
            }
            //System.out.println(Arrays.toString(next.pattern));
            if (next.isDone()) {
                ButtonPress press = new ButtonPress(new int[lightDiagram.buttons.size()]).press(combination);

                if (min.sum() > press.sum()){
                    min = press;
                }
            } else if (next.isValidState() && next.isEvenState()) {
                int[] newGoal = new int[next.pattern.length];
                for (int i = 0; i < next.pattern.length; i++) {
                    newGoal[i] = next.pattern[i] / 2;
                }
                //debug(indent + "R " + Arrays.toString(newGoal) + " " + combination.stream().map(Button::id).toList());
                ButtonPress press = solve2(lightDiagram, newGoal, indent + " ");
                if (press != null && press.sum() < Integer.MAX_VALUE) {
                    ButtonPress press1 = press.doubleVal().press(combination);
                    //debug(indent+"R " + Arrays.toString(newGoal) + "=" + press1);
                    if(min.sum() > press1.sum()){
                        min = press1;
                    }
                }
            }
        }
        solvedStates.put(Arrays.toString(goal), min);
        //debug(indent + Arrays.toString(goal) +"="+ min );
        return min;
    }


    record Button(int id, List<Integer> switches) {
        String press(String pattern) {
            char[] s = pattern.toCharArray();
            switches.forEach(i -> s[i] = s[i] == '#' ? '.' : '#');
            return new String(s);
        }

        int[] press2(int[] pattern) {
            int[] s = new int[pattern.length];
            System.arraycopy(pattern, 0, s, 0, pattern.length);
            switches.forEach(i -> s[i]--);
            return s;
        }

    }

    record LightDiagram(String pattern, List<Button> buttons, String joltage) {

    }
}
