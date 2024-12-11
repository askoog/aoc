package se.askware.aoc2023.common;

import java.util.Objects;

public class Cell {

	public int row;
	public int col;

	public String print() {
		return "?";
	}

	public GridPos pos(){
		return new GridPos(row,col);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final Cell cell = (Cell) o;
		return row == cell.row && col == cell.col;
	}

	@Override
	public int hashCode() {
		return Objects.hash(row, col);
	}
}
