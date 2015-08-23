
package org.itsnat.spistfulhsapitut;

import org.itsnat.spi.SPIStateDescriptor;
import org.itsnat.spistful.SPIStateStful;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

public class SPITutStateOverview extends SPIStateStful implements EventListener
{
    protected Element popupElem;
    protected SPITutStateOverviewPopup popup;

    public SPITutStateOverview(SPITutMainDocument spiTutDoc,SPIStateDescriptor stateDesc,boolean showPopup)
    {
        super(spiTutDoc,stateDesc,!showPopup);

        HTMLDocument doc = getItsNatHTMLDocument().getHTMLDocument();
        this.popupElem = doc.getElementById("popupId");
        ((EventTarget)popupElem).addEventListener("click",this,false);

        if (showPopup) showOverviewPopup();
    }

    @Override
    public void dispose()
    {
        if (popup != null) popup.dispose();
        ((EventTarget)popupElem).removeEventListener("click",this,false);
    }

    @Override
    public void handleEvent(Event evt)
    {
        showOverviewPopup();
    }

    public void showOverviewPopup()
    {
        ((EventTarget)popupElem).removeEventListener("click",this,false); // Avoids two consecutive clicks
        this.popup = new SPITutStateOverviewPopup(this);
    }

    public void onDisposeOverviewPopup()
    {
        this.popup = null;
        ((EventTarget)popupElem).addEventListener("click",this,false); // Restores
        spiDoc.registerState(this);
    }
    
}
