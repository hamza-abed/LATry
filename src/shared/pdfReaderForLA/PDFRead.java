/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.pdfReaderForLA;

import java.io.File;
import java.io.FileInputStream;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
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
    
}
