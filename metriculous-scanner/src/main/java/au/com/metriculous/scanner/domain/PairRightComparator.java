package au.com.metriculous.scanner.domain;

import java.util.Comparator;

public class PairRightComparator implements Comparator<Pair> {
    @Override
    public int compare(Pair pair, Pair other) {
        return pair.getRight().compareTo(other.getRight());
    }
}
