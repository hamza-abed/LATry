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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Regroupe une liste de method permettant de facilité l'acces à des fichiers
 * XML, et de simplifier l'ecriture ou la lecture d'un document DOM.
 * <ul>
 * <li>Simplifie l'acces aux fichier.</li>
 * <li>Construction d'Element</li>
 * <li>Lectuer Element</li>
 * <li></li>
 * </ul>
 * 
 * @author <b>Shionn</b>, shionn@gmail.com <i>http://shionn.org</i><br>
 * GCS d- s+:+ a- C++ UL/M P L+ E--- W++ N K- w-- M+ t+ 5 X R+ !tv b+ D+ G- e+++ h+ r- !y-
 */
public class XMLUtils {
	private static final Logger logger = Logger.getLogger(XMLUtils.class.getName());

	/**
	 * Constante pour l'intenantation
	 */
	private static final String XML_INDENT = "{http://xml.apache.org/xalan}indent-amount";

	/**
	 * Nombre d'espaces pour chaque indentation
	 */
	private static final String XML_INDENT_VALUE = "4";

	/**
	 * Encodage des fichiers
	 */
	private static final String XML_ENCODING = "UTF-8";

	/**
	 * Version XML
	 */
	private static final String XML_VERSION = "1.0";
	
	
	
	/* ****************************************************** *
	 * *					FICHIER							* *
	 * ****************************************************** */

	/**
	 * Raccourci pour parser un fichier XML.
	 * @param file fichier XML que l'on souhait convertir en DOM
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException en cas de probleme lors de la lecture du fichier
	 * @throws SAXException 
	 */
	public static Document parseFile(File file) throws SAXException, IOException, ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
	}
	
	/**
	 * Raccourci pour parser un flux XML.
	 * @param is du  fichier XML que l'on souhait convertir en DOM
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException en cas de probleme lors de la lecture du fichier
	 * @throws SAXException 
	 */
	public static Document parseStream(InputStream is) throws SAXException, IOException, ParserConfigurationException {
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
	}
	
	/**
	 * Sauvegarde un document XML dans le fichier passé en parametre.
	 * @param document document à enregistrer 
	 * @param file Fichier de sortie
	 * @throws TransformerException 
	 */
	public static void saveDocumentToFile(Document document, File file) throws TransformerException {
		logger.entering(XMLUtils.class.getName(), "saveDocumentToFile");

		logger.fine("creation du flux de sortie");
		Result result = new StreamResult(file);

		logger.fine("Configuration du flux XML de sortie");
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, XML_ENCODING);
		transformer.setOutputProperty(XML_INDENT, XML_INDENT_VALUE);
		transformer.setOutputProperty(OutputKeys.VERSION, XML_VERSION);

		logger.fine("transphormation du document dans flux XML");
		transformer.transform(new DOMSource(document), result);

		logger.exiting(XMLUtils.class.getName(), "saveDocumentToFile");
	}
	
	/**
	 * sauvegarde le document
	 * @param doc
	 * @param os
	 * @throws TransformerException 
	 */
	public static void saveDocumentToStream(Document doc, OutputStream os) throws TransformerException {
		Result result = new StreamResult(os);
		
		logger.fine("Configuration du flux XML de sortie");
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		//transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		//transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		//transformer.setOutputProperty(OutputKeys.ENCODING, XML_ENCODING);
		//transformer.setOutputProperty(XML_INDENT, XML_INDENT_VALUE);
		//transformer.setOutputProperty(OutputKeys.VERSION, XML_VERSION);

		logger.fine("transphormation du document dans flux XML");
		transformer.transform(new DOMSource(doc), result);
	}
	
	/* ****************************************************** *
	 * *				CONSTRUCTION DOM					* *
	 * ****************************************************** */
	/**
	 * Raccourci pour creer un nouveau document
	 * @param rootElementTag tag le l'element racine du document
	 * @return le document crée
	 * @throws ParserConfigurationException 
	 */
	public static Document createDocument(String rootElementTag) throws ParserConfigurationException {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		document.setXmlVersion(XML_VERSION);
		document.setXmlStandalone(true);

		// création de l'element root
		Element root = document.createElement(rootElementTag);
		document.appendChild(root);

		return document;
	}
	
	/**
	 * Crais un element XML en fils de l'element parent passé en parametre et le renvoie. 
	 * @param parent du noeud à ajouter
	 * @param tagName nom du noeud
	 * @return le nouveau neoud creer
	 */
	public static Element addElement(Element parent,String tagName) {
		Element element = parent.getOwnerDocument().createElement(tagName);
		parent.appendChild(element);
		return element;
	}

	/**
	 * Crais un element XML en fils de l'element parent passé en parametre et le renvoie. 
	 * Contrairement à la methode présende ici l'element aurat une valeur de type Text.
	 * @param parent du nom
	 * @param tagName nom du noeud
	 * @param value valeur du noeud
	 * @return
	 */
	public static Element addFinalElement(Element parent,String tagName, String value ) {
		Element element = addElement(parent, tagName);
		element.setTextContent(value);
		return element;
	}
	
	/* ****************************************************** *
	 * *				PARCOURT STRUCTURE DOM				* *
	 * ****************************************************** */

	/**
	 * Renvole premier fils de l'element dont le tag correspont
	 * @param element
	 * @param childName
	 * @return
	 */
	public static Element getChildElement(Element element,String childName) {
		logger.entering(XMLUtils.class.getName(), "getChildElement");
		NodeList list = element.getElementsByTagName(childName);
		if(list.getLength() == 0)
			return null;
		logger.exiting(XMLUtils.class.getName(), "getChildElement");
		return (Element) list.item(0);
	}

	/**
	 * Renvole la valeur du premier fils de l'element dont le tag correspont
	 * @param element
	 * @param childName
	 * @return
	 */
	public static String getChildValue(Element element,String childName) {
		Element child = getChildElement(element, childName);
		if (child == null)
			return "null :: no child !!";
		return child.getTextContent();
	}


	/**
	 * Renvoie la liste de tous les fils de l'element dont leurs 
	 * tag correspond au tag donnée. Les fils de tous niveau sont 
	 * concidérer. Sauf dans le cas ou le tagname est "*", alors 
	 * seul tous les fils du premier niveau uniquement sont renvoyés. 
	 * @param element
	 * @param tagName
	 * @return
	 */
	public static Collection<Element> getChildElements(Element element, String tagName) {
		NodeList list = element.getElementsByTagName(tagName);
		ArrayList<Element> elements = new ArrayList<Element>();

		for (int i=0;i<list.getLength();i++)
			if (!tagName.equals("*") || element.isSameNode(list.item(i).getParentNode()))
				elements.add((Element) list.item(i));

		return elements;
	}

	
}
