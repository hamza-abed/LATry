/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.network;

import client.HttpResourceLocator;
import client.LaTraces;
import client.RessourceManager;
import client.chat.ChatSystem;
import client.editor.ServerEditor;
import client.interfaces.network.Sharable;
import client.map.World;
import client.task.PingPongTask;
import com.sun.sgs.client.ClientChannel;
import com.sun.sgs.client.ClientChannelListener;
import com.sun.sgs.client.simple.SimpleClient;
import com.sun.sgs.client.simple.SimpleClientListener;
import de.lessvoid.nifty.tools.Color;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.nio.ByteBuffer;
import java.nio.channels.UnresolvedAddressException;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
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
        this.login=login; this.pass=pass;
        
        this.ressources = new RessourceManager();
		
		// récupération des propriétés de configuration
        this.props = ressources.getProps();		
    }
    
    private boolean connecting=false;

   
    
    
    private RessourceManager ressources;
    
    private Properties props;
    private static final Logger logger = Logger.getLogger("RessourceManager");
    public Properties getProps() {
        return props;
    }
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

    private ChatSystem chatSystem;
      private World world;

    private LaTraces traces;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
    private String login,pass;
    /**
   * Initiates asynchronous login to the RDS server specified by
   * the host and port properties.
   */
  protected void login() {
	  setStatus("this is login method");
      String host = System.getProperty(HOST_PROPERTY, DEFAULT_HOST);
      String port = System.getProperty(PORT_PROPERTY, DEFAULT_PORT);

      
      
      
      try {
          logger.info("---------> methode login trying to login");
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
      if(Variables.getConsole()!=null)
      Variables.getConsole().output("> " + status);
      else
          System.out.println("> " + status);
      
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
        	    	// Variables.getConsole().output("Password authentification called");
                         setStatus("Password authentification called");
        	    	
        	    	return new PasswordAuthentication(this.login, this.pass.toCharArray());
        	    }

        	    /**
        	     * {@inheritDoc}
        	     * <p>
        	     * Enables input and updates the status message on successful login.
        	     */
        	    public void loggedIn() {
        	      connecting=false;
                        Connected=true;
        	     // Variables.getConsole().output("Logged in");
                        setStatus("Logged in");
        	      
        	        getPingPongTask().start();
        	   //   Variables.getConsole().output("ping pong task has just started !");
                        setStatus("ping pong task has just started !");
        	    }

        	    /**
        	     * {@inheritDoc}
        	     * <p>
        	     * Updates the status message on failed login.
        	     */
        	    public void loginFailed(String reason) {
                        connecting=false;
                        logger.info("Login failed: " + reason);
        	        setStatus("Login failed: " + reason);
                        Variables.getConnectionStatusLabel().setText("Login failed: " + reason);
                        Variables.getConnectionStatusLabel().setColor(new Color(1, 0f, 0, 1));
        	    }

        	    /**
        	     * {@inheritDoc}
        	     * <p>
        	     * Disables input and updates the status message on disconnect.
        	     */
        	    public void disconnected(boolean graceful, String reason) {
        	      connecting=false;
                        logger.info("->Disconnected: " + reason);
        	      
                       Variables.getConnectionStatusLabel().setText(reason);
                        Variables.getConnectionStatusLabel().setColor(new Color(1, 0, 0, 1));
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

    
    private boolean Connected=false;
    public boolean isConnected() {
        return Connected;
        //return true;
        /*
         * return true just pour le test de l'accées
         */
    }
    
    
    /**
	 * initialise les ressources Http pour le serveur sélectionné
	 * @author philippe pernelle
	 */
	private void initRessourcesHttp() {
		//if (this.ressourceHttp !=null) 
		//ResourceLocatorTool.addResourceLocator(HttpResourceLocator.RESOURCE, new HttpResourceLocator(this.ressourceHttp));
		/*
                 * Le RessourceLocatorToll n'existe plus en JME3
                 */
	}

    public void setConnectionStatus(boolean ConnectionStatus) {
        this.Connected = ConnectionStatus;
    }
	
				
			//	private String rmi;
			//	private String ressourceHttp;
                                
			        public void connect(String login, String pass, int num) {
                                    
                                    
                                    this.connecting=true;
                                    this.login=login;
                                    this.pass=pass;
                                    System.out.println("this is connect login="+login+"  pass="+pass);
				Properties properties = new Properties(); //http://134.214.147.28/
                                
                                properties.put("host", System.getProperty("server."+num+".host", getProps().getProperty("server."+num+".host")));
				//properties.put("host", System.getProperty("server.0.host", "127.0.0.1"));
				 setStatus("first prop setted");
				properties.put("port", System.getProperty("server."+num+".port", getProps().getProperty("server."+num+".port")));
				setStatus("second prop setted");
			//	rmi = props.getProperty("server."+num+".rmi", null);
			//	ressourceHttp= props.getProperty("server."+num+".http.resources", null);
				
			//	initRessourcesHttp();

				
//				this.rmiEditorAdress = props.getProperty("server."+num+".editorhost", "rmi://127.0.0.1/");
//				TODO
				this.simpleClient = new SimpleClient(this);
				try {
                                    Variables.getConnectionStatusLabel().setText("Demande de connection au serveur");
                                    Variables.getConnectionStatusLabel().setColor(new Color(0.3f, 0.7f, 0, 1));
					 setStatus("connection au server");
					 simpleClient.login(properties);
                                        
				} catch (IOException e) {
					setStatus("warning : IOException : Probablement un probleme avec la connexion au serveur.");
                                        connecting=false;
				} catch (UnresolvedAddressException e) {
					setStatus(e.getClass().getSimpleName()+" : Probablement un probleme avec la résolution du DNS ou de Routing.");
                                        connecting=false;
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
			/**loggedIn
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
                        
                        
                        
                        
                        /*
                         * 
                         * connecting server
                         */
                        
                        
                        
                         /**
     * Demande de REQUEST au serveur
     *
     * @param map
     */
    public void updateFromServer(Sharable sharable) {
        //logger.info("Update de " + sharable.getKey());
        if(sharable==null) System.out.println("sharable is null");
        
        setStatus("Update de " + sharable.getKey());
        getChatSystem().debug(
                "? " + sharable.getKey() + " (" + sharable.getVersionCode()
                + ")");
        Pck pck = new Pck(PckCode.UPDATE);
        pck.putString(sharable.getKey());
        pck.putInt(sharable.getVersionCode());
        //send(pck); c'est au SimpleClient 

    }
      public ChatSystem getChatSystem() {
        if (chatSystem == null) {
            chatSystem = new ChatSystem(Variables.getLaGame());
        }
        return chatSystem;
    }

      public void updateFromServerAndWait(String key, Runnable callback) {
        updateFromServerAndWait(world.getSharable(key), callback);
    }

    public void updateFromServerAndWait(Sharable s, Runnable callback) {
        String key = s.getKey();
        if (!callbackWaitingUpdateAnswer.containsKey(key)) {
            callbackWaitingUpdateAnswer.put(key, new LinkedBlockingQueue<Runnable>());
        }
        try {
            callbackWaitingUpdateAnswer.get(key).put(callback);
            getChatSystem().debug("pause de " + callback);
            updateFromServer(s);
        } catch (InterruptedException e) {
            //logger.warning("impossible de mettre en pause le callback");
            Variables.getConsole().output("impossible de mettre en pause le callback");
        }

    }

    /**
     * Demande au serveur
     *
     * @param sharable
     * @param callback
     */
    public void updateFromServerAndWait(Sharable s) {
        //final Thread waiter = new Thread();
        //waiter.interrupt();
        final Object sync = new Object();

        synchronized (sync) {
            updateFromServerAndWait(s, new Runnable() {
                @Override
                public void run() {
                    synchronized (sync) {
                        sync.notifyAll();
                    }
                }
            });
            try {
                sync.wait();
            } catch (InterruptedException e) {
                //logger.warning(e.getLocalizedMessage());
                Variables.getConsole().output(e.getLocalizedMessage());
            }
        }

    }
    /**
     * gestionnaire de tache
     *
     * @return
     */
    private ThreadPoolExecutor executor;

    public ThreadPoolExecutor getTaskExecutor() {
        if (executor == null) {
            executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Integer.parseInt(
                    Variables.getClientConnecteur().getProps()
                    .getProperty("la.max.parallel.task", "8")));
            getSchedulerTaskExecutor().scheduleAtFixedRate(new Runnable() {
             @Override
             public void run() {
            // logger.info("state : "+executor.getActiveCount()+":"+executor.getQueue().size());
             Variables.getConsole().output("state : "+executor.getActiveCount()+":"+executor.getQueue().size());
             }
             }, 1, 1, TimeUnit.SECONDS);
        }
        //logger.info("state : "+executor.getActiveCount()+":"+executor.getQueue().size());
        return executor;
    }
    /**
     * gestionnaire de tache
     *
     * @return
     */
    /**
     * service de programmation de task
     */
    private ServerEditor serverEditor;

    public ServerEditor getServerEditor() {
        if (serverEditor == null) {
            serverEditor = new ServerEditor(Variables.getLaGame());
        }
        return serverEditor;
    }
    /**
     * Demande au serveur
     *
     * @param sharable
     * @param callback
     */
    private HashMap<String, LinkedBlockingQueue<Runnable>> callbackWaitingUpdateAnswer =
            new HashMap<String, LinkedBlockingQueue<Runnable>>();
    
    /**
     * renvoie l'object permettant d'envoyé des traces
     *
     * @return
     */
    

    public LaTraces getTraces() {
        if (traces == null) {
            traces = new LaTraces(Variables.getLaGame());
        }
        return traces;
    }

    /**
     * Notify sur le server les modification
     *
     * @param move
     */
    public void commitOnServer(Sharable sharable) {
        //logger.info("Commit de " + sharable.getKey());
        Variables.getConsole().output("Commit de " + sharable.getKey());
        getChatSystem().debug(
                "< " + sharable.getKey() + " (" + sharable.getVersionCode()
                + ")");
        Pck pck = new Pck(PckCode.COMMIT);
        pck.putString(sharable.getKey());
        pck.putInt(sharable.getVersionCode());
        sharable.addData(pck);
        //send(pck);
        Variables.getClientConnecteur().send(pck);
    }
			 
	
     public boolean isConnecting() {
        return connecting;
    }

    public void setConnecting(boolean connecting) {
        this.connecting = connecting;
    }
    
    
    
        	}

