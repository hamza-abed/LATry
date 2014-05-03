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

import shared.enums.LaComponent;
import client.interfaces.graphic.GraphicShadowed;
import client.interfaces.graphic.GraphicWithGround;
import client.map.World;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Renderer;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
/*
import com.jme.intersection.TrianglePickResults;
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
*/
/**
 * un batiment
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class BuildingMapObject extends AbstractMapObject implements
		GraphicWithGround, GraphicShadowed {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("BuildingMapObject");

	/**
	 * @param world
	 * @param id
	 */
	public BuildingMapObject(World world, int id) {
		super(world, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.building.prefix() + id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.map.object.AbstractMapObject#isBuilding()
	 */
	@Override
	protected boolean isBuilding() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.GraphicsWalkable#getGround()
	 */
	@Override
	public Spatial getGround() {
		return ground;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.GraphicShadowed#getShadowed()
	 */
	@Override
	public Collection<Spatial> getShadowed() {
            
            System.out.println("BuildingMapObject->getShadowed() : manquante !!");
		ArrayList<Spatial> shadow = new ArrayList<Spatial>();
		/*if (getGraphic() instanceof Node)
			for (Spatial s : ((Node)getGraphic()).getChildren()) {
				if (s.getRenderQueueMode() != Renderer.QUEUE_TRANSPARENT)
					shadow.add(s);
			}
		else
			shadow.add(getGraphic()); */
		return shadow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * client.interfaces.graphic.GraphicWalkable#getHeightAt(com.jme.math.Vector3f
	 * )
	 */
	@Override
	public Vector3f getHeightAt(Vector3f v) {
		//if (ground == null)
		

		Ray ray = new Ray(v, Vector3f.UNIT_Y.negate());
                /*
		TrianglePickResults results = new TrianglePickResults();
		results.setCheckDistance(true);

		ground.calculatePick(ray, results);
		if (results.getNumber() < 1)
			return null;
		float d = results.getPickData(0).getDistance();
		if (Float.isInfinite(d) || Float.isNaN(d))
			return null;

		return v.add(Vector3f.UNIT_Y.mult(-d));
                */
                	return null;
	}

	/* ********************************************************** *
	 * * TAG * * **********************************************************
	 */

	/* ********************************************************** *
	 * * TAG * * **********************************************************
	 */

   
    
}
