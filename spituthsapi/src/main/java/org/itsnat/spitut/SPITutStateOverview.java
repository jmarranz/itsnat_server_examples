
package org.itsnat.spitut;

import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

public class SPITutStateOverview extends SPITutState implements EventListener
{
    protected Element popupElem;
    protected SPITutStateOverviewPopup popup;

    public SPITutStateOverview(SPITutMainDocument spiTutDoc,boolean popup)
    {
        super(spiTutDoc);

        HTMLDocument doc = getItsNatHTMLDocument().getHTMLDocument();
        this.popupElem = doc.getElementById("popupId");
        ((EventTarget)popupElem).addEventListener("click",this,false);

        if (popup) showOverviewPopup();
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
        spiTutDoc.registerState(this);
    }
    
    @Override
    public String getStateTitle()
    {
        return "Overview";
    }

    @Override
    public String getStateName()
    {
        return "overview";
    }
}
