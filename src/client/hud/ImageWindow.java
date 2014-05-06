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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import client.HttpResourceLocator;
import client.hud.components.LaBButton;
import client.hud.components.WindowMoveListener;
import client.utils.FileLoader;


import com.jmex.bui.BContainer;
import com.jmex.bui.BImage;
import com.jmex.bui.BLabel;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.background.ImageBackground;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.enumeratedConstants.ImageBackgroundMode;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.BorderLayout;
import shared.variables.Variables;

/**
 * Affiche une image 
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class ImageWindow extends BWindow {
	private static final Logger logger = Logger.getLogger("ImageWindow");
	private Hud hud;
	private BContainer imageContainer;
	private BLabel title;

	/**
	 * @param hud
	 */
	public ImageWindow(Hud hud) {
		super("ImageWindow", 
				BStyleSheetUtil.getStyleSheet(FileLoader.getResourceAsUrl("data/image-style.bss")), 
				new BorderLayout(Hud.SPACE, Hud.SPACE));
		this.hud = hud;

		initialize();
	}

	/**
	 * initialise le contenu
	 */
	private void initialize() {
		title = new BLabel(hud.getLocalText("imageviewer.title"));
		title.addListener(new WindowMoveListener(this));
		title.setStyleClass("label-title");
		this.add(title, BorderLayout.NORTH);
		
		imageContainer = new BContainer();
		this.add(imageContainer,BorderLayout.CENTER);

		LaBButton close = new LaBButton(hud.getLocalText("imageviewer.close"));
		close.setPreferredSize(hud.w/3, -1);
		close.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
			}
		});
		this.add(close, BorderLayout.SOUTH);
		
		BuiSystem.addWindow(this);
		this.center();
	}
	
	/**
	 * 
	 * @param url
	 */
	public void open(final String url,final String title) {
	/*	GameTaskQueueManager.getManager().update(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				loadImage(url,title);
				return null;
			}
		});
                */
            System.out.println("ImageWindow -> open() : GameTaskQueueManager !!");
	}
	
	/**
	 * charge l'image à afficher
	 */
	private void loadImage(String url,String title) {
		logger.fine("ouverture de "+url);
                System.out.println("ImageWindow -> loadImage() : vide !!");
		/*
                try {
			this.title.setText(title==null?hud.getLocalText("imageviewer.title"):title);
			BImage img = new BImage(ResourceLocatorTool.locateResource(HttpResourceLocator.RESOURCE, url));
			ImageBackground background = new ImageBackground(ImageBackgroundMode.CENTER_XY, img);
			imageContainer.setBackground(DEFAULT, background);
			imageContainer.setBackground(HOVER, background);
			imageContainer.setBackground(DISABLED, background);
			this.setPreferredSize(
					background.getMinimumWidth()+Hud.SPACE, 
					background.getMinimumHeight()+Hud.SPACE*2+18*2);
			pack();
			center();
			setVisible(true);
		} catch (MalformedURLException e) {
			logger.warning(e.getMessage());
			                 Variables.getChatSystem().debug("MalformedURL "+url);
		} catch (IOException e) {
			logger.warning(e.getMessage());
			Variables.getChatSystem().debug("InvalidURL "+url);
		} catch (IllegalArgumentException e) {
			logger.warning(e.getMessage());
			Variables.getChatSystem().debug("Null "+url);
		}
                * */
	}
	
	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
