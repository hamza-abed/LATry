/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.hud.missionStatus;

import com.jme3.app.state.AbstractAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import shared.variables.Variables;
/**
 *
 * @author admin
 */
public class MissionStatus extends Node{
    

    Box centerBox;
    private static final float INCLINAISON = FastMath.HALF_PI;
    private float ROTATION = 0; // the rotatioon of the box, max =180°
    private Box box_de_status; // The box showing the mission status
    private Box center; // Le cube montrant l'evolution de la mission
    private float boxHeight = 45f;
    private Node missionStatusNode;
    private Geometry statusBoxGeo;
    
    
    public MissionStatus()
    {
        super("missionStatus");
        createBoxes();
    }
    
   private void createBoxes()
   {
       Vector3f centerOfTheBox = new Vector3f(0, 0, 0);
      Box statusBox= new Box(centerOfTheBox,boxHeight, boxHeight, boxHeight);
       statusBoxGeo = new Geometry("statusBox", statusBox);
       Material mat = new Material(Variables.getLaGame().getAssetManager(), 
        "Common/MatDefs/Misc/Unshaded.j3md");
        
   
       
   mat.setColor("Color", new ColorRGBA(0.5f, 0.3f, 0.1f, 0.3f)); // with Unshaded.j3md
   
    statusBoxGeo.setMaterial(mat);
   
    statusBoxGeo.rotate(1.6f, 0, 0);          // Rotate it a bit
    
    this.attachChild(statusBoxGeo);
    
    
    System.out.println("ici cerateBoxes");
  
   }
    
    private float variation=0.000f;
   
    public void update(float tpf)
    {
        
        System.out.println("ici modif");
        if(ROTATION>3) {ROTATION=0.1f; 
				
		}
		else ROTATION+=0.01;
	//	Matrix3f m=new Matrix3f(0, 0, ROTATION, 0, 0, 0, 0, 0, 0);
		statusBoxGeo.setLocalRotation(new Quaternion(ROTATION, 0.1f, 0.2f, 0.8f));
		
		variation+=0.0001f;
		//if(variation>0.005f) {variation=0.001f; center.setLocalScale(initialScale);
		
		
		}
    

    
    


}