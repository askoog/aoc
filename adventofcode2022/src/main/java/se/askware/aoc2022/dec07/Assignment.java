package se.askware.aoc2022.dec07;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import se.askware.aoc2022.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		Dir root = parseDirs(input);

		System.out.println("total size: " + root.getTotalSize());
		System.out.println("sum: " + root.dirStream().filter(d -> d.getTotalSize() <= 100000).mapToLong(Dir::getTotalSize).sum());
	}

	private static Dir parseDirs(List<String> input) {
		Dir root = new Dir("/", null);
		Dir current = root;
		for (String s : input) {
			final String[] split = s.split(" ");
			if (split[0].equals("$")) {
				if (split[1].equals("cd")) {
					String dirName = split[2];
					if (dirName.equals("..")) {
						current = current.parent;
					} else if (dirName.equals("/")) {
						current = root;
					} else {
						Dir x = current;
						current = current.dirs.stream().filter(d -> d.name.equals(dirName)).findAny().orElseThrow(
								() -> new RuntimeException(x.name + " has no subdir " + dirName)
						);
					}
				}
			} else {
				if (split[0].equals("dir")) {
					current.dirs.add(new Dir(split[1], current));
				} else {
					long size = Long.parseLong(split[0]);
					current.files.add(new File(split[1], size));
				}
			}
		}
		return root;
	}

	@Override
	public void solvePartTwo(List<String> input) {
		Dir root = parseDirs(input);

		long totalSize = 70000000;
		long freeSpace = totalSize - root.getTotalSize();
		long required = 30000000 - freeSpace;
		System.out.println("free space: " + freeSpace + ", required " + required);


		System.out.println(root.dirStream().filter(d -> d.getTotalSize() >= required)
				.sorted(Comparator.comparing(Dir::getTotalSize))
				.findFirst().get());

	}

	private static class Dir {

		String name;
		Dir parent;
		List<Dir> dirs = new ArrayList<>();
		List<Assignment.File> files = new ArrayList<>();

		public Dir(String name, Dir parent) {
			this.name = name;
			this.parent = parent;
		}

		public long getTotalSize() {
			return files.stream().mapToLong(Assignment.File::getSize).sum() +
					dirs.stream().mapToLong(Dir::getTotalSize).sum();
		}

		public Stream<Dir> dirStream() {
			return Stream.concat(Stream.of(this), dirs.stream().flatMap(Dir::dirStream));
		}

		@Override
		public String toString() {
			return path() + "  " + getTotalSize();
		}

		public String path(){
			if (parent != null){
				return parent.path() + "/" + name;
			} else {
				return name;
			}
		}
	}

	private static class File {
		String name;
		long size;

		public File(String name, long size) {
			this.name = name;
			this.size = size;
		}

		public long getSize() {
			return size;
		}
	}
}
