
package org.itsnat.spistfulhashbangtut;

import org.itsnat.spistfulhashbang.SPIState;
import org.itsnat.core.http.ItsNatHttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServletResponse;
import org.itsnat.spistfulhashbang.SPIMainDocument;
import org.itsnat.spistfulhashbang.SPIMainDocumentConfig;
import org.itsnat.spistfulhashbang.SPIStateDescriptor;
import org.w3c.dom.Element;

public class SPITutMainDocument extends SPIMainDocument
{
    public SPITutMainDocument(ItsNatHttpServletRequest request, ItsNatHttpServletResponse response,SPIMainDocumentConfig config)
    {
        super(request,response,config);
    }

    @Override
    public SPIState changeState(String stateName,ItsNatHttpServletRequest request,ItsNatHttpServletResponse response)
    {
        SPIState state = super.changeState(stateName,request,response);
       
        itsNatDoc.addCodeToSend("try{ window.scroll(0,-5000); }catch(ex){}");
        // try/catch is used to avoid exceptions when some (mobile) browser does not support window.scroll()        
        
        return state;
    }        
    
    @Override
    public SPIState createSPIState(SPIStateDescriptor stateDesc,ItsNatHttpServletRequest request,ItsNatHttpServletResponse response)    
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
            return new SPITutStateDetail(this,stateDesc);
        else
            return null;
    }
    

    @Override
    public void onChangeActiveMenu(Element prevActiveMenuItemElem,Element currActiveMenuItemElem)
    {
        if (prevActiveMenuItemElem != null)
            prevActiveMenuItemElem.removeAttribute("class");
        if (currActiveMenuItemElem != null)
            currActiveMenuItemElem.setAttribute("class","menuOpSelected");
    }
 
}
