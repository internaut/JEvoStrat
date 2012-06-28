/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mkonrad.evostrat;

import java.util.HashMap;
import java.util.HashSet;


/**
 *
 * @author markus
 */
public class EvoStrat {
    private EvoOptimizable target;
    
    /**
     * Defines in which direction we strive: To a maximum, minimum or 0.
     * -1 means to minimum, +1 to maximum, 0 to zero.
     */
    private int striveValue;
    
    private HashSet<EvoParam> optimizedParams;
    
    private float maxSD;
    private float sdDecreaseFactor;
    private float worstPossibleImprov;
    
    private boolean dbg;
    
    public EvoStrat() {
        dbg = false;
        striveValue = 1;
        maxSD = 1.0f;
        sdDecreaseFactor = 0.8f;
        worstPossibleImprov = -10.0f;
    }

    public boolean isDbg() {
        return dbg;
    }

    public void setDbg(boolean dbg) {
        this.dbg = dbg;
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

    public int getStriveValue() {
        return striveValue;
    }

    public void setStriveValue(int striveValue) {
        this.striveValue = striveValue;
    }

    public float getWorstPossibleImprov() {
        return worstPossibleImprov;
    }

    public void setWorstPossibleImprov(float worstPossibleImprov) {
        this.worstPossibleImprov = worstPossibleImprov;
    }
    
    public void optimize() {
        boolean isOptimized = false;
        HashSet<EvoParamProperties> paramProps = target.getParamPropertiesSet();
        HashMap<String, EvoParamCandidate> paramCandidatesParent = new HashMap<String, EvoParamCandidate>(paramProps.size());
        HashMap<String, EvoParamCandidate> paramCandidatesChild = new HashMap<String, EvoParamCandidate>(paramProps.size());
        float childFitness, parentFitness = -Float.MAX_VALUE;
        float randChoiceSD = maxSD;
        int iter = 0;
        
        while (!isOptimized) {
            // mutate each parameter
            for (EvoParamProperties prop : paramProps) {
                // get the parameter name
                String paramName = prop.getName();
                
                // get the parameter candidate from the parent
                EvoParamCandidate candidate = paramCandidatesParent.get(paramName);
                
                if (candidate == null) {    // create a candidate if it does not exist yet (1st run)
                    candidate = new EvoParamCandidate(paramName, prop.getMaxSD());
                    paramCandidatesParent.put(paramName, candidate);
                }
                
                // create a copy for the child
                EvoParamCandidate childCandidate = new EvoParamCandidate(candidate);
                
                // create a mutated value around the mean value
                float mutatedVal = EvoRandom.gaussianFloat(prop.getMeanVal(), childCandidate.curSD);
                childCandidate.val = mutatedVal;
                
                // save it as child
                paramCandidatesChild.put(paramName, childCandidate);
            }
            
            if (dbg) {
                System.out.println("Iteration #" + iter + " with random choice SD " + randChoiceSD + ": ");
            }
            
            // set the parameters
            for (EvoParam param : paramCandidatesChild.values()) {
                target.setParam(param);
                
                if (dbg) {
                    System.out.println("> param " + param.name + " = " + param.val + " (sd = " + ((EvoParamCandidate)param).curSD + ")");
                }
            }
            
            // try them out
            if (dbg) {
                System.out.println("> starting test run...");
            }
            
            childFitness = target.makeTestRun();
            
            if (parentFitness == -Float.MAX_VALUE) {    // 1st run: just created only the first parent
                parentFitness = childFitness;
                
                continue;
            }
            
            // calculate the improvement value
            float improv = striveValue * (childFitness - parentFitness);
            
            if (dbg) {
                System.out.println("> fitness=" + childFitness);
                System.out.println("> improv=" + improv);
            }
            
            // check if we switch to a new generation
            float randChoice = EvoRandom.gaussianFloat(0.0f, randChoiceSD);
            if (improv > 0 || (randChoice >= randChoiceSD / 2.0f && improv >= worstPossibleImprov)) {
                if (dbg) {
                    System.out.println("> switched generation! child is now new parent.");
                }
                
                // swap values
                parentFitness = childFitness;
                paramCandidatesParent = paramCandidatesChild;
                
                // update SD values
                randChoiceSD *= sdDecreaseFactor;
                
                for (EvoParamProperties prop : paramProps) {
                    EvoParamCandidate candidate = paramCandidatesParent.get(prop.getName());
                    
                    candidate.curSD *= prop.getSdDecreaseFactor();
                }
            } else {
                if (dbg) {
                    System.out.println("> dismissed child.");
                }
            }
            
            // check if we want to optimize any further
            isOptimized = randChoiceSD <= 0.10f;
            
            // increase number of iterations
            iter++;
        }
        
        // create the final results
        optimizedParams = new HashSet<EvoParam>();
        
        for (EvoParam param : paramCandidatesParent.values()) {
            optimizedParams.add(param);
        }
    }
    
    public HashSet<EvoParam> getOptimizedParams() {
        return optimizedParams;
    }

    public EvoOptimizable getTarget() {
        return target;
    }

    public void setTarget(EvoOptimizable target) {
        this.target = target;
    }
    
    private void updateParamValue(EvoParamProperties prop, EvoParamCandidate param) {
        float g = EvoRandom.gaussianFloat(prop.getMeanVal(), param.curSD);
        
    }
    
    private class EvoParamCandidate extends EvoParam {
        float curSD;
        
        public EvoParamCandidate(String name, float curSD) {
            this.name = name;
            this.curSD = curSD;
        }
        
        public EvoParamCandidate(EvoParamCandidate orig) {
            name = orig.name;
            curSD = orig.curSD;
            val = orig.val;
        }
    }
}
