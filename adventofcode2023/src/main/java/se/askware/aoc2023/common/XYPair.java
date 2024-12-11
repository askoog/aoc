package se.askware.aoc2023.common;

public class XYPair extends Pair<Integer>{

	public XYPair(Integer first, Integer second) {
		super(first, second);
	}

	public Integer getX() {
		return super.getFirst();
	}

	public Integer getY() {
		return super.getSecond();
	}

	public XYPair update(Integer first, Integer second){
		return new XYPair(first,second);
	}


	public void setX(Integer x){
		first = x;
	}

	public void setY(Integer y){
		second = y;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

	public XYPair translate(int x, int y){
		return new XYPair(first + x, second + y);
	}
}
