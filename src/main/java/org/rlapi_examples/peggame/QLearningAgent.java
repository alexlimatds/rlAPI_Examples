package org.rlapi_examples.peggame;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.rlapi.EGreedyPolicy;
import org.rlapi.Policy;
import org.rlapi.QLearning;
import org.rlapi.TableBuilderInMemory;

/**
 * QLearning agent for a peg game.
 * @author Alexandre Lima
 */
public class QLearningAgent {
    public static void main(String[] args) throws IOException {
        NumberOfLevels numberOfLevels = NumberOfLevels.SIX;
        int numberOfIterations = 2_500_000;
        double alpha = 0.8;
        double gamma = 0.9;
        double epsilon = 0.20;
        
        PegGame game = new PegGame(numberOfLevels);
        QLearning agent = new QLearning(game, new TableBuilderInMemory());
        Policy eGreedyPolicy = new EGreedyPolicy(epsilon, game, agent.getActionValueTable());
        
        System.out.print("Training agent...");
        long startTime = System.currentTimeMillis();
        
        game.setInitialNumberOfEmptyPlaces(1);
        Map<String, String> policy = agent.train(numberOfIterations, alpha, gamma, eGreedyPolicy);
        long endTime = System.currentTimeMillis();
        long trainningTime = (endTime - startTime) / 1000;
        System.out.println(" OK. " + trainningTime + " seconds.");
        
        System.out.print("Running agent...");
        game.setInitialNumberOfEmptyPlaces(1);
        int numberOfMatches = 100000;
        int solvedMatches = 0;
        for(int i = 1; i  <= numberOfMatches; i++){
            game.startGame();
            while(!game.isEndOfGame()){
                String action = policy.get(game.getCurrentState());
                game.movePiece(action);
            }
            if(game.getNumberOfOccupiedPlaces() == 1)
                solvedMatches++;
        }
        double successRate = solvedMatches / (double)numberOfMatches;
        System.out.println("OK!");
        
        SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd_HHmmss");
        DecimalFormat decFmt = new DecimalFormat("#0%");
        String fileName = 
                "policy-"+ numberOfLevels.number +"levels-"+ 
                dateFmt.format(new Date()) +".txt";
        String appendix = 
                "Number of levels: " + numberOfLevels + 
                "\nNumber of episodes: " + numberOfIterations + 
                "\nNumber of matches: " + numberOfMatches +
                "\nRate of success: " + decFmt.format(successRate);
        Util.writePolicy("./src/main/resources/" + fileName, policy, appendix);
        
        System.out.println("Solved matches: " + solvedMatches);
        System.out.println("Success rate: " + decFmt.format(successRate));
    }
}
