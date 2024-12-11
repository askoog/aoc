package se.askware.aoc2022.dec22;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.askware.aoc2022.common.AocBase;
import se.askware.aoc2022.common.CharCell;
import se.askware.aoc2022.common.Grid;
import se.askware.aoc2022.common.GridPos;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.PART1, RunMode.PART2).run();
	}

	private static class Move {
		int moves;
		char direction;

		public Move(int moves, char direction) {
			this.moves = moves;
			this.direction = direction;
		}

		public CharCell move(Grid<CharCell> grid, CharCell curPos) {
			GridPos pos = curPos.pos();
			for (int i = 0; i < moves; i++) {
				GridPos p2 = pos;
				pos = moveOneStep(grid, pos);
			}
			return grid.getCell(pos);
		}

		private GridPos moveOneStep(Grid<CharCell> grid, GridPos pos) {
			GridPos newPos = move(pos);
			newPos = new GridPos((newPos.getRow() + grid.getNumRows()) % grid.getNumRows(), (newPos.getCol() + grid.getNumColumns()) % grid.getNumColumns());
			CharCell cell = grid.getCell(newPos);

			final CharCell warpCell = warp.get(new CharCell(newPos.getRow(), newPos.getCol(), direction));
			if (warpCell != null) {
				final CharCell gridCell = grid.getCell(warpCell.row, warpCell.col);
				//System.out.println (" WARP[" + newPos + ":" + direction + "->" + warpCell + "(" + gridCell.value + ")] ");
				if (gridCell.value == '#') {
					return pos;
				}
				direction = warpCell.value;
				gridCell.value = direction;
				return warpCell.pos();
			}
			while (cell.value == ' ') {
				if (!warp.isEmpty()){
					System.out.println("???" + cell);
					cell.value = 'X';
					grid.print();
					System.exit(0);
				}
				newPos = move(newPos);
				newPos = new GridPos((newPos.getRow() + grid.getNumRows()) % grid.getNumRows(), (newPos.getCol() + grid.getNumColumns()) % grid.getNumColumns());
				cell = grid.getCell(newPos);
			}
			if (cell.value == '#') {
				return pos;
			}
			cell.value = direction;
			return cell.pos();
		}

		private GridPos move(GridPos pos) {
			switch (direction) {
				case 'R':
					return pos.translate(new GridPos(0, 1));
				case 'L':
					return pos.translate(new GridPos(0, -1));
				case 'U':
					return pos.translate(new GridPos(-1, 0));
				case 'D':
					return pos.translate(new GridPos(1, 0));
				default:
					throw new RuntimeException("??" + direction);
			}
		}
	}

	@Override
	public void solvePartOne(List<String> input) {
		final List<String> gridInput = input.subList(0, input.size() - 2);
		final Grid<CharCell> grid = Grid.charGrid(gridInput.size(), gridInput.stream().mapToInt(String::length).max().getAsInt(),
				(row, col) -> input.get(row).length() > col ? input.get(row).charAt(col) : ' ');

		CharCell pos = grid.getCell(0, 0);
		while (pos.value == ' ') {
			pos = grid.getCell(pos.row, pos.col + 1);
		}

		String moves = input.get(input.size() - 1);
		int offset = 0;
		char dir = 'R';
		Map<Character, Character> rightTurns = Map.of('R', 'D',
				'L', 'U',
				'U', 'R',
				'D', 'L');
		Map<Character, Character> leftTurns = Map.of('R', 'U',
				'L', 'D',
				'U', 'L',
				'D', 'R');
		while (offset < moves.length()) {
			int len = 0;
			while (offset + len < moves.length() && Character.isDigit(moves.charAt(offset + len))) {
				len++;
			}
			final int numMoves = Integer.parseInt(moves.substring(offset, offset + len));

			//System.out.print(pos + " move " + numMoves + " " + dir + " -> ");

			Move m = new Move(numMoves, dir);
			pos = m.move(grid, pos);

			if (offset + len < moves.length()) {

				char dir2 = moves.charAt(offset + len);
				if (dir2 == 'R') {
					dir = rightTurns.get(m.direction);
				} else {
					dir = leftTurns.get(m.direction);
				}
				//System.out.println(pos + " " + dir2 + "->" + dir);
			}
			offset += len + 1;
			//grid.print();
		}

		//System.out.println(moves.charAt(moves.length() - 1));
		//grid.print();
		String dirs = "RDLU";
		//System.out.println(pos + " " + dir);
		System.out.println((1000 * (pos.row + 1)) + (4 * (pos.col + 1)) + dirs.indexOf(dir));

	}

	private static Map<CharCell, CharCell> warp = new HashMap<>();

	@Override
	public void solvePartTwo(List<String> input) {
		setupWarp(input.size() / 4);
		solvePartOne(input);
	}

	private static void setupWarp(int width) {
		for (int i = 0; i < width; i++) {
			/*
			  12
			  3
			 45
			 6
			 */
			// 1U - 6R
			warp.put(new CharCell(4 * width - 1, width + i, 'U'), new CharCell(3 * width + i, 0, 'R'));
			// 6L - 1D
			warp.put(new CharCell(3 * width + i, 3 * width - 1, 'L'), new CharCell(0, width + i, 'D'));

			// 1L - 4R
			warp.put(new CharCell(i, width - 1, 'L'), new CharCell(3 * width -1 -  i, 0, 'R'));
			// 4L - 1R
			warp.put(new CharCell(3 * width - i - 1, 3 * width - 1, 'L'), new CharCell(i, width, 'R'));

			// 2U - 6U
			warp.put(new CharCell(4 * width - 1, 2 * width + i, 'U'), new CharCell(4 * width - 1, i, 'U'));
			// 6D - 2D
			warp.put(new CharCell(0, i, 'D'), new CharCell(0, 2 * width + i, 'D'));

			// 2R - 5L
			warp.put(new CharCell(i, 0, 'R'), new CharCell(3 * width -1 - i, 2 * width - 1, 'L'));
			// 5R - 2L
			warp.put(new CharCell(3 * width -1 - i, 2 * width, 'R'), new CharCell(i, 3 * width - 1, 'L'));

			// 2D - 3R
			warp.put(new CharCell(width, 2 * width + i, 'D'), new CharCell(width + i,2 * width - 1,  'L'));
			// 3R - 2U
			warp.put(new CharCell(width + i, 2 * width, 'R'), new CharCell(width - 1, 2 * width + i, 'U'));

			// 3L - 4D
			warp.put(new CharCell(width + i, width - 1, 'L'), new CharCell(2 * width, i, 'D'));
			// 4U - 3R
			warp.put(new CharCell(2 * width - 1, i, 'U'), new CharCell(width + i, width, 'R'));

			// 5D - 6L
			warp.put(new CharCell(3 * width, width + i, 'D'), new CharCell(3 * width + i, width - 1, 'L'));
			// 6R - 5U
			warp.put(new CharCell(3 * width + i, width, 'R'), new CharCell(3 * width - 1, width + i, 'U'));
		}
	}

}
