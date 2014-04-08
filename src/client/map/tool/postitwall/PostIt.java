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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import client.HttpResourceLocator;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;
/*
import com.jme.bounding.OrientedBoundingBox;
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.RenderState.StateType;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.TextureManager;
import com.jme.util.resource.ResourceLocatorTool;
*/
/**
 * un Post it sur le mur à post id
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
public class PostIt extends Node {
	private static final long serialVersionUID = 5443298098622440539L;
	private static final Logger logger = Logger.getLogger("PostIt");

	private int id;
	private PostItWallEngine wall;

	private Quad background;

	private URL textureUrl;

	//private TextureState ts;

	/**
	 * @param id
	 * @param postItWallEngine
	 */
	public PostIt(int id, PostItWallEngine engine) {
		super("postIt-" + id);
		this.id = id;
		this.wall = engine;
		//this.setModelBound(new OrientedBoundingBox());
		reload();
	}

	/**
	 * Recharge le contenu graphique du post it
	 */
	public void reload() {
		wall.getTool().getWorld().getGame().getTaskExecutor().execute(
				new Runnable() {
					public void run() {
						reloadInThread();
					}
				});
		logger.fine("mise à jour de : " + this);
	}

	/**
	 * Rechargement graphique de l'outil dans un thread pour ce qui est lent
	 */
	private void reloadInThread() {
		/*if (background == null) {
			background = new Quad("post-it background", wall.getDataAsFloat(
					"post-" + id + "-w", 3), wall.getDataAsFloat("post-" + id
					+ "-h", 3));
			background.setLocalTranslation(0, 0, 0.01f);
			background.setLightCombineMode(LightCombineMode.Off);
			background.setModelBound(new OrientedBoundingBox());
			try {
				GameTaskQueueManager.getManager().update(new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						attachChild(background);
						return null;
					}
				}).get();
			} catch (InterruptedException e) {
				logger.warning("InterruptedException : Je le savais !");
			} catch (ExecutionException e) {
				logger.warning("ExecutionException : Je le savais !");
			}
		} else {
			background.resize(wall.getDataAsFloat("post-" + id + "-w", 3), 
					wall.getDataAsFloat("post-" + id + "-h", 3));
		}

		String file = wall.getData("post-" + id + "-img", "post-it.jpg");
		URL url = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_TEXTURE,file);
		if (url == null)
			url = ResourceLocatorTool.locateResource(HttpResourceLocator.RESOURCE,file);			

		if (textureUrl == null || !textureUrl.equals(url)) {
			textureUrl = url;
			applyTexture();
		}

		if (getRenderState(StateType.Blend) == null) {
			BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
			bs.setEnabled(true);
			bs.setBlendEnabled(true);
			this.setRenderState(bs);
		}
		
		this.getLocalTranslation().setX(wall.getDataAsFloat("post-" + id + "-x", 0));
		this.getLocalTranslation().setY(wall.getDataAsFloat("post-" + id + "-y", 0));
		this.updateRenderState();
		this.updateModelBound();
                * */
	}

	/**
	 * Applique la texture
	 */
	private void applyTexture() {
		logger.entering(PostIt.class.getName(), "applyTexture");
/*
		final Texture texture = TextureManager.loadTexture(textureUrl);
		texture.setMinificationFilter(MinificationFilter.BilinearNoMipMaps);
		texture.setMagnificationFilter(MagnificationFilter.Bilinear);

		GameTaskQueueManager.getManager().update(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				if (ts == null) {
					ts = DisplaySystem.getDisplaySystem().getRenderer()
							.createTextureState();
					background.setRenderState(ts);
				} else
					ts.clearTextures();
				ts.setTexture(texture);
				background.updateRenderState();
				return null;
			}
		});

		logger.exiting(PostIt.class.getName(), "applyTexture");
                * */
	}

	/**
	 * envoie les nouvelle coordonné au serveur
	 */
	public void sendCoordToServer() {
		wall.setData("post-" + id + "-x", Float
				.toString(getLocalTranslation().x));
		wall.setData("post-" + id + "-y", Float
				.toString(getLocalTranslation().y));
		wall.commitChangeOnServer();
	}

}
