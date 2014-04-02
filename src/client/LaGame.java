package client;
import client.hud3D.MoveCursor;
import client.input.MainGameListener;
import client.map.character.Player;
import client.network.SimpleClientConnector;
import client.task.PingPongTask;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix4f;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;
import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import com.sun.sgs.client.simple.SimpleClient;
import com.sun.sgs.client.simple.SimpleClientListener;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.nio.ByteBuffer;
import java.nio.channels.UnresolvedAddressException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import shared.constants.PckCode;
import shared.pck.Pck;
import shared.variables.Variables;

/**
 * test
 * @author Hamza ABED 2014
 * hamza.abed.professionel@gmail.com
 */
public class LaGame extends SimpleApplication {

    
  private  MainGameListener gameListener;
  private final float RADIUS=9f;
  private static final float A = .35f;
  private static final ColorRGBA DISK_COLOR = new ColorRGBA(0, 0.9f, 0.9f, A);// j'ai changé 0.9f à 0
  private Vector3f lightDirection=new Vector3f(-4,-1,5);
  private Spatial sceneModel;
  private BulletAppState bulletAppState;
  private RigidBodyControl landscape;
  
  private static LaGame app;
  
  protected SimpleClient simpleClient;
  
  private Player joueur;

  
  
  public static void main(String[] args) {
    /*
     * Setting main properties for the game
     */
    app = new LaGame();
    AppSettings settings = new AppSettings(true);
    settings.setResolution(1024, 763);
    app.setShowSettings(false); // splashscreen
    app.setSettings(settings);
    app.start();
        
        /*
         * ici on doit se connecter pour commencer à recevoir les message ping pong
         * on appelle la methode connect du SimpleClientConnector
         */
    }
    private Quad quad;
    private WaterFilter water;

    public LaGame() {
        
        //SimpleClientConnector = new SimpleClientConnector();
        /*
         * On a même pas besoin d'ajouter du code ici pour connecter le serveur 
         * on se connecte que lorsque c'est essentiel,
         * alors sa sera utile pour se connecter dans le main
         */
        this.actionListener = new ActionListener() { 
public void onAction(String name, boolean keyPressed, float tpf) {
if (name.equals("addObject")) {
//Node fireCamp=(Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");
Spatial fireCamp = assetManager.loadModel("Models/campfire/campfire.j3o");
rootNode.attachChild(fireCamp);

fireCamp.setLocalTranslation(new Vector3f(joueur.getPlayer().getPhysicsLocation().x+30,joueur.getPlayer().getPhysicsLocation().y
        ,joueur.getPlayer().getPhysicsLocation().z));
System.out.println("child attached !!");

}

}
}; 
}

 
    @Override
    public void simpleInitApp() {
       
    app.setDisplayFps(false);
    app.setDisplayStatView(false);
    bulletAppState = new BulletAppState(); //Ceci c'est pour spécifier 
    stateManager.attach(bulletAppState); //qu'on va travailler avec des physics
    //le bulletAppState est un variable utilisé dans tout le jeux
    //pour ajouter des palyers
    
   initNiftyGUI();  
    
   
        initLight();
        initScene();
        
        initPlayer();
        gameListener=new MainGameListener(sceneModel);
        initCamera();
        setUpKeys();
       
      
       bulletAppState.getPhysicsSpace().enableDebug(assetManager);
      //  afficherTexte("Version d'essai");
       initSimpleWater();initPPcWater();       
      
 
      /*
       * some tries About Boussole
       */ 
        
    /** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
   
      	Cylinder disk = new Cylinder(5,32,225,0.1f,true);
       // Box box =new Box(100, 100, 100);
        
                
 Geometry laserBeam = new Geometry("laserbeam", disk);
 Material matlaser = new Material(assetManager,   "Common/MatDefs/Misc/Unshaded.j3md");
     matlaser.setTexture("ColorMap", assetManager.loadTexture(
     //   "Textures/Terrain/splat/grass.jpg"));//
     "Textures/boussole/boussole1.png"));
     
                matlaser.setColor("Color", ColorRGBA.Orange);
                matlaser.setColor("GlowColor", ColorRGBA.Red);
                
                
  //  matlaser.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
   // laserBeam.setQueueBucket(Bucket.Transparent);
    
    laserBeam.setMaterial(matlaser);
    //disk.get
      //  disk.getRotation().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
		//disk.setDefaultColor(DISK_COLOR); 
                //disk
       
       
 Picture pic = new Picture("HUD Picture");
pic.setImage(assetManager, "Textures/boussole/boussole1.png", true);
pic.setWidth(450);
pic.setHeight(450);
pic.setPosition(-225, -225);
boussole=new Node("boussole");
boussole.setLocalScale(0.25f);
boussole.move(910, 600, 0);
//boussole.attachChild(pic);
//boussole.attachChild(laserBeam);

//pic.getWorldBound().getCenter()

//pic.setPosition(boussole.getWorldBound().getCenter().getX()-pic.getWorldBound().getCenter().getX(),
  //      boussole.getWorldBound().getCenter().getY()-pic.getWorldBound().getCenter().getY());
//guiNode.attachChild(boussole);
//guiNode.getChild("boussole").setLocalTranslation(650, 500, 0);

       
    }
Node boussole;
    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    public void setBulletAppState(BulletAppState bulletAppState) {
        this.bulletAppState = bulletAppState;
    }
    ChaseCamera chaseCam;
    
    private void initCamera()
    {
         viewPort.setBackgroundColor(new ColorRGBA(0.7f,0.8f,1f,1f));
        flyCam.setMoveSpeed(150);
       // cam.setLocation(lightDirection);
       // channel.reset(true); // this stop the animation
      
        //***** DISABLING THE FLYING CAMERA  ******///
       flyCam.setEnabled(false);
       chaseCam = new ChaseCamera(cam, joueur.getPlayerModel(), inputManager);
    }
    
    
    private void initPlayer()
    {
        joueur=new  Player();
      
       // walkDirection=joueur.getWalkDirection();
        joueur.attachToScene();
        Variables.setMainPlayer(joueur);
       
       
    }    
    
    
    private BitmapText afficherTexte(String txt)
    {
        guiNode.detachAllChildren();
        guiFont = assetManager.loadFont("Interface/Fonts/Mangal.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText(txt);
        helloText.setLocalTranslation(500, helloText.getLineHeight(), 0);
        guiNode.attachChild(helloText);
        return helloText;
    }
    
    
     
     private ActionListener actionListener;
     
    
     
     
    
  
     
    
   
    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        // cam.setLocation(new Vector3f(0, 10, 0) );
        // if(!up) channel.setAnim("idle");
         
      boussole.setLocalRotation(new Quaternion(boussole.getLocalRotation().getX(),boussole.getLocalRotation().getY(),
              cam.getRotation().getZ(),boussole.getLocalRotation().getW() ));
        joueur.update();
        joueur.getPlayerModel().removeFromParent();
                joueur.getPlayerModel().setLocalTranslation(new Vector3f(
        joueur.getPlayer().getPhysicsLocation().x, joueur.getPlayer().getPhysicsLocation().y-2.5f,
        joueur.getPlayer().getPhysicsLocation().z)); 
                joueur.attachToScene(); 
        //walkDirection.set(0, 0, 0);
         
    }
    
    /** We over-write some navigational key mappings here, so we can
   * add physics-controlled walking and jumping: */
   
  private void setUpKeys() {
    inputManager.addMapping("Left", new  KeyTrigger(KeyInput.KEY_LEFT));
    inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
    inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
    inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
    inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
    inputManager.addMapping("addObject", new KeyTrigger(KeyInput.KEY_X));
    
    inputManager.addMapping("LClick", new MouseButtonTrigger(0));         // Left-button click
    inputManager.addMapping("RClick", new MouseButtonTrigger(1));         // Right-button click
    inputManager.addListener(gameListener, "Left");
    inputManager.addListener(gameListener, "Right");
    inputManager.addListener(gameListener, "Up");
    inputManager.addListener(gameListener, "Down");
    inputManager.addListener(gameListener, "Jump");
    inputManager.addListener(gameListener,"LClick");
    inputManager.addListener(actionListener, "addObject");
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
       
       
     //  assetManager.registerLocator("newScene.zip", ZipLocator.class);
   // sceneModel = assetManager.loadModel("Models/terrain2/terrain2.j3o"); sceneModel.setLocalScale(10f, 2f, 10f);
    
    rootNode.attachChild(sceneModel);
     
       
    sceneModel.setLocalTranslation(0, 2, 0);
    
      //initTerrain();
    //initSimpleWater();
     //initPPcWater();
    
     CollisionShape sceneShape =
        CollisionShapeFactory.createMeshShape((Node)sceneModel);
        landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);  //define the scene as a rigid body
        
        bulletAppState.getPhysicsSpace().add(landscape);
              
       Variables.setSceneModel(sceneModel);
    
   }
 


    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
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
    waterProcessor.setDistortionScale(0.07f); // strength of waves 
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
    
   
   
   
   private TerrainQuad terrain;
  Material mat_terrain;
   
   private void initTerrain() // essaye de charger un terrain à partir d'un height map
   {
       /** 1. Create terrain material and load four textures into it. */
    mat_terrain = new Material(assetManager, 
            "Common/MatDefs/Terrain/Terrain.j3md");
 
    /** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
    mat_terrain.setTexture("Alpha", assetManager.loadTexture(
            "Textures/Terrain/splat/alphamap.png"));
 
    /** 1.2) Add GRASS texture into the red layer (Tex1). */
    Texture grass = assetManager.loadTexture(
            "Textures/Terrain/splat/dirt.jpg");//grass
    grass.setWrap(WrapMode.Repeat);
    mat_terrain.setTexture("Tex1", grass);
    mat_terrain.setFloat("Tex1Scale", 64f);
 
    /** 1.3) Add DIRT texture into the green layer (Tex2) */
    Texture dirt = assetManager.loadTexture(
            "Textures/Terrain/splat/dirt.jpg");
    dirt.setWrap(WrapMode.Repeat);
    mat_terrain.setTexture("Tex2", dirt);
    mat_terrain.setFloat("Tex2Scale", 32f);
 
    /** 1.4) Add ROAD texture into the blue layer (Tex3) */
    Texture rock = assetManager.loadTexture(
            "Textures/Terrain/splat/dirt.jpg");//road
    rock.setWrap(WrapMode.Repeat);
    mat_terrain.setTexture("Tex3", rock);
    mat_terrain.setFloat("Tex3Scale", 128f);
 
    /** 2. Create the height map */
    AbstractHeightMap heightmap = null;
    Texture heightMapImage = assetManager.loadTexture(
            "Textures/Terrain/splat/scene4.png");
    heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
    heightmap.load();
 
    /** 3. We have prepared material and heightmap. 
     * Now we create the actual terrain:
     * 3.1) Create a TerrainQuad and name it "my terrain".
     * 3.2) A good value for terrain tiles is 64x64 -- so we supply 64+1=65.
     * 3.3) We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
     * 3.4) As LOD step scale we supply Vector3f(1,1,1).
     * 3.5) We supply the prepared heightmap itself.
     */
    int patchSize = 65;
    terrain = new TerrainQuad("my terrain", patchSize, 513, heightmap.getHeightMap());
 
    /** 4. We give the terrain its material, position & scale it, and attach it. */
    terrain.setMaterial(mat_terrain);
    terrain.setLocalTranslation(0, -100, 0);
    terrain.setLocalScale(2f, 1f, 2f);
    rootNode.attachChild(terrain);
    sceneModel=terrain;
   }
           
   public void initNiftyGUI()
   {
        
       Variables.setLaGame(this);
       Variables.setMoveCursor(new MoveCursor());
    NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
            assetManager, inputManager, audioRenderer, guiViewPort);
    Nifty nifty = niftyDisplay.getNifty();
    guiViewPort.addProcessor(niftyDisplay);
   // flyCam.setDragToRotate(true);
    
   
     /**
     * Åctivate the Nifty-JME integration: 
     */
    
    //nifty.setDebugOptionPanelColors(true);
    nifty.fromXml("Interface/int.xml", "start");
    
    //nifty.fromXml("Interface/tutorial/screen2.xml", "hud");
    
   
   }
   public void saySomething()
   {
       System.out.println("hello this is me");
   }

    public PhysicsSpace getPhysicsSpace() {
      return bulletAppState.getPhysicsSpace();
    }
   
   
   
   
   
   

}