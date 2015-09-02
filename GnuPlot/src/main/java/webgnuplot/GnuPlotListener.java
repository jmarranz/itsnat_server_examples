/*
 * GnuPlotListener.java
 *
 * Created on 14 de abril de 2008, 9:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package webgnuplot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.swing.text.BadLocationException;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.button.normal.ItsNatHTMLInputButtonNormal;
import org.itsnat.comp.text.ItsNatHTMLTextArea;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLImageElement;

/**
 *
 * @author jmarranz
 */
public class GnuPlotListener implements ActionListener
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected ItsNatHTMLTextArea textArea;
    protected ItsNatHTMLInputButtonNormal button;
    
    /** Creates a new instance of GnuPlotListener */
    public GnuPlotListener(ItsNatHTMLDocument itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        
        Element commandsElem = doc.getElementById("commandsId");        
        this.textArea = (ItsNatHTMLTextArea)compMgr.createItsNatComponent(commandsElem);
        
        javax.swing.text.Document swingDoc = textArea.getDocument();
        String commands = "";
        commands += "set xrange[-3:7] \n";
        commands += "set yrange[1:5] \n";
        commands += "set isosamples 50 \n";        
        commands += "set hidden3d \n";        
        commands += "splot exp(-0.2*x)*cos(x*y)*sin(y) \n";        
        try { swingDoc.insertString(0,commands,null); } catch(BadLocationException ex){ }    
        // Alternative: textArea.setText(commands);
        
        Element plotElem = doc.getElementById("plotId");
        this.button = (ItsNatHTMLInputButtonNormal)compMgr.createItsNatComponent(plotElem);
        button.getButtonModel().addActionListener(this);                
        
        setImage(commands);
    }

    public void actionPerformed(ActionEvent e)
    {
        javax.swing.text.Document swingDoc = textArea.getDocument();
        String commands = ""; 
        try { commands = swingDoc.getText(0,swingDoc.getLength()); } catch(BadLocationException ex){ }
        // Alternative: String commands = textArea.getText();  
        setImage(commands);
    }
    
    public void setImage(String commands)
    {
        HTMLImageElement img = (HTMLImageElement)itsNatDoc.getDocument().getElementById("plotImageId");
        try{ commands = URLEncoder.encode(commands,"UTF-8"); } catch(UnsupportedEncodingException ex) {}
        img.setSrc("imgservlet?commands=" + commands);
    }
}
