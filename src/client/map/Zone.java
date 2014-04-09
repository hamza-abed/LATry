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
package client.map;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;

import shared.constants.PckCode;
import shared.enums.LaComponent;
import shared.pck.Pck;
import client.interfaces.graphic.Graphic;
import client.interfaces.network.Sharable;
import client.interfaces.network.SharableGroup;

import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import shared.variables.Variables;

/**
 * Les zones sont des lieux d'echange d'information
 * <ul>
 * <li>Une putain de galere, c'est dangeureux de travailler dedans</li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class Zone implements ClientChannelListener, SharableGroup {
	private static final Logger logger = Logger.getLogger("Zone");

	@SuppressWarnings("unused")
	private ClientChannel channel;

	/**
	 * mode conteneur
	 */
	private World world;

	/**
	 * coordonné de la zone
	 */
	private int x, z;

	private int versionCode = -1;

	private ArrayList<Sharable> sharables = new ArrayList<Sharable>();

	/**
	 * 
	 * @param world
	 * @param x
	 * @param z
	 */
	public Zone(World world, int x, int z) {
		this.world = world;
		this.x = x;
		this.z = z;
	}

	/* ********************************************************** *
	 * * 					CHANNEL - IMPLEMENTS 				* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sun.sgs.client.ClientChannelListener#leftChannel(com.sun.sgs.client
	 * .ClientChannel)
	 */
	@Override
	public void leftChannel(ClientChannel channel) {
		logger.info("quite la " + this);
		for (final Sharable s : sharables) {
			if (s instanceof Graphic)
				((Graphic) s).removeFromRenderTask();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sun.sgs.client.ClientChannelListener#receivedMessage(com.sun.sgs.
	 * client.ClientChannel, java.nio.ByteBuffer)
	 */
	@Override
	public void receivedMessage(ClientChannel channel, ByteBuffer message) {
		short c = message.getShort();
		switch (c) {
		case PckCode.EXTENDED_DATA:
			world.receiveExtendedDataPck(message);
			break;
		case PckCode.ADD_OBJECT:
			world.receiveSharedAddPck(message);
			break;
		case PckCode.DEL_OBJECT:
			world.receiveSharedDel(message);
			break;
		case PckCode.PLAYER_START_MOVE:
			world.receivePlayerMove(message);
			break;
		case PckCode.PLAYER_END_MOVE:
			world.receivePlayerEndMove(message);
			break;
		case PckCode.COMMIT:
			world.receiveCommitPck(message);
			break;
		case PckCode.PLAYER_DISCONNECT:
			world.receivePlayerDisconnect(message);
			break;
		case PckCode.CHAT:
			//world.getGame().
                    Variables.getClientConnecteur().getChatSystem().receivedChatMessage(message);
			break;
		default:
			logger.warning("code packet incconnu : " + c);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.network.SharableGroup#addFromServer(java.util.ArrayList)
	 */
	@Override
	public void addFromServer(ArrayList<Sharable> sharables) {
		logger.fine(sharables + " rejoins " + this);
		this.sharables.addAll(sharables);
		for (final Sharable s : sharables) {
			if (s instanceof Graphic)
				((Graphic) s).addToRenderTask();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * client.interfaces.network.SharableGroup#delFromServer(java.util.ArrayList
	 * )
	 */
	@Override
	public void delFromServer(ArrayList<Sharable> sharables) {
		logger.fine(sharables + " quitte " + this);
		this.sharables.removeAll(sharables);
		// TODO gerer un joueur
	}

	/* ********************************************************** *
	 * * 					SharableImplements					* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.network.Sharable#getVersionCode()
	 */
	@Override
	public int getVersionCode() {
		return versionCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	@Override
	public void addData(Pck pck) {
		throw new RuntimeException("NYI");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	public String getKey() {
		return LaComponent.zone.prefix() + x + ":" + z;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * client.interfaces.network.Sharable#receiveCommit(java.nio.ByteBuffer)
	 */
	@Override
	public void receiveCommitPck(ByteBuffer message) {
		int nv = message.getInt();
		/*if (nv>versionCode) {
			world.getGame().up
		}//*/
		this.versionCode = nv;
	}

	/* ********************************************************** *
	 * * 					GETTERS / SETTERS 					* *
	 * ********************************************************** */

	/**
	 * fix le channel de communication (reviens à le rejoindre)
	 * 
	 * @param channel
	 */
	public void setChannel(ClientChannel channel) {
		this.channel = channel;
		//world.getGame().
                Variables.getClientConnecteur().updateFromServer(this);
		logger.fine("rejoins la " + this);
		for (final Sharable s : sharables) {
			if (s instanceof Graphic)
				((Graphic) s).addToRenderTask();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getKey();
	}

}
