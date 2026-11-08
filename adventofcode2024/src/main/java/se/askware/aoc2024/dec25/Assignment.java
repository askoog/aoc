package se.askware.aoc2024.dec25;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.askware.aoc2024.common.AocBase;
import se.askware.aoc2024.common.CharCell;
import se.askware.aoc2024.common.Grid;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		List<String> current = new ArrayList<>();
		List<Key> keys = new ArrayList<>();
		List<Lock> locks = new ArrayList<>();
		input.add("");
		for (String s : input) {
			if (s.trim().isEmpty()) {
				boolean isLock = current.getFirst().charAt(0) == '#';
				Grid<CharCell> charCellGrid = Grid.charGrid(current.subList(1, current.size() - 1));
				List<Integer> pins = new ArrayList<>();
				for (int i = 0; i < charCellGrid.getNumColumns(); i++) {
					long count = charCellGrid.getColumn(i).stream().filter(c -> c.matches('#')).count();
					pins.add((int) count);
				}
				if (isLock) {
					locks.add(new Lock(pins));
				} else {
					keys.add(new Key(pins));
				}
				current.clear();
			} else {
				current.add(s);
			}
		}
		System.out.println(keys);
		System.out.println(locks);
		long count = 0;
		for (Lock lock : locks) {
			for (Key key : keys) {
				if (key.fits(lock)){
					count++;
				}
			}
		}
		System.out.println(count);
	}

	record Key(List<Integer> pins){
		boolean fits(Lock lock){
			for (int i = 0; i < pins.size(); i++) {
				if (pins.get(i) + lock.pins.get(i) > pins.size()) {
					return false;
				}
			}
			return true;
		}

	}
	record Lock(List<Integer> pins){
		
	}
	
	@Override
	public void solvePartTwo(List<String> input) {
	}


}
