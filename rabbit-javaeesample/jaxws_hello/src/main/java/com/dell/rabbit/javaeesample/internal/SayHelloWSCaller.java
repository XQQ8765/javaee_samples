package com.dell.rabbit.javaeesample.internal;

import com.dell.rabbit.javaeesample.internal.webservice.client.HelloWs;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

/**
 * Created by rxiao on 3/21/14.
 */
public class SayHelloWSCaller {
    private URL wsdlURL;

    public SayHelloWSCaller(URL wsdlURL) {
        this.wsdlURL = wsdlURL;
    }

    public String sayHello(String content) {
        QName qName = new QName("http://service.javaeesample.rabbit.dell.com/",
                "HelloWsService");
        Service service = Service.create(wsdlURL, qName);
        HelloWs helloService = service.getPort(HelloWs.class);
        return helloService.sayHello(content);
    }

}
