package com.k0s.web.filter;

import com.k0s.entity.user.Role;
import com.k0s.security.Session;
import com.k0s.service.SecurityService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;


public class AuthFilter implements Filter {
    private static final String USER_TOKEN = "user-token";
    private static final String ROLE_ATT = "role";
    private static final String SESSION_ATT = "session";
    private static final List<String> SKIP_AUTHORIZATION_PATH = new ArrayList<>();
    private static final Properties PROPERTIES = new Properties();


    private final SecurityService securityService;

    public AuthFilter(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        PROPERTIES.putAll(securityService.getProperties());
        SKIP_AUTHORIZATION_PATH.addAll(Arrays.asList(PROPERTIES.getProperty("security.skipPath").split(",")));
        System.out.println("AuthFilter init!");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;


        String requestPath = req.getServletPath();
        String requestUri = req.getRequestURI();
        System.out.println("REQUEST PATH = " + requestPath);
        System.out.println("REQUEST URI = " + requestUri);
//        if (skipAutorization(requestPath)) {
        if (SKIP_AUTHORIZATION_PATH.contains(requestUri)) {
            System.out.println("AUTH SKIP PATH = " + requestPath);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        System.out.println("=====AFTER SKIP======");
        Session session = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            System.out.println("CHECK COOKIE");
            for (Cookie cookie : cookies) {
                if (USER_TOKEN.equals(cookie.getName())) {
                    session = securityService.getSession(cookie.getValue());
                    break;
                }
            }
        }

        if (session == null) {
            req.setAttribute(ROLE_ATT, Role.GUEST);
        } else {
            req.setAttribute(SESSION_ATT, session);
        }

//        if (requestPath.startsWith("/admin")){
        if (requestPath.startsWith(PROPERTIES.getProperty("security.adminPath"))){
            System.out.print(" IN ADMIN : ");
            if (isAllow(session, Role.ADMIN)){
                System.out.println(" session role = " + session.getRole());
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                System.out.println(" access deny ");
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            return;
        }

//        if (requestPath.startsWith("/user")){
        if (requestPath.startsWith(PROPERTIES.getProperty("security.userPath"))){
            System.out.print(" IN USER : ");
            if (isAllow(session, Role.USER)){
                System.out.println(" session role = " + session.getRole());
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                System.out.println(" access deny ");
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            return;
        }

        System.out.println("####doFilter");
        filterChain.doFilter(servletRequest, servletResponse);

    }

//    private boolean skipAutorization(String requestUri) {
//        if ("/login".equals(requestUri)) {
//            return true;
//        }
//        if ("/resources".equals(requestUri)) {
//            return true;
//        }
//        if ("/favicon.ico".equals(requestUri)) {
//            return true;
//        }
//        return false;
//    }
//    private boolean skipAutorization(String requestUri) {
//        return SKIP_AUTHORIZATION_PATH.contains(requestUri);
//        for (String s : SKIP_AUTHORIZATION_PATH) {
//            if( requestUri.equals(s)){
//                return true;
//            }
//        }
//        return false;
//    }

    private boolean isAllow(Session session, Role role) {
        if (session == null) {
            return false;
        }
        return role.equals(session.getRole());
    }


    @Override
    public void destroy() {
    }
}
