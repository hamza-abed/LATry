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
package client.hud;

import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Quad;
import java.util.HashSet;
import java.util.logging.Logger;
/*
import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.renderer.Renderer;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
*/
/**
 * Splascreen pour le chargement 
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class LoadingScreen extends Quad {
	private static final long serialVersionUID = 6536993294240496666L;
	private static final Logger logger = Logger.getLogger("LoadingScreen");
	private HashSet<Object> list = new HashSet<Object>();
	private static final int COUNT = 2;

	/**
	 * 
	 */
	public LoadingScreen(Hud hud) {
	
            System.out.println("LoadingScreen -> constructeur : une classe vide !!");
            
            /*
            super("loading-screen",hud.w,hud.h);

		setRenderQueueMode(Renderer.QUEUE_ORTHO);

		Texture t = TextureManager.loadTexture(hud.getGame().getProps().getProperty("la.loading.screen", "loading.png"), MinificationFilter.Trilinear, MagnificationFilter.Bilinear);
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture(t);
		ts.setEnabled(true);
		
		BlendState bs = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		bs.setBlendEnabled(true);
		
		setRenderState(ts);
		setRenderState(bs);
		
		this.updateRenderState();
		
		setLocalTranslation(hud.w/2,hud.h/2,0);
		
		hide();
                */
	}
	
	/**
	 * Masque le splash screen de chargement.
	 */
	/*private void hide() {
		logger.info("ca masque le loading");
		setCullHint(CullHint.Always);
		updateRenderState();
	}
	
	/**
	 * affiche le splash screen
	 */
	/*private void show() {
		logger.info("ca affiche le loading");
		setCullHint(CullHint.Never);
		updateRenderState();
	}

	
	/* (non-Javadoc)
	 * @see com.jme.scene.Spatial#updateGeometricState(float, boolean)
	 */
/*	private int p = 0;
	@Override
	public synchronized void updateGeometricState(float time, boolean initiator) {
		int size = list.size();
		if (size!=p) {
			logger.fine("Il reste "+size+" object à chargé\n"+list);
		} 
		p=size;
		if (isVisible() && size<COUNT) hide();
		else if (!isVisible() && size>COUNT) show();
		
		super.updateGeometricState(time, initiator);
	}

	/**
	 * indique q'un objet doit etre charger
	 * @param particulEngine
	 */
	/*public synchronized void add(Object  obj) {
		this.list.add(obj);
		logger.fine("attend le chargement de "+obj);
	}

	/**
	 * indique qu'un objet est chargé
	 * @param obj
	 */
	/*public synchronized void remove(Object obj) {
		this.list.remove(obj);
		logger.fine("A charger "+obj+" reste "+list.size());
	}

	/**
	 * @return the visible
	 */
	/*public boolean isVisible() {
		return getCullHint()==CullHint.Never;
	}

	public int getSize() {
		return list.size();
	}

	/**
	 * 
	 */
	/*public void forceHide() {
		list.clear();
		
	}*/
}
