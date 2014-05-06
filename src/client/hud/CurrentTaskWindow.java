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

import shared.constants.LaConstants;
import shared.utils.Couple;
import client.hud.components.LaBButton;
import client.hud.components.WindowMoveListener;
import client.map.character.Player;
import client.map.character.stats.Task;
import client.utils.FileLoader;


import com.jmex.bui.BComponent;
import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BTextArea;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.icon.IconUtil;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.TableLayout;
import shared.variables.Variables;

/**
 * Fenetre d'affichage de la tache courante 
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class CurrentTaskWindow extends BWindow implements Runnable, Callable<Void> {
	private Hud hud;

	private BContainer panel;

	private Player player;

	/**
	 * @param hud
	 */
	public CurrentTaskWindow(Hud hud) {
		super("tasDescription window", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/hud/style/current-task.bss")), 
				new BorderLayout(Hud.SPACE, Hud.SPACE));
		this.hud = hud;

		initialize();
	}

	/**
	 * initialise le contenu de la fenetre
	 */
	private void initialize() {
		BLabel title = new BLabel(hud.getLocalText("minitodo.title"));
		title.setStyleClass("label-title");
		this.add(title,BorderLayout.NORTH);

		panel = new BContainer(new TableLayout(1, Hud.SPACE, 0));
		this.add(panel,BorderLayout.CENTER);

		this.setSize(200, 250);

		BuiSystem.addWindow(this);
		setLayer(-1);
		this.setLocation(hud.getW()-200-Hud.SPACE, hud.getH()-200-250-Hud.SPACE);
	}

	/**
	 * 
	 */
	private void refresh() {
		player = Variables.getMainPlayer();
		panel.removeAll();

		for (final Task task : player.getFollowedsTask()) {
			if (! Variables.getWorld().isUpdate(task)) {
				Variables.getClientConnecteur().updateFromServerAndWait(task, this);
				return;
			}

			LaBButton label = new LaBButton(task.getName());
			label.setStyleClass("label-task");
			label.setIcon(IconUtil.getIcon(FileLoader.getResourceAsUrl(
					LaConstants.DIR_CC_BY_ICON+"eye-12.png")));
			label.addListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					new TaskDescriptionWindow(task);
				}
			});
			panel.add(label);

			int objectiv = 0;
			for (Couple<String, Integer> o : task.getAllObjectives()) {
				if (player.hasObjectivVisible(task.getId(), objectiv)) {
					boolean succes = player.hasSuccesObjectiv(task.getId(), objectiv);
					BLabel l = new BLabel(o.getA());
					l.setIcon(IconUtil.getIcon(FileLoader.getResourceAsUrl(
							LaConstants.DIR_LGPL_ICON+(succes?"ok-12.png":"cross-12.png"))));
					l.setStyleClass("task-"+o.getB());

					panel.add(l);					
				}
				objectiv++;
			}
			setVisible(true);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		requestRefresh();
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
		refresh();
		return null;
	}

	/**
	 * demande une mise à jour dans le thraed fait pour
	 */
	public void requestRefresh() {
		//GameTaskQueueManager.getManager().update(this);
            System.out.println("CurrentTaskWindow -> requestRefresh() : GameTaskQueueManager !!!");
	}

	/**
	 * Fenetre  d'affichage de la description de quete
	 * 
	 * 
	 * @author Ludovic, Syscom 2009-2011
	 */
	private class TaskDescriptionWindow extends BWindow {
		/**
		 * 
		 */
		public TaskDescriptionWindow(Task task) {
			super("description window", BStyleSheetUtil.getStyleSheet(FileLoader
					.getResourceAsUrl("data/task.bss")), 
					new BorderLayout(Hud.SPACE, Hud.SPACE));

			BLabel title = new BLabel(task.getName());
			title.setStyleClass("label-title");
			title.addListener(new WindowMoveListener(this));
			this.add(title, BorderLayout.NORTH);

			String text = task.getDescription();
			if (text == null || text.length() == 0)
				text = hud.getLocalText("task.nodescription");

			BTextArea textArea = new BTextArea(hud.format(text));
			this.add(textArea, BorderLayout.CENTER);

			LaBButton close = new LaBButton(hud.getLocalText("task.close"), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					setVisible(false);
				}
			}, "close");
			this.add(close,BorderLayout.SOUTH);

			this.setPreferredSize(250, 380);
			this.pack();
			this.center();
			BuiSystem.addWindow(this);
		}
	}

	/**
	 * insdique si la souris est au dessus de la fenetre (cas spécial a cause de sa transparence
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isMouseOver(int x, int y) {
		BComponent c = getHitComponent(x, y);
		return c != null && c instanceof BLabel;
	}	

}
