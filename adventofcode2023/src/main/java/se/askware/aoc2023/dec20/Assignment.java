package se.askware.aoc2023.dec20;

import java.awt.Button;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.io.output.CloseShieldOutputStream;

import se.askware.aoc2023.common.AocBase;
import sun.misc.Signal;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.PART2).run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		Wiring wiring = parseWiring(input);
		System.out.println(wiring);
		Map<String, Integer> state = new HashMap<>();
		state.put(wiring.toState(), 0);
		long numLow = 0;
		long numHigh = 0;
		boolean loopFound = true;
		for (int i = 1; i <= 1000_000; i++) {
			final Map<Signal, AtomicInteger> signalAtomicIntegerMap = wiring.pressButton();
			final String wiringState = wiring.toState();
			numLow += signalAtomicIntegerMap.get(Signal.LOW).get();
			numHigh += signalAtomicIntegerMap.get(Signal.HIGH).get();
			if (!loopFound && state.containsKey(wiringState)) {
				System.out.println("Loop " + wiringState + " " + state.get(wiringState) + " - " + i);
				int loopLength = i - state.get(wiringState);
				long lowsPerLoop = numLow;
				long highsPerLoop = numHigh;
				System.out.println(lowsPerLoop + " " + highsPerLoop);
				/*while (i + loopLength <= 1000) {
					i += loopLength;
					numLow += lowsPerLoop;
					numHigh += highsPerLoop;
				}*/
				loopFound = true;
			}
			state.put(wiringState, i);
		}
		//System.out.println(wiring.toState());
		//System.out.println(state);
		System.out.println(numLow * numHigh);
	}

	private static Wiring parseWiring(List<String> input) {
		Wiring wiring = new Wiring();
		for (String s : input) {
			if (s.isEmpty()) {
				continue;
			}
			final String identifier = s.substring(0, s.indexOf(' '));
			if (identifier.startsWith("%")) {
				final String id = identifier.substring(1);
				wiring.modules.put(id, new FlipFlop(id));
			} else if (identifier.startsWith("&")) {
				final String id = identifier.substring(1);
				wiring.modules.put(id, new Conjunction(id));
			} else if (identifier.equals("broadcaster")) {
				wiring.modules.put(identifier, new Broadcaster());
			} else {
				throw new IllegalArgumentException(s);
			}
		}
		wiring.modules.put("output", new Output());
		System.out.println(wiring);
		for (String s : input) {
			if (s.isEmpty()) {
				continue;
			}
			final String identifier = s.substring(0, s.indexOf(' ')).replace("%", "").replace("&", "");

			final String[] split = s.substring(s.indexOf(">") + 2).replace(",", "").split(" ");
			for (String out : split) {

				final Module receiver = wiring.modules.computeIfAbsent(out, name -> new Output());
				final Module emitter = Optional.ofNullable(wiring.modules.get(identifier)).orElseThrow(() -> new IllegalArgumentException(identifier));
				emitter.connections.add(receiver);
				if (receiver instanceof Conjunction) {
					((Conjunction) receiver).lastState.put(emitter.identifier, Signal.LOW);
				}
			}
		}
		return wiring;
	}

	@Override
	public void solvePartTwo(List<String> input) {
		Wiring wiring = parseWiring(input);
		//System.out.println(wiring);
		//Map<String, Integer> state = new HashMap<>();
		//state.put(wiring.toState(), 0);
		//long numLow = 0;
		//long numHigh = 0;
		//oolean loopFound = true;

		Map<String, List<Integer>> highPulse = new HashMap<>();
		Map<String, List<Integer>> lowPulse = new HashMap<>();

		String[] highIdentifiers = new String[]{"st", "tn", "hh", "dt"};
		for (String e : highIdentifiers) {
			wiring.lowSignalTrack.put(e, 0L);
		}
		String[] lowIdentifiers = new String[]{"gr", "vc", "db", "lz"};
		Map<String, Long> loops = new HashMap<>();
		for (int i = 1; i < 10_000_000; i++) {
			final Map<Signal, AtomicInteger> signalAtomicIntegerMap = wiring.pressButton();
			if (wiring.modules.get("rx").output == Signal.LOW){
				System.out.println(i);
				return;
			}
			for (String e : highIdentifiers) {
				if (wiring.lowSignalTrack.get(e) > 0 && !loops.containsKey(e)) {
					System.out.println(e + " " + i);
					loops.put(e, (long)i);
				}
			}
			if (wiring.lowSignalTrack.values().stream().allMatch(j -> j > 0)){
				final long lcm = lcm(loops.values().stream().toList());
				System.out.println(lcm);
				return;
			}
/*			for (String identifier : highIdentifiers) {
				if (wiring.modules.get(identifier).output == Signal.HIGH){
					highPulse.computeIfAbsent(identifier, s -> new ArrayList<>()).add(i);
					System.out.println(identifier);
				}
			}
			for (String identifier : lowIdentifiers) {
				if (wiring.modules.get(identifier).output == Signal.LOW){
					lowPulse.computeIfAbsent(identifier, s -> new ArrayList<>()).add(i);
					System.out.println(identifier);
					//System.out.println(lowPulse);
				}
			}*/
			/*System.out.println(wiring.modules.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(
					e -> String.valueOf(e.getValue().output.ordinal())).collect(Collectors.joining()));
			*/String ss  = "";
			//System.out.println(ss);

			if (i%1000_000 == 0){
				System.out.println(i +" ...");
			}
/*			final String wiringState = wiring.toState();
			numLow += signalAtomicIntegerMap.get(Signal.LOW).get();
			numHigh += signalAtomicIntegerMap.get(Signal.HIGH).get();
			if (!loopFound && state.containsKey(wiringState)) {
				System.out.println("Loop " + wiringState + " " + state.get(wiringState) + " - " + i);
				int loopLength = i - state.get(wiringState);
				long lowsPerLoop = numLow;
				long highsPerLoop = numHigh;
				System.out.println(lowsPerLoop + " " + highsPerLoop);
				while (i + loopLength <= 1000) {
					i += loopLength;
					numLow += lowsPerLoop;
					numHigh += highsPerLoop;
				}
				loopFound = true;
			}
			state.put(wiringState, i);*/
		}
		for (Map.Entry<String, List<Integer>> stringListEntry : highPulse.entrySet()) {
			System.out.println(stringListEntry);
		}
		for (Map.Entry<String, List<Integer>> stringListEntry : lowPulse.entrySet()) {
			System.out.println(stringListEntry);
		}
/*
		System.out.println(wiring.toState());
		System.out.println(state);
		System.out.println(numLow * numHigh);
*/
	}

	static class Wiring {
		Map<String, Module> modules = new HashMap<>();

		@Override
		public String toString() {
			return "Wiring{" +
					"modules=\n" + modules.entrySet().stream().map(e -> "  " + e.getKey() + ":" + e.getValue() + "\n").collect(Collectors.joining()) +
					'}';
		}

		Map<String, Long> lowSignalTrack = new HashMap<>();

		public Map<Signal, AtomicInteger> pressButton() {
			return pressButton(0);
		}
		public Map<Signal, AtomicInteger> pressButton(int totalPulses) {

			Map<Signal, AtomicInteger> count = Map.of(Signal.LOW, new AtomicInteger(), Signal.HIGH, new AtomicInteger());
			long pulseCount = totalPulses;
			Queue<Pulse> pulses = new ArrayDeque<>();
			pulses.add(new Pulse(Signal.LOW, new Button(), modules.get("broadcaster")));

			while (!pulses.isEmpty()) {
				final Pulse p = pulses.poll();
				pulseCount++;
				count.get(p.signal).incrementAndGet();
				debug(p);
				final List<Pulse> next = p.receiver.handlePulse(p);
				pulses.addAll(next);
				for (String e : lowSignalTrack.keySet()) {
					if (modules.get(e).output == Signal.HIGH){
						if (lowSignalTrack.get(e) == 0) {
							lowSignalTrack.put(e, pulseCount);
						}
					}
				}
			}
			return count;
		}

		public String toState() {
			int numHigh = getNumOutputs(Signal.HIGH);
			int numLow = getNumOutputs(Signal.LOW);

			return numLow + "|" + numHigh + "|" + modules.entrySet().stream().sorted(Map.Entry.comparingByKey())
					.map(e -> e.getValue().toState()).collect(Collectors.joining(","));
		}

		private int getNumOutputs(Signal high) {
			return (int) modules.entrySet().stream()
					.filter(e -> e.getValue().output == high)
					.count();
		}
	}

	enum Signal {LOW, HIGH}

	;

	record Pulse(Signal signal, Module emitter, Module receiver) {
		@Override
		public String toString() {
			return emitter.identifier + " - " + signal + " -> " + receiver.identifier;
		}
	}

	static abstract class Module {
		String identifier;

		Signal output = Signal.LOW;

		List<Module> connections = new ArrayList<>();

		public Module(String id) {
			this.identifier = id;
		}

		List<Pulse> sendToAll(Signal signal) {
			this.output = signal;
			return connections.stream().map(m -> new Pulse(signal, this, m)).toList();
		}

		abstract List<Pulse> handlePulse(Pulse p);

		public String toState() {
			return String.valueOf(output.ordinal());
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof Module module)) {
				return false;
			}
			return Objects.equals(identifier, module.identifier) && output == module.output && Objects.equals(connections, module.connections);
		}

		@Override
		public int hashCode() {
			return Objects.hash(identifier, output, connections);
		}

		@Override
		public String toString() {
			return getClass().getSimpleName() + "{" +
					"identifier='" + identifier + '\'' +
					", output=" + output +
					", connections=" + connections.stream().map(m -> m.identifier).toList() +
					'}';
		}
	}

	static class FlipFlop extends Module {

		boolean on = false;

		public FlipFlop(String id) {
			super(id);
		}

		@Override
		List<Pulse> handlePulse(Pulse p) {
			if (p.signal == Signal.LOW) {
				on = !on;
				return sendToAll(on ? Signal.HIGH : Signal.LOW);
			} else {
				return List.of();
			}
		}

		public String toState() {
			return String.valueOf(output.ordinal()) + on;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof FlipFlop flipFlop)) {
				return false;
			}
			if (!super.equals(o)) {
				return false;
			}
			return on == flipFlop.on;
		}

		@Override
		public int hashCode() {
			return Objects.hash(super.hashCode(), on);
		}

		@Override
		public String toString() {
			return super.toString() + "[" + on + "]";
		}
	}

	static class Conjunction extends Module {
		Map<String, Signal> lastState = new HashMap<>();

		boolean trackLow = false;

		public Conjunction(String id) {
			super(id);
		}

		@Override
		List<Pulse> handlePulse(Pulse p) {
			lastState.put(p.emitter.identifier, p.signal);
			if (lastState.values().stream().allMatch(s -> s == Signal.HIGH)) {
				return sendToAll(Signal.LOW);
			} else {
				return sendToAll(Signal.HIGH);
			}
		}

		@Override
		public String toString() {
			return super.toString() + "[" + lastState + "]";
		}

		public String toState() {
			return output.ordinal() + lastState.entrySet().stream().map(e -> e.getKey() + e.getValue().ordinal()).collect(Collectors.joining());
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof Conjunction that)) {
				return false;
			}
			if (!super.equals(o)) {
				return false;
			}
			return Objects.equals(lastState, that.lastState);
		}

		@Override
		public int hashCode() {
			return Objects.hash(super.hashCode(), lastState);
		}
	}

	static class Broadcaster extends Module {
		Broadcaster() {
			super("broadcaster");
		}

		@Override
		List<Pulse> handlePulse(Pulse p) {
			return sendToAll(p.signal);
		}

	}

	static class Button extends Module {
		Button() {
			super("button");
		}

		@Override
		List<Pulse> handlePulse(Pulse p) {
			return List.of();
		}
	}

	static class Output extends Module {
		Output() {
			super("output");
		}

		@Override
		List<Pulse> handlePulse(Pulse p) {
			if (identifier.equals("rx") && p.signal == Signal.LOW){
				System.out.println("DONE");
			}
			sendToAll(p.signal);
			return List.of();
		}
	}
}
