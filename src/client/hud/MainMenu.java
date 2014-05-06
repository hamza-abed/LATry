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

import java.util.logging.Logger;
//pas opérationnelle en v31	
//import client.extern.scenarization.InterfaceScenarisation;
import client.hud.components.LaBButton;
import client.utils.FileLoader;

import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.TableLayout;
import shared.variables.Variables;

/**
 * Menu principoal du jeux
 * <ul>
 * <li>Contient la configuation et exit</li>
 * <li></li>
 * <li>suppression package client.extern.scenarization;
 * client.hud.scenarization;</li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2010-2013
 */
 
public class MainMenu extends BWindow {
	private static final Logger logger = Logger.getLogger("MainMenu");
	private Hud hud;

	private BContainer panel;
	private LaBButton scenarization;

	private int w = 120, h = 16;
	private LaBButton groupeditor;
	private LaBButton populus;
	private LaBButton playerseditor;
	private LaBButton questeditor;
	private LaBButton modelConverter;

	/* ********************************************************** *
	 * * 						INITIALISATION 					* *
	 * ********************************************************** */
	/**
	 * Construit la fenetre de demande d'identification
	 * 
	 * @param hud
	 */
	public MainMenu(Hud hud) {
		super("main menu window", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/hud/style/main-menu.bss")),
				new BorderLayout(Hud.SPACE, Hud.SPACE));
		this.hud = hud;
		initialize();
	}

	/**
	 * initialise le contenu de la fenetre
	 */
	private void initialize() {
		logger.entering(this.getClass().getName(), "initialize");
		BLabel title = new BLabel(hud.getLocalText("mainmenu.title"));
		title.setStyleClass("label-title");
		title.setPreferredSize(w, h);
		this.add(title, BorderLayout.NORTH);

		initMiddle();

		setLayer(2);

		// this.setPreferredSize(120,-1);
		pack();
		center();
		BuiSystem.addWindow(this);

		logger.exiting(this.getClass().getName(), "initialize");
	}

	/**
	 * initialise le panneau principal
	 */
	private void initMiddle() {
		logger.entering(this.getClass().getName(), "initMiddle");

		LaBButton exit = new LaBButton(hud.getLocalText("mainmenu.exitbutton"),
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Variables.getLaGame().quitGame();
			}
		}, "exit");
		exit.setPreferredSize(w, h);

		populus = new LaBButton(hud.getLocalText("mainmenu.populus"),
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (populus.isEnabled() && hud.getPlayer().isAdmin()) {
					//hud.getPopulus().setVisible(true);
					setVisible(false);
				}
			}
		}, "populateur");
		populus.setPreferredSize(w, h);

		scenarization = new LaBButton(hud.getLocalText("mainmenu.scenarization"),
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (scenarization.isEnabled() && hud.getPlayer().isAdmin()) {
					/*hud.getEditeurDeButs().setVisible(!hud.getEditeurDeButs().isVisible());
						hud.getEditeurDActions().setVisible(!hud.getEditeurDActions().isVisible());
						hud.getZonesDActivites().setVisible(!hud.getZonesDActivites().isVisible());
						hud.getElementsDActivites().setVisible(!hud.getElementsDActivites().isVisible());
					 */
					//hud.getZonesDActivites().setVisible(!hud.getZonesDActivites().isVisible());
					//hud.getElementsDActivites().setVisible(!hud.getElementsDActivites().isVisible());
					
					// pas opérationnelle en v31	(juste ce qui est en dessous
					//InterfaceScenarisation.show(hud);
					setVisible(false);
				}
			}
		}, "close");
		scenarization.setPreferredSize(w, h);

		groupeditor = new LaBButton(hud.getLocalText("mainmenu.groupeditor"),
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (groupeditor.isEnabled() && hud.getPlayer().isAdmin()) {
					hud.getGroupeditor().setVisible(true);
					setVisible(false);
				}
			}
		}, "group editor");
		groupeditor.setPreferredSize(w, h);

		playerseditor = new LaBButton(hud.getLocalText("mainmenu.playereditor"),
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (playerseditor.isEnabled() && hud.getPlayer().isAdmin()) {
					hud.getPlayerEditor().setVisible(true);
					setVisible(false);
				}
			}
		}, "players editor");
		playerseditor.setPreferredSize(w, h);

		questeditor = new LaBButton(hud.getLocalText("mainmenu.questeditor"),
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (questeditor.isEnabled() && hud.getPlayer().isAdmin()) try {
					// pas opérationnelle en v31	
					//hud.getQuestEditor().setVisible(true);
					setVisible(false);
				} catch (Exception e) {
					hud.openErrorPopup(e);
				}
			}
		}, "quest editor");
		questeditor.setPreferredSize(w, h);
		
		modelConverter = new LaBButton(hud.getLocalText("mainmenu.modelconverter"),
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
                            System.out.println("MainMenu -> actionPerformed : vide!!!");
				/*if (modelConverter.isEnabled() && hud.getPlayer().isAdmin()) try {
					new client.utils.ObjToJmexFrame();
				} catch (Exception e) {
					e.printStackTrace();
					hud.openErrorPopup(e);
				}
                                */
			}
		}, "quest editor");
		modelConverter.setPreferredSize(w, h);
		
		LaBButton close = new LaBButton(hud.getLocalText("mainmenu.closebutton"),
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
			}
		}, "close");
		close.setPreferredSize(w, h);

		TableLayout layout = new TableLayout(1, Hud.SPACE, Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);

		panel = new BContainer(layout);
		panel.add(scenarization);
		panel.add(questeditor);
		panel.add(populus);
		panel.add(modelConverter);
		panel.add(groupeditor);
		panel.add(playerseditor);
		panel.add(exit);
		panel.add(close);
		this.add(panel, BorderLayout.CENTER);

		logger.exiting(this.getClass().getName(), "initMiddle");
	}
	/**
	 * met à jour le menu en fonction des droit
	 * @param admin
	 */
	public void updateRights(final boolean admin) {
		scenarization.setEnabled(admin);
		questeditor.setEnabled(admin);
		populus.setEnabled(admin);
		groupeditor.setEnabled(admin);
		playerseditor.setEnabled(admin);
		modelConverter.setEnabled(admin);
	}

}
