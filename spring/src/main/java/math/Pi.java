package math;

/**
 * This class calculates Pi using the Gregory-Leibniz Series
 * 
 *   pi = 4 * ((1/1 - 1/3) + (1/5 - 1/7) + (1/9 - 1/11) + ...)
 */
public class Pi {

    /**
     * Calculates Pi using the series.
     * 
     * @param n series expansions
     */
    public static double calculate(int n) {
        double v = 0.0;
        for (int i = 1; i <= n; i += 4) { // increment by 4
            v += 1.0 / i - 1.0 / (i + 2); // add the value of the series
        }
        return 4.0 * v;
    }
}