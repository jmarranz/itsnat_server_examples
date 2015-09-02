/*
 * plotservlet.java
 *
 * Created on 14 de abril de 2008, 9:04
 */


import javax.servlet.*;
import org.itsnat.core.ItsNatServletConfig;
import org.itsnat.core.http.HttpServletWrapper;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import webgnuplot.GnuPlotLoadListener;

/**
 *
 * @author jmarranz
 * @version
 */
public class plotservlet extends HttpServletWrapper
{
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        
        String pathPrefix = getServletContext().getRealPath("/");
        pathPrefix += "/WEB-INF/pages/";
        
        ItsNatHttpServlet itsNatServlet = getItsNatHttpServlet();
        ItsNatDocumentTemplate docTemplate = itsNatServlet.registerItsNatDocumentTemplate("gnuplot","text/html",pathPrefix + "gnuplot.xhtml");
        docTemplate.addItsNatServletRequestListener(new GnuPlotLoadListener());        
    }
}
