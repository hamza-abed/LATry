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
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import shared.enums.LaComponent;
import client.LaGame;
import client.editor.ServerEditor.CreatorCallBack;
import client.interfaces.network.Sharable;
import client.interfaces.network.SharableReflexEditable;
import shared.variables.Variables;

/**
 * TODO Commentaire de 
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * @param <Clazz>
 * @param <Clazz>
 */
public class AnnotationCreator {
	private static final Logger logger = Logger.getLogger("AnnotationCreato");
	private ArrayList<Field> fields;
	private HashMap<Field, Object> values = new HashMap<Field, Object>();
	private LaGame game;
	private LaComponent type;
	
	 
	
	/**
	 * 
	 */
	public AnnotationCreator(LaComponent type,LaGame game) {
		this.type = type;
		this.game =  game;
	}
	
	/* ********************************************************** *
	 * *			Renvoie la liste des champs					* *
	 * ********************************************************** */

	public Collection<Field> getFields() {
		if (fields == null) {
			fields = new ArrayList<Field>();
			AnnotationUtils.findField(type, fields);
		}
		return fields;
	}

	/**
	 * 
	 * @param f
	 * @param file
	 */
	public void set(Field f, Object file) {
		this.values.put(f,file);
	}
	/* ********************************************************** *
	 * *					creation						* *
	 * ********************************************************** */
	/**
	 * 
	 */
	public void create() {
		game.getServerEditor().createAndCall(LaComponent.object, new CreatorCallBack() {
			@Override
			public void created(String key) {
				Sharable obj = Variables.getWorld().getSharable(key);
				while (!Variables.getWorld().isUpdate(obj))
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						logger.warning("InterruptedException : Je le savais !");
					}
				AnnotationEditor editor = new AnnotationEditor((SharableReflexEditable) obj, game);
				for (Entry<Field, Object> e : values.entrySet()) {
					editor.set(e.getKey(), e.getValue());
				}
				editor.commit();
			}
		});
	}
	
	

	
}
