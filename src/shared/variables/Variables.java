/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.variables;

import client.LaGame;
import client.NGUI_LA;
import client.hud3D.MoveCursor;
import client.map.World;
import client.map.character.Player;
import client.network.SimpleClientConnector;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.controls.Label;
import java.util.Properties;
import java.util.concurrent.Executors;
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
    private static  Player playerMission;

    public static Player getPlayerMission() {
        return playerMission;
    }

    public static void setPlayerMission(Player playerMission) {
        Variables.playerMission = playerMission;
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
        /**
	 * gestionnaire de tache
	 * 
	 * @return
	 */
        private static ScheduledThreadPoolExecutor scheduledExecutor;
	public static ScheduledThreadPoolExecutor getSchedulerTaskExecutor() {
		if (scheduledExecutor == null) {
			scheduledExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(
					Integer.parseInt(props.getProperty("la.scheduled.task", "10")));
			
			
			

		}
		return scheduledExecutor;
	}

    
}
