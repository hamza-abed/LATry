package client.videoPlayer;

import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.plugins.AWTLoader;
import com.sun.jna.NativeLibrary;
import de.lessvoid.nifty.render.NiftyImage;
import java.awt.image.BufferedImage;
import shared.variables.Variables;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import java.util.concurrent.Callable;

/**
 * test
 * @author Hamza ABED hamza.abed.professionel@gmail.com
 */
public class VideoPlayer {

   
    
      String chemin="C:\\Wildlife.wmv";
      Material mat1;
      Texture tex;
      DirectMediaPlayerComponentTest playerComponent;
      Element element;
   
      public VideoPlayer(String chemin) {
        
    
      this.chemin=chemin;
      String osName=System.getProperty("os.name");
      System.out.println("OsName="+osName);
        
     
        
        NativeLibrary.addSearchPath(
        RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC"
            );
        
        Box b = new Box(new Vector3f(0, 0, 0), 400, 200, 5);
        geom = new Geometry("Box", b);
        //geom.setLocalScale(3);
        Material mat = new Material(Variables.getLaGame().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        
     
        Variables.getLaGame().getGuiNode().attachChild(geom);
        geom.move(500, 350, 2);
        
         mat1 = new Material(Variables.getLaGame().getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
        
         key =new TextureKey("Textures/dirt.jpg",false);
        tex = Variables.getLaGame().getAssetManager().loadTexture(key);
        
    }
      TextureKey key;
      Geometry geom;
     
    public void startPlaying()
    {
        // this.element=element;
   //    playerComponent= new DirectMediaPlayerComponentTest(this);
       playerComponent.start(chemin); 
       key =new TextureKey( "Textures/dirt.jpg",false);
       tex = Variables.getLaGame().getAssetManager().loadTexture(key);
       tex.setAnisotropicFilter(16);
       tex.setMagFilter(Texture.MagFilter.Bilinear.Bilinear);
    }
    
   
    
    int i=0;
    
    public void apply(BufferedImage image)
    {
       // System.out.println("this is apply!!!");
      AWTLoader loader = new AWTLoader();
      com.jme3.texture.Image load = loader.load(image, true);
      
      
  
        
       mat1 = new Material(Variables.getLaGame().getAssetManager(),
                "Common/MatDefs/Misc/Unshaded.j3md");
           
       
      
        
        tex.setImage(load);
        mat1.setTexture("ColorMap",tex);
        mat1.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        
        Variables.getLaGame().enqueue(
                new Callable<Void>() {
            @Override
            public Void call() throws Exception {
        geom.setMaterial(mat1);
        return null;
            }
                });
        
        
    /*
        Variables.getNifty().getScreen("VideoWindow").findElementByName("VIDWindow").
        getRenderer(ImageRenderer.class).setImage(toNiftyImage(image)); */
       
    }

 
    
    
 public NiftyImage toNiftyImage(BufferedImage image) {
        NiftyImage niftyImage = null;

        com.jme3.texture.Image imageJME = null;
        AWTLoader loader = new AWTLoader();

        try {

            imageJME = loader.load(image, true);

            /*
             * adding to cash as textures
             */
           
  try{         
 
tex = Variables.getLaGame().getAssetManager().loadTexture("Common/MatDefs/SSAO/Textures/random.png");
  }catch(Exception ex)
  {
      System.out.println("problem at tex!!!");
  }
        
         tex.setAnisotropicFilter(16);
         tex.setMagFilter(Texture.MagFilter.Bilinear.Bilinear);
            tex.setImage(imageJME);
            ((DesktopAssetManager) Variables.getLaGame().getAssetManager()).
                    addToCache(new TextureKey("videoFrame"), tex);



            try {

                niftyImage = Variables.getNifty().createImage("videoFrame", false);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return niftyImage;
    }
}
