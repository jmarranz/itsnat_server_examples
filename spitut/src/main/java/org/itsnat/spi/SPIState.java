
package org.itsnat.spi;

import org.itsnat.core.html.ItsNatHTMLDocument;

public abstract class SPIState
{
    protected SPIMainDocument spiTutDoc;

    public SPIState(SPIMainDocument spiTutDoc,boolean register)
    {
        this.spiTutDoc = spiTutDoc;

        if (register)
            spiTutDoc.registerState(this);
    }

    public SPIMainDocument getSPIMainDocument()
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
