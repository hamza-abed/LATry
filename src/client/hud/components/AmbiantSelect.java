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
package client.hud.components;

import java.util.ArrayList;
import java.util.logging.Logger;


import com.jmex.bui.BContainer;
import com.jmex.bui.background.TintedBackground;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.TableLayout;

/**
 * Selecteur de couleur pour l'editeur de personnage 
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class AmbiantSelect extends BContainer {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger("ColorSelect");

	private static final float STEP = 1/15f;

	private ArrayList<ChangeAmbientListener> list = new ArrayList<ChangeAmbientListener>();

	/**
	 * @param w
	 * @param h
	 */
	public AmbiantSelect(int w, int h, ChangeAmbientListener listener) {
		super(new TableLayout(16, 0, 0));
		this.list.add(listener);
                
                System.out.println("AmbiantSelect -> constructeur : manquante !! "
                        + "TintedBackground ne fonctionne qu'avec JME2");
/*
		for (float a=0;a<=1;a+=STEP) { 
			LaBButton button = new LaBButton("");
			TintedBackground background = new TintedBackground(new ColorRGBA(a,a,a, 1));
			button.setBackground(LaBButton.DOWN, background);
			button.setBackground(LaBButton.DISABLED, background);
			button.setBackground(LaBButton.HOVER, background);
			button.setBackground(LaBButton.DEFAULT, background);
			this.add(button);
			final float ap = Math.min(a,1); 

			button.addListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					changeColor(ap);
				}
			});

			button.setSize(10, 10);
			button.setPreferredSize(10,10);
		}
                */

		//setSize(w, h);
		setPreferredSize(w, h);
	}

	/**
	 * @param ambiant
	 */
	protected void changeColor(float ambiant) {
		for (ChangeAmbientListener l : list ) {
			l.ambiantChange(ambiant);
		}
	}



	public interface ChangeAmbientListener {

		/**
		 * TODO commentaire ChangeColorListener.colorChange
		 * @param r
		 * @param g
		 * @param b
		 */
		void ambiantChange(float ambiant);

	}

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
