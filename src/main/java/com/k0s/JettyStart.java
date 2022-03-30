package com.k0s;

import com.k0s.dao.Dao;
import com.k0s.dao.jdbc.ConnectionFactory;
import com.k0s.dao.jdbc.JdbcProductDao;
import com.k0s.dao.jdbc.JdbcUserDao;
import com.k0s.entity.Product;
import com.k0s.entity.user.User;
import com.k0s.service.ProductService;
import com.k0s.service.SecurityService;
import com.k0s.service.UserService;
import com.k0s.util.PropertiesReader;
import com.k0s.web.*;
import com.k0s.web.admin.AddProductServlet;
import com.k0s.web.admin.DeleteProductServlet;
import com.k0s.web.admin.EditProductServlet;
import com.k0s.web.filter.AuthFilter;
import com.k0s.web.user.AddProductToCartServlet;
import com.k0s.web.user.CartServlet;
import com.k0s.web.user.ClearProductCartServlet;
import com.k0s.web.user.DeleteProductFromCartServlet;
import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;
import java.util.EnumSet;
import java.util.Properties;

@Slf4j
public class JettyStart {
    private static final String DEFAULT_APP_PROPERTIES = "application.properties";
    private static final int DEFAULT_SERVER_PORT = 8080;


    public static void main(String[] args) throws Exception {

        PropertiesReader propertiesReader = new PropertiesReader(DEFAULT_APP_PROPERTIES);
        Properties properties = propertiesReader.getProperties();

        ConnectionFactory connectionFactory = new ConnectionFactory(properties);

        flywayMigration(connectionFactory);

        Dao<Product> jdbcProductDao = new JdbcProductDao(connectionFactory);
        ProductService productService = new ProductService(jdbcProductDao);

        Dao<User> jdbcUserDao = new JdbcUserDao(connectionFactory);
        UserService userService = new UserService(jdbcUserDao);

        SecurityService securityService = new SecurityService(userService, properties);

        AuthFilter authFilter = new AuthFilter(securityService);


        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

//        index
        servletContextHandler.addServlet(new ServletHolder(new IndexServlet(productService)), "");

//        admin
        servletContextHandler.addServlet(new ServletHolder(new AddProductServlet(productService)), "/admin/product/add");
        servletContextHandler.addServlet(new ServletHolder(new EditProductServlet(productService)), "/admin/product/edit");
        servletContextHandler.addServlet(new ServletHolder(new DeleteProductServlet(productService)), "/admin/product/delete");

//        user
        servletContextHandler.addServlet(new ServletHolder(new AddProductToCartServlet(productService)), "/user/product/add");
        servletContextHandler.addServlet(new ServletHolder(new DeleteProductFromCartServlet()), "/user/product/delete");
        servletContextHandler.addServlet(new ServletHolder(new CartServlet()), "/user/cart");
        servletContextHandler.addServlet(new ServletHolder(new ClearProductCartServlet()), "/user/cart/clear");


        servletContextHandler.addServlet(new ServletHolder(new SearchServlet(productService)), "/search");
        servletContextHandler.addServlet(new ServletHolder(new ResourcesServlet()), "/resources/*");
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(securityService)), "/login");
        servletContextHandler.addServlet(new ServletHolder(new LogoutServlet(securityService)), "/logout");

//        filter
        servletContextHandler.addFilter(new FilterHolder(authFilter), "/*", EnumSet.of(DispatcherType.REQUEST));

        Server server = new Server(getPort(properties.getProperty("server.port")));

        server.setHandler(servletContextHandler);

        server.start();
        server.join();

    }


    private static void flywayMigration(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
    }

    private static int getPort(String configPort) {
        try {
            String port = System.getenv("PORT");
            if (port == null) {
                port = configPort;
            }
            return Integer.parseInt(port);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
        }
        return DEFAULT_SERVER_PORT;
    }
}
