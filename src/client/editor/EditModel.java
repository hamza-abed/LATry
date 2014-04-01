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
package client.editor;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Permet de représenté la structure d'edition d'un objet
 * <ul>
 * <li>Deprecated on prefere utilisé l'introspection</li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
@Deprecated
public abstract class EditModel {
	private HashMap<String, Object> saves = new HashMap<String, Object>();;

	/**
	 * Doit renvoyé la liste des champ editable
	 * 
	 * @return
	 */
	public abstract Collection<String> getFieldList();

	/**
	 * Doit renvoyé le type d'un champ
	 * 
	 * @return
	 */
	public abstract FieldEditType getFieldType(String name);

	/**
	 * Renvoie la valeur actuele du champ
	 * 
	 * @param name
	 * @return
	 */
	public abstract Object getValue(String name);

	/**
	 * Doit renvoyer la valeur minimal dans le cas d'un intervale
	 * 
	 * @param name
	 * @return
	 */
	public abstract Number getMinValue(String name);

	/**
	 * Doit renvoyer la valeur minimal dans le cas d'un intervale
	 * 
	 * @param name
	 * @return
	 */
	public abstract Number getMaxValue(String name);

	/**
	 * Fix la valeur d'un champ
	 */
	public final void setValue(String name, Object value) {
		if (!saves.containsKey(name))
			saves.put(name, getValue(name));
		apply(name, value);
	}

	/**
	 * Applique la valeurs
	 * 
	 * @param name
	 * @param value
	 */
	protected abstract void apply(String name, Object value);

	/**
	 * Permet d'acceder directement sous forme de floatant
	 * 
	 * @param name
	 * @return
	 */
	public float getFloat(String name) {
		try {
			return (Float) getValue(name);
		} catch (ClassCastException e) {
			return Float.NaN;
		}
	}

	/**
	 * Permet d'acceder directement sous forme de entier
	 * 
	 * @param name
	 * @return
	 */
	public int getInt(String name) {
		try {
			return (Integer) getValue(name);
		} catch (ClassCastException e) {
			return 0;
		}
	}

	/**
	 * Racourci en valeur booleene
	 * 
	 * @param name
	 * @return
	 */
	public boolean getBoolean(String name) {
		try {
			return (Boolean) getValue(name);
		} catch (ClassCastException e) {
			return false;
		}
	}

	/**
	 * Annule tous les changement qui on été fait
	 */
	public void rewind() {
		for (Entry<String, Object> e : saves.entrySet()) {
			apply(e.getKey(), e.getValue());
		}
		saves.clear();
	}

	/**
	 * Efface les auvegardes de donnée (celle qui sont reappliqué avec le
	 * rewind)
	 */
	public void clearUnDos() {
		saves.clear();
	}

	/**
	 * Renvoie la liste des valeur enuméré possible
	 * 
	 * @param name
	 * @return
	 */
	public abstract Object[] getEnumValues(String name);

	/**
	 * Revoie le chemin ou sont les fichiers
	 * 
	 * @return
	 */
	public abstract File getFileFolder(String name);

	/**
	 * @param name
	 * @return
	 */
	public Number getStepValue(String name) {
		return 1.0f;
	}

}
