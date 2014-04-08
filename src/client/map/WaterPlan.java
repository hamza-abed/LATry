/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.map;

import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture2D;
import com.jme3.water.SimpleWaterProcessor;
import com.jme3.water.WaterFilter;
import shared.variables.Variables;

/**
 * Etendu d'eau infini
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Hamza ABED, <b> hamza.abed.professionel@gmail.com</b>, 2014
 */
public class WaterPlan {
    
     private Spatial sceneModel;
    private Quad quad;
    private WaterFilter water;
    private Vector3f lightDirection=new Vector3f(-4,-1,5);
    
    public WaterPlan(Spatial sceneModel){
    this.sceneModel=sceneModel;
    }
    /// ABOUT WATER
   public void initSimpleWater(){ 
       
    SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(Variables.getLaGame().getAssetManager());
       
    waterProcessor.setReflectionScene(sceneModel); 
    Vector3f waterLocation=new Vector3f(0,-6,0); 
    waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
    Variables.getLaGame().getViewPort().addProcessor(waterProcessor); waterProcessor.setWaterDepth(10); // transparency of water 
    waterProcessor.setDistortionScale(0.07f); // strength of waves 
    waterProcessor.setWaveSpeed(0.05f); // speed of waves Quad 
    quad = new Quad(800,800); 
    quad.scaleTextureCoordinates(new Vector2f(6f,6f)); 
    Geometry water=new Geometry("water", quad); 
    water.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X)); 
    water.setLocalTranslation(-400, 0.32f, 400); 
    water.setShadowMode(RenderQueue.ShadowMode.Receive); 
    water.setMaterial(waterProcessor.getMaterial()); 
    Variables.getLaGame().getRootNode().attachChild(water); } 
   
   public void initPPcWater(){ 
       FilterPostProcessor fpp = new FilterPostProcessor(Variables.getLaGame().getAssetManager()); 
       water = new WaterFilter(Variables.getLaGame().getRootNode(), lightDirection); 
       water.setCenter(Vector3f.ZERO); 
       water.setRadius(2600); 
       water.setWaveScale(0.003f); 
       water.setMaxAmplitude(2f); 
       water.setFoamExistence(new Vector3f(1f, 4f, 0.5f)); 
       water.setFoamTexture((Texture2D) Variables.getLaGame().getAssetManager().loadTexture("Common/MatDefs/Water/Textures/foam2.jpg")); 
       water.setRefractionStrength(0.2f); 
       water.setWaterHeight(0.002f); fpp.addFilter(water); 
       Variables.getLaGame().getViewPort().addProcessor(fpp); }



public void setWaterOnTheGame()
{
initSimpleWater();
initPPcWater();
}
}