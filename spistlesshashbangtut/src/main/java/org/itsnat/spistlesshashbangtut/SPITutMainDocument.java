
package org.itsnat.spistlesshashbangtut;

import org.itsnat.spistlesshashbang.SPIStlessHashbangMainDocument;
import org.itsnat.core.event.ItsNatEventDOMStateless;
import org.itsnat.core.http.ItsNatHttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServletResponse;
import org.itsnat.spi.SPIMainDocumentConfig;
import org.itsnat.spi.SPIState;
import org.itsnat.spi.SPIStateDescriptor;
import org.w3c.dom.Element;

public class SPITutMainDocument extends SPIStlessHashbangMainDocument
{
    public SPITutMainDocument(ItsNatHttpServletRequest request, ItsNatHttpServletResponse response,SPIMainDocumentConfig config)
    {
        super(request,response,config);
    }

    @Override
    public SPIState changeState(String stateName)
    {
        SPIState state = super.changeState(stateName);
       
        itsNatDoc.addCodeToSend("try{ window.scroll(0,-5000); }catch(ex){}");
        // try/catch is used to avoid exceptions when some (mobile) browser does not support window.scroll()        
        
        return state;
    }        
    
    @Override
    public SPIState createSPIState(SPIStateDescriptor stateDesc,ItsNatEventDOMStateless itsNatEvt)    
    {
        String stateName = stateDesc.getStateName();
        if (stateName.equals("overview")||stateName.equals("overview.popup"))
        {
            boolean popup = false;
            if (stateName.equals("overview.popup"))
            {
                popup = true;
                stateDesc = getSPIStateDescriptor("overview");
            }
            return new SPITutStateOverview(this,stateDesc,popup);            
        }
        else if (stateName.equals("detail"))
        {
            String stateSecondaryName = itsNatEvt != null? (String)itsNatEvt.getExtraParam("state_secondary_name") : null;            
            return new SPITutStateDetail(this,stateDesc,stateSecondaryName);
        }
        else
            return null;
    }
    
    @Override
    public void onChangeActiveMenu(Element menuItemElem,boolean active)
    {
        if (active)
        {
            menuItemElem.setAttribute("class","menuOpSelected");
        }
        else
        {
            menuItemElem.setAttribute("class","foo");            
            menuItemElem.removeAttribute("class");
        }        
    }

}
