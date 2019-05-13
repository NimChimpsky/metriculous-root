package au.com.metriculous.scanner.domain;

import java.util.Objects;

public class Pair<L extends Comparable, R extends Comparable> implements Comparable<Pair<L, R>> {
    private final L left;
    private final R right;

    public Pair(final L left, final R right) {
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
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(getLeft(), pair.getLeft()) &&
                Objects.equals(getRight(), pair.getRight());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLeft(), getRight());
    }

    @Override
    public int compareTo(Pair<L, R> other) {
        return getLeft().compareTo(other.getLeft());
    }

}