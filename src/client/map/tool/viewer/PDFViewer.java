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
package client.map.tool.viewer;

import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.texture.Texture;
import com.jme3.texture.plugins.AWTLoader;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import java.awt.image.BufferedImage;
import shared.variables.Variables;

/**
 *
 * @author Hamza ABED 2014 hamza.abed.professionel@gmail.com
 */

/*
 * <ul>
 * <li>Cette classe servera à aficher un PDF dans une fenêtre Nifty</li>
 * <li>elle contion toutes les méthodes pour la gestion du PDF </li>
 * <li>En tanque moteur de PDF elle génère une texture pour JME3</li>
 * <li>En tanque moteur de PDF elle génère une image Nifty pour l'afficher dans un window </li>
 * </ul>
 */
public class PDFViewer {
    
    String path="C:\\classes.pdf";
    private PDFRead pdfRead;
    
    
    public PDFViewer()
    {
        pdfRead=new PDFRead(path);
    
    }
    
 
    
    
    
    
    
    
 public void ouvrirPDF()
 {

BufferedImage image=pdfRead.toImage();  
TextureKey key = new TextureKey("Textures/dirt.jpg",false);
tex = Variables.getLaGame().getAssetManager().loadTexture(key);
tex.setAnisotropicFilter(16);
tex.setMagFilter(Texture.MagFilter.Bilinear.Bilinear);
AWTLoader loader =new AWTLoader();
com.jme3.texture.Image imageJME=loader.load(image, true);;

tex.setImage(imageJME);

((DesktopAssetManager)Variables.getLaGame().getAssetManager()).addToCache(new TextureKey("pippo"), tex);

NiftyImage img2 = null;
try{
img2 = Variables.getNifty().createImage("pippo", false);
}
catch (Exception e)
{
System.out.println(e.getMessage());
}
Variables.getNifty().getScreen("pdfReaderScreen").findElementByName("pdfPage").getRenderer(ImageRenderer.class).setImage(img2);

   
 }
 
 private Texture tex;
 public void predPdfPage()
 {
     BufferedImage image=pdfRead.predPdfPage();
             

AWTLoader loader =new AWTLoader();
com.jme3.texture.Image imageJME=loader.load(image, true);;

tex.setImage(imageJME);

((DesktopAssetManager)Variables.getLaGame().getAssetManager()).addToCache(new TextureKey("pred"), tex);

NiftyImage img2 = null;
try{
img2 = Variables.getNifty().createImage("pred", false);
}
catch (Exception e)
{
System.out.println(e.getMessage());
}
Variables.getNifty().getScreen("pdfReaderScreen").findElementByName("pdfPage").getRenderer(ImageRenderer.class).setImage(img2);
 }
 
 
 public void suivPdfPage()
 {
     System.out.println("\n \n page suivante");
 BufferedImage image=pdfRead.nextPdfPage();
AWTLoader loader =new AWTLoader();
com.jme3.texture.Image imageJME=loader.load(image, true);;

tex.setImage(imageJME);

((DesktopAssetManager)Variables.getLaGame().getAssetManager()).
                       addToCache(new TextureKey("suiv"), tex);

NiftyImage img2 = null;
try{
img2 = Variables.getNifty().createImage("suiv", false);
}
catch (Exception e)
{
System.out.println(e.getMessage());
}
System.out.println("\n \n jusqu ici");
Variables.getNifty().getScreen("pdfReaderScreen").findElementByName("pdfPage").getRenderer(ImageRenderer.class).setImage(img2);
 }
 
}
