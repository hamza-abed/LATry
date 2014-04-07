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
package client.editor;

import java.util.HashSet;
import java.util.logging.Logger;

import client.LaGame;
import client.input.PickUtils;
import client.interfaces.LaListener;
import client.interfaces.graphic.GraphicEditable;
import client.interfaces.graphic.GraphicReflexEditable;
import client.interfaces.graphic.GraphicScenarizedEditable;
import client.interfaces.graphic.GraphicScenarizedEditableRegion;
import client.map.Region;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
//import com.jme3.image.Texture;
//import com.jme3.image.Texture.MagnificationFilter;
//import com.jme3.image.Texture.MinificationFilter;
//import com.jme3.intersection.TrianglePickResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Renderer;
//import com.jme3.scene.Spatial.LightCombineMode;
import com.jme3.scene.shape.Box;
/*import com.jme3.scene.shape.GeoSphere;
import com.jme3.scene.state.BlendState;
import com.jme3.scene.state.CullState;
import com.jme3.scene.state.TextureState;
import com.jme3.scene.state.ZBufferState;
import com.jme3.scene.state.CullState.Face;
import com.jme3.system.DisplaySystem;
import com.jme3.util.TextureManager;
*/
/**
 * Permet l'edition d'objet graphic à l'aide de gizmo
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class GraphicEditor{
	private static final Logger logger = Logger.getLogger("GraphicEditor");

	private static final float SCALE_FACTOR = 1 / 4f;

	/**
	 * jeux conteneur
	 */
	private LaGame game;

	/**
	 * Liste des object edité
	 */
	private HashSet<GraphicEditable> objects = new HashSet<GraphicEditable>();

	/**
	 * Sphere permettant de déplacé l'objet à son sol
	 */
	//private GeoSphere moveSphere;

	/**
	 * object qu'on deplace, rotatate..
	 */
	private GraphicEditable move = null, yMove = null;// , xRot=null,
	// yRot=null, zRot =
	// null;

	private float dist;

	/**
	 * poit de depart dans certain calcul
	 */
	// private Vector3f startP;

	private Box hudEditBox;

	private boolean openEdit;

	private boolean magneticMove = true;

	private float magnetic = .5f;

	/**
	 * Editeur graphic du jeux
	 */
	public GraphicEditor(LaGame game) {
		this.game = game;
/*
		BlendState bs = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		bs.setBlendEnabled(true);

		ZBufferState zs = DisplaySystem.getDisplaySystem().getRenderer()
				.createZBufferState();
		zs.setFunction(ZBufferState.TestFunction.LessThan);

		CullState cs = DisplaySystem.getDisplaySystem().getRenderer()
				.createCullState();
		cs.setEnabled(true);
		cs.setCullFace(Face.Back);

		Texture texture = TextureManager.loadTexture("edit.png",
				MinificationFilter.BilinearNearestMipMap,
				MagnificationFilter.Bilinear);
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		ts.setTexture(texture);

		moveSphere = new GeoSphere("gizmo-move", true, 2);
		moveSphere.setModelBound(new BoundingSphere());
		moveSphere.updateModelBound();
		moveSphere.setDefaultColor(new ColorRGBA(1, 1, 1, .3f));
		moveSphere.setRenderState(bs);
		moveSphere.setRenderState(zs);
		moveSphere.setRenderState(cs);
		moveSphere.updateRenderState();

		hudEditBox = new Box("gizmo-hud-edit", new Vector3f(), 1, 1, 1);
		hudEditBox.setModelBound(new BoundingBox());
		hudEditBox.updateModelBound();
		hudEditBox.setDefaultColor(new ColorRGBA(1, 1, 1, .8f));
		hudEditBox.setRenderState(bs);
		hudEditBox.setRenderState(zs);
		hudEditBox.setRenderState(cs);
		hudEditBox.setRenderState(ts);
		hudEditBox.setLightCombineMode(LightCombineMode.Off);
		hudEditBox.updateRenderState();

		// MouseInput.get().addListener(this);
                */
	}

	/* ********************************************************** *
	 * * 			MouseInputListener - IMPLEMENTS 			* *
	 * ********************************************************** */
	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.LaMouseListener#onButton(int, boolean, int, int)
	 */
	public boolean onButton(int button, boolean pressed, int x, int y) {
		/*if (button == 0 && pressed) {
			Ray ray = PickUtils.getMouseRay(x, y);

			TrianglePickResults result = new TrianglePickResults();
			result.setCheckDistance(true);
			for (GraphicEditable obj : objects) {
				updateGizmos(obj);

				result.clear();

				moveSphere.findPick(ray, result);
				if (result.getNumber() > 0) {
					move = obj;
					return true;
				}

				if (obj instanceof GraphicReflexEditable) {
					hudEditBox.findPick(ray, result);
					if (result.getNumber() > 0) {
						game.getServerEditor().edit(obj.getKey());
						openEdit = true;
						return true;
					}
				}
			}
            
		}

		if (button == 0 && !pressed) {
			boolean result = openEdit;
			openEdit = false;
			if (move != null) {
				game.commitOnServer(move);
				//commit région scénarisation
				result = true;
			}
			if (yMove != null) {
				game.commitOnServer(yMove);
				result = true;
			}
			move = null;
			yMove = null;
			return result;
		}
            */
            
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.LaMouseListener#onMove(int, int, int, int)
	 */
	public boolean onMove(int xDelta, int yDelta, int newX, int newY) {
	/*	
            Ray ray = PickUtils.getMouseRay(newX, newY);
		if (move != null) {
			Vector3f v = game.getWorld().pickAt(ray);
			float prevX = this.move.getX();
			float prevZ = this.move.getZ();
			if (v != null) {
				logger.fine("move " + move + "@(" + v.x + "," + v.y + "," + v.z
						+ ")");
				if (magneticMove) {
					v.x = ((int) (v.x / magnetic)) * magnetic;
					v.z = ((int) (v.z / magnetic)) * magnetic;
				}
				move.setGeometrics(v.x, Float.NaN, v.z, Float.NaN, Float.NaN,
						Float.NaN, Float.NaN);
				//Mieux pour mise à jour/modif régions
				float deltaX = this.move.getX() - prevX;
				float deltaZ = this.move.getZ() - prevZ;
				
				if (move instanceof GraphicScenarizedEditable && !((GraphicScenarizedEditable)move).getLinkedRegion().equalsIgnoreCase(""))	
					if (move instanceof GraphicScenarizedEditableRegion)
						updateLinkedScenarizedEditables((GraphicScenarizedEditableRegion)move, deltaX, deltaZ);
					else
						updateLinkedRegion((GraphicScenarizedEditable)move);
			}
		}
		if (yMove != null) {
			Vector3f v = ray.getOrigin().add(ray.getDirection().mult(dist));
			if (v != null) {
				v.y -= game.getWorld().getHeightAt(v.x, v.z);
				yMove.setGeometrics(Float.NaN, v.y, Float.NaN, Float.NaN,
						Float.NaN, Float.NaN, Float.NaN);
			}
		}
		return move != null || yMove != null;
                */
            return false;
	}
	
	
	private void updateLinkedScenarizedEditables(GraphicScenarizedEditableRegion object, float deltaX, float deltaZ){
		if (deltaX != 0.0 || deltaZ != 0.0)
		{
			for (GraphicScenarizedEditable editables : object.getLinkedScenarizedEditables())
			{
				editables.setGeometrics(editables.getX() + deltaX, Float.NaN, editables.getZ() + deltaZ, Float.NaN, Float.NaN, Float.NaN, Float.NaN);
			}
		}
	}
	
	
	private void updateLinkedRegion(GraphicScenarizedEditable object){
		/*if (object.getLinkedRegion() != ""){
			Region linked = game.getWorld().getRegionBuildIfAbsent(object.getLinkedRegion());
			float x = object.getX();
			float z = object.getZ();
			if (x < linked.getX() - linked.getW())
			{
				//on élargit en dessous de X : OK
				float gap = (linked.getX() - (linked.getW()) - x);
				linked.setW(linked.getW()+gap);
				linked.setX(linked.getX() - (gap));
			}
			else if (x > linked.getX() + linked.getW())
			{
				//on élargit au dessus de X
				float gap = (x - (linked.getX() + linked.getW()));
				linked.setW(linked.getW()+gap);
				linked.setX(linked.getX() + (gap));
			}
			if (z < linked.getZ() - linked.getH())
			{
				//on élargit en dessous de Z : OK
				float gap = (linked.getZ() - linked.getH() - z);
				linked.setH(linked.getH()+gap);
				linked.setZ(linked.getZ() - (gap));
			}
			else if (z > linked.getZ() + linked.getH())
			{
				//on élargit au dessus de Z
				float gap = (z - (linked.getZ() + linked.getH()));
				linked.setH(linked.getH()+gap);
				linked.setZ(linked.getZ() + (gap));
			}
		}
                */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.LaMouseListener#onWheel(int, int, int)
	 */
	public boolean onWheel(int wheelDelta, int x, int y) {
		return false;
	}

	/* ********************************************************** *
	 * *					 Affichage 							* *
	 * ********************************************************** */

	/**
	 * 
	 * @param world
	 */
	public void drawGizmos(Renderer r) {
	/*
            DisplaySystem.getDisplaySystem().getRenderer().clearZBuffer();
		for (GraphicEditable obj : objects) {
			updateGizmos(obj);
			// gizmoAxis.draw(r);
			moveSphere.draw(r);
			/*
			 * donutRotX.draw(r); donutRotY.draw(r); donutRotZ.draw(r);//
			 */
            /*
			if (obj instanceof GraphicReflexEditable)
				hudEditBox.draw(r);
		}
                */
	}

	/* ********************************************************** *
	 * * 					update gizmos 						* *
	 * ********************************************************** */

	/**
	 * Met à jour la position des gizmo
	 * 
	 */
	private void updateGizmos(GraphicEditable obj) {
	/*	
            BoundingVolume target = obj.getGraphic().getWorldBound();
		BoundingBox size = new BoundingBox();
		Vector3f loc = target == null ? new Vector3f() : target.getCenter();
		float s, worldY;
		size.setCenter(loc);
		size.xExtent = size.yExtent = size.zExtent = 0;
		if (target != null)
			size.mergeLocal(target);
		s = size.xExtent + size.yExtent + size.zExtent;
		s = Math.min(4f, s / 3f);

		worldY = game.getWorld().getHeightAt(loc.x, loc.z);

		moveSphere.setLocalTranslation(loc.x, worldY, loc.z);
		moveSphere.setLocalScale(s * SCALE_FACTOR);
		moveSphere.updateGeometricState(0, true);

		hudEditBox.setLocalTranslation(loc.x - size.xExtent, loc.y
				+ size.yExtent, loc.z - size.zExtent);
		hudEditBox.setLocalScale(s * SCALE_FACTOR);
		hudEditBox.updateGeometricState(0, true);
                */
	}

	/* ********************************************************** *
	 * * 				GESTION DES OBJET EDITABLE 				* *
	 * ********************************************************** */

	/**
	 * Ajout un nouvelle objet editable
	 * 
	 * @param s
	 */
	public void add(GraphicEditable s) {
		this.objects.add(s);
	}

	/**
	 * upprime un objet editable
	 * 
	 * @param s
	 */
	public void remove(GraphicEditable s) {
		this.objects.remove(s);
	}

}
