/**
 * Copyright 2010 http://learning-adventure.fr
 * Tous droits réservés
 * 
 * 
 * ----------------------------------------------------------------------------
 * Ce fichier fait partie de LA-Shared.
 *
 * LA-Shared est un logiciel libre ; vous pouvez le redistribuer ou le modifier 
 * suivant les termes de la GNU General Public License telle que publiée par
 * la Free Software Foundation ; soit la version 3 de la licence, soit 
 * (à votre gré) toute version ultérieure.
 * 
 * LA-Shared est distribué dans l'espoir qu'il sera utile, 
 * mais SANS AUCUNE GARANTIE ; pas même la garantie implicite de 
 * COMMERCIABILISABILITÉ ni d'ADÉQUATION à UN OBJECTIF PARTICULIER. 
 * Consultez la GNU General Public License pour plus de détails.
 * 
 * Vous devez avoir reçu une copie de la GNU General Public License 
 * en même temps que LA-Shared ; si ce n'est pas le cas, 
 * consultez <http://www.gnu.org/licenses>.
 * ----------------------------------------------------------------------------
 * This file is part of LA-Shared.
 *
 * LA-Shared is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LA-Shared is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with LA-Shared.  If not, see <http://www.gnu.org/licenses/>.
 * ----------------------------------------------------------------------------
 */

package shared.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;



//import admin.utils.EntryInputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.UserInfo;

/**
 * Utilitaire sur des fichier 
 * <ul>
 * <li></li>
 * <li></li>
 * 
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2013
 */

public class SSHTransfert {
	
	private static final Logger logger = Logger.getLogger("SSHTransfert");

	private String sshUser;
	private String serverHost;
	private int serverPort;
	private String sshPass;


	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}





	private InputStream serverInputStream;

	private String configFileName;

	private Session session;

	private Channel channel;

	private String sshpath;
	
	
	/**
	 * constructeur de la classe SSHTransfert
	 * @param sshUser
	 * @param sshPass
	 * @param serverHost
	 * @param serverPort
	 */
	public SSHTransfert(String sshUser, String sshPass, String serverHost,int serverPort) {
		this.setSshUser(sshUser);
		this.setSshPass(sshPass);
		this.setServerHost(serverHost);
		this.setServerPort(serverPort);
	}
	
	
	
	/**
	 * Renvoie la session SSH en cours ou la crée si elle n'existe pas
	 * @return session : une session SSH
	 * @throws JSchException 
	 */
	private Session getSession() throws JSchException {
		if (session == null || !session.isConnected())  {
			
			// création d'une classe JSCH
			JSch ssh = new JSch();
			
			// création d'une session de connexion
			session = ssh.getSession(this.sshUser, this.serverHost, this.serverPort);
			
			session.setUserInfo(new UserInfo() {
				@SuppressWarnings("unused")
				private String passPhrase;
				public void showMessage(String message) {}
				public boolean promptYesNo(String message) { return true; }
				public boolean promptPassword(String message) { return true; }
				public boolean promptPassphrase(String message) { this.passPhrase = message; return false; }
				public String getPassword() { return sshPass; }
				public String getPassphrase() { return null; }
			});
			session.connect();
			logger.info("Connexion SSH"+ this.sshUser+"/"+this.sshPass+"/"+this.serverHost);
		}

		return session;
	}
	

	/**
	 * renvoie le channel de communication ssh vers le server
	 * @return
	 * @throws JSchException 
	 */
	public ChannelExec getExecChannel() throws JSchException {
		if (channel == null || !channel.isConnected() ) {
			channel = getSession().openChannel("exec");
			//	channel.connect();
			try {
				serverInputStream = channel.getInputStream();
			} catch (IOException e) {

			}//*/
		}
		return (ChannelExec)channel;
	}

	/**
	 * renvoie un channel de connection de SFTP
	 * @return
	 * @throws JSchException 
	 */
	public  ChannelSftp getSftpChannel() throws JSchException {
		
		if (channel == null || !channel.isConnected() ) {
			channel = getSession().openChannel("sftp");
			//	channel.connect();
			try {
				serverInputStream = channel.getInputStream();
			} catch (IOException e) {

			}//*/
		}
		return (ChannelSftp) getSession().openChannel("sftp");
	}

	
	public  void PutFile(String sourcePath, String destPath) {
		try {
			//recupération du cannaux SFTP
			ChannelSftp sftp = getSftpChannel();
			
			sftp.connect();
			
			//sftp.cd(path);
			//sftp.rm(serverBackupFolder+"/*");
			/*sftp.rmdir(serverBackupFolder);
			sftp.mkdir(serverBackupFolder);//*/
			logger.info("transfert par sftp de "+sourcePath+ "vers "+destPath); 
			sftp.put(sourcePath, destPath);
			
			sftp.exit();
			sftp.disconnect();
			
			
			
			

			
			
			

		} catch (JSchException e) {
			logger.warning("JSchException : "+e.getMessage());
		} catch (SftpException e) {
			logger.warning(e.getClass().getName()+"\n"+e.getMessage());
		}
	}


/**
 * @return the sshUser
 */
public String getSshUser() {
	return sshUser;
}


/**
 * @param sshUser the sshUser to set
 */
public void setSshUser(String sshUser) {
	this.sshUser = sshUser;
}


/**
 * @return the sshPass
 */
public String getSshPass() {
	return sshPass;
}


/**
 * @param sshPass the sshPass to set
 */
public void setSshPass(String sshPass) {
	this.sshPass = sshPass;
}

/**
 * @return the serverHost
 */
public String getServerHost() {
	return serverHost;
}



/**
 * @param serverHost the serverHost to set
 */
public void setServerHost(String serverHost) {
	this.serverHost = serverHost;
}



/**
 * @return the serverPort
 */
public int getServerPort() {
	return serverPort;
}

}
