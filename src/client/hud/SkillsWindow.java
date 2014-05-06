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

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import shared.constants.LaConstants;
import client.hud.components.LaBButton;
import client.hud.components.WindowMoveListener;
import client.map.character.stats.PlayerSkills;
import client.map.character.stats.Skill;
import client.map.character.stats.UserModel;
import client.utils.FileLoader;

//import com.jme.util.GameTaskQueueManager;
import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BScrollPane;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.BLabel.Fit;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.event.MouseEvent;
import com.jmex.bui.event.MouseListener;
import com.jmex.bui.icon.IconUtil;
import com.jmex.bui.icon.ImageIcon;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.Justification;
import com.jmex.bui.layout.Policy;
import com.jmex.bui.layout.VGroupLayout;
import shared.variables.Variables;

/**
 * Livre de competence
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class SkillsWindow extends BWindow implements Callable<Void> {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("SkillsWindow");
	private BContainer panel;
	private LaBButton closeButton;
	private Hud hud;

	// liste des competence trié par categorie.
	private HashMap<String, HashMap<String, String>> skills = new HashMap<String, HashMap<String,String>>();
	private ImageIcon catOpen;
	private HashMap<String, Boolean> open = new HashMap<String, Boolean>();
	private ImageIcon catClose;

	/**
	 * 
	 */
	public SkillsWindow(Hud hud) {
		super("skills window", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/hud/style/skills.bss")), 
				new BorderLayout(Hud.SPACE, Hud.SPACE));
		this.hud = hud;
		initialize();
	}

	/**
	 * Initialise le contenu de la fenetre
	 */
	private void initialize() {
		catOpen = IconUtil.getIcon(FileLoader.getResourceAsUrl(
				LaConstants.DIR_LGPL_ICON+"downarrow-12.png"));
		catClose = IconUtil.getIcon(FileLoader.getResourceAsUrl(
				LaConstants.DIR_LGPL_ICON+"rightarrow-12.png"));

		initTop();
		initMiddle();


		BScrollPane sp = new BScrollPane(panel,true,false);
		sp.setStyleClass("container-middle");
		//sp.getVerticalScrollBar();
		this.add(sp, BorderLayout.CENTER);

		BuiSystem.addWindow(this);
		this.pack();
		this.center();
		this.setLocation(hud.w-getWidth()-Hud.SPACE, getY()+Hud.WINDOW_DECAL_Y);
	}

	/**
	 * Initialise la partie superieure du skill book
	 */
	private void initTop() {
		BContainer top = new BContainer(new BorderLayout(0,0));

		BLabel space = new BLabel("");
		space.setStyleClass("close");
		top.add(space, BorderLayout.WEST);

		BLabel title = new BLabel(hud.getLocalText("skillbook.title"));
		title.setStyleClass("label-title");
		title.addListener(new WindowMoveListener(this));
		top.add(title, BorderLayout.CENTER);

		closeButton = new LaBButton("");
		closeButton.setStyleClass("close");
		closeButton.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		top.add(closeButton,BorderLayout.EAST);

		this.add(top, BorderLayout.NORTH);
	}

	private void initMiddle() {
		VGroupLayout layout = new VGroupLayout( 
				Justification.TOP,
				Policy.NONE);
		layout.setOffAxisJustification(Justification.LEFT);
		layout.setOffAxisPolicy(Policy.CONSTRAIN);
		layout.setGap(0);

		panel = new BContainer(layout);
		panel.setStyleClass("dialog-container");
	}

	/* ********************************************************** *
	 * *	 				Mise à jour			 				* *
	 * ********************************************************** */

	/**
	 * Met à jour le contenu de la fenetre
	 */
	private void update() {
		updateSkills();
		panel.removeAll();
		for (final Entry<String, HashMap<String, String>> cat : skills.entrySet()) {
			BLabel categorie = new BLabel(cat.getKey());
			categorie.setFit(Fit.TRUNCATE);
			categorie.setStyleClass("categorie");
			categorie.setIcon(open.get(cat.getKey())?catOpen:catClose);
			categorie.addListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent event) {
					open.put(cat.getKey(),!open.get(cat.getKey()));
					requestUpdate();
				}
				@Override
				public void mousePressed(MouseEvent event) { }
				@Override
				public void mouseExited(MouseEvent event) { }
				@Override
				public void mouseEntered(MouseEvent event) { }
			});
			panel.add(categorie);
			if (open.get(cat.getKey()))
				for (final Entry<String, String> skill : cat.getValue().entrySet()) {
					if (cat.getKey().equalsIgnoreCase("widget")) {
						LaBButton button = new LaBButton(skill.getKey());
						button.addListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent event) {
								openWidget(skill.getValue());
							}
						});
						button.setStyleClass("widget");
						button.setFit(Fit.WRAP);
						panel.add(button);
					} else {
						BLabel label = new BLabel(skill.getKey()+" : "+skill.getValue());
						label.setStyleClass("skill");
						label.setFit(Fit.WRAP);
						panel.add(label);
					}
				}
		}
	}

	/**
	 * ouvre le widget correspondant à la valeur
	 * @param value
	 */
	protected void openWidget(String value) {
		String [] args= value.split("#");
		switch (Integer.parseInt(args[0])) {
		case 11: hud.openSpiderWidget(args[1], args[2], args[3], args[4]);
		break;
		}
	}

	/**
	 * Met à jour en local la liste des skills 
	 * rangé par categorie
	 */
	private void updateSkills() {
		PlayerSkills ps= Variables.getMainPlayer().getSkills();
		UserModel um = Variables.getMainPlayer().getUserModel();

		// clear des competence affiché
		for (HashMap<String, String> list : skills.values())
			list.clear();
		// ajout des competence du jeux
		for (Entry<Skill, String> e : ps.getAllSkills().entrySet()) {
			Skill s = e.getKey();
			if (s.getCategorie().equalsIgnoreCase(LaConstants.UNSET_STRING)) 
				continue;
			if (!skills.containsKey(s.getCategorie())) {
				skills.put(s.getCategorie(), new HashMap<String, String>());
				open .put(s.getCategorie(),true);
			}
			skills.get(s.getCategorie()).put(s.getName(), e.getValue());
		}
		
		// ajout des competence du UM.
		for (String cat : um.listCategories()) {
			if (!skills.containsKey(cat)) {
				skills.put(cat, new HashMap<String, String>());
				open.put(cat,true);
			}
			for (String attr : um.listAttributes(cat)) 
				skills.get(cat).put(attr,um.getValue(cat,attr));	
		}

	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
		update();
		return null;
	}

	/**
	 * met à jour la fenetre
	 */
	public void refresh() {
		/*if (isVisible())
			GameTaskQueueManager.getManager().update(this);
                        */
            System.out.println("SkillsWindow -> refresh() : GameTaskQueueManager !!");
	}
	
	/* ********************************************************** *
	 * * 			Affichage du livre de competence 			* *
	 * ********************************************************** */


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jmex.bui.BContainer#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible)
			update();
		super.setVisible(visible);
	}

	/**
	 * Demande de mise à jour de la fenetre des skil
	 */
	public void requestUpdate() {
		if (isVisible())
			//GameTaskQueueManager.getManager().update(this);
                    System.out.println("SkillsWindow -> requestUpdate() : GameTaskQueueManager !!");
	}


}
