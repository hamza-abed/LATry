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
package client.editor.annotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import shared.enums.LaComponent;
import client.editor.FieldEditType;
//import client.map.object.BasicMapObject;
//import client.map.object.BuildingMapObject;
//import client.map.object.MapTable;

/**
 * Methode commune à la creation et à l'edition
 * <ul>
 * <li>modif pp : ajout traitement table</li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2013
 */
public class AnnotationUtils {
	private static final Logger logger = Logger.getLogger(AnnotationUtils.class.getName());

	/**
	 * Recherche les champs editable
	 */
	static void findField(Class<?> clazz, Collection<Field> fields) {
		for (Field f : clazz.getDeclaredFields())
			if (f.isAnnotationPresent(Editable.class)) {
				f.setAccessible(true);
				fields.add(f);
			} 
		if (clazz.getSuperclass() != null)
			findField(clazz.getSuperclass(),fields);
	}

	/**
	 * indique si le champ est editable par le populateur
	 * @param f
	 * @return
	 */
	public static boolean isPopulable(Field f) {
		return f.getAnnotation(Editable.class).populus();
	}

	/**
	 * renvoie le type 
	 * @param f
	 * @return
	 */
	public static FieldEditType getType(Field f) {
		return f.getAnnotation(Editable.class).type();
	}

	/**
	 * recherche des champs par le type
	 * @param type
	 * @param fields
	 * @author philippe
	 */
	static void findField(LaComponent type, ArrayList<Field> fields) {
		switch (type) {
		//case building : findField(BuildingMapObject.class, fields);	break;
		//case object: findField(BasicMapObject.class, fields); break;
		//case table: findField(MapTable.class, fields); break;

		default:
			logger.warning("type non suuporter "+type);
			throw new RuntimeException("type non suuporter "+type+ " dans AnnotationUtils");
		}
	}

	/**
	 * renvoie sa valeur par defaut
	 * @param f
	 * @return
	 */
	public static float getDefaultFloatValue(Field f) {
		return f.getAnnotation(Editable.class).defaultReal();
	}

	/**
	 * TODO commentaire AnnotationUtils.getDefaultBoolValue
	 * @return
	 */
	public static boolean getDefaultBoolValue(Field f) {
		return f.getAnnotation(Editable.class).defaultBool();
	}
	
	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
