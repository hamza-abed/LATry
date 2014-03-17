package dynamicLoad;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class MyStartScreen extends AbstractAppState implements ScreenController {

  private Nifty nifty;
  private Application app;
  private Screen screen;

  /** custom methods */
  public MyStartScreen() {
    /** You custom constructor, can accept arguments */
  }
String nextScreen;
  public void startGame(String nextScreen) {
    nifty.gotoScreen(nextScreen);  // switch to another screen
   this.nextScreen=nextScreen;
  // displaySplashScreen();
   nifty.removeScreen(nextScreen);
    
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
   
  }
}
