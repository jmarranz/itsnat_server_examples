
package org.itsnat.spistfulhashbang;

import org.itsnat.spi.SPIMainDocumentConfig;
import javax.servlet.http.HttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServletRequest;
import org.itsnat.core.http.ItsNatHttpServletResponse;
import org.itsnat.spistful.SPIStfulMainDocument;

public abstract class SPIStfulHashbangMainDocument extends SPIStfulMainDocument
{
    public SPIStfulHashbangMainDocument(ItsNatHttpServletRequest request, ItsNatHttpServletResponse response,SPIMainDocumentConfig config)
    {
        super(request, response,config);

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
}
