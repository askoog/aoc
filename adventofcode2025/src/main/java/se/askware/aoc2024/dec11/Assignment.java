package se.askware.aoc2024.dec11;

import se.askware.aoc2024.common.AocBase;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

    @Override
    public long solvePartOne(List<String> input) {


        return solve("you", "out", parseConnections(input));
    }

    private static long solve(String from, String to, Map<String, List<String>> connections) {

        Set<List<String>> seen = new HashSet<>();
        Queue<List<String>> queue = new ArrayDeque<>();
        List<String> initial = new ArrayList<>();
        initial.add(from);
        queue.add(initial);
        long numConnections = 0;
        int i = 0;
        while(!queue.isEmpty()){
            if(i%100 == 0){
                //System.out.println(queue.size());
            }
            List<String> poll = queue.poll();
            if (poll.getLast().equals(to)){
                numConnections++;
            } else {
                List<String> out = connections.get(poll.getLast());
                if (out != null) {
                    for (String s : out) {

                        List<String> next = new ArrayList<>(poll);

                        next.add(s);
                        if (seen.add(next)) {
                            queue.add(next);
                        }
                    }
                }
            }
        }
        return numConnections;
    }

    private static Map<String, List<String>> parseConnections(List<String> input) {
        Map<String,List<String>> connections = new HashMap<>();

        input.forEach(line -> {
            String[] split = line.split(":");
            Arrays.stream(split[1].trim().split(" ")).forEach(o -> 
                    connections.computeIfAbsent(split[0], k -> new ArrayList<>()).add(o)
            );
        });
        return connections;
    }

    @Override
    public long solvePartTwo(List<String> input) {

        Map<String, List<String>> connections = parseConnections(input);

        HashMap<String, Long> cache = new HashMap<>();
        calculate(connections, cache, new ArrayList<>(List.of("dac", "fft")), "svr");
        return cache.get("svrdacfft");
    }

    private static Long calculate( Map<String, List<String>> connections, HashMap<String, Long> cache, List<String> required, String cur) {
        StringBuilder sb = new StringBuilder(cur);
        for (String r : required) {
            sb.append(r);
        }
        String cacheKey = sb.toString();
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }
        if (cur.equals("out")) {
            return required.isEmpty() ? 1L : 0L;
        }
        // required is part of the cache key and it being empty is the end condition
        // so if cur is one of the required ones, remove it for the recursion (and add it back after)
        boolean foundOne = required.contains(cur);
        if (foundOne) {
            required.remove(cur);
        }
        AtomicLong result = new AtomicLong();
        connections.get(cur).forEach(target -> result.addAndGet(calculate(connections, cache, required, target)));
        cache.put(cacheKey, result.get());
        if (foundOne) {
            required.add(cur);
        }
        return result.get();
    }


}
