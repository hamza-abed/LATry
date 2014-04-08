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
package client.map.tool.misc;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Prévu pour le partage de fichiers. Permet de récupérer le type du fichier
 * partagé, l'adresse d'une icône locale associée au type.
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class FileType {
	private static final Logger logger = Logger.getLogger(FileType.class
			.getName());

	/**
	 * Constantes pour désigner des types de fichier 0x1.. : images A modifier
	 * si nécessaire
	 */
	public static final int UNKNOWN = 0x800,

	TEXT = 0x001, HTML = 0x002, PDF = 0x003, WORD = 0x004, EXCEL = 0x005,
			PPOINT = 0x006,

			PICTURE_MASK = 0x100, BITMAP = 0x101, TIFF = 0x102, GIF = 0x103,
			TARGA = 0x104, PNG = 0x105, JPEG = 0x106,

			VIDEO_MASK = 0x200, AVI = 0x201, MPEG = 0x202, MOV = 0x203,
			FLASH = 0x204, MP4 = 0x205;

	/**
	 * A partir de l'extension, donne directement le type de fichier
	 */
	private static final HashMap<String, Integer> iconExtension = new HashMap<String, Integer>();
	static {
		iconExtension.put(".txt", TEXT);
		iconExtension.put(".rtf", TEXT);
		iconExtension.put(".htm", HTML);
		iconExtension.put(".html", HTML);
		iconExtension.put(".pdf", PDF);
		iconExtension.put(".doc", WORD);
		iconExtension.put(".docx", WORD);
		iconExtension.put(".xls", EXCEL);
		iconExtension.put(".xlsx", EXCEL);
		iconExtension.put(".ppt", PPOINT);
		iconExtension.put(".pps", PPOINT);
		iconExtension.put(".pptx", PPOINT);
		iconExtension.put(".ppsx", PPOINT);

		iconExtension.put(".bmp", BITMAP);
		iconExtension.put(".tif", TIFF);
		iconExtension.put(".tiff", TIFF);
		iconExtension.put(".gif", GIF);
		iconExtension.put(".tga", TARGA);
		iconExtension.put(".png", PNG);
		iconExtension.put(".jpg", JPEG);
		iconExtension.put(".jpe", JPEG);
		iconExtension.put(".jpeg", JPEG);

		iconExtension.put(".avi", AVI);
		iconExtension.put(".mpg", MPEG);
		iconExtension.put(".mpeg", MPEG);
		iconExtension.put(".mov", MOV);
		iconExtension.put(".flv", FLASH);
		iconExtension.put(".mp4", MP4);
	}

	/**
	 * fait la correspondance entre le type de fichier et le chemin de l'icône
	 * associée
	 */
	private static final HashMap<Integer, String> iconPath = new HashMap<Integer, String>();
	static {
		iconPath.put(TEXT, "text.png");
		iconPath.put(HTML, "html.png");
		iconPath.put(PDF, "pdf.png");
		iconPath.put(WORD, "word.png");
		iconPath.put(EXCEL, "excel.png");
		iconPath.put(PPOINT, "powerpoint.png");
		iconPath.put(BITMAP, "bmp.png");
		iconPath.put(TIFF, "tiff.png");
		iconPath.put(GIF, "gif.png");
		iconPath.put(TARGA, "tga.png");
		iconPath.put(PNG, "png.png");
		iconPath.put(JPEG, "jpeg.png");
		iconPath.put(AVI, "avi.png");
		iconPath.put(MPEG, "mpeg.png");
		iconPath.put(MOV, "mov.png");
		iconPath.put(FLASH, "flv.png");
		iconPath.put(UNKNOWN, "unknown.png");
	}

	/**
	 * @param fileName
	 *            le nom du fichier
	 * @return son extension (".jpeg", etc.)
	 */
	public static String getExtension(String fileName) {
		logger.entering(FileType.class.getName(), "getExtension");

		int dotPos = fileName.lastIndexOf(".");
		if (dotPos == -1)
			return "";

		logger.exiting(FileType.class.getName(), "getExtension");
		return fileName.substring(dotPos);
	}

	/**
	 * @param fileName
	 *            le nom du fichier
	 * @return son type (FileShareType.JPEG, etc.)
	 */
	public static int getType(String fileName) {
		try {
			return iconExtension.get(getExtension(fileName));
		} catch (NullPointerException e) {
			try {
				if (fileName.startsWith("http") || fileName.startsWith("ftp"))
					return HTML;
				else
					return UNKNOWN;
			} catch (NullPointerException e2) {
				return UNKNOWN;
			}
		}
	}

	/**
	 * @param fileName
	 *            le nom du fichier
	 * @return le chemin de l'icône associée à son type
	 */
	public static String getIcon(String fileName) {
		return iconPath.get(getType(fileName));
	}

	/**
	 * @param fileName
	 *            le nom du fichier
	 * @return si son extension correspond à un type d'image connue
	 */
	public static boolean isPicture(String fileName) {
		return (getType(fileName) & PICTURE_MASK) != 0;
	}

	/**
	 * @param fileName
	 *            le nom du fichier
	 * @return si son extension correspond à un type de video connue
	 */
	public static boolean isVideo(String fileName) {
		return (getType(fileName) & VIDEO_MASK) != 0;
	}

	/**
	 * @param fileName
	 *            le nom du fichier
	 * @return si le fichier se termine en ".pdf"
	 */
	public static boolean isPDF(String fileName) {
		return getType(fileName) == PDF;
	}
}
