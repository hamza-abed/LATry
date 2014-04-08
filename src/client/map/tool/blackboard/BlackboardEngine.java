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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.input.PickUtils;
import client.map.tool.Tool;
import client.map.tool.ToolEngine;
import client.utils.ModelLoader;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
/*
import com.jme.bounding.OrientedBoundingBox;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.math.Matrix3f;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.Spatial.LightCombineMode;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
*/
/**
 * Moteur du tableau blanc.
 * 
 * @author Syscom
 */
public class BlackboardEngine extends ToolEngine {
	private static final Logger logger = Logger
			.getLogger(BlackboardEngine.class.getName());
	private static final int IMAGE_RESOLUTION_Y = 256;

	/** Les différents modes d'interaction avec le tableau */
	public static enum Modes {
		SELECT, PENCIL, LINE, RECTANGLE, OVAL, POLYGON, TEXT
	}

	private Node graphicNode;
	private Spatial board;
	/**
	 * Comme image, stocke toutes les figures. Est là pour faire la
	 * correspondance avec son id dans cet outil
	 */
	private HashMap<Integer, AbstractFigure> shapes = new HashMap<Integer, AbstractFigure>();
	/** Zone de dessin */
	private Quad plan;
	//private TextureState ts;
	//private CullState cs;
	/**
	 * Panneau latéral de sélection d'outil, de couleur, d'épaisseur du trait...
	 */
	private ToolPanel outils;
	/** Verrouille le panneau lors d'un déplacement de figure */
	private boolean lockPanel;
	/**
	 * L'image où l'on dessine. Sert de texture qui sera plaquée sur le plan du
	 * tableau blanc. Hérite de BufferedImage, elle stocke toutes les figures
	 * dessinées dessus.
	 */
	private BlackboardBufferedImage image;
	/**
	 * La forme actuellement sélectionnée. Le panneau latéral utilise cette
	 * variable pour modifier les attributs d'une forme. Doit être à null si on
	 * n'est pas en mode SELECT. Seule exception : lors de la création d'un
	 * nouveau texte, select == currShape pour permettre de modifier la couleur
	 * du texte en train d'être tapé depuis le panneau latéral.
	 */
	private AbstractFigure selected;
	/**
	 * La forme en train d'être dessinée. Permet de voir la figure se dessiner
	 * en temps réel, en modifiant ses attributs à chaque action à la souris.
	 * Doit passer à null une fois envoyée sur le serveur.
	 */
	private AbstractFigure currShape;
	/**
	 * Boite de dialogue pour le texte sélectionné ou en train d'être créé.
	 * Comprend le texte à afficher, sa taille, et son style (gras et italique)
	 */
	private TextDialog bbtd;
	private Point bbtdPosition;
	/** Couleur de dessin courante */
	private Color currColor;
	/** Mode de dessin actuel */
	private Modes currMode;
	/** Mode de remplissage de plygone actuel */
	private boolean fillPoly;
	/** Epaisseur du trait */
	private int thickness;
	/**
	 * Sert pour les déplacements des formes. Indique si on vient de cliquer ou
	 * pas.
	 */
	private boolean click;
	/**
	 * Permet de savoir si on n'a pas encore terminé le dessin du polygone
	 * courant, même si on a relaché le clic.
	 */
	private boolean currShapeIsNotFinished;
	/** Rapport largeur/hauteur du tableau */
	private float aspectRatio;

	public BlackboardEngine(Tool tool) {
		super(tool);
		thickness = 1;
		fillPoly = false;
		currMode = Modes.SELECT;
		currColor = Color.black;
		currShapeIsNotFinished = false;
	}

	/* ********************************************************** *
	 * * Gestion graphique * *
	 * **********************************************************
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.tool.ToolEngine#getGraphic()
	 */
	//@Override
	public Spatial getGraphic() {
		logger.entering(BlackboardEngine.class.getName(), "getGraphic");

		if (graphicNode == null) {
			rebuildGraphicNode();
		}

		logger.exiting(BlackboardEngine.class.getName(), "getGraphic");
		return graphicNode;
	}

	/**
	 * Initialise/reconstruit le noeud graphique
	 */
	public void rebuildGraphicNode() {
		logger.entering(BlackboardEngine.class.getName(), "rebuildGraphicNode");

		float w = getDataAsFloat("width", 12);
		float h = getDataAsFloat("height", 8);
		float s = getDataAsFloat("model-s", 5);
		float x = getDataAsFloat("model-x", 0);
		float y = getDataAsFloat("model-y", -2.1f);
		float z = getDataAsFloat("model-z", -0.5f);
		float r = getDataAsFloat("model-r", -1.57f);
		float rx = getDataAsFloat("model-rx", 0);
		float ry = getDataAsFloat("model-ry", 1);
		float rz = getDataAsFloat("model-rz", 0);
		String model = getData("model", "board.jmex");

		aspectRatio = w / h;

		if (graphicNode == null) {
			graphicNode = new Node("Blackboard - Node");

			// Plan clicable du tableau
			plan = new Quad( w, h);

			/*ts = DisplaySystem.getDisplaySystem().getRenderer()
					.createTextureState();
			cs = DisplaySystem.getDisplaySystem().getRenderer()
					.createCullState();
			cs.setEnabled(false);
			// Pour la transparence du tableau
			BlendState bs = DisplaySystem.getDisplaySystem().getRenderer()
					.createBlendState();
			bs.setBlendEnabled(true);

			plan.setRenderState(ts);
			plan.setRenderState(cs);
			plan.setRenderState(bs);
			plan.setDefaultColor(ColorRGBA.white);
			plan.setLightCombineMode(LightCombineMode.Off);
			graphicNode.attachChild(plan);

			// Panneau latéral de sélection d'outil de dessin, de couleur, ...
			outils = new ToolPanel("toolbox", 0.2f * h, 0.8f * h, this,
					currMode, currColor, fillPoly, thickness);
			outils.setRenderState(cs);
			outils.setRenderState(bs);
			outils.setLightCombineMode(LightCombineMode.Off);
			graphicNode.attachChild(outils);

			image = new BlackboardBufferedImage(
					(int) (aspectRatio * IMAGE_RESOLUTION_Y),
					IMAGE_RESOLUTION_Y, BlackboardBufferedImage.TYPE_INT_ARGB);
			image.setBackground(new Color(1f, 1f, 1f, 1f));

			// Création de l'image dans laquelle on dessine
			HashMap<Key, Object> hints = new HashMap<Key, Object>();
			hints.put(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			image.createGraphics().addRenderingHints(hints);
		}

		if (board == null || !board.getName().equals(model)) {
			if (board != null)
				board.removeFromParent();
			board = ModelLoader.get().load(model);
			board.setName(model);
			graphicNode.attachChild(board);
		}

		Matrix3f mr = new Matrix3f();
		mr.fromAngleNormalAxis(r, new Vector3f(rx, ry, rz));
		board.setLocalTranslation(x, y, z);
		board.setLocalRotation(mr);
		board.setLocalScale(s);

		plan.resize(w, h);
		plan.setLocalTranslation(0, 0.5f * h, 0);
		outils.resize(0.2f * h, 0.8f * h);
		outils.setLocalTranslation(-0.5f * w - 0.15f * h, 0.5f * h, 0);

		reloadAllShapes();

		image.repaint();
		TextureManager.clearCache();
		boolean compress = TextureManager.COMPRESS_BY_DEFAULT;
		TextureManager.COMPRESS_BY_DEFAULT = false;
		ts.setTexture(TextureManager.loadTexture(image,
				MinificationFilter.BilinearNoMipMaps,
				MagnificationFilter.Bilinear, true));
		TextureManager.COMPRESS_BY_DEFAULT = compress;

		graphicNode.setModelBound(new OrientedBoundingBox());
		graphicNode.updateModelBound();

		logger.exiting(BlackboardEngine.class.getName(), "rebuildGraphicNode");
                */
                }
	}

	/**
	 * Tient à jour la liste locale de figures. Pour ne pas surcharger le
	 * serveur, une figure est envoyée uniquement lorsque l'utilisateur a fini
	 * d'agir dessus. Il y a donc des décalages possibles entre la liste côté
	 * serveur et côté client. Ainsi, on évitera de supprimer la figure courante
	 * (currShape, celle qui est en train d'être dessinée, != null) si elle
	 * n'est pas sur le serveur.
	 */
	private void reloadAllShapes() {
		logger.entering(BlackboardEngine.class.getName(), "reloadAllShapes");

		Collection<String> keys = getAllKey();
		Pattern reg = Pattern.compile("^shape-(\\d+)$");
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (String key : keys) {
			Matcher m = reg.matcher(key);
			if (m.find()) {
				ids.add(Integer.parseInt(m.group(1)));
			}
		}

		if (ids.isEmpty()) {
			image.clear();
			shapes.clear();

			// On a une figure en train d'être dessinée qui doit continuer
			// d'apparaître à l'utilisateur
			if (currShape != null) {
				shapes.put(currShape.getId(), currShape);
				image.add(currShape);
			}
		} else {
			for (int id : ids)
				reloadShape(id);

			ArrayList<Integer> ite = new ArrayList<Integer>(shapes.keySet());
			for (int id : ite) {
				AbstractFigure s = shapes.get(id);
				if (!ids.contains(id) && s != currShape) {
					image.remove(s);
					shapes.remove(id);
				}
			}
		}

		logger.exiting(BlackboardEngine.class.getName(), "reloadAllShapes");
	}

	/**
	 * Recharge la figure d'un certain id.
	 * 
	 * @param id
	 */
	private void reloadShape(int id) {
		logger.entering(BlackboardEngine.class.getName(), "reloadShape");

		String shapeParams = getData("shape-" + id, null);
		if (shapeParams != null) {
			if (shapes.containsKey(id)) {
				// Si la figure existe déjà, on la met à jour pour éviter de
				// corrompre les autres références (currShape, selected...)
				shapes.get(id).update(shapeParams);
			} else {
				AbstractFigure newShape = null;

				// Instanciation d'une nouvelle figure à partir de la valeur de
				// la clé
				if (shapeParams.startsWith("path"))
					newShape = Chemin.newBBPath(id, shapeParams);
				else if (shapeParams.startsWith("line"))
					newShape = Ligne.newBBLine(id, shapeParams);
				else if (shapeParams.startsWith("rect"))
					newShape = Rect.newBBRectangle(id, shapeParams);
				else if (shapeParams.startsWith("oval"))
					newShape = Ellipse.newBBOval(id, shapeParams);
				else if (shapeParams.startsWith("polygon"))
					newShape = Polygone.newBBPolygon(id, shapeParams);
				else if (shapeParams.startsWith("text"))
					newShape = Texte.newBBText(id, shapeParams);

				shapes.put(id, newShape);
				image.add(newShape);
			}
		}

		logger.exiting(BlackboardEngine.class.getName(), "reloadShape");
	}

	/**
	 * Ne redessine que la zone de dessin
	 */
	public void repaint() {
		logger.entering(BlackboardEngine.class.getName(), "repaint");

		image.repaint();
		/*
                TextureManager.clearCache();
		boolean compress = TextureManager.COMPRESS_BY_DEFAULT;
		TextureManager.COMPRESS_BY_DEFAULT = false;
		ts.setTexture(TextureManager.loadTexture(image,
				MinificationFilter.BilinearNoMipMaps,
				MagnificationFilter.Bilinear, true));
		TextureManager.COMPRESS_BY_DEFAULT = compress;

		logger.exiting(BlackboardEngine.class.getName(), "repaint");
                */
	}

	/* ********************************************************** *
	 * * Gestion des événements * *
	 * **********************************************************
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.map.tool.ToolEngine#mouseClick(com.jme.math.Ray, int, boolean)
	 */
	//@Override
	public boolean onButton(int button, boolean pressed, Ray ray) {
		logger.entering(BlackboardEngine.class.getName(), "mouseClick");

		lockPanel = false;

		/*Vector2f mouseCoord = PickUtils.getQuadCoord(ray, outils);
		if (mouseCoord != null) {
			// Cliqué sur le panneau
			outils.mouseClick(mouseCoord);
			click = pressed;
		} else {
			mouseCoord = PickUtils.getQuadCoord(ray, plan);
			if (mouseCoord == null)
				return true;
			// Cliqué sur le tableau

			Point quadCoord = new Point(
					(int) (aspectRatio * IMAGE_RESOLUTION_Y * (mouseCoord.x + 0.5f)),
					(int) (IMAGE_RESOLUTION_Y * (1f - mouseCoord.y - 0.5f)));

			if (button == 0) {
				if (pressed) { // Dessin, sélection
					mouseAction(quadCoord);
					click = true;
					lockPanel = true;
					repaint();
				} else { // Relâchement bouton, on envoie la nouvelle figure sur
					// le serveur
					// Un polygone se dessine en plusieurs clics, d'où le
					// currShapeIsNotFinished...
					if (currShape != null && !currShapeIsNotFinished) {
						sendShape(currShape);
						currShape = null;
					}
					click = false;
				}
			}
		}

		// Envoi des modifications sur la figure sélectionnée
		if (!pressed && selected != null)
			sendShape(selected);

		logger.exiting(BlackboardEngine.class.getName(), "mouseClick");
                */
		return true;
	}

	/**
	 * Exécute l'action associée à l'outil courant, sur la coordonnée quadCoord
	 * 
	 * @param quadCoord
	 */
	private void mouseAction(Point quadCoord) {
		logger.entering(BlackboardEngine.class.getName(), "mouseAction");

		int id = newId();
		switch (currMode) {
		case SELECT:
			// On fait une nouvelle sélection.
			image.unselectAll();
			AbstractFigure prevSelected = selected;
			selected = image.select(quadCoord);
			outils.setDeletable(selected != null);

			// Fermeture de l'ancienne popup de texte, ouverture d'une autre si
			// besoin
			if (prevSelected != selected && selected instanceof Texte) {
				if (bbtd != null) {
					bbtd.dispose();
					while (bbtd.isAlive())
						;
				}
				bbtd = new TextDialog(this, (Texte) selected, bbtdPosition);
				bbtd.run();
			} else if (prevSelected != selected) {
				if (bbtd != null) {
					bbtd.dispose();
					while (bbtd.isAlive())
						;
					bbtd = null;
				}
			}
			break;

		case PENCIL:
			currShape = new Chemin(id, quadCoord, currColor, thickness);
			image.add(currShape);
			shapes.put(id, currShape);
			break;

		case LINE:
			currShape = new Ligne(id, quadCoord, quadCoord, currColor,
					thickness);
			image.add(currShape);
			shapes.put(id, currShape);
			break;

		case RECTANGLE:
			currShape = new Rect(id, quadCoord, quadCoord, currColor, fillPoly,
					thickness);
			image.add(currShape);
			shapes.put(id, currShape);
			break;

		case OVAL:
			currShape = new Ellipse(id, quadCoord, quadCoord, currColor,
					fillPoly, thickness);
			image.add(currShape);
			shapes.put(id, currShape);
			break;

		case POLYGON:
			if (currShapeIsNotFinished) {
				// On rajoute un point, ou bien si ce point est assez près du
				// premier, on ferme le polygone.
				Rectangle r = new Rectangle(((Polygone) currShape)
						.getFirstPoint().x - 4, ((Polygone) currShape)
						.getFirstPoint().y - 4, 9, 9);

				if (r.contains(quadCoord)) {
					((Polygone) currShape).setFinished(true);
					currShapeIsNotFinished = false;
				} else
					((Polygone) currShape).addPoint(quadCoord);
			} else {
				// On démarre un nouveau polygone
				currShape = new Polygone(id, quadCoord, quadCoord, currColor,
						fillPoly, thickness);
				image.add(currShape);
				shapes.put(id, currShape);
				currShapeIsNotFinished = true;
			}
			break;

		case TEXT:
			currShape = new Texte(id, quadCoord, currColor);
			image.add(currShape);
			shapes.put(id, currShape);
			selected = currShape; // Ca permet de modifier la couleur du texte à
			// partir du panneau latéral en même temps
			// qu'on le crée.
			currShapeIsNotFinished = true;

			if (bbtd != null) {
				bbtd.dispose();
				while (bbtd.isAlive())
					;
			}
			bbtd = new TextDialog(this, (Texte) currShape, bbtdPosition);
			bbtd.run();
			break;
		}

		logger.exiting(BlackboardEngine.class.getName(), "mouseAction");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.map.tool.ToolEngine#mouseMove(com.jme.math.Ray)
	 */
	//@Override
	public boolean onMove(Ray ray) {
		logger.entering(BlackboardEngine.class.getName(), "mouseMove");

		if (!click)
			return false;

		/*Vector2f mouseCoord = PickUtils.getQuadCoord(ray, outils);
		if (mouseCoord != null && !lockPanel) {
			outils.mouseClick(mouseCoord);
		} else {
			mouseCoord = PickUtils.getQuadCoord(ray, plan);
			if (mouseCoord == null)
				return false;

			Point quadCoord = new Point(
					(int) (aspectRatio * IMAGE_RESOLUTION_Y * (mouseCoord.x + 0.5f)),
					(int) (IMAGE_RESOLUTION_Y * (1f - mouseCoord.y - 0.5f)));

			switch (currMode) {
			case SELECT:
				if (selected != null)
					selected.setPosition(quadCoord);
				break;

			case PENCIL:
				((Chemin) currShape).addPoint(quadCoord);
				break;

			case POLYGON:
				if (currShapeIsNotFinished) // Le dernier point d'un polygone
					// fermé n'existe pas
					currShape.setLastPoint(quadCoord);
				break;

			case LINE:
			case RECTANGLE:
			case OVAL:
				if (currShape!=null)
					currShape.setLastPoint(quadCoord);
				break;
			}

			repaint();
		}

		logger.exiting(BlackboardEngine.class.getName(), "mouseMove");
                */
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.interfaces.graphic.GraphicMouseListener#onWheel(int,
	 * com.jme.math.Ray)
	 */
	//@Override
	public boolean onWheel(int wheelDelta, Ray ray) {
		logger.entering(this.getClass().getName(), "onWheel");
		logger.exiting(this.getClass().getName(), "onWheel");
		return false;
	}

	/* ********************************************************** *
	 * * MISE A JOUR * *
	 * **********************************************************
	 */

	/**
	 * Se charge d'envoyer une figure sur le serveur.
	 * 
	 * @param shape
	 */
	public void sendShape(AbstractFigure shape) {
		logger.entering(BlackboardEngine.class.getName(), "sendShape");

		int id = shape.getId();
		setData("shape-" + id, shape.toString());
		commitChangeOnServer();

		logger.exiting(BlackboardEngine.class.getName(), "sendShape");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.map.tool.ToolEngine#dataChanged(java.lang.String)
	 */
	@Override
	public void dataChanged(Collection<String> keys) {
		logger.entering(BlackboardEngine.class.getName(), "dataChanged");

		boolean keysContainsModel = false;
		for (String key : keys)
			if (key.startsWith("model"))
				keysContainsModel = true;

		if (keys.contains("width") || keys.contains("height")
				|| keysContainsModel) {
			rebuildGraphicNode();
		} else {
			repaint();
		}

		logger.exiting(BlackboardEngine.class.getName(), "dataChanged");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.map.tool.ToolEngine#dataDeleted(java.lang.String)
	 */
	@Override
	public void dataDeleted(Collection<String> keys) {
		logger.entering(BlackboardEngine.class.getName(), "dataDeleted");
		for (String key : keys) {
			logger.info("deleted Key : " + key);
			Pattern reg = Pattern.compile("^shape-(\\d+)$");
			Matcher m = reg.matcher(key);
			if (m.find()) {
				AbstractFigure f = shapes.remove(Integer.parseInt(m.group(1)));
				if (f != null)
					image.remove(f);
			}
		}
		dataChanged(keys);

		logger.exiting(BlackboardEngine.class.getName(), "dataDeleted");
	}

	/**
	 * Renvoie un id inutilisé
	 */
	public int newId() {
		logger.entering(BlackboardEngine.class.getName(), "newId");

		Collection<String> keys = getAllKey();
		Pattern reg = Pattern.compile("^shape-(\\d+)$");
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (String key : keys) {
			Matcher m = reg.matcher(key);
			if (m.find()) {
				ids.add(Integer.parseInt(m.group(1)));
			}
		}

		int newId = 1;
		for (int i : ids)
			if (newId <= i)
				newId = i + 1;

		logger.exiting(BlackboardEngine.class.getName(), "newId");
		return newId;
	}

	/**
	 * Supprime la figure sélectionnée.
	 */
	public void delete() {
		logger.entering(BlackboardEngine.class.getName(), "delete");

		if (selected != null) {
			if (bbtd != null) {
				bbtd.kill();
				while (bbtd.isAlive())
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						logger.warning("InterruptedException : Je le savais !");
					}
				bbtd = null;
			}
			deleteData("shape-" + selected.getId());
			unselect();
			reloadAllShapes();
			commitChangeOnServer();
		}

		logger.exiting(BlackboardEngine.class.getName(), "delete");
	}

	public void saveBbtdPosition(Point pos) {
		bbtdPosition = pos;
	}

	/**
	 * Déselectionne la figure courante.
	 */
	private void unselect() {
		logger.entering(BlackboardEngine.class.getName(), "unselect");

		if (currShape != null && !currShapeIsNotFinished) {
			sendShape(currShape);
			currShape = null;
		}
		selected = null;
		outils.setDeletable(false);
		if (bbtd != null) {
			bbtd.dispose();
			while (bbtd.isAlive())
				;
			bbtd = null;
		}
		image.unselectAll();

		logger.exiting(BlackboardEngine.class.getName(), "unselect");
	}

	/**
	 * Change l'outil de dessin courant.
	 * 
	 * @param mode
	 */
	public void setDrawMode(Modes mode) {
		logger.entering(BlackboardEngine.class.getName(), "setDrawMode");

		currMode = mode;
		unselect();
		currShapeIsNotFinished = false;
		rebuildGraphicNode();

		logger.exiting(BlackboardEngine.class.getName(), "setDrawMode");
	}

	/**
	 * Change la façon de dessiner les polygones ou les ellipses (contours ou
	 * pleins)
	 * 
	 * @param fillPoly
	 */
	public void setDrawMode(boolean fillPoly) {
		logger.entering(BlackboardEngine.class.getName(), "setDrawMode");

		this.fillPoly = fillPoly;
		if (selected != null) {
			selected.setFillPoly(fillPoly);
			repaint();
		}

		logger.exiting(BlackboardEngine.class.getName(), "setDrawMode");
	}

	/**
	 * Change l'épaisseur du trait de dessin.
	 * 
	 * @param thickness
	 *            L'épaisseur du trait en pixels
	 */
	public void setThickness(int thickness) {
		logger.entering(BlackboardEngine.class.getName(), "setThickness");

		this.thickness = thickness;
		if (selected != null) {
			selected.setThickness(thickness);
			repaint();
		}

		logger.exiting(BlackboardEngine.class.getName(), "setThickness");
	}

	/**
	 * Change la couleur de dessin courante.
	 * 
	 * @param c
	 */
	public void setColor(Color c) {
		logger.entering(BlackboardEngine.class.getName(), "setColor");

		currColor = c;
		if (selected != null) {
			selected.setColor(currColor);
			repaint();
		}

		logger.exiting(BlackboardEngine.class.getName(), "setColor");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see client.tool.ToolEngine#getKeysDescriptions()
	 */
	@Override
	public Collection<String> getKeysDescriptions() {
		return new ArrayList<String>();
	}
}
