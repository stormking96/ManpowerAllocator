/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manpowerallocator;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import javax.swing.JTextArea;

/**
 *
 * @author storm
 */
public class PrintTextFile 
{
    private String fileName;
    
    public PrintTextFile(String fileN) throws FileNotFoundException, PrintException, IOException
    { 
        fileName = fileN;
    }

    public void printFile() throws PrintException, IOException, PrinterException
    {
        String line = null; 
        StringBuilder fileText = new StringBuilder();
        try 
        {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            while((line = br.readLine()) != null)
            {
                fileText.append(line);
                fileText.append("\n");
            }
            
            br.close();
        }
        catch(FileNotFoundException ex)
        {
            System.out.println("Unable to open file" + fileName);
        }
        catch(IOException ex)
        {
            System.out.println("Error reading file");
        }
        
        String text = fileText.toString();
        JTextArea textToPrint = new JTextArea(text);
        textToPrint.print();
        
    }
}

