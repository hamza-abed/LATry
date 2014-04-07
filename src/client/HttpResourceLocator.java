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
package client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

//import com.jme.util.resource.ResourceLocator;

/**
 * TODO Commentaire de 
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class HttpResourceLocator {//implements ResourceLocator {
	private static final Logger logger = Logger
			.getLogger("HttpResourceLocator");
	public static final String RESOURCE = "game-ressource";
	private String http;

	/**
	 * @param http
	 */
	public HttpResourceLocator(String http) {
		this.http = http;
	}

	/* (non-Javadoc)
	 * @see com.jme.util.resource.ResourceLocator#locateResource(java.lang.String)
	 */
	//@Override
	public URL locateResource(String resourceName) {
		try {
			URL u = new URL(http+resourceName); 
			u.openStream().close();
			return u;
		} catch (MalformedURLException e) {
			logger.warning("MalformedURLException : Je le savais !");
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
