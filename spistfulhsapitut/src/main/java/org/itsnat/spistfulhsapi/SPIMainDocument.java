
package org.itsnat.spistfulhsapi;

import org.itsnat.spi.SPIMainDocumentConfig;
import org.itsnat.spi.SPIStateDescriptor;
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

        this.title = config.getTitleElement().getText(); // Initial value
        this.googleAnalyticsIFrameURL = config.getGoogleAnalyticsElement().getAttribute("src");  // Initial value

        EventListener listener = new EventListener()
        {
            @Override
            public void handleEvent(Event evt)
            {
                ItsNatUserEvent itsNatEvt = (ItsNatUserEvent)evt;
                ItsNatHttpServletRequest request = (ItsNatHttpServletRequest)itsNatEvt.getItsNatServletRequest();
                ItsNatHttpServletResponse response = (ItsNatHttpServletResponse)itsNatEvt.getItsNatServletResponse();
                String name = (String)itsNatEvt.getExtraParam("name");
                changeState(name,request,response);
            }
        };
        itsNatDoc.addUserEventListener(null,"setState", listener);

        String stateName;
        HttpServletRequest servReq = request.getHttpServletRequest();
        String reqURI = servReq.getRequestURI();

        if (!reqURI.endsWith("/"))
        {
            // Pretty URL case
            int pos = reqURI.lastIndexOf("/");
            stateName = reqURI.substring(pos + 1); // "/name" => "name"
        }
        else
        {
            stateName = config.getDefaultStateName();
        }


        changeState(stateName,request,response);
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
        int pos = stateName.indexOf('-');
        if (pos != -1) firstLevelName = stateName.substring(0, pos); // Case "overview-popup"
        return firstLevelName;
    }

    public SPIState changeState(String stateName,ItsNatHttpServletRequest request,ItsNatHttpServletResponse response)
    {
        SPIStateDescriptor stateDesc = config.getSPIStateDescriptor(stateName);
        if (stateDesc == null)
        {
            return changeState(config.getNotFoundStateName(),request,response);
        }

        // Cleaning previous state:
        if (currentState != null)
        {
            currentState.dispose();
            this.currentState = null;
        }

        ItsNatDOMUtil.removeAllChildren(config.getContentParentElement());

        // Setting new state:
        changeActiveMenu(stateName);

        String fragmentName = stateDesc.isMainLevel() ? stateName : getFirstLevelStateName(stateName);
        DocumentFragment frag = loadDocumentFragment(fragmentName);
        config.getContentParentElement().appendChild(frag);

        this.currentState = createSPIState(stateDesc,request,response);

        return currentState;
    }

    public abstract SPIState createSPIState(SPIStateDescriptor stateDesc,ItsNatHttpServletRequest request,ItsNatHttpServletResponse response);

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


    public void changeActiveMenu(String stateName)
    {
        String mainMenuItemName = getFirstLevelStateName(stateName);

        Element prevActiveMenuItemElem = this.currentMenuItemElem;

        this.currentMenuItemElem = config.getMenuElement(mainMenuItemName);

        Element currActiveMenuItemElem = this.currentMenuItemElem;

        onChangeActiveMenu(prevActiveMenuItemElem,currActiveMenuItemElem);
    }

    public abstract void onChangeActiveMenu(Element prevActiveMenuItemElem, Element currActiveMenuItemElem);
}
