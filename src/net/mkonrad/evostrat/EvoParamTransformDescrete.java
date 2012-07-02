/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mkonrad.evostrat;

/**
 *
 * @author markus
 */
public class EvoParamTransformDescrete extends EvoParamTransform {
    public float base;
    
    public EvoParamTransformDescrete() {
        base = 1.0f;
    }
    
    @Override
    public float transformValue(float v) {
        return Math.round(v / base) * base;
    }
    
}
