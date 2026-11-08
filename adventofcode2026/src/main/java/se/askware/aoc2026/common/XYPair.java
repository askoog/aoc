package se.askware.aoc2026.common;

public class XYPair extends Pair<Integer>{

	public XYPair(Integer first, Integer second) {
		super(first, second);
	}

    public static XYPair parse(String s, char delimiter) {
        return new XYPair(Integer.parseInt(s.substring(0, s.indexOf(delimiter))), 
                Integer.parseInt(s.substring(s.indexOf(delimiter) + 1)));
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
