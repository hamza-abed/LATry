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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Du texte. Utilise un popup Swing pour gérer le texte et quelques attributs
 * comme sa taille et son style (gras, italique).
 * 
 * @author Syscom
 */
public class Texte extends AbstractFigure {
	private Font font;
	private String text;

	public Texte(int id, int x0, int y0, String text, Color color, int size,
			int style) {
		super(id, x0, y0, color, 0);
		font = new Font("Arial", style, size);
		this.text = text;
	}

	public Texte(int id, int x0, int y0, Color color) {
		this(id, x0, y0, "", color, 20, Font.PLAIN);
	}

	public Texte(int id, Point p0, Color color) {
		this(id, p0.x, p0.y, color);
	}

	/**
	 * @return La taille du texte.
	 */
	public int getSize() {
		return font.getSize();
	}

	/**
	 * @return Le style du texte (un masque contenant Font.BOLD et Font.ITALIC)
	 */
	public int getStyle() {
		return font.getStyle();
	}

	/**
	 * @returnc Le texte.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Définit la taille du texte (en points).
	 * 
	 * @param size
	 */
	public void setSize(int size) {
		font = new Font(font.getName(), font.getStyle(), size);
	}

	/**
	 * Définit le style du texte.
	 * 
	 * @param style
	 *            Ex : Font.PLAIN, Font.BOLD, Font.ITALIC, Font.BOLD |
	 *            Font.ITALIC, Font.BOLD + Font.ITALIC
	 */
	public void setStyle(int style) {
		font = new Font(font.getName(), style, font.getSize());
	}

	/**
	 * Définit le texte à afficher.
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	protected void draw(Graphics2D g) {
		g.setFont(font);
		g.drawString(text, p0.x, p0.y);

		Rectangle r = g.getFontMetrics().getStringBounds(text, g).getBounds();
		bounds.width = r.width;
		bounds.height = r.height;
		bounds.x = p0.x;
		bounds.y = p0.y - bounds.height + 2;
	}

	@Override
	public String toString() {
		return "text¤" + p0.x + "¤" + p0.y + "¤" + text + "¤" + color.getRGB()
				+ "¤" + getSize() + "¤" + getStyle();
	}

	@Override
	public void update(String params) {
		ArrayList<String> paramsList = AbstractFigure.toShapeParams(params);

		setFirstPoint(Integer.parseInt(paramsList.get(1)), Integer
				.parseInt(paramsList.get(2)));
		setText(paramsList.get(3));
		setColor(new Color(Integer.parseInt(paramsList.get(4))));
		setSize(Integer.parseInt(paramsList.get(5)));
		setStyle(Integer.parseInt(paramsList.get(6)));
	}

	/**
	 * @param id
	 * @param params
	 * @return Une nouvelle instance de figure à partir des paramètres
	 *         sauvegardés dans la chaîne params.
	 */
	public static Texte newBBText(int id, String params) {
		ArrayList<String> paramsList = AbstractFigure.toShapeParams(params);

		Point p0 = new Point(Integer.parseInt(paramsList.get(1)), Integer
				.parseInt(paramsList.get(2)));
		String text = paramsList.get(3);
		Color c = new Color(Integer.parseInt(paramsList.get(4)));
		int size = Integer.parseInt(paramsList.get(5));
		int style = Integer.parseInt(paramsList.get(6));

		return new Texte(id, p0.x, p0.y, text, c, size, style);
	}
}
