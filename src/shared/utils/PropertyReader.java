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
package shared.utils;

import java.util.Properties;

/**
 * Simplifie l'acces au fichier properties
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class PropertyReader {
	/**
	 * permet de lire un floattant dans un propertie Java Renvoie la valeur par defaut si la property n'est pas trouvé ou si ce n'est pas un floattant
	 * @param props
	 * @param name
	 * @param defaulValue
	 * @return
	 */
	public static float getFloat(Properties props,String name,float defaulValue) {
		try {
			return Float.parseFloat(props.getProperty(name));
		} catch (NumberFormatException e) {
			
		} catch (NullPointerException e) {
			
		}
		return defaulValue;
	}

	/**
	 * Permet de lire un entier
	 * @param props
	 * @param string
	 * @param default Value
	 * @return
	 */
	public static int getInt(Properties props, String name, int defaulValue) {
		try {
			return Integer.parseInt(props.getProperty(name));
		} catch (NumberFormatException e) {
			
		} catch (NullPointerException e) {
			
		}
		return defaulValue;
	}

	/**
	 * Permet de lire un boolean
	 * @param props
	 * @param string
	 * @return
	 */
	public static boolean getBoolean(Properties props, String name) {
		return "true".equalsIgnoreCase(props.getProperty(name, "false"));
	}
}
