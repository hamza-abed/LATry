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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.w3c.dom.Document;

import shared.utils.SSHTransfert;
import shared.utils.xml.XMLUtils;
import client.map.World;
//import client.map.tool.feather.FeatherEngine;
import client.map.tool.poll.Poll;
//import client.map.tool.feather.FeatherEngine;
//import client.map.tool.poll.Poll;

/**
 * Permet l'envoie de données via un serveur ftp ou sftp relier au monde 
 * <ul>
 * <li>useSSH est le parametre du fichier de configuration client qui indique si sftp</li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * @author Philippe Pernelle, <b>philippe.pernelle@gmail.com</b>, 2010-2013
 */
public class FtpManager {
	protected static final Logger logger = Logger.getLogger("FtpManager");

	public static final String POLL = "poll";

	/**
	 * Parametre du serveur FTP
	 */
	private String pass, url, user, folder;
	
	/**
	 * classe gérant les communication par ssh
	 */
	private SSHTransfert sshclient;

	/**
	 * Monde associer au serveur FTP ou SFTP
	 */
	private World world;
	
	private boolean useSSH =false;

	/**
	 * 
	 */
	public FtpManager(World world, boolean useSSH) {
		this.world = world;
		this.useSSH = useSSH;
	}

	/**
	 * Definit les parametre du gestionnaire FTP de LA3
	 * @param ftpUrl
	 * @param ftpUser
	 * @param ftpPass
	 * @param ftpFolder
	 */
	public void setParam(String ftpUrl, String ftpUser, String ftpPass,
			String ftpFolder) {
		this.url = ftpUrl;
		this.user = ftpUser;
		this.pass = ftpPass;
		this.folder = ftpFolder;

	}


	/* ********************************************************** *
	 * *		GESTION d'Evnoie de document spécifique			* *
	 * ********************************************************** */

	
	/**
	 * Envoie les réponse d'un sondage
	 */
	public void sendPollAnswer(Poll poll) {
		world.getGame().getTaskExecutor().execute(new SendPollAnswer(poll,this.useSSH, this.folder));
	}

	/**
	 * Tache d'envoie d'une réponse d'un poll
	 * <ul>
	 * <li></li>
	 * <li></li>
	 * </ul>
	 * 
	 * 
	 * @author Ludovic, Syscom 2009-2011
	 */
	private class SendPollAnswer implements Runnable {
		private Poll poll;
		private boolean useSSH;
		private String dest;

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		/**
		 * @param poll
		 */
		public SendPollAnswer(Poll poll, boolean useSSH, String dest) {
			this.poll = poll;
			this.useSSH = useSSH;
			this.dest = dest;
		}

		@Override
		public void run() {
			try {
				
				// création du fichier poll
				Document doc = poll.getDocumentAnswer();
				// génération du nom du poll
				String file = poll.getTitle()+"-"+getPlayerName()+"-"+System.currentTimeMillis()+".xml";

				XMLUtils.saveDocumentToFile(doc, new File("data/"+file));
				logger.info("Sauvegarde du sondage local : "+"data/"+file);
				if (this.useSSH){
					logger.info(" --> envoi du sondage par protocol SFTP");
					SSHTransfert clientssh = openClientSSH();
					
					clientssh.PutFile("data/"+file, this.dest);
					
					
					
				} else {
					logger.info(" --> envoi du sondage par protocol FTP");
					FTPClient client = openClient(POLL);
					

					OutputStream os = client.appendFileStream(file);
					BufferedOutputStream xmlStream = new BufferedOutputStream(os);
					XMLUtils.saveDocumentToStream(doc, xmlStream);
					xmlStream.close();
					os.close();
					client.disconnect();
				}
				
			} catch (Exception e) {
				//world.getGame().getHud().openFtpErrorPopup(e,url);
				e.printStackTrace();
			}
			try {
				poll.sendToBelbin(getPlayerName());
			} catch (Exception e) {
				//world.getGame().getHud().openFtpErrorPopup(e,url);
				e.printStackTrace();
			}
			try {
				poll.sendAnswerToTraces(world.getGame().getTraces());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
	
	/**
	 * envoie le docuemnt finale de la plume sous forme d'image
	 * @param featherEngine
	 */
        /*
	public void sendFeatherDocument(final FeatherEngine engine) {
		world.getGame().getSchedulerTaskExecutor().schedule(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				try {
					FTPClient client = openClient(engine.getKey());
					client.deleteFile("document.png");
					client.setFileType(FTP.BINARY_FILE_TYPE);
					OutputStream os = client.appendFileStream("document.png");
					//ImageIO.write(engine.getWall().getTexture().getImage(), "PNG", os);
					os.flush();
					os.close();
					client.disconnect();
				}  catch (Exception e) {
					logger.warning(e.getLocalizedMessage());
					e.printStackTrace();
				}
				return null;
			}
		}, 10, TimeUnit.SECONDS);
		
		
	}
*/
	/* ********************************************************** *
	 * *					GESTION FTP							* *
	 * ********************************************************** */

	/**
	 * ouvre un client ftp sur le sous dossier indiqué 
	 * @param poll
	 * @return 
	 * @throws IOException 
	 * @throws SocketException 
	 */
	public FTPClient openClient(String subFolder) throws SocketException, IOException {
		FTPClient client = new FTPClient();
		client.connect(url);
		client.login(user, pass);
		client.enterRemotePassiveMode();
		client.enterLocalPassiveMode();
		if (!client.changeWorkingDirectory(folder)) {
			client.makeDirectory(folder);
			client.changeWorkingDirectory(folder);
		}
		if (!client.changeWorkingDirectory(subFolder)) {
			client.makeDirectory(subFolder);
			client.changeWorkingDirectory(subFolder);
		}
		return client;		
	}
	
	public SSHTransfert openClientSSH() throws SocketException, IOException {
		
		logger.info(" --> appel d'un client SSH ("+this.user+","+this.pass+","+this.url);
		sshclient = new SSHTransfert(this.user, this.pass, this.url, 22);	
	
		return sshclient;		
	}
	
	
	


	/**
	 * Renvoi le nom du joueur
	 * @return
	 */
	public String getPlayerName() {
		//return world.getPlayer().getLogin();
                return "Sinbad";
	}

	
	

	/* ********************************************************** *
	 * *					TAG						* *
	 * ********************************************************** */
}
