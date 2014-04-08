package client;

import client.chat.ChatSystem;
import client.editor.ServerEditor;
import client.hud.boussole.Boussole;
import client.hud3D.MoveCursor;
import client.input.MainGameListener;
import client.interfaces.network.Sharable;
import client.map.WaterPlan;
import client.map.World;
import client.map.character.Player;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.system.AppSettings;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.ui.Picture;
import com.sun.sgs.client.simple.SimpleClient;
import de.lessvoid.nifty.Nifty;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import org.lwjgl.util.glu.Disk;
import shared.constants.PckCode;
import shared.pck.Pck;
import shared.variables.Variables;

/**
 * test
 *
 * @author Hamza ABED 2014 hamza.abed.professionel@gmail.com
 */
public class LaGame extends SimpleApplication {

    private MainGameListener gameListener;
    private final float RADIUS = 9f;
    private static final float A = .35f;
    private static final ColorRGBA DISK_COLOR = new ColorRGBA(0, 0.9f, 0.9f, A);// j'ai changé 0.9f à 0
    private Spatial sceneModel;
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private static LaGame app;
    protected SimpleClient simpleClient; //Répresente toutes communication avec le serveur
    private Player joueur; // Le joueur actuel avec son modèle graphique
    private Quaternion qPlan=new Quaternion();

    public Spatial getSceneModel() {
        return sceneModel;
    }

    public void setSceneModel(Spatial sceneModel) {
        this.sceneModel = sceneModel;
    }
    /**
     * moteur de chat
     */
    private ChatSystem chatSystem;

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

                    fireCamp.setLocalTranslation(new Vector3f(joueur.getPlayer().getPhysicsLocation().x + 30, joueur.getPlayer().getPhysicsLocation().y, joueur.getPlayer().getPhysicsLocation().z));
                    System.out.println("child attached !!");

                }

            }
        };
    }
    Boussole boussole1;

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
        gameListener = new MainGameListener(sceneModel);
        initCamera();
        setUpKeys();


        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        //  afficherTexte("Version d'essai");



        /*
         * some tries About Boussole
         */


    
    Box box = new Box(new Vector3f(0, 0, 0), 90, 90, 5);
cube = new Geometry("box1", box);
Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//mat1.setColor("Color", ColorRGBA.Blue);
mat1.setTexture("ColorMap",assetManager.loadTexture("Textures/boussole/boussole1.png"));
mat1.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
cube.setMaterial(mat1);
cube.move(850,660,2);
guiNode.attachChild(cube);

   



    }
    Geometry cube;
    Node boussole;
private static final float INCLINAISON = FastMath.HALF_PI;
    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    public void setBulletAppState(BulletAppState bulletAppState) {
        this.bulletAppState = bulletAppState;
    }
    ChaseCamera chaseCam;

    private void initCamera() {
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(150);
        // cam.setLocation(lightDirection);
        // channel.reset(true); // this stop the animation

        //***** DISABLING THE FLYING CAMERA  ******///
        flyCam.setEnabled(false);
        chaseCam = new ChaseCamera(cam, joueur.getPlayerModel(), inputManager);
    }

    private void initPlayer() {
        joueur = new Player();

        // walkDirection=joueur.getWalkDirection();
        joueur.attachToScene();
        Variables.setMainPlayer(joueur);


    }

    private BitmapText afficherTexte(String txt) {
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
Quaternion q=new Quaternion();
         
    @Override
    public void simpleUpdate(float tpf) {
        
        
       

		//q.fromAngleNormalAxis(cam.getRotation().getY(), Vector3f.UNIT_Y);
	cube.setLocalRotation(new Quaternion(cube.getLocalRotation().getX(),cube.getLocalRotation().getY(),
               cam.getRotation().getZ(),cam.getRotation().getW()));

        //TODO: add update code
        // cam.setLocation(new Vector3f(0, 10, 0) );
        // if(!up) channel.setAnim("idle");

   /*     boussole.setLocalRotation(new Quaternion(boussole.getLocalRotation().getX(), boussole.getLocalRotation().getY(),
                cam.getRotation().getZ(), boussole.getLocalRotation().getW()));
                */
        joueur.update();
        joueur.getPlayerModel().removeFromParent();
        joueur.getPlayerModel().setLocalTranslation(new Vector3f(
                joueur.getPlayer().getPhysicsLocation().x, joueur.getPlayer().getPhysicsLocation().y - 2.5f,
                joueur.getPlayer().getPhysicsLocation().z));
        joueur.attachToScene();
        //walkDirection.set(0, 0, 0);

    }

    /**
     * We over-write some navigational key mappings here, so we can add
     * physics-controlled walking and jumping:
     */
    private void setUpKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
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
        inputManager.addListener(gameListener, "LClick");
        inputManager.addListener(actionListener, "addObject");
    }

    private void initLight() {
        /**
         * A white ambient light source.
         */
        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);
    }
    
    WaterPlan waterPlan;
    private void initScene() {
        sceneModel = assetManager.loadModel("Scenes/scene1.j3o");
        rootNode.attachChild(sceneModel);
        sceneModel.setLocalTranslation(0, 2, 0);
        //initTerrain();
        //initSimpleWater();
        //initPPcWater();
        CollisionShape sceneShape =
                CollisionShapeFactory.createMeshShape((Node) sceneModel);
        landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);  //define the scene as a rigid body

        bulletAppState.getPhysicsSpace().add(landscape);

        Variables.setSceneModel(sceneModel);
        
       waterPlan=new WaterPlan(sceneModel);
       waterPlan.setWaterOnTheGame();
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private TerrainQuad terrain;
    Material mat_terrain;

    private void initTerrain() // essaye de charger un terrain à partir d'un height map
    {
        /*
         * Cette méthode n'est pas utilisée dans le jeux jusqu'à maintenant
         */
        /**
         * 1. Create terrain material and load four textures into it.
         */
        mat_terrain = new Material(assetManager,
                "Common/MatDefs/Terrain/Terrain.j3md");

        /**
         * 1.1) Add ALPHA map (for red-blue-green coded splat textures)
         */
        mat_terrain.setTexture("Alpha", assetManager.loadTexture(
                "Textures/Terrain/splat/alphamap.png"));

        /**
         * 1.2) Add GRASS texture into the red layer (Tex1).
         */
        Texture grass = assetManager.loadTexture(
                "Textures/Terrain/splat/dirt.jpg");//grass
        grass.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex1", grass);
        mat_terrain.setFloat("Tex1Scale", 64f);

        /**
         * 1.3) Add DIRT texture into the green layer (Tex2)
         */
        Texture dirt = assetManager.loadTexture(
                "Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex2", dirt);
        mat_terrain.setFloat("Tex2Scale", 32f);

        /**
         * 1.4) Add ROAD texture into the blue layer (Tex3)
         */
        Texture rock = assetManager.loadTexture(
                "Textures/Terrain/splat/dirt.jpg");//road
        rock.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex3", rock);
        mat_terrain.setFloat("Tex3Scale", 128f);

        /**
         * 2. Create the height map
         */
        AbstractHeightMap heightmap = null;
        Texture heightMapImage = assetManager.loadTexture(
                "Textures/Terrain/splat/scene4.png");
        heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();

        /**
         * 3. We have prepared material and heightmap. Now we create the actual
         * terrain: 3.1) Create a TerrainQuad and name it "my terrain". 3.2) A
         * good value for terrain tiles is 64x64 -- so we supply 64+1=65. 3.3)
         * We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
         * 3.4) As LOD step scale we supply Vector3f(1,1,1). 3.5) We supply the
         * prepared heightmap itself.
         */
        int patchSize = 65;
        terrain = new TerrainQuad("my terrain", patchSize, 513, heightmap.getHeightMap());

        /**
         * 4. We give the terrain its material, position & scale it, and attach
         * it.
         */
        terrain.setMaterial(mat_terrain);
        terrain.setLocalTranslation(0, -100, 0);
        terrain.setLocalScale(2f, 1f, 2f);
        rootNode.attachChild(terrain);
        sceneModel = terrain;
    }

    public void initNiftyGUI() {

        Variables.setLaGame(this);
        Variables.setMoveCursor(new MoveCursor());
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        nifty.fromXml("Interface/int.xml", "start");
    }

    public void saySomething() {
        System.out.println("hello this is me");
    }

    public PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }

    public ChatSystem getChatSystem() {
        if (chatSystem == null) {
            chatSystem = new ChatSystem(this);
        }
        return chatSystem;
    }

    /**
     * Demande de REQUEST au serveur
     *
     * @param map
     */
    public void updateFromServer(Sharable sharable) {
        //logger.info("Update de " + sharable.getKey());
        getChatSystem().debug(
                "? " + sharable.getKey() + " (" + sharable.getVersionCode()
                + ")");
        Pck pck = new Pck(PckCode.UPDATE);
        pck.putString(sharable.getKey());
        pck.putInt(sharable.getVersionCode());
        //send(pck); c'est au SimpleClient 

    }
    private ServerEditor serverEditor;

    public ServerEditor getServerEditor() {
        if (serverEditor == null) {
            serverEditor = new ServerEditor(this);
        }
        return serverEditor;
    }
    /**
     * Demande au serveur
     *
     * @param sharable
     * @param callback
     */
    private HashMap<String, LinkedBlockingQueue<Runnable>> callbackWaitingUpdateAnswer =
            new HashMap<String, LinkedBlockingQueue<Runnable>>();
    /**
     * Demande au serveur
     *
     * @param script
     * @param scriptTask
     */
    private World world;

    public void updateFromServerAndWait(String key, Runnable callback) {
        updateFromServerAndWait(world.getSharable(key), callback);
    }

    public void updateFromServerAndWait(Sharable s, Runnable callback) {
        String key = s.getKey();
        if (!callbackWaitingUpdateAnswer.containsKey(key)) {
            callbackWaitingUpdateAnswer.put(key, new LinkedBlockingQueue<Runnable>());
        }
        try {
            callbackWaitingUpdateAnswer.get(key).put(callback);
            getChatSystem().debug("pause de " + callback);
            updateFromServer(s);
        } catch (InterruptedException e) {
            //logger.warning("impossible de mettre en pause le callback");
            Variables.getConsole().output("impossible de mettre en pause le callback");
        }

    }

    /**
     * Demande au serveur
     *
     * @param sharable
     * @param callback
     */
    public void updateFromServerAndWait(Sharable s) {
        //final Thread waiter = new Thread();
        //waiter.interrupt();
        final Object sync = new Object();

        synchronized (sync) {
            updateFromServerAndWait(s, new Runnable() {
                @Override
                public void run() {
                    synchronized (sync) {
                        sync.notifyAll();
                    }
                }
            });
            try {
                sync.wait();
            } catch (InterruptedException e) {
                //logger.warning(e.getLocalizedMessage());
                Variables.getConsole().output(e.getLocalizedMessage());
            }
        }

    }
    /**
     * gestionnaire de tache
     *
     * @return
     */
    private ThreadPoolExecutor executor;

    public ThreadPoolExecutor getTaskExecutor() {
        if (executor == null) {
            executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Integer.parseInt(
                    Variables.getClientConnecteur().getProps()
                    .getProperty("la.max.parallel.task", "8")));
            /*getSchedulerTaskExecutor().scheduleAtFixedRate(new Runnable() {
             @Override
             public void run() {
             logger.info("state : "+executor.getActiveCount()+":"+executor.getQueue().size());
             }
             }, 1, 1, TimeUnit.SECONDS);//*/
        }
        //logger.info("state : "+executor.getActiveCount()+":"+executor.getQueue().size());
        return executor;
    }
    /**
     * gestionnaire de tache
     *
     * @return
     */
    /**
     * service de programmation de task
     */
    private ScheduledThreadPoolExecutor scheduledExecutor;

    public ScheduledThreadPoolExecutor getSchedulerTaskExecutor() {
        if (scheduledExecutor == null) {
            scheduledExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(
                    Integer.parseInt(Variables.getClientConnecteur().getProps()
                    .getProperty("la.scheduled.task", "10")));




        }
        return scheduledExecutor;
    }
    /**
     * renvoie l'object permettant d'envoyé des traces
     *
     * @return
     */
    private LaTraces traces;

    public LaTraces getTraces() {
        if (traces == null) {
            traces = new LaTraces(this);
        }
        return traces;
    }

    /**
     * Notify sur le server les modification
     *
     * @param move
     */
    public void commitOnServer(Sharable sharable) {
        //logger.info("Commit de " + sharable.getKey());
        Variables.getConsole().output("Commit de " + sharable.getKey());
        getChatSystem().debug(
                "< " + sharable.getKey() + " (" + sharable.getVersionCode()
                + ")");
        Pck pck = new Pck(PckCode.COMMIT);
        pck.putString(sharable.getKey());
        pck.putInt(sharable.getVersionCode());
        sharable.addData(pck);
        //send(pck);
        Variables.getClientConnecteur().send(pck);
    }
}