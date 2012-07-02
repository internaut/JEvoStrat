/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mkonrad.evostrat;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author markus
 */
public class EvoParamProperties {
    private String name;
    private float meanVal;
    private float maxSD;
    private float sdDecreaseFactor;
    private float minEntropy;
    private HashSet<EvoParamCondition> conditions;
    private ArrayList<EvoParamTransform> transforms;

    public EvoParamProperties(String name, float meanVal, float maxSD, float sdDecrease, float minEntropy) {
        this.name = name;
        this.meanVal = meanVal;
        this.maxSD = maxSD;
        this.sdDecreaseFactor = sdDecrease;
        this.minEntropy = minEntropy;
        this.conditions = new HashSet<EvoParamCondition>();
        this.transforms = new ArrayList<EvoParamTransform>();
    }
    
    public void addParamCondition(EvoParamCondition paramCond) {
        conditions.add(paramCond);
    }
    
    public void removeParamCondition(EvoParamCondition paramCond) {
        conditions.remove(paramCond);
    }
    
    public void addParamTransform(EvoParamTransform paramTrans) {
        transforms.add(paramTrans);
    }
    
    public void removeParamCondition(EvoParamTransform paramTrans) {
        transforms.remove(paramTrans);
    }    
    
    public HashSet<EvoParamCondition> getConditions() {
        return conditions;
    }

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
    
    public boolean isValidValue(float v) {
        for (EvoParamCondition cond : conditions) {
            if (!cond.valueIsPossible(v)) return false;
        }
        
        return true;
    }
    
    public float transformValue(float v) {
        for (EvoParamTransform trans : transforms) {
            v = trans.transformValue(v);
        }
        
        return v;
    }
}
