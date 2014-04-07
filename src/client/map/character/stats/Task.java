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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;

import shared.constants.LaConstants;
import shared.enums.LaComponent;
import shared.pck.Pck;
import shared.utils.Couple;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
import client.interfaces.network.SharableReflexEditable;
import client.map.World;

/**
 * Une liste de tache à acomplire 
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class Task implements SharableReflexEditable {
	/**
	 * identifiant de la tache
	 */
	private int id;
	
	private int versionCode = -1;
	
	/**
	 * nom de la tache
	 */
	@Editable(type=FieldEditType.string)
	private String name = LaConstants.UNSET_STRING;;
	
	/**
	 * decription globale de la tache
	 */
	@Editable(type=FieldEditType.text)
	private String description = LaConstants.UNSET_STRING;
	
	@Editable(type=FieldEditType.list,innerType=FieldEditType.string)
	private ArrayList<String> names = new ArrayList<String>();
	
	@Editable(type=FieldEditType.list,innerType=FieldEditType.integer)
	private ArrayList<Integer> deeps = new ArrayList<Integer>();

	/**
	 * 
	 */
	public Task(World world, int id ) {
		this.id = id;
	}
	
	/* ********************************************************** *
	 * * 				Sharable - IMPLEMENTS 					* *
	 * ********************************************************** */

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	@Override
	public void addData(Pck pck) {
		pck.putString(name,description);
		int count = Math.min(names.size(), deeps.size());
		pck.putInt(count);
		for (int i=0;i<count;i++) {
			pck.putString(names.get(i));
			pck.putInt(deeps.get(i));
		}
	}

	/* (non-Javadoc)
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.task.prefix()+id;
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
		this.name = Pck.readString(message);
		this.description = Pck.readString(message);
		int count = message.getInt();
		this.names.clear();
		this.deeps.clear();
		while (count-->0) {
			this.names.add(Pck.readString(message));
			this.deeps.add(message.getInt());
		}
	}
	/* ********************************************************** *
	 * *					GETTERS / SETTERS					* *
	 * ********************************************************** */

	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return
	 */
	public Collection<Couple<String, Integer>> getAllObjectives() {
		ArrayList<Couple<String, Integer>> out = new ArrayList<Couple<String, Integer>>();
		int count = Math.min(names.size(), deeps.size());
		for (int i=0;i<count;i++) {
			out.add(new Couple<String, Integer>(names.get(i), deeps.get(i)));
		}
		return out;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	

}
