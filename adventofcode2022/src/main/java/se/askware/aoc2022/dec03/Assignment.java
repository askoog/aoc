package se.askware.aoc2022.dec03;

import java.io.IOException;
import java.util.List;

import se.askware.aoc2022.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		int sum = 0;
		for (String s : input) {
			String first = s.substring(0, s.length() / 2);
			String second = s.substring(s.length() / 2);
			for (char c : first.toCharArray()){
				if (second.indexOf(c) >= 0){
					if (Character.isLowerCase(c)) {
						sum += c - 'a' + 1;
					}else{
						sum += c - 'A' + 27;
					}
					break;
				}
			}
		}
		System.out.println(sum);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		int sum = 0;
		for (int i = 0; i < input.size(); i+=3) {
			String s1 = input.get(i);
			String s2 = input.get(i+1);
			String s3 = input.get(i+2);

			for (char c : s1.toCharArray()){
				if (s2.indexOf(c) >= 0 && s3.indexOf(c) >= 0){
					if (Character.isLowerCase(c)) {
						sum += c - 'a' + 1;
					}else{
						sum += c - 'A' + 27;
					}
					break;
				}
			}
		}
		System.out.println(sum);
	}

}
