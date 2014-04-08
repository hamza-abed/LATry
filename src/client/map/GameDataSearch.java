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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Permet de faire une recherche dans les data
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class GameDataSearch extends ArrayList<GameData> {
	private static final long serialVersionUID = -6103635070879088338L;
	
	/**
	 * @param values
	 */
	public GameDataSearch(Collection<GameData> values) {
		super(values); 
	}
	
	/**
	 * Trie les resultat par le champ indiquer
	 * @param key
	 * @param asc
	 * @return
	 */
	public GameDataSearch sortInt(final String key,final boolean asc) {
		ArrayList<GameData> removes = new ArrayList<GameData>();
		for (GameData d : this) 
			if (d.get(key)==null) removes.add(d);
		this.removeAll(removes);

		Collections.sort(this, new Comparator<GameData>() {
			@Override
			public int compare(GameData o1, GameData o2) {
				if (asc) return o1.getInt(key).compareTo(o2.getInt(key));
				return o2.getInt(key).compareTo(o1.getInt(key));
			}
		});
		return this;
	}
	
	public GameDataSearch whereValue(String key, String value) {
		ArrayList<GameData> removes = new ArrayList<GameData>();
		for (GameData d : this) {
			if (d.get(key)==null) removes.add(d);
			else if (!d.get(key).equals(value)) removes.add(d);
		}
		this.removeAll(removes);
		return this;
	}
	
	
	public GameData first() {
		if (size()>0) return super.get(0);
		return null;
	}
	
	public GameDataSearch limit(int size) {
		if (size()>size)
			return new GameDataSearch(this.subList(0, size));
		return this;
	}
	

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
