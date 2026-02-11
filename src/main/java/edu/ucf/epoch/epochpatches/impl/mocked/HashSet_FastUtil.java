package edu.ucf.epoch.epochpatches.impl.mocked;

import edu.ucf.epoch.epochpatches.util.documentation.UsedViaReflection;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Can be stuck into a `HashSet` slot, but uses the MUCH faster FastUtil set.
 *
 * Seriously, why are Java's HashSets so bad???
 */
@UsedViaReflection
public final class HashSet_FastUtil<T> extends HashSet<T> {
	private final ObjectOpenHashSet<T> internal;
	
	@UsedViaReflection
	public HashSet_FastUtil() {
		internal = new ObjectOpenHashSet<>();
	}
	
	@UsedViaReflection
	public HashSet_FastUtil(@NotNull Collection<? extends T> c) {
		internal = new ObjectOpenHashSet<>(c);
	}
	
	@UsedViaReflection
	public HashSet_FastUtil(int initialCapacity) {
		internal = new ObjectOpenHashSet<>(initialCapacity);
	}
	
	@UsedViaReflection
	public HashSet_FastUtil(int initialCapacity, float loadFactor) {
		internal = new ObjectOpenHashSet<>(initialCapacity, loadFactor);
	}
	
	
	
	@Override
	public boolean add(T t) {
		return internal.add(t);
	}
	
	@Override
	public void clear() {
		internal.clear();
	}
	
	@SuppressWarnings("MethodDoesntCallSuperMethod")
	@Override
	public Object clone() {
		return internal.clone();
	}
	
	@Override
	public boolean contains(Object o) {
		return internal.contains(o);
	}
	
	@Override
	public boolean isEmpty() {
		return internal.isEmpty();
	}
	
	@Override
	public Iterator<T> iterator() {
		return internal.iterator();
	}
	
	@Override
	public boolean remove(Object o) {
		return internal.remove(o);
	}
	
	@Override
	public int size() {
		return internal.size();
	}
	
	@Override
	public Spliterator<T> spliterator() {
		return internal.spliterator();
	}
	
	@Override
	public Object[] toArray() {
		return internal.toArray();
	}
	
	@Override
	public <T1> T1[] toArray(T1[] a) {
		return internal.toArray(a);
	}
	
	@Override
	public boolean equals(Object o) {
		return internal.equals(o);
	}
	
	@Override
	public int hashCode() {
		return internal.hashCode();
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		return internal.removeAll(c);
	}
	
	@Override
	public boolean addAll(@NotNull Collection<? extends T> c) {
		return internal.addAll(c);
	}
	
	@Override
	public boolean containsAll(@NotNull Collection<?> c) {
		return internal.containsAll(c);
	}
	
	@Override
	public boolean retainAll(@NotNull Collection<?> c) {
		return internal.retainAll(c);
	}
	
	@Override
	public String toString() {
		return internal.toString();
	}
	
	@Override
	public @NotNull Stream<T> parallelStream() {
		return internal.parallelStream();
	}
	
	@Override
	public boolean removeIf(@NotNull Predicate<? super T> filter) {
		return internal.removeIf(filter);
	}
	
	@Override
	public @NotNull Stream<T> stream() {
		return internal.stream();
	}
	
	@Override
	public <T1> T1[] toArray(@NotNull IntFunction<T1[]> generator) {
		return internal.toArray(generator);
	}
	
	@Override
	public void forEach(Consumer<? super T> action) {
		internal.forEach(action);
	}
}
