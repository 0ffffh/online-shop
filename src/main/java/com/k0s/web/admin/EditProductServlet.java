package com.k0s.web.admin;

import com.k0s.entity.Product;
import com.k0s.service.ProductService;
import com.k0s.util.PageGenerator;
import jakarta.servlet.RequestDispatcher;
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
    private final ProductService productService;

    public EditProductServlet(ProductService productService) {
        this.productService = productService;
    }

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

            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(PageGenerator.getInstance().getPage(HTML_PAGE, pageVariables));

        } catch (Exception e) {
            log.error("Exception ", e);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(req.getServletPath() + "?" + req.getQueryString());

        System.out.println(req.getServletPath() + "?" + req.getQueryString());

        if (!isValidRequest(req)) {
            resp.sendRedirect("/admin/product/edit?id="+req.getParameter("id"));
        } else {
            try {
                productService.update(
                        Product.builder()
                                .id(Long.parseLong(req.getParameter("id")))
                                .name(req.getParameter("name"))
                                .price(Double.parseDouble(req.getParameter("price")))
                                .creationDate(LocalDateTime.now())
                                .description(req.getParameter("description"))
                                .build());
            } catch (Exception e) {
                log.error("Edit product error ", e);
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
//            doGet(req, resp);
            resp.sendRedirect("/admin/product/edit?id="+req.getParameter("id"));

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
