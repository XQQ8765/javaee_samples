package com.dell.rabbit.javaeesample.servlet;

import com.dell.rabbit.javaeesample.internal.SayHelloWSCaller;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rxiao on 3/21/14.
 */
public class SayHelloServlet extends HttpServlet{
    private static URL WSDL_URL;
            //= "http://localhost:8988/jaxws_hello/HelloWs?wsdl";

    public void init(ServletConfig config) throws ServletException{
        String wsdl_file = config.getInitParameter("WSDL_FILE");
        try {
            WSDL_URL =  config.getServletContext().getResource(wsdl_file);
        } catch (MalformedURLException e) {
            throw new  ServletException("Can't load wsdl_file:" + wsdl_file);
        }
    }
     public void doGet(HttpServletRequest request,
                      HttpServletResponse response)throws IOException,ServletException{
        String content = request.getParameter("content");
        if (content == null) {
            content = "";
        }

        SayHelloWSCaller caller = new SayHelloWSCaller(WSDL_URL);
        long t0 = System.currentTimeMillis();
        String result = caller.sayHello(content);
        long spentTime = System.currentTimeMillis() - t0;

        String responseContent = "<br>WSDL_URL is:"+WSDL_URL+"<br/><br>result is:("+result+"), spent ("+spentTime+") ms<br/>";
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
