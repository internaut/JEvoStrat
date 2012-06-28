/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mkonrad.evostrat;

/**
 *
 * @author markus
 */
public class EvoParamConditionMinMax extends EvoParamCondition {
    float minVal;
    float maxVal;
    
    @Override
    boolean valueIsPossible(float v) {
        return v >= minVal && v <= maxVal;
    }
    
}
