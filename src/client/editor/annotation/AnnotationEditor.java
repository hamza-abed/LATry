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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;

import shared.constants.LaConstants;
import shared.constants.ScriptConstants;
import client.LaGame;
import client.interfaces.network.Sharable;
import client.interfaces.network.SharableReflexEditable;
import client.map.tool.Tool;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
//import client.map.object.MapTable;

//import com.jme.math.Vector3f;
//import com.jme.renderer.ColorRGBA;

/**
 * Simplifie l'acces au annotation pour editer l'objet
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class AnnotationEditor {
	private static final Logger logger = Logger.getLogger("AnnotationEditor");
	
	private Sharable obj;

	/**
	 * liste des champ de 'object editer
	 */
	private ArrayList<Field> fields;

	private LaGame game;

	private TreeSet<String> toolInitialDatas;

	
	/**
	 * @param obj
	 */
	public AnnotationEditor(SharableReflexEditable obj,LaGame game) {
		this.obj = obj;
		this.game = game;
	/*	if (obj instanceof MapTable)
			logger.info("Annotation editeur sur table"); */
		if (obj instanceof Tool)
			this.toolInitialDatas = new TreeSet<String>(((Tool)obj).getDataKeys());
	}
	
	/* ********************************************************** *
	 * *						NetWork							* *
	 * ********************************************************** */
	
	/**
	 * 
	 */
	public void commit() {
		game.commitOnServer(obj);
		if (obj instanceof Tool) {
			toolInitialDatas.addAll(((Tool)obj).getDataKeys());
			((Tool)obj).sendChangeOnServer(toolInitialDatas);
		}
	}
	
	/**
	 * supprime l'objet
	 */
	public void delete() {
	//	game.getServerEditor().delete(obj);
	}

	
	/**
	 * 
	 */
	public void reset() {
		game.updateFromServer(obj);
	}
	
	/* ********************************************************** *
	 * *				Accede au champ de l'obj				* *
	 * ********************************************************** */
	/**
	 * execute un callback sur un object
	 * @param applyMethod
	 */
	@SuppressWarnings("all")
	private void callMethod(Class<?> clazz, String method) {
		try {
			
			Method m = clazz.getDeclaredMethod(method, null);
			logger.info(" ---> appel sur classe : "+clazz.getSimpleName().toString() + " methode :"+method);
			m.setAccessible(true);
			m.invoke(obj, null);
		} catch (SecurityException e) {
			logger.warning("SecurityException : Je le savais !");
		} catch (NoSuchMethodException e) {
			if (clazz.getSuperclass() != null)
				callMethod(clazz.getSuperclass(), method);
			else 
				logger.warning("NoSuchMethodException : Je le savais !");
		} catch (IllegalArgumentException e) {
			logger.warning("IllegalArgumentException : Je le savais !");
		} catch (IllegalAccessException e) {
			logger.warning("IllegalAccessException : Je le savais !");
		} catch (InvocationTargetException e) {
			logger.warning("InvocationTargetException : Je le savais !");
		}
	}
	
	/* ********************************************************** *
	 * *					LECTURE DES VALEURS					* *
	 * ********************************************************** */

	/**
	 * Lecture la plus basic de la valeur
	 * @param f 
	 * @return
	 */
	public Object get(Field f) {
		try {
			return f.get(obj);
		} catch (IllegalArgumentException e) {
			logger.warning("IllegalArgumentException : Je le savais !");
		} catch (IllegalAccessException e) {
			logger.warning("IllegalAccessException : Je le savais !");
		}
		return "null";
	}

	
	/**
	 * Lit la valeur en entier
	 * @return
	 */
	public Integer getInt(Field field) {
		try {
			return (Integer) field.get(obj);
		} catch (IllegalArgumentException e) {
			logger.warning("IllegalArgumentException : Je le savais !");
		} catch (IllegalAccessException e) {
			logger.warning("IllegalAccessException : Je le savais !");
		} catch (ClassCastException e) {
			
		}
		return 0;
	}
	
	/**
	 * Lit la valeur en entier
	 * @return
	 */
	public Float getFloat(Field field) {
		try {
			return (Float) get(field);
		} catch (ClassCastException e) { }
		return 0f;
	}

	/**
	 * Renvoie le champ en tans que vecteur
	 * @param field
	 * @return
	 */
	public Vector3f getVertex(Field field) {
		try {
			return (Vector3f) get(field);
		} catch (ClassCastException e) {}
		return new Vector3f(0, 0, 0);
	}

	/**
	 * lecteur 'une couleur
	 * @param field
	 * @return
	 */
	public ColorRGBA getColor(Field field) {
		try {
			return (ColorRGBA) get(field);
		} catch (ClassCastException e) {}
		return new ColorRGBA(0, 0, 0, 1);
	}
	
	/**
	 * Lecture d'une chaine
	 * @param field
	 */
	@SuppressWarnings("unchecked")
	public List<Object> getList(Field field) {
		try {
			return (List<Object>)field.get(obj);
		} catch (IllegalArgumentException e) {
			logger.warning("IllegalArgumentException : Je le savais !");
		} catch (IllegalAccessException e) {
			logger.warning("IllegalAccessException : Je le savais !");
		} catch (ClassCastException e) {
			logger.warning("ClassCastException : Je le savais !");
		}
		return null;
	}
	
	/**
	 * Renvoie la valeur d'un champ en en un SET
	 * @param field
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Set<Object> getSet(Field field) {
		try {
			return (Set<Object>)field.get(obj);
		} catch (IllegalArgumentException e) {
			logger.warning("IllegalArgumentException : Je le savais !");
		} catch (IllegalAccessException e) {
			logger.warning("IllegalAccessException : Je le savais !");
		} catch (ClassCastException e) {
			logger.warning("ClassCastException : Je le savais !");
		}
		return null;
	}

	
	
	/**
	 * permet l'acces sous forme d'une Map<K,V> 
	 * @param field
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<Object, Object> getMap(Field field) {
		try {
			return (Map<Object, Object>)field.get(obj);
		} catch (IllegalArgumentException e) {
			logger.warning("IllegalArgumentException : Je le savais !");
		} catch (IllegalAccessException e) {
			logger.warning("IllegalAccessException : Je le savais !");
		} catch (ClassCastException e) {
			logger.warning("ClassCastException : Je le savais !");
		}
		return null;
	}
	
	
	/**
	 * Lecture d'une chaien
	 * @param field
	 * @return
	 */
	public String getString(Field field) {
		try {
			return (String)get(field);
		} catch (ClassCastException e) {
			return "null";
		}
	}
	
	/**
	 * renvoie le champ en tans que boolean
	 * @param f
	 * @return
	 */
	public boolean getBoolean(Field f) {
		try {
			return (Boolean)get(f);
		} catch (ClassCastException e) {
			return false;
		}
	}

	
	/* ********************************************************** *
	 * *					ECRITURE DES VALEURS				* *
	 * ********************************************************** */

	void set(Field f,Object value) {
		try {
			f.set(obj,value);
			if (!f.getAnnotation(Editable.class).applyMethod().equals("void"))
				callMethod(obj.getClass(),f.getAnnotation(Editable.class).applyMethod());
		} catch (IllegalArgumentException e) {
			logger.warning("IllegalArgumentException : Je le savais !");
		} catch (IllegalAccessException e) {
			logger.warning("IllegalAccessException : Je le savais !");
		}
	}


	/**
	 * Change la valeur sous forme d'un entier
	 * @param i
	 */
	public void setInt(Field field,int value) {
		set(field,value);
	}
	
	/**
	 * Setter d'entier par une chaine
	 * @param field
	 * @param text
	 */
	public void setInt(Field field, String value) {
		try {
			set(field, Integer.parseInt(value));
		} catch(NumberFormatException e) { } 
	}
	
	/**
	 * Setter de floattant par une chaine
	 * @param field
	 * @param text
	 */
	public void setFloat(Field field, String value) {
		try {
			set(field, Float.parseFloat(value));
		} catch(NumberFormatException e) { } 
	}
	
	/**
	 * Setter de flotant par valeur
	 * @param field
	 * @param value
	 */
	public void setFloat(Field field, float value) {
		set(field, value);
	}

	/**
	 * Change une valeur pour un vectgeur
	 * @param field
	 * @param v
	 */
	public void setVertex(Field field, Vector3f v) {
		set(field, v);
	}

	/**
	 * Change une valeur pour une couleur
	 * @param field
	 * @param v
	 */
	public void setColor(Field field, ColorRGBA v) {
		set(field, v);
	}
	/**
	 * setter par une chaine de caractere
	 * @param f
	 * @param text
	 */
	public void setStr(Field f, String text) {
		set(f, text);
	}
	
	/**
	 * Stter pour un type enuméré
	 * @param field
	 * @param selectedValue
	 */
	public void setEnum(Field field, Object value) {
		set(field,value);
	}
	
	/**
	 * TODO commentaire AnnotationEditor.setBoolean
	 * @param f
	 * @param selected
	 */
	public void setBoolean(Field field, boolean value) {
		set(field,value);
	}
	
	/* ********************************************************** *
	 * *		Creation DES VALEURS contenu dans une liste		* *
	 * ********************************************************** */

	/**
	 * Crais un nouvelle Element
	 * @param field
	 * @return
	 */
	public Object createElement(Field field) {
		switch (field.getAnnotation(Editable.class).innerType()) {
		case string: return LaConstants.UNSET_STRING;
		case bool: return false;
		case integer: return 0;
		case real: return 0f;
		case action: return ScriptConstants.VOID_SCRIPT;
		case text: return LaConstants.UNSET_STRING;
		case sharedKey: return ScriptConstants.VOID_SCRIPT;
		case file: return "NULL";
		case vertex: return new Vector3f();
		default:
			throw new RuntimeException("Type pas encore supporté");
		}
	}
	
	
	/* ********************************************************** *
	 * *					GETTER /  SETTER					* *
	 * ********************************************************** */
	/**
	 * Renvoie la liste des champ
	 */
	public Collection<Field> getFields() {
		if (fields == null) {
			fields = new ArrayList<Field>();
			AnnotationUtils.findField(obj.getClass(),fields);
		}	
		return fields;
	}

	/**
	 * renvoie l'enssemble des valeur enumérépossible
	 * @param field
	 * @return
	 */
	public Object[] getEnumValue(Field field) {
		return 	field.getType().getEnumConstants();
	}


	



	
}
