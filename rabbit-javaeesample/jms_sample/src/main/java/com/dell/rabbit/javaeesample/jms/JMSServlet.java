package com.dell.rabbit.javaeesample.jms;

import org.apache.log4j.Logger;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by rxiao on 3/28/14.
 */
public class JMSServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(JMSServlet.class.getName());
    private static final int JMS_MESSAGE_COUNT = 1;
    private InitialContext initCtx;
    private Context envContext;
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session jmsSession;
    private MessageProducer producer;

    public void init() throws ServletException {
        try {
            initCtx = new InitialContext();
            envContext = (Context) initCtx.lookup("java:comp/env");
            connectionFactory = (ConnectionFactory) envContext.lookup(JMSConstant.JMS_CONNECTION_FACTORY_NAME);
            logger.info("Found connection factory " + JMSConstant.JMS_CONNECTION_FACTORY_NAME + " in JNDI");
            connection = connectionFactory.createConnection();
            jmsSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = jmsSession.createProducer((Destination) envContext.lookup(JMSConstant.JMS_TOPIC_NAME));
            logger.info("producer with Destination: " + JMSConstant.JMS_TOPIC_NAME + " in JNDI");
            connection.start();
            logger.info("JMS connection started." );
        } catch (NamingException e) {
            logger.error("NamingException occurs", e);
        } catch (JMSException e) {
            logger.error("JMSException occurs", e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String messagesCountParam = req.getParameter("messagesCount");
        String content = req.getParameter("content");
        if (content == null) {
            content = "";
        }
        int messagesCount = messagesCountParam == null ? JMS_MESSAGE_COUNT : Integer.valueOf(messagesCountParam);
        jms(resp, messagesCount, content);
    }

    private void jms(HttpServletResponse resp, int messagesCount, String content) {
        long t0 = System.currentTimeMillis();
        try {
            for (int i = 0; i < messagesCount; i++) {
                String messageContent = "Message with content:(" + content + ") to be sent to " + JMSConstant.JMS_TOPIC_NAME + ", currentTime:" + new Date();
                TextMessage message = jmsSession.createTextMessage(messageContent);
                if (producer instanceof TopicPublisher) {
                    ((TopicPublisher) producer).publish(message);
                } else {
                    producer.send(message);
                }
            }
            long spentTime = System.currentTimeMillis() - t0;
            resp.getWriter().println("Sent (" + messagesCount + ")jms messages within destination " + JMSConstant.JMS_TOPIC_NAME + ", spentTime=" + spentTime + "ms.");
        } catch (JMSException e) {
            logger.error("JMSException is thrown: Error message:" + e.getMessage());
        } catch (IOException e) {
            logger.error("IOException is thrown: Error message:" + e.getMessage());
        }
    }
}
