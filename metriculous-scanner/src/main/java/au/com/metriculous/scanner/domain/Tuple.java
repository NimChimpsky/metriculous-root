package au.com.metriculous.scanner.domain;

import java.util.Objects;

public class Tuple<L extends Comparable, R extends Comparable> implements Comparable<Tuple<L, R>> {
    private final L left;
    private final R right;

    public Tuple(final L left, final R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(getLeft(), tuple.getLeft()) &&
                Objects.equals(getRight(), tuple.getRight());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLeft(), getRight());
    }

    @Override
    public int compareTo(Tuple<L, R> other) {
        return getLeft().compareTo(other.getLeft());
    }

}