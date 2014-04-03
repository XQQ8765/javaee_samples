package com.dell.rabbit.javaeesample.servlet;

import org.apache.log4j.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by rxiao on 4/3/14.
 */
public class MySQLDBServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(MySQLDBServlet.class.getName());
    private static final String DATASOURCE_NAME = "jdbc/MYSQLTestDataSource";
    private InitialContext initCtx;
    private Context envContext;

    public void init(ServletConfig config) throws ServletException {
        try {
            initCtx = new InitialContext();
            envContext = (Context) initCtx.lookup("java:comp/env");
        } catch (NamingException e) {
            throw new ServletException(e);
        }

    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException, ServletException {
        try {
            DataSource dataSource = (DataSource) envContext.lookup(DATASOURCE_NAME);
            String sql = "select name, value from information_schema.settings";
            java.sql.Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                resultSet.getString("name");
                resultSet.getString("value");
            }
            logger.info("Executed sql: " + sql);
            response.getWriter().println("Executed SQL : " + sql);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
