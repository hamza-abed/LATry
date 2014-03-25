/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.input;

import client.hud3D.MoveCursor;
import com.jme3.input.controls.ActionListener;
import com.jme3.scene.Spatial;
import shared.variables.Variables;

/**
 *
 * @author Hamza ABED
 * Dans la nouvelle version JME3, la classe MouseInputListener n'existe plus
 * c'est pour quoi on a recours à la classe ActionListener
 */
public class MainGameMouseListener implements ActionListener{

    Spatial sceneModel;
   
    public MainGameMouseListener(Spatial sceneModel)
    {
        super();
        this.sceneModel=sceneModel;
    }
    public void onAction(String name, boolean isPressed, float tpf) {
       if(name.equals("LClick"))
{
Variables.getMoveCursor().afficherFlecheDestination();
}
    }
    
}
