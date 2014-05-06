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

import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import client.hud.components.LaBButton;
import client.hud.components.WindowMoveListener;
import client.map.character.Player;
import client.map.character.stats.Item;
import client.utils.FileLoader;

//import com.jme.util.GameTaskQueueManager;
import com.jmex.bui.BComponent;
import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BScrollPane;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.BLabel.Fit;
import com.jmex.bui.background.BBackground;
import com.jmex.bui.background.ImageBackground;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.enumeratedConstants.ImageBackgroundMode;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.image.ImageUtil;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.TableLayout;
import shared.variables.Variables;

/**
 * Interface du sac du joueur
 * <ul>
 * <li>PROPOSE afficher le sac du groupe ?</li>
 * <li>PROPOSE afficher le sac de la guilde ?</li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class BagWindow extends BWindow implements Callable<Void>{
	private static final Logger logger = Logger.getLogger("BagWindow");
	private Hud hud;
	private BContainer itemsContainer;

	public BagWindow(Hud hud) {
		super("bag window", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/bag-style.bss")), new BorderLayout(
				0, 0));
		this.hud = hud;

		initialize();
	}

	/**
	 * initialise la fenetre
	 */
	private void initialize() {
		BContainer top = new BContainer(new BorderLayout(0,0));
		
		BLabel space = new BLabel("");
		space.setStyleClass("close");
		top.add(space, BorderLayout.WEST);
		
		BLabel title = new BLabel(hud.getLocalText("bag.title"));
		title.setStyleClass("label-title");
		title.addListener(new WindowMoveListener(this));
		top.add(title,BorderLayout.CENTER);
		
		LaBButton closeButton = new LaBButton("");
		closeButton.setStyleClass("close");
		closeButton.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		top.add(closeButton,BorderLayout.EAST);
				
		this.add(top, BorderLayout.NORTH);

		TableLayout layout = new TableLayout(5, Hud.SPACE, Hud.SPACE);
		layout.setEqualRows(true);
		layout.setHorizontalAlignment(TableLayout.CENTER);
		itemsContainer = new BContainer(layout);
		itemsContainer.setStyleClass("items-container");

		BScrollPane sp = new BScrollPane(itemsContainer);
		sp.setStyleClass("container-middle");
		this.add(sp, BorderLayout.CENTER);

		this.setVisible(false);
		
		BuiSystem.addWindow(this);
		this.pack();
		this.center();
		this.setLocation(hud.w-getWidth()-Hud.SPACE, getY()+Hud.WINDOW_DECAL_Y);
		
		//refresh();
	}

	/**
	 * Recharge le contenu du sac
	 */
	private void reloadItems() {
		itemsContainer.removeAll();
		Player player = Variables.getMainPlayer();

		if (player != null)
			for (Entry<Item, Integer> entry : player.getItems().getAllItem()
					.entrySet()) {

				final Item item = entry.getKey();

				logger.fine("Ajout de l'item au sac :" + item.getName());
				BBackground back = new ImageBackground(
						ImageBackgroundMode.CENTER_XY, ImageUtil.getImage(item
								.getIconUrl()));

				
				LaBButton button = new LaBButton(
						Integer.toString(entry.getValue()),
						new ActionListener() {
							public void actionPerformed(ActionEvent event) {
								item.exec();
							}
						}, item.getName());

				button.setFit(Fit.SCALE);
				
				button.setBackground(BComponent.DEFAULT, back);
				button.setBackground(BComponent.HOVER, back);
				button.setBackground(LaBButton.DOWN, back);

				button.setStyleClass("button-icon");
				button.setTooltipText(item.getName() + " (x" + entry.getValue()	+ ")");
				button.setTooltipRelativeToMouse(true);

				button.setPreferredSize(42, 42);
				button.setSize(42, 42);

				itemsContainer.add(button);
			}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jmex.bui.BContainer#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible) reloadItems();
		super.setVisible(visible);
	}
	
	/* ********************************************************** *
	 * * 			Rafraichir le contenu du Sac 				* * 
	 * ********************************************************** */
	/**
	 * rafraichit l'interface
	 */
	public void refresh() {
            System.out.println("BagWindow -> refresh() : GameTaskQueueManager désactivée !!");
		//GameTaskQueueManager.getManager().update(this);
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Void call() throws Exception {
		this.reloadItems();
		return null;
	}

}
