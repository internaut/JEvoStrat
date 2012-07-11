package net.mkonrad.evostrat;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Implementation of the "Evolutionsstrategie" (Evolutionary Strategy) of
 * Ingo Rechenberg.
 * 
 * @author Markus Konrad <post@mkonrad.net>
 */
public class EvoStrat {
    /**
     * Target to be optimized.
     */
    private EvoOptimizable target;
    
    /**
     * Defines in which direction we strive: To a maximum or minimum
     * -1 means to minimum, +1 to maximum
     */
    private int striveValue;
    
    /**
     * FIFO queue with last <fitnessListSize> values.
     */
    private LinkedList<Float> fitnessList;
    
    /**
     * Result parameters.
     */
    private HashSet<EvoParam> optimizedParams;
    
    /**
     * Size of the fitness list, i.e. how many of the last fitness results are
     * saved in the <fitnessList>.
     */
    private int fitnessListSize;
    
    /**
     * Minimum fitness list entropy until computation terminates.
     */
    private float minFitnessEntropy;
    
    /**
     * Initial standard deviation.
     */
    private float maxSD;
    
    /**
     * Decrease factor for the standard deviation.
     */
    private float sdDecreaseFactor;
    
    /**
     * Worst impossible fitness "improve" value when considering taking a worse
     * child than the current parent.
     */
    private float worstPossibleImprov;
    
    /**
     * Debug output on/off.
     */
    private boolean dbg;
    
    /**
     * Create the Evolutionary Strategy class with default values.
     */
    public EvoStrat() {
        dbg = false;
        striveValue = 1;
        maxSD = 1.0f;
        sdDecreaseFactor = 0.8f;
        worstPossibleImprov = -1.0f;
        fitnessListSize = 10;
        
        fitnessList = new LinkedList<Float>();
        minFitnessEntropy = 1.0f;
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

    public int getFitnessListSize() {
        return fitnessListSize;
    }

    public void setFitnessListSize(int fitnessListSize) {
        this.fitnessListSize = fitnessListSize;
    }

    public float getMinFitnessEntropy() {
        return minFitnessEntropy;
    }

    public void setMinFitnessEntropy(float minFitnessEntropy) {
        this.minFitnessEntropy = minFitnessEntropy;
    }
    
    /**
     * Load parameter values from a file.
     * @param file path to save-file
     * @return true on success, else false.
     */
    public boolean load(String file) {
        File f = new File(file);
        if (!f.canRead()) {
            return false;
        }
        
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream in = new ObjectInputStream(fis);
            
            optimizedParams = (HashSet<EvoParam>)in.readObject();
            
            in.close();
            fis.close();
        } catch (IOException ex) {
            System.err.println("IO Error - could not load file " + file);
            System.err.println("Reason: " + ex.getMessage());
            return false;
        } catch(ClassNotFoundException ex) {
            System.err.println("Class not found error - could not load file " + file);
            System.err.println("Reason: " + ex.getMessage());
            return false;            
        }
        
        System.out.println("Loaded parameters:");
        
        for (EvoParam param : optimizedParams) {
            System.out.println("> param " + param.name + " = " + param.val);
        }
        
        return true;
    }
    
    /**
     * Save the current optimized values in a file.
     * @param file path to save-file
     * @return true on success, else false.
     */
    public boolean save(String file) {
        if (optimizedParams == null || optimizedParams.size() <= 0) {
            System.err.println("There are no optimized parameters to save!");
            
            return false;
        }
        
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            
            out.writeObject(optimizedParams);
            
            out.close();
            fos.close();
        } catch (IOException ex) {
            System.err.println("Could not save file " + file);
            System.err.println("Reason: " + ex.getMessage());
            return false;
        }
        
        System.out.println("Saved parameters:");
        
        for (EvoParam param : optimizedParams) {
            System.out.println("> param " + param.name + " = " + param.val);
        }
        
        return true;
    }
    
    /**
     * Start the optimization process.
     */
    public void optimize() {
        fitnessList.clear();
        
        boolean isOptimized = false;
        HashSet<EvoParamProperties> paramProps = target.getParamPropertiesSet();
        HashMap<String, EvoParamCandidate> paramCandidatesParent = new HashMap<String, EvoParamCandidate>(paramProps.size());
        HashMap<String, EvoParamCandidate> paramCandidatesChild = new HashMap<String, EvoParamCandidate>(paramProps.size());
        float childFitness, parentFitness = -Float.MAX_VALUE;
        float randChoiceSD = maxSD;
        int iter = 0;
        float fitnessEntropy = Float.MAX_VALUE;
        
        while (!isOptimized) {
            // mutate each parameter
            for (EvoParamProperties prop : paramProps) {
                // get the parameter name
                String paramName = prop.getName();
                
                // get the parameter candidate from the parent
                EvoParamCandidate parentCandidate = paramCandidatesParent.get(paramName);
                
                if (parentCandidate == null) {    // create a candidate if it does not exist yet (1st run)
                    parentCandidate = new EvoParamCandidate(paramName, prop.getMeanVal());
                    paramCandidatesParent.put(paramName, parentCandidate);
                }
                
                // create a copy for the child
                EvoParamCandidate childCandidate = new EvoParamCandidate(parentCandidate);
                
                // create a VALID mutated value around the mean value
                float mutatedVal;
                do {
                    mutatedVal = prop.transformValue(EvoRandom.gaussianFloat(parentCandidate.val, childCandidate.curSD));
                } while (!prop.isValidValue(mutatedVal));
                
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
            float randChoice = Math.abs(EvoRandom.gaussianFloat(0.0f, randChoiceSD));
            if (improv > 0 || (randChoice >= randChoiceSD / 2.0f && improv >= worstPossibleImprov)) {
                if (dbg) {
                    System.out.println("> switched generation! child is now new parent.");
                }
                
                // swap values
                parentFitness = childFitness;
                paramCandidatesParent = (HashMap<String, EvoParamCandidate>)paramCandidatesChild.clone();
                
                // update SD values                
                for (EvoParamProperties prop : paramProps) {
                    EvoParamCandidate candidate = paramCandidatesParent.get(prop.getName());
                    
                    candidate.curSD *= prop.getSdDecreaseFactor();
                }
                
                // add to fitness list
                fitnessList.add(new Float(parentFitness));
                if (fitnessList.size() > fitnessListSize) {
                    fitnessList.poll();
                    fitnessEntropy = calcEntropy(fitnessList);
                }
            } else {
                if (dbg) {
                    System.out.println("> dismissed child.");
                }
            }
            
            paramCandidatesChild.clear();
            
            randChoiceSD *= sdDecreaseFactor;
            
            // check if we want to optimize any further
            isOptimized = randChoiceSD <= 0.000001f || fitnessEntropy <= minFitnessEntropy;
            
            // increase number of iterations
            iter++;
        }
        
        // create the final results
        optimizedParams = new HashSet<EvoParam>();
        
        for (EvoParam param : paramCandidatesParent.values()) {
            optimizedParams.add(param);
        }
    }
    
    /**
     * Return the optimized parameters.
     * @return 
     */
    public HashSet<EvoParam> getOptimizedParams() {
        return optimizedParams;
    }

    public EvoOptimizable getTarget() {
        return target;
    }

    public void setTarget(EvoOptimizable target) {
        this.target = target;
    }
    
    
    /**
     * Calculate the entropy of a list.
     * @param list
     * @return 
     */
    private float calcEntropy(Iterable list) {
        float entropy = -Float.MAX_VALUE;
        
        Iterator it = list.iterator();
        float lastVal = ((Float)it.next()).floatValue();
        while (it.hasNext()) {
            float curVal = ((Float)it.next()).floatValue();
//            float d = (lastVal - curVal) * (lastVal - curVal);
            float d = Math.abs(lastVal - curVal);
            if (d > entropy) {
                entropy = d;
            }
        }
        
        return entropy;
    }
    
    /**
     * Extended EvoParam with a standard deviation value.
     */
    static private class EvoParamCandidate extends EvoParam implements Serializable {
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
