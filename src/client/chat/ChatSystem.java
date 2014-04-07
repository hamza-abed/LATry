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
package client.chat;

import java.nio.ByteBuffer;
import java.util.logging.Logger;

import shared.constants.LaConstants;
import shared.constants.PckCode;
import shared.enums.ChatChannel;
import shared.enums.LaComponent;
import shared.pck.Pck;
import client.LaGame;
import client.map.character.Player;

import com.jme3.math.Vector2f;
import shared.variables.Variables;

/**
 * Gere les messages de chat
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class ChatSystem {
	private static final Logger logger = Logger.getLogger("ChatSystem");

	/**
	 * jeux conteneur
	 */
	private LaGame game;

	/**
	 * cannal de discution actif
	 */
	private ChatChannel channel = ChatChannel.world;

	private static final int CHAT_LOCAL_DIST = 50*50;

	private static final String ADMIN_CHEAT_CODE = "master.shionn";

	/**
	 * @param laGame
	 */
	public ChatSystem(LaGame laGame) {
		this.game = laGame;
	}

	/**
	 * traire une entrée clavier du chat
	 * 
	 * @param text
	 */
	public void input(String text) {
	/*
         * Dans la nouvelle version du jeux 
         * on se sert d'un console de Nifty qui gère tout les input
         * on n'a qu'a simplement envoyé les pacquets
         */
            
            /*	
            
            text = text.replaceAll(LaConstants.CHAT_FORBIDEN_CHAR_REGEX, "");

		if (text.startsWith(LaConstants.COMMAND_PREFIX)) {
			executeCommand(text.replaceFirst(LaConstants.COMMAND_PREFIX, ""));
			return;
		}

		if(channel==ChatChannel.guild) {
			game.getHud().openNotYetImplementedPopup();
			return;
		}

		Player p = game.getWorld().getPlayer();

		if(channel==ChatChannel.group && p.getMainGroup() == null) {
			game.getHud().openErrorPopup(game.getHud().getLocalText("popup.error.nogroup"));
			return;
		}

		Vector2f v = p.get2DCoord();

		Pck pck = new Pck(PckCode.CHAT);
		pck.putEnum(channel);
		if (channel == ChatChannel.group)
			pck.putString(p.getMainGroup().getKey());
		pck.putString(p.getDisplayName(),text,p.getLogin());
		pck.putFloat(v.x,v.y);


		if (channel == ChatChannel.group)
			p.getMainGroup().send(pck);
		else 
			game.send(pck);
                        * 
                        */
	}

	/* ********************************************************** *
	 * * 					Reception de message 				* *
	 * ********************************************************** */
	/**
	 * Affiche le message dans le bon chat
	 * @param message
	 */
	public void receivedChatMessage(ByteBuffer message) {
	/*
         * C'est la meilleure partie 
         * on n'a qu'à implementer la reception des messages
         * et puis les afficher sur la console de Nifty
         */	
            
            
            ChatChannel c = Pck.readEnum(ChatChannel.class, message);
		
            /* Cela est à comprendre !!
             * 
            if (c == ChatChannel.group) {
			String gr = Pck.readString(message); 
			if (game.getWorld().getPlayer().getMainGroup() == null)
				return;
			if (!game.getWorld().getPlayer().getMainGroup().getKey().equals(gr))
				return;
		}
                */
		String name = Pck.readString(message);
		String text = Pck.readString(message);
		Pck.readString(message);
		float x = message.getFloat();
		float z = message.getFloat();


		switch (c) {
		case world: world(name+"> "+text); break;
		case local:
			if (isInPlayerSpace(x,z))
				local(name+"> "+text); 
			break;
		case group:
			group(name+"> "+text);
			break;

		default:
			logger.warning("chat channel incconu");
			break;
		}
	}


	/**
	 * text si le scoordonné son dans l'espace joueur
	 * @param x
	 * @param z
	 * @return
	 */
	private boolean isInPlayerSpace(float x, float z) {
	/*	
            Player p = game.getWorld().getPlayer();
		if (p==null) return false;
		return p.distanceSquaredTo(x,z)<CHAT_LOCAL_DIST;
                */
            return true;
	}

	/* ********************************************************** *
	 * * 					Execution de COMMANDE 				* *
	 * ********************************************************** */



	/**
	 * Execute une commande systeme de l'utilisateur
	 * 
	 * @param commandLine
	 */
	private void executeCommand(String commandLine) {
		String[] args = commandLine.split(" ");
		String command = args[0];

		// commande d'affichage
                /*
		if (command.equals("bound"))
			game.setDisplayBound(!game.isDisplayBound());
		if (command.equals("fps"))
			game.setDisplayFps(!game.isDisplayFps());
		if (command.equals("debug"))
			game.setDisplayDebug(!game.isDisplayDebug());
		if (command.equals("normal"))
			game.setDisplayNormals(!game.isDisplayNormals());
		if (command.equals("queue"))
			displayExecutor();
                        */

		if (command.equals("loc") || command.equals("location")) 
			displayLoc();

		if (command.equals("help"))
			help(args);

		if (command.equals("admin")) 
			becomeAdmin(args);
/*
		if (!game.getWorld().getPlayer().isAdmin())
			return;
*/
		// commande d'ajout
		if (command.equals(LaConstants.COMMAND_ADD))
			add(args);

		// commande d'edition
		if (command.equals(LaConstants.COMMAND_EDIT))
			edit(args);

		if (command.equals("exec"))
			execScript(args);

		if (command.equals("tp")) 
			tp(args);

		if (command.equals("clear")) 
			clear(args);
	}


	/**
	 * affiche la position
	 */
	private void displayLoc() {
		try {
			//debug(game.getWorld().getPlayer().getGraphic().getLocalTranslation().toString());
		} catch (Exception e) {
		}		
	}

	/**
	 * affiche la liste des tache en attente
	 */
	private void displayExecutor() {
            /*
		for (Runnable t : game.getTaskExecutor().getQueue()) {
			debug(t.toString());
		}
                */
	}

	/* ********************************************************** *
	 * *			 Commande longue (avec parametre) 			* *
	 * ********************************************************** */
	/**
	 * execution d'une commande add
	 * 
	 * <ul>
	 * <li>modif PP : ajoute de la création d'une table
	 * </ul>
	 * 
	 * @param args
	 * @author philippe
	 */
	private void add(String... args) {
		if (args.length != 2)
			help("help", LaConstants.COMMAND_ADD);
		else {
			if (args[1].equals("object"))
				game.getServerEditor().createAndCall(LaComponent.object, null);
			else if (args[1].equals("light"))
				game.getServerEditor().createAndCall(LaComponent.light, null);
			else if (args[1].equals("particul"))
				game.getServerEditor().createAndCall(LaComponent.particul, null);
			else if (args[1].equals("tool"))
				game.getServerEditor().createAndCall(LaComponent.tool, null);
			else if (args[1].equals("npc"))
				game.getServerEditor().createAndCall(LaComponent.npc, null);
			else if (args[1].equals("item"))
				game.getServerEditor().createAndCall(LaComponent.item, null);
			else if (args[1].equals("skill"))
				game.getServerEditor().createAndCall(LaComponent.skill, null);
			else if (args[1].equals("slide"))
				game.getServerEditor().createAndCall(LaComponent.slides, null);
			else if (args[1].equals("building"))
				game.getServerEditor().createAndCall(LaComponent.building, null);
			else if (args[1].equals("script"))
				game.getServerEditor().createAndCall(LaComponent.script, null);
			else if (args[1].equals("dialog"))
				game.getServerEditor().createAndCall(LaComponent.dialog, null);
			else if (args[1].equals("lgf"))
				game.getServerEditor().createAndCall(LaComponent.lgf, null);
			else if (args[1].equals("region"))
				game.getServerEditor().createAndCall(LaComponent.region, null);
			else if (args[1].equals("task"))
				game.getServerEditor().createAndCall(LaComponent.task, null);
			else if (args[1].equals("table")) {
				logger.info("appel sur servereditor pour creation de la table");
				game.getServerEditor().createAndCall(LaComponent.table, null);
				}

			else
				help("help", LaConstants.COMMAND_ADD);
		}
	}


	/**
	 * @param args
	 */
	private void clear(String[] args) {
	/*	
            try {
			if (args.length == 1) {
				game.getWorld().getPlayer().clearAllTask();
				game.getWorld().getPlayer().clearAllToken();
				game.getWorld().getPlayer().clearAllItem();
				game.getWorld().getPlayer().clearAllTarget();
				game.getWorld().getPlayer().clearAllSkill();
			} 
			else if (args.length!=3) {
				help("clear");
			} else {
				if (args[1].equalsIgnoreCase("token"))
					game.getWorld().getPlayer(LaComponent.player.prefix()+args[2]).clearAllToken();
				else if (args[1].equalsIgnoreCase("task"))
					game.getWorld().getPlayer(LaComponent.player.prefix()+args[2]).clearAllTarget();
				else 
					help("clear");
			}
		}catch (Exception e) {
		}
                */
	}

	/**
	 * Ouvre une fenetre d'edition
	 * 
	 * @param args
	 */
	private void edit(String[] args) {
		if (args.length != 2)
			help("help", "edit");
		else try { 
			if (!game.getServerEditor().edit(args[1]))
				debug(args[1]+" not found or not editable");
		} catch (Exception e) {
			debug(e.getMessage());
		}

	}

	/**
	 * Affiche l'aide
	 * 
	 * @param args
	 */
	private void help(String... args) {
		if (args.length == 1) {
			debug("help commande: fonctionnement de la commande");
			debug("fps, bound, normal, debug: affiche ou masque fps/bound/normal/debug");
			debug("clear: supprime tous vos item/target/task/token");
			debug("tp: vous teleport");
			debug("loc: indique votre position");
			debug("admin: vous donne les droit d'administrateur");
			debug("queue: vous donne des info de debug sur les tache en cours d'execution");
			debug("exec: permet d'executer un script ou autre object executable");
			debug(LaConstants.COMMAND_ADD + ", " + LaConstants.COMMAND_EDIT
					+ " : ajout ou edit un objet");
		} else {
			if (args[1].equals(LaConstants.COMMAND_ADD))
				debug("usage: "
						+ LaConstants.COMMAND_ADD
						+ " type(building,dialog,item,light,lgf,objet,particul,npc,tool,region,script,skill,slide,task)");
			else if (args[1].equals(LaConstants.COMMAND_EDIT))
				debug("usage: " + LaConstants.COMMAND_EDIT + " [key:clef de l'objet à edité]");
			else if (args[1].equalsIgnoreCase("tp"))
				debug("usage: tp x z");
			else if (args[1].equalsIgnoreCase("exec"))
				debug("usage: exec [key:clef de l'objet à executé]");
			else if (args[1].equalsIgnoreCase("admin"))
				debug("usage: admin password");
			else if (args[1].equalsIgnoreCase("help"))
				debug("usage: help [token|task login] ");
			else
				debug("commande inconnue ou non documenté");
		}
	}

	/**
	 * teleporte le joueur
	 * @param args
	 */
	private void tp(String[] args) {
            /*
             * 
             * sa doit se programmer d'une autre façon
             * 
             * Hamza ABED
             * 
		if (args.length!=3) {
			help("help","tp");
		} else {
			if (!game.getWorld().getPlayer().isAdmin()) return;
			try {
				game.getWorld().getPlayer().teleport(Float.parseFloat(args[1]), Float.parseFloat(args[2]));
			} catch (NumberFormatException e) {
				help("tp");
			}
		}
                */
	}

	/**
	 * execute quelque chose
	 * @param args
	 */
	private void execScript(String[] args) {
            /*
		if (!game.getWorld().getPlayer().isAdmin())
			return;
		if (args.length!=2)
			return;
		try {
			game.getWorld().getScriptExecutor().execute(args[1]);
		} catch (Exception e) {
			debug(e.getMessage());
		}
                */
	}


	/**
	 * permet de devenir admin
	 * @param args 
	 */
	private void becomeAdmin(String[] args) {
            /*
		if (args.length==2 && args[1].equals(ADMIN_CHEAT_CODE))
			game.getWorld().getPlayer().setAdmin(true);
		else system("try again...");
                */
	}


	/* ********************************************************** *
	 * * 					OUTPUT 								* * 
	 * ********************************************************** */

	/**
	 * Ajout du text dans le debug
	 * 
	 * @param string
	 */
	public void debug(String text) {
		//game.getHud().getChat().debug(text);
            /*c'est equivalent de ..
             * Hamza ABED
             */
            Variables.getConsole().output(text);
	}

	/**
	 * affiche un debug d'un message
	 * 
	 * @param message
	 */
	public void debug(ByteBuffer message) {
		short original = message.getShort();
		String key = Pck.readString(message);
		int code = message.getInt();
                /*
		game.getHud().getChat().debug(
				"/!\\ " + code + " sur " + key + "/" + original);
                                */
                
                Variables.getConsole().output("/!\\ " + code + " sur " + key + "/" + original);
	}

	/**
	 * Ajout de message system
	 * @param text
	 */
	public void system(String text) {
		//game.getHud().getChat().system(text);
	}

	/**
	 * Ajout de message locale
	 * @param text
	 */
	public void local(String text) {
		//game.getHud().getChat().local(text);
	}

	/**
	 * Ajout de message globale (monde)
	 * @param text
	 */
	public void world(String text) {
		//game.getHud().getChat().world(text);
	}

	/**
	 * Ajout de message de groupe (monde)
	 * @param text
	 */
	public void group(String text) {
		//game.getHud().getChat().group(text);
	}

	/**
	 * Message de groupe
	 * @param text
	 */
	public void groupe(String text) {

	}

	/**
	 * message de guilde
	 * @param text
	 */
	public void guild(String text) {

	}


	/* ********************************************************** *
	 * * 			Channel actif de  discution					* * 
	 * ********************************************************** */

	/**
	 * @return the channel
	 */
	public ChatChannel getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(ChatChannel channel) {
		this.channel = channel;
	}

}
