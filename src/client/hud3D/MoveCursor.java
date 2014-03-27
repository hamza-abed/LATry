/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.hud3D;

import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.debug.Arrow;
import shared.variables.Variables;

/**
 *
 * @author Hamza ABED
 * hamza.abed.professionel@gmail.com
 * Cette classe pemet d'afficher un curseur sur le terrain vers lequel
 * la personnage s'oriente et marche vers.
 */
public class MoveCursor extends Arrow{
    
    public MoveCursor()
    {
        super();
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
arrow = new Arrow(Vector3f.UNIT_Y);
arrow.setLineWidth(4); // make arrow thicker

///Vector3f origin    = Variables.getMainPlayer().getPlayerModel().getLocalTranslation();
///Vector3f direction = Variables.getCam().getWorldCoordinates(Variables.getLaGame().getInputManager().getCursorPosition(), 0.3f);
/*ame().getInputManager().getCursorPosition(), 0.3f);*/

///Vector3f location = direction.subtractLocal(origin).normalizeLocal();
//putShape(arrow, ColorRGBA.Green).setLocalTranslation(
//new Vector3f(location.x, location.y,location.z)
//); 
//System.out.println("location "+location + "sinbad "+sinbadPlayer.getLocalTranslation());


/////
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
        System.out.println("----- Collisions? " + results.size() + "-----");
        Vector3f pt=null;
        for (int i = 0; i < results.size(); i++) {
          // For each hit, we know distance, impact point, name of geometry.
          float dist = results.getCollision(i).getDistance();
          pt = results.getCollision(i).getContactPoint();
          String hit = results.getCollision(i).getGeometry().getName();
          System.out.println("* Collision #" + i);
          System.out.println("  You shot " + hit + " at " + pt + ", " + dist + " wu away.");
        }
        System.out.println("sinbad "+Variables.getMainPlayer().getPlayerModel().getLocalTranslation());
        // 5. Use the results (we mark the hit object)
        
        
       // if(arrow!=null) rootNode.detachChild(arrow);
        removeArrow();
        putShape(arrow, ColorRGBA.Green).setLocalTranslation(new Vector3f(pt.x, pt.y,pt.z));
        
        //rootNode.detachChild(sceneModel);

///
    }
    
     Geometry g=null;
private Geometry putShape(Mesh shape, ColorRGBA color){
  g = new Geometry("coordinate axis", shape);
  Material mat = new Material(Variables.getLaGame().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
  mat.getAdditionalRenderState().setWireframe(true);
  mat.setColor("Color", color);
  g.setMaterial(mat);
  Variables.getLaGame().getRootNode().attachChild(g);
  targetAttended=false;
  
  walkToTarget();
  
  return g;
}
private void removeArrow()
{    if(g!=null)
    Variables.getLaGame().getRootNode().detachChild(g);
//Variables.getConsole().output("this is removeArrow from MoveCursor Class");
}

private void walkToTarget()
{
    
}

}
