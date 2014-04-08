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
package client.map.tool.blackboard;

import java.awt.Font;
import java.awt.Point;
import java.util.logging.Logger;

//import client.hud.Hud;
//import client.hud.components.WindowMoveListener;
import client.utils.FileLoader;

import com.jmex.bui.BCheckBox;
import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BTextField;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.event.KeyEvent;
import com.jmex.bui.event.KeyListener;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.TableLayout;

/**
 * Popup permettant de modifier quelques attributs d'un BBText comme son texte,
 * sa taille et son style. Demande au tableau blanc associé de se raffraichir à
 * chaque modification.
 * 
 * @author Syscom
 */
public class TextDialog extends Thread implements ActionListener, KeyListener {
	private static final Logger logger = Logger.getLogger(TextDialog.class
			.getName());
	private static final long serialVersionUID = 6804144187314540665L;
	private BWindow dialog;
	private BTextField field, size;
	private BCheckBox bold, italic;

	private BlackboardEngine blackboard;
	private Texte bbtext;
	private Point position;

	public TextDialog(BlackboardEngine blackboard, Texte bbtext, Point position) {
		this.blackboard = blackboard;
		this.bbtext = bbtext;
		this.position = position;
	}

	/**
	 * Création de la boîte de dialogue
	 */
	private void createDialog() {
		logger.entering(TextDialog.class.getName(), "createDialog");

		/*dialog = new BWindow(BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/textedit-style.bss")),
				new BorderLayout(Hud.SPACE, Hud.SPACE));

		// TOP
		BLabel title = new BLabel(blackboard.getTool().getWorld().getGame()
				.getHud().getLocalText("blackboard.textedit.title"));
		title.addListener(new WindowMoveListener(dialog));
		dialog.add(title, BorderLayout.NORTH);

		// Middle
		BLabel textLabel = new BLabel("Texte");
		field = new BTextField(bbtext.getText());
		field.setPreferredWidth(100);
		field.addListener(this);

		BLabel sizeLabel = new BLabel("Taille");
		size = new BTextField(Integer.toString(bbtext.getSize()));
		size.setPreferredWidth(100);
		size.addListener(this);

		TableLayout layout = new TableLayout(2, Hud.SPACE, Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);
		BContainer panel = new BContainer(layout);
		panel.add(textLabel);
		panel.add(field);
		panel.add(sizeLabel);
		panel.add(size);

		dialog.add(panel, BorderLayout.CENTER);

		// Bottom
		bold = new BCheckBox("Gras");
		bold.setSelected((bbtext.getStyle() & Font.BOLD) != 0);
		bold.addListener(this);

		italic = new BCheckBox("Italique");
		italic.setSelected((bbtext.getStyle() & Font.ITALIC) != 0);
		italic.addListener(this);

		TableLayout layout2 = new TableLayout(2, Hud.SPACE, Hud.SPACE);
		layout2.setHorizontalAlignment(TableLayout.STRETCH);
		BContainer panel2 = new BContainer(layout);
		panel2.add(bold);
		panel2.add(italic);

		dialog.add(panel2, BorderLayout.SOUTH);

		BuiSystem.addWindow(dialog);
		dialog.pack();
		if (position == null)
			dialog.center();
		else
			dialog.setLocation(position.x, position.y);

		logger.exiting(TextDialog.class.getName(), "createDialog");
                */
	}

	@Override
	public void run() {
		logger.entering(TextDialog.class.getName(), "run");

		createDialog();
		dialog.setVisible(true);

		logger.exiting(TextDialog.class.getName(), "run");
	}

	public void dispose() {
		logger.entering(TextDialog.class.getName(), "dispose");
		blackboard.saveBbtdPosition(new Point(dialog.getAbsoluteX(), dialog
				.getAbsoluteY()));
		dialog.dismiss();
		blackboard.sendShape(bbtext);
		logger.exiting(TextDialog.class.getName(), "dispose");
	}

	public void kill() {
		logger.entering(TextDialog.class.getName(), "kill");
		blackboard.saveBbtdPosition(new Point(dialog.getAbsoluteX(), dialog
				.getAbsoluteY()));
		dialog.dismiss();
		logger.exiting(TextDialog.class.getName(), "kill");
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		logger.entering(TextDialog.class.getName(), "actionPerformed");
		updateText();
		logger.exiting(TextDialog.class.getName(), "actionPerformed");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		logger.entering(TextDialog.class.getName(), "keyReleased");
		updateText();
		logger.exiting(TextDialog.class.getName(), "keyReleased");
	}

	/**
	 * Applique les mises à jour sur le BBText et répercute sur le tableau
	 * blanc.
	 */
	private void updateText() {
		logger.entering(TextDialog.class.getName(), "updateText");

		int s = Font.PLAIN | (bold.isSelected() ? Font.BOLD : 0)
				| (italic.isSelected() ? Font.ITALIC : 0);

		try {
			bbtext.setSize(Integer.parseInt(size.getText()));
		} catch (Exception e) {
			bbtext.setSize(20);
		}
		bbtext.setStyle(s);
		bbtext.setText(field.getText());

		blackboard.repaint();

		logger.exiting(TextDialog.class.getName(), "updateText");
	}

	// Ne sert à rien.
	@Override
	public void keyPressed(KeyEvent e) {
	}
}
