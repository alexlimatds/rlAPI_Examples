package org.rlapi_examples.peggame;

/**
 * Place of a peg game board.
 * 
 * @author Alexandre Lima
 */
public class Place {
    
    private boolean occupied;
    private final int level;
    private final int position;

    public Place(int level, int position) {
        this.level = level;
        this.position = position;
        this.occupied = true;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public int getLevel() {
        return level;
    }

    public int getPosition() {
        return position;
    }

    public void occupy(){
        if(isOccupied()){
            throw new IllegalArgumentException("The place is already occupied");
        }
        this.occupied = true;
    }
    
    public void clear(){
        if(!isOccupied()){
            throw new IllegalArgumentException("The place is already empty");
        }
        this.occupied = false;
    }
}