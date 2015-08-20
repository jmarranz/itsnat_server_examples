package org.itsnat.spistlesshashbangtut;

import org.itsnat.spistlesshashbang.SPIMainDocument;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.layer.ItsNatModalLayer;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.spistlesshashbang.SPIState;
import org.itsnat.spi.SPIStateDescriptor;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLBodyElement;
import org.w3c.dom.html.HTMLDocument;

public class SPITutStateOverviewPopup extends SPIState
{
    protected SPITutStateOverview parent;
    protected ItsNatModalLayer layer;

    public SPITutStateOverviewPopup(SPITutStateOverview parent)
    {
        this(parent,parent.getSPIMainDocument().getSPIStateDescriptor("overview.popup"));    
    }
    
    public SPITutStateOverviewPopup(SPITutStateOverview parent,SPIStateDescriptor stateDesc)
    {
        super(parent.getSPIMainDocument(),stateDesc,true);
        this.parent = parent;    

        SPIMainDocument spiTutDoc = getSPIMainDocument();
        ItsNatHTMLDocument itsNatDoc = getItsNatHTMLDocument();
        HTMLDocument doc = itsNatDoc.getHTMLDocument();
        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        ItsNatModalLayer layer = compMgr.createItsNatModalLayer(null,false,1,0.5f,"black",null);
        Element parentLayer = layer.getElement();
        parentLayer.setAttribute("id","overviewPopupLayerContainerId");
        
        HTMLBodyElement body = (HTMLBodyElement)doc.getBody();

        DocumentFragment frag = spiTutDoc.loadDocumentFragment("overview.popup");
        Element container = ItsNatTreeWalker.getFirstChildElement(frag);
        body.appendChild(container);

        container.setAttribute("id","overviewPopupContentContainerId");        
        
        itsNatDoc.addCodeToSend("try{ window.scroll(0,-1000); }catch(ex){}");
        // try/catch is used to prevent some mobile browser does not support it
    }

    public static void dispose(SPITutStateOverview parent)
    {
        ClientDocument clientDoc = parent.getItsNatHTMLDocument().getClientDocumentOwner();
        clientDoc.addCodeToSend("removeById('overviewPopupLayerContainerId');");
        clientDoc.addCodeToSend("removeById('overviewPopupContentContainerId');"); 
    }

}
