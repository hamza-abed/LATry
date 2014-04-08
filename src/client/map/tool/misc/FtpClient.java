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
package client.map.tool.misc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPReply;

/**
 * Mini-client FTP : envoie, réceptionne et supprime un fichier.
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 */
public class FtpClient {
	private static final Logger logger = Logger.getLogger(FtpClient.class
			.getName());
	public static final int UPLOAD = 1, DOWNLOAD = 2, DELETE = 3;

	/**
	 * Télécharge un fichier directement avec l'URL, sans passer par la
	 * connexion au serveur FTP.
	 * 
	 * @param url
	 * @param localFile
	 *            Le chemin du fichier qui sera enregistré en local
	 */
	public static void directDownload(URL url, String localFile) {
		logger.entering(FtpClient.class.getName(), "directDownload");

		InputStream input = null;
		FileOutputStream writeFile = null;

		try {
			URLConnection connection = url.openConnection();
			if (connection.getContentLength() == -1) {
				logger.info("URL ou nom de fichier incorrect.");
				return;
			}

			input = connection.getInputStream();
			writeFile = new FileOutputStream(localFile);
			byte[] buffer = new byte[1024];
			int read;

			logger.info("Téléchargement de " + url.getFile());
			while ((read = input.read(buffer)) > 0)
				writeFile.write(buffer, 0, read);
			writeFile.flush();
			logger.info("Téléchargement terminé.");
		} catch (IOException e) {
			logger
					.info("Impossible de télécharger le fichier "
							+ url.getFile());
		} finally {
			try {
				writeFile.close();
				input.close();
			} catch (Exception e) {
			}
		}

		logger.exiting(FtpClient.class.getName(), "directDownload");
	}

	/**
	 * Exécute une action sur le serveur host (envoi ou mise à jour d'un
	 * fichier, téléchargement, suppression)
	 */
	public static boolean ftp_do(int action, String host, String user,
			String password, String localFile, String remoteFile) {
		logger.entering(FtpClient.class.getName(), "ftp_do");

		if (host == null)
			return false;

		FTPClient ftp = new FTPClient();

		// Connection au serveur
		try {
			ftp.connect(host);
			logger.info("Connecté à " + host + ".");

			// Vérification du code retourné pour voir si tout s'est bien passé
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				logger.info("Connexion au serveur FTP refusée.");
				return false;
			}
		} catch (IOException e) {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException f) {
				}
			}
			logger.info("La connexion au serveur a échoué.");
			return false;
		}

		try {
			// Login
			if (!ftp.login(user, password)) {
				ftp.logout();
				return false;
			}

			ftp.enterLocalPassiveMode(); // Firewall-proof
			ftp.setFileType(FTP.BINARY_FILE_TYPE);

			switch (action) {
			case UPLOAD:
				upload(ftp, localFile, remoteFile);
				break;
			case DOWNLOAD:
				download(ftp, remoteFile, localFile);
				break;
			case DELETE:
				delete(ftp, remoteFile);
				break;
			}
		} catch (FTPConnectionClosedException e) {
			logger.info("Connexion interrompue par le serveur.");
			return false;
		} catch (IOException e) {
			logger.info("Erreur lors du transfert du fichier " + remoteFile);
			return false;
		}

		// Déconnexion
		if (ftp.isConnected()) {
			try {
				ftp.disconnect();
			} catch (IOException f) {
			}
		}
		logger.info("Connexion fermée.");

		logger.exiting(FtpClient.class.getName(), "ftp_do");
		return true;
	}

	/**
	 * Uploade un fichier
	 * 
	 * @throws IOException
	 */
	private static void upload(FTPClient ftp, String localFile,
			String remoteFile) throws IOException {
		logger.entering(FtpClient.class.getName(), "upload");

		InputStream input = new FileInputStream(localFile);
		logger.info("Envoi de " + localFile);
		ftp.storeFile(remoteFile, input);
		logger.info("Transfert terminé.");
		input.close();

		logger.exiting(FtpClient.class.getName(), "upload");
	}

	/**
	 * Downloade un fichier
	 * 
	 * @throws IOException
	 */
	private static void download(FTPClient ftp, String remoteFile,
			String localFile) throws IOException {
		logger.entering(FtpClient.class.getName(), "download");

		OutputStream output = new FileOutputStream(localFile);
		logger.info("Téléchargement de " + remoteFile);
		ftp.retrieveFile(remoteFile, output);
		logger.info("Téléchargement terminé.");
		output.close();

		logger.exiting(FtpClient.class.getName(), "download");
	}

	/**
	 * Supprime un fichier sur le serveur
	 * 
	 * @throws IOException
	 */
	private static void delete(FTPClient ftp, String remoteFile)
			throws IOException {
		logger.entering(FtpClient.class.getName(), "delete");

		ftp.deleteFile(remoteFile);
		logger.info("Suppression du fichier " + remoteFile + " terminée.");

		logger.exiting(FtpClient.class.getName(), "delete");
	}
}
