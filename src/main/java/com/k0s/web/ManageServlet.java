package com.k0s.web;

import com.k0s.service.ProductService;
import com.k0s.util.PageGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ManageServlet extends HttpServlet {

    private ProductService productService;

    public ManageServlet(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put("products", productService.getAll());

            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(PageGenerator.getInstance().getPage("manage.html", pageVariables));
        } catch (Exception e) {
            e.printStackTrace();
            setError(resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            productService.remove(Integer.parseInt(req.getParameter("id")));
        } catch (Exception e) {
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
