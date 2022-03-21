package com.k0s.web.filter;

import com.k0s.service.SecurityService;
import com.k0s.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public class AuthFilter implements Filter {


    private final SecurityService securityService;

    public AuthFilter(SecurityService securityService){
        this.securityService = securityService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AuthFilter init!");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String requestUri = req.getRequestURI();

        if (skipRequest(requestUri)){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("user-token".equals(cookie.getName())) {
                    if(securityService.isValidSession(cookie.getValue())){
                        filterChain.doFilter(servletRequest, servletResponse);
                        return;
                    }
                }
            }
        }
        resp.sendRedirect("/login");
    }

    private boolean skipRequest(String requestUri){
        if("/".equals(requestUri)){
            return true;
        }
        if("/login".equals(requestUri)){
            return true;
        }
        if("/search".equals(requestUri)){
            return true;
        }
        if(requestUri.startsWith("/resources")){
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {
    }
}
