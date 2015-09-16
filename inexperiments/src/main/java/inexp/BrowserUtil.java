package inexp;

import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.http.ItsNatHttpSession;


/**
 *
 * @author jmarranz
 */
public class BrowserUtil
{
    public static boolean isMobileBrowser(ItsNatServletRequest request)
    {
        ItsNatHttpSession session = (ItsNatHttpSession)request.getItsNatSession();
        String userAgent = session.getUserAgent();
        return (userAgent.contains("Android")) ||  
               (userAgent.contains("iPod")) ||
               (userAgent.contains("iPhone")) ||
               (userAgent.contains("iPad")) ||                
               (userAgent.contains("BlackBerry"));
    }

    public static boolean isJoystickModePreferred(ItsNatServletRequest request)
    {
        return isMobileBrowser(request);
    }

}
