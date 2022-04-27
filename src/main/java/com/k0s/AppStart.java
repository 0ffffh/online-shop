//package com.k0s;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.eclipse.jetty.server.Server;
//import org.eclipse.jetty.webapp.WebAppContext;
//import org.flywaydb.core.Flyway;
//
//import javax.sql.DataSource;
//
//@Slf4j
//public class JettyStart {
//    private static final String DEFAULT_APP_PROPERTIES = "application.properties";
//    private static final int DEFAULT_SERVER_PORT = 8080;
//
//
//    public static void main(String[] args) throws Exception {
//
////        PropertiesReader propertiesReader = new PropertiesReader(DEFAULT_APP_PROPERTIES);
////        Properties properties = propertiesReader.getProperties();
////
////        ConnectionFactory connectionFactory = new ConnectionFactory(properties);
////
////        flywayMigration(connectionFactory);
////        flywayMigration(ServiceLocator.getService(ConnectionFactory.class));
////
//////        Dao<Product> jdbcProductDao = new JdbcProductDao(connectionFactory);
////        ProductDao jdbcProductDao = new JdbcProductDao(connectionFactory);
////        ProductService productService = new ProductService(jdbcProductDao);
////
//////        Dao<User> jdbcUserDao = new JdbcUserDao(connectionFactory);
////        UserDao jdbcUserDao = new JdbcUserDao(connectionFactory);
////        UserService userService = new UserService(jdbcUserDao);
////        SessionService sessionService = new SessionService(userService, properties);
////
//////        SecurityService securityService = new SecurityService(userService, properties);
////        SecurityService securityService = new SecurityService(sessionService, properties);
////
////        AuthFilter authFilter = new AuthFilter(securityService);
////
////
////        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
////
//////        index
////        servletContextHandler.addServlet(new ServletHolder(new IndexServlet(productService)), "");
////
//////        admin
////        servletContextHandler.addServlet(new ServletHolder(new AddProductServlet(productService)), "/admin/product/add");
////        servletContextHandler.addServlet(new ServletHolder(new EditProductServlet(productService)), "/admin/product/edit");
////        servletContextHandler.addServlet(new ServletHolder(new DeleteProductServlet(productService)), "/admin/product/delete");
////
//////        user
////        servletContextHandler.addServlet(new ServletHolder(new AddProductToCartServlet(productService)), "/user/product/add");
////        servletContextHandler.addServlet(new ServletHolder(new DeleteProductFromCartServlet()), "/user/product/delete");
////        servletContextHandler.addServlet(new ServletHolder(new CartServlet()), "/user/cart");
////        servletContextHandler.addServlet(new ServletHolder(new ClearProductCartServlet()), "/user/cart/clear");
////
////
////        servletContextHandler.addServlet(new ServletHolder(new SearchServlet(productService)), "/search");
////        servletContextHandler.addServlet(new ServletHolder(new ResourcesServlet()), "/resources/*");
////        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(securityService)), "/login");
////        servletContextHandler.addServlet(new ServletHolder(new LogoutServlet(securityService)), "/logout");
////
//////        filter
////        servletContextHandler.addFilter(new FilterHolder(authFilter), "/*", EnumSet.of(DispatcherType.REQUEST));
////
////        Server server = new Server(getPort(properties.getProperty("server.port")));
////
////        server.setHandler(servletContextHandler);
////
////        server.start();
////        server.join();
//        WebAppContext webAppContext = new WebAppContext();
//
////        URL webAppDir = Thread.currentThread().getContextClassLoader().getResource("src/main/webapp");
////        webAppContext.setResourceBase(webAppDir.toURI().toString());
//////
//////        // if setDescriptor set null or don't used jetty looking for /WEB-INF/web.xml under resource base
////        webAppContext.setDescriptor(webAppDir.toURI().toString() + "web.xml");
//
////        webAppContext.setDescriptor(".src/main/webapp/WEB-INF/web.xml");
////        webAppContext.setDescriptor(webAppDir);
//
////        File file = new File("webapp/WEB-INF/web.xml");
////        String webappPath = file.getAbsolutePath();
////        System.out.println(webappPath);
//        webAppContext.setResourceBase("src/main/webapp");
//        webAppContext.setDescriptor("src/main/webapp/WEB-INF/web.xml");
////        webAppContext.setDescriptor(webappPath);
//
//        Server server = new Server(8080);
//        server.setHandler(webAppContext);
//        server.start();
////        server.join();
//
//    }
//
//
//    private static void flywayMigration(DataSource dataSource) {
//        Flyway flyway = Flyway.configure()
//                .dataSource(dataSource)
//                .locations("classpath:/db/migration")
//                .load();
//        flyway.migrate();
//    }
//
//    private static int getPort(String configPort) {
//        try {
//            String port = System.getenv("PORT");
//            if (port == null) {
//                port = configPort;
//            }
//            return Integer.parseInt(port);
//        } catch (NumberFormatException e) {
//            log.error("Get port error ", e);
//        }
//        return DEFAULT_SERVER_PORT;
//    }
//}
