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

import shared.constants.LaConstants;
import client.LaGame;
import client.map.character.Player;
import client.utils.FileLoader;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Quad;
import shared.variables.Variables;

/*
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.font3d.Text3D;
*/
/**
 * cible de la mission
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * 
 */
public class TargetNode extends Node {
	private static final long serialVersionUID = 2205786997651390945L;
	private Cylinder cone;
	private Vector3f v;
	private LaGame game;
	private Player player;
	//private Text3D text;
	//private Text3D dist;
	private Quad up;

	Quaternion q = new Quaternion();
	private MissionStatus missionStatus;
	private Quad down;
	private Node textNode;
	private float width;
	private int textMode = 2;


	/**
	 * @param game 
	 * @param name nom de la cible
	 * @param v position cible de la mission
	 */
	public TargetNode(MissionStatus missionStatus, String name, Vector3f v) {
		super(name);
		this.game = missionStatus.getGame();
		this.missionStatus = missionStatus;
		this.v = v;
/*  					*******We don't need any of this for the status*******
 * 
		Texture upTexture = TextureManager.loadTexture(
				FileLoader.getResourceAsUrl(LaConstants.DIR_SYSCOM_ICON+"stairs-up-16.png"),
				false);
		Texture downTexture = TextureManager.loadTexture(
				FileLoader.getResourceAsUrl(LaConstants.DIR_SYSCOM_ICON+"stairs-down-16.png"),
				false);
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture(upTexture);

		up = new Quad("up",MissionStatus.CONE_RADIUS*2,MissionStatus.CONE_RADIUS*2);
		up.setRenderState(ts);
		up.getLocalRotation().fromAngleNormalAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
		q.fromAngleNormalAxis(FastMath.HALF_PI, Vector3f.UNIT_Z);
		up.getLocalRotation().multLocal(q);
		up.setLocalTranslation(0, 0, -MissionStatus.RADIUS);

		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture(downTexture);

		down = new Quad("down",MissionStatus.CONE_RADIUS*2,MissionStatus.CONE_RADIUS*2);
		down.setRenderState(ts);
		down.getLocalRotation().fromAngleNormalAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
		q.fromAngleNormalAxis(FastMath.HALF_PI, Vector3f.UNIT_Z);
		down.getLocalRotation().multLocal(q);
		down.setLocalTranslation(0, 0, -MissionStatus.RADIUS);

		cone = new Cylinder("arrow", 5, 16, MissionStatus.CONE_RADIUS, 0, MissionStatus.CONE_LENGHT, true, false);
		cone.setDefaultColor(ColorRGBA.black);
		cone.setLocalTranslation(0, 0, -MissionStatus.RADIUS);


		text = missionStatus.getFont().createText(name, MissionStatus.FONT_SIZE, 0);
		width = text.getWidth();
		text.setLocalTranslation(-width/2, +MissionStatus.FONT_SIZE/4, 0);

		dist = missionStatus.getFont().createText("100m", MissionStatus.FONT_SIZE, 0);
		dist.setLocalTranslation(-width/2,-MissionStatus.FONT_SIZE*3f/4f, 0);

		textNode = new Node("text-node");
		textNode.setLocalTranslation(0,0,-MissionStatus.RADIUS+MissionStatus.FONT_SIZE+width/2);

		q.fromAngleNormalAxis(-FastMath.HALF_PI, Vector3f.UNIT_Z);
		textNode.getLocalRotation().set(missionStatus.getQplan());
		textNode.getLocalRotation().multLocal(q);

		textNode.attachChild(text);
		textNode.attachChild(dist);

		this.attachChild(textNode);
		this.attachChild(up);
		this.attachChild(down);
		this.attachChild(cone);
*/
		//this.updateRenderState();

		

	}


	/**
	 * change la target du joueur
	 * @param value
	 */
	public void updateTarget(Vector3f value) {
		this.v= value;
	}

	

	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#updateGeometricState(float, boolean)
	 */
	
	public void updateGeometricState(float time, boolean initiator) {
/*
		Vector2f p = getPlayerLocation();
		Vector2f t = getTarget();
		t.subtractLocal(p);
	//	dist.setText(Math.round(t.length()/2)+"m");
		
		float loc = Math.min(t.length()*MissionStatus.RADIUS/MissionStatus.DIST_VIEW,MissionStatus.RADIUS);
		up.getLocalTranslation().setZ(-loc);
		down.getLocalTranslation().setZ(-loc);
		cone.getLocalTranslation().setZ(-loc);
		
		
		
		float r =t.normalizeLocal().angleBetween(new Vector2f(0,1));

		this.getLocalRotation().fromAngleNormalAxis(r, Vector3f.UNIT_Y);

		if (isTargetOver()) {
			if (!this.hasChild(up))		this.attachChild(up);
			if (this.hasChild(cone)) 	cone.removeFromParent();
			if (this.hasChild(down)) 	down.removeFromParent();
		} else if (isTargetBottom()) {
			if (!this.hasChild(down))	this.attachChild(down);
			if (this.hasChild(cone)) 	cone.removeFromParent();
			if (this.hasChild(up)) 		up.removeFromParent();
		} else { 
			if (!this.hasChild(cone))	this.attachChild(cone);
			if (this.hasChild(up)) 		up.removeFromParent();
			if (this.hasChild(down)) 	down.removeFromParent();
		}

		up.getLocalRotation().fromAngleNormalAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
		q.fromAngleNormalAxis(r-game.getCameraControler().getRy(), Vector3f.UNIT_Z);
		up.getLocalRotation().multLocal(q);
		down.getLocalRotation().set(up.getLocalRotation());


		textNode.getLocalRotation().set(missionStatus.getQplan());
		float a = -r+game.getCameraControler().getRy();
		
		if (textMode == 1) {
			/*if (name.equals("arbre"))
				System.out.println(a%FastMath.PI);
			if (a<FastMath.PI) {
				q.fromAngleNormalAxis(-FastMath.HALF_PI,Vector3f.UNIT_Z);
				textNode.getLocalRotation().multLocal(q);
			} else {
				q.fromAngleNormalAxis(FastMath.HALF_PI,Vector3f.UNIT_Z);
				textNode.getLocalRotation().multLocal(q);
			}
		} else if (textMode == 2) {
			textNode.setLocalTranslation(0,0,
					-MissionStatus.RADIUS+0.2f+ 
					Math.max(Math.abs(FastMath.sin(a)),0.1f)*(width/2));
			q.fromAngleNormalAxis(a,Vector3f.UNIT_Z);
			textNode.getLocalRotation().multLocal(q);
		} else {
			q.fromAngleNormalAxis(-FastMath.HALF_PI,Vector3f.UNIT_Z);
			textNode.getLocalRotation().multLocal(q);
		}
		//*/
		/*	dist.setLocalRotation(text.getLocalRotation());//*/

		//super.updateGeometricState(time, initiator);
            System.out.println("TargetNode -> updateGeometricState() : vide !!");
	}

	/**
	 * @return
	 */
	private Vector2f getTarget() {
		return new Vector2f(v.x,v.z);
	}

	/**
	 * renvoie la position du joueur en 2D
	 * @return
	 */
	private Vector2f getPlayerLocation() {
		if (player == null) try {
	 player = Variables.getWorld().getPlayer();
		} catch (NullPointerException e) { }
		if (player != null)
			return player.get2DCoord();
		return new Vector2f(512,512);
	}


	/**
	 * test si la target est au dessus du joueur
	 * @return
	 */
	private boolean isTargetOver() {
		if (player == null) try {
			player = Variables.getWorld().getPlayer();
		} catch (NullPointerException e) { }
		if (player != null)
			return player.getY()<this.v.y-5;
		return false;
	}

	/**
	 * test si la target est au dessus du joueur
	 * @return
	 */
	private boolean isTargetBottom() {
		if (player == null) try {
			player = Variables.getWorld().getPlayer();
		} catch (NullPointerException e) { }
		if (player != null)
			return player.getY()-5>this.v.y;
			return false;
	}


	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
