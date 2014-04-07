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
 * Livre de competence du joueur
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class PlayerSkills implements SharableReflexEditable,Runnable {

	/**
	 * 
	 */
	private int versionCode = -1;

	/**
	 * 
	 */
	private HashMap<Skill, String> skills = new HashMap<Skill, String>();

	/**
	 * joueur à qui appartiennent ce sac
	 */
	private PlayableCharacter player = null;

	/**
	 * jeux conteneur pour envoyé les modification
	 */
	private LaGame game;

	/**
	 * liste des competence du joueur
	 */
	private boolean commiting;

	/**
	 * 
	 */
	public PlayerSkills(PlayableCharacter player) {
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
		pck.putInt(skills.size());
		for (Entry<Skill, String> skill : skills.entrySet()) {
			pck.putInt(skill.getKey().getId());
			pck.putString(skill.getValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.playerSkill.prefix() + player.getLogin();
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
		skills.clear();
		while (c-->0) {
			int skillId = message.getInt();
			String str = Pck.readString(message);
			//Skill skill = player.getWorld().getSkillBuildIfAbsent(skillId);
			//skills.put(skill, str);
		}
		//player.getWorld().getGame().getHud().getSkillBook().requestUpdate();
	}


	/* ********************************************************** *
	 * *			TEST / LECTURE des competences 				* *
	 * ********************************************************** */
	/**
	 * test si le joueur possede cet objet
	 * 
	 * @param skill
	 * @return
	 */
	public boolean hasSkill(Skill skill) {
		return skills.containsKey(skill);
	}

	/**
	 * Renvoie la quantité d'un objet du joueur. null si il n'en a pas
	 * @return
	 */
	public String getSkill(Skill skill) {
		return skills.get(skill);
	}

	/**
	 * Renvoie la valeur du skill
	 * @param skill
	 * @return
	 */
	public String getSkill(int skill) {
		//return getSkill(player.getWorld().getSkillBuildIfAbsent(skill));
            return null;
	}

	/**
	 * Renvoie la valeur du skill
	 * @param skill
	 * @return
	 */
	public float getSkillAsFloat(int skill) {
		try {
		//	return Float.parseFloat(getSkill(player.getWorld().getSkillBuildIfAbsent(skill)));
		} catch (Exception e) {}
		return 0f;
	}

	/* ********************************************************** *
	 * * 					Ajout de skill 						* *
	 * ********************************************************** */

	/**
	 * supprime tous les objet
	 */
	public void clearAll() {
		skills.clear();
		commit();
	}

	/**
	 * Renvoie une copie de l'enssemble des skill
	 * @return 
	 */
	public HashMap<Skill, String> getAllSkills() {
		return new HashMap<Skill,String>(skills);
	}

	/**
	 * change la valeur de la skill, l'ajout si il ne l'a pas
	 * @param id
	 * @param value
	 */
	public void setValue(int id, String value) {
		//Skill skill = player.getWorld().getSkillBuildIfAbsent(id);
		//this.skills.put(skill, value);
		commit();
	}

}
