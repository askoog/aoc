package se.askware.aoc2023.common;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Range {

	public long start;
	public long end;

	public Range(long start, long end) {
		this.start = start;
		this.end = end;
	}

	public static Range valueOf(String s){
		final String[] split = s.split("-");
		return new Range(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
	}

	public long distance(){
		return end - start + 1;
	}

	public boolean isCoveredBy(Range other){
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

	public Range intersection(Range other){
		return new Range(Math.max(this.start, other.start), Math.min(this.end, other.end));
	}

	/**
	 * Returns a list of sub ranges for a
	 * @param other
	 * @return
	 */
	public List<Range> subRanges(Range other){
		List<Range> r = new ArrayList<>();
		if (this.start < other.start){
			r.add(new Range(this.start, other.start - 1));
		}
		r.add(new Range(Math.max(this.start, other.start), Math.min(this.end, other.end)));
		if (this.end > other.end){
			r.add(new Range(other.end + 1, this.end));
		}
		return r;
	}

	public static Range merge(Range r1, Range r2){
		return r1.merge(r2);
	}

	public Range merge(Range other){
		return new Range(Math.min(this.start, other.start), Math.max(this.end,other.end));
	}

	public Range copy(){
		return new Range(start, end);
	}


	@Override
	public String toString() {
		return "Range{" +
				"start=" + start +
				", end=" + end +
				'}';
	}
}
