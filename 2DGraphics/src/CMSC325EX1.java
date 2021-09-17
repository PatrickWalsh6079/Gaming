/*
 * Filename: CMSC325EX1.java
 * Author: Patrick Walsh
 * Date: 8/20/2021
 * Purpose: Creates graphics shapes used to draw PacMan and
 * Friends.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Line2D;
import javax.swing.JPanel;

public class CMSC325EX1 extends JPanel {
	
	// Java graphics X=0 and Y=0 is located at top left corner of screen
	
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Call methods to draw
        pacManHead(g);
        pacManMouth(g);
        pacManEyes(g);
        ghostHead(g);
        ghostEyes(g);
        ghostMouth(g);
    }
    
 // Method to draw an Ellipse
    private void pacManMouth(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;       
        Ellipse2D e = new Ellipse2D.Double(225, 130, 22, 90);  // (X, Y, width, height)
        
        g2d.setColor(Color.black);
        g2d.fill(e);
        // draw triangular part of mouth:
        g2d.fillPolygon(new int[] {238, 170, 238}, new int[] {130, 170, 220}, 3);  // ({set X values}, {set of Y values}, length of array)
        g2d.draw(e);

    }
    
    
    // Method to draw a Circle
    private void pacManHead(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;   
        Ellipse2D head = new Ellipse2D.Double(45, 75, 200, 200);   // (X, Y, width, height)
        Ellipse2D headOutline = new Ellipse2D.Double(44, 74, 202, 202);   // (X, Y, width, height)
        
        g2d.setColor(Color.black);
        g2d.draw(headOutline);
        
        g2d.setColor(Color.yellow);
        g2d.fill(head);
        g2d.draw(head);
     
    }
    
    // Method to draw a Rectangle
    private void pacManEyes(Graphics g) {

       Graphics2D g2d = (Graphics2D) g;   
       Rectangle2D eyes = new Rectangle2D.Double(150, 110, 10, 10);  // (X, Y, width, height)
        
       g2d.setColor(Color.blue);
       g2d.fill(eyes);
       g2d.draw(eyes);     
    }
    
    // Method to draw a Circle
    private void ghostHead(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;   
        Ellipse2D head = new Ellipse2D.Double(275, 145, 50, 70);   // (X, Y, width, height)
        Ellipse2D bottomHead1 = new Ellipse2D.Double(270, 200, 21, 20);   // (X, Y, width, height)
        Ellipse2D bottomHead2 = new Ellipse2D.Double(290, 200, 21, 20);   // (X, Y, width, height)
        Ellipse2D bottomHead3 = new Ellipse2D.Double(310, 200, 21, 20);   // (X, Y, width, height)
        Ellipse2D headOutline = new Ellipse2D.Double(274, 144, 52, 72);   // (X, Y, width, height)
        
        
        g2d.setColor(Color.green);
        g2d.fill(head);
        g2d.draw(head);
        
        g2d.setColor(Color.black);
        g2d.draw(headOutline);
        g2d.fill(bottomHead1);
        g2d.draw(bottomHead1);
        g2d.fill(bottomHead2);
        g2d.draw(bottomHead2);
        g2d.fill(bottomHead3);
        g2d.draw(bottomHead3);
        
     
    }
    
    // Method to draw a Rectangle
    private void ghostEyes(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;   
        Rectangle2D eyeLeft = new Rectangle2D.Double(290, 157, 5, 5);  // (X, Y, width, height)
        Rectangle2D eyeRight = new Rectangle2D.Double(305, 157, 5, 5);  // (X, Y, width, height)
        
        g2d.setColor(Color.red);
        g2d.fill(eyeLeft);
        g2d.draw(eyeLeft);
        g2d.fill(eyeRight);
        g2d.draw(eyeRight);
    }
     
    // Method to draw a Line
    private void ghostMouth(Graphics g) {

         Graphics2D g2d = (Graphics2D) g;   
         Line2D mouth = new Line2D.Double(290, 180, 310, 180);  // (xP1, yP1, xP2, yP2)  
         g2d.setColor(Color.red);
         g2d.draw(mouth);     
     }
    
     
    
}
