//package com.k0s.security;
//
//import com.k0s.dao.UserDao;
//import com.k0s.dao.jdbc.DataSourceFactory;
//import com.k0s.dao.jdbc.JdbcUserDao;
//import com.k0s.security.SecurityService;
//import com.k0s.security.SessionService;
//import com.k0s.service.ServiceLocator;
//import com.k0s.service.UserService;
//import com.k0s.util.PropertiesReader;
//import com.k0s.web.security.AuthFilter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.FilterConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.flywaydb.core.Flyway;
//import org.junit.jupiter.api.*;
//
//import javax.sql.DataSource;
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.sql.Connection;
//import java.util.Properties;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//class AuthFilterTest {
//
//    private static PropertiesReader propertiesReader;
//    private static DataSourceFactory dataSourceFactory;
//    private static DataSource dataSource;
//    private static JdbcUserDao jdbcUserDao;
//    private static UserService userService;
//    private static SecurityService securityService;
//    private static SessionService sessionService;
//    private static AuthFilter authFilter;
//
//
//    @BeforeAll
//    @Disabled
//    static void setUp() throws ServletException, NoSuchFieldException, IllegalAccessException {
//
//
//        propertiesReader = new PropertiesReader("test-db.properties");
//
//        Properties properties = propertiesReader.getProperties();
//        dataSourceFactory = new DataSourceFactory();
//        dataSource = dataSourceFactory.createDataSource(properties);
//        jdbcUserDao = new JdbcUserDao(dataSource);
//        userService = new UserService(jdbcUserDao);
//        sessionService = new SessionService(userService, properties);
//
//        securityService = new SecurityService(sessionService, properties);
//
//
//
////        authFilter = new AuthFilter();
//
//
////        FilterConfig filterConfig = mock(FilterConfig.class);
////        authFilter.init(filterConfig);
//
//        Flyway flyway = Flyway.configure()
//                .dataSource(dataSource)
//                .load();
//        flyway.migrate();
//    }
//
//
//    @Test
//    @Disabled
//    @DisplayName("Authorization access forbidden test")
//    void testFilterForbidden() throws ServletException, IOException {
//        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
//        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
//        FilterChain filterChain = mock(FilterChain.class);
//
//        when(servletRequest.getServletPath()).thenReturn("/admin");
//        Session session = securityService.login("user", "user");
//        when(servletRequest.getCookies()).thenReturn(new Cookie[]{new Cookie("user-token", session.getToken())});
//
//        authFilter.doFilter(servletRequest, servletResponse, filterChain);
//        verify(servletRequest, times(1)).getServletPath();
//        verify(servletRequest, times(1)).getCookies();
//        verify(servletRequest, times(1)).setAttribute(anyString(), any());
//        verify(filterChain, never()).doFilter(any(), any());
//
//    }
//
//
//    @Test
//    @Disabled
//    @DisplayName("Authorization access allow test")
//    void testFilterAccesOk() throws ServletException, IOException {
//        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
//        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
//        FilterChain filterChain = mock(FilterChain.class);
//
//        when(servletRequest.getServletPath()).thenReturn("/user");
//        Session session = securityService.login("user", "user");
//        when(servletRequest.getCookies()).thenReturn(new Cookie[]{new Cookie("user-token", session.getToken())});
//
//        authFilter.doFilter(servletRequest, servletResponse, filterChain);
//        verify(servletRequest, times(1)).getServletPath();
//        verify(servletRequest, times(1)).getCookies();
//        verify(servletRequest, times(1)).setAttribute(anyString(), any());
////        verify(filterChain, times(1)).doFilter(any(), any());
//
//    }
//
//
//    @Test
//    @Disabled
//    @DisplayName("Authorization skip test")
//    void testFilterSkipAuth() throws ServletException, IOException {
//        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
//        HttpServletResponse servletResponse = mock(HttpServletResponse.class);
//        FilterChain filterChain = mock(FilterChain.class);
//
//        when(servletRequest.getServletPath()).thenReturn("/resources");
//        Session session = securityService.login("user", "user");
//        when(servletRequest.getCookies()).thenReturn(new Cookie[]{new Cookie("user-token", session.getToken())});
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