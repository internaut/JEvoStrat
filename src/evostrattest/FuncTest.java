package evostrattest;

import java.util.HashMap;
import java.util.HashSet;
import net.mkonrad.evostrat.EvoOptimizable;
import net.mkonrad.evostrat.EvoParam;
import net.mkonrad.evostrat.EvoParamConditionMinMax;
import net.mkonrad.evostrat.EvoParamProperties;

/**
 * Test class that wants its parameter "x" to be optimized.
 * 
 * @author Markus Konrad - <post@mkonrad.net>
 */
public class FuncTest implements EvoOptimizable {
    private HashMap<String, EvoParam> params;
    private HashSet<EvoParamProperties> paramProps;

    public FuncTest() {
        params = new HashMap<String,EvoParam>();
        paramProps = new HashSet<EvoParamProperties>();
        
        EvoParamProperties propX = new EvoParamProperties("x", 0.5f, 1.0f, 0.9f, 0.1f);
        propX.addParamCondition(new EvoParamConditionMinMax(0.0f, 1.0f));
        paramProps.add(propX);
    }
    
    @Override
    public HashSet<EvoParamProperties> getParamPropertiesSet() {
        return paramProps;
    }

    @Override
    public void setParam(EvoParam param) {
        params.put(param.name, param);
    }

    @Override
    public float makeTestRun() {
        float y;
        float x = params.get("x").val;
        
//        y = (float)Math.sin(x * Math.PI);     // max: x = 0.5, y = 1.0
//        y = (float)Math.sin(x * x * Math.PI);   // max: x = 0.705, y = 1.0
        y = (float)Math.cos(x * Math.PI) + x;   // max: x = 0.1, y = 1.05
        
        return y;
    }

    @Override
    public void setParamSet(HashSet<EvoParam> params) {
        for (EvoParam p : params) {
            setParam(p);
        }
    }
    
}
