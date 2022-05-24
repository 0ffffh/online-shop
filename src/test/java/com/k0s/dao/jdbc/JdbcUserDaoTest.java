//package com.k0s.dao.jdbc;
//
//import com.k0s.security.user.Role;
//import com.k0s.security.user.User;
//import org.flywaydb.core.Flyway;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import javax.sql.DataSource;
//
//import java.util.Properties;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class JdbcUserDaoTest {
//
//    private static PropertiesReader propertiesReader;
//    private static DataSourceFactory dataSourceFactory;
//    private static DataSource dataSource;
//    private static JdbcUserDao jdbcUserDao;
//
//    @BeforeAll
//    static void setUp() {
//
//
//        propertiesReader = new PropertiesReader("test-db.properties");
//
//        Properties properties = propertiesReader.getProperties();
//        dataSourceFactory = new DataSourceFactory();
//        dataSource = dataSourceFactory.createDataSource(properties);
//        jdbcUserDao = new JdbcUserDao(dataSource);
//
//
//        Flyway flyway = Flyway.configure()
//                .dataSource(dataSource)
//                .load();
//        flyway.migrate();
//    }
//
//    @Test
//    void get() {
//        DataSource dataSource = mock(DataSource.class);
//        JdbcUserDao jdbcUserDao = mock(JdbcUserDao.class);
//        User user = new User();
//        when(jdbcUserDao.get(any())).thenReturn(user);
//
//        jdbcUserDao.get("root");
//
//        verify(jdbcUserDao).get("root");
//    }
//
//    @Test
//    void getUser() {
//
//
//        User user = jdbcUserDao.get("root");
//
//        assertEquals("root", user.getName());
//        assertEquals(Role.ADMIN, user.getRole());
//
//    }
//}