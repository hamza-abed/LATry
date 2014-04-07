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
package shared.editor;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import shared.enums.LaComponent;

/**
 * Interface RMI poru l'edition du server 
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public interface RmiEditor extends Remote {

	/**
	 * Renvoie la liste des clef correspondant au type demander
	 * dans le parent indiquer
	 * @param parentKey 
	 * @param regex
	 * @return
	 * @throws RemoteException
	 */
	public Collection<String> list(String parentKey, LaComponent type) throws RemoteException;
	
	
	/**
	 * Crais un nouvel objet correspondant au type
	 * @param type
	 * @param String parentKey
	 * @return
	 */
	public String create(LaComponent type,String parentKey) throws RemoteException;

	
	/**
	 * Renvoie la valeur d'un champ d'un objet du serveur
	 * @param key
	 * @param field
	 * @return
	 * @throws RemoteException
	 */
	public Object get(String key, String field) throws RemoteException;

	/**
	 * change la valeur d'un champ d'un objet du serveur
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 * @throws RemoteException
	 */
	public void set(String key, String field, Object Value) throws RemoteException;

	/**
	 * supprime la clef id de la map field de l'objet key
	 * @param key
	 * @param field
	 * @param id
	 * @param value
	 */
	public void setMapKeyValue(String key, String field, Object id, Object Value) throws RemoteException;

	/**
	 * Ajout l'objet value à la list field de l'objet key
	 * @param key
	 * @param field
	 * @param value
	 */
	public void addInList(String key, String field, Object value) throws RemoteException;

	/**
	 * Supprime l'objet 
	 * @param key
	 * @param field
	 * @param value
	 */
	public void delInList(String key, String field, Object value) throws RemoteException;


	/**
	 * appele un methode sur un objet
	 * @param key
	 * @param method
	 * @return
	 * @throws RemoteException
	 */
	public void callMethod(String key,String method, Object... args) throws RemoteException;


	/**
	 * pour connaitre la disponibilité du serveur
	 */
	public void ping() throws RemoteException;
	
}
