package net.mkonrad.evostrat;

/**
 * Abstract class for transforming parameter values.
 * 
 * @author Markus Konrad <post@mkonrad.net>
 */
abstract public class EvoParamTransform {
    /**
     * Transform the value <v>.
     * @param v value
     * @return transformed value
     */
    abstract public float transformValue(float v);
}
