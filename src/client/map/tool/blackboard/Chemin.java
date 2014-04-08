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
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Une ligne brisée représentant un dessin à main levée, un graphe, etc. Proche
 * de la classe BBPolygon qui a été écrite après, et finalement utiliser un
 * Polygon à la place d'une LinkedList aurait été aussi bien...
 * 
 * @author Syscom
 */
public class Chemin extends AbstractFigure {
	/** Liste des points */
	private LinkedList<Point> points = new LinkedList<Point>();

	public Chemin(int id, Point p0, Color color, int thickness) {
		super(id, color, thickness);
		addPoint(p0);
	}

	public Chemin(int id, int x0, int y0, Color color, int thickness) {
		this(id, new Point(x0, y0), color, thickness);
	}

	/**
	 * Ajoute le point p à la forme.
	 * 
	 * @param p
	 */
	public void addPoint(Point p) {
		points.add(p);
		recalculateBounds();
	}

	/**
	 * Ajoute le point (x, y) à la forme.
	 * 
	 * @param x
	 * @param y
	 */
	public void addPoint(int x, int y) {
		addPoint(new Point(x, y));
	}

	/**
	 * Efface tous les points de la forme.
	 */
	public void clear() {
		points.clear();
		recalculateBounds();
	}

	@Override
	protected void recalculateBounds() {
		if (points.isEmpty()) {
			bounds.setRect(-1, -1, 0, 0);
			return;
		}

		Point min = new Point(points.getFirst());
		Point max = new Point(points.getFirst());
		for (Point p : points) {
			if (p.x < min.x)
				min.x = p.x;
			if (p.y < min.y)
				min.y = p.y;
			if (p.x > max.x)
				max.x = p.x;
			if (p.y > max.y)
				max.y = p.y;
		}

		bounds.x = min.x;
		bounds.y = min.y;
		bounds.width = max.x - min.x;
		bounds.height = max.y - min.y;
	}

	@Override
	protected void draw(Graphics2D g) {
		if (points.isEmpty())
			return;

		Point pprec = points.getFirst();
		for (Point p : points) {
			g.drawLine(pprec.x, pprec.y, p.x, p.y);
			pprec = p;
		}
	}

	@Override
	public void setPosition(int x, int y) {
		Point offset = new Point(x - getPosition().x, y - getPosition().y);

		bounds.translate(offset.x, offset.y);
		for (Point p : points)
			p.translate(offset.x, offset.y);
	}

	@Override
	public String toString() {
		String s = "path¤" + color.getRGB() + "¤" + thickness;
		for (Point p : points)
			s += "¤" + p.x + "¤" + p.y;
		return s;
	}

	@Override
	public void update(String params) {
		ArrayList<String> paramsList = AbstractFigure.toShapeParams(params);

		setColor(new Color(Integer.parseInt(paramsList.get(1))));
		setThickness(Integer.parseInt(paramsList.get(2)));

		clear();

		int i = 3;
		while (i < paramsList.size()) {
			String x = paramsList.get(i);
			String y = paramsList.get(i + 1);
			addPoint(Integer.parseInt(x), Integer.parseInt(y));

			i += 2;
		}
	}

	/**
	 * @param id
	 * @param params
	 * @return Une nouvelle instance de figure à partir des paramètres
	 *         sauvegardés dans la chaîne params.
	 */
	public static Chemin newBBPath(int id, String params) {
		ArrayList<String> paramsList = AbstractFigure.toShapeParams(params);

		Color c = new Color(Integer.parseInt(paramsList.get(1)));
		int thick = Integer.parseInt(paramsList.get(2));
		Point p0 = new Point(Integer.parseInt(paramsList.get(3)), Integer
				.parseInt(paramsList.get(4)));

		Chemin path = new Chemin(id, p0, c, thick);

		int i = 5;
		while (i < paramsList.size()) {
			String x = paramsList.get(i);
			String y = paramsList.get(i + 1);
			path.addPoint(Integer.parseInt(x), Integer.parseInt(y));

			i += 2;
		}

		return path;
	}
}
