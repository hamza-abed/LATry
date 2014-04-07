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
package client.map;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map.Entry;

import shared.enums.LaComponent;
import shared.pck.Pck;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
import client.interfaces.network.SharableReflexEditable;
import client.script.ScriptableMethod;

/**
 * Donnée de jeux 
 * <ul>
 * <li>Permet de stockée des information structuré</li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class GameData implements SharableReflexEditable {
	private int versionCode = -1;

	@Editable(type=FieldEditType.map, keyType=FieldEditType.string, innerType=FieldEditType.string)
	private HashMap<String, String> datas = new HashMap<String, String>();
	
	private int id = -1;
	
	private World world;
	
	/**
	 * @param id 
	 * 
	 */
	public GameData(World world, int id) {
		this.world = world;
		this.id = id;
	}
	
	/* ********************************************************** *
	 * *				Sharable - Implements					* *
	 * ********************************************************** */

	
	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	@Override
	public void addData(Pck pck) {
		pck.putInt(datas.size());
		for (Entry<String, String>d : datas.entrySet()) 
			pck.putString(d.getKey(),d.getValue());
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.gamedata.prefix()+id;
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#getVersionCode()
	 */
	@Override
	public int getVersionCode() {
		return versionCode;
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#receiveCommitPck(java.nio.ByteBuffer)
	 */
	@Override
	public void receiveCommitPck(ByteBuffer message) {
		versionCode = message.getInt();
		datas.clear();
		int count = message.getInt();
		while(count-->0)
			datas.put(Pck.readString(message), Pck.readString(message));
	}

	/* ********************************************************** *
	 * *					SETTERS / GETTERS					* *
	 * ********************************************************** */
	
	@ScriptableMethod(description="ajout une valeur à la data\n(key,value)")
	public void set(String key, String value) {
		datas.put(key, value);
	}
	
	@ScriptableMethod(description="ajout une valeur à la data\n(key,value)")
	public void set(String key, int value) {
		datas.put(key, Integer.toString(value));
	}
	
	@ScriptableMethod(description="supprime un champ de la data")
	public void del(String key) {
		datas.remove(key);
	}
	
	@ScriptableMethod(description="permet de lire les datas")
	public String get(String key) {
		return datas.get(key);
	}

	@ScriptableMethod(description="lire en tans qu'entier")
	public Integer getInt(String key) {
		return Integer.parseInt(datas.get(key));
	}
	
	@ScriptableMethod(description="commit les changement au serveur")
	public void commit() {
		//world.getGame().commitOnServer(this);
		//world.getGame().getTraces().sendGameData(this);
	}

	/**
	 * @return
	 */
	public int size() {
		return datas.size();
	}

	/**
	 * @return
	 */
	public HashMap<String, String> getDatas() {
		return datas;
	}
	
}
