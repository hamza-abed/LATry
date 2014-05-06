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
import java.util.logging.Logger;

import client.editor.annotation.AnnotationEditor;
import client.editor.annotation.Editable;
import client.hud.Hud;

import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BSlider;
import com.jmex.bui.BTextField;
import com.jmex.bui.BLabel.Fit;
import com.jmex.bui.enumeratedConstants.Orientation;
import com.jmex.bui.event.ChangeEvent;
import com.jmex.bui.event.ChangeListener;
import com.jmex.bui.event.FocusEvent;
import com.jmex.bui.event.FocusListener;
import com.jmex.bui.layout.TableLayout;

/**
 * TODO Commentaire de 
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class FloatIntervalEdit extends BContainer {
	private static final Logger logger = Logger.getLogger("FloatIntervalEdit");
	private AnnotationEditor editor;
	private Field field;
	private BSlider slider;
	private BTextField textField;

	
	/**
	 * 
	 */
	public FloatIntervalEdit(EditAnnotationWindow w, Field f) {
		super();

		this.editor = w.getEditor();
		this.field = f;

		TableLayout layout = new TableLayout(3,0,Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);
		this.setLayoutManager(layout);

		BLabel label = new BLabel(f.getName());
		label.setFit(Fit.SCALE);
		this.add(label);
		
		textField = new BTextField(Float.toString(editor.getFloat(field)));
		textField.setPreferredSize(40, -1);

		float min = field.getAnnotation(Editable.class).realMinValue();
		float max = field.getAnnotation(Editable.class).realMaxValue();
		float step = field.getAnnotation(Editable.class).realStepValue();
		final float factor = 1f / step;

		slider = new BSlider(Orientation.HORIZONTAL,
				(int) (min * factor), (int) (max * factor), 
				(int) (editor.getFloat(field) * factor));

		slider.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				logger.info("changed");
				editor.setFloat(field,(float) (slider.getModel().getValue() / factor));
				slider.getModel().setValue((int) (editor.getFloat(field) * factor));
				textField.setText(Float.toString(editor.getFloat(field)));
			}
		});
		slider.setPreferredSize(100, -1);

		textField.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent event) {
				editor.setFloat(field, textField.getText());
				textField.setText(Float.toString(editor.getFloat(field)));
				slider.getModel().setValue((int) (editor.getFloat(field) * factor));
			}
			
			@Override
			public void focusGained(FocusEvent event) {}
		});
		
		this.add(slider);
		this.add(textField);		

	}
	
	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
