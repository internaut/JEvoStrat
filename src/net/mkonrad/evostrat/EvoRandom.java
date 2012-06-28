/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mkonrad.evostrat;

/**
 *
 * @author markus
 */
public class EvoRandom {
    static public double gaussianDouble(double m, double sd) {
        double[] r = new double[2];
        for (int i = 0; i < 2; i++) {
            r[i] = Math.random();
        }
        
        double g = Math.sqrt(-2.0 * Math.log(r[0])) * Math.cos(2.0 * Math.PI * r[1]);
        
        return m + g * sd * sd;
    }
    
    static public float gaussianFloat(float m, float sd) {
        float[] r = new float[2];
        for (int i = 0; i < 2; i++) {
            r[i] = (float)Math.random();
        }
        
        float g = (float)Math.sqrt(-2.0f * (float)Math.log(r[0])) * (float)Math.cos(2.0 * Math.PI * r[1]);
        
        return m + g * sd * sd;
    }
}
