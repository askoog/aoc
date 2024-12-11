package se.askware.aoc2022.dec18;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import se.askware.aoc2022.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	private static class Pos3 {
		int x, y, z;

		public Pos3(int[] p) {
			this.x = p[0];
			this.y = p[1];
			this.z = p[2];
		}

		@Override
		public String toString() {
			return x + "," + y + "," + z;
		}
	}

	@Override
	public void solvePartOne(List<String> input) {
		Set<String> cache = new HashSet<>(input);
		final List<Pos3> pos = input.stream()
				.map(s -> Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray())
				.map(Pos3::new).collect(Collectors.toList());

		int xmax = pos.stream().mapToInt(p -> p.x).max().getAsInt();
		int ymax = pos.stream().mapToInt(p -> p.y).max().getAsInt();
		int zmax = pos.stream().mapToInt(p -> p.z).max().getAsInt();

		System.out.println(xmax + "," + ymax + "," + zmax);
		int offset = 1;
		boolean[][][] cube = new boolean[xmax + 3][ymax + 3][zmax + 3];

		pos.stream().forEach(p -> cube[p.x + offset][p.y + offset][p.z + offset] = true);

		int free = 0;
		for (Pos3 p : pos) {
			free += cube[p.x + 1 + offset][p.y + offset][p.z + offset] ? 0 : 1;
			free += cube[p.x - 1 + offset][p.y + offset][p.z + offset] ? 0 : 1;
			free += cube[p.x + offset][p.y + 1 + offset][p.z + offset] ? 0 : 1;
			free += cube[p.x + offset][p.y - 1 + offset][p.z + offset] ? 0 : 1;
			free += cube[p.x + offset][p.y + offset][p.z + 1 + offset] ? 0 : 1;
			free += cube[p.x + offset][p.y + offset][p.z - 1 + offset] ? 0 : 1;
		}
		System.out.println(free);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		Set<String> cache = new HashSet<>(input);
		final List<Pos3> pos = input.stream()
				.map(s -> Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray())
				.map(Pos3::new).collect(Collectors.toList());

		int xmax = pos.stream().mapToInt(p -> p.x).max().getAsInt() + 3;
		int ymax = pos.stream().mapToInt(p -> p.y).max().getAsInt() + 3;
		int zmax = pos.stream().mapToInt(p -> p.z).max().getAsInt() + 3;

		System.out.println(xmax + "," + ymax + "," + zmax);
		int offset = 1;
		int[][][] cube = new int[xmax][ymax][zmax];

		for (int x = 0; x < xmax; x++) {
			for (int y = 0; y < ymax; y++) {
				for (int z = 0; z < zmax; z++) {
					cube[x][y][z] = -1;
				}
			}
		}
		pos.stream().forEach(p -> cube[p.x + offset][p.y + offset][p.z + offset] = 1);

		for (int x = 0; x < xmax; x++) {
			for (int y = 0; y < ymax; y++) {
				for (int z = 0; z < zmax; z++) {
					if (x == 0 || y == 0 || z == 0 ||
							x == xmax - 1 || y == ymax - 1 || z == zmax - 1) {
						cube[x][y][z] = 0; // outside air;
					}
				}
			}
		}

		boolean changed = true;
		while(changed) {
			changed = false;
			for (int x = 0; x < xmax; x++) {
				for (int y = 0; y < ymax; y++) {
					for (int z = 0; z < zmax; z++) {
						if (cube[x][y][z] == -1) {
							if (cube[x - 1][y][z] == 0 ||
									cube[x + 1][y][z] == 0 ||
									cube[x][y - 1][z] == 0 ||
									cube[x][y + 1][z] == 0 ||
									cube[x][y][z - 1] == 0 ||
									cube[x][y][z + 1] == 0) {
								cube[x][y][z] = 0; // connected to outside air;
								changed = true;
							}
						}
					}
				}
			}
		}

		int trapped = 0;
		for (int x = 0; x < xmax; x++) {
			for (int y = 0; y < ymax; y++) {
				for (int z = 0; z < zmax; z++) {
					if (cube[x][y][z] == -1) {
						trapped++;
					}
				}
			}
		}

		System.out.println(trapped);


		int free = 0;
		for (Pos3 p : pos) {
			free += cube[p.x + 1 + offset][p.y + offset][p.z + offset] == 0 ? 1 : 0;
			free += cube[p.x - 1 + offset][p.y + offset][p.z + offset] == 0 ? 1 : 0;
			free += cube[p.x + offset][p.y + 1 + offset][p.z + offset] == 0 ? 1 : 0;
			free += cube[p.x + offset][p.y - 1 + offset][p.z + offset] == 0 ? 1 : 0;
			free += cube[p.x + offset][p.y + offset][p.z + 1 + offset] == 0 ? 1 : 0;
			free += cube[p.x + offset][p.y + offset][p.z - 1 + offset] == 0 ? 1 : 0;
		}
		System.out.println(free);
	}

}
