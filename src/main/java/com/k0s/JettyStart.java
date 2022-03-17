package com.k0s;

import com.k0s.dao.jdbc.ConnectionFactory;
import com.k0s.dao.jdbc.JdbcProductDao;
import com.k0s.dao.jdbc.JdbcUserDao;
import com.k0s.service.ProductService;
import com.k0s.service.UserService;
import com.k0s.util.PropertiesReader;
import com.k0s.web.*;
import com.k0s.web.filter.AuthFilter;
import com.k0s.web.filter.LogFilter;
import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Properties;

public class JettyStart {


    public static void main(String[] args) throws Exception {

        PropertiesReader propertiesReader = new PropertiesReader("db.properties");
        propertiesReader.readProperties();
        ConnectionFactory connectionFactory = new ConnectionFactory(propertiesReader.getProperties());

        JdbcProductDao jdbcProductDao = new JdbcProductDao(connectionFactory);
        ProductService productService = new ProductService(jdbcProductDao);

        JdbcUserDao jdbcUserDao = new JdbcUserDao(connectionFactory);
        UserService userService = new UserService(jdbcUserDao);

        AuthFilter authFilter = new AuthFilter(userService);
        LogFilter logFilter = new LogFilter();

        flywayMigration(propertiesReader.getProperties());

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);


        servletContextHandler.addServlet(new ServletHolder(new IndexServlet(productService)), "");
        servletContextHandler.addServlet(new ServletHolder(new AddServlet(productService)), "/admin/add");
        servletContextHandler.addServlet(new ServletHolder(new ManageServlet(productService)), "/admin");
        servletContextHandler.addServlet(new ServletHolder(new EditServlet(productService)), "/admin/edit");
        servletContextHandler.addServlet(new ServletHolder(new SearchServlet(productService)), "/search");
        servletContextHandler.addServlet(new ServletHolder(new ResourcesServlet()), "/resources/*");
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(userService)), "/login");
        servletContextHandler.addServlet(new ServletHolder(new LogoutServlet()), "/logout");


        servletContextHandler.addFilter(new FilterHolder(logFilter), "/*", EnumSet.of(DispatcherType.REQUEST));
        servletContextHandler.addFilter(new FilterHolder(authFilter), "/*", EnumSet.of(DispatcherType.REQUEST));

        Server server = new Server(8080);
        server.setHandler(servletContextHandler);

        server.start();

    }

    private static void flywayMigration(Properties dbProperties) throws IOException {

        Flyway flyway = Flyway.configure()
                .dataSource(dbProperties.getProperty("url"),
                        dbProperties.getProperty("user"),
                        dbProperties.getProperty("password"))
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
    }
}
