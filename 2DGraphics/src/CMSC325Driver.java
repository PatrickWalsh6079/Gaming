/*
 * Filename: CMSC325Driver.java
 * Author: Patrick Walsh
 * Date: 8/20/2021
 * Purpose: Creates window and draws PacMan and Friends using simple
 * 2D Java graphics.
 */

import java.awt.Color;
import javax.swing.JFrame;

public class CMSC325Driver extends JFrame {


	public CMSC325Driver() {

		// Construct Class with Graphics Component
		CMSC325EX1 myExample = new CMSC325EX1();
		myExample.setBackground(Color.black);  // set background color
		// Add to JFrame
		add(myExample);
		// Set the Default Size and title
		setSize(400, 400);
//		setBackground(Color.black);
        setTitle("Pac Man and Friends!");
        
        // Frame Default to be able to close
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         // Center the Frame
         setLocationRelativeTo(null);		       
    }

    
    public static void main(String[] args) {   	
    	
        CMSC325Driver myDriver = new CMSC325Driver();
//        myDriver.getContentPane().setBackground(Color.black);
        myDriver.setVisible(true);
                 
    }
}
