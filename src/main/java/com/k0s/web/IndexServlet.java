package com.k0s.web;

import com.k0s.entity.user.Role;
import com.k0s.security.Session;
import com.k0s.service.ProductService;
import com.k0s.util.PageGenerator;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IndexServlet extends HttpServlet {

    private final ProductService productService;

    public IndexServlet(ProductService productService){
        this.productService = productService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try{
            Map<String, Object> pageVariables = new HashMap<>();
            System.out.println("isLogin = " + req.getAttribute("isLogin"));
            System.out.println("Role = " + req.getAttribute("role"));
            System.out.println("Session = " + req.getAttribute("session"));

            Session session = (Session) req.getAttribute("session");

            if(session != null){
                pageVariables.put("isLogin", true);
                pageVariables.put("role", session.getUser().getRole());
            } else{
                pageVariables.put("isLogin", false);
                pageVariables.put("role", Role.GUEST);
            }


            pageVariables.put("products", productService.getAll());


            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(PageGenerator.getInstance().getPage("index.html", pageVariables));

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}