package net.mkonrad.evostrat;

/**
 * Class for transforming float values to descrete values with a specified base.
 * By default rounds the value to become an integer value.
 * 
 * @author Markus Konrad <post@mkonrad.net>
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
