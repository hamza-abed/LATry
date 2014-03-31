/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.hud3D;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
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
   
    public void afficherFlecheDestination()
    {
                
///This is about arrow
arrow = new Arrow(new Vector3f(0,3,0));
arrow.setLineWidth(10); // make arrow thicker
// arrêt du joueur selon la collision avec l'indicateur

 // 1. Reset results list.
CollisionResults results = new CollisionResults();
Vector2f click2d = Variables.getLaGame().getInputManager().getCursorPosition();
Vector3f click3d = Variables.getCam().getWorldCoordinates(
    new Vector2f(click2d.x, click2d.y), 0f).clone();
Vector3f dir = Variables.getCam().getWorldCoordinates(
    new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
Ray ray = new Ray(click3d, dir);
Variables.getSceneModel().collideWith(ray, results);



//        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.
  //      Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        // 3. Collect intersections between Ray and Shootables in results list.
        Variables.getSceneModel().collideWith(ray, results);
        // 4. Print the results
       // System.out.println("----- Collisions? " + results.size() + "-----");
        Vector3f pt=null;
        for (int i = 0; i < results.size(); i++) {
          // For each hit, we know distance, impact point, name of geometry.
          float dist = results.getCollision(i).getDistance();
          pt = results.getCollision(i).getContactPoint();
          String hit = results.getCollision(i).getGeometry().getName();
        
        }
      
        removeArrow();
       // Vector3f camDir=new Vector3f();
      //  camDir.set(Variables.getCam().getDirection()).multLocal(0.6f);
        if(pt!=null)
        {
            putShape(arrow, ColorRGBA.Green,pt).setLocalTranslation(new Vector3f(pt.x, pt.y,pt.z));
           // putShape(arrow, ColorRGBA.Green).setLocalTranslation(new Vector3f(camDir.x+5,camDir.y-5,camDir.z+2));
          //  System.out.println("pt= "+pt.toString());
            Variables.getMainPlayer().moveTo(pt.getX(), pt.getZ()); // utilisation de la méthode de Ludovic Kepka
            
     //   Variables.getMainPlayer().moveTo(pt.normalize());
            
    //*** pour la collision avec la flèche
    
    // initiateGostCursor(pt);
    
     
      //  System.out.println("this is after move to");
        }
        //rootNode.detachChild(sceneModel);

///
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
  //Variables.getLaGame().getPhysicsSpace().add(bx);
  //ghostCursor.setPhysicsLocation(pt);
  System.out.println("control cursor location = "+ghostCursor.getPhysicsLocation().toString());
 return nodeGostCursor;
}

public Geometry getShapeCursor()
{
    return g;
}
private void removeArrow()
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
