
package org.itsnat.spitut;

import org.itsnat.spi.SPIState;
import org.itsnat.core.http.ItsNatHttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServletResponse;
import org.itsnat.spi.SPIMainDocument;
import org.itsnat.spi.SPIMainDocumentConfig;
import org.w3c.dom.Element;

public class SPITutMainDocument extends SPIMainDocument
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
    public SPIState createSPIState(String stateName)    
    {
        if (stateName.equals("overview")||stateName.equals("overview.popup"))
        {
            boolean popup = stateName.equals("overview.popup");
            return new SPITutStateOverview(this,popup);
        }
        else if (stateName.equals("detail"))
            return new SPITutStateDetail(this);
        else
            return null;
    }
    

    @Override
    public void onChangeActiveMenu(Element prevActiveMenuItemElem,Element currActiveMenuItemElem,String mainMenuItemName)
    {
        if (prevActiveMenuItemElem != null)
            prevActiveMenuItemElem.removeAttribute("class");
        if (currActiveMenuItemElem != null)
            currActiveMenuItemElem.setAttribute("class","menuOpSelected");
    }
 
}
