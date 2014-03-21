package com.dell.rabbit.javaeesample.jaxws;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.URL;

/**
 * Created by rxiao on 3/21/14.
 */
public class SayHelloServlet extends HttpServlet{
    private static String WSDL_URL= "http://localhost:8988/jaxws_hello/HelloWs?wsdl";
     public void doGet(HttpServletRequest request,
                      HttpServletResponse response)throws IOException,ServletException{
        String content = request.getParameter("content");
        if (content == null) {
            content = "";
        }
        URL url = new URL(WSDL_URL);
        SayHelloWSCaller caller = new SayHelloWSCaller(url);
        long t0 = System.currentTimeMillis();
        String result = caller.sayHello(content);
        long spentTime = System.currentTimeMillis() - t0;

        String responseContent = "WSDL_URL is:"+WSDL_URL+"result is:("+result+"), spent ("+spentTime+") ms";
        output(response, responseContent);
    }

    private void output(HttpServletResponse response, String content) throws IOException {
        PrintWriter out= response.getWriter();

        //set content type
        response.setContentType("text/html;charset=UTF-8");
        out.print("<HTML><HEAD><TITLE>HelloWs invoke result</TITLE>");
        out.print("</HEAD><BODY>");
        out.println("<h1><p>"+content+"</h1>");
        out.print("</BODY></HTML>");
        out.close();
    }
}
