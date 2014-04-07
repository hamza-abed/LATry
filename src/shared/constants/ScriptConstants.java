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

import java.util.regex.Pattern;

import shared.enums.LaComponent;

/**
 * Constante pour l'execution des fichiers de scripts 
 * <ul>
 * <li>ajout de la table tactile</li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2013
 */
public class ScriptConstants {

	/**
	 * Langage du moteur de script
	 */
	public static final String SCRIPT_LANGUAGE = "ECMAScript";

	/* ****************************************************************** *
	 * *				CLEF DE CERTAIN OBJET CONSTANT					* * 		
	 * ****************************************************************** */
	/**
	 * mot clef designant le joueur courant
	 */
	public static final String PLAYER_KEYWORD = "player";

	/**
	 * mot clef designant l'interface du joueur courant
	 */
	public static final String HUD_KEYWORD = "hud";

	/**
	 * mot clef designant le gestionnaire de son
	 */
	public static final String SOUND_KEYWORD = "sound";

	/**
	 * mot clef designant le monde
	 */
	public static final String WORLD_KEYWORD = "world";

	/**
	 * Liste des mot-clefdu moteur de script
	 */
	public static final String[] KEYWORDS = {
		PLAYER_KEYWORD,
		HUD_KEYWORD,
		SOUND_KEYWORD,
		WORLD_KEYWORD
	};
	
	/* ****************************************************************** *
	 * *				EXPRESSION DES OBJECT SCRIPT					* * 		
	 * ****************************************************************** */

	public static final String OBJECT_REGEX = "("+LaComponent.object.prefix().replace(":", ")")+"(\\d+)";
	
	public static final String BUILDING_REGEX = "("+LaComponent.building.prefix().replace(":", ")")+"(\\d+)";

	public static final String ITEM_REGEX = "("+LaComponent.item.prefix().replace(":", ")")+"(\\d+)";

	public static final String NPC_REGEX = "("+LaComponent.npc.prefix().replace(":", ")")+"(\\d+)";

	public static final String LGF_REGEX = "("+LaComponent.lgf.prefix().replace(":", ")")+"(\\d+)";

	public static final String TOOL_REGEX = "("+LaComponent.tool.prefix().replace(":", ")")+"(\\d+)";

	public static final String SLIDE_REGEX = "("+LaComponent.slides.prefix().replace(":", ")")+"(\\d+)";

	public static final String SCRIPT_REGEX = "("+LaComponent.script.prefix().replace(":", ")")+"(\\d+)";

	public static final String GROUP_REGEX = "("+LaComponent.group.prefix().replace(":", ")")+"([^\\.]*)";

	public static final String PLAYER_REGEX =  "("+LaComponent.player.prefix().replace(":", ")")+"(\\d+)";

	/**
	 *@author philippe
	 */
	public static final String TABLE_REGEX =  "("+LaComponent.table.prefix().replace(":", ")")+"(\\d+)";

	
	public static final String[] REGEXS = {
		OBJECT_REGEX,
		BUILDING_REGEX,
		ITEM_REGEX,
		NPC_REGEX,
		PLAYER_REGEX,
		LGF_REGEX,
		GROUP_REGEX,
		SLIDE_REGEX,
		SCRIPT_REGEX,
		TOOL_REGEX,
		TABLE_REGEX
	};
	
	public static final Pattern[] PATTERNS = {
		Pattern.compile(OBJECT_REGEX),
		Pattern.compile(NPC_REGEX),
		Pattern.compile(ITEM_REGEX),
		Pattern.compile(LGF_REGEX),
		Pattern.compile(PLAYER_REGEX),
		Pattern.compile(SCRIPT_REGEX),
		Pattern.compile(GROUP_REGEX),
		Pattern.compile(SLIDE_REGEX),
		Pattern.compile(TOOL_REGEX),
		Pattern.compile(TABLE_REGEX)
	};
	
	

	
	/* ****************************************************************** *
	 * *					SCRIPT SIMPLIST								* * 		
	 * ****************************************************************** */

	/**
	 * Clef d'un script renvoyant toujours vrai
	 */
	public static final String TRUE_SCRIPT = "true";

	/**
	 * Clef d'un script renvoyant toujours faux
	 */
	public static final String FALSE_SCRIPT = "false";
	
	/**
	 * Clef d'un script ne fesant jamais rien
	 */
	public static final String VOID_SCRIPT = "void";
	
	
	
}
