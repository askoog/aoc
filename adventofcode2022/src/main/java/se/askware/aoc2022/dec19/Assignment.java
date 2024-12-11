package se.askware.aoc2022.dec19;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import se.askware.aoc2022.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.values()).run();
	}

	private static class Cost {

		private final int cost;
		private Ore type;

		public Cost(int cost, String type) {
			this.type = Ore.valueOf(type);
			this.cost = cost;
		}

	}

	private static class Robot {
		Ore type;
		List<Cost> cost;

		public Robot(String type, List<Cost> cost) {
			this.type = Ore.valueOf(type);
			this.cost = cost;
		}

		@Override
		public String toString() {
			return "Robot{" +
					"type='" + type + '\'' +
					", cost=" + cost +
					'}';
		}

		public Ore getType() {
			return type;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof Robot)) {
				return false;
			}
			final Robot robot = (Robot) o;
			return Objects.equals(type, robot.type) && Objects.equals(cost, robot.cost);
		}

		@Override
		public int hashCode() {
			return Objects.hash(type, cost);
		}
	}

	enum Ore {
		geode, obsidian, clay, ore
	}

	private static class BluePrint {
		List<Robot> robots;

		public BluePrint(List<Robot> robots) {
			this.robots = robots.stream().sorted(Comparator.comparing(Robot::getType)).collect(Collectors.toList());
		}

		@Override
		public String toString() {
			return "BluePrint{" +
					"robots=" + robots +
					'}';
		}
	}

	private static class State {
		int[] ores = new int[Ore.values().length];
		int[] robots = new int[Ore.values().length];

		public State copy() {
			State copy = new State();
			copy.ores = Arrays.copyOf(ores, ores.length);
			copy.robots = Arrays.copyOf(robots, robots.length);
			return copy;
		}

		public void addOre(Ore ore) {
			ores[ore.ordinal()]++;
		}

		public void increaseOres() {
			for (int i = 0; i < robots.length; i++) {
				ores[i] += robots[i];
			}
		}

		public boolean canBuy(Robot robot) {
			for (Cost cost : robot.cost) {
				if (ores[cost.type.ordinal()] < cost.cost) {
					return false;
				}
			}
			return true;
		}

		public void buy(Robot robot) {
			for (Cost cost : robot.cost) {
				ores[cost.type.ordinal()] -= cost.cost;
			}
			robots[robot.type.ordinal()]++;
		}

		@Override
		public String toString() {
			return "State{" +
					"ores=" + ores +
					", robots=" + robots +
					'}';
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof State)) {
				return false;
			}
			final State state = (State) o;
			return Objects.equals(ores, state.ores) && Objects.equals(robots, state.robots);
		}

		@Override
		public int hashCode() {
			return Objects.hash(ores, robots);
		}

		private int robotSum() {
			int sum = 0;
			for (int i = 0; i < robots.length; i++) {
				sum += robots[i];
			}
			return sum;
		}

		public int numRobots() {
			return robotSum();
		}
	}

	private static class Path {
		State curState, lastState;
		int numStates;

		public Path(State curState, State lastState) {
			this.curState = curState;
			this.lastState = lastState;
		}

		public State curState() {
			return curState;
		}

		public State lastState() {
			return lastState;
		}

		public Path copyWithState(State newState) {

			final Path path = new Path(newState, curState);
			path.numStates = numStates + 1;
			return path;
		}

		@Override
		public String toString() {
			return "Path{" +
					"states=" + curState +
					'}';
		}

		public int totalValue() {
			return curState().ores[Ore.geode.ordinal()];
		}
	}

	private static class Simulation {
		BluePrint bluePrint;

		public Simulation(BluePrint b) {

			this.bluePrint = b;
		}

		public int simulate() {
			return simulate(24);
		}

		public int simulate(int rounds){

			Queue<Path> queue = new LinkedList<>();
			final State initial = new State();
			initial.robots[Ore.ore.ordinal()]++;
			initial.increaseOres();
			Path bestPath = new Path(initial, null);
			bestPath.numStates = 1;
			queue.add(bestPath);
			int[] best = new int[rounds + 1];
			Robot geodeRobot = bluePrint.robots.get(0);
			List<Robot> otherRobots = bluePrint.robots.stream().skip(1).collect(Collectors.toList());
			while (!queue.isEmpty()) {
				Path p = queue.poll();
				if (p.totalValue() < best[p.numStates]) {
					continue;
				}
				best[p.numStates] = p.totalValue();
				//System.out.println(p.totalValue());
				if (p.numStates == rounds) {
					bestPath = p;
					continue;
				}

				final State state = p.curState();
				// Always buy geode robot if we can
				if (state.canBuy(geodeRobot)) {
					State s1 = state.copy();
					s1.increaseOres();
					s1.buy(geodeRobot);
					queue.add(p.copyWithState(s1));
					continue;
				}


				for (Robot robot : otherRobots) {

					if (state.canBuy(robot)) {
						final State lastState = p.lastState();
						if (lastState != null
								&& lastState.numRobots() == state.numRobots()
								&& lastState.canBuy(robot)) {
							// skip buying the same robot we skipped in last round, it's always better to buy early
							continue;
						}
						//System.out.println(newState);
						//System.out.println("buy " + robot);
						State s1 = state.copy();
						s1.increaseOres();
						s1.buy(robot);
						queue.add(p.copyWithState(s1));
						//System.out.println(s1);
					}
				}
				State newState = state.copy();
				newState.increaseOres();
				queue.add(p.copyWithState(newState));

			}
			System.out.println(bestPath.totalValue());
			return bestPath.totalValue();
		}
	}

	@Override
	public void solvePartOne(List<String> input) {

		List<BluePrint> bluePrints = getBluePrints(input);

		int sum = 0;
		int index = 0;
		for (BluePrint bluePrint : bluePrints) {
			sum += (++index * new Simulation(bluePrint).simulate(24));
		}
		System.out.println(sum);
	}

	private static List<BluePrint> getBluePrints(List<String> input) {
		List<BluePrint> bluePrints = new ArrayList<>();
		Pattern p = Pattern.compile("Each (.*) robot costs (.*)?");
		for (String blueprintStr : input) {
			final String[] split = blueprintStr.split("\\.");
			List<Robot> robots = new ArrayList<>();
			for (String s : split) {
				final Matcher matcher = p.matcher(s);
				matcher.find();
				int i = 1;
				final String type = matcher.group(i);
				final String[] costTokens = matcher.group(i + 1).split(" and ");
				List<Cost> cost = new ArrayList<>();
				for (String costToken : costTokens) {
					final String[] s1 = costToken.split(" ");
					final int i1 = Integer.parseInt(s1[0]);
					String oreType = s1[1];
					//System.out.println(i1 + ":" + oreType);
					cost.add(new Cost(i1, oreType));
				}
				Robot r = new Robot(type, cost);
				robots.add(r);
			}
			BluePrint b = new BluePrint(robots);
			bluePrints.add(b);
			//System.out.println(b);
		}
		return bluePrints;
	}

	@Override
	public void solvePartTwo(List<String> input) {
		List<BluePrint> bluePrints = getBluePrints(input).stream().limit(3).collect(Collectors.toList());

		int sum = 1;
		int index = 0;
		for (BluePrint bluePrint : bluePrints) {
			sum *= new Simulation(bluePrint).simulate(32);
		}
		System.out.println(sum);
	}

}
