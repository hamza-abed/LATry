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
package client.hud;

import client.hud.components.LaBButton;
import client.hud.components.WindowMoveListener;
import client.utils.FileLoader;

import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.HGroupLayout;
import com.jmex.bui.layout.Justification;
import com.jmex.bui.layout.Policy;

/**
 * Fenetre pour posé des question
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class YesNoQuestionWindow extends BWindow {
	private Hud hud;
	private BLabel title;
	private String question;
	private YesNoListener listener;

	public YesNoQuestionWindow(Hud hud,String question,YesNoListener listener) {
		super("Open Question", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/hud/style/yesnoquestion.bss")), new BorderLayout(
				Hud.SPACE, Hud.SPACE));
		this.hud = hud;	
		this.question = question;
		this.listener = listener;
		
		initialize();
	}
	
	
	/* ********************************************************** *
	 * *				Initialise le contenu 					* *
	 * ********************************************************** */

	/**
	 * 
	 */
	private void initialize() {
		title = new BLabel(hud.getLocalText("yesnoquestion.title"));
		title.setStyleClass("label-title");
		title.addListener(new WindowMoveListener(this));
		this.add(title, BorderLayout.NORTH);
		
		BLabel label = new BLabel(question);
		this.add(label,BorderLayout.CENTER);
		
		BContainer panel = new BContainer(new HGroupLayout(Justification.CENTER,Policy.STRETCH));
		this.add(panel,BorderLayout.SOUTH);
		
		panel.add(new LaBButton(hud.getLocalText("yesnoquestion.button.no"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				listener.answer(false);
				setVisible(false);
			}
		}, "valid"));		
		panel.add(new LaBButton(hud.getLocalText("yesnoquestion.button.yes"), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				listener.answer(true);
				setVisible(false);
			}
		}, "valid"));
		
		BuiSystem.addWindow(this);
		this.pack();
		this.center();
	}
	
	/* (non-Javadoc)
	 * @see com.jmex.bui.BContainer#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(!visible)
			BuiSystem.removeWindow(this);
	}
	
	
	
	public interface YesNoListener {

		/**
		 * 
		 * @param yerorno
		 */
		void answer(boolean yerorno);
		
	}
}
