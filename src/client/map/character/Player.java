/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.map.character;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.state.AbstractAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Matrix3f;
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
      
    } else if (binding.equals("Up") || moving) {
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
    
    private boolean moving=false;
    private Vector3f target;
    
    public void moveTo(Vector3f to)
    {
        i1=1;
       
        target=new Vector3f(to);
        moving=true;
    }
    public void endMoving()
    {
        moving=false;
    }
  
   int i1=0;
   public void update()
   
    {
        //System.out.println("this is update from the player class");
          
        camDir.set(Variables.getCam().getDirection()).multLocal(0.6f);
        camLeft.set(Variables.getCam().getLeft()).multLocal(0.4f);
         walkDirection.set(0, 0, 0);
        if(moving && i1<50)
        { i1++;
            if(target.x==playerModel.getLocalTranslation().x) moving=false;
        walkDirection.addLocal(target.x,target.y,target.z);
        player.setWalkDirection(walkDirection);
        player.setViewDirection(walkDirection);
            System.out.println("is moving "+target.x+" "+camDir.y+" "+target.z);
        }
        else{
       
       
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
           System.err.println("walkDirection= "+walkDirection.toString());
       player.setViewDirection(new Vector3f(walkDirection.x, 0, walkDirection.z)); 
       lastWalkDirection=new Vector3f(walkDirection.x,walkDirection.y,walkDirection.z);
       }
      //  System.err.println("walkdirection= "+walkDirection.toString());
 // playerModel.rotate(5, 0, 0);
 //player.setViewDirection(walkDirection);
 // modification de l'emplacement du character
 
      /*
        playerModel.move(new Vector3f(
        player.getPhysicsLocation().x, player.getPhysicsLocation().y-10,
        player.getPhysicsLocation().z-10));
      */
    }
    }
 /*  
   * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
   * updated for the v.2014 by Hamza ABED  @2014
 */
   public void moveTo(float x, float z) {
		//this.moving = Moving.target;
		//moveAnimation();

		Vector3f o = playerModel.getLocalTranslation();

		// check je crois que c'est inutile
		// Rep : oui c'est utile pour le calcul de déplacement
		//this.x = x;
		//this.z = z;
		// fin check

		Vector3f t = new Vector3f(x, o.y, z);
		t.subtractLocal(o).normalizeLocal();

		Matrix3f m = new Matrix3f();
		m.fromStartEndVectors(Vector3f.UNIT_Z, t);

		Quaternion q = new Quaternion().fromRotationMatrix(m);
		Quaternion q2 = new Quaternion().fromRotationMatrix(m.invert());

		// correction d'un bug survenant quand la camera est dans l'axe des Z
		if (q.getX() > 0.5f) {
                    q.set(0,q.getX(),q.getZ(),q.getW());
			//q.y = q.x;
			//q.x = 0;
                    q2.set(0, q2.getX(), q2.getZ(), q2.getW());
			//q2.y = q2.x;
			//q2.x = 0;	
		}
//this is about rotation
                
                playerModel.setLocalRotation(q);
                /*
		if (characterNode != null) {
			characterNode.setLocalRotation(q);
			onHead.setLocalRotation(q2);
		}
                */
	}
 
}
