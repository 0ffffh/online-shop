package com.k0s.web.admin;

import com.k0s.entity.Product;
import com.k0s.service.ProductService;
import com.k0s.service.ServiceLocator;
import com.k0s.web.util.PageGenerator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AddProductServlet extends HttpServlet {
    private static final String HTML_PAGE = "addProduct.html";
    private final ProductService productService = ServiceLocator.getService(ProductService.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Map<String, Object> pageVariables = new HashMap<>();

            pageVariables.put("products", productService.getAll());

            resp.setStatus(HttpServletResponse.SC_OK);
//            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println(PageGenerator.getInstance().getPage(HTML_PAGE, pageVariables));
        } catch (Exception e) {
            log.info("Add product page servlet error: ", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!isValidRequest(req)) {
//            doGet(req, resp);
            resp.sendRedirect(req.getHeader("referer"));
        } else {
            try {
                Product product = new Product();
                product.setName(req.getParameter("name"));
                product.setPrice(Double.parseDouble(req.getParameter("price")));
                product.setCreationDate(LocalDateTime.now());
                product.setDescription(req.getParameter("description"));
                productService.add(product);
//                productService.add(Product.builder()
//                        .name(req.getParameter("name"))
//                        .price(Double.parseDouble(req.getParameter("price")))
//                        .creationDate(LocalDateTime.now())
//                        .description(req.getParameter("description"))
//                        .build());
            } catch (Exception e) {
                log.info("Add product error ",e);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
            resp.sendRedirect(req.getHeader("referer"));
        }
    }

    private boolean isValidRequest(HttpServletRequest req) {
        String name = req.getParameter("name");
        String price = req.getParameter("price");
        return name != null &&
                price != null && price.length() > 0 &&
                price.matches("[+]?\\d*\\.?\\d+");
    }

}
