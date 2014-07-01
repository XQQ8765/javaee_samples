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
        java.sql.Connection connection = null;
        try {
            DataSource dataSource = (DataSource) envContext.lookup(DATASOURCE_NAME);
            String sql = "select TABLE_NAME, TABLE_TYPE from information_schema.TABLES";
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            StringBuilder sb = new StringBuilder();
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                String tableType = resultSet.getString("TABLE_TYPE");
                sb.append("TableName:");
                sb.append(tableName);
                sb.append(", TableType:");
                sb.append(tableType);
                sb.append("<br/>");
            }
            logger.info("Executed sql: " + sql + "Result:" + sb.toString());
            response.getWriter().println("Executed SQL : " + sql);
            response.getWriter().println("Executed Result : " + sb.toString());
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new ServletException("Error occurs when close connection", e);
                }
            }
        }
    }
}
