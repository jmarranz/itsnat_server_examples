
package org.itsnat.spistlesstut;

import org.itsnat.spistless.SPIState;
import org.itsnat.spistless.SPIStateDescriptor;

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
