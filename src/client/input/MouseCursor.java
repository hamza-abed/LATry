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
package client.input;

import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import org.w3c.dom.Element;

import shared.utils.xml.XMLUtils;

import client.utils.FileLoader;
import com.jme3.texture.Image;
import com.jme3.texture.plugins.AWTLoader;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
/*
import com.jme.image.Image;
import com.jme.input.MouseInput;
import com.jme.util.TextureManager;
*/
/**
 * Gestion du curseur de la souris 
 * <ul>
 * <li>modèles disponibles : "ecliz","iron","mario","steampunk"</li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class MouseCursor {
	private static final Logger logger = Logger.getLogger("MouseCursor");

	private static MouseCursor instance = null;

	/**
	 * type de base sur lequel va s'appliquer le curseur
	 * @author philippe
	 *
	 */
	public enum CursorType {
		base,npc,object, move;
	}
	
	private static final String[] names = {
		"ecliz","iron","mario","steampunk"
	};

	/**
	 * Ensemble des modèles du curseur disponibles sous forme de HashMapHashMap<String, CursorModel>
	 */
	private HashMap<String, CursorModel> models = new HashMap<String, CursorModel>();

	/**
	 * modele du curseur courant de type CursorModel
	 */
	private CursorModel current;

	private String defaultModel;

	/**
	 * Singleton
	 * @return
	 */
	public static MouseCursor get() {
		if (instance==null)
			instance = new MouseCursor();
		return instance;
	}

	/**
	 * 
	 */
	private MouseCursor() {}


	public void initialize(Properties props) {
		try {
			// en attendant que je trouve mieux
			for (String n : names)	loadModel(n);
			defaultModel = props.getProperty("la.input.mouse.cursor","iron");
			changeModel(defaultModel);
		} catch (Exception e) {
			logger.warning(e.getClass().getName()+" : lors du chargement des curseurs ");
		}
	}

	/**
	 * charge le pattern contenu dans le dossier 
	 * @param name
	 */
	private void loadModel(String name) {
		try {
			// charge le file info contenant les modèles
			Element e_root = XMLUtils.parseStream(
					FileLoader.getResourceAsStream(
							"data/cursor/"+name+"/info.xml")).getDocumentElement();
			
			// creation d'une classe de modele
			CursorModel m = new CursorModel();
			
			//chargement de la classe à partir de info.xml
			for (CursorType ct : CursorType.values() ) {
				Element e = XMLUtils.getChildElement(e_root, ct.name());
				if (e==null) continue;
				m.xs.put(ct, Integer.parseInt(e.getAttribute("x")));
				m.ys.put(ct, Integer.parseInt(e.getAttribute("y")));


				if (e.hasAttribute("delay")) {
					int delay = Integer.parseInt(e.getAttribute("delay"));
					String[] files = e.getTextContent().split(";");
					Image[] images = new Image[files.length];
					int[] delays  = new int[files.length];
					for (int i=0;i<files.length;i++) {
                                            
                                            try {
    URL url = new URL("examples/strawberry.jpg");
    BufferedImage img = ImageIO.read(url);
 
    AWTLoader awtL = new AWTLoader();
    Image imgJME = awtL.load(img, false);
 images[i] =imgJME;
 delays[i] = delay;
    // own cache.add(imgTex)
} catch (IOException ex) {
    System.out.println("err");
}
						
                                                        //TextureManager.loadImage(FileLoader.getResourceAsUrl("data/cursor/"+name+"/"+files[i]), true);
						
					}
					m.images.put(ct, images);
					m.delays.put(ct, delays);
					m.urls.put(ct,FileLoader.getResourceAsUrl("data/cursor/"+name+"/"+files[0]));
				} else 
					m.urls.put(ct,FileLoader.getResourceAsUrl("data/cursor/"+name+"/"+e.getTextContent()));

			}
			models.put(name, m);
			
		} catch (Exception e) {
			logger.warning(e.getClass().getName()+" : lors du chargement des curseurs "+name);
		} 
	}

	/* ********************************************************** *
	 * *				Changement de model / curseur			* *
	 * ********************************************************** */

	/**
	 * change sur le model dont le nom est passé en parametre
	 * @param name = nom du modèle (iron, mario, steampunk, ...) 
	 */
	public void changeModel(String name) {
		
		// si le nom du modèle est null on prend le nom définit par défault
		if (name == null) name = defaultModel;
		
		//  definit le modele courrant en le cherchant de puis tous les modeles
		current = models.get(name);
		
		if (current == null)
			current = models.get(defaultModel);
		
		// applique le curseur du modele
		switchCursor(CursorType.base);
	}
	/**
	 * Applique le curseur  du modele de curseur courrant (current) en fonction du type
	 * @param ct (CursorType) type d'objet sur lequel s'applique le curseur
	 */
	public void switchCursor(CursorType ct) {
		if (current==null) return;
		if (current.urls.get(ct)==null){
			if (ct ==CursorType.base) return;
			switchCursor(CursorType.base);
		} else {
                    System.out.println("MouseCursor -> switchCursor(ct) : manquante !!");
			/*if (current.images.get(ct) == null)
				MouseInput.get().setHardwareCursor(current.urls.get(ct), current.xs.get(ct), current.ys.get(ct));
			else 
				MouseInput.get().setHardwareCursor(current.urls.get(ct), 
						current.images.get(ct), current.delays.get(ct), 
						current.xs.get(ct), current.ys.get(ct));				
                                                */
		}

	}



	/* ********************************************************** *
	 * *					InnerClasses						* *
	 * ********************************************************** */
	
	/**
	 * Contient les modèles sous forme de HashMap.
	 * un modele est constitué :
	 * <ul>
	 *  <li>d'une URL
	 *  <li>xs, ys
	 *  <li>delays
	 *  <li>tableau d'image
	 * </ul>
	 * @author philippe
	 *
	 */
	private class CursorModel {
		HashMap<CursorType, URL> urls = new HashMap<CursorType, URL>();
		HashMap<CursorType, Integer> xs = new HashMap<CursorType, Integer>();
		HashMap<CursorType, Integer> ys = new HashMap<CursorType, Integer>();
		HashMap<CursorType, int[]> delays = new HashMap<CursorType, int[]>();
		HashMap<CursorType, Image[]> images = new HashMap<CursorType, Image[]>();
	}

}
