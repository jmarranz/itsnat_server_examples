
package org.itsnat.spistfulhsapi;

import org.itsnat.spi.SPIMainDocumentConfig;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServletResponse;
import org.itsnat.spistful.SPIStfulMainDocument;

public abstract class SPIStfulHsapiMainDocument extends SPIStfulMainDocument
{
    public SPIStfulHsapiMainDocument(ItsNatHttpServletRequest request, ItsNatHttpServletResponse response,SPIMainDocumentConfig config)
    {
        super(request, response,config);

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
}
