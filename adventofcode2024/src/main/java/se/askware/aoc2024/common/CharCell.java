package se.askware.aoc2024.common;

import java.util.Objects;

public class CharCell extends Cell {

	public static CharCell EMPTY = new CharCell(-1, -1, ' ');
	public char value;

	public CharCell(char value) {
		this.value = value;
	}

	public CharCell (int row, int col, char value){
		this.value = value;
		this.row = row;
		this.col = col;
	}

	@Override
	public String print() {
		return String.valueOf(value);
	}

	public boolean matches(char ... c){
		for (char c1 : c) {
			if (c1 == value){
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "CharCell{" +
				"value=" + value +
				", row=" + row +
				", col=" + col +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof CharCell)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		final CharCell charCell = (CharCell) o;
		return value == charCell.value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), value);
	}

	public int intValue(){
		return Character.digit(value, 10);
	}

	public char getValue() {
		return value;
	}
}