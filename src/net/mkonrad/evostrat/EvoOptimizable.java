package net.mkonrad.evostrat;

import java.util.HashSet;

/**
 * Interface for a class that wants to optimize its parameters using
 * Evolutionary strategy.
 * 
 * @author Markus Konrad <post@mkonrad.net>
 */
public interface EvoOptimizable {
    /**
     * Return a defined parameter property set containing all parameters with
     * their names, condiditions and so on.
     * @return parameter properties
     */
    HashSet<EvoParamProperties> getParamPropertiesSet();
    
    /**
     * Set a parameter to try it out.
     * @param param parameter with name and value
     */
    void setParam(EvoParam param);
    
    /**
     * Set all params that are contained in a HashSet
     * @param params 
     */
    void setParamSet(HashSet<EvoParam> params);
    
    /**
     * Make the test run and return the quality
     * @return quality measure
     */
    float makeTestRun();

}
