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
package client.editor;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import shared.constants.PckCode;
import shared.enums.LaComponent;
import shared.pck.Pck;
import client.LaGame;
import client.interfaces.network.Sharable;
import client.interfaces.network.SharableEditable;
import client.interfaces.network.SharableReflexEditable;

import com.jme3.math.FastMath;
import shared.variables.Variables;
//import com.jme.util.GameTaskQueueManager;

/**
 * Editeur d'objet permettant la gestion (création, modification, suppression) d'un objet sur le server
 * <ul>
 * <li>Il est possible de demandé la création synchrone d'un objet mais un seul
 * à la fois</li>
 * <li>les opérations (creation/modification/suppression) se font par envoie de message</li>
 * <li>il y a deux modes d'édition d'un objet
 * <ul>
 * <li>un mode avec un model spécifique à chaque objet si l'objet est de type <b>SharableEditable</b>
 * <li>un mode par instrospection si l'objet est de type <b>SharableReflexEditable</b>
 * </ul></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka,     <b>shionn@gmail.com</b>, 2009-2011
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2013
 */
public class ServerEditor {

	private static final Logger logger = Logger.getLogger("ServerEditor");

	/**
	 * jeux conteneur (pour acceder à la connection)
	 */
	private LaGame game;

	/**
	 * liste des callback
	 */
	private HashMap<String, CreatorCallBack> callbacks = new HashMap<String, CreatorCallBack>();

	/**
	 * Constructeur de la classe
	 * @param laGame : client de jeu appelant
	 */
	public ServerEditor(LaGame laGame) {
		this.game = laGame;
	}

	/* ********************************************************** *
	 * * 						Edition							* * 
	 * ********************************************************** */

	public boolean edit(String key) {
            return false;
	//	try {
			
		/*	final Sharable s = "player".equalsIgnoreCase(key)? game.getWorld().getPlayer(): game.getWorld().getSharable(key);

			if (s==null) return false;

			if (s instanceof SharableReflexEditable){
				logger.info("serverEdidtor : lancement edition par intospection de "+s.getKey());
				launchReflexEdit((SharableReflexEditable)s);
			}
				
			else if (s instanceof SharableEditable) { 
				logger.info("serverEdidtor : lancement edition par modèle spécifique de "+s.getKey());
				launchModelEdit((SharableEditable)s);
			}
			else 
				return false;
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
                */
	}

	/**
	 * lance l'editeur par modele
	 * @param s
	 */
                /*
	private void launchModelEdit(final SharableEditable s) {
		if (!game.getWorld().isUpdate(s))
			game.updateFromServerAndWait(s, new Runnable() {
				@Override
				public void run() {
					GameTaskQueueManager.getManager().update(new Callable<Void>() {
						@Override
						public Void call() throws Exception {
							game.getHud().openHudEdit(s);
							return null;
						}
					});
				}
			});
		else 
			game.getHud().openHudEdit(s);

	}

	/**
	 * Lance l'edition d'un bject par introspection
	 * @param s 
	 */
                /*
	private void launchReflexEdit(final SharableReflexEditable s) {
		if (!game.getWorld().isUpdate(s))
			game.updateFromServerAndWait(s, new Runnable() {
				@Override
				public void run() {
					GameTaskQueueManager.getManager().update(new Callable<Void>() {
						@Override
						public Void call() throws Exception {
							game.getHud().openHudIntrospectEdit(s);
							return null;
						}
					});
				}
			});
		else 
			game.getHud().openHudIntrospectEdit(s);		
	}

	/* ********************************************************** *
	 * * 						DELETE							* * 
	 * ********************************************************** */

	/**
	 * supprime l'objet du server par envoi du packet <b>PckCode.DELETE_OBJECT</b>
	 */
                /*
                    
	public void delete(Sharable s) {
		Pck pck = new Pck(PckCode.DELETE_OBJECT);
		pck.putString(s.getKey());
		game.send(pck);
	}


	/* ********************************************************** *
	 * * 						CREATION						* * 
	 * ********************************************************** */

	/**
	 * Reception d'un message de retour de création d'un objet 
	 * (Retour de réponse du serveur)
	 * 
	 * @param message
	 */
                /*
	public void receiveCreate(ByteBuffer message) {
		
		// clef du message de réponse
		final String key = Pck.readString(message);
		
		// affichage dans la zone de débug du jeu
		game.getChatSystem().debug(key + " created");
		
		final CreatorCallBack callback = callbacks.remove(Pck.readString(message));
		if (callback!=null) {
			game.getTaskExecutor().execute(new Runnable() {
				@Override
				public void run() {
					callback.created(key);
				}
			});
		}
	}

	
	/**
	 * Création d'un objet du type mentionné par envoie d'un packet <b>PckCode.CREATE_OBJECT</b>
	 * @param type : type d'objet à créer
	 * @param callback
	 */
	@Deprecated
	public void createAndCall(String prefix, CreatorCallBack callback) {
		long time = System.currentTimeMillis();
		int rand = FastMath.nextRandomInt();
		if (callback!=null)
			this.callbacks.put(rand+"#"+time,callback);
		
		Pck pck = new Pck(PckCode.CREATE_OBJECT);
		pck.putEnum(LaComponent.typeFromPrefix(prefix));
		pck.putString(rand+"#"+time);
		//game.send(pck);
                Variables.getClientConnecteur().send(pck);
	}
	
	/**
	 * Création d'un objet du type mentionné par envoie d'un packet <b>PckCode.CREATE_OBJECT</b>
	 * @param type : type d'objet à créer
	 * @param callback
	 */
	public void createAndCall(LaComponent type, CreatorCallBack callback) {
		long time = System.currentTimeMillis();
		int rand = FastMath.nextRandomInt();
		if (callback!=null)
			this.callbacks.put(rand+"#"+time,callback);

		Pck pck = new Pck(PckCode.CREATE_OBJECT);
		pck.putEnum(type);
		pck.putString(rand+"#"+time);
		//game.send(pck);
                Variables.getClientConnecteur().send(pck);
	}

	/* ********************************************************** *
	 * * 					CALLBACK de CREATION				* * 
	 * ********************************************************** */

	public interface CreatorCallBack {
		public void created(String key);
	}

}
