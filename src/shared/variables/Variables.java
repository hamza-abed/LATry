/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.variables;

import client.LaGame;
import client.NGUI_LA;
import client.chat.ChatSystem;
import client.hud.Hud;
import client.hud3D.MoveCursor;
import client.map.World;
import client.map.character.Player;
import client.network.SimpleClientConnector;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.controls.Label;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
/**
 *
 * @author admin
 */
public class Variables {
    
    
    private static boolean progressBarBound=false;

    public static boolean isProgressBarBound() {
        return progressBarBound;
    }

    public static void setProgressBarBound(boolean progressBarBound) {
        Variables.progressBarBound = progressBarBound;
    }
    private static NGUI_LA niftyGUI;

    public static NGUI_LA getNiftyGUI() {
        return niftyGUI;
    }

    public static void setNiftyGUI(NGUI_LA niftyGUI) {
        Variables.niftyGUI = niftyGUI;
    }
 
   private static Properties props;

   private static  Nifty nifty;

    public static Nifty getNifty() {
        return nifty;
    }

    public static void setNifty(Nifty nifty) {
        Variables.nifty = nifty;
    }
   
   
    public static Properties getProps() {
        return props;
    }

    public static void setProps(Properties props) {
        Variables.props = props;
    }

    public static ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public static void setExecutor(ThreadPoolExecutor executor) {
        Variables.executor = executor;
    }
  
    private static Label connectionStatusLabel;

    public static Label getConnectionStatusLabel() {
        return connectionStatusLabel;
    }

    public static void setConnectionStatusLabel(Label connectionStatusLabel) {
        Variables.connectionStatusLabel = connectionStatusLabel;
    }

    /*
     * Ensemble de contenus des Threads de l'exécuteur
     */
    private static ArrayList<Future> futures=new ArrayList<Future>();
    
    /*
     * Ensemble de méthodes à exécuter dans un thread simultanné
     */
    private static ArrayList<Callable> findWay=new ArrayList<Callable>();
    private static LaGame laGame;
    public static MoveCursor moveCursor;
    private static World world;
    
    public static MoveCursor getMoveCursor() {
        return moveCursor;
    }

    public static void setMoveCursor(MoveCursor moveCursor) {
        Variables.moveCursor = moveCursor;
    }
    private static de.lessvoid.nifty.controls.Console console;
    private static SimpleClientConnector clientConnecteur;
    private static  Player mainPlayer;
   /* private static  Player playerMission;

    public static Player getPlayerMission() {
        return playerMission;
    }

    public static void setPlayerMission(Player playerMission) {
        Variables.playerMission = playerMission;
    }*/
    
    public static int mapN=0;
    private static boolean mapsLoaded=false;

    public static boolean isMapsLoaded() {
        return mapsLoaded;
    }

    public static void setMapsLoaded(boolean mapsLoaded) {
        Variables.mapsLoaded = mapsLoaded;
    }
    
    
    private static boolean playerModelLoaded=false;

    public static boolean isPlayerModelLoaded() {
        return playerModelLoaded;
    }

    public static void setPlayerModelLoaded(boolean playerModelLoaded) {
        Variables.playerModelLoaded = playerModelLoaded;
    }
    
    private static Spatial sceneModel;

    public static Spatial getSceneModel() {
        return sceneModel;
    }

    public static World getWorld() {
        return world;
    }

    public static void setWorld(World world) {
         System.out.println("Variables -> setWorld");
        Variables.world = world;
    }

    public static void setSceneModel(Spatial sceneModel) {
        Variables.sceneModel = sceneModel;
    }
    

    public static Camera getCam() {
        return laGame.getCamera();
    }

  
    public static Player getMainPlayer() {
        return mainPlayer;
    }

    public static void setMainPlayer(Player mainPlayer) {
        Variables.mainPlayer = mainPlayer;
    }
    
    public static void setConsole(Console console) {
        Variables.console = console;
    }

    private static boolean clientConnectorSetted=false;

    public static boolean isClientConnectorSetted() {
        return clientConnectorSetted;
    }
    public static void setClientConnecteur(SimpleClientConnector clientConnecteur) {
        Variables.clientConnecteur = clientConnecteur;
        clientConnectorSetted=true;
    }

    public static Console getConsole() {
        return console;
    }

    public static SimpleClientConnector getClientConnecteur() {
     //   if(clientConnecteur==null) clientConnecteur=new SimpleClientConnector();
        return clientConnecteur;
    }
    public static LaGame getLaGame()
    {
        return laGame;
    }
    
    public static void setLaGame(LaGame main)
    {
        Variables.laGame=main;
       
    }
    
    private static ThreadPoolExecutor executor;
    	public static ThreadPoolExecutor getTaskExecutor() {
		if (executor == null) {
			executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Integer.parseInt(props
					.getProperty("la.max.parallel.task", "8")));
			
		}
		//logger.info("state : "+executor.getActiveCount()+":"+executor.getQueue().size());
		return executor;
	}
    private static ChatSystem chatSystem;
        

    /**
	 * renvoie le gestionnaire de chat
	 * 
	 * @return
	 */
	public static ChatSystem getChatSystem() {
		if (chatSystem == null) {
			chatSystem = new ChatSystem(getLaGame());
		}
		return chatSystem;
	}
        /**
	 * indique si le jeux tourne en mode editeur Graphic
	 */
	private static boolean editMode;
        
        /**
	 * indique si le jeux est en mode edit ou pas
	 * 
	 * @return
	 */
	public static boolean isEditMode() {
		return editMode;
	}
        
	/**
	 * active / desactiv le mode d'edition
	 * 
	 * @param b
	 */
	public static void setEditMode(boolean b) {
		editMode = b;
	}
        
       
        /**
	 * Indique la fin du jeux
	 */
	private static boolean finished = false;
        
        /**
	 * @return the finished
	 */
	public static boolean isFinished() {
		return finished;
	}
        
        public static void setFinished(boolean b)
        {
            finished=b;
        }
private static String language;
	public static String getLanguage() {
            
            if(language ==null) language = props.getProperty("la.language","en");
		return language;
	}
        
        
        /**
	 * Affichage en tete d'ecran
	 */
	private static Hud hud;
       /**
	 * @return the hud
	 */
	public static Hud getHud() {
            if(hud==null) hud = new Hud(laGame);
		return hud;
	}
        
     
    public static ArrayList<Future> getFutures() {
        return futures;
    }

    public static void setFutures(ArrayList<Future> futures) {
        Variables.futures = futures;
    }

    public static boolean findWayExist(Callable c)
    {
        for(int i=0;i<findWay.size();i++)
            if(findWay.get(i).equals(c)) return true;
        
        return false;
    }
    
    public static ArrayList<Callable> getFindWay() {
        return findWay;
    }

    public static void setFindWay(ArrayList<Callable> findWay) {
        Variables.findWay = findWay;
    }
         
}
