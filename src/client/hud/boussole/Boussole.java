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
package client.hud.boussole;

import client.LaGame;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import shared.variables.Variables;

/**
 * Bousolle 3D dans l'interface
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * @author Hamza ABED, <b> hamza.abed.professione@gmail.com</b>, 2014
 */
public class Boussole {

    private static final long serialVersionUID = 7633204763292312313L;
    Geometry cube;
   // private static final float INCLINAISON = FastMath.HALF_PI;
    public static final float DIST_VIEW = 50f;  // 25m
    /**
     *
     */
    Node diskBoussole; // je les ai mis dans des Node parce que la méthode GetLocalRotation
    // n'est plus supporté pour les type Cylender

    public Boussole() {
        Box box = new Box(new Vector3f(0, 0, 0), 90, 90, 5);
        cube = new Geometry("box1", box);
        Material mat1 = new Material(Variables.getLaGame().getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
//mat1.setColor("Color", ColorRGBA.Blue);
        mat1.setTexture("ColorMap", Variables.getLaGame().getAssetManager().
                loadTexture("Textures/boussole/boussole1.png"));
        mat1.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        cube.setMaterial(mat1);
        cube.move(850, 660, 2);
        Variables.getLaGame().getGuiNode().attachChild(cube);

    }
    
    
   public void update()
   {
       /*
        * modifie l'orientation de la boussole selon l'orientation de la caméra 
        * qui suit par defaut le joueur.
        * Cette méthode est à appeler à chaque appel d'update du jeu 
        */
       cube.setLocalRotation(new Quaternion(cube.getLocalRotation().getX(),cube.getLocalRotation().getY(),
               Variables.getCam().getRotation().getY(),Variables.getCam().getRotation().getW()));
   }
}
