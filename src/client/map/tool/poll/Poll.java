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
package client.map.tool.poll;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import shared.utils.xml.XMLUtils;
import client.LaTraces;

import com.sun.jersey.api.client.Client;

/**
 * Sondage avec ces question et ces réponse  
 * <ul>
 * <li>le sondage est décrit par un fichier XML</li>
 * <li>le sondage est chargé depuis un lien URL accessible< /li>
 * <li>dans le fichier XML, l'attribut belbin indique l'adresse du usermodel qui va traiter les réponses</li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2010-2013
 */
public class Poll {
	private static final Logger logger = Logger.getLogger("Poll");
	private String url;
	private String title;
	
	private ArrayList<PollQuestion> questions = new ArrayList<PollQuestion>();
	private String belbin;


	/**
	 * Construit un sondage à partir d'un fichier XMLaccessible depuis une URL 
	 * @param url (<b>String</b>) : adresse http de depot du sondage 
	 */
	public Poll(String url) {
		this.url = url;
		initialize();
	}

	/**
	 * initialise le contenu du sondage depuis sa représentation XML passé en URL
	 */
	private void initialize() {
		try {
			// recupération du fichier XML
			Element doc = XMLUtils.parseStream(new URL(url).openStream()).getDocumentElement();
			title = doc.getAttribute("title");
			
			// récupération si elle existe de l'adresse du usermodel
			// exemple : http://134.214.147.195:8080/usermodel/belbin
			belbin = doc.getAttribute("belbin");

			for (Element question : XMLUtils.getChildElements(doc, "question"))	{
				String type = question.getAttribute("type");
				if (type.equalsIgnoreCase("qcm"))
					questions.add(new QCM(question));
				else if (type.equalsIgnoreCase("open"))
					questions.add(new OpenQuestion(question));
				else if (type.equalsIgnoreCase("multiintvalue"))
					questions.add(new MultiIntValueQuestion(question));
			}
		} catch (SAXException e) {
			logger.warning("SAXException : Je le savais !");
		} catch (IOException e) {
			logger.warning("IOException : Je le savais !");
		} catch (ParserConfigurationException e) {
			logger.warning("ParserConfigurationException : Je le savais !");
		}	
	}

	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return
	 */
	public ArrayList<PollQuestion> getQuestions() {
		return questions;
	}
	
	/**
	 * 
	 */
	public void sendAnswerToTraces(final LaTraces traces) {
		for (PollQuestion question : questions) 
			question.sendToTrace(traces, belbin);
		traces.getGame().getSchedulerTaskExecutor().schedule(new Runnable() {
			@Override
			public void run() {
				traces.sendPollLastTrace(url, belbin);
			}
		}, 10, TimeUnit.SECONDS);
	}

	/**
	 * reponse à un sondage
	 * @return
	 */
	public Document getDocumentAnswer() {
		try {
			Document doc = XMLUtils.createDocument("la-poll-answer");
			doc.getDocumentElement().setAttribute("title", title);
			doc.getDocumentElement().setAttribute("url", url);
			
			for (PollQuestion question : questions) {
				question.addAnswer(doc.getDocumentElement());
			}
			return doc;
		} catch (ParserConfigurationException e) {
			logger.warning("ParserConfigurationException : Je le savais !");
		}
		return null;
	}

	public String getBelbin() {
		return belbin;
	}
	
	public int getBelbinNbQuestion() {
		//-1 parce que la premier question n'a pas  de réponse
		return questions.size()-1;
	}

	/**
	 * envoi du questionnaire spécifique BELBIN sur le serveur pour un traitement spécifique
	 * @param player : identiant du joueur qui a remplit le questionnaire
	 * @author philippe pernelle
	 */
	public void sendToBelbin(String player) {
		if (belbin == null) return;
		/* obsolete : ancienne méthode par ftp */
		/* new Client().resource(belbin).queryParam("user", player).queryParam("url", 
				"http://manouchian.univ-savoie.fr/~la3/la3-ftp/"+
				FtpManager.POLL+"/"+url).post();//*/
		
		// requete REST contenant les réponses et envoyé au serveur de modele 
		// usager pour le calcul des groupes
		new Client().resource(belbin).queryParam("user", player).entity(
		 		getDocumentAnswer(),MediaType.TEXT_XML_TYPE).post();
	}

}
