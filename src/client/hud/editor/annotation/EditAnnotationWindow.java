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
import java.util.ArrayList;

import client.editor.annotation.AnnotationEditor;
import client.editor.annotation.Editable;
import client.hud.Hud;
import client.hud.components.LaBButton;
import client.hud.components.WindowMoveListener;
import client.interfaces.graphic.Graphic;
import client.interfaces.network.Sharable;
import client.interfaces.network.SharableReflexEditable;
import client.utils.FileLoader;

import com.jme3.math.Vector3f;
import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BScrollPane;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.BLabel.Fit;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.TableLayout;

/**
 * Fenetre permettant l'edition d'un objet
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class EditAnnotationWindow extends BWindow {
	private Hud hud;
	private Sharable obj;
	private BContainer panel;
	private AnnotationEditor editor;
	private static ArrayList<EditAnnotationWindow> opens;
		
	/**
	 * @param hud
	 * @param obj
	 */
	public EditAnnotationWindow(Hud hud, SharableReflexEditable obj) {
		
		
		super("edit anot window", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/edit-style.bss")), new BorderLayout(
						Hud.SPACE, Hud.SPACE));
		this.hud = hud;
		this.obj = obj;
		this.editor = new AnnotationEditor(obj,hud.getGame());
		
		initialize();
	}



	/**
	 * Initialise le contenu de la fenetre
	 */
	private void initialize() {
		String titleTxt = "Editor : " + obj.getKey();
		try {
		if (obj instanceof Graphic)
			titleTxt += " (" + ((Graphic) obj).getGraphic().getTriangleCount()
			+ ")";
		} catch (NullPointerException e) {}

		BLabel title = new BLabel(titleTxt);
		title.setStyleClass("label-title");
		title.addListener(new WindowMoveListener(this));
		this.add(title, BorderLayout.NORTH);

		initMiddle();
		initBottom();

		this.setPreferredSize(250, 450);
		this.pack();
		BuiSystem.addWindow(this);

		center();
		locate();
	}

	/**
	 * TODO commentaire EditAnnotationWindow.locate
	 */
	private void locate() {
		if (opens == null) 
			opens = new ArrayList<EditAnnotationWindow>();
		opens.add(this);
		int i = opens.indexOf(this);
		setLocation(i*getWidth()+(i+1)*5, hud.h-getHeight()-20);
	}

	/**
	 * TODO commentaire EditAnnotationWindow.unlocate
	 */
	private void unlocate() {
		opens.remove(this);
		int i = 0;
		for (EditAnnotationWindow w:opens) {
			w.setLocation(i*getWidth()+(i+1)*5, hud.h-getHeight()-20);
			i++;
		}
	}


	/**
	 * Initialise le panneau principal
	 */
	private void initMiddle() {
		TableLayout layout = new TableLayout(1,Hud.SPACE,0);
		layout.setHorizontalAlignment(TableLayout.STRETCH);

		panel = new BContainer(layout);
		panel.setStyleClass("container-in-scroll-pane");

		refreshPanel();

		BScrollPane scrollPane = new BScrollPane(panel);
		scrollPane.setStyleClass("container-middle");
		this.add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * met à jour le contenu du panel
	 */
	public void refreshPanel() {
		panel.removeAll();
		for (Field f : editor.getFields()) 
			switch (f.getAnnotation(Editable.class).type()) {
			case action : panel.add(new ActionEdit(this, f)); break;
			case bool : panel.add(new BooleanEdit(this, f)); break;
			case code : panel.add(new CodeEdit(this,f)); break;
			case color : panel.add(new ColorEdit(this,f,true)); break;
			case integer : panel.add(new IntegerEdit(this, f)); break;
			case integerinterval : panel.add(new IntegerIntervalEdit(this, f)); break;
			case editable : panel.add(new EditableEdit(this,f)); break;
			case enumerate : panel.add(new EnumEdit(this,f)); break;
			case file : panel.add(new FileEdit(this, f)); break;
			case list : createListElement(f); break;
			case map : createMapElement(f); break;
			case set : createSetElement(f); break;
			case real: panel.add(new FloatEdit(this,f)); break;
			case realinterval: panel.add(new FloatIntervalEdit(this,f)); break;
			case string : panel.add(new StringEdit(this, f)); break;
			case text : panel.add(new TextEdit(this, f)); break;
			case vertex : panel.add(new VertexEdit(this,f)); break;
			default: initNyi(f); break;
			}
	}

	/**
	 * Initialise le type de la List
	 * @param f 
	 */
	private void createListElement(Field f) {
		switch(f.getAnnotation(Editable.class).innerType()) {
		case action : panel.add(new ListEdit<String>(this,f)); break;
		case string : panel.add(new ListEdit<String>(this,f)); break;
		case integer : panel.add(new ListEdit<Integer>(this,f)); break;
		case file : panel.add(new ListEdit<String>(this,f)); break;
		case sharedKey : panel.add(new ListEdit<String>(this,f)); break;
		default: initNyi(f); break;
		}
	}
	
	/**
	 * Initialise le type de la set
	 * @param f 
	 */
	private void createSetElement(Field f) {
		switch(f.getAnnotation(Editable.class).innerType()) {
		case string : panel.add(new SetEdit<String>(this,f)); break;
		default: initNyi(f); break;
		}
	}

	/**
	 * Initialise le type de la List
	 * @param f 
	 */
	private void createMapElement(Field f) {
		switch (f.getAnnotation(Editable.class).keyType()) {
		case integer : initIntegerIndexedMap(f); break;
		case sharedKey :
		case string : initStringIndexedMap(f); break;
		default : initNyi(f); 
		}
		
	}

	/**
	 * TODO commentaire EditAnnotationWindow.initIntegerIndexedMap
	 * @param f
	 */
	private void initIntegerIndexedMap(Field f) {
		switch (f.getAnnotation(Editable.class).innerType()) {
		case sharedKey :
		case string : panel.add(new MapEdit<Integer, String>(this, f)); break;
		default : initNyi(f);
		}
	}

	/**
	 * TODO commentaire EditAnnotationWindow.initStringIndexedMap
	 * @param f
	 */
	private void initStringIndexedMap(Field f) {
		switch (f.getAnnotation(Editable.class).innerType()) {
		case sharedKey :
		case string : panel.add(new MapEdit<String, String>(this, f)); break;
		case vertex : panel.add(new MapEdit<String, Vector3f>(this, f)); break;
		default : initNyi(f);
		}
	}



	/**
	 * Affiche comme quoi ca marche pas encore
	 * @param f
	 */
	private void initNyi(Field f) {
		TableLayout layout = new TableLayout(2,0,Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);
		BContainer p = new BContainer(layout);
		panel.add(p);

		BLabel label = new BLabel(f.getName());
		label.setFit(Fit.SCALE);
		p.add(label);

		label = new BLabel("j'y boss");
		label.setFit(Fit.SCALE);
		p.add(label);
	}
	/**
	 * Initialise la partie basse de la fenetre
	 */
	private void initBottom() {
		LaBButton cancel = new LaBButton("cancel", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				editor.reset();
				close();
			}
		}, "cancel");
		LaBButton delte = new LaBButton("delete", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				editor.delete();
				close();
			}
		}, "delte");
		
		LaBButton ok = new LaBButton("apply", new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				editor.commit();
				close();
			}
		}, "ok");

		TableLayout layout = new TableLayout(3, 0, Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);
		BContainer bottom = new BContainer(layout);
		bottom.add(cancel);
		bottom.add(delte);
		bottom.add(ok);
		this.add(bottom, BorderLayout.SOUTH);
	}
	
	
	/* ********************************************************** *
	 * * 			SETTERS/GETTERS PAT INTROSPECTION			* *
	 * ********************************************************** */

	
	
	
	/* ********************************************************** *
	 * * 					SETTERS/GETTERS 					* *
	 * ********************************************************** */

	/**
	 * Renvoie le contenaeur graphique
	 * 
	 * @return
	 */
	public Hud getHud() {
		return hud;
	}

	public void close() {
		unlocate();
		BuiSystem.removeWindow(this);
	}
	



	/**
	 * @return the editor
	 */
	public AnnotationEditor getEditor() {
		return editor;
	}


}
