package se.askware.aoc2022.common;

public class Range {

	public int start;
	public int end;

	public Range(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public static Range valueOf(String s){
		final String[] split = s.split("-");
		return new Range(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
	}

	public boolean contains(Range other){
		return start >= other.start && end <= other.end;
	}

	public boolean overlaps(Range other){
		return (start >= other.start && start <= other.end) ||
				(end >= other.start && end <= other.end) ||
				(other.start >= start && other.start <= end) ||
				(other.end >= start && other.end <= end);
	}

	public boolean covers(Range other){
		return start <= other.start && end >= other.end;
	}

	public Range merge(Range r1, Range r2){
		return new Range(Math.min(r1.start, r2.start), Math.max(r1.end,r2.end));
	}

	@Override
	public String toString() {
		return "Range{" +
				"start=" + start +
				", end=" + end +
				'}';
	}
}
