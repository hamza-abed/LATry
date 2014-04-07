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
package client.script;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import shared.constants.ScriptConstants;
import shared.enums.LaComponent;
import shared.pck.Pck;
import client.map.World;

/**
 * Moteur d'execution de script
 * <ul>
 * <li>Permet de ne pas executer 2 fois le même script pour ne pas surtchager la
 * machine</li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2010-2013
 */
public class ScriptExecutor extends SimpleScriptContext {
	private static final Logger logger = Logger.getLogger("ScriptExecutor");

	private TreeSet<String> queue = new TreeSet<String>();

	private World world;

	private ScriptEngine engine;

	private String sharedRegex;

	private ExecutorService executor;

	/**
	 * @param world
	 */
	public ScriptExecutor(World world) {
		this.world = world;
		this.executor = Executors.newFixedThreadPool(2);
	}
	
	/**
	 * @param message
	 */
	public void receivedExecuteScript(ByteBuffer message) {
		String script = Pck.readString(message);
		HashMap<String, Object> params = new HashMap<String, Object>();
		int count = message.getInt();
		while (count-->0) 
			params.put(Pck.readString(message), Pck.readString(message));
		executeWithParams(script, params);
	}

	
	/**
	 * met le script en execution
	 * 
	 * @param action
	 */
	public void execute(String scriptKey) {
		executeWithParams(scriptKey, null);
	}

	/**
	 * met le script en execution
	 * 
	 * @param action
	 */
	public void execute(String scriptKey, Object source) {
		HashMap<String, Object> p = new HashMap<String, Object>();
		p.put("source", source);
		executeWithParams(scriptKey, p);
	}

	/**
	 * un script sous forme de boolean et attend la réponse
	 * @param scriptKey
	 */
	public boolean executeBoolean(final String scriptKey) {
		// suppression des cas simpliste
		if (scriptKey == null) return false;
		if (scriptKey.equalsIgnoreCase(ScriptConstants.TRUE_SCRIPT)) return true;
		if (scriptKey.equalsIgnoreCase(ScriptConstants.FALSE_SCRIPT)) return false;
		if (!scriptKey.matches(LaComponent.script.regex())) return false;
		
		try {
			return executor.submit(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					Script script = world.getScriptBuildIfAbsent(scriptKey);
					
					if (!world.isUpdate(script))
						world.getGame().updateFromServerAndWait(script);
					return Boolean.TRUE.equals(executeOnLocal(script, null));
				}
			}).get();
		} catch (InterruptedException e) {
			logger.warning("InterruptedException : Je le savais !");
		} catch (ExecutionException e) {
			e.printStackTrace();
			logger.warning("ExecutionException : Je le savais !");
		}
		return false;
	}

	
	
	/**
	 * Execute un script avec des parametre en plus
	 * 
	 * @param string
	 * @param params
	 */
	public void executeWithParams(String scriptKey, HashMap<String, Object> params) {
		if (scriptKey == null) {
			logger.info("WTF execution de script null");
			return;
		}

		if (scriptKey.equalsIgnoreCase(ScriptConstants.VOID_SCRIPT))
			return;
		if (scriptKey.matches(LaComponent.dialog.regex())) {
			logger.info("ce script est un dialog, on execute le dialog");
			//world.getPlayer().displayDialog(scriptKey);
			return;
		}
// pas opérationnel en v31
//		if (scriptKey.matches(LaComponent.lgf.regex())) {
//			logger.info("ce script est une Ressource LGF, on l'execute");
//			world.getLgfBuildIfAbsent(scriptKey).execute();
//			return;
//		}

		if (scriptKey.matches(LaComponent.slides.regex())) {
			logger.info("ce script est un slideshow, on l'affiche");
			//world.getSlideShowBuildIfAbsent(scriptKey).open();
			return;
		}
		
		if (!scriptKey.matches(LaComponent.script.regex())) {
			logger.warning("Ce n'est pas une clef valide pour être executé");
			return;
		}
			
			

		if (queue.contains(scriptKey)) {
			logger.info(scriptKey + " deja en execution > skip");
			return;
		}
		queue.add(scriptKey);
		Script script = world.getScriptBuildIfAbsent(scriptKey);
		world.getGame().getTaskExecutor().execute(
				new ScriptTask(script, params));
	}

	/**
	 * Execute le script en local sur le client
	 * 
	 * @param script
	 * @param params
	 * @return
	 */
	protected Object executeOnLocal(Script script,
			HashMap<String, Object> params) {
		logger.entering("ScriptExecutor", "executeOnLocal");
		logger.fine("execution local du script : " + script);
		world.getGame().getChatSystem().debug(script.getKey() + " execute");
		this.queue.remove(script.getKey());
		try {
			ScriptContext context = getEngine().getContext();

			if (params != null)
				for (Entry<String, Object> sentry : params.entrySet())
					context.setAttribute(sentry.getKey(), sentry.getValue(),
							ENGINE_SCOPE);

			Object obj = getEngine().eval(script.getScript());

			if (params != null)
				for (String p : params.keySet())
					context.removeAttribute(p, ENGINE_SCOPE);
			return obj;
		} catch (ScriptException e) {
			logger.warning("erreur dans l'execution du script");
			e.printStackTrace();
		}
		logger.exiting("ScriptExecutor", "executeOnLocal");
		return null;
	}

	/**
	 * Getter de engine
	 * 
	 * @return the engine
	 */
	private ScriptEngine getEngine() {
		if (engine == null) {
			engine = new ScriptEngineManager().getEngineByName(ScriptConstants.SCRIPT_LANGUAGE);
			engine.setContext(this);
		}
		return engine;
	}

	/* ********************************************************** *
	 * * 				IMPLEMENTATION DU CONTEXT 				* *
	 * ********************************************************** */
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.script.SimpleScriptContext#getAttribute(java.lang.String, int)
	 */
	@Override
	public Object getAttribute(String name, int scope) {

		if (name.equals(ScriptConstants.PLAYER_KEYWORD))
			return world.getPlayer();
		//if (name.equals(ScriptConstants.HUD_KEYWORD))
		//	return world.getGame().getHud();
		//if (name.equals(ScriptConstants.SOUND_KEYWORD))
		//	return world.getGame().getSoundSystem();
		if (name.equals(ScriptConstants.WORLD_KEYWORD))
			return world;

		
		for (Pattern p : ScriptConstants.PATTERNS) {
			Matcher m = p.matcher(name);
			if (m.find())
				return world.getSharable(m.group(1) + ":" + m.group(2));
		}

		//logger.warning(name	+ " n'est pas connu de cet envirronement de script.");

		return super.getAttribute(name, scope);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.script.SimpleScriptContext#getAttributesScope(java.lang.String)
	 */
	@Override
	public int getAttributesScope(String name) {
		logger.entering(this.getClass().getName(), "getAttributesScope");

		if (name.equals(ScriptConstants.PLAYER_KEYWORD) || 
				name.equals(ScriptConstants.HUD_KEYWORD) ||
				name.equals(ScriptConstants.SOUND_KEYWORD) ||
				name.equals(ScriptConstants.WORLD_KEYWORD) ||
				name.matches(getSharedRegex()))
			return SimpleScriptContext.ENGINE_SCOPE;

		return super.getAttributesScope(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.script.SimpleScriptContext#getAttribute(java.lang.String)
	 */
	@Override
	public Object getAttribute(String name) {
		logger.entering(this.getClass().getName(), "getAttribute");

		logger.warning("Appel a getAttribute(" + name + ")");

		logger.exiting(this.getClass().getName(), "getAttribute");
		return super.getAttribute(name);
	}

	/* ********************************************************** *
	 * * 				tache d'execution du script 			* *
	 * ********************************************************** */
	/**
	 * 
	 */
	private class ScriptTask implements Runnable {
		private Script script;
		private LinkedList<String> keys;
		private HashMap<String, Object> params;

		/**
		 * @param script
		 * @param params
		 */
		public ScriptTask(Script script, HashMap<String, Object> params) {
			this.script = script;
			this.keys = new LinkedList<String>(script.getSharedKeys());
			this.params = params;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			// test si e script est incconnu
			if (!world.isUpdate(script)) {
				logger.info("requete de " + script.getKey()	+ " sur le serveur");
				world.getGame().updateFromServerAndWait(script, this);
				return ;
			}

			if (keys.isEmpty())
				this.keys = new LinkedList<String>(script.getSharedKeys());
			
			// verifie que l'enssemble des objet est a jour
			String key = keys.poll();
			while (key != null && world.isUpdate(key))
				key = keys.poll();
			if (key != null) {
				logger.info("requete de " + key + " sur le serveur");
				world.getGame().updateFromServerAndWait(key, this);
				return ;
			}
			executeOnLocal(script, params);
			
			return ;
		}


		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return ScriptTask.class.getName() + "@" + script.getKey();
		}

	}
	/* ********************************************************** *
	 * * 			Construction des experssion reguliere 		* *
	 * ********************************************************** */

	/**
	 * Expression reguliere de l'enssemble des des objet paratgé
	 * @return
	 */
	private String getSharedRegex() {
		if (sharedRegex == null) {
			sharedRegex = "^";
			for (String reg : ScriptConstants.REGEXS)
				sharedRegex+="("+reg+")|";
			sharedRegex = sharedRegex.replaceAll("\\|$", "")+"$";
		}
		return sharedRegex;
	}

	/**
	 * Arrete l'executeur si y en a un lancer
	 */
	public void close() {
		if (executor!=null) executor.shutdownNow();
	}

	

}
