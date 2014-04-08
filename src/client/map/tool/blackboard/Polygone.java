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
import java.awt.Polygon;
import java.util.ArrayList;

/**
 * Un polygône à nombre arbitraire de côtés.
 * 
 * @author Syscom
 */
public class Polygone extends AbstractFigure {
	/** Liste des points */
	private Polygon poly;
	/**
	 * Indique si la figure est considérée comme complète ou en train d'être
	 * dessinée
	 */
	private boolean partial;

	public Polygone(int id, int x0, int y0, int x1, int y1, Color color,
			boolean fill, int thickness) {
		super(id, color, fill, thickness);
		poly = new Polygon();
		poly.addPoint(x0, y0);
		poly.addPoint(x1, y1);
		partial = true;
	}

	public Polygone(int id, Point p0, Point p1, Color color, boolean fill,
			int thickness) {
		this(id, p0.x, p0.y, p1.x, p1.y, color, fill, thickness);
	}

	/**
	 * Ajoute le point (x, y) au polygone.
	 * 
	 * @param x
	 * @param y
	 */
	public void addPoint(int x, int y) {
		poly.addPoint(x, y);
		recalculateBounds();
	}

	/**
	 * Ajoute le point p au polygone.
	 * 
	 * @param p
	 */
	public void addPoint(Point p) {
		addPoint(p.x, p.y);
		recalculateBounds();
	}

	/**
	 * Efface tous les points du polygone.
	 */
	public void clear() {
		poly.reset();
		recalculateBounds();
	}

	/**
	 * @return Le premier point du polygone. Pendant la construction de la
	 *         figure, permet de savoir quand on ferme la figure (on a cliqué
	 *         près de ce premier point par ex).
	 */
	public Point getFirstPoint() {
		if (poly.npoints == 0)
			return null;
		return new Point(poly.xpoints[0], poly.ypoints[0]);
	}

	/**
	 * Modifie les coordonnées du dernier point entré.
	 * 
	 * @param x
	 * @param y
	 */
	@Override
	public void setLastPoint(int x, int y) {
		if (poly.npoints == 0)
			return;
		poly.xpoints[poly.npoints - 1] = x;
		poly.ypoints[poly.npoints - 1] = y;
		recalculateBounds();
	}

	/**
	 * Modifie les coordonnées du dernier point entré.
	 * 
	 * @param p
	 */
	@Override
	public void setLastPoint(Point p) {
		setLastPoint(p.x, p.y);
	}

	/**
	 * Permet d'informer si cette figure doit être considérée comme terminée. Si
	 * ça n'est pas le cas, le polygône est affiché comme une ligne brisée, non
	 * fermée.
	 * 
	 * @param finished
	 */
	public void setFinished(boolean finished) {
		partial = !finished;
	}

	@Override
	protected void recalculateBounds() {
		poly.invalidate();
		bounds = poly.getBounds();
	}

	@Override
	protected void draw(Graphics2D g) {
		if (partial) {
			float dash[] = { 2, 2 };
			g.setStroke(new BasicStroke(thickness, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, 10, dash, 0));
			g.drawPolyline(poly.xpoints, poly.ypoints, poly.npoints);
		} else if (fillPoly)
			g.fillPolygon(poly);
		else
			g.drawPolygon(poly);
	}

	@Override
	public void setPosition(int x, int y) {
		Point offset = new Point(x - getPosition().x, y - getPosition().y);

		poly.translate(offset.x, offset.y);
		bounds.translate(offset.x, offset.y);
	}

	@Override
	public String toString() {
		String s = "polygon¤" + color.getRGB() + "¤" + fillPoly + "¤"
				+ thickness;
		for (int i = 0; i < poly.npoints; i++)
			s += "¤" + poly.xpoints[i] + "¤" + poly.ypoints[i];
		return s;
	}

	@Override
	public void update(String params) {
		ArrayList<String> paramsList = AbstractFigure.toShapeParams(params);

		setColor(new Color(Integer.parseInt(paramsList.get(1))));
		setFillPoly(Boolean.parseBoolean(paramsList.get(2)));
		setThickness(Integer.parseInt(paramsList.get(3)));

		clear();

		int i = 4;
		while (i < paramsList.size()) {
			String x = paramsList.get(i);
			String y = paramsList.get(i + 1);
			addPoint(Integer.parseInt(x), Integer.parseInt(y));

			i += 2;
		}

		setFinished(true);
	}

	/**
	 * @param id
	 * @param params
	 * @return Une nouvelle instance de figure à partir des paramètres
	 *         sauvegardés dans la chaîne params.
	 */
	public static Polygone newBBPolygon(int id, String params) {
		ArrayList<String> paramsList = AbstractFigure.toShapeParams(params);

		Color c = new Color(Integer.parseInt(paramsList.get(1)));
		boolean fill = Boolean.parseBoolean(paramsList.get(2));
		int thick = Integer.parseInt(paramsList.get(3));
		Point p0 = new Point(Integer.parseInt(paramsList.get(4)), Integer
				.parseInt(paramsList.get(5)));
		Point p1 = new Point(Integer.parseInt(paramsList.get(6)), Integer
				.parseInt(paramsList.get(7)));

		Polygone poly = new Polygone(id, p0, p1, c, fill, thick);

		int i = 8;
		while (i < paramsList.size()) {
			String x = paramsList.get(i);
			String y = paramsList.get(i + 1);
			poly.addPoint(Integer.parseInt(x), Integer.parseInt(y));

			i += 2;
		}

		poly.setFinished(true);
		return poly;
	}
}
