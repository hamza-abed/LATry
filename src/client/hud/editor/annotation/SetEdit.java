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

import java.lang.reflect.Field;
import java.util.Set;

import shared.constants.LaConstants;
import client.editor.annotation.AnnotationEditor;
import client.editor.annotation.Editable;
import client.hud.Hud;
import client.hud.components.LaBButton;
import client.utils.FileLoader;

import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BTextField;
import com.jmex.bui.BLabel.Fit;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.event.FocusEvent;
import com.jmex.bui.event.FocusListener;
import com.jmex.bui.icon.IconUtil;
import com.jmex.bui.layout.TableLayout;

/**
 * Edite une collection par introspection 
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class SetEdit<E> extends BContainer {
	private Field field;
	private EditAnnotationWindow window;
	private Set<E> set;
	private AnnotationEditor editor;

	/**
	 * @param editIntrospecWindow
	 * @param f
	 */
	@SuppressWarnings("unchecked")
	public SetEdit(EditAnnotationWindow w, Field f) {

		this.field = f;
		this.window = w;
		this.editor = w.getEditor();
		this.set = (Set<E>) editor.getSet(field);

		TableLayout layout = new TableLayout(3,0,Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);
		this.setLayoutManager(layout);

		BLabel label = new BLabel(f.getName());
		label.setFit(Fit.SCALE);
		this.add(label);

		// champ d'insertion
		final BTextField text = new BTextField("indice");
		text.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent event) {}

			@Override
			public void focusGained(FocusEvent event) {
				if(text.equals("indice"))
					text.setText("");
			}
		});
		this.add(text);
		
		
		LaBButton add = new LaBButton("add",new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					int indice = Integer.parseInt(text.getText());
					if (indice>=0 && indice<=set.size()) {
						set.add(convertToE(text.getText()));
						window.refreshPanel();
					}
				} catch (NumberFormatException e) {
					window.getHud().openErrorPopup("Enter indice to insert in list "+field.getName());
				} catch (RuntimeException e) {
					window.getHud().openNotYetImplementedPopup();
				}
			}
		},"add");
		this.add(add);

		for (final E  e: set) {
			label = new BLabel(""+e);
			label.setFit(Fit.SCALE);
			this.add(label);

			this.add(new BLabel(" "));
						
			LaBButton delete = new LaBButton(IconUtil.getIcon(FileLoader
					.getResourceAsUrl(LaConstants.DIR_LGPL_ICON
							+ "cross-12.png")), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					set.remove(e);
					window.refreshPanel();
				}
			}, "delete");
			delete.setStyleClass("button-icon");
			this.add(delete);
		}
		

	}

	/**
	 * Converti l'objet en un type de ce qui est contenu dans le Set
	 * @param text
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected E convertToE(String text) {
		switch (field.getAnnotation(Editable.class).innerType()) {
		case string : return (E) text;
			
		default:
			break;
		}
		return null;
	}

	
}
