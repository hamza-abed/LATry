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

import java.util.concurrent.Callable;
import java.util.logging.Logger;

import shared.utils.Couple;
import client.hud.components.LaBButton;
import client.hud.components.WindowMoveListener;
import client.map.character.Dialog;
import client.utils.FileLoader;

//import com.jme.util.GameTaskQueueManager;
import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BScrollPane;
import com.jmex.bui.BTextArea;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.BLabel.Fit;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.Justification;
import com.jmex.bui.layout.Policy;
import com.jmex.bui.layout.VGroupLayout;
import shared.variables.Variables;

/**
 * Fenetre d'affichage de dialog de personnage non joueur
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class NpcDailogWindow extends BWindow implements Callable<Void> {
	private static final Logger logger = Logger.getLogger("NpcDailogWindow");
	private BTextArea textArea;
	private Dialog activDialog;
	private BContainer panel;
	private LaBButton closeButton;
	private Hud hud;

	/**
	 * 
	 */
	public NpcDailogWindow(Hud hud) {
		super("npc dialog window", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl(Variables.getProps().getProperty("hud.bui.dialog.style", "data/dialog.bss"))), 
				new BorderLayout(Hud.SPACE, Hud.SPACE));
		this.hud = hud;
		initialize();
	}

	/**
	 * Initialise le contenu de la fenetre
	 */
	private void initialize() {
		BContainer top = new BContainer(new BorderLayout(0,0));

		BLabel space = new BLabel("");
		space.setStyleClass("close");
		top.add(space, BorderLayout.WEST);

		BLabel title = new BLabel("Dialog");
		title.setStyleClass("label-title");
		title.addListener(new WindowMoveListener(this));
		top.add(title, BorderLayout.CENTER);

		closeButton = new LaBButton("") {
			public boolean isEnabled() {
				return activDialog!=null && activDialog.getChoices().size()==0;
			}
		};
		closeButton.setStyleClass("close");
		closeButton.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (activDialog.getChoices().size()==0)
					setVisible(false);
			}
		});
		top.add(closeButton,BorderLayout.EAST);

		this.add(top, BorderLayout.NORTH);

		VGroupLayout layout = new VGroupLayout( 
				Justification.TOP,
				Policy.NONE);
		layout.setOffAxisJustification(Justification.CENTER);
		layout.setOffAxisPolicy(Policy.STRETCH);
		layout.setGap(0);
		
		panel = new BContainer(layout);
		panel.setStyleClass("dialog-container");

		textArea = new BTextArea("ici il y aurat le dialog quand il serat chargé");
		panel.add(textArea);

		BScrollPane sp = new BScrollPane(panel,true,false);
		sp.setStyleClass("container-middle");
		//sp.getVerticalScrollBar();
		this.add(sp, BorderLayout.CENTER);

		BuiSystem.addWindow(this);
		this.pack();
		this.center();
		this.setLocation(hud.getW()-getWidth()*2-Hud.SPACE*2, getY()+Hud.WINDOW_DECAL_Y);
	}

	/* ********************************************************** *
	 * *	 				Affichage du dialog 				* *
	 * ********************************************************** */

	/**
	 * Insert le dialog de text dans son expace d'affichage
	 */
	private void updateText() {
		textArea.setText(hud.format(activDialog.getText()));
		textArea.validate();
	}

	/**
	 * ajout la liste des choix
	 */
	private void updtateChoices() {
		while (panel.getComponentCount()>1)
			panel.remove(1);

		//	choices.removeAll();
		for (final Couple<String, String> c : activDialog.getChoices()) {
			LaBButton button = new LaBButton(c.getA(), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					setVisible(false);
					activDialog.activChoice(c.getB());
				}
			}, "action");
			button.setFit(Fit.WRAP);
			/*button.setSize(229, 24);
			button.setPreferredSize(229, 24);//*/
			panel.add(button);
		}
		pack();
		
	//	closeButton.setEnabled(panel.getComponentCount() == 1);
	}

	/* ********************************************************** *
	 * * 				Affichage du dialog 					* *
	 * ********************************************************** */

	/**
	 * @param dialog
	 */
	public void display(Dialog dialog) {
		logger.info("on affiche au prochain update  : "+dialog);
		activDialog = dialog;
		//GameTaskQueueManager.getManager().update(this);
                System.out.println("NpcDailogWindow -> display() : GameTaskQueueManager !!");
	}

	/*
	 * (non-Javadoc) 
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
		logger.info("on affiche");
		updateText();
		updtateChoices();

		setVisible(true);
		return null;
	}

	/**
	 * inidique si le dialog est la dialog affiché en ce moemnt
	 * 
	 * @param dialog
	 * @return
	 */
	public boolean isCurrentDialog(Dialog dialog) {
		return isVisible() && dialog == activDialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jmex.bui.BContainer#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		/*if (!visible)
			activDialog = null;//*/
	}
}
