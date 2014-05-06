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
import java.util.List;

import shared.constants.LaConstants;
import shared.constants.ScriptConstants;
import shared.enums.LaComponent;
import client.editor.FieldEditType;
import client.editor.annotation.AnnotationEditor;
import client.editor.annotation.Editable;
import client.hud.Hud;
import client.hud.components.FileSelectButton;
import client.hud.components.LaBButton;
import client.hud.components.FileSelectButton.FileSelectListener;
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
public class ListEdit<E> extends BContainer {
	private Field field;
	private EditAnnotationWindow window;
	private List<E> list;
	private AnnotationEditor editor;

	/**
	 * @param editIntrospecWindow
	 * @param f
	 */
	@SuppressWarnings("unchecked")
	public ListEdit(EditAnnotationWindow w, Field f) {

		this.field = f;
		this.window = w;
		this.editor = w.getEditor();
		this.list = (List<E>) editor.getList(field);

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
					if (indice>=0 && indice<=list.size()) {
						list.add(indice, (E)editor.createElement(field));
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

		for (int i=0;i<list.size();i++) {
			final int indice = i;

			BLabel indiceLabel = new BLabel(Integer.toString(indice));
			indiceLabel.setFit(Fit.SCALE);
			this.add(indiceLabel);

			LaBButton delete = new LaBButton(IconUtil.getIcon(FileLoader
					.getResourceAsUrl(LaConstants.DIR_LGPL_ICON
							+ "cross-12.png")), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					list.remove(indice);
					window.refreshPanel();
				}
			}, "delete");
			delete.setStyleClass("button-icon");
			this.add(delete);

			initEdit(f,i);
		}

	}

	/**
	 * initialise l'edition d'un composant
	 * @param f
	 * @param i
	 */
	private void initEdit(final Field f, final int i) {
		final BTextField text;
		switch (f.getAnnotation(Editable.class).innerType()) {
		case string: 
		case sharedKey:
			text = new BTextField(list.get(i).toString());
			text.addFocusListener(new FocusListener() {
				@SuppressWarnings("unchecked")
				@Override
				public void focusLost(FocusEvent event) {
					String str = (String)text.getText();
					if (f.getAnnotation(Editable.class).innerType() == 
						FieldEditType.sharedKey) {
						if (LaComponent.type(str)!=LaComponent.NULL
								|| str.equals(ScriptConstants.VOID_SCRIPT))
							list.set(i,(E) str);
					}
					else 
						list.set(i,(E) str);
					text.setText((String) list.get(i));
				}
				@Override
				public void focusGained(FocusEvent event) { }
			});
			add(text);
			break;
		case integer :
			text = new BTextField(list.get(i).toString());
			text.addFocusListener(new FocusListener() {
				@SuppressWarnings("unchecked")
				@Override
				public void focusLost(FocusEvent event) {
					try {
						list.set(i, (E)(Integer)Integer.parseInt(text.getText()));
						text.setText(list.get(i).toString());
					} catch(NumberFormatException e) {

					}
				}
				@Override
				public void focusGained(FocusEvent event) { }
			});
			add(text);
			break;
		case file : 
			add(new FileSelectButton(list.get(i).toString(), 
					field.getAnnotation(Editable.class).fileFolder(), 
					new FileSelectListener() {
				@SuppressWarnings("unchecked")
				@Override
				public void fileSelect(String file) {
					list.set(i, (E) file);
				}
			}));
			break;
		}
	}


	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
