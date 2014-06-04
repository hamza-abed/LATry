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

import client.LaGame;
import com.jme3.asset.TextureKey;
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
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.texture.Texture;
import com.jme3.texture.plugins.AWTLoader;
import shared.variables.Variables;

/**
 *
 * @author Hamza ABED
 * hamza.abed.professionel@gmail.com
 * Cette classe pemet d'afficher un curseur sur le terrain vers lequel
 * la personnage s'oriente et marche vers.
 */
public class MoveCursor{
    
    private LaGame game;
    public MoveCursor(LaGame game)
    {
     this.game=game;
       //  Variables.getLaGame().getPhysicsSpace().addCollisionListener(this);
    }
    private boolean targetAttended=false;

    public boolean isTargetAttended() {
        return targetAttended;
    }

    public void setTargetAttended(boolean targetAttended) {
        this.targetAttended = targetAttended;
    }
  //  private Arrow arrow;
    private Spatial arrow;
   Vector3f point;
    public void afficherFlecheDestination(Vector3f pt)
    {
                
///This is about arrow
//arrow = new Arrow(new Vector3f(0,3,0));
        arrow=Variables.getLaGame().getAssetManager().loadModel("Models/pointer2/pointer2.j3o");

//arrow.setLineWidth(10); // make arrow thicker
removeArrow();

if(pt!=null)
        {
            putShape(arrow, ColorRGBA.Green,pt).setLocalTranslation(new Vector3f(pt.x, pt.y,pt.z));
            startPosition=pt.y;
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
//private Node putShape(Mesh shape, ColorRGBA color,Vector3f pt){
private Node putShape(Spatial shape, ColorRGBA color,Vector3f pt){
    
   if(ghostCursor==null)
   {
  Material mat = new Material(Variables.getLaGame().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
  mat.getAdditionalRenderState().setWireframe(true);
  mat.setColor("Color", color);
  
  
  
  /*
   * 
   */
 
  
   Material mat1 = new Material(Variables.getLaGame().getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
           
        
        TextureKey key =new TextureKey("Textures/tex2.jpg",false);
        Texture tex = Variables.getLaGame().getAssetManager().loadTexture(key);
        System.out.println("this is show PDF attaching child");
        
        //tex.setImage(
        mat1.setTexture("ColorMap",tex);
      //  shape.setMaterial(mat1);
        
  
  // g = new Geometry("coordinate axis", shape);
  // g.setMaterial(mat);
        bx= new BoxCollisionShape(new Vector3f(3f,02f,1));
        ghostCursor = new GhostControl(bx);
        nodeGostCursor=new Node("gostC");
        //nodeGostCursor.attachChild(g);
        nodeGostCursor.attachChild(shape);
   }
     
  
  game.getRootNode().attachChild(nodeGostCursor);
  game.getPhysicsSpace().add(ghostCursor);
  
  visible=true;
  //targetAttended=false;
 // g.addControl(ghostCursor);
  nodeGostCursor.addControl(ghostCursor);
  //nodeGostCursor.setLocalRotation(new Quaternion(0, 0 , 180, 180));
  
  
 return nodeGostCursor;
}


public void removeArrow()
{    if(Variables.getLaGame().getRootNode().getChild("gostC")!=null)
    Variables.getLaGame().getRootNode().detachChild(Variables.getLaGame().getRootNode().getChild("gostC"));
visible=false;
//Variables.getConsole().output("this is removeArrow from MoveCursor Class");
}


    public void collision(PhysicsCollisionEvent event) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      
     
        if ( event.getNodeB().getName().equals("playerModel") ) 
        {System.out.println("collision name : playerModel"); 
        Variables.getMainPlayer().setTarget(null);
                }
        
        
    }
    
    private boolean visible=false;
    
    
    /*
     * ceci pour faire la petite animation de monté et décente du pointeur
     */
    float animationIndex=0f;
    float startPosition=0f;
    boolean movingUp=true;
    public void update()
    {
        if(animationIndex>=3) movingUp=false;
        if(animationIndex<=0) movingUp=true;
        
        if(movingUp) animationIndex+=0.03f;
        else animationIndex -=0.03f;
        
        if(visible)
            nodeGostCursor.setLocalTranslation(nodeGostCursor.getLocalTranslation().getX(),
                    startPosition+animationIndex,
                    nodeGostCursor.getLocalTranslation().getZ());
    }

}
