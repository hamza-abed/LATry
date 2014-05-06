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
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import client.hud.components.LaBButton;
import client.map.character.PlayableCharacter;
import client.utils.FileLoader;

//import com.jme.util.GameTaskQueueManager;
import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.Policy;
import com.jmex.bui.layout.VGroupLayout;
import shared.variables.Variables;

/**
 * Fenetre du groupe du joueur 
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class Group extends BWindow implements Callable<Void> {
	private static final Logger logger = Logger.getLogger("Group");
	private Hud hud;
	private BContainer panel;
	private client.map.character.Group group;
	private LaBButton title;

	/**
	 * 
	 */
	public Group(Hud hud) {
		super("Group", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/hud/style/group.bss")), 
				new BorderLayout(Hud.SPACE, Hud.SPACE));
		this.hud = hud;
		initialize();
	}
	
	/**
	 * Initialise le contenu de la fenetre
	 */
	private void initialize() {
		title = new LaBButton("group");
		title.setStyleClass("label-title");
		title.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				logger.info("refresh");
				update();
			}
		});
		//title.addListener(new WindowMoveListener(this));
		this.add(title, BorderLayout.NORTH);

		VGroupLayout layout = new VGroupLayout();
		layout.setOffAxisPolicy(Policy.STRETCH);
		panel = new BContainer(layout);
		panel.add(new BLabel("empty"));
		this.add(panel,BorderLayout.CENTER);
		
		setVisible(false);

		BuiSystem.addWindow(this);
		//setSize(100,-1);
		setPreferredSize(100, -1);
		setLocation(Hud.SPACE, 30);
		pack();
		
		hud.getGame().getSchedulerTaskExecutor().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				update();
			}
		},30,30,TimeUnit.SECONDS);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
		if (group == null) {
			setVisible(false);
			return null;
		}
		
		logger.fine("mise à jour de la fenetre du groupe");
		title.setText(group.toString());
		panel.removeAll();

		for (PlayableCharacter c : group.getPlayers()) {
			BLabel label = new BLabel(c.getDisplayName());
			if (c.isConnected()) {
				label.setStyleClass("connected");
			} else {
				label.setStyleClass("disconnected");
			}
			panel.add(label);
		}

		setVisible(true);
		return null;
	}
	
	/**
	 * MET À JOUR LA FENETRE
	 */
	private void update() {
		group = Variables.getMainPlayer().getMainGroup();
		//GameTaskQueueManager.getManager().update(this);
                System.out.println("Group -> update : GameTaskQueueManager !!");
                
	}
	
	/* (non-Javadoc)
	 * @see com.jmex.bui.BContainer#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		//setSize(100,-1);
		setPreferredSize(-1, -1);
		pack();
		setLocation(Hud.SPACE, hud.h-getHeight()-Hud.SPACE);
		
	}
	
	
	
	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
