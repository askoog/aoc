package se.askware.aoc2023.dec08;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import se.askware.aoc2023.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.values()).run();
	}

	@Override
	public void solvePartOne(List<String> input) {

		NodeMap map = getNodeMap(input);
		Node n = map.nodeMap().get("AAA");
		int turn = 0;
		while (!n.id.equals("ZZZ")) {
			debug(n);
			n = move(n, 1, turn, map);
			turn++;
		}
		System.out.println(turn);
	}

	private static NodeMap getNodeMap(List<String> input) {

		Map<String, Node> nodeMap = new HashMap<>();
		final String instruction = input.get(0);
		for (String s : input) {
			if (s.contains("=")) {
				// AAA = (BBB, CCC)
				String nodeId = s.substring(0, 3);
				String left = s.substring(7, 10);
				String right = s.substring(12, 15);
				Node n = new Node(nodeId, left, right);
				nodeMap.put(nodeId, n);
			}
		}
		debug(nodeMap);
		return new NodeMap(nodeMap, instruction);
	}

	private record NodeMap(Map<String, Assignment.Node> nodeMap, String instruction) {}
	private record Node (String id, String left, String right){}


	private Node move(Node n, long numMoves, long turn, NodeMap map) {

		for (long i = 0; i < numMoves; i++) {
			char move = map.instruction().charAt((int) ((turn + i) % map.instruction().length()));
			String next = move == 'L' ? n.left : n.right;
			n = map.nodeMap().get(next);
		}
		return n;
	}

	@Override
	public void solvePartTwo(List<String> input) {

		NodeMap map = getNodeMap(input);
		List<Node> nodes = map.nodeMap().values().stream().filter(node -> node.id.endsWith("A")).toList();
		List<Long> paths = nodes.stream().map(n -> {
			long turn = 0;
			while (!n.id.endsWith("Z")) {
				n = move(n, 1, turn, map);
				turn++;
			}
			return turn;
		}).toList();

		long result = lcm(paths);
		System.out.println(result);

	}

}
