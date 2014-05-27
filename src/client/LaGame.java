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

import client.editor.ServerEditor;
import client.hud.boussole.Boussole;
import client.hud.missionStatus.MissionStatus;

import client.hud3D.MoveCursor;
import client.input.MainGameListener;
import client.map.WaterPlan;
import client.map.World;
import client.map.character.Player;
import client.map.tool.viewer.PDFViewer;
import client.network.SimpleClientConnector;
import client.utils.FileLoader;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
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
import com.jme3.terrain.heightmap.RawHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.plugins.AWTLoader;
import com.sun.sgs.client.simple.SimpleClient;
import de.lessvoid.nifty.Nifty;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

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

                    fireCamp.setLocalTranslation(new Vector3f(joueur.getplayerControl().getPhysicsLocation().x + 30, joueur.getplayerControl().getPhysicsLocation().y, joueur.getplayerControl().getPhysicsLocation().z));
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

        System.out.println("This is calling find way0 "+Thread.currentThread().getName()); 
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
       
     
        
        
           
     
        }
  
    
    
  private Callable findWay;  

    public Callable getFindWay() {
        return findWay;
    }
  
  
  
private static final float INCLINAISON = FastMath.HALF_PI;
    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    public void setBulletAppState(BulletAppState bulletAppState) {
        this.bulletAppState = bulletAppState;
    }
    ChaseCamera chaseCam;
    ChaseCamera chaseCamIntro;

    private void initCamera() {
        this.enqueue(
                new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                chaseCamIntro.setEnabled(false);
      //Variables.getConsole().output("initCamera()"); 
      viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
      flyCam.setMoveSpeed(150);
    

      //***** DISABLING THE FLYING CAMERA  ******///
      flyCam.setEnabled(false);
      chaseCam = new ChaseCamera(cam, joueur.getPlayerModel(), inputManager);
        
      //chaseCam.setDefaultDistance(350);
      chaseCam.setEnabled(true);
   return null;
            }
                });
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
         chaseCamIntro = new ChaseCamera(cam, fireCamp, inputManager);
         chaseCamIntro.setDefaultDistance(500);
         chaseCamIntro.setLookAtOffset(sceneModel1.getLocalTranslation());
         //chaseCam.set
         chaseCamIntro.setEnabled(paused);
         
        
        // Material m=fireCamp.get
         
    }
private boolean playerInitialized=false;
    private void initPlayer() {
     
  this.enqueue(
                new Callable<Void>() {
            @Override
            public Void call() throws Exception {

    System.out.println("initPlayer()");
    if(Variables.getMainPlayer()!=null && Variables.isPlayerModelLoaded())
    {
        System.out.println("LaGame -> initPlayer :  ça marche normale");
        joueur = Variables.getMainPlayer();
        joueur.initPlayer();
        joueur.attachToScene();
        
        playerInitialized=true;
    }else
    System.out.println("LaGame-> initPlayer() : ERREUR !!");
        
    return null;
            }
                });
      


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
    
    public BitmapFont getGuiFont()
    {
        return guiFont;
    }
    
    
    private ActionListener actionListener;
    Quaternion q=new Quaternion();
         
    @Override
    public void simpleUpdate(float tpf) {
       if(!isStartScreen)
       {
           //Variables.getConsole().output("updating");
        if(boussole ==null )boussole=new Boussole(); 
        
        if(missionStatus!=null) missionStatus.update(tpf);
        boussole.update();
        
        if(playerInitialized)
        {
        this.enqueue(
                new Callable<Void>() {
            @Override
            public Void call() throws Exception {
        joueur.update();
        return null; }
                });
        
              
        //joueur.getPlayerModel().removeFromParent();
        joueur.getPlayerModel().setLocalTranslation(new Vector3f(
                joueur.getplayerControl().getPhysicsLocation().x, joueur.getplayerControl().getPhysicsLocation().y - 2.5f,
                joueur.getplayerControl().getPhysicsLocation().z));
        
        }
       // joueur.attachToScene();
        //walkDirection.set(0, 0, 0);
        
        if(chaseCam !=null && !chaseCam.isEnabled())
        chaseCam.setEnabled(true);
       
       }else
         if( Variables.getConsole()!=null)  Variables.getConsole().output("is not updating");
       
       /*
        * Gestion des Thread
        * Ceci pour optimiser l'espace mémoire occupé par les Thread
        */
       gestionThread();

    }
    
    
       /*
        * Gestion des Thread
        * Ceci pour optimiser l'espace mémoire occupé par les Thread
        * @author Hamza ABED
        */
    private void gestionThread()
    {
        boolean listeThreadVide=true;
       for(int i=0;i<Variables.getFutures().size();i++)
       {
           //System.out.println("update !!");
           Future future=Variables.getFutures().get(i);
           if(future != null){
               listeThreadVide=false;
            //Get the waylist when its done
            if(future.isDone()){
               // System.out.println("future is done !!");
                //future = null;
                Variables.getFutures().set(i, null);
                
            }
            else if(future.isCancelled()){
                //System.out.println("future is cancelled !!");
                //Set future to null. Maybe we succeed next time...
                Variables.getFutures().set(i, null);
            }
       }
    }
       if(listeThreadVide) {
           Variables.setFutures(new ArrayList<Future>());
           Variables.setFindWay(new ArrayList<Callable>());
       }
    }

    /**
     * We over-write some navigational key mappings here, so we can add
     * physics-controlled walking and jumping:
     */
    private void setUpKeys() {
    
        this.enqueue(
                new Callable<Void>() {
            @Override
            public Void call() throws Exception {
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
        return null;
            }
                });
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

        
        sceneModel1.setLocalTranslation(0, -170, 0);
       
        waterPlan=new WaterPlan(sceneModel1);
        waterPlan.setWaterOnTheGame();
       
    }
  
    MissionStatus missionStatus;
    
    private void initSceneGame() {
     
        this.enqueue(
                new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                
                System.out.println("initSceneGame()");
                boussole = new Boussole();
                // sceneModel1.updateGeometricState();
                // rootNode.updateGeometricState();
                rootNode.detachAllChildren();

                sceneModel1.updateGeometricState();
                rootNode.updateGeometricState();
/* */
                sceneModel = assetManager.loadModel("Scenes/scene1.j3o");
                sceneModel.updateGeometricState();
                sceneModel.setName("Scene of the main game");
                //sceneModel = assetManager.loadModel("Scenes/starting.j3o");
              //  rootNode.attachChild(sceneModel);
                //rootNode.updateGeometricState();
                sceneModel.setLocalTranslation(0, 2, 0);

//*/                 
               initTerrain();

if(Variables.isMapsLoaded())
{
    System.out.println("all maps loaded !!");
   // sceneModel=Variables.getSceneModel();
}
else
    System.out.println("maps not loaded !!");
                sceneModel.setName("Scene of the main game");
                if(Variables.getSceneModel()==null) System.out.println("scene not setted!!");
                
                //sceneModel=Variables.getSceneModel();
               
                
                
             //    TerrainLodControl control = new TerrainGridLodControl((Terrain) sceneModel, getCamera());
             //control.setLodCalculator( new DistanceLodCalculator(65, 2.7f) ); // patch size, and a multiplier
             // sceneModel.addControl(control);
                
                
               
                CollisionShape sceneShape =CollisionShapeFactory.createMeshShape((Node) sceneModel);
                System.out.println("Collision for scene created !!");
                landscape = new RigidBodyControl(sceneShape, 0);
                sceneModel.addControl(landscape);  //define the scene as a rigid body
                System.err.println("name="+sceneModel.getName());

             
               sceneModel.setLocalTranslation(0, -50, 0);
                rootNode.attachChild(sceneModel);
                bulletAppState.getPhysicsSpace().add(landscape);

                Variables.setSceneModel(sceneModel);

              //  waterPlan = new WaterPlan(sceneModel);
              waterPlan.removeWater();
              
              /*******************************************/
              /***********ATTACH MISSION STATUS *********/
              /******************************************/
              missionStatus =new MissionStatus();
              missionStatus.setLocalTranslation(150, 660, 2);
              guiNode.attachChild(missionStatus);
              
              

                return null;
            }
        });

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
        mat_terrain = new Material(assetManager,"Common/MatDefs/Terrain/Terrain.j3md");

        /**
         * 1.1) Add ALPHA map (for red-blue-green coded splat textures)
         */
    //    mat_terrain.setTexture("Alpha", assetManager.loadTexture("Textures/Terrain/splat/alphamap.png"));

        /**
         * 1.2) Add GRASS texture into the red layer (Tex1).
         */
        //Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");//grass
        Texture grass = assetManager.loadTexture("Textures/grass-texture.jpg");//grass
        grass.setWrap(WrapMode.Repeat);
        mat_terrain.setTexture("Tex1", grass);
        mat_terrain.setFloat("Tex1Scale", 64f);

        /**
         * 1.3) Add DIRT texture into the green layer (Tex2)
         */
        Texture dirt = assetManager.loadTexture(
                "Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(WrapMode.Repeat);
       // mat_terrain.setTexture("Tex2", dirt);
        //mat_terrain.setFloat("Tex2Scale", 32f);

        /**
         * 1.4) Add ROAD texture into the blue layer (Tex3)
         */
        Texture rock = assetManager.loadTexture(
                "Textures/Terrain/splat/dirt.jpg");//road
        rock.setWrap(WrapMode.Repeat);
        //mat_terrain.setTexture("Tex3", rock);
        //mat_terrain.setFloat("Tex3Scale", 128f);

        /**
         * 2. Create the height map
         */
        AbstractHeightMap heightmap = null;
        Texture heightMapImage = assetManager.loadTexture(
                "Scenes/height.png");
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
        AbstractHeightMap heightMap=null;
        try {
            heightMap = new RawHeightMap(FileLoader
                           .getResourceAsUrl("data/map/demo/demo.raw"),
                           129, RawHeightMap.FORMAT_16BITBE, false);
        } catch (Exception ex) {
            Logger.getLogger(LaGame.class.getName()).log(Level.SEVERE, null, ex);
        }
        heightMap.load();
        int patchSize = 65;
        terrain = new TerrainQuad("my terrain", patchSize, 513, heightmap.getHeightMap());
        //terrain = new TerrainQuad("my terrain", patchSize, 129, heightMap.getHeightMap());

        /**
         * 4. We give the terrain its material, position & scale it, and attach
         * it.
         **/
        terrain.setMaterial(mat_terrain);
        terrain.setLocalTranslation(0, 00, 0);
        //terrain.setLocalScale(2f, 1f, 2f);
        terrain.setLocalScale(8f, 0.2f, 8f);
        rootNode.attachChild(terrain);
        sceneModel = terrain;
      // Variables.setSceneModel(sceneModel);
              
    }

    
    
    
 private Future ft;

    /*
     * cette méthode exécute la méthode 
     */
    public Future getFt() {
        return ft;
    }

    public void setFt(Future ft) {
        this.ft = ft;
    }
  
    
    public void initGameWorld()
    {
        if (!Variables.isPlayerModelLoaded()) {
            System.out.println("LaGame -> initGameWorld() :player not loaded yet !!!!!");
        }
        System.out.println("This is calling find way2 " + Thread.currentThread().getName());
        
                initSceneGame();
                
                scheduledExecutor.submit(
                 new Callable<Void>() {
             @Override
             public Void call() {
                 
                 if(Variables.isMapsLoaded())
                initPlayer();
                 else
                     try {
                         System.out.println("Player attend le chargement du terrain!!");
                     Thread.sleep(100);
                     initPlayer();
                 } catch (InterruptedException ex) {
                     Logger.getLogger(LaGame.class.getName()).log(Level.SEVERE, null, ex);
                 }
                return null;
             }
                 });
                gameListener = new MainGameListener(sceneModel);

                
             scheduledExecutor.submit(
                 new Callable<Void>() {
             @Override
             public Void call() {
                 
                 if(playerInitialized)   
                initCamera();
                  else
                     try {
                         System.out.println("Camera attend le chargement du player!!");
                     Thread.sleep(100);
                      initCamera();
                 } catch (InterruptedException ex) {
                     Logger.getLogger(LaGame.class.getName()).log(Level.SEVERE, null, ex);
                 }
                return null;
             }
                 });

                isStartScreen = false;

                bulletAppState.getPhysicsSpace().enableDebug(assetManager);

                setUpKeys();
          
       
       
       
       
         
         
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
 /*
  * Ordonnencement des Thread
  */

 /**
	 * gestionnaire de tache
	 * 
	 * @return
	 */
        private  ScheduledThreadPoolExecutor scheduledExecutor;
	public  ScheduledThreadPoolExecutor getSchedulerTaskExecutor() {
		if (scheduledExecutor == null) {
	scheduledExecutor = new ScheduledThreadPoolExecutor(
		Integer.parseInt(Variables.getProps().getProperty("la.scheduled.task", "10")));
			
			
			

		}
		return scheduledExecutor;
	}
        
        public void quitGame()
        {
            Variables.setFinished(true);
            
            System.out.println("ici quit game !!");
        }
        /**
	 * permet de creer des objets sur le server
	 */
	private ServerEditor serverEditor;
        
        private RessourceManager ressources;
        private LaTraces traces;
        /**
	 * renvoie l'object permettant d'envoyé des traces
	 * @return
	 */
	public LaTraces getTraces() {
		if (traces == null) 
			traces = new LaTraces(this);
		return traces;
	}
        
        /**
	 * Permet la creation de nouveau objet sur le server
	 * 
	 * @return
	 */
	public ServerEditor getServerEditor() {
		if (serverEditor == null) {
			serverEditor = new ServerEditor(this);
		}
		return serverEditor;
	}
       
        
        
}