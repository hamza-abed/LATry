/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.map.character;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import shared.variables.Variables;

/**
 *
 * @author admin
 */
public class Player implements AnimEventListener{
    private AnimChannel channel;
    private boolean left=false,right=false,up=false,down=false;
    private Vector3f camDir  = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    

    public AnimChannel getChannel() {
        return channel;
    }

    public void setChannel(AnimChannel channel) {
        this.channel = channel;
    }

    public AnimControl getControl() {
        return control;
    }

    public void setControl(AnimControl control) {
        this.control = control;
    }

    public Vector3f getWalkDirection() {
        return walkDirection;
    }

    public void setWalkDirection(Vector3f walkDirection) {
        this.walkDirection = walkDirection;
    }
    private AnimControl control;
    private CharacterControl player;

    public CharacterControl getPlayer() {
        return player;
    }

    public void setPlayer(CharacterControl player) {
        this.player = player;
    }
    private  Node playerModel;

    public Node getPlayerModel() {
        return playerModel;
    }

    public void setPlayerModel(Node playerModel) {
        this.playerModel = playerModel;
    }
    private Vector3f walkDirection = new Vector3f();
    public Player()
    {
        initPlayer();
    }
    
    private void initPlayer()
    {
          
        playerModel =(Node) Variables.getLaGame().getAssetManager().loadModel("Models/high-sinbad/Sinbad.mesh.xml");
        playerModel.setLocalScale(0.5f);
       // rootNode.attachChild(playerModel);
        playerModel.move(10, 5, -10); 
        
        control = playerModel.getControl(AnimControl.class);
        control.addListener((AnimEventListener) this);
        channel = control.createChannel();
        channel.setAnim("idle");
        
         CapsuleCollisionShape capsuleShape = 
         new CapsuleCollisionShape(2f, 1f, 1);
        
        
        player = new CharacterControl(capsuleShape, 0.1f);
        player.setJumpSpeed(30);
        player.setFallSpeed(600);
        player.setGravity(50);
        
        player.setPhysicsLocation(new Vector3f(-10, 5, -10));
       // playerModel.addControl(control);
        playerModel.setName("playerModel");
       
        
     Variables.getLaGame().getBulletAppState().getPhysicsSpace().add(player);
     // Variables.setMainPlayer(sinbadPlayer);
      
       
    }
    
   
    public void attachToScene()
    {
        Variables.getLaGame().getRootNode().attachChild(playerModel);
        
    }
    //private float turned=0;
   public  void turnCharacterDependentlyOnCam()
   {   System.out.println("this is turn character");
     //if(turned==0) turned=walkDirection.y;
       System.out.println("walkDirection = "+Variables.getCam().getRotation().getY()+"  "+playerModel.getLocalRotation().getY());
       Quaternion q=playerModel.getLocalRotation();
       playerModel.setLocalRotation(new Quaternion(q.getX(),Variables.getCam().getRotation().getY(),q.getZ(),Variables.getCam().getRotation().getW()));
     //playerModel.rotate(0, 10, 0); 
      // Variables.getLaGame().getRootNode().getChild("playerModel").rotate(0, 10, 0); 
       playerModel.updateGeometricState();
  }

   public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
  System.out.println("this is on animcycleDone");
       
       if (animName.equals("walk")&&!up) {
        channel.setAnim("idle");    
    }
       else if (animName.equals("walk")) channel.setAnim("walk");
  }
   
   
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        
    }
    
    
    
    
    private int verifyUpAnalog1=0; 
    private int verifyUpAnalog2=0; 
    public void freeMovePlayer(String binding)
    {
      
    
       turnCharacterDependentlyOnCam();
      if (binding.equals("Left")) {
      left = true;
      
    } else if (binding.equals("Right")) {
      right= true;
      
    } else if (binding.equals("Up")) {
      up = true;
      verifyUpAnalog1++;
   // sinbadPlayer.rotateUpTo(new Vector3f(player.getViewDirection().x,cam.getRotation().getY(),sinbadPlayer.getLocalRotation().getZ()));
      
    } else if (binding.equals("Down")) {
      down = true;
    } else if (binding.equals("Jump")) {
       
       player.jump();
    }
      
  // System.out.println("walk "+ isPressed +" "+ binding);
        if (up && !channel.getAnimationName().equals("walk")) {
          channel.setAnim("walk",0.5f);
          channel.setLoopMode(LoopMode.Loop);
        }
        if(!up) channel.setAnim("idle");
        
    }
    Vector3f lastWalkDirection=new Vector3f(0,0,0);
    Vector3f nullVector=new Vector3f(0,0,0);
    public void update()
    {
        //System.out.println("this is update from the player class");
          
        camDir.set(Variables.getCam().getDirection()).multLocal(0.6f);
        camLeft.set(Variables.getCam().getLeft()).multLocal(0.4f);
        walkDirection.set(0, 0, 0);
       
       if (left) {
            walkDirection.addLocal(camLeft);
            left=false;
        
                  }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
            right=false;
        }
        if (up) {
            walkDirection.addLocal(camDir);
           
            if(verifyUpAnalog1 ==verifyUpAnalog2){up=false; verifyUpAnalog1 =verifyUpAnalog2=0; }
            else
                verifyUpAnalog2=verifyUpAnalog1;
        }
        if(down) {
            walkDirection.addLocal(camDir.negate());
            down=false;
        }
        
        
       player.setWalkDirection(walkDirection);
       if(walkDirection.equals(nullVector))
       player.setViewDirection(new Vector3f(lastWalkDirection.x, 0, lastWalkDirection.z));
       else
       {
       player.setViewDirection(new Vector3f(walkDirection.x, 0, walkDirection.z)); 
       lastWalkDirection=new Vector3f(walkDirection.x,walkDirection.y,walkDirection.z);
       }
       
      // playerModel.rotate(5, 0, 0);
       //player.setViewDirection(walkDirection);
       
     
        // modification de l'emplacement du character
     /*   playerModel.move(new Vector3f(
        player.getPhysicsLocation().x, player.getPhysicsLocation().y-10,
        player.getPhysicsLocation().z-10));
      */
    }
 
}
