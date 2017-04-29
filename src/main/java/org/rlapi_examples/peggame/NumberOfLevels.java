package org.rlapi_examples.peggame;

/**
 *
 * @author Alexandre Lima
 */
public enum NumberOfLevels {
    
    FIVE(5), SIX(6);
    
    public final int number;
    
    private NumberOfLevels(int n){
        this.number = n;
    }
}
