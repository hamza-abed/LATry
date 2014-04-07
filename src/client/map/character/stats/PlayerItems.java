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
package client.map.character.stats;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import shared.constants.LaConstants;
import shared.enums.LaComponent;
import shared.pck.Pck;
import client.LaGame;
import client.interfaces.network.SharableReflexEditable;
import client.map.character.PlayableCharacter;

/**
 * Sac d'un joueur
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class PlayerItems implements SharableReflexEditable,Runnable {

	/**
	 * 
	 */
	private int versionCode = -1;

	/**
	 * 
	 */
	private HashMap<Item, Integer> items = new HashMap<Item, Integer>();

	/**
	 * joueur à qui appartiennent ce sac
	 */
	private PlayableCharacter player = null;

	/**
	 * jeux conteneur pour envoyé les modification
	 */
	private LaGame game;

	private boolean commiting;

	/**
	 * 
	 */
	public PlayerItems(PlayableCharacter player) {
		this.player = player;
		this.game = player.getWorld().getGame();
	}
	
	/**
	 * Commit sur le serveur mais dans un thread à part à cause des modification commune
	 */
	private void commit() {
		if (!commiting) {
			commiting = true;
			//game.getSchedulerTaskExecutor().schedule(this,LaConstants.WAIT_COMMITING_TIME,TimeUnit.MILLISECONDS);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		commiting = false;
		//game.commitOnServer(this);		
	}

	/* ********************************************************** *
	 * * 				Sharable - IMPLEMENTS 					* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	@Override
	public void addData(Pck pck) {
		pck.putInt(items.size());
		for (Entry<Item, Integer> item : items.entrySet())
			pck.putInt(item.getKey().getId(), item.getValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.playerBag.prefix() + player.getLogin();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#getVersionCode()
	 */
	@Override
	public int getVersionCode() {
		return versionCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * client.interfaces.network.Sharable#receiveCommitPck(java.nio.ByteBuffer)
	 */
	@Override
	public void receiveCommitPck(ByteBuffer message) {
		this.versionCode = message.getInt(); // version code
		int c = message.getInt();
		items.clear();
		for (int i = 0; i < c; i++) {
			int itemId = message.getInt();
			int q = message.getInt();
			//Item item = player.getWorld().getItemBuildIfAbsent(
				//	LaComponent.item.prefix() + itemId);
			//items.put(item, q);
		}
		if (player.isPlayer())
			refreshBag();
	}

	/**
	 * Rafraichit le contenu du sac dans l'interface
	 */
	private void refreshBag() {
		//player.getWorld().getGame().getHud().getBagWindow().refresh();
	}

	/* ********************************************************** *
	 * *				TEST / LECTURE d'items 					* *
	 * ********************************************************** */
	/**
	 * test si le joueur possede cet objet
	 * 
	 * @param Item
	 * @return
	 */
	public boolean hasItem(Item item) {
		return items.containsKey(item);
	}

	/**
	 * Renvoie la quantité d'un objet du joueur. 0 si il n'en as pas
	 * 
	 * @param cat
	 * @param token
	 * @return
	 */
	public int getItem(Item item) {
		try {
			Integer v = items.get(item);
			if (v == null)
				return 0;
			return v;
		} catch (NullPointerException e) {
			return 0;
		}
	}
	
	/**
	 * Renvoie l'enssemble objet et leurs quantité.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<Item, Integer> getAllItem() {
		return (HashMap<Item, Integer>) items.clone();
	}
	
	/* ********************************************************** *
	 * * 					Ajout d'items 						* *
	 * ********************************************************** */
	/**
	 * Ajout une certaine quantité d'un objet dans le sac du joueur
	 * 
	 * @param Item
	 * @Param quantity
	 * @return la quantité d'objet ajouté à l'inventaire
	 */
	public void addItem(Item item, int quantity) {
		int q = getItem(item);
		if (q + quantity <= 0) 
			this.items.remove(item);
		else
			this.items.put(item, q + quantity);
		//game.getTraces().sendBagChange(player,item);
		commit();
	}

	/**
	 * Ajout un objet
	 * 
	 * @param item
	 * @return vrai ou faut suivant si on as pu ajouter l'objet en fonction de si 
	 * il en avait trop pas
	 */
	public void addItem(Item item) {
		addItem(item, 1);
	}

	/**
	 * fixe la quantité d'un objet dans le sac
	 * 
	 * @param cat
	 * @param token
	 * @param value
	 */
	public void setItem(Item item, int quantity) {
		if (quantity == 0)
			items.remove(item);
		else
			items.put(item, quantity);
		//game.getTraces().sendBagChange(player,item);
		commit();
	}

	/* ********************************************************** *
	 * *				SUppresion d'items 						* *
	 * ********************************************************** */
	/**
	 * Supprime un objet du sac du joueur
	 */
	public void delItem(Item item) {
		int q = getItem(item);
		if (q>1)
			items.put(item, q-1);
		else if (q == 1) 
			items.remove(item);
		//game.getTraces().sendBagChange(player,item);
		commit();
	}
	
	/**
	 * Supprime un objet du sac du joueur
	 */
	public void delItem(Item item, int quantity) {
		int q = getItem(item);
		if (q-quantity>0)
			items.put(item, q-quantity);
		else 
			items.remove(item);
		//game.getTraces().sendBagChange(player,item);
		commit();
	}

	/**
	 * supprime tous les objet
	 */
	public void clearAll() {
		items.clear();
		commit();
	}
	

	
}
