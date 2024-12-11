package se.askware.aoc2022.dec01;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import se.askware.aoc2022.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		System.out.println(getSum(input, 1));//71502
	}

	@Override
	public void solvePartTwo(List<String> input) {
		System.out.println(getSum(input, 3));//208191
	}

	private List<Deer> getDeers(List<String> input) {
		List<Deer> deers = new ArrayList<>();
		Deer current = new Deer();
		deers.add(current);
		for (String s : input) {
			if (s.isBlank()){
				 current = new Deer();
				deers.add(current);
			} else {
				current.addLoad(Long.parseLong(s));
			}
		}
		return deers;
	}

	private long getSum(List<String> input, int numDeers) {
		List<Deer> deers = getDeers(input);
		final long sum = deers.stream()
				.sorted(Comparator.comparing(Deer::getTotalLoad).reversed())
				.limit(numDeers)
				.mapToLong(Deer::getTotalLoad)
				.sum();
		return sum;
	}

	private static class Deer {
		List<Long> loads = new ArrayList<>();
		public long getTotalLoad(){
			return loads.stream().mapToLong(Long::longValue).sum();
		}

		public void addLoad(Long load){
			loads.add(load);
		}
	}
}
