/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mkonrad.evostrat;

import java.util.HashSet;

/**
 *
 * @author markus
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
