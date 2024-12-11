package se.askware.aoc2022.dec05;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.askware.aoc2022.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		Stack<String> boxes = new Stack<>();
		List<String> moves = new ArrayList<>();
		int numBoxes = 1;
		for (String s : input) {
			if (s.matches("[(\\s)+(\\d)]+")){
				while (s.contains("" + numBoxes)){
					numBoxes++;
				}
				numBoxes--;
				System.out.println(numBoxes);
			} else if (s.contains("[")){
				boxes.push(s);
			} else if (!s.isEmpty()){
				moves.add(s);
			}
		}

		List<Stack<String>> crane = new ArrayList<>();
		for (int i = 0; i <numBoxes; i++) {
			crane.add(new Stack<>());
		}

		while(!boxes.isEmpty()){
			String pos = boxes.pop();
			pos = pos.substring(1);
			for (int i=0; i<pos.length(); i+=4){
				if (pos.charAt(i) != ' '){
					crane.get(i / 4).push(String.valueOf(pos.charAt(i)));
				}
				System.out.println(pos.charAt(i));
			}
		}
		System.out.println(crane);

		Pattern p = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");
		for (String move : moves) {
			System.out.println(move);
			final Matcher matcher = p.matcher(move);
			matcher.find();
			int count = Integer.parseInt(matcher.group(1));
			int from = Integer.parseInt(matcher.group(2));
			int to = Integer.parseInt(matcher.group(3));
			for (int i = 0; i < count; i++) {
				crane.get(to - 1).push(crane.get(from - 1).pop());
			}
			System.out.println(crane);

		}
		System.out.println(crane);
		StringBuilder sb = new StringBuilder();
		for (Stack<String> c : crane) {
			sb.append(c.peek());
		}
		System.out.println(sb);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		Stack<String> boxes = new Stack<>();
		List<String> moves = new ArrayList<>();
		int numBoxes = 1;
		for (String s : input) {
			if (s.matches("[(\\s)+(\\d)]+")){
				while (s.contains("" + numBoxes)){
					numBoxes++;
				}
				numBoxes--;
				System.out.println(numBoxes);
			} else if (s.contains("[")){
				boxes.push(s);
			} else if (!s.isEmpty()){
				moves.add(s);
			}
		}

		List<Stack<String>> crane = new ArrayList<>();
		for (int i = 0; i <numBoxes; i++) {
			crane.add(new Stack<>());
		}

		while(!boxes.isEmpty()){
			String pos = boxes.pop();
			pos = pos.substring(1);
			for (int i=0; i<pos.length(); i+=4){
				if (pos.charAt(i) != ' '){
					crane.get(i / 4).push(String.valueOf(pos.charAt(i)));
				}
				System.out.println(pos.charAt(i));
			}
		}
		System.out.println(crane);

		Pattern p = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");
		for (String move : moves) {
			System.out.println(move);
			final Matcher matcher = p.matcher(move);
			matcher.find();
			int count = Integer.parseInt(matcher.group(1));
			int from = Integer.parseInt(matcher.group(2));
			int to = Integer.parseInt(matcher.group(3));
			Stack<String> tmp = new Stack<>();
			for (int i = 0; i < count; i++) {
				tmp.push(crane.get(from - 1).pop());
			}
			for (int i = 0; i < count; i++) {
				crane.get(to - 1).push(tmp.pop());
			}
			System.out.println(crane);

		}
		System.out.println(crane);
		StringBuilder sb = new StringBuilder();
		for (Stack<String> c : crane) {
			sb.append(c.peek());
		}
		System.out.println(sb);
	}

}
