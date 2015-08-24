
package org.itsnat.spistful;

import org.itsnat.spi.SPIMainDocument;
import org.itsnat.spi.SPIStateDescriptor;
import org.itsnat.spi.SPIState;

public abstract class SPIStfulState extends SPIState 
{
    public SPIStfulState(SPIMainDocument spiDoc,SPIStateDescriptor stateDesc,boolean register)
    {
        super(spiDoc,stateDesc,register);
    }

    public abstract void dispose();
}
