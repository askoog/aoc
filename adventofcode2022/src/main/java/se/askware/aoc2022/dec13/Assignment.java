package se.askware.aoc2022.dec13;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.askware.aoc2022.common.AocBase;

public class Assignment extends AocBase {

	public static void main(String[] args) throws IOException {
		new Assignment().run();
	}

	@Override
	public void solvePartOne(List<String> input) {
		int pair = 1;
		int sum = 0;
		for (int i = 0; i < input.size(); i+=3) {
			String s1 = input.get(i);
			String s2 = input.get(i + 1);
			if (inRightOrder(s1, s2)){
				sum+= pair;
			}
			pair++;
		}
		System.out.println(sum);
	}

	private boolean inRightOrder(String s1, String s2) {
		Item i1 = toItem(s1);
		Item i2 = toItem(s2);
		System.out.println(i1);
		System.out.println(i2);
		System.out.println(inRightOrder(i1, i2));
		System.out.println();
		return inRightOrder(i1, i2) <0;
	}

	public int inRightOrder(Item i1, Item i2){
		if (i1 instanceof IntItem){
			if (i2 instanceof IntItem) {
				if (((IntItem) i1).value < ((IntItem) i2).value){
					return -1;
				} else if (((IntItem) i1).value == ((IntItem) i2).value){
					return 0;
				} else {
					return 1;
				}
			} else {
				return inRightOrder(((IntItem) i1).toList(), i2);
			}
		} else {
			if (i2 instanceof ListItem){
				ListItem l1 = (ListItem) i1;
				ListItem l2 = (ListItem) i2;
				int index = 0;
				while (true) {
					if (index == l1.items.size()){
						if (index == l2.items.size()){
							return 0;
						}
						return -1;
					}
					if (index == l2.items.size()){
						return 1;
					}
					final int comparison = inRightOrder(l1.items.get(index), l2.items.get(index));
					if (comparison != 0){
						return comparison;
					}
					index++;
				}
			} else {
				return inRightOrder(i1, ((IntItem) i2).toList());
			}
		}
	}

	private Item toItem(String s2) {
		ListItem current = new ListItem(null);

		final String[] split = s2.split(",");
		for (String s : split) {
			while (s.charAt(0) == '['){
				final ListItem next = new ListItem(current);
				current.items.add(next);
				current = next;
				s = s.substring(1);
			}
			String tail = "";
			if (s.indexOf(']') >=0){
				tail = s.substring(s.indexOf(']'));
				s = s.substring(0, s.indexOf(']'));
			}
			if (!s.isEmpty()){
				current.items.add(new IntItem(current, Integer.parseInt(s)));
			}
			for (int i = 0; i < tail.length(); i++) {
				current = current.parent;
			}
		}
		return current.items.get(0);
	}

	@Override
	public void solvePartTwo(List<String> input) {
		List<Item> items = new ArrayList<>();
		for (int i = 0; i < input.size(); i+=3) {
			items.add( toItem( input.get(i)));
			items.add( toItem( input.get(i + 1)));
		}
		final Item divider1 = toItem("[[2]]");
		final Item divider2 = toItem("[[6]]");
		items.add(divider1);
		items.add(divider2);
		Collections.sort(items, this::inRightOrder);
		System.out.println(items);

		System.out.println(items.indexOf(divider1) + 1);
		System.out.println(items.indexOf(divider2) + 1);

		System.out.println((items.indexOf(divider1) + 1) * (items.indexOf(divider2) + 1));

	}

	private static  abstract class Item{
		ListItem parent;

		public Item(ListItem parent) {
			this.parent = parent;
		}

	}

	private static class IntItem extends Item {
		int value;

		public IntItem(ListItem parent, int value) {
			super(parent);
			this.value = value;
		}

		@Override
		public String toString() {
			return "" + value;
		}

		public ListItem toList(){
			final ListItem listItem = new ListItem(parent);
			listItem.items.add(this);
			return listItem;

		}
	}

	private static class ListItem extends Item {
		List<Item> items = new ArrayList<>();

		public ListItem(ListItem parent) {
			super(parent);
		}


		@Override
		public String toString() {
			return items.toString();
		}

	}
}
