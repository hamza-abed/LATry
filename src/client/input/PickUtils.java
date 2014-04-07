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

/**
 * Code sous licence LGPLv3 (http://www.gnu.org/licenses/lgpl.html)
 *
 * @author <b>Shionn</b>, shionn@gmail.com <i>http://shionn.org</i><br>
 * GCS d- s+:+ a- C++ UL/M P L+ E--- W++ N K- w-- M+ t+ 5 X R+ !tv b+ D+ G- e+++ h+ r- !y-
 */
package client.input;

import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Quad;
import java.nio.FloatBuffer;
/*
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;
*/
/**
 * Divers utilitaire pour le picking
 * 
 * 
 * @author <b>Shionn</b>, shionn@gmail.com <i>http://shionn.org</i><br>
 *         GCS d- s+:+ a- C++ UL/M P L+ E--- W++ N K- w-- M+ t+ 5 X R+ !tv b+ D+
 *         G- e+++ h+ r- !y-
 */
public class PickUtils {

	/**
	 * Renvoie les coordopnné en 2D dans un quad par rapport à un ray
	 * 
	 * @param ray
	 * @param plan2
	 * @return null si le rayon ne travers pas le quad
	 */
	public static Vector2f getQuadCoord(Ray ray, Quad quad) {
		FloatBuffer buffer = quad.getFloatBuffer(null);
		float[] coords = new float[buffer.capacity()];
		buffer.get(coords, 0, buffer.capacity());
		Vector3f v = new Vector3f();

		float x = 0, y = 0;
		if (ray.intersectWherePlanar(new Vector3f(coords[0], coords[1],
				coords[2]), new Vector3f(coords[9], coords[10], coords[11]),
				new Vector3f(coords[3], coords[4], coords[5]), v)) {
			x = v.y - 0.5f;
			y = 0.5f - v.z;
		} else if (ray.intersectWherePlanar(new Vector3f(coords[6], coords[7],
				coords[8]), new Vector3f(coords[3], coords[4], coords[5]),
				new Vector3f(coords[9], coords[10], coords[11]), v)) {
			x = 0.5f - v.y;
			y = v.z - 0.5f;
		} else
			return null;
		return new Vector2f(x, y);
	}

	public static Ray getMouseRay(int x, int y) {
            return null;
	/*	Camera camera = DisplaySystem.getDisplaySystem().getRenderer()
				.getCamera();
		Vector3f orig = DisplaySystem.getDisplaySystem().getWorldCoordinates(
				new Vector2f(x, y), 0);
		return new Ray(camera.getLocation(), orig.subtractLocal(
				camera.getLocation()).normalize()); */

	}

}
