package org.rlapi_examples.peggame;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Panel that encapsulates and shows a peg game.
 * @author  Alexandre Lima
 */
public class PnlDynamicPegGame extends javax.swing.JPanel{
    
    private NumberOfLevels numberOfLevels;
    private int timeStep = 1000; //autopilot's delay
    private PegGame game;
    private PlaceLabel[] labels;
    private PlaceLabelListener listener;
    private PlaceLabel selectedPlace;
    
    public PnlDynamicPegGame(){
        this(NumberOfLevels.FIVE);
    }
    
    public PnlDynamicPegGame(NumberOfLevels numberOfLevels) {
        this.numberOfLevels = numberOfLevels;
        game = new PegGame(numberOfLevels);
        listener = new PlaceLabelListener();
        initComponents();
        buildBoard();
    }
    
    public void newGame(){
        getGame().startGame();
        repaint();
    }
    
    /**
     * Starts the autopilot. That means the computer starts playing by itself 
     * against the current board game. The moves are selected according to the 
     * policy generated by the reinforcement learning algorithm.
     */
    public void startAutopilot(){
        try{
            final Map<String, String> policy;
            if(numberOfLevels.number == 5){
                policy = Util.readPolicy("peggame/policy-5levels.txt");
            }
            else if(numberOfLevels.number == 6){
                policy = Util.readPolicy("peggame/policy-6levels.txt");
            }
            else{
                throw new IllegalStateException("There is not a policy for the current level");
            }
            final JPanel panel = this;
            Timer timer = new Timer(timeStep, new ActionListener() {
                private PlaceLabel sourcePlace;
                @Override
                public void actionPerformed(ActionEvent e) {
                    String state = game.getBinaryRepresentation();
                    String action = policy.get(state);
                    if(action == null)
                        ((Timer)e.getSource()).stop();
                    else{
                        if(sourcePlace == null){ //selection estep
                            int indexSpace = action.indexOf(" ");
                            int numberOfSourcePlace = Integer.parseInt( action.substring(0, indexSpace) ) - 1;
                            sourcePlace = labels[numberOfSourcePlace];
                            sourcePlace.setSelected(true);
                        }
                        else{ //move step
                            game.movePiece(action);
                            panel.repaint();
                            sourcePlace.setSelected(false);
                            sourcePlace = null;
                        }
                    }
                }
            });
            timer.start();
        }
        catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //Creates and places the components that represent the places of a peg game's board
    private void buildBoard(){
        //Calculates the size of this panel
        //60 = 30 (right margin) + 30 (left margin)
        int w = (numberOfLevels.number - 1) * 116 + 60;
        int h = (numberOfLevels.number - 1) * 100 + 60;
        setSize(w, h);
        
        //Creates and configures the labels
        w = 52; 
        h = 52; //labels' size
        labels = new PlaceLabel[game.getPlaces().size()];
        int horizontalHalf = 58;
        int levelsDiff = 100;
        int xStart = 30 + horizontalHalf * (numberOfLevels.number - 1);
        int yStart = 30;
        int xLabel = xStart;
        int yLabel = yStart;
        int i = 0;
        for(int level = 0; level < numberOfLevels.number; level++){
            for(int col = 0; col <= level; col++){
                labels[i] = new PlaceLabel(getGame().getPlace(level, col));
                labels[i].setSize(w, h);
                labels[i].setLocation(xLabel, yLabel);
                labels[i].addMouseListener(listener);
                add(labels[i]);
                i++;
                xLabel += horizontalHalf * 2;
            }
            xStart -= horizontalHalf;
            yStart += levelsDiff;
            xLabel = xStart;
            yLabel = yStart;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Draws the lines that link the board's places
        if(labels != null){
            for(PlaceLabel lb : labels){
                List<Place> neighbors = getGame().getNeighbors(lb.getPlace());
                for(Place neighborPlace : neighbors){
                    PlaceLabel lbVizinho = getLabel(neighborPlace);
                    //Draws a line between the current label and its neighbor
                    g.drawLine(lb.getX() + lb.getWidth() / 2, lb.getY() + lb.getHeight() / 2, 
                            lbVizinho.getX() + lbVizinho.getWidth() / 2, lbVizinho.getY() + lbVizinho.getHeight() / 2);
                }
            }
        }
    }
    
    /**
     * Returns de instance of <code>PlaceLabel</code> associated to a 
     * specific game place.
     * @param c The game place asscoiated to the desired PlaceLabel.
     * @return
     */
    private PlaceLabel getLabel(Place c){
        for(PlaceLabel lb : labels){
            if(lb.getPlace() == c){
                return lb;
            }
        }
        return null;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 655, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 519, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public PegGame getGame() {
        return game;
    }

    public int getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(int timeStep) {
        this.timeStep = timeStep;
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    class PlaceLabelListener extends MouseAdapter{

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getSource() instanceof PlaceLabel){
                PlaceLabel c = (PlaceLabel)e.getSource();
                if(selectedPlace != null){ //there is a selected place already
                    getGame().movePiece(selectedPlace.getPlace(), c.getPlace());
                    selectedPlace.setSelected(false);
                    selectedPlace = null;
                }
                else{
                    c.setSelected(true);
                    selectedPlace = c;
                }
                repaint();
            }
        }
    }
    
}
