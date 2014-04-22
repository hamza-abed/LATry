package client;
import client.network.SimpleClientConnector;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ConsoleExecuteCommandEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
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
import de.lessvoid.nifty.controls.label.LabelControl;
import shared.pdfReaderForLA.PDFRead;
import shared.utils.PropertyReader;

/**
 *
 */
public class NGUI_LA extends AbstractAppState implements ScreenController {

  private Nifty nifty;
  private Application app;
  private Screen screen;
  private boolean firstTime=true;
  /** custom methods */
  public NGUI_LA() {
      //screen= this
   
      
    /** You custom constructor, can accept arguments */
  }
String nextScreen;


  public void startGame(String nextScreen) {
      
      System.out.println("this is start game");
     Variables.setConsole(nifty.getScreen("chatbar").
      findNiftyControl("textfield2",Console.class));
    
    
       //textfield2
      if(nifty==null)System.out.println("\n\n null");   
      
  //  nifty.gotoScreen(nextScreen);  // switch to another screen
   this.nextScreen=nextScreen;
  // displaySplashScreen();
   //nifty.removeScreen(nextScreen);
  
       if(app==null) System.out.println("app= null");
       LaGame m= Variables.getLaGame();
       //m = th
      // m.saySomething();
     //System.out.println("class = ");
       nifty.gotoScreen("chatbar");
   
   connectServer();
   Variables.getLaGame().initGameWold();
   
   }

  
  public void displaySplashScreen()
  {
       try {
          Thread.sleep(3000);
         
      } catch (InterruptedException ex) {
          Logger.getLogger(NGUI_LA.class.getName()).log(Level.SEVERE, null, ex);
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
 public void fillListBox() {
    
    
    DropDown dropDown = nifty.getCurrentScreen().findNiftyControl("myListBox", DropDown.class);
    if(dropDown!=null)
    {
    client=new SimpleClientConnector();
    for (int i=0;i<PropertyReader.getInt(client.getProps(),"server.count", 1);i++) 
     dropDown.addItem(client.getProps().getProperty("server."+i+".name", "Server inconnu : "+i));
    }
    else System.out.println("it s null");
  }
  public void onStartScreen() {
      if(nifty.getCurrentScreen().getScreenId().equals("start"))
      {
         // nifty.getCurrentScreen().findNiftyControl("txtf_login", TextField.class).setText("hello");
          System.out.println("this is start ");
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
  {
      
      /// this about connection to the Red Dwarf server
      
  String login=nifty.getCurrentScreen().findNiftyControl("txtf_login", TextField.class).getText();
  String pass=nifty.getCurrentScreen().findNiftyControl("txtf_pass", TextField.class).getText();
  System.out.println("Connection au serveur  -- login="+login+"  pass="+pass);
  
  client.connect(login, pass);
  
 // pdfViewer.enable();
 //nifty.gotoScreen("LACorePDFReader");

  }
  
  public void showText()
  {
      String text=new PDFRead("C:\\classes.pdf").transformToText();
      nifty.getScreen("LACorePDFReader").
      //findElementByName("PDFViewer").getRenderer(TextRenderer.class).setText(text);
    findNiftyControl("PDFViewer",Label.class).setText("hello");
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
}
