/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.hud.missionStatus;

import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import shared.variables.Variables;
/**
 *
 * @author admin
 */
public class MissionStatus extends Node{
    


    private static final float INCLINAISON = FastMath.HALF_PI;
    private float ROTATION = 0; // the rotatioon of the box, max =180°
   
    private float boxHeight = 45f;
    private float centerBoxHeight = 15f;
   
    private Geometry statusBoxGeo;
    private Geometry centerOfStatusBoxGeo;
    private float initialScale;
    
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
                loadTexture("Textures/cage.png"));
        mat1.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
   
    statusBoxGeo.setMaterial(mat1);
   
   
    /********************************************************************/
    /*******************Center Box **************************************/
    /********************************************************************/
    
     Box center= new Box(centerOfTheBox,centerBoxHeight, centerBoxHeight, centerBoxHeight);
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
       BitmapText text = new BitmapText(Variables.getLaGame().getGuiFont());
       text.setSize(Variables.getLaGame().getGuiFont().getCharSet().getRenderedSize());
       text.setText(txt);
       text.setLocalTranslation(statusBoxGeo.getLocalTranslation().x - boxHeight*1.3f,
               -boxHeight*1.3f, 0);

       
       
       this.attachChild(text);
  
   }
    
    private float variation=0.000f;
    private int retardeurDeuxFrames =0;
    public void update(float tpf) {

        
        if(retardeurDeuxFrames==0)retardeurDeuxFrames=1;
        else retardeurDeuxFrames=0;
        if (ROTATION > 10) {
            ROTATION = 0.1f;

        } else {
            ROTATION += 0.01;
        }
        //	Matrix3f m=new Matrix3f(0, 0, ROTATION, 0, 0, 0, 0, 0, 0);
        statusBoxGeo.setLocalRotation(new Quaternion(ROTATION, 0.1f, 0.2f, 0.8f));
        centerOfStatusBoxGeo.setLocalRotation(new Quaternion(ROTATION, 0.1f, 0.2f, 0.8f));
        variation += 0.0001f*retardeurDeuxFrames;
        
        if (variation > 0.005f) {
            variation = 0.001f;
            centerOfStatusBoxGeo.setLocalScale(initialScale);


        }
        
        centerOfStatusBoxGeo.setLocalScale(centerOfStatusBoxGeo.getLocalScale().x+variation);
        centerOfStatusBoxGeo.setLocalRotation(new Quaternion(ROTATION, 0.1f, 0.2f, 0.8f));
    }
}
    

    
    


