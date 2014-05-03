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
package client.map.object;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import shared.constants.ScriptConstants;
import shared.enums.LaComponent;
//import client.input.MouseCursor;
//import client.input.MouseCursor.CursorType;
//import client.interfaces.graphic.GraphicMouseFocusListener;
//import client.interfaces.graphic.GraphicMouseListener;
import client.interfaces.graphic.GraphicScenarizedEditable;
import client.interfaces.graphic.GraphicShadowed;
import client.interfaces.graphic.GraphicWalkOver;
import client.map.World;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.nio.ByteBuffer;
import shared.pck.Pck;
/*
import com.jme.math.FastMath;
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState.StateType;
import com.jme.system.DisplaySystem;
*/
/**
 * Un object static d'une carte
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic, Syscom 2009-2011
 */
public class BasicMapObject extends AbstractMapObject implements
 GraphicShadowed, GraphicScenarizedEditable, GraphicWalkOver
{
	private static final Logger logger = Logger.getLogger("BasicMapObject");
	private boolean glowing;
	private float currentGlowTime;
	private int glowTime;
	//private ArrayList<MaterialState> glowMats;

	private static final float GLOW_INTENSITY = 0.4f;
	private static final float GLOW_SPEED = .8f;
	private static final int   GLOW_ACTIVE_OBJECT_TIME = 10;


	private String linkedRegion = "";

	/**
	 * @param world
	 * @param idgetSharable
	 */
	public BasicMapObject(World world, int id) {
		super(world, id);
	}

	/* ********************************************************** *
	 * * 				Sharable - IMPLEMENTS 					* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.object.prefix() + id;
	}


	/**
	 * lance un effet de glow sur 2 seconde
	 * @param i
	 */
	private void glow(int time) {
            
            System.out.println("BasicMapObject->glow(int) : manquante !!");
		glowing = true;
		currentGlowTime = 0.0f;
		glowTime=time;
		/*if (model != null) {
			glowMats = new ArrayList<MaterialState>();
			MaterialState glowMat = (MaterialState) model.getRenderState(StateType.Material);
			if (glowMat == null) {
				glowMat = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
				model.setRenderState(glowMat);
				glowMats.add(glowMat);
			} 
			glowMats.add(glowMat);
			if (model instanceof Node) 
				for (Spatial s : ((Node)model).getChildren()) {
					MaterialState ms = (MaterialState) s.getRenderState(StateType.Material);
					if (ms !=null) 
						glowMats.add(ms);
				}
			model.updateRenderState();
		}
                */
	}

	/**
	 * met à jour l'objet
	 * @param interpolation
	 */
	public void update(float interpolation) {
            
            System.out.println("BasicMapObject->update(float) : manquante !!");
		if (model == null) return;
		/*if (glowing) if (currentGlowTime<glowTime) {
			float c = GLOW_INTENSITY*(FastMath.sin(currentGlowTime*GLOW_SPEED*FastMath.TWO_PI));
			for (MaterialState ms : glowMats)
				ms.getEmissive().set(c, c, c, 0);
			currentGlowTime+=interpolation;
		} else {
			glowing = false;
			for (MaterialState ms : glowMats)
				ms.getEmissive().set(0, 0, 0, 0);
		} */
	}


	
	@Override
	public void linkToRegion(String linkedRegionKey) {
		this.linkedRegion  = linkedRegionKey;
	}
	@Override
	public String getLinkedRegion() {
		return linkedRegion;
	}

	/* (non-Javadoc)
	 * @see client.interfaces.graphic.GraphicWalkOver#canWalkOver()
	 */
	@Override
	public boolean canWalkOver() {
		return walkOver; 
	}

	/**
	 * @return
	 */
	public Vector3f getWorldLoc() {
		return getWorld().getLocAt(x, z);
	}

    public Collection<Spatial> getShadowed() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

  

}		