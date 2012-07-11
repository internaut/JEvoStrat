package net.mkonrad.evostrat;

/**
 * Class that holds static functions for generating different kinds of random
 * numbers.
 * 
 * @author Markus Konrad <post@mkonrad.net>
 */
public class EvoRandom {
    /**
     * Get a gaussian random number of type double with <m> as mean and <sd> as
     * standard deviation.
     * Implements the box muller method.
     * 
     * @param m     mean
     * @param sd    standard deviation
     * @return gaussian random number
     */
    static public double gaussianDouble(double m, double sd) {
        double[] r = new double[2];
        for (int i = 0; i < 2; i++) {
            r[i] = Math.random();
        }
        
        double g = Math.sqrt(-2.0 * Math.log(r[0])) * Math.cos(2.0 * Math.PI * r[1]);
        
        return m + g * sd * sd;
    }

    /**
     * Get a gaussian random number of type float with <m> as mean and <sd> as
     * standard deviation.
     * Implements the box muller method.
     * 
     * @param m     mean
     * @param sd    standard deviation
     * @return gaussian random number
     */
    static public float gaussianFloat(float m, float sd) {
        float[] r = new float[2];
        for (int i = 0; i < 2; i++) {
            r[i] = (float)Math.random();
        }
        
        float g = (float)Math.sqrt(-2.0f * (float)Math.log(r[0])) * (float)Math.cos(2.0 * Math.PI * r[1]);
        
        return m + g * sd;
    }
}
