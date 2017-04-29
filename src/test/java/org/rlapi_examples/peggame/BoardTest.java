package org.rlapi_examples.peggame;

import java.util.Scanner;
import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Alexandre Lima
 */
public class BoardTest {
    
    @Test
    public void testGetNumberOfOccupiedPlaces_1(){
        PegGame game = new PegGame(NumberOfLevels.FIVE);
        Assert.assertEquals(game.getPlaces().size() - 1, 
                game.getNumberOfOccupiedPlaces());
    }
    
    @Test
    public void testGetNumberOfOccupiedPlaces_2(){
        PegGame game = new PegGame(NumberOfLevels.FIVE);
        game.setBoardState("011000110000000");
        Assert.assertEquals(4, 
                game.getNumberOfOccupiedPlaces());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetBoardState_1(){
        //Validates precondition checking
        PegGame game = new PegGame(NumberOfLevels.FIVE); //15 places
        String input = "00110110001101";
        
        game.setBoardState(input);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetBoardState_2(){
        //Validates character checking
        PegGame game = new PegGame(NumberOfLevels.FIVE); //15 places
        String input = "001101120001101";
        
        game.setBoardState(input);
    }
    
    @Test
    public void testStartGame_1(){
        //Checks the initial amount of empty places
        PegGame game = new PegGame(NumberOfLevels.FIVE);
        game.startGame();
        long emptyCount = countEmptyPlaces(game);
        Assert.assertEquals(1, emptyCount);
    }
    
    @Test
    public void testStartGame_2(){
        //Checks the initial amount of empty places
        PegGame game = new PegGame(NumberOfLevels.FIVE);
        int correctEmptyCount = 4;
        game.setInitialNumberOfEmptyPlaces(correctEmptyCount);
        game.startGame();
        long emptyCount = countEmptyPlaces(game);
        Assert.assertEquals(correctEmptyCount, emptyCount);
    }
    
    @Test
    public void testIsInTerminalState_1(){
        PegGame game = new PegGame(NumberOfLevels.FIVE);
        game.setBoardState("101001000100001");
        Assert.assertTrue(game.isInTerminalState());
    }
    
    @Test
    public void testIsInTerminalState_2(){
        PegGame game = new PegGame(NumberOfLevels.FIVE);
        game.setBoardState("111000000000000");
        Assert.assertFalse(game.isInTerminalState());
    }
    
    private long countEmptyPlaces(PegGame game){
        return game.getPlaces().stream()
                .filter(p -> !p.isOccupied())
                .count();
    }
    
    public static void main(String[] args) {
        PegGame game = new PegGame(NumberOfLevels.FIVE);
        Scanner scan = new Scanner(System.in);
        System.out.println(game.print2());
        while (true) {
            System.out.print("Piece to move (line): ");
            int lineP = scan.nextInt();
            System.out.print("\nPiece to move (column): ");
            int colP = scan.nextInt();

            System.out.print("\nTarget place (line): ");
            int lineT = scan.nextInt();
            System.out.print("\nTarget place (column): ");
            int colT = scan.nextInt();
            boolean m = game.movePiece(lineP, colP, lineT, colT);
            System.out.println("\nMove: " + m);
            System.out.println(game.print2());
        }
    }
}