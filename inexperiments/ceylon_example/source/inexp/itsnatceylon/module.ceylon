
"ItsNat example based on Ceylon"
by("Jose Maria Arranz")
license("http://www.apache.org/licenses/LICENSE-2.0")
native("jvm") module inexp.itsnatceylon "1.0.0" {
    import java.base "7";
    import javax.xml "7";

    /* shared import "xml-apis:xml-apis" "1.3.04";  */
    /* shared import "javax.servlet" "3.1.0"; */

    shared import "xml.apis" "1.3.04";
    shared import "javax.servlet" "2.5";
    shared import itsnat_server "1.4";
}

/*
module inexp.itsnatceylon "1.0.0" {
 
    import java.base "7";
    import javax.xml "7";
    shared import "org.w3c.dom" "1.3.04";
    shared import "javax.servlet" "2.5";
    shared import itsnat_server "1.4";
}
*/