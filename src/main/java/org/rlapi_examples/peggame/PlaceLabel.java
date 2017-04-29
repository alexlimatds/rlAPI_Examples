package org.rlapi_examples.peggame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;

/**
 * UI component which represents a place in the board of a peg game.
 * @author Alexandre Lima
 */
public class PlaceLabel extends JLabel{
    
    private final Place place;
    private boolean selected;
    
    public PlaceLabel(Place c){
        super();
        this.place = c;
        setBackground(Color.BLUE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        //draws the piece
        if(getPlace().isOccupied()){
            g2.setColor(Color.RED);
        }
        else{
            g2.setColor(Color.WHITE);
        }
        g2.fillOval(1, 1, 49, 49);
        g2.setColor(Color.BLACK);
        g2.drawOval(1, 1, 49, 49);
        
        g2.dispose();
    }

    public Place getPlace() {
        return place;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        setOpaque(selected);
        repaint();
    }
    
}