package client;
import client.network.SimpleClientConnector;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ChatTextSendEvent;
import de.lessvoid.nifty.controls.ConsoleExecuteCommandEvent;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;
import de.lessvoid.nifty.tools.SizeValue;

import java.util.logging.Level;
import java.util.logging.Logger;
import shared.variables.Variables;
import  de.lessvoid.nifty.controls.Console;

/**
 *
 */
public class MyStartScreen extends AbstractAppState implements ScreenController {

  private Nifty nifty;
  private Application app;
  private Screen screen;
  private boolean firstTime=true;
  /** custom methods */
  public MyStartScreen() {
      //screen= this
   
      
    /** You custom constructor, can accept arguments */
  }
String nextScreen;
  public void startGame(String nextScreen) {
      
      System.out.println("this is start game");
     Variables.console=nifty.getScreen("chatbar").
             findNiftyControl("textfield2",Console.class);
    
       //textfield2
      if(nifty==null)System.out.println("\n\n null");   
      
  //  nifty.gotoScreen(nextScreen);  // switch to another screen
   this.nextScreen=nextScreen;
  // displaySplashScreen();
   //nifty.removeScreen(nextScreen);
  
       if(app==null) System.out.println("app= null");
       LaGame m= Variables.getLaGame();
       //m = th
      m.saySomething();
     //System.out.println("class = ");
   nifty.gotoScreen("chatbar");
   }

  
  public void displaySplashScreen()
  {
       try {
          Thread.sleep(3000);
         
      } catch (InterruptedException ex) {
          Logger.getLogger(MyStartScreen.class.getName()).log(Level.SEVERE, null, ex);
      }
     nifty.removeScreen(nextScreen);
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
  }

  public void onStartScreen() {
  }

  public void onEndScreen() {
  }

  /** jME3 AppState methods */
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    this.app = app;
  }

 
  
  @Override
  public void update(float tpf) {
    if (screen.getScreenId().equals("hud")) {
      Element niftyElement = nifty.getCurrentScreen().findElementByName("score");
      // Display the time-per-frame -- this field could also display the score etc...
      niftyElement.getRenderer(TextRenderer.class).setText((int)(tpf*100000) + ""); 
     
    }
  System.out.println("this is nifty update");
  }
  
  public void connectServer()
  {
  System.out.println("Connection au serveur");
  Variables.setClientConnecteur(new SimpleClientConnector());
  Variables.getClientConnecteur().connect();
  }
  
  
  
 @NiftyEventSubscriber(id="textfield2")
 public void onChatTextSendEvent(String id, ConsoleExecuteCommandEvent event) {
 System.out.println("element with id [" + id + "] "
         + "clicked at [" + event.getCommandLine());
      
     
      Variables.console.output(">J'ai recu sa", Color.BLACK);
     
}
  
  public void disablePanel()
  {
        Variables.console.disable();
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
}
