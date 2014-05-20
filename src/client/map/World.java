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
package client.map;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import shared.constants.LaConstants;
import shared.constants.PckCode;
import shared.enums.LaComponent;
import shared.pck.Pck;
//import shared.utils.SSHTransfert;
import client.FtpManager;
import client.LaGame;
import client.editor.ServerEditor.CreatorCallBack;
import client.interfaces.graphic.Graphic;
import client.interfaces.graphic.GraphicCollidable;
import client.interfaces.graphic.GraphicWalkOver;
import client.interfaces.graphic.GraphicWithGround;
import client.interfaces.network.Sharable;
import client.interfaces.network.SharableGroup;
import client.map.character.AbstractCharacter.Moving;
import client.map.character.Dialog;
import client.map.character.Group;
import client.map.character.NonPlayableCharacter;
import client.map.character.OtherPlayer;
import client.map.character.PlayableCharacter;
import client.map.character.Player;
import client.map.character.stats.GroupTokens;
import client.map.character.stats.Item;
import client.map.character.stats.PlayerItems;
import client.map.character.stats.PlayerSkills;
import client.map.character.stats.PlayerTargets;
import client.map.character.stats.PlayerTasks;
import client.map.character.stats.PlayerTokens;
import client.map.character.stats.Skill;
import client.map.character.stats.Task;
import client.map.data.SlideShow;
import client.map.object.BasicMapObject;
import client.map.object.BuildingMapObject;
import client.map.object.MapGraphics;
import client.map.object.MapTable;
import client.map.object.ParticulEngine;
//import client.map.object.BasicMapObject;
//import client.map.object.BuildingMapObject;
//import client.map.object.MapGraphics;
//import client.map.object.MapLight;
//import client.map.object.ParticulEngine;
//import client.map.object.MapTable;
// pas opérationnelle en v31
// import client.map.tool.Lgf;
import client.map.tool.Tool;
import client.script.Script;
import client.script.ScriptExecutor;
import client.script.ScriptableMethod;
import com.jme3.app.state.ScreenshotAppState;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.material.Material;

//import com.jme.intersection.PickResults;
//import com.jme.intersection.TrianglePickResults;
//import com.jme.light.DirectionalLight;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.terrain.geomipmap.TerrainGrid;
import com.jme3.terrain.geomipmap.TerrainGridLodControl;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.grid.ImageTileLoader;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.heightmap.Namer;
import com.jme3.texture.Texture;
//import com.jme.system.DisplaySystem;
//import com.jme.util.GameTaskQueueManager;
import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import java.util.Collection;
import java.util.concurrent.Callable;

import shared.variables.Variables;

/**
 * Monde
 * <ul>
 * <li>Contient l'enssemble des objet du jeux</li>
 * <li>modif pp : ajout de la connexion avec ssh</li>
 * <li>modif pp : ajout de la table</li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2010-2013
 * @author Hamza ABED, <b>hamza.abed.pofessionel@gmail.com</b>, 2014
 */


/*
 * Le problème c'est que tout les variables de mapping 
 * ,de shadow lighting etc.. sont plus simplifié avec jme3 
 * et sont bien pris en charge.
 * ce qui nous impose de les enlever de cette classe
 */
public  class World  implements ClientChannelListener,Sharable {
	private static final long serialVersionUID = 3254692658300941324L;

	private static final Logger logger = Logger.getLogger("World");

	//private WaterPlan water;

//	private Shadow shadow;
        private float mapSize, zoneSize;

	private HashMap<String, Map> maps = new HashMap<String, Map>();

	private HashMap<String, Zone> zones = new HashMap<String, Zone>();

	private HashMap<String, Tool> tools = new HashMap<String, Tool>();

	private HashMap<String, MapGraphics> objects = new HashMap<String, MapGraphics>();

//	private HashMap<String, MapLight> lights = new HashMap<String, MapLight>();

	private ArrayList<GraphicWithGround> groundeds = new ArrayList<GraphicWithGround>();

	private ArrayList<GraphicWalkOver> walkOvers = new ArrayList<GraphicWalkOver>();

	private ArrayList<GraphicCollidable> collidables = new ArrayList<GraphicCollidable>();

	private HashMap<String, PlayableCharacter> playableCharacter = new HashMap<String, PlayableCharacter>();

	private HashMap<String, Group> groups = new HashMap<String, Group>();

	private HashMap<String, NonPlayableCharacter> npcs = new HashMap<String, NonPlayableCharacter>();

	private HashMap<String, Item> items = new HashMap<String, Item>();

	private HashMap<String, Dialog> dialogs = new HashMap<String, Dialog>();

	// pas opérationnelle en v31
	//private HashMap<String, Lgf> lgfs = new HashMap<String, Lgf>();

	private HashMap<String, Script> scripts = new HashMap<String, Script>();

	private HashMap<String, Skill> skills = new HashMap<String, Skill>();

	private HashMap<String, SlideShow> slides = new HashMap<String, SlideShow>();

	private HashMap<String, Region> regions = new HashMap<String, Region>();

	private HashMap<String, Task> tasks = new HashMap<String, Task>();

	private HashMap<String, GameData> gameDatas = new HashMap<String, GameData>();
	
	private HashMap<String, MapTable> tables = new HashMap<String, MapTable>();

//	private Sky sky;

	private Player player;

	private LaGame game;

	private int worldSizeX, worldSizeZ;

	private float worldScaleY, worldWaterDeep;

	

	private int versionCode = -1;

	private ScriptExecutor scriptExecutor;

	private boolean idenObjectVisible = false;

	private FtpManager ftp;
	

	private WorldTokens worldTok;

	@SuppressWarnings("unused")
	private ClientChannel channel;

        //LaGame game;
	/**
	 * @param light 
	 * @param laClient
	 * 
	 */
   //     private HashMap<String, Script> scripts = new HashMap<String, Script>();
	public World(LaGame game) {
            System.out.println("instantiation de World");
		//super("world");
		this.game = game;
                sceneModel=new Node("Scene of the main game");
		//sky = new Sky(this);
		//water = new WaterPlan(sky, game.getProps());
		//shadow = new Shadow(game.getProps(),light);

		//water.addReflet(this);
	}

	/**
	 * Suppression de tous les element graphique (en cas de deconnection)
	 */
	public void clean() {
            /*
             * pour vider le game
             * il suffit de détacher tout de la superbe
             * rootNode de jme3
             * 
             * Hamza ABED
             */
            
        game.getRootNode().detachAllChildren();
            
		//this.detachAllChildren();

		//this.shadow.clean();
		//this.water.clean();

		this.dialogs.clear();
		this.collidables.clear();
		this.groups.clear();
		this.items.clear();
		//pas opérationnele en v31
		//this.lgfs.clear();
		this.npcs.clear();
		this.maps.clear();
		this.objects.clear();
		this.playableCharacter.clear();
		this.regions.clear();
		this.skills.clear();
		this.slides.clear();
		this.scripts.clear();
		this.tasks.clear();
		this.groundeds.clear();
		this.walkOvers.clear();
		this.zones.clear();
		this.tables.clear();
		this.player = null;
		this.worldTok = null;
		this.versionCode = -1;
                
	}
        /**********************************************************************/
        /*********************CREATION D'UN TERRAIN GRID **********************/
        /**********************************************************************/
        
    TerrainGrid terrain;
    private Material mat_terrain;
     private float grassScale = 64;
    private float dirtScale = 16;
    private float rockScale = 128;
   
    void createTerrainGrid()
    {
        
        System.out.println("CreateTerrainGrid() :  called !!");
 
        // TERRAIN TEXTURE material
        mat_terrain = new Material(Variables.getLaGame().getAssetManager(), "Common/MatDefs/Terrain/HeightBasedTerrain.j3md");

        // Parameters to material:
        // regionXColorMap: X = 1..4 the texture that should be appliad to state X
        // regionX: a Vector3f containing the following information:
        //      regionX.x: the start height of the region
        //      regionX.y: the end height of the region
        //      regionX.z: the texture scale for the region
        //  it might not be the most elegant way for storing these 3 values, but it packs the data nicely :)
        // slopeColorMap: the texture to be used for cliffs, and steep mountain sites
        // slopeTileFactor: the texture scale for slopes
        // terrainSize: the total size of the terrain (used for scaling the texture)
        // GRASS texture
        Texture grass = Variables.getLaGame().getAssetManager().loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        this.mat_terrain.setTexture("region1ColorMap", grass);
        this.mat_terrain.setVector3("region1", new Vector3f(88, 200, this.grassScale));

        // DIRT texture
        Texture dirt = Variables.getLaGame().getAssetManager().loadTexture("Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        this.mat_terrain.setTexture("region2ColorMap", dirt);
        this.mat_terrain.setVector3("region2", new Vector3f(0, 90, this.dirtScale));

        // ROCK texture
        Texture rock = Variables.getLaGame().getAssetManager().loadTexture("Textures/Terrain/Rock2/rock.jpg");
        rock.setWrap(Texture.WrapMode.Repeat);
        this.mat_terrain.setTexture("region3ColorMap", rock);
        this.mat_terrain.setVector3("region3", new Vector3f(198, 260, this.rockScale));

        this.mat_terrain.setTexture("region4ColorMap", rock);
        this.mat_terrain.setVector3("region4", new Vector3f(198, 260, this.rockScale));

        this.mat_terrain.setTexture("slopeColorMap", rock);
        this.mat_terrain.setFloat("slopeTileFactor", 32);

        this.mat_terrain.setFloat("terrainSize", 129);
      
       
        
        this.terrain = new TerrainGrid("terrain", 65, 257, new ImageTileLoader(Variables.getLaGame().getAssetManager(), new Namer() {

       public String getName(int x, int y) {
         System.out.println("Scenes/TerrainMountains/terrain_" + x + "_" + y + ".png");
         if(x==2 && y==2)  Variables.setMapsLoaded(true);
                return "Scenes/TerrainMountains/terrain_" + x + "_" + y + ".png";
            }
        }));
        
        this.terrain.setMaterial(mat_terrain);
        

       
        
    //  Variables.getLaGame().getRootNode().attachChild(terrain);
       Variables.setSceneModel(terrain);
        
           
            
            System.out.println("CreateTerrainGrid() :  END !!");
    }
        
        
        
        
	/**
	 * Construit les cartes Elle ne sont pas encore construite graphiquement car
	 * voila
	 */
	private void build() {
            
           
	/*
         * Détails : charge les map en fonction des dimetions du World
         * Hamza ABED
         */
       createTerrainGrid();
       /*
            for (int x = 0; x < worldSizeX; x++)
			for (int z = 0; z < worldSizeZ; z++)
				if (!maps.containsKey(LaComponent.map.prefix() + x + ":" + z)) {
                                    System.err.println("ajout map "+z+" worldSizeX="+worldSizeX
                                            +" worldSizeZ="+worldSizeZ);
					Map map = new Map(this, x, z);
					this.maps.put(map.getKey(), map);
				}
            
            if(this.maps.size()==(worldSizeX*worldSizeZ))              
              Variables.setMapsLoaded(true);
            Variables.setSceneModel(sceneModel); //cette sceneModel est un lot de maps 
            //construites lors de la boucle à l'aide de la méthode addGraphic de World$
            //appelé depuis Map
          
          */
            
         
	}

	/* ********************************************************** *
	 * *	 			GESTION DES JOUEURS 					* *
	 * ********************************************************** */

	/**
	 * Crais le joueur controler par l'instance
	 * 
	 * @param login
	 */
	public void createPlayer(String login) {
            
            
            
		this.player = new Player(this, login);
		this.playableCharacter.put(player.getKey(), player);
                System.out.println("Player created player.getKey()="+player.getName());
                Variables.setMainPlayer(player);
            
	}

	/* ********************************************************** *
	 * * 				Mise à Jour graphic 					* *
	 * ********************************************************** */

	/**
	 * Met à jour les animation
	 * 
	 * @param interpolation
	 */
	public void update(float interpolation) {
            System.out.println("World->update(interpolation="+interpolation+") : vide !!");
            /*
             * En jme3 on a pas besoin de mettre à jour des animation
             * car les animation sont tout simplement gérés par 
             * leurs channels
             * 
             * HAMZA ABED
             */
		/* Camera camera = DisplaySystem.getDisplaySystem().getRenderer().getCamera();
		sky.setLocalTranslation(camera.getLocation());
		water.update(interpolation);
		shadow.update(interpolation);

		Matrix3f cameraMatrix = new Matrix3f();
		cameraMatrix.fromAngleNormalAxis(
				getGame().getCameraControler().getRy()+FastMath.PI, 
				Vector3f.UNIT_Y);

		for (PlayableCharacter p : playableCharacter.values())
			p.update(interpolation,cameraMatrix);
		for (NonPlayableCharacter npc : npcs.values())
			npc.update(interpolation,cameraMatrix);
		for (Map map : maps.values())
			map.update(interpolation);
		for (Tool tool : tools.values())
			tool.update(interpolation);
		for (MapGraphics obj : objects.values())
			obj.update(interpolation);
		for (MapLight light : lights.values())
			light.update(interpolation);
		for (MapTable table : tables.values())
			table.update(interpolation);

		if (game.isEditMode() && !isIdenObjectVisible())
			setIdenObjectVisible(true);
		else if (!game.isEditMode() && isIdenObjectVisible()) 
			setIdenObjectVisible(false);
                 
                 */
	}

	/* ********************************************************** *
	 * * 				GESTION DES OBJECT GRAPHIQUE 			* *
	 * ********************************************************** */
	/**
	 * Ajout un objet graphic au rendu. 
	 * 
	 * @param map
	 */
	public void removeGraphics(Graphic s) {
            
           /*
		if (s.getGraphic() == null)
			return;

		if (s instanceof GraphicShadowed) 
			for (Spatial c : ((GraphicShadowed) s).getShadowed())
				shadow.removeShadowed(c);
		if (s instanceof GraphicShadowCaster)
			shadow.removeOcculter(s.getGraphic());

		s.getGraphic().removeFromParent();

		if (s instanceof GraphicEditable)
			game.getGraphicEditor().remove((GraphicEditable) s);
		if (s instanceof GraphicMouseListener)
			game.getMouseControler().remove((GraphicMouseListener) s);

		if (s instanceof GraphicWithGround)
			this.groundeds.remove((GraphicWithGround) s);
		if (s instanceof GraphicCollidable)
			this.collidables.remove((GraphicCollidable) s);
		if (s instanceof GraphicWalkOver)
			this.walkOvers.remove((GraphicWalkOver) s);
                       
                       */
                   
	}

	/**
	 * ajout un objet graphique
	 * 
	 * @param s
	 */
        Node sceneModel;
	public void addGraphics(Graphic s) {
            System.out.println("World -> addGraphics() : en construction !!");
         
            
           
		if (s == null || s.getGraphic() == null)
			return;
		//logger.fine("ajout de "+s);
                System.out.println("ajout de "+s);

		//int l = s.getGraphic().getLocks();
		//s.getGraphic().setLocked 
                
                /*
                 * Peut être on aura besoin de débloquer le terrain !!!
                 */

		//this.attachChild(s.getGraphic());
                // on va supposer que le world ajoute tout au scène 
                sceneModel.attachChild(s.getGraphic());
		/*
                if (s instanceof GraphicShadowed)
			for (Spatial c : ((GraphicShadowed) s).getShadowed())
				shadow.addShadowed(c);
		if (s instanceof GraphicShadowCaster)
			shadow.addOculter(s.getGraphic());

		s.getGraphic().updateGeometricState(0, true);
		s.getGraphic().updateWorldVectors(true);
		s.getGraphic().updateRenderState();
		s.getGraphic().updateModelBound();

		// object editable par l'interface graphique
		if (s instanceof GraphicEditable)
			game.getGraphicEditor().add((GraphicEditable) s);
		// object ecoutant les mouvement souris
		if (s instanceof GraphicMouseListener)
			game.getMouseControler().add((GraphicMouseListener) s);

		// propriété de l'objet
		if (s instanceof GraphicWithGround)
			this.groundeds.add((GraphicWithGround) s);
		if (s instanceof GraphicCollidable)
			this.collidables.add((GraphicCollidable) s);
		if (s instanceof GraphicWalkOver)
			this.walkOvers.add((GraphicWalkOver)s);

		s.getGraphic().setLocks(l);
		if (s instanceof Map)
			updateMapObjectY((Map) s);
                        
                  */      
                        
                        
                       
	}

	/**
	 * met a jour les hauteur des objet présent sur les carte en cas de
	 * changemet d'latimetrie
	 * 
	 * @param s
	 */
	private void updateMapObjectY(Map s) {
            /*
             * Cela ne sera plus pris en compte
             * Hamza ABED
             */ 
            System.out.println("World->updateMapObjectY(Map)");
		for (MapGraphics obj : objects.values())
			obj.updateY();
                      
	}

	/* ********************************************************** *
	 * *	 				RECEPTION MESSAGE 					* *
	 * ********************************************************** */
	/**
	 * Recoit un commit
	 * 
	 * @param message
	 */
	public void receiveCommitPck(ByteBuffer message) {
		String key = Pck.readString(message);
		try {
			Sharable s = getSharable(key);
                 System.out.println("reception d'un paquet de commit de " + key);
			//logger.info("reception d'un paquet de commit de " + key);
			//logger.info("reception d'un paquet de commit sur " + s.getClass().toString());
			s.receiveCommitPck(message);
			//getGame().getChatSystem().debug("> " + key + " (" + s.getVersionCode() + ")");
                        Variables.getClientConnecteur().notifyWaitingThread(key);
			//getGame().notifyWaitingThread(key);
		} catch (NullPointerException e) {
                    System.out.println("World->receiveCommitPck(msg) ceci est une exception null key="+key+"ce qui sui est un warning");
			//logger.warning("null " + key);
		}
	}

	/**
	 * Recoit une notification d'update
	 * 
	 * @param message
	 */
	public void receiveUpToDatePck(ByteBuffer message) {
		String key = Pck.readString(message);
		logger.fine("reception UpToDate de " + key);
		//getGame().getChatSystem().debug("= " + key);
		try {
			// Sharable s = getSharable(key);
			//getGame().notifyWaitingThread(key);
		} catch (NullPointerException e) {
			logger.warning("null " + key);
		}
	}

	/**
	 * supprime un objet
	 * @param readString
	 */
	private void dropObject(final String key) {
	/*	GameTaskQueueManager.getManager().update(new Callable<Void>() {
			/* (non-Javadoc)
			 * @see java.util.concurrent.Callable#call()
			 */
            /*
			@Override
			public Void call() throws Exception {
				Sharable s = null;
				switch (LaComponent.type(key)) {
				//case table: s=tables.remove(key); break;
				case building:
				case particul:
				//case object: s = objects.remove(key); break;
				//case tool: s = tools.remove(key); break;
				//case gamedata: s = gameDatas.remove(key); break;
				default:
					logger.warning("le serveur à delete ca "+key+" mais je le connais pas ou je sais pas comment faire");
					return null;
				}
				//game.getChatSystem().debug("X "+key);
                                
                                
                                
				if (s instanceof Graphic)
					removeGraphics((Graphic) s);
				return null;
			}
		});
               */ 

	}

	/**
	 * Recois la, structure du monde
	 * 
	 * @param message
	 */
	public void receivedWorldDataPck(ByteBuffer message) {
		
            int serverVersion = message.getInt();
		if (LaConstants.VERSION != serverVersion)  {
                    Variables.getClientConnecteur().disconnect(null);
               System.out.println("World->receivedWorldDataPck(message) -> deconnection  causé par différence de version");
			//game.disconnect(null);
		/*	game.getHud().openErrorPopup(game.getHud().getLocalText("popup.error.version","%server%",Integer.toString(serverVersion),
					"%client%",Integer.toString(LaConstants.VERSION)));
                                        */
			return;
		}

		this.worldSizeX = message.getInt();
		this.worldSizeZ = message.getInt();
		this.worldScaleY = message.getFloat();
		this.worldWaterDeep = message.getFloat();
		this.mapSize = message.getFloat();
		this.zoneSize = message.getFloat();

		String ftpUrl = Pck.readString(message);
		String ftpFolder = Pck.readString(message);
		String ftpUser = Pck.readString(message);
		String ftpPass = Pck.readString(message);

		getFtp().setParam(ftpUrl,ftpUser,ftpPass,ftpFolder);
System.out.println("\n\n world class**************\n worldSizeX="+worldSizeX+"\n"
        + " zoneSize= "+zoneSize+"\n "
        + " mapSize= "+mapSize+"\n "
        + " worldWaterDeep= "+worldWaterDeep+"\n "
        + " worldScaleY= "+worldScaleY+"\n "
        + " worldSizeZ= "+worldSizeZ+"\n "
        + " ftpUrl= "+ftpUrl+"\n "
        + " ftpFolder= "+ftpFolder+"\n "
        + " ftpUser= "+ftpUser+"\n "
        + " ftpPass= "+ftpPass+"\n "
        + "***********\n\n");
		build();
                
	}

	/**
	 * Recois la structure d'une carte
	 * 
	 * @param message
	 */
	public void receivedMapDataPck(ByteBuffer message) {
            System.out.println("received");
		String mapKey = Pck.readString(message);
		Map map = getMapBuildIfAbsent(mapKey);
		map.receiveCommitPck(message);
	}

	/**
	 * Recois un add depuis un
	 * 
	 * @param message
	 */
	public void receiveSharedAddPck(ByteBuffer message) {
		String key = Pck.readString(message);
		Sharable s = getSharable(key);
		if (!(s instanceof SharableGroup)) {
			//logger.info("reception d'un add sur un truc qui n'est pas un groupe");
                        System.out.println("reception d'un add sur un truc qui n'est pas un groupe");
			return;
		}
		int nb = message.getInt();
		ArrayList<Sharable> sharables = new ArrayList<Sharable>();
		for (int i = 0; i < nb; i++) {
			Sharable obj = getSharable(Pck.readString(message));
                        if(obj==null) System.out.println("obj=null !!");
			int incomingVersionCode = message.getInt();
                        System.out.println("incomingVersionCode= "+incomingVersionCode);
			if (obj.getVersionCode() != incomingVersionCode)
				//game.
                            Variables.getClientConnecteur().updateFromServer(obj);
			sharables.add(obj);
		}

		((SharableGroup) s).addFromServer(sharables);
	}

	/**
	 * Recois un del
	 * 
	 * @param message
	 */
	public void receiveSharedDel(ByteBuffer message) {
		String key = Pck.readString(message);
		Sharable s = getSharable(key);
		if (!(s instanceof SharableGroup)) {
			logger.warning("reception d'un del sur un truc qui n'est pas un groupe");
			return;
		}

		int nb = message.getInt();
		ArrayList<Sharable> sharables = new ArrayList<Sharable>();
		for (int i = 0; i < nb; i++) {
			Sharable obj = getSharable(Pck.readString(message));
			@SuppressWarnings("unused")
			int incomingVersionCode = message.getInt();
			sharables.add(obj);
		}

		((SharableGroup) s).delFromServer(sharables);
	}

	/* ********************************************************** *
	 * * 			CHANNEL LISTENER IMPLEMENTS 				* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sun.sgs.client.ClientChannelListener#leftChannel(com.sun.sgs.client
	 * .ClientChannel)
	 */
	//@Override
	public void leftChannel(ClientChannel arg0) {
	 System.out.println("Mouarf le client quitte le channel world, ca reviens à être deconnecter");
                Variables.getClientConnecteur().disconnect("World Channel Leave");
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sun.sgs.client.ClientChannelListener#receivedMessage(com.sun.sgs.
	 * client.ClientChannel, java.nio.ByteBuffer)
	 */
	//@Override
	public void receivedMessage(ClientChannel channel, ByteBuffer message) {
		short code = message.getShort();
		switch (code) {
		case PckCode.COMMIT: receiveCommitPck(message);	break;
		case PckCode.CHAT: //getGame().
                    Variables.getClientConnecteur().getChatSystem().receivedChatMessage(message); break;
		case PckCode.DELETE_OBJECT: dropObject(Pck.readString(message)); break;
		case PckCode.PLAYER_DISCONNECT: receivePlayerDisconnect(message); break;

		default:
			logger.warning("Code packet incconu dans le world : " + code);
		}

	}

	/**
	 * recois un message de déplacement d'un joueur
	 * 
	 * @param message
	 */
	public void receivePlayerMove(final ByteBuffer message) {
            
            System.out.println("World -> receivePlayerMove");
		String key = Pck.readString(message);
		final PlayableCharacter player = getPlayerBuildIfAbsent(key);
		if (player.isPlayer())
			return;
               
		game.enqueue(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				float x1 = message.getFloat();
				float z1 = message.getFloat();
				Moving m = Pck.readEnum(Moving.class, message);
				if (!player.isConnected())
					Variables.getClientConnecteur()
                                                .updateFromServer(player);					

				if (m == Moving.target) {
					player.moveFromTo(x1, z1,
							message.getFloat(), message.getFloat(),
							Pck.readBoolean(message));
				} else if (m == Moving.directionnal) {
					player.moveFromTo(x1, z1,
							message.getFloat(),
							Pck.readBoolean(message));
				}
				return null;
			}
		});
              
	}

	/**
	 * 
	 * @param message
	 */
	public void receivePlayerEndMove(final ByteBuffer message) {
            System.out.println("World -> receivePlayerEndMove() : appelé !!");
		String key = Pck.readString(message);
		final PlayableCharacter player = getPlayerBuildIfAbsent(key);
		if (player.isPlayer())
			return;
                
		Variables.getLaGame().enqueue(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				player.endMoveAt(message.getFloat(), message.getFloat());
				return null;
			}
		});
                
	}

	/**
	 * traite la deconnection d'un joueur
	 * 
	 * @param message
	 */
	public void receivePlayerDisconnect(ByteBuffer message) {
            System.out.println("World -> receivePlayerDisconnect() : appelé !!");
		final PlayableCharacter p = getPlayer(Pck.readString(message));
		 if (p != null)
			Variables.getLaGame().enqueue(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					p.disconnected();
					//playableCharacter.remove(p.getKey());
					return null;
				}
			});
                        
	}

	/**
	 * Traite la reception d'un message de changement d'une clef
	 * 
	 * @param message
	 */
	public void receiveExtendedDataPck(ByteBuffer message) {
		String key = Pck.readString(message);
		Sharable shared = getSharable(key);
		if (shared == null) {
			                 System.err.println(key + " n'existe pas");
			return;
		}

		if (shared instanceof Tool)
			((Tool) shared).receiveToolDataChange(message);
	}

	/* ********************************************************** *
	 * * 						Picking 						* * 
	 * ********************************************************** */

	/**
	 * Calcul l'intersection avec le terrain
	 * 
	 * @param ray
	 * @return
	 */
	public Vector3f pickAt(Ray ray) {
		/* PickResults r = new TrianglePickResults();
		r.setCheckDistance(true);
		for (GraphicWithGround w : groundeds)
			if (w.getGround() != null) {
				r.clear();
				w.getGround().findPick(ray, r);
				if (r.getNumber() > 0) {
					int i = 0;
					while (i < r.getNumber()
							&& r.getPickData(i).getDistance() < 1)
						i++;
					if (i < r.getNumber()
							&& !Float
							.isInfinite(r.getPickData(i).getDistance())) {
						logger.info("pick on : " + w + " : "
								+ r.getPickData(i).getDistance());
						return ray.getOrigin().add(
								ray.getDirection().mult(
										r.getPickData(i).getDistance()));
					}
				}
			}

		for (Map map : maps.values()) {
			Vector3f v = map.pickAt(ray);
			if (v != null)
				return v;
		}
                */
		return null;
	}

	/**
	 * Renvoie l'altitude aux coordonnée indiqué en prenant en compte les
	 * batiment et autre
	 * 
	 * @param x
	 * @param y uniquement en dessous
	 * @param z
	 * @return
	 */
	public float getHeightAt(float x, float y, float z) {
            /*
             * cela fonctionnera uniquement avec des height map
             * 
             * Hamza ABED
             */
		Ray ray = new Ray(new Vector3f(x, y, z), Vector3f.UNIT_Y.negate());
		/* PickResults results = new TrianglePickResults();
		results.setCheckDistance(true);

		for (GraphicWithGround g : groundeds)
			if (g.getGround() != null) {
				results.clear();
				g.getGround().findPick(ray, results);
				if (results.getNumber() > 0) {
					float h = ray.getOrigin().add(
							ray.getDirection().mult(
									results.getPickData(0).getDistance())).y;
					if (!Float.isNaN(h) && !Float.isInfinite(h))
						return Math.max(h,getHeightAt(x, z));
				}
			}

		for (GraphicWalkOver g : walkOvers)
			if (g.canWalkOver()) {
				results.clear();
				g.getGraphic().findPick(ray, results);
				if (results.getNumber() > 0) {
					float h = ray.getOrigin().add(
							ray.getDirection().mult(
									results.getPickData(0).getDistance())).y;
					if (!Float.isNaN(h) && !Float.isInfinite(h))
						return Math.max(h,getHeightAt(x, z));
				}
			}


		return getHeightAt(x, z);
                */
                return 0;
	}

	/**
	 * Renvoie l'objet qui contient un sol à cet endroit
	 * 
	 * @param v
	 * @return
	 */
	public GraphicWithGround getGroundedAt(Vector3f v) {
            /*
		for (GraphicWithGround w : groundeds)
			if (w.getGround() != null) {
				if (w.getGraphic().getWorldBound().contains(v))
					return w;
			}
                        */
		return null;
                
	}

	/**
	 * auteur sur le terrain (ne prend pas en compte les batiments);
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	public float getHeightAt(float x, float z) {
		Map map = getMapAt(x, z);
		if (map == null)
			return worldWaterDeep;
	   float h = map.getHeightAt(x, z);
	
                    if (Float.isNaN(h))
			return worldWaterDeep;
		return h;
                
                   
	}

	/**
	 * renvoie la hauteur à ce point la mais avec un vecteur complet
	 * @param x
	 * @param z
	 * @return
	 */
	public Vector3f getLocAt(float x, float z) {
		return new Vector3f(x,getHeightAt(x, z),z);
	}

	/**
	 * renvoie la pente a ce point ne tiens pas en compte les batiment
	 * @return
	 */
	public float getSlopAt(float x, float z) {
		Map map = getMapAt(x, z);
		if (map == null) return 0;
		/*float h = map.getSlopAt(x, z);
		if (Float.isNaN(h))
			return 0;

		return h; */
                return 0;
	}

	/* ********************************************************** *
	 * * 						ACCES MAP 						* *
	 * ********************************************************** */

	/**
	 * acces à la carte et la construit si ca existe pas
	 */
	private Map getMapBuildIfAbsent(String key) {
            
		if (!maps.containsKey(key)) {
			throw new RuntimeException("Pas encore prevu : getMapBuildIfAbsent");
		}
		return maps.get(key);
               
            
	}

	/**
	 * renvoie la map correspondant à la clef
	 * 
	 * @param key
	 * @return
	 */
	private Map getMap(String key) {
	return maps.get(key);
           
	}

	/**
	 * Renvoie la carte au coordonnée
	 * 
	 * @param x
	 * @param z
	 */
	public Map getMapAt(float x, float z) {
		if (x<0 || z<0) return null;
		return getMapAt((int) (x / mapSize), (int) (z / mapSize));
	}

	/**
	 * Renvoie la carte (x,z)
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	public Map getMapAt(int x, int z) {
		if (x<0 || z<0) return null;
                System.out.println("world->getMapAt->return "+LaComponent.map.prefix() + x + ":" + z);
		return maps.get(LaComponent.map.prefix() + x + ":" + z);
                
	}

	/* ********************************************************** *
	 * * 					ACCES ZONE 							* *
	 * ********************************************************** */

	/**
	 * renvoie la zone corresppondant
	 * 
	 * @param name
	 * @return
	 */
	public Zone getZoneBuildIfAbsent(String name) {
		if (!zones.containsKey(name)) {
			String[] str = name.split(":");
			Zone zone = new Zone(this, Integer.parseInt(str[1]), Integer.parseInt(str[2]));
			this.zones.put(name, zone);
			return zone;
		}
		return zones.get(name); 
            //return null;
	}

	/**
	 * Renvoie la coordonée en x de la zone
	 * 
	 * @param x
	 * @return
	 */
	public int getZoneCoordX(float x) {
		return (int) (x / zoneSize);
	}

	/**
	 * Renvoie la coordonnée en z de la zone
	 * 
	 * @param z
	 * @return
	 */
	public int getZoneCoordZ(float z) {
		return (int) (z / zoneSize);
	}

	/* ********************************************************** *
	 * * 					ACCES JOUEUR 						* *
	 * ********************************************************** */

	/**
	 * Renvoi le joueur derriere la clef
	 * 
	 * @param key
	 * @return
	 */
	public PlayableCharacter getPlayer(String key) {
		
		 if (key.equals(getPlayer().getKey())) return getPlayer();
		
		return playableCharacter.get(key);
            
                
	}

	/**
	 * Renvoi le joueur derriere la clef Ne construit que les Autres
	 * 
	 * @param key
	 * @return
	 */
	public PlayableCharacter getPlayerBuildIfAbsent(String key) {
		 if (key.equals(getPlayer().getKey()))
			return getPlayer(key);
		if (!playableCharacter.containsKey(key)) {
			OtherPlayer other = new OtherPlayer(this, key.split(":")[1]);
			playableCharacter.put(key, other);
		}
		return playableCharacter.get(key);
                
         //   return null;
	}

	/**
	 * Renvoie le groupe correspondant à la clef
	 * @param key
	 * @return
	 */
	public Group getGroupBuildIfAbsent(String key) {
		
            /*
            if (!groups.containsKey(key)) {
			Group gr = new Group(this,key.split(":")[1]);
			groups.put(key,gr);
			game.updateFromServer(gr);
			return gr;
		}
		return groups.get(key);
                */
            return null;
	}

	/**
	 * renvoie les token de group correspondant
	 * @param key
	 * @return
	 */
	private GroupTokens getGroupToken(String key) {
		return getGroupBuildIfAbsent(LaComponent.group.prefix()+key.split(":")[1]).getTokens();
	}

	/* ********************************************************** *
	 * * 					ACCES NPC 							* *
	 * ********************************************************** */

	/**
	 * Renvoi le PNJ derriere la clef Ne construit que les Autres
	 * 
	 * @param key
	 * @return
	 */
	public NonPlayableCharacter getNpcBuildIfAbsent(String key) {
	
            if (!npcs.containsKey(key)) {
			NonPlayableCharacter npc = new NonPlayableCharacter(this, Integer
					.parseInt(key.split(":")[1]));
                        
                        
                        
			npcs.put(key, npc);
                        Variables.getClientConnecteur()
			.updateFromServer(npc);
                        
		}
		return npcs.get(key);
                
            
	}

	/**
	 * @param id
	 * @return 
	 */
	public NonPlayableCharacter getNpcBuildIfAbsent(int id) {
		return getNpcBuildIfAbsent(LaComponent.npc.prefix()+id);
	}


	/* ********************************************************** *
	 * * 				ACCES CONSTRUCTION 						* *
	 * ********************************************************** */

	/**
	 * TODO commentaire World.getLightBuiltIfAbsent
	 * @param key
	 * @return
	 */
        /*
	private MapLight getLightBuiltIfAbsent(String key) {
		if (!lights.containsKey(key)) {
			MapLight l = new MapLight(this,Integer.parseInt(key.split(":")[1]));
			this.lights.put(key, l);
		}
		return lights.get(key);
	}
        */

	/**
	 * Retrouve un objet et le construit si il n'existe pas
	 * <ul>
	 * <li>modif pp : ajout table
	 * </ul>
	 * 
	 * @param key
	 * @return
	 * @author philippe
	 */
        
        
        
	public MapGraphics getMapObject(String key) {
            
            System.out.println("World->getMapObject(key) key ="+key);
		if (!objects.containsKey(key)) {
			MapGraphics obj;
			if (key.matches(LaComponent.object.regex()))
                        {	obj = new BasicMapObject(this, Integer.parseInt(key.split(":")[1])); System.out.println("OBJECT");}
			else if (key.matches(LaComponent.building.regex()))
                        {obj = new BuildingMapObject(this, Integer.parseInt(key.split(":")[1]));System.out.println("BUILDING");}
			else if (key.matches(LaComponent.particul.regex()))
                        {obj = new ParticulEngine(this, Integer.parseInt(key.split(":")[1])); System.out.println("PARTICUL");}
			else if (key.matches(LaComponent.table.regex()))
                        {obj = new MapTable(this, Integer.parseInt(key.split(":")[1])); System.out.println("TABLE");}
			else 
				throw new IllegalArgumentException("type de clef incconnu "+key);
			this.objects.put(key, obj);
		}
		return objects.get(key);
	}

	/**
	 * Retrouve une region et le construit si il n'existe pas
	 * @param key
	 * @return
	 */
        
        
	public Region getRegionBuildIfAbsent(String key) {
		if (!regions.containsKey(key)) {
			this.regions.put(key, new Region(this,Integer.parseInt(key.split(":")[1])));
		}
		return regions.get(key);
	}

	/**
	 * Renvoie l'outil correspondant
	 * 
	 * @param key
	 * @return
	 */
       
	private Tool getToolBuildIfAbsent(String key) {
		if (!tools.containsKey(key)) {
			Tool tool = new Tool(this, Integer.parseInt(key.split(":")[1]));
			this.tools.put(key, tool);
		}
		return tools.get(key);
	}

	/**
	 * Renvoie l'outil correspondant
	 * 
	 * @param key
	 * @return
	 */
        
        
	public Tool getToolBuildIfAbsent(int id) {
		return getToolBuildIfAbsent(LaComponent.tool.prefix()+id);
	}

	/**
	 * Renvoie l'item la crais si elle n'existe pas
	 * 
	 * @param key
	 * @return
	 */
        
	public MapTable getTableBuildIfAbsent(String key) {
		if (!tables.containsKey(key)) {
			MapTable obj = new MapTable(this, Integer.parseInt(key.split(":")[1]));
			this.tables.put(key, obj);
			Variables.getClientConnecteur().updateFromServer(obj);
		}
		return tables.get(key);
	}
	
	/**
	 * Renvoie l'item la crais si elle n'existe pas
	 * 
	 * @param key
	 * @return
	 */
        
       
	public Item getItemBuildIfAbsent(String key) {
		if (!items.containsKey(key)) {
			Item obj = new Item(this, Integer.parseInt(key.split(":")[1]));
			this.items.put(key, obj);
			Variables.getClientConnecteur().updateFromServer(obj);
		}
		return items.get(key);
	}

	/**
	 * Renvoie le skill la crais si elle n'existe pas
	 * 
	 * @param key
	 * @return
	 */
       
	private Skill getSkillBuildIfAbsent(String key) {
		if (!skills.containsKey(key)) {
			Skill obj = new Skill(this, Integer.parseInt(key.split(":")[1]));
			this.skills.put(key, obj);
			Variables.getClientConnecteur().updateFromServer(obj);
		}
		return skills.get(key);
	}

	/**
	 * idem mais avec un identifiant
	 * @param skill
	 * @return
	 */
       
	public Skill getSkillBuildIfAbsent(int skill) {
		return getSkillBuildIfAbsent(LaComponent.skill.prefix()+skill);
	}

	/**
	 * Renvoie le dialog le crais si absent
	 * 
	 * @param key
	 * @return
	 */
        
       
	public Dialog getDialogBuildIfAbsent(String key) {
		if (!dialogs.containsKey(key)) {
			Dialog obj = new Dialog(this, Integer.parseInt(key.split(":")[1]));
			this.dialogs.put(key, obj);
		}
		return dialogs.get(key);
	}

	/**
	 * TODO commentaire World.getGameDataBuildIfAbsent
	 * @param key
	 * @return
	 */
        
       
	private GameData getGameDataBuildIfAbsent(String key) {
		if (!gameDatas.containsKey(key)) {
			GameData gd = new GameData(this, Integer.parseInt(key.split(":")[1]));
			this.gameDatas.put(key, gd);
		}
		return gameDatas.get(key);
	}

	/**
	 * Renvoie la ressource LGF le creer si absent
	 * 
	 * @param key
	 * @return
	 */
// pas opérationnele en v31	
//	public Lgf getLgfBuildIfAbsent(String key) {
//		if (!lgfs.containsKey(key)) {
//			Lgf obj = new Lgf(this, Integer.parseInt(key.split(":")[1]));
//			this.lgfs.put(key, obj);
//		}
//		return lgfs.get(key);
//	}

	/**
	 * Renvoie la todo  la construit si absente
	 * 
	 * @param key
	 * @return
	 */
        
       
	private Task getTaskBuildIfAbsent(String key) {
		if (!tasks.containsKey(key)) {
			Task obj = new Task(this, Integer.parseInt(key.split(":")[1]));
			this.tasks.put(key, obj);
		}
		return tasks.get(key);
	}

	/**
	 * idem avec un identifiant numeraire
	 * @param taskId
	 * @return
	 */
        
       
	public Task getTaskBuildIfAbsent(int taskId) {
		return getTaskBuildIfAbsent(LaComponent.task.prefix()+taskId);
	}

	/**
	 * Renvoie le script le crais si il n'existe pas
	 * 
	 * @param key
	 * @return
	 */
        
        
	public Script getScriptBuildIfAbsent(String key) {
		if (!scripts.containsKey(key)) {
			Script obj = new Script(this, Integer.parseInt(key.split(":")[1]));
			this.scripts.put(key, obj);
		}
		return scripts.get(key);
	}

	/**
	 * Renvoie le slideshow
	 * @param string
	 * @return
	 */
        
        
	public SlideShow getSlideShowBuildIfAbsent(String key) {
		if (!slides.containsKey(key)) {
			SlideShow s = new SlideShow(this,Integer.parseInt(key.split(":")[1]));
			this.slides.put(key,s);
		}
		return slides.get(key);
	}


	/**
	 * @return
	 */
        
       
	public WorldTokens getWorldToken() {
		if (worldTok == null) {
			worldTok = new WorldTokens(this);
		}
		return worldTok;
	}

	/**
	 * Renvoie les token du joueur
	 * 
	 * @param key
	 * @return
	 */
	public PlayerTokens getPlayerTokensBuildIfAbsent(String key) {
		return getPlayerBuildIfAbsent(
				LaComponent.player.prefix() + key.split(":")[1]).getTokens();
	}

	/**
	 * Renvoie les targets du joueur
	 * 
	 * @param key
	 * @return
	 */
	public PlayerTargets getPlayerTargetsBuildIfAbsent(String key) {
		return getPlayerBuildIfAbsent(
				LaComponent.player.prefix() + key.split(":")[1]).getTargets();
	}

	/**
	 * Renvoie les taches du joueur
	 * 
	 * @param key
	 * @return
	 */
	public PlayerTasks getPlayerTasksBuildIfAbsent(String key) {
		return getPlayerBuildIfAbsent(
				LaComponent.player.prefix() + key.split(":")[1]).getTasks();
	}

	/**
	 * Renvoie les items d'un joueur
	 * 
	 * @param key
	 * @return
	 */
	public PlayerItems getPlayerItemBuildIfAbsent(String key) {
		return getPlayerBuildIfAbsent(
				LaComponent.player.prefix() + key.split(":")[1]).getItems();
	}

	/**
	 * Renvoie les items d'un joueur
	 * 
	 * @param key
	 * @return
	 */
	public PlayerSkills getPlayerSkillsBuildIfAbsent(String key) {
		return getPlayerBuildIfAbsent(
				LaComponent.player.prefix() + key.split(":")[1]).getSkills();
	}

	/* ********************************************************** *
	 * * 					ACCES SHARED GENERIC 				* *
	 * ********************************************************** */

	/**
	 * Acces à un objet partagé
	 * <ul>
	 * <li> modif pp : ajout table
	 * </ul>
	 * 
	 * @param key
	 * @author philippe pernelle
	 */
	public Sharable getSharable(String key) {
		
            switch (LaComponent.type(key)) {
		case table: System.out.println("\nWORLD table \n"); return getTableBuildIfAbsent(key);
		case building:System.out.println("\nWORLD building \n"); return getMapObject(key);
		case dialog:System.out.println("\nWORLD dialog \n"); break;//return getDialogBuildIfAbsent(key);
		case gamedata:System.out.println("\nWORLD gamedata \n"); break;//return getGameDataBuildIfAbsent(key);
		case group:return getGroupBuildIfAbsent(key);
		case groupToken: return getGroupToken(key);
		case item:System.out.println("\nWORLD item \n"); break; // return getItemBuildIfAbsent(key);
		case light:System.out.println("\nWORLD light \n"); break;// return getLightBuiltIfAbsent(key);
		
		case map:System.out.println("\nWORLD map \n"); return getMap(key);
		case npc: System.out.println("\nWORLD NPC \n key= "+key); return getNpcBuildIfAbsent(key);
		case object: System.out.println("\nWORLD object \n"); return getMapObject(key);
		case particul:System.out.println("\nWORLD particul \n"); break;// return getMapObject(key);
		case player: System.out.println("\nWORLD retourne player \n"); return getPlayerBuildIfAbsent(key);
		case playerBag: return getPlayerItemBuildIfAbsent(key);
		case playerSkill: return getPlayerSkillsBuildIfAbsent(key);
		case playerTarget: return getPlayerTargetsBuildIfAbsent(key);
		case playerTask: return getPlayerTasksBuildIfAbsent(key);
		case playerToken: return getPlayerTokensBuildIfAbsent(key);
		case region: System.out.println("\nWORLD getRegionBuild \n"); return getRegionBuildIfAbsent(key);
		case script: return getScriptBuildIfAbsent(key);
		case skill: System.out.println("\nWORLD skill \n"); return getSkillBuildIfAbsent(key);
		case slides: System.out.println("\nWORLD slides \n"); return getSlideShowBuildIfAbsent(key);
		case task: System.out.println("\nWORLD task \n"); return getTaskBuildIfAbsent(key);
		case tool: System.out.println("\nWORLD tool \n"); return getToolBuildIfAbsent(key);
		case zone: return getZoneBuildIfAbsent(key);
		case worldToken: System.out.println("\nWORLD world token \n"); break;//return getWorldToken();
		default:
			throw new IllegalArgumentException("type de clef incconnu "+key);
		}
               
            return null;
	}

	/**
	 * Indique si l'objet est a jour avec le server
	 * 
	 * @param key
	 * @return
	 */
	public boolean isUpdate(String key) {
		Sharable s = getSharable(key);
		return isUpdate(s);
	}

	/**
	 * Indique si l'objet est a jour avec le server
	 * 
	 * @param key
	 * @return
	 */
	public boolean isUpdate(Sharable s) {
		if (s == null)
			return true;
		// TODO Amelioré le test pour les object graphique
		logger.info("IsUpdate "+ s.getVersionCode());
		return s.getVersionCode() > 0;
	}

	/* ********************************************************** *
	 * * 				Sharable - IMPLEMENTS 					* *
	 * ********************************************************** */
	/*
	 * (non-Javadoc)
	 * 
	 * @see client.network.Sharable#getKey()
	 */
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see client.network.Sharable#getVersionCode()
	 */
	//@Override
	public int getVersionCode() {
		return versionCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	//@Override
	public void addData(Pck pck) {
		throw new RuntimeException("NYI");
	}
	
	/* ********************************************************** *
	 * * 					script : temps 						* *
	 * ********************************************************** */

	@ScriptableMethod(description="Renvoi le nombre de jour dans le referenciel Posix")
	public long day() {
		return System.currentTimeMillis()/(1000*60*60*24);//+5;
	}

	@ScriptableMethod(description="Renvoi le nombre d'heure dans le referenciel Posix")
	public long hour() {
		return System.currentTimeMillis()/(1000*60*60);
	}

	@ScriptableMethod(description="Renvoi le nombre de minute dans le referenciel Posix")
	public long min() {
		return System.currentTimeMillis()/(1000*60);
	}


	/* ********************************************************** *
	 * * 			script : Gestion des Tokens 				* *
	 * ********************************************************** */

	/**
	 * test si le monde à le token dans la categorie indiqué.
	 */
	@ScriptableMethod(description="Test si le monde a ce token dans la cat indiqué")
	public boolean hasToken(String cat, String token) {
		return getWorldToken().hasToken(cat, token);
            
	}

	/**
	 * test si le monde à le token dans la categorie default
	 * 
	 * @param token
	 * @return
	 */
	@ScriptableMethod(description="Test si le monde a ce token dans la cat par defaut")
	public boolean hasToken(String token) {
		return getWorldToken().hasToken(token);
            
            
	}

	/**
	 * Ajout un token au monde dans la categorie indiqué avec une valeur de type
	 * String. le remplace si il existe déjà PlayableCharacter.addToken
	 * 
	 * @param cat
	 * @param token
	 * @param value
	 */
	@ScriptableMethod(description="change la valeur du token dans la categorie indiqué")
	public void setToken(String cat, String token, String value) {
		getWorldToken().addToken(cat, token, value);
	}

	@ScriptableMethod(description="change la valeur du token dans la categorie indiqué en long")
	public void setToken(String cat, String token, long value) {
		getWorldToken().addToken(cat, token, Long.toString(value));
	}
	
	/**
	 * Idem mais dans la categorie par defaut
	 * 
	 * @param token
	 * @return
	 */
	@ScriptableMethod(description="change la valeur du token dans la categorie par defaut")
	public void setToken(String token, String value) {
		getWorldToken().addToken(token, value);
	}

		
	/**
	 * supprime un token dans la categorie par defaut
	 * 
	 * @param token
	 */
	@ScriptableMethod(description="supprime le token dans la categorie par defaut")
	public void delToken(String token) {
		getWorldToken().delToken(token);
	}

	/**
	 * supprime un token dans la categorie indiqué
	 * 
	 * @param token
	 */
	@ScriptableMethod(description="supprime le token dans la categorie donné")
	public void delToken(String cat, String token) {
		getWorldToken().delToken(cat, token);
	}

	/**
	 * Renvoie la valeur Chaine du token sous forme de long
	 * @param cat
	 * @param tok
	 * @return
	 */
	@ScriptableMethod(description="renvoie la valeur string du token")
	public Long getTokenAsLong(String cat, String token) {
		return getWorldToken().getTokenAsLong(cat, token);
            
	}

	/**
	 * Renvoie la valeur Chaine du token
	 * @param cat
	 * @param tok
	 * @return
	 */
	@ScriptableMethod(description="renvoie la valeur string du token")
	public String getTokenAsString(String cat, String token) {
		return getWorldToken().getTokenAsString(cat, token);
             
	}

	/**
	 * Renoie la valeur numerale du token, 0 si il n'existe pas ou si il ne
	 * s'agit pas d'une chaine.
	 * 
	 * Commentaire spécial pour dallas : On s'en tape du type float ou integer
	 * car le javascript s'en tape également. Si tu me crais un getTokenAsInt je
	 * te fou les couilles dans un tuperware.
	 * 
	 * @param cat
	 * @param token
	 * @return
	 */
	@ScriptableMethod(description="renvoie la valeur numerere du token")
	public float getToken(String cat, String token) {
		return getWorldToken().getToken(cat, token);
             
	}

	/**
	 * Renvoie la valeur numerale du token, 0 si il n'existe pas ou si il ne
	 * s'agit pas d'une chaine.
	 * 
	 * Commentaire spécial pour dallas : On s'en tape du type float ou integer
	 * car le javascript s'en tape également. Si tu me crais un getTokenAsInt je
	 * te fou les couilles dans un tuperware.
	 * 
	 * @param token
	 * @return
	 */
	@ScriptableMethod(description="renvoie la valeur numerere du token")
	public float getToken(String token) {
		return getWorldToken().getToken(token);
             
	}

	/**
	 * supprime tous les token du monde
	 */
	@ScriptableMethod(description="supprimme tous les token du monde")
	public void clearAllToken() {
		getWorldToken().clearAll();
	}


	/* ********************************************************** *
	 * * 			script : Gestion des GameData 				* *
	 * ********************************************************** */

	/**
	 * game data
	 * @throws InterruptedException 
	 */
	@ScriptableMethod(description="creation d'un nouveau game data. attention c'est une methode plutot lente car synchrone avec le serveur")
	public GameData createGameData() throws InterruptedException {
		final String[] out = new String[1]; 
		synchronized (out) {
			//game.
                    Variables.getClientConnecteur().getServerEditor().createAndCall(LaComponent.gamedata, new CreatorCallBack() {
				@Override
				public void created(String key) {
					synchronized (out) {
						out[0] = key;
						out.notifyAll();
					}
				}
			});
			out.wait();
		}
		return getGameDataBuildIfAbsent(out[0]);
                
               
	}
	
	@ScriptableMethod(description="Renvoie la donner de jeux correspondant à la clef")
	public GameData getGameData(String key) {
		return getGameDataBuildIfAbsent(key);
             
	}

	@ScriptableMethod(description="permet de lancer une recher sur les game data")
	public GameDataSearch findGameData() {
		return new GameDataSearch(gameDatas.values());
	}
	
	/* ********************************************************** *
	 * * 					Getters / Setters 					* *
	 * ********************************************************** */

	/**
	 * @return the skybox
	 */
        /*
	public Sky getSky() {
		return sky;
	}
*/
	/**
	 * @return the waterPlan
	 */
        /*
	public WaterPlan getWaterPlan() {
		return water;
	}
*/
	/**
	 * @return the shadow
	 */
        /*
        
	public Shadow getShadow() {
		return shadow;
	}
*/
	/**
	 * @return the channel
	 */
	public ClientChannel getChannel() {
		return channel;
	}

	/**
	 * @param channel
	 *            the channel to set
	 */
        
	public void setChannel(ClientChannel channel) {
		this.channel = channel;
	}

	/**
	 * @return the worldScaleY
	 */
       
	public float getWorldScaleY() {
		return worldScaleY;
	}

	/**
	 * @return the worldWaterDeep
	 */
       
	public float getWorldWaterDeep() {
		return worldWaterDeep;
	}

	/**
	 * @return the mapSize
	 */
	public float getMapSize() {
		return mapSize;
	}

	/**
	 * @return the zoneSize
	 */
	public float getZoneSize() {
		return zoneSize;
	}

	/**
	 * @return the game
	 */
	public LaGame getGame() {
		return game;
	}

	/**
	 * @return the player
	 */
        
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return the collidables
	 */
	public ArrayList<GraphicCollidable> getCollidables() {
		return collidables;
           
	}

	/**
	 * envoie le moteur d'execution des scripts
	 * 
	 * @return
	 */
       
	public ScriptExecutor getScriptExecutor() {
		if (scriptExecutor == null)
			scriptExecutor = new ScriptExecutor(this);
		return scriptExecutor;
	}

	/**
	 * Renvoie la region en ce point
	 * @param newPos
	 * @return
	 */
       
	public Region getRegionAt(Vector3f v) {
		for (Region r : regions.values()) 
			if (Math.abs(v.x-r.getX())<r.getW() && Math.abs(v.z-r.getZ())<r.getH())
				return r;
		return null;
	}
	/**
	 * renvoie la zone à ce point
	 * @param x
	 * @param z
	 * @return
	 */
	public Zone getZone(float x, float z) {
		return getZoneBuildIfAbsent(LaComponent.zone.prefix()+getZoneCoordX(x)+":"+getZoneCoordZ(z));
	}


	/**
	 * @return the regionVisible
	 */
	public boolean isIdenObjectVisible() {
		return idenObjectVisible;
           
	}

	/**
	 * @param idenObjectVisible the regionVisible to set
	 */
	public void setIdenObjectVisible(boolean idenObjectVisible) {
            System.out.println("World -> setIdenObjectVisible(bool) : vide");
		/*this.idenObjectVisible = idenObjectVisible;
		for(Region r : regions.values())
			r.setVisible(idenObjectVisible);
		for(MapLight l : lights.values())
			l.setVisible(idenObjectVisible);
                        */
	}

	/**
	 * Renvoie le gestionnaire de FTP
	 * @return
	 */
        
	public FtpManager getFtp() {
		if (ftp == null) {
			ftp = new FtpManager(this,Variables.getClientConnecteur().isUseSSH());
		}
		return ftp;
	}

   /* ********************************************************** *
	 * * 				Sharable - IMPLEMENTS 					* *
	 * ********************************************************** */
	/*
	 * (non-Javadoc)
	 * 
	 * @see client.network.Sharable#getKey()
	 */
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */

    public String getKey() {
        
       return LaComponent.world.prefix(); //To change body of generated methods, choose Tools | Templates.
    }
    
        
        
        
	
     
	/**
	 * @return
	 */
       
	public Collection<Group> getGroups() {
		return groups.values();
	}

	/**
	 * Renvoie la liste des joueur connu
	 * @return
	 */
        
	public Collection<PlayableCharacter> getPlayers() {
		return playableCharacter.values();
	}

	/**
	 * change la couleur du brouillard
	 * @param fogR
	 * @param fogG
	 * @param fogB
	 * @param fogA
	 */
        /*
	public void setFogColor(float fogR, float fogG, float fogB, float fogA) {
		game.getFs().getColor().set(fogR,fogG,fogB,fogA);
		game.getFs().setNeedsRefresh(true);
	}
*/







}
