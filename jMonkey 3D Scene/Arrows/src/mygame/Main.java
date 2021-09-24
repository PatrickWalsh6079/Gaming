/*
Project: Arrows
Author: Patrick Walsh
Date: 9/23/2021
Description: Game loads two "ships" represented as simple blue and red
boxes with an arrow object pointing from the blue ship down towards the 
red ship. When the user presses the Space bar, the arrow fires from the blue ship
towards the red ship. The arrow stop when it hits the red ship. The user
can then press 'R' to reset/reload the arrow. The y coordinate of the arrow
is printed in the output window.
*/


package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    
    /*
    Main method. Initializes the app.
    */
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    
    protected Node arrow;  // arrow Node that attaches to the arrow components.
    private boolean fired = false;  // tells game when arrow has been fired.
    
    
    
    /*
    Populates the scene graph.
    */
    @Override
    public void simpleInitApp() {
        // Ship 1
        Box b = new Box(10, 1, 1);  // Box(int x, int y, int z)
        Geometry ship1 = new Geometry("Box", b);
        Material blueMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        blueMat.setColor("Color", ColorRGBA.Blue);
        ship1.setLocalTranslation(0.0f,5.0f,-5.0f);  // setLocalTranslation(float x, float y, float z)
        ship1.setMaterial(blueMat);
        
        // Ship 2
        Geometry ship2 = new Geometry("Box", b);
        Material redMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        redMat.setColor("Color", ColorRGBA.Red);
        ship2.setLocalTranslation(0.0f,-5.0f,-5.0f);  // setLocalTranslation(float x, float y, float z)
        ship2.setMaterial(redMat);
        
        // arrow shaft
        Cylinder c = new Cylinder(15, 15, 0.2f, 3.0f);  // Cylinder(int axisSamples, int radialSamples, float radius, float height)
        Geometry arrowShaft = new Geometry("Cylinder", c);
        Material brownMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        brownMat.setColor("Color", ColorRGBA.Brown);
        arrowShaft.setLocalTranslation(0.0f,2.5f,-5.0f);  // setLocalTranslation(float x, float y, float z)
        arrowShaft.rotate(90*FastMath.DEG_TO_RAD, 0.0f, 0.0f);  // rotate(x, y, z)
        arrowShaft.setMaterial(brownMat);
        
        // arrow head
        Dome d = new Dome(Vector3f.ZERO, 2, 32, 0.5f,false); // Dome(Vector3f center, int planes, int radialSamples, float radius, boolean insideView)
        Geometry arrowHead = new Geometry("Dome", d);
        Material grayMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        grayMat.setColor("Color", ColorRGBA.Gray);
        arrowHead.setLocalTranslation(0.0f,1.0f,-5.0f);  // setLocalTranslation(float x, float y, float z)
        arrowHead.rotate(180*FastMath.DEG_TO_RAD, 0.0f, 0.0f);  // rotate(float x, float y, float z)
        arrowHead.setMaterial(grayMat);
        
        // arrow fletching
        Geometry arrowFletch = new Geometry("Dome", d);
        Material whiteMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        whiteMat.setColor("Color", ColorRGBA.White);
        arrowFletch.setLocalTranslation(0.0f,4.0f,-5.0f);  // setLocalTranslation(float x, float y, float z)
        arrowFletch.rotate(180*FastMath.DEG_TO_RAD, 0.0f, 0.0f);  // rotate(float x, float y, float z)
        arrowFletch.setMaterial(whiteMat);
        
        
        /** Create a pivot node at (0,0,0) and attach it to the root node */
        arrow = new Node("pivot");
        rootNode.attachChild(arrow); // put this node in the scene
        
        rootNode.attachChild(ship1);
        rootNode.attachChild(ship2);
        arrow.attachChild(arrowShaft);
        arrow.attachChild(arrowHead);
        arrow.attachChild(arrowFletch);
        
        initKeys(); // load my custom keybinding
    }
    
    
    /**
     * Custom Keybinding: Maps 'R' key and 'SPACE' key.
     */
    private void initKeys() {
        // You can map one or several inputs to one named action
        inputManager.addMapping("Reload", new KeyTrigger(KeyInput.KEY_R));
        inputManager.addMapping("Fire", new KeyTrigger(KeyInput.KEY_SPACE));
        // Add the names to the action listener.
        inputManager.addListener(actionListener, "Reload");
//        inputManager.addListener(analogListener, "Fire");
        inputManager.addListener(actionListener, "Fire");

    }
    
    
    /*
    ActionListener to handle key presses.
    */
    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Fire") && !keyPressed) {
                System.out.println("FIRED!");
                fired = true;
            } else if (name.equals("Reload") && !keyPressed){
                fired = false;
                arrow.setLocalTranslation(0.0f,0.0f,0.0f);
            }
        }
    };
    
    
    
    
    /* 
    Use the main event loop to trigger repeating actions. 
    */
    @Override
    public void simpleUpdate(float tpf) {
        // 
        if (fired){
            Vector3f v = arrow.getLocalTranslation();
            System.out.println("arrow y loc: " + v.y);
            if (v.y > -4.7){
                arrow.setLocalTranslation(0.0f, v.y - 0.01f, 0.0f);  // setLocalTranslation(x, y, z)
            }
            
        }
    }

 
}
