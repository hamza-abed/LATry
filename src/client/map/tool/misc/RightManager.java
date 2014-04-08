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

/**
 * Gestionnaire de droits d'accès à la UNIX, regroupe les fonctionnalités les
 * plus importantes.
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class RightManager {
	/** Voir pour l'utilisateur */
	public static final int USER_VIS = 0400;
	/** Lire pour l'utilisateur */
	public static final int USER_READ = 0200;
	/** Voir et lire pour l'utilisateur */
	public static final int USER_VIS_AND_READ = 0600;
	/** Ecrire pour l'utilisateur */
	public static final int USER_WRITE = 0100;
	/** Tous les droits pour l'utilisateur */
	public static final int USER_FULL_RIGHTS = 0700;

	/** Voir pour le groupe de l'utilisateur */
	public static final int USERGROUP_VIS = 040;
	/** Lire pour le groupe de l'utilisateur */
	public static final int USERGROUP_READ = 020;
	/** Voir et lire pour le groupe de l'utilisateur */
	public static final int USERGROUP_VIS_AND_READ = 060;
	/** Ecrire pour le groupe de l'utilisateur */
	public static final int USERGROUP_WRITE = 010;
	/** Tous les droits pour le groupe de l'utilisateur */
	public static final int USERGROUP_FULL_RIGHTS = 070;

	/** Voir pour les autres */
	public static final int OTHERS_VIS = 04;
	/** Lire pour les autres */
	public static final int OTHERS_READ = 02;
	/** Voir et lire pour les autres */
	public static final int OTHERS_VIS_AND_READ = 06;
	/** Ecrire pour les autres */
	public static final int OTHERS_WRITE = 01;
	/** Tous les droits pour les autres */
	public static final int OTHERS_FULL_RIGHTS = 07;

	/** Voir pour tout le monde */
	public static final int VIS = 0444;
	/** Lire pour tout le monde */
	public static final int READ = 0222;
	/** Voir et lire pour tout le monde */
	public static final int VIS_AND_READ = 0666;
	/** Ecrire pour tout le monde */
	public static final int WRITE = 0111;
	/** Tous les droits pour tout le monde */
	public static final int FULL_RIGHTS = 0777;

	private int rights;

	/**
	 * @param defaultRights
	 *            Code <b><u>octal</u></b> représentant les droits initiaux. Ex:
	 *            0766 : l'utilisateur a tous les droits, les autres peuvent le
	 *            voir et le lire.
	 */
	public RightManager(int defaultRights) {
		rights = defaultRights;
	}

	/**
	 * @param defaultRights
	 *            Code octal représentant les droits initiaux. Ex: "766" :
	 *            l'utilisateur a tous les droits, les autres peuvent le voir et
	 *            le lire.
	 */
	public RightManager(String defaultRights) {
		try {
			rights = Integer.parseInt(defaultRights, 8);
		} catch (Exception e) {
			rights = 0;
		}
	}

	/**
	 * @param rightMask
	 *            Indique l'entité et le droit à tester.
	 * @return si un certain droit d'accès est accordé.
	 */
	public boolean hasRightTo(int rightMask) {
		return (rights & rightMask) == rightMask;
	}

	/**
	 * @param rights
	 */
	public void addRights(int rights) {
		this.rights |= rights;
	}

	/**
	 * @param rights
	 */
	public void removeRights(int rights) {
		this.rights &= ~rights;
	}

	/**
	 * @return Le code octal des droits, avec la portion du groupe de
	 *         l'utilisateur et des autres masquée à 0.
	 */
	public int getUserRights() {
		return rights & USER_FULL_RIGHTS;
	}

	/**
	 * @return Le code octal des droits, avec la portion de l'utilisateur et des
	 *         autres masquée à 0.
	 */
	public int getUserGroupRights() {
		return rights & USERGROUP_FULL_RIGHTS;
	}

	/**
	 * @return Le code octal des droits, avec la portion de l'utilisateur et de
	 *         son groupe masquée à 0.
	 */
	public int getOthersRights() {
		return rights & OTHERS_FULL_RIGHTS;
	}

	/**
	 * @return Les droits sous forme de code octal
	 */
	public int getRights() {
		return rights;
	}

	/**
	 * @return Les droits sous forme de chaine avec le code en octal
	 */
	@Override
	public String toString() {
		return Integer.toOctalString(rights);
	}
}
