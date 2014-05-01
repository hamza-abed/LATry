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
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
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
    
     public int pageNumber=0;
     public int pageCourante=1;
    public ArrayList<BufferedImage> images;
    public ArrayList<com.jme3.texture.Image> imagesJME;
    ArrayList<NiftyImage>  imagesNifty;
    AWTLoader loader ;
    ArrayList<Texture> texs;
    public PDFViewer()
    {
    imagesNifty=new ArrayList<NiftyImage>();
    loader =new AWTLoader();
    images=new ArrayList<BufferedImage>();
    imagesJME=new ArrayList<com.jme3.texture.Image>();
    texs=new ArrayList<Texture>();
    toImages();
        System.out.println("pageNumber=  "+pageNumber);
    }
    
 
    
    
    
    
    
    
 public void ouvrirPDF()
 {
   

Variables.getNifty().getScreen("pdfReaderScreen").findElementByName("pdfPage").getRenderer(ImageRenderer.class).setImage(imagesNifty.get(0));

   Variables.getNifty().getScreen("pdfReaderScreen").
           findNiftyControl("numeroPage", TextField.class).setText(pageCourante+"/"+pageNumber);
 }
 

 public void predPdfPage()
 {
    if(pageCourante>1)pageCourante--;
Variables.getNifty().getScreen("pdfReaderScreen").findElementByName("pdfPage").
        getRenderer(ImageRenderer.class).setImage(imagesNifty.get(pageCourante-1));


Variables.getNifty().getScreen("pdfReaderScreen").
        findNiftyControl("numeroPage", TextField.class).setText(pageCourante+"/"+pageNumber);
 }
 
 
 public void suivPdfPage()
 {
 
 if(pageCourante<pageNumber)pageCourante++;
 
Variables.getNifty().getScreen("pdfReaderScreen").findElementByName("pdfPage").
        getRenderer(ImageRenderer.class).setImage(imagesNifty.get(pageCourante-1));

Variables.getNifty().getScreen("pdfReaderScreen").findNiftyControl("numeroPage", TextField.class).setText(pageCourante+"/"+pageNumber);
 }
 
 
 
 public ArrayList<BufferedImage> getImages() {
        return getImages();
    }
 
 
 
 
 

/*
 * il faut une liste d'images qui représente 
 * la totalitée du document
 */
 
 public void toImages() 
{
    System.out.println("size from toImages = "+images.size());
    BufferedImage image=null;
    com.jme3.texture.Image imageJME=null;
    try {
            String sourceDir = path;
            
            File oldFile = new File(sourceDir);
            String fileName = oldFile.getName().replace(".pdf", "");
            if (oldFile.exists()) {

            PDDocument document = PDDocument.load(sourceDir);
            List<PDPage> list = document.getDocumentCatalog().getAllPages();

             pageNumber= 1;
            for (PDPage page : list) {
                image = page.convertToImage();
                images.add(image);
               imageJME=loader.load(image, true);
               imagesJME.add(imageJME);
               /*
                * adding to cash as textures
                */
TextureKey key = new TextureKey("Textures/dirt.jpg",false);

Texture tex = Variables.getLaGame().getAssetManager().loadTexture(key);
tex.setAnisotropicFilter(16);
tex.setMagFilter(Texture.MagFilter.Bilinear.Bilinear);
loader =new AWTLoader();
tex.setImage(imageJME);
((DesktopAssetManager)Variables.getLaGame().getAssetManager()).addToCache(new TextureKey("pippo"+pageNumber), tex);

texs.add(tex);

try{
  
imagesNifty.add(Variables.getNifty().createImage("pippo"+pageNumber, false));

}
catch (Exception e)
{
System.out.println(e.getMessage());
}

pageNumber++;
            }
            document.close();

        } else {
            System.err.println(fileName +"File not exists");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
  pageNumber--;
}
 
 
 
 
 
 
 
 
  
    public void setImages(ArrayList<BufferedImage> images) {
        this.images = images;
    }
}
