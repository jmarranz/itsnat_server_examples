
package org.itsnat.spi;

import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.event.ItsNatUserEvent;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.itsnat.core.http.ItsNatHttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServletResponse;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public abstract class SPIMainDocument
{
    protected ItsNatHTMLDocument itsNatDoc;
    protected SPIMainDocumentConfig config;
    protected String title;
    protected Element currentMenuItemElem;
    protected SPIState currentState;
    protected String googleAnalyticsIFrameURL;

    public SPIMainDocument(ItsNatHttpServletRequest request, ItsNatHttpServletResponse response,SPIMainDocumentConfig config)
    {
        config.check();
        
        this.config = config;
        this.itsNatDoc = (ItsNatHTMLDocument)request.getItsNatDocument();

        this.title = config.titleElem.getText(); // Initial value
        this.googleAnalyticsIFrameURL = config.googleAnalyticsElem.getAttribute("src");  // Initial value

        EventListener listener = new EventListener()
        {
            @Override
            public void handleEvent(Event evt)
            {
                ItsNatUserEvent itsNatEvt = (ItsNatUserEvent)evt;
                String name = (String)itsNatEvt.getExtraParam("name");
                changeState(name);
            }        
        };
        itsNatDoc.addUserEventListener(null,"setState", listener);

        HttpServletRequest servReq = request.getHttpServletRequest();
        String stateName = servReq.getParameter("_escaped_fragment_"); // Google bot, has priority, its value is based on the hash fragment
        if (stateName != null)
        {
            if (stateName.startsWith("st=")) // st means "state"
                stateName = stateName.substring("st=".length(), stateName.length());
            else // Wrong format
                stateName = config.defaultStateName;
        }
        else
        {
            stateName = servReq.getParameter("st");
            if (stateName == null)
                stateName = config.defaultStateName;
        }

        changeState(stateName);
    }

    public ItsNatHTMLDocument getItsNatHTMLDocument()
    {
        return itsNatDoc;
    }

    public void setStateTitle(String stateTitle)
    {
        String pageTitle = stateTitle + " - " + title;
        if (itsNatDoc.isLoading())
            config.titleElem.setText(pageTitle);
        else
            itsNatDoc.addCodeToSend("document.title = \"" + pageTitle + "\";\n");
    }

    public Element getContentParentElement()
    {
        return config.contentParentElem;
    }

    public ItsNatDocFragmentTemplate getFragmentTemplate(String name)
    {
        ItsNatServlet servlet = itsNatDoc.getItsNatDocumentTemplate().getItsNatServlet();
        return servlet.getItsNatDocFragmentTemplate(name);
    }

    public DocumentFragment loadDocumentFragment(String name)
    {
        ItsNatDocFragmentTemplate template = getFragmentTemplate(name);
        if (template == null) return null;
        return template.loadDocumentFragment(itsNatDoc);
    }

    public String getFirstLevelStateName(String stateName)
    {
        String firstLevelName = stateName;
        int pos = stateName.indexOf('.');
        if (pos != -1) firstLevelName = stateName.substring(0, pos); // Case "overview.popup"
        return firstLevelName;
    }

    public SPIState changeState(String stateName)
    {
        String fragmentName = getFirstLevelStateName(stateName);

        ItsNatDocFragmentTemplate template = getFragmentTemplate(fragmentName);
        if (template == null)
        {
            return changeState(config.notFoundStateName);
        }

        // Cleaning previous state:
        if (currentState != null)
        {
            currentState.dispose();
            this.currentState = null;
        }

        ItsNatDOMUtil.removeAllChildren(config.contentParentElem);

        // Setting new state:
        changeActiveMenu(stateName);

        DocumentFragment frag = template.loadDocumentFragment(itsNatDoc);
        config.contentParentElem.appendChild(frag);

        this.currentState = createSPIState(stateName);
        
        return currentState;
    }

    public abstract SPIState createSPIState(String stateName);
    
    public void registerState(SPIState state)
    {
        setStateTitle(state.getStateTitle());
        String stateName = state.getStateName();
        itsNatDoc.addCodeToSend("spiSite.setStateInURL(\"" + stateName + "\");");
        // googleAnalyticsElem.setAttribute("src",googleAnalyticsIFrameURL + stateName);
        // http://stackoverflow.com/questions/24407573/how-can-i-make-an-iframe-not-save-to-history-in-chrome
        String jsIFrameRef = itsNatDoc.getScriptUtil().getNodeReference(config.googleAnalyticsElem);
        itsNatDoc.addCodeToSend("var elem = " + jsIFrameRef + "; try{ elem.contentWindow.location.replace('" + googleAnalyticsIFrameURL + stateName + "'); } catch(e) {}");
    }


    public void changeActiveMenu(String stateName)
    {
        String mainMenuItemName = getFirstLevelStateName(stateName);

        Element prevActiveMenuItemElem = this.currentMenuItemElem;

        this.currentMenuItemElem = config.menuElemMap.get(mainMenuItemName);

        Element currActiveMenuItemElem = this.currentMenuItemElem;
        
        onChangeActiveMenu(prevActiveMenuItemElem,currActiveMenuItemElem,mainMenuItemName);
    }
    
    public abstract void onChangeActiveMenu(Element prevActiveMenuItemElem,Element currActiveMenuItemElem,String mainMenuItemName);
}
