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

import shared.constants.LaConstants;
import client.editor.annotation.AnnotationEditor;
import client.hud.Hud;
import client.hud.components.LaBButton;
import client.utils.FileLoader;
import com.jme3.math.ColorRGBA;

//import com.jme.renderer.ColorRGBA;
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
 * interface d'edition d'un vertex par annotation 
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
public class ColorEdit extends BContainer {
	private AnnotationEditor editor;
	private Field field;
	private BTextField[] text;
	private boolean alpha;

	/**
	 * @param w
	 * @param f
	 */
	public ColorEdit(EditAnnotationWindow w, Field f,boolean a) {
		super();
		
		this.editor = w.getEditor();
		this.field = f;
		this.alpha = a;

		TableLayout layout = new TableLayout(3,0,Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);
		this.setLayoutManager(layout);
		
		text = new BTextField[4];
		
		for (int i=0;i<(alpha?4:3);i++) {
			final int index = i;
			
			String name = f.getName();
			if (i==0) name+="-r";
			if (i==1) name+="-g";
			if (i==2) name+="-b";
			if (i==3) name+="-a";

			BLabel label = new BLabel(name);
			label.setFit(Fit.SCALE);
			this.add(label);
			
			LaBButton plus = new LaBButton(IconUtil.getIcon(FileLoader
					.getResourceAsUrl(LaConstants.DIR_LGPL_ICON + "add-10.png")),
					new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							ColorRGBA c = editor.getColor(field);
							switch (index) {
							case 0: c.r+=.05f; break;
							case 1: c.g+=.05f; break;
							case 2: c.b+=.05f; break;
							case 3: c.a+=.05f; break;
							}
							editor.setColor(field, c);
							update();
						}
					}, "add");
			plus.setStyleClass("button-icon");

			LaBButton minus = new LaBButton(IconUtil.getIcon(FileLoader
					.getResourceAsUrl(LaConstants.DIR_LGPL_ICON + "minus-10.png")),
					new ActionListener() {
						public void actionPerformed(ActionEvent event) {
							ColorRGBA c = editor.getColor(field);
							switch (index) {
							case 0: c.r-=.05f; break;
							case 1: c.g-=.05f; break;
							case 2: c.b-=.05f; break;
							case 3: c.a-=.05f; break;
							}
							editor.setColor(field, c);
							update();
						}
					}, "minus");
			plus.setStyleClass("button-icon");
			minus.setStyleClass("button-icon");
			
			BContainer panel = new BContainer(new TableLayout(1, 0, 0));
			panel.add(plus);
			panel.add(minus);
			this.add(panel);
			
			text[i] = new BTextField("0");
			text[i].addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent event) {
					try {
					float num = Float.parseFloat(text[index].getText());
					ColorRGBA c = editor.getColor(field);
					switch (index) {
					case 0: c.r=num; break;
					case 1: c.g=num; break;
					case 2: c.b=num; break;
					case 3: c.a=num; break;
					}
					editor.setColor(field, c);
					update();
					} catch (NumberFormatException e) {
					}
				}
				@Override
				public void focusGained(FocusEvent event) { }
			});
			this.add(text[i]);
		}
		
		update();
	}

	/**
	 * 
	 */
	protected void update() {
		ColorRGBA c = editor.getColor(field);
		text[0].setText(Float.toString(c.r));
		text[1].setText(Float.toString(c.g));
		text[2].setText(Float.toString(c.b));
		if (alpha) text[3].setText(Float.toString(c.a));
	}


	
	
	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
