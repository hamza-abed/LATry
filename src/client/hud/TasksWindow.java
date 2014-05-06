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
import client.map.character.stats.PlayerTasks;
import client.map.character.stats.Task;
import client.utils.FileLoader;

//import com.jme.util.GameTaskQueueManager;
import com.jmex.bui.BCheckBox;
import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BScrollPane;
import com.jmex.bui.BTabbedPane;
import com.jmex.bui.BTextArea;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.BLabel.Fit;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.enumeratedConstants.Orientation;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.event.MouseEvent;
import com.jmex.bui.event.MouseListener;
import com.jmex.bui.icon.IconUtil;
import com.jmex.bui.icon.ImageIcon;
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
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class TasksWindow extends BWindow implements Callable<Void> {
	private LaBButton closeButton;
	private Hud hud;
	private BContainer running;
	private BContainer finished;
	private ImageIcon down;
	private ImageIcon right;
	private Task detailled;

	/**
	 * 
	 */
	public TasksWindow(Hud hud) {
		super("all task window", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/hud/style/task.bss")), 
				new BorderLayout(Hud.SPACE, Hud.SPACE));
		this.hud = hud;
		initialize();
	}

	/**
	 * Initialise le contenu de la fenetre
	 */
	private void initialize() {
		down = IconUtil.getIcon(FileLoader.getResourceAsUrl(
				LaConstants.DIR_LGPL_ICON+"downarrow-12.png"));
		right = IconUtil.getIcon(FileLoader.getResourceAsUrl(
				LaConstants.DIR_LGPL_ICON+"rightarrow-12.png"));

		initTop();
		initMiddle();

		BuiSystem.addWindow(this);
		this.pack();
		this.center();
		this.setLocation(hud.getW()-getWidth()*1-Hud.SPACE*1, getY()+Hud.WINDOW_DECAL_Y);
	}

	/**
	 * init middle
	 */
	private void initMiddle() {
		VGroupLayout layout = new VGroupLayout( 
				Justification.TOP,
				Policy.NONE);
		layout.setOffAxisJustification(Justification.CENTER);
		layout.setOffAxisPolicy(Policy.STRETCH);
		layout.setGap(0);
		running = new BContainer(layout);

		layout = new VGroupLayout( 
				Justification.TOP,
				Policy.NONE);
		layout.setOffAxisJustification(Justification.CENTER);
		layout.setOffAxisPolicy(Policy.STRETCH);
		layout.setGap(0);
		finished = new BContainer(layout);

		BTabbedPane tabbedPane = new BTabbedPane(Justification.CENTER, 45);
		tabbedPane.setStyleClass("tab-container");

		tabbedPane.addTab(hud.getLocalText("quests.running.tab"), running);
		tabbedPane.addTab(hud.getLocalText("quests.finished.tab"), finished);

		BScrollPane sp = new BScrollPane(tabbedPane,true,false);
		sp.setStyleClass("container-middle");
		this.add(sp, BorderLayout.CENTER);
	}

	/**
	 * initialise la barre superieur
	 */
	private void initTop() {
		BContainer top = new BContainer(new BorderLayout(0,0));

		BLabel space = new BLabel("");
		space.setStyleClass("close");
		top.add(space, BorderLayout.WEST);

		BLabel title = new BLabel(hud.getLocalText("quests.title"));
		title.setStyleClass("label-title");
		title.addListener(new WindowMoveListener(this));
		top.add(title, BorderLayout.CENTER);

		closeButton = new LaBButton("");
		closeButton.setStyleClass("close");
		closeButton.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		top.add(closeButton,BorderLayout.EAST);

		this.add(top, BorderLayout.NORTH);
	}



	/* ********************************************************** *
	 * *	 				mise à jour 						* *
	 * ********************************************************** */

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
		finished.removeAll();
		running.removeAll();
		final PlayerTasks pt = hud.getPlayer().getTasks();
		for (final Task t : pt.list()) { 
			if (!Variables.getWorld().isUpdate(t)) 
				Variables.getClientConnecteur().updateFromServerAndWait(t);

			BLabel l = new BLabel(t.getName());
			l.setStyleClass("quest-title");
			l.setIcon(t==detailled?down:right);
			l.addListener(new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent event) {
					if (detailled == t) detailled = null;
					else detailled = t;
					requestRefresh();
				}
				@Override
				public void mousePressed(MouseEvent event) { }
				@Override
				public void mouseExited(MouseEvent event) { }
				@Override
				public void mouseEntered(MouseEvent event) { }
			});

			if (pt.hasSuccesTask(t.getId())) finished.add(l);
			else {
				running.add(l);
				final BCheckBox follow = new BCheckBox(hud.getLocalText("quests.follow"));
				follow.setSelected(pt.isFollowed(t.getId()));
				follow.addListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						pt.setFollowed(t.getId(), follow.isSelected());
					}
				});
				follow.setOrientation(Orientation.HORIZONTAL);
				follow.setFit(Fit.SCALE);
				running.add(follow);
			}

			if (detailled == t) {

				BTextArea text = new BTextArea(hud.format(t.getDescription()));
				if (pt.hasSuccesTask(t.getId())) finished.add(text);
				else running.add(text);
				int objectiv = 0;
				for (Couple<String, Integer> obj : t.getAllObjectives()) {
					if (pt.isObjectivVisible(t.getId(), objectiv)){
						BLabel la = new BLabel(obj.getA());
						la.setStyleClass(pt.hasSuccesObjectiv(t.getId(), objectiv)?"objectiv-succes":"objectiv");
						if (pt.hasSuccesTask(t.getId())) finished.add(la);
						else running.add(la);
					}
					objectiv++;

				}

			}

		}
		return null;
	}


	/**
	 * mise à jour
	 */
	public void requestRefresh() {
		if (isVisible())
			//GameTaskQueueManager.getManager().update(this);
                    System.out.println("TasksWindow -> requestRefresh() : vide !!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jmex.bui.BContainer#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) requestRefresh();
	}

}
