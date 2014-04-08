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
package client.map.tool.postitwall;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.HttpResourceLocator;
import client.input.PickUtils;
import client.map.tool.Tool;
import client.map.tool.ToolEngine;
import client.utils.ModelLoader;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
/*
import com.jme.bounding.OrientedBoundingBox;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.CullState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.RenderState.StateType;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;
*/
/**
 * Moteur pour le mur à post-it
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class PostItWallEngine extends ToolEngine {
	private static final Logger logger = Logger.getLogger("PostItWallEngine");

	private Node graphicNode;

	private Spatial col1, col2;

	private Quad plan;

	private Node postItNode;

	private HashMap<Integer, PostIt> postIts = new HashMap<Integer, PostIt>();

	private PostIt selected;

	/**
	 * @param tool
	 */
	public PostItWallEngine(Tool tool) {
		super(tool);
	}

	/* ********************************************************** *
	 * * 						Graphics 						* *
	 * ********************************************************** */

	/**
	 * initialise/ reconstruit le noeud graphic
	 */
	private void rebuildGraphicNode() {

		final float w = getDataAsInt("wall-width", 18);
		final float h = getDataAsInt("wall-height", 12);

		// construction des elements graphiques rapide
		if (postItNode == null) {
			graphicNode = new Node("PostItWall - Node");
			postItNode = new Node("all-post-it");

			/*plan = new Quad("plan", w, h);
			CullState cs = DisplaySystem.getDisplaySystem().getRenderer()
					.createCullState();
			cs.setEnabled(false);
			plan.setLightCombineMode(LightCombineMode.Off);
			plan.setRenderState(cs);

			graphicNode.attachChild(plan);
			graphicNode.attachChild(postItNode); */
		} /*else 
			plan.resize(w, h); 
		
		applyBackground();

		final String model = getData("model", "columns.jmex");
		if (col1 == null || !col1.getName().equals(model)) {
			tool.getWorld().getGame().getTaskExecutor().execute(new Runnable() {
				@Override
				public void run() {
					rebuildModels(model, w, h);
				}
			});
		}

		graphicNode.setModelBound(new OrientedBoundingBox());
		graphicNode.updateRenderState();
		graphicNode.updateModelBound();

		reloadAllPostIt(getAllKey()); */

	}

	/**
	 * Applique le fond d'ecran du plan du mur à post it.
	 */
	private void applyBackground() {
		String back = getData("background", null);
	/*
         * if (back == null && plan.getRenderState(StateType.Texture)!=null)
			plan.clearRenderState(StateType.Texture);
		else if (back!=null) {
			URL url = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE, back);
			if (url ==null)
				url = ResourceLocatorTool.locateResource(HttpResourceLocator.RESOURCE, back);
			if (plan.getRenderState(StateType.Texture)!=null) 
				((TextureState)plan.getRenderState(StateType.Texture)).setTexture(TextureManager.loadTexture(url));
			else {
				TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
				ts.setTexture(TextureManager.loadTexture(url));
				plan.setRenderState(ts);
			}
		}
		plan.updateRenderState();
                * */
	}

	/**
	 * Chargement des models comme c'est lent c'est dans un autre thread
	 * 
	 * @param model
	 * @param h
	 * @param w
	 */
	private void rebuildModels(String model, float w, float h) {
		/* if (col1 != null)
			try {
				GameTaskQueueManager.getManager().update(new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						col1.removeFromParent();
						col2.removeFromParent();
						return null;
					}
				}).get();
			} catch (Exception e) {
				logger.warning(e.getClass().getName()
						+ " lors de la suppression des columns au noeud graphique");
			}

		col1 = ModelLoader.get().load(model);
		col2 = ModelLoader.get().load(model);
		col1.setName(model);

		float s = getDataAsFloat("model-s", 2);
		float y = getDataAsFloat("model-y", -8);
		float x = getDataAsFloat("model-x", 3);

		// graphicNode.unlock();
		col1.setLocalTranslation(-w / 2 - x, y, 0);
		col2.setLocalTranslation(w / 2 + x, y, 0);
		col1.setLocalScale(s);
		col2.setLocalScale(s);

		GameTaskQueueManager.getManager().update(new Callable<Void>() {
			public Void call() throws Exception {
				graphicNode.attachChild(col1);
				graphicNode.attachChild(col2);
				col1.updateRenderState();
				col2.updateRenderState();
				return null;
			}
		});
*/
	}

	/* ********************************************************** *
	 * * 					TODO commentaire 					* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.tool.ToolEngine#getKeysDescriptions()
	 */
	@Override
	public Collection<String> getKeysDescriptions() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("wall-width");
		list.add("wall-height");
		list.add("background");
		list.add("model");
		list.add("model-s");
		list.add("model-y");
		list.add("model-x");
		list.add("post-(\\d+)-x");
		list.add("post-(\\d+)-y");
		list.add("post-(\\d+)-w");
		list.add("post-(\\d+)-h");
		list.add("post-(\\d+)-img");
		return list;
	}

	/* ********************************************************** *
	 * * 						POST-IT 						* * 
	 * ********************************************************** */

	/**
	 * Recharge l'enssemble des posts-it
	 * 
	 * @param keys
	 */
	private void reloadAllPostIt(Collection<String> keys) {
		Pattern reg = Pattern.compile("^post-(\\d+)-.*$");
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (String key : keys) {
			Matcher m = reg.matcher(key);
			if (m.find())
				ids.add(Integer.parseInt(m.group(1)));
		}

		for (int id : ids)
			reloadPost(id);
	}

	/**
	 * Recharge le postIt
	 * 
	 * @param id
	 */
	private void reloadPost(int id) {
		if (!postIts.containsKey(id)) {
			PostIt post = new PostIt(id, this);
			this.postIts.put(id, post);
			postItNode.attachChild(post);
			/*
			 * post.updateModelBound(); post.updateGeometricState(0, true);//
			 */
		} else
			this.postIts.get(id).reload();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.tool.ToolEngine#getGraphic()
	 */
	//@Override
	public Spatial getGraphic() {
		if (graphicNode == null) {
			rebuildGraphicNode();
		}
		return graphicNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.tool.ToolEngine#dataChanged(java.lang.String)
	 */
	@Override
	public void dataChanged(Collection<String> keys) {
		if (keys.contains("wall-width") || keys.contains("wall-height")
				|| keys.contains("model") || keys.contains("model-s")
				|| keys.contains("model-y") || keys.contains("model-x")
				|| keys.contains("background"))
			rebuildGraphicNode();
		reloadAllPostIt(keys);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.tool.ToolEngine#dataDeleted(java.lang.String)
	 */
	@Override
	public void dataDeleted(Collection<String> keys) {
		if (keys.contains("wall-width") || keys.contains("wall-height")
				|| keys.contains("model") || keys.contains("model-s")
				|| keys.contains("model-y") || keys.contains("model-x")
				|| keys.contains("background"))
			rebuildGraphicNode();
	}

	/* ********************************************************** *
	 * * 			GraphicMouseListener - Implements 			* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.GraphicMouseListener#onButton(int,
	 * boolean, com.jme.math.Ray)
	 */
//	@Override
	public boolean onButton(int button, boolean pressed, Ray ray) {
		Vector2f mouseCoord = PickUtils.getQuadCoord(ray, plan);
		if (mouseCoord == null)
			return true;
/*
		if (button == 0 && pressed) {
			TrianglePickResults pickResults = new TrianglePickResults();
			pickResults.setCheckDistance(true);

			for (PostIt postIt : postIts.values())
				postIt.findPick(ray, pickResults);

			if (pickResults.getNumber() == 0)
				selected = null;
			else
				selected = (PostIt) pickResults.getPickData(0).getTargetMesh()
						.getParent();
		} else if (button == 0 && !pressed && selected != null) {
			selected.sendCoordToServer();
			selected = null;
		}// */

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * client.interfaces.graphic.GraphicMouseListener#onMove(com.jme.math.Ray)
	 */
	//@Override
	public boolean onMove(Ray ray) {
		if (selected == null)
			return true;

		Vector2f mouseCoord = PickUtils.getQuadCoord(ray, plan);
		if (mouseCoord == null)
			return true;

		selected.getLocalTranslation().setX(
				mouseCoord.x * getDataAsFloat("wall-width", 18));
		selected.getLocalTranslation().setY(
				mouseCoord.y * getDataAsFloat("wall-height", 12));

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.GraphicMouseListener#onWheel(int,
	 * com.jme.math.Ray)
	 */
	//@Override
	public boolean onWheel(int wheelDelta, Ray ray) {
		return false;
	}

}
