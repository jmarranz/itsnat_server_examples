
package org.itsnat.spistlesshashbangtut;

import org.itsnat.spistlesshashbang.SPIState;
import org.itsnat.spistlesshashbang.SPIStateDescriptor;

public class SPITutStateOverview extends SPIState
{
    public SPITutStateOverview(SPITutMainDocument spiTutDoc,SPIStateDescriptor stateDesc,boolean showPopup)
    {
        super(spiTutDoc,stateDesc,!showPopup);

        if (showPopup) showOverviewPopup();
        else cleanOverviewPopup();
    }

    public void showOverviewPopup()
    {
        new SPITutStateOverviewPopup(this);
    }

    public void cleanOverviewPopup()
    {    
        SPITutStateOverviewPopup.dispose(this);
    }

}
