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
package client.script;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.logging.Logger;

import shared.constants.ScriptConstants;
//import client.hud.Hud;
import client.map.World;
import client.map.character.Group;
import client.map.character.NonPlayableCharacter;
import client.map.character.Player;
import client.map.data.SlideShow;
//import client.map.object.BasicMapObject;
//pas opérationnelle en v31
//import client.map.tool.Lgf;
import client.map.tool.Tool;
import client.sound.SoundSystem;

/**
 * Permet de trouver les methodes dans les script disponible sur les types
 * <ul>
 * <li></li>
 * <li>suppression client.map.tool.lgf , client.editor.model.lgfeditmodel</li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2010-2013
 */
public class ScriptMethods {
	private static final Logger logger = Logger.getLogger("ScriptMethods");

	private static ScriptMethods instance;

	private HashMap<Class<?>, TreeSet<Method>> methods = new HashMap<Class<?>, TreeSet<Method>>();

	private HashMap<Class<?>, TreeSet<String>> methodNames = new HashMap<Class<?>, TreeSet<String>>();


	/**
	 * 
	 */
	private ScriptMethods() {
	}

	public static ScriptMethods getInstance() {
		if (instance == null)
			instance = new ScriptMethods();
		return instance;
	}

	/**
	 * Renvoie la liste des methode scriptable en fonction d'un mot 
	 * clef.
	 * 
	 * @see {@link ScriptConstants}
	 *  
	 */
	public Collection<Method> getMethods(String scriptKeyWord) {
		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.PLAYER_KEYWORD))
			return getMethods(Player.class);
		/*if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.HUD_KEYWORD))
			return getMethods(Hud.class); */
		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.SOUND_KEYWORD))
			return getMethods(SoundSystem.class);
		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.WORLD_KEYWORD))
			return getMethods(World.class);
		/*if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.OBJECT_REGEX))
			return getMethods(BasicMapObject.class); */
		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.NPC_REGEX))
			return getMethods(NonPlayableCharacter.class);
// pas opérationnelle en v31
//		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.LGF_REGEX))
//			return getMethods(Lgf.class);
		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.TOOL_REGEX))
			return getMethods(Tool.class);
		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.SLIDE_REGEX))
			return getMethods(SlideShow.class);
		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.SCRIPT_REGEX))
			return getMethods(Script.class);
		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.GROUP_REGEX))
			return getMethods(Group.class);
		return new ArrayList<Method>();
	}

	/**
	 * Renvoie les methode de la classe supposé scriptable
	 * @param clazz
	 * @return
	 */
	public Collection<Method> getMethods(Class<?> clazz) {
		if (methods.containsKey(clazz))
			return methods.get(clazz);
		TreeSet<Method> out = new TreeSet<Method>(new Comparator<Method>() {
			@Override
			public int compare(Method o1, Method o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});
		findMethod(clazz, out);
		methods.put(clazz, out);
		return out;
	}

	/**
	 * Renvoie juste la liste des nom de methode
	 * @param key
	 * @return
	 */
	public Collection<String> getMethodNames(String scriptKeyWord) {
		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.PLAYER_KEYWORD))
			return getMethodNames(Player.class);
		/*if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.HUD_KEYWORD))
			return getMethodNames(Hud.class); */
		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.SOUND_KEYWORD))
			return getMethodNames(SoundSystem.class);
		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.WORLD_KEYWORD))
			return getMethodNames(World.class);
		/*if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.OBJECT_REGEX))
			return getMethodNames(BasicMapObject.class); */
		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.TOOL_REGEX))
			return getMethodNames(Tool.class);
		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.NPC_REGEX))
			return getMethodNames(NonPlayableCharacter.class);
// pas opérationnelle en v31
//		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.LGF_REGEX))
//			return getMethodNames(Lgf.class);
		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.SLIDE_REGEX))
			return getMethodNames(SlideShow.class);
		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.SCRIPT_REGEX))
			return getMethodNames(Script.class);
		if (scriptKeyWord.equalsIgnoreCase(ScriptConstants.GROUP_REGEX))
			return getMethodNames(Group.class);
		return new ArrayList<String>();
	}

	/**
	 * Renvoie la liste des nom de methode scriptable
	 * @param class1
	 * @return
	 */
	public Collection<String> getMethodNames(Class<?> clazz) {
		if (methodNames.containsKey(clazz))
			return methodNames.get(clazz);
		TreeSet<String> out = new TreeSet<String>();
		for (Method m : getMethods(clazz))
			out.add(m.getName());
		methodNames.put(clazz, out);
		return out;
	}

	/* ********************************************************** *
	 * * 				Recherche des methode  					* *
	 * ********************************************************** */


	/**
	 * Reccourci pour recherche des methode scriptable dans une class
	 * 
	 * @param c class à rechercher
	 * @param methods collection de sortie
	 */
	private void findMethod(Class<?> c, TreeSet<Method> methods) {
		logger.entering("ScriptMethods", "findMethod");
		logger.fine("cherche les methode scriptable de " + c.getName());
		for (Method m : c.getMethods()) {
			if (m.isAnnotationPresent(ScriptableMethod.class)) {
				methods.add(m);
				logger.fine(m.getName() + " est scriptable");
			}
		}
		logger.exiting("ScriptMethods", "findMethod");
	}



}
