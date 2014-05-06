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
package client.hud;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.logging.Logger;
import java.util.regex.Matcher;

import shared.enums.CharacterModel;
import shared.enums.LaComponent;
import shared.utils.PropertyReader;
//pas opérationnelle en v31
//import ui.QuestEditorFrame;
import client.LaGame;
//import quest.Quest;
import client.hud.MultiQuestionWindow.ReponseListener;
import client.hud.OpenQuestionWindow.OpenQuestionListener;
import client.hud.PopupWindow.PopupIcon;
import client.hud.YesNoQuestionWindow.YesNoListener;
//import client.hud.boussole.BoussoleWindow;
import client.hud.components.LaBButton;
import client.hud.editor.EditWindow;
//import client.hud.editor.EditWindow;
import client.hud.editor.HudPopulateurAnnotation;
import client.hud.editor.annotation.EditAnnotationWindow;
import client.hud.missionStatus.MissionStatusWindow;
//pas opérationnelle en v31
//import client.hud.scenarization.HudScenarizationAction;
//import client.hud.scenarization.HudScenarizationBut;
//import client.hud.scenarization.HudScenarizationChargementEnCours;
//import client.hud.scenarization.HudScenarizationDroppableElementsDActivite;
//import client.hud.scenarization.HudScenarizationDroppableZonesDActivites;
//import client.hud3d.SplashText3D;
//import client.input.MainKeyInput;
import client.input.MouseCursor;
import client.interfaces.network.SharableEditable;
import client.interfaces.network.SharableReflexEditable;
import client.map.World;
import client.map.character.NonPlayableCharacter;
import client.map.character.Player;
import client.map.data.SlideShow;
import client.script.ScriptableMethod;
import client.utils.FileLoader;

//import com.jme.system.DisplaySystem;
//import com.jme.util.GameTaskQueueManager;
import com.jmex.bui.BComboBox;
import com.jmex.bui.BContainer;
import com.jmex.bui.BLabel;
import com.jmex.bui.BPasswordField;
import com.jmex.bui.BRootNode;
import com.jmex.bui.BTextField;
import com.jmex.bui.BWindow;
import com.jmex.bui.BuiSystem;
import com.jmex.bui.bss.BStyleSheetUtil;
import com.jmex.bui.event.ActionEvent;
import com.jmex.bui.event.ActionListener;
import com.jmex.bui.layout.BorderLayout;
import com.jmex.bui.layout.TableLayout;
import org.lwjgl.opengl.Display;
import shared.variables.Variables;

/**
 * Classe racine de l'interface utilisateur
 * <ul>
 * <li>contient les methodes scriptables </li>
 * 	<ul>
 * 		<li>setMouseIcon</li>
 * 		<li>openStylisme</li>
 * 		<li>setChatVisible</li>
 * 	</ul>
 * <li>suppression package client.extern.scenarization;
 * client.hud.scenarization;</li>
 * </ul>
 * 
 * 
 * @author Ludovic Kepka, <b> shionn@gmail.com</b>, 2009-2011
 * @author philippe pernelle, <b>philippe.pernelle@gmail.com</b>, 2010-2013
 */
public class Hud  {
	private static final Logger logger = Logger.getLogger("Hud");

	/**
	 * espace dans les layouts
	 */
	public static final int SPACE = 3;

	public static final int BAG_WIDTH = 250;

	public static final int WINDOW_DECAL_Y = -60; // anciennne valeur -10

	/**
	 * Reference du jeu
	 */
	private LaGame game;

	/**
	 * fenetre du menu
	 */
	private Menu menu;

	/**
	 * taille de l'ecran pour le placement des fenetres
	 */
	public int w, h;

	/**
	 * fenetre du chat
	 */
	private ChatWindow chat;

	/**
	 * configuration de la langue
	 */
	private Properties lang;

	/**
	 * menu principale avec les bouton en bas à droite
	 */
	MainMenu mainMenu;

	/**
	 * fenetre de connection principal au  jeu 
	 */
	private BWindow loginWindow;

	/**
	 * fenetre de discution avec un PNJ
	 */
	private NpcDailogWindow npcDialog;

	/**
	 * Fenetre du sac
	 */
	BagWindow bags;

	/**
	 * Scenarisation
	 */
// pas opérationnelle en v31
//	private HudScenarizationBut editeurDeButs;
//	private HudScenarizationAction editeurDActions;
//	
//	private HudScenarizationDroppableZonesDActivites zonesActivites;
//	private HudScenarizationDroppableElementsDActivite elementsActivites;
	
	private HudPopulateurAnnotation populus;
	
	private SlideShowHud slideshow;

	private boolean visible = true;

	private ImageWindow image;

	private CurrentTaskWindow followedTask;

	//private BoussoleWindow boussole;

	private MissionStatusWindow missionStatus;
	
	private BComboBox serverSelector;

	private PollWindow poll;

	private LoadingScreen loading;
	
// pas opérationnelle en v31	
//	private HudScenarizationChargementEnCours scenarization_loading;

	SkillsWindow skill;

	private Group group;

	private GroupEditor groupeditor;

	private PlayerEditor playerEditor;

	TasksWindow tasks;
// pas opérationnelle en v31
//	private QuestEditorFrame questeditor;

	public Hud(LaGame game) {
		this.game = game;
	//	MainKeyInput.get().setHud(this);

		// charge le style des interfaces pour les fenetres principales
		BuiSystem.init(FileLoader.getResourceAsUrl(Variables.getProps().getProperty("hud.bui.file", "data/style.bss")));

		// charge la lang
		lang = new Properties();
		try {
			lang.load(FileLoader.getResourceAsStream("data/"+Variables.getLanguage()+".properties"));
		} catch (IOException e) {
			logger.warning("impossible d'ouvrir le fichier de langue");
		}

		w = Display.getWidth();
		h = Display.getHeight();

		// construction des divers fenetres
		this.menu = new Menu(this);
		initLogin();

		this.bags = new BagWindow(this);
		//this.boussole = new BoussoleWindow(this);
		this.missionStatus = new MissionStatusWindow(this);
		this.chat = new ChatWindow(this);
		this.image = new ImageWindow(this);
		this.followedTask = new CurrentTaskWindow(this);
		this.mainMenu = new MainMenu(this);
		this.npcDialog = new NpcDailogWindow(this);
		this.skill = new SkillsWindow(this);
		this.slideshow = new SlideShowHud(this);
		this.tasks = new TasksWindow(this);
		this.poll = new PollWindow(this);
		this.group = new Group(this);
		this.groupeditor = new GroupEditor(this);
		this.playerEditor = new PlayerEditor(this);
// pas opérationnelle en v31	
//		this.zonesActivites = new HudScenarizationDroppableZonesDActivites(this);
//		this.elementsActivites = new HudScenarizationDroppableElementsDActivite(this);
//		
//		this.editeurDeButs = new HudScenarizationBut(this);
//		this.editeurDActions = new HudScenarizationAction(this);
		
		this.populus = new HudPopulateurAnnotation(this);
		
		this.loading = new LoadingScreen(this);
//      pas opérationnelle en v31			
//		this.scenarization_loading = new HudScenarizationChargementEnCours(this);
		
		updateVisibility();
		
		new Thread(menu).start();
	}

	private void initLogin() {
		loginWindow = new BWindow("login", BStyleSheetUtil
				.getStyleSheet(FileLoader.getResourceAsUrl("data/login.bss")),
				new BorderLayout(SPACE, SPACE));

		// TOP
		BLabel title = new BLabel(getLocalText("connexion.title"));
		title.setStyleClass("label-title");
		loginWindow.add(title, BorderLayout.NORTH);

		// Middle login
		BLabel loginLabel = new BLabel(getLocalText("connexion.login"));
		final BTextField userTextField = new BTextField();
		userTextField.setPreferredWidth(100);
		
		// Middle pass
		BLabel passLabel = new BLabel(getLocalText("connexion.pass"));
		final BPasswordField passTextField = new BPasswordField();
		passTextField.setPreferredWidth(100);
		
		passTextField.addListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				int server = serverSelector.getSelectedIndex();
				if (server == -1) server = 0;
				Variables.getClientConnecteur().connect(userTextField.getText(), passTextField.getText(), server);
			}
		});

		// Middle server
		BLabel serverLabel = new BLabel(getLocalText("connexion.server"));
		serverSelector = new BComboBox();
		//serverSelector.setText(game.getProps().getProperty("server.0.name", "Server inconnu : 0"));
		for (int i=0;i<PropertyReader.getInt(Variables.getProps(),"server.count", 1);i++) 
			serverSelector.addItem(Variables.getProps().getProperty("server."+i+".name", "Server inconnu : "+i));
		serverSelector.selectValue(serverSelector.getValue(0));
		//serverSelector.setPreferredSize(200,20);
		
		TableLayout layout = new TableLayout(2, Hud.SPACE, Hud.SPACE);
		layout.setHorizontalAlignment(TableLayout.STRETCH);
		
		BContainer panel = new BContainer(layout);
		panel.add(loginLabel);
		panel.add(userTextField);
		panel.add(passLabel);
		panel.add(passTextField);
		panel.add(serverLabel);
		panel.add(serverSelector);

		loginWindow.add(panel, BorderLayout.CENTER);
		
		// BOTTOM
		loginWindow.add(new LaBButton(
				getLocalText("connexion.connexion"),
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						int server = serverSelector.getSelectedIndex();
						if (server == -1) server = 0;
						Variables.getClientConnecteur().connect(userTextField.getText(), passTextField.getText(), serverSelector.getSelectedIndex());
					}
				}, "connexion"),
				BorderLayout.SOUTH);
		
		
		BuiSystem.addWindow(loginWindow);

		loginWindow.pack();
		loginWindow.center();
	}

	/* ********************************************************** *
	 * * 					Rafraichissement 					* *
	 * ********************************************************** */
	/**
	 * Met à jour la visibilité des interface
	 */
	public void updateVisibility() {
		bags.setVisible(false);
		chat.setVisible(visible && Variables.getClientConnecteur().isConnected());
		followedTask.setVisible(visible && Variables.getClientConnecteur().isConnected());
		image.setVisible(false);
		loginWindow.setVisible(visible && !Variables.getClientConnecteur().isConnected());
		mainMenu.setVisible(false);
		menu.setVisible(visible && Variables.getClientConnecteur().isConnected());
		npcDialog.setVisible(false);
		skill.setVisible(false);
		slideshow.setVisible(false);
		tasks.setVisible(false);
		poll.setVisible(false);
// pas opérationnelle en v31		
//		editeurDeButs.setVisible(false);
//		editeurDActions.setVisible(false);
//		zonesActivites.setVisible(false);
//		elementsActivites.setVisible(false);
		populus.setVisible(false);
		group.setVisible(false);
		groupeditor.setVisible(false);
		playerEditor.setVisible(false);
		
		//boussole.setVisible(visible && Variables.getClientConnecteur().isConnected());
		missionStatus.setVisible(visible && Variables.getClientConnecteur().isConnected());
	}
	
// pas opérationnelle en v31	
//	public void updateScenarizationWindowsStatuses()
//	{
//		editeurDeButs.rafraichir();
//		editeurDActions.rafraichir();
//		zonesActivites.rafraichir();
//		elementsActivites.rafraichir();
//	}
	
	/**
	 * Met à jour l'affichage des task
	 */
	public void refreshTask() {
		followedTask.requestRefresh();
		tasks.requestRefresh();
	}
	
	/**
	 * Met à jour l'affichage de la boussole
	 */
	public void refreshBoussole() {
		//boussole.requestRefresh();
            
            System.out.println("Hud-> refreshBoussole() : vide !!");
	}
	
	
	public void refreshMissionStatus() {
		missionStatus.requestRefresh();
	}
	
	/**
	 * Met à jour l'affichage du hud en fonction des droits
	 * @param admin
	 */
	public void updateRights(boolean admin) {
		getMenu().updateRights(admin);
		getMainMenu().updateRights(admin);
	}


	/* ********************************************************** *
	 * *						HUD FOR TOOLS 					* *
	 * ********************************************************** */

	
	/* ********************************************************** *
	 * * 			Ouverture de fenetre en cours de jeux 		* *
	 * ********************************************************** */

	/**
	 * ouvre une fenetre d'edition sur un objet
	 * 
	 * @param s
	 */
	public void openHudEdit(SharableEditable s) {
		logger.info("appel fenetre edition de :"+s.getKey());
		new EditWindow(this, s);
	}
	
	/**
	 * ouvre une fenetre d'édition par introspection
	 * @param s
	 */
	public void openHudIntrospectEdit(SharableReflexEditable s) {
		new EditAnnotationWindow(this, s);
	}

	/**
	 * ouvre un popup
	 * 
	 * @param title
	 * @param reason
	 */
	public void openDisconnectPopup(String reason) {
		new PopupWindow(getLocalText("popup.disconnect.title"),
				getLocalText("popup.disconnect.message","%reason%",reason==null?"NULL":reason),
				getLocalText("popup.close"), false, PopupIcon.disconnect);
	}

	/**
	 * ouvre un popup
	 * 
	 * @param title
	 * @param reason
	 */
	public void openCantConnectPopup(String reason) {
		new PopupWindow(getLocalText("popup.cantconnect.title"),
				getLocalText("popup.cantconnect.message","%reason%",reason),
				getLocalText("popup.close"), true, PopupIcon.disconnect, 340,230,null);
	}

	/**
	 * ouvre un popup indiquant que ce à quoi le programme acced n'est pas
	 * encore implémenté.
	 */
	@ScriptableMethod(description="affiche un text d'une fonctionnalité pas encore faite")
	public void openNotYetImplementedPopup() {
		new PopupWindow(getLocalText("popup.nyi.title"),
				getLocalText("popup.nyi.message"), getLocalText("popup.close"),
				false, PopupIcon.nyi);
	}
	
	/**
	 * Ouvre un popup d'erreur
	 * @param string
	 */
	@ScriptableMethod(description="affiche un message d'erreur")
	public void openErrorPopup(String error) {
		new PopupWindow(getLocalText("popup.error.title"),
				error, getLocalText("popup.close"), 
				false, PopupIcon.warning);
	}
	
	/**
	 * idem avec une exception
	 * @param error
	 */
	public void openErrorPopup(Exception error) {
		StringWriter sw = new StringWriter();
		error.printStackTrace(new PrintWriter(sw));
		new PopupWindow(getLocalText("popup.error.title"),
				error.getMessage()+"\n"+sw.toString(), getLocalText("popup.close"), 
				true, PopupIcon.warning, w*8/10, h*8/10, null);
	}
	
	@ScriptableMethod(description="ouvre un message")
	public void openMessagePopup(String message) {
		new PopupWindow(getLocalText("popup.message.title"),
				message, getLocalText("popup.close"), 
				false, PopupIcon.message);
	}

	/**
	 * Affiche une erreur relative au FTP
	 * @param url 
	 * @param string
	 */
	public void openFtpErrorPopup(Exception e, String url) {
		new PopupWindow(getLocalText("popup.error.ftp.title"),
				getLocalText("popup.error.ftp.message")+"\n"+url+"\n"+
				e.getLocalizedMessage(),
				getLocalText("popup.close"), 
				false, PopupIcon.disconnect);
	}	
	
	/**
	 * ouvre un popup d'attente
	 * @param error
	 * @return
	 */
	public PopupWindow openWaitingPopop(String title, String message) {
		return new PopupWindow(title,
				message, null, 
				false, PopupIcon.waiting);
	}
	
	/**
	 * ouvre un popup d'attente
	 * @param error
	 * @return
	 */
	public PopupWindow openLgfWaitingPopop() {
		return new PopupWindow(getLocalText("popup.lgf.title"),
				getLocalText("popup.lgf.message"), null, 
				false, PopupIcon.waiting);
	}

	/* ********************************************************** *
	 * *			Methode scriptable par le HUD				* * 	
	 * *				AFFICHAGE MASQUAGE						* *
	 * ********************************************************** */

	@ScriptableMethod(description="affiche/masque le chat")
	public void setChatVisible(boolean visible) {
		chat.setVisible(visible);
	}
	
	@ScriptableMethod(description="affiche/masque la boussole")
	public void setBoussoleVisible(boolean visible) {
		//boussole.setVisible(visible);
            System.out.println("Hud -> setBoussoleVisible() : vide !!");
	}
	
	@ScriptableMethod(description="affiche/masque le menu")
	public void setMenuVisible(boolean visible) {
		menu.setVisible(visible);
	}
	
	
	/* ********************************************************** *
	 * *			Methode scriptable par le HUD				* *
	 * *				Gestion de la souris	 				* *
	 * ********************************************************** */

	@ScriptableMethod(description="change le model d'icone de souris. Si c'est null, ca remet le model par defaut")
	public void setMouseIcon(String model) {
		MouseCursor.get().changeModel(model);
	}

	/* ********************************************************** *
	 * *			Methode scriptable par le HUD 				* *
	 * ********************************************************** */

	/**
	 * Ouvre le coiffeur 
	 */
	@ScriptableMethod(description="ouvre une fenetre d'edition de personnage")
	public void openStylisme() {
		final Hud h = this;
                /*
		GameTaskQueueManager.getManager().update(new Callable<Void>() {
			/* (non-Javadoc)
			 * @see java.util.concurrent.Callable#call()
			 */
		/*	@Override
			public Void call() throws Exception {
				CharacterModel[] models = new CharacterModel[] { CharacterModel.men, CharacterModel.women };
				new StylismeWindow(h,Variables.getWorld().getPlayer(),models);
				return null;
			}
		}); */
                System.out.println("Hud -> openStylisme() : vide !!");
	}
	
	/**
	 * ouvre le slideShow de l'index id
	 * @param identifiant
	 */
	@ScriptableMethod(description="Ouvre le slide show correspondant à l'Id.")
	public void openSlidShow(int id) {
		openSlideShow(Variables.getWorld().getSlideShowBuildIfAbsent(LaComponent.slides.prefix()+id),0);
	}	
	
	/**
	 * ouvre la slide
	 * @param slide
	 */
	public void openSlideShow(SlideShow slide,int page) {
		if (slide !=null) slideshow.open(slide,page);
	}
	
	public void closeSlideShow(SlideShow slide) {
		if (slideshow.slideShow == slide && slideshow.isVisible()) {
			slideshow.setVisible(false);
			slide.end();
		}	
	}

	/**
	 * Affiche du text en 3D au dessus du joueur
	 * @param text
	 */
	@ScriptableMethod(description="affiche du text en 3D au dessus du joueur\n\nle premier parametre est le text\nle second est la durée en ms")
	public void showText3d(String text,int time) {
		//new SplashText3D(text, getGame(),time);
            System.out.println("Hud -> showText3d() : vide !!!");
	}

	/**
	 * Affiche l'image à l'URL
	 * @param url
	 */
	@ScriptableMethod(description="ouvre une image dans une URL")
	public void openImage(String url) {
		image.open(url,null);
	}

	
	@ScriptableMethod(description="ouvre une image dans une URL\nLe second parametre est le titre de la fenetre")
	public void openImage(String url,String title) {
		image.open(url,title);
	}

	@ScriptableMethod(description="ouvre une question ouverte. execute le script une fois la réponse validée avec un parametre 'answer'")
	public void openOpenQuestion(String question,final int script) {
		openOpenQuestion(question, new OpenQuestionListener() {
			@Override
			public void answer(String answer) {
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("answer", answer);
				Variables.getWorld().getScriptExecutor().executeWithParams(LaComponent.script.prefix()+script,params);
			}
		});
	}
	
	public void openOpenQuestion(String question,OpenQuestionListener listener) {
		new OpenQuestionWindow(this, question, listener);
	}
	
	@ScriptableMethod(description="ouvre une question à réponse oui/non. execute le script une fois la réponse validée avec un parametre 'answer'")
	public void openYesNoQuestion(String question,final int script) {
		openYesNoQuestion(question, new YesNoListener() {
			@Override
			public void answer(boolean yesorno) {
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("answer", yesorno);
				Variables.getWorld().getScriptExecutor().executeWithParams(LaComponent.script.prefix()+script,params);
			}
		});
	}
	
	/**
	 * ouvre un popup avec une question en oui/non
	 * @param hud
	 */
	public void openYesNoQuestion(String question,YesNoListener listener) {
		new YesNoQuestionWindow(this,question,listener);
	}
	
	
	
	
	
	/**
	 * @param question
	 * @param script
	 * @author philippe pernelle
	 */
	@ScriptableMethod(description="ouvre une question à réponse multiple. execute le script une fois la réponse validée avec un parametre 'answer'")
	public void openMultiQuestion(String question,final int script) {
		openMultiQuestion(question, new ReponseListener() {
			@Override
			public void answer(int reponse) {
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("answer", reponse);
				Variables.getWorld().getScriptExecutor().executeWithParams(LaComponent.script.prefix()+script,params);
			}
		});
	}
	
	/**
	 * ouvre un popup avec une question en oui/non
	 * @param hud
	 * @author philippe pernelle
	 */
	public void openMultiQuestion(String question,ReponseListener listener) {
		new MultiQuestionWindow(this,question,listener);
	}
	
	
	
	
	
	
	
	/**
	 * Ouvre un sondage. 
	 * @param url
	 */
	@ScriptableMethod(description="ouvre un sondage, le parametre est une url ou est accessible le sondage")
	public void openHttpPoll(String url) {
		poll.openHttp(url,null);
	}
	
	/**
	 * Ouvre un sondage. avec un script exiter a la validation du sondage 
	 * @param url
	 */
	@ScriptableMethod(description="ouvre un sondage, le parametre est une url ou est accessible le sondage\nle soncond parametre est la clef du script à appelé à la fin")
	public void openHttpPoll(String url,String script) {
		poll.openHttp(url,script);
	}
	
	/**
	 * permet d'ouvrir un widget dans une application externe
	 */
	@ScriptableMethod(description="Permet de lancer un widget de type spyder. \nP1La requettes SQL\nP2Les Series\nP3Les categorie\nP4LEs Valeurs")
	public void openSpiderWidget(String SQL, String serie, String categorie, String values) {
		String[] cmd = new String[8];
		cmd[0] = "java";
		cmd[1] = "-jar";
		cmd[2] = "SyscomObserverForLA.jar";
		cmd[3] = "11";
		cmd[4] = SQL;
		cmd[5] = serie;
		cmd[6] = categorie;
		cmd[7] = values;
		
		File folder = new File("../LaBins/Observer/");
		if (!folder.exists())
			folder= new File("./"); 
		
		try {
			//final InputStream es =
			Runtime.getRuntime().exec(cmd, null, folder);//.getErrorStream();
			/*new Thread(new Runnable() {
				
				@Override
				public void run() {
					int c;
					try {
						while ((c = es.read())!=-1) {
							System.out.print((char)c);
						}
					} catch (IOException e) {
						logger.warning("IOException : Je le savais !");
					}
				}
			}).start();//*/
		} catch (Exception e) {
			logger.warning(e.getLocalizedMessage());
		}
	}

	/**
	 * permet d'ouvrir un widget dans une application externe
	 */
	@ScriptableMethod(description="Permet de lancer un widget à partir du numéro de composant. \nP1 Le type du widget\nP2 La requettes SQL\nP3 Le numéro du composant à instancier")
	public void openWidget(String type, String SQL, String instance) {
		String[] cmd = new String[6];
		cmd[0] = "java";
		cmd[1] = "-jar";
		cmd[2] = "SyscomObserverForLA.jar";
		cmd[3] = type;
		cmd[4] = SQL;
		cmd[5] = instance;
		
		File folder = new File("../LaBins/Observer/");
		if (!folder.exists())
			folder= new File("./"); 
		
		try {
			Runtime.getRuntime().exec(cmd, null, folder);
		} catch (Exception e) {
			logger.warning(e.getLocalizedMessage());
		}
	}
	
	
	/* ********************************************************** *
	 * * 				Fait clignoté le menu					* *
	 * ********************************************************** */
	
	/**
	 * fait clignoté le sac
	 * @param b
	 */
	@ScriptableMethod(description="Active/Descative le clignotement du sac")
	public void setBagBlink(boolean b) {
		menu.bagBlink = b;
	}
	
	/**
	 * fait clignoter le skill
	 * @param b
	 */
	@ScriptableMethod(description="Active/Descative le clignotement du status")
	public void setSkillBlink(boolean b) {
		menu.skillBlink = b;
	}
	
	/**
	 * fait clignoter le tasklist
	 * @param b
	 */
	@ScriptableMethod(description="Active/Descative le clignotement du livre de quete")
	public void setTaskBlink(boolean b) {
		menu.todoBlink = b;
	}
	
	/* ********************************************************** *
	 * * 					GETTERS / SETTERS 					* *
	 * ********************************************************** */

	/**
	 * @return the menu
	 */
	public Menu getMenu() {
		return menu;
	}

	/**
	 * return le noeud graphique d'affichage de l'interface
	 * 
	 * @return
	 */
	public BRootNode getGraphic() {
		return BuiSystem.getRootNode();
	}

	/**
	 * renvoie la traduction local pour cette clef
	 * 
	 * @param key
	 * @return
	 */
	public String getLocalText(String key) {
		return lang.getProperty(key, key+" undifined");
	}

	/**
	 * Envoie un message local avec argument
	 * @param key
	 * @param args
	 * @return
	 */
	public String getLocalText(String key, String... args) {
		String text = getLocalText(key);
		if(args.length%2 == 0) 
			for (int i=0;i<args.length;i+=2)
				text = text.replaceAll(args[i], args[i+1]);
		return text;
	}

	/**
	 * format du text pour un affichage multi lighne
	 * @param description
	 * @return
	 */
	public String format(String str) {
		while (str.contains("\n\n"))
			str = str.replaceAll("\n\n", "\n \n");

		// remplacement des nom de npc
		Matcher m = java.util.regex.Pattern.compile("%npc(\\d+)%").matcher(str);
		TreeSet<Integer> npcs = new TreeSet<Integer>();
		while (m.find()) 
			npcs.add(Integer.parseInt(m.group(1)));
		for (int id : npcs) {
			NonPlayableCharacter npc = getWorld().getNpcBuildIfAbsent(id);
			if (!getWorld().isUpdate(npc))
				Variables.getClientConnecteur().updateFromServerAndWait(npc);
			str = str.replaceAll("%npc"+id+"%", npc.getName());
		}

		str = str.replaceAll("%player%", getWorld().getPlayer().getDisplayName());
		
		str = str.replaceFirst("\n$", "");
		if (str.length()==0)
			return " ";
		
		return str;
	}

	
	/**
	 * TODO commentaire Hud.getWorld
	 * @return
	 */
	public World getWorld() {
		return Variables.getWorld();
	}

	/**
	 * @return the game
	 */
	public LaGame getGame() {
		return game;
	}

	/**
	 * test si la souris est au dessus de l'interface ou non
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isMouseOver(int x, int y) {
		for (BWindow win : BuiSystem.getRootNode().getAllWindows())
			if (win == followedTask && win.isVisible() && followedTask.isMouseOver(x,y))
				return true;				
			else if (win != followedTask && win.isVisible() && win.getHitComponent(x, y) != null)
				return true;
		
		return false;
	}

	/**
	 * test si le hud a le focus (utile pour empeché les event clavier)
	 * @return
	 */
	public boolean hasFocus() {
		for (BWindow win : BuiSystem.getRootNode().getAllWindows())
			if (win.hasFocus())
				return true;
		return chat.getInput().hasFocus();
	}
	
	
	/**
	 * @return the chat
	 */
	public ChatWindow getChat() {
		return chat;
	}

	/**
	 * @return the npcdialogwindow
	 */
	public NpcDailogWindow getDialogWindow() {
		return npcDialog;
	}

	/**
	 * @return
	 */
	public BagWindow getBagWindow() {
		return bags;
	}

	/**
	 * @return
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param b
	 */
	public void setVisible(boolean b) {
		visible = b;
		updateVisibility();
	}

	/**
	 * @return the w
	 */
	public int getW() {
		return w;
	}

	/**
	 * @return the h
	 */
	public int getH() {
		return h;
	}

	/**
	 * renvoie le joueur courant
	 * @return
	 */
	public Player getPlayer() {
		return Variables.getWorld().getPlayer();
	}

	/**
	 * @return the loading
	 */
	public LoadingScreen getLoading() {
		return loading;
	}

	/**
	 * @return the loading
	 */
// pas opérationnelle en v31	
//	public HudScenarizationChargementEnCours getScenarizationLoading() {
//		return scenarization_loading;
//	}
	
	/**
	 * renvoie le livre de quete
	 * @return
	 */
	public SkillsWindow getSkillBook() {
		return skill;
	}

	/**
	 * renvoie le menu principal
	 * @return
	 */
	public MainMenu getMainMenu() {
		return mainMenu;
	}

	/**
	 * populateur par annotation
	 * @return
	 */
	public HudPopulateurAnnotation getPopulus() {
		return populus;
	}

	/**
	 * renvoie l'editeur de buts
	 * @return
	 */
// pas opérationnelle en v31	
//	public HudScenarizationBut getEditeurDeButs(){
//		return editeurDeButs;
//	}


	/**
	 * renvoie l'editeur d'actions
	 * @return
	 */
// pas opérationnelle en v31	
//	public HudScenarizationAction getEditeurDActions(){
//		return editeurDActions;
//	}
	/**
	 * renvoie les zones d'activités
	 * @return
	 */
// pas opérationnelle en v31		
//	public HudScenarizationDroppableZonesDActivites getZonesDActivites(){
//		return this.zonesActivites;
//	}
	/**
	 * renvoie les éléments d'activités
	 * @return
	 */
// pas opérationnelle en v31		
//	public HudScenarizationDroppableElementsDActivite getElementsDActivites(){
//		return this.elementsActivites;
//	}

	/**
	 * @return the groupeditor
	 */
	public GroupEditor getGroupeditor() {
		return groupeditor;
	}

	/**
	 * @return the playerEditor
	 */
	public PlayerEditor getPlayerEditor() {
		return playerEditor;
	}
// pas opérationnelle en v31		
//	public void showScenarizationWindows()
//	{
//		this.editeurDeButs.setVisible(true);
//		this.zonesActivites.setVisible(true);
//		
//		this.editeurDActions.setVisible(true);
//		this.elementsActivites.setVisible(true);
//	}
//	
//	public void hideScenarizationWindows()
//	{
//		this.editeurDeButs.setVisible(false);
//		this.zonesActivites.setVisible(false);
//		
//		this.editeurDActions.setVisible(false);
//		this.elementsActivites.setVisible(false);
//	}

	/**
	 * renvoie la fenetre d'edition de quete (en swig car sinon trop relou)
	 * @return
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 */
// pas opérationnelle en v31		
//	public QuestEditorFrame getQuestEditor() throws MalformedURLException, RemoteException, NotBoundException {
//		if (questeditor == null) 
//			questeditor = new QuestEditorFrame(new Quest(game.getRmiEditor(),getPlayer().getX(),getPlayer().getZ()),true);
//		return questeditor;
//	}



}
