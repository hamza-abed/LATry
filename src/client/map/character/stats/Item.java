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

import java.net.URL;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import shared.constants.LaConstants;
import shared.constants.ScriptConstants;
import shared.enums.LaComponent;
import shared.pck.Pck;
import shared.utils.FileLoader;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
import client.interfaces.network.SharableReflexEditable;
import client.map.World;

/**
 * un item dans le sac du joueur
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class Item implements SharableReflexEditable {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("Item");

	/**
	 * identifiant de l'item
	 */
	private int id;

	/**
	 * monde conteneur
	 */
	private World world;

	private int versionCode = -1;
	
	@Editable(type=FieldEditType.integer)
	private int maxAcquierable = 1;

	@Editable(type=FieldEditType.string)
	private String name = LaConstants.UNSET_STRING;
	
	@Editable(type=FieldEditType.file,fileFolder=LaConstants.DIR_ICON)
	private String icon = LaConstants.DEFAULT_ICON;
	
	@Editable(type=FieldEditType.action)
	private String action = ScriptConstants.VOID_SCRIPT;

	/**
	 * 
	 */
	public Item(World world, int id) {
		this.world = world;
		this.id = id;
	}

	/**
	 * execute l'action associé à l'item
	 */
	public void exec() {
		world.getScriptExecutor().execute(action);
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
		pck.putString(name, icon, action);
		pck.putInt(maxAcquierable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.item.prefix() + id;
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
		this.versionCode = message.getInt();
		this.name = Pck.readString(message);
		this.icon = Pck.readString(message);
		this.action = Pck.readString(message);
		this.maxAcquierable = message.getInt();
	}

	/* ********************************************************** *
	 * * 					GETTER / SETTERS 					* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getKey();
	}

	/**
	 * @return the maxAcquierable
	 */
	public int getMaxAcquierable() {
		return maxAcquierable;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param maxAcquierable
	 *            the maxAcquierable to set
	 */
	public void setMaxAcquierable(int maxAcquierable) {
		this.maxAcquierable = maxAcquierable;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * renvoie l'identifiant de l'item
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * renvoie l'url vers l'icone
	 * 
	 * @return
	 */
	public URL getIconUrl() {
		URL url = FileLoader.getResourceAsUrl(LaConstants.DIR_ICON+getIcon());
		if (url == null)
			url = FileLoader.getResourceAsUrl(LaConstants.DIR_LGPL_ICON+getIcon());
		if (url == null)
			url = FileLoader.getResourceAsUrl(LaConstants.DIR_CC_BY_ICON+getIcon());
		if (url == null)
			url = FileLoader.getResourceAsUrl(LaConstants.DIR_SYSCOM_ICON+getIcon());
		if (url == null)
			url = FileLoader.getResourceAsUrl(LaConstants.DIR_LGPL_ICON+LaConstants.DEFAULT_ICON);
		return url;//*/
		//return ResourceLocatorTool.locateResource("icons", getIcon());
	}



}
