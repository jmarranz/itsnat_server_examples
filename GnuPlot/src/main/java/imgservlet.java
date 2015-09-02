/*
 * imgservlet.java
 *
 * Created on 14 de abril de 2008, 10:04
 */

import de.wg.core.GnuplotHelper;
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author jmarranz
 * @version
 */
public class imgservlet extends HttpServlet
{
 
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
            
        GnuplotHelper.pathPrefix = config.getServletContext().getRealPath("/") + "/WEB-INF/gnuplot/usr_local/usr/local/bin/";             
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        String commands = request.getParameter("commands");
        GnuplotHelper helper = GnuplotHelper.getDefault();
        
        response.setContentType("image/jpeg");
        OutputStream out = response.getOutputStream();
        out.write(helper.getImageBytes(commands));

        out.close();
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo()
    {
        return "Short description";
    }
    // </editor-fold>
}
