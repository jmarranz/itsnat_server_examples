
package org.itsnat.spistfulhashbang;

import org.itsnat.spi.SPIMainDocumentConfig;
import org.itsnat.spi.SPIStateDescriptor;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.domutil.ItsNatDOMUtil;
import org.itsnat.core.event.ItsNatUserEvent;
import org.itsnat.core.http.ItsNatHttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServletResponse;
import org.itsnat.spi.SPIMainDocument;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;

public abstract class SPIStfulHashbangMainDocument extends SPIMainDocument
{
    protected Element currentMenuItemElem;
    protected SPIStateStful currentState;

    public SPIStfulHashbangMainDocument(ItsNatHttpServletRequest request, ItsNatHttpServletResponse response,SPIMainDocumentConfig config)
    {
        super(request, response,config);

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

        HttpServletRequest servReq = request.getHttpServletRequest();
        String stateName = servReq.getParameter("_escaped_fragment_"); // Google bot, has priority, its value is based on the hash fragment
        if (stateName != null)
        {
            if (stateName.startsWith("st=")) // st means "state"
                stateName = stateName.substring("st=".length(), stateName.length());
            else // Wrong format
                stateName = config.getDefaultStateName();
        }
        else
        {
            stateName = servReq.getParameter("st");
            if (stateName == null)
                stateName = config.getDefaultStateName();
        }


        changeState(stateName,request,response);
    }

    public SPIStateStful changeState(String stateName,ItsNatHttpServletRequest request,ItsNatHttpServletResponse response)
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

    public abstract SPIStateStful createSPIState(SPIStateDescriptor stateDesc,ItsNatHttpServletRequest request,ItsNatHttpServletResponse response);


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
