package se.askware.aoc2024.dec08;

import se.askware.aoc2024.common.AocBase;

import java.io.IOException;
import java.util.*;

public class Assignment extends AocBase {

    public static void main(String[] args) throws IOException {
        new Assignment().run();
    }

    @Override
    public long solveExampleOne(List<String> input) {
        return solve(input, 10);
    }

    @Override
    public long solvePartOne(List<String> input) {
        return solve(input, 1000);
    }

    @Override
    public long solvePartTwo(List<String> input) {
        return solve(input, Integer.MAX_VALUE);
    }

    public long solve(List<String> input, int num) {
        List<Point3D> points = input.stream()
                .map(s -> Arrays.stream(s.split(","))
                        .mapToInt(Integer::parseInt).toArray())
                .map(a -> new Point3D(a[0], a[1], a[2])).toList();

        List<Distance> list = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point3D p1 = points.get(i);
            for (int j = i + 1; j < points.size(); j++) {
                Distance distance = p1.distance(points.get(j));
                list.add(distance);
            }
        }

        Collections.sort(list);
        Set<Circuit> circuits = new HashSet<>();
        for (int i = 0; i < num; i++) {
            Distance distance = list.get(i);
            Optional<Circuit> first = circuits.stream().filter(c -> c.contains(distance.p1)).findFirst();
            Optional<Circuit> second = circuits.stream().filter(c -> c.contains(distance.p2)).findFirst();
            if (first.isPresent() && second.isPresent()) {
                Circuit firstCircuit = first.get();
                Circuit secondCircuit = second.get();
                if (firstCircuit != secondCircuit) {
                    circuits.remove(secondCircuit);
                    secondCircuit.distances.forEach(firstCircuit::add);
                }
            } else if (first.isPresent()) {
                first.get().add(distance);
            } else if (second.isPresent()) {
                second.get().add(distance);
            } else {
                circuits.add(new Circuit(distance));
            }
            // Part 2
            if (circuits.size() == 1 && circuits.iterator().next().size() == points.size()) {
                return ((long) distance.p1.x) * distance.p2.x;
            }

        }
        List<Circuit> list1 = circuits.stream().sorted().limit(3).toList();

        return list1.stream().
                mapToLong(Circuit::size).
                reduce(1, (a, b) -> a * b);
    }

    record Point3D(int x, int y, int z) {
        public Distance distance(Point3D other) {
            return new Distance(
                    Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2) + Math.pow(z - other.z, 2)),
                    this, other);
        }
    }

    record Distance(double distance, Point3D p1, Point3D p2) implements Comparable<Distance> {
        @Override
        public int compareTo(Distance o) {
            return (int) (this.distance - o.distance) * 1000;
        }
    }

    private static class Circuit implements Comparable<Circuit> {
        Set<Point3D> points = new HashSet<>();
        List<Distance> distances = new ArrayList<>();

        public Circuit(Distance distance) {
            distances.add(distance);
            points.add(distance.p1);
            points.add(distance.p2);
        }

        @Override
        public String toString() {
            return "Circuit{" +
                    "points=" + points +
                    ", distances=" + distances +
                    '}';
        }

        public int size() {
            return points.size();
        }

        @Override
        public int compareTo(Circuit o) {
            return Integer.compare(o.size(), size());
        }

        public boolean contains(Point3D point) {
            return points.contains(point);
        }

        public void add(Distance distance) {
            distances.add(distance);
            points.add(distance.p1);
            points.add(distance.p2);
        }
    }
}

