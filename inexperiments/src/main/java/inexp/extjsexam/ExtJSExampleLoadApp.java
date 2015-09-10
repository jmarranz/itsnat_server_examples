
package inexp.extjsexam;

import inexp.extjsexam.model.DBSimulator;
import org.itsnat.core.tmpl.ItsNatDocumentTemplate;
import org.itsnat.core.http.ItsNatHttpServlet;
import org.itsnat.core.tmpl.ItsNatDocFragmentTemplate;

/**
 *
 * @author jmarranz
 */
public class ExtJSExampleLoadApp
{
    public static void init(ItsNatHttpServlet itsNatServlet,String pathPrefix)
    {
        String pagesPrefix = pathPrefix + "extjsexample/pages/";
        String fragsPrefix = pathPrefix + "extjsexample/frags/";

        DBSimulator dataModel = new DBSimulator();

        ItsNatDocumentTemplate docTemplate;
        docTemplate = itsNatServlet.registerItsNatDocumentTemplate("extjsexample","text/html", pagesPrefix + "extjsexample.html");
        docTemplate.addItsNatServletRequestListener(new ExtJSExampleLoadListener(dataModel));

        ItsNatDocFragmentTemplate docFragTemplate;
        docFragTemplate = itsNatServlet.registerItsNatDocFragmentTemplate("extjsexample_introduction", "text/html", fragsPrefix + "introduction.html");
        docFragTemplate = itsNatServlet.registerItsNatDocFragmentTemplate("extjsexample_paneltable", "text/html", fragsPrefix + "panel_table.html");
        docFragTemplate = itsNatServlet.registerItsNatDocFragmentTemplate("extjsexample_add_new_item", "text/html", fragsPrefix + "add_new_item.html");
        docFragTemplate = itsNatServlet.registerItsNatDocFragmentTemplate("extjsexample_confirm_remove_item", "text/html", fragsPrefix + "confirm_remove_item.html");
        docFragTemplate = itsNatServlet.registerItsNatDocFragmentTemplate("extjsexample_error_new_item", "text/html", fragsPrefix + "error_new_item.html");
    }

}
