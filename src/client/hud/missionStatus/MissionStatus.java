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
package client.hud.missionStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import shared.constants.LaConstants;
import sun.security.ssl.Debug;
import client.LaGame;
import client.map.character.Player;
import client.utils.FileLoader;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import shared.variables.Variables;
/*
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.shape.RoundedBox;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.WireframeState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.angelfont.Rectangle;
import com.jmex.font3d.Font3D;
import com.jmex.font3d.Text3D;
import com.jmex.font3d.effects.Font3DBorder;
import com.jmex.font3d.effects.Font3DGradient;
*/
/**
 * Bousolle 3D dans l'interface
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class MissionStatus extends Node implements Callable<Void>{
	private static final long serialVersionUID = 7633204763292312313L;
	private static final Logger logger = Logger.getLogger("MissionStatus");
	//private ZBufferState zs; ///jme
	//private Text3D N;       ///jme
	//private Text3D pourcentageAvancement;
	//private WireframeState ws; /////jme
	private LaGame game;
	private Quaternion qPlan;
	private Quaternion q;
	//private Font3D font;

	static final float RADIUS = 9f;
	static final float CENTER_RADIUS = .4f;
	private static final float A = .35f;
	static final float FONT_SIZE = 1.2f;
	static final ColorRGBA FONT_COLOR = new ColorRGBA(0,0,0,1);
	static final float CONE_RADIUS = .5f;
	static final float CONE_LENGHT = .8f;
	private static final ColorRGBA DISK_COLOR = new ColorRGBA(0, 0, 0.9f, A);// j'ai changé 0.9f à 0
	private static final ColorRGBA CENTER_COLOR = new ColorRGBA(0f, 0f, 0f, 1);
	private float initialScale;

	private static final float INCLINAISON = FastMath.HALF_PI;
	private float ROTATION=0; // the rotatioon of the box, max =180°
	private Box box_de_status; // The box showing the mission status
	private Box center; // Le cube montrant l'evolution de la mission
	private float boxHeight=3.6f;

	public static final float DIST_VIEW = 50f;  // 25m
	/**
	 * 
	 */
	public MissionStatus(MissionStatusWindow window) {
		super("MissionStatus-node");
		this.game = window.getHud().getGame();
/*
		font = window.getHud().getGame().get3DFont();
		new Font3DGradient(Vector3f.UNIT_Y,FONT_COLOR,FONT_COLOR).applyEffect(font);
		new Font3DBorder(0.03f,FONT_COLOR,FONT_COLOR,font).applyEffect(font);

		zs = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
		zs.setEnabled(true);

		ws = DisplaySystem.getDisplaySystem().getRenderer().createWireframeState();
		ws.setEnabled(true);

		BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		bs.setEnabled(true);
		bs.setBlendEnabled(true);

		setRenderState(zs);
		setRenderState(bs);
		//setRenderState(ws);
		setLightCombineMode(LightCombineMode.Off);

		//setLocalTranslation(100, 100, 0);
        
		getLocalRotation().fromAngleNormalAxis(INCLINAISON, Vector3f.UNIT_X);
        
		
		//RoundedBox disk=new RoundedBox("mission-status",
		Vector3f centerOfTheBox = new Vector3f(0, 0, 0);
		box_de_status = new Box("box", centerOfTheBox, boxHeight, boxHeight, boxHeight);
		//Cylinder disk = new Cylinder("disk-boussolle",5,3,RADIUS,0.1f,true);
		//disk.getLocalRotation().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
		box_de_status.setDefaultColor(new ColorRGBA(0.0f,0.0f,0.0f,0.35f));
		//this.attachChild(box_de_status);
		
		
		center = new Box("box_2", centerOfTheBox, 2, 2, 2);
		center.getLocalRotation().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
		center.setDefaultColor(new ColorRGBA(0.3f,1.5f,0.66f,0.8f));
		//this.attachChild(disk); // je pense que ce n'est pas nécessaire
		this.attachChild(center); // c'est le box au centre pour afficher l'evolution dans la mission
		this.attachChild(box_de_status);
		

		qPlan = new Quaternion();
		qPlan.fromAngleNormalAxis(-INCLINAISON, Vector3f.UNIT_X);


		N = font.createText("Accomplissment mission", FONT_SIZE, 1);
		pourcentageAvancement=font.createText("40%", FONT_SIZE+0.4f, 1);
		N.setLocalTranslation(-8, 0.2f, RADIUS-3);// c'etais N.getWidth() pour l'axe des X je l'ai mis à -6
		pourcentageAvancement.setLocalTranslation(N.getLocalTranslation().x+ N.getWidth()+0.5f,N.getLocalTranslation().y,N.getLocalTranslation().z);
		pourcentageAvancement.setFontColor(ColorRGBA.green);
		pourcentageAvancement.getLocalRotation().set(qPlan);
		
		N.getLocalRotation().set(qPlan);
		this.attachChild(N);
		this.attachChild(pourcentageAvancement);

		//this.attachChild(new TargetNode(this,"target",new Vector3f(512,0,512)));

		//this.attachChild(new Box("essaie", new Vector3f(), 1, 1, 1));

		q = new Quaternion();

		this.updateRenderState();
		this.updateGeometricState(0, true);
		initialScale=center.getLocalScale().x;
		avancementMission(40);
                */
                System.out.println("MissionStatus -> constructeur : vide !!!");
	}

	public void update() {

	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#updateGeometricState(float, boolean)
	 */
	
	private float variation=0.000f;
	
	public void updateGeometricState(float time, boolean initiator) {
            
            System.out.println("MissionStatus -> updateGeometricState() : manquante !!");
		
		// Ce n'est pas utile de tourner l'objet montrant l'etat de la mission
		
	  /*  getLocalRotation().fromAngleNormalAxis(INCLINAISON, Vector3f.UNIT_X);
		q.fromAngleNormalAxis(-game.getCameraControler().getRy(), Vector3f.UNIT_Y);
		getLocalRotation().multLocal(q);

		q.fromAngleNormalAxis(game.getCameraControler().getRy(), Vector3f.UNIT_Y);
		N.getLocalRotation().set(q.mult(qPlan));

		super.updateGeometricState(time, initiator); 
		*/
		//N.getLocalRotation().set(q.mult(qPlan)); // à effacé après l'essaye
		
		// ** ajout d'un quaternion pour une simple animation du BOX
		if(ROTATION>3) {ROTATION=0.1f; 
		
		//game.getChatSystem().debug("new rotation iteration");
		}
		else ROTATION+=0.01;
	//	Matrix3f m=new Matrix3f(0, 0, ROTATION, 0, 0, 0, 0, 0, 0);
		//box_de_status.setLocalRotation(new Quaternion(ROTATION, 0.1f, 0.2f, 0.8f));
		
		variation+=0.0001f;
		/*if(variation>0.005f) {variation=0.001f; center.setLocalScale(initialScale);
		
		
		}
		
		center.setLocalScale(center.getLocalScale().x+variation);
		center.setLocalRotation(new Quaternion(ROTATION, 0.1f, 0.2f, 0.8f));
		
		super.updateGeometricState(time, initiator);
		//TextureManager.loadImage(); //ajout d'une texture pour le box
		Texture upTexture = TextureManager.loadTexture(
				FileLoader.getResourceAsUrl(LaConstants.DIR_SYSCOM_ICON+"stairs-up-16.png"),
				false);
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture(upTexture);	
		box_de_status.setRenderState(ts);
	//avancementMission(40); // valeur réel de l'avencement en 40 %,  c'est un exemple
		*/
                
	}


	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
		/*try {
			Player player = Variables.getMainPlayer();
			HashMap<String, Vector3f> targets = player.getTargets().getAll();
			ArrayList<Spatial> list = new ArrayList<Spatial>(this.getChildren());
			for (Spatial s : list) 
				if (s instanceof TargetNode && !targets.containsKey(s.getName()))
					s.removeFromParent();

			
			for (Entry<String, Vector3f>target : targets.entrySet()) {
				TargetNode targetNode = (TargetNode) this.getChild(target.getKey()); 
				if (targetNode == null)
					this.attachChild(new TargetNode(this, target.getKey(), target.getValue()));
				else targetNode.updateTarget(target.getValue());
			}

		} catch (Exception e) {
			logger.warning(e.getClass().getName());
		}
                *    */
            System.out.println("MissionStatus -> call() : vide !!");
		return null;
             
            
	}
	
	
	
	public void avancementMission(float x)
	{
		//la valeur de x reçu est en %
		float realValue=x*(boxHeight-1)/100;
		/*
       center.setLocalScale(new Vector3f(realValue, realValue, realValue));
       initialScale=center.getLocalScale().x; // valeur utilisée dans la vibration du cube au centre
       */
                System.out.println("MissionStatus -> avancementMission() : manquante !!!");
	}

	/**
	 * @return the game
	 */
	public LaGame getGame() {
		return game;
	}

	/**
	 * @return
	 */
	public Quaternion getQplan() {
		return qPlan;
	}

	/**
	 * @return the font
	 */
	/*public Font3D getFont() {
		return font;
	}*/
	

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
