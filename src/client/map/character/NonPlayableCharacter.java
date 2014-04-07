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
package client.map.character;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import shared.constants.LaConstants;
import shared.constants.ScriptConstants;
import shared.enums.CharacterModel;
import shared.enums.LaComponent;
import shared.pck.Pck;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
//import client.hud3d.SplashText3D;
//import client.input.MouseCursor;
//import client.input.MouseCursor.CursorType;
import client.interfaces.graphic.GraphicCollidable;
//import client.interfaces.graphic.GraphicMouseFocusListener;

//import client.interfaces.graphic.GraphicMouseListener;
import client.interfaces.graphic.GraphicReflexEditable;
import client.interfaces.graphic.GraphicScenarizedEditable;
import client.interfaces.graphic.GraphicShadowCaster;
import client.map.World;
import client.script.ScriptableMethod;
import com.jme3.math.ColorRGBA;

import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
//import com.jme.renderer.ColorRGBA;
import com.jme3.scene.Node;
//import com.jme.util.GameTaskQueueManager;

/**
 * Class de représentation des PNJ
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class NonPlayableCharacter extends AbstractCharacter implements
		//GraphicMouseListener, GraphicCollidable, GraphicMouseFocusListener,
		GraphicShadowCaster, GraphicScenarizedEditable, GraphicReflexEditable {
	private static final Logger logger = Logger.getLogger("NonPlayableCharacter");

	/**
	 * identifiant du pnj
	 */
	private int id;

	/**
	 * orientation Doit être envoyé au dessus.
	 */
	@Editable(type=FieldEditType.realinterval,
			realMinValue=0,realMaxValue=FastMath.TWO_PI,
			realStepValue=FastMath.QUARTER_PI/2f,
			applyMethod="rebuild")
	private float ry;

	/**
	 * clef du script à executer quand on clic sur le PNJ
	 */
	@Editable(type=FieldEditType.action)
	private String action = ScriptConstants.VOID_SCRIPT;

	/**
	 * distance d'action d'un pnj
	 */
	@Editable(type=FieldEditType.real)
	private float actionDist = 0;


	private String linkedRegion = "";
	

	
	/**
	 * @param world
	 */
	public NonPlayableCharacter(World world, int id) {
		super(world);
		this.id = id;
		//world.getGame().getHud().getLoading().add(this);
	}

	/**
	 * re position le personnage
	 */
	private void rebuildGeometrics() {
		if (characterNode != null) {
			characterNode.setLocalTranslation(x, world.getHeightAt(x, z)+y, z);
			characterNode.getLocalRotation().fromAngleNormalAxis(ry, Vector3f.UNIT_Y);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see client.map.character.AbstractCharacter#rebuildApply(com.jme.scene.Node)
	 */
	@Override
	protected void rebuildApply(Node newModel) {
		super.rebuildApply(newModel);
		rebuildGeometrics();
	}
	
	/* ********************************************************** *
	 * * 				GESTION DE LA VISIBILIT2 				* *
	 * ********************************************************** */

	/**
	 * met à jour la visibilité
	 * @param equalsIgnoreCase
	 */
	private void updateVisibility(boolean visible) {
		logger.fine(this.toString()+":"+visible);
		if (this.visible  != visible) {
			this.visible = visible;
			if (visible)
				addToRenderTask();
			else 
				removeFromRenderTask();
		}
	}

	/**
	 * Met à jour la visibilité des token
	 */
	public void updateVisibility() {
		//updateVisibility(!world.getPlayer().hasToken(LaConstants.NPC_VISIBILITY_CAT_TOKEN, Integer.toString(id)) ||
		//		Boolean.TRUE.equals(world.getPlayer().getToken(LaConstants.NPC_VISIBILITY_CAT_TOKEN, Integer.toString(id))));
	}

	/**
	 * 
	 */
	@ScriptableMethod(description="Affiche l'objet pour le joueur courant")
	public void showForPlayer() {
		updateVisibility(true);
		//world.getPlayer().delToken(LaConstants.NPC_VISIBILITY_CAT_TOKEN, Integer.toString(id));		
	}

	/**
	 * 
	 */
	@ScriptableMethod(description="Masque l'objet pour le joueur courant")
	public void hideForPlayer() {
		updateVisibility(false);
	//	world.getPlayer().setToken(LaConstants.NPC_VISIBILITY_CAT_TOKEN, Integer.toString(id), Boolean.FALSE.toString());
	}


	/* ********************************************************** *
	 * * 					Sharable - IMPLEMENTS 				* *
	 * ********************************************************** */
	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	@Override
	public void addData(Pck pck) {
		pck.putString(displayName, action);
		pck.putEnum(modelType);
		pck.putFloat(x, y, z, ry, actionDist);

		// couleur de vetement
		pck.putFloat(skin.r, skin.g, skin.b, skinAmbient);
		pck.putFloat(topCloth.r, topCloth.g, topCloth.b, topClothAmbient);
		pck.putFloat(bottomCloth.r, bottomCloth.g, bottomCloth.b, bottomClothAmbient);
		pck.putFloat(hair.r, hair.g, hair.b, hairAmbient);
		pck.putFloat(shoes.r, shoes.g, shoes.b, shoesAmbient);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.npc.prefix() + id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * client.interfaces.network.Sharable#receiveCommitPck(java.nio.ByteBuffer)
	 */
	@Override
	public void receiveCommitPck(ByteBuffer message) {
		CharacterModel oldModel = modelType;

		this.versionCode = message.getInt();
		this.displayName = Pck.readString(message);
		this.action = Pck.readString(message);
		this.modelType = Pck.readEnum(CharacterModel.class, message);
		this.x = message.getFloat();
		this.y = message.getFloat();
		this.z = message.getFloat();
		this.ry = message.getFloat();
		this.actionDist = message.getFloat();

		this.skin = new ColorRGBA(message.getFloat(), message.getFloat(),message.getFloat(), 1);
		this.skinAmbient = message.getFloat();
		this.topCloth = new ColorRGBA(message.getFloat(), message.getFloat(),message.getFloat(), 1);
		this.topClothAmbient = message.getFloat();
		this.bottomCloth = new ColorRGBA(message.getFloat(), message.getFloat(), message.getFloat(), 1);
		this.bottomClothAmbient = message.getFloat();
		this.hair = new ColorRGBA(message.getFloat(), message.getFloat(), message.getFloat(), 1);
		this.hairAmbient = message.getFloat();
		this.shoes = new ColorRGBA(message.getFloat(), message.getFloat(), message.getFloat(), 1);
		this.shoesAmbient = message.getFloat();

		if (oldModel != modelType)
			rebuild();
		/*else
			GameTaskQueueManager.getManager().update(new Callable<Void>() {
				public Void call() throws Exception {
					rebuildGeometrics();
					return null;
				}
			}); */

	}
	
	/* ********************************************************** *
	 * *	 			AFFICHAGE DE TEXT ON HEAD 				* *
	 * ********************************************************** */
	
	/**
	 * Affiche un text au dessus d'un pnj
	 */
	@ScriptableMethod(description="affiche un texte au dessus d'un PNJ\nSeulement le joueur courant le voie. Le text disparairait apres un certain temps.\n \n(text, time)")
	public void show3dTextForPlayer(String text, int time) {
		//new SplashText3D(this, text, world.getGame(), time);
	}
	
	/**
	 * Affiche un text au dessus d'un pnj
	 */
	@ScriptableMethod(description="affiche un texte au dessus d'un PNJ\nSeulement le joueur courant le voie. Le text disparairait apres un appel à clearText")
	public void show3dTextForPlayer(String text) {
		//world.getPlayer().setToken(LaConstants.NPC_ON_HEAD_TOKEN_CAT, Integer.toString(id), text);
	}

	/**
	 * supprime l'affichage du text pour le joueur courant
	 * @param text
	 */
	@ScriptableMethod(description="Efface tous les text au dessus du PNJ pour le joueur courant")
	public void clear3dTextForPlayer() {
		/*GameTaskQueueManager.getManager().update(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				clearOnHead();
				return null;
			}
		});
		world.getPlayer().delToken(LaConstants.NPC_ON_HEAD_TOKEN_CAT, Integer.toString(id));		
                * */
	}
	
	/**
	 * Renvoie la position dans le monde du personnage
	 * @return
	 */
	public Vector3f getWorldLoc() {
		return world.getLocAt(x,z);
	}
	
	/**
	 * 
	 */
	public void updateOnHead() {
		final NonPlayableCharacter npc = this;
		/*GameTaskQueueManager.getManager().update(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				clearOnHead();
				String text = world.getPlayer().getTokenAsString(LaConstants.NPC_ON_HEAD_TOKEN_CAT, Integer.toString(id));
				if (text != null)
					new SplashText3D(npc, text, world.getGame(), -1);
				return null;
			}
		}); */
	}

	

	/* ********************************************************** *
	 * * 			AbstractCharacter - IMPLEMENTS 				* *
	 * ********************************************************** */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * client.character.AbstractCharacter#testCollision(com.jme.math.Vector3f)
	 */
	@Override
	protected boolean testCollision(Vector3f newPos, Vector3f dir) {
		return false;
	}

	/* ********************************************************** *
	 * * 				LaMouseListener - IMPLEMENTS 			* *
	 * ********************************************************** */
	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.GraphicMouseListener#onButton(int,
	 * boolean, com.jme.math.Ray)
	 */
	//@Override
	public boolean onButton(int button, boolean pressed, Ray ray) {
	/*	if (button == 0 && !pressed
				&& !action.equals(ScriptConstants.VOID_SCRIPT) ) {
			if (world.getPlayer().distanceTo(this) < actionDist) {
				world.getScriptExecutor().execute(action);
				getWorld().getGame().getTraces().sendActiveNpcAction(this);
			} else 
				world.getPlayer().moveTo(x,z);
			return true;
		} */
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * client.interfaces.graphic.GraphicMouseListener#onMove(com.jme.math.Ray)
	 */
	//@Override
	public boolean onMove(Ray ray) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.GraphicMouseListener#onWheel(int,
	 * com.jme.math.Ray)
	 */
//	@Override
	public boolean onWheel(int wheelDelta, Ray ray) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see client.interfaces.graphic.GraphicMouseFocusListener#onMouseEnter()
	 */
	//@Override
	public void onMouseEnter() {
	/*	if (!ScriptConstants.VOID_SCRIPT.equals(action) && getWorld().getPlayer().distanceTo(this) < actionDist)
			MouseCursor.get().switchCursor(CursorType.npc);
	*/	
	}
	
	/* (non-Javadoc)
	 * @see client.interfaces.graphic.GraphicMouseFocusListener#onMouseExit()
	 */
//	@Override
	public void onMouseExit() {
//		MouseCursor.get().switchCursor(CursorType.base);
		
	}

	/* ********************************************************** *
	 * * 			GraphicHudEditable - IMPLEMENTS 			* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.GraphicEditable#setGeometrics(float,
	 * float, float, float, float, float, float)
	 */
	@Override
	public void setGeometrics(float x, float y, float z, float rx, float ry,
			float rz, float scale) {
		this.x = x;
		//this.y = y;
		this.z = z;
		rebuildGeometrics();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.character.AbstractCharacter#setX(float)
	 */
	@Override
	public void setX(float x) {
		super.setX(x);
		rebuildGeometrics();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.character.AbstractCharacter#setX(float)
	 */
	@Override
	public void setY(float y) {
		super.setY(y);
		rebuildGeometrics();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.character.AbstractCharacter#setZ(float)
	 */
	@Override
	public void setZ(float z) {
		super.setZ(z);
		rebuildGeometrics();
	}

	/**
	 * @param ry
	 *            the ry to set
	 */
	public void setRy(float ry) {
		this.ry = ry;
		rebuildGeometrics();
	}

	/**
	 * @return the ry
	 */
	public float getRy() {
		return ry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.character.AbstractCharacter#canMoveAt(com.jme.math.Vector3f)
	 */
	@Override
	protected boolean canMoveAt(Vector3f newPos) {
		return true;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the actionDist
	 */
	public float getActionDist() {
		return actionDist;
	}

	/**
	 * @param actionDist
	 *            the actionDist to set
	 */
	public void setActionDist(float actionDist) {
		this.actionDist = actionDist;
	}

	/* (non-Javadoc)
	 * @see client.interfaces.graphic.GraphicCollidable#isCollidable()
	 */
	//@Override
	public boolean isCollidable() {
		return true;
	}

	/* (non-Javadoc)
	 * @see client.map.character.AbstractCharacter#isNpc()
	 */
	@Override
	public boolean isNpc() {
		return true;
	}

	@Override
	public void linkToRegion(String linkedRegionKey) {
		this.linkedRegion  = linkedRegionKey;
	}
	@Override
	public String getLinkedRegion(){
		return linkedRegion;
	}

}
