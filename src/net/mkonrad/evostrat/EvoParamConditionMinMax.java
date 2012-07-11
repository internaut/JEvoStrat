package net.mkonrad.evostrat;

/**
 * Parameter condition for minimum and maximum value.
 * 
 * @author Markus Konrad <post@mkonrad.net>
 */
public class EvoParamConditionMinMax extends EvoParamCondition {
    public float minVal;
    public float maxVal;
    
    /**
     * Create a condition for a parameter with <min> minimum value and <max>
     * maximum value.
     * 
     * @param min minimum value
     * @param max maximum value
     */
    public EvoParamConditionMinMax(float min, float max) {
        minVal = min;
        maxVal = max;
    }
    
    @Override
    boolean valueIsPossible(float v) {
        return v >= minVal && v <= maxVal;
    }
    
}
