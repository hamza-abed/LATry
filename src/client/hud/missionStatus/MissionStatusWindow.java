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
package client.hud.missionStatus;

import client.hud.Hud;
import client.utils.FileLoader;

//import com.jme.util.GameTaskQueueManager;
import com.jmex.bui.BGeomView;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.layout.BorderLayout;

/**
 * fenetre contenant l'etat d'avancement du joueur dans la mission 
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class MissionStatusWindow extends BWindow{
	private MissionStatus missionStatus;
	private Hud hud;

	
	/**
	 * 
	 */
	public MissionStatusWindow(Hud hud) {
		super("Mission status Window", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/boussole.bss")), new BorderLayout(
				Hud.SPACE, Hud.SPACE)); // même style avec la boussole
		this.hud = hud;
		this.missionStatus = new MissionStatus(this);
		
		initialize();
	}


	/**
	 * configure la vue de l'etat de la mission
	 */
	private void initialize() {
		//BGeomView view = new BGeomView(missionStatus);
            System.out.println("MissionStatusWindow -> initialize() : constructeur changé !!");
            BGeomView view = new BGeomView();
		this.add(view,BorderLayout.CENTER);
		
		view.setPreferredSize(200,200);
		pack();
		setLocation(Hud.SPACE, hud.getH()-this.getHeight()-Hud.SPACE);
		BuiSystem.addWindow(this);
	}


	/**
	 * @return the hud
	 */
	public Hud getHud() {
		return hud;
	}


	/**
	 * met à jour le contenu de la zone status du joueur
	 */
	public void requestRefresh() {
		//GameTaskQueueManager.getManager().update(missionStatus);
            System.out.println("MissionStatusWindow -> ");
	}
	
	
	
	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
