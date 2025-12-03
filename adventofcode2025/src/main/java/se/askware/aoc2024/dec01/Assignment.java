package se.askware.aoc2024.dec01;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import se.askware.aoc2024.common.AocBase;
import se.askware.aoc2024.common.Direction;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

    @Override
    public long solvePartOne(List<String> input) {
        List<Turn> turns = convertInput(input, Turn::create);
        int dial = 50;
        int numZero = 0;
        for (Turn turn : turns) {
            if (turn.dir == Direction.RIGHT){
                dial += turn.amount();
            } else {
                dial -= turn.amount();
            }
            dial = (dial + 100) % 100;
            if (dial == 0) {
                numZero++;
            }
        }
        return numZero;
    }

    @Override
    public long solvePartTwo(List<String> input) {
        List<Turn> turns = convertInput(input, Turn::create);
        int dial = 50;
        int numZero = 0;
        for (Turn turn : turns) {
            
            if (turn.dir == Direction.RIGHT){
                for (int i = 0; i < turn.amount(); i++) {
                    dial++;
                    dial %= 100;
                    if (dial == 0) {
                        numZero++;
                    }
                }
            } else {
                for (int i = 0; i < turn.amount(); i++) {
                    dial--;
                    dial += 100;
                    dial %= 100;
                    if (dial == 0) {
                        numZero++;
                    }
                }
            }
        }
        return numZero;
    }
    
    record Turn(Direction dir, int amount){
        
        public static Turn create(String input){
            Direction dir = switch (input.charAt(0)) {
                case 'R' -> Direction.RIGHT;
                case 'L' -> Direction.LEFT;
                default -> throw new IllegalArgumentException();
            };
            int amount = Integer.parseInt(input.substring(1));
            return new Turn(dir, amount);
        }
        
    }
}
