/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evostrattest;

import java.util.HashMap;
import java.util.HashSet;
import net.mkonrad.evostrat.EvoOptimizable;
import net.mkonrad.evostrat.EvoParam;
import net.mkonrad.evostrat.EvoParamProperties;

/**
 *
 * @author markus
 */
public class FuncTest implements EvoOptimizable {
    private HashMap<String, EvoParam> params;
    private HashSet<EvoParamProperties> paramProps;

    public FuncTest() {
        params = new HashMap<String,EvoParam>();
        paramProps = new HashSet<EvoParamProperties>();
        
        EvoParamProperties propX = new EvoParamProperties("x", 0.0f, 5.0f, 0.8f, 0.1f);
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
        
        y = (float)Math.sin(x * Math.PI);
        
        return y;
    }

    @Override
    public void setParamSet(HashSet<EvoParam> params) {
        for (EvoParam p : params) {
            setParam(p);
        }
    }
    
}
