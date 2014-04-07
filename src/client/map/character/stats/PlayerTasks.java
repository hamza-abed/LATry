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
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import shared.constants.LaConstants;
import shared.enums.LaComponent;
import shared.pck.Pck;
import client.LaGame;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
import client.interfaces.network.SharableReflexEditable;
import client.map.character.PlayableCharacter;

/**
 * List des taches qu'effectue un joueur
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class PlayerTasks implements SharableReflexEditable,Runnable {

	/**
	 * 
	 */
	private int versionCode = -1;

	/**
	 * 
	 */
	private TreeSet<Integer> tasks = new TreeSet<Integer>();

	/**
	 * état des task / objectif  (accompli/non, visible ou non)
	 * task fini "id"
	 * task followed "idF"
	 * objectif visible "idVid"
	 * objectif accompli "idAid";
	 */
	@Editable(type=FieldEditType.set,innerType=FieldEditType.string)
	private TreeSet<String> states = new TreeSet<String>(); 

	/**
	 * joueur à qui appartiennent ces taches
	 */
	private PlayableCharacter player = null;

	/**
	 * jeux conteneur pour envoyé les modification
	 */
	private LaGame game;

	/**
	 * 
	 */
	private boolean commiting;

	/**
	 * 
	 */
	public PlayerTasks(PlayableCharacter player) {
		this.player = player;
		this.game = player.getWorld().getGame();
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
		//pck.putInt(current == null?-1:current.getId());
		pck.putInt(tasks.size());
		for (int t : tasks) 
			pck.putInt(t);
		pck.putInt(states.size());
		for (String s : states)
			pck.putString(s);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.playerTask.prefix() + player.getLogin();
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
		TreeSet<Integer> news = new TreeSet<Integer>();
		while (c-->0) 
			news.add(message.getInt());

		c = message.getInt();
		TreeSet<String> newStates = new TreeSet<String>();
		while(c-->0)
			newStates.add(Pck.readString(message));

		tasks = news;
		states = newStates;

		refreshTask();
	}

	/**
	 * Rafraichit le contenu de sac d'object
	 */
	private void refreshTask() {
		if (!player.isPlayer()) return;

		//game.getHud().refreshTask();
	}

	// magouille Trés moche
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
	 * *					Follow des task		 				* *
	 * ********************************************************** */
	/**
	 * Rend une tache suivi
	 * @param id
	 * @param selected
	 */
	public void setFollowed(int id, boolean follow) {
		if (follow) states.add(id+"F");
		else states.remove(id+"F");
		commit();
	}

	/**
	 * Renoi la liste des tache suivi
	 * @return
	 */
	public List<Task> followeds() {
		ArrayList<Task> out = new ArrayList<Task>();
		for (String k:states) 
			if (k.matches("\\d+F"))
				out.add(getTask(Integer.parseInt(
						k.substring(0, k.length()-1))));
		return out;
	}

	/**
	 * inidque si le joueur suis la quete
	 * @param id
	 * @return
	 */
	public boolean isFollowed(int id) {
		return states.contains(id+"F");
	}


	/* ********************************************************** *
	 * *						TASK		 					* *
	 * ********************************************************** */

	/**
	 * test si le joueur possede cette tache
	 * 
	 * @param task
	 * @return
	 */
	public boolean hasTask(int task) {
		return tasks.contains(task);
	}

	/**
	 * Ajoute une tache à un joueur. Si le joueur à deja accompli la tache ca la reset
	 * 
	 * @param task
	 * @return
	 */
	public void addTask(int task) {
		//game.getTraces().sendAddTask(player,task);
		tasks.add(task);
		states.remove(Integer.toString(task));
		states.add(task+"F");
		commit();
	}



	/**
	 * 
	 * 
	 * @param task
	 * @return
	 */
	public void delTask(int task) {
		if (tasks.contains(task)) {
			//game.getTraces().sendDelTask(player, task);
			states.remove(task+"F");
			states.remove(Integer.toString(task));
			tasks.remove(task);
			commit();
		}
	}

	/**
	 * Indique que le joueur à accompli une tache
	 * @param task
	 */
	public void succesTask(int task) {
		states.add(Integer.toString(task));
		states.remove(task+"F");
		//game.getTraces().sendSuccesTask(player, getTask(task));
		commit();
	}

	/**
	 * Indique que le joueur à accompli une tache
	 * @param task
	 */
	public void unSuccesTask(int task) {
		states.remove(Integer.toString(task));
		states.add(task+"F");
		//game.getTraces().sendUnSuccesTask(player, getTask(task));
		commit();
	}

	/**
	 * Indique que le joueur à accompli une tache
	 * @param task
	 */
	public boolean hasSuccesTask(int task) {
		return states.contains(Integer.toString(task));
	}

	/* ********************************************************** *
	 * *					Objectiv	 						* *
	 * ********************************************************** */

	/**
	 * indique si un objectif est visible
	 */
	public boolean isObjectivVisible(int task, int objectiv) { 
		return states.contains(task+"V"+objectiv);
	}

	/**
	 * Affiche un objectif
	 */
	public void showObjectiv(int task, int objectiv) { 
		states.add(task+"V"+objectiv);
		//game.getTraces().sendAddObjectivTask(player, getTask(task), objectiv);
		commit();
	}

	/**
	 * Affiche un objectif
	 */
	public void hideObjectiv(int task, int objectiv) { 
		states.remove(task+"V"+objectiv);
		//game.getTraces().sendDelObjectivTask(player, getTask(task), objectiv);
		commit();
	}

	/**
	 * indique si un objectif est Accompli
	 */
	public boolean hasSuccesObjectiv(int task, int objectiv) { 
		return states.contains(task+"A"+objectiv);
	}

	/**
	 * Accompli un objectif
	 */
	public void succesObjectiv(int task, int objectiv) { 
		states.add(task+"A"+objectiv);
		//game.getTraces().sendSuccesObjectivTask(player, getTask(task), objectiv);
		commit();
	}

	/**
	 * Accompli un objectif
	 */
	public void unSuccesObjectiv(int task, int objectiv) { 
		states.remove(task+"A"+objectiv);
		//game.getTraces().sendUnSuccesObjectivTask(player, getTask(task), objectiv);
		commit();
	}

	/**
	 * renvoie la tache de 'id
	 * @param task
	 * @return
	 */
	private Task getTask(int task) {
		//return player.getWorld().getTaskBuildIfAbsent(task);
            return null;
	}

	/**
	 * supprime tous
	 */
	public void clearAllTask() {
		this.states.clear();
		this.tasks.clear();
		commit();
	}

	/**
	 * Liste les tache dujouer
	 */
	public List<Task> list() {
		ArrayList<Task> out = new ArrayList<Task>();
		for (int i:tasks)
			out.add(getTask(i));
		return out;
	}


}
