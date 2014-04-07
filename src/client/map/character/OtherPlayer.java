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
package client.map.character;

import java.util.logging.Logger;

import client.interfaces.network.SharableReflexEditable;
import client.map.World;

import com.jme3.math.Vector3f;

/**
 * les autres joueurs
 * <ul>
 * <li>Les autres joueurs</li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class OtherPlayer extends PlayableCharacter implements SharableReflexEditable {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("OtherPlayer");
	private boolean renderer;

	/**
	 * @param world
	 * @param login
	 */
	public OtherPlayer(World world, String login) {
		super(world, login);

	}

	/* ********************************************************** *
	 * * 						Deplacement 					* *
	 * ********************************************************** */
	/*
	 * (non-Javadoc)
	 * 
	 * @see client.character.PlayableCharacter#endMove()
	 * 
	 * A la fin du deplacement il faut verifié que le joueur soit dans l'espace
	 * de vue du joueur
	 */
	@Override
	protected void endMove() {
		super.endMove();
		/*if (!world.getPlayer().isInVueSpace(x, z)) {
			removeFromRenderTask();
			// permet de gerer la reaparition quand il sort de l'espace de vue
			renderer = false;
		}*/
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.character.PlayableCharacter#setXZ(float, float)
	 */
	@Override
	protected void setXZ(float x, float z) {
		super.setXZ(x, z);
		/*if (!renderer && world.getPlayer().isInVueSpace(x, z)) {
			addToRenderTask();
			// permet de gerer la reaparition quand il sort de l'espace de vue
			renderer = true;
			world.getGame().updateFromServer(super.tokens);
		} */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * client.character.AbstractCharacter#testCollision(com.jme.math.Vector3f)
	 */
	@Override
	protected boolean testCollision(Vector3f newPos, Vector3f dir) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.character.AbstractCharacter#canMoveAt(com.jme.math.Vector3f)
	 */
	@Override
	protected boolean canMoveAt(Vector3f newPos) {
		return true;
	}

	
}
