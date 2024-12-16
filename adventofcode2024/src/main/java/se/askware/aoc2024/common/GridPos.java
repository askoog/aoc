package se.askware.aoc2024.common;

public class GridPos extends Pair<Integer> {

	public GridPos(Integer row, Integer col) {
		super(row, col);
	}

	public int getRow(){
		return getFirst();
	}

	public int getCol(){
		return getSecond();
	}

	public GridPos translate(GridPos other){
		return new GridPos(getRow() + other.getRow(), getCol() + other.getCol());
	}

	public int distance(GridPos other){
		return Math.abs(getRow() - other.getRow()) + Math.abs(getCol() - other.getCol());
	}

	public GridPos gridDistance(GridPos other){
		return new GridPos(getRow() - other.getRow(), getCol() - other.getCol());
	}

	public GridPos neighbor(Direction dir){
		return translate(dir.getPos());
	}

	public GridPos move(Direction dir){
		return translate(dir.getPos());
	}

	public GridPos move(Direction dir, int numSteps){
		final GridPos pos = dir.getPos();
		return translate(new GridPos(pos.getRow() * numSteps, pos.getCol() * numSteps));
	}

	public GridPos move(XYPair dir, int numSteps){
		return translate(new GridPos(dir.getY() * numSteps, dir.getX() * numSteps));
	}

}
