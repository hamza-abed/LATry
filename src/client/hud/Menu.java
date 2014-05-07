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

import java.util.concurrent.Callable;
import java.util.logging.Logger;

import client.hud.components.LaBButton;
import client.utils.FileLoader;
import com.jme3.math.ColorRGBA;

//import com.jme.renderer.ColorRGBA;
//import com.jme.util.GameTaskQueueManager;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.background.BBackground;
import com.jmex.bui.background.TintedBackground;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.icon.IconUtil;
import com.jmex.bui.icon.ImageIcon;
import com.jmex.bui.layout.TableLayout;
import shared.variables.Variables;

/**
 * menu avec la barre en bas 
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class Menu extends BWindow implements Runnable, Callable<Void> {
	private static final Logger logger = Logger.getLogger("Menu");
	private BBackground background;
	private Hud hud;
	private LaBButton bagButton;

	private boolean blink = false;
	boolean bagBlink = false, skillBlink = false, todoBlink = false;
	private LaBButton skillButton;
	private LaBButton todoButton;
	private ImageIcon walk, run, editOn,editOff;
	private LaBButton walkRunButton;
	private LaBButton editButton;


	/**
	 * 
	 */
	public Menu(Hud hud) {
           
		super("menu",
				BStyleSheetUtil.getStyleSheet(FileLoader.getResourceAsUrl("data/hud/style/menu.bss")),
				new TableLayout(6, 0, 0));

		this.hud = hud;
                 System.out.println("Menu -> constructeur !!!");

		initialize();
	}

	/**
	 * Initilise le contenu de la fanetre
	 */
	private void initialize() {
		//ImageIcon icon = IconUtil.getIcon(FileLoader.getResourceAsUrl(LaConstants.DIR_SYSCOM_ICON+"icon-back.png"));
		/*try {
			background = new ImageBackground(ImageBackgroundMode.SCALE_XY, new BImage(FileLoader.getResourceAsUrl(LaConstants.DIR_SYSCOM_ICON+"icon-back.png")));
		} catch (IOException e) {//*/
			//background = new TintedBackground(ColorRGBA.Pink);
            System.out.println("Menus -> initialize () : vide !!");
		//}
	

		walk = IconUtil.getIcon(FileLoader.getResourceAsUrl("data/hud/menu-walk.png"));
		run = IconUtil.getIcon(FileLoader.getResourceAsUrl("data/hud/menu-run.png"));
		
		editOn = IconUtil.getIcon(FileLoader.getResourceAsUrl("data/hud/menu-edit.png"));
		editOff = IconUtil.getIcon(FileLoader.getResourceAsUrl("data/hud/menu-edit-disable.png"));
		


		walkRunButton = new LaBButton(walk,
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				hud.getPlayer().setWalk(
						!hud.getPlayer().isWalk());
				walkRunButton.setIcon(hud.getPlayer().isWalk()?walk:run);
			}
		}, "switch to walk/run");
		walkRunButton.setTooltipText(hud.getLocalText("menu.walkrun.tooltip"));
		this.add(walkRunButton);//*/

		bagButton = new LaBButton(IconUtil.getIcon(FileLoader
				.getResourceAsUrl("data/hud/menu-bag.png")),
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				bagBlink = false;
				hud.bags.setVisible(!hud.bags.isVisible());
			}
		}, "Open / Close bags");
		bagButton.setTooltipText(hud.getLocalText("menu.bag.tooltip"));
		this.add(bagButton);

		skillButton = new LaBButton(IconUtil.getIcon(FileLoader
				.getResourceAsUrl("data/hud/menu-stat.png")),
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				skillBlink = false;
				hud.skill.setVisible(!hud.skill.isVisible());
			}
		}, "Open / Close skill book");
		skillButton.setTooltipText(hud.getLocalText("menu.skill.tooltip"));
		this.add(skillButton);

		todoButton = new LaBButton(IconUtil.getIcon(FileLoader
				.getResourceAsUrl("data/hud/menu-quest.png")),
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				todoBlink = false;
				hud.tasks.setVisible(!hud.tasks.isVisible());
			}
		}, "Open / Close quests book");
		todoButton.setTooltipText(hud.getLocalText("menu.todos.tooltip"));
		this.add(todoButton);

		editButton = new LaBButton(editOn,
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Variables.setEditMode(Variables.isEditMode());
			}
		}, "switch to edit");
		editButton.setTooltipText(hud.getLocalText("menu.edit.tooltip"));
		this.add(editButton);

		LaBButton mainMenuButton = new LaBButton(IconUtil.getIcon(FileLoader
				.getResourceAsUrl("data/hud/menu-exit.png")),
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				hud.mainMenu.setVisible(true);
			}
		}, "switch to edit");
		mainMenuButton.setTooltipText(hud.getLocalText("menu.mainmenu.tooltip"));
		this.add(mainMenuButton);

		BuiSystem.addWindow(this);
		this.pack(246,42);

		this.setLocation(hud.w - getWidth()-1, 1);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
		logger.fine("blink");
		if (blink && bagBlink) {
			bagButton.setBackground(LaBButton.DOWN, background);
			bagButton.setBackground(LaBButton.DISABLED, background);
			bagButton.setBackground(LaBButton.HOVER, background);
			bagButton.setBackground(LaBButton.DEFAULT, background);
		} else if (bagButton.getBackground()!=null) {
			bagButton.setBackground(LaBButton.DOWN, null);
			bagButton.setBackground(LaBButton.DISABLED, null);
			bagButton.setBackground(LaBButton.HOVER, null);
			bagButton.setBackground(LaBButton.DEFAULT, null);
		}

		if (blink && skillBlink) {
			skillButton.setBackground(LaBButton.DOWN, background);
			skillButton.setBackground(LaBButton.DISABLED, background);
			skillButton.setBackground(LaBButton.HOVER, background);
			skillButton.setBackground(LaBButton.DEFAULT, background);
		} else if (skillButton.getBackground()!=null) {
			skillButton.setBackground(LaBButton.DOWN, null);
			skillButton.setBackground(LaBButton.DISABLED, null);
			skillButton.setBackground(LaBButton.HOVER, null);
			skillButton.setBackground(LaBButton.DEFAULT, null);
		}

		if (blink && todoBlink) {
			todoButton.setBackground(LaBButton.DOWN, background);
			todoButton.setBackground(LaBButton.DISABLED, background);
			todoButton.setBackground(LaBButton.HOVER, background);
			todoButton.setBackground(LaBButton.DEFAULT, background);
		} else if (todoButton.getBackground()!=null) {
			todoButton.setBackground(LaBButton.DOWN, null);
			todoButton.setBackground(LaBButton.DISABLED, null);
			todoButton.setBackground(LaBButton.HOVER, null);
			todoButton.setBackground(LaBButton.DEFAULT, null);
		}
		blink = ! blink;
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
            /*
		while(!Variables.isFinished()) try {
			GameTaskQueueManager.getManager().update(this);
			Thread.sleep(500);
		} catch (InterruptedException e) {
			logger.warning("InterruptedException "+e);
		}
                */
            System.out.println("Menu -> run() : vide !! GameTAskQueueManager !!");
	}

	/* (non-Javadoc)
	 * @see com.jmex.bui.BContainer#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

	}

	/**
	 * met à jour le menu en fonction des droit
	 * @param admin
	 */
	public void updateRights(final boolean admin) {
		editButton.setEnabled(admin);
	//	GameTaskQueueManager.getManager().update(new Callable<Void>() {
			/* (non-Javadoc)
			 * @see java.util.concurrent.Callable#call()
			 */
	/*		@Override
			public Void call() throws Exception {
				editButton.setIcon(admin?editOn:editOff);
				return null;
			}
		});
                */
                 System.out.println("Menu -> updateRights() : vide !! GameTAskQueueManager !!");
	}


	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
