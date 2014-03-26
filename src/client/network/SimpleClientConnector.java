/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.network;

import client.task.PingPongTask;
import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import com.sun.sgs.client.simple.SimpleClient;
import com.sun.sgs.client.simple.SimpleClientListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.nio.ByteBuffer;
import java.nio.channels.UnresolvedAddressException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import shared.constants.PckCode;
import shared.pck.Pck;
import shared.variables.Variables;

/**
 *
 * @author admin
 */
public class SimpleClientConnector implements SimpleClientListener{
    
    
    public SimpleClientConnector()
    {
        Variables.setClientConnecteur(this);
    }
    
    private Properties props;
    /** The version of the serialized form of this class. */
    private static final long serialVersionUID = 1L;

    /** The name of the host property. */
    public static final String HOST_PROPERTY = "server.0.host";

    /** The default hostname. */
    public static final String DEFAULT_HOST = "127.0.0.1";

    /** The name of the port property. */
    public static final String PORT_PROPERTY = "server.0.port";

    /** The default port. */
    public static final String DEFAULT_PORT = "10513";//"1139";

    /** The message encoding. */
    public static final String MESSAGE_CHARSET = "UTF-8";

   
    /** The {@link SimpleClient} instance for this client. */
    protected SimpleClient simpleClient;

    
    

    
    
    /**
   * Initiates asynchronous login to the RDS server specified by
   * the host and port properties.
   */
  protected void login() {
	  setStatus("this is login method");
      String host = System.getProperty(HOST_PROPERTY, DEFAULT_HOST);
      String port = System.getProperty(PORT_PROPERTY, DEFAULT_PORT);

      
      
      
      try {
    	  setStatus("try to login");
          Properties connectProps = new Properties();
          connectProps.put("host", host);
          connectProps.put("port", port);
          simpleClient.login(connectProps);
      } catch (Exception e) {
    	  setStatus("exception in login void");
          e.printStackTrace();
          disconnected(false, e.getMessage());
      }
  }

  /**
   * Displays the given string in this client's status bar.
   *
   * @param status the status message to set
   */
  protected void setStatus(String status) {
      Variables.console.output("> " + status);
      
  }

  /**
   * Encodes a {@code String} into an array of bytes.
   *
   * @param s the string to encode
   * @return the byte array which encodes the given string
   */
  protected static byte[] encodeString(String s) {
      try {
          return s.getBytes(MESSAGE_CHARSET);
      } catch (UnsupportedEncodingException e) {
          throw new Error("Required character set " + MESSAGE_CHARSET +
              " not found", e);
      }
  }

  /**
   * Decodes an array of bytes into a {@code String}.
   *
   * @param bytes the bytes to decode
   * @return the decoded string
   */
  protected static String decodeString(byte[] bytes) {
      try {
          return new String(bytes, MESSAGE_CHARSET);
      } catch (UnsupportedEncodingException e) {
          throw new Error("Required character set " + MESSAGE_CHARSET +
        	         " not found", e);
        	        }
        	    }

        	   
        	   

        	    // Implement SimpleClientListener

        	    /**
        	     * {@inheritDoc}
        	     * <p>
        	     * Returns dummy credentials where user is "guest-&lt;random&gt;"
        	     * and the password is "guest."  Real-world clients are likely
        	     * to pop up a login dialog to get these fields from the player.
        	     */
        	    public PasswordAuthentication getPasswordAuthentication() {
        	    	 Variables.console.output("Password authentification called");
        	    	
        	    	return new PasswordAuthentication(this.login, this.pass.toCharArray());
        	    }

        	    /**
        	     * {@inheritDoc}
        	     * <p>
        	     * Enables input and updates the status message on successful login.
        	     */
        	    public void loggedIn() {
        	       
        	      Variables.console.output("Logged in");
        	      
        	        getPingPongTask().start();
        	      Variables.console.output("ping pong task has just started !");
        	    }

        	    /**
        	     * {@inheritDoc}
        	     * <p>
        	     * Updates the status message on failed login.
        	     */
        	    public void loginFailed(String reason) {
        	        setStatus("Login failed: " + reason);
        	    }

        	    /**
        	     * {@inheritDoc}
        	     * <p>
        	     * Disables input and updates the status message on disconnect.
        	     */
        	    public void disconnected(boolean graceful, String reason) {
        	      
        	       Variables.console.output("Disconnected: " + reason);
        	    }
        	    /**
        	     * {@inheritDoc}
        	     * <p>
        	     * Returns {@code null} since this basic client doesn't support channels.
        	     */
        	    public ClientChannelListener joinedChannel(ClientChannel channel) {
        	        return null;
        	    }

        	    /**
        	     * {@inheritDoc}
        	     * <p>
        	     * Decodes the message data and adds it to the display.
        	     */
        	    
        	          	    
        	    public void receivedMessage(byte[] message) {
        	    	
        	    	          
        	        setStatus("Server: " + decodeString(message));
        	       
        	    }

        	    /**
        	     * {@inheritDoc}
        	     * <p>
        	     * Updates the status message on successful reconnect.
        	     */
        	    public void reconnected() {
        	        setStatus("reconnected");
        	    }

        	    /**
        	     * {@inheritDoc}
        	     * <p>
        	     * Updates the status message when reconnection is attempted.
        	     */
        	    public void reconnecting() {
        	        setStatus("reconnecting");
        	    }

        	  

				@Override
				public void receivedMessage(ByteBuffer message) {
					
					 short c = message.getShort();
					setStatus("a message received from server ! : "+c);
					switch (c) {
				case PckCode.PING:
					getPingPongTask().pong();
					break;
					}
				}
				
				
		//	private SimpleClient client;
			private String login,pass;
				
			//	private String rmi;
				//private String ressourceHttp;
			public void connect() {
				Properties properties = new Properties();
				properties.put("host", System.getProperty("server.0.host", "127.0.0.1"));
				 setStatus("first prop setted");
				properties.put("port", System.getProperty("server.0.port", "10510"));
				setStatus("second prop setted");
			//	rmi = props.getProperty("server."+num+".rmi", null);
				//ressourceHttp= props.getProperty("server."+num+".http.resources", null);
				
			//	initRessourcesHttp();

				this.login = "demo";
				this.pass = "demo";
//				this.rmiEditorAdress = props.getProperty("server."+num+".editorhost", "rmi://127.0.0.1/");
//				TODO
				this.simpleClient = new SimpleClient(this);
				try {
					 setStatus("connection au server");
					
					 simpleClient.login(properties);
				} catch (IOException e) {
					setStatus("warning : IOException : Probablement un probleme avec la connexion au serveur.");
				} catch (UnresolvedAddressException e) {
					setStatus(e.getClass().getSimpleName()+" : Probablement un probleme avec la résolution du DNS ou de Routing.");
				}
			}
			
			
			// à propos des propriètés
			/**
			 * Charge les propriétes de LaClient	
			 * @author philippe pernelle 
			 */
		
			/* ********************************************************** *
			 * * 				ENVOI MESSAGE REDDWARF 					* *
			 * ********************************************************** */
			/**
			 * Envoie un message au server
			 * 
			 * @param pck
			 */
			public void send(Pck pck) {
				try {
					simpleClient.send(pck.toByteBuffer());
					setStatus("a message has been sent to the server ! : "+pck.toString());
				} catch (IOException e) {
					//logger.warning("Connection au serveur echoué");
					disconnect("Broken Pipe");
					setStatus("Broken pipe1");
				} catch (IllegalStateException e) {
					//logger.warning("Connection au serveur echoué");
					// ca arrive quand on envoie un packet alors que le joueur est déconnecté
					disconnect("Broken Pipe");
					setStatus("Broken pipe2");
				} catch (Exception e) {
					//logger.warning(e.getClass().getName()+"\n lors de la communication serveur");
					disconnect("Broken Pipe");
					setStatus("Broken pipe3");
				}
			}
	
			
			/**
			 * demande la deconnection
			 */
			public void disconnect(final String reason) {
				try {
					this.simpleClient.logout(true);
				} catch (IllegalStateException e) {
				}
				getPingPongTask().stop();
				System.out.println("this is disconnection");
			
			}

			/**
			 * gestionnaire de tache
			 * 
			 * @return
			 */
			/**
			 * service de programmation de task
			 */
			private ScheduledThreadPoolExecutor scheduledExecutor;

			public ScheduledThreadPoolExecutor getSchedulerTaskExecutor() {
				if (scheduledExecutor == null) {
					/*scheduledExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(
							Integer.parseInt(props.getProperty("la.scheduled.task","10")));
*/
					scheduledExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(4);
				}
				return scheduledExecutor;
			}
			
			
			/**
			 * tache de test de connexion
			 */
			private PingPongTask pingPongTask;
			public PingPongTask getPingPongTask() {
				if (pingPongTask == null) 
					pingPongTask = new PingPongTask(this);
				return pingPongTask;
			}

			 
			
        	}
