package com.k0s.web.admin;

import com.k0s.dao.Dao;
import com.k0s.dao.jdbc.ConnectionFactory;
import com.k0s.dao.jdbc.JdbcProductDao;
import com.k0s.entity.Product;
import com.k0s.service.ProductService;
import com.k0s.util.PropertiesReader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AddProductServletTest {

    PropertiesReader propertiesReader;
    ConnectionFactory connectionFactory;
    Dao<Product> jdbcProductDao;
    ProductService productService;
    AddProductServlet addProductServlet;
    HttpServletRequest servletRequest;
    HttpServletResponse servletResponse;


    @BeforeEach
    void setUp() {

        propertiesReader = new PropertiesReader("test-db.properties");

        Properties properties = propertiesReader.getProperties();
        connectionFactory = new ConnectionFactory(properties);
        jdbcProductDao = new JdbcProductDao(connectionFactory);
        productService = new ProductService(jdbcProductDao);
        addProductServlet = new AddProductServlet(productService);
        servletRequest = mock(HttpServletRequest.class);
        servletResponse = mock(HttpServletResponse.class);
        Connection connection = connectionFactory.getConnection();
        assertNotNull(connection);

        Flyway flyway = Flyway.configure()
                .dataSource(connectionFactory)
                .load();
        flyway.migrate();
    }

    @Test
    @DisplayName("Add product")
    void testAddServlet() throws IOException {

        when(servletRequest.getParameter("name")).thenReturn("banana");
        when(servletRequest.getParameter("price")).thenReturn("111");
        when(servletRequest.getParameter("description")).thenReturn("banana");
        when(servletResponse.getWriter()).thenReturn(mock(PrintWriter.class));

        int size = productService.getAll().size();
        addProductServlet.doPost(servletRequest, servletResponse);
        List<Product> productList = productService.getAll();
        for (Product product : productList) {
            System.out.println(product);
        }
        Product product = productService.search("banana").get(0);

        assertEquals("banana", product.getName());
        assertEquals("banana", product.getDescription());
        assertTrue(size < productService.getAll().size());

    }


}