<%
    response.addDateHeader("Expires", 0);
    response.addHeader("Cache-Control", "no-store,no-cache,must-revalidate,post-check=0,pre-check=0");
    
    response.sendRedirect("overview");    
    // Alternative: <jsp:forward page="/servlet" />    
%>

