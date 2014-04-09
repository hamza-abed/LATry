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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import shared.constants.LaConstants;
import shared.constants.PckCode;
import shared.enums.LaComponent;
import shared.pck.Pck;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
import client.interfaces.network.SharableReflexEditable;
import client.map.World;
import client.map.character.stats.GroupTokens;
import client.script.Script;
import client.script.ScriptableMethod;

import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import shared.variables.Variables;

/**
 * Group d'un joueur 
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class Group implements SharableReflexEditable, ClientChannelListener {
	private static final Logger logger = Logger.getLogger("Group");
	private World world;

	private String id;
	private int versionCode = -1;
	
	@Editable(type=FieldEditType.string)
	private String name = LaConstants.UNSET_STRING;
	
	private ArrayList<PlayableCharacter> players = new ArrayList<PlayableCharacter>();
	private ClientChannel channel;
	private GroupTokens tokens;
	
	/**
	 * @param id 
	 * 
	 */
	public Group(World world, String id) {
		this.world = world;
		this.id = id;
		logger.info("creation du group "+id);
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	@Override
	public void addData(Pck pck) {
		pck.putString(name);
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.group.prefix()+id;
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#getVersionCode()
	 */
	@Override
	public int getVersionCode() {
		return versionCode ;
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#receiveCommitPck(java.nio.ByteBuffer)
	 */
	@Override
	public void receiveCommitPck(ByteBuffer message) {
		this.versionCode = message.getInt();
		this.name = Pck.readString(message);
		int size = message.getInt();
		logger.fine("reception de "+id+" avec "+size+" joueurs");
		ArrayList<PlayableCharacter> news = new ArrayList<PlayableCharacter>();
		while (size-->0) {
			String login = Pck.readString(message);
			PlayableCharacter p = world.getPlayerBuildIfAbsent(LaComponent.player.prefix()+login);
			if (!players.contains(p))
				p.join(this);
			news.add(p);
		}
		
		for (PlayableCharacter p : players) 
			if (!news.contains(p))
				p.leave(this);
		
		players = news;
	}
	
	/* ********************************************************** *
	 * *					Construction de group				* *
	 * ********************************************************** */
	/**
	 * Ajout un joueur au groupe
	 * @param playableCharacter
	 */
	@ScriptableMethod(description = "ajout un joueur au groupe")
	public void addPlayer(PlayableCharacter p) {
		Pck pck = new Pck(PckCode.JOIN_GROUP);
		pck.putString(getKey(),p.getKey());
                Variables.getClientConnecteur().send(pck);
		//world.getGame().send(pck);
	}
	
	@ScriptableMethod(description = "ajout un joueur au groupe\nle parametre est le login du joueur")
	public void addPlayer(String login) {
		addPlayer(world.getPlayer(LaComponent.player.prefix()+login));
	}

	/**
	 * Ajout un joueur au groupe
	 * @param playableCharacter
	 */
	@ScriptableMethod(description = "supprime un joueur au groupe")
	public void delPlayer(PlayableCharacter p) {
		Pck pck = new Pck(PckCode.LEAVE_GROUP);
		pck.putString(getKey(),p.getKey());
		//world.getGame().send(pck);
                Variables.getClientConnecteur().send(pck);
                
	}

	@ScriptableMethod(description = "supprime un joueur au groupe\nle parametre est le login du joueur")
	public void delPlayer(String login) {
		addPlayer(world.getPlayer(LaComponent.player.prefix()+login));
	}
	
	/**
	 * @param name the name to set
	 */
	@ScriptableMethod(description="change le nom d'affichage du groupe")
	public void setName(String name) {
		this.name = name;
		//world.getGame().
                Variables.getClientConnecteur().commitOnServer(this);
	}

	/**
	 * @return le nombre de player
	 */
	@ScriptableMethod(description="Renvoi le nombre de player dans le groupe")
	public int getNbPlayer() {
		return players.size();
	}
	
	/**
	 * @param playableCharacter
	 * @return true or false
	 */
	@ScriptableMethod(description="permet de savoir si le player appartient au groupe")
	public boolean hasPlayer(PlayableCharacter p) {
		return players.contains(p);
	}
	
	/* ********************************************************** *
	 * *				Statistique du groupe					* *
	 * ********************************************************** */

	/**
	 * @param item
	 * @return true or false
	 */
	@ScriptableMethod(description="Permet de savoir si un des player du groupe posséde l'item")
	public boolean hasItem(int item) {
		for (PlayableCharacter p : players) 
			if (p.getItem(item) != 0)
				return true;
		return false;
	}
	
	/**
	 * @param cat
	 * @param token
	 * @param value
	 */
	@ScriptableMethod(description="Ajoute un token a chaque membre du groupe (token, value)")
	public void setToken(String token, String value) {
		getTokens().addToken(token, value);
	}
	
	/**
	 * @param token
	 * @return true or false
	 */
	@ScriptableMethod(description="Test si un des joueurs du groupe a le token")
	public boolean hasToken(String token) {
		return getTokens().hasToken(token);
	}
	
	@ScriptableMethod(description="Renvoie la valeur du premier token trouver d'un membre du groupe")
	public float getToken(String token) {
		return getTokens().getToken(token);
	}
	
	@ScriptableMethod(description="Supprime un token dans la categorie par defaut")
	public void delToken(String token) {
		getTokens().delToken(token);
	}
	
	/* ********************************************************** *
	 * *						SCRIPT							* *
	 * ********************************************************** */
	
	/**
	 * execute un script pour tous le groupe
	 * @param script, le script à executer.
	 */
	@ScriptableMethod(description="demande l'execution du script pour chaque membre du groupe.")
	public void executeScript(Script script) {
		executeScript(script.getId());
	}
	
	/**
	 * execute un script pour tous le groupe
	 * @param id
	 */
	@ScriptableMethod(description="demande l'execution du script pour chaque membre du groupe.")
	public void executeScript(int id) {
		Pck pck = new Pck(PckCode.EXECUTE_GROUP_SCRIPT);
		pck.putString(LaComponent.script.prefix()+id);
		send(pck);
	}
	
	/* ********************************************************** *
	 * *					GROUP LISTENER	 					* *
	 * ********************************************************** */
	
	
	/**
	 * envoie un pck au server sur le channel de group
	 * @param pck
	 */
	public void send(Pck pck) {
		try {
			channel.send(pck.toByteBuffer());
		} catch (IOException e) {
			logger.warning("IOException : Je le savais !");
		}	
	}
	
	/* (non-Javadoc)
	 * @see com.sun.sgs.client.ClientChannelListener#leftChannel(com.sun.sgs.client.ClientChannel)
	 */
	@Override
	public void leftChannel(ClientChannel arg0) {
		logger.fine("quitte le groupe channel, pas de soucis si il quitte bien le groupe");
	}

	/* (non-Javadoc)
	 * @see com.sun.sgs.client.ClientChannelListener#receivedMessage(com.sun.sgs.client.ClientChannel, java.nio.ByteBuffer)
	 */
	@Override
	public void receivedMessage(ClientChannel channel, ByteBuffer message) {
		short code = message.getShort();
		switch (code) {
		case PckCode.EXECUTE_GROUP_SCRIPT:
			world.getScriptExecutor().execute(Pck.readString(message));
			break;
		
		case PckCode.CHAT:
			//if (world.getPlayer().getMainGroup() == this)
				//world.getGame().
                    Variables.getClientConnecteur().getChatSystem().receivedChatMessage(message); 
			break;
			
		case PckCode.COMMIT:
			world.receiveCommitPck(message);
			break;
			
		default:
			logger.warning("Code packet incconu dans le group : "+code);
			break;
		}
	}
	
	/* ********************************************************** *
	 * *					GETTERS / SETTERS 					* *
	 * ********************************************************** */
	
	

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name.equalsIgnoreCase(LaConstants.UNSET_STRING)?id:name;
	}

	@ScriptableMethod(description="retourne le nom du script")
	public String getName() {
		return name;
	}

	/**
	 * channel du groupe
	 * @param channel
	 */
	public void setChannel(ClientChannel channel) {
		logger.info("affectation du channel de groupe : "+getKey());
		this.channel = channel;
		if (tokens == null) 
			this.tokens = new GroupTokens(this);
		//getWorld().getGame().
                Variables.getClientConnecteur().updateFromServer(this.tokens);
	}

	/**
	 * @return
	 */
	@ScriptableMethod(description="renvoie la liste des joueurs\njava.util.Collection<client.map.character.PlayableCharacter>")
	public Collection<PlayableCharacter> getPlayers() {
		return new ArrayList<PlayableCharacter>(players);
	}

	/**
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	public GroupTokens getTokens() {
		if (tokens == null) {
			tokens = new GroupTokens(this);
			//getWorld().getGame().
                        Variables.getClientConnecteur().updateFromServer(tokens);
		}
		return tokens;
	}



	

}
