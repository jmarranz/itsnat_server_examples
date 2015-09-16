package inexp.jproxyex;

import com.innowhere.relproxy.jproxy.JProxy;
import org.itsnat.comp.ItsNatComponentManager;
import org.itsnat.comp.text.ItsNatHTMLInputText;
import org.itsnat.core.ItsNatServletRequest;
import org.itsnat.core.html.ItsNatHTMLDocument;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLDocument;

public class JProxyExampleDocument
{
    protected ItsNatHTMLDocument itsNatDoc; // ItsNatHTMLDocument
    protected ItsNatHTMLInputText textInput; // ItsNatHTMLInputText
    protected Element resultsElem; // Element             

    public JProxyExampleDocument() // required by RelProxy (when registering the EventListener)
    {          
    }
    
    public JProxyExampleDocument(ItsNatServletRequest request,ItsNatHTMLDocument itsNatDoc,FalseDB db)
    {      
        this.itsNatDoc = itsNatDoc;

        if (db.getCityList().size() != 3) 
            throw new RuntimeException("Unexpected");

        HTMLDocument doc = itsNatDoc.getHTMLDocument();

        ItsNatComponentManager compMgr = itsNatDoc.getItsNatComponentManager();
        this.textInput = (ItsNatHTMLInputText)compMgr.createItsNatComponentById("inputId");

        final String comment1 = "";        
        EventListener listener = new EventListener()
        {    
            @Override
            public void handleEvent(Event evt) 
            {
                String text = textInput.getText(); 
                String comment2 = "";
                resultsElem.setTextContent(text + comment1 + comment2);
            }
        };
        listener = JProxy.create(listener, EventListener.class);
        Element buttonElem = doc.getElementById("buttonId");
        ((EventTarget)buttonElem).addEventListener("click",listener,false);

        this.resultsElem = doc.getElementById("resultsId");
    }
}
