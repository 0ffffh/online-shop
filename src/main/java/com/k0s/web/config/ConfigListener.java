package com.k0s.web.config;

import com.k0s.dao.ProductDao;
import com.k0s.dao.UserDao;
import com.k0s.dao.jdbc.DataSourceFactory;
import com.k0s.dao.jdbc.JdbcProductDao;
import com.k0s.dao.jdbc.JdbcUserDao;
import com.k0s.security.SecurityService;
import com.k0s.security.SessionService;
import com.k0s.service.ProductService;
import com.k0s.service.UserService;
import com.k0s.util.PropertiesReader;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
public class ConfigListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        log.info("Init application context");

        ServletContext servletContext = sce.getServletContext();
        String appPropertiesPath = servletContext.getInitParameter("properties");

        PropertiesReader propertiesReader = new PropertiesReader(appPropertiesPath);
        Properties properties = propertiesReader.getProperties();

        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        DataSource dataSource = dataSourceFactory.createDataSource(properties);
        ProductDao jdbcProductDao = new JdbcProductDao(dataSource);

        ProductService productService = new ProductService(jdbcProductDao);

        UserDao jdbcUserDao = new JdbcUserDao(dataSource);

        UserService userService = new UserService(jdbcUserDao);

        SessionService sessionService = new SessionService(userService, properties);

        SecurityService securityService = new SecurityService(sessionService, properties);

        servletContext.setAttribute("userService", userService);
        servletContext.setAttribute("productService", productService);
        servletContext.setAttribute("securityService", securityService);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Destroy application context");
    }
}
