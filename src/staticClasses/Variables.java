/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package staticClasses;

import dynamicLoad.Main;

/**
 *
 * @author admin
 */
public class Variables {
    
    public static Main main;
    
    public static de.lessvoid.nifty.controls.Console console;
    public static Main getMain()
    {
        return main;
    }
    
    public static void setMain(Main main)
    {
        Variables.main=main;
    }
    
}
