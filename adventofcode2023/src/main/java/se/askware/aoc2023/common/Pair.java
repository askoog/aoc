package se.askware.aoc2023.common;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class Pair<T> {

	T first;
	T second;

	public Pair(T first, T second) {
		this.first = first;
		this.second = second;
	}

	public static Function<String, Pair<String>> parse(char delimiter){
		return s -> valueOf(s, delimiter);
	}

	public static Pair<String> valueOf(String s, char delimiter){
		return new Pair(s.substring(0, s.indexOf(delimiter)), s.substring(s.indexOf(delimiter) + 1));
	}

	public <R> Pair<R> map(Function<T,R> mapper){
		return new Pair<>(mapper.apply(first),mapper.apply(second));
	}

	public <R> R reduce(BiFunction<T,T,R> function){
		return function.apply(first,second);
	}

	public boolean matches(BiPredicate<T,T> predicate){
		return predicate.test(first,second);
	}

	public Pair<T> flip(){
		return new Pair(second, first);
	}

	public Pair<T> update(T first, T second){
		return new Pair<>(first,second);
	}

	public T getFirst() {
		return first;
	}

	public T getSecond() {
		return second;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final Pair<?> pair = (Pair<?>) o;
		return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
	}

	@Override
	public int hashCode() {
		return Objects.hash(first, second);
	}

	@Override
	public String toString() {
		return first +  "," + second;
	}
}
