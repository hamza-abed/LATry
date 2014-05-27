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

import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import shared.variables.Variables;
/**
 * Cette classe représente un noeud graphique qui est utile pour être attaché 
 * à l'interface 2D du jeu et représente un Loading Bar composé de
 *<ul>
 * <li> 2 cubes, l'un à l'intérieur de l'autre </li>
 * <li> Un noeud textuel qui permet d'fficher le message de status de la mission</li>
 * <li> la méthode setAvancementMission(x) prend la valeur du pourcentage d'avancement dans la mission</li>
 * </ul>
 * 
 * @author Hamza ABED 2014 hamza.abed.professionel@gmail.com
 */
public class MissionStatus extends Node{
    


    private static final float INCLINAISON = FastMath.HALF_PI;
    private float ROTATION = 0; // the rotatioon of the box, max =180°
   
    private float boxHeight = 45f;
    private float centerBoxHeight = 15f;
   
    private Geometry statusBoxGeo;
    private Geometry centerOfStatusBoxGeo;
    private float initialScale;
    private BitmapText text;
    public MissionStatus()
    {
        super("missionStatus");
        createBoxes();
    }
    
   private void createBoxes()
   {
       Vector3f centerOfTheBox = new Vector3f(0, 0, 0);
      Box statusBox= new Box(centerOfTheBox,boxHeight, boxHeight, boxHeight);
       statusBoxGeo = new Geometry("statusBox", statusBox);
        Material mat1 = new Material(Variables.getLaGame().getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
              
        mat1.setTexture("ColorMap", Variables.getLaGame().getAssetManager().
                loadTexture("Textures/verre.png"));
        mat1.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
   
    statusBoxGeo.setMaterial(mat1);
   
   
    /********************************************************************/
    /*******************Center Box **************************************/
    /********************************************************************/
    
     Box center= new Box(centerOfTheBox,boxHeight, boxHeight, boxHeight);
       centerOfStatusBoxGeo = new Geometry("statusBox", center);
      Material mat2 = new Material(Variables.getLaGame().getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
              
        mat2.setTexture("ColorMap", Variables.getLaGame().getAssetManager().
                loadTexture("Textures/box.jpg"));
        mat2.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
    
        mat2.setColor("GlowColor", ColorRGBA.Red);
      centerOfStatusBoxGeo.setMaterial(mat2);
        
  
    this.attachChild(centerOfStatusBoxGeo);
      this.attachChild(statusBoxGeo);
    initialScale=centerOfStatusBoxGeo.getLocalScale().x;
    
    /*************************************************************************/
    /***************************AJOUT DE TEXTE *******************************/
    /*************************************************************************/
      
       String txt = "Avancement 10%";
       text = new BitmapText(Variables.getLaGame().getGuiFont());
       text.setSize(Variables.getLaGame().getGuiFont().getCharSet().getRenderedSize());
       text.setText(txt);
       text.setLocalTranslation(statusBoxGeo.getLocalTranslation().x - boxHeight*1.3f,
               -boxHeight*1.3f, 0);

       
       
       this.attachChild(text);
  setAvancementMission(80);
   }
    
    private float variation=0.000f;
  
    public void update(float tpf) {

        
       
        if (ROTATION > 10) {
            ROTATION = 0.1f;

        } else {
            ROTATION += 0.01;
        }
        //	Matrix3f m=new Matrix3f(0, 0, ROTATION, 0, 0, 0, 0, 0, 0);
        statusBoxGeo.setLocalRotation(new Quaternion(ROTATION, 0.1f, 0.2f, 0.8f));
        centerOfStatusBoxGeo.setLocalRotation(new Quaternion(ROTATION, 0.1f, 0.2f, 0.8f));
        variation += 0.0001f;
        
        if (variation > 0.005f) {
            variation = 0.001f;
            centerOfStatusBoxGeo.setLocalScale(initialScale);


        }
        
       // centerOfStatusBoxGeo.setLocalScale(centerOfStatusBoxGeo.getLocalScale().x+variation);
        centerOfStatusBoxGeo.setLocalRotation(new Quaternion(ROTATION, 0.1f, 0.2f, 0.8f));
    }
    
    
    
        public void setAvancementMission(float x) {
            if(x>100)x=100;
            if(x<0)x=0;
        //la valeur de x reçu est en %
        float realValue = x * statusBoxGeo.getLocalScale().x / 100;
            

        centerOfStatusBoxGeo.setLocalScale(realValue);
        initialScale = centerOfStatusBoxGeo.getLocalScale().x; // valeur utilisée dans la vibration du cube au centre
         text.setText("Avancement "+x+"%");
    }
}
    

    
    


