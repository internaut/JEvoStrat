package net.mkonrad.evostrat;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class that specifies parameter properties for use in the Evolutionary
 * Strategy algorithm.
 * 
 * @author Markus Konrad <post@mkonrad.net>
 */
public class EvoParamProperties {
    private String name;
    private float meanVal;
    private float maxSD;
    private float sdDecreaseFactor;
    private float minEntropy;
    private HashSet<EvoParamCondition> conditions;
    private ArrayList<EvoParamTransform> transforms;

    /**
     * Construct a new parameter with defined properties.
     * 
     * @param name          parameter name
     * @param meanVal       mean value
     * @param maxSD         maximum (initial) standard deviation (sd)
     * @param sdDecrease    standard deviation decrease factor
     * @param minEntropy    (not used)
     */
    public EvoParamProperties(String name, float meanVal, float maxSD, float sdDecrease, float minEntropy) {
        this.name = name;
        this.meanVal = meanVal;
        this.maxSD = maxSD;
        this.sdDecreaseFactor = sdDecrease;
        this.minEntropy = minEntropy;
        this.conditions = new HashSet<EvoParamCondition>();
        this.transforms = new ArrayList<EvoParamTransform>();
    }
    
    /**
     * Add a new condition the parameter has to fulfill.
     * @param paramCond parameter condition
     */
    public void addParamCondition(EvoParamCondition paramCond) {
        conditions.add(paramCond);
    }
    
    /**
     * Remove a parameter condition.
     * @param paramCond parameter condition
     */
    public void removeParamCondition(EvoParamCondition paramCond) {
        conditions.remove(paramCond);
    }
    
    /**
     * Add parameter value transformer.
     * @param paramTrans    parameter value transformer
     */
    public void addParamTransform(EvoParamTransform paramTrans) {
        transforms.add(paramTrans);
    }
    
    /**
     * Remove parameter value transformer.
     * @param paramTrans    parameter value transformer
     */
    public void removeParamCondition(EvoParamTransform paramTrans) {
        transforms.remove(paramTrans);
    }    
    
    /**
     * Return all parameter conditions.
     * @return all parameter conditions
     */
    public HashSet<EvoParamCondition> getConditions() {
        return conditions;
    }

    /**
     * Return all parameter transformers.
     * @return all parameter transformers
     */
    public ArrayList<EvoParamTransform> getTransforms() {
        return transforms;
    }

    public float getMaxSD() {
        return maxSD;
    }

    public void setMaxSD(float maxSD) {
        this.maxSD = maxSD;
    }

    public float getSdDecreaseFactor() {
        return sdDecreaseFactor;
    }

    public void setSdDecreaseFactor(float sdDecreaseFactor) {
        this.sdDecreaseFactor = sdDecreaseFactor;
    }   
    
    public void setConditions(HashSet<EvoParamCondition> conditions) {
        this.conditions = conditions;
    }

    public float getMeanVal() {
        return meanVal;
    }

    public void setMeanVal(float meanVal) {
        this.meanVal = meanVal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMinEntropy() {
        return minEntropy;
    }

    public void setMinEntropy(float minEntropy) {
        this.minEntropy = minEntropy;
    }
    
    /**
     * Check if <v> meets the parameter's conditions.
     * @param v value
     * @return true if it meets all parameter's conditions, else false
     */
    public boolean isValidValue(float v) {
        for (EvoParamCondition cond : conditions) {
            if (!cond.valueIsPossible(v)) return false;
        }
        
        return true;
    }
    
    /**
     * Transform the value <v> using all added parameter transformers.
     * @param v value
     * @return transformed value
     */
    public float transformValue(float v) {
        for (EvoParamTransform trans : transforms) {
            v = trans.transformValue(v);
        }
        
        return v;
    }
}
