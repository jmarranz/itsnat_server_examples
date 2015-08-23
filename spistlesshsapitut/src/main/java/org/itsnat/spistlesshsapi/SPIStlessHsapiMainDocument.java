
package org.itsnat.spistlesshsapi;

import org.itsnat.spi.SPIMainDocumentConfig;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServletResponse;
import org.itsnat.spistless.SPIStlessMainDocument;

public abstract class SPIStlessHsapiMainDocument extends SPIStlessMainDocument
{
    public SPIStlessHsapiMainDocument(ItsNatHttpServletRequest request, ItsNatHttpServletResponse response,SPIMainDocumentConfig config)
    {
        super(request, response,config);
        
        if (!itsNatDoc.isCreatedByStatelessEvent())
        {
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

            changeState(stateName);
        }
    }
}
