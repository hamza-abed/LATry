package shared.constants;

/**
 * Copyright 2010 http://learning-adventure.fr
 * Tous droits rÃ©servÃ©s
 * 
 * 
 * ----------------------------------------------------------------------------
 * Ce fichier fait partie de LA-Shared.
 *
 * LA-Shared est un logiciel libre ; vous pouvez le redistribuer ou le modifier 
 * suivant les termes de la GNU General Public License telle que publiÃ©e par
 * la Free Software Foundation ; soit la version 3 de la licence, soit 
 * (Ã  votre grÃ©) toute version ultÃ©rieure.
 * 
 * LA-Shared est distribuÃ© dans l'espoir qu'il sera utile, 
 * mais SANS AUCUNE GARANTIE ; pas mÃªme la garantie implicite de 
 * COMMERCIABILISABILITÃ‰ ni d'ADÃ‰QUATION Ã  UN OBJECTIF PARTICULIER. 
 * Consultez la GNU General Public License pour plus de dÃ©tails.
 * 
 * Vous devez avoir reÃ§u une copie de la GNU General Public License 
 * en mÃªme temps que LA-Shared ; si ce n'est pas le cas, 
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




/**
 * Regroupe l'enssemble des packets du jeux.<br>Pour les objets de 
 * donnÃ©e, chaque objet est identifiÃ© par une clef, (par exemple map:3:2
 * ou zone:23:4 ou player:72)<br>Suite Ã  cette clef  
 * 
 * Chaque type d'object Ã  sa propre structure :<br><br>
 * 
 * Joueur
 * <ul>
 * <li>str: identifiant du joueur</li>
 * <li>int: version code (sert a savoir si il y a eu des changements)</li>
 * <li>str: nom d'affichage du joueur</li>
 * <li>str: groups (separÃ© par une ",")</li>
 * <li>bool: admin</li>
 * <li>bool: connected /!\ envoyer uniquement par le server pas par le client</li>
 * <li>enum: modelType</li>
 * <li>int: hairCut</li>
 * <li>float: x,z: coordonnÃ©e courante</li>
 * <li>float: skinRGB + Amb</li>
 * <li>float: colorTopClothRGB + Amb</li>
 * <li>float: colorBottomClothRGB + Amb</li>
 * <li>float: colorHairRGB + Amb</li>
 * <li>float: colorShoesRGB + Amb</li>
 * </ul>
 * 
 * Group
 * <ul>
 * <li>str: identifiant du groupe</li>
 * <li>int: version code </li>
 * <li>str: label du groupe</li>
 * La liste des joueur n'est envoyÃ© que dans le sens server>client
 * <li>int: nb de joueur
 * <ul>
 * <li>str: login joueur</li>
 * </ul></li>
 * </ul>
 * 
 * Token (joueur) Cas exeptionnale pour ce packet. Le joueur
 * n'envoie que les modification. le serveur fait le merge et renvoie Ã  tous. 
 * <ul>
 * <li>str: identifiant du sac Ã  token</li>
 * <li>int: version code (sert a savoir si il y a eu des changements)</li>
 * <li>int: nombre de token
 * <ul><li>str: nom du token</li>
 * <li>str: valeur du token</li></ul></li>
 * </ul>
 * 
 * PlayerItem : 
 * <ul>
 * <li>str: identifiant du sac du joueur</li>
 * <li>int: versioncode</li>
 * <li>int: nombre d'objet
 * <ul>
 * <li>int: identifiant de l'item (ici exeption Ã  la regle, on ne stock pas la clef de l'objet mais seulement l'identifiant numerique pour rÃ©duire la taille du packet)</li>
 * <li>int: quantitÃ©</li>
 * </ul>
 * </ul> 
 * 
 * PlayerSkill : 
 * <ul>
 * <li>str: identifiant du livre de competence du joueur</li>
 * <li>int: versioncode</li>
 * <li>int: nombre de skill
 * <ul>
 * <li>int: identifiant du skill (ici exeption Ã  la regle, on ne stock pas la clef de l'objet mais seulement l'identifiant numerique pour rÃ©duire la taille du packet)</li>
 * <li>str: value</li>
 * </ul>
 * </ul> 
 * 
 * Object ou Building: 
 * <ul>
 * <li>str <i>object:\d+</i>: identifiant de l'objet</li>
 * <li>int: version code (sert a savoir si il y a eu des changements)</li>
 * <li>str: model</li>
 * <li>str: action, script</li>
 * <li>boolean: collidable</li>
 * <li>float: x,y,z,rx,ry,rz,s: position/orientation/echelle</li>
 * <li>float: distance d'action</li>
 * </ul>
 * 
 * Particul Engine
 * <ul>
 * <li>str: identifiant de l'objet</li>
 * <li>int: version code</li>
 * <li>enum: emitType</li>
 * <li>float: x,y,z</li>
 * <li>int: factorynumber,particulPerSecond</li>
 * <li>float: emitWidth,emitHeight,emitInnerRadius,emitOutterRadius</li>
 * <li>float: emitDirection[x,y,z]</li>
 * <li>float: minimumAngle,maximumAngle,minimumLifeTime,maximumLiftTime</li>
 * <li>float: initialVelocity,speed, particlesPerSecondVariante</li>
 * <li>float: startMass, startSize, startColor[r,g,b,a]</li>
 * <li>float: endMass, endSize, endColor[r,g,b,a]</li>
 * <li>boolean: controlFlow, cameraFacing</li>
 * </ul>
 * 
 * Tool : 
 * <ul>
 * <li>str <i>tool:\d+</i>: identifiant de l'object</li>
 * <li>int: version code</li>
 * <li>float: x,y,z,rx,ry,rz: position/orientation</li>
 * <li>enum: type</li>
 * </ul>
 * 
 * NPC : 
 * <ul>
 * <li>str: identifiant de l'objet</li>
 * <li>int: versionCode</li>
 * <li>str: nom d'affichage du PNJ</li>
 * <li>str: script action</li>
 * <li>enum: modelType</li>
 * <li>float: x,y,z ,ry: coordonnÃ©e courante</li>
 * <li>float: actionDist</li>
 * <li>float: skinRGB + Amb</li>
 * <li>float: colorTopClothRGB + Amb</li>
 * <li>float: colorBottomClothRGB + Amb</li>
 * <li>float: colorHairRGB + Amb</li>
 * <li>float: colorShoesRGB + Amb</li>
 * </ul>
 *
 * Dialog : 
 * <ul>
 * <li>str: identifiant de l'objet</li>
 * <li>int: versionCode</li>
 * <li>str: text</li>
 * <li>int: nbChoix<ul>
 * <li>str: label</li>
 * <li>str: action</li>
 * </ul></li>
 * </ul>
 *
 * Item : 
 * <ul>
 * <li>str: identifiant de l'item</li>
 * <li>int: versionCode</li>
 * <li>str: nom de l'item</li>
 * <li>str: icone de l'item</li>
 * <li>str: action</li>
 * <li>int: nombre maximal possedable</li>
 * </ul>
 * 
 * MAP :
 * <ul>
 * <li>str: identifiant de la carte</li>
 * <li>int: versioncode</li>
 * <li>str: fichier d'altitude</li>
 * <li>str: on enter</li>
 * <li>str: ciel</li>
 * <li>int: (n) nombre de couche de texture</li>
 * <li>str * n: les textures</li>
 * <li>str * (n-1): les alphas</li>
 * </ul> 
 * 
 * Region :
 * <ul>
 * <li>str identifiant de la region</li>
 * <li>int: versioncode</li>
 * <li>float: x,z,w,h</li>
 * <li>str : canEnter, onEnter, onFailEnter</li>
 * <li>str : canLeave, onLeave, onFailLeave</li>
 * </ul>
 * 
 * LGF : 
 * <ul>
 * <li>str: identifiant de la carte</li>
 * <li>int: versioncode</li>
 * <li>enum: LgfPlatform</li>
 * <li>str: composant</li>
 * <li>int: nombre de parametres
 * <ul>
 * <li>str: param name</li>
 * <li>str: param value</li>
 * </ul></li>
 * <li>int: nombre d'event
 * <ul>
 * <li>str: event name</li>
 * <li>str: script</li>
 * </ul></li>
 * </ul>
 * 
 * Skill : 
 * <ul>
 * <li>str: identifiant du skill</str>
 * <li>int: versionCode</li>
 * <li>str: categorie</li>
 * <li>str: name</li>
 * </ul>
 * 
 * Slides : 
 * <ul>
 * <li>str: identifiant des slides</str>
 * <li>int: versionCode</li>
 * <li>str: url d'enregistrement des slides</li>
 * <li>int: nombre de slide</li>
 * <li>str: title</li>
 * <li>str: endScript</li>
 * </ul>
 * 
 * Task :
 * <ul>
 * <li>str: identifiant de la tache</str>
 * <li>int: versionCode</li>
 * <li>str: name</li>
 * <li>str: description</li>
 * <li>int: count
 * <ul>
 * <li>str: name</li>
 * <li>int: deep</li>
 * </ul>/li>
 * </ul>
 * 
 * Player-Task
 * <ul>
 * <li>str: identifiant de la liste tache-joueur</str>
 * <li>int: versionCode</li>
 * <li>int: current</li>
 * <li>int: task-count
 * <ul>
 * <li>int: task-id</li>
 * </ul></li>
 * <li>int: state-count
 * <ul>
 * <li>str: state</li>
 * </ul></li>
 * </ul>
 *  
 * Zone
 * <ul>
 * <li>str: identifiant de la zone</li>
 * <li>int: version code (sert a savoir si il y a eu des changements)</li>
 * </ul>
 *  
 *  
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2013
 */
public class PckCode {

	/* ********************************************************** *
	 * * 						DATA							* *
	 * ********************************************************** */
	/**
	 * Requete de demande de mise Ã  d'un objet, carte, zone ect...
	 * Client  > Server
	 * <ul>
	 * <li>str : Identifiant de l'objet</li>
	 * <li>int : version code</li>
	 * </ul>
	 */
	public static final short UPDATE = 100;

	/**
	 * Envoie de donnÃ© d'un objet
	 * Client <> Server (le server envoie direct au client ou sur channel)
	 * <ul>
	 * <li>str : Identifiant de l'objet</li>
	 * <li>int : version code</li>
	 * <li>La structure (fonction du type, regarder la doc de la classe)</li>
	 * </ul>
	 */
	public static final short COMMIT = 101;


	/**
	 * Le serveur notifi un client qu'un objet possede un nouveau 
	 * objet en fils. Peu aussi Ãªtre envoyÃ© par le client si il
	 * ajout un objet (comme un post-it sur un mur)<br><br>
	 * 
	 * Client <> Server sur le channel en cours d'execution ou 
	 * en direct si c'est en rÃ©ponse Ã  un request<br>
	 * 
	 * <ul>
	 * <li>str: Identifiant de l'objet racine</li>
	 * <li>int: nombre d'objet</li>
	 * <li>str: Identifiant de l'objet Ã  l'interieur</li>
	 * <li>int: version code (sert a savoir si il y a eu des changements)</li>
	 * </ul>
	 */
	public static final short ADD_OBJECT = 102;

	/**
	 * Idem Ã  ADD_OBJECT mais dans l'autre sens
	 */
	public static final short DEL_OBJECT = 103;

	/**
	 * Demande du client de creer un nouvelle objet
	 * la clef de l'objet sera new genre 
	 * MapObjecversionCodet:new ou Tool:new<br>
	 * Le serveur repondra par un add et/ou un create</br>
	 * 
	 * Si l'objet est geolocalisÃ© il sera creer juste 
	 * devant le joueur
	 * <ul>
	 * <li>enum : LaComponant type de l'objet Ã  creer.</li>
	 * <li>str : clef du callback</li>
	 * <li>float : x,z (option dans le cas de l'editor)</li>
	 * </ul>
	 */
	public static final short CREATE_OBJECT = 104;	


	/**
	 * Transmition d'un donnÃ©e etendu (au nombre inconnu) comme
	 * les data d'un outil<br>
	 * 
	 * Pour un outil : Si la valeur <i>## DELETED ##</i> alors c'est une 
	 * suppression de clef.
	 * <ul>
	 * <li>str : identifiant de l'outil</li>
	 * <li>int : nombre de clef changeante</li>
	 * <li>str : key</li>
	 * <li>str : value</li>
	 * </ul>
	 * 
	 */
	public static final short EXTENDED_DATA = 105;

	
	/**
	 * Code packet indiquant Ã  l'utilisateur que ca demande de notification 
	 * ne servais Ã  rien, son objet est Ã  jour
	 */
	public static final short UP_TO_DATE_OBJECT = 106;


	/**
	 * demande de suppression d'un objet
	 */
	public static final short DELETE_OBJECT = 107;


	/**
	 * Reponse du server au packet REQUEST sur le monde
	 * Client <  Server
	 * <ul>
	 * <li>int : code version du monde</li>
	 * <li>int : worldSizeX,worldSizeZ</li>
	 * <li>float : worldScaleY, worldWaterDeep</li>
	 * <li>float : mapSize,zoneSize</li>
	 * <li>str : ftpUrl,ftpFolder,ftpUser,ftpPass</li>
	 * </ul>
	 */
	public static final short WORLD_DATA = 111;

	/**
	 * erreur renvoyÃ© par le serveur lors qu'un packet n'est pas 
	 * compris.
	 * 
	 * <ul>
	 * <li>short: code packet initial</li>
	 * <li>str: clef objet initial</li>
	 * <li>int: code erreur
	 * <ul>
	 * <li>404 : object not fount</li>
	 * <li>405 : type d'object inconnu</li>
	 * <li>500 : erreur inconnu</li>
	 * <li>...</li>
	 * </ul></li>
	 * </ul>
	 */
	public static final short ERROR_DATA = 150;

	/* ********************************************************** *
	 * * 					Constant Packet DATA				* *
	 * ********************************************************** */

	/**
	 * Quand un packet contient un nombre de donnÃ©e variable c'est cette
	 * variable qui permet de definir le nombre max quil y en a par packet 
	 */
	public static final int PCK_SPLIT= 50;

	/**
	 * valeur renvoyer lors de la suppressetion d'un clef dans les outils
	 */
	public static final String TOOL_DELETED_KEY_VALUE = "<i>## DELETED ##</i>";

	/* ********************************************************** *
	 * *				PLAYER ET DEPLACEMENT					* *
	 * ********************************************************** */
	/**
	 * Un joueur change de zone
	 * Client <  Server  (sur channel ou direct)
	 * <ul>
	 * <li>str: identifiant du joeur</li>
	 * <li>str: identifiant de l'ancienne zone</li>
	 * <li>str: identifiant de la nouvelle zone</li>
	 * </ul></li>
	 * </ul>
	 */
	public static final short ZONE_PLAYER_CHANGE = 200;

	/**
	 * deconnection d'un joueur
	 * <ul>
	 * <li>str: identifiant du joueur</li>
	 * </ul>
	 */
	public static final short PLAYER_DISCONNECT = 201;

	/**
	 * EnvoyÃ© par le client ou le serveur suivant le cas
	 * <ul>
	 * <li>str : identifiant</li>
	 * <li>float x,z: coordonnÃ©e actuel</li>
	 * <li>enum : MovingType</li>
	 * <li>float x,z: coordonnÃ©e de destination (dans le cas d'un deplacement par target)</li>
	 * <li>float a: angle de dircetion (dans le cas d'un deplacement par direction)</li>
	 * <li>boolean: run ou walk</li>
	 * </ul>
	 */
	public static final short PLAYER_START_MOVE = 202;

	/**
	 * EnvoyÃ© par le client quand il a fini son deplacement
	 * <ul>
	 * <li>str : identifiant</li>
	 * <li>float x,z: coordonnÃ©e d'arret</li>
	 * </ul>
	 */
	public static final short PLAYER_END_MOVE = 203;
	
	
	/**
	 * Message de chat envoyÃ© par un joueur
	 * <ul>
	 * <li>enum : ChatChannel</li>
	 * <li>str : nom du group si discussion de group</li>
	 * <li>str : nom du joueur</li>
	 * <li>str : message</li>
	 * </ul>
	 */
	public static final short CHAT = 204;
	
	/**
	 * Le joueur demande une mise Ã  jour des zone
	 */
	public static final short PLAYER_REQUEST_ZONE_UPDATE = 205;
	
	/**
	 * le joueur envoie une trace
	 * <ul>
	 * <li>int : nombre clef/valeur
	 * <ul>
	 * <li>str*2 : clef/valeur</li>
	 * </ul></li>
	 * </ul>
	 */
	public static final short TRACE = 206;
	
	/**
	 * demande d'ajout de joueur Ã  un group
	 * <ul>
	 * <li>str: group key</li>
	 * <li>str: player key</li>
	 * </ul>
	 */
	public static final short JOIN_GROUP = 207;

	/**
	 * demande de suppression d'un joueur d'un group
	 */
	public static final short LEAVE_GROUP = 208;
	
	/**
	 * teleport le joueur
	 * <ul>
	 * <li>str : identifiant</li>
	 * <li>float x,z: coordonnÃ©e d'arret</li>
	 * </ul>
	 */
	public static final short PLAYER_TELEPORT = 209;
	
	/**
	 * packet envoyer par le client qui doit etre repondu par un
	 * un autre packet ping de la par du serveur. 
	 * 
	 * Permet de calculer la latence et de tester si le client est toujours connecter
	 */
	public static final short PING = 210;

	
	
	/**
	 * EnvoyÃ© par le serveur 
	 * <ul>
	 * <li>str : identifiant</li>
	 * <li>float x,z: coordonnÃ©e d'arret</li>
	 * </ul>
	 */
	public static final short PLAYER_IN_TABLE = 211;
	
	

	/* ********************************************************** *
	 * *				PACKET UN PEU SPECIAL (500)				* *
	 * ********************************************************** */

	/**
	 * demande au client d'executer un script
	 * <ul>
	 * <li>str : script</li>
	 * <li>int : nombre de parametre
	 * <ul>
	 * <li>str : nom du param</li>
	 * <li>str : valeur</li>
	 * </ul></li>
	 * </ul>
	 */
	public static final short EXECUTE_SCRIPT = 500;
	
	/**
	 * le client enregistre sur le serveur que les event lgf sur
	 * cette instance doivent revenir vers lui
	 * <ul>
	 * <li>str : instance</li>
	 * </ul>
	 */
	public static final short LGF_REGISTER_EVENT = 501;


	/**
	 * Retour du serveur
	 * <ul>
	 * <li>str : event</li>
	 * </ul>
	 */
	public static final short LGF_NOTIFY_EVENT = 502;

	/**
	 * le client desenregistre sur le serveur que les event lgf sur
	 * cette instance doivent revenir vers lui
	 * <ul>
	 * <li>str : instance</li>
	 * </ul>
	 */
	public static final short LGF_UNREGISTER_EVENT = 503;
	
	/**
	 * demande au groupe d'executÃ© un script.
	 * <ul>
	 * <li>str : script key</li>
	 * </ul>
	 */
	public static final short EXECUTE_GROUP_SCRIPT = 504;
	

	/* ********************************************************** *
	 * *		Valeure speciales pour l'editeur				* *
	 * ********************************************************** */

	/**
	 * envoyÃ© pour demandÃ© la creation d'u compte
	 */
	public static final short EDITOR_CREATE_ACCOUNT = 2201;





	


}
