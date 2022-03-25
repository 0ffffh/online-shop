package com.k0s.web.admin;

import com.k0s.dao.ProductDao;
import com.k0s.dao.jdbc.ConnectionFactory;
import com.k0s.dao.jdbc.JdbcProductDao;
import com.k0s.entity.Product;
import com.k0s.service.ProductService;
import com.k0s.util.PropertiesReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DeleteProductServletTest {

    PropertiesReader propertiesReader;
    ConnectionFactory connectionFactory;
    ProductDao<Product> jdbcProductDao;
    ProductService productService;
    DeleteProductServlet deleteProductServlet;
    AddProductServlet addProductServlet;

    HttpServletRequest servletRequest;
    HttpServletResponse servletResponse;


    @BeforeEach
    void setUp() throws SQLException {

        propertiesReader = new PropertiesReader("test-db.properties");
        propertiesReader.readProperties();
        Properties properties = propertiesReader.getProperties();
        connectionFactory = new ConnectionFactory(properties);
        jdbcProductDao = new JdbcProductDao(connectionFactory);
        productService = new ProductService(jdbcProductDao);
        deleteProductServlet = new DeleteProductServlet(productService);
        addProductServlet = new AddProductServlet(productService);


        servletRequest = mock(HttpServletRequest.class);
        servletResponse = mock(HttpServletResponse.class);
        Connection connection = connectionFactory.getConnection();
        assertNotNull(connection);

        Flyway flyway = Flyway.configure()
                .dataSource(properties.getProperty("url"),
                        properties.getProperty("user"),
                        properties.getProperty("password"))
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
    }

    @Test
    void doGet() throws IOException, ServletException {
        when(servletRequest.getParameter("id")).thenReturn("1").thenReturn("values");

        when(servletResponse.getWriter()).thenReturn(mock(PrintWriter.class));

        int size = productService.getAll().size();
        deleteProductServlet.doGet(servletRequest, servletResponse);
        assertTrue(productService.search("book").isEmpty());
        assertTrue(size > productService.getAll().size());


    }
}