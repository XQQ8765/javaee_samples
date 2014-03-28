package com.dell.rabbit.javaeesample.jms;

import org.apache.log4j.Logger;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Enumeration;

/**
 * Created by rxiao on 3/28/14.
 */
public class JMSListener implements ServletContextListener, MessageListener {
    private static final Logger logger = Logger.getLogger(JMSListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            System.out.println("JMSListener is starting");
            logger.info("JMSListener is starting");
            InitialContext initCtx = new InitialContext();
            Context envContext = (Context) initCtx.lookup("java:comp/env");
            ConnectionFactory connectionFactory = (ConnectionFactory) envContext
                    .lookup(JMSConstant.JMS_CONNECTION_FACTORY_NAME);
            Connection connection = connectionFactory.createConnection();
            connection.setClientID(JMSConstant.JMS_CLIENT_ID);
            Session jmsSession = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            TopicSubscriber consumer=jmsSession.createDurableSubscriber(
                    (Topic)envContext.lookup(JMSConstant.JMS_TOPIC_NAME),
                    JMSConstant.JMS_SUBJECT_NAME);
            consumer.setMessageListener(this);
            connection.start();
            System.out.println("JMS connection started.");
            logger.info("JMS connection started. connectionFactory:" + connectionFactory + ", connection:" + connection + ", Consumer: Topic:" + JMSConstant.JMS_TOPIC_NAME + ", Subject:" + JMSConstant.JMS_SUBJECT_NAME);
        } catch (NamingException e) {
            logger.error("NamingException occurs", e);
        } catch (JMSException e) {
            logger.error("JMSException occurs", e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("JMS stopped.");
    }

    @Override
    public void onMessage(Message message) {
        try {
            String messageId = message.getJMSMessageID();
            Enumeration propertyNames = message.getPropertyNames();
            if (propertyNames != null && propertyNames.hasMoreElements()) {
                String propertyName = (String) propertyNames.nextElement();
                String messageContent = message.getStringProperty(propertyName);
                logger.info("Received message: propertyName:" + propertyName + ", messageContent:" + messageContent);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
