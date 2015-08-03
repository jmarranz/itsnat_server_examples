
package org.itsnat.spitut;

public class SPITutStateOverview extends SPITutState
{
    public SPITutStateOverview(SPITutMainDocument spiTutDoc,boolean popup)
    {
        super(spiTutDoc);

        if (popup) showOverviewPopup();
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
