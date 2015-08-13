
package org.itsnat.spistfulhsapi;

import org.itsnat.core.html.ItsNatHTMLDocument;

public abstract class SPIState
{
    protected SPIMainDocument spiTutDoc;
    protected SPIStateDescriptor stateDesc;
    
    public SPIState(SPIMainDocument spiTutDoc,SPIStateDescriptor stateDesc,boolean register)
    {
        this.spiTutDoc = spiTutDoc;
        this.stateDesc = stateDesc;
        
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

    public String getStateName()
    {
        return stateDesc.getStateName();
    }
    
    public String getStateTitle()
    {
        return stateDesc.getStateTitle();
    }  
    
    public abstract void dispose();

}
