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

import com.jme3.math.Vector2f;
import com.jme3.scene.shape.Quad;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.util.HashMap;
import java.util.logging.Logger;
/*
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.Vector2f;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
*/
/**
 * Le panneau associé au tableau blanc. Il permet de sélectionner son outil de
 * dessin, la couleur, ...
 * 
 * @author Syscom
 */
public class ToolPanel extends Quad {
	private static final long serialVersionUID = -5233449732993845032L;
	private static final Logger logger = Logger.getLogger(ToolPanel.class
			.getName());

	/** Résolution de la texture du panneau */
	private static final int TEXTURE_RESOLUTION_X = 64,
			TEXTURE_RESOLUTION_Y = 256;

	/** Le moteur du tableau blanc */
	private BlackboardEngine blackboard;
	/** L'image du panneau */
	private BlackboardBufferedImage image;
	/** Le Graphics2D pour dessiner dans l'image du panneau */
	private Graphics2D g;
	//private TextureState ts;
	/** Couleur courante */
	private Color currColor;
	/**
	 * Coordonnée de la couleur courante dans la palette, pour regénérer la
	 * couleur à partir du gradient de saturation
	 */
	private Point currColorCoords;
	/** Saturation de la couleur courante */
	private float saturation;
	/** Coordonnées des boutons */
	private Rectangle selectCoords, deleteCoords, pencilCoords, lineCoords,
			rectCoords, ovalCoords, polygonCoords, textCoords, thicknessCoords,
			emptyPolyCoords, fillPolyCoords;
	/** Bouton sélectionné */
	private Rectangle selected;
	/** Coordonnées de la palette */
	private Rectangle paletteCoords, saturationCoords, colorCoords;
	/** Mode de remplissage des polygones courant */
	private boolean fillPoly;
	/** Epaisseur de trait courante */
	private int thickness;
	/** Si une figure est sélectionnée (et donc supprimable) */
	private boolean canDelete;

	public ToolPanel(String name, float width, float height,
			BlackboardEngine blackboard, BlackboardEngine.Modes mode,
			Color color, boolean fillPoly, int thickness) {
		//super(name, width, height);

		this.blackboard = blackboard;
		image = new BlackboardBufferedImage(TEXTURE_RESOLUTION_X,
				TEXTURE_RESOLUTION_Y, BlackboardBufferedImage.TYPE_INT_RGB);
		g = image.createGraphics();
		HashMap<Key, Object> hints = new HashMap<Key, Object>();
		hints.put(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.addRenderingHints(hints);

		// Définition des coordonnées
		selectCoords = newRectangle(0.1f, 0.1f, 0.35f, 0.35f);
		deleteCoords = newRectangle(0.55f, 0.1f, 0.35f, 0.35f);
		pencilCoords = newRectangle(0.1f, 0.55f, 0.35f, 0.35f);
		lineCoords = newRectangle(0.55f, 0.55f, 0.35f, 0.35f);
		rectCoords = newRectangle(0.1f, 1.0f, 0.35f, 0.35f);
		ovalCoords = newRectangle(0.55f, 1.0f, 0.35f, 0.35f);
		polygonCoords = newRectangle(0.1f, 1.45f, 0.35f, 0.35f);
		textCoords = newRectangle(0.55f, 1.45f, 0.35f, 0.35f);
		emptyPolyCoords = newRectangle(0.1f, 1.95f, 0.4f, 0.5f);
		fillPolyCoords = newRectangle(0.5f, 1.95f, 0.4f, 0.5f);
		thicknessCoords = newRectangle(0.1f, 2.55f, 0.8f, 0.6f);
		colorCoords = newRectangle(0.4f, 3.25f, 0.2f, 0.2f);
		saturationCoords = newRectangle(0.1f, 3.5f, 0.1f, 0.4f);
		paletteCoords = newRectangle(0.25f, 3.5f, 0.65f, 0.4f);

		switch (mode) {
		case SELECT:
			selected = selectCoords;
			break;
		case PENCIL:
			selected = pencilCoords;
			break;
		case LINE:
			selected = lineCoords;
			break;
		case RECTANGLE:
			selected = rectCoords;
			break;
		case OVAL:
			selected = ovalCoords;
			break;
		case POLYGON:
			selected = polygonCoords;
			break;
		case TEXT:
			selected = textCoords;
			break;
		default:
			selected = selectCoords;
		}

		canDelete = false;
		this.fillPoly = fillPoly;
		this.thickness = thickness;
		this.currColor = color;
		currColorCoords = new Point(paletteCoords.x, paletteCoords.y
				+ paletteCoords.height);
		saturation = 1.0f;

                /*
		ts = DisplaySystem.getDisplaySystem().getRenderer()
				.createTextureState();
		setRenderState(ts);
*/
		createGraphics();
	}

	// Raccourci facilitant l'écriture...
	private Rectangle newRectangle(float x, float y, float width, float height) {
            return null;
		/*float w = TEXTURE_RESOLUTION_X, h = TEXTURE_RESOLUTION_Y * this.width
				/ this.height;
		return new Rectangle((int) (x * w), (int) (y * h), (int) (width * w),
				(int) (height * h)); */
	}

	/**
	 * Redessine le panneau
	 */
	public void createGraphics() {
		logger.entering(ToolPanel.class.getName(), "createGraphics");

		g.setBackground(Color.white);
		g.clearRect(0, 0, TEXTURE_RESOLUTION_X, TEXTURE_RESOLUTION_Y);

		drawSelectButton();
		drawDeleteButton();
		drawPencilButton();
		drawLineButton();
		drawRectButton();
		drawOvalButton();
		drawPolygonButton();
		drawTextButton();
		drawEmptyPolyButton();
		drawFillPolyButton();
		drawThicknessButton();
		drawCurrColor();
		drawPalette();
		drawSaturationCoords();
/*TextureManager.clearCache();
		boolean compress = TextureManager.COMPRESS_BY_DEFAULT;
		TextureManager.COMPRESS_BY_DEFAULT = false;
		ts.setTexture(TextureManager.loadTexture(image,
				MinificationFilter.BilinearNoMipMaps,
				MagnificationFilter.Bilinear, true));
		TextureManager.COMPRESS_BY_DEFAULT = compress;
		updateRenderState();

		logger.exiting(ToolPanel.class.getName(), "createGraphics");
                */
	}

	/**
	 * Dessin du bouton de sélection
	 */
	private void drawSelectButton() {
		logger.entering(ToolPanel.class.getName(), "drawSelectButton");

		drawButton(selectCoords, selected == selectCoords);
		g.drawLine(selectCoords.x + selectCoords.width / 2, selectCoords.y
				+ selectCoords.height / 2, selectCoords.x + 5
				* selectCoords.width / 8, selectCoords.y + 6
				* selectCoords.height / 8);

		Polygon p = new Polygon();
		p.addPoint(selectCoords.x + selectCoords.width / 2, selectCoords.y
				+ selectCoords.height / 2);
		p.addPoint(selectCoords.x + 6 * selectCoords.width / 9, selectCoords.y
				+ selectCoords.height / 2);
		p.addPoint(selectCoords.x + 3 * selectCoords.width / 8, selectCoords.y
				+ 2 * selectCoords.height / 8);
		p.addPoint(selectCoords.x + 3 * selectCoords.width / 8, selectCoords.y
				+ 5 * selectCoords.height / 8);
		g.fillPolygon(p);

		logger.exiting(ToolPanel.class.getName(), "drawSelectButton");
	}

	/**
	 * Dessin du bouton de suppression
	 */
	private void drawDeleteButton() {
		logger.entering(ToolPanel.class.getName(), "drawDeleteButton");

		drawButton(deleteCoords, selected == deleteCoords);

		Stroke s = g.getStroke();
		g.setStroke(new BasicStroke(3));
		g.setColor(canDelete ? Color.red : Color.gray);

		g.drawLine(deleteCoords.x + deleteCoords.width / 4, deleteCoords.y
				+ deleteCoords.height / 4, deleteCoords.x + 3
				* deleteCoords.width / 4, deleteCoords.y + 3
				* deleteCoords.height / 4);
		g.drawLine(deleteCoords.x + 3 * deleteCoords.width / 4, deleteCoords.y
				+ deleteCoords.height / 4, deleteCoords.x + deleteCoords.width
				/ 4, deleteCoords.y + 3 * deleteCoords.height / 4);

		g.setStroke(s);

		logger.exiting(ToolPanel.class.getName(), "drawDeleteButton");
	}

	/**
	 * Dessin du bouton de dessin à main levée
	 */
	private void drawPencilButton() {
		logger.entering(ToolPanel.class.getName(), "drawPencilButton");

		drawButton(pencilCoords, selected == pencilCoords);

		Point prec = new Point(pencilCoords.x + pencilCoords.width / 8,
				pencilCoords.y + 7 * pencilCoords.height / 8);
		for (int x = pencilCoords.x + pencilCoords.width / 8; x < pencilCoords.x
				+ 7 * pencilCoords.width / 8; x++) {
			int y = prec.y - (int) (2 * Math.sin(42 * x) + 1);

			g.drawLine(prec.x, prec.y, x, y);

			prec.x = x;
			prec.y = y;
		}

		logger.exiting(ToolPanel.class.getName(), "drawPencilButton");
	}

	/**
	 * Dessin du bouton ligne
	 */
	private void drawLineButton() {
		logger.entering(ToolPanel.class.getName(), "drawLineButton");

		drawButton(lineCoords, selected == lineCoords);
		g.drawLine(lineCoords.x + lineCoords.width / 8, lineCoords.y + 7
				* lineCoords.height / 8, lineCoords.x + 7 * lineCoords.width
				/ 8, lineCoords.y + lineCoords.height / 8);

		logger.exiting(ToolPanel.class.getName(), "drawLineButton");
	}

	/**
	 * Dessin du bouton rectangle
	 */
	private void drawRectButton() {
		logger.entering(ToolPanel.class.getName(), "drawRectButton");

		drawButton(rectCoords, selected == rectCoords);
		g.drawRect(rectCoords.x + rectCoords.width / 8 + 1, rectCoords.y
				+ rectCoords.height / 6 + 1, 3 * rectCoords.width / 4,
				2 * rectCoords.height / 3);

		logger.exiting(ToolPanel.class.getName(), "drawRectButton");
	}

	/**
	 * Dessin du bouton ellipse
	 */
	private void drawOvalButton() {
		logger.entering(ToolPanel.class.getName(), "drawOvalButton");

		drawButton(ovalCoords, selected == ovalCoords);
		g.drawOval(ovalCoords.x + ovalCoords.width / 8 + 1, ovalCoords.y
				+ ovalCoords.height / 7 + 1, 3 * ovalCoords.width / 4,
				5 * ovalCoords.height / 7);

		logger.exiting(ToolPanel.class.getName(), "drawOvalButton");
	}

	/**
	 * Dessin du bouton Polygon
	 */
	private void drawPolygonButton() {
		logger.entering(ToolPanel.class.getName(), "drawPolygonButton");

		drawButton(polygonCoords, selected == polygonCoords);
		Polygon p = new Polygon();
		p.addPoint(polygonCoords.x + 4 * polygonCoords.width / 6,
				polygonCoords.y + polygonCoords.height / 6);
		p.addPoint(polygonCoords.x + 5 * polygonCoords.width / 6,
				polygonCoords.y + 3 * polygonCoords.height / 6);
		p.addPoint(polygonCoords.x + 4 * polygonCoords.width / 6,
				polygonCoords.y + 5 * polygonCoords.height / 6);
		p.addPoint(polygonCoords.x + polygonCoords.width / 6, polygonCoords.y
				+ 4 * polygonCoords.height / 6);
		p.addPoint(polygonCoords.x + 3 * polygonCoords.width / 6,
				polygonCoords.y + 3 * polygonCoords.height / 6);
		g.drawPolygon(p);

		logger.exiting(ToolPanel.class.getName(), "drawPolygonButton");
	}

	/**
	 * Dessin du bouton de texte
	 */
	private void drawTextButton() {
		logger.entering(ToolPanel.class.getName(), "drawTextButton");

		drawButton(textCoords, selected == textCoords);
		g.setFont(new Font(Font.SERIF, Font.PLAIN, textCoords.height));
		g.drawString("T", textCoords.x + textCoords.width / 4, textCoords.y + 9
				* textCoords.height / 10);

		logger.exiting(ToolPanel.class.getName(), "drawTextButton");
	}

	/**
	 * Dessin du bouton polygone vide
	 */
	private void drawEmptyPolyButton() {
		logger.entering(ToolPanel.class.getName(), "drawEmptyPolyButton");

		drawButton(emptyPolyCoords, !fillPoly);
		g.drawOval(emptyPolyCoords.x + emptyPolyCoords.width / 8,
				emptyPolyCoords.y + emptyPolyCoords.height / 8,
				3 * emptyPolyCoords.width / 4, 3 * emptyPolyCoords.height / 4);

		logger.exiting(ToolPanel.class.getName(), "drawEmptyPolyButton");
	}

	/**
	 * Dessin du bouton polygone plein
	 */
	private void drawFillPolyButton() {
		logger.entering(ToolPanel.class.getName(), "drawFillPolyButton");

		drawButton(fillPolyCoords, fillPoly);
		g.fillOval(fillPolyCoords.x + fillPolyCoords.width / 8,
				fillPolyCoords.y + fillPolyCoords.height / 8,
				6 * fillPolyCoords.width / 8, 6 * fillPolyCoords.height / 8);

		logger.exiting(ToolPanel.class.getName(), "drawFillPolyButton");
	}

	/**
	 * Dessin du panel de l'épaisseur de trait
	 */
	private void drawThicknessButton() {
		logger.entering(ToolPanel.class.getName(), "drawThicknessButton");

		Stroke s = g.getStroke();
		drawButton(thicknessCoords, false);
		g.setColor(thickness == 1 ? Color.black : Color.lightGray);
		g.drawLine(thicknessCoords.x + thicknessCoords.width / 6,
				thicknessCoords.y + thicknessCoords.height / 5,
				thicknessCoords.x + 5 * thicknessCoords.width / 6,
				thicknessCoords.y + thicknessCoords.height / 5);
		g.setStroke(new BasicStroke(2));
		g.setColor(thickness == 2 ? Color.black : Color.lightGray);
		g.drawLine(thicknessCoords.x + thicknessCoords.width / 6,
				thicknessCoords.y + 2 * thicknessCoords.height / 5,
				thicknessCoords.x + 5 * thicknessCoords.width / 6,
				thicknessCoords.y + 2 * thicknessCoords.height / 5);
		g.setStroke(new BasicStroke(3));
		g.setColor(thickness == 3 ? Color.black : Color.lightGray);
		g.drawLine(thicknessCoords.x + thicknessCoords.width / 6,
				thicknessCoords.y + 3 * thicknessCoords.height / 5,
				thicknessCoords.x + 5 * thicknessCoords.width / 6,
				thicknessCoords.y + 3 * thicknessCoords.height / 5);
		g.setStroke(new BasicStroke(4));
		g.setColor(thickness == 4 ? Color.black : Color.lightGray);
		g.drawLine(thicknessCoords.x + thicknessCoords.width / 6,
				thicknessCoords.y + 4 * thicknessCoords.height / 5,
				thicknessCoords.x + 5 * thicknessCoords.width / 6,
				thicknessCoords.y + 4 * thicknessCoords.height / 5);
		g.setStroke(s);

		logger.exiting(ToolPanel.class.getName(), "drawThicknessButton");
	}

	/**
	 * Dessin du fond d'un bouton
	 * 
	 * @param rect
	 *            Coordonnées du bouton
	 * @param active
	 *            mode sélectionné/non sélectionné
	 */
	private void drawButton(Rectangle rect, boolean active) {
		logger.entering(ToolPanel.class.getName(), "drawButton");

		g.setColor(Color.lightGray);
		if (active)
			g.fillRect(rect.x, rect.y, rect.width, rect.height);
		g.drawRect(rect.x, rect.y, rect.width, rect.height);
		g.setColor(Color.black);

		logger.exiting(ToolPanel.class.getName(), "drawButton");
	}

	/**
	 * Dessin de la couleur courante
	 */
	private void drawCurrColor() {
		logger.entering(ToolPanel.class.getName(), "drawCurrColor");

		g.setColor(currColor);
		g.fillRect(colorCoords.x, colorCoords.y, colorCoords.width,
				colorCoords.height);
		drawButton(colorCoords, false);

		logger.exiting(ToolPanel.class.getName(), "drawCurrColor");
	}

	/**
	 * Dessin de la palette
	 */
	private void drawPalette() {
		logger.entering(ToolPanel.class.getName(), "drawPalette");

		for (int y = paletteCoords.y; y <= paletteCoords.y
				+ paletteCoords.height; y++)
			for (int x = paletteCoords.x; x <= paletteCoords.x
					+ paletteCoords.width; x++)
				image.setRGB(x, y, paletteToRGB(x, y, 1.0f).getRGB());

		logger.exiting(ToolPanel.class.getName(), "drawPalette");
	}

	/**
	 * Dessin du gradient de luminosité de la couleur courante
	 */
	private void drawSaturationCoords() {
		logger.entering(ToolPanel.class.getName(), "drawBrightnessCoords");

		for (int y = saturationCoords.y; y <= saturationCoords.y
				+ saturationCoords.height; y++)
			for (int x = saturationCoords.x; x <= saturationCoords.x
					+ saturationCoords.width; x++)
				image.setRGB(x, y, saturationToRGB(x, y).getRGB());

		logger.exiting(ToolPanel.class.getName(), "drawBrightnessCoords");
	}

	/**
	 * Permet de passer d'une coordonnée sur la palette à la couleur associée.
	 * 
	 * @param x
	 *            Coordonnée x du point à échantillonner
	 * @param y
	 *            Coordonnée y du point à échantillonner
	 * @return La couleur associée
	 */
	private Color paletteToRGB(int x, int y, float saturation) {
		logger.entering(ToolPanel.class.getName(), "paletteToRGB");

		if (!paletteCoords.contains(x, y))
			return Color.white;

		float hue = ((float) x - paletteCoords.x) / paletteCoords.width;
		float brightness = 1.0f - ((float) y - paletteCoords.y)
				/ paletteCoords.height;

		logger.exiting(ToolPanel.class.getName(), "paletteToRGB");
		return new Color(Color.HSBtoRGB(hue, saturation, brightness));
	}

	/**
	 * Permet de passer d'une coordinnée sur le gradient à la couleur associée.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Color saturationToRGB(int x, int y) {
		logger.entering(ToolPanel.class.getName(), "brightnessToRGB");

		if (!saturationCoords.contains(x, y))
			return Color.white;

		float hue = ((float) currColorCoords.x - paletteCoords.x)
				/ paletteCoords.width;
		float brightness = 1.0f - ((float) currColorCoords.y - paletteCoords.y)
				/ paletteCoords.height;
		float saturation = ((float) y - saturationCoords.y)
				/ saturationCoords.height;

		logger.exiting(ToolPanel.class.getName(), "brightnessToRGB");
		return new Color(Color.HSBtoRGB(hue, saturation, brightness));
	}

	/**
	 * Informe qu'un objet est sélectionné pour autoriser la suppression.
	 * 
	 * @param deletable
	 */
	public void setDeletable(boolean deletable) {
		canDelete = deletable;
		createGraphics();
	}

	/**
	 * Gestion du clic de la souris.
	 * 
	 * @param mouseCoord
	 */
	public void mouseClick(Vector2f mouseCoord) {
		logger.entering(ToolPanel.class.getName(), "mouseClick");

		float x = (mouseCoord.x + 0.5f) * TEXTURE_RESOLUTION_X;
		float y = -(mouseCoord.y - 0.5f) * TEXTURE_RESOLUTION_Y;

		if (selectCoords.contains(x, y)) {
			blackboard.setDrawMode(BlackboardEngine.Modes.SELECT);
			selected = selectCoords;
		} else if (canDelete && deleteCoords.contains(x, y)) {
			blackboard.delete();
		} else if (pencilCoords.contains(x, y)) {
			blackboard.setDrawMode(BlackboardEngine.Modes.PENCIL);
			selected = pencilCoords;
		} else if (lineCoords.contains(x, y)) {
			blackboard.setDrawMode(BlackboardEngine.Modes.LINE);
			selected = lineCoords;
		} else if (rectCoords.contains(x, y)) {
			blackboard.setDrawMode(BlackboardEngine.Modes.RECTANGLE);
			selected = rectCoords;
		} else if (ovalCoords.contains(x, y)) {
			blackboard.setDrawMode(BlackboardEngine.Modes.OVAL);
			selected = ovalCoords;
		} else if (polygonCoords.contains(x, y)) {
			blackboard.setDrawMode(BlackboardEngine.Modes.POLYGON);
			selected = polygonCoords;
		} else if (textCoords.contains(x, y)) {
			blackboard.setDrawMode(BlackboardEngine.Modes.TEXT);
			selected = textCoords;
		} else if (emptyPolyCoords.contains(x, y)) {
			fillPoly = false;
			blackboard.setDrawMode(fillPoly);
		} else if (fillPolyCoords.contains(x, y)) {
			fillPoly = true;
			blackboard.setDrawMode(fillPoly);
		} else if (thicknessCoords.contains(x, y)) {
			if (y > thicknessCoords.y + 0.5 * thicknessCoords.height / 5
					&& y < thicknessCoords.y + 1.5 * thicknessCoords.height / 5) {
				thickness = 1;
			} else if (y > thicknessCoords.y + 1.5 * thicknessCoords.height / 5
					&& y < thicknessCoords.y + 2.5 * thicknessCoords.height / 5) {
				thickness = 2;
			} else if (y > thicknessCoords.y + 2.5 * thicknessCoords.height / 5
					&& y < thicknessCoords.y + 3.5 * thicknessCoords.height / 5) {
				thickness = 3;
			} else if (y > thicknessCoords.y + 3.5 * thicknessCoords.height / 5
					&& y < thicknessCoords.y + 4.5 * thicknessCoords.height / 5) {
				thickness = 4;
			}
			blackboard.setThickness(thickness);
		} else if (paletteCoords.contains(x, y)) {
			currColor = paletteToRGB((int) x, (int) y, saturation);
			currColorCoords.setLocation(x, y);
			blackboard.setColor(currColor);
		} else if (saturationCoords.contains(x, y)) {
			saturation = ((float) y - saturationCoords.y)
					/ saturationCoords.height;
			currColor = paletteToRGB(currColorCoords.x, currColorCoords.y,
					saturation);
			blackboard.setColor(currColor);
		}

		createGraphics();

		logger.exiting(ToolPanel.class.getName(), "mouseClick");
	}
}
