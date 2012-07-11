package evostrattest;

import java.util.HashSet;
import net.mkonrad.evostrat.EvoParam;
import net.mkonrad.evostrat.EvoStrat;

/**
 * Main test class for EvoStrat library.
 * 
 * @author Markus Konrad - <post@mkonrad.net>
 */
public class EvoStratTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // "FuncTest1" optimizes one parameter "x" to find the maximum y of a
        // math. function.
        FuncTest t = new FuncTest();
        // you can try out "FuncTest2" also. It has 2 parameters to optimize.
//        FuncTest2 t2 = new FuncTest2();
        
        // Create the Evolutionary Strategy object
        EvoStrat evoStrat = new EvoStrat();
        
        // set some properties
        
        // this is the decrease factor for the initial standard deviation
        // it is decreased by this value on each generation.
        // The higher, the more accurate is the result but also the longer
        // is the computation
        evoStrat.setSdDecreaseFactor(0.99f);
        
        // Value that defines when a child is taken in favor of his parent
        // although it is worse in terms of fitness. Must be a negative value!
        evoStrat.setWorstPossibleImprov(-0.05f);
        
        // Defines the minimum entropy of the last results. When the last
        // fitness results don't change very much (i.e. the entropy is low)
        // the computation terminates.
        evoStrat.setMinFitnessEntropy(0.01f);
        
        // Debug output on/off
        evoStrat.setDbg(true);
        
        // Set the target class which implements EvoOptimizable
        evoStrat.setTarget(t);
        
        // Set if we want to find a maximum ("1") or a minimum ("-1")
        evoStrat.setStriveValue(1);
        
        // Start the process.
        evoStrat.optimize();
        
        // Get the optimized parameters.
        HashSet<EvoParam> optParams = evoStrat.getOptimizedParams();
        t.setParamSet(optParams);
        float res = t.makeTestRun();
        
        // Output the optimized parameters.
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
