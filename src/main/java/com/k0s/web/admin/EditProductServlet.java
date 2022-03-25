package com.k0s.web.admin;

import com.k0s.entity.Product;
import com.k0s.service.ProductService;
import com.k0s.util.PageGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class EditProductServlet extends HttpServlet {

    private final ProductService productService;

    public EditProductServlet(ProductService productService){
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
            resp.getWriter().println(PageGenerator.getInstance().getPage("editProduct.html", pageVariables));

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (!isValidRequest(req)){
            doGet(req, resp);
        }
        try {
            productService.update(new Product(Long.parseLong(req.getParameter("id")),
                    req.getParameter("name"),
                    Double.parseDouble(req.getParameter("price")),
                    LocalDateTime.now(),
                    req.getParameter("description")));
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        doGet(req, resp);
    }


    private boolean isValidRequest(HttpServletRequest req){
        String name = req.getParameter("name");
        String price = req.getParameter("price");
        return name != null && name.matches("^[a-z0-9_-]{3,25}$") &&
                price != null && price.length() > 0 &&
                price.matches("[+]?\\d*\\.?\\d+");
    }


}
