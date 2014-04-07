/**
 * Copyright 2010 http://learning-adventure.fr
 * Tous droits réservés
 * 
 * 
 * ----------------------------------------------------------------------------
 * Ce fichier fait partie de LA-Shared.
 *
 * LA-Shared est un logiciel libre ; vous pouvez le redistribuer ou le modifier 
 * suivant les termes de la GNU General Public License telle que publiée par
 * la Free Software Foundation ; soit la version 3 de la licence, soit 
 * (à votre gré) toute version ultérieure.
 * 
 * LA-Shared est distribué dans l'espoir qu'il sera utile, 
 * mais SANS AUCUNE GARANTIE ; pas même la garantie implicite de 
 * COMMERCIABILISABILITÉ ni d'ADÉQUATION à UN OBJECTIF PARTICULIER. 
 * Consultez la GNU General Public License pour plus de détails.
 * 
 * Vous devez avoir reçu une copie de la GNU General Public License 
 * en même temps que LA-Shared ; si ce n'est pas le cas, 
 * consultez <http://www.gnu.org/licenses>.
 * ----------------------------------------------------------------------------
 * This file is part of LA-Shared.
 *
 * LA-Shared is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LA-Shared is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with LA-Shared.  If not, see <http://www.gnu.org/licenses/>.
 * ----------------------------------------------------------------------------
 */
package shared.constants;

/**
 * Liste des constantes du serveur, utilisées par l'ensemble des composants
 * <ul>
 * <li>Identifiants des objets du jeux</li>
 * <li>Constantes de repertoire</li>
 * <li>valeurs par défaut</li>
 * </ul>
 *
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2010-2013
 */
public class LaConstants {

	/*
	 * version du jeux. evolue à chaque fois que le client et le server ne sont plus compatible
	 * 
	 * Historique 
	 * - 10 : indication dans le packet joueur si il est connecté
	 * - 11 : Integration d'un service RMI pour l'edition du serveur 
	 * - 12 : delete d'objet
	 * - 13 : character arachnie
	 * - 14 : disparision des clef pour être remplacé par des type énuméré moins lourd dans le reseau
	 * - 15 : group token !
	 * - 16 : utilisation de LaComponant lors de la creation d'objet
	 * - 17 : Ajout de l'outil "evalFeather"
	 * - 18 : Ajout d'un boolean dans le packet des objet graphiqe (walk over)
	 * - 19 : prise en compte du slideshow dans le getShared
	 * - 20 : ajout des world tokens
	 * - 21 : ajout du fog dans la map
	 * - 22 : changement du mote de pass admin
	 * - 23 : ajout des lumieres
	 * - 24 : --- censuré ---
	 * - 25 : Maintenant on peu suivre plusieur tache mais du coup ca change le packet reseau
	 * - 26 : ajout des game data. ca permet de stoicker des information structuré
	 * - 27 : Le serveur gere maintenant la teleportation
	 * - 28 : requete d'execution de script
	 * - 29 : Mise en place du ping/pong pour detecter les deconnection cliente
	 * - 30 : Mise en place d'un nouvel objet Table
	 * - 31 : mise en conformité licence GPL
	 */
	public static final int VERSION = 31;


	/* ********************************************************** *
	 * *				PREFIX DES DATA MANAGER					* *
	 * * 			DEPRECATED use enum : LaComponent			* *
	 * ********************************************************** */
	@Deprecated
	public static final String BUILDING_PREFIX = "building:";
	@Deprecated
	public static final String DIALOG_PREFIX = "dialog:";
	@Deprecated
	public static final String GROUP_PREFIX = "gr:";
	@Deprecated
	public static final String ITEM_PREFIX = "item:";
	@Deprecated
	public static final String LGF_PREFIX = "lgf:";
	@Deprecated
	public static final String MAP_PREFIX = "map:";
	@Deprecated
	public static final String NPC_PREFIX = "npc:";
	@Deprecated
	public static final String PARTICUL_PREFIX = "particul:";
	@Deprecated
	public static final String PLAYER_BAG_PREFIX = "player-bag:";
	@Deprecated
	public static final String PLAYER_PREFIX = "player:";
	@Deprecated
	public static final String PLAYER_SKILL_PREFIX = "player-skill:";
	@Deprecated
	public static final String PLAYER_TARGETS_PREFIX = "player-target:";
	@Deprecated
	public static final String PLAYER_TASK_PREFIX = "player-task:";
	@Deprecated
	public static final String PLAYER_TOKENS_PREFIX = "player-token:";
	@Deprecated
	public static final String OBJECT_PREFIX = "object:";
	@Deprecated
	public static final String TABLE_PREFIX = "table:";
	@Deprecated
	public static final String REGION_PREFIX = "region:";
	@Deprecated
	public static final String SCRIPT_PREFIX = "script:";
	@Deprecated
	public static final String SKILL_PREFIX = "skill:";
	@Deprecated
	public static final String SLIDESHOW_PREFIX = "slide:";
	@Deprecated
	public static final String TASK_PREFIX = "task:";
	@Deprecated
	public static final String TOOL_PREFIX = "tool:";
	@Deprecated
	public static final String ZONE_PREFIX = "zone:";

	public static final String BACKUP_KEY = "backup";

	public static final String STATS_KEY = "servers-stats";
	
	@Deprecated
	public static final String WORLD_KEY = "world";

	/* ********************************************************** *
	 * *	Expression reguliere correspondant aux clef 		* *
	 * *		DEPRECATED use enum : LaComponent				* *
	 * ********************************************************** */
	@Deprecated
	public static final String BUILDING_REGEX = "^building:\\d+$";	
	@Deprecated
	public static final String DIALOG_REGEX = "^dialog:\\d+$";
	@Deprecated
	public static final String GROUP_REGEX = "^gr:.+$";
	@Deprecated
	public static final String ITEM_REGEX = "^item:\\d+$";
	@Deprecated
	public static final String LGF_REGEX = "^lgf:\\d+$";
	@Deprecated
	public static final String MAP_REGEX = "^map:\\d+:\\d+$";
	@Deprecated
	public static final String NPC_REGEX = "^npc:\\d+$";
	@Deprecated
	public static final String PARTICUL_REGEX = "^particul:\\d+$";
	@Deprecated
	public static final String PLAYER_BAG_REGEX = "^player-bag:.*$";
	@Deprecated
	public static final String PLAYER_REGEX = "^player:.*$";
	@Deprecated
	public static final String PLAYER_SKILL_REGEX = "^player-skill:.*$";
	@Deprecated
	public static final String PLAYER_TARGETS_REGEX = "^player-target:.*$";
	@Deprecated
	public static final String PLAYER_TASK_REGEX = "^player-task:.*$";
	@Deprecated
	public static final String PLAYER_TOKENS_REGEX = "^player-token:.*$";
	@Deprecated
	public static final String OBJECT_REGEX = "^object:\\d+$";	
	@Deprecated
	public static final String TABLE_REGEX = "^table:\\d+$";	
	@Deprecated
	public static final String REGION_REGEX = "^region:\\d+$";
	@Deprecated
	public static final String SCRIPT_REGEX = "^script:\\d+$";
	@Deprecated
	public static final String SKILL_REGEX = "^skill:\\d+$";
	@Deprecated
	public static final String SLIDESHOW_REGEX = "^slide:\\d+$";
	@Deprecated
	public static final String TASK_REGEX = "^task:\\d+$";
	@Deprecated
	public static final String TOOL_REGEX = "^tool:\\d+$";
	@Deprecated
	public static final String ZONE_REGEX = "^zone:\\d+:\\d+$";

	/* ********************************************************** *
	 * *					LOGIN SPECIFIQUE					* *
	 * ********************************************************** */
	public static final String ADMINISTRATOR_LOGIN = "administrator";
	public static final String PROXY_TOMCAT_LOGIN = "laproxy";
	public static final String PROXY_TABLE_LOGIN = "laproxy";

	/* ************************************************************** *
	 * *					Constante du Chat						* *
	 * ************************************************************** */
	/**
	 * liste des charactere interdit dans le chat
	 */
	public static final String CHAT_FORBIDEN_CHAR_REGEX = "[\"]";
	
	/* ********************************************************** *
	 * *		CONSTANT de VALEUR des OBJECT divers				* *
	 * ********************************************************** */

	/**
	 * un chaine de caractere par defaut
	 */
	public static final String UNSET_STRING = "## UNSET ##";
	
	/**
	 * icone par défaut
	 */
	public static final String DEFAULT_ICON = "question-mark.png";
	
	public static final String DEFAULT_DIALOG = "## Dialog par Defaut ##";

	public static final String DEFAULT_DIALOG_CHOICE = UNSET_STRING;
	/**
	 * distance par defaut pour activié une action sur un pnj ou un objet d'un NPC
	 */
	public static final float DEFAULT_NPC_OR_OBJECT_ACTION_DIST = 10; // 5 metre
	
	/**
	 * URL par deaut lors de la création d'un slide
	 */
	public static final String DEFAULT_SLIDES_BASE_URL = "http://manouchian.univ-savoie.fr/~la3";
	
	
	/* ********************************************************** *
	 * *					CONSTANTE COMMANDE					* *
	 * ********************************************************** */
	
	/**
	 * prefix des commandes
	 */
	public static final String COMMAND_PREFIX = "!";

	/**
	 * Commande pour ajoute un objet
	 */
	public static final String COMMAND_ADD = "add";
	
	/**
	 * Commande pour editer un objet
	 */
	public static final String COMMAND_EDIT = "edit";
	
	/* ********************************************************** *
	 * *					CONSTANTE TOKEN						* *
	 * ********************************************************** */
	
	public static final String DEFAULT_TOKEN_CAT = "void";

	public static final String NPC_VISIBILITY_CAT_TOKEN = "npc-v";

	public static final String OBJECT_VISIBILITY_CAT_TOKEN = "obj-v";
	
	public static final String NPC_ON_HEAD_TOKEN_CAT = "npc-oh";
	
	/* ********************************************************** *
	 * *					CONSTANTE BIZARE					* *
	 * **********************************s************************ */
	public static final long WAIT_COMMITING_TIME = 1000;
	public static final int ZONE_VUE_SPACE = 2;
	
	/* ********************************************************** *
	 * *					CONSTANTE DOSSIER					* *
	 * **********************************s************************ */
	
	private static final String DIR_DATA = "data/";
	
	/**
	 * repertoire contenant les donnée des carte (hauteur et melange de texture
	 */
	public static final String DIR_MAP_DATA = DIR_DATA+"map/";
	
	/**
	 * Dossier contenenat les data des perssonage
	 */
	public static final String DIR_CHARACTER_DATA = DIR_DATA+"character/";
	
	/**
	 * repertoire contenant les donnée des carte en dot
	 */
	public static final String DIR_MAP_DOT_DATA = DIR_MAP_DATA+"dot/";
		
	/**
	 * repertoire racine des texture
	 */
	private static final String DIR_TEXTURE = DIR_DATA+"texture/";
	
	/**
	 * divers texture sans categorie
	 */
	public static final String DIR_TEXTURE_BASIC = DIR_TEXTURE+"basic/";
	
	/**
	 * texture pour les outils
	 */
	public static final String DIR_TEXTURE_TOOL = DIR_TEXTURE+"tool/";
	
	/**
	 * repertoire racine des texture de carte
	 */
	public static final String DIR_MAP_HIGH_RES_TEXTURE = DIR_TEXTURE +"high/map/";
	public static final String DIR_MAP_LOW_RES_TEXTURE = DIR_TEXTURE +"low/map/";
	
	/**
	 * repertoir conteneant les textures pour les skybox
	 */
	public static final String DIR_SKY_TEXTURE = DIR_TEXTURE +"skybox/";

	/**
	 * repertoir contenant l'enssemble des texture pour les object au format jmex
	 */
	public static final String DIR_JMEX_OBJECT_LOW_RES_TEXTURE = DIR_TEXTURE + "low/object/";
	public static final String DIR_JMEX_OBJECT_HIGH_RES_TEXTURE = DIR_TEXTURE + "high/object/";

	/**
	 * repertoir contenant l'enssemble des texture pour dot
	 */
	public static final String DIR_DOT_TEXTURE = DIR_DATA + "texture/dot/";

	/**
	 * 
	 */
	private static final String DIR_OBJECT_MODEL = DIR_DATA+"object/";

	/**
	 * repertoire contenant l'enssemble des model en dot
	 */
	public static final String DIR_DOT_OBJECT = DIR_OBJECT_MODEL+"dot/";
	
	/**
	 * repertoire contenant l'enssemble des model en jme-xml
	 */
	public static final String DIR_JMEX_OBJECT_MODEL = DIR_OBJECT_MODEL+"jmex/";
	
	/**
	 * 
	 */
	public static final String DIR_ICON = DIR_DATA + "icon/";
	
	/**
	 * Repertoire contenant les icones sous licence lgpl
	 */
	public static final String DIR_LGPL_ICON = DIR_ICON + "lgpl/";
	
	/**
	 * Repertoire contenant les icones sous licence CC-By
	 */
	public static final String DIR_CC_BY_ICON = DIR_ICON + "ccby/";

	public static final String DIR_SYSCOM_ICON = DIR_ICON + "syscom/";

	/**
	 * Repertoire contenant les son
	 */
	public static final String DIR_SOUND = DIR_DATA+"sound/";
	public static final String DIR_MUSIC = DIR_DATA+"music/";
	
	/* ********************************************************** *
	 * *					CONSTANT FICHIER					* *
	 * ********************************************************** */
	
	public static final String FILE_NULL_TOOL_OBJECT_MODEL = DIR_JMEX_OBJECT_MODEL+"box-la.jmex";
	
	
	
	/* ********************************************************** *
	 * *					CONSTANT LGF						* *
	 * ********************************************************** */


	/* ********************************************************** *
	 * *					CONSTANTES OBJECTS					* *
	 * ********************************************************** */
	
	public static final String DEFAULT_OBJECT_NAME            = "box-la.jmex";
	public static final String SCENARIZATION_CHEST_MODEL_NAME = "treasure-chest.jmex";
	public static final String SCENARIZATION_JAR_MODEL_NAME   = "jarre.jmex";
	
	
	public static final String DEFAULT_TABLE_MODEL			= "pegase-sci-fi/table-tactile.jmex";
	public static final String DEFAULT_TABLE_NAME             = "box-la.jmex";
	public static final float DEFAULT_TABLE_DX = 5 ;
	public static final float DEFAULT_TABLE_DZ = 5 ;

	/* ********************************************************** *
	 * *					CONSTANTES NPCS						* *
	 * ********************************************************** */
	public static final String DEFAULT_NPC_NAME = "Nouveau Pnj";
	public static final String DEFAULT_NPC_MODEL = "women";

	public static final String DEFAULT_OBJECT_MODEL = "box-la.jmex";

	/* ********************************************************** *
	 * *				  CONSTANTES OUTILS						* *
	 * ********************************************************** */
	public static final String FEATHER_END_SCRIPT_KEY = "end_script";

	/* ********************************************************** *
	 * *			  CONSTANTES SCENARIZATION					* *
	 * ********************************************************** */
	public static final String SCENARII_DIR = "scenarii/";
}
