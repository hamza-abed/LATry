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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import shared.constants.ScriptConstants;
import shared.enums.LaComponent;
import shared.pck.Pck;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
import client.interfaces.network.SharableReflexEditable;
import client.map.World;

/**
 * un script du jeux
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
public class Script implements SharableReflexEditable {
	/**
	 * identifiant
	 */
	private int id;

	@Editable(type=FieldEditType.code)
	private String script;

	@Editable(type=FieldEditType.bool)
	private boolean clientSide;

	private int versionCode = -1;
	
	@Editable(type=FieldEditType.list,innerType=FieldEditType.sharedKey)
	private ArrayList<String> sharedKeys = new ArrayList<String>();

	private World world;

	/**
	 * @param world
	 * @param parseInt
	 */
	public Script(World world, int id) {
		this.id = id;
		this.world = world;
	}

	/* ********************************************************** *
	 * * 				SHARABLE IMPLEMENTS 					* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	@Override
	public void addData(Pck pck) {
		pck.putBoolean(clientSide);
		pck.putString(script);
		pck.putInt(sharedKeys.size());
		for (String key : sharedKeys)
			pck.putString(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.script.prefix() + id;
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
		this.clientSide = Pck.readBoolean(message);
		this.script = Pck.readString(message);
		int size = message.getInt();
		this.sharedKeys.clear();
		while (size-->0) 
			this.sharedKeys.add(Pck.readString(message));
	}

	
	/**
	 * execute un script dans un delay donné. 
	 * @param time
	 */
	@ScriptableMethod(description="execute un script apres un certain delay en millisecond")
	public void execute(long delay) {
	/*	
            
            world.getGame().getSchedulerTaskExecutor().schedule(new Runnable() {
			@Override
			public void run() {
				world.getScriptExecutor().execute(getKey());
			}
		},delay,TimeUnit.MILLISECONDS);
                */
	}
	
	/**
	 * execute un script asap. 
	 * @param time
	 */
	@ScriptableMethod(description="execute un script asap")
	public void execute() {
		world.getScriptExecutor().execute(getKey());
	}
	
	/* ********************************************************** *
	 * * 				GETTERS / SETTERS 						* *
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
	 * @return the script
	 */
	public String getScript() {
		if (script == null) 
			script = ScriptConstants.VOID_SCRIPT;
		return script;
	}

	/**
	 * @param script
	 *            the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * @return the clientSide
	 */
	public boolean isClientSide() {
		return clientSide;
	}

	/**
	 * @param clientSide
	 *            the clientSide to set
	 */
	public void setClientSide(boolean clientSide) {
		this.clientSide = clientSide;
	}

	/**
	 * @return the sharedKeys
	 */
	public ArrayList<String> getSharedKeys() {
		return sharedKeys;
	}

	public int getId() {
		return id;
	}

}
