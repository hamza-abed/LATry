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

import client.gui.controllers.ProgressbarControl;
import client.map.tool.viewer.PDFViewer;
import client.network.SimpleClientConnector;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Spatial;
import com.sun.pdfview.PDFFile;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ConsoleExecuteCommandEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;

import java.util.logging.Level;
import java.util.logging.Logger;
import shared.variables.Variables;
import  de.lessvoid.nifty.controls.Console;
import de.lessvoid.nifty.controls.Label;

import  de.lessvoid.nifty.controls.TextField;
import  de.lessvoid.nifty.controls.DropDown;
import de.lessvoid.nifty.controls.Window;
import de.lessvoid.nifty.controls.WindowClosedEvent;
import java.awt.Desktop;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.Callable;

import shared.utils.PropertyReader;

/**
 * Cette classe se chage d'afficher l'interface graphique du jeu,
 * faire une liaison forte entre le code java et l'interface GUI 
 * en se basant esssentiellement sur les librairies de Nifty GUI
 * 
 * @author Hamza ABED 2014 hamza.abed.professionel@gmail.com
 */
public class NGUI_LA extends AbstractAppState implements ScreenController {

  private Nifty nifty;
  private Application app;
  private Screen screen;
  private boolean firstTime=true;
  /** custom methods */
  public NGUI_LA() {
      
    /** You custom constructor, can accept arguments */
  }
String nextScreen;

private float progressing=0.0f;

public void progress()
{
 if(Variables.isProgressBarBound())  
 {
     System.out.println("Progressing="+progressing);
    progressing+=0.3f;
    nifty.getScreen("start").findControl("loader", ProgressbarControl.class).setProgress(progressing);
 }
 else
     System.out.println("Progress bar not bound yet!!!");
   
 
}

private boolean canStartLoading=false;
public void startloadingTheGame()
{
    System.out.println("start loading the game !!");
 
   
    
     //nifty.getScreen("start").findElementByName("GameTitle").setVisible(false);
     nifty.getScreen("start").findElementByName("user").setVisible(false);
     nifty.getScreen("start").findElementByName("password").setVisible(false);
     nifty.getScreen("start").findElementByName("serverSelection").setVisible(false);
     nifty.getScreen("start").findElementByName("connectionStatus").setVisible(false);
     nifty.getScreen("start").findElementByName("btnContinue").setVisible(false);
     nifty.getScreen("start").findElementByName("btnQuit").setVisible(false);
     nifty.getScreen("start").findElementByName("txtf_login").setVisible(false);
     nifty.getScreen("start").findElementByName("txtf_pass").setVisible(false);
     nifty.getScreen("start").findElementByName("selectServer").setVisible(false);
      
     nifty.getScreen("start").findElementByName("loader").setVisible(true);
         
}
  public void startGame() {
     /*
       * Verification de la connection
       */
      if(Variables.getConnectionStatusLabel()==null)
          Variables.setConnectionStatusLabel(nifty.getScreen("start").findNiftyControl("connectionStatus",Label.class));
      
 ///on a besoin de lancer un autre thread ici
        client.setConnecting(true);
         connectServer();
/* Variables.getTaskExecutor().execute(new Runnable() {
   @Override
   public void run() {
  */ 
   
                     
      

  
   
   }
int callThread=0;
 public void movetoGameScreen()
 {
     callThread++;
     Variables.setConsole(nifty.getScreen("chatbar").
             findNiftyControl("textfield2", Console.class));
     System.out.println("This is calling find way niftyCallThread=" + callThread + " threadName=" + Thread.currentThread().getName());
        Variables.getLaGame().enqueue(
                 new Callable<Void>() {
             @Override
             public Void call() {
                 // Variables.getLaGame().getSchedulerTaskExecutor().submit(Variables.getLaGame().getFindWay());
                 System.out.println("This is calling find way from nifty " + Thread.currentThread().getName());
                 if (Variables.isPlayerModelLoaded())
                 Variables.getLaGame().initGameWorld();
                 else 
                     try {
             Thread.sleep(100);
             Variables.getClientConnecteur().callMoveToGameScreen();
         } catch (Exception ex) {
         }
                 return null;
             }
         });
    


     nifty.gotoScreen("chatbar");
     // Variables.getLaGame().initGameWorld();
 }
 
  
  public void quitGame() {
    app.stop();
  }

  public String getPlayerName() {
    return System.getProperty("user.name");
  }

  /** Nifty GUI ScreenControl methods */
  public void bind(Nifty nifty, Screen screen) {
      
     
    this.nifty = nifty;
    this.screen = screen;
    Variables.setNifty(nifty);
   
  }
  
  
  /*
   * Cette méthode remplit la listBox pour 
   * la sélection d'un serveur
   */
 public void fillListBox() {
    
    
    DropDown dropDown = nifty.getCurrentScreen().findNiftyControl("selectServer", DropDown.class);
    if(dropDown!=null)
    {/*
     * Une fois la classe SimpleClientConnector est instancié 
     * elle est automatiquement affecté dans la calsse statique Variables
     */
    client=Variables.getClientConnecteur();
    for (int i=0;i<PropertyReader.getInt(client.getProps(),"server.count", 1);i++) 
     dropDown.addItem(client.getProps().getProperty("server."+i+".name", "Server inconnu : "+i));
    }
    else System.out.println("it s null");
  }
  
 
 public void onStartScreen() {
      if(nifty.getCurrentScreen().getScreenId().equals("start"))
      {
         // nifty.getCurrentScreen().findNiftyControl("txtf_login", TextField.class).setText("hello");
          //System.out.println("this is start ");
          fillListBox();
      }
      
  }

  public void onEndScreen() {
  }

  /** jME3 AppState methods */
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
  //  this.app = app;
      super.initialize(stateManager, app);
  }

 
  
  
  
  @Override
  public void update(float tpf) {
   
  System.out.println("this is nifty update");
  }
  
  SimpleClientConnector client;
   
  public void connectServer()
  {Variables.setNiftyGUI(this);
      
      System.err.println("This is connect server");
      /// this about connection to the Red Dwarf server
      
  String login=nifty.getCurrentScreen().findNiftyControl("txtf_login", TextField.class).getText();
  String pass=nifty.getCurrentScreen().findNiftyControl("txtf_pass", TextField.class).getText();
  
  
  int index=nifty.getCurrentScreen().findNiftyControl("selectServer", DropDown.class).getSelectedIndex();
  System.out.println("Connection au serveur  -- login="+login+"  pass="+pass+" index="+index);
  
  if(index==-1)index=0; 
     client.connect(login, pass,index);
      

  }
  
 
  
 @NiftyEventSubscriber(id="textfield2")
 public void onChatTextSendEvent(String id, ConsoleExecuteCommandEvent event) {
 System.out.println("element with id [" + id + "] "
         + "clicked at [" + event.getCommandLine());
      
     
      Variables.getConsole().output(">J'ai recu sa", Color.BLACK);
     
}
 
 
 


  public void disablePanel()
  {
        Variables.getConsole().disable();
  }
  
  
  private boolean chatBarHidden=false;
  public void hideChatBar(String ch, String btn)
  {
      
      Element btnClicked=nifty.getCurrentScreen().findElementByName("edit_tabs");
     
      if(ch.equals("true"))
      {
      //Variables.console.output("this is hide chatBar");
      
    chatBarHidden=true;
      nifty.gotoScreen("chatbarHidden");
      
      }
      else
      {
          chatBarHidden=false;
          nifty.gotoScreen("chatbar");
          
      }
    Variables.getLaGame().gainFocus();
      //btnClicked.disableFocus();
      //btnClicked.enable();
      //nifty.getCurrentScreen().findElementByName("btnReduce").disableFocus();
      //nifty.getCurrentScreen().findElementByName("btnRetour").disableFocus();
    System.out.println("focus is on \n\n\n\n"+
           nifty.getCurrentScreen().getDefaultFocusElementId()+"\n\n\n");
    //Variables.getLaGame().restart();
      
   // nifty.getClipboard().
      
  }
  
 /*
  * Ceci pour ouvrir des liens sur
  * un navigateur web
  * (des videos)
  */
  
  
  public static void openWebpage(String urlString) {
    try {
        Desktop.getDesktop().browse(new URL(urlString).toURI());
    } catch (Exception e) {
        e.printStackTrace();
    }
}
  
  
        /*********                                                  **********
         ***** Cette partie est conçus pour tester l'ouverture d'un pdf **********
         *********                                                  **********
        */
  
  
  
         
         /**
	 * Charge le fichier PDF, le télécharge s'il n'est pas en local
	 */
  private static final Logger logger = Logger.getLogger("NGUI_LA");
  PDFFile pdffile;
	
  
  private void openPDF(File f) {
		//logger.entering(ViewerEngine.class.getName(), "openPDF");

		if (pdffile == null)
			try {
				// load the pdf from a byte buffer
				RandomAccessFile raf = new RandomAccessFile(f, "r");
				logger.log(Level.INFO, "ouverture PDF{0}", f.getPath());
				FileChannel channel = raf.getChannel();
				ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0,
						channel.size());
				pdffile = new PDFFile(buf);
				// ajout pp
				raf.close();
			} catch (Exception e) {
				/*new PopupWindow("Visualisateur",
						"Erreur lors de l'ouverture de " + f.getName(), "OK",
						false, PopupIcon.warning); */
			}

			logger.exiting(NGUI_LA.class.getName(), "openPDF");
	}
  
  
  public void autreTaches()
  {
      /*
       * Ceci execute un bout de code pour afficher un PDF sur
       * le Plan 2D du jeu, en le transformant en image et puis l'appliquant 
       * comme texture sur un cube
       */
      Variables.getLaGame().showPDF();
     
}
  
  
  
  /*                                            ********************
   * Ceci est conçu pour l'affichage d'un PDF     ******************
   */                                                          
 public void showPDFBrowser()
 {
     
    nifty.gotoScreen("pdfReaderScreen"); 
    
  //  WindowControl w = nifty.getScreen("pdfReaderScreen").findNiftyControl("PDFWindow", WindowControl.class);
    
    Element z = nifty.getScreen("pdfReaderScreen").findElementByName("PDFWindow");
    z.setVisible(!z.isVisible());
    setNiftyImage();
    
   
 }
 
 
 
 
 
 
  @NiftyEventSubscriber(id="PDFWindow")
 public void onPDFWindowClose(String id, WindowClosedEvent event) {
 System.out.println("element with id [" + id + "] "
         + "clicked at [" + event.toString());
      nifty.getScreen("pdfReaderScreen").resetLayout();
    nifty.gotoScreen("chatbar");
     nifty.getScreen("pdfReaderScreen").findNiftyControl("PDFWindow", Window.class).closeWindow();
    System.out.println("window closed");
    
    
}
  
  public PDFViewer pdfViewer;
 
private void setNiftyImage() {
   System.out.println("Instantiation PDFViewer"); 
 pdfViewer=new PDFViewer();
pdfViewer.ouvrirPDF();
    

}


public void nextPagePDF()
{
  //  System.out.println("traitement fait!!");
    if(pdfViewer!=null)
    pdfViewer.suivPdfPage();
    else 
    {pdfViewer=new PDFViewer();
        System.out.println("something is happening !!");
        
    pdfViewer.suivPdfPage();
    }

    
}


public void prevPagePDF()
{
    pdfViewer.predPdfPage();
}


}
