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
import client.map.character.NonPlayableCharacter;
import client.map.character.PlayableCharacter;
import client.map.object.BasicMapObject;
import client.map.object.MapTable;
import com.jme3.math.Vector3f;
import shared.variables.Variables;
/*
import client.map.object.BasicMapObject;
import client.map.object.MapTable;

import com.jme.math.Vector3f;
*/
/**
 * liste des cibles d'un joueur 
 * <ul>
 * <li>modification pp : ajout de la table comme cible supplémentaire</li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2010-2013
 */
public class PlayerTargets implements SharableReflexEditable, Runnable {
	private int versionCode = -1;
	
	private static final Logger logger = Logger.getLogger("PlayerTargets");



	@Editable(type=FieldEditType.map,innerType=FieldEditType.vertex, keyType=FieldEditType.string)
	private HashMap<String, Vector3f> targets = new HashMap<String, Vector3f>();

	/**
	 * joueur à qui appartiennent ces tokens
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
	public PlayerTargets(PlayableCharacter player) {
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
	public void addData(Pck pck) {
		
            pck.putInt(targets.size());
		for (Entry<String, Vector3f> target : targets.entrySet()) {
			pck.putString(target.getKey());	
			pck.putFloat(target.getValue().x,target.getValue().y,target.getValue().z);
		}
               
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.playerTarget.prefix() + player.getLogin();
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
		
                HashMap<String, Vector3f> news = new HashMap<String, Vector3f>();
		for (int i = 0; i < c; i++)
			news.put(Pck.readString(message), new Vector3f(
					message.getFloat(), 
					message.getFloat(), 
					message.getFloat()));
		targets = news;
		refreshHud();
                
	}
	
	/**
	 * demande au hud de se mettre à jour
	 */
	private void refreshHud() {
		if (!player.isPlayer()) return;
                System.out.println("PlayerTargets->refreshHud() : instruction manquante!!");
		// refresh boussole pour afficher l'objectif sur la boussole
		//game.getHud().refreshBoussole();
	}

	// magouille Trés moche
	/**
	 * Commit sur le serveur mais dans un thread à part à cause des modification commune
	 */
	private void commit() {
		if (!commiting) {
			logger.fine("requete de commit des targets");
			commiting = true;
			Variables.getLaGame().getSchedulerTaskExecutor().schedule(this,LaConstants.WAIT_COMMITING_TIME,TimeUnit.MILLISECONDS);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		logger.fine("ca commit les targets");
		commiting = false;
		          Variables.getClientConnecteur().commitOnServer(this);		
	}

	/* ********************************************************** *
	 * * 				Modification de target 					* *
	 * ********************************************************** */
	/**
	 * test si le joueur à ce target
	 * 
	 * @param target
	 * @return
	 */
	public boolean hasTarget(String target) {
		return targets.containsKey(target);
           
	}

	/**
	 * 
	 * @param target
	 * @param v
	 */
	public void setTarget(String target, Vector3f v) {
		targets.put(target, v);
		commit();
	}
	
	/**
	 * @param npc
	 */
	public void setTarget(final NonPlayableCharacter npc) {
		Variables.getClientConnecteur().updateFromServerAndWait(npc,new Runnable() {
			@Override
			public void run() {
			logger.fine("Ajout d'une target de type NPC\n" + npc.getName()+" "+npc.getWorldLoc());
				setTarget(npc.getName(),npc.getWorldLoc());
			}
		});
               
	}
	
	/**
	 * @param npc
	 */
	public void delTarget(final NonPlayableCharacter npc) {
		
            //player.getWorld().getGame()
              Variables.getClientConnecteur().updateFromServerAndWait(npc,new Runnable() {
			@Override
			public void run() {
				logger.fine("Ajout d'une target de type NPC\n" + npc.getName()+" "+npc.getWorldLoc());
				delTarget(npc.getName());
			}
		});
                
	}
	
	/**
	 * @param name
	 * @param obj
	 */
     
	public void setTarget(final String name, final BasicMapObject obj) {
		Variables.getClientConnecteur().updateFromServerAndWait(obj,new Runnable() {
			@Override
			public void run() {
				setTarget(name,obj.getWorldLoc());
			}
		});
	}
	
	/**
	 * @param name
	 * @param obj
	 */
        
	public void setTarget(final String name, final MapTable obj) {
		//player.getWorld().getGame()
          Variables.getClientConnecteur().updateFromServerAndWait(obj,new Runnable() {
			@Override
			public void run() {
				setTarget(name,obj.getWorldLoc());
			}
		});
	}
        

	/**
	 * supprime un target
	 * 
	 * @param target
	 */
	public void delTarget(String target) {
		targets.remove(target);
		commit();
	}

	/**
	 * supprime tous les target du joueur
	 */
	public void clear() {
		targets.clear();
		commit();
	}
	
	/**
	 * @return
	 */
	public HashMap<String, Vector3f> getAll() {
		return new HashMap<String, Vector3f>(targets);
            //return null;
	}

	





	
}
