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
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DeleteProductServletTest {

    PropertiesReader propertiesReader;
    ConnectionFactory connectionFactory;
//    Dao<Product> jdbcProductDao;
    ProductDao jdbcProductDao;

    ProductService productService;
    DeleteProductServlet deleteProductServlet;
    AddProductServlet addProductServlet;

    HttpServletRequest servletRequest;
    HttpServletResponse servletResponse;


    @BeforeEach
    void setUp() throws SQLException {

//        propertiesReader = new PropertiesReader("test-db.properties");
        propertiesReader = new PropertiesReader("application.properties");

        Properties properties = propertiesReader.getProperties();
        connectionFactory = new ConnectionFactory(properties);
        jdbcProductDao = new JdbcProductDao(connectionFactory);
        productService = new ProductService(jdbcProductDao);
        deleteProductServlet = new DeleteProductServlet();
        addProductServlet = new AddProductServlet();


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
    void doGet() throws IOException, ServletException {
//        when(servletRequest.getParameter("id")).thenReturn("1").thenReturn("values");
//
//        when(servletResponse.getWriter()).thenReturn(mock(PrintWriter.class));
//
//        int size = productService.getAll().size();
//        deleteProductServlet.doGet(servletRequest, servletResponse);
//        assertTrue(productService.search("book").isEmpty());
//        assertTrue(size > productService.getAll().size());
        List<Product> list = productService.search("KKKK");
        System.out.println(list.size());

        for (Product product : list) {
            productService.remove(product.getId());
        }


    }
}