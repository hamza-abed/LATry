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

import client.utils.FileLoader;

import com.jmex.bui.BLabel;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.layout.BorderLayout;

/**
 * Hud Daffichage des slideShow 
 * <ul>
 * <li>En suspend</li>
 * <li></li>
 * </ul>
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class GroupMeetWindow extends BWindow {
	private static final Logger logger = Logger.getLogger("SlideShowHud");

	private Hud hud;

	/**
	 * 
	 */
	public GroupMeetWindow(Hud hud) {
		super("SlideShowWindow", 
				BStyleSheetUtil.getStyleSheet(FileLoader.getResourceAsUrl("data/hud/style/group-meeting.bss")), 
				new BorderLayout(Hud.SPACE, Hud.SPACE));
		this.hud = hud;

		initialize();
	}

	/**
	 * initialise la fenetre de base
	 */
	private void initialize() {
		BLabel title = new BLabel(hud.getLocalText("slideshow.title"));
		title.setStyleClass("label-title");
		this.add(title, BorderLayout.NORTH);

		//initLeft();
		

		//initBottom();

		this.setPreferredSize(hud.w-Hud.SPACE*30, hud.h-Hud.SPACE*30);
		//this.setPreferredSize(hud.w/2, hud.h/2);
		this.setVisible(false);
		this.setLayer(1);

		BuiSystem.addWindow(this);
		this.pack();
		this.center();
	}
	
	/*private void initCenter() {
		
		
		BList list = new BList(values)
	}
	

	/**
	 * TODO commentaire GroupMeetWindow.initLeft
	 */
	/*private void initLeft() {
		BContainer left = new BContainer(new BorderLayout(Hud.SPACE, Hud.SPACE));
		left.add(new BLabel(hud.getLocalText(")), constraints)
	}

	/**
	 * initialise les 3 bouton du bas
	 */
	/*private void initBottom() {
		TableLayout layout = new TableLayout(3,0,Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);
		bottom = new BContainer(layout);

		BButton previous = new BButton(hud.getLocalText("slideshow.previous"));
		previous.setPreferredSize(hud.w/3, -1);
		previous.addListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				displayPrevious();
			}
		});
		bottom.add(previous);

		BButton close = new BButton(hud.getLocalText("slideshow.close"));
		close.setPreferredSize(hud.w/3, -1);
		close.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
			}
		});
		bottom.add(close);

		BButton next = new BButton(hud.getLocalText("slideshow.next"));
		next.setPreferredSize(hud.w/3, -1);
		next.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				displayNext();
			}
		});
		bottom.add(next);

		this.add(bottom,BorderLayout.SOUTH);
	}//*/

	


	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
