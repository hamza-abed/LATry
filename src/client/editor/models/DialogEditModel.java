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
package client.editor.models;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import client.editor.EditModel;
import client.editor.FieldEditType;
import client.map.character.Dialog;

/**
 * Model d'edition d'un dialog
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class DialogEditModel extends EditModel {
	private ArrayList<String> names = new ArrayList<String>();

	private Dialog dialog;

	/**
	 * @param dialog
	 */
	public DialogEditModel(Dialog dialog) {
		this.dialog = dialog;
		names.add("text");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.editor.EditModel#apply(java.lang.String, java.lang.Object)
	 */
	@Override
	protected void apply(String name, Object value) {
		int i = names.indexOf(name);
		if (i == 0)
			dialog.setText((String) value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.editor.EditModel#getEnumValues(java.lang.String)
	 */
	@Override
	public Object[] getEnumValues(String name) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.editor.EditModel#getFieldList()
	 */
	@Override
	public Collection<String> getFieldList() {
		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.editor.EditModel#getFieldType(java.lang.String)
	 */
	@Override
	public FieldEditType getFieldType(String name) {
		int i = names.indexOf(name);
		if (i == 0)
			return FieldEditType.text;
		return FieldEditType.action;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.editor.EditModel#getFileFolder(java.lang.String)
	 */
	@Override
	public File getFileFolder(String name) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.editor.EditModel#getMaxValue(java.lang.String)
	 */
	@Override
	public Number getMaxValue(String name) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.editor.EditModel#getMinValue(java.lang.String)
	 */
	@Override
	public Number getMinValue(String name) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.editor.EditModel#getValue(java.lang.String)
	 */
	@Override
	public Object getValue(String name) {
		int i = names.indexOf(name);
		if (i == 0)
			return dialog.getText();
		return null;
	}

	/* ********************************************************** *
	 * * TAG * * **********************************************************
	 */

	/* ********************************************************** *
	 * * TAG * * **********************************************************
	 */
}
