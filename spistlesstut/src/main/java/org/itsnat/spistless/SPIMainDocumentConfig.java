package org.itsnat.spistless;

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
    protected HTMLTitleElement titleElem; // HTMLTitleElement 
    protected Element contentParentElem;
    protected Element googleAnalyticsElem;    
    protected Map<String,Element> menuElemMap = new HashMap<String,Element>();    
    protected String defaultStateName;
    protected String notFoundStateName;
    
    public SPIMainDocumentConfig setTitleElement(Element titleElem)
    {
        this.titleElem = (HTMLTitleElement)titleElem;
        return this;
    }
    
    public SPIMainDocumentConfig setContentParentElement(Element contentParentElem)
    {
        this.contentParentElem = contentParentElem;
        return this;
    }
    
    public SPIMainDocumentConfig setGoogleAnalyticsElement(Element googleAnalyticsElem)
    {
        this.googleAnalyticsElem = googleAnalyticsElem;
        return this;
    }    
    
    public SPIMainDocumentConfig addMenuElement(String stateName,Element menuElem)
    {
        menuElemMap.put(stateName,menuElem);
        return this;
    }   
    
    public SPIMainDocumentConfig setDefaultStateName(String defaultStateName)
    {
        this.defaultStateName = defaultStateName;
        return this;
    }    
    
    public SPIMainDocumentConfig setNotFoundStateName(String notFoundStateName)
    {
        this.notFoundStateName = notFoundStateName;
        return this;
    }       
    
    public SPIMainDocumentConfig check()
    {
        if (titleElem == null) throw new RuntimeException("Missing titleElement");
        if (contentParentElem == null) throw new RuntimeException("Missing contentParentElement");        
        if (googleAnalyticsElem == null) throw new RuntimeException("Missing googleAnalyticsElement");         
        // menuElemMap puede estar vacío (?)
        if (defaultStateName == null) throw new RuntimeException("Missing defaultStateName");        
        if (notFoundStateName == null) throw new RuntimeException("Missing notFoundStateName");          
        
        return this;
    }
}
