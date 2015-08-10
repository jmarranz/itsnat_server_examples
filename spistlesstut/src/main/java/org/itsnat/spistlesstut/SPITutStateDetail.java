
package org.itsnat.spistlesstut;

import org.itsnat.core.ClientDocument;
import org.itsnat.core.domutil.ItsNatTreeWalker;
import org.itsnat.spistless.SPIState;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

public class SPITutStateDetail extends SPIState
{
    public SPITutStateDetail(SPITutMainDocument spiTutDoc,String stateSecondaryName)
    {
        super(spiTutDoc,true);
        
        HTMLDocument doc = getItsNatHTMLDocument().getHTMLDocument();            
        Element detailMoreLink = doc.getElementById("detailMoreId");        
        
        if ("moreDetail".equals(stateSecondaryName))
        {
            DocumentFragment frag = spiTutDoc.loadDocumentFragment("detail.more");
            Element detailMoreElem = ItsNatTreeWalker.getFirstChildElement(frag);        
            detailMoreElem.setAttribute("id", "detailContentId");            
            
            Element contentParentElem = spiTutDoc.getContentParentElement();
            contentParentElem.appendChild(detailMoreElem);
            detailMoreLink.setTextContent("Hide");
            detailMoreLink.setAttribute("action","lessDetail");
        }
        else
        {
            ClientDocument clientDoc = getItsNatHTMLDocument().getClientDocumentOwner();
            clientDoc.addCodeToSend("window.spiSite.removeById('detailContentId');");
                      
            detailMoreLink.setTextContent("More Detail");   
            detailMoreLink.setAttribute("action","moreDetail");            
        }
    }

    @Override
    public String getStateTitle()
    {
        return "Detail";
    }

    @Override
    public String getStateName()
    {
        return "detail";
    }

}
