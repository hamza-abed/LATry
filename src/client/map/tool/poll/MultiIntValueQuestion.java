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
import java.util.logging.Logger;

import org.w3c.dom.Element;

import shared.utils.xml.XMLUtils;
import client.LaTraces;

/**
 *  
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Jérémy depoil, <b>jeremy.depoil@gmail.com</b>, 2009-2011
 */

public class MultiIntValueQuestion extends PollQuestion {

	private ArrayList<String> choices = new ArrayList<String>();
	private ArrayList<String> answers= new ArrayList<String>();
	private ArrayList<String> idAnswer= new ArrayList<String>();
	private int maxSelectedAnswer;
	private int maxPoint;

	/**
	 * @param question
	 */
	public MultiIntValueQuestion(Element question) {
		super(question);
		maxSelectedAnswer = Integer.valueOf(question.getAttribute("maxSelectedAnswer"));
		maxPoint = Integer.valueOf(question.getAttribute("maxPoint"));
		for (Element choice : XMLUtils.getChildElements(question, "choice")) { 
			choices.add(choice.getTextContent());
			answers.add("0");
			idAnswer.add(choice.getAttribute("id"));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see client.map.tool.poll.PollQuestion#addAnswer(org.w3c.dom.Element)
	 */
	@Override
	public void addAnswer(Element parent) {
		Element element = XMLUtils.addElement(parent, "question");
		element.setAttribute("type", "multiintvalue");
		element.setAttribute("question", getQuestion());
		element.setAttribute("id", getId());
		for (int i=0;i<choices.size();i++) {
			Element choice = XMLUtils.addFinalElement(element,"choice","");
			choice.setAttribute("answer",answers.get(i));
			choice.setAttribute("id", idAnswer.get(i));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see client.map.tool.poll.PollQuestion#sendToTrace(client.LaTraces)
	 */
	@Override
	public void sendToTrace(LaTraces traces, String belbin) {
		for (int i = 0; i < choices.size(); i++) {
			Logger.getLogger(this.getClass().getName()).info("Envoie trace : "+i);
			traces.sendPollAnswer(getId(), idAnswer.get(i), answers.get(i), belbin);
		}
	}
	
	/* ********************************************************** *
	 * *					GETTERS / SETTERS					* *
	 * ********************************************************** */
	
	/**
	 * @return the choices
	 */
	public ArrayList<String> getChoices() {
		return choices;
	}

	/**
	 * @param i
	 * @param t
	 */
	public void setAnswers(int i, String t) {
		answers.set(i, t);
	}

	/**
	 * @return
	 */
	public int getMaxSelectedAnswer() {
		return maxSelectedAnswer;
	}

	/**
	 * @return
	 */
	public int getMaxPoint() {
		return maxPoint;
	}

}
