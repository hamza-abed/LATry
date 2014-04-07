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
import java.util.logging.Logger;

import shared.constants.LaConstants;
import shared.enums.LaComponent;
import shared.pck.Pck;
import client.LaGame;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
import client.interfaces.network.SharableReflexEditable;
import client.map.character.PlayableCharacter;
//import client.map.object.AbstractMapObject;

/**
 * Enssembles des token d'un joueurs.
 * <ul>
 * <li>La clef d'un token dans une hashmap est cat:nom</li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class PlayerTokens implements SharableReflexEditable,Runnable {
	private int versionCode = -1;
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("PlayerTokens");

	@Editable(type=FieldEditType.map,keyType=FieldEditType.string,innerType=FieldEditType.string)
	private HashMap<String, String> tokens = new HashMap<String, String>();
	
	@Editable(type=FieldEditType.map,keyType=FieldEditType.string,innerType=FieldEditType.string)
	private HashMap<String, String> toCommit = new HashMap<String, String>(); 

	/**
	 * joueur à qui appartiennent ces tokens
	 */
	private PlayableCharacter player = null;

	/**
	 * jeux conteneur pour envoyé les modification
	 */
	private LaGame game;
	
	/**
	 * iindique qu'il est court de commit
	 */
	private boolean commiting = false;
	
	/**
	 * 
	 */
	public PlayerTokens(PlayableCharacter player) {
		this.player = player;
		this.game = player.getWorld().getGame();
	}

	/* ********************************************************** *
	 * * 					Sharable - IMPLEMENTS 				* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	@Override
	public synchronized void addData(Pck pck) {
		/*int count = tokens.size();
		// enleve le compte de ce qu'il y a à supprime
		for (Entry<String, String>t : toCommit.entrySet())
			if (t.getValue()==null && tokens.containsKey(t.getKey()))
				count --;
			else if (t.getValue()!=null && !tokens.containsKey(t.getKey()))
				count++;
		
		pck.putInt(count);
		for (Entry<String, String> token : tokens.entrySet())
			if (!toCommit.containsKey(token.getKey()))
				pck.putString(token.getKey(), token.getValue());
		for (Entry<String, String> token : toCommit.entrySet())
			if (token.getValue()!=null)
				pck.putString(token.getKey(), token.getValue());//*/
		pck.putInt(toCommit.size());
		for (Entry<String, String> token : toCommit.entrySet())
			pck.putString(token.getKey(), token.getValue());//*/
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.playerToken.prefix() + player.getLogin();
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
	public synchronized void receiveCommitPck(ByteBuffer message) {
		this.versionCode = message.getInt(); // version code
		int c = message.getInt();
		HashMap<String, String> old = new HashMap<String, String>(tokens);
		tokens.clear();
		while (c-->0) 
			tokens.put(Pck.readString(message), Pck.readString(message));
		checkChange(old);
	}

	/**
	 * @param news
	 */
	private void checkChange(HashMap<String, String> old) {
		if (!player.isPlayer()) return;
		
		for (Entry<String, String> n : tokens.entrySet()) 
			if (!old.containsKey(n.getKey()) || 
					!old.get(n.getKey()).equalsIgnoreCase(n.getValue()))
				tokenChange(n.getKey(), n.getValue());
		for (Entry<String, String> n : old.entrySet())
			if (!tokens.containsKey(n.getKey()))
				tokenChange(n.getKey(), null);
		
	}
	
	/**
	 * envoie l'objet sur le serveur
	 */
	private synchronized void commit() {
		if (!commiting) {
			commiting = true;
			//game.getSchedulerTaskExecutor().schedule(this, LaConstants.WAIT_COMMITING_TIME, TimeUnit.MILLISECONDS);
		}
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public synchronized void run() {
		//game.commitOnServer(this);		
		toCommit.clear();
		commiting = false;
	}
	
	
	/* ********************************************************** *
	 * * 				GESTION DES CHANGEMENT 					* *
	 * ********************************************************** */

	/**
	 * @param cat
	 * @param token
	 * @param value
	 */
	private void tokenChange(String cat, String token, String value) {
	/*	
            if (!player.isPlayer()) return;
		
		if (cat.equalsIgnoreCase(LaConstants.OBJECT_VISIBILITY_CAT_TOKEN))
			((AbstractMapObject)player.getWorld().getMapObject(
					LaComponent.object.prefix()+token)).updateVisibility();
		else if (cat.equalsIgnoreCase(LaConstants.NPC_ON_HEAD_TOKEN_CAT))
			player.getWorld().getNpcBuildIfAbsent(
					LaComponent.npc.prefix()+token).updateOnHead();
		else if (cat.equalsIgnoreCase(LaConstants.NPC_VISIBILITY_CAT_TOKEN))
			player.getWorld().getNpcBuildIfAbsent(
					LaComponent.npc.prefix()+token).updateVisibility();
                                        */
	}
	
	/**
	 * @param key
	 * @param value
	 */
	private void tokenChange(String key, String value) {
		tokenChange(key.split(":")[0], key.split(":")[1], value);
	}
	
	/* ********************************************************** *
	 * * 				Modification de Token 					* *
	 * ********************************************************** */
	/**
	 * test si ce token existe
	 * 
	 * @param cat
	 * @param token
	 * @return
	 */
	public boolean hasToken(String cat, String token) {
		return getTokenAsString(cat, token)!=null;
	}

	/**
	 * test si le token existe dans la categorie par defaut
	 * 
	 * @param token
	 * @return
	 */
	public boolean hasToken(String token) {
		return hasToken(LaConstants.DEFAULT_TOKEN_CAT, token);
	}

	/**
	 * 
	 * @param token
	 * @param value
	 */
	public void addToken(String token, String value) {
		addToken(LaConstants.DEFAULT_TOKEN_CAT, token, value);
	}

	/**
	 * ajout un token dans la categorie indiqué, avec la valeur indiqué
	 * 
	 * @param cat
	 * @param token
	 * @param value
	 */
	public synchronized void addToken(String cat, String token, String value) {
		toCommit.put(cat+":"+token, value);
		//tokens.put(cat + ":" + token, value);
		tokenChange(cat,token,value);
		commit();
		//player.getWorld().getGame().getTraces().sendSetToken("player", cat,token,value);
	}

	/**
	 * supprime un token
	 * 
	 * @param cat
	 * @param token
	 */
	public synchronized void delToken(String cat, String token) {
		toCommit.put(cat+":"+token, null);
		//tokens.remove(cat + ":" + token);
		tokenChange(cat,token,null);
		commit();
	}

	/**
	 * supprime un token dans la categorie par défaut
	 * 
	 * @param token
	 */
	public void delToken(String token) {
		delToken(LaConstants.DEFAULT_TOKEN_CAT, token);
	}

	/**
	 * Renvoie la valeur numerale du Token. 0 si il n'existe pas ou si il ne
	 * contien pas une valeur numérale.
	 * 
	 * @See Player : pour le type de valeur numérale.
	 * 
	 * @param cat
	 * @param token
	 * @return
	 */
	public float getToken(String cat, String token) {
		try {
			return Float.parseFloat(getTokenAsString(cat, token));
		} catch (NullPointerException e) {
			return 0;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * Renvoie la valeur numerale du Token. 0 si il n'existe pas ou si il ne
	 * contien pas une valeur numérale
	 * 
	 * @See Player : pour le type de valeur numérale.
	 * 
	 * @param token
	 * @return
	 */
	public float getToken(String token) {
		return getToken(LaConstants.DEFAULT_TOKEN_CAT, token);
	}

	/**
	 * Supprime tous les tokens
	 */
	public synchronized void clearAll() {
		for (String key:tokens.keySet())
			this.toCommit.put(key, null);
		commit();	
	}

	/**
	 * renvoie la chaine de ce token
	 * @param cat
	 * @param token
	 * @return
	 */
	public String getTokenAsString(String cat, String token) {
		if (toCommit.containsKey(cat+":"+token))
			return toCommit.get(cat+":"+token);
		return tokens.get(cat+":"+token);
	}
	
	/**
	 * Renvoie la valeur du token sous forme de long
	 * @param cat
	 * @param token
	 * @return
	 */
	public Long getTokenAsLong(String token) {
		return getTokenAsLong(LaConstants.DEFAULT_TOKEN_CAT, token);
	}


	/**
	 * Renvoie la valeur du token sous forme de long
	 * @param cat
	 * @param token
	 * @return
	 */
	public Long getTokenAsLong(String cat, String token) {
		try {
			return Long.parseLong(getTokenAsString(cat, token));
		} catch (NullPointerException e) {
			return 0L;
		} catch (NumberFormatException e) {
			return 0L;
		}
	}

}
