package au.com.metriculous.scanner.domain;

public class Triple<T, U, R> {
    private final T left;
    private final U centre;
    private final R right;

    public T getLeft() {
        return left;
    }

    public U getCentre() {
        return centre;
    }

    public R getRight() {
        return right;
    }

    public Triple(final T left, final U centre, final R right) {
        this.left = left;
        this.centre = centre;
        this.right = right;
    }
}
