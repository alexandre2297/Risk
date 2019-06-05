package IA;

public class Pair<T, U> implements Comparable {
    public final T first;
    public final U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    //USE ONLY FOR COMPARING INTS, otherwise it will throw
    @Override
    public int compareTo(Object o) {
        Pair pair = (Pair) o;
        return Integer.compare((Integer) first, (Integer) pair.first);
    }
}
