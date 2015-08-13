
package org.itsnat.spistlesshashbang;

import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.ClientDocument;
import org.itsnat.core.ItsNatServlet;
import org.itsnat.core.event.ItsNatEventDOMStateless;
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
    protected SPIState currentState;
    protected String googleAnalyticsIFrameURL;

    public SPIMainDocument(ItsNatHttpServletRequest request, ItsNatHttpServletResponse response,SPIMainDocumentConfig config)
    {
        config.check();
        
        this.config = config;
        this.itsNatDoc = (ItsNatHTMLDocument)request.getItsNatDocument();

        this.title = config.titleElem.getText(); // Initial value
        this.googleAnalyticsIFrameURL = config.googleAnalyticsElem.getAttribute("src");  // Initial value

        if (!itsNatDoc.isCreatedByStatelessEvent())
        {        
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
        else
        {
            EventListener listener = new EventListener()
            {
                @Override
                public void handleEvent(Event evt)
                {
                    ItsNatEventDOMStateless itsNatEvt = (ItsNatEventDOMStateless)evt;

                    String stateName = (String)itsNatEvt.getExtraParam("state_name");

                    changeState(stateName,itsNatEvt);
                }
            };
            
            itsNatDoc.addEventListener(listener);             
        }
    }

    public ItsNatHTMLDocument getItsNatHTMLDocument()
    {
        return itsNatDoc;
    }

    public SPIStateDescriptor getSPIStateDescriptor(String stateName)
    {
        return config.stateMap.get(stateName);
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
        if (template == null)        
            throw new RuntimeException("There is no template registered for state or fragment name: " + name);        
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
        return changeState(stateName,null);
    }
    
    public SPIState changeState(String stateName,ItsNatEventDOMStateless itsNatEvt)
    {
        SPIStateDescriptor stateDesc = config.stateMap.get(stateName);
        if (stateDesc == null)
        {
            return changeState(config.notFoundStateName,itsNatEvt);
        }        
        
        // Cleaning previous state:
        if (!itsNatDoc.isLoading())
        {
            ClientDocument clientDoc = itsNatDoc.getClientDocumentOwner();
            String contentParentRef = clientDoc.getScriptUtil().getNodeReference(config.contentParentElem);            
            clientDoc.addCodeToSend("window.spiSite.removeChildren(" + contentParentRef + ");");  // ".innerHTML = '';"
        }

        // Setting new state:
        changeActiveMenu(stateName);

        String fragmentName = stateDesc.isMainLevel() ? stateName : getFirstLevelStateName(stateName);        
        DocumentFragment frag = loadDocumentFragment(fragmentName);
        config.contentParentElem.appendChild(frag);

        this.currentState = createSPIState(stateDesc,itsNatEvt);
        
        return currentState;
    }

    public abstract SPIState createSPIState(SPIStateDescriptor stateDesc,ItsNatEventDOMStateless itsNatEvt);
    
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

        Element currentMenuItemElem = config.menuElemMap.get(mainMenuItemName);        
        for(Element menuItemElem : config.menuElemMap.values())
        {
            onChangeActiveMenu(menuItemElem,(currentMenuItemElem == menuItemElem));
        }
    }
    
    public abstract void onChangeActiveMenu(Element menuItemElem,boolean active);
}
