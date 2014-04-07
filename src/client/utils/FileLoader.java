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
package client.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Permet d'avoir acces à des ressource
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class FileLoader {

	public static class FileLoaderException extends RuntimeException {
		private static final long serialVersionUID = -498903514832573060L;
		private String file;

		public FileLoaderException(String file) {
			this.file = file;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Throwable#getMessage()
		 */
		@Override
		public String getMessage() {
			return "File Not Found : " + file;
		}
	}

	/**
	 * 
	 * @param string
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getResourceAsStream(String f) {
		// recherche hors de l'application (erepertoir courant)
		InputStream is;
		try {
			is = new FileInputStream(f);
			if (is != null)
				return is;
		} catch (FileNotFoundException e) {

		}

		// Recherche dans l'application (jar)
		is = FileLoader.class.getClassLoader().getResourceAsStream(f);
		if (is != null)
			return is;

		throw new FileLoaderException(f);
	}

	public static URL getResourceAsUrl(String name) {
		// recherche hors de l'application (repertoire courant)
		URL url = null;
		File f = new File(name);
		if (f.exists())
			try {
				return f.toURI().toURL();
			} catch (MalformedURLException e) {
				throw new FileLoaderException(name);
			}

		// Recherche dans l'application (jar)
		url = FileLoader.class.getClassLoader().getResource(name);
		return url;
	}

	public static File getResourceAsFile(String name) {
		// recherche hors de l'application (erepertoir courant)
		File f = new File(name);
		if (f.exists())
			return f;

		// Recherche dans l'application (jar)
		f = new File(FileLoader.class.getClassLoader().getResource(name)
				.getFile());
		if (f.exists())
			return f;

		throw new FileLoaderException(name);
	}

}
