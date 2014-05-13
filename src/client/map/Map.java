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
package client.map;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import shared.constants.LaConstants;
import shared.constants.ScriptConstants;
import shared.enums.LaComponent;
import shared.pck.Pck;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
import client.interfaces.graphic.GraphicShadowed;
import client.interfaces.network.SharableReflexEditable;
import client.utils.FileLoader;
import com.jme3.material.Material;
import com.jme3.math.Ray;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.terrain.heightmap.RawHeightMap;
import com.jme3.texture.plugins.AWTLoader;
import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import shared.variables.Variables;

/*
import com.jme.image.Texture;
import com.jme.intersection.PickResults;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.Ray;
import com.jme.math.Triangle;
import com.jme.math.Vector3f;
import com.jme.scene.PassNode;
import com.jme.scene.PassNodeState;
import com.jme.scene.Spatial;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jmex.terrain.TerrainPage;
import com.jmex.terrain.util.AbstractHeightMap;
import com.jmex.terrain.util.ImageBasedHeightMap;
import com.jmex.terrain.util.RawHeightMap;
*/
/**
 * Une carte du jeux
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class Map implements SharableReflexEditable, GraphicShadowed {
	private static final long serialVersionUID = 4207771985761681469L;
	private static final Logger logger = Logger.getLogger("Map");
	
	private static final float CALC=.5f;

	/**
	 * position du terrain
	 */
	private int x, z;

	/**
	 * liste des texture
	 */
	@Editable(type=FieldEditType.list,innerType=FieldEditType.file,fileFolder=LaConstants.DIR_MAP_HIGH_RES_TEXTURE,applyMethod="launchRebuild")
	private ArrayList<String> textures = new ArrayList<String>();

	/**
	 * liste des melange alpha
	 */
	@Editable(type=FieldEditType.list,innerType=FieldEditType.file,fileFolder=LaConstants.DIR_MAP_DATA,applyMethod="launchRebuild")
	private ArrayList<String> alphas = new ArrayList<String>();

	/**
	 * altimetrie
	 */
	@Editable(type=FieldEditType.file,fileFolder=LaConstants.DIR_MAP_DATA,applyMethod="launchRebuild")
	private String height;

	/**
	 * test de profondeur
	 */
	//private ZBufferState zs;

	/**
	 * fonction de melange de couleur des differente altimetrie
	 */
	//private BlendState bs;

	/**
	 * Object simplifier pour la projection des ombre (pas de texture en multi
	 * pass)
	 */
	//private TerrainPage shadowed;

	/**
	 * terrain
	 */
	//private TerrainPage terrain;

	/**
	 * monde conteneur
	 */
	private World world;

	/**
	 * 
	 */
	private boolean loaded = false;

	private int size;

	private int versionCode = -1;

	//private PassNodeState debugPns;

	@Editable(type=FieldEditType.action)
	private String onEnter = ScriptConstants.VOID_SCRIPT;
	
	@Editable(type=FieldEditType.string)
	private String sky = "jme";
	
	//private DotMap dotmap;
	//private boolean isDotMap; 
	
	// vecteur de calcule.
	private Vector3f v1=new Vector3f(),v2=new Vector3f(),v3=new Vector3f(),v4=new Vector3f();
	@Editable(type=FieldEditType.realinterval,realMinValue=0f,realMaxValue=1f,realStepValue=.05f)
	private float fogR = 1f, fogG=1, fogB=1, fogA=1;
	
	/**
	 * @param x
	 * @param z
	 */
	public Map(World world, int x, int z) {
	//	super("Map(" + x + "," + z + ")");
		this.world = world;
		this.x = x;
		this.z = z;
/*
		zs = DisplaySystem.getDisplaySystem().getRenderer()
		.createZBufferState();
		zs.setEnabled(true);
		zs.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);

		bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		bs.setBlendEnabled(true);
		bs.setEnabled(true);
		bs.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		bs.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		bs.setTestEnabled(true);
		bs.setTestFunction(BlendState.TestFunction.GreaterThan);

		this.setLocalTranslation((x + .5f) * world.getMapSize(), -world.getWorldWaterDeep(), (z + .5f) * world.getMapSize());
		this.setCullHint(CullHint.Dynamic);
*/
		height = "basic/underwater.jpg";
		textures.add("jme/darkrock.jpg");
		launchRebuild();

		Variables.getClientConnecteur().updateFromServer(this);
	}

	/**
	 * lance la reconstruction graphique dans un nouveau thread à l'update
	 * 
	 * @return
	 */
	private Future<Void> launchRebuild() {
           
		return Variables.getLaGame().enqueue(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				rebuild();
				return null;
			}
		});
                
           
          
	}

	/**
	 * Reconstruit la carte
	 */
	private void rebuild() {
            
            System.out.println("Map -> rebuild()");
		if (loaded) {
			world.removeGraphics(this);// méthode vide !!
			//super.clearAll();
			//terrain.removeFromParent();
		}
		
		//updateType();
		/*if (isDotMap)
			loadDotMap();
		else//*/
			loadHeightMap();

		loaded = true;
		world.addGraphics(this);
	}

	/**
	 * met à jour le type de carte
	 */
	/*private void updateType() {
		isDotMap = height.endsWith("map");
	}//*/

	/**
	 * charge une carte en dot
	 */
	/*private void loadDotMap() {
		dotmap = new DotMap("LA3-Newb", 512, world.getPlayer().getGraphic());
		dotmap.setLocalTranslation(this.getLocalTranslation());
	}//*/

	/**
	 * charge une carte en heightmaps
	 */
        
        /*
         * Dans la version courante en souhaite remplacer le terrainPage qui est impossible à 
         * établir en JME3 par 
         */
        private AbstractHeightMap heightMap;
        private TerrainQuad terrain;
    Material mat_terrain;
	private void loadHeightMap() {
		          try {

                @SuppressWarnings("unused")
                boolean flipTexture = false;


                if (height.endsWith("raw")) {
                    heightMap = new RawHeightMap(FileLoader
                            .getResourceAsUrl(LaConstants.DIR_MAP_DATA + height),
                            129, RawHeightMap.FORMAT_16BITBE, false);
                    flipTexture = true;
                } else {
                    /* ceci charge une image en BuffredImage et la converte en jme3.Image
                     * Hamza ABED
                     */
                    BufferedImage bImg = ImageIO.read(FileLoader
                            .getResourceAsUrl(LaConstants.DIR_MAP_DATA + height));
                    AWTLoader loader = new AWTLoader();
                    com.jme3.texture.Image load = loader.load(bImg, true);
                    heightMap = new ImageBasedHeightMap(load);
                    heightMap.load();
                }

                size = heightMap.getSize();
                Vector3f scale = new Vector3f(world.getMapSize() / (size - 1f),
                        world.getWorldScaleY(), world.getMapSize() / (size - 1f));
                int patchSize = 65;
                //terrain = new TerrainQuad("my terrain", patchSize, 513, heightMap.getHeightMap());
                terrain = new TerrainQuad("my terrain", 33, size,scale ,heightMap.getHeightMap());


                //terrain = new TerrainPage(toString(), 33, size, scale, heightMap.getHeightMap());
/*
                 shadowed = new TerrainPage(toString(), 33, size, scale, heightMap
                 .getHeightMap());

                 shadowed.setLocalTranslation(this.getLocalTranslation());
                 shadowed.setRenderState(zs);
                 shadowed.updateRenderState();
                 shadowed.setCullHint(CullHint.Dynamic);
                 shadowed.lock();

                 terrain.setDetailTexture(1, 1);

                 this.attachChild(terrain);

                 PassNodeState pns = new PassNodeState();
                 pns.setPassState(createTextureLayer(textures.get(0), null, true));
                 this.addPass(pns);

                 int nbSplat = Math.min(textures.size() - 1, alphas.size());
                 for (int i = 0; i < nbSplat; i++) {
                 pns = new PassNodeState();
                 pns.setPassState(createTextureLayer(textures.get(i + 1), alphas
                 .get(i), false));
                 pns.setPassState(bs);
                 pns.setPassState(zs);
                 this.addPass(pns);
                 }

                 debugPns = new PassNodeState();
                 debugPns.setPassState(createTextureLayer("basic/red.jpg","basic/zone.png", true));
                 debugPns.setPassState(bs);
                 debugPns.setPassState(zs);
                 debugPns.setEnabled(false);
                 this.addPass(debugPns);

                 // this.updateRenderState();
                 terrain.lock();
                 */
            } catch (IOException e) {
                throw new RuntimeException("Putain de too Many Exception");
            } catch (ArrayIndexOutOfBoundsException e) {
                logger.warning("Il manque un alpha ou une texture ");
            } catch (Exception ex) {
                Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
            }
	}

	/**
	 * met à jour la carte (affiche masque le decoupage de zone en fonction de
	 * l'activation/desactivation du mode debug)
	 * 
	 * @param interpolation
	 */
	public void update(float interpolation) {
		/*if (loaded) {
			if (Variables.isEditMode() && !debugPns.isEnabled())
				debugPns.setEnabled(true);
			if (!world.getGame().isEditMode() && debugPns.isEnabled())
				debugPns.setEnabled(false);
		}*
                */
            System.out.println("Map-> update() : vide !!");
	}

	/* ********************************************************** *
	 * * 				Graphics - Implements 					* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.Graphic#getGraphic()
	 */
	@Override
	public Spatial getGraphic() {
		//return this;
            return null;
		//return isDotMap?dotmap:this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.GraphicShadowed#getShadowed()
	 */
	public Collection<Spatial> getShadowed() {
		ArrayList<Spatial> out = new ArrayList<Spatial>();
		//out.add(shadowed);
                System.out.println("Map -> getShadowed() : manquante !!");
		return out;
	}

	/* ********************************************************** *
	 * * 						PICKING 						* * 
	 * ********************************************************** */

	/**
	 * Trouve le point d'intersection avec le terrain
	 * 
	 * @param ray
	 * @return
	 */
	public Vector3f pickAt(Ray ray) {
	/*	PickResults result = new TrianglePickResults();
		result.setCheckDistance(true);
		result.clear();

		// TODO faire un check sur une distance positive

		this.findPick(ray, result);
		if (result.getNumber() > 0
				&& !Float.isInfinite(result.getPickData(0).getDistance())) {
			float dist = result.getPickData(0).getDistance();
			Vector3f v = ray.getOrigin().add(ray.getDirection().mult(dist));
			logger.fine("pick on map " + this + "@(" + v.x + "," + v.y + ","
					+ v.z + ")");
			return v;
		}

		logger.exiting(Map.class.getName(), "pick"); */
            System.out.println("Map -> pickAT() : vide !!");
		return null;
	}

	/* ********************************************************** *
	 * * 			Creation d'un état de texture 				* *
	 * ********************************************************** */
/*   INTRODUISABLE EN JME3 !!! on n'a pas de TextureState en JME3 !!
	private TextureState createTextureLayer(String texture, String alpha,
			boolean flipAlpha) {
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer()
		.createTextureState();

		Texture baseTexture = TextureManager.loadTexture(texture,
				Texture.MinificationFilter.Trilinear,
				Texture.MagnificationFilter.Bilinear);
		baseTexture.setWrap(Texture.WrapMode.Repeat);
		baseTexture.setApply(Texture.ApplyMode.Modulate);
		baseTexture.setScale(new Vector3f(this.size, this.size, 1.0f));
		ts.setTexture(baseTexture, 0);

		if (alpha != null) {
			Texture alphaTexture = TextureManager.loadTexture(alpha,
					Texture.MinificationFilter.Trilinear,
					Texture.MagnificationFilter.Bilinear, 0, flipAlpha);
			alphaTexture.setWrap(Texture.WrapMode.Repeat);
			alphaTexture.setApply(Texture.ApplyMode.Combine);
			alphaTexture.setCombineFuncRGB(Texture.CombinerFunctionRGB.Replace);
			alphaTexture.setCombineSrc0RGB(Texture.CombinerSource.Previous);
			alphaTexture
			.setCombineOp0RGB(Texture.CombinerOperandRGB.SourceColor);
			alphaTexture
			.setCombineFuncAlpha(Texture.CombinerFunctionAlpha.Replace);
			ts.setTexture(alphaTexture, ts.getNumberOfSetTextures());
		}

		return ts;
	}
*/
	/* ********************************************************** *
	 * * 					Model editeur de carte 				* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * * 					CALCUL DE HAUTEUR 					* *
	 * ********************************************************** */

	/**
	 * Renvoie la hauteur au point du terrain (coordonnée dans le monde)
	 * mise à jour par Hamza ABED
	 * @param x coordonnée du monde
	 * @param z coordonnée du monde
	 * @return
	 */
	public float getHeightAt(float x, float z) {
     /*    if (loaded)   
          return  heightMap.getInterpolatedHeight(x - getLocalTranslation().x, z
					- getLocalTranslation().z)- world.getWorldWaterDeep();
                                        */
		/*if (loaded)
			return terrain.getHeight(x - getLocalTranslation().x, z
					- getLocalTranslation().z)
					- world.getWorldWaterDeep(); */
            System.out.println("Map -> getHeightAt() vide !!!");
		return 0;
	}

	/**
	 * Renvoie la pente en ce point
	 * @param x
	 * @param z
	 * @return
	 */
	public float getSlopAt(float x, float z) {
		/*if (loaded) {
			x = x- getLocalTranslation().x;
			z = z - getLocalTranslation().z;
                        System.out.println("Map -> getSlopAt() : méthode mise à jour !!!!");
			v1.set(x-CALC, heightMap.getInterpolatedHeight(x-CALC, z-CALC), z-CALC);
			v2.set(x-CALC, heightMap.getInterpolatedHeight(x-CALC, z-CALC), z+CALC);
			v3.set(x+CALC, heightMap.getInterpolatedHeight(x-CALC, z-CALC), z+CALC);
			v4.set(x+CALC, heightMap.getInterpolatedHeight(x-CALC, z-CALC), z-CALC);
			
			Triangle t1 = new Triangle(v1,v2,v3);
			Triangle t2 = new Triangle(v1,v3,v4);
			
			t1.calculateNormal();
			t2.calculateNormal();
			
			
			
			return Math.max(t1.getNormal().angleBetween(Vector3f.UNIT_Y),
					t2.getNormal().angleBetween(Vector3f.UNIT_Y));
			
			//errain.get
			
			
			//return terrain.getSurfaceNormal(x- getLocalTranslation().x,z - getLocalTranslation().z, null).angleBetween(Vector3f.UNIT_Y);
		}*/
            System.out.println("Map -> getSlopAt() vide !!!");
		return 0;
                
	}


	/* ********************************************************** *
	 * * 					Sharable - IMPLEMENTS 				* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.network.Sharable#getVersionCode()
	 */
	@Override
	public int getVersionCode() {
		return versionCode;
	}

	/*
	 
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * client.interfaces.network.Sharable#receiveCommit(java.nio.ByteBuffer)
	 */
	@Override
	public void receiveCommitPck(ByteBuffer message) {
		int nvc = message.getInt(); // version code
		this.height = Pck.readString(message);
		this.onEnter = Pck.readString(message);
		this.sky = Pck.readString(message);
		this.fogR=message.getFloat();
		this.fogG=message.getFloat();
		this.fogB=message.getFloat();
		this.fogA=message.getFloat();
		
		int nb = message.getInt();

		this.textures.clear();
		for (int i = 0; i < nb; i++)
			this.textures.add(Pck.readString(message));

		this.alphas.clear();
		for (int i = 0; i < nb - 1; i++)
			this.alphas.add(Pck.readString(message));

		logger.fine("Receive map : " + this + " (" + textures.size()
				+ " textures, " + alphas.size() + " alpha)");
		this.versionCode = nvc;
		try {
			Variables.getTaskExecutor().execute(new Runnable() {
				@Override
				public void run() {
					world.getPlayer().mapUpdate();
				}
			});
			launchRebuild().get();
		} catch (InterruptedException e) {
			logger.warning("InterruptedException : Je le savais !");
		} catch (ExecutionException e) {
			logger.warning("ExecutionException : Je le savais !");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	@Override
	public void addData(Pck pck) {
		pck.putString(height,onEnter,sky);
		pck.putFloat(fogR,fogG,fogB,fogA);
		int nb = Math.min(textures.size(), alphas.size() + 1);
		pck.putInt(nb);
		for (int i = 0; i < nb; i++)
			pck.putString(textures.get(i));
		for (int i = 0; i < nb - 1; i++)
			pck.putString(alphas.get(i));
	}

	/* ********************************************************** *
	 * * 				Graphics - IMPLEMENTS 					* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.Graphic#addToRenderTask()
	 */
	@Override
	public void addToRenderTask() {
		throw new RuntimeException("Not Have to Be Used");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.Graphic#removeFromRenderTask()
	 */
	@Override
	public void removeFromRenderTask() {
		throw new RuntimeException("Not Have to Be Used");
	}


	/* ********************************************************** *
	 * * 				EXECUTION DE SCRIPT 					* *
	 * ********************************************************** */

	/**
	 * Execute l'entré d'une carte
	 */
	public void enter() {
		world.getGame().getSchedulerTaskExecutor().schedule(new Runnable() {
			@Override
			public void run() {
				if (versionCode != -1) {
					world.getScriptExecutor().execute(onEnter);
					//world.getSky().set(sky);
					//world.setFogColor(fogR,fogG,fogB,fogA);
				} else 
					world.getGame().getSchedulerTaskExecutor().schedule(this,200,TimeUnit.MILLISECONDS);
			}
		},200,TimeUnit.MILLISECONDS);
		
	}


	/* ********************************************************** *
	 * * 					GETTERS / SETTERS 					* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.scene.Spatial#toString()
	 */
	@Override
	public String toString() {
		return "Map(" + x + "," + z + ")";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.scene.Spatial#isCollidable()
	 */
	
	public boolean isCollidable() {
		return false;
	}

	/**
	 * @return the textures
	 */
	public ArrayList<String> getTextures() {
		return textures;
	}

	/**
	 * @return the alphas
	 */
	public ArrayList<String> getAlphas() {
		return alphas;
	}

	/**
	 * @return the height
	 */
	public String getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(String height) {
		this.height = height;
		launchRebuild();
	}

	/**
	 * @param layer
	 * @param value
	 */
	public void setAlpha(int layer, String value) {
		if (value == null)
			this.alphas.remove(layer);
		else
			this.alphas.set(layer, value);
		launchRebuild();
	}

	/**
	 * @param layer
	 * @param value
	 */
	public void setTexture(int layer, String value) {
		if (value == null)
			this.textures.remove(layer);
		else
			this.textures.set(layer, value);

		launchRebuild();
	}

	public void addLayer() {
		this.textures.add("jme/darkrock.jpg");
		this.alphas.add("basic/none.png");
		launchRebuild();
	}

 
 public String getKey() {
   return LaComponent.map.prefix() + x + ":" + z;
       }
   

}
