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

import java.util.Collection;

import client.hud.components.LaBButton;
import client.hud.components.WindowMoveListener;
import client.map.character.Group;
import client.map.character.PlayableCharacter;
import client.utils.FileLoader;

import com.jmex.bui.BCheckBox;
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
 * Fenetre d'édition des groupes 
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class GroupEditor extends BWindow {
	private Hud hud;
	private BContainer panel;
	private BLabel title;

	/**
	 * 
	 */
	public GroupEditor(Hud hud) {
		super("Group", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/hud/style/group-editor.bss")), 
				new BorderLayout(Hud.SPACE, Hud.SPACE));
		this.hud = hud;
		initialize();
	}
	
	/**
	 * Initialise le contenu de la fenetre
	 */
	private void initialize() {
		title = new BLabel(hud.getLocalText("groupeditor.title"));
		title.setStyleClass("label-title");
		title.addListener(new WindowMoveListener(this));
		this.add(title, BorderLayout.NORTH);

		TableLayout layout = new TableLayout(1, 0, 0);
		panel = new BContainer(layout);
		panel.add(new BLabel("empty"));
		this.add(panel,BorderLayout.CENTER);
		
		this.add(new LaBButton(hud.getLocalText("groupeditor.close"),new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
			}
		},"close"),BorderLayout.SOUTH);
		
		
		setVisible(false);

		BuiSystem.addWindow(this);
		//setSize(100,-1);
		setPreferredSize(100, -1);
		setLocation(Hud.SPACE, 30);
		pack();
		
	}
	
	/**
	 * TODO commentaire GroupEditor.refresh
	 */
	private void refresh() {
		Collection<Group> groups = Variables.getWorld().getGroups();
		Collection<PlayableCharacter> players = Variables.getWorld().getPlayers();
		
		TableLayout layout = new TableLayout(groups.size()+1, 0, 0);
		panel.removeAll();
		panel.setLayoutManager(layout);
		panel.add(new BLabel("\\"));
		
		for (Group g : groups) 
			panel.add(new BLabel(g.getName()));
		
		for (final PlayableCharacter p : players) {
			panel.add(new BLabel(p.getName()));
			for (final Group g : groups) {
				final BCheckBox check = new BCheckBox("");
				check.setSelected(g.hasPlayer(p));
				check.addListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						if (check.isSelected())
							g.addPlayer(p);
						else 
							g.delPlayer(p);
					}
				});
				panel.add(check);
			}
		}
	}	

	/* (non-Javadoc)
	 * @see com.jmex.bui.BContainer#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) {
			refresh();
			setPreferredSize(-1, -1);
			pack();
			center();
		}
	}


	
	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
