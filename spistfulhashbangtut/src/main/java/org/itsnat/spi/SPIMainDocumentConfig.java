package org.itsnat.spi;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLTitleElement;

/**
 *
 * @author jmarranz
 */
public class SPIMainDocumentConfig
{
    protected char stateNameSeparator = 0;
    protected HTMLTitleElement titleElem; // HTMLTitleElement
    protected Element contentParentElem;
    protected Element googleAnalyticsElem;
    protected Map<String,SPIStateDescriptor> stateMap = new HashMap<String,SPIStateDescriptor>();
    protected Map<String,Element> menuElemMap = new HashMap<String,Element>();
    protected String defaultStateName;
    protected String notFoundStateName;

    public char getStateNameSeparator()
    {
        return stateNameSeparator;
    }

    public SPIMainDocumentConfig setStateNameSeparator(char stateNameSeparator)
    {
        this.stateNameSeparator = stateNameSeparator;
        return this;
    }
    
    public HTMLTitleElement getTitleElement()
    {
        return titleElem;
    }

    public SPIMainDocumentConfig setTitleElement(Element titleElem)
    {
        this.titleElem = (HTMLTitleElement)titleElem;
        return this;
    }

    public Element getContentParentElement()
    {
        return contentParentElem;
    }

    public SPIMainDocumentConfig setContentParentElement(Element contentParentElem)
    {
        this.contentParentElem = contentParentElem;
        return this;
    }

    public Element getGoogleAnalyticsElement()
    {
        return googleAnalyticsElem;
    }

    public SPIMainDocumentConfig setGoogleAnalyticsElement(Element googleAnalyticsElem)
    {
        this.googleAnalyticsElem = googleAnalyticsElem;
        return this;
    }

    public SPIStateDescriptor getSPIStateDescriptor(String stateName)
    {
        return stateMap.get(stateName);
    }

    public SPIMainDocumentConfig addSPIStateDescriptor(SPIStateDescriptor stateDesc)
    {
        SPIStateDescriptor old = stateMap.put(stateDesc.getStateName(),stateDesc);
        if (old != null) throw new RuntimeException("Already registered a state with name: " + stateDesc.getStateName());
        return this;
    }

    public Element getMenuElement(String stateName)
    {
        return menuElemMap.get(stateName);
    }

    public Map<String,Element> getMenuElementMap()
    {
        return menuElemMap;
    }

    public SPIMainDocumentConfig addMenuElement(String stateName,Element menuElem)
    {
        menuElemMap.put(stateName,menuElem);
        return this;
    }

    public String getDefaultStateName()
    {
        return defaultStateName;
    }

    public SPIMainDocumentConfig setDefaultStateName(String defaultStateName)
    {
        this.defaultStateName = defaultStateName;
        return this;
    }

    public String getNotFoundStateName()
    {
        return notFoundStateName;
    }

    public SPIMainDocumentConfig setNotFoundStateName(String notFoundStateName)
    {
        this.notFoundStateName = notFoundStateName;
        return this;
    }

    public SPIMainDocumentConfig check()
    {
        if (stateNameSeparator == 0) throw new RuntimeException("State name list separator is not defined, use any char if you don't need it");
        if (titleElem == null) throw new RuntimeException("Missing titleElement");
        if (contentParentElem == null) throw new RuntimeException("Missing contentParentElement");
        if (googleAnalyticsElem == null) throw new RuntimeException("Missing googleAnalyticsElement");
        for(Map.Entry<String,Element> entry : menuElemMap.entrySet())
        {
            Element menuElem = entry.getValue();
            if (menuElem == null) throw new RuntimeException("Menu element of " + entry.getKey() + " is null");
        }
        // menuElemMap puede estar vacío (?)
        if (defaultStateName == null) throw new RuntimeException("Missing defaultStateName");
        if (notFoundStateName == null) throw new RuntimeException("Missing notFoundStateName");

        if (stateMap.get(defaultStateName) == null) throw new RuntimeException("Missing state declaration for default state: " + defaultStateName);
        if (stateMap.get(notFoundStateName) == null) throw new RuntimeException("Missing state declaration for not found state: " + notFoundStateName);

        return this;
    }
}
