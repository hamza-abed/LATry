/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.input;

import client.map.character.Player;
import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import shared.variables.Variables;

/**
 *
 * @author Hamza ABED
 * Dans la nouvelle version JME3, la classe MouseInputListener n'existe plus
 * c'est pour quoi on a recours à la classe ActionListener
 */
public class MainGameListener implements ActionListener, AnalogListener{

    Spatial sceneModel;
    Player player;
    boolean leftClickForScene=false;
    Node previousCollided=null; //l'objet récement cliqué 
    public MainGameListener(Spatial sceneModel)
    {
        super();
        this.sceneModel=sceneModel;
        player=Variables.getMainPlayer();
    }
    public void onAction(String name, boolean isPressed, float tpf) {
       if(name.equals("LClick"))
{
   
isLeftClickForScene();

}
       
    }
    
    public void onKeyRelease(KeyInputEvent evt)
    {
        System.err.println("key released !! "+evt.getKeyCode());
    }

    public void onAnalog(String name, float value, float tpf) {
       System.out.println("this is analog from mouse listener "+name);
       if(!name.equals("LClick"))
        player.freeMovePlayer(name);
       else // Dans le cas oui en tient un clic sur le bouton gauche de la sourie
       {
           if(!leftClickForScene) // si ce n'est pas un click pour la scène
               //alors il s'agit de glisser un objet
           {
               if(previousCollided!=null)
               { System.out.println("PreviousCollidedName="+previousCollided.getName());
          draggingNode(previousCollided);}
               else
                   System.err.println("Vous avez sélectionné le vide !!");
           
           }
       }
    }
  
   
    
    /*
     * après on doit faire appel à cette méthode à partir
     * d'une classe spécifique "Object" par exemple.
     */
  private void draggingNode(Node object)
  {
      
      
      //On doit tout d'abord fixer la vue de la camera 
      
CollisionResults results = new CollisionResults();
Vector2f click2d = Variables.getLaGame().getInputManager().getCursorPosition();
Vector3f click3d = Variables.getCam().getWorldCoordinates(
    new Vector2f(click2d.x, click2d.y), 0f).clone();
Vector3f dir = Variables.getCam().getWorldCoordinates(
    new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
Ray ray = new Ray(click3d, dir);

Variables.getSceneModel().collideWith(ray, results);



Vector3f pt=null;
  
Variables.getConsole().clear();
        for (int i = 0; i < results.size(); i++) {
          // For each hit, we know distance, impact point, name of geometry.
         // float dist = results.getCollision(i).getDistance();
          pt = results.getCollision(i).getContactPoint();
         
                
        }
        
        
        object.setLocalTranslation(pt);
  }
   
    
    
    /*
     * Cette méthode à pour fonction de s'assurer que l'action de clic 
     * est orienté pour le terrain(scene) ou bien pour un autre objet afin de 
     * le sélectionner
     */
   boolean isClicking=false;
   boolean isDragging=false;
          
  private boolean isLeftClickForScene()
  {  
      System.out.println("verifying if leftClickForScene");
      if(!isClicking && !isDragging)
      {
          isClicking=true;
 leftClickForScene=false;   
CollisionResults results = new CollisionResults();
Vector2f click2d = Variables.getLaGame().getInputManager().getCursorPosition();
Vector3f click3d = Variables.getCam().getWorldCoordinates(
    new Vector2f(click2d.x, click2d.y), 0f).clone();
Vector3f dir = Variables.getCam().getWorldCoordinates(
    new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();
Ray ray = new Ray(click3d, dir);

Variables.getLaGame().getRootNode().collideWith(ray, results);



Vector3f pt=null;
  
Variables.getConsole().clear();
        for (int i = 0; i < results.size(); i++) {
          // For each hit, we know distance, impact point, name of geometry.
          float dist = results.getCollision(i).getDistance();
          pt = results.getCollision(i).getContactPoint();
         String hit="";
         Node collided=results.getCollision(i).getGeometry().getParent();
         previousCollided=collided;
         System.out.println("collided1="+collided.getName());
         while(!collided.getName().equals("Root Node"))
         {
             System.out.println("collided="+collided.getName());
         previousCollided=collided;
         collided=collided.getParent();
                
         }
          
          Variables.getConsole().output("collision avec "+previousCollided.getName());
          /////////// Decision  \\\\\
          if(previousCollided.getName().equals("Scene of the main game"))
          { Variables.getMoveCursor().afficherFlecheDestination(pt); 
          leftClickForScene=true;
         
          
          }
           break; /// c'est un seul itération
        }
        isClicking=false;
      }
        return leftClickForScene;
  }
    
}
