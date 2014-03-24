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

import dynamicLoad.Main;
import java.util.concurrent.TimeUnit;

import shared.constants.PckCode;
import shared.pck.Pck;



/**
 * 
 * Permet de s'assurer que le client est toujours connecter et permet de calculé la latence
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class PingPongTask implements Runnable {
	
	
	/**
	 * delay entre deux ping, si a l'enoie d'un ping il n'as pas recu la réponse 
	 * alors on indique au client qu'il est deconnecter
	 */
	private static int delay = 5000;

	/**
	 * Au dela de ce nomrbre le joueur est deco
	 */
	private static int maxPingLost = 20;
	
	/**
	 * inidique si la tache doit etre executer ou non
	 */
	private boolean enable = false;

	/**
	 * Conteneur du jeux
	 */
	private Main game;

	private Pck pck;

	private long lastPing =-1;

	private long latency;

	private int nbPingLost;

	/**
	 * @param laGame
	 */
	public PingPongTask(Main game) {
		this.game = game;
		this.pck = new Pck(PckCode.PING);
		game.getSchedulerTaskExecutor().scheduleAtFixedRate(this, delay, delay, TimeUnit.MILLISECONDS);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (!enable) return;
		if (lastPing != -1) {
			nbPingLost++;
			if (nbPingLost >= maxPingLost) {
				stop();
				game.disconnect("Timeout");
				return;
			}
		}
		lastPing = System.currentTimeMillis();
		game.send(pck);
	}
	
	/**
	 * 
	 */
	public void pong() {
		latency = System.currentTimeMillis()-lastPing;
		lastPing = -1;
		nbPingLost = 0;
	}

	/**
	 * 
	 */
	public void start() {
		enable = true;		
	}

	/**
	 * 
	 */
	public void stop() {
		enable=false;
		lastPing=-1;
		latency=-1;
	}

	/**
	 * @return the latency
	 */
	public long getLatency() {
		return latency;
	}
	

}
