package se.askware.aoc2024.common;

public enum Direction {
	UP(new GridPos(-1, 0)),
	RIGHT(new GridPos(0, 1)),
	DOWN(new GridPos(1, 0)),
	LEFT(new GridPos(0, -1));
	GridPos pos;

	Direction(GridPos pos) {
		this.pos = pos;
	}

	public GridPos getPos() {
		return pos;
	}

	public char charValue() {
		return switch (this) {
			case UP -> '^';
			case DOWN -> 'v';
			case LEFT -> '<';
			case RIGHT -> '>';
		};
	}

	public static Direction valueOf(char c) {
		return switch (c) {
			case '^' -> UP;
			case 'v' -> DOWN;
			case '<' -> LEFT;
			case '>' -> RIGHT;
			default ->  throw new IllegalArgumentException(String.valueOf(c));
		};
	}

	public Direction opposite() {
		return switch (this) {
			case UP -> DOWN;
			case DOWN -> UP;
			case LEFT -> RIGHT;
			case RIGHT -> LEFT;
			default -> throw new IllegalArgumentException(name());
		};
	}

	public static Direction parse(String s) {
		return switch (s) {
			case "L" -> LEFT;
			case "R" -> RIGHT;
			case "D" -> DOWN;
			case "U" -> UP;
			default -> throw new IllegalArgumentException(s);
		};
	}

	public Direction turn(Direction direction) {
		if (direction == LEFT){
			return switch (this) {
				case UP -> LEFT;
				case LEFT -> DOWN;
				case DOWN -> RIGHT;
				case RIGHT -> UP;
			};
		} else if (direction == RIGHT){
			return switch (this) {
				case UP -> RIGHT;
				case RIGHT -> DOWN;
				case DOWN -> LEFT;
				case LEFT -> UP;
			};
		} else {
			throw new IllegalArgumentException("Invalid direction: " + direction);
		}
	}



}
