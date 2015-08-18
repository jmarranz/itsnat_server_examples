
package org.itsnat.spistlesshashbang;

import org.itsnat.core.html.ItsNatHTMLDocument;

public abstract class SPIState
{
    protected SPIMainDocument spiDoc;
    protected SPIStateDescriptor stateDesc;
    
    public SPIState(SPIMainDocument spiDoc,SPIStateDescriptor stateDesc,boolean register)
    {
        this.spiDoc = spiDoc;
        this.stateDesc = stateDesc;
        
        if (register)
            spiDoc.registerState(this);
    }

    public SPIMainDocument getSPIMainDocument()
    {
        return spiDoc;
    }

    public ItsNatHTMLDocument getItsNatHTMLDocument()
    {
        return spiDoc.getItsNatHTMLDocument();
    }

    public String getStateName()
    {
        return stateDesc.getStateName();
    }
    
    public String getStateTitle()
    {
        return stateDesc.getStateTitle();
    }  
    
}
