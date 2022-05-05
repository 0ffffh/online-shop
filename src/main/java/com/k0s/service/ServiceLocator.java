package com.k0s.service;

import com.k0s.dao.ProductDao;
import com.k0s.dao.UserDao;
import com.k0s.dao.jdbc.DataSourceFactory;
import com.k0s.dao.jdbc.JdbcProductDao;
import com.k0s.dao.jdbc.JdbcUserDao;
import com.k0s.security.SecurityService;
import com.k0s.security.SessionService;
import com.k0s.util.PropertiesReader;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceLocator {
    private static final Map<Class<?>, Object> SERVICES = new ConcurrentHashMap<>();
    private static final String DEFAULT_APP_PROPERTIES = "application.properties";


    static {
        PropertiesReader propertiesReader = new PropertiesReader(DEFAULT_APP_PROPERTIES);
        Properties properties = propertiesReader.getProperties();

        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        DataSource dataSource = dataSourceFactory.createDataSource(properties);
        ProductDao jdbcProductDao = new JdbcProductDao(dataSource);

        ProductService productService = new ProductService(jdbcProductDao);

        UserDao jdbcUserDao = new JdbcUserDao(dataSource);

        UserService userService = new UserService(jdbcUserDao);

        SessionService sessionService = new SessionService(userService, properties);

        SecurityService securityService = new SecurityService(sessionService, properties);

        addService(ProductService.class, productService);
        addService(SecurityService.class, securityService);
    }

    public static <T> T getService(Class<T> serviceClass){
        return serviceClass.cast(SERVICES.get(serviceClass));
    }
    public static void addService(Class<?> serviceClass, Object service){
        SERVICES.put(serviceClass, service);
    }
}
