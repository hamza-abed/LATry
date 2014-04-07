/**
 * Copyright 2009 <b>Shionn</b>, shionn@gmail.com <i>http://shionn.org</i><br>
 * GCS d- s+:+ a- C++ UL/M P L+ E--- W++ N K- w-- M+ t+ 5 X R+ !tv b+ D+ G- e+++ h+ r- !y-
 * 
 * 
 * ----------------------------------------------------------------------------
 * Ce fichier fait partie de LaShared.
 *
 * LaShared est un logiciel libre ; vous pouvez le redistribuer ou le modifier 
 * suivant les termes de la GNU General Public License telle que publiée par
 * la Free Software Foundation ; soit la version 3 de la licence, soit 
 * (à votre gré) toute version ultérieure.
 * 
 * LaShared est distribué dans l'espoir qu'il sera utile, 
 * mais SANS AUCUNE GARANTIE ; pas même la garantie implicite de 
 * COMMERCIABILISABILITÉ ni d'ADÉQUATION à UN OBJECTIF PARTICULIER. 
 * Consultez la GNU General Public License pour plus de détails.
 * 
 * Vous devez avoir reçu une copie de la GNU General Public License 
 * en même temps que LaShared ; si ce n'est pas le cas, 
 * consultez <http://www.gnu.org/licenses>.
 * ----------------------------------------------------------------------------
 * This file is part of LaShared.
 *
 * LaShared is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LaShared is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with LaShared.  If not, see <http://www.gnu.org/licenses/>.
 * ----------------------------------------------------------------------------
 */
package shared.utils.xml;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.logging.Logger;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Permet la serialisation de n'importe quel objet dans un fichier XML.
 * 
 * @author <b>Shionn</b>, shionn@gmail.com <i>http://shionn.org</i><br>
 * GCS d- s+:+ a- C++ UL/M P L+ E--- W++ N K- w-- M+ t+ 5 X R+ !tv b+ D+ G- e+++ h+ r- !y-
 */
public class XMLSerializer {
	private static final Logger logger = Logger.getLogger(XMLSerializer.class
			.getName());

	/* ****************************************************** *
	 * *				CLASSE en BALISE					* *
	 * ****************************************************** */



	/* ****************************************************** *
	 * *				CHAMP en ATTRIBUT					* *
	 * ****************************************************** */

	/**
	 * Enregistre l'enssemble des attributs de la classe par introspection.
	 * Le nom des attribut est automatiquement le même que celui du champ.
	 * Seule les chaine de caractere et les type primitif sont serialisés 
	 * de cette maniere. N'enregistre que les champ de la classe en elle même 
	 * et pas celle dont elle herite.<br><br>
	 * type pris en charge : boolean, int, float, String
	 * @see XMLSerializer.parseAttributes(Element element,Object object)
	 * @param element dans lequel l'objet est serialisé
	 * @param object à serialiser
	 */
	public static void addAttributes(Element element,Object object) {
		logger.entering(XMLSerializer.class.getName(),"addAttributes");
		Class<? extends Object> c = object.getClass();
		while (c != null) {
			Field[] fields = c.getDeclaredFields();
			for (Field field : fields) {
				String name = field.getName();
				try {
					field.setAccessible(true);
					Object value = field.get(object);
					if (value != null) {
						logger.fine("Save attribut ("+name+","+value+")");
						if (value.getClass().equals(String.class) ) 
							element.setAttribute(name, (String) value);
						else if (value.getClass().equals(Integer.class) )
							element.setAttribute(name, Integer.toString((Integer)value));
						else if (value.getClass().equals(Float.class) )
							element.setAttribute(name, Float.toString((Float)value));
						else if (value.getClass().equals(Boolean.class) )
							element.setAttribute(name, Boolean.toString((Boolean)value));
					}
				} catch (IllegalArgumentException e1) {
					logger.warning("IllegalArgumentException sur : "+name);
				} catch (IllegalAccessException e1) {
					logger.warning("IllegalAccessException sur : "+name);
				}

			}
			c = c.getSuperclass();
		}
		logger.exiting(XMLSerializer.class.getName(),"addAttributes");
	}

	/**
	 * Fait l'inverse que la précédente méthode. lit les attributs XML et fixe les valeur de l'objet correspondant.
	 * Même limitation.
	 * @see XMLSerializer.addAttributes(Element element,Object object)
	 * @param element 
	 * @param objet
	 */
	public static void parseAttributes(Element element, Object object) {
		logger.entering(XMLSerializer.class.getName(),"parseAttributes");

		NamedNodeMap attributes = element.getAttributes();
		for (int i=0;i<attributes.getLength();i++) {
			Node node = attributes.item(i);
			String name = node.getNodeName();
			String value = node.getNodeValue();
			try {
				Field field = getField(object.getClass(),name);

				field.setAccessible(true);
				Type type = field.getGenericType();
				if (type == String.class) field.set(object, value);
				else if (type == Integer.TYPE) field.setInt(object, Integer.parseInt(value));
				else if (type == Float.TYPE) field.setFloat(object, Float.parseFloat(value));
				else if (type == Boolean.TYPE) field.setBoolean(object, Boolean.parseBoolean(value));
				else logger.fine("Type non pris en compte : " +type.toString()+ " sur : "+name + ", "+ value);
			} catch (NumberFormatException e) {
				logger.warning("NumberFormatException : Changement de types.");
			} catch (SecurityException e) {
				logger.warning("SecurityException : No Detail.");
			} catch (NoSuchFieldException e) {
				logger.warning("NoSuchFieldException : Un attribut n'existe plus : "+name+" dans "+object.getClass().getName());
			} catch (IllegalArgumentException e) {
				logger.warning("IllegalArgumentException : Le type n'est pas bon : "+name);
			} catch (IllegalAccessException e) {
				logger.warning("IllegalAccessException : Impossible j'ai fait un field.setAccessible(true)");
			} 
		}
		logger.exiting(XMLSerializer.class.getName(),"parseAttributes");
	}

	/**
	 * renvoie le champ d'une class
	 * @param class1
	 * @param name
	 * @return
	 * @throws NoSuchFieldException 
	 */
	private static Field getField(Class<? extends Object> c, String name) throws NoSuchFieldException {
		try {
			return c.getDeclaredField(name);
		} catch (NoSuchFieldException e) {
			if (c.getSuperclass() == null) throw new NoSuchFieldException();
			return getField(c.getSuperclass(), name); 
		}
	}




}
