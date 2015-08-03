
package org.itsnat.spitut;

import org.itsnat.core.html.ItsNatHTMLDocument;

public abstract class SPITutState
{
    protected SPITutMainDocument spiTutDoc;

    public SPITutState(SPITutMainDocument spiTutDoc,boolean register)
    {
        this.spiTutDoc = spiTutDoc;

        if (register)
            spiTutDoc.registerState(this);
    }

    public SPITutMainDocument getSPITutMainDocument()
    {
        return spiTutDoc;
    }

    public ItsNatHTMLDocument getItsNatHTMLDocument()
    {
        return spiTutDoc.getItsNatHTMLDocument();
    }

    public abstract void dispose();
    public abstract String getStateTitle();
    public abstract String getStateName();
}
