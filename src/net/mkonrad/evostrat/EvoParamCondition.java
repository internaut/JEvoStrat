package net.mkonrad.evostrat;

/**
 * Abstract class for parameter conditions.
 * 
 * @author Markus Konrad <post@mkonrad.net>
 */
abstract public class EvoParamCondition {
    /**
     * Check if a value meets the condition.
     * 
     * @param v value
     * @return true if condition is met, else false.
     */
    abstract boolean valueIsPossible(float v);
}
