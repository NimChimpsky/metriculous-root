import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class Snip {

    public static void main(String[] args) {
        Set<String> test = new TreeSet<>(new NullFirstNaturalComparator());
        test.add("cheese");
        test.add("ham");
        test.add(null);
        System.out.println("Contains " + (test.contains(null)));
    }

    public static int fastModulo(int dividend, int divisor) {
        return dividend & (divisor - 1);
    }

    public static class NullFirstNaturalComparator implements Comparator<Comparable>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int compare(Comparable object, Comparable other) {
            if (object == null) {
                return (other == null) ? 0 : -1;
            } else if (other == null) {
                return 1;
            } else {
                return object.compareTo(other);
            }
        }
    }
}