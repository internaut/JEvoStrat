/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package evostrattest;

import java.util.HashSet;
import net.mkonrad.evostrat.EvoParam;
import net.mkonrad.evostrat.EvoStrat;

/**
 *
 * @author markus
 */
public class EvoStratTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FuncTest t = new FuncTest();
        FuncTest2 t2 = new FuncTest2();
        
        EvoStrat evoStrat = new EvoStrat();
        
        evoStrat.setSdDecreaseFactor(0.95f);
        evoStrat.setWorstPossibleImprov(-0.05f);
        evoStrat.setDbg(true);
        evoStrat.setTarget(t2);
        evoStrat.setStriveValue(1);
        
        evoStrat.optimize();
        
        HashSet<EvoParam> optParams = evoStrat.getOptimizedParams();
        t2.setParamSet(optParams);
        float res = t2.makeTestRun();
        
        System.out.println();
        System.out.println();
        System.out.println("Optimized params:");
        float x = 0.0f;
        float y = 0.0f;
        for (EvoParam param : optParams) {
            System.out.println("> " + param.name + " = " + param.val);
        }
        
        
        System.out.println("Result:");
        System.out.println("> z = " + res);
    }
}
