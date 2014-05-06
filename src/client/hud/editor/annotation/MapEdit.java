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
import java.util.Map.Entry;

import shared.constants.LaConstants;
import shared.constants.ScriptConstants;
import shared.enums.LaComponent;
import client.editor.FieldEditType;
import client.editor.annotation.AnnotationEditor;
import client.editor.annotation.Editable;
import client.hud.Hud;
import client.hud.components.LaBButton;
import client.utils.FileLoader;

import com.jme3.math.Vector3f;
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
public class MapEdit<K,V> extends BContainer {
	private Field field;
	private EditAnnotationWindow window;
	private java.util.Map<K,V> map;
	private AnnotationEditor editor;

	/**
	 * @param editIntrospecWindow
	 * @param f
	 */
	@SuppressWarnings("unchecked")
	public MapEdit(EditAnnotationWindow w, Field f) {

		this.field = f;
		this.window = w;
		this.editor = w.getEditor();
		this.map =  (java.util.Map<K, V>) editor.getMap(field);

		TableLayout layout = new TableLayout(3,0,Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);
		this.setLayoutManager(layout);

		BLabel label = new BLabel(f.getName());
		label.setFit(Fit.SCALE);
		this.add(label);

		// champ d'insertion
		final BTextField text = new BTextField("key");
		text.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent event) {}
			@Override
			public void focusGained(FocusEvent event) {
				if(text.equals("key"))
					text.setText("");
			}
		});
		this.add(text);

		LaBButton add = new LaBButton("add",new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					K key = convertToKey(text.getText());
					if (!map.containsKey(key)) {
						map.put(key, (V)editor.createElement(field));
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

		for (final Entry<K, V> entry : map.entrySet()) {

			BLabel indiceLabel = new BLabel(entry.getKey().toString());
			indiceLabel.setFit(Fit.SCALE);
			this.add(indiceLabel);

			LaBButton delete = new LaBButton(IconUtil.getIcon(FileLoader
					.getResourceAsUrl(LaConstants.DIR_LGPL_ICON
							+ "cross-12.png")), new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					map.remove(entry.getKey());
					window.refreshPanel();
				}
			}, "delete");
			delete.setStyleClass("button-icon");
			this.add(delete);

			initEdit(f,entry);
		}

	}

	/**
	 * TODO commentaire MapEdit.convertToKey
	 * @param text
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected K convertToKey(String text) {
		switch (field.getAnnotation(Editable.class).keyType()) {
		case string: return (K) text;
		case bool: return (K)(Boolean)("true".equalsIgnoreCase(text));
		case integer: return (K)(Integer)(Integer.parseInt(text));
		case real: return (K)(Float)(Float.parseFloat(text));
		case action: return (K) text;
		case text: return (K) text;
		case sharedKey: return (K) text;
		default:
			throw new RuntimeException("Type pas encore supporté");
		}
	}

	/**
	 * initialise l'edition d'un composant
	 * @param f
	 * @param entry
	 */
	private void initEdit(final Field f, final Entry<K, V> entry) {
		final BTextField text;
		switch (f.getAnnotation(Editable.class).innerType()) {
		case string: 
		case sharedKey:
			text = new BTextField(entry.getValue()==null?
					"Null":entry.getValue().toString());
			text.addFocusListener(new FocusListener() {
				@SuppressWarnings("unchecked")
				@Override
				public void focusLost(FocusEvent event) {
					String str = (String)text.getText();
					if (f.getAnnotation(Editable.class).innerType() == FieldEditType.sharedKey) {
						if (LaComponent.type(str)!=LaComponent.NULL
								|| str.equals(ScriptConstants.VOID_SCRIPT))
							entry.setValue((V) str);
					}
					else 
						entry.setValue((V) str);
					text.setText((String) entry.getValue());
				}
				@Override
				public void focusGained(FocusEvent event) { }
			});
			add(text);
			break;
		case vertex: 
			text = new BTextField(entry.getValue().toString());
			text.addFocusListener(new FocusListener() {
				@SuppressWarnings("unchecked")
				public void focusLost(FocusEvent arg0) {
					try {
					String[] strs = text.getText().split(",");
					entry.setValue((V)new Vector3f(
							Float.parseFloat(strs[0]),
							Float.parseFloat(strs[1]),
							Float.parseFloat(strs[2])));
					} catch (Exception e) {
						System.out.println(e.getMessage());
					} finally {
						text.setText(""+entry.getValue());
					}
				}
				public void focusGained(FocusEvent arg0) {}
			});
			add(text);
			break;
		}
	}


}
