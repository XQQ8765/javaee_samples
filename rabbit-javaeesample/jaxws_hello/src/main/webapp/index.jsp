<%
String requestPath = request.getServletPath();
String requestURL = request.getRequestURL().toString();
String servletPath = request.getServletPath();
String contextPath = request.getSession().getServletContext().getContextPath();
%>
<html>
<body>
<h5>Hello World!</h5>
<h2>sessionID:<%=session.getId() %></h2>
<ul>
    <li>requestPath: <%=requestPath%></li>
    <li>requestURL: <%=requestURL%></li>
    <li>servletPath: <%=servletPath%></li>
    <li>contextPath: <%=contextPath%></li>
</ul>

h3. Test Links
h4. Poor/Fair Hits, Browser Instrumentation
<ul>
    <li><a href="<%=requestURL%>/random.jsp">/random.jsp</a></li>
</ul>

h4. Error Hits
<ul>
    <li><a href="<%=requestURL%>/action/errorResponseCode">/action/errorResponseCode</a></li>
</ul>

h4. DataSource
<ul>
    <li><a href="<%=requestURL%>/action/accessMySQL">/action/accessMySQL</a></li>
</ul>

h4. Web Service
<ul>
    <li><a href="<%=requestURL%>/action/sayHello?content=Rabbit">/action/sayHello?content=Rabbit</a></li>
</ul>

</body>
</html>
