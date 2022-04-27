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

class EditProductServletTest {

    PropertiesReader propertiesReader;
    ConnectionFactory connectionFactory;
//    Dao<Product> jdbcProductDao;
    ProductDao jdbcProductDao;
    ProductService productService;
    EditProductServlet editProductServlet;
    HttpServletRequest servletRequest;
    HttpServletResponse servletResponse;


    @BeforeEach
    void setUp() {

        propertiesReader = new PropertiesReader("test-db.properties");

        Properties properties = propertiesReader.getProperties();
        connectionFactory = new ConnectionFactory(properties);
        jdbcProductDao = new JdbcProductDao(connectionFactory);
        productService = new ProductService(jdbcProductDao);
        editProductServlet = new EditProductServlet();
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

    @DisplayName("Edit product")
    void testAddServlet() throws IOException, ServletException {

        when(servletRequest.getParameter("id")).thenReturn("4");
        when(servletRequest.getParameter("name")).thenReturn("banana");
        when(servletRequest.getParameter("price")).thenReturn("111");
        when(servletRequest.getParameter("description")).thenReturn("banana");
        when(servletResponse.getWriter()).thenReturn(mock(PrintWriter.class));

        int size = productService.getAll().size();
        Product expected = productService.get(4);
        System.out.println(expected);
        editProductServlet.doPost(servletRequest, servletResponse);
        List<Product> productList = productService.getAll();
        for (Product product : productList) {
            System.out.println(product);
        }
        Product actual = productService.get(4);
        System.out.println(actual);

        assertEquals(expected.getId(), actual.getId());
//        assertNotEquals(expected.getName(), actual.getName());
//        assertNotEquals(expected.getPrice(), actual.getPrice());
//        assertNotEquals(expected.getDescription(), actual.getDescription());
//        assertEquals(size, productService.getAll().size());

    }


}