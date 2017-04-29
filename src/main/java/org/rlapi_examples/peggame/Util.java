package org.rlapi_examples.peggame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alexandre Lima
 */
public class Util {
    public static Map<String, String> readPolicy(String fileName) throws FileNotFoundException, 
            IOException{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        Map<String, String> policy = new HashMap<>();
        String line = reader.readLine();
        while(line != null && !line.trim().equals("") ){
            int indexSpace1 = line.indexOf(" ");
            String state = line.substring(0, indexSpace1);
            String action = line.substring(indexSpace1 + 1);
            policy.put(state, action);
            line = reader.readLine();
        }
        return policy;
    }
    
    public static void writePolicy(String fileName, Map<String, String> politica, 
            String appendix) throws IOException{
        try (FileWriter fWriter = new FileWriter(fileName)) {
            for (String key : politica.keySet()) {
                fWriter.write(key + " " + politica.get(key) + "\n");
            }
            fWriter.write("\n");
            fWriter.write(appendix);
        }
    }
    
    /**
     * Returns the number of empty spaces on a board game.
     * @param boardState binary representation of a board.
     * @return
     */
    public static int countEmptyPlaces(String boardState){
        int count = 0;
        for(char c : boardState.toCharArray()){
            if(c == '1'){
                count++;
            }
        }
        return count;
    }
}
