package com.dell.rabbit.javaeesample.jms;

import org.apache.activemq.broker.BrokerService;

/**
 * Created by rxiao on 4/2/14.
 */
public class JMSBrokerServiceMain
{
    public static void main(String[] args) {
        JMSBrokerServiceMain mainInstance = new JMSBrokerServiceMain();
        mainInstance.startBrokerService();
    }

    public void startBrokerService() {
        BrokerService broker = new BrokerService();
        broker.setBrokerName("Rabbit-ActiveMQ-BrokerService");
        broker.setPersistent(false);
        try {
            broker.addConnector("tcp://localhost:61616");
            broker.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
