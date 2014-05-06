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
package client.hud.editor.annotation;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import client.editor.annotation.AnnotationEditor;
import client.hud.Hud;
import client.hud.components.LaBButton;

import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BLabel.Fit;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.TableLayout;

/**
 * Composant de l'editeur par annotation d'un text. 
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class TextEdit extends BContainer {
	private Field field;
	private AnnotationEditor editor;

	
	/**
	 * @param editAnnotationWindow
	 * @param f
	 */
	public TextEdit(EditAnnotationWindow editAnnotationWindow, Field f) {
		super();
		
		this.editor = editAnnotationWindow.getEditor();
		this.field = f;
		

		TableLayout layout = new TableLayout(2,0,Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);
		this.setLayoutManager(layout);
				
		BLabel label = new BLabel(f.getName());
		label.setFit(Fit.SCALE);
		this.add(label);
		
		LaBButton edit = new LaBButton("edit", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				new TextEditWindow();
			}
		}, "edit");
		this.add(edit);
	}
	
	
	private class TextEditWindow extends JFrame {
		private static final long serialVersionUID = 990163767580049982L;
		private JPanel mainPanel;
		private JTextArea textEditor;
		private JPanel southPnal;
		private JButton cancel;
		private JButton apply;

		/**
		 * permet l'edition sous forme de text du champ field dans le model name
		 * 
		 * @param model
		 * @param name
		 */
		public TextEditWindow() {
			initialize();
		}

		/**
		 * Initialise le contenu de la fenetre
		 */
		private void initialize() {
			setTitle("LA3 TextEditor : " + field);
			setContentPane(getMainPanel());
			setSize(250, 350);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setLocationRelativeTo(null);
			setVisible(true);
		}

		/**
		 * renvoie le panneau de controel principal
		 * 
		 * @return
		 */
		private Container getMainPanel() {
			if (mainPanel == null) {
				mainPanel = new JPanel(new java.awt.BorderLayout());
				mainPanel.add(new JScrollPane(getTextEditor()), java.awt.BorderLayout.CENTER);
				mainPanel.add(getButtonsPanel(), java.awt.BorderLayout.SOUTH);
			}
			return mainPanel;
		}

		/**
		 * Renvoie le panneau posseddant les 2 bouton de controle
		 * 
		 * @return
		 */
		private Component getButtonsPanel() {
			if (southPnal == null) {
				southPnal = new JPanel(new GridLayout(0, 2));
				southPnal.add(getCancelButton());
				southPnal.add(getApplyButton());
			}
			return southPnal;
		}

		/**
		 * @return
		 */
		private Component getApplyButton() {
			if (apply == null) {
				apply = new JButton("Apply");
				apply.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent e) {
						editor.setStr(field, getTextEditor().getText());
						setVisible(false);
						dispose();
					}
				});
			}
			return apply;
		}

		/**
		 * @return
		 */
		private Component getCancelButton() {
			if (cancel == null) {
				cancel = new JButton("Cancel");
				cancel.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(java.awt.event.ActionEvent e) {
						setVisible(false);
						dispose();
					}
				});
			}
			return cancel;
		}

		/**
		 * @return
		 */
		private JTextArea getTextEditor() {
			if (textEditor == null) {
				textEditor = new JTextArea(editor.get(field).toString());
				textEditor.setLineWrap(true);
			}
			return textEditor;
		}

	}

}
