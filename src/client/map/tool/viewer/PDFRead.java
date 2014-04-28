/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.map.tool.viewer;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import jme3tools.savegame.SaveGame;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 *
 * @author admin
 */
public class PDFRead {
    
    private String path;
    
    public PDFRead(String path)
    {
        this.path=path;
        images=new ArrayList<BufferedImage>();
        System.out.println("\n \n ********* instantiation ******** \n \n");
    }
    
    
    public String transformToText()
    {
    PDFParser parser = null;
    PDDocument pdDoc = null;
    COSDocument cosDoc = null;
    PDFTextStripper pdfStripper;

    String parsedText="";
    String fileName = path;
    File file = new File(fileName);
    try {
       
        parser = new PDFParser(new FileInputStream(file));
        parser.parse();
        cosDoc = parser.getDocument();
        pdfStripper = new PDFTextStripper();
        pdDoc = new PDDocument(cosDoc);
        parsedText = pdfStripper.getText(pdDoc);
        parser.clearResources();
        
        //System.out.println(parsedText);
        //System.out.println(parsedText.replaceAll("[^A-Za-z0-9. ]+", ""));
    } catch (Exception e) {
        e.printStackTrace();
        try {
            if (cosDoc != null)
                cosDoc.close();
            if (pdDoc != null)
                pdDoc.close();
        } catch (Exception e1) {
            e.printStackTrace();
        }

    }
    return parsedText;
    }
    

    
 private ArrayList<BufferedImage> images;

   
 private int pageNumber=0;
 private int pageCourante=1;
   
    

public BufferedImage toImage() //getting the last image
{
   toImages();
    System.out.println("buffer size="+images.size());
   return images.get(0);
}

/*
 * il faut une liste d'images qui représente 
 * la totalitée du document
 */
 
 public void toImages() 
{
    System.out.println("size from toImages = "+images.size());
    BufferedImage image=null;
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
              
               
                pageNumber++;
            }
            document.close();

        } else {
            System.err.println(fileName +"File not exists");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
  
}
 
 
 public BufferedImage nextPdfPage()
 {
     System.out.println("\n \n "
             + "pageNumber= "+images.size()+" \n \n");
     
     if(pageCourante<images.size())
         pageCourante++;
     return images.get(pageCourante-1);
 }

 
 public BufferedImage predPdfPage()
 {
     if(pageCourante>1)
         pageCourante--;
     return images.get(pageCourante-1);
 }
 
 
 
  public ArrayList<BufferedImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<BufferedImage> images) {
        this.images = images;
    }
 
}