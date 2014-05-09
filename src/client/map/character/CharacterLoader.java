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
package client.map.character;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import shared.enums.CharacterModel;
import client.map.character.AbstractCharacter.CharacterAnimation;
import client.utils.FileLoader;
import client.utils.ModelLoader;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.plugins.ogre.MaterialLoader;
import com.model.md5.MD5Node;
import com.model.md5.controller.MD5Controller;
import com.model.md5.importer.MD5Importer;
import static shared.enums.CharacterModel.synbad;
import shared.variables.Variables;

/*
import com.jme.animation.SkinNode;
import com.jme.bounding.BoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Controller;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.state.CullState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.MaterialState.MaterialFace;
import com.jme.scene.state.RenderState.StateType;
import com.jme.system.DisplaySystem;
import com.jmex.model.ModelFormatException;
import com.jmex.model.ogrexml.MaterialLoader;
import com.jmex.model.ogrexml.OgreLoader;
import com.model.md5.MD5Node;
import com.model.md5.controller.MD5Controller;
import com.model.md5.importer.MD5Importer;
*/
/**
 * Permet de chargé un model graphique de personnage
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class CharacterLoader {
	private static final Logger logger = Logger.getLogger("CharacterLoader");

	private static final String MARINE_FOLDER = "data/character/marine-md5/";

	private static final String MAGE_FOLDER = "data/character/mage-md5/";

	
	private static final String BODY_NAME = "body";

	private static String HEAD_NAME = "head";

	private static boolean high = false;

	static final String WALK_ANIM_NAME = "walk";

	static final String IDLE_ANIM_NAME = "idle";

	static final String RUN_ANIM_NAME = "run";

	/*
	 * CHARGEMENT DU MESH GRAPHIQUE
	 */

	public static Node loadNode(AbstractCharacter character) throws IOException {
		Node node;
		if (isMd5Model(character))
                {
                    System.out.println("model is Md5Model mais on l a remplacé par synbad !!");
                  //  node = loadMd5(character);
                    node = loadOgre(character);
                
                }
		else if (isJmexModel(character) || isDotModel(character)) 
                {System.out.println("model is Jmex Model !!");
                    node = loadJmex(character);
                }
		else 
                {System.out.println("model is Ogre !!");
                    node = loadOgre(character);
                }
		
                if(node==null) System.out.println("CharacterLoader->loadNode() : node =null !!");
                node.setLocalScale(getModelScale(character));
                

		/*if (character.isPlayer())
			for (Spatial s :node.getChildren())
				System.out.println(s.getName());//*/

		if (getModelOrient(character)!=null)
			for (Spatial s :node.getChildren())
				s.getLocalRotation().set(getModelOrient(character));

		node.setLocalTranslation(character.x, character.world.getHeightAt(character.x, character.z), character.z);
		return node;
	}

	/**
	 * charge mais au format MD5
	 * @param character 
	 * @return
	 * @throws IOException 
	 */

        private static Node loadMd5(AbstractCharacter character) throws IOException {
	           System.out.println("CharacterLoader -> loadMd5() : vide !!");	
         /*  MD5Importer importer = new MD5Importer();
           MD5Node body;

		switch (character.modelType) {
		case marine:
                    
			importer.load(
					getMd5MeshUrl(character), BODY_NAME,
					getMd5Anim(character,CharacterAnimation.walk), WALK_ANIM_NAME,
					1); //Controller.RT_WRAP=1
			body = (MD5Node) importer.getMD5Node();
			importer.cleanup();

			importer.load(
					FileLoader.getResourceAsUrl(MARINE_FOLDER+"sarge.md5mesh"), HEAD_NAME,
					FileLoader.getResourceAsUrl(MARINE_FOLDER+"sarge.md5anim"), WALK_ANIM_NAME,
					1);
			body.attachChild(importer.getMD5Node(), "Shoulders");
			importer.cleanup();

			importer.loadAnim(getMd5Anim(character,CharacterAnimation.idle), IDLE_ANIM_NAME);
			//body.addAnimation(importer.getAnimation());
			importer.cleanup();
			return body;
		        default:
			importer.load(
					getMd5MeshUrl(character), BODY_NAME, 
					getMd5Anim(character,CharacterAnimation.idle), IDLE_ANIM_NAME,
					1);
			body = (MD5Node) importer.getMD5Node();
			importer.cleanup();

			importer.loadAnim(getMd5Anim(character,CharacterAnimation.run), RUN_ANIM_NAME);
			((MD5Controller)body.getController(0)).addAnimation(importer.getAnimation());
			importer.cleanup();

			importer.loadAnim(getMd5Anim(character,CharacterAnimation.walk), WALK_ANIM_NAME);
			((MD5Controller)body.getController(0)).addAnimation(importer.getAnimation());
			importer.cleanup();

			return body;
		}
               */
            return null;
	}

	/**
	 * charge un personnage dans un model Ogre
	 * @param character 
	 * @return
	 * @throws IOException 
	 * @throws ModelFormatException 
	 */

	private static Node loadOgre(AbstractCharacter character) throws IOException {
	System.out.println("CharacterLoader -> loadOgre() : modifié !!");	
          /* OgreLoader ol = new OgreLoader();
		MaterialLoader ml = new MaterialLoader();

		// tentative de resolution des bonhomme blanc
		while (ml.getMaterials() == null || ml.getMaterials().isEmpty())
			ml.load(getOgreMaterialUrl(character));

		ol.setMaterials(ml.getMaterials());
                */
       Node playerModel = (Node) Variables.getLaGame().getAssetManager().loadModel(
		getOgreMeshUrl(character));
             
            return playerModel;
	}


	private static Node loadJmex(AbstractCharacter character) {
            System.out.println("CharacterLoader -> loadJmex() : vide !!");
            
		Spatial s = ModelLoader.get().load(character.modelType.name()+".jmex");
		if (s instanceof Node ) {
			s.setModelBound(new BoundingBox());
			
			return (Node)s;
		}
		Node n = new Node(character.modelType.name());
		//s.setLocalTranslation(0, getModelTranslation(character), 0);
		n.attachChild(s);
		n.setModelBound(new BoundingBox());
		return n;
                
           // return null;
	}
	
	/**
	 * @param character 
	 * @return
	 */

        static boolean isMd5Model(AbstractCharacter character) {
		return character.modelType == CharacterModel.marine || character.modelType==CharacterModel.mage;
		//character.modelType == CharacterModel.skelet;
	}
	
	static boolean isJmexModel(AbstractCharacter character) {
		return character.modelType == CharacterModel.laurelGreen || character.modelType == CharacterModel.laurelCyan 
		|| character.modelType == CharacterModel.laurelPink || character.modelType == CharacterModel.hardyBlue
		|| character.modelType == CharacterModel.hardyRed || character.modelType == CharacterModel.hardyYellow;
	}
	
	static boolean isDotModel(AbstractCharacter character) {
		return character.modelType == CharacterModel.dotDwarf;
	}

	/* ********************************************************** *
	 * * 				URL DES MODELS OGRES 					* *
	 * ********************************************************** */

	/**
	 * renvoi l'adresse du material correspondant
	 * @param character 
	 * 
	 * @return
	 */
	protected static URL getOgreMaterialUrl(AbstractCharacter character) {
		switch (character.modelType) {
		case arachnie: return FileLoader.getResourceAsUrl("data/character/arachnie-ogre/GoodScene.material");
		case synbad: return FileLoader.getResourceAsUrl("data/character/"+(high?"high":"low")+"-sinbad/Sinbad.material");
		case women: return FileLoader.getResourceAsUrl("data/character/"+(high?"high":"low")+"-women/Scene.material");
		case men: return FileLoader.getResourceAsUrl("data/character/"+(high?"high":"low")+"-men/man.material");
		case robot: return FileLoader.getResourceAsUrl("data/character/robot-ogre/robot.material");
		case tank: return FileLoader.getResourceAsUrl("data/character/sc2-tank-ogre/Scene.material");
		case hellion: return FileLoader.getResourceAsUrl("data/character/sc2-hellion-ogre/Scene.material");
		case skelet: return FileLoader.getResourceAsUrl("data/character/skeleton-ogre/Scene.material");
		
		//case mage: return FileLoader.getResourceAsUrl("data/character/mage-md5/mage.material");  
                    /*
                     * ICI on remplace le "mage" par "synbad"
                     */
                case mage: return FileLoader.getResourceAsUrl("data/character/"+(high?"high":"low")+"-sinbad/Sinbad.material");
		
		default: return FileLoader.getResourceAsUrl("data/character/spirit-ogre/Spirit.material");
		
		}
	}

	/**
	 * renvoi l'adresse du material correspondant
	 * 
	 * @return
	 */
	protected static String getOgreMeshUrl(AbstractCharacter character) {
		switch (character.modelType) {
		case arachnie: return"Models/character/arachnie-ogre/enemy_01.mesh.xml";
		case synbad: return"Models/character/"+(high?"high":"low")+"-sinbad/Sinbad.mesh.xml";
		case women: return"Models/character/"+(high?"high":"low")+"-women/LA3_girl.mesh.xml";
		case men: return"Models/character/"+(high?"high":"low")+"-men/man.mesh.xml";
		case robot: return"Models/character/robot-ogre/robot.mesh.xml";
		case tank: return "Models/character/sc2-tank-ogre/tank.mesh.xml";
		case hellion: return "Models/character/sc2-hellion-ogre/hellion.mesh.xml";
		case skelet: return "Models/character/skeleton-ogre/Cube.004.mesh.xml";
		//case mage: return "Models/character/mage-md5/Cube.001.mesh.xml";
                    /*
                     * ICI on remplace "mage" par "synbad"
                     */
                case mage: return"Models/character/"+(high?"high":"low")+"-sinbad/Sinbad.mesh.xml";
		default: return "Models/character/spirit-ogre/Spirit.mesh.xml";
		}
	}


	/* ********************************************************** *
	 * * 				URL DES MODELS MD5	 					* *
	 * ********************************************************** */

	/**
	 * renvoie le fichier de mesh d'un model MD5
	 * @param character 
	 * @return
	 */
	protected static URL getMd5MeshUrl(AbstractCharacter character) {
		switch (character.modelType) {
		case marine: return FileLoader.getResourceAsUrl(MARINE_FOLDER+"marine.md5mesh");
		case mage: return FileLoader.getResourceAsUrl(MAGE_FOLDER+"mage.md5mesh");
		}
		return null;
	}

	/**
	 * Renvoie le fichier d'annimation MD5 en fonction du type d'animation
	 * @param character 
	 * @param anim
	 * @return
	 */
	protected static URL getMd5Anim(AbstractCharacter character, CharacterAnimation anim) {
		if (character.modelType == CharacterModel.marine) 
			switch (anim) {
			case idle: return FileLoader.getResourceAsUrl(MARINE_FOLDER+"marine_stand.md5anim");
			case walk: return FileLoader.getResourceAsUrl(MARINE_FOLDER+"marine.md5anim");
			case run: return FileLoader.getResourceAsUrl(MARINE_FOLDER+"marine.md5anim");
			}

		if (character.modelType == CharacterModel.mage) 
			switch (anim) {
			case idle: return FileLoader.getResourceAsUrl(MAGE_FOLDER+"idle.md5anim");
			case walk: return FileLoader.getResourceAsUrl(MAGE_FOLDER+"walk.md5anim");
			case run: return FileLoader.getResourceAsUrl(MAGE_FOLDER+"walk.md5anim");
			}
		
		return null;
	}


	/* ********************************************************** *
	 * *				GESTION GEOMETRIQUE						* *
	 * ********************************************************** */

	/**
	 * renvoie l'echelle de d'un model par defaut 
	 * @param character 
	 * 
	 * @return
	 */
	protected static float getModelScale(AbstractCharacter character) {
		switch (character.modelType) {
		case arachnie: return 1/1.3f;
		case hidden: return 0f;
		case marine: return 1.8f/38.89f;
		case men: return 1.8f/12.70f;
		case robot: return 2.5f/48.83f;
		//case sintel: return 1.65f/0.76f;
		case spirit: return 0.5f;
		case synbad: return 2 / 4.47f;
		case tank: return 1.5f/0.8054695f;
		case unset: return 0.5f;
		case women:	return 1.7f / 21.70007f;
		case skelet: return 1.6f / 0.9554205f;
		
		//case mage: return 1.75f/9.56f;
                case mage: return 2 / 4.47f;
		case hardyBlue:
		case hardyRed:
		case hardyYellow: return 2f/2.0874596f;
		case laurelCyan:
		case laurelGreen:
		case laurelPink: return 1.8f/1.8437233f;
		//case dotDwarf: return 1.4f/10.912025f;
		}
		return 1;
	}

	protected static float getStdHeight(AbstractCharacter character) {
		switch (character.modelType) {
		case arachnie: return 1.1f*2;
		case hidden: return 0;
		case marine: return 1.8f*2;
		case men: return 1.8f*2;
		case robot: return 2.5f*2;
		case spirit: return .5f*2;
		case synbad: return 2*2;
		case tank: return 1.5f*2;
		case unset: return .5f*2;
		case women : return 1.7f*2;
		case skelet: return 1.6f*2;
		
		//case mage: return 1.75f*2;
                case mage: return 2*2;
		case hardyBlue:
		case hardyRed:
		case hardyYellow: return 4; // 2*2
		
		case laurelCyan:
		case laurelGreen:
		case laurelPink: return 1.8f*2;

		default:
		}
		return 2f;
	}
	
	/**
	 * renvoie l'orientation du model
	 * @param character
	 * @return
	 */
        
	protected static Quaternion getModelOrient(AbstractCharacter character) {
		switch (character.modelType) {
		case robot: return new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI,Vector3f.UNIT_Y); 
		//case mage: return new Quaternion().fromAngleNormalAxis(-FastMath.HALF_PI,Vector3f.UNIT_Z); 
		}
		return null;
	}

	/**
	 * renvoie la position du model
	 * @param character
	 * @return
	 */
	protected static float getModelTranslation(AbstractCharacter character) {
		switch (character.modelType) {
		case dotDwarf: return 8f;
		}
		return 0;
	}

	/* ********************************************************** *
	 * * 				Applique les couleur 					* *
	 * ********************************************************** */

	/**
	 * applique les materiaux
	 */
      
	public static void applyMaterials(AbstractCharacter character, Node graphic) {
	
            System.out.println("CharacterLoader -> applyMaterials() : vide !!");
            /*	
            if (graphic != null)
			try {
				for (MaterialState m : getSkinMaterials(character,graphic)) {
					m.setDiffuse(character.skin);
					m.setAmbient(new ColorRGBA(
							character.skin.r * character.skinAmbient, 
							character.skin.g * character.skinAmbient, 
							character.skin.b * character.skinAmbient, 1));
					m.setMaterialFace(MaterialFace.FrontAndBack);
				}
				for (MaterialState m : getHairMaterials(character,graphic)) {
					m.setDiffuse(character.hair);
					m.setAmbient(new ColorRGBA(
							character.hair.r * character.hairAmbient, 
							character.hair.g * character.hairAmbient, 
							character.hair.b * character.hairAmbient, 1));
					m.setMaterialFace(MaterialFace.FrontAndBack);
				}
				for (MaterialState m : getTopClothsMaterials(character,graphic)) {
					m.setDiffuse(character.topCloth);
					m.setAmbient(new ColorRGBA(
							character.topCloth.r * character.topClothAmbient,
							character.topCloth.g * character.topClothAmbient, 
							character.topCloth.b * character.topClothAmbient, 1));
					m.setMaterialFace(MaterialFace.FrontAndBack);
				}
				for (MaterialState m : getBottomClothsMaterials(character,graphic)) {
					m.setDiffuse(character.bottomCloth);
					m.getAmbient().set(
							character.bottomCloth.r * character.bottomClothAmbient,
							character.bottomCloth.g * character.bottomClothAmbient, 
							character.bottomCloth.b * character.bottomClothAmbient, 1);
					m.setMaterialFace(MaterialFace.FrontAndBack);
				}
				for (MaterialState m : getShoesMaterials(character,graphic)) {
					m.setDiffuse(character.shoes);
					m.setAmbient(new ColorRGBA(
							character.shoes.r * character.shoesAmbient, 
							character.shoes.g * character.shoesAmbient, 
							character.shoes.b * character.shoesAmbient, 1));
					m.setMaterialFace(MaterialFace.FrontAndBack);
				}
				int cut = 0;
				for (ArrayList<Spatial> col : getHairSpatials(character,graphic)) { 
					for (Spatial s : col) {
						s.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);

						if (cut == character.hairCut) {
							s.setCullHint(CullHint.Inherit);
						} else {
							s.setCullHint(CullHint.Always);
						}

						/*BlendState bs =	DisplaySystem.getDisplaySystem().getRenderer().createBlendState(); 
					bs.setBlendEnabled(true);
					bs.setTestEnabled(true);
					bs.setTestFunction(TestFunction.GreaterThan);
					s.setRenderState(bs);//*/

				/*		CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
						cs.setCullFace(CullState.Face.None);
						s.setRenderState(cs);

						/*ZBufferState zs = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
					zs.setWritable(false);
					s.setRenderState(zs);//*/

					/*	s.updateRenderState();
					}
					cut++;
				}
			} catch (NullPointerException e) {
				logger.warning("NullPointerException : Le model n'est pas encore chargé");
			}
                        */
	}


	/* ********************************************************** *
	 * * 				SOUS mesh / Material des OGRES 			* *
	 * ********************************************************** */
	/**
	 * renvoie les materiaux des vetements
	 * 
	 * @param newModel
	 * @return
	 */
        /*
	private static Collection<MaterialState> getTopClothsMaterials(AbstractCharacter character,Node model) {
		ArrayList<MaterialState> out = new ArrayList<MaterialState>();
		switch (character.modelType) {
		case women:	
			out.add((MaterialState) model.getChild("LA3_girlMesh003").getRenderState(StateType.Material)); 
			out.add((MaterialState) model.getChild("LA3_girlMesh004").getRenderState(StateType.Material)); 
			break;
		case men: out.add((MaterialState) model.getChild("manMesh001").getRenderState(StateType.Material)); 
		break;

		}
		return out;
	}

	/**
	 * renvoie les materiaux des vetements
	 * 
	 * @param newModel
	 * @return
	 */
        /*
	private static Collection<MaterialState> getBottomClothsMaterials(AbstractCharacter character,Node model) {
		ArrayList<MaterialState> out = new ArrayList<MaterialState>();
		switch (character.modelType) {
		case women:	
			out.add((MaterialState) model.getChild("LA3_girlMesh002").getRenderState(StateType.Material)); 
			out.add((MaterialState) model.getChild("LA3_girlMesh006").getRenderState(StateType.Material)); 
			break;
		case men: out.add((MaterialState) model.getChild("manMesh003").getRenderState(StateType.Material)); break; 

		/*out.add(new ArrayList<Spatial>());
		out.get(1).add(model.getChild("LA3_girlMesh004"));//*/
		/*out.add(new ArrayList<Spatial>());
		out.get(2).add(model.getChild("LA3_girlMesh006"));//*/
/*
		}
		return out;
	}//*/

	/**
	 * Les noeuds des cheveux
	 * 
	 * @param newModel
	 * @return
	 */
	private static ArrayList<ArrayList<Spatial>> getHairSpatials(AbstractCharacter character,Node model) {
		ArrayList<ArrayList<Spatial>> out = new ArrayList<ArrayList<Spatial>>();
		switch (character.modelType) {
		case women:	
			out.add(new ArrayList<Spatial>());
			out.get(0).add(model.getChild("LA3_girlMesh000")); // ok
			out.add(new ArrayList<Spatial>());
			out.get(1).add(model.getChild("LA3_girlMesh007"));
			out.get(1).add(model.getChild("LA3_girlMesh008")); // ok
			out.add(new ArrayList<Spatial>());
			out.get(2).add(model.getChild("LA3_girlMesh009")); // ok
			out.add(new ArrayList<Spatial>());
			out.get(3).add(model.getChild("LA3_girlMesh010")); // ok
			break;//*/
		default : 
			out.add(new ArrayList<Spatial>());
			break;
		}
		return out;
	}


	/**
	 * renvoie les materiaux de la peau
	 * @param character 
	 * 
	 * @param model
	 * @return
	 */
        /*
	private static Collection<MaterialState> getSkinMaterials(AbstractCharacter character, Node model) {
		ArrayList<MaterialState> out = new ArrayList<MaterialState>();
		switch (character.modelType) {
		case women:	out.add((MaterialState) model.getChild("LA3_girlMesh001").getRenderState(StateType.Material)); break;
		case men: out.add((MaterialState) model.getChild("manMesh000").getRenderState(StateType.Material)); break;
		}
		return out;
	}

	/**
	 * renvoie les materiaux des cheveux
	 * vérifier
	 * @param character 
	 * @param newModel
	 * @return
	 */
        /*
	private static ArrayList<MaterialState> getHairMaterials(AbstractCharacter character, Node model) {
		ArrayList<MaterialState> out = new ArrayList<MaterialState>();
		switch (character.modelType) {
		case women : 
			out.add((MaterialState) model.getChild("LA3_girlMesh000").getRenderState(StateType.Material)); // ok
			out.add((MaterialState) model.getChild("LA3_girlMesh007").getRenderState(StateType.Material));
			out.add((MaterialState) model.getChild("LA3_girlMesh008").getRenderState(StateType.Material)); // ok
			out.add((MaterialState) model.getChild("LA3_girlMesh009").getRenderState(StateType.Material)); // ok
			out.add((MaterialState) model.getChild("LA3_girlMesh010").getRenderState(StateType.Material)); // ok
			break;
		}
		return out;
	}

	/**
	 * Renvoie el nombre de coupe de cheveuyx disponible
	 * @param player
	 * @return
	 */
       
	public static int getHairCutCount(Player player) {
		switch (player.modelType) {
		case women: return 4;
		default: return 0;
		}

	}


	/**
	 * renvoie les materiaux des cheveux
	 * 
	 * @param character personnage contenant les information
	 * @param model object graphique qui va se prendre les info dans le fions
	 * @return
	 */
        /*
	private static Collection<MaterialState> getShoesMaterials(AbstractCharacter character,Node model) {
		ArrayList<MaterialState> out = new ArrayList<MaterialState>();
		switch (character.modelType) {
		case women:	out.add((MaterialState) model.getChild("LA3_girlMesh005").getRenderState(StateType.Material)); break;
		case men: out.add((MaterialState) model.getChild("manMesh002").getRenderState(StateType.Material));	break;
		}
		return out;
	}

	/**
	 * definit le niveau de détail des personnage 
	 * @param high
	 */
	public static void setHighDetail(boolean high) {
		CharacterLoader.high  = high;
	}


}
