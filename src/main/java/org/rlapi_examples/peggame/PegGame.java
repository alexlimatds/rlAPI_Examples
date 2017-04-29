package org.rlapi_examples.peggame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.rlapi.Environment;

/**
 * A peg game with triangle shape.
 * 
 * @author Alexandre Lima
 */
public class PegGame implements Environment{
    
    private final List<Place> places;
    private int numberOfLevels;
    private int initialNumberOfEmptyPlaces;
    private Random rand;
    
    /**
     * Creates a new game together with its places and pieces.
     * @param levels The number of levels (lines) in the board.
     */
    public PegGame(NumberOfLevels levels) {
        this.rand = new Random();
        this.numberOfLevels = levels.number;
        this.initialNumberOfEmptyPlaces = 1;
        this.places = new ArrayList<>();
        //creates the places without the pieces
        for (int level = 0; level < numberOfLevels; level++) {
            for (int position = 0; position <= level; position++) {
                Place c = new Place(level, position);
                places.add(c);
            }
        }
        startGame();
    }
    
    public void setInitialNumberOfEmptyPlaces(int number) {
        if(number < 1 && number > places.size() - 2)
            throw new IllegalArgumentException("Invalid value for the initial number of empty places: " + number);
        this.initialNumberOfEmptyPlaces = number;
    }
    
    /**
     * Changes de board towards the inputed state.
     * @param newState indicates the new board set. Each character from the string 
     * represents a board place. The character 1 means occupied place and the 
     * character 0 means empty place.
     * @throws IllegalArgumentException if the number of characters is different 
     * from the board places number or <code>newState</code> has a character 
     * different from 1 or 0.
     */
    public void setBoardState(String newState){
        if(newState.length() != places.size()){
            throw new IllegalArgumentException("The input number of places is "
                    + "different from the current number");
        }
        for(int i = 0; i < newState.length(); i++){
            char c = newState.charAt(i);
            if(c == '1'){
                if(!places.get(i).isOccupied())
                    places.get(i).occupy();
            }
            else if(c == '0'){
                if(places.get(i).isOccupied())
                    places.get(i).clear();
            }
            else{
                throw new IllegalArgumentException("Character not allowed in "
                        + "the inputed string: " + c);
            }
        }
    }

    /**
     * Starts a new game, i. e., randomly fills the places with pieces.
     */
    public void startGame() {
        for (Place place : places) {
            if(!place.isOccupied())
                place.occupy();
        }
        //Randomly removes pieces according to initial number of empty places
        int i = 0;
        while (i < getInitialNumberOfEmptyPlaces()) {
            int n = rand.nextInt(getNumberOfLevels());
            int pos = rand.nextInt(n + 1);
            if (getPlace(n, pos).isOccupied()) {
                getPlace(n, pos).clear();
                i++;
            }
        }
    }

    /**
     * Returns a string that represents the board current state. Each line 
     * represents one board level, while each character represents one place. 
     * The letter P means that the place is occupied with a piece, while the 
     * letter E means the place is empty.
     * @return
     */
    public String print() {
        StringBuilder strB = new StringBuilder();
        int currentLevel = 0;
        for (Place c : places) {
            if (c.getLevel() != currentLevel) {
                strB.append("\n");
                currentLevel = c.getLevel();
            }
            if (c.isOccupied()) {
                strB.append("P ");
            } else {
                strB.append("E ");
            }
        }
        return strB.toString();
    }

    /**
     * Returns a string that represents the board current state. Each line 
     * represents one board level, while each character represents one place. 
     * The letter P means that the place is occupied with a piece, while the 
     * letter E means the place is empty.
     * @return
     */
    public String print2() {
        StringBuilder strB = new StringBuilder();
        int spaces = getNumberOfLevels() - 1;
        for (int currentLevel = 0; currentLevel < getNumberOfLevels(); currentLevel++) {
            for (int i = 0; i < spaces; i++) {
                strB.append(" ");
            }
            spaces--;
            for (int i = 0; i <= currentLevel; i++) {
                Place c = getPlace(currentLevel, i);
                if (c.isOccupied()) {
                    strB.append("P ");
                } else {
                    strB.append("V ");
                }
            }
            strB.append("\n");
        }
        return strB.toString();
    }

    public List<Place> getPlaces() {
        return places;
    }
    
    /**
     * Moves a piece to other place. The transfer will happen only if the source 
     * place has a piece, the target place is empty and the move is valid. 
     * A valid move means that the target place is neighbor, in the same position, 
     * of a neighbor of the source place. For instance, target place is the east 
     * neighbor of the east neighbor of the source place.
     * @param move A string which the first token is the number of the source 
     * place and the second token is the number of the target place. The tokens 
     * must be separated by an empty space.
     * @return true if the move is valid and false in the other case.
     */
    public boolean movePiece(String move){
        int spaceIndex = move.indexOf(" ");
        int numberOfSourcePlace = Integer.parseInt( move.substring(0, spaceIndex) );
        int numberOfTargetPlace = Integer.parseInt( move.substring(spaceIndex + 1) );
        return movePiece(places.get(numberOfSourcePlace - 1), places.get(numberOfTargetPlace - 1));
    }
    
    /**
     * Moves a piece to other place. The transfer will happen only if the source 
     * place has a piece, the target place is empty and the move is valid. 
     * A valid move means that the target place is neighbor, in the same position, 
     * of a neighbor of the source place. For instance, target place is the east 
     * neighbor of the east neighbor of the source place.
     * @param sourceLevel       Level of the source place.
     * @param sourcePosition    Position of the source place.
     * @param targetLevel       Level of the target place.
     * @param targetPosition    Position of the target place.
     * @return  true if the move is valid and false in the other case.
     */
    public boolean movePiece(int sourceLevel, int sourcePosition, int targetLevel,
            int targetPosition) {
        Place source = getPlace(sourceLevel, sourcePosition);
        Place target = getPlace(targetLevel, targetPosition);
        return movePiece(source, target);
    }

    /**
     * Moves a piece to other place. The transfer will happen only if the source 
     * place has a piece, the target place is empty and the move is valid. 
     * A valid move means that the target place is neighbor, in the same position, 
     * of a neighbor of the source place. For instance, target place is the east 
     * neighbor of the east neighbor of the source place.
     * @param source    The source place.
     * @param target   The target place.
     * @return  true if the move is valid and false in the other case.
     */
    public boolean movePiece(Place source, Place target) {
        //TODO use the method getValidMoves
        if (!source.isOccupied() || target.isOccupied()) {
            return false;
        }
        //Checks if target is the northeast neighbor of the northeast neighbor of source
        Place intermediator = getNortheastNeighbor(source);
        if (getNortheastNeighbor(intermediator) == target && intermediator.isOccupied()) {
            source.clear();
            target.occupy();
            intermediator.clear(); //piece is 'eatten'
            return true;
        }
        //As above but to northwest position
        intermediator = getNorthwestNeighbor(source);
        if (getNorthwestNeighbor(intermediator) == target && intermediator.isOccupied()) {
            source.clear();
            target.occupy();
            intermediator.clear(); //piece is 'eatten'
            return true;
        }
        //As above but to east position
        intermediator = getEastNeighbor(source);
        if (getEastNeighbor(intermediator) == target && intermediator.isOccupied()) {
            source.clear();
            target.occupy();
            intermediator.clear(); //piece is 'eatten'
            return true;
        }
        //As above but to west position
        intermediator = getWestNeighbor(source);
        if (getWestNeighbor(intermediator) == target && intermediator.isOccupied()) {
            source.clear();
            target.occupy();
            intermediator.clear(); //piece is 'eatten'
            return true;
        }
        //As above but to southeast position
        intermediator = getSoutheastNeighbor(source);
        if (getSoutheastNeighbor(intermediator) == target && intermediator.isOccupied()) {
            source.clear();
            target.occupy();
            intermediator.clear(); //piece is 'eatten'
            return true;
        }
        //As above but to southwest position
        intermediator = getSouthwestNeighbor(source);
        if (getSouthwestNeighbor(intermediator) == target && intermediator.isOccupied()) {
            source.clear();
            target.occupy();
            intermediator.clear(); //piece is 'eatten'
            return true;
        }
        
        return false;
    }
    
    /**
     * Return all the valid moves to the current board state.
     * @return Each string from the list represents one move, i. e., the source 
     * place and the target place. The source is the first token of the string 
     * and the target is the second token, and they are separated by a empty space. 
     * For instance: for a valid move which the place 7 is the source and the 
     * place 2 is the target, this move would be represented by the string 
     * <code>7 2</code>.
     * @return 
     */
    public List<String> getValidMoves2(){
        List<Integer[]> moves = getValidMoves();
        List<String> result = new ArrayList<>(moves.size());
        for(Integer[] m : moves){
            Place source = getPlace(m[0], m[1]);
            Place target = getPlace(m[2], m[3]);
            //Notice that for the first place its number is 1
            result.add((places.indexOf(source) + 1) + " " + (places.indexOf(target) + 1) );
        }
        
        return result;
    }
    
    /**
     * Return all the valid moves to the current board state.
     * @return  A list of arrays. Each array has 4 elements distributed by the 
     * following way (index - meaning): 0 - source level; 1 - source position; 
     * 2 - target level; 3 - target position.
     */
    public List<Integer[]> getValidMoves() {
        List<Integer[]> moves = new ArrayList<>();
        for (Place source : places) {
            if (source.isOccupied()) {
                //EAST
                Place neighbor = getEastNeighbor(source);
                Place neighborOfNeighbor = getEastNeighbor(neighbor); //neighbor of neigbhor is the target
                processMove(source, neighbor, neighborOfNeighbor, moves);
                //NORTHEAST
                neighbor = getNortheastNeighbor(source);
                neighborOfNeighbor = getNortheastNeighbor(neighbor);
                processMove(source, neighbor, neighborOfNeighbor, moves);
                //NORTHWEST
                neighbor = getNorthwestNeighbor(source);
                neighborOfNeighbor = getNorthwestNeighbor(neighbor);
                processMove(source, neighbor, neighborOfNeighbor, moves);
                //WEST
                neighbor = getWestNeighbor(source);
                neighborOfNeighbor = getWestNeighbor(neighbor);
                processMove(source, neighbor, neighborOfNeighbor, moves);
                //SOUTHEAST
                neighbor = getSoutheastNeighbor(source);
                neighborOfNeighbor = getSoutheastNeighbor(neighbor);
                processMove(source, neighbor, neighborOfNeighbor, moves);
                //SOUTHWEST
                neighbor = getSouthwestNeighbor(source);
                neighborOfNeighbor = getSouthwestNeighbor(neighbor);
                processMove(source, neighbor, neighborOfNeighbor, moves);
            }
        }
        return moves;
    }
    
    private void processMove(Place source, Place neighbor, Place neighborOfNeighbor, 
            List<Integer[]> moves){
        if(neighborOfNeighbor != null && neighbor != null && 
                neighbor.isOccupied() && !neighborOfNeighbor.isOccupied()){
            moves.add(new Integer[]{source.getLevel(), source.getPosition(), 
            neighborOfNeighbor.getLevel(), neighborOfNeighbor.getPosition()});
        }
    }

    /**
     * Tells if the two places are neighbors of each other.
     * @param a
     * @param b
     * @return  true if the places are neighbors of each other and false in the other case.
     */
    public boolean areNeighbors(Place a, Place b) {
        for (Place c : getNeighbors(a)) {
            if (c.equals(b)) {
                return true;
            }
        }
        return false;
    }

    public List<Place> getNeighbors(Place c) {
        List<Place> neighbors = new ArrayList<>();
        Place n = getEastNeighbor(c);
        if (n != null) {
            neighbors.add(n);
        }
        n = getNortheastNeighbor(c);
        if (n != null) {
            neighbors.add(n);
        }
        n = getNorthwestNeighbor(c);
        if (n != null) {
            neighbors.add(n);
        }
        n = getWestNeighbor(c);
        if (n != null) {
            neighbors.add(n);
        }
        n = getSoutheastNeighbor(c);
        if (n != null) {
            neighbors.add(n);
        }
        n = getSouthwestNeighbor(c);
        if (n != null) {
            neighbors.add(n);
        }
        return neighbors;
    }

    public Place getNortheastNeighbor(Place c) {
        if (c == null) {
            return null;
        }
        int level = c.getLevel();
        int position = c.getPosition();
        return getPlace(level - 1, position);
    }

    public Place getNorthwestNeighbor(Place c) {
        if (c == null) {
            return null;
        }
        int level = c.getLevel();
        int position = c.getPosition();
        return getPlace(level - 1, position - 1);
    }

    public Place getEastNeighbor(Place c) {
        if (c == null) {
            return null;
        }
        int level = c.getLevel();
        int position = c.getPosition();
        return getPlace(level, position + 1);
    }

    public Place getWestNeighbor(Place c) {
        if (c == null) {
            return null;
        }
        int level = c.getLevel();
        int position = c.getPosition();
        return getPlace(level, position - 1);
    }

    public Place getSoutheastNeighbor(Place c) {
        if (c == null) {
            return null;
        }
        int level = c.getLevel();
        int position = c.getPosition();
        return getPlace(level + 1, position + 1);
    }

    public Place getSouthwestNeighbor(Place c) {
        if (c == null) {
            return null;
        }
        int level = c.getLevel();
        int position = c.getPosition();
        return getPlace(level + 1, position);
    }

    /**
     * Returns a place of the board.
     * @param level     Level of the desired place.
     * @param position  Position of the desired place.
     * @return A place or null if the level or position are wrong (don't exist 
     * on the board).
     */
    public Place getPlace(int level, int position) {
        for (Place c : places) {
            if (c.getLevel() == level && c.getPosition() == position) {
                return c;
            }
        }
        return null;
    }

    public int getInitialNumberOfEmptyPlaces() {
        return initialNumberOfEmptyPlaces;
    }

    public int getNumberOfLevels() {
        return numberOfLevels;
    }
    
    /**
     * Returns a string which represents the current board state. The occupied 
     * places are represented by the digit one and the empty places are 
     * represented by the digit zero. The first digit represents the first place 
     * from level zero, the second digit represents the first place from level 1, 
     * the third digit represents the second place from level 1, the forth digit 
     * represents the first place from level 2 and so on. For instance, to the 
     * board represented below, the returned string will be 1011111011.
     * <code>
     *    1
     *   0 1
     *  1 1 1
     * 1 0 1 1
     * </code>
     * @return
     */
    public String getBinaryRepresentation(){
        String representation = "";
        for(Place c : places){
            if(c.isOccupied()){
                representation += 1;
            }
            else{
                representation += 0;
            }
        }
        return representation;
    }
    
    public int getNumberOfOccupiedPlaces(){
        int number = places.stream()
                .filter(p -> p.isOccupied())
                .mapToInt(item -> 1)
                .sum();
        return number;
    }
    
    public boolean isEndOfGame(){
        return getAvailableActions().isEmpty();
    }

    @Override
    public List<String> getAvailableActions() {
        return getValidMoves2();
    }

    @Override
    public String getCurrentState() {
        return getBinaryRepresentation();
    }

    @Override
    public Double performAction(String action) {
        boolean validMove = movePiece(action);
        if(!validMove)
            return null;
        int occupiedPlaces = getNumberOfOccupiedPlaces();
        if(occupiedPlaces == 1){
            //System.out.println("It Hits!!");
            return 100.0;
        }
        else if(isInTerminalState() && occupiedPlaces != 1)
            return -100.0;
        return 1.0;
    }

    @Override
    public void reset() {
        //Varys the inital number of empty places in order to promote 
        //the exploring starts
        int n = 1 + rand.nextInt(getPlaces().size() - 2);
        setInitialNumberOfEmptyPlaces(n);
        startGame();
    }

    @Override
    public boolean isInTerminalState() {
        return isEndOfGame();
    }

    @Override
    public List<String> getStateActions(String state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
