package com.k0s.web.admin;

import com.k0s.entity.Product;
import com.k0s.service.ProductService;
import com.k0s.service.ServiceLocator;
import com.k0s.web.util.PageGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class EditProductServlet extends HttpServlet {
    private static final String HTML_PAGE = "editProduct.html";
    private final ProductService productService = ServiceLocator.getService(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Product product = productService.get(Long.parseLong(req.getParameter("id")));
            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put("id", product.getId());
            pageVariables.put("name", product.getName());
            pageVariables.put("price", product.getPrice());
            pageVariables.put("creation_date", product.getCreationDate());
            pageVariables.put("description", product.getDescription());

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(PageGenerator.getInstance().getPage(HTML_PAGE, pageVariables));

        } catch (Exception e) {
            log.info("Edit product servlet ", e);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (!isValidRequest(req)) {
            resp.sendRedirect(req.getHeader("referer"));
        } else {
            try {
                Product product = new Product();
                product.setId(Long.parseLong(req.getParameter("id")));
                product.setName(req.getParameter("name"));
                product.setPrice(Double.parseDouble(req.getParameter("price")));
                product.setCreationDate(LocalDateTime.now());
                product.setDescription(req.getParameter("description"));
                productService.update(product);
//                productService.update(
//                        Product.builder()
//                                .id(Long.parseLong(req.getParameter("id")))
//                                .name(req.getParameter("name"))
//                                .price(Double.parseDouble(req.getParameter("price")))
//                                .creationDate(LocalDateTime.now())
//                                .description(req.getParameter("description"))
//                                .build());
            } catch (Exception e) {
                log.info("Edit product error ", e);
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
