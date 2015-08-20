
package org.itsnat.spi;

/**
 *
 * @author jmarranz
 */
public class SPIStateDescriptor
{
    protected final String stateName;
    protected final String stateTitle;
    protected final boolean mainLevel;

    public SPIStateDescriptor(String stateName,String stateTitle,boolean mainLevel)
    {
        this.stateName = stateName;
        this.stateTitle = stateTitle;
        this.mainLevel = mainLevel;
    }

    public String getStateName()
    {
        return stateName;
    }

    public String getStateTitle()
    {
        return stateTitle;
    }

    public boolean isMainLevel()
    {
        return mainLevel;
    }
}
