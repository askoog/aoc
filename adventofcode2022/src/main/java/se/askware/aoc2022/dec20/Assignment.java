package se.askware.aoc2022.dec20;

import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import se.askware.aoc2022.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().withRunMode(RunMode.values()).run();
	}

	private static class Item {
		BigInteger value;
		int index;

		public Item(BigInteger value, int index) {
			this.value = value;
			this.index = index;
		}

		@Override
		public String toString() {
			return "Item{" +
					"value=" + value +
					", index=" + index +
					'}';
		}
	}

	@Override
	public void solvePartOne(List<String> input) {
		final List<Item> itemsInOrder = readItems(input);
		List<Item> linkedList = new LinkedList<>(itemsInOrder);

		mixList(itemsInOrder, linkedList);
		printResult(linkedList);
	}

	private static void printResult(List<Item> linkedList) {
		final Item zeroItem = linkedList.stream().filter(it -> it.value.equals(BigInteger.ZERO)).findAny().get();
		final int zeroIndex = linkedList.indexOf(zeroItem);
		BigInteger result = BigInteger.ZERO;
		result = result.add(linkedList.get((zeroIndex + 1000) % linkedList.size()).value);
		result = result.add(linkedList.get((zeroIndex + 2000) % linkedList.size()).value);
		result = result.add(linkedList.get((zeroIndex + 3000) % linkedList.size()).value);
		System.out.println(result);
	}

	private static List<Item> readItems(List<String> input) {
		AtomicInteger indexCounter = new AtomicInteger();
		final List<Item> itemsInOrder = input.stream().mapToInt(Integer::parseInt).mapToObj(i -> new Item(BigInteger.valueOf(i), indexCounter.incrementAndGet())).collect(Collectors.toList());
		return itemsInOrder;
	}

	private static void mixList(List<Item> itemsInOrder, List<Item> linkedList) {
		for (int i = 0; i < itemsInOrder.size(); i++) {
			int index = linkedList.indexOf(itemsInOrder.get(i));
			final Item removedItem = linkedList.remove(index);
			int newIndex = removedItem.value.add(BigInteger.valueOf(index)).mod(BigInteger.valueOf(linkedList.size())).intValue();
			if (newIndex <= 0) {
				newIndex = newIndex + linkedList.size();
			}
			linkedList.add(newIndex, removedItem);
		}
	}

	@Override
	public void solvePartTwo(List<String> input) {
		final List<Item> itemsInOrder = readItems(input);
		List<Item> linkedList = new LinkedList<>(itemsInOrder);

		itemsInOrder.forEach(item -> item.value = item.value.multiply(BigInteger.valueOf(811589153)));
		for (int i = 0; i < 10; i++) {
			mixList(itemsInOrder, linkedList);
		}
		printResult(linkedList);
	}
}


