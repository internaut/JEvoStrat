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
        
        EvoStrat evoStrat = new EvoStrat();
        
        evoStrat.setWorstPossibleImprov(-0.1f);
        evoStrat.setDbg(true);
        evoStrat.setTarget(t);
        
        evoStrat.optimize();
        
        HashSet<EvoParam> optParams = evoStrat.getOptimizedParams();
        t.setParamSet(optParams);
        float y = t.makeTestRun();
        
        System.out.println();
        System.out.println();
        System.out.println("Optimized params:");
        for (EvoParam param : optParams) {
            System.out.println("> " + param.name + " = " + param.val);
        }
        
        
        System.out.println("Result:");
        System.out.println("> y = " + y);
    }
}
