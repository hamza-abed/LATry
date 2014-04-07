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
package client.map.tool;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import client.LaGame;
import client.interfaces.graphic.Graphic;
//import client.hud.Hud;
//import client.interfaces.graphic.GraphicMouseListener;
import client.map.World;
import client.task.GraphicsAddRemoveSceneTask;
import com.jme3.math.ColorRGBA;

//import com.jme.renderer.ColorRGBA;

/**
 * Moteur abstrait du moteur d'outil
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
public abstract class ToolEngine {
	protected static final Logger logger = Logger.getLogger("ToolEngine");

	/**
	 * outil conteneur
	 */
	protected Tool tool;

	private HashSet<String> changed = new HashSet<String>();

	private GraphicsAddRemoveSceneTask task;

	/**
	 * @param tool
	 */
	public ToolEngine(Tool tool) {
		this.tool = tool;
	}

	/**
	 * A surcouché en cas de besoin
	 * 
	 * @param interpolation
	 */
	public void update(float interpolation) {

	}

	/* ********************************************************** *
	 * * SPECIFICATION ABSTRAITE * *
	 * **********************************************************
	 */

	/**
	 * une clef à été supprimé
	 * 
	 * @param key
	 */
	public abstract void dataDeleted(Collection<String> keys);

	/**
	 * notifie l'outil qu'une data à changé
	 * 
	 * @param key
	 */
	public abstract void dataChanged(Collection<String> keys);

	/**
	 * Doit renvoyé la liste des clef basic de l'outil
	 * 
	 * @return
	 */
	public abstract Collection<String> getKeysDescriptions();

	/* ********************************************************** *
	 * * 				Change la valeur d'une clef 			* *
	 * ********************************************************** */

	/**
	 * change une valeur d'une clef
	 * 
	 * @param key
	 * @param data
	 */
	public void setData(String key, String value) {
		this.changed.add(key);
		this.tool.setDataAndNoSendOnServer(key, value);
	}

	/**
	 * Supprime une clef
	 * 
	 * @param key
	 */
	public void deleteData(String key) {
		logger.info("suppression de " + key);
		this.changed.add(key);
		this.tool.deleteDataAndNoSendOnServer(key);
	}

	/**
	 * Envoie les changements effectuer par setData et deleteData de puis le
	 * dernier appel à ce commit
	 */
	public void commitChangeOnServer() {
		tool.sendChangeOnServer(changed);
		this.changed.clear();
	}

	/* ********************************************************** *
	 * * 						ACCES AU DATA 					* *
	 * ********************************************************** */

	/**
	 * renvoie la valeur d'une clef et la valeur par defaut si elle n'existe
	 * pas. sous forme d'entier (renvoie la valeur par defaut si la donnée n'est
	 * pas un entier)
	 */
	public int getDataAsInt(String key, int defaultValue) {
		if (!tool.datas.containsKey(key))
			return defaultValue;
		try {
			return Integer.parseInt(tool.datas.get(key));
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * renvoie la valeur d'une clef sous forme de boolean 
	 * (renvoi faux si il n'existe pas)
	 * @param key
	 */
	public boolean getDataAsBoolean(String key) {
		try {
			return Boolean.parseBoolean(tool.datas.get(key));
		} catch (NullPointerException e) {
			return false;
		}
	}


	/**
	 * renvoie la valeur d'une clef et la valeur par defaut si elle n'existe
	 * pas.
	 */
	public String getData(String key, String defaultValue) {
		if (!tool.datas.containsKey(key))
			return defaultValue;
		return tool.datas.get(key);
	}

	/**
	 * Renvoie la clef en tans que couleur
	 * @param key
	 * @param defaultColor
	 * @return
	 */
	public ColorRGBA getDataAsColor(String key, ColorRGBA defaultColor) {
		try {
			String value = tool.datas.get(key);
			if (!value.startsWith("#")) value = "#"+value;

			Integer v = Integer.decode(value);
			float r = (v&0xff0000)>>>16;
			float g = (v&0xff00)>>>8;
			float b = v&0xff;
			return new ColorRGBA(r/255f, g/255f, b/255f, 1);
		} catch (Exception e) {
			return defaultColor;
		}
	}




	/**
	 * Renvoie la valeur d'une clef en temps qu'url et l'url par defaut si ca
	 * marche pas
	 * 
	 * @param key
	 * @param defaultUrl
	 * @return
	 */
	public URL getDataAsURL(String key, URL defaultUrl) {
		String data = getData(key, null);

		if (data == null)
			return defaultUrl;

		try {
			return new URL(data);
		} catch (MalformedURLException e) {
		}

		return defaultUrl;
	}

	/**
	 * renvoie la valeur d'une clef et la valeur par defaut si elle n'existe
	 * pas. sous forme de floattant (renvoie la valeur par defaut si la donnée
	 * n'est pas un floatant)
	 */
	public float getDataAsFloat(String key, float defaultValue) {
		if (!tool.datas.containsKey(key))
			return defaultValue;
		try {
			return Float.parseFloat(tool.datas.get(key));
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * Renvoie la clef en une collection de chaine. la donnée et spliter par
	 * l'expression reguliere
	 * 
	 * @param key
	 * @param regex
	 */
	protected ArrayList<String> getDataAsStringCollection(String key,
			String regex) {
		ArrayList<String> out = new ArrayList<String>();
		String data = getData(key, null);
		if (data != null)
			for (String str : data.split(regex))
				out.add(str);

		return out;
	}

	/**
	 * Renvoie la clef en une collection de chaine. la donnée et spliter par
	 * l'expression reguliere
	 * 
	 * @param key
	 * @param regex
	 */
	public ArrayList<Integer> getDataAsIntCollection(String key, String regex) {
		ArrayList<String> datas = getDataAsStringCollection(key, regex);
		ArrayList<Integer> out = new ArrayList<Integer>();

		for (String str : datas)
			try {
				out.add(Integer.parseInt(str));
			} catch (NumberFormatException e) {
			}

			return out;
	}

	/**
	 * Renvoie l'enssemble des clef matchant l'expression reguliers
	 * 
	 * @param regex
	 * @return
	 */
	public Collection<String> getKeyMatch(String regex) {
		Pattern reg = Pattern.compile(regex);
		ArrayList<String> out = new ArrayList<String>();
		for (String key : tool.datas.keySet()) {
			if (reg.matcher(key).find())
				out.add(key);
		}
		return out;
	}

	/**
	 * Renvoie toute les clefs
	 * 
	 * @return
	 */
	public Collection<String> getAllKey() {
		return new ArrayList<String>(tool.datas.keySet());
	}

	/**
	 * @return the tool
	 */
	public Tool getTool() {
		return tool;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.Graphic#addToRenderTask()
	 */
	//@Override
	public void addToRenderTask() {
		if (task == null)
			task = new GraphicsAddRemoveSceneTask((Graphic) this, tool.getWorld());
		task.add();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.Graphic#removeFromRenderTask()
	 */
	//@Override
	public void removeFromRenderTask() {
		if (task == null)
			task = new GraphicsAddRemoveSceneTask((Graphic) this, tool.getWorld());
		task.remove();
	}

	public LaGame getGame() {
		return tool.getWorld().getGame();
	}


	public World getWorld() {
		return tool.getWorld();
	}

	/*public Hud getHud() {
		return getGame().getHud();
	} */

	/**
	 * appeler pour supprimer l'objet
	 */
	public void delete() {

	}
}
