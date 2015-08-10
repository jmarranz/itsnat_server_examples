
package org.itsnat.spistlesstut;

import org.itsnat.spistless.SPIState;

public class SPITutStateOverview extends SPIState
{
    public SPITutStateOverview(SPITutMainDocument spiTutDoc,boolean showPopup)
    {
        super(spiTutDoc,!showPopup);

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
