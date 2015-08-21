
function LocationState()
{
    this.getURL = getURL;
    this.setURL = setURL;    
    this.getStateName = getStateName;
    this.setStateName = setStateName;   
    this.isStateNameChanged = isStateNameChanged;
    
    this.url = window.location.href;
    
    function getURL() { return window.location.href; }
    function setURL(url) { window.location.href = url; }   
    
    function getStateName()
    {
        var url = this.getURL();        
        var posR = url.lastIndexOf("/");
        if (posR == -1) return null;
        var stateName = url.substring(posR + 1);
        if (stateName == "") return null;
        return stateName;
    }
    
    function setStateName(stateName)
    {
        var url = this.getURL();        
        var posR = url.lastIndexOf("/");        
        var url2;
        if (url.length > posR + 1) url2 = url.substring(0,posR + 1);
        else url2 = url;
        url2 = url2 + stateName;
        if (url == url2) return;

        if (window.history.pushState)
            window.history.pushState(null, null, url2);   
        else
        {
            if (window.location.href != url2)
                window.location.href = url2;
        }
    }
    
    function isStateNameChanged(newUrl)
    {
        var url = this.getURL();
        if (newUrl == url) return false;
        var posR = url.lastIndexOf("/");
        if (posR == -1) return false;
        var posR2 = newUrl.lastIndexOf("/");
        if (posR2 == -1) return false;        
        if (posR != posR2) return false;
        var stateName = url.substring(posR + 1);        
        var newStateName = newUrl.substring(posR + 1);
        if (stateName == newStateName) return false;    
        return true;
    }
}

function SPISite()
{
    this.load = load;
    this.detectURLStateChange = detectURLStateChange;
    this.detectURLStateChangeCB = detectURLStateChangeCB;
    this.setStateInURL = setStateInURL;
    this.removeChildren = removeChildren;
    this.onBackForward = null; // Public, user defined

    this.firstTime = true;
    this.href = null;
    this.disabled = false;

    this.load();

    function load() // page load phase
    {
        if (this.disabled) return;
    }

    function setStateInURL(stateName)
    {
        if (this.disabled) return;

        var currLoc = new LocationState();
        currLoc.setStateName(stateName);
            
        this.href = currLoc.getURL();

        if (!this.firstTime) return;
        this.firstTime = false;

        this.detectURLStateChange();
    }

    function detectURLStateChange()
    {
        var onpopstateSupport = ("onpopstate" in window); // Supported in IE 10            
        if (onpopstateSupport)
        {         
            var func = function()
            {
                arguments.callee.spiSite.detectURLStateChangeCB();
            };            
            func.spiSite = this;
            window.addEventListener("popstate", func, false);                
        }
    }

    function detectURLStateChangeCB()
    {
        // Detecting when only the state of the reference part of the URL changes
        var currLoc = new LocationState();
        if (!currLoc.isStateNameChanged(this.href)) return;
            
        // Only changed the state in reference part
        this.href = currLoc.getURL();

        var stateName = currLoc.getStateName();        
        if (this.onBackForward) this.onBackForward(stateName);
        else try { window.location.reload(true); }
             catch(ex) { window.location = window.location; }
    }
    
    function removeChildren(node) // used by spistless
    {
        while(node.firstChild) { var child = node.firstChild; node.removeChild(child); }; // Altnernative: node.innerHTML = ""
    }    
}

window.spiSite = new SPISite();

