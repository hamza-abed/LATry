/*
 * NOT USED YET
 */
package client.interfaces.graphic;

import com.jme3.scene.Spatial;

/**
 * Un objet affichable
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public interface Graphic {
   
	/**
	 * Doit renvoyé le noeud qui permet d'affiché l'objet
	 * 
	 * @return
	 */
	public Spatial getGraphic();

	/**
	 * Doit lancé une tache qui ajout l'objet a l'affichage
	 */
	public void addToRenderTask();

	/**
	 * Doit lancé une tache qui enleve l'objet
	 */
	public void removeFromRenderTask();

}
