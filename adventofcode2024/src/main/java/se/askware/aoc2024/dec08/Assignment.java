package se.askware.aoc2024.dec08;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.askware.aoc2024.common.AocBase;
import se.askware.aoc2024.common.CharCell;
import se.askware.aoc2024.common.Grid;
import se.askware.aoc2024.common.GridPos;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Set<GridPos> antinodes = new HashSet<>();
		for (int i = 0; i < chars.length(); i++) {
			char c = chars.charAt(i);

			final List<CharCell> list = grid.getAll().filter(cell -> cell.value == c).toList();
			for (CharCell c1 : list) {
				for (CharCell c2 : list) {
					if (c1 != c2){
						GridPos dist = c2.pos().gridDistance(c1.pos());
						grid.getOptionalCell(c2.pos().translate(dist)).ifPresent(cc -> antinodes.add(cc.pos()));
					}
				}
			}

		}
		System.out.println(antinodes.size());
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Set<GridPos> antinodes = new HashSet<>();
		for (int i = 0; i < chars.length(); i++) {
			char c = chars.charAt(i);

			final List<CharCell> list = grid.getAll().filter(cell -> cell.value == c).toList();
			for (CharCell c1 : list) {
				for (CharCell c2 : list) {
					if (c1 != c2){
						GridPos dist = c2.pos().gridDistance(c1.pos());
						GridPos p = c2.pos();
						while(p != null){
							final CharCell p3 = grid.getOptionalCell(p.translate(dist)).orElse(null);
							if (p3 != null){
								antinodes.add(p3.pos());
								p = p3.pos();
							} else {
								p = null;
							}
						}
					}
				}
			}

		}

		long numAntinodes = antinodes.stream().filter(p -> grid.getCell(p).value == '.').count();
		final long existingAntinodes = grid.getAll().filter(c -> c.value != '.').count();

		System.out.println(numAntinodes + existingAntinodes);
	}

}
