package dynamicLoad;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture2D;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;

/**
 * test
 * @author Hamza ABED 2014
 * hamza.abed.professionel@gmail.com
 */
public class Main extends SimpleApplication implements AnalogListener,AnimEventListener{

    
    
  private AnimChannel channel;
  private AnimControl control;
  private CharacterControl player;
  private  Node sinbadPlayer;
  private Vector3f lightDirection=new Vector3f(-4,-1,5);
  private Spatial sceneModel;
  private BulletAppState bulletAppState;
  private RigidBodyControl landscape;
  private boolean left = false, right = false, up = false, down = false;
  private Vector3f camDir  = new Vector3f();
  private Vector3f camLeft = new Vector3f();
  private Vector3f walkDirection = new Vector3f();
  
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    private Quad quad;
    private WaterFilter water;

    @Override
    public void simpleInitApp() {
        
    bulletAppState = new BulletAppState(); //Ceci c'est pour spécifier 
    stateManager.attach(bulletAppState); //qu'on va travailler avec des physics
    
        initLight();
        initScene();
        
        setUpKeys();
        viewPort.setBackgroundColor(new ColorRGBA(0.7f,0.8f,1f,1f));
        flyCam.setMoveSpeed(100);
        cam.setLocation(lightDirection);
        sinbadPlayer =(Node) assetManager.loadModel("Models/high-sinbad/Sinbad.mesh.xml");
        sinbadPlayer.setLocalScale(1f);
        rootNode.attachChild(sinbadPlayer);
        sinbadPlayer.move(11, 1, 1); 
        control = sinbadPlayer.getControl(AnimControl.class);
        control.addListener((AnimEventListener) this);
    channel = control.createChannel();
    channel.setAnim("idle");
  //  channel.reset(true); // this stop the animation
      
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(30);
        player.setPhysicsLocation(new Vector3f(0, 10, 0));
        
        CollisionShape sceneShape =
        CollisionShapeFactory.createMeshShape((Node) sceneModel);
        landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);  //define the scene as a rigid body
        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(player);
        
        afficherTexte("Version d'essaie");
      //  initSimpleWater();initPPcWater();
    }
    
    private BitmapText afficherTexte(String txt)
    {
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Orbitron.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText(txt);
        helloText.setLocalTranslation(300, helloText.getLineHeight(), 0);
        guiNode.attachChild(helloText);
        return helloText;
    }
    
     public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
    if (animName.equals("walk")) {
      channel.setAnim("idle");
      
    }
  }
    
     
     
    
      
     public void onAnalog(String binding, float isPressed, float tpf) {
    if (binding.equals("Left")) {
      left = true;
    } else if (binding.equals("Right")) {
      right= true;
    } else if (binding.equals("Up")) {
      up = true;
    } else if (binding.equals("Down")) {
      down = true;
    } else if (binding.equals("Jump")) {
       player.jump(); 
    }
    
   System.out.println("walk "+ isPressed +" "+ binding);
        if (up && !channel.getAnimationName().equals("walk")) {
          channel.setAnim("walk",0.5f);
          channel.setLoopMode(LoopMode.DontLoop);
        }
        if(!up) channel.setAnim("idle");
    }
  
     
     
     
     private Vector3f lastPostion=new Vector3f(0,0,0);

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        // cam.setLocation(new Vector3f(0, 10, 0) );
        // if(!up) channel.setAnim("idle");
         
         camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);
       
        if (left) {
            walkDirection.addLocal(camLeft);
            left=false;
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
            right=false;
        }
        if (up) {
            walkDirection.addLocal(camDir);
            up=false;
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
            down=false;
        }
        
        player.setWalkDirection(walkDirection);
        
        sinbadPlayer.setLocalRotation(cam.getRotation());
        //sinbadPlayer.setLocalRotation(new Quaternion(cam.getRotation().getW(),0,90,0));
        System.out.println("cam : "+cam.toString());
        //cam.setLocation(player.getPhysicsLocation());
        cam.setLocation(new Vector3f(player.getPhysicsLocation().x,
                player.getPhysicsLocation().y+15,
                player.getPhysicsLocation().z
                ));
        
        /// modifaction de la la position du player 
        sinbadPlayer.setLocalTranslation(new Vector3f(
                player.getPhysicsLocation().x+10, player.getPhysicsLocation().y,
                player.getPhysicsLocation().z+10));
        
       
      
        
         lastPostion=player.getPhysicsLocation();
         
    }
    
    /** We over-write some navigational key mappings here, so we can
   * add physics-controlled walking and jumping: */
  private void setUpKeys() {
    inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
    inputManager.addListener(this, "Left");
    inputManager.addListener(this, "Right");
    inputManager.addListener(this, "Up");
    inputManager.addListener(this, "Down");
    inputManager.addListener(this, "Jump");
  }
    
    
    
    
    
     private void initLight()
    {
           /** A white ambient light source. */ 
    AmbientLight ambient = new AmbientLight();
    ambient.setColor(ColorRGBA.White);
    rootNode.addLight(ambient); 
    
    DirectionalLight dl = new DirectionalLight();
    dl.setColor(ColorRGBA.White);
    dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
    rootNode.addLight(dl);
    }
     
     
   private void initScene()
   {
        sceneModel=assetManager.loadModel("Scenes/scene1.j3o");
        rootNode.attachChild(sceneModel);          
    
   }

    @Override
    public void simpleRender(RenderManager rm) {
        
        
        //TODO: add render code
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    
    
    
    
    
    /// ABOUT WATER
   public void initSimpleWater(){ 
       SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
       
    waterProcessor.setReflectionScene(sceneModel); 
    Vector3f waterLocation=new Vector3f(0,-6,0); 
    waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
    viewPort.addProcessor(waterProcessor); waterProcessor.setWaterDepth(10); // transparency of water 
    waterProcessor.setDistortionScale(0.05f); // strength of waves 
    waterProcessor.setWaveSpeed(0.05f); // speed of waves Quad 
    quad = new Quad(800,800); 
    quad.scaleTextureCoordinates(new Vector2f(6f,6f)); 
    Geometry water=new Geometry("water", quad); 
    water.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X)); 
    water.setLocalTranslation(-400, 0.32f, 400); 
    water.setShadowMode(RenderQueue.ShadowMode.Receive); 
    water.setMaterial(waterProcessor.getMaterial()); 
    rootNode.attachChild(water); } 
   
   public void initPPcWater(){ 
       FilterPostProcessor fpp = new FilterPostProcessor(assetManager); 
       water = new WaterFilter(rootNode, lightDirection); 
       water.setCenter(Vector3f.ZERO); 
       water.setRadius(2600); 
       water.setWaveScale(0.003f); 
       water.setMaxAmplitude(2f); 
       water.setFoamExistence(new Vector3f(1f, 4f, 0.5f)); 
       water.setFoamTexture((Texture2D) assetManager.loadTexture("Common/MatDefs/Water/Textures/foam2.jpg")); 
       water.setRefractionStrength(0.2f); 
       water.setWaterHeight(0.002f); fpp.addFilter(water); 
       viewPort.addProcessor(fpp); }
    
   

}