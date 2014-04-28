/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.pdfReaderForLA;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
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
    


public BufferedImage toImage() //getting the last image
{
    BufferedImage image=null;
    try {
            String sourceDir = "C:\\classes.pdf";
            String destinationDir = "C:\\test";
            File oldFile = new File(sourceDir);
            String fileName = oldFile.getName().replace(".pdf", "");
            if (oldFile.exists()) {

            PDDocument document = PDDocument.load(sourceDir);
            List<PDPage> list = document.getDocumentCatalog().getAllPages();

            int pageNumber= 1;
            for (PDPage page : list) {
                image = page.convertToImage();
                
              //  File outputfile = new File("Interface/imgs/img1_"+ pageNumber+".png");
               // ImageIO.write(image, "png", outputfile);
               
                pageNumber++;
            }
            document.close();

        } else {
            System.err.println(fileName +"File not exists");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return image;
}

}
