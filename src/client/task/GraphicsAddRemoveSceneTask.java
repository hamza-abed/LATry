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
package client.task;

import java.util.concurrent.Callable;

import client.interfaces.graphic.Graphic;
import client.map.World;
import client.map.character.Player;

//import com.jme.util.GameTaskQueueManager;

/**
 * Teache d'ajout/suppression d'un objet en scene
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class GraphicsAddRemoveSceneTask implements Callable<Void> {

	private Graphic obj;
	private boolean add;
	private World world;

	/**
	 * @param abstractCharacter
	 */
	public GraphicsAddRemoveSceneTask(Graphic obj, World world) {
		this.obj = obj;
		this.world = world;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
		if (add) {
			world.addGraphics(obj);
                        /*
                         * Dans la nouvelle version la Chase Cam est toujour activé 
                         * elle suit le joueur principale pour qu'il sent qu'il est bien integré 
                         * dans le jeux 
                         *  Hamza ABED
                         */
			//if (obj instanceof Player) 
			//	world.getGame().getCameraControler().switchToChase((Player)obj);
			/*else if (obj instanceof AbstractMapObject)
				((AbstractMapObject)obj).rebuildGeometrics();//*/
		}
		else
			world.removeGraphics(obj);
		return null;
	}

	/**
	 * Demande d'affiché l'objet
	 */
	public void add() {
		add = true;
		//GameTaskQueueManager.getManager().update(this);
                //La notion du Task QueueManager n'existe pas en JME3
                
	}

	/**
	 * Demande de masquer l'objet
	 */
	public void remove() {
		add = false;
		//GameTaskQueueManager.getManager().update(this);
	}
}
