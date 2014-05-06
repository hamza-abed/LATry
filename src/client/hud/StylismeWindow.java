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
package client.hud;

import java.io.IOException;
import java.util.logging.Logger;

import shared.enums.CharacterModel;
import client.hud.components.AmbiantSelect;
import client.hud.components.ColorSelect;
import client.hud.components.LaBButton;
import client.hud.components.AmbiantSelect.ChangeAmbientListener;
import client.hud.components.ColorSelect.ChangeColorListener;
import client.map.character.CharacterLoader;
import client.map.character.Player;
import client.utils.FileLoader;
import com.jme3.scene.Node;

/*import com.jme.bounding.BoundingBox;
import com.jme.light.DirectionalLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial.CullHint;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem; */
import com.jmex.bui.BComboBox;
import com.jmex.bui.BContainer;
import com.jmex.bui.BGeomView;
import com.jmex.bui.BLabel;
import com.jmex.bui.BTextField;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.event.FocusEvent;
import com.jmex.bui.event.FocusListener;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.Justification;
import com.jmex.bui.layout.Policy;
import com.jmex.bui.layout.VGroupLayout;
import shared.variables.Variables;
//import com.jmex.model.ModelFormatException;

/**
 * Fenetre pour qu'un joueur eit son personnage 
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class StylismeWindow extends BWindow {
	private static final Logger logger = Logger.getLogger("StylismeWindow");
	private Hud hud;
	private Player player;
	private BGeomView geomView;
	private Node s;
	private CharacterModel[] models;
	private BContainer panel;

	/**
	 * 
	 */
	public StylismeWindow(Hud hud,Player player, CharacterModel[] models) {
		super("StylismeWindow",
				BStyleSheetUtil.getStyleSheet(FileLoader.getResourceAsUrl(
						"data/stylisme.bss")),
				new BorderLayout(Hud.SPACE,Hud.SPACE));

		this.hud = hud;
		this.player = player;
		this.models = models;

		initialize();
		//setVisible(false);
	}


	/**
	 * intialise le contenu de la fenetre
	 */
	private void initialize() {
		initLeft();
		initMiddle();


		this.setSize(hud.w-50, hud.h-50);
		this.setPreferredSize(hud.w-50, hud.h-50);
		this.center();
		this.pack();
		this.setLayer(2);
		BuiSystem.addWindow(this);
	}

	/**
	 * initialise l'enssemble du panneau gauche
	 */
	private void initLeft() {
		if (panel == null) {
			VGroupLayout layout = new VGroupLayout(Justification.CENTER,Policy.NONE);
			layout.setOffAxisJustification(Justification.CENTER);
			layout.setOffAxisPolicy(Policy.STRETCH);
			panel = new BContainer(layout);
			this.add(panel,BorderLayout.WEST);
		}
		else 
			panel.removeAll();
		panel.setPreferredSize(200, -1);

		panel.add(new BLabel(hud.getLocalText("fashion.name")));
		final BTextField name = new BTextField(player.getName());
		name.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent event) {
				player.setName(name.getText());
				name.setText(player.getName());
			}
			@Override
			public void focusGained(FocusEvent event) {}
		});
		panel.add(name);
		name.requestFocus();

		panel.add(new BLabel(hud.getLocalText("fashion.model")));
		final BComboBox model = new BComboBox(models);
		model.setText(player.getModelType().toString());
		model.setStyleClass("combobox");
		model.addListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				player.setModelType((CharacterModel) model.getSelectedValue());
				initMiddle();
				initLeft();
			}
		});
		panel.add(model);

		panel.add(new BLabel(hud.getLocalText("fashion.topcloth")));
		panel.add(new ColorSelect(-1,-1, new ChangeColorListener() {
			@Override
			public void colorChange(float r, float g, float b) {
				player.getTopCloth().set(r, g, b, 1);
				CharacterLoader.applyMaterials(player, s);
			}
		}));
		panel.add(new AmbiantSelect(-1,-1, new ChangeAmbientListener() {
			@Override
			public void ambiantChange(float ambiant) {
				player.setTopClothAmbient(ambiant);
				CharacterLoader.applyMaterials(player, s);
			}
		}));

		panel.add(new BLabel(hud.getLocalText("fashion.bottomcloth")));
		panel.add(new ColorSelect(-1,-1, new ChangeColorListener() {
			@Override
			public void colorChange(float r, float g, float b) {
				player.getBottomCloth().set(r, g, b, 1);
				CharacterLoader.applyMaterials(player, s);
			}
		}));
		panel.add(new AmbiantSelect(-1,-1, new ChangeAmbientListener() {
			@Override
			public void ambiantChange(float ambiant) {
				player.setBottomClothAmbient(ambiant);
				CharacterLoader.applyMaterials(player, s);
			}
		}));

		panel.add(new BLabel(hud.getLocalText("fashion.shoes")));
		panel.add(new ColorSelect(-1,-1, new ChangeColorListener() {
			@Override
			public void colorChange(float r, float g, float b) {
				player.getShoes().set(r, g, b, 1);
				CharacterLoader.applyMaterials(player, s);
			}
		}));
		panel.add(new AmbiantSelect(-1,-1, new ChangeAmbientListener() {
			@Override
			public void ambiantChange(float ambiant) {
				player.setShoesAmbient(ambiant);
				CharacterLoader.applyMaterials(player, s);
			}
		}));

		int hairCutCount = CharacterLoader.getHairCutCount(player);
		if (CharacterLoader.getHairCutCount(player)>0) {

			panel.add(new BLabel(hud.getLocalText("fashion.haircut")));
			String [] cut = new String[CharacterLoader.getHairCutCount(player)];
			for (int i=0;i<hairCutCount;i++)
				cut[i]=Integer.toString(i);
			final BComboBox haircut = new BComboBox(cut);
			haircut.setText(Integer.toString(player.getHairCut()));
			haircut.setStyleClass("combobox");
			haircut.addListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					player.setHairCut(haircut.getSelectedIndex());
					CharacterLoader.applyMaterials(player, s);
				}
			});
			panel.add(haircut);
			
			panel.add(new BLabel(hud.getLocalText("fashion.hairs")));
			panel.add(new ColorSelect(-1,-1, new ChangeColorListener() {
				@Override
				public void colorChange(float r, float g, float b) {
					player.getHair().set(r, g, b, 1);
					CharacterLoader.applyMaterials(player, s);
				}
			}));
			panel.add(new AmbiantSelect(-1,-1, new ChangeAmbientListener() {
				@Override
				public void ambiantChange(float ambiant) {
					player.setHairAmbient(ambiant);
					CharacterLoader.applyMaterials(player, s);
				}
			}));
		}


		panel.add(new BLabel(hud.getLocalText("fashion.skin")));
		panel.add(new ColorSelect(-1,-1, new ChangeColorListener() {
			@Override
			public void colorChange(float r, float g, float b) {
				player.getSkin().set(r, g, b, 1);
				CharacterLoader.applyMaterials(player, s);
			}
		}));
		panel.add(new AmbiantSelect(-1,-1, new ChangeAmbientListener() {
			@Override
			public void ambiantChange(float ambiant) {
				player.setSkinAmbient(ambiant);
				CharacterLoader.applyMaterials(player, s);
			}
		}));

		panel.add(new LaBButton(hud.getLocalText("fashion.apply"),
				new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (!player.isAdmin() && !player.getDisplayName().matches("^[a-zA-Z ]{5,20}$"))
					hud.openErrorPopup(hud.getLocalText("popup.error.invalidname"));
				else {
					boolean valid = player.isAdmin();
					for (CharacterModel m : models)
						valid |= m==player.getModelType();
					if (! valid)
						hud.openErrorPopup(hud.getLocalText("popup.error.inlavidmodeltype"));
					else 
						setVisible(false);
				}
			}
		},"apply"));	
		
	}


	/**
	 * initialise la vue 3D sur joueur
	 */
	private void initMiddle() {
		if (geomView !=null)
			this.remove(geomView);
                System.out.println("StylismeWindow -> initMiddle() : vide !!");
/*
		try {
			s = CharacterLoader.loadNode(player);
			CharacterLoader.applyMaterials(player, s);

			ZBufferState zs = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
			zs.setEnabled(true);

			DirectionalLight light = new DirectionalLight();
			light.setDirection(new Vector3f(-1,-2,-1).normalize());
			light.setEnabled(true);
			light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
			light.setAmbient(new ColorRGBA(.75f, .75f, .75f, 1.0f));

			LightState ls = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
			ls.detachAll();
			ls.attach(light);
			ls.setEnabled(true);

			CullState cs = DisplaySystem.getDisplaySystem().getRenderer().createCullState();
			cs.setEnabled(true);
			cs.setCullFace(CullState.Face.Back);

			s.setRenderState(zs);
			s.setRenderState(cs);
			s.setRenderState(ls);

			s.setCullHint(CullHint.Never);
			//s.setLightCombineMode(LightCombineMode.Off);

			s.updateRenderState();

			s.getLocalScale().multLocal(3);
			s.setLocalTranslation(Vector3f.ZERO);

			s.setModelBound(new BoundingBox());
			s.updateModelBound();
			s.updateWorldBound();

			BoundingBox b = (BoundingBox) s.getWorldBound();
			s.updateGeometricState(0, true);

			geomView = new BGeomView(s);
			geomView.getCamera().lookAt(b.getCenter(), Vector3f.UNIT_Y);
			//geomView.getCamera().getLocation().y = b.getCenter().y;
			geomView.getCamera().update();
			geomView.setStyleClass("geoview");

			this.add(geomView, BorderLayout.CENTER);
		} catch (IOException e) {
			logger.warning("IOException : Je le savais !");
		} catch (ModelFormatException e) {
			logger.warning("ModelFormatException : Je le savais !");
		}
                */
	}


	/* (non-Javadoc)
	 * @see com.jmex.bui.BContainer#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (!visible) {
			BuiSystem.removeWindow(this);
			Variables.getClientConnecteur().commitOnServer(player);
			player.getUserModel().sendCharacterPreference();
		}
	}

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
