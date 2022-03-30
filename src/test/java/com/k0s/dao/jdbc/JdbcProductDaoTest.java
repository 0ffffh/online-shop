package com.k0s.dao.jdbc;

import com.k0s.dao.Dao;
import com.k0s.entity.Product;
import com.k0s.util.PropertiesReader;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class JdbcProductDaoTest {

    LocalDateTime now = LocalDateTime.now();

    Connection conn = mock(Connection.class);

    ConnectionFactory mockConnectionFactory = mock(ConnectionFactory.class);
    PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
    ResultSet mockResultSet = mock(ResultSet.class);


    PropertiesReader propertiesReader;
    ConnectionFactory connectionFactory;
    Dao<Product> jdbcProductDao;


    @BeforeEach
    void setUp() throws SQLException {

        propertiesReader = new PropertiesReader("test-db.properties");

        Properties properties = propertiesReader.getProperties();
        connectionFactory = new ConnectionFactory(properties);
        jdbcProductDao = new JdbcProductDao(connectionFactory);
        Connection connection = connectionFactory.getConnection();
        assertNotNull(connection);

        Flyway flyway = Flyway.configure()
                .dataSource(connectionFactory)
                .load();
        flyway.migrate();


        when(mockConnectionFactory.getConnection()).thenReturn(conn);
        when(conn.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet).thenThrow(new RuntimeException());
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);

        when(mockResultSet.getString("name")).thenReturn("name");
        when(mockResultSet.getString("description")).thenReturn("description");
        when(mockResultSet.getLong("id")).thenReturn(Long.valueOf(1));
        when(mockResultSet.getDouble("price")).thenReturn(111.1);
        when(mockResultSet.getTimestamp("creation_date")).thenReturn(Timestamp.valueOf(now));

    }


    @Test
    @DisplayName("JdbcProductDao getAll() mocked test")
    void getAll() throws SQLException {
        Dao<Product> jdbcProductDao = new JdbcProductDao(mockConnectionFactory);

        List<Product> productList = jdbcProductDao.getAll();

        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(2)).next();

        assertEquals(1, productList.size());
        assertThrows(RuntimeException.class, () -> jdbcProductDao.getAll());
    }

    @Test
    @DisplayName("JdbcProductDao getAll() test")
    void getAllH2() throws SQLException {
        List<Product> productList = jdbcProductDao.getAll();
        assertTrue(productList.size() > 0);

    }

    @Test
    @DisplayName("JdbcProductDao get() mocked test")
    void get() throws SQLException {

        Dao<Product> jdbcProductDao = new JdbcProductDao(mockConnectionFactory);

        Product product = jdbcProductDao.get(1);
        assertNotNull(product);
        assertEquals("name", product.getName());
        assertEquals("description", product.getDescription());

        verify(mockPreparedStatement, times(1)).setLong(anyInt(), anyLong());
        verify(mockPreparedStatement, times(1)).executeQuery();
        verify(mockResultSet, times(1)).next();

        assertThrows(RuntimeException.class, () -> jdbcProductDao.get(2));
    }

    @Test
    @DisplayName("JdbcProductDao get() test")
    void getH2() throws SQLException {
        List<Product> list = jdbcProductDao.getAll();
        for (Product product : list) {
            System.out.println(product);
        }

        Product product = jdbcProductDao.get(6);
        assertNotNull(product);
        assertEquals("Bread", product.getName());
        assertEquals("Bread", product.getDescription());

        assertThrows(RuntimeException.class, () -> jdbcProductDao.get(1234567));

    }

    @Test
    @DisplayName("JdbcProductDao remove() mocked test")
    void remove() throws SQLException {

        Dao<Product> jdbcProductDao = new JdbcProductDao(mockConnectionFactory);
        jdbcProductDao.remove(1);

        verify(mockPreparedStatement, times(1)).setLong(1, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();

    }

    @Test
    @DisplayName("JdbcProductDao remove()  test")
    void removeH2() throws SQLException {

        List<Product> productList = jdbcProductDao.getAll();
        int size = productList.size();
        jdbcProductDao.remove(2);

        productList = jdbcProductDao.getAll();

        assertEquals(size - 1, productList.size());
    }

    @Test
    @DisplayName("JdbcProductDao update() mocked test")
    void update() throws SQLException {

//        Product product = new Product(1, "product", 111.1, now, "description");
        Product product = Product.builder()
                .name("product")
                .price(111.11)
                .description("new product")
                .creationDate(LocalDateTime.now())
                .build();
        Dao<Product> jdbcProductDao = new JdbcProductDao(mockConnectionFactory);

        jdbcProductDao.update(product);

        verify(mockPreparedStatement, times(1)).setString(1, product.getName());
        verify(mockPreparedStatement, times(1)).setDouble(2, product.getPrice());
        verify(mockPreparedStatement, times(1)).setTimestamp(3, Timestamp.valueOf(product.getCreationDate()));
        verify(mockPreparedStatement, times(1)).setString(4, product.getDescription());
        verify(mockPreparedStatement, times(1)).setLong(5, product.getId());
        verify(mockPreparedStatement, times(1)).executeUpdate();

        assertThrows(NullPointerException.class, () -> jdbcProductDao.update(null));

    }

    @Test
    @DisplayName("JdbcProductDao get() test")
    void updateH2() throws SQLException {

        Product product = jdbcProductDao.get(8);

        String oldName = product.getName();

        product.setName("tttttttt");

        jdbcProductDao.update(product);

        assertNotEquals(oldName, jdbcProductDao.get(8).getName());
        assertThrows(NullPointerException.class, () -> jdbcProductDao.update(null));

    }

    @Test
    @DisplayName("JdbcProductDao get() test")
    void search() throws SQLException {

        List<Product> productList = jdbcProductDao.search("searchValue");

        assertEquals(0, productList.size());

        productList = jdbcProductDao.search("bread");
        assertEquals(2, productList.size());

    }

    @Test
    @DisplayName("JdbcProductDao add()  test")
    void add() throws SQLException {

        Product product = Product.builder()
                .name("product")
                .price(111.11)
                .description("new product")
                .creationDate(LocalDateTime.now())
                .build();
//                new Product("product", 111.11, "new product", LocalDateTime.now());

        int oldSize = jdbcProductDao.getAll().size();

        jdbcProductDao.add(product);

        int newSize = jdbcProductDao.getAll().size();

        assertNotEquals(oldSize, newSize);
        assertEquals(oldSize, newSize - 1);

        Product addedProduct = jdbcProductDao.search("new product").get(0);
        assertEquals(product.getName(), addedProduct.getName());
        assertEquals(product.getDescription(), addedProduct.getDescription());
        assertThrows(NullPointerException.class, () -> jdbcProductDao.add(null));

    }
}