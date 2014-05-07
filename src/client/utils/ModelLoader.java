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
package client.utils;

import com.jme3.export.xml.XMLImporter;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;
import shared.variables.Variables;
/*
import com.jme.animation.SkinNode;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import com.jme.scene.state.GLSLShaderObjectsState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.MaterialState.MaterialFace;
import com.jme.scene.state.RenderState.StateType;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.export.xml.XMLImporter;
import com.jme.util.resource.ResourceLocatorTool;
*/
/**
 * permet de charger les models
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */

public class ModelLoader {
	private static final Logger logger = Logger.getLogger("ModelLoader");
	private static ModelLoader instance;
/*
	private HashMap<ShaderInfo, GLSLShaderObjectsState> shaders = 
		new HashMap<ShaderInfo, GLSLShaderObjectsState>(); */
	private boolean bumb;

	public static ModelLoader get() {
		if (instance == null) {
			instance = new ModelLoader();
		}
		return instance;
	}

	/**
	 * 
	 */
	private ModelLoader() {
	}

	/**
	 * charge le model
	 * 
	 * @param model
	 * @return
	 */
	/*
         * Modifié Hamza ABED
         * il suffit de recharger le modéle à partir de l'assets MAnager
         */
       
        public Spatial load(String model) {
		//try {
	Spatial s = Variables.getLaGame().getAssetManager().loadModel(model);
                /*(Spatial) XMLImporter.getInstance().load(
					ResourceLocatorTool.locateResource(
							ResourceLocatorTool.TYPE_MODEL, model)); */
			/*if (!(s instanceof SkinNode))
				if (bumb) reloadShader(s);
				else removeBumb(s);
			
			normalizeMaterials(s); */
			//s.updateRenderState();
			return s;
	/*	} catch (IOException e) {
			logger.warning("IOException : Je le savais !");
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			logger.severe(e.getClass().getName()+" lors du chargeemnt de "+model);
		}
		return new Box(1, 1, 1); */
                        
                       
	}

	/**
	 * @param s
	 */
        
        /*
	private void normalizeMaterials(Spatial s) {
		if (s instanceof Node) {
			for (Spatial child : ((Node)s).getChildren())
				normalizeMaterials(child);
		} else if(s.getRenderState(StateType.Material) ==null && s.getRenderState(StateType.Blend)==null ) {
			MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer().createMaterialState();
			ms.setAmbient(new ColorRGBA(.4f,.4f,.4f,0));
			ms.setDiffuse(new ColorRGBA(.8f,.8f,.8f,0));
			ms.setMaterialFace(MaterialFace.FrontAndBack);
			s.setRenderState(ms);
			s.updateRenderState();
		}
	}
        * */

	/**
	 * enleve les effet de bumb mapping
	 * @param s
	 */
        /*
	private void removeBumb(Spatial s) {
		GLSLShaderObjectsState shader = (GLSLShaderObjectsState) s.getRenderState(StateType.GLSLShaderObjects);
		if (shader !=null) {
			shader.setEnabled(false);
			s.clearRenderState(StateType.GLSLShaderObjects);
			TextureState ts = (TextureState) s.getRenderState(StateType.Texture);
			if (ts != null) {
				int i= ts.getNumberOfSetTextures()-1;
				while (i>0 && ts.removeTexture(i))
					i--;
			}
		}
		if (s instanceof Node) {
			for (Spatial child : ((Node)s).getChildren())
				reloadShader(child);
		}
	}

	/** 
	 * applique les shader à un objet charger
	 * @param s
	 */
       /*
	private void reloadShader(Spatial s) {
		GLSLShaderObjectsState shader = (GLSLShaderObjectsState) s.getRenderState(StateType.GLSLShaderObjects);
		if (shader !=null) try {
			ShaderInfo si = (ShaderInfo) s.getUserData("fragement");
			if (si!=null) 
				if (shaders.containsKey(si))
					s.setRenderState(shaders.get(si));
				else {
					shader.load(
							ModelLoader.class.getClassLoader()
							.getResource(si.vert), 
							ModelLoader.class.getClassLoader()
							.getResource(si.frag));
					shaders.put(si, shader);
				}
		} catch (JmeException e) {
			logger.warning(e.getMessage() + " lors du chargement des shaders");	
		}

		if (s instanceof Node) {
			for (Spatial child : ((Node)s).getChildren())
				reloadShader(child);
		}
	}
*/
	/**
	 * active / desactive le bumb
	 * @param parseBoolean
	 */
        /*
	public void enableBumb(boolean enable) {
		this.bumb = enable;
	} */

}
