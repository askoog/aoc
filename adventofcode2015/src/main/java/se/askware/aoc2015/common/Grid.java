package se.askware.aoc2015.common;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Grid<T extends Cell> {

	Cell[][] cells;

	public Grid(int x, int y) {
		cells = new Cell[x][y];
	}

	public void init(BiFunction<Integer, Integer, T> initiator) {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				setCell(initiator.apply(i, j), i, j);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public T getCell(int x, int y) {
		return (T) cells[x][y];
	}

	public void setCell(Cell cell, int x, int y) {
		cells[x][y] = cell;
		cell.x = x;
		cell.y = y;
	}

	public List<T> getAllNeighbors(Cell cell) {
		List<T> neighbors = new ArrayList<>();
		for (int i = Math.max(cell.x - 1, 0); i < Math.min(cell.x + 2, cells.length); i++) {
			for (int j = Math.max(cell.y - 1, 0); j < Math.min(cell.y + 2, cells[i].length); j++) {
				if (cells[i][j] != cell) {
					neighbors.add((T) cells[i][j]);
				}
			}
			
		}
		//System.out.println(cell.x + "," + cell.y + " : " + cell.print() + " " + neighbors.size());
		return neighbors;
	}

	public Stream<T> getAll(){
		return IntStream.range(0, cells.length).boxed()
				.flatMap(x -> IntStream.range(0, cells[x].length).mapToObj(y -> getCell(x, y)));
		
	}

	public void print() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				System.out.print(getCell(i, j).print());
			}
			System.out.println();
		}
		System.out.println();

	}
}
