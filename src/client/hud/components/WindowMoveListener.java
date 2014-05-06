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

import client.input.MouseCursor;
import client.input.MouseCursor.CursorType;

import com.jmex.bui.BWindow;
import com.jmex.bui.event.MouseEvent;
import com.jmex.bui.event.MouseListener;
import com.jmex.bui.event.MouseMotionListener;

/**
 * Listener permettant de deplacement d'une fenetre GBUI
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class WindowMoveListener implements MouseMotionListener, MouseListener {
	private BWindow win;
	private int x, y;

	/**
	 * @param graphicEditWindow
	 */
	public WindowMoveListener(BWindow window) {
		this.win = window;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jmex.bui.event.MouseMotionListener#mouseDragged(com.jmex.bui.event
	 * .MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent event) {
		if (event.getModifiers() == MouseEvent.BUTTON1_DOWN_MASK) {
			int dx = event.getX() - x;
			int dy = event.getY() - y;
			win.setLocation(win.getX() + dx, win.getY() + dy);
			x = event.getX();
			y = event.getY();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jmex.bui.event.MouseMotionListener#mouseMoved(com.jmex.bui.event.
	 * MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent event) {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jmex.bui.event.MouseListener#mouseEntered(com.jmex.bui.event.MouseEvent
	 * )
	 */
	@Override
	public void mouseEntered(MouseEvent event) { 
		MouseCursor.get().switchCursor(CursorType.move);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jmex.bui.event.MouseListener#mouseExited(com.jmex.bui.event.MouseEvent
	 * )
	 */
	@Override
	public void mouseExited(MouseEvent event) { 
		MouseCursor.get().switchCursor(CursorType.base);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jmex.bui.event.MouseListener#mousePressed(com.jmex.bui.event.MouseEvent
	 * )
	 */
	@Override
	public void mousePressed(MouseEvent event) {
		this.x = event.getX();
		this.y = event.getY();
		win.setLayer(1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jmex.bui.event.MouseListener#mouseReleased(com.jmex.bui.event.MouseEvent
	 * )
	 */
	@Override
	public void mouseReleased(MouseEvent event) {
		win.setLayer(0);
	}

}
