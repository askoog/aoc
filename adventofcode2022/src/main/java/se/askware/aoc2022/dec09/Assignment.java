package se.askware.aoc2022.dec09;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.askware.aoc2022.common.AocBase;
import se.askware.aoc2022.common.Pair;
import se.askware.aoc2022.common.XYPair;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {

		XYPair headPos = new XYPair(10000,10000);
		XYPair tailPos = new XYPair(10000,10000);

		Set<XYPair> unique = new HashSet<>();

		for (String row : input) {
			char dir = row.charAt(0);
			int length = Integer.parseInt(row.substring(2));

			System.out.println(dir + ":" + length);
			for (int i = 0; i < length; i++) {

				if (dir == 'R'){
					headPos = headPos.update(headPos.getX() + 1, headPos.getY());
				}
				if (dir == 'U'){
					headPos = headPos.update(headPos.getX(), headPos.getY() + 1);
				}
				if (dir == 'L'){
					headPos = headPos.update(headPos.getX() - 1, headPos.getY());
				}
				if (dir == 'D'){
					headPos = headPos.update(headPos.getX(), headPos.getY() - 1);
				}

				tailPos = follow(headPos, tailPos);
				System.out.println(headPos + "  " + tailPos);
				unique.add(tailPos);
			}

		}
		System.out.println(unique.size());
	}

	private static XYPair follow(XYPair headPos, XYPair tailPos) {
		if (headPos.getX() > tailPos.getX() + 1){
			if (headPos.getY() > tailPos.getY()){
				tailPos = tailPos.update(tailPos.getX() + 1, tailPos.getY() + 1);
			} else if (headPos.getY() < tailPos.getY()){
				tailPos = tailPos.update(tailPos.getX() + 1, tailPos.getY() - 1);
			} else {
				tailPos = tailPos.update(tailPos.getX() + 1, tailPos.getY());
			}
		} else if (headPos.getX() < tailPos.getX() - 1){
			if (headPos.getY() > tailPos.getY()){
				tailPos = tailPos.update(tailPos.getX() - 1, tailPos.getY() + 1);
			} else if (headPos.getY() < tailPos.getY()){
				tailPos = tailPos.update(tailPos.getX() - 1, tailPos.getY() - 1);
			} else {
				tailPos = tailPos.update(tailPos.getX() - 1, tailPos.getY());
			}
		} else if (headPos.getY() > tailPos.getY() + 1){
			if (headPos.getX() > tailPos.getX()){
				tailPos = tailPos.update(tailPos.getX() + 1, tailPos.getY() + 1);
			} else if (headPos.getX() < tailPos.getX()){
				tailPos = tailPos.update(tailPos.getX() - 1, tailPos.getY() + 1);
			} else {
				tailPos = tailPos.update(tailPos.getX(), tailPos.getY() + 1);
			}
		} else if (headPos.getY() < tailPos.getY() - 1){
			if (headPos.getX() > tailPos.getX()){
				tailPos = tailPos.update(tailPos.getX() + 1, tailPos.getY() - 1);
			} else if (headPos.getX() < tailPos.getX()){
				tailPos = tailPos.update(tailPos.getX() - 1, tailPos.getY() - 1);
			} else {
				tailPos = tailPos.update(tailPos.getX(), tailPos.getY() - 1);
			}
		}
		return tailPos;
	}

	@Override
	public void solvePartTwo(List<String> input) {
		XYPair headPos = new XYPair(10000,10000);

		Set<XYPair> unique = new HashSet<>();
		List<XYPair> tails = new ArrayList<>();
		for (int i = 1; i <= 9; i++) {
			tails.add(new XYPair(10000, 10000));
		}

		for (String row : input) {
			char dir = row.charAt(0);
			int length = Integer.parseInt(row.substring(2));

			for (int i = 0; i < length; i++) {

				if (dir == 'R'){
					headPos = headPos.update(headPos.getX() + 1, headPos.getY());
				}
				if (dir == 'U'){
					headPos = headPos.update(headPos.getX(), headPos.getY() + 1);
				}
				if (dir == 'L'){
					headPos = headPos.update(headPos.getX() - 1, headPos.getY());
				}
				if (dir == 'D'){
					headPos = headPos.update(headPos.getX(), headPos.getY() - 1);
				}
				XYPair last = headPos;
				for (XYPair tailPos : tails) {

					XYPair newPos = follow(last, tailPos);
					//System.out.println(headPos + "  " + tailPos + " -> "  + newPos);
					tailPos.setX(newPos.getX());
					tailPos.setY(newPos.getY());

					last = tailPos;
				}

				unique.add(new XYPair(last.getFirst(), last.getSecond()));
				if (unique.size() < 40) {
					System.out.println(unique);
				}
			}

		}
		System.out.println(unique.size());
	}

}
