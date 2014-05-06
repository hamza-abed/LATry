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

import java.net.URL;
import java.util.concurrent.Callable;

import org.lwjgl.opengl.Display;

import shared.constants.LaConstants;
import client.hud.components.LaBButton;
import client.hud.components.WindowMoveListener;
import client.utils.FileLoader;

//import com.jme.system.DisplaySystem;
//import com.jme.util.GameTaskQueueManager;
import com.jmex.bui.BLabel;
import com.jmex.bui.BScrollBar;
import com.jmex.bui.BTextArea;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.enumeratedConstants.Orientation;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.icon.IconUtil;
import com.jmex.bui.layout.BorderLayout;

/**
 * Attention la construction d'un popup dois se faire dans la boucle d'update !
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class PopupWindow extends BWindow implements Callable<Void>{
	private static final int DEFAULT_H = 150;
	private static final int DEFAULT_W = 250;

	public enum PopupIcon {
		none, disconnect, warning, nyi, waiting, message
	}

	private int h;
	private int w;
	private boolean right;

	/**
	 * Attention ne construire le pupop que dans la boucle d'update !
	 * 
	 * @param message
	 *            le message à afficher obligatoire
	 * @param close
	 *            le text sur le bouton fermer (obligatoir)
	 * @param icon
	 *            l'icone du message
	 */
	public PopupWindow(String message, String close) {
		this(null, message, close, false, PopupIcon.none, DEFAULT_W, DEFAULT_H,
				null);
	}

	/**
	 * Attention ne construire le pupop que dans la boucle d'update !
	 * 
	 * @param message
	 *            le message à afficher obligatoire
	 * @param close
	 *            le text sur le bouton fermer (obligatoir)
	 * @param icon
	 *            l'icone du message
	 */
	public PopupWindow(String message, String close, PopupIcon icon) {
		this(null, message, close, false, icon, DEFAULT_W, DEFAULT_H, null);
	}

	/**
	 * Attention ne construire le pupop que dans la boucle d'update !
	 * 
	 * @param message
	 *            le message à afficher obligatoire
	 * @param close
	 *            le text sur le bouton fermer (obligatoir)
	 * @param scroll
	 *            indique si il y a une scrollbar
	 * @param icon
	 *            l'icone du message
	 */
	public PopupWindow(String message, String close, boolean scroll,
			PopupIcon icon) {
		this(null, message, close, scroll, icon, DEFAULT_W, DEFAULT_H, null);
	}

	/**
	 * Attention ne construire le pupop que dans la boucle d'update !
	 * 
	 * @param title
	 *            le titre du popup si aucun
	 * @param message
	 *            le message à afficher obligatoire
	 * @param close
	 *            le text sur le bouton fermer (obligatoir)
	 * @param scroll
	 *            indique si il y a une scrollbar
	 * @param icon
	 *            l'icone du message
	 */
	public PopupWindow(String title, String message, String close,
			boolean scroll, PopupIcon icon) {
		this(title, message, close, scroll, icon, DEFAULT_W, DEFAULT_H, null);
	}

	/**
	 * Attention ne construire le pupop que dans la boucle d'update !
	 * 
	 * @param title
	 *            le titre du popup null si aucun
	 * @param message
	 *            le message à afficher obligatoire
	 * @param close
	 *            le text sur le bouton fermer (obligatoir)
	 * @param scroll
	 *            indique si il y a une scrollbar
	 * @param icon
	 *            l'icone du message
	 * @param width
	 *            largeur du popup
	 * @param height
	 *            hauteur du popup
	 * @param closeAction
	 *            listener appélé à la fermeture du popup
	 */
	public PopupWindow(String title, String message, String close,
			boolean scroll, PopupIcon icon, int width, int height,
			ActionListener closeAction) {
		super("chat window", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/popup-style.bss")), new BorderLayout(
						Hud.SPACE, Hud.SPACE));

		if (title != null)
			initializeTitle(title);
		if (icon != null && icon != PopupIcon.none)
			initializeIcon(icon);
		initializeMessage(message, scroll);
		initializeCloseButton(close, closeAction);

		w = width;
		h = height;
		
                System.out.println("PopupWindow -> constructeur : GameTaskManager !!" );
		//GameTaskQueueManager.getManager().update(this);

	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
		if (isVisible()) {
			BuiSystem.addWindow(this);
			setLayer(3);
			this.setPreferredSize(w, h);
			this.pack();
			this.center();
                        System.out.println("PopupWindow -> call() : manquante !!");
		//	if (right)setLocation(DisplaySystem.getDisplaySystem().getWidth()-getWidth()-Hud.SPACE, getY());
				
		} else 
			BuiSystem.removeWindow(this);
		return null;
	}


	/**
	 * initialise le titre de la fenetre
	 * 
	 * @param title
	 */
	private void initializeTitle(String title) {
		BLabel label = new BLabel(title);
		label.addListener(new WindowMoveListener(this));
		// label.setStyleClass("label-title");
		this.add(label, BorderLayout.NORTH);
	}

	/**
	 * 
	 * @param icon
	 */
	private void initializeIcon(PopupIcon icon) {
		BLabel label = new BLabel(IconUtil.getIcon(getIconUrl(icon)));
		this.add(label, BorderLayout.WEST);
	}

	/**
	 * renvoie l'url de l'icone
	 * 
	 * @param icon
	 * @return
	 */
	private URL getIconUrl(PopupIcon icon) {
		switch (icon) {
		case disconnect: return FileLoader.getResourceAsUrl(LaConstants.DIR_LGPL_ICON + "network-64.png");
		case nyi: return FileLoader.getResourceAsUrl(LaConstants.DIR_LGPL_ICON + "desktop-64.png");
		case waiting: return FileLoader.getResourceAsUrl(LaConstants.DIR_CC_BY_ICON + "hourglass-64.png");
		case message: return FileLoader.getResourceAsUrl(LaConstants.DIR_LGPL_ICON + "letters-64.png");
		case warning:
		default:
			return FileLoader.getResourceAsUrl(LaConstants.DIR_LGPL_ICON
					+ "warning-64.png");
		}
	}

	/**
	 * Intialise le contenu text
	 * 
	 * @param message
	 * @param scroll
	 */
	private void initializeMessage(String message, boolean scroll) {
		BTextArea text = new BTextArea(message);
		this.add(text, BorderLayout.CENTER);

		if (scroll)
			this.add(new BScrollBar(Orientation.VERTICAL,text.getScrollModel()), BorderLayout.EAST);

	}

	/**
	 * bouton de fermeture du popup
	 * 
	 * @param close
	 * @param closeAction
	 */
	private void initializeCloseButton(String close, ActionListener closeAction) {
		if (close != null) {
			LaBButton button = new LaBButton(close);
			button.addListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					setVisible(false);
				}
			});
			if (closeAction != null)
				button.addListener(closeAction);
			this.add(button, BorderLayout.SOUTH);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jmex.bui.BContainer#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
                System.out.println("PopupWindow -> setVisible() : GameTaskManager !!");
		//GameTaskQueueManager.getManager().update(this);
	}
}
