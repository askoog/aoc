package se.askware.aoc2024.common;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class Grid<T extends Cell> {

	Cell[][] cells;

	public Grid(int rows, int columns) {
		cells = new Cell[rows][columns];
	}

	public void init(BiFunction<Integer, Integer, T> initiator) {
		for (int row = 0; row < cells.length; row++) {
			for (int column = 0; column < cells[row].length; column++) {
				setCell(initiator.apply(row, column), row, column);
			}
		}
	}
	public static Grid<CharCell> charGrid(List<String> input) {
		return charGrid(input.size(),input.get(0).length(), (row,col) -> input.get(row).charAt(col));
	}

	public static Grid<CharCell> charGrid(int rows, int columns, BiFunction<Integer, Integer, Character> initiator) {
		Grid<CharCell> grid = new Grid<>(rows, columns);
		grid.init((row, col) -> new CharCell(initiator.apply(row, col)));
		return grid;
	}

	/**
	 * Initiate a grid from input lines. Assumes all lines in input has the same length. For each character in the input, a cell will be created
	 *
	 * @param input     input data
	 * @param initiator callback for creating the cell at a given pos with the character at row,col
	 * @param <T>
	 * @return an initiated grid
	 */
	public static <T extends Cell> Grid<T> init(List<String> input, BiFunction<Pair<Integer>, Character, T> initiator) {
		Grid<T> grid = new Grid<>(input.size(), input.get(0).length());
		grid.init((row, col) -> initiator.apply(new Pair<>(row, col), input.get(row).charAt(col)));
		return grid;
	}

	public int getNumRows() {
		return cells.length;
	}

	public int getNumColumns() {
		return cells[0].length;
	}

	public List<T> getRow(int row) {
		return (List<T>) Arrays.asList(cells[row]);
	}

	public List<T> getColumn(int column) {
		List<T> result = new ArrayList<>();
		for (int i = 0; i < cells.length; i++) {
			result.add((T) cells[i][column]);
		}
		return result;
	}

	public void setRow(int row, List<T> elements) {
		for (int i = 0; i < elements.size(); i++) {
			setCell(elements.get(i), row, i);
		}
	}

	public void setColumn(int column, List<T> elements) {
		for (int i = 0; i < elements.size(); i++) {
			setCell(elements.get(i), i, column);
		}
	}

	public T getCell(int row, int col) {
		return (T) cells[row][col];
	}

	public T getCell(GridPos pos) {
		return (T) cells[pos.getRow()][pos.getCol()];
	}

	public Optional<T> getOptionalCell(GridPos pos) {
		return getOptionalCell(pos.getRow(), pos.getCol());
	}

	public Optional<T> getOptionalCell(int row, int col) {
		if (row < 0 || row >= cells.length || col >= cells[0].length || col < 0) {
			return Optional.empty();
		}
		return Optional.of((T) cells[row][col]);
	}


	public Optional<T> getOptionalCell(GridPos source, Direction dir){
		return getOptionalCell(source.translate(dir.getPos()));
	}

	public void setCell(Cell cell, int row, int col) {
		cells[row][col] = cell;
		cell.row = row;
		cell.col = col;
	}

	public List<T> getAllNeighbors(Cell cell) {
		return getAllNeighbors(cell.pos());
	}

	public List<T> getAllNeighbors(GridPos cell) {
		List<T> neighbors = new ArrayList<>();
		for (int i = Math.max(cell.getRow() - 1, 0); i < Math.min(cell.getRow() + 2, cells.length); i++) {
			for (int j = Math.max(cell.getCol() - 1, 0); j < Math.min(cell.getCol() + 2, cells[i].length); j++) {
				if (!cells[i][j].pos().equals(cell)) {
					neighbors.add((T) cells[i][j]);
				}
			}

		}
		//System.out.println(cell.x + "," + cell.y + " : " + cell.print() + " " + neighbors.size());
		return neighbors;
	}

	public List<T> getXYNeighbors(Cell cell) {
		return getXYNeighbors(cell.pos());
	}

	public List<T> getXYNeighbors(GridPos cell) {
		return Stream.of(
						getOptionalCell(cell.getRow() - 1, cell.getCol()),
						getOptionalCell(cell.getRow() + 1, cell.getCol()),
						getOptionalCell(cell.getRow(), cell.getCol() - 1),
						getOptionalCell(cell.getRow(), cell.getCol() + 1))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());
	}

	public Stream<T> getAll() {
		return IntStream.range(0, cells.length).boxed()
				.flatMap(x -> IntStream.range(0, cells[x].length).mapToObj(y -> getCell(x, y)));

	}

	public void print(BiPredicate<Integer, Integer> test) {
		print(test, System.out);
	}

	public void print(BiPredicate<Integer, Integer> test, PrintStream out) {
		for (int i = 0; i < cells.length; i++) {
			boolean visible = false;
			for (int j = 0; j < cells[i].length; j++) {
				if (test.test(i, j)) {
					visible = true;
					out.print(getCell(i, j).print());
				}
			}
			if (visible) {
				out.println();
			}
		}
		out.println();

	}

	public void print() {
		print((i, j) -> true);
	}

	public Optional<T> findFirst(Predicate<T> test) {
		return getAll().filter(test).findFirst();
	}

	public List<T> findPathXY(T start, T end, BiPredicate<T, T> allowedToEnterPredicate) {
		boolean[][] seen = new boolean[cells.length][cells[0].length];
		Queue<List<T>> queue = new LinkedList<>();
		queue.add(List.of(start));
		while (!queue.isEmpty()) {
			final List<T> path = queue.poll();
			final T last = path.get(path.size() - 1);
			if (last == end) {
				return path;
			} else {
				getXYNeighbors(last).forEach(c -> {
					if (!seen[c.row][c.col] && allowedToEnterPredicate.test(last, c)) {
						seen[c.row][c.col] = true;
						List<T> copy = new ArrayList<>(path);
						copy.add(c);
						queue.add(copy);

					}
				});
			}
		}
		return List.of();
	}

}
