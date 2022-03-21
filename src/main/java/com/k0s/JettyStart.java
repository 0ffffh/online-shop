package com.k0s;

import com.k0s.dao.jdbc.ConnectionFactory;
import com.k0s.dao.jdbc.JdbcProductDao;
import com.k0s.dao.jdbc.JdbcUserDao;
import com.k0s.service.ProductService;
import com.k0s.service.SecurityService;
import com.k0s.service.UserService;
import com.k0s.util.PropertiesReader;
import com.k0s.web.*;
import com.k0s.web.admin.AddServlet;
import com.k0s.web.admin.DeleteServlet;
import com.k0s.web.admin.EditServlet;
import com.k0s.web.admin.ManageServlet;
import com.k0s.web.filter.AuthFilter;
import com.k0s.web.filter.LogFilter;
import com.k0s.web.user.AddProductToCartServlet;
import com.k0s.web.user.CartServlet;
import com.k0s.web.user.DeleteProductFromCartServlet;
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

        SecurityService securityService = new SecurityService(userService);

        AuthFilter authFilter = new AuthFilter(securityService);
        LogFilter logFilter = new LogFilter();

        flywayMigration(propertiesReader.getProperties());

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

//        index
        servletContextHandler.addServlet(new ServletHolder(new IndexServlet(productService)), "");

//        admin
        servletContextHandler.addServlet(new ServletHolder(new AddServlet(productService)), "/admin/add");
        servletContextHandler.addServlet(new ServletHolder(new EditServlet(productService)), "/admin/edit");
        servletContextHandler.addServlet(new ServletHolder(new DeleteServlet(productService)), "/admin/delete");

//        user
        servletContextHandler.addServlet(new ServletHolder(new AddProductToCartServlet(productService)), "/user/product/add");
        servletContextHandler.addServlet(new ServletHolder(new DeleteProductFromCartServlet(productService)), "/user/product/delete");
        servletContextHandler.addServlet(new ServletHolder(new CartServlet()), "/user/cart");


        servletContextHandler.addServlet(new ServletHolder(new SearchServlet(productService)), "/search");
        servletContextHandler.addServlet(new ServletHolder(new ResourcesServlet()), "/resources/*");
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(securityService)), "/login");
        servletContextHandler.addServlet(new ServletHolder(new LogoutServlet()), "/logout");


//        filter
        servletContextHandler.addFilter(new FilterHolder(logFilter), "/*", EnumSet.of(DispatcherType.REQUEST));
        servletContextHandler.addFilter(new FilterHolder(authFilter), "/*", EnumSet.of(DispatcherType.REQUEST));
//        servletContextHandler.addFilter(new FilterHolder(authFilter), "/admin/*", EnumSet.of(DispatcherType.REQUEST));
//        servletContextHandler.addFilter(new FilterHolder(authFilter), "/user/*", EnumSet.of(DispatcherType.REQUEST));

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
