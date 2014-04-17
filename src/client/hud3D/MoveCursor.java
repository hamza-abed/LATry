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

package client.hud3D;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import shared.variables.Variables;

/**
 *
 * @author Hamza ABED
 * hamza.abed.professionel@gmail.com
 * Cette classe pemet d'afficher un curseur sur le terrain vers lequel
 * la personnage s'oriente et marche vers.
 */
public class MoveCursor{
    
    public MoveCursor()
    {
     
       //  Variables.getLaGame().getPhysicsSpace().addCollisionListener(this);
    }
    private boolean targetAttended=false;

    public boolean isTargetAttended() {
        return targetAttended;
    }

    public void setTargetAttended(boolean targetAttended) {
        this.targetAttended = targetAttended;
    }
    private Arrow arrow;
   Vector3f point;
    public void afficherFlecheDestination(Vector3f pt)
    {
                
///This is about arrow
arrow = new Arrow(new Vector3f(0,3,0));

arrow.setLineWidth(10); // make arrow thicker
removeArrow();

if(pt!=null)
        {
            putShape(arrow, ColorRGBA.Green,pt).setLocalTranslation(new Vector3f(pt.x, pt.y,pt.z));
            Variables.getMainPlayer().moveTo(pt.getX(), pt.getZ()); // utilisation de la méthode de Ludovic Kepka
            
        }



    }
   
    private Node nodeGostCursor;

    public Node getNodeGostCursor() {
        return nodeGostCursor;
    }
    BoxCollisionShape bx;
    public BoxCollisionShape getBoxCollisionGostCursor()
    {
        return bx;
    }
    
    GhostControl ghostCursor;
    
    
    
     Geometry g=null;
private Node putShape(Mesh shape, ColorRGBA color,Vector3f pt){
    
   if(ghostCursor==null)
   {
  Material mat = new Material(Variables.getLaGame().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
  mat.getAdditionalRenderState().setWireframe(true);
  mat.setColor("Color", color);
  
  
  
   g = new Geometry("coordinate axis", shape);
   g.setMaterial(mat);
        bx= new BoxCollisionShape(new Vector3f(3f,02f,1));
        ghostCursor = new GhostControl(bx);
        nodeGostCursor=new Node("gostC");
        nodeGostCursor.attachChild(g);
   }
     
  
  Variables.getLaGame().getRootNode().attachChild(nodeGostCursor);
  Variables.getLaGame().getPhysicsSpace().add(ghostCursor);
  //targetAttended=false;
  g.addControl(ghostCursor);
  nodeGostCursor.addControl(ghostCursor);
  //nodeGostCursor.setLocalRotation(new Quaternion(0, 0 , 180, 180));
  
  
 return nodeGostCursor;
}

public Geometry getShapeCursor()
{
    return g;
}
public void removeArrow()
{    if(Variables.getLaGame().getRootNode().getChild("gostC")!=null)
    Variables.getLaGame().getRootNode().detachChild(Variables.getLaGame().getRootNode().getChild("gostC"));
//Variables.getConsole().output("this is removeArrow from MoveCursor Class");
}


    public void collision(PhysicsCollisionEvent event) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      
     
        if ( event.getNodeB().getName().equals("playerModel") ) 
        {System.out.println("collision name : playerModel"); 
        Variables.getMainPlayer().setTarget(null);
                }
        
        
    }

}
