
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
package client.hud.editor;

import java.lang.reflect.Field;
import java.util.logging.Logger;

import shared.enums.LaComponent;
import client.editor.annotation.AnnotationCreator;
import client.editor.annotation.AnnotationUtils;
import client.editor.annotation.Editable;
import client.hud.Hud;
import client.hud.components.FileSelectButton;
import client.hud.components.LaBButton;
import client.hud.components.WindowMoveListener;
import client.hud.components.FileSelectButton.FileSelectListener;
import client.utils.FileLoader;

import com.jmex.bui.BCheckBox;
import com.jmex.bui.BComboBox;
import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BSlider;
import com.jmex.bui.BTextField;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.BLabel.Fit;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.enumeratedConstants.Orientation;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.event.ChangeEvent;
import com.jmex.bui.event.ChangeListener;
import com.jmex.bui.event.FocusEvent;
import com.jmex.bui.event.FocusListener;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.HGroupLayout;
import com.jmex.bui.layout.Justification;
import com.jmex.bui.layout.Policy;
import com.jmex.bui.layout.VGroupLayout;

/**
 * Populateur utilisant la reflexion 
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
public class HudPopulateurAnnotation extends BWindow  {
	private static final Logger logger = Logger.getLogger("HudPopulateurAnnotation");
	
	
	private Hud hud;

	private BContainer editor;

	private AnnotationCreator creator;

	private static final LaComponent types[] = {
		LaComponent.object
	};

	public HudPopulateurAnnotation(Hud hud) {
		super("populating window", BStyleSheetUtil.getStyleSheet(FileLoader
				.getResourceAsUrl("data/hud/style/populus.bss")), new BorderLayout(
						Hud.SPACE, Hud.SPACE));

		this.hud = hud;

		initialize();
	}

	/**
	 * Initialise le contenu de la fenetre
	 */
	private void initialize() {
		BLabel title = new BLabel(hud.getLocalText("populus.2.title"));
		title.setStyleClass("label-title");
		title.addListener(new WindowMoveListener(this));
		this.add(title, BorderLayout.NORTH);

		initMiddle();

		this.add(new LaBButton(hud.getLocalText("populus.2.close"),
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setVisible(false);
			}
		},"close"),BorderLayout.SOUTH);

		this.setPreferredSize(150, 200);
		this.pack();
		BuiSystem.addWindow(this);

		this.center();
	}

	/**
	 * Initialise le panneau principal
	 */
	private void initMiddle() {
		BContainer panel = new BContainer(new BorderLayout());
		final BComboBox selector = new BComboBox(types);
		selector.setText("select type");
		selector.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				reloadEditPart((LaComponent)selector.getSelectedValue());
			}
		});
		panel.add(selector,BorderLayout.NORTH);
		VGroupLayout layout = new VGroupLayout(Justification.LEFT);
		layout.setOffAxisPolicy(Policy.STRETCH);
		editor = new BContainer(layout);
		panel.add(editor,BorderLayout.CENTER);

		panel.add(new LaBButton(hud.getLocalText("populus.2.add"), 
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (creator != null) {
					creator.create();
				}
			}
		}, "add"), BorderLayout.SOUTH);

		this.add(panel,BorderLayout.CENTER);
	}

	/**
	 * recharge 
	 * @param laComponent 
	 */
	protected void reloadEditPart(LaComponent type) {
		System.out.println("reload");
		editor.removeAll();
		switch (type) {
		case object:
			this.creator = new AnnotationCreator(LaComponent.object,hud.getGame());
			break;

		default:
			return;
		}
		reload();
	}

	/**
	 * Recharghe la vue en cas d'un objet de type carte
	 */
	private void reload() {
		System.out.println("reload Fields");
		for (Field f : creator.getFields()) 
			if (AnnotationUtils.isPopulable(f)) {
				switch (AnnotationUtils.getType(f)) {
				case file: addFile(f); break;
				case bool: addBool(f); break;
				case real: addReal(f); break;
				case realinterval: addRealInterval(f); break;
				default:
					break;
				}
			}
	}

	/**
	 * TODO commentaire HudPopulateurAnnotation.addRealInterval
	 * @param f
	 */
	private void addRealInterval(final Field f) {
		BContainer panel = new BContainer(new HGroupLayout());
		panel.add(new BLabel(f.getName()));
		
		final BTextField textField = new BTextField(Float.toString(AnnotationUtils.getDefaultFloatValue(f)));
		textField.setPreferredSize(40, -1);

		float min = f.getAnnotation(Editable.class).realMinValue();
		float max = f.getAnnotation(Editable.class).realMaxValue();
		float step = f.getAnnotation(Editable.class).realStepValue();
		final float factor = 1f / step;

		final BSlider slider = new BSlider(Orientation.HORIZONTAL,
				(int) (min * factor), (int) (max * factor), 
				(int) (AnnotationUtils.getDefaultFloatValue(f) * factor));

		slider.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				logger.info("changed");
				float v = (float) (slider.getModel().getValue() / factor);
				creator.set(f,v);
				slider.getModel().setValue((int) (v * factor));
				textField.setText(Float.toString(v));
			}
		});
		slider.setPreferredSize(100, -1);

		textField.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent event) {
				creator.set(f, Float.parseFloat(textField.getText()));
				slider.getModel().setValue((int) (Float.parseFloat(textField.getText()) * factor));
			}
			
			@Override
			public void focusGained(FocusEvent event) {}
		});
		
		panel.add(slider);
		panel.add(textField);		
		editor.add(panel);
	}

	/**
	 * TODO commentaire HudPopulateurAnnotation.addReal
	 * @param f
	 */
	private void addReal(final Field f) {
		BContainer panel = new BContainer(new HGroupLayout());
		panel.add(new BLabel(f.getName()));
		
		final BTextField text = new BTextField(Float.toString(AnnotationUtils.getDefaultFloatValue(f)));
		text.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent event) {
				try {
				creator.set(f,Float.parseFloat(text.getText()));
				} catch (NumberFormatException e) {
					logger.warning("le user ne sais pas taper au clavier");
				}
			}
			@Override
			public void focusGained(FocusEvent event) { }
		});
		panel.add(text);
		
		editor.add(panel);
	}

	/**
	 * @param f
	 */
	private void addBool(final Field f) {
		final BCheckBox check = new BCheckBox(f.getName());
		check.setFit(Fit.SCALE);
		check.setSelected(AnnotationUtils.getDefaultBoolValue(f));
		check.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				creator.set(f,check.isSelected());
			}
		});
		editor.add(check);
	}

	/**
	 * @param f
	 */
	private void addFile(final Field f) {
		editor.add(new FileSelectButton(f.getName(), 
				f.getAnnotation(Editable.class).fileFolder(), 
				new FileSelectListener() {
			@Override
			public void fileSelect(String file) {
				creator.set(f, file);
			}
		}));
	}


}
