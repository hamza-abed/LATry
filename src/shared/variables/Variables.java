/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.variables;

import client.LaGame;
import client.hud3D.MoveCursor;
import client.map.World;
import client.map.character.Player;
import client.network.SimpleClientConnector;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.controls.Console;

/**
 *
 * @author admin
 */
public class Variables {
    
    
    

    
    
    
    
   
   
  
    
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
    private static Spatial sceneModel;

    public static Spatial getSceneModel() {
        return sceneModel;
    }

    public static World getWorld() {
        return world;
    }

    public static void setWorld(World world) {
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
        if(clientConnecteur==null) clientConnecteur=new SimpleClientConnector();
        return clientConnecteur;
    }
    public static LaGame getLaGame()
    {
        return laGame;
    }
    
    public static void setLaGame(LaGame main)
    {
        Variables.laGame=main;
        world=new World(laGame);
    }
    
}
