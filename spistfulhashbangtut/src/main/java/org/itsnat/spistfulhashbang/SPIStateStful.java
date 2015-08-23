
package org.itsnat.spistfulhashbang;

import org.itsnat.spi.SPIMainDocument;
import org.itsnat.spi.SPIStateDescriptor;
import org.itsnat.spi.SPIState;

public abstract class SPIStateStful extends SPIState 
{
    public SPIStateStful(SPIMainDocument spiDoc,SPIStateDescriptor stateDesc,boolean register)
    {
        super(spiDoc,stateDesc,register);
    }

    public abstract void dispose();

}
