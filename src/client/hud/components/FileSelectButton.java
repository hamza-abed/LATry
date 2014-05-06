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

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import client.input.MouseCursor;
import client.input.MouseCursor.CursorType;


import com.jmex.bui.BButton;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.event.MouseEvent;
import com.jmex.bui.event.MouseListener;
import shared.variables.Variables;

/**
 * Permet de selectionner un fichier contenu dans un sous dossier,
 * renvoie une url locale
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class FileSelectButton extends BButton implements ActionListener,MouseListener {
	private static final Logger logger = Logger
			.getLogger("FileSelectActionListener");
	private File folder;
	private FileSelectListener listener;

	/**
	 * 
	 */
	public FileSelectButton(String name, File folder,
			FileSelectListener listener) {
		super(name);
		this.folder = folder;
		this.listener = listener;
		this.addListener(this);
	}
	
	
	public FileSelectButton(String name, String folder, FileSelectListener listener) {
		this(name,new File(folder),listener);
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jmex.bui.event.ActionListener#actionPerformed(com.jmex.bui.event.
	 * ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				final JFileChooser fc = new JFileChooser(folder) {
					private static final long serialVersionUID = 4383186120076573127L;

					@Override
					protected JDialog createDialog(Component parent)
							throws HeadlessException {
						JDialog diag = super.createDialog(parent);
						diag.setAlwaysOnTop(true);
						return diag;
					}
				};
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                                    System.out.println("FileSelectButton ->actionPerformed() : manquante !!");
					/*GameTaskQueueManager.getManager().update(
							new Callable<Void>() {
								@Override
								public Void call() throws Exception {
									String f = fc.getSelectedFile()
											.getAbsolutePath().replace(
													folder.getAbsolutePath(),
													"");
									f = f.replaceAll("\\\\", "/").replaceFirst("/", "");
									logger.info("Fichier normalisé " + f);
									if (listener != null)
										listener.fileSelect(f);
									setText(f);
									return null;
								}
							}); */
				}
			}
		}).start();

	}

	/* ********************************************************** *
	 * * 	Classe de gestion de la selection de ficher 		* *
	 * ********************************************************** */

	/**
	 * listener appelé lors de la selection d'un fichier
	 */
	public interface FileSelectListener {
		public void fileSelect(String file);
	}

	/* (non-Javadoc)
	 * @see com.jmex.bui.event.MouseListener#mouseEntered(com.jmex.bui.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent event) {
		if (isEnabled())
			MouseCursor.get().switchCursor(CursorType.object);
	}

	/* (non-Javadoc)
	 * @see com.jmex.bui.event.MouseListener#mouseExited(com.jmex.bui.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent event) {
		MouseCursor.get().switchCursor(CursorType.base);
	}

	/* (non-Javadoc)
	 * @see com.jmex.bui.event.MouseListener#mousePressed(com.jmex.bui.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent event) {
		MouseCursor.get().switchCursor(CursorType.base);
	}

	/* (non-Javadoc)
	 * @see com.jmex.bui.event.MouseListener#mouseReleased(com.jmex.bui.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent event) {}
}
