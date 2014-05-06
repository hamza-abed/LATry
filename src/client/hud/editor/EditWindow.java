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

import java.util.logging.Logger;

import client.editor.EditModel;
import client.hud.Hud;
import client.hud.components.LaBButton;
import client.hud.components.WindowMoveListener;
import client.interfaces.graphic.Graphic;
import client.interfaces.network.SharableEditable;
import client.map.character.Dialog;
import client.utils.FileLoader;

import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BScrollPane;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.BLabel.Fit;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.TableLayout;
import shared.variables.Variables;

/**
 * Fenetre d'edition d'un objet du monde
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2010-2013
 */
public class EditWindow extends BWindow {
	private static final Logger logger = Logger.getLogger("GraphicEditWindow");
	private Hud hud;
	private SharableEditable obj;
	private EditModel model;
	private BContainer panel;
	private DialogChoicesWindow dialog;

	/**
	 * Constructeur de la fenetre 
	 * @param hud : racine des objets de l'utilisateur
	 * @param obj : objet à créer de type <b>SharableEditable</b>
	 */
	public EditWindow(Hud hud, SharableEditable obj) {
		super("edit window", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/edit-style.bss")), new BorderLayout(
				Hud.SPACE, Hud.SPACE));
		this.hud = hud;
		this.obj = obj;
		this.model = obj.getEditModel();

		if (obj instanceof Dialog)
			this.dialog = new DialogChoicesWindow(hud, (Dialog) obj);

		initialize();

	}

	/**
	 * Initialise le contenu de la fenetre
	 */
	private void initialize() {
		String titleTxt = "Editor : " + obj.getKey();
		if (obj instanceof Graphic)
			titleTxt += " (" + ((Graphic) obj).getGraphic().getTriangleCount()
					+ ")";

		BLabel title = new BLabel(titleTxt);
		title.setStyleClass("label-title");
		title.addListener(new WindowMoveListener(this));
		this.add(title, BorderLayout.NORTH);

		initMiddle();
		initBottom();

		this.setPreferredSize(350, 450);
		this.pack();
		BuiSystem.addWindow(this);

		this.center();
		this.setLocation(hud.w - 350, getY());
	}

	/**
	 * Initialise le panneau principal
	 */
	private void initMiddle() {
		TableLayout layout = new TableLayout(2, Hud.SPACE, Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);

		panel = new BContainer(layout);
		panel.setStyleClass("container-in-scroll-pane");
		for (String name : model.getFieldList()) {
			BLabel label = new BLabel(name);
			label.setFit(Fit.WRAP);
			label.setPreferredSize(200, -1);
			panel.add(label);
			panel.add(new EditWindowComponent(hud, model, name));
		}

		BScrollPane scrollPane = new BScrollPane(panel);
		scrollPane.setStyleClass("container-middle");
		this.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Initialise la partie basse de la fenetre
	 */
	private void initBottom() {
		LaBButton update = new LaBButton("update", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				hud.openNotYetImplementedPopup();
			}
		}, "update");
		update.setEnabled(true);
		LaBButton cancel = new LaBButton("cancel", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				logger.info("annule les precedentes modificatons");
				model.rewind();
				close();
			}
		}, "cancel");
		LaBButton ok = new LaBButton("apply", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Variables.getClientConnecteur().commitOnServer(obj);
				model.clearUnDos();
				close();
			}
		}, "ok");

		TableLayout layout = new TableLayout(3, 0, Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);
		BContainer bottom = new BContainer(layout);
		bottom.add(update);
		bottom.add(cancel);
		bottom.add(ok);
		this.add(bottom, BorderLayout.SOUTH);
	}

	/* ********************************************************** *
	 * * 					SETTERS/GETTERS 					* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jmex.bui.BComponent#setLocation(int, int)
	 */
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		if (dialog != null)
			dialog.setLocation(x + this.getWidth(), y);
	}

	/**
	 * Renvoie le contenaeur graphique
	 * 
	 * @return
	 */
	public Hud getHud() {
		return hud;
	}

	public void close() {
		BuiSystem.removeWindow(this);
		if (dialog != null)
			BuiSystem.removeWindow(dialog);

	}
}
