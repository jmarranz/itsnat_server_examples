package webgnuplot;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.html.ItsNatHTMLDocument;
/*
 * GnuPlotLoadListener.java
 *
 * Created on 14 de abril de 2008, 9:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author jmarranz
 */
public class GnuPlotLoadListener implements ItsNatServletRequestListener
{
    
    /** Creates a new instance of GnuPlotLoadListener */
    public GnuPlotLoadListener()
    {
    }

    public void processRequest(ItsNatServletRequest itsNatServletRequest, ItsNatServletResponse itsNatServletResponse)
    {
        ItsNatHTMLDocument itsNatDoc = (ItsNatHTMLDocument)itsNatServletRequest.getItsNatDocument();
        new GnuPlotListener(itsNatDoc);
    }
    
}
