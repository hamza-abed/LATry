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
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import shared.constants.ScriptConstants;
import client.HttpResourceLocator;
import client.hud.components.LaBButton;
import client.map.data.SlideShow;
import client.utils.FileLoader;

//import com.jme.scene.state.TextureState;
//import com.jme.scene.state.RenderState.StateType;
//import com.jme.util.GameTaskQueueManager;
//import com.jme.util.TextureManager;
//import com.jme.util.resource.ResourceLocatorTool;
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
import com.jmex.bui.layout.TableLayout;
import shared.variables.Variables;

/**
 * Hud Daffichage des slideShow 
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class SlideShowHud extends BWindow implements Callable<Void>, Runnable {
	private static final Logger logger = Logger.getLogger("SlideShowHud");

	private Hud hud;

	SlideShow slideShow;

	private int current;

	private BContainer slideContainer;

	private BImage imgCurrent;

	private BLabel title;

	private BContainer bottom;

	/**
	 * 
	 */
	public SlideShowHud(Hud hud) {
		super("SlideShowWindow", 
				BStyleSheetUtil.getStyleSheet(FileLoader.getResourceAsUrl("data/slideshow-style.bss")), 
				new BorderLayout(Hud.SPACE, Hud.SPACE));
		this.hud = hud;

		initialize();
	}

	/**
	 * initialise la fenetre de base
	 */
	private void initialize() {
		title = new BLabel(hud.getLocalText("slideshow.title"));
		title.setStyleClass("label-title");
		this.add(title, BorderLayout.NORTH);

		slideContainer =  new BContainer();
		this.add(slideContainer,BorderLayout.CENTER);

		initBottom();

		this.setPreferredSize(hud.w-Hud.SPACE*30, hud.h-Hud.SPACE*30);
		//this.setPreferredSize(hud.w/2, hud.h/2);
		this.setVisible(false);
		this.setLayer(1);

		BuiSystem.addWindow(this);
		this.pack();
		this.center();
	}

	/**
	 * initialise les 3 bouton du bas
	 */
	private void initBottom() {
		TableLayout layout = new TableLayout(3,0,Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);
		bottom = new BContainer(layout);

		LaBButton previous = new LaBButton(hud.getLocalText("slideshow.previous"));
		previous.setPreferredSize(hud.w/3, -1);
		previous.addListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				displayPrevious();
			}
		});
		bottom.add(previous);

		LaBButton close = new LaBButton(hud.getLocalText("slideshow.close")) {
			/* (non-Javadoc)
			 * @see com.jmex.bui.BComponent#isEnabled()
			 */
			@Override
			public boolean isEnabled() {
				return slideShow==null || ScriptConstants.VOID_SCRIPT.equalsIgnoreCase(slideShow.getOnEnd());
			}
		};
		close.setPreferredSize(hud.w/3, -1);
		close.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
			}
		});
		bottom.add(close);

		LaBButton next = new LaBButton(hud.getLocalText("slideshow.next"));
		next.setPreferredSize(hud.w/3, -1);
		next.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				displayNext();
			}
		});
		bottom.add(next);

		this.add(bottom,BorderLayout.SOUTH);
	}

	/**
	 * ouvre le slide show
	 * @param slideShow
	 */
	public void open(SlideShow slideShow,int page) {
		this.slideShow = slideShow;
		current = page;
		refresh();
	}

	public void displayNext() {
		unloadCurrent();
		current++;
		refresh();
	}
	public void displayPrevious() {
		unloadCurrent();
		current = Math.max(0,current-1);;
		refresh();
	}

	/**
	 * met à jour le contenu de la slide
	 */
	private void refresh() {
		if (slideShow == null) return;

		if (Variables.getWorld().isUpdate(this.slideShow))
			//GameTaskQueueManager.getManager().update(this);
                System.out.println("SlideShowHud -> refresh() : GameTaskQueueManager !!");
		else 
			Variables.getClientConnecteur().updateFromServerAndWait(slideShow, this);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
		if (slideShow == null || current>=slideShow.getSlideCount()) {
			setVisible(false);
			if (slideShow!=null)
				slideShow.end();
			return null;
		}

		loadCurrentSlide();
		if (!isVisible())
			setVisible(true);

		return null;
	}


	/**
	 * charge la slide courent
	 */
	private void loadCurrentSlide() {
		bottom.setVisible(slideShow.isDisplayButtons());
		
		try {
			setSize(hud.w*slideShow.getW()/100,hud.h*slideShow.getH()/100);
			setLocation(hud.w*slideShow.getX()/100-getWidth()/2,hud.h*slideShow.getY()/100-getHeight()/2);
			
			if (!title.getText().equalsIgnoreCase(slideShow.getTitle()))
				title.setText(slideShow.getTitle());
			imgCurrent = new BImage(getUrl(slideShow.getSlideUrl(this.current)));
			ImageBackground background = new ImageBackground(ImageBackgroundMode.SCALE_XY, imgCurrent);
			slideContainer.setBackground(DEFAULT, background);
			slideContainer.setBackground(HOVER, background);
			slideContainer.setBackground(DISABLED, background);
			
			//hud.getGame().getSoundSystem().play(slideShow.getSlideSound(this.current));
                        System.out.println("SlideShowHud -> loadCurrentSlide() : not activating sound !!");
		} catch (MalformedURLException e) {
			logger.warning(e.getMessage());
		} catch (IOException e) {
			logger.warning(e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.warning(e.getMessage());
		} finally {
		/*	GameTaskQueueManager.getManager().update(new Callable<Void>() {
				/* (non-Javadoc)
				 * @see java.util.concurrent.Callable#call()
				 */
		/*		@Override
				public Void call() throws Exception {
					bottom.setVisible(slideShow.isDisplayButtons());
					validate();
					return null;
				}
			});*/
		} 
                     System.out.println("SlideShowHud -> loadCurrentSlide() : GameTaskQueueManager !!");
		//http://prjserv002.insa-lyon.fr/pegase-iut/ressources/slide-pegase-intro
	}

	/**
	 * recourci
	 * @param slideUrl
	 * @return
	 * @throws MalformedURLException 
	 */
	private URL getUrl(String u) throws MalformedURLException {
		if (u.startsWith("http://"))
			return new URL(u);
		
                System.out.println("SlideShowHud -> getUrl() : ResourceLocatorTool !!!");
                return null;
                //return ResourceLocatorTool.locateResource(HttpResourceLocator.RESOURCE, u);
	}

	/**
	 * desactive la music de la slide courante
	 */
	private void unloadCurrent() {
		if (slideShow==null) return; 
                System.out.println("SlideShowHud -> unloadCurrent() : manquante !!");
		/*if (imgCurrent!=null)
			TextureManager.deleteTextureFromCard(((TextureState)imgCurrent.getRenderState(StateType.Texture)).getTexture());
		hud.getGame().getSoundSystem().stop(slideShow.getSlideSound(this.current));		
                */
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
            System.out.println("SlideShowHud -> run() : GameTaskQueueManager !!");
		//GameTaskQueueManager.getManager().update(this);
	}


	/* (non-Javadoc)
	 * @see com.jmex.bui.BContainer#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (!visible)
			unloadCurrent();
	}

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
