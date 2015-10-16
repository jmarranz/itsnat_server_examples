
import java.lang {StringJava = String}
import java.io {Serializable}
import org.w3c.dom.html {HTMLDocument}

// import org.w3c.dom {Document}


// import org.w3c.dom.events {EventListener}
// import org.w3c.dom.events {Event,EventListener,EventTarget} 

import org.itsnat.core {ItsNatServletRequest,ItsNatServletResponse,ItsNatDocument}
import org.itsnat.core.http {ItsNatHttpServlet}
import org.itsnat.core.tmpl {ItsNatDocumentTemplate}
import org.itsnat.core.event {ItsNatServletRequestListener}
import org.itsnat.core.html {ItsNatHTMLDocument}
 



"The classic Hello World program"
shared void hello(String name = "World") {
    print("Hello, `` name ``!");
}

"The runnable method of the module." 
shared void run(){
    if (nonempty args=process.arguments) {
        for (arg in args) {
            hello(arg);
        }
    }
    else {
        hello();
    }
}

class CoreExampleDocument(ItsNatHTMLDocument itsNatDoc) // satisfies EventListener // & Serializable
{

    print("Hello CoreExampleDocument");

/*
    Document doc = itsNatDoc.doc;
    if (is HTMLDocument doc)
    {

    }
*/

/*
    HTMLDocument doc = itsNatDoc.getHTMLDocument();  

    Element clickElem1 = doc.getElementById("clickableId1");
    Element clickElem2 = doc.getElementById("clickableId2"); 
*/

/*
    shared actual void handleEvent(Event event)
    {       
    }
*/

}

class CoreExampleLoadListener() satisfies ItsNatServletRequestListener
{
    shared actual void processRequest(ItsNatServletRequest request, ItsNatServletResponse response)
    {       
        ItsNatDocument itsNatDoc = request.itsNatDocument;
        if (is ItsNatHTMLDocument itsNatDoc)
        {
            CoreExampleDocument(itsNatDoc); 
        }
    }
}

"ItsNat Example"
shared void ceylonex_init(ItsNatHttpServlet itsNatServlet,variable String pathPrefix) 
{
    // print("Hello Ceylon , `` pathPrefix ``!");

    pathPrefix += "ceylonex/pages/";

    ItsNatDocumentTemplate docTemplate;
    docTemplate = itsNatServlet.registerItsNatDocumentTemplate("ceylonex","text/html",StringJava(pathPrefix + "ceylonex.html")); // formal param is "Object source", explicit StringJava conversion is needed
    docTemplate.addItsNatServletRequestListener(CoreExampleLoadListener());
}

