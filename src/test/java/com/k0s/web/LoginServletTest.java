package com.k0s.web;

import com.k0s.dao.Dao;

import com.k0s.dao.UserDao;
import com.k0s.dao.jdbc.ConnectionFactory;
import com.k0s.dao.jdbc.JdbcUserDao;
import com.k0s.entity.user.User;
import com.k0s.service.SecurityService;
import com.k0s.service.SessionService;
import com.k0s.service.UserService;
import com.k0s.util.PropertiesReader;
import com.k0s.web.filter.AuthFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class LoginServletTest {

    PropertiesReader propertiesReader;
    ConnectionFactory connectionFactory;
//    Dao<User> userDao;
    UserDao userDao;
    UserService userService;
    SessionService sessionService;

    SecurityService securityService;
    LoginServlet loginServlet;


    @BeforeEach
    void setUp() throws ServletException {

        propertiesReader = new PropertiesReader("test-db.properties");
        Properties properties = propertiesReader.getProperties();
        connectionFactory = new ConnectionFactory(properties);
        userDao = new JdbcUserDao(connectionFactory);
        userService = new UserService(userDao);
        sessionService = new SessionService(userService, properties);
        securityService = new SecurityService(sessionService, properties);
        loginServlet = new LoginServlet(securityService);
        Connection connection = connectionFactory.getConnection();
        assertNotNull(connection);

//        Flyway flyway = Flyway.configure()
//                .dataSource(connectionFactory)
//                .load();
//        flyway.migrate();

    }

    @Test
    @DisplayName("Authorization login servlet test")
    void testFilterForbidden() throws ServletException, IOException {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        HttpServletResponse servletResponse = mock(HttpServletResponse.class);


        when(servletRequest.getParameter("name")).thenReturn("user");
        when(servletRequest.getParameter("password")).thenReturn("user");

        loginServlet.doPost(servletRequest, servletResponse);
        verify(servletResponse, times(1)).addCookie(any());
        verify(servletResponse, times(1)).sendRedirect(anyString());
        verify(servletRequest, times(1)).getParameter("name");
        verify(servletRequest, times(1)).getParameter("password");

    }

}