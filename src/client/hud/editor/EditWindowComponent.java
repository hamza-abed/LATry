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

import java.awt.Component;
import java.awt.HeadlessException;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import shared.constants.LaConstants;
import shared.constants.ScriptConstants;
import shared.enums.LaComponent;
import client.editor.EditModel;
import client.hud.Hud;
import client.hud.components.LaBButton;
import client.utils.FileLoader;


import com.jmex.bui.BCheckBox;
import com.jmex.bui.BComboBox;
import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BSlider;
import com.jmex.bui.BTextField;
import com.jmex.bui.enumeratedConstants.Orientation;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.event.ChangeEvent;
import com.jmex.bui.event.ChangeListener;
import com.jmex.bui.icon.IconUtil;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.TableLayout;
import shared.variables.Variables;

/**
 * Composant permettant d'editer un attribut d'un GraphicHudEditable
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class EditWindowComponent extends BContainer {
	private static final Logger logger = Logger
			.getLogger("EditWindowComponent");
	protected EditModel model;
	protected String name;
	private LaBButton select;
	private LaBButton edit;
	private Hud hud;

	/**
	 * @param editWindow
	 * @param model
	 * @param name
	 */
	public EditWindowComponent(Hud hud, EditModel model,
			String name) {
		this.hud = hud;
		this.model = model;
		this.name = name;
		switch (model.getFieldType(name)) {
		case string:
			initStringComponent();
			break;
		case integer:
			initIntComponent();
			break;
		case real:
			initRealComponent();
			break;
		case realinterval:
			initRealIntervalComponent();
			break;
		case file:
			initFileComponent();
			break;
		case enumerate:
			initEnumComponent();
			break;
		case bool:
			initBooleanComponent();
			break;
		case text:
			initextComponent();
			break;
		case action: initActionComponent(); break;
		case code: iniCodeComponent(); break;
		default:
			setLayoutManager(new BorderLayout());
			this.add(new BLabel("type non implementé"), BorderLayout.CENTER);
		}

	}

	/**
	 * initialise le composant en tans que selecteur de script
	 */
	private void initActionComponent() {
		super.setLayoutManager(new BorderLayout(0, 0));

		final BTextField textField = new BTextField("" + model.getValue(name));
		textField.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String str = textField.getText();
				if (str.matches(LaComponent.script.regex())
						|| str.matches(LaComponent.dialog.regex())
						|| str.matches(LaComponent.lgf.regex())
						|| str.matches(LaComponent.slides.regex())
						|| str.equals(ScriptConstants.VOID_SCRIPT)
						|| str.equals(ScriptConstants.TRUE_SCRIPT)
						|| str.equals(ScriptConstants.FALSE_SCRIPT))
					model.setValue(name, str);
				textField.setText("" + model.getValue(name));
			}
		});
		this.add(textField, BorderLayout.CENTER);
	}

	/**
	 * initialise le composant en tans qu'editeur de chaine de caractere
	 */
	private void initStringComponent() {
		super.setLayoutManager(new BorderLayout(0, 0));

		final BTextField textField = new BTextField("" + model.getValue(name));
		textField.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				model.setValue(name, textField.getText());
				textField.setText("" + model.getValue(name));
			}
		});
		this.add(textField, BorderLayout.CENTER);

	}

	/**
	 * initialise le composant en tans qu'editeur de Int
	 */
	private void initIntComponent() {
		super.setLayoutManager(new BorderLayout(Hud.SPACE, Hud.SPACE));

		final BTextField textField = new BTextField("" + model.getValue(name));
		textField.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					model.setValue(name, Integer.parseInt(textField.getText()));
				} catch (NumberFormatException e) {
					logger.warning("l'utilisateur n'as pas entré un int");
				}
				textField.setText("" + model.getValue(name));
			}
		});
		this.add(textField, BorderLayout.CENTER);

		LaBButton plus = new LaBButton(IconUtil.getIcon(FileLoader
				.getResourceAsUrl(LaConstants.DIR_LGPL_ICON + "add-10.png")),
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						model.setValue(name, model.getInt(name) + 1);
						textField.setText("" + model.getValue(name));
					}
				}, "add");
		plus.setStyleClass("button-icon");

		LaBButton minus = new LaBButton(IconUtil.getIcon(FileLoader
				.getResourceAsUrl(LaConstants.DIR_LGPL_ICON + "minus-10.png")),
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						model.setValue(name, model.getInt(name) - 1);
						textField.setText("" + model.getValue(name));
					}
				}, "minus");
		plus.setStyleClass("button-icon");
		minus.setStyleClass("button-icon");

		BContainer panel = new BContainer(new TableLayout(1, 0, 0));
		panel.add(plus);
		panel.add(minus);
		this.add(panel, BorderLayout.WEST);
	}

	/**
	 * initialise le composant en tans qu'editeur de real
	 */
	private void initRealComponent() {
		super.setLayoutManager(new BorderLayout(Hud.SPACE, Hud.SPACE));

		final BTextField textField = new BTextField("" + model.getValue(name));
		textField.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					model.setValue(name, Float.parseFloat(textField.getText()));
				} catch (NumberFormatException e) {
					logger.warning("l'utilisateur n'as pas entré un floatant");
				}
				textField.setText("" + model.getValue(name));
			}
		});
		this.add(textField, BorderLayout.CENTER);

		LaBButton plus = new LaBButton(IconUtil.getIcon(FileLoader
				.getResourceAsUrl(LaConstants.DIR_LGPL_ICON + "add-10.png")),
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						model.setValue(name, model.getFloat(name) + 1);
						textField.setText("" + model.getValue(name));
					}
				}, "add");
		plus.setStyleClass("button-icon");

		LaBButton minus = new LaBButton(IconUtil.getIcon(FileLoader
				.getResourceAsUrl(LaConstants.DIR_LGPL_ICON + "minus-10.png")),
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						model.setValue(name, model.getFloat(name) - 1);
						textField.setText("" + model.getValue(name));
					}
				}, "minus");
		plus.setStyleClass("button-icon");
		minus.setStyleClass("button-icon");

		BContainer panel = new BContainer(new TableLayout(1, 0, 0));
		panel.add(plus);
		panel.add(minus);
		this.add(panel, BorderLayout.WEST);
	}

	/**
	 * Interval sur un floatant
	 */
	private void initRealIntervalComponent() {
		TableLayout layout = new TableLayout(2, Hud.SPACE, Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);
		this.setLayoutManager(layout);

		final BTextField textField = new BTextField("" + model.getValue(name));
		textField.setPreferredSize(40, -1);

		float min = (Float) model.getMinValue(name);
		float max = (Float) model.getMaxValue(name);
		final float factor = 1f / (Float) model.getStepValue(name);

		final BSlider slider = new BSlider(Orientation.HORIZONTAL,
				(int) (min * factor), (int) (max * factor), (int) (model
						.getFloat(name) * factor));

		slider.getModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				logger.info("changed");
				model.setValue(name,
						(float) (slider.getModel().getValue() / factor));
				slider.getModel().setValue((int) (model.getFloat(name) * factor));
				textField.setText("" + model.getValue(name));
			}
		});
		slider.setPreferredSize(100, -1);

		textField.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					model.setValue(name, Float.parseFloat(textField.getText()));
				} catch (NumberFormatException e) {
				}
				slider.getModel().setValue((int) (model.getFloat(name) * factor));
				textField.setText("" + model.getValue(name));
			}
		});

		this.add(slider);
		this.add(textField);
	}

	/**
	 * Initialise sous forme d'un composant d'edition de fichier
	 */
	private void initFileComponent() {
		this.setLayoutManager(new BorderLayout(0, 0));
		select = new LaBButton("" + model.getValue(name), new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						final JFileChooser fc = new JFileChooser(model
								.getFileFolder(name)) {
							private static final long serialVersionUID = 4383186120076573127L;

							@Override
							protected JDialog createDialog(Component parent)
									throws HeadlessException {
								JDialog diag = super.createDialog(parent);
								diag.setAlwaysOnTop(true);
								return diag;
							}
						};
						/*
						 * fc.setFileFilter(new FileFilter() {
						 * 
						 * @Override public String getDescription() { return
						 * "JME-XML (*.jmex)"; }
						 * 
						 * @Override public boolean accept(File f) { return
						 * f.getName().endsWith("jmex"); } });//
						 */
                                                
						if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						/*	GameTaskQueueManager.getManager().update(
									new Callable<Void>() {
										@Override
										public Void call() throws Exception {
											String f = fc
													.getSelectedFile()
													.getAbsolutePath()
													.replace(
															model
																	.getFileFolder(
																			name)
																	.getAbsolutePath(),
															"");
											f = f.replaceFirst("/", "");
											logger.info("Fichier normalisé "
													+ f);
											model.setValue(name, f);
											select.setText(f);
											return null;
										}
									});*/
                                                    System.out.println("EditWindowComponent -> initFileComponent() : GameTaskQueueManager !!!");
						}
					}
				}).start();
			}
		}, "select");
		this.add(select, BorderLayout.CENTER);
	}

	/**
	 * Composant editeur de text
	 */
	private void initextComponent() {
		this.setLayoutManager(new BorderLayout(0, 0));
		edit = new LaBButton("edit", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				new TextEditWindow(model, name);
			}
		}, "edit");
		this.add(edit, BorderLayout.CENTER);
	}

	/**
	 * Composant editeur de text
	 */
	private void iniCodeComponent() {
		this.setLayoutManager(new BorderLayout(0, 0));
		edit = new LaBButton("edit", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				/*GameTaskQueueManager.getManager().update(new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						new ScriptEditWindow(model, name)
						.setVisible(true);
						return null;
					}
				});//*/
				Variables.getTaskExecutor().execute(
						new Runnable() {
							@Override
							public void run() {
								//new ScriptEditWindow(model, name);
							}
						});//*/
			}
		}, "edit");
		this.add(edit, BorderLayout.CENTER);
	}

	/**
	 * Initialisation du composant dans e cas d'un type enumerer
	 */
	private void initEnumComponent() {
		this.setLayoutManager(new BorderLayout(0, 0));
		final BComboBox combo = new BComboBox(model.getEnumValues(name));
		combo.setText("" + model.getValue(name));

		combo.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				model.setValue(name, combo.getSelectedValue());
			}
		});

		this.add(combo, BorderLayout.CENTER);
	}

	/**
	 * Initialise le composant d'edition de boolean
	 */
	private void initBooleanComponent() {
		this.setLayoutManager(new BorderLayout(0, 0));
		final BCheckBox check = new BCheckBox(name);
		check.setSelected(model.getBoolean(name));
		check.addListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				model.setValue(name, check.isSelected());
			}
		});
		this.add(check, BorderLayout.CENTER);
	}

}
