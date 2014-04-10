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
package client;

import java.util.logging.Logger;

import shared.constants.PckCode;
import shared.enums.LaComponent;
import shared.pck.Pck;
import client.map.GameData;
import client.map.character.Dialog;
import client.map.character.NonPlayableCharacter;
import client.map.character.PlayableCharacter;
import client.map.character.stats.Item;
import client.map.character.stats.Task;
import shared.variables.Variables;
/*
import client.map.object.BasicMapObject;
import client.map.object.MapTable;
import client.map.tool.evalfeather.Idea;
import client.map.tool.feather.FeatherAction;
import client.map.tool.feather.FeatherComment;
import client.map.tool.feather.FeatherIdea;
*/
/**
 * Permet d'envoyé des traces au serveur 
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class LaTraces {
	private static final Logger logger = Logger.getLogger("Traces");
	private LaGame game;

	/**
	 * @param laGame
	 */
	public LaTraces(LaGame laGame) {
		this.game = laGame;
	}

	public LaGame getGame() {
		return game;
	}

	/**
	 * Envoie une trace d'execution d'un action de NPC
	 * @param nonPlayableCharacters
	 */
	public void sendActiveNpcAction(NonPlayableCharacter npc) {
	/*	
            send("type","npc-action",
				"login",game.getWorld().getPlayer().getLogin(),
				"npc",npc.getKey(),
				"action",npc.getAction(),
				"client-time",Long.toString(System.currentTimeMillis()));
                                */
	}

	/**
	 * Envoie une trace d'execution d'un action d'Objet
	 * @param obj
	 */
        
        /*
	public void sendActiveObjectAction(BasicMapObject obj) {
		send("type","object-action",
				"login",game.getWorld().getPlayer().getLogin(),
				"object",obj.getKey(),
				"action",obj.getAction(),
				"client-time",Long.toString(System.currentTimeMillis()));
	}
        */
	/**
	 * Envoie une trace d'execution d'un action d'Objet
	 * @param obj
	 * @author philippe
	 */
        
       /*
	public void sendActiveObjectAction(MapTable obj) {
		send("type","object-action",
				"login",game.getWorld().getPlayer().getLogin(),
				"object",obj.getKey(),
				"action",obj.getAction(),
				"client-time",Long.toString(System.currentTimeMillis()));
	}
	
	*/


	/**
	 * Envoie la trace
	 * @param strs
	 */
	private void send(String... strs) {
		if (strs.length%2!=0)
			throw new IllegalArgumentException("le nombre d'element d'une trace dois être paire");
		Pck pck = new Pck(PckCode.TRACE); 
		pck.putInt(strs.length/2);
		pck.putString(strs);
		//game.send(pck);
		logger.fine("envoie de la trace "+strs[1]);
	}

	/**
	 * Envoie une trace d'activation de choix de dialog
	 * @param dialog
	 * @param choice
	 */
        
	public void sendActiveDialogChoice(Dialog dialog, String choiceAction) {
		send("type","dialog-choice-activ",
				"login", Variables.getClientConnecteur().getLogin(),
				"dialog", dialog.getKey(),
				"choice-action", choiceAction,
				"client-time",Long.toString(System.currentTimeMillis()));
	}
        

	/**
	 * Envoie une trace d'activation de choix de dialog
	 * @param dialog
	 */
        
       
	public void sendActiveDialog(Dialog dialog) {
		send("type","dialog-activ",
				"login",//game.getWorld().getPlayer().
                                Variables.getClientConnecteur().getLogin(),
				"dialog", dialog.getKey(),
				"client-time",Long.toString(System.currentTimeMillis()));
	}
       

	/*
	 * ENVOIE RELATIF AU TACHES
	 */

	/**
	 * envoie le gain d'une tache
	 * @param player
	 * @param task
	 */
	public void sendAddTask(PlayableCharacter player, int task) {
		send("type","add-task",
				"login",player.getLogin(),
				"task", LaComponent.task.prefix()+Integer.toString(task),
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * envoie la perte d'une tache
	 * @param player
	 * @param task
	 */
	public void sendDelTask(PlayableCharacter player, int task) {
		send("type","del-task",
				"login",player.getLogin(),
				"task", LaComponent.task.prefix()+Integer.toString(task),
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * envoie le succes d'une tache
	 * @param player
	 * @param t
	 */
	public void sendSuccesTask(PlayableCharacter player, Task t) {
		send("type","succes-task",
				"login",player.getLogin(),
				"task", t.getKey(),
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * envoie l'echec d'une tache
	 * @param player
	 * @param t
	 */
	public void sendUnSuccesTask(PlayableCharacter player, Task t) {
		send("type","unsucces-task",
				"login",player.getLogin(),
				"task", t.getKey(),
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * envoie l'ajout d'un objectof
	 * @param player
	 * @param task
	 * @param objectiv
	 */
	public void sendAddObjectivTask(PlayableCharacter player, Task task,
			int objectiv) {
		send("type","add-task-objectiv",
				"login",player.getLogin(),
				"task", task.getKey(),
				"objectiv", Integer.toString(objectiv),
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * envoie la suppresion d'un objectof
	 * @param player
	 * @param task
	 * @param objectiv
	 */
	public void sendDelObjectivTask(PlayableCharacter player, Task task,
			int objectiv) {
		send("type","del-task-objectiv",
				"login",player.getLogin(),
				"task", task.getKey(),
				"objectiv", Integer.toString(objectiv),
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * envoie l'ajout d'un objectof
	 * @param player
	 * @param task
	 * @param objectiv
	 */
	public void sendSuccesObjectivTask(PlayableCharacter player, Task task,
			int objectiv) {
		send("type","succes-task-objectiv",
				"login",player.getLogin(),
				"task", task.getKey(),
				"objectiv", Integer.toString(objectiv),
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * envoie l'ajout d'un objectof
	 * @param player
	 * @param task
	 * @param objectiv
	 */
	public void sendUnSuccesObjectivTask(PlayableCharacter player, Task task,
			int objectiv) {
		send("type","unsucces-task-objectiv",
				"login",player.getLogin(),
				"task", task.getKey(),
				"objectiv", Integer.toString(objectiv),
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * envoie l'information de changement de quantité du sac
	 * @param player
	 * @param item
	 */
	public void sendBagChange(PlayableCharacter player, Item item) {
		send("type","bag-change",
				"login",player.getLogin(),
				"item", item.getKey(),
				"new-quantity",Integer.toString(player.getItem(item)),
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * Envoie une reponse à une question ouverte
	 * @param question
	 * @param text
	 */
        
	public void sendOpenQuestionAnswer(String question, String answer) {
	/*	
            send("type","open-question-answer",
				"login",game.getWorld().getPlayer().getLogin(),
				"question",question,
				"answer",answer,
			"client-time",Long.toString(System.currentTimeMillis()));
                        */
	}

	/**
	 * TODO commentaire LaTraces.sendFeatherCreateIdea
	 * @param featherIdea
	 */
        /*
	public void sendFeatherCreateIdea(FeatherIdea idea,String toolId) {
		
            send("type","feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"feather-type","create-idea",
				"idea-key",idea.getKey(),
				"idea-x",Integer.toString(idea.getX()),
				"idea-y",Integer.toString(idea.getY()),
				"idea-author",idea.getAuthor(),
				"action-author",idea.getAuthor(),
				"tool-id", toolId,
				"client-time",Long.toString(System.currentTimeMillis()));
                                
	}
*/
	/**
	 * TODO commentaire LaTraces.sendFeatherIdeaPropose
	 * @param featherIdea
	 */
        /*
	public void sendFeatherIdeaPropose(FeatherIdea idea,String toolId) {
		send("type","feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"feather-type","propose-idea",
				"idea-key",idea.getKey(),
				"idea-author",idea.getAuthor(),
				"action-author",idea.getAuthor(),
				"idea-text",idea.getText(),
				"tool-id", toolId,
				"client-time",Long.toString(System.currentTimeMillis()));
	}
*/
	/**
	 * TODO commentaire LaTraces.sendFeatherIdeaPosComment
	 * @param featherIdea
	 * @param c
	 */
        /*
	public void sendFeatherIdeaPosComment(FeatherIdea idea, FeatherComment comment,String toolId) {
		send("type","feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"feather-type","positive-comment-idea",
				"idea-key",idea.getKey(),
				"action-author",comment.getAuthor(),
				"comment-text",comment.getComment(),
				"idea-author",idea.getAuthor(),
				"tool-id", toolId,
				"client-time",Long.toString(System.currentTimeMillis()));
	}
*/
	/**
	 * TODO commentaire LaTraces.sendFeatherIdeaNegComment
	 * @param featherIdea
	 * @param c
	 */
        /*
	public void sendFeatherIdeaNegComment(FeatherIdea idea, FeatherComment comment, String toolId) {
		send("type","feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"feather-type","negative-comment-idea",
				"idea-key",idea.getKey(),
				"action-author",comment.getAuthor(),
				"comment-text",comment.getComment(),
				"idea-author",idea.getAuthor(),
				"tool-id", toolId,
				"client-time",Long.toString(System.currentTimeMillis()));
	}

*/
	/**
	 * 
	 * @param action
	 * @param idea 
	 */
        /*
	public void sendFeatherAskDetail(FeatherAction action, FeatherIdea idea, String toolId) {
		send("type","feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"feather-type","ask-detail",
				"idea-key",idea.getKey(),
				"action-author",action.getAuthor(),
				"action-data",action.getData(),
				"action-data2",action.getData2(),
				"idea-author",idea.getAuthor(),
				"tool-id", toolId,
				"client-time",Long.toString(System.currentTimeMillis()));
	}
*/
	/**
	 * 
	 * @param action
	 * @param idea
	 */
        /*
	public void sendFeatherIdeaOrganize(FeatherAction action, String toolId) {
		send("type","feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"feather-type","organize-idea",
				"action-author",action.getAuthor(),
				"action-data",action.getData(),
				"action-data2",action.getData2(),
				"tool-id", toolId,
				"client-time",Long.toString(System.currentTimeMillis()));
	}
        * */

	/**
	 * indique qu'un joueur demande un vote
	 * @param action
	 */
        /*
	public void sendFeatherIdeaVote(FeatherAction action, String toolId) {
		send("type","feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"feather-type","vote-idea",
				"action-author",action.getAuthor(),
				"action-data",action.getData(),
				"action-data2",action.getData2(),
				"tool-id", toolId,
				"client-time",Long.toString(System.currentTimeMillis()));
	}
*/
	/**
	 * 
	 * @param action
	 * @param idea
	 */
        /*
	public void sendFeatherIdeaDeletion(FeatherAction action, FeatherIdea idea, String toolId) {
		send("type","feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"feather-type","delete-idea-request",
				"idea-key",idea.getKey(),
				"action-author",action.getAuthor(),
				"action-data",action.getData(),
				"action-data2",action.getData2(),
				"idea-author",idea.getAuthor(),
				"tool-id", toolId,
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * 
	 * @param action
	 * @param idea
	 */
        /*
	public void sendFeatherAnswerDetail(FeatherAction action, FeatherIdea idea, String toolId) {
		send("type","feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"feather-type","question-answering",
				"idea-key",idea.getKey(),
				"action-author",action.getAuthor(),
				"action-data",action.getData(),
				"action-data2",action.getData2(),
				"idea-author",idea.getAuthor(),
				"tool-id", toolId,
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	public void sendFeatherActionCanceled(FeatherAction action,
			String type, String toolId) {
		send("type","feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"feather-type","cancel-action",
				"cancel-type",type,
				"action-author",action.getAuthor(),
				"action-data",action.getData(),
				"action-data2",action.getData2(),
				"tool-id", toolId,
				"client-time",Long.toString(System.currentTimeMillis()));		
	}

	/**
	 * TODO commentaire LaTraces.featherIdeaToWall
	 * @param author
	 * @param featherIdea
	 */
        /*
	public void featherIdeaToWall(String author, FeatherIdea idea,String toolId) {
		send("type","feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"feather-type","submit-idea",
				"idea-key",idea.getKey(),
				"action-author",author,
				"idea-author",idea.getAuthor(),
				"tool-id", toolId,
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * TODO commentaire LaTraces.featherIdeaTextChange
	 * @param featherIdea
	 */
        /*
	public void featherIdeaChange(String changeAuthor,FeatherIdea idea, String toolId) {
		if (idea.isMoved()) 
			send("type","feather",
					"login",game.getWorld().getPlayer().getLogin(),
					"feather-type","move-idea",
					"idea-key",idea.getKey(),
					"idea-text",idea.getText(),
					"idea-x",Integer.toString(idea.getX()),
					"idea-y",Integer.toString(idea.getY()),
					"idea-state",idea.getState().toString(),
					"action-author", changeAuthor,
					"idea-author",idea.getAuthor(),
					"tool-id", toolId,
					"client-time",Long.toString(System.currentTimeMillis()));
		if (idea.isTextChange())
			send("type","feather",
					"login",game.getWorld().getPlayer().getLogin(),
					"feather-type","edit-idea",
					"idea-key",idea.getKey(),
					"idea-text",idea.getText(),
					"idea-x",Integer.toString(idea.getX()),
					"idea-y",Integer.toString(idea.getY()),
					"idea-state",idea.getState().toString(),
					"action-author", changeAuthor,
					"idea-author",idea.getAuthor(),
					"tool-id", toolId,
					"client-time",Long.toString(System.currentTimeMillis()));
		idea.setMoved(false);
		idea.setTextChange(false);
	}

	/**
	 * TODO commentaire LaTraces.featherActionCreate
	 * @param action
	 */
        
        /*
	public void featherActionCreate(FeatherAction action) {
		send("type","feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"feather-type","create-action",
				"action-type",action.getType().toString(),
				"action-key",action.getKey(),
				"action-author",action.getAuthor(),
				"action-idea",Long.toString(action.getIdea()),
				"action-data",action.getData(),
				"action-data2",action.getData2(),
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * TODO commentaire LaTraces.actionExecuted
	 * @param featherAction
	 */
        
        
        /*
	public void actionExecuted(FeatherAction action) {
		send("type","feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"feather-type","execute-action",
				"action-type",action.getType().toString(),
				"action-key",action.getKey(),
				"action-author",action.getAuthor(),
				"action-idea",Long.toString(action.getIdea()),
				"action-data",action.getData(),
				"action-data2",action.getData2(),
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * TODO commentaire LaTraces.actionCanceled
	 * @param featherAction
	 */
        
        /*
	public void actionCanceled(FeatherAction action) {
		send("type","feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"feather-type","cancel-action",
				"action-type",action.getType().toString(),
				"action-key",action.getKey(),
				"action-author",action.getAuthor(),
				"action-idea",Long.toString(action.getIdea()),
				"action-data",action.getData(),
				"action-data2",action.getData2(),
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * Envoie une trace comme quoi un joueur regarde la description d'un
	 * @param idea
	 */
        
        /*
	public void sendEvalFeathShowDescription(Idea idea) {
		send("type", "eval-feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"eval-feather-type", "show-description",
				"tool-id", idea.getToolId(),
				"idea-id", Integer.toString(idea.getId()),
				"idea-text", idea.getText(),
				"idea-author", idea.getIdeaAuthor(),
				"idea-description", idea.getDescription(),
				"idea-color", idea.getColor().toString(),
				"idea-vote", idea.getVoteCount(),
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * envoie uen trace comme quoi un joueur vote
	 * @param idea
	 * @param b positif ou non
	 */
        
        /*
	public void sendEvalFeatherDoVote(Idea idea, boolean b) {
		send("type", "eval-feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"eval-feather-type", b?"vote-positif":"vote-negatif",
						"tool-id", idea.getToolId(),
						"idea-id", Integer.toString(idea.getId()),
						"idea-text", idea.getText(),
						"idea-author", idea.getIdeaAuthor(),
						"idea-description", idea.getDescription(),
						"idea-color", idea.getColor().toString(),
						"idea-vote", idea.getVoteCount(),
						"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * Envoie une trace d'ajout de commentaire
	 * @param idea
	 * @param comment
	 */
        
        
        /*
	public void sendEvalFeatherDoComment(Idea idea, String comment) {
		send("type", "eval-feather",
				"login",game.getWorld().getPlayer().getLogin(),
				"eval-feather-type", "comment",
				"tool-id", idea.getToolId(),
				"idea-id", Integer.toString(idea.getId()),
				"idea-text", idea.getText(),
				"idea-author", idea.getIdeaAuthor(),
				"idea-description", idea.getDescription(),
				"idea-color", idea.getColor().toString(),
				"idea-vote", idea.getVoteCount(),
				"client-time",Long.toString(System.currentTimeMillis()));
	}

	/**
	 * @param type : world, group, player
	 * @param cat
	 * @param token
	 * @param value
	 */
	public void sendSetToken(String type, String cat, String token,
			String value) {
		/*send("type", "token",
				"token-type", type,
				"player", getGame().getWorld().getPlayer().getLogin(),
				"token-action", "setValue",
				"categorie", cat,
				"value", value,
				"token", token);
                                * 
                                */
	}

	/**
	 * @param type : world, group, player
	 * @param cat
	 * @param token
	 */
	public void sendDelToken(String type, String cat, String token) {
		/* send("type", "token",
				"token-type", type,
				"player", getGame().getWorld().getPlayer().getLogin(),
				"token-action", "delete",
				"categorie", cat,
				"token", token);
                                * 
                                */
	}

	/**
	 * @param url
	 * @param belbin 
	 */
	public void sendPollLastTrace(String url, String belbin) {
		
            /*if (belbin == null)
			send("type","poll-answer",
					"poll-type","end",
					"login",game.getWorld().getPlayer().getLogin(),
					"url",url);
		else
			send("type","poll-answer",
					"poll-type","end-belbin",
					"login",game.getWorld().getPlayer().getLogin(),
					"url",url);	
                                        */
	}

	public void sendPollAnswer(String idQuestion, String idAnswer, String answer, String belbin) {
	/*	
            if (belbin == null)
			send("type", "poll-answer",
					"poll-type","answer",
					"login",game.getWorld().getPlayer().getLogin(),
					"id-question", idQuestion,
					"id-answer",idAnswer,
					"answer",answer);
		else {
			if (!answer.equals("0"))
				send("type", "poll-answer",
						"poll-type","belbin",
						"login",game.getWorld().getPlayer().getLogin(),
						"id-question", idQuestion,
						"id-answer",idAnswer,
						"answer",answer);
		}
                */
	}

	/**
	 * Envoie le nouvel etat d'un game data
	 * @param gameData
	 */
	public void sendGameData(GameData gd) {
		send("type","game-data",
				"id",gd.getKey(),
				"size",Integer.toString(gd.size()),
				"datas",gd.getDatas().toString());
	}

	
}
