/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.input;

import client.hud3D.MoveCursor;
import client.map.character.Player;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.scene.Spatial;
import shared.variables.Variables;

/**
 *
 * @author Hamza ABED
 * Dans la nouvelle version JME3, la classe MouseInputListener n'existe plus
 * c'est pour quoi on a recours à la classe ActionListener
 */
public class MainGameListener implements ActionListener, AnalogListener{

    Spatial sceneModel;
    Player player;
   
    public MainGameListener(Spatial sceneModel)
    {
        super();
        this.sceneModel=sceneModel;
        player=Variables.getMainPlayer();
    }
    public void onAction(String name, boolean isPressed, float tpf) {
       if(name.equals("LClick"))
{
Variables.getMoveCursor().afficherFlecheDestination();
}
    }

    public void onAnalog(String name, float value, float tpf) {
       //System.out.println("this is analog from mouse listener "+name);
       if(!name.equals("LClick"))
        player.freeMovePlayer(name);
    }
    
}
