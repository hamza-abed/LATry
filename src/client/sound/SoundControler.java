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
package client.sound;


import com.jme3.animation.AudioTrack;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
//import com.jme3.system.DisplaySystem;
//import com.jmex3.audio.AudioTrack;

/**
 * Controle le volume du son en fonction de la distance. 
 * <ul>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class SoundControler {
	private AudioTrack sound;
	private Camera camera;
	private boolean enable;

	
	/**
	 * @param sound
	 */
	public SoundControler(AudioTrack sound) {
		this.sound = sound;
	}


	/**
	 * Met a jour le volume du son en fonction de la distance de camera
	 * @param interpolation
	 */
        
        
        /*
         * On aura pas besoin de cette fonction
         * puisque la cam suit toujours le joueur 
         * et le son en fait c'est le son du jeux,
         * peut être qu'on remplacera la distance par rapport 
         * à la caméra par la distance par rapport au joueur.
         *
         */
        /*
	public void update(float interpolation) {
		if (!enable) return;
		if (camera == null) 
			camera = DisplaySystem.getDisplaySystem().getRenderer().getCamera();
		Vector3f loc = camera.getLocation();
		
		float v = (sound.getMaxVolume()-sound.getMinVolume());
		float d = sound.getWorldPosition().distance(loc);
		if (d==0) v=sound.getMaxVolume();
		else v=v/d + sound.getMinVolume();
		sound.setVolume(v);
	}
*/
	/**
	 * active ou descative le controler
	 * @param b
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	
	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
