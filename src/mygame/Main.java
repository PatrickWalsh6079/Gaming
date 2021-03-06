package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;



/** Sample 10 - How to create fast-rendering terrains from heightmaps,
and how to use texture splatting to make the terrain look good.  */
public class Main extends SimpleApplication {
    
    private AudioNode audio_gun;
    private AudioNode audio_music;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    /** Prepare the Physics Application State (jBullet) */
    private BulletAppState bulletAppState;

    /** Prepare Materials */
    Material wall_mat;
    Material stone_mat;
    Material floor_mat;

    /** Prepare geometries and physical nodes for bricks and cannon balls. */
    private RigidBodyControl brick_phy;
    private static final Box box;
    private RigidBodyControl ball_phy;
    private static final Sphere sphere;
    private RigidBodyControl floor_phy;
    private static final Box floor;

    /** dimensions used for bricks and wall */
    private static final float brickLength = 0.48f;
    private static final float brickWidth  = 0.24f;
    private static final float brickHeight = 0.12f;
    
    // Text on screen with shot counter
    BitmapText hudText;
    
            
    // Keep track of the number of shots fired
    private int shotNum = 0;

    static {
        /** Initialize the cannon ball geometry */
        sphere = new Sphere(32, 32, 0.4f, true, false);
        sphere.setTextureMode(TextureMode.Projected);
        /** Initialize the brick geometry */
        box = new Box(brickLength, brickHeight, brickWidth);
        box.scaleTextureCoordinates(new Vector2f(1f, .5f));
        /** Initialize the floor geometry */
        floor = new Box(10f, 0.1f, 5f);
        floor.scaleTextureCoordinates(new Vector2f(3, 6));
    }

    @Override
    public void simpleInitApp() {
        // Clear text on window
        setDisplayStatView(false); setDisplayFps(false);
        
        hudText = new BitmapText(guiFont, false);
        hudText.setSize(0.5f);  // font size
        hudText.setColor(ColorRGBA.Blue);  // font color
        hudText.setText("SHOTS FIRED: " + shotNum + "\n(left click to fire)");
        hudText.setLocalTranslation(-1, 5.2f, 5);  // position
        guiNode.attachChild(hudText);
        rootNode.attachChild(hudText);
        
        flyCam.setMoveSpeed(50);
        
        
        /** custom init methods, see below */
        initKeys();
        initAudio();
        
        
        /** Set up Physics Game */
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
//        bulletAppState.getPhysicsSpace().enableDebug(assetManager);

        /** Configure cam to look at scene */
        cam.setLocation(new Vector3f(0, 4f, 12f));
        cam.lookAt(new Vector3f(2, 2, 0), Vector3f.UNIT_Y);
        /** Add InputManager action: Left click triggers shooting. */
        inputManager.addMapping("shoot",
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "shoot");
        
        /** Initialize the scene, materials, and physics space */
        initMaterials();
        initWall(4);  // (int position)
        initWall(1);  // (int position)
        initWall(-2);  // (int position)
        initFloor(0, -0.1f, 0);  // (int x, float y, int z)
        initCrossHairs();
    }
    
    /** We create two audio nodes. */
    private void initAudio() {
        /* gun shot sound is to be triggered by a mouse click. */
        audio_gun = new AudioNode(assetManager, "Sound/Effects/Gun.wav", DataType.Buffer);
        audio_gun.setPositional(false);
        audio_gun.setLooping(false);
        audio_gun.setVolume(2);
        rootNode.attachChild(audio_gun);

        /* nature sound - keeps playing in a loop. */
        audio_music = new AudioNode(assetManager, "Sounds/Action_Movie.wav", DataType.Stream);
        audio_music.setLooping(true);  // activate continuous playing
        audio_music.setPositional(true);
        audio_music.setVolume(3);
        rootNode.attachChild(audio_music);
        audio_music.play(); // play continuously!
    }

    /** Declaring "Shoot" action, mapping it to a trigger (mouse left click). */
    private void initKeys() {
        inputManager.addMapping("Shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "Shoot");
    }



    /** Move the listener with the a camera - for 3D audio.
     * @param tpf */
    @Override
    public void simpleUpdate(float tpf) {
        listener.setLocation(cam.getLocation());
        listener.setRotation(cam.getRotation());
        hudText.setText("SHOTS FIRED: " + shotNum + "\n(left click to fire)");
    }
    
    /**
    * Every time the shoot action is triggered, a new cannon ball is produced.
    * The ball is set up to fly from the camera position in the camera direction.
    */
   private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("shoot") && !keyPressed) {
                audio_gun.playInstance(); // play each instance once!
                makeCannonBall();
                shotNum ++;
            }
        }
   };
   

   /** Initialize the materials used in this scene. */
   public void initMaterials() {
        wall_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key = new TextureKey("Textures/Terrain/BrickWall/BrickWall.jpg");
        key.setGenerateMips(true);
        Texture tex = assetManager.loadTexture(key);
        wall_mat.setTexture("ColorMap", tex);

        stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key2 = new TextureKey("Textures/Terrain/Rock/Rock.PNG");
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        stone_mat.setTexture("ColorMap", tex2);

        floor_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key3 = new TextureKey("Textures/Terrain/Pond/Pond.jpg");
        key3.setGenerateMips(true);
        Texture tex3 = assetManager.loadTexture(key3);
        tex3.setWrap(WrapMode.Repeat);
        floor_mat.setTexture("ColorMap", tex3);
   }

   /** Make a solid floor and add it to the scene.
     * @param x
     * @param y
     * @param z */
   public void initFloor(int x, float y, int z) {
        Geometry floor_geo = new Geometry("Floor", floor);
        floor_geo.setMaterial(floor_mat);
        floor_geo.setLocalTranslation(x, y, z);  
        this.rootNode.attachChild(floor_geo);
        /* Make the floor physical with mass 0.0f! */
        floor_phy = new RigidBodyControl(0.0f);
        floor_geo.addControl(floor_phy);
        bulletAppState.getPhysicsSpace().add(floor_phy);
   }

   /** This loop builds a wall out of individual bricks.
     * @param position */
   public void initWall(int position) {
        float startpt = brickLength / 4;
        float height = 0;
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 6; i++) {
                Vector3f vt = new Vector3f(i * brickLength * 2 + startpt, brickHeight + height, position);
                makeBrick(vt);
            }
            startpt = -startpt;
            height += 2 * brickHeight;
        }
   }
   

   /** This method creates one individual physical brick.
     * @param loc */
   public void makeBrick(Vector3f loc) {
        /** Create a brick geometry and attach to scene graph. */
        Geometry brick_geo = new Geometry("brick", box);
        brick_geo.setMaterial(wall_mat);
        rootNode.attachChild(brick_geo);
        /** Position the brick geometry  */
        brick_geo.setLocalTranslation(loc);
        /** Make brick physical with a mass > 0.0f. */
        brick_phy = new RigidBodyControl(2f);
        /** Add physical brick to physics space. */
        brick_geo.addControl(brick_phy);
        bulletAppState.getPhysicsSpace().add(brick_phy);
   }

   /** This method creates one individual physical cannon ball.
    * By default, the ball is accelerated and flies
    * from the camera position in the camera direction.*/
    public void makeCannonBall() {
        /** Create a cannon ball geometry and attach to scene graph. */
        Geometry ball_geo = new Geometry("cannon ball", sphere);
        ball_geo.setMaterial(stone_mat);
        rootNode.attachChild(ball_geo);
        /** Position the cannon ball  */
        ball_geo.setLocalTranslation(cam.getLocation());
        /** Make the ball physcial with a mass > 0.0f */
        ball_phy = new RigidBodyControl(1f);
        /** Add physical ball to physics space. */
        ball_geo.addControl(ball_phy);
        bulletAppState.getPhysicsSpace().add(ball_phy);
        /** Accelerate the physcial ball to shoot it. */
        ball_phy.setLinearVelocity(cam.getDirection().mult(60));  // speed determined by mult(int)
   }

   /** A plus sign used as crosshairs to help the player with aiming.*/
   protected void initCrossHairs() {
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+");        // fake crosshairs :)
        ch.setLocalTranslation( // center
          settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
          settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
      }
}
