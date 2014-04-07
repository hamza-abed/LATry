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



import java.io.StringWriter;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;




/**
 * Permet la construction simplifié de page HTML 
 * <ul>
 * <li>Cette classe est entierement pomper depuis un code personnel. J'ai pas envie de tous reecrire</li>
 * <li></li>
 * </ul>
 * 
 * 
 * Code sous licence GPLv3 (http://www.gnu.org/licenses/gpl.html)
 *
 * @author <b>Shionn</b>, shionn@gmail.com <i>http://shionn.org</i><br>
 * GCS d- s+:+ a- C++ UL/M P L+ E--- W++ N K- w-- M+ t+ 5 X R+ !tv b+ D+ G- e+++ h+ r- !y-
 */
public class HtmlBuilder {

	private Document doc;

	private HtmlBuilder parent;

	private Element element;

	/**
	 * @throws ParserConfigurationException 
	 * 
	 */
	private HtmlBuilder() throws ParserConfigurationException {
		doc = XMLUtils.createDocument("html");
		element = doc.getDocumentElement();
		HtmlBuilder head = new HtmlBuilder(this, "head");
		HtmlBuilder style = new HtmlBuilder(head,"style");
		String css = "* {font-size : 11px;font-family: sans-serif; } \n" ;
				//"table { border: 0px hidden  #000000; empty-cells: show; background: #000000; } \n" +
				//"td { background: #F0F0F0; }";
		style.contend(css);
		
	}
	
	/**
	 * @param htmlBuilder
	 * @param string
	 */
	private HtmlBuilder(HtmlBuilder parent, String tag,String... attributs) {
		this.parent = parent;
		this.element = XMLUtils.addElement(parent.element,tag);
	}

	/* ********************************************************** *
	 * *						BALISE							* *
	 * ********************************************************** */
	
	public static HtmlBuilder html() throws ParserConfigurationException {
		return new HtmlBuilder();
	}

	/**
	 * ajoute la balise body
	 * @return
	 */
	public HtmlBuilder body() {
		return new HtmlBuilder(this,"body").attribut("font-size", "10");
	}

	/**
	 * ajoute la balise table
	 * @return
	 */
	public HtmlBuilder table() {
		return new HtmlBuilder(this,"table");
	}

	/**
	 * ajoute la balise tr
	 * @return
	 */
	public HtmlBuilder tr() {
		return new HtmlBuilder(this,"tr");
	}

	/**
	 * Ajout la balise td
	 * @return
	 */
	public HtmlBuilder td() {
		return new HtmlBuilder(this,"td");
	}
	
	/**
	 * a ajoute une balise a et spécifit l'attribut href de la balise a
	 * @return
	 */
	public HtmlBuilder a(String href) {
		return new HtmlBuilder(this,"a").attribut("href",href);
	}
	
	/**
	 * ajoute une balise ul
	 * @return
	 */
	public HtmlBuilder ul() {
		return new HtmlBuilder(this,"ul");
	}
	
	/**
	 * Ajoute une balise li
	 * @return
	 */
	public HtmlBuilder li() {
		return new HtmlBuilder(this,"li");
	}
	
	/**
	 * Ajoute une balise center
	 * @return
	 */
	public HtmlBuilder center() {
		return new HtmlBuilder(this,"center");
	}

	/**
	 * ajoute une balise br
	 * @return
	 */
	public HtmlBuilder br() {
		return new HtmlBuilder(this,"br");
	}
	
	/**
	 * Ajoute une balise img et spécifi la source de l'image
	 * @param src
	 * @return
	 */
	public HtmlBuilder img(String src) {
		return new HtmlBuilder(this, "img").attribut("src", src);
	}
	
	/**
	 * met en gras
	 * @return
	 */
	public HtmlBuilder b() {
		return new HtmlBuilder(this,"b");
	}
	
	/**
	 * souligne
	 * @return
	 */
	public HtmlBuilder u() {
		return new HtmlBuilder(this,"u");
	}
	
	
	/* ********************************************************** *
	 * *						Contenu							* *
	 * ********************************************************** */

	/**
	 * Definiti le contenu text d'une balise 
	 */
	public HtmlBuilder contend(String text) {
		element.appendChild(getDocument().createTextNode(text==null?"null":text));
		//element.setTextContent(text);
		return this;
	}
	
	/**
	 * change la valeur d'un attribut d'une balise
	 * @param attrib 
	 * @param value
	 * @return
	 */
	public HtmlBuilder attribut(String attrib, String value) {
		this.element.setAttribute(attrib, value);
		return this;
	}

	
	
	/* ********************************************************** *
	 * *					TRANSPORMATION						* *
	 * ********************************************************** */
	
	/**
	 * Transphorme le document en String
	 * @return
	 */
	public String DOM2String() {
		
		DOMSource domSource = new DOMSource(this.getDocument());
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		
		TransformerFactory tf = TransformerFactory.newInstance();
	
		try {
			Transformer transformer = tf.newTransformer();
			//transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(domSource, result);
			
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String stringResult = writer.toString();
		
		return stringResult;

	}
	

	/**
	 * Renvoie le document Html correspondant à la page ecrite
	 * @return
	 */
	public Document getDocument() {
		if (parent == null)
			return doc;
		return parent.getDocument();
	}

	/**
	 * Transphorme le document en une reponse Rest
	 * @return
	 */
	public Response response() {
		return Response.ok(this.getDocument(),MediaType.TEXT_HTML).build();
	}



	

}
