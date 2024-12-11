package se.askware.aoc2022.dec24;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import se.askware.aoc2022.common.AocBase;
import se.askware.aoc2022.common.CharCell;
import se.askware.aoc2022.common.Grid;
import se.askware.aoc2022.common.GridPos;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input.size(), input.get(0).length(), (row, col) -> input.get(row).charAt(col));
		final CharCell start = grid.getCell(0, 1);
		final CharCell end = grid.getCell(grid.getNumRows() - 1, grid.getNumColumns() - 2);

		grid.print();

		List<Set<GridPos>> free = setupFreePositions(grid, start, end);

		Path bestPath = findPath(grid, start, end, free, 0);
		System.out.println(bestPath.path.size() - 1);
		for (int i = 1; i < bestPath.path.size(); i++) {
			if (!free.get(i).contains(bestPath.path.get(i))){
				System.out.println(i + " " + bestPath.path.get(i) + " " + free.get(i));
			}
		}
	}

	private static List<Set<GridPos>> setupFreePositions(Grid<CharCell> grid, CharCell start, CharCell end) {
		List<Wind> winds = grid.getAll().filter(c -> c.matches('>', '<', '^', 'v')).map(c -> new Wind(c.value, c.pos())).collect(Collectors.toList());
		List<Set<GridPos>> free = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			Grid<CharCell> g = Grid.charGrid(grid.getNumRows() - 2, grid.getNumColumns() - 2, (r,c)->'.');
			winds.forEach(w -> g.getCell(w.pos.getRow() -1, w.pos.getCol() -1).value='#');
			//g.print();
			final Set<GridPos> freePos = g.getAll().filter(c -> c.value == '.')
					.map(c -> c.pos().translate(new GridPos(1,1)))
					.collect(Collectors.toSet());
			freePos.add(start.pos());
			freePos.add(end.pos());
			free.add(freePos);
			winds = winds.stream().map(w -> w.next(grid)).collect(Collectors.toList());
			//System.out.println(winds);
			//System.out.println(freePos);
		}
		return free;
	}

	private static Path findPath(Grid<CharCell> grid, CharCell start, CharCell end, List<Set<GridPos>> free, int offset) {
		Queue<Path> queue = new LinkedList<>();
		queue.add(new Path().copy(start.pos()));
		Map<Integer, Set<GridPos>> seenPositions = new HashMap<>();
		while(!queue.isEmpty()){
			Path path = queue.poll();
			//System.out.println(seenPositions);
			final int index = path.path.size() + offset;
			//System.out.println(index);

			final Set<GridPos> freePos = free.get(index);
			final GridPos currentPos = path.last();
			if (currentPos.equals(end.pos())){
				return path;
			}

			final Set<GridPos> currentSeen = seenPositions.computeIfAbsent(index, i -> new HashSet<>());
			grid.getXYNeighbors(currentPos).stream().filter(c -> freePos.contains(c.pos()))
					.forEach(c -> {
						if (c.pos().equals(end.pos())){
							//System.out.println(path);
							queue.clear();
							queue.add(path.copy(c.pos()));
							return;
						}
						if (!currentSeen.contains(c.pos())){
							currentSeen.add(c.pos());
							queue.add(path.copy(c.pos()));
						} else {
							//System.out.println("seen " + c.pos() + " in " + currentSeen);
						}
					});
			if (freePos.contains(currentPos) && !currentSeen.contains(currentPos)) {
				currentSeen.add(currentPos);
				queue.add(path.copy(currentPos));
			}
			//System.out.println("q=" + queue);
		}
		return null;
	}

	private static class Path {
		List<GridPos> path = new ArrayList<>();
		public Path copy(GridPos newPos){
			Path p = new Path();
			p.path = new ArrayList<>(path);
			p.path.add(newPos);
			return p;
		}

		public GridPos last(){
			return path.get(path.size() -1);
		}

		@Override
		public String toString() {
			return "Path{" +
					"path=" + path +
					'}';
		}
	}

	private static Map<Character, GridPos> dirMap = Map.of('<', new GridPos(0, -1),
			'>', new GridPos(0,1),
			'^', new GridPos(-1,0),
			'v', new GridPos(1,0));
	private static class Wind {
		char dir;
		GridPos pos;

		public Wind(char dir, GridPos pos) {
			this.dir = dir;
			this.pos = pos;
		}

		public Wind next(Grid<CharCell> grid){
			final GridPos pos2 = pos.translate(dirMap.get(dir));
			if (pos2.getCol() == 0){
				return new Wind(dir, new GridPos(pos2.getRow(), grid.getNumColumns() -2));
			}
			if (pos2.getCol() == grid.getNumColumns() - 1){
				return new Wind(dir, new GridPos(pos2.getRow(), 1));
			}
			if (pos2.getRow() == 0){
				return new Wind(dir, new GridPos(grid.getNumRows() - 2, pos2.getCol()));
			}
			if (pos2.getRow() == grid.getNumRows() - 1){
				return new Wind(dir, new GridPos(1, pos2.getCol()));
			}
			return new Wind(dir, pos2);
		}

		@Override
		public String toString() {
			return "Wind{" +
					"dir=" + dir +
					", pos=" + pos +
					'}';
		}
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final Grid<CharCell> grid = Grid.charGrid(input);
		final CharCell start = grid.getCell(0, 1);
		final CharCell end = grid.getCell(grid.getNumRows() - 1, grid.getNumColumns() - 2);

		List<Set<GridPos>> free = setupFreePositions(grid, start, end);

		Path bestPath1 = findPath(grid, start, end, free, 0);
		Path bestPath2 = findPath(grid, end, start, free, bestPath1.path.size() - 1);
		Path bestPath3 = findPath(grid, start, end, free, bestPath1.path.size() - 1 + bestPath2.path.size() - 1);

		System.out.println(bestPath1.path.size() - 1 + bestPath2.path.size() - 1 + bestPath3.path.size() - 1);

	}

}
