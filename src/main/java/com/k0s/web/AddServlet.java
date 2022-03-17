package com.k0s.web;

import com.k0s.entity.Product;
import com.k0s.service.ProductService;
import com.k0s.util.PageGenerator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddServlet extends HttpServlet {

    private final ProductService productService;

    public AddServlet(ProductService productService){
        this.productService = productService;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Map<String, Object> pageVariables = new HashMap<>();
            List<Product> list = productService.getAll();
            for (Product product : list) {
                System.out.println(product);
            }

            pageVariables.put("products", productService.getAll());

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().println(PageGenerator.getInstance().getPage("add.html", pageVariables));
        }   catch (Exception e) {
            e.printStackTrace();
            setError(resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!isValidRequest(req)){
            doGet(req, resp);
        } else {
            try{
                Product product = new Product();
                product.setName(req.getParameter("name"));
                product.setPrice(Double.parseDouble(req.getParameter("price")));
                product.setDescription(req.getParameter("description")); // тут проверку на null
                productService.add(product);
            } catch (Exception e) {
                e.printStackTrace();
                setError(resp);
            }
            doGet(req, resp);
        }
    }

    private boolean isValidRequest(HttpServletRequest req){
        String name = req.getParameter("name");
        String price = req.getParameter("price");
        return name != null && name.length() > 0 &&
                price != null && price.length() > 0 &&
                price.matches("[+]?\\d*\\.?\\d+");
    }

    private void setError(HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println("500 Internal server error.");
    }


}
