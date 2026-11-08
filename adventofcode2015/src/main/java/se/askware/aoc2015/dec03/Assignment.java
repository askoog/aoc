package se.askware.aoc2015.dec03;

import java.io.IOException;
import java.util.List;

import se.askware.aoc2015.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		for (String string : input) {
			short[][] map = new short[1000][1000];
			int x = 500;
			int y = 500;
			map[x][y]++;
			for (char c : string.toCharArray()) {
				if (c == '<') {
					x--;
				} else if (c == '>') {
					x++;
				} else if (c == '^') {
					y++;
				} else {
					y--;
				}
				map[x][y]++;
			}
			int sum = 0;
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					//System.out.print(map[i][j] > 9 ? "*" : map[i][j]);
					if (map[i][j] > 0) {
						sum++;
					}
				}
				//System.out.println();
			}
			System.out.println(sum);
		}
	}

	@Override
	public void solvePartTwo(List<String> input) {
		for (String string : input) {
			short[][] map = new short[1000][1000];
			int x = 500;
			int y = 500;
			int x1 = 500;
			int y1 = 500;

			map[x][y]++;
			map[x1][y1]++;
			boolean robo = false;
			for (char c : string.toCharArray()) {
				if (robo) {
					if (c == '<') {
						x1--;
					} else if (c == '>') {
						x1++;
					} else if (c == '^') {
						y1++;
					} else {
						y1--;
					}
					map[x1][y1]++;
				} else {
					if (c == '<') {
						x--;
					} else if (c == '>') {
						x++;
					} else if (c == '^') {
						y++;
					} else {
						y--;
					}
					map[x][y]++;
				}
				robo = !robo;
			}
			int sum = 0;
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < map[i].length; j++) {
					//System.out.print(map[i][j] > 9 ? "*" : map[i][j]);
					if (map[i][j] > 0) {
						sum++;
					}
				}
				//System.out.println();
			}
			System.out.println(sum);
		}
	}

}
