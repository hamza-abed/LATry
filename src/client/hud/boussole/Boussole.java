/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.hud.boussole;

import client.LaGame;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import shared.variables.Variables;

/**
 *
 * @author admin
 */
public class Boussole extends Node{
    private static final long serialVersionUID = 7633204763292312313L;
	
	
	private LaGame game;
	private Quaternion qPlan;
	private Quaternion q;
	

	static final float RADIUS = 9f;
	static final float CENTER_RADIUS = .4f;
	private static final float A = .35f;
	static final float FONT_SIZE = 1.2f;
	static final ColorRGBA FONT_COLOR = new ColorRGBA(0,0,0,1);
	static final float CONE_RADIUS = .5f;
	static final float CONE_LENGHT = .8f;
	private static final ColorRGBA DISK_COLOR = new ColorRGBA(0, 0.9f, 0.9f, A);// j'ai changé 0.9f à 0
	private static final ColorRGBA CENTER_COLOR = new ColorRGBA(0f, 0f, 0f, 1);

	private static final float INCLINAISON = FastMath.HALF_PI;

	public static final float DIST_VIEW = 50f;  // 25m
	/**
	 * 
	 */
        Node diskBoussole; // je les ai mis dans des Node parce que la méthode GetLocalRotation
       // n'est plus supporté pour les type Cylender
        public Boussole()
        {
            super("boussole-node");
            diskBoussole=new Node("Disk");
            
            Cylinder disk = new Cylinder(5,32,RADIUS,0.1f,true);
            Geometry diskGeo = new Geometry("DiskBoussole", disk);
            diskGeo.getLocalRotation().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
                
		//diskBoussole.setDefaultColor(DISK_COLOR);
		
		
		Cylinder center = new Cylinder(5,32,CENTER_RADIUS,0.15f,true);
                Geometry centerBssGeo= new Geometry("DiskBoussole", center);
		centerBssGeo.getLocalRotation().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
		//centerBssGeo.set(CENTER_COLOR);
		

		qPlan = new Quaternion();
		qPlan.fromAngleNormalAxis(-INCLINAISON, Vector3f.UNIT_X);

                
                diskBoussole.attachChild(diskGeo);
                diskBoussole.attachChild(centerBssGeo);
                
                this.attachChild(diskBoussole);
 
		

		//this.attachChild(new TargetNode(this,"target",new Vector3f(512,0,512)));

		//this.attachChild(new Box("essaie", new Vector3f(), 1, 1, 1));

		q = new Quaternion();
                
                /*
                 * Assigning materials  for all objects
                 * 
                 */
       Material matlaser = new Material(Variables.getLaGame().getAssetManager(),   "Common/MatDefs/Misc/Unshaded.j3md");
    
     
                matlaser.setColor("Color", ColorRGBA.Orange);
                matlaser.setColor("GlowColor", ColorRGBA.Red);
                
                

	//	this.updateRenderState();
		this.updateGeometricState();

        }
        
        
        
        
       
	public void updateGeometricState(float time, boolean initiator) {
            game= Variables.getLaGame();
            
		getLocalRotation().fromAngleNormalAxis(INCLINAISON, Vector3f.UNIT_X);
		q.fromAngleNormalAxis(-game.getCamera().getRotation().getY(), Vector3f.UNIT_Y);
		getLocalRotation().multLocal(q);

		//q.fromAngleNormalAxis(game.getCameraControler().getRy(), Vector3f.UNIT_Y);
	//	N.getLocalRotation().set(q.mult(qPlan));

	//	super.updateGeometricState(time, initiator);
	}

        
        
        
}
