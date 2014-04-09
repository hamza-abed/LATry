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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Logger;

import client.HttpResourceLocator;
import client.LaGame;
import client.script.ScriptableMethod;
import com.jme3.animation.AudioTrack;

import com.jme3.renderer.Camera;
import shared.variables.Variables;
//import com.jme.system.DisplaySystem;
//import com.jme.util.resource.ResourceLocatorTool;

//import com.jmex.audio.AudioSystem;
//import com.jmex.audio.AudioTrack;

/**
 * Permet l'execution de son 
 * <ul>
 * <li></li>
 * <li></li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class SoundSystem {
	private static final Logger logger = Logger.getLogger("Sound");

	private boolean enable;

	private LaGame game;

	private HashMap<String, AudioTrack> sounds = new HashMap<String, AudioTrack>();

	private HashMap<AudioTrack, SoundControler> trackers = new HashMap<AudioTrack, SoundControler>();

	private String background = null;

	/**
	 * @param laGame
	 */
	public SoundSystem(LaGame game) {
            
		enable = "true".equalsIgnoreCase(Variables.getClientConnecteur().getProps().getProperty("la.sound.enable","false"));
		this.game = game;
	}

	/**
	 * Lance un son lu une seul fois
	 * @param url
	 */
	@ScriptableMethod(description="Lit un fichier musicale.\n" +
			"le premier paramere est le nom du fichier ou une URL.\n" +
			"Le second parametre indique si il faut le lire en bouble ou non\n " +
	"le troisieme est le volume du son entre 0 et 1")
	public void play(final String file, final boolean looping, final float volume) {
		if (!enable) return; 
		//game.
                Variables.getClientConnecteur().getTaskExecutor().execute(new Runnable() {
			@Override
			public void run() {
				AudioTrack sound = getSound(file);
				if (sound == null) return;
                                
				/*sound.stop();
				sound.setLooping(looping);
				sound.play();
				sound.setVolume(volume);
				sound.setMinVolume(volume);
				sound.setMaxVolume(volume); */
				getSoundControler(sound).setEnable(false);
			}
		});
	}

	@ScriptableMethod
	public void playLocal(final String file,final boolean looping, final float x, final float y,final float z,
			final float minVolume, final float maxVolume, final float maxDistance) {
		if (!enable) return; 	
		//game.
                Variables.getClientConnecteur().getTaskExecutor().execute(new Runnable() {
			@Override
			public void run() {
				AudioTrack sound = getSound(file);
				if (sound == null) return;
                                /*
				sound.stop();
				sound.setLooping(looping);
				sound.setMinVolume(minVolume);
				sound.setVolume(minVolume);
				sound.setMaxVolume(maxVolume);
				sound.setWorldPosition(x, y, z);
				sound.setMaxAudibleDistance(maxDistance);
				getSoundControler(sound).setEnable(true);

				sound.play(); */
			}


		});
	}


	/**
	 * Lance un son lu une seul fois
	 * @param url
	 */
	@ScriptableMethod(description="lit un fichier audio une seul fois.\nLe parametre est le nom du fichier local")
	public void play(String file) {
		play(file,false,1);
	}

	@ScriptableMethod(description="lit un fichier audio une seul fois.\nLe parametre est le nom du fichier local, Le second parametre indique si il faut lire en boucle")
	public void play(String file,boolean looping) {
		play(file,looping,1);
	}


	/**
	 * Arrete tous les son courant mais pas la musique de background
	 * 
	 */
	@ScriptableMethod(description="Coupe toute les musiques courante mais pas le background")
	public void stopAll() {
		if (!enable) return; 
		/*for (AudioTrack track:sounds.values()) 
			track.stop(); */

	}

	/**
	 * Coupe le son correspondantr à cette URL
	 * @param url
	 */
	@ScriptableMethod(description="Arrete la lecture ed la musique correspondant à cette URL ou fichier")
	public void stop(String url) {
		if (!enable) return; 
		AudioTrack s = sounds.get(url);
		//if (s!=null) s.stop();
	}

	@ScriptableMethod(description="Change la musique de fond,\narrete donc la precedente si il y en as une,\nle volume est entre 0 et 1")
	public void background(String url, float volume) {
		if (background!=null && !background.equalsIgnoreCase(url)) stop(background);
		background = url;
		play(url, true, volume);
	}

	/**
	 * arrete le gestionnaire de son
	 */
	public void close() {
		if (enable) try {
		//	AudioSystem.getSystem().cleanup();
		} catch (Exception e) {
			logger.warning(e.getClass().getName()+ " lors de l'arrete du systeme Audio");
		}
	}

	private SoundControler getSoundControler(AudioTrack sound) {
		if (!trackers.containsKey(sound))
			trackers.put(sound, new SoundControler(sound));
		return trackers.get(sound);
	}

	/**
	 * renvoie un fichier de son
	 * @return
	 */
	protected AudioTrack getSound(String file) {
		if (sounds.containsKey(file)) 
			return sounds.get(file);
		/*try {
			URL u = ResourceLocatorTool.locateResource(ResourceLocatorTool.TYPE_AUDIO, file);
			if (u == null)
				u = ResourceLocatorTool.locateResource(HttpResourceLocator.RESOURCE, file);
			if (u==null)  {
				u=new URL(file);
				u.openStream().close();
			}
			AudioTrack sound = AudioSystem.getSystem().createAudioTrack(u, false);
			sounds.put(file, sound);
			return sound;
		} catch (MalformedURLException e) {
			logger.warning("MalformedURLException : Je le savais !");
		} catch (IOException e) {
			logger.warning(file +" n'existe pas");
		} catch (Exception e) {
			logger.warning(e.getClass().getName()+" lors de la lecture de "+file);
		}*/
		return null;
	}

	/**
	 * definit le point de vue
	 * @param camera
	 */
	public void setCamera(Camera camera) {
	/*	if (enable) {
			Camera cam = DisplaySystem.getDisplaySystem().getRenderer().getCamera();
			AudioSystem.getSystem().getEar().trackOrientation(cam);
			AudioSystem.getSystem().getEar().trackPosition(cam);
		}*/
	}

	/**
	 * Evolution des son
	 * @param interpolation
	 */
	public void update(float interpolation) {
		if (enable) {
			/*AudioSystem.getSystem().update();
			for (SoundControler controler : trackers.values())
				controler.update(interpolation); */

		}
	}


	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
