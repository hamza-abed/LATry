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
package client.map;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import shared.enums.LaComponent;
import shared.pck.Pck;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
import client.interfaces.graphic.GraphicReflexEditable;
import client.interfaces.graphic.GraphicScenarizedEditable;
import client.interfaces.graphic.GraphicScenarizedEditableRegion;
import client.map.character.Player;
import client.task.GraphicsAddRemoveSceneTask;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;
//import com.jme.renderer.ColorRGBA;
import com.jme3.scene.Node;
//import com.jme.scene.QuadMesh;
import com.jme3.scene.Spatial;
//import com.jme.scene.QuadMesh.Mode;
import com.jme3.scene.Spatial.CullHint;
//import com.jme.scene.Spatial.LightCombineMode;
//import com.jme.scene.state.WireframeState;
//import com.jme.system.DisplaySystem;
//import com.jme.util.GameTaskQueueManager;
//import com.jme.util.geom.BufferUtils;

/**
 * region, permet de declancher des trigger quand le joueur 
 * arrive dans certain point 
 * <ul>
 * <li>PROPOSE faire d'autre forme que rectangulaire</li>
 * <li>gere les permition d'entree/sortie</li>
 * <li></li>
 * <li>suppression package client.extern.scenarization;
 * client.hud.scenarization;</li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2013
 */

public class Region implements GraphicScenarizedEditableRegion, GraphicReflexEditable,Callable<Void> {
	
	/**
	 * identifiant redwraf et version reseau
	 */
	private int id =-1, versionCode = -1;

	/**
	 * position/taille de la region
	 */
	@Editable(type=FieldEditType.real,applyMethod="rebuildTask")
	private float x=0,z=0;

	@Editable(type=FieldEditType.real,applyMethod="rebuildTask")
	private float w=1,h=1;
	
	/**
	 * Eléments de scénarisation liés
	 */
	private ArrayList<GraphicScenarizedEditable> elementsLies = new ArrayList<GraphicScenarizedEditable>();
	
	/**
	 * action/script executé
	 */
	@Editable(type=FieldEditType.action)
	private String canEnter,onEnter,onFailEnter,canLeave,onLeave,onFailLeave;
	
	
	private World world;
	
	/**
	 * 
	 */
	private GraphicsAddRemoveSceneTask task;
	
	/**
	 * noeud graphique contenant l'enssemble des 
	 * quad d'affichage de region
	 */
	private Node root;
	
	/**
	 * 
	 */
	public Region(World world, int id) {
		this.world = world;
		this.id = id;
		
		this.root = new Node(getKey());
		this.root.setCullHint(CullHint.Always);
		//this.root.setCollisionMask(0);
                
                /*
                 * Pour les trucs de collisions avec le terrain 
                 * en JME3 il suffit d'attacher la scen au bullet app state
                 * qui gère tout les aspects physique
                 * 
                 * Hamza ABED
                 */
		//this.root.setRenderQueueMode(Renderer.QUEUE_SKIP);
	}
	
	/**
	 * recharge l'apperence de la region dans un thread synchronisé dans l'update
	 */
	private void rebuildTask() {
		//GameTaskQueueManager.getManager().update(this);
            /*
             * à enlever
             * 
             * Hamza ABED
             */
	}
	
	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
        
        /*
         * J'ai pas encore compris son utilité 
         * 
         * Hamza ABED
         */
	public Void call() throws Exception {
/*
		int NB_QUAD = 10;
		
		root.detachAllChildren();
		float stepx = 2f*w/NB_QUAD;
		float stepz = 2f*h/NB_QUAD;
				
		FloatBuffer vs = BufferUtils.createFloatBuffer(3*(NB_QUAD+1)*(NB_QUAD+1));
		IntBuffer is = BufferUtils.createIntBuffer(4*NB_QUAD*NB_QUAD);
		for (int zi=-NB_QUAD/2;zi<=NB_QUAD/2;zi++) 
			for (int xi=-NB_QUAD/2;xi<=NB_QUAD/2;xi++) {
				float x = this.x+xi*stepx;
				float z = this.z-zi*stepz;
				vs.put(x);
				vs.put(world.getHeightAt(x, z)+0.02f);
				vs.put(z);
			}

		for (int zi=0;zi<NB_QUAD;zi++)
			for (int xi=0;xi<NB_QUAD;xi++) {
				is.put(xi+zi*(NB_QUAD+1));
				is.put(xi+1+zi*(NB_QUAD+1));
				is.put(xi+1+(zi+1)*(NB_QUAD+1));
				is.put(xi+(zi+1)*(NB_QUAD+1));
			}
		
		vs.rewind();
		is.rewind();
		
		QuadMesh q = new QuadMesh(getKey(), vs, null, null, null, is);
		q.setMode(Mode.Quads);
		q.setLightCombineMode(LightCombineMode.Off);
		q.setDefaultColor(ColorRGBA.white);
		
		WireframeState ws = DisplaySystem.getDisplaySystem().getRenderer().createWireframeState();
		ws.setEnabled(true);
		q.setRenderState(ws);
		
		q.updateRenderState();
		
		root.attachChild(q);
		
		q.setModelBound(new BoundingBox());
		q.updateModelBound();
		  */
		return null;
              
	}
	
	/* ********************************************************** *
	 * * 			GraphicGizmoEditable - IMPLEMENTS 			* *
	 * ********************************************************** */

	/* (non-Javadoc)
	 * @see client.interfaces.graphic.GraphicGizmoEditable#setGeometrics(float, float, float, float, float, float, float)
	 */
	@Override
	public void setGeometrics(float x, float y, float z, float rx, float ry,
			float rz, float scale) {
		this.x = x;
		this.z = z;
		rebuildTask();
	}
	/* ********************************************************** *
	 * * 				Graphic - IMPLEMENTS 					* *
	 * ********************************************************** */


	/* (non-Javadoc)
	 * @see client.interfaces.graphic.Graphic#getGraphic()
	 */
	@Override
	public Spatial getGraphic() {
		return root;
	}
	
	/* (non-Javadoc)
	 * @see client.interfaces.graphic.Graphic#addToRenderTask()
	 */
	@Override
	public void addToRenderTask() {
		if (task == null)
			task = new GraphicsAddRemoveSceneTask(this, world);
		task.add();
	}

	/* (non-Javadoc)
	 * @see client.interfaces.graphic.Graphic#removeFromRenderTask()
	 */
	@Override
	public void removeFromRenderTask() {
		if (task == null)
			task = new GraphicsAddRemoveSceneTask(this, world);
		task.remove();	
	}

	/* ********************************************************** *
	 * * 				Sharable - IMPLEMENTS 					* *
	 * ********************************************************** */

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	@Override
	public void addData(Pck pck) {
		pck.putFloat(x,z,w,h);
		pck.putString(canEnter,onEnter,onFailEnter);
		pck.putString(canLeave,onLeave,onFailLeave);
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.region.prefix()+id;
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#getVersionCode()
	 */
	@Override
	public int getVersionCode() {
		return versionCode;
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#receiveCommitPck(java.nio.ByteBuffer)
	 */
	@Override
	public void receiveCommitPck(ByteBuffer message) {
		this.versionCode = message.getInt();
		this.x = message.getFloat();
		this.z = message.getFloat();
		this.w = message.getFloat();
		this.h = message.getFloat();
		this.canEnter = Pck.readString(message);
		this.onEnter = Pck.readString(message);
		this.onFailEnter = Pck.readString(message);
		this.canLeave = Pck.readString(message);
		this.onLeave = Pck.readString(message);
		this.onFailLeave = Pck.readString(message);
		
		rebuildTask();
		
		// je prefere le desactivé car ca peu posé probleme au loading
		/*world.getGame().getTaskExecutor().execute(new Runnable() {
			@Override
			public void run() {
				world.getPlayer().regionUpdate(false);
			}
		});//*/
	}
	/* ********************************************************** *
	 * * 				Test de position 					* *
	 * ********************************************************** */
	/**
	 * indique si ce point est dans la region
	 * @param v
	 * @return
	 */
	public boolean isCoordIn(Vector3f v) {
		return Math.abs(x-v.x)<w && Math.abs(z-v.z)<h;
	}
	
	/* ********************************************************** *
	 * * 				EXECUTION DE SCRIPT 					* *
	 * ********************************************************** */

	/**
	 * Execution des test de region
	 * @param player
	 * @return
	 */
	public boolean canLeave(Player player) {
		return world.getScriptExecutor().executeBoolean(canLeave);
	}

	/**
	 * test si le joueur à le droit d'entré
	 * @return
	 */
	public boolean canEnter() {
		return world.getScriptExecutor().executeBoolean(canEnter);
	}

	/**
	 * executele script d'inpossibilité de sortie
	 */
	public void failLeave() {
		world.getScriptExecutor().execute(onFailLeave);
	}

	/**
	 * execute le script d'entrer de region
	 */
	public void failEnter() {
		world.getScriptExecutor().execute(onFailEnter);	
	}

	/**
	 * execute le script à la sortie de la region
	 */
	public void leave() {
            /*
		if (world.getPlayer().isScenarizing())
		{
			// pas opérationnelle en v31
			//world.getGame().getHud().getElementsDActivites().rafraichir(this, false);
		}
		world.getScriptExecutor().execute(onLeave);	
                */
	}

	/**
	 * Execute le script à l'entré de la region
	 */
	public void enter() {
            /*
		if (world.getPlayer().isScenarizing())
		{
			// pas opérationnelle en v31
			//world.getGame().getHud().getElementsDActivites().rafraichir(this, true);
		}
                */
		world.getScriptExecutor().execute(onEnter);
	}


	/* ********************************************************** *
	 * * 					GETTERS / SETTERS 					* *
	 * ********************************************************** */
	
	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(float x) {
		this.x = x;
		rebuildTask();
	}

	/**
	 * @return the z
	 */
	public float getZ() {
		return z;
	}

	/**
	 * @param z the z to set
	 */
	public void setZ(float z) {
		this.z = z;
		rebuildTask();
	}

	/**
	 * @return the w
	 */
	public float getW() {
		return w;
	}

	/**
	 * @param w the w to set
	 */
	public void setW(float w) {
		this.w = w;
		rebuildTask();
	}

	/**
	 * @return the h
	 */
	public float getH() {
		return h;
	}

	/**
	 * @param h the h to set
	 */
	public void setH(float h) {
		this.h = h;
		rebuildTask();
	}

	/**
	 * @return the canEnter
	 */
	public String getCanEnter() {
		return canEnter;
	}

	/**
	 * @param canEnter the canEnter to set
	 */
	public void setCanEnter(String canEnter) {
		this.canEnter = canEnter;
	}

	/**
	 * @return the onEnter
	 */
	public String getOnEnter() {
		return onEnter;
	}

	/**
	 * @param onEnter the onEnter to set
	 */
	public void setOnEnter(String onEnter) {
		this.onEnter = onEnter;
	}

	/**
	 * @return the onFailEnter
	 */
	public String getOnFailEnter() {
		return onFailEnter;
	}

	/**
	 * @param onFailEnter the onFailEnter to set
	 */
	public void setOnFailEnter(String onFailEnter) {
		this.onFailEnter = onFailEnter;
	}

	/**
	 * @return the canLeave
	 */
	public String getCanLeave() {
		return canLeave;
	}

	/**
	 * @param canLeave the canLeave to set
	 */
	public void setCanLeave(String canLeave) {
		this.canLeave = canLeave;
	}

	/**
	 * @return the onLeave
	 */
	public String getOnLeave() {
		return onLeave;
	}

	/**
	 * @param onLeave the onLeave to set
	 */
	public void setOnLeave(String onLeave) {
		this.onLeave = onLeave;
	}

	/**
	 * @return the onFailLeave
	 */
	public String getOnFailLeave() {
		return onFailLeave;
	}

	/**
	 * @param onFailLeave the onFailLeave to set
	 */
	public void setOnFailLeave(String onFailLeave) {
		this.onFailLeave = onFailLeave;
	}

	/**
	 * Affiche / masque les region pour l'edition
	 * @param regionVisible
	 */
	public void setVisible(boolean regionVisible) {
		getGraphic().setCullHint(regionVisible?CullHint.Never:CullHint.Always);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getKey();
	}

	@Override
	public ArrayList<GraphicScenarizedEditable> getLinkedScenarizedEditables() {
		return elementsLies;
	}

	@Override
	public void linkScenarizedEditableToRegion(GraphicScenarizedEditable linkedKey) {
		elementsLies.add(linkedKey);
	}

	@Override
	public String getLinkedRegion() {
		return this.getKey();
	}

	@Override
	public void linkToRegion(String linkedRegionKey) {
		//nothing to do here : region already knows itself
	}

}
