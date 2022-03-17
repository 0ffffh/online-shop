package com.k0s.web;

import com.k0s.entity.Product;
import com.k0s.service.ProductService;
import com.k0s.util.PageGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditServlet extends HttpServlet {

    private final ProductService productService;

    public EditServlet(ProductService productService){
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
            resp.getWriter().println(PageGenerator.getInstance().getPage("edit.html", pageVariables));

        } catch (NumberFormatException e) {
            e.printStackTrace();
            setError(resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {


        try {
            Product product = new Product();
            product.setId(Long.parseLong(req.getParameter("id")));
            product.setName(req.getParameter("name"));
            product.setPrice(Double.parseDouble(req.getParameter("price")));
            product.setDescription(req.getParameter("description"));

            productService.update(product);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            setError(resp);
        }
        doGet(req, resp);
    }

    private void setError(HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println("500 Internal server error.");
    }

}
