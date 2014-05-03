/**
 * Copyright 2010 http://learning-adventure.fr
 * Tous droits réservés
 * 
 * 
 * ----------------------------------------------------------------------------
 * Ce fichier fait partie de LA-Client.
 *
 * LA-Client est un logiciel libre ; vous pouvez le redistribuer ou le modifier 
 * suivant les termes de la GNU General Public License telle que publiée par
 * la Free Software Foundation ; soit la version 3 de la licence, soit 
 * (à votre gré) toute version ultérieure.
 * 
 * LA-Client est distribué dans l'espoir qu'il sera utile, 
 * mais SANS AUCUNE GARANTIE ; pas même la garantie implicite de 
 * COMMERCIABILISABILITÉ ni d'ADÉQUATION à UN OBJECTIF PARTICULIER. 
 * Consultez la GNU General Public License pour plus de détails.
 * 
 * Vous devez avoir reçu une copie de la GNU General Public License 
 * en même temps que LA-Client ; si ce n'est pas le cas, 
 * consultez <http://www.gnu.org/licenses>.
 * ----------------------------------------------------------------------------
 * This file is part of LA-Client.
 *
 * LA-Client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LA-Client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with LA-Client.  If not, see <http://www.gnu.org/licenses/>.
 * ----------------------------------------------------------------------------
 */


package client;

import client.hud.boussole.Boussole;
import client.hud3D.MoveCursor;
import client.input.MainGameListener;
import client.map.WaterPlan;
import client.map.World;
import client.map.character.NonPlayableCharacter;
import client.map.character.Player;
import client.map.tool.viewer.PDFRead;
import client.map.tool.viewer.PDFViewer;
import client.network.SimpleClientConnector;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.plugins.AWTLoader;
import com.sun.jersey.core.spi.scanning.uri.VfsSchemeScanner;
import com.sun.sgs.client.simple.SimpleClient;
import de.lessvoid.nifty.Nifty;
import java.awt.Image;
import java.awt.image.BufferedImage;

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
    private WaterPlan waterPlan;
    private Spatial sceneModel;
    private Spatial sceneModel1;
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private static LaGame app;
    protected SimpleClient simpleClient; //Répresente toutes communication avec le serveur
    private Player joueur; // Le joueur actuel avec son modèle graphique
    private Quaternion qPlan=new Quaternion();
    private  Boussole boussole;
    
    /**
     * moteur de chat
     */
    

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
        
        Variables.setWorld(new World(this));
        Variables.setClientConnecteur(new SimpleClientConnector());

        this.actionListener = new ActionListener() {
            public void onAction(String name, boolean keyPressed, float tpf) {
                if (name.equals("addObject")) {
                    /*
                     * 
                     */
                    Player pl=Variables.getWorld().getPlayer();
                    if(pl!=null)
                    {
              System.out.println("\n\n\n ********************* AFFICHAGE DES PARAMETRES DU JOUEUR **************\n\n\n");
              System.out.println("joueur.getTokens().toString()= "+pl.getTokens().toString());
              System.out.println("joueur.getItems().toString()= "+pl.getItems().getAllItem().size());
              System.out.println("joueur.getTasks().toString()= "+pl.getTasks().list().size());
              System.out.println("pl.getTargets().getAll().size()= "+pl.getTargets().getAll().size());
              System.out.println("pl.getSkills().getAllSkills().size()= "+pl.getSkills().getAllSkills().size());
              System.out.println("pl.getName()= "+pl.getName());
              
                    }else System.out.println("player got from world est nulll !!");
              
                    
                   //Node fireCamp=(Node) assetManager.loadModel("Models/Oto/Oto.mesh.xml");
                    Spatial fireCamp = assetManager.loadModel("Models/campfire/campfire.j3o");
                    fireCamp.setName("fireCamp");
                    rootNode.attachChild(fireCamp);

                    fireCamp.setLocalTranslation(new Vector3f(joueur.getPlayer().getPhysicsLocation().x + 30, joueur.getPlayer().getPhysicsLocation().y, joueur.getPlayer().getPhysicsLocation().z));
                    System.out.println("child attached !!");
                    
                    //Geometry fire=new Geometry("fireCamp")
                     CollisionShape collisionC =
                CollisionShapeFactory.createMeshShape((Node) fireCamp);
       RigidBodyControl fireC = new RigidBodyControl(collisionC, 0);
        fireCamp.addControl(fireC);  //define the scene as a rigid body
        
        bulletAppState.getPhysicsSpace().add(fireC);

                    
                    
      /////////////////////////////////////////////////////////
        
        
         CollisionResults results = new CollisionResults();
        // Convert screen click to 3d position
        Vector2f click2d = inputManager.getCursorPosition();
        Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f dir = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
        // Aim the ray from the clicked spot forwards.
        Ray ray = new Ray(click3d, dir);
        // Collect intersections between ray and all nodes in results list.
        fireCamp.collideWith(ray, results);
        // (Print the results so we see what is going on:)
        for (int i = 0; i < results.size(); i++) {
          // (For each “hit”, we know distance, impact point, geometry.)
          float dist = results.getCollision(i).getDistance();
          Vector3f pt = results.getCollision(i).getContactPoint();
          String target = results.getCollision(i).getGeometry().getName();
          System.err.println("Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away.");
        }

            }
            }  
        };
    }
   private boolean isStartScreen=true;

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
        initStartingScene();
       
        initStartingCamera();
        /*
         * 
         *
         * 
        initScene();

        initPlayer();
        gameListener = new MainGameListener(sceneModel);
        initCamera();
        setUpKeys();
        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        */
        }
    
private static final float INCLINAISON = FastMath.HALF_PI;
    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    public void setBulletAppState(BulletAppState bulletAppState) {
        this.bulletAppState = bulletAppState;
    }
    ChaseCamera chaseCam;

    private void initCamera() {
      //  Variables.getConsole().output("initCamera()"); 
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(150);
        // cam.setLocation(lightDirection);
        // channel.reset(true); // this stop the animation

        //***** DISABLING THE FLYING CAMERA  ******///
        flyCam.setEnabled(false);
        chaseCam = new ChaseCamera(cam, joueur.getPlayerModel(), inputManager);
        
         //chaseCam.setDefaultDistance(350);
        chaseCam.setEnabled(true);
        Variables.getConsole().output("end of initCamera()"); 
        
        
inputManager.deleteMapping("CHASECAM_Left");
inputManager.deleteMapping("CHASECAM_Right");
inputManager.deleteMapping("CHASECAM_Up");
inputManager.deleteMapping("CHASECAM_Down");
inputManager.deleteMapping("CHASECAM_ZoomIn");
inputManager.deleteMapping("CHASECAM_ZoomOut");

inputManager.setCursorVisible( true );
    }
    
    
    private void initStartingCamera() {
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        flyCam.setMoveSpeed(150);
        // cam.setLocation(lightDirection);
        // channel.reset(true); // this stop the animation

        //***** DISABLING THE FLYING CAMERA  ******///
        flyCam.setEnabled(false);
        
         Spatial fireCamp = assetManager.loadModel("Models/campfire/campfire.j3o");
                    rootNode.attachChild(fireCamp);
                    fireCamp.setLocalTranslation(50,20,0);
        //chaseCam = new ChaseCamera(cam, joueur.getPlayerModel(), inputManager);
         chaseCam = new ChaseCamera(cam, fireCamp, inputManager);
         chaseCam.setDefaultDistance(500);
         chaseCam.setLookAtOffset(sceneModel1.getLocalTranslation());
         //chaseCam.set
         chaseCam.setEnabled(paused);
         
        
        // Material m=fireCamp.get
         
    }

    private void initPlayer() {
     
    
        joueur = new Player(Variables.getWorld(),"demo");

     //   joueur = new Player();
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
       if(!isStartScreen)
       {
           //Variables.getConsole().output("updating");
        boussole.update();
        
        joueur.update();
        //joueur.getPlayerModel().removeFromParent();
        joueur.getPlayerModel().setLocalTranslation(new Vector3f(
                joueur.getPlayer().getPhysicsLocation().x, joueur.getPlayer().getPhysicsLocation().y - 2.5f,
                joueur.getPlayer().getPhysicsLocation().z));
       // joueur.attachToScene();
        //walkDirection.set(0, 0, 0);
        if(!chaseCam.isEnabled())
        chaseCam.setEnabled(true);
       
       }else
         if( Variables.getConsole()!=null)  Variables.getConsole().output("is not updating");

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
        inputManager.addMapping("addObject", new KeyTrigger(KeyInput.KEY_A));
        
      //  inputManager.deleteTrigger("toggleRotate", new MouseButtonTrigger(0));
        
    
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
    
  
    private void initStartingScene() {
       // sceneModel = assetManager.loadModel("Scenes/scene1.j3o");
         sceneModel1 = assetManager.loadModel("Scenes/starting.j3o");
        rootNode.attachChild(sceneModel1);
        sceneModel1.setName("sceneModel1");
     //   sceneModel.setLocalTranslation(0, 2, 0);
        
        sceneModel1.setLocalTranslation(0, -170, 0);
        //initTerrain();
        //initSimpleWater();
        //initPPcWater();
        /*    CollisionShape sceneShape =
                CollisionShapeFactory.createMeshShape((Node) sceneModel);
        landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);  //define the scene as a rigid body

        bulletAppState.getPhysicsSpace().add(landscape);

        Variables.setSceneModel(sceneModel);
        */
        waterPlan=new WaterPlan(sceneModel1);
        waterPlan.setWaterOnTheGame();
       // waterPlan.setSimpleWaterOnTheGame();
    }
    
    
    private void initSceneGame() {
          //  Variables.getConsole().output("initSceneGame()"); 
        boussole=new Boussole();
        rootNode.detachAllChildren();
        sceneModel1.updateGeometricState();
        //rootNode.updateGeometricState();
        //  rootNode.detachChildNamed("water"); 
        //rootNode.detachChild(sceneModel1);
        sceneModel = assetManager.loadModel("Scenes/scene1.j3o");
       // sceneModel.updateGeometricState();
        sceneModel.setName("Scene of the main game");
         //sceneModel = assetManager.loadModel("Scenes/starting.j3o");
          rootNode.attachChild(sceneModel);
          sceneModel.setLocalTranslation(0, 2, 0);
        
        
        CollisionShape sceneShape =
                CollisionShapeFactory.createMeshShape((Node) sceneModel);
        landscape = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(landscape);  //define the scene as a rigid body

        bulletAppState.getPhysicsSpace().add(landscape);

        Variables.setSceneModel(sceneModel);
        
        waterPlan=new WaterPlan(sceneModel);
        
     //   waterPlan.setWaterOnTheGame();
       // waterPlan.setSimpleWaterOnTheGame();
      //   Variables.getConsole().output("end initSceneGame()"); 
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
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

    
    public void initGameWold()
    {
       
        initSceneGame();
        initPlayer();
        gameListener = new MainGameListener(sceneModel);

        initCamera();
       
        isStartScreen=false;

        bulletAppState.getPhysicsSpace().enableDebug(assetManager);
       
         setUpKeys();
       //   isStartScreen=false;
         
    }
    public void initNiftyGUI() {

        Variables.setLaGame(this);
        Variables.setMoveCursor(new MoveCursor());
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        nifty.fromXml("Interface/MainInterface.xml", "start");
        
    }

    public void saySomething() {
        System.out.println("hello this is me");
    }

    public PhysicsSpace getPhysicsSpace() {
        return bulletAppState.getPhysicsSpace();
    }
  
  

    
    public Spatial getSceneModel() {
        return sceneModel;
    }

    public void setSceneModel(Spatial sceneModel) {
        this.sceneModel = sceneModel;
    }
    
     
 public void showPDF()
 {
        System.out.println("this is show PDF");
        PDFViewer pdfViewer=new PDFViewer();
        pdfViewer.toImages();
        BufferedImage image=pdfViewer.getImages().get(0);
        AWTLoader loader = new AWTLoader();
        com.jme3.texture.Image load = loader.load(image, true);
      
  
         Box box = new Box(new Vector3f(0, 0, 0), 450, 450, 5);
       Geometry cube = new Geometry("boxPDF", box);
        Material mat1 = new Material(Variables.getLaGame().getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
           
        
        TextureKey key =new TextureKey("Textures/dirt.jpg",false);
        Texture tex = assetManager.loadTexture(key);
        System.out.println("this is show PDF attaching child");
        
        tex.setImage(load);
        mat1.setTexture("ColorMap",tex);
        mat1.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        cube.setMaterial(mat1);
        cube.move(600, 660, 2);
        guiNode.attachChild(cube);
        System.out.println("this is show PDF attaching child end");
       /*
        * 
        */
}
 AbstractHeightMap d;
ImageBasedHeightMap i;   

}