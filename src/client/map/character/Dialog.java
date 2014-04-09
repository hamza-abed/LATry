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
package client.map.character;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;

import shared.constants.LaConstants;
import shared.constants.ScriptConstants;
import shared.enums.LaComponent;
import shared.pck.Pck;
import shared.utils.Couple;
import client.editor.EditModel;
import client.editor.FieldEditType;
import client.editor.annotation.Editable;
import client.editor.models.DialogEditModel;
//import client.editor.models.DialogEditModel;
import client.interfaces.network.SharableEditable;
import client.map.World;
import shared.variables.Variables;

/**
 * Dialog d'un pnj
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class Dialog implements SharableEditable, Runnable {
	private static final Logger logger = Logger.getLogger("Dialog");
	private DialogEditModel editModel;

	@Editable(type=FieldEditType.text)
	private String text = LaConstants.DEFAULT_DIALOG;

	private int versionCode = -1;

	private int id;

	//@Editable(type=FieldEditType.list,innerType=couple,coupleTypeA,coupleTypeB,coupleNameA,coupleNameB)
	private ArrayList<Couple<String, String>> choices = new ArrayList<Couple<String, String>>();

	private World world;

	/**
	 * @param world
	 * @param id
	 */
	public Dialog(World world, int id) {
		this.world = world;
		this.id = id;
	}

	/* ********************************************************** *
	 * * 				EDITABLE - Implements 					* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.SharableEditable#getEditModel()
	 */
	@Override
	public EditModel getEditModel() {
		if (editModel == null) {
			editModel = new DialogEditModel(this);
		}
		return editModel;
	}

	/* ********************************************************** *
	 * *				 Sharable - Implements 					* *
	 * ********************************************************** */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#addData(shared.pck.Pck)
	 */
	@Override
	public void addData(Pck pck) {
		pck.putString(text);
		pck.putInt(choices.size());
		for (Couple<String, String> c : choices)
			pck.putString(c.getA(), c.getB());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#getKey()
	 */
	@Override
	public String getKey() {
		return LaComponent.dialog.prefix() + id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.network.Sharable#getVersionCode()
	 */
	@Override
	public int getVersionCode() {
		return versionCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * client.interfaces.network.Sharable#receiveCommitPck(java.nio.ByteBuffer)
	 */
	@Override
	public void receiveCommitPck(ByteBuffer message) {
		this.versionCode = message.getInt();
		this.text = Pck.readString(message);
		int nbChoix = message.getInt();
		this.choices.clear();
		for (int i = 0; i < nbChoix; i++)
			this.choices.add(new Couple<String, String>(
					Pck.readString(message), Pck.readString(message)));
	}

	/* ********************************************************** *
	 * * 					affiche le dialog 					* *
	 * ********************************************************** */

	/**
	 * Affiche le dialog dans la fenetre utile pour cela
	 */
	public void display() {
		logger.info("demande d'affichage du dialog : "+this);
		if (!world.isUpdate(this)) {
			//world.getGame().
                    Variables.getClientConnecteur().updateFromServerAndWait(this, this);
			logger.info("le dialog n'est pas ajour on et en attente ");
		}
		else
			run();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		logger.info("le dialog est a jour on le passe à l'interface : "+this);	
		//world.getGame().getTraces().sendActiveDialog(this);
		//world.getGame().getHud().getDialogWindow().display(this);
	}

	/* ********************************************************** *
	 * * 				GETTERS / SETTERS 						* *
	 * ********************************************************** */

	/**
	 * @return the choices
	 */
	public ArrayList<Couple<String, String>> getChoices() {
		return choices;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Ajout un choix
	 */
	public void addChoice() {
		choices.add(new Couple<String, String>(
				LaConstants.DEFAULT_DIALOG_CHOICE,
				ScriptConstants.VOID_SCRIPT));
	}

	/**
	 * active le choix
	 * @param b
	 */
	public void activChoice(String choice) {
		//world.getGame().getTraces().sendActiveDialogChoice(this,choice);
		world.getScriptExecutor().execute(choice);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getKey();
	}
	
}
