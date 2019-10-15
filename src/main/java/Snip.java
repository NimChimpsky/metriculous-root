import java.io.Serializable;
import java.time.LocalTime;
import java.util.Comparator;

public class Snip {

    public static void main(String[] args) {
        LocalTime now = LocalTime.now();
        LocalTime before = LocalTime.MIN;
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