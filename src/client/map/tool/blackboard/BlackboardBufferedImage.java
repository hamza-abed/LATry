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
package client.map.tool.blackboard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Surcouche à BufferedImage. Au lieu de stocker une image qu'on modifie au fur
 * et à mesure, on stocke des objets graphiques qui ont la charge de se
 * dessiner. On peut alors les modifier par la suite (déplacer, changer de
 * couleur...) indépendamment des autres figures.
 * 
 * @author Syscom
 */
public class BlackboardBufferedImage extends BufferedImage {
	private static final Logger logger = Logger
			.getLogger(BlackboardBufferedImage.class.getName());
	private LinkedList<AbstractFigure> shapes = new LinkedList<AbstractFigure>();
	private Graphics2D g;

	public BlackboardBufferedImage(int width, int height, int imageType) {
		super(width, height, imageType);
		g = super.createGraphics();
	}

	/**
	 * Redessine le BlackboardImage : on demande à toutes les figures
	 * enregistrées de se redessiner.
	 */
	public void repaint() {
		logger.entering(BlackboardBufferedImage.class.getName(), "repaint");

		g.clearRect(0, 0, g.getDeviceConfiguration().getBounds().width, g
				.getDeviceConfiguration().getBounds().height);
		for (AbstractFigure s : shapes)
			s.drawShape(g);

		logger.exiting(BlackboardBufferedImage.class.getName(), "repaint");
	}

	/**
	 * Retourne le Graphics2D permettant de dessiner dans l'image.
	 */
	public Graphics2D createGraphics() {
		return g;
	}

	/**
	 * @return La couleur du fond
	 */
	public Color getBackground() {
		return g.getBackground();
	}

	/**
	 * Change la couleur de fond de l'image.
	 * 
	 * @param c
	 */
	public void setBackground(Color c) {
		g.setBackground(c);
	}

	/**
	 * Ajoute une figure à dessiner dans l'image.
	 * 
	 * @param s
	 */
	public void add(AbstractFigure s) {
		shapes.add(s);
	}

	/**
	 * Supprime une figure de l'image.
	 * 
	 * @param s
	 *            La figure à supprimer.
	 */
	public void remove(AbstractFigure s) {
		shapes.remove(s);
	}

	/**
	 * Supprime une liste de figures de l'image (obtenue via select par ex).
	 * 
	 * @param shapeList
	 */
	public void remove(ArrayList<AbstractFigure> shapeList) {
		for (AbstractFigure s : shapeList)
			shapes.remove(s);
	}

	/**
	 * Efface toutes les figures de l'image.
	 */
	public void clear() {
		shapes.clear();
	}

	/**
	 * @param x
	 * @param y
	 * @return La figure contenant le point (x, y) la plus récente.
	 */
	public AbstractFigure select(int x, int y) {
		logger.entering(BlackboardBufferedImage.class.getName(), "select");

		ArrayList<AbstractFigure> shapeList = new ArrayList<AbstractFigure>();
		for (AbstractFigure s : shapes)
			if (s.getRectangle().contains(x, y))
				shapeList.add(s);

		unselectAll();
		if (shapeList.isEmpty())
			return null;

		AbstractFigure s = shapeList.get(shapeList.size() - 1);
		s.setSelected(true);

		logger.exiting(BlackboardBufferedImage.class.getName(), "select");
		return s;
	}

	/**
	 * @param x
	 * @param y
	 * @return La figure contenant le point p la plus récente.
	 */
	public AbstractFigure select(Point p) {
		return select(p.x, p.y);
	}

	/**
	 * Déselectionne toutes les figure de l'image.
	 */
	public void unselectAll() {
		for (AbstractFigure s : shapes)
			s.setSelected(false);
	}
}
