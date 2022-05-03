//package com.k0s.web.security;
//
//import com.k0s.dao.UserDao;
//import com.k0s.dao.jdbc.ConnectionFactory;
//import com.k0s.dao.jdbc.JdbcUserDao;
//import com.k0s.security.SecurityService;
//import com.k0s.security.SessionService;
//import com.k0s.service.UserService;
//import com.k0s.util.PropertiesReader;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.flywaydb.core.Flyway;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.sql.Connection;
//import java.util.Properties;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//class AuthFilterTest {
//
//    PropertiesReader propertiesReader;
//     ConnectionFactory connectionFactory;
////     Dao<User> userDao;
//     UserDao userDao;
//     UserService userService;
//     SessionService sessionService;
//
//     SecurityService securityService;
//     AuthFilter authFilter;
//
//
//     @BeforeEach
//    void setUp() throws ServletException {
//
//        propertiesReader = new PropertiesReader("test-db.properties");
//
//        Properties properties = propertiesReader.getProperties();
//        connectionFactory = new ConnectionFactory(properties);
//        userDao = new JdbcUserDao(connectionFactory);
//        userService = new UserService(userDao);
//        sessionService = new SessionService(userService, properties);
//        securityService = new SecurityService(sessionService, properties);
//        authFilter = new AuthFilter();
//        Connection connection = connectionFactory.getConnection();
//        assertNotNull(connection);
//
//        FilterConfig filterConfig = mock(FilterConfig.class);
//        authFilter.init(filterConfig);
//
//         Flyway flyway = Flyway.configure()
//                 .dataSource(connectionFactory)
//                 .load();
//         flyway.migrate();
//
//    }
//
//    @Test
//    @DisplayName("Authorization access forbidden test")
//    void testFilterForbidden() throws ServletException, IOException {
//        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
//        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
//        FilterChain filterChain = mock(FilterChain.class);
//
//        when(servletRequest.getServletPath()).thenReturn("/admin");
//        String token = securityService.login("user", "user");
//        when(servletRequest.getCookies()).thenReturn(new Cookie[]{new Cookie("user-token", token)});
//
//        authFilter.doFilter(servletRequest, servletResponse, filterChain);
//        verify(servletRequest, times(1)).getServletPath();
//        verify(servletRequest, times(1)).getCookies();
//        verify(servletRequest, times(1)).setAttribute(anyString(), any());
//        verify(filterChain, never()).doFilter(any(), any());
//
//    }
//
//    @Test
//    @DisplayName("Authorization access allow test")
//    void testFilterAccesOk() throws ServletException, IOException {
//        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
//        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
//        FilterChain filterChain = mock(FilterChain.class);
//
//        when(servletRequest.getServletPath()).thenReturn("/user");
//        String token = securityService.login("user", "user");
//        when(servletRequest.getCookies()).thenReturn(new Cookie[]{new Cookie("user-token", token)});
//
//        authFilter.doFilter(servletRequest, servletResponse, filterChain);
//        verify(servletRequest, times(1)).getServletPath();
//        verify(servletRequest, times(1)).getCookies();
//        verify(servletRequest, times(1)).setAttribute(anyString(), any());
////        verify(filterChain, times(1)).doFilter(any(), any());
//
//    }
//
//    @Test
//    @DisplayName("Authorization skip test")
//    void testFilterSkipAuth() throws ServletException, IOException {
//        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
//        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
//        FilterChain filterChain = mock(FilterChain.class);
//
//        when(servletRequest.getServletPath()).thenReturn("/resources");
//        String token = securityService.login("user", "user");
//        when(servletRequest.getCookies()).thenReturn(new Cookie[]{new Cookie("user-token", token)});
//
//        authFilter.doFilter(servletRequest, servletResponse, filterChain);
//        verify(servletRequest, times(1)).getServletPath();
//        verify(servletRequest, never()).getCookies();
//        verify(servletRequest, never()).setAttribute(anyString(), any());
//
//        verify(filterChain, times(1)).doFilter(any(), any());
//
//    }
//}