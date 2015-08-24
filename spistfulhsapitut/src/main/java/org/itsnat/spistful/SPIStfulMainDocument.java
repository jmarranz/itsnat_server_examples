
package org.itsnat.spistful;

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

public abstract class SPIStfulMainDocument extends SPIMainDocument
{
    protected Element currentMenuItemElem;
    protected SPIStfulState currentState;

    public SPIStfulMainDocument(ItsNatHttpServletRequest request, ItsNatHttpServletResponse response,SPIMainDocumentConfig config)
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
    }

    public SPIStfulState changeState(String stateName,ItsNatHttpServletRequest request,ItsNatHttpServletResponse response)
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

    public abstract SPIStfulState createSPIState(SPIStateDescriptor stateDesc,ItsNatHttpServletRequest request,ItsNatHttpServletResponse response);


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
