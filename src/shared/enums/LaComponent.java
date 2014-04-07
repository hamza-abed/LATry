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
package shared.enums;
 


/**
 * Liste les divers composants du jeux. 
 * <ul>
 * <li>ajout de la table (pp)</li>
 * <li></li>
 * </ul>
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2013
 */
public enum LaComponent {

	table("table:","^table:\\d+$"),
	building("building:","^building:\\d+$"),
	dialog("dialog:","^dialog:\\d+$"),
	gamedata("gamedata:","^gamedata:\\d+$"),
	group("gr:","^gr:.+$"),
	groupToken("gr-tok:","^gr-tok:.+$"),
	item("item:","^item:\\d+$"), 
	lgf("lgf:","^lgf:\\d+$"), 
	light("light:","^light:\\d+$"),
	map("map:","^map:\\d+:\\d+$"), 
	npc("npc:","^npc:\\d+$"), 
	particul("particul:","^particul:\\d+$"), 
	player("player:","^player:.*$"), 
	playerBag("player-bag:","^player-bag:.*$"), 
	playerSkill("player-skill:","^player-skill:.*$"), 
	playerTarget("player-target:","^player-target:.*$"), 
	playerTask("player-task:","^player-task:.*$"), 
	playerToken("player-token:","^player-token:.*$"),
	object("object:","^object:\\d+$"), 
	region("region:","^region:\\d+$"), 
	script("script:","^script:\\d+$"), 
	skill("skill:","^skill:\\d+$"), 
	slides("slide:","^slide:\\d+$"), 
	task("task:","^task:\\d+$"), 
	tool("tool:","^tool:\\d+$"), 
	world("world","^world$"), 
	worldToken("world-tok","^world-tok$"),
	zone("zone:","^zone:\\d+:\\d+$"),
	
	pedaScenario("peda-scenario:","^peda-scenario:\\d+$"),
	pedaBut("peda-but:","^peda-but:\\d+:\\d+$"),
	pedaMoyen("peda-moyen:","^peda-moyen:\\d+:\\d+$"),
	pedaCondition("peda-condition:","^peda-condition:\\d+:\\d+$"),
	pedaAction("peda-action:","^peda-action:\\d+:\\d+$"),
	
	NULL("NULL","NULL");
	
	private String prefix;
	private String regex;

	/**
	 * 
	 */
	private LaComponent(String prefix, String regex) {
		this.prefix = prefix;
		this.regex = regex;
	}
	
	/**
	 * Renvoie le type en fonction de la clef
	 * @param key
	 * @return
	 */
	public static LaComponent type(String key) {
		for (LaComponent e : LaComponent.values())
			if (key.matches(e.regex))
				return e;
		return NULL;
	}
	
	/**
	 * renvoie le type en fonction du prefix
	 * @param prefix
	 * @return
	 */
	public static LaComponent typeFromPrefix(String prefix) {
		for (LaComponent e : LaComponent.values())
			if (prefix.equalsIgnoreCase(e.prefix))
				return e;
		return type(prefix);
	}
	
	
	public String prefix() {
		return prefix;
	}
	
	public String regex() {
		return regex;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	/*@Override
	public String toString() {
		throw new RuntimeException("Boulet t'as fait oublier une methode sur "+this.prefix);
	}//*/

	/**
	 * @return 
	 */
	public String backupFileRegex() {
		return regex.replaceAll(":", "(-|:)").replaceAll("\\$", "\\\\.xml\\$");
	}


}
