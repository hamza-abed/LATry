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

import java.util.ArrayList;

import org.w3c.dom.Element;

import shared.utils.xml.XMLUtils;
import client.LaTraces;

/**
 * question e type qCM pour un poll 
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class QCM extends PollQuestion {
	private ArrayList<String> choices = new ArrayList<String>();
	private ArrayList<Boolean> correctAnswer = new ArrayList<Boolean>();
	private ArrayList<Boolean> answer = new ArrayList<Boolean>();

	/**
	 * @param question
	 */
	public QCM(Element question) {
		super(question);
		for (Element choice : XMLUtils.getChildElements(question, "choice")) { 
			choices.add(choice.getTextContent());
			correctAnswer.add(choice.getAttribute("valid").equalsIgnoreCase("true"));
			answer.add(false);
		}
		
	}

	/**
	 * @return the choices
	 */
	public ArrayList<String> getChoices() {
		return choices;
	}
	
	
	public void setAnswer(int i,boolean b) {
		answer.set(i, b);
	}

	/* (non-Javadoc)
	 * @see client.map.tool.poll.PollQuestion#addAnswer(org.w3c.dom.Element)
	 */
	@Override
	public void addAnswer(Element parent) {
		Element element = XMLUtils.addElement(parent, "question");
		element.setAttribute("type", "qcm");
		element.setAttribute("question", getQuestion());
		
		for (int i=0;i<choices.size();i++) {
			Element choice = XMLUtils.addFinalElement(element,"choice",choices.get(i));
			choice.setAttribute("valid", Boolean.toString(correctAnswer.get(i)));
			choice.setAttribute("answer", Boolean.toString(answer.get(i)));
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see client.map.tool.poll.PollQuestion#sendToTrace(client.LaTraces, java.lang.String)
	 */
	@Override
	public void sendToTrace(LaTraces traces, String belbin) {
		// TODO envoi des traces
	}

}
