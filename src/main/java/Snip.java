public class Snip {

    public static void main(String[] args) {

    }

    public static int fastModulo(int dividend, int divisor) {
        return dividend & (divisor - 1);
    }
}