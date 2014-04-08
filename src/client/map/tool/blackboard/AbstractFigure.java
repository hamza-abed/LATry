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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Tout ce qu'il faut pour gérer une figure.
 * 
 * @author Syscom
 */
public abstract class AbstractFigure {
	private static final Logger logger = Logger
			.getLogger(BlackboardBufferedImage.class.getName());

	/** Identifiant de la figure, utile pour le tableau blanc */
	private int id;
	/** Bounding box de la figure */
	protected Rectangle bounds;
	/** Sa couleur */
	protected Color color;
	/**
	 * 2 extrémités de la figure, pas forcément lié à la BBox, sert pour le
	 * dessin d'une ligne par ex.
	 */
	protected Point p0, p1;
	/**
	 * Pour les polygones ou les ellipses, sert à dire si elle est pleine ou
	 * vide
	 */
	protected boolean fillPoly;
	/** La figure est sélectionnée ou pas (et va se dessiner comme telle) */
	protected boolean selected;
	/** L'épaisseur de trait */
	protected int thickness;

	public AbstractFigure(int id, int x0, int y0, int x1, int y1, Color color,
			boolean fillPoly, int thickness) {
		int x = Math.min(x0, x1);
		int y = Math.min(y0, y1);
		int width = Math.abs(x1 - x0);
		int height = Math.abs(y1 - y0);

		p0 = new Point(x0, y0);
		p1 = new Point(x1, y1);
		this.bounds = new Rectangle(x, y, width, height);

		this.id = id;
		this.color = color;
		this.fillPoly = fillPoly;
		this.thickness = thickness;
		selected = false;
	}

	public AbstractFigure(int id, int x0, int y0, Color color, int thickness) {
		this(id, x0, y0, x0, y0, color, false, thickness);
	}

	public AbstractFigure(int id, Color color, boolean fillPoly, int thickness) {
		this.id = id;
		this.color = color;
		this.fillPoly = fillPoly;
		this.thickness = thickness;
		bounds = new Rectangle();
		selected = false;
	}

	public AbstractFigure(int id, Color color, int thickness) {
		this(id, color, false, thickness);
	}

	/**
	 * Déplace la première extrémité de la figure.
	 * 
	 * @param x
	 * @param y
	 */
	public void setFirstPoint(int x, int y) {
		p0.setLocation(x, y);
		recalculateBounds();
	}

	/**
	 * Déplace la première extrémité de la figure.
	 * 
	 * @param p
	 */
	public void setFirstPoint(Point p) {
		p0.setLocation(p);
		recalculateBounds();
	}

	/**
	 * Déplace la seconde extrémité de la figure.
	 * 
	 * @param x
	 * @param y
	 */
	public void setLastPoint(int x, int y) {
		p1.setLocation(x, y);
		recalculateBounds();
	}

	/**
	 * Déplace la seconde extrémité de la figure.
	 * 
	 * @param p
	 */
	public void setLastPoint(Point p) {
		p1.setLocation(p);
		recalculateBounds();
	}

	/**
	 * Change sa couleur.
	 * 
	 * @param c
	 */
	public void setColor(Color c) {
		color = c;
	}

	/**
	 * Change le mode de remplissage.
	 * 
	 * @param fillPoly
	 */
	public void setFillPoly(boolean fillPoly) {
		this.fillPoly = fillPoly;
	}

	/**
	 * Change l'épaisseur du trait de la figure.
	 * 
	 * @param thick
	 *            L'épaisseur du trait en pixels
	 */
	public void setThickness(int thick) {
		this.thickness = thick;
	}

	/**
	 * La sélectionne/déselectionne.
	 * 
	 * @param selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Recalcule la BBox.
	 */
	protected void recalculateBounds() {
		bounds.x = Math.min(p0.x, p1.x);
		bounds.width = Math.abs(p1.x - p0.x);
		bounds.y = Math.min(p0.y, p1.y);
		bounds.height = Math.abs(p1.y - p0.y);
	}

	/**
	 * Dessin de la figure, avec ou sans cadre de sélection
	 * 
	 * @param g
	 */
	public void drawShape(Graphics2D g) {
		logger.entering(AbstractFigure.class.getName(), "drawShape");

		g.setColor(color);
		Stroke s = g.getStroke();
		g.setStroke(new BasicStroke(thickness));
		draw(g);

		if (selected) {
			g.setColor(Color.lightGray);
			float dash[] = { 2, 2 };
			g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, 10, dash, 0));
			g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
		}

		g.setStroke(s);

		logger.exiting(AbstractFigure.class.getName(), "drawShape");
	}

	/**
	 * Définit comment la figure doit se dessiner.
	 * 
	 * @param g
	 *            Le Graphics2D où doit se dessiner la figure.
	 */
	protected abstract void draw(Graphics2D g);

	/**
	 * @return La couleur de la figure.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * @return La BBox de la figure.
	 */
	public Rectangle getRectangle() {
		return bounds;
	}

	/**
	 * @return Le point central de l'objet.
	 */
	public Point getPosition() {
		Point p = bounds.getLocation();
		p.x += bounds.width / 2;
		p.y += bounds.height / 2;

		return p;
	}

	/**
	 * Définit la nouvelle position du centre de la figure (après un déplacement
	 * à la souris par ex).
	 * 
	 * @param x
	 * @param y
	 */
	public void setPosition(int x, int y) {
		Point offset = new Point(x - getPosition().x, y - getPosition().y);
		p0.translate(offset.x, offset.y);
		p1.translate(offset.x, offset.y);
		bounds.translate(offset.x, offset.y);
	}

	/**
	 * Définit la nouvelle position du centre de la figure (après un déplacement
	 * à la souris par ex).
	 * 
	 * @param p
	 */
	public void setPosition(Point p) {
		setPosition(p.x, p.y);
	}

	/**
	 * @return L'id de la figure.
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return Les attributs de la figure sous forme de chaine de caractère.
	 */
	@Override
	public abstract String toString();

	/**
	 * Met à jour la figure à partir des paramètres générés par un toString()
	 * d'une même figure.
	 * 
	 * @param params
	 */
	public abstract void update(String params);

	/**
	 * Découpe la chaîne pour faciliter l'accès à chaque paramètre.
	 * 
	 * @param params
	 *            Les attributs d'une figure après un toString().
	 * @return La liste des paramètres sous forme de chaines de caractères.
	 */
	public static ArrayList<String> toShapeParams(String params) {
		ArrayList<String> paramsList = new ArrayList<String>();
		for (String p : params.split("¤"))
			paramsList.add(p);

		return paramsList;
	}
}
