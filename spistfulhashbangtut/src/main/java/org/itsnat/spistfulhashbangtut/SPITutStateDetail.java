
package org.itsnat.spistfulhashbangtut;

import org.itsnat.spistful.SPIStfulState;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.spi.SPIStateDescriptor;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

public class SPITutStateDetail extends SPIStfulState implements EventListener
{
    protected Element detailMoreLink;
    protected Element detailMoreElem;
    protected boolean inserted = false;
    
    public SPITutStateDetail(SPITutMainDocument spiTutDoc,SPIStateDescriptor stateDesc)
    {
        super(spiTutDoc,stateDesc,true);

        HTMLDocument doc = getItsNatHTMLDocument().getHTMLDocument();
        this.detailMoreLink = doc.getElementById("detailMoreId");
        ((EventTarget)detailMoreLink).addEventListener("click",this,false);
    }

    @Override
    public void dispose()
    {
        ((EventTarget)detailMoreLink).removeEventListener("click",this,false);
    }

    @Override
    public void handleEvent(Event evt)
    {
        if (detailMoreElem == null)
        {
            DocumentFragment frag = spiDoc.loadDocumentFragment("detail.more");
            this.detailMoreElem = ItsNatTreeWalker.getFirstChildElement(frag);
        }

        if (!inserted)
        {
            Element contentParentElem = spiDoc.getContentParentElement();
            contentParentElem.appendChild(detailMoreElem);
            detailMoreLink.setTextContent("Hide");
            this.inserted = true;
        }
        else
        {
            detailMoreElem.getParentNode().removeChild(detailMoreElem);            
            detailMoreLink.setTextContent("More Detail");
            this.inserted = false;
        }
    }
}
