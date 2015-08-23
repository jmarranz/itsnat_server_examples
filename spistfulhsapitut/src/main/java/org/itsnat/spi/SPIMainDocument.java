
package org.itsnat.spi;

import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.http.ItsNatHttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServletResponse;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

public abstract class SPIMainDocument
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected SPIMainDocumentConfig config;
    protected String title;
    protected String googleAnalyticsIFrameURL;

    
    public SPIMainDocument(ItsNatHttpServletRequest request, ItsNatHttpServletResponse response,SPIMainDocumentConfig config)
    {
        config.check();
        
        this.config = config;
        this.itsNatDoc = (ItsNatHTMLDocument)request.getItsNatDocument();

        this.title = config.getTitleElement().getText(); // Initial value
        this.googleAnalyticsIFrameURL = config.getGoogleAnalyticsElement().getAttribute("src");  // Initial value
    }

    public ItsNatHTMLDocument getItsNatHTMLDocument()
    {
        return itsNatDoc;
    }

    public SPIStateDescriptor getSPIStateDescriptor(String stateName)
    {
        return config.getSPIStateDescriptor(stateName);
    }    
    
    public void setStateTitle(String stateTitle)
    {
        String pageTitle = stateTitle + " - " + title;
        if (itsNatDoc.isLoading())
            config.getTitleElement().setText(pageTitle);
        else
            itsNatDoc.addCodeToSend("document.title = \"" + pageTitle + "\";\n");
    }

    public Element getContentParentElement()
    {
        return config.getContentParentElement();
    }

    public ItsNatDocFragmentTemplate getFragmentTemplate(String name)
    {
        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();
        return servlet.getItsNatDocFragmentTemplate(name);
    }

    public DocumentFragment loadDocumentFragment(String name)
    {
        ItsNatDocFragmentTemplate template = getFragmentTemplate(name);
        if (template == null)        
            throw new RuntimeException("There is no template registered for state or fragment name: " + name);        
        return template.loadDocumentFragment(itsNatDoc);
    }

    public String getFirstLevelStateName(String stateName)
    {
        String firstLevelName = stateName;
        int pos = stateName.indexOf(config.getStateNameSeparator());
        if (pos != -1) firstLevelName = stateName.substring(0, pos); // Case "overview.popup"
        return firstLevelName;
    }
    
    public void registerState(SPIState state)
    {
        setStateTitle(state.getStateTitle());
        String stateName = state.getStateName();
        itsNatDoc.addCodeToSend("spiSite.setStateInURL(\"" + stateName + "\");");
        // googleAnalyticsElem.setAttribute("src",googleAnalyticsIFrameURL + stateName);
        // http://stackoverflow.com/questions/24407573/how-can-i-make-an-iframe-not-save-to-history-in-chrome
        String jsIFrameRef = itsNatDoc.getScriptUtil().getNodeReference(config.getGoogleAnalyticsElement());
        itsNatDoc.addCodeToSend("var elem = " + jsIFrameRef + "; try{ elem.contentWindow.location.replace('" + googleAnalyticsIFrameURL + stateName + "'); } catch(e) {}");
    }
}
