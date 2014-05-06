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
package client.hud.editor;

import shared.constants.LaConstants;
import shared.constants.ScriptConstants;
import shared.enums.LaComponent;
import client.hud.Hud;
import client.hud.components.LaBButton;
import client.map.character.Dialog;
import client.utils.FileLoader;

import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BScrollPane;
import com.jmex.bui.BTextField;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.BLabel.Fit;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.icon.IconUtil;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.TableLayout;

/**
 * Permet l'edition de la liste des choix d'un dialog
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class DialogChoicesWindow extends BWindow {

	private Dialog dialog;
	private BContainer panel;

	/**
	 * 
	 * @param hud
	 * @param obj
	 */
	public DialogChoicesWindow(Hud hud, Dialog obj) {
		super("edit window", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/edit-style.bss")), new BorderLayout(
				Hud.SPACE, Hud.SPACE));

		//this.hud = hud;
		this.dialog = obj;

		initialize();
	}

	/**
	 * Initialise le contenu de la fenetre
	 */
	private void initialize() {
		BLabel title = new BLabel("Choices : " + dialog.getKey());
		title.setStyleClass("label-title");
		// title.addListener(new WindowMoveListener(this));
		this.add(title, BorderLayout.NORTH);

		TableLayout layout = new TableLayout(3, 0, 0);
		layout.setHorizontalAlignment(TableLayout.STRETCH);
		panel = new BContainer(layout);
		panel.setStyleClass("container-in-scroll-pane");
		reloadPanel();

		BScrollPane scrollPane = new BScrollPane(panel);
		scrollPane.setStyleClass("container-middle");
		this.add(scrollPane, BorderLayout.CENTER);

		initBottom();

		this.setPreferredSize(250, 450);
		this.pack();
		BuiSystem.addWindow(this);
	}

	/**
	 * recharge la liste des choix
	 */
	private void reloadPanel() {
		panel.removeAll();
		for (int i = 0; i < dialog.getChoices().size(); i++) {

			BLabel label = new BLabel("Choix " + i);
			label.setFit(Fit.SCALE);
			panel.add(label);

			final int index = i;

			final BTextField text = new BTextField(dialog.getChoices().get(i)
					.getA());
			text.addListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					dialog.getChoices().get(index).setA(text.getText());
					reloadPanel();
				}
			});
			panel.add(text);

			LaBButton delete = new LaBButton(IconUtil.getIcon(FileLoader
					.getResourceAsUrl(LaConstants.DIR_LGPL_ICON
							+ "cross-12.png")), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					dialog.getChoices().remove(index);
					reloadPanel();
				}
			}, "delete");
			delete.setStyleClass("button-icon");
			panel.add(delete);

			// ajout de l'edition de l'action associer au choix
			label = new BLabel("Action " + i);
			label.setFit(Fit.SCALE);
			panel.add(label);

			final BTextField script = new BTextField(dialog.getChoices().get(i)
					.getB());
			script.addListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					String str = script.getText();
					if (str.matches(LaComponent.script.regex())
							|| str.matches(LaComponent.dialog.regex())
							|| str.matches(LaComponent.lgf.regex())
							|| str.equals(ScriptConstants.VOID_SCRIPT)
							|| str.equals(ScriptConstants.TRUE_SCRIPT)
							|| str.equals(ScriptConstants.FALSE_SCRIPT))
						dialog.getChoices().get(index).setB(str);
					reloadPanel();
				}
			});
			panel.add(script);

			delete = new LaBButton(IconUtil.getIcon(FileLoader
					.getResourceAsUrl(LaConstants.DIR_LGPL_ICON
							+ "cross-12.png")), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					dialog.getChoices().remove(index);
					reloadPanel();
				}
			}, "delete");
			delete.setStyleClass("button-icon");
			panel.add(delete);
		}
	}

	/**
	 * 
	 */
	private void initBottom() {
		LaBButton add = new LaBButton("add", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dialog.addChoice();
				reloadPanel();
			}
		}, "add");
		this.add(add, BorderLayout.SOUTH);
	}

}
