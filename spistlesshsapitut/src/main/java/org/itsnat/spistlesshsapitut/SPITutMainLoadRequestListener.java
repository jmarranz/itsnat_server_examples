
package org.itsnat.spistlesshsapitut;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.ItsNatServletResponse;
import org.itsnat.core.event.ItsNatServletRequestListener;
import org.itsnat.core.http.ItsNatHttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServletResponse;
import org.itsnat.spi.SPIMainDocumentConfig;
import org.itsnat.spi.SPIStateDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLTitleElement;

public class SPITutMainLoadRequestListener implements ItsNatServletRequestListener
{
    @Override
    public void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {
        Document doc = request.getItsNatDocument().getDocument();
        
        SPIMainDocumentConfig config = new SPIMainDocumentConfig();
        config.setTitleElement((HTMLTitleElement)doc.getElementById("titleId"))
              .setContentParentElement(doc.getElementById("contentParentId"))
              .setGoogleAnalyticsElement(doc.getElementById("googleAnalyticsId"))
              .addMenuElement("overview",doc.getElementById("menuOpOverviewId"))
              .addMenuElement("detail",doc.getElementById("menuOpDetailId"))        
              .addSPIStateDescriptor(new SPIStateDescriptor("overview","Overview",true))
              .addSPIStateDescriptor(new SPIStateDescriptor("overview-popup","Overview Popup",false))                
              .addSPIStateDescriptor(new SPIStateDescriptor("detail","Detail",true))                
              .addSPIStateDescriptor(new SPIStateDescriptor("not_found","Not Found",true))                  
                  // Remember to add fundamental (that is bookmarkable) states also to web.xml
              .setDefaultStateName("overview")
              .setNotFoundStateName("not_found");
               
                
        new SPITutMainDocument((ItsNatHttpServletRequest)request,(ItsNatHttpServletResponse)response,config);        
    }
}
