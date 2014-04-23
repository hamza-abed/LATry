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
package client;

import java.io.File;
import java.io.IOException;

import java.io.Reader;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;

import java.util.Enumeration;

import java.util.List;
import java.util.Properties;


import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;


import client.utils.FileLoader;



import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * Cette classe permet d'accéder à differentes ressources de configuration pour LaClient
 * par l'intermédiaire d'un serveur REST.
 * Cette classe effectue les opérations suivantes :
 * <ul>
 * <li>collecte des adresses IP du client</li>
 * <li>lecteure </li>
 * </ul>
 * 
 * 
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2013
 */
public class RessourceManager {
	
	/**
	 * Variable pour les log
	 */
	private static final Logger logger = Logger.getLogger("RessourceManager");
	

	/**
	 * Serveur REST contenant les ressources
	 */
	private String server="http://134.214.147.234:8080/laproxy";
	/**
	 * Client de connexion REST
	 */
	//private Client client;
	
	
	private Properties props;
	
	/**
	 * Retourne les propriétés de configuration
	 * @return classe Properties
	 */
	public Properties getProps() {
		return props;
	}

	/**
	 * Charge les propriétes de LaClient	
	 * @author philippe pernelle 
	 */
	public void setProps() {
		
		logger.info("*** Chargement de la configuration client ***");
		try {
			
			this.props = new Properties(); 
			
			// chargement du fichier de propriété
			
			File fichierconf= new File("data/client.conf");
			
			if (fichierconf.exists()) 
			{
				props.load(FileLoader.getResourceAsStream("data/client.conf"));
				logger.info(" --> Chargement fichier propriété : data/client.conf");
			
				if (new File(System.getProperty("user.home")+"/client.conf").exists())
				{
					props.load(FileLoader.getResourceAsStream(System.getProperty("user.home")+"/client.conf"));//*/
					logger.info("Chargement de :"+System.getProperty("user.home")+"/client.conf");
				}
			} 
			else
			{
				logger.info(" --> Chargement depuis serveur ressourcef"+server);
				 if (! ChargeRessource()) 
				 {
					 logger.info("  ---> probleme pas de chargement de ressourcef");
				 }
			}
			
			
			
			//setProps(props);
			
	
			
			logger.info("*** fin Chargement de la configuration client ***");
	
			
		} catch (IOException e) {
			logger.warning("IOException : Je le savais !");
		}
	
	}


	private String adresseIP;
	
	public String getAdresseIP() {
		return adresseIP;
	}


	public void setAdresseIP(String adresseIP) {
		this.adresseIP = adresseIP;
	}


	

	/**
	 * @param playableCharacter
	 */
	public RessourceManager() {
		
		// recuperation des adresses IP
		List<String> mesips=this.getIps();
		
		// par défaut je garde la premiere adresse
		this.setAdresseIP(mesips.get(0));
		
		// je donne priorité aux adresse prive
		for (int i=0;i<mesips.size(); i++)
		{
			if (mesips.get(i).startsWith("192")) {
				this.setAdresseIP(mesips.get(i));
			}
		}
		
		
		
		logger.info(" ==> Adresse IP client validée pour ressource = "+this.getAdresseIP());
		
		
		setProps();
		
		//this.clientgame = clientgame;
		//this.server = clientgame.getWorld().getGame().getProps().getProperty("la.ressourcegame.server", "http://google.fr");

		
		
		
		// FIXME close l'executor à la sortie
	}

	
	/**
	 * Retourne toutes les adresses ips des cartes réseau de la machine.
	 * Retourne seulement les addresses IPV4
	 * 
	 * @return List(String) : Une liste des addresses ip
	 */
	public List<String> getIps(){
		List<String> ips = new ArrayList<String>();
		logger.info(" ***** Analyse des interfaces réseau du client ***");
		try{
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		                
	         while (interfaces.hasMoreElements()) {  // carte reseau trouvee
	        	 
	            NetworkInterface interfaceN = (NetworkInterface)interfaces.nextElement(); 
	            Enumeration<InetAddress> ienum = interfaceN.getInetAddresses();
	            logger.info("Interface réseau : "+interfaceN.getDisplayName());
	            while (ienum.hasMoreElements()) {  // retourne l adresse IPv4 et IPv6
	                InetAddress ia = ienum.nextElement();
	                String adress = ia.getHostAddress().toString();
	                
	                if( adress.length() < 16){          //On s'assure ainsi que l'adresse IP est bien IPv4
	                    if(adress.startsWith("127")){  //Ce n'est pas l'adresse IP Local' 
	                    	logger.info(" --> adresse trouvé non validée Loopback :"+ia.getHostAddress());
	                    }
	                    else if(adress.indexOf(":") > 0){
	                    	logger.info(" --> adresse trouvé non validée IPV6 :"+ia.getHostAddress()); // les ":" indique que c'est une IPv6"
	                    }
	                    else {
	                    	ips.add(adress);
	                    	logger.info(" --> adresse trouvé validée :"+adress);
	                    }
	                } 
	                
	                //ips.add(adress);        
	            }
	        }
	    }
	    catch(Exception e){
	    	logger.info("pas de carte reseau");
	        
	    }
		logger.info(" ***** Fin Analyse réseau du client ***");
	    return ips;
	}

	/**
	 * Charge un fichier de ressoures à partir d'ub REST
	 */
	
	public boolean ChargeRessource() {
		
		boolean retourRequete = false;
		
		logger.info(" --> lancement requete ressource REST sur : "+server+"/conf/ip/"+getAdresseIP());
		
		//try {
			
		Client client = new Client();
		ClientConfig config = new DefaultClientConfig();
		client = Client.create(config);
		WebResource service = client.resource(server+"/conf/ip/"+getAdresseIP());

		//ServiceRequest request = new ServiceRequest();
		//ServiceResponse response = service.path("addUser").type(MediaType.APPLICATION_XML)
		//.accept(MediaType.APPLICATION_XML).entity(request).post(ServiceRequest.class);
		
		
		//	logger.info("  ---> requete sur = "+server+"/conf/ip/"+getAdresseIP());
		//	ClientResponse r = client.resource(server+"/conf/ip/"+getAdresseIP()).
		//							accept(MediaType.TEXT_PLAIN).
		//							get(ClientResponse.class);
			
		ClientResponse r = service.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);
	        //System.out.println(r.getEntity(String.class));
			
		logger.info(" --> lancement requete ressource REST : OK");	
			
			
		String result = r.getEntity(String.class);
		//logger.info("  ---> resulta requete "+result);
			
			try {
				Reader myprop = new StringReader(result);
				props.load(myprop);
				logger.info("  ---> chargement propoerties depuis reponse REST OK");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*
			UserType u = r.getEntity(UserType.class);
			HashMap<String, HashMap<String, DataType>> list = new HashMap<String, HashMap<String, DataType>>();
			for (CategoryType cat : u.getCategory()) {
				HashMap<String, DataType> datas = new HashMap<String, DataType>();
				for (DataType data : cat.getData()) 
					datas.put(data.getName(), data);
				list.put(cat.getName(), datas);
								
			}
			
			applyNewList(list);
			*/
			
			retourRequete= true ;
			
		//} catch (Exception e) {

		//	logger.info("  ---> aucune reponse sur requete REST  = " +e.getLocalizedMessage());
			/*task.cancel(false);*/
		//}
		return retourRequete;

	}

	



	




	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
