package se.askware.aoc2022.dec16;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.naming.InitialContext;

import se.askware.aoc2022.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.PART2).run();
	}

	private static class Valve {
		String id;
		Set<Valve> connectedValves = new HashSet<>();
		int flowRate;

		public Valve(String id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return "Valve{" +
					"id='" + id + '\'' +
					", connectedValves=" + connectedValves.stream().map(v -> v.id).collect(Collectors.toList()) +
					", flowRate=" + flowRate +
					'}';
		}
	}

	private static class Path {
		int totalFlowRate;
		Valve valve;
		Valve valve2;
		List<Valve> path1 = new ArrayList<>();
		List<Valve> path2 = new ArrayList<>();
		List<Valve> openValves = new ArrayList<>();
		int timeElapsed = 0;

		public int timeElapsed() {
			return timeElapsed;
			//return valves.size() - 1;
		}

		public Path copy() {
			Path p = new Path();
			p.totalFlowRate = totalFlowRate;
			p.valve = valve;
			p.valve2 = valve2;
			p.openValves = new ArrayList<>(openValves);
			p.timeElapsed = timeElapsed;
			p.path1 = new ArrayList<>(path1);
			p.path2 = new ArrayList<>(path2);
			return p;
		}

		@Override
		public String toString() {
			return "Path{" +
					"totalFlowRate=" + totalFlowRate +
					", valve=" + valve +
					", open=" + openValves.stream().map(v -> v.id).collect(Collectors.joining(",")) +
					"\n, path1=" + path1.stream().map(v -> v.id).collect(Collectors.joining(",")) +
					"\n, path2=" + path2.stream().map(v -> v.id).collect(Collectors.joining(",")) +
					'}';
		}

		public String open(){
			return openValves.stream().map(v -> v.id).collect(Collectors.joining(","));
		}
	}

	@Override
	public void solvePartOne(List<String> input) {
		Map<String, Valve> valves = readValves(input);

		Path intitial = new Path();
		intitial.valve = valves.get("AA");
		intitial.timeElapsed = 1;
		Queue<Path> queue = new LinkedList<>();
		queue.add(intitial);
		int maxTime = 30;

		Map<String, Path> best = new HashMap<>();
		Path bestPath = null;
		while (!queue.isEmpty()) {
			Path p = queue.poll();
			//System.out.println(p.timeElapsed);

			Valve cur = p.valve;
			final String key = cur.id + ":" + p.open() +":" + p.timeElapsed();
			final Path path = best.get(key);
			if (path == null || path.totalFlowRate < p.totalFlowRate) {
				best.put(key, p);

				if (p.timeElapsed() == maxTime) {
					//System.out.println(p);
					if (bestPath == null || bestPath.totalFlowRate < p.totalFlowRate){
						bestPath = p;
					}
				} else {
					if (cur.flowRate != 0 && !p.openValves.contains(cur)) {
						final int totalFlow = cur.flowRate * (maxTime - p.timeElapsed());
						Path p2 = p.copy();
						p2.totalFlowRate += totalFlow;
						p2.valve = cur;
						p2.openValves.add(cur);
						p2.timeElapsed = p.timeElapsed + 1;
						// turn on
						queue.add(p2);
					}
					for (Valve v : cur.connectedValves) {
						Path p2 = p.copy();
						p2.valve = v;
						p2.timeElapsed = p.timeElapsed + 1;
						queue.add(p2);
						// move v
					}
				}
			} else {
				//System.out.println("Found better path: " + path);
			}

		}
			System.out.println("Best flow " + bestPath.totalFlowRate);
		System.out.println(bestPath);

	}

	private Map<String, Valve> readValves(List<String> input) {
		Pattern p = Pattern.compile("Valve ([A-Z]+) has flow rate=(\\d+); tunnels? leads? to valves? (.*)");

		Map<String, Valve> valves = new HashMap<>();
		input.forEach(line -> {
			//System.out.println(line);
			final Matcher matcher = p.matcher(line);
			matcher.find();
			final String id = matcher.group(1);
			final int flowRate = Integer.parseInt(matcher.group(2));
			Valve v = valves.computeIfAbsent(id, x -> new Valve(x));
			v.flowRate = flowRate;
			final String[] group = matcher.group(3).split(",");
			for (String s : group) {
				final String toId = s.trim();
				Valve toV = valves.computeIfAbsent(toId, x -> new Valve(x));
				v.connectedValves.add(toV);
				toV.connectedValves.add(v);
			}
		});
		return valves;
	}

	@Override
	public void solvePartTwo(List<String> input) {
		Map<String, Valve> valves = readValves(input);

		final List<Valve> usefulValves = valves.values().stream().filter(v -> v.flowRate > 0).collect(Collectors.toList());

		Path initial = new Path();
		initial.valve = valves.get("AA");
		initial.valve2 = valves.get("AA");
		initial.timeElapsed = 0;
		Queue<Path> queue = new LinkedList<>();
		queue.add(initial);
		int maxTime = 26;

		Map<String, Path> best = new HashMap<>();
		Path bestPath = null;
		int c = 0;

		final int totalFlow = valves.values().stream().mapToInt(v -> v.flowRate).sum();

		while (!queue.isEmpty()) {
			final Path p = queue.poll();
			//	System.out.println(p);
			Valve curValve1 = p.valve;
			Valve curValve2 = p.valve2;
			final String key = curValve1.id + ":" + curValve2.id + ":" + p.timeElapsed() ;
			final Path currentBest = best.get(key);
			if (currentBest != null && currentBest.totalFlowRate >= p.totalFlowRate) {
				continue;
			}
			final int curScore = p.openValves.stream().mapToInt(v -> v.flowRate).sum();

			/*if (bestPath != null){
				if (bestPath.totalFlowRate > p.totalFlowRate + (totalFlow - curScore) * (maxTime-p.timeElapsed)){
					//System.out.println("skip " + p);
					continue;
				}
			}*/

			best.put(key, p);

			if (p.timeElapsed == maxTime) {
				if (bestPath == null || bestPath.totalFlowRate < p.totalFlowRate){
					bestPath = p;
				}
				continue;
			}


			if (p.openValves.size() == usefulValves.size()) {
				Path p2 = p.copy();
				while(p2.timeElapsed < maxTime){
					p2.timeElapsed++;
					p2.totalFlowRate += curScore;
					queue.add(p2);
					p2 = p2.copy();
				}
				continue;
			}


			if (curValve1.flowRate != 0 && !p.openValves.contains(curValve1)) {
				Path p2 = p.copy();
				p2.timeElapsed = p.timeElapsed + 1;
				p2.valve = curValve1;
				p2.path1.add(curValve1);
				p2.openValves.add(curValve1);
				p2.totalFlowRate += curScore;// + curValve1.flowRate;
				// turn on

				if (curValve2.flowRate != 0 && !p2.openValves.contains(curValve2)) {
					Path p3 = p2.copy();
					p3.valve2 = curValve2;
					p3.path2.add(curValve2);
					p3.openValves.add(curValve2);
					//newPath.totalFlowRate += curValve2.flowRate;
					// turn on
					queue.add(p3);
				}

				for (Valve v : curValve2.connectedValves) {
					Path p3 = p2.copy();
					p3.valve2 = v;
					p3.path2.add(v);

					queue.add(p3);

				}
			}
			for (Valve v : curValve1.connectedValves) {
				Path p2 = p.copy();
				p2.valve = v;
				p2.path1.add(v);

				p2.timeElapsed = p.timeElapsed + 1;
				p2.totalFlowRate += curScore;
				// move v

				if (curValve2.flowRate != 0 && !p2.openValves.contains(curValve2)) {
					Path p3 = p2.copy();
					p3.valve2 = curValve2;
					p3.path2.add(curValve2);
					p3.openValves.add(curValve2);
					//newPath.totalFlowRate += curValve2.flowRate;
					// turn on
					queue.add(p3);
				}

				for (Valve v2 : curValve2.connectedValves) {
					Path p3 = p2.copy();
					p3.valve2 = v2;
					p3.path2.add(v2);

					queue.add(p3);

				}
			}

/*			for (Path newPath : newPaths) {
				if (curValve2.flowRate != 0 && !newPath.openValves.contains(curValve2)) {
					newPath.valve2 = curValve2;
					newPath.path2.add(curValve2);
					newPath.openValves.add(curValve2);
					//newPath.totalFlowRate += curValve2.flowRate;
					// turn on
					queue.add(newPath);
				}
				for (Valve v : curValve2.connectedValves) {
					Path p2 = newPath.copy();
					p2.valve2 = v;
					p2.path2.add(v);

					queue.add(p2);
				}


			}
*/
		}


		System.out.println("Best flow " + bestPath.totalFlowRate);
		System.out.println(bestPath);
	}

}
