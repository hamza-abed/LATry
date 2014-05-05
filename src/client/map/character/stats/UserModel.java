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
package client.map.character.stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import shared.enums.LaComponent;
import syscom.xsd.usermodel.CategoryType;
import syscom.xsd.usermodel.DataType;
import syscom.xsd.usermodel.UserType;
import client.map.character.PlayableCharacter;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import shared.variables.Variables;

/**
 * Représente le usermodel du joueur
 * <ul>
 * <li>le UserModel s'execute au sein d'une tâche</li>
 * <li></li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class UserModel implements Runnable {
	private static final Logger logger = Logger.getLogger("UserModel");
	private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
	private PlayableCharacter player;
	private String server;
	private Client client;
	private ScheduledFuture<?> task;
	private HashMap<String, HashMap<String, DataType>> list;
	private String prefCatName;
	private String groupCatName;

	/**
	 * @param playableCharacter
	 */
	public UserModel(PlayableCharacter playableCharacter) {
		
            this.player = playableCharacter;
		this.server = Variables.getProps().getProperty("la.usermodel.server", "http://google.fr");
		int refresh = Integer.parseInt(Variables.getProps().getProperty("la.usermodel.refreshtime","30"));
		//this.prefCatName = player.getWorld().getGame().getHud().getLocalText("la.usermodel.preference.cat", "préférence");
		//this.groupCatName = player.getWorld().getGame().getHud().getLocalText("la.usermodel.group.cat", "group");
		this.client = new Client();
		this.task = executor.scheduleAtFixedRate(this, 0, player.isPlayer()?refresh:refresh*10, TimeUnit.SECONDS);
                
		
		// FIXME close l'executor à la sortie
	}


	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		logger.info("start UM Task "+server+"user/"+player.getLogin());
		try {
			
			logger.info("UM serveur "+player);
			ClientResponse r = client.
			resource(server+"user/"+player.getLogin()).
			accept(MediaType.APPLICATION_XML).
			get(ClientResponse.class);
			//System.out.println(r.getEntity(String.class));
			UserType u = r.getEntity(UserType.class);

			HashMap<String, HashMap<String, DataType>> list = new HashMap<String, HashMap<String, DataType>>();
			for (CategoryType cat : u.getCategory()) {
				HashMap<String, DataType> datas = new HashMap<String, DataType>();
				for (DataType data : cat.getData()) 
					datas.put(data.getName(), data);
				list.put(cat.getName(), datas);				
			}
			
			applyNewList(list);
		}catch (Exception e) {
			logger.warning("le Server UserModel marche pas j'arrete la tache pour : "+player+"[erreur= "+e.getLocalizedMessage()+"]");
			task.cancel(false);
		}
		logger.fine("end UM Task "+player);
	}

	/**
	 * Application des changements
	 * @param list
	 */
	private void applyNewList(HashMap<String, HashMap<String, DataType>> list) {
		if (!player.isPlayer()) {
			this.list = list;
			return;
		}
		
		// TODO ajouter un test de cheangement dans les data.
				
		// application des nouveau Group
		HashMap<String, DataType> actuals = this.list==null?null:this.list.get(groupCatName);
		HashMap<String, DataType> news = list.get(groupCatName);
		if (news!=null) 
			for (String gr : news.keySet()) {
				if (actuals==null || actuals.get(gr)==null) {
					if (!gr.startsWith(LaComponent.group.prefix()))
						gr=LaComponent.group.prefix()+gr;
					player.getWorld().getGroupBuildIfAbsent(gr).addPlayer(player);
				}
			}
		
		if (actuals!=null) 
			for (String gr : actuals.keySet())
				if (news==null || !news.containsKey(gr)) {
					if (!gr.startsWith(LaComponent.group.prefix()))
						gr=LaComponent.group.prefix()+gr;
					player.getWorld().getGroupBuildIfAbsent(gr).delPlayer(player);
				}
				
		this.list = list;
		refreshHud();
	}

	/**
	 * Met à jour le hud.
	 */
	private void refreshHud() {
		//player.getWorld().getGame().getHud().getSkillBook().refresh();
	}


	/**
	 * Renvoie la liste des categorie du user
	 * @return
	 */
	public Collection<String> listCategories() {
		ArrayList<String> out = new ArrayList<String>();
		if (list!=null)  
			out.addAll(list.keySet());
		return out;
	}


	/**
	 * liste les competence d'une categorie.
	 * @param cat
	 * @return
	 */
	public Collection<String> listAttributes(String cat) {
		ArrayList<String> out = new ArrayList<String>();
		if (list!=null && list.containsKey(cat)) 
			out.addAll(list.get(cat).keySet());
		return out;
	}


	/**
	 * Renvoie la valeur d'un attribut du UserModel
	 * @param cat
	 * @param attr
	 * @return 
	 */
	public String getValue(String cat, String attr) {
		if (list!=null && list.containsKey(cat))
			return list.get(cat).get(attr).getValue();
		return null;
	}

	/**
	 * Renvoie la valeur d'un attribut du UserModel en valeur numeral
	 * @param cat
	 * @param attr
	 * @return
	 */
	public float getValueAsFloat(String cat, String attr) {
		try {
			if (list!=null && list.containsKey(cat))
				return Float.parseFloat(list.get(cat).get(attr).getValue());
		} catch (Exception e) {}
		return 0f;
	}

	/**
	 * Change la valeur d'un attribut du UM
	 * @param cat
	 * @param attr
	 * @return 
	 */
	public void setValue(String cat, String attr, String value,String certitude) {
		ClientResponse r = client.resource(server+"umupdate").
		queryParam("user", player.getLogin()).queryParam("category",cat).
		queryParam("attribute", attr).queryParam("value",value).
		queryParam("certitude", certitude).post(ClientResponse.class);
		logger.info("Reponse du UM "+r.getStatus());
		/*if (list!=null && list.containsKey(cat))
			return list.get(cat).get(attr).getValue();
		return null;//*/
	}


	/**
	 * Envoie les préférence de l'utisateur sur son apperence
	 */
	public void sendCharacterPreference() {
	/*	
            player.getWorld().getGame().getTaskExecutor().execute(new Runnable() {
			@Override
			public void run() {
				setValue(prefCatName, "model", player.getModelType().toString(),"1");
				setValue(prefCatName, "skin-color", player.getSkin().toString(),"1");
				setValue(prefCatName, "skin-ambient", Float.toString(player.getSkinAmbient()),"1");
				setValue(prefCatName, "cloth-top-color", player.getTopCloth().toString(),"1");
				setValue(prefCatName, "cloth-top-ambient", Float.toString(player.getTopClothAmbient()),"1");
				setValue(prefCatName, "cloth-bottom-color", player.getBottomCloth().toString(),"1");
				setValue(prefCatName, "cloth-bottom-ambient", Float.toString(player.getBottomClothAmbient()),"1");
				setValue(prefCatName, "cloth-shoes-color", player.getShoes().toString(),"1");
				setValue(prefCatName, "cloth-shoes-ambient", Float.toString(player.getShoesAmbient()),"1");
				setValue(prefCatName, "hair-cut", Integer.toString(player.getHairCut()),"1");
				setValue(prefCatName, "hair-color", player.getHair().toString(),"1");
				setValue(prefCatName, "hair-ambient", Float.toString(player.getHairAmbient()),"1");
			}
		});
                */
	}

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
